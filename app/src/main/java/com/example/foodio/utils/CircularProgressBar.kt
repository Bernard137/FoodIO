package com.example.foodio.utils

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun CircularProgressBar(
    isDisplayed: Boolean,
) {
    if (isDisplayed) {
        CircularProgressIndicator()
    }
}
