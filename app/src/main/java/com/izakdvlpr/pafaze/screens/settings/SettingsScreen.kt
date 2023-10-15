package com.izakdvlpr.pafaze.screens.settings

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
import com.izakdvlpr.pafaze.screens.settings.components.SettingsRow
import com.izakdvlpr.pafaze.screens.settings.components.ThemeDialog
import com.izakdvlpr.pafaze.utils.noRippleClickable
import com.izakdvlpr.pafaze.viewmodels.ColorMode
import com.izakdvlpr.pafaze.viewmodels.DialogTypes
import com.izakdvlpr.pafaze.viewmodels.SettingsViewModel
import com.izakdvlpr.pafaze.viewmodels.ThemeState
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

        SettingsRow(
          title = "Tema",
          value = themeState.colorMode.title,
          icon = Icons.Outlined.WbSunny,
          onClick = { toggleThemeDialog() }
        )

        SettingsRow(
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