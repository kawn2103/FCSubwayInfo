 package kst.app.fcsubwayinfo.presentation.stations

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kst.app.fcsubwayinfo.data.repository.StationRepository
import kst.app.fcsubwayinfo.domain.Station

class StationsPresenter(
    private val view: StationsContract.View,
    private val stationRepository: StationRepository
) : StationsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    private val queryString: MutableStateFlow<String> = MutableStateFlow("")
    private val stations: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList())

    init {
        observeStations()
    }

    override fun onViewCreated() {
        scope.launch {
            view.showStations(stations.value)
            stationRepository.refreshStations()
        }
    }

    override fun onDestroyView() {}

    override fun filterStations(query: String) {
        scope.launch {
            queryString.emit(query)
        }
    }

    private fun observeStations() {
        stationRepository
            .stations
            .combine(queryString) { stations, query ->
                if (query.isBlank()) {
                    stations
                } else {
                    stations.filter { it.name.contains(query) }
                }
            }
            .onStart { view.showLoadingIndicator() }
            .onEach {
                if (it.isNotEmpty()) {
                    view.hideLoadingIndicator()
                }
                stations.value = it
                view.showStations(it)
            }
            .catch {
                it.printStackTrace()
                view.hideLoadingIndicator()
            }
            .launchIn(scope)
    }

    override fun toggleStationFavorite(station: Station) {
        scope.launch {
            stationRepository.updateStation(station.copy(isFavorited = !station.isFavorited))
        }
    }
}