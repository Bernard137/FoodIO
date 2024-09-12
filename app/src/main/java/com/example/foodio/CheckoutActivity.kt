package com.example.foodio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.ui.theme.FoodIOTheme
import com.example.foodio.views.MyCheckoutActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TotalPriceViewModel @Inject constructor() : ViewModel() {
    var totalPrice = mutableFloatStateOf(0f)
}

class CheckoutActivity : ComponentActivity() {
    private val viewModel: TotalPriceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodIOTheme(dynamicColor = false) {
                MyCheckoutActivity(ShoppingCartRepository.getFoodInCart(), viewModel)
            }
        }
    }
}

