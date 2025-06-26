package com.example.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bihe0832.android.lib.sherpa.OnnxAsr;
import com.bihe0832.android.lib.sherpa.OnnxTts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
  private ImageView backgroundImage;
  private boolean isBackgroundVisible = false;
  private static final String TAG = "MainActivity";

  private AudioRecord recorder;
  private Thread recordingThread;
  private final AtomicBoolean isRecording = new AtomicBoolean(false);
  private ByteArrayOutputStream recordedStream;
  private EditText textOutput;

  private OnnxAsr asr;
  private OnnxTts tts;

  private static final String[] MODEL_URLS = {
    "https://huggingface.co/rhasspy/piper-voices/resolve/main/en/en_US/lessac/medium/en_US-lessac-medium.onnx",
    "https://huggingface.co/rhasspy/piper-voices/resolve/main/en/en_US/lessac/medium/en_US-lessac-medium.json",
    "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-tiny.bin"
  };

  private static final String[] MODEL_NAMES = {
    "en_US-lessac-medium.onnx",
    "en_US-lessac-medium.json",
    "ggml-tiny.bin"
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    backgroundImage = findViewById(R.id.backgroundImage);
    textOutput = findViewById(R.id.textOutput);
    Button recordBtn = findViewById(R.id.recordButton);
    Button toTextBtn = findViewById(R.id.toTextButton);
    Button readTextBtn = findViewById(R.id.readTextButton);

    Button toggleBtn = findViewById(R.id.toggleButton);
    toggleBtn.setOnClickListener(v -> toggleBackground());

    Button exitBtn = findViewById(R.id.exitButton);
    exitBtn.setOnClickListener(v -> {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) finishAndRemoveTask();
      else finish();
    });

    for (int i = 0; i < MODEL_NAMES.length; i++) {
      File file = new File(getFilesDir(), MODEL_NAMES[i]);
      if (!file.exists()) downloadFile(MODEL_URLS[i], file);
    }

    recordBtn.setOnClickListener(v -> toggleRecording());

    toTextBtn.setOnClickListener(v -> {
      if (recordedStream == null) return;
      if (asr == null) {
        asr = new OnnxAsr(getFilesDir() + "/ggml-tiny.bin");
      }
      float[] audioFloat = bytesToFloatArray(recordedStream.toByteArray());
      String text = asr.transcribe(audioFloat);
      textOutput.setText(text);
    });

    readTextBtn.setOnClickListener(v -> {
      if (tts == null) {
        tts = new OnnxTts(getFilesDir() + "/en_US-lessac-medium.onnx", getFilesDir() + "/en_US-lessac-medium.json");
      }
      String text = textOutput.getText().toString();
      short[] pcm = tts.synthesize(text);
      playPCM(pcm);
    });
  }

  private void playPCM(short[] pcm) {
    AudioTrack audioTrack = new AudioTrack(
      AudioManager.STREAM_MUSIC,
      22050,
      AudioFormat.CHANNEL_OUT_MONO,
      AudioFormat.ENCODING_PCM_16BIT,
      pcm.length * 2,
      AudioTrack.MODE_STATIC);
    audioTrack.write(pcm, 0, pcm.length);
    audioTrack.play();
  }

  private float[] bytesToFloatArray(byte[] bytes) {
    float[] result = new float[bytes.length / 2];
    ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    for (int i = 0; i < result.length; i++) {
      result[i] = buffer.getShort() / 32768f;
    }
    return result;
  }

  private void toggleRecording() {
    if (isRecording.get()) {
      isRecording.set(false);
      if (recorder != null) recorder.stop();
    } else {
      int bufSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
      recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO,
                                  AudioFormat.ENCODING_PCM_16BIT, bufSize);
      recordedStream = new ByteArrayOutputStream();
      recorder.startRecording();
      isRecording.set(true);
      recordingThread = new Thread(() -> {
        byte[] buffer = new byte[bufSize];
        while (isRecording.get()) {
          int read = recorder.read(buffer, 0, buffer.length);
          if (read > 0) recordedStream.write(buffer, 0, read);
        }
      });
      recordingThread.start();
    }
  }

  private void downloadFile(String urlStr, File outFile) {
    new Thread(() -> {
      try {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        try (InputStream in = conn.getInputStream();
             FileOutputStream out = new FileOutputStream(outFile)) {
          byte[] buffer = new byte[4096];
          int len;
          while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
          }
        }
        Log.i(TAG, "Downloaded: " + outFile.getName());
      } catch (IOException e) {
        Log.e(TAG, "Download failed: " + urlStr, e);
      }
    }).start();
  }

  private void toggleBackground() {
    isBackgroundVisible = !isBackgroundVisible;
    backgroundImage.setVisibility(isBackgroundVisible ? View.VISIBLE : View.GONE);
  }
}