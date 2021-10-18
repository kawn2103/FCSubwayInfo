package kst.app.fcsubwayinfo.data.api.response

import com.google.gson.annotations.SerializedName
import kst.app.fcsubwayinfo.data.api.response.ErrorMessage
import kst.app.fcsubwayinfo.data.api.response.RealtimeArrival

data class RealtimeStationArrivals(
    @SerializedName("errorMessage")
    val errorMessage: ErrorMessage? = null,
    @SerializedName("realtimeArrivalList")
    val realtimeArrivalList: List<RealtimeArrival>? = null
)