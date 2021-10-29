package kst.app.fcsubwayinfo.presentation.stations

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kst.app.fcsubwayinfo.databinding.FragmentStationsBinding
import kst.app.fcsubwayinfo.domain.Station
import kst.app.fcsubwayinfo.extension.toGone
import kst.app.fcsubwayinfo.extension.toVisible
import org.koin.android.scope.ScopeFragment

// ScopeFragment() -> extend  StationsContract.View -> implement
class StationsFragment : ScopeFragment(), StationsContract.View {

    //app 모듈에서 선언 해둔 presenter by inject()로 주입 받아옴
    override val presenter: StationsContract.Presenter by inject()

    private var binding: FragmentStationsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentStationsBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뷰 선언
        initViews()
        bindViews()
        // presenter 생명주기 맞춤
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // presenter 생명주기 맞춤
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard()
        // presenter 생명주기 맞춤
        presenter.onDestroy()
    }

    // implementation 받은 interface의 function 내용 선언
    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    // implementation 받은 interface의 function 내용 선언
    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
    }

    // implementation 받은 interface의 function 내용 선언
    override fun showStations(stations: List<Station>) {
        (binding?.recyclerView?.adapter as? StationsAdapter)?.run {
            this.data = stations
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        // 리사이클러 뷰 선언
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = StationsAdapter()
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun bindViews() {
        binding?.searchEditText?.addTextChangedListener { editable ->
            presenter.filterStations(editable.toString())
        }


        (binding?.recyclerView?.adapter as? StationsAdapter)?.apply {
            // 클릭 리스너 연결하기
            onItemClickListener = { station ->
                val action = StationsFragmentDirections.toStationArrivalsAction(station)
                findNavController().navigate(action)
            }
            // 클릭 리스너 연결하기
            onFavoriteClickListener = { station ->
                presenter.toggleStationFavorite(station)
            }
        }
    }

    //키보드 숨기기
    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}