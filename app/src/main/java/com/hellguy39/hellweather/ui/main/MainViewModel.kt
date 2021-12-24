package com.hellguy39.hellweather.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hellguy39.hellweather.utils.SingleLiveEvent

class MainViewModel : ViewModel() {

    private val loadingMut = MutableLiveData<Boolean>()
    private val errorMut: MutableLiveData<Unit> = SingleLiveEvent()

    val loading: LiveData<Boolean> get() = loadingMut
    val error: LiveData<Unit> get() = errorMut

    fun onRefresh()
    {
        loadingMut.value = false
        errorMut.value = Unit
    }

}