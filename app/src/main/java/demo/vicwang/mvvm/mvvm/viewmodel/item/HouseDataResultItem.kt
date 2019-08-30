package demo.vicwang.mvvm.mvvm.viewmodel.item

import demo.vicwang.mvvm.adapter.item.MainHouseListItem
import java.util.*

data class HouseDataResultItem(private val status: DataLoadStatus
                               , private val mainHouseSuccessData: ArrayList<MainHouseListItem>?
                               , private val errorType: Int?) {
    fun getHouseData(): ArrayList<MainHouseListItem>? {
        return mainHouseSuccessData
    }

    fun getErrorType(): Int? {
        return errorType
    }

    fun getStatus(): DataLoadStatus {
        return status
    }
}