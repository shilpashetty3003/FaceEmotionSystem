package com.example.fermusicsystem.screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fermusicsystem.databinding.ActivitySplashScreenBinding
import com.example.fermusicsystem.screen.camera.CameraScreen
import com.example.fermusicsystem.screen.home.HomeScreen

class SplashScreen : AppCompatActivity() {
    lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)

        binding.bntStart.setOnClickListener {
            startActivity(Intent(this, HomeScreen::class.java))
        }
    }
}