package com.aymen.dev

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import ui.core.CustomConstraintLayoutExample
import ui.shared.SharedBackPressHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val pxValue =
                LocalDensity.current.run { 16.toDp() }

            println("ddd density=${pxValue.value}")
            App()
            BackHandler(enabled =!SharedBackPressHandler.enabled) {
                SharedBackPressHandler.onBackPressed()
            }
        }
    }
}
@Preview
@Composable
fun PreviewCustomConstraintLayoutWithConstraintsExample() {
    CustomConstraintLayoutExample()
}

