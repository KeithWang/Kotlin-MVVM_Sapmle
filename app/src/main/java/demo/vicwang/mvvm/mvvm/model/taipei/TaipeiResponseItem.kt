package demo.vicwang.mvvm.mvvm.model.taipei

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TaipeiResponseItem(
        @SerializedName("result") val resultObj: JsonObject
) : Serializable {

    val resultJsonArray: JsonArray
        get() = resultObj.getAsJsonArray("results")
}