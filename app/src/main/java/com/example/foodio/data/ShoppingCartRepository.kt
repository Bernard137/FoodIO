package com.example.foodio.data

import com.example.foodio.models.Food
import java.math.RoundingMode

data class CartFood(var amountToBuy: Int = 0, val food: Food)

object ShoppingCartRepository {
    private val foodInCart: MutableList<CartFood> = mutableListOf()
    private var discount: Float = 0f
    private var username: String = ""

    fun add(food: CartFood) {
        val foodCartPos = foodInCart.indexOfFirst { it.food.name == food.food.name }
        if (foodCartPos != -1) {
            foodInCart[foodCartPos].amountToBuy = food.amountToBuy
        } else {
            foodInCart.add(food)
        }
    }

    fun getFoodInCart(): MutableList<CartFood> = this.foodInCart
    fun remove(foodName: String) = foodInCart.removeAll { it.food.name == foodName }
    fun clearFoodCart() = foodInCart.clear()
    fun setDiscount(discount: Float = 0f) {
        this.discount = discount
    }

    fun getDiscount(): Float = this.discount
    fun getFoodFromCartWithName(foodName: String): CartFood? {
        val foodCartPos = foodInCart.indexOfFirst { it.food.name == foodName }
        if (foodCartPos != -1) {
            return this.foodInCart[foodCartPos]
        }
        return null
    }

    fun setUsername(username: String = "") {
        this.username = username
    }

    fun getUsername(): String = this.username

    fun calculateTotalPrice(): Float {
        var totalPrice = 0f
        for (item in this.foodInCart) {
            totalPrice += item.amountToBuy * item.food.price!!
            totalPrice -= totalPrice * (this.discount / 100)
        }
        return totalPrice.toBigDecimal().setScale(2, RoundingMode.UP).toFloat()
    }
}
