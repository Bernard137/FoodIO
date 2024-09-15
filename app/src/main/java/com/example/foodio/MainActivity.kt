package com.example.foodio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.ui.theme.FoodIOTheme
import com.example.foodio.viewmodel.CategoryViewModel
import com.example.foodio.views.CategoriesActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username")
        username?.let { ShoppingCartRepository.setUsername(it) }
        if (intent.getBooleanExtra("first_shopping", false))
            ShoppingCartRepository.setDiscount(20f)

        setContent {
            val dataOrException = viewModel.data.value
            if (username != null) {
                FoodIOTheme(dynamicColor = false) {
                    CategoriesActivity(dataOrException, username, viewModel)
                }
            }
        }
    }
}


