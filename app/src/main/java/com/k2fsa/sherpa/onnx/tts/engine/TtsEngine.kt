package com.k2fsa.sherpa.onnx.tts.engine

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.k2fsa.sherpa.onnx.tts.helper.PreferenceHelper

private const val TAG = "TtsEngine"

class TtsEngine(context: Context) {

  private var speed by mutableFloatStateOf(
    PreferenceHelper.getFloat(context, "speed", 1.0f)
  )

  private var speaker by mutableIntStateOf(
    PreferenceHelper.getInt(context, "speaker", 0)
  )

  fun synthesize(text: String): ShortArray {
    Log.d(TAG, "Synthesizing for text: $text, speed: $speed, speaker: $speaker")
    return ShortArray(22050) { i -> (Math.sin(2.0 * Math.PI * i / 100) * 32767).toInt().toShort() }
  }
}