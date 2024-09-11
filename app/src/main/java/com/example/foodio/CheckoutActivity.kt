package com.example.foodio

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.foodio.data.CartFood
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.models.Food
import com.example.foodio.services.Firebase
import com.example.foodio.ui.theme.FoodIOTheme
import com.example.foodio.views.CheckoutCartCard
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
            FoodIOTheme {
                MyCheckoutActivity(ShoppingCartRepository.getFoodInCart(), viewModel)
            }
        }
    }
}

@Composable
fun MyCheckoutActivity(foodInCart: MutableList<CartFood>, viewModel: TotalPriceViewModel) {
    val context = LocalContext.current
    viewModel.totalPrice.floatValue = ShoppingCartRepository.calculateTotalPrice()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .align(Alignment.Start)
        ) {
            Image(
                painter = painterResource(com.chillibits.composenumberpicker.R.drawable.ic_arrow_left),
                contentDescription = "AdminScreenReturn",
                modifier = Modifier
                    .size(70.dp)
                    .padding(10.dp)
                    .background(Color.Blue)
                    .clickable {
                        val contFinish = context as Activity
                        contFinish.finish()
                    }
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 15.dp),
                text = "Shopping cart",
                fontFamily = FontFamily.Cursive,
                fontSize = TextUnit(10f, TextUnitType.Em),
                textAlign = TextAlign.Center,
                lineHeight = TextUnit(0.8f, TextUnitType.Em)
            )
        }

        LazyColumn {
            Modifier
            items(
                items = foodInCart
            ) { food ->
                CheckoutCartCard(cartFood = food, viewModel)
            }
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 15.dp),
            text = "Total price: ${viewModel.totalPrice.floatValue}e (-${ShoppingCartRepository.getDiscount()}%)",
            fontFamily = FontFamily.Cursive,
            fontSize = TextUnit(8f, TextUnitType.Em),
            textAlign = TextAlign.Center,
            lineHeight = TextUnit(0.8f, TextUnitType.Em)
        )

        Button(
            onClick = {
                Firebase.shopWithUsername(ShoppingCartRepository.getUsername(), context)
                ShoppingCartRepository.clearFoodCart()
                ShoppingCartRepository.setDiscount(0.0f)
                val activity = context as Activity
                activity.finish()
            },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.90f),
        ) {
            Text("Checkout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutCartCardPreview() {
    val list: MutableList<CartFood> = mutableListOf()
    list.add(CartFood(10, Food(name = "Hamburger", price = 20f)))
    list.add(CartFood(10, Food(name = "Pizza", price = 20f)))
    FoodIOTheme {
        //MyCheckoutActivity(list, viewModel)
    }
}