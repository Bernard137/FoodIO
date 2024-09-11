package com.example.foodio.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodio.data.CartFood
import com.example.foodio.models.Category
import com.example.foodio.models.Food
import com.example.foodio.ui.theme.FoodIOTheme
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
                .padding(all = 12.dp)
        ) {
            category.name?.let { name ->
                Text(
                    text = name,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .wrapContentWidth(Alignment.Start),
                    color = Color.DarkGray,
                    fontSize = 25.sp
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

@OptIn(ExperimentalCoroutinesApi::class)
@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    val list: MutableList<Food> = mutableListOf()
    list.add(Food(name = "Hamburger1", description = "Description", price = 20f))
    FoodIOTheme {
        CategoryCard(Category(name = "Hamburgers", food = list))
    }
}