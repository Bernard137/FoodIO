package com.example.foodio.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Food(
    var id: String? = null,
    var name: String? = null,
    var price: Float? = null,
    var description: String? = null,
    var amount: Int? = null,
)

data class Category(
    var id: String? = null,
    var name: String? = null,
    var food: List<Food>? = null,
    @ServerTimestamp
    var date: Date? = null,
)

