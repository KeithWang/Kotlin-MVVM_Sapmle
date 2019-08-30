package demo.vicwang.mvvm.adapter.item

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MainHouseListItem(
        @SerializedName("_id") val _id: Int
        , @SerializedName("E_Pic_URL") val E_Pic_URL: String
        , @SerializedName("E_Info") val E_Info: String
        , @SerializedName("E_no") val E_no: String
        , @SerializedName("E_Category") val E_Category: String
        , @SerializedName("E_Memo") val E_Memo: String
        , @SerializedName("E_Name") val E_Name: String
        , @SerializedName("E_URL") val E_URL: String
) : Serializable