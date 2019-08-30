package demo.vicwang.mvvm.adapter.item

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HouseListAnimalItem(
        @SerializedName("\uFEFFA_Name_Ch") val A_Name_Ch: String
        , @SerializedName("A_Name_En") val A_Name_En: String
        , @SerializedName("A_AlsoKnown") val A_AlsoKnown: String
        , @SerializedName("A_Interpretation") val A_Interpretation: String
        , @SerializedName("A_Behavior") val A_Behavior: String
        , @SerializedName("A_Diet") val A_Diet: String
        , @SerializedName("A_Feature") val A_Feature: String
        , @SerializedName("A_Habitat") val A_Habitat: String
        , @SerializedName("A_Update") val A_Update: String
        , @SerializedName("A_Pic01_URL") val A_Pic01_URL: String
        , @SerializedName("A_Pic02_URL") val A_Pic02_URL: String
) : Serializable