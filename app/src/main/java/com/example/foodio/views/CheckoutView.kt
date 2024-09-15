package com.example.foodio.views

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chillibits.composenumberpicker.HorizontalNumberPicker
import com.example.foodio.TotalPriceViewModel
import com.example.foodio.data.CartFood
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.models.Food
import com.example.foodio.services.Firebase
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(all = 12.dp)
                    .fillMaxWidth(),
            ) {
                cartFood.food.name?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 25.sp
                    )
                }
                Text(
                    text = "Price: ${cartFood.food.price}€",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp
                )

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    cartFood.food.let {
                        it.amount?.let { it1 ->
                            HorizontalNumberPicker(
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
    }
}

@Composable
fun CheckoutCartCard(cartFood: CartFood, viewModel: TotalPriceViewModel) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 4.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 2.dp)
        ) {
            CartFoodCard(cartFood, viewModel)
        }
    }
}

@Composable
fun MyCheckoutActivity(foodInCart: MutableList<CartFood>, viewModel: TotalPriceViewModel) {
    Surface {
        val context = LocalContext.current
        viewModel.totalPrice.floatValue = ShoppingCartRepository.calculateTotalPrice()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.size(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyBackButton(context)
                Text(
                    text = "Shopping cart",
                    fontFamily = FontFamily.Cursive,
                    fontSize = TextUnit(10f, TextUnitType.Em),
                    textAlign = TextAlign.Center,
                    lineHeight = TextUnit(0.8f, TextUnitType.Em)
                )
            }

            LazyColumn(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                items(
                    items = foodInCart
                ) { food ->
                    CheckoutCartCard(cartFood = food, viewModel)
                }
            }

            Spacer(Modifier.size(25.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Total price: ${viewModel.totalPrice.floatValue}e (-${ShoppingCartRepository.getDiscount()}%)",
                fontFamily = FontFamily.Cursive,
                fontSize = TextUnit(8f, TextUnitType.Em),
                textAlign = TextAlign.Center,
                lineHeight = TextUnit(0.8f, TextUnitType.Em)
            )

            Button(
                onClick = {
                    if (ShoppingCartRepository.getFoodInCart().isNotEmpty()) {
                        Firebase.shopWithUsername(ShoppingCartRepository.getUsername(), context)
                        ShoppingCartRepository.clearFoodCart()
                        ShoppingCartRepository.setDiscount(0.0f)
                        goBackToCleanMainActivity(context)
                    } else {
                        Toast.makeText(context, "Put something in cart first!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
            ) {
                Text("Checkout")
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyCheckoutActivityPreview() {
    val list: MutableList<CartFood> = mutableListOf()
    list.add(CartFood(10, Food(name = "Hamburger", price = 20f)))
    list.add(CartFood(1, Food(name = "Pizza", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(10, Food(name = "Hamburger", price = 20f)))
    list.add(CartFood(1, Food(name = "Pizza", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(10, Food(name = "Hamburger", price = 20f)))
    list.add(CartFood(1, Food(name = "Pizza", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(10, Food(name = "Hamburger", price = 20f)))
    list.add(CartFood(1, Food(name = "Pizza", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))

    FoodIOTheme(dynamicColor = false) {
        MyCheckoutActivity(list, TotalPriceViewModel())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyCheckoutActivityPreviewDark() {
    val list: MutableList<CartFood> = mutableListOf()
    list.add(CartFood(10, Food(name = "Hamburger", price = 20f)))
    list.add(CartFood(1, Food(name = "Pizza", price = 10f)))
    list.add(CartFood(1, Food(name = "Cornet", price = 10f)))

    FoodIOTheme(dynamicColor = false, darkTheme = true) {
        MyCheckoutActivity(list, TotalPriceViewModel())
    }
}
