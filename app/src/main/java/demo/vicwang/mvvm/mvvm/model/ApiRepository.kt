package demo.vicwang.mvvm.mvvm.model

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiRepository{
    private val url = "https://data.taipei/opendata/datalist/"

    private var api: ApiInterface

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        this.api = retrofit.create<ApiInterface>(ApiInterface::class.java)
    }


    fun getHouseData(): Observable<ResponseItem> {
        return api.getHouseData()
    }

    fun getAnimalData(targetArea: String): Observable<ResponseItem> {
        return api.getAnimalData(targetArea)
    }

}