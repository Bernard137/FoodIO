
package com.example.foodio.views

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignLanguage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodio.AdminActivity
import com.example.foodio.CheckoutActivity
import com.example.foodio.LoginActivity
import com.example.foodio.data.DataOrException
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.models.Category
import com.example.foodio.models.Food
import com.example.foodio.ui.theme.FoodIOTheme
import com.example.foodio.utils.CircularProgressBar
import com.example.foodio.viewmodel.CategoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun CategoryCard(
    category: Category,
) {
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
                .padding(all = 6.dp)
        ) {
            category.name?.let { name ->
                Text(
                    text = name,
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp
                )
            }
            if (category.food != null) {
                for (food: Food in category.food!!) {
                    FoodCard(food)
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun CategoriesActivity(dataOrException: DataOrException<List<Category>, Exception>, username: String, viewModel: CategoryViewModel) {
    Surface {
        val categories = dataOrException.data
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (username == "admin") {
                    Button(
                        onClick = {
                            val intent = Intent(context, AdminActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                    )
                    {
                        Text("Add new product")
                    }
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Default.SignLanguage,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            ShoppingCartRepository.clearFoodCart()
                            context.startActivity(Intent(context, LoginActivity::class.java))
                            (context as Activity).finish()
                        }
                )
            }

            categories?.let {
                LazyColumn {
                    items(
                        items = categories
                    ) { category ->
                        CategoryCard(category = category)
                    }
                }
            }
        }

        val e = dataOrException.e
        e?.let {
            Text(
                text = e.message!!,
                modifier = Modifier.padding(16.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressBar(
                isDisplayed = viewModel.loading.value
            )
            Button(onClick = {
                context.startActivity(Intent(context, CheckoutActivity::class.java))
                (context as Activity).finish()
            },
                modifier = Modifier
                    .offset(130.dp, 370.dp)
                    .size(height = 85.dp, width = 110.dp)
            ) {
                Text("Checkout")
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoryCardPreview() {
    val listFood: MutableList<Food> = mutableListOf()
    listFood.add(Food(name = "Hamburger_1", price = 20f, description = "Lorem ipsum est", amount = 10))
    listFood.add(Food(name = "Hamburger_2", price = 20f, description = "Lorem ipsum est", amount = 10))
    listFood.add(Food(name = "Hamburger_3", price = 20f, description = "Lorem ipsum est", amount = 10))

    FoodIOTheme(dynamicColor = false) {
        CategoryCard(Category(name = "Hamburger", food = listFood))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoryCardPreviewDark() {
    val listFood: MutableList<Food> = mutableListOf()
    listFood.add(Food(name = "Hamburger_1", price = 20f, description = "Lorem ipsum estLorem ipsum estLorem ipsum estLorem Lorem ipsum estLorem ipsum estimate estLorem ipsum estLorem ipsum est", amount = 10))
    listFood.add(Food(name = "Hamburger_2", price = 20f, description = "Lorem ipsum est", amount = 10))
    listFood.add(Food(name = "Hamburger_3", price = 20f, description = "Lorem ipsum est", amount = 10))
    listFood.add(Food(name = "Hamburger_3", price = 20f, description = "Lorem ipsum est", amount = 10))
    listFood.add(Food(name = "Hamburger_3", price = 20f, description = "Lorem ipsum est", amount = 10))
    listFood.add(Food(name = "Hamburger_3", price = 20f, description = "Lorem ipsum est", amount = 10))
    listFood.add(Food(name = "Hamburger_3", price = 20f, description = "Lorem ipsum est", amount = 10))

    FoodIOTheme(dynamicColor = false, darkTheme = true) {
        CategoryCard(Category(name = "Hamburger", food = listFood))
    }
}
