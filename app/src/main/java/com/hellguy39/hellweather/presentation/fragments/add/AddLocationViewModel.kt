package com.hellguy39.hellweather.presentation.fragments.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.data.api.ApiService
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val mService: ApiService
) : ViewModel() {

    private val lang = Locale.getDefault().country
    val userLocationLive = MutableLiveData<UserLocation?>()
    val statusLive = MutableLiveData<String>()

    fun clearData() {
        userLocationLive.value = null
    }

    fun requestController(type: String, cityName: String = "", lat: Double = 0.0, lon: Double = 0.0) {
        viewModelScope.launch {
            if (statusLive.value != IN_PROGRESS)
            {
                statusLive.value = IN_PROGRESS

                when (type) {
                    TYPE_CITY_NAME -> {
                        /*val request = sendRequestWithCity(cityName)
                        val converter = Converter()

                        if (converter.checkRequest(request) == FAILURE) {
                            statusLive.value = FAILURE
                        } else if (converter.checkRequest(request) == INCORRECT_OBJ) {
                            statusLive.value = ERROR
                        }

                        userLocationLive.value = converter.toUserLocation(request)
                        statusLive.value = SUCCESSFUL*/

                    }
                    TYPE_LAT_LON -> {
                        /*val request = sendRequestWithLatLon(lat, lon)
                        val converter = Converter()

                        if (converter.checkRequest(request) == FAILURE) {
                            statusLive.value = FAILURE
                        } else if (converter.checkRequest(request) == INCORRECT_OBJ) {
                            statusLive.value = ERROR
                        }

                        userLocationLive.value = converter.toUserLocation(request)
                        statusLive.value = SUCCESSFUL
*/
                    }
                    else -> {
                        statusLive.value = FAILURE
                    }
                }
            }
        }
    }

}