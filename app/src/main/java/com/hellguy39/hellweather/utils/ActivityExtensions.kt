package com.hellguy39.hellweather.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> AppCompatActivity.observe(data: LiveData<T>, eventCallback: (T) -> Unit)
{
    data.observe(this, Observer {
        it?.let {
            eventCallback(it)
        }
    })
}

fun <T> Fragment.observe(data: LiveData<T>, eventCallback: (T) -> Unit)
{
    data.observe(viewLifecycleOwner, Observer {
        it?.let {
            eventCallback(it)
        }
    })
}