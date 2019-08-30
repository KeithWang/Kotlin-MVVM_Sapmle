package demo.vicwang.mvvm.mvvm.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import demo.vicwang.mvvm.adapter.item.HouseListAnimalItem
import demo.vicwang.mvvm.adapter.item.MainHouseListItem
import demo.vicwang.mvvm.mvvm.model.ApiRepository
import demo.vicwang.mvvm.mvvm.rxprovider.SchedulerProvider
import demo.vicwang.mvvm.mvvm.viewmodel.item.AnimalListDataResultItem
import demo.vicwang.mvvm.mvvm.viewmodel.item.DataLoadStatus
import demo.vicwang.mvvm.mvvm.viewmodel.item.HouseDataResultItem
import java.util.*


class DataLoadViewModel(private val mApiRepo: ApiRepository
                        , private val mRxProvider: SchedulerProvider) : BasicViewModel() {

    private var mHouseData = MutableLiveData<HouseDataResultItem>()

    private var mAnimListData = MutableLiveData<AnimalListDataResultItem>()

    private var mLoadingShow = MutableLiveData<Boolean>()

    fun onLoadHouseData() {
        disposables.add(mApiRepo.getHouseData()
                .subscribeOn(mRxProvider.io())
                .observeOn(mRxProvider.ui())
                .doOnSubscribe { mLoadingShow.value = true }
                .doFinally {
                    mLoadingShow.value = false
                    disposables.clear()
                }
                .subscribe({ result ->
                    val array = result.resultJsonArray
                    val listType = object : TypeToken<ArrayList<MainHouseListItem>>() {}.type
                    val listData: ArrayList<MainHouseListItem> = Gson().fromJson(array.toString(), listType)

                    mHouseData.value = HouseDataResultItem(DataLoadStatus.SUCCESS, listData, null)
                }, { error ->
                    mHouseData.value = HouseDataResultItem(DataLoadStatus.FAIL, null, 1)
                    error.printStackTrace()
                }))
    }

    fun onLoadAnimalListData(item: MainHouseListItem) {
        disposables.add(mApiRepo.getAnimalData(item.E_Name)
                .subscribeOn(mRxProvider.io())
                .observeOn(mRxProvider.ui())
                .doOnSubscribe { mLoadingShow.value = true }
                .doFinally {
                    mLoadingShow.value = false
                    disposables.clear()
                }
                .subscribe({ result ->
                    val array = result.resultJsonArray
                    val listType = object : TypeToken<ArrayList<HouseListAnimalItem>>() {}.type
                    val listData: ArrayList<HouseListAnimalItem> = Gson().fromJson(array.toString(), listType)

                    mAnimListData.value = AnimalListDataResultItem(DataLoadStatus.SUCCESS, listData, item, null)
                }, { error ->
                    error.printStackTrace()
                    mAnimListData.value = AnimalListDataResultItem(DataLoadStatus.FAIL, null, item, 1)
                }))
    }

    fun getAnimalListData(): LiveData<AnimalListDataResultItem> {
        return mAnimListData
    }

    fun getHouseData(): LiveData<HouseDataResultItem> {
        return mHouseData
    }

    fun getLoadingStatus(): LiveData<Boolean> {
        return mLoadingShow
    }


}