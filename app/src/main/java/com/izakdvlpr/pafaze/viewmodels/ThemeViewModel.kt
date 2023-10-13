package com.izakdvlpr.pafaze.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.material3.ColorScheme as Material3ColorScheme
import androidx.lifecycle.AndroidViewModel
import com.izakdvlpr.pafaze.theme.ColorScheme
import com.izakdvlpr.pafaze.utils.PreferencesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
  @SuppressLint("StaticFieldLeak")
  val context = application as Context

  val state by lazy { MutableStateFlow<ThemeState>(ThemeState()) }

  fun resetState() {
    state.value = ThemeState()
  }

  private fun getPreferences(): PreferencesUtils {
    return PreferencesUtils(context)
  }

  private fun setDefaultColorMode() {
    val theme = getPreferences().getTheme()

    val colorMode = ColorMode.findByValue(theme) ?: ThemeState.DEFAULT_COLOR_MODE

    setColorMode(colorMode)
  }

  fun setColorMode(colorMode: ColorMode) {
    state.update { it.copy(colorMode = colorMode) }

    getPreferences().setTheme(colorMode.value)
  }

  init {
    setDefaultColorMode()
  }
}

enum class ColorMode(val title: String, val value: String, var schema: Material3ColorScheme) {
  NORD(title = "Nord", value = "nord", schema = ColorScheme.Nord),
  DRACULA(title = "Dracula", value = "dracula", schema = ColorScheme.Dracula),
  HACKER(title = "Hacker", value = "hacker", schema = ColorScheme.Hacker);

  companion object {
    fun findByValue(value: String): ColorMode? {
      return ColorMode.values().find { it.value == value }
    }
  }
}

data class ThemeState(
  val colorMode: ColorMode = DEFAULT_COLOR_MODE,
) {
  companion object {
    val DEFAULT_COLOR_MODE = ColorMode.NORD
  }
}