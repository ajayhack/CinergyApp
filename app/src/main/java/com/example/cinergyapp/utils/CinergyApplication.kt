package com.example.cinergyapp.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.StrictMode
import android.support.multidex.MultiDex

class CinergyApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var appContext: Context
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        MultiDex.install(this)
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())


        registerActivityLifecycleCallbacks(object: ActivityLifecycleCallbacks{
            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityStarted(p0: Activity) {
                if (p0 != null) {

                    val name = p0::class.java.simpleName
                }
            }

            override fun onActivityDestroyed(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityStopped(p0: Activity) {
                val name = p0::class.java.simpleName
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                p0.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            }
        })
    }
}