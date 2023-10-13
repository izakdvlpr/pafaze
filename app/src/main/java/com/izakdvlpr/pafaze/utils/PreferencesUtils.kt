package com.izakdvlpr.pafaze.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesUtils(private val context: Context) {
  private fun sharedPreferences(): SharedPreferences {
    return context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
  }

  fun hasTheme(): Boolean {
    return sharedPreferences().contains(THEME_KEY)
  }

  fun getTheme(): String {
    return sharedPreferences().getString(THEME_KEY, null).orEmpty()
  }

  fun setTheme(theme: String?) {
    sharedPreferences().edit().putString(THEME_KEY, theme).apply()
  }

  fun removeTheme(): Unit {
    sharedPreferences().edit().remove(THEME_KEY).apply()
  }

  companion object {
    val SETTINGS_PREFERENCES = "pafaze_settings"
    val THEME_KEY = "pafaze_theme"
  }
}