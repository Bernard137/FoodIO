package com.example.foodio.views

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chillibits.composenumberpicker.VerticalNumberPicker
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
        shape = MaterialTheme.shapes.extraSmall,
        modifier = Modifier
            .padding(
                start = 4.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .clickable {
                        Toast
                            .makeText(context, "You clicked ${food.name}, no more info than this, sorry! Enjoy food :)", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .padding(all = 12.dp)
                    .weight(1f),
            ) {
                food.name?.let { name ->
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 25.sp
                    )
                }
                food.description?.let { description ->
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp
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

        food.price?.let { price ->
            Text(
                modifier = Modifier.padding(start = 12.dp, bottom = 6.dp),
                text = "Price: ${price}€",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 25.sp
            )
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