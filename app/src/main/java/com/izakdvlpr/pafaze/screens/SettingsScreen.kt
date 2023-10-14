package com.izakdvlpr.pafaze.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.utils.noRippleClickable
import com.izakdvlpr.pafaze.viewmodels.ColorMode
import com.izakdvlpr.pafaze.viewmodels.DialogTypes
import com.izakdvlpr.pafaze.viewmodels.SettingsViewModel
import com.izakdvlpr.pafaze.viewmodels.ThemeState
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  navController: NavHostController,
  themeViewModel: ThemeViewModel,
  settingsViewModel: SettingsViewModel
) {
  val context = LocalContext.current

  val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
  val packageName = packageInfo.packageName
  val versionName = "v${packageInfo.versionName}"

  val themeState = themeViewModel.state.collectAsState().value
  val settingsState = settingsViewModel.state.collectAsState().value

  fun toggleThemeDialog() {
    settingsViewModel.setCurrentDialog(DialogTypes.THEME_DIALOG)

    settingsViewModel.setIsOpenDialog(!settingsState.isOpenDialog)
  }

  Scaffold(
    topBar = {
      TopBar(
        navController = navController,
        themeViewModel = themeViewModel
      )
    }
  ) { innerPadding ->
    val screenPadding = 20.dp
    val screenGap = 20.dp

    Column(
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(screenPadding),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(screenGap)
      ) {
        Text(
          text = "Exibição",
          style = MaterialTheme.typography.titleLarge
        )

        OptionRow(
          title = "Tema",
          value = themeState.colorMode.title,
          icon = Icons.Outlined.WbSunny,
          onClick = { toggleThemeDialog() }
        )

        OptionRow(
          title = "Idioma",
          value = "Português",
          icon = Icons.Outlined.Translate,
          onClick = {}
        )

        if (settingsState.isOpenDialog && settingsState.currentDialog != null) {
          when (settingsState.currentDialog) {
            DialogTypes.THEME_DIALOG -> {
              ThemeDialog(
                themeState = themeState,
                themeViewModel = themeViewModel,
                onCloseThemeDialog = { toggleThemeDialog() }
              )
            }

            DialogTypes.LANGUAGE_DIALOG -> {}
          }
        }
      }

      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = "PaFaze",
          color = MaterialTheme.colorScheme.outline,
          style = MaterialTheme.typography.titleMedium
        )

        Text(
          text = versionName,
          color = MaterialTheme.colorScheme.outline,
          style = MaterialTheme.typography.bodySmall
        )

        Text(
          text = packageName,
          color = MaterialTheme.colorScheme.outline,
          style = MaterialTheme.typography.bodySmall
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
  navController: NavHostController,
  themeViewModel: ThemeViewModel
) {
  CenterAlignedTopAppBar(
    navigationIcon = {
      IconButton(onClick = { navController.navigateUp() }) {
        Icon(
          imageVector = Icons.Filled.ArrowBack,
          contentDescription = "Back"
        )
      }
    },
    title = {
      Text(
        text = "Configurações",
        style = MaterialTheme.typography.bodyLarge
      )
    },
    actions = {
      IconButton(onClick = { themeViewModel.resetState() }) {
        Icon(
          imageVector = Icons.Outlined.RestartAlt,
          contentDescription = "Reset"
        )
      }
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      titleContentColor = MaterialTheme.colorScheme.primary,
      navigationIconContentColor = Color.White,
      actionIconContentColor = Color.White,
    ),
  )
}

@Composable
private fun OptionRow(
  title: String,
  value: String,
  icon: ImageVector,
  onClick: () -> Unit,
) {
  val containerGap = 20.dp

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .noRippleClickable { onClick() },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(containerGap)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = "Theme"
    )

    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge
      )

      Text(
        text = value,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodySmall
      )
    }
  }
}

@Composable
private fun ThemeDialog(
  themeState: ThemeState,
  themeViewModel: ThemeViewModel,
  onCloseThemeDialog: () -> Unit,
) {
  val cardPadding = 30.dp
  val cardHeight = 225.dp
  val cardGap = 20.dp

  Dialog(onDismissRequest = { onCloseThemeDialog() }) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(cardHeight),
      shape = MaterialTheme.shapes.large,
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(cardPadding),
        verticalArrangement = Arrangement.spacedBy(cardGap)
      ) {
        Text(
          text = "Tema",
          color = MaterialTheme.colorScheme.primary,
          style = MaterialTheme.typography.titleMedium
        )

        val themes = listOf(ColorMode.NORD, ColorMode.DRACULA, ColorMode.HACKER)

        themes.forEach { theme ->
          Row(
            modifier = Modifier.noRippleClickable {
              themeViewModel.setColorMode(theme)

              onCloseThemeDialog()
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((cardGap - 10.dp) / 2)
          ) {
            RadioButton(selected = themeState.colorMode == theme, onClick = null)

            Text(text = theme.title)
          }
        }
      }
    }
  }
}