package com.izakdvlpr.pafaze.screens

import android.os.Handler
import android.os.Looper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.R
import com.izakdvlpr.pafaze.navigation.NavigationRoutes

@Composable
fun SplashScreen(
  navController: NavHostController
) {
  BackHandler {}

  LaunchedEffect(key1 = Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
      navController.navigate(NavigationRoutes.tasks)
    }, 3 * 1000)
  }

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Image(
      painter = painterResource(R.drawable.pafaze_logo),
      modifier = Modifier.size(180.dp),
      contentDescription = null,
      contentScale = ContentScale.Fit
    )
  }
}