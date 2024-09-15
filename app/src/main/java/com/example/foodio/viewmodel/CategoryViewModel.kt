
package com.example.foodio.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodio.models.Category
import com.example.foodio.data.CategoryRepository
import com.example.foodio.data.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository,
) : ViewModel() {
    var loading = mutableStateOf(false)
    val data: MutableState<DataOrException<List<Category>, Exception>> = mutableStateOf(
        DataOrException(
            listOf(),
            Exception("")
        )
    )

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            loading.value = true
            data.value = repository.getCategoriesFromFirestore()
            loading.value = false
        }
    }
}
