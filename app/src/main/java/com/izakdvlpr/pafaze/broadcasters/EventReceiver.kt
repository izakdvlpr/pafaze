package com.izakdvlpr.pafaze.broadcasters

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class EventReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    println("acorda baianinho de maua")
  }
}