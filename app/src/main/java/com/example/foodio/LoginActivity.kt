
package com.example.foodio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.foodio.services.Firebase.fetchUsers
import com.example.foodio.ui.theme.FoodIOTheme
import com.example.foodio.views.LoginForm

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            installSplashScreen().setKeepOnScreenCondition {
                fetchUsers()
                false
            }
            FoodIOTheme(dynamicColor = false) {
                LoginForm()
            }
        }
    }
}
