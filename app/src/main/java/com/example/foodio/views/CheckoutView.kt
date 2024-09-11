package com.example.foodio.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chillibits.composenumberpicker.VerticalNumberPicker
import com.example.foodio.TotalPriceViewModel
import com.example.foodio.data.CartFood
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.models.Food
import com.example.foodio.ui.theme.FoodIOTheme

@Composable
fun CartFoodCard(
    cartFood: CartFood,
    viewModel: TotalPriceViewModel,
) {
    var pickerValue by remember { mutableIntStateOf(cartFood.amountToBuy) }

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            ),
        border = BorderStroke(2.dp, Color.Red),
        colors = CardDefaults.cardColors(containerColor = Yellow)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(all = 12.dp)
                    .width(width = 280.dp),
            ) {
                cartFood.food.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        color = Color.Red,
                        fontSize = 25.sp
                    )
                }
                Text(
                    text = "Cijena: ${cartFood.food.price}€",
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End),
                    color = Color.DarkGray,
                    fontSize = 25.sp
                )
            }
            cartFood.food.let {
                it.amount?.let { it1 ->
                    VerticalNumberPicker(
                        max = it1,
                        default = cartFood.amountToBuy,
                        onValueChange = { value ->
                            pickerValue = value
                            viewModel.totalPrice.floatValue = pickerValue.toFloat()
                            if (pickerValue != 0) {
                                ShoppingCartRepository.add(CartFood(amountToBuy = pickerValue, cartFood.food))
                            } else {
                                cartFood.food.name?.let { it2 -> ShoppingCartRepository.remove(it2) }
                            }
                            viewModel.totalPrice.floatValue = ShoppingCartRepository.calculateTotalPrice()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CheckoutCartCard(cartFood: CartFood, viewModel: TotalPriceViewModel) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
        ) {
            CartFoodCard(cartFood, viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutCartCardPreview() {
    FoodIOTheme {
        //CheckoutCartCard(CartFood(10, Food(name = "Hamburger", price = 20f)), viewModel)
    }
}