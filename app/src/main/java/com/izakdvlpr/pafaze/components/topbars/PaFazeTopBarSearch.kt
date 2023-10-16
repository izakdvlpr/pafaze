package com.izakdvlpr.pafaze.components.topbars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaFazeTopBarSearch(
  navController: NavHostController,
  search: String,
  placeholder: String,
  isSearchMode: Boolean,
  onSearchMode: (isSearchMode: Boolean) -> Unit,
  onSearchChangeValue: (search: String) -> Unit,
  onActionBack: () -> Unit,
) {
  val searchInputFocusRequester = remember { FocusRequester() }

  val (isSearchTextFieldLoaded, setIsSearchTextFieldLoaded) = remember { mutableStateOf(false) }

  TopAppBar(
    title = {
      if (isSearchMode) {
        BasicTextField(
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(searchInputFocusRequester)
            .onGloballyPositioned {
              if (!isSearchTextFieldLoaded) {
                searchInputFocusRequester.requestFocus()

                setIsSearchTextFieldLoaded(true)
              }
            },
          value = search,
          maxLines = 1,
          singleLine = true,
          cursorBrush = SolidColor(Color.White),
          textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
          decorationBox = { innerTextField ->
            Row(modifier = Modifier.fillMaxWidth()) {
              if (search.isEmpty()) Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyMedium
              )
            }

            innerTextField()
          },
          onValueChange = { onSearchChangeValue(it) }
        )
      } else {
        Text(text = "PaFaze")
      }
    },
    navigationIcon = {
      if (isSearchMode) {
        IconButton(
          onClick = {
            onSearchMode(false)
            setIsSearchTextFieldLoaded(false)
            onActionBack()
          }
        ) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back"
          )
        }
      }
    },
    actions = {
      if (isSearchMode) {
        Box(modifier = Modifier.width(40.dp))
      } else {
        IconButton(onClick = { onSearchMode(!isSearchMode) }) {
          Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "Search"
          )
        }

        IconButton(onClick = { navController.navigate(NavigationRoutes.settings) }) {
          Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = "Settings"
          )
        }
      }
    }
  )
}