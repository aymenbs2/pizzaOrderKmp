package com.aymen.dev

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import main.ui.shared.SharedBackPressHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
            BackHandler(enabled =!SharedBackPressHandler.enabled) {
                SharedBackPressHandler.onBackPressed()
            }
        }
    }
}

