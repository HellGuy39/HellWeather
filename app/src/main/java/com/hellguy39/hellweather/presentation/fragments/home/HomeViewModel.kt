package com.hellguy39.hellweather.presentation.fragments.home

import android.util.Log
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    init {
        Log.d("##### DEBUG #####", "MODEL INIT")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("##### DEBUG #####", "ON CLEARED")
    }

}