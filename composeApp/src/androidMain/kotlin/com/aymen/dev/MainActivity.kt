package com.aymen.dev

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.shared.SharedBackPressHandler

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

@Preview
@Composable
fun AppAndroidPreview() {
    App()

}