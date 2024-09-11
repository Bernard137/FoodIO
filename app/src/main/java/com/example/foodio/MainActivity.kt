package com.example.foodio

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.foodio.data.DataOrException
import com.example.foodio.data.ShoppingCartRepository
import com.example.foodio.models.Category
import com.example.foodio.utils.CircularProgressBar
import com.example.foodio.viewmodel.CategoryViewModel
import com.example.foodio.views.CategoryCard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username")
        username?.let { ShoppingCartRepository.setUsername(it) }
        if (intent.getBooleanExtra("first_shopping", false))
            ShoppingCartRepository.setDiscount(10f)

        setContent {
            val dataOrException = viewModel.data.value
            if (username != null) {
                CategoriesActivity(dataOrException, username)
            }
        }
    }

    @Composable
    fun CategoriesActivity(dataOrException: DataOrException<List<Category>, Exception>, username: String) {
        val categories = dataOrException.data
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    Text("Dodaj novi produkt")
                }
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
                val intent = Intent(context, CheckoutActivity::class.java)
                context.startActivity(intent)
            },
                modifier = Modifier
                    .offset(130.dp, 350.dp)
                    .size(100.dp)
            ) {
                Text("Buy")
            }
        }
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)

        val username = intent.getStringExtra("username")
        setContent {
            val dataOrException = viewModel.data.value
            if (username != null) {
                CategoriesActivity(dataOrException, username)
            }
        }
    }
}


