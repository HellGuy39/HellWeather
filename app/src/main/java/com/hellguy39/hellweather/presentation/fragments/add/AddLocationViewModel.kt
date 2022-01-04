package com.hellguy39.hellweather.presentation.fragments.add

import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.google.gson.JsonObject
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.server.Common
import com.hellguy39.hellweather.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddLocationViewModel() : ViewModel() {

    private val mService = Common.retrofitServices
    val userLocationLive = MutableLiveData<UserLocation>()
    val isLoadingLive = MutableLiveData<Boolean>()
    val requestResLive = MutableLiveData<String>()

    fun checkCityInAPI(input: String) {

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
                            usrLoc.lat = coordinates.get("lat").asString
                            usrLoc.lon = coordinates.get("lon").asString

                            usrLoc.cityName = input
                            usrLoc.region = jObj.get("name").asString
                            usrLoc.cod = jObj.get("cod").asString
                            usrLoc.id = jObj.get("id").asInt
                            usrLoc.timezone = jObj.get("timezone").asString

                            Log.d("LOG", "Lat:${usrLoc.lat} Lon:${usrLoc.lon}")

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
        isLoadingLive.value = false
    }


}