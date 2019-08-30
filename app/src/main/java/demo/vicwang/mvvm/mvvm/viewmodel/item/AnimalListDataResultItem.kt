package demo.vicwang.mvvm.mvvm.viewmodel.item

import demo.vicwang.mvvm.adapter.item.HouseListAnimalItem
import demo.vicwang.mvvm.adapter.item.MainHouseListItem
import java.util.*

data class AnimalListDataResultItem(private val status: DataLoadStatus
                                    , private val houseAnimalSuccessData: ArrayList<HouseListAnimalItem>?
                                    , private val houseItem: MainHouseListItem?
                                    , private val errorType: Int?) {

    fun getHouseItem(): MainHouseListItem? {
        return houseItem
    }

    fun getHouseListAnimalData(): ArrayList<HouseListAnimalItem>? {
        return houseAnimalSuccessData
    }

    fun getErrorType(): Int? {
        return errorType
    }

    fun getStatus(): DataLoadStatus {
        return status
    }
}