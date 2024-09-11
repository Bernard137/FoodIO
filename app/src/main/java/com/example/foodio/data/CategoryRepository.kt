package com.example.foodio.data

import com.example.foodio.models.Category
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val queryProductsByName: Query,
) {
    suspend fun getCategoriesFromFirestore(): DataOrException<List<Category>, Exception> {
        val dataOrException = DataOrException<List<Category>, Exception>()
        try {
            dataOrException.data = queryProductsByName.get().await().map { document ->
                document.toObject(Category::class.java)
            }
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }
        return dataOrException
    }
}