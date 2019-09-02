package demo.vicwang.mvvm.mvvm.model.taipei

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiTaipeiInterface {

    @GET("apiAccess?scope=resourceAquire&rid=5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a")
    fun getHouseData(@Header("token") token: String): Observable<TaipeiResponseItem>

    @GET("apiAccess?scope=resourceAquire&rid=a3e2b221-75e0-45c1-8f97-75acbd43d613&q={}")
    fun getAnimalData(@Query("q") targetArea: String): Observable<TaipeiResponseItem>
}