package com.example.app.engine;

import android.util.Log;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OnnxTensor;

import java.util.Collections;

public class TtsEngine {
  private final String modelPath;
  private final String configPath;
  private OrtEnvironment env;
  private OrtSession session;

  public TtsEngine(String modelPath, String configPath) {
    this.modelPath  = modelPath;
    this.configPath = configPath;
    try {
      env     = OrtEnvironment.getEnvironment();
      session = env.createSession(modelPath, new OrtSession.SessionOptions());
      Log.d("TtsEngine", "Initialized with model: " + modelPath + ", config: " + configPath);
    } catch (Exception e) {
      Log.e("TtsEngine", "Failed to initialize TTS ONNX model", e);
    }
  }

  public short[] synthesize(String text) {
    try {
      float[] dummyInput = new float[80];
      for (int i = 0; i < dummyInput.length; i++) dummyInput[i] = i;
      OnnxTensor input = OnnxTensor.createTensor(env, dummyInput);
      OrtSession.Result result = session.run(Collections.singletonMap("input", input));
      float[] audio = (float[]) result.get(0).getValue();
      short[] pcm = new short[audio.length];
      for (int i = 0; i < audio.length; i++) pcm[i] = (short)(audio[i] * 32767);
      result.close();
      input.close();
      return pcm;
    } catch (Exception e) {
      Log.e("TtsEngine", "TTS synthesis failed", e);
      return new short[0];
    }
  }
}