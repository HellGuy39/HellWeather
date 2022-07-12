package com.hellguy39.hellweather.domain.usecase

import com.hellguy39.hellweather.domain.repository.LocalRepository
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class GetWeatherForecastUseCaseTest {

    private val remoteRepository = mock<RemoteRepository>()
    private val localRepository = mock<LocalRepository>()

    @Test
    fun `awesome test`() {
//        val useCase = GetWeatherForecastUseCase(remoteRepository, localRepository)
//
//        useCase.invoke(45.0, 38.0, true)
    }
}