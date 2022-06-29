package com.hellguy39.hellweather.presentation.activities.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomSplashScreen: AppCompatActivity() {

//    @Inject
//    lateinit var serviceUseCases: ServiceUseCases
//    @Inject
//    lateinit var firstBootValueUseCases: FirstBootValueUseCases
//
//    private var isFirstBoot = false
//    private var serviceMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        startActivity(Intent(this, MainActivity::class.java))
        finish()

//        val intent = Intent(this, MainActivity::class.java)
//
//        CoroutineScope(Dispatchers.IO).launch {
//
//            isFirstBoot = firstBootValueUseCases.getFirstBootValueUseCase.invoke()
//            serviceMode = serviceUseCases.getServiceModeUseCase.invoke()
//
//            if (isFirstBoot)
//            {
//                intent.putExtra(Prefs.FirstBoot.name, true)
//            }
//            else
//            {
//                intent.putExtra(Prefs.FirstBoot.name, false)
//            }
//            intent.putExtra(Prefs.ServiceMode.name, serviceMode)
//
//            startActivity(intent)
//            finish()
//        }
    }
}