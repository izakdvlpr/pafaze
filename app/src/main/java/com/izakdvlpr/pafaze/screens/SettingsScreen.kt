package com.izakdvlpr.pafaze.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.WbSunny
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.utils.noRippleClickable
import com.izakdvlpr.pafaze.viewmodels.ColorMode
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  navController: NavHostController,
  themeViewModel: ThemeViewModel
) {
  val context = LocalContext.current

  val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
  val packageName = packageInfo.packageName
  val versionName = "v${packageInfo.versionName}"

  val themeState = themeViewModel.state.collectAsState().value

  val (isOpenThemeDialog, setOpenThemeDialog) = remember { mutableStateOf(false) }

  fun toggleThemeDialog() {
    setOpenThemeDialog(!isOpenThemeDialog)
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
        .padding(screenPadding),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(screenGap)
      ) {
        Row(
          modifier = Modifier.noRippleClickable { toggleThemeDialog() },
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(screenGap)
        ) {
          Icon(
            imageVector = Icons.Outlined.WbSunny,
            contentDescription = "Theme"
          )

          Column {
            Text(
              text = "Tema",
              style = MaterialTheme.typography.bodyLarge
            )

            Text(
              text = themeState.colorMode.title,
              color = MaterialTheme.colorScheme.secondary,
              style = MaterialTheme.typography.bodySmall
            )
          }
        }

        if (isOpenThemeDialog) {
          Dialog(onDismissRequest = { toggleThemeDialog() }) {
            val cardHeight = 200.dp
            val cardBorderRadius = 16.dp

            Card(
              modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight),
              shape = RoundedCornerShape(cardBorderRadius),
            ) {
              Column(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(screenPadding + 10.dp),
                verticalArrangement = Arrangement.spacedBy(screenGap)
              ) {
                Text(
                  text = "Tema",
                  color = MaterialTheme.colorScheme.primary,
                  style = MaterialTheme.typography.titleMedium
                )

                val themes = listOf(ColorMode.NORD, ColorMode.DRACULA, ColorMode.HACKER)

                Column(
                  verticalArrangement = Arrangement.spacedBy(screenGap / 2)
                ) {
                  themes.forEach { theme ->
                    Row(
                      modifier = Modifier.noRippleClickable {
                        themeViewModel.setColorMode(theme)
                        toggleThemeDialog()
                      },
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(screenGap / 2)
                    ) {
                      RadioButton(selected = themeState.colorMode == theme, onClick = null)

                      Text(text = theme.title)
                    }
                  }
                }
              }
            }
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
          style = MaterialTheme.typography.titleLarge
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