package demo.vicwang.mvvm.mvvm.model.login

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponseItem(
        @SerializedName("token") val token: String
) : Serializable