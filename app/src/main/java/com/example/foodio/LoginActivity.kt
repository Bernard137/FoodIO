package com.example.foodio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.foodio.views.LoginForm
import com.example.foodio.services.Firebase.fetchUsers

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            installSplashScreen().setKeepOnScreenCondition {
                fetchUsers()
                false
            }
            LoginForm()
        }
    }
}