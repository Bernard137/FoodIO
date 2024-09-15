package com.example.foodio.di

import com.example.foodio.utils.Constants.CATEGORIES_COLLECTION
import com.example.foodio.utils.Constants.NAME_PROPERTY
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.ASCENDING
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideQueryCategoriesByName() = FirebaseFirestore.getInstance()
        .collection(CATEGORIES_COLLECTION)
        .orderBy(NAME_PROPERTY, ASCENDING)
}
