package com.example.foodio.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodio.MainActivity
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.ui.theme.FoodIOTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun MyBackButton(context: Context) {
    Image(
        painter = painterResource(com.chillibits.composenumberpicker.R.drawable.ic_arrow_left),
        contentDescription = "AdminScreenReturn",
        modifier = Modifier
            .size(90.dp)
            .padding(vertical = 15.dp)
            .clickable {
                goBackToCleanMainActivity(context)
            }
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun goBackToCleanMainActivity(context: Context) {
    val isFirstShopping: Boolean = if (ShoppingCartRepository.getDiscount() > 0f) true else false

    context.startActivity(Intent(context, MainActivity::class.java)
        .putExtra("username", ShoppingCartRepository.getUsername())
        .putExtra("first_shopping", isFirstShopping))
    (context as Activity).finish()
}

@Preview
@Composable
fun PreviewButton (){
    FoodIOTheme(dynamicColor = false) {
        MyBackButton(LocalContext.current)
    }
}