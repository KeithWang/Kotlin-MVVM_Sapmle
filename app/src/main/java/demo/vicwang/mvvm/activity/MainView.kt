package demo.vicwang.mvvm.activity

import demo.vicwang.mvvm.adapter.item.HouseListAnimalItem

interface MainView {
    fun onCallToast(str: String, Long: Boolean)

    fun onBackFragment()

    fun onOpenAnimalInfoPage(item: HouseListAnimalItem)
}