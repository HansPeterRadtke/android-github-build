package com.example.app.engine;

import android.content.Context;
import android.util.Log;
import com.microsoft.onnxruntime.*;
import java.io.*;
import java.nio.FloatBuffer;
import java.util.Collections;

public class SttEngine {
  private static final String TAG = "SttEngine";
  private final Context context;
  private OrtEnvironment env;
  private OrtSession session;

  public SttEngine(Context context) {
    this.context = context;
    try {
      Log.d(TAG, "Initializing STT Engine");
      env = OrtEnvironment.getEnvironment();
      OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
      InputStream modelIn = context.getAssets().open("models/stt/whisper_tiny_en.onnx");
      File modelFile = new File(context.getCacheDir(), "whisper.onnx");
      try (FileOutputStream out = new FileOutputStream(modelFile)) {
        byte[] buffer = new byte[4096];
        int read;
        while ((read = modelIn.read(buffer)) != -1) out.write(buffer, 0, read);
      }
      session = env.createSession(modelFile.getAbsolutePath(), opts);
    } catch (Exception e) {
      Log.e(TAG, "Failed to load STT model", e);
    }
  }

  public String transcribe(short[] pcmData) {
    try {
      float[] input = new float[pcmData.length];
      for (int i = 0; i < pcmData.length; i++) input[i] = pcmData[i] / 32768f;
      OnnxTensor inputTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(input), new long[]{1, input.length, 1});
      OrtSession.Result output = session.run(Collections.singletonMap("audio_input", inputTensor));
      float[][] logits = (float[][]) output.get(0).getValue();
      // This is a placeholder. Token decoding needs to be implemented with proper Whisper tokenizer.
      return "[decoded text]";
    } catch (Exception e) {
      Log.e(TAG, "Transcription failed", e);
      return null;
    }
  }
}