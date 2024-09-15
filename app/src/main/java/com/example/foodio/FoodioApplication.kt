package com.example.foodio

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodioApplication : Application() {
    companion object {
        lateinit var ApplicationContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationContext = this
    }
}
