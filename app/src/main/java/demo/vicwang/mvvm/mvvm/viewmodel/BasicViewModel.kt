package demo.vicwang.mvvm.mvvm.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BasicViewModel : ViewModel() {

    /*
    * To Manage Rxjava Life Cycle
    * */
    val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

}