package com.example.foodio.services

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.foodio.FoodioApplication
import com.example.foodio.MainActivity
import com.example.foodio.data.UserRepository
import com.example.foodio.models.Category
import com.example.foodio.models.Food
import com.example.foodio.models.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi

object Firebase {
    private val db by lazy { Firebase.firestore }

    fun fetchUsers() {
        this.db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val user = User(document["username"].toString(), document["password"].toString(), document["first_shopping"] as Boolean)
                    UserRepository.add(user)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun registerUser(username: String, password: String, context: Context) {
        val user = User(username, password)

        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("first_shopping", true)
                context.startActivity(intent)
                (context as Activity).finish()

                Toast.makeText(context, "Registration is done successfully.!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(context, "There was an issue with registration!", Toast.LENGTH_SHORT).show()
            }
    }

    fun shopWithUsername(username: String, context: Context) {
        this.db.collection("users")
            .whereEqualTo("username", username)
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.documents.size > 0) { // Node exist
                        db.collection("users").document(task.result.documents[0].id)
                            .update("first_shopping", false)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Thank You for the order!", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                                Toast.makeText(context, "There was an issue with ordering products!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
    }

    fun addProductToStorage(category: String, foodName: String, foodDescription: String, foodPrice: Float, foodAmount: Int) {
        this.db.collection("categories")
            .whereEqualTo("id", "${category}_ID")
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.documents.size > 0) { // Node exist
                        db.collection("categories").document(task.result.documents[0].id)
                            .update("food", FieldValue.arrayUnion(Food("${foodName}_ID", foodName, foodPrice, foodDescription, foodAmount)))
                            .addOnSuccessListener {
                                Toast.makeText(FoodioApplication.ApplicationContext, "Adding product is done successfully.!", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                                Toast.makeText(FoodioApplication.ApplicationContext, "There was an issue with adding product!", Toast.LENGTH_SHORT).show()
                            }
                    } else { // Node does not exist
                        val foodList = listOf(Food("${foodName}_ID", foodName, foodPrice, foodDescription, foodAmount))
                        db.collection("categories")
                            .add(Category(
                                id = "${category}_ID",
                                name = category,
                                food = foodList
                            ))
                            .addOnSuccessListener {
                                Toast.makeText(FoodioApplication.ApplicationContext, "Adding fully new product is done successfully.!", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                                Toast.makeText(FoodioApplication.ApplicationContext, "There was an issue with adding product!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
    }
}

/*data class Food(
    var id: String? = null,
    var name: String? = null,
    var price: Float? = null,
    var description: String? = null,
    var amount: Int? = null
)*/



