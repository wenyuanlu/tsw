package com.qichuang.commonlibs.basic

import android.app.Activity
import android.app.Application

/**
 * author : xpSun
 * date : 6/10/21
 * description :
 */
open class CustomBasicApplication : Application() {

    companion object {
        lateinit var instance: CustomBasicApplication
        private var activitys: MutableList<Activity> = mutableListOf()
    }

    fun addActivity(activity: Activity) {
        activitys.add(activity)
    }

    fun removeActivity(activity: Activity){
        activitys.remove(activity)
    }

    fun fetchActivity(): Activity? {
        if (!activitys.isNullOrEmpty()) {
            return activitys[activitys.size - 1]
        }
        return null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}