package com.chilly.android

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.chilly.android.di.activity.ActivityComponent
import com.chilly.android.di.activity.DaggerActivityComponent
import com.chilly.android.presentation.navigation.ChillyNavHost
import com.chilly.android.presentation.theme.ChillyTheme
import org.osmdroid.config.Configuration as MapConfiguration

class MainActivity : ComponentActivity() {

    lateinit var activityComponent: ActivityComponent

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadMapConfiguration()
        enableEdgeToEdge()

        createComponent()

        setContent {
            ChillyTheme {
                ChillyNavHost()
            }
        }
    }

    private fun createComponent() {
        activityComponent = DaggerActivityComponent.builder()
            .activity(this)
            .parent(applicationComponent)
            .build()
    }

    private fun loadMapConfiguration() {
        MapConfiguration.getInstance().load(
            applicationContext,
            getSharedPreferences(OSM_PREFERENCES_NAME, MODE_PRIVATE)
        )
    }

    companion object {
        const val OSM_PREFERENCES_NAME = "osm_preferences"
    }
}

val Context.activityComponent: ActivityComponent
    get() = (this as MainActivity).activityComponent