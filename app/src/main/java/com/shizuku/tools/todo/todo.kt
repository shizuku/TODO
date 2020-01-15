package com.shizuku.tools.todo

import android.app.Application
import android.content.Context

class todo : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
