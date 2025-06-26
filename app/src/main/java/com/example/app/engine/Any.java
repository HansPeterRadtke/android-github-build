package com.example.app.engine;

import android.util.Log;

public class Any {
  private final String modelPath;

  public Any(String modelPath) {
    this.modelPath = modelPath;
    Log.d("Any", "Initialized with model path: " + modelPath);
  }

  public String transcribe(float[] audioData) {
    Log.d("Any", "Transcribing audio data of length: " + audioData.length);
    return "<dummy transcription result>";
  }
}