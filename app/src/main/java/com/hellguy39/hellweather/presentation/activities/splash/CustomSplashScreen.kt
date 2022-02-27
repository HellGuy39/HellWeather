package com.hellguy39.hellweather.presentation.activities.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.domain.usecase.prefs.first_boot.FirstBootValueUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.service.ServiceUseCases
import com.hellguy39.hellweather.domain.utils.Prefs
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CustomSplashScreen: AppCompatActivity() {

    @Inject
    lateinit var serviceUseCases: ServiceUseCases
    @Inject
    lateinit var firstBootValueUseCases: FirstBootValueUseCases

    private var isFirstBoot = false
    private var serviceMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val intent = Intent(this, MainActivity::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            isFirstBoot = firstBootValueUseCases.getFirstBootValueUseCase.invoke()
            serviceMode = serviceUseCases.getServiceModeUseCase.invoke()

            if (isFirstBoot)
            {
                intent.putExtra(Prefs.FirstBoot.name, true)
            }
            else
            {
                intent.putExtra(Prefs.FirstBoot.name, false)
            }
            intent.putExtra(Prefs.ServiceMode.name, serviceMode)

            startActivity(intent)
            finish()
        }
    }
}