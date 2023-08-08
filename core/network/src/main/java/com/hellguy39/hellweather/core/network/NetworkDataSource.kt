package com.hellguy39.hellweather.core.network

import com.hellguy39.hellweather.core.model.RequestQuery
import com.hellguy39.hellweather.core.network.util.RequestResult
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

class NetworkDataSource() {

    private val client = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        defaultRequest {
            url(BuildConfig.BASE_URL)
        }
    }

    private val key = BuildConfig.API_KEY

    object Params {
        /* Required */
        const val Key = "key"
        /* Required */
        const val Query = "q"
        /* Required only with forecast API method */
        const val Days = "days"
        /* Required for History and Future API */
        const val Date = "dt"
        /* Returns 'condition:text' field in API in the desired language */
        const val Language = "lang"
    }

    suspend fun getRealtime(q: RequestQuery): RequestResult<RealtimeWeatherDto> {
        return catchExceptions(
            block = {
                val response = client.get(HttpRoute.Current) {
                    url {
                        parameters.append(Params.Key, key)
                        parameters.append(Params.Query, q.asValue())
                    }
                }
                RequestResult.Success(response.body())
            },
            onError = { code, message -> RequestResult.Error("$code: $message") }
        )
    }

    fun getForecast() {

    }

    fun getHistory() {

    }

    fun search() {

    }

    fun getAstronomy() {

    }

    fun getTimezone() {

    }

    fun getSports() {

    }

    private suspend fun <T> catchExceptions(
        block: suspend () -> RequestResult<T>,
        onError: suspend (Int, String) -> RequestResult<T>
    ): RequestResult<T> {
        return try {
            block()
        } catch (e: RedirectResponseException) {
            val code = e.response.status.value
            val message = e.response.status.description
            onError(code, message)
        } catch (e: ClientRequestException) {
            val code = e.response.status.value
            val message = e.response.status.description
            onError(code, message)
        } catch (e: ServerResponseException) {
            val code = e.response.status.value
            val message = e.response.status.description
            onError(code, message)
        } catch (e: NoTransformationFoundException) {
            val message = e.localizedMessage
            onError(0, message)
        } catch (e: IOException) {
            val message = e.localizedMessage
            onError(0, message)
        } catch (e: SerializationException) {
            val message = e.localizedMessage
            onError(0, message)
        }
    }

}