package com.chilly.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chilly.android.presentation.navigation.ChillyNavHost
import com.chilly.android.presentation.theme.ChillyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}