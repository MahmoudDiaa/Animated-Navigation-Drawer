package com.diaa.customAnimatedNavigationDrawer

import android.app.Application
import android.content.res.Resources


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }




  companion object{
      lateinit var instance: App
      fun getApplicationTheme(): Resources.Theme = instance.theme
  }

}