package demo.vicwang.mvvm.mvvm.model

import demo.vicwang.mvvm.mvvm.model.login.ApiLoginInterface
import demo.vicwang.mvvm.mvvm.model.login.LoginResponseItem
import demo.vicwang.mvvm.mvvm.model.taipei.ApiTaipeiInterface
import demo.vicwang.mvvm.mvvm.model.taipei.TaipeiResponseItem
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiRepository {
    /*
    * Taipei Zoo
    * */
    private val urlTaipei = "https://data.taipei/opendata/datalist/"
    private var apiTaipei: ApiTaipeiInterface

    /*
    * Login Test
    * */
    private val urlLogin = "https://reqres.in/"
    private var apiLogin: ApiLoginInterface

    init {
        val retrofitTaipei = Retrofit.Builder()
                .baseUrl(urlTaipei)
                .client(getBuildOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        this.apiTaipei = retrofitTaipei.create<ApiTaipeiInterface>(ApiTaipeiInterface::class.java)

        val retrofitLogin = Retrofit.Builder()
                .baseUrl(urlLogin)
                .client(getBuildOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        this.apiLogin = retrofitLogin.create<ApiLoginInterface>(ApiLoginInterface::class.java)
    }

    private fun getBuildOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build()
    }


    fun getLoginToken(email: String, password: String): Observable<LoginResponseItem> {
        return apiLogin.getToken(email, password)
    }

    fun getHouseData(token: String): Observable<TaipeiResponseItem> {
        return apiTaipei.getHouseData(token)
    }

    fun getAnimalData(targetArea: String): Observable<TaipeiResponseItem> {
        return apiTaipei.getAnimalData(targetArea)
    }


}