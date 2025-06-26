package com.example.app.engine;

import android.util.Log;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OnnxTensor;

import java.nio.FloatBuffer;
import java.util.Collections;

public class Any {
  private final String modelPath;
  private OrtEnvironment env;
  private OrtSession session;

  public Any(String modelPath) {
    this.modelPath = modelPath;
    try {
      env = OrtEnvironment.getEnvironment();
      session = env.createSession(modelPath, new OrtSession.SessionOptions());
      Log.d("Any", "Initialized with model path: " + modelPath);
    } catch (Exception e) {
      Log.e("Any", "Failed to initialize ONNX model", e);
    }
  }

  public String transcribe(float[] audioData) {
    try {
      long[] shape = new long[]{1, audioData.length};
      OnnxTensor input = OnnxTensor.createTensor(env, FloatBuffer.wrap(audioData), shape);
      OrtSession.Result result = session.run(Collections.singletonMap("input", input));
      String output = result.get(0).getValue().toString();
      result.close();
      input.close();
      return output;
    } catch (Exception e) {
      Log.e("Any", "Transcription failed", e);
      return "Error during transcription";
    }
  }
}