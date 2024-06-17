package com.example.carmanagement.util

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {

    private const val PREFERENCES_NAME = "my_preferences"
    private const val KEY_USER_NAME = "key_user_name"
    private const val KEY_IMAGE = "key_image"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getUserNameValue(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_NAME, "نام و نام خانوادگی")
    }

    fun setUserNameValue(context: Context, value: String) {
        getPreferences(context).edit().putString(KEY_USER_NAME, value).apply()
    }

    fun getImageValue(context: Context): String? {
        return getPreferences(context).getString(KEY_IMAGE, null)
    }

    fun setImageValue(context: Context, value: String) {
        getPreferences(context).edit().putString(KEY_IMAGE, value).apply()
    }


}