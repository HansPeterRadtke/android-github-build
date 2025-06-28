package com.example.app;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.app.engine.SttEngine;
import com.example.app.engine.TtsEngine;

public class MainActivity extends Activity {

  private ImageView backgroundImage;
  private Button toggleButton;
  private Button exitButton;
  private Button recordButton;
  private Button toTextButton;
  private Button readTextButton;
  private EditText textOutput;
  private boolean isRecording = false;
  private AudioRecord audioRecord;
  private Thread recordingThread;
  private byte[] recordedAudio;
  private SttEngine sttEngine;
  private TtsEngine ttsEngine;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    try {
      backgroundImage = findViewById(R.id.backgroundImage);
      toggleButton = findViewById(R.id.toggleButton);
      exitButton = findViewById(R.id.exitButton);
      recordButton = findViewById(R.id.recordButton);
      toTextButton = findViewById(R.id.toTextButton);
      readTextButton = findViewById(R.id.readTextButton);
      textOutput = findViewById(R.id.textOutput);

      sttEngine = new SttEngine(this);
      ttsEngine = new TtsEngine(this);

      toggleButton.setOnClickListener(v -> {
        try {
          int visibility = backgroundImage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
          backgroundImage.setVisibility(visibility);
        } catch (Exception e) {
          Toast.makeText(this, "Toggle error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

      exitButton.setOnClickListener(v -> finish());

      recordButton.setOnClickListener(v -> {
        try {
          if (!isRecording) {
            int sampleRate = 16000;
            int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
              AudioFormat.CHANNEL_IN_MONO,
              AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
              sampleRate,
              AudioFormat.CHANNEL_IN_MONO,
              AudioFormat.ENCODING_PCM_16BIT,
              bufferSize);
            recordedAudio = new byte[bufferSize * 10];
            audioRecord.startRecording();
            isRecording = true;
            recordingThread = new Thread(() -> {
              int offset = 0;
              while (isRecording && offset < recordedAudio.length) {
                int read = audioRecord.read(recordedAudio, offset, bufferSize);
                if (read > 0) offset += read;
              }
            });
            recordingThread.start();
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
          } else {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            recordingThread = null;
            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
          }
        } catch (Exception e) {
          Toast.makeText(this, "Recording error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

      toTextButton.setOnClickListener(v -> {
        try {
          if (recordedAudio != null) {
            short[] pcmShort = new short[recordedAudio.length / 2];
            float[] pcmFloat = new float[pcmShort.length];
            for (int i = 0; i < pcmShort.length; i++) {
              pcmShort[i] = (short) ((recordedAudio[2*i+1] << 8) | (recordedAudio[2*i] & 0xFF));
              pcmFloat[i] = pcmShort[i] / 32768.0f;
            }
            String text = sttEngine.transcribe(pcmFloat);
            textOutput.setText(text);
          } else {
            Toast.makeText(this, "No audio recorded", Toast.LENGTH_SHORT).show();
          }
        } catch (Exception e) {
          Toast.makeText(this, "STT error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

      readTextButton.setOnClickListener(v -> {
        try {
          String text = textOutput.getText().toString();
          short[] pcm = ttsEngine.synthesize(text);
          AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 16000,
            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
            pcm.length * 2, AudioTrack.MODE_STATIC);
          track.write(pcm, 0, pcm.length);
          track.play();
        } catch (Exception e) {
          Toast.makeText(this, "TTS error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });
    } catch (Exception e) {
      Toast.makeText(this, "Init error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }
}