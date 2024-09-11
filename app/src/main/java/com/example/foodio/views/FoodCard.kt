package com.example.foodio.views

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chillibits.composenumberpicker.VerticalNumberPicker
import com.example.foodio.FoodioApplication
import com.example.foodio.data.CartFood
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.models.Food
import com.example.foodio.ui.theme.FoodIOTheme

@Composable
fun FoodCard(
    food: Food,
) {
    val context = LocalContext.current
    var pickerValue by remember { mutableIntStateOf(0) }
    val defaultFoodInCart: CartFood? = food.name?.let { ShoppingCartRepository.getFoodFromCartWithName(it) }
    if (defaultFoodInCart != null) {
        pickerValue = defaultFoodInCart.amountToBuy
    }

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
                    .clickable {
                        Toast
                            .makeText(context, "You clicked ${food.name}, no more info than this, sorry!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .padding(all = 12.dp)
                    .width(width = 280.dp),
            ) {
                food.name?.let { name ->
                    Text(
                        text = name,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        color = Color.Red,
                        fontSize = 25.sp
                    )
                }
                food.description?.let { description ->
                    Text(
                        text = description,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End),
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    )
                }
                food.price?.let { price ->
                    Text(
                        text = "Cijena: ${price}€",
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End),
                        color = Color.DarkGray,
                        fontSize = 25.sp
                    )
                }
            }

            food.amount?.let {
                VerticalNumberPicker(
                    max = it,
                    default = pickerValue,
                    onValueChange = { value ->
                        pickerValue = value
                        if (pickerValue != 0) {
                            ShoppingCartRepository.add(CartFood(amountToBuy = pickerValue, food))
                        } else {
                            food.name?.let { it1 -> ShoppingCartRepository.remove(it1) }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodCardPreview() {
    FoodIOTheme {
        FoodCard(Food(name = "Hamburger1", description = "Description", price = 20f))
    }
}