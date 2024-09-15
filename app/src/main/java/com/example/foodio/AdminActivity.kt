
package com.example.foodio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.foodio.ui.theme.FoodIOTheme
import com.example.foodio.views.AdminForm

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodIOTheme(dynamicColor = false) {
                AdminForm()
            }
        }
    }
}
