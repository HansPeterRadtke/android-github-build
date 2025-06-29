
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.app.engine.SttEngine;
import com.example.app.engine.TtsEngine;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

  private static final int REQUEST_RECORD_AUDIO = 1001;

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        } else {
          startRecording();
  }

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
          track.release();
        } catch (Exception e) {
          Toast.makeText(this, "TTS error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

    } catch (Exception e) {
      Toast.makeText(this, "Init error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }


    try {
      int sampleRate = 16000;
      int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT);

      audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize);

      ByteArrayOutputStream audioStream = new ByteArrayOutputStream();
      audioRecord.startRecording();
  }


      recordingThread = new Thread(() -> {
        byte[] buffer = new byte[bufferSize];
        while (isRecording) {
          int read = audioRecord.read(buffer, 0, buffer.length);
          if (read > 0) {
            audioStream.write(buffer, 0, read);
          }
        }
        recordedAudio = audioStream.toByteArray();
      });
      recordingThread.start();

      Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();

    } catch (Exception e) {
      Toast.makeText(this, "Recording error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      startRecording();
  }

    } else {
      Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
    }
  }
  private void stopRecording() {
    isRecording = false;
    if (audioRecord != null) {
      audioRecord.stop();
      audioRecord.release();
      audioRecord = null;
    }
  }

}
  private void startRecording() {
    try {
      int sampleRate = 16000;
      int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT);

      audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize);

      ByteArrayOutputStream audioStream = new ByteArrayOutputStream();
      isRecording = true;
      audioRecord.startRecording();

      recordingThread = new Thread(() -> {
        byte[] buffer = new byte[bufferSize];
        while (isRecording) {
          int read = audioRecord.read(buffer, 0, buffer.length);
          if (read > 0) {
            audioStream.write(buffer, 0, read);
          }
        }
        recordedAudio = audioStream.toByteArray();
      });
      recordingThread.start();

      Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
      Toast.makeText(this, "Recording error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }
