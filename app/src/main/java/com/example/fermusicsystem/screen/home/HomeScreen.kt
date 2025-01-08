package com.example.fermusicsystem.screen.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.fermusicsystem.ui.theme.FERMusicSystemTheme


class HomeScreen :ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FERMusicSystemTheme {
                Surface {

                }
            }
        }
    }
}