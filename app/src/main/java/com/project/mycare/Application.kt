package com.project.mycare

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class PostNatalCareApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}