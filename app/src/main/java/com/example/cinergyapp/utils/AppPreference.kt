package com.example.cinergyapp.utils

import android.content.Context

object AppPreference {

   private const val PREFERENCE_NAME = "proudPunjabiPref"

    @JvmStatic
    fun saveBoolean(key: String, value: Boolean) {
        CinergyApplication.appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(key, value).apply()
    }

    @JvmStatic
    fun getBoolean(key: String): Boolean =
        CinergyApplication.appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getBoolean(key, false)

    @JvmStatic
    fun getString(key: String): String? =
        CinergyApplication.appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getString(key , "")

    @JvmStatic
    fun saveString(key: String, value: String) {
        CinergyApplication.appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        .putString(key, value).apply()
    }

    @JvmStatic
    fun getInt(key: String): Int =
        CinergyApplication.appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getInt(key , -1947)

    @JvmStatic
    fun saveInt(key: String, value: Int) {
        CinergyApplication.appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        .putInt(key, value).apply()
    }
}