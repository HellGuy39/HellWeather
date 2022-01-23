package com.hellguy39.hellweather.presentation.fragments.add

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.server.ApiService
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val mService: ApiService
) : ViewModel() {

    val userLocationLive = MutableLiveData<UserLocation>()
    val isLoadingLive = MutableLiveData<Boolean>()
    val requestResLive = MutableLiveData<String?>()

    fun clearData() {
        requestResLive.value = null
    }

    suspend fun checkCityInAPI(input: String) = coroutineScope {
        mService.getCurrentWeather(input, METRIC, OPEN_WEATHER_API_KEY)
            .enqueue(object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful)
                    {
                        val jObj: JsonObject? = response.body()
                        if (jObj != null)
                        {
                            val usrLoc = UserLocation()

                            val coordinates = jObj.getAsJsonObject("coord")
                            val sys = jObj.getAsJsonObject("sys")
                            usrLoc.lat = coordinates.get("lat").asString
                            usrLoc.lon = coordinates.get("lon").asString

                            usrLoc.requestName = input
                            usrLoc.locationName = jObj.get("name").asString
                            usrLoc.country = sys.get("country").asString
                            usrLoc.cod = jObj.get("cod").asString
                            //usrLoc.id = jObj.get("id").asInt
                            usrLoc.timezone = jObj.get("timezone").asInt / 3600

                            Log.d("LOG", "Lat:${usrLoc.lat} & Lon:${usrLoc.lon}")

                            if (usrLoc.lat != "N/A" || !TextUtils.isEmpty(usrLoc.lat) &&
                                usrLoc.lon != "N/A" || !TextUtils.isEmpty(usrLoc.lon)) {
                                    userLocationLive.value = usrLoc
                                    requestResLive.value = SUCCESSFUL
                            }
                            else
                            {
                                requestResLive.value = EMPTY_BODY
                            }
                        }
                        else
                        {
                            requestResLive.value = EMPTY_BODY
                        }
                    }
                    else
                    {
                        requestResLive.value = ERROR
                        //response.code().toString()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    requestResLive.value = FAILURE
                    //t.toString())
                }

            })
    }

}