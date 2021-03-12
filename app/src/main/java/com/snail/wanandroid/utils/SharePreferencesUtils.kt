package com.snail.wanandroid.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.snail.wanandroid.base.WanAndroidApplication
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharePreferencesUtils {
    companion object {
        val instance: SharePreferencesUtils
            get() = HANDLER.sharePreferencesUtils
    }

    private object HANDLER {
         val sharePreferencesUtils = SharePreferencesUtils()
    }

    var cookie  by SharedPreferenceDelegates(ShareKey.COOKIE,"")

    private inner class SharedPreferenceDelegates<T>(
        private val key: String,
        private val defaultValue: T
    ) : ReadWriteProperty<Any?, T> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return getSharePreferences(key, defaultValue)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            putSharePreferences(key, value)
        }

        private val preferences: SharedPreferences by lazy {
            WanAndroidApplication.mApplication.getSharedPreferences(
                "_config",
                Context.MODE_PRIVATE
            )
        }

        @SuppressLint("ApplySharedPref")
        private fun putSharePreferences(name: String, value: T) = with(preferences.edit()) {
            val editor = when (value) {
                is Long -> putLong(name, value)
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Boolean -> putBoolean(name, value)
                is Float -> putFloat(name, value)
                else -> throw IllegalArgumentException("Type Error, cannot be saved!")
            }
            editor.apply()
        }

        @Suppress("UNCHECKED_CAST")
        private fun getSharePreferences(name: String, default: T): T = with(preferences) {
            val res = when (default) {
                is Long -> getLong(name, default)
                is String -> getString(name, default)
                is Int -> getInt(name, default)
                is Boolean -> getBoolean(name, default)
                is Float -> getFloat(name, default)
                else -> throw IllegalArgumentException("Type Error, cannot be got!")
            }
            return res as T
        }
    }
}