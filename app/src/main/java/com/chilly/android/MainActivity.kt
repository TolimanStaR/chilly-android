package com.chilly.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.chilly.android.presentation.navigation.ChillyNavHost
import com.chilly.android.presentation.theme.ChillyTheme
import org.osmdroid.config.Configuration as MapConfiguration

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadMapConfiguration()
        applicationComponent.resourceHolder.set(resources)
        enableEdgeToEdge()
        setContent {
            ChillyTheme {
                ChillyNavHost()
            }
        }
    }

    override fun onDestroy() {
        applicationComponent.resourceHolder.release()
        super.onDestroy()
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