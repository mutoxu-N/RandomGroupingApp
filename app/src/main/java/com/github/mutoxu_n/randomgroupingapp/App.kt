package com.github.mutoxu_n.randomgroupingapp

import android.app.Application
import android.content.Context

class App: Application() {
    companion object {
        private lateinit var application: Application

        fun getContext(): Context {
            return application.applicationContext!!
        }
    }

    init {
        application = this
    }
}