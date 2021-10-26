package kst.app.fcsubwayinfo.data.api

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kst.app.fcsubwayinfo.data.db.entity.StationEntity
import kst.app.fcsubwayinfo.data.db.entity.SubwayEntity

class StationStorageApi(
    firebaseStorage: FirebaseStorage
) : StationApi {

    private val sheetReference = firebaseStorage.reference.child(STATION_DATA_FILE_NAME)

    override suspend fun getStationDataUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>> {
        val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
        val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

        return byteArray.decodeToString()
            .lines()
            .drop(1)
            .map {
                it.split(",")   //it은 받아온 데이터터
            }
            .map { StationEntity(it[1]) to SubwayEntity(it[0].toInt()) }
    }

    companion object {
        private const val STATION_DATA_FILE_NAME = "station_data.csv"
    }
}