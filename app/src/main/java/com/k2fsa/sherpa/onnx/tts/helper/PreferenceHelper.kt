package com.k2fsa.sherpa.onnx.tts.helper

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
  private const val PREF_NAME = "tts_prefs"

  fun getFloat(context: Context, key: String, defaultValue: Float): Float {
    return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
      .getFloat(key, defaultValue)
  }

  fun getInt(context: Context, key: String, defaultValue: Int): Int {
    return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
      .getInt(key, defaultValue)
  }
}