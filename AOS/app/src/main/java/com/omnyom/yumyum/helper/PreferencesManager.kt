package com.omnyom.yumyum.helper

import android.content.Context
import android.content.SharedPreferences


class PreferencesManager {
    companion object {
        private const val PREFERENCES_NAME = "my_preference"
        private const val DEFAULT_VALUE_STRING = ""

        private fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }

        fun setString(context: Context, key: String, value: String) {
            with(getPreferences(context).edit()) {
                putString(key, value)
                apply()
            }
//            val prefs = getPreferences(context)
//            val editor = prefs.edit()
//            editor.putString(key, value)
//            editor.apply()
        }

        fun getString(context:Context, key: String): String? {
            val prefs = getPreferences(context)
            return prefs.getString(key, DEFAULT_VALUE_STRING)
        }
    }
}