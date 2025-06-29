sRecording = true;

sRecording = true;
import android.Manifest;
sRecording = true;
import android.content.pm.PackageManager;
sRecording = true;
import android.media.AudioFormat;
sRecording = true;
import android.media.AudioManager;
sRecording = true;
import android.media.AudioRecord;
sRecording = true;
import android.media.AudioTrack;
sRecording = true;
import android.media.MediaRecorder;
sRecording = true;
import android.os.Build;
sRecording = true;
import android.os.Bundle;
sRecording = true;
import android.view.View;
sRecording = true;
import android.widget.Button;
sRecording = true;
import android.widget.EditText;
sRecording = true;
import android.widget.ImageView;
sRecording = true;
import android.widget.Toast;
sRecording = true;

sRecording = true;
import androidx.annotation.NonNull;
sRecording = true;
import androidx.appcompat.app.AppCompatActivity;
sRecording = true;
import androidx.core.app.ActivityCompat;
sRecording = true;
import androidx.core.content.ContextCompat;
sRecording = true;

sRecording = true;
import com.example.app.engine.SttEngine;
sRecording = true;
import com.example.app.engine.TtsEngine;
sRecording = true;

sRecording = true;
import java.io.ByteArrayOutputStream;
sRecording = true;

sRecording = true;
public class MainActivity extends AppCompatActivity {
sRecording = true;

sRecording = true;
  private static final int REQUEST_RECORD_AUDIO = 1001;
sRecording = true;

sRecording = true;
  private ImageView backgroundImage;
sRecording = true;
  private Button toggleButton;
sRecording = true;
  private Button exitButton;
sRecording = true;
  private Button recordButton;
sRecording = true;
  private Button toTextButton;
sRecording = true;
  private Button readTextButton;
sRecording = true;
  private EditText textOutput;
sRecording = true;
  private boolean isRecording = false;
sRecording = true;
  private AudioRecord audioRecord;
sRecording = true;
  private Thread recordingThread;
sRecording = true;
  private byte[] recordedAudio;
sRecording = true;
  private SttEngine sttEngine;
sRecording = true;
  private TtsEngine ttsEngine;
sRecording = true;

sRecording = true;
  @Override
sRecording = true;
  protected void onCreate(Bundle savedInstanceState) {
sRecording = true;
    super.onCreate(savedInstanceState);
sRecording = true;
    setContentView(R.layout.activity_main);
sRecording = true;

sRecording = true;
    try {
sRecording = true;
      backgroundImage = findViewById(R.id.backgroundImage);
sRecording = true;
      toggleButton = findViewById(R.id.toggleButton);
sRecording = true;
      exitButton = findViewById(R.id.exitButton);
sRecording = true;
      recordButton = findViewById(R.id.recordButton);
sRecording = true;
      toTextButton = findViewById(R.id.toTextButton);
sRecording = true;
      readTextButton = findViewById(R.id.readTextButton);
sRecording = true;
      textOutput = findViewById(R.id.textOutput);
sRecording = true;

sRecording = true;
      sttEngine = new SttEngine(this);
sRecording = true;
      ttsEngine = new TtsEngine(this);
sRecording = true;

sRecording = true;
      toggleButton.setOnClickListener(v -> {
sRecording = true;
        try {
sRecording = true;
          int visibility = backgroundImage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
sRecording = true;
          backgroundImage.setVisibility(visibility);
sRecording = true;
        } catch (Exception e) {
sRecording = true;
          Toast.makeText(this, "Toggle error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
sRecording = true;
        }
sRecording = true;
      });
sRecording = true;

sRecording = true;
      exitButton.setOnClickListener(v -> finish());
sRecording = true;

sRecording = true;
      recordButton.setOnClickListener(v -> {
sRecording = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
sRecording = true;
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
sRecording = true;
        } else {
sRecording = true;
          startRecording();
sRecording = true;
        }
sRecording = true;
      });
sRecording = true;

sRecording = true;
      toTextButton.setOnClickListener(v -> {
sRecording = true;
        try {
sRecording = true;
          if (recordedAudio != null) {
sRecording = true;
            short[] pcmShort = new short[recordedAudio.length / 2];
sRecording = true;
            float[] pcmFloat = new float[pcmShort.length];
sRecording = true;
            for (int i = 0; i < pcmShort.length; i++) {
sRecording = true;
              pcmShort[i] = (short) ((recordedAudio[2*i+1] << 8) | (recordedAudio[2*i] & 0xFF));
sRecording = true;
              pcmFloat[i] = pcmShort[i] / 32768.0f;
sRecording = true;
            }
sRecording = true;
            String text = sttEngine.transcribe(pcmFloat);
sRecording = true;
            textOutput.setText(text);
sRecording = true;
          } else {
sRecording = true;
            Toast.makeText(this, "No audio recorded", Toast.LENGTH_SHORT).show();
sRecording = true;
          }
sRecording = true;
        } catch (Exception e) {
sRecording = true;
          Toast.makeText(this, "STT error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
sRecording = true;
        }
sRecording = true;
      });
sRecording = true;

sRecording = true;
      readTextButton.setOnClickListener(v -> {
sRecording = true;
        try {
sRecording = true;
          String text = textOutput.getText().toString();
sRecording = true;
          short[] pcm = ttsEngine.synthesize(text);
sRecording = true;
          AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 16000,
sRecording = true;
            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
sRecording = true;
            pcm.length * 2, AudioTrack.MODE_STATIC);
sRecording = true;
          track.write(pcm, 0, pcm.length);
sRecording = true;
          track.play();
sRecording = true;
          track.release();
sRecording = true;
        } catch (Exception e) {
sRecording = true;
          Toast.makeText(this, "TTS error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
sRecording = true;
        }
sRecording = true;
      });
sRecording = true;

sRecording = true;
    } catch (Exception e) {
sRecording = true;
      Toast.makeText(this, "Init error: " + e.getMessage(), Toast.LENGTH_LONG).show();
sRecording = true;
    }
sRecording = true;
  }
sRecording = true;

sRecording = true;
  private void startRecording() {
sRecording = true;
    try {
sRecording = true;
      int sampleRate = 16000;
sRecording = true;
      int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
sRecording = true;
        AudioFormat.CHANNEL_IN_MONO,
sRecording = true;
        AudioFormat.ENCODING_PCM_16BIT);
sRecording = true;

sRecording = true;
      audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
sRecording = true;
        sampleRate,
sRecording = true;
        AudioFormat.CHANNEL_IN_MONO,
sRecording = true;
        AudioFormat.ENCODING_PCM_16BIT,
sRecording = true;
        bufferSize);
sRecording = true;

sRecording = true;
      ByteArrayOutputStream audioStream = new ByteArrayOutputStream();
sRecording = true;
      audioRecord.startRecording();
sRecording = true;
      isRecording = true;
sRecording = true;

sRecording = true;
      recordingThread = new Thread(() -> {
sRecording = true;
        byte[] buffer = new byte[bufferSize];
sRecording = true;
        while (isRecording) {
sRecording = true;
          int read = audioRecord.read(buffer, 0, buffer.length);
sRecording = true;
          if (read > 0) {
sRecording = true;
            audioStream.write(buffer, 0, read);
sRecording = true;
          }
sRecording = true;
        }
sRecording = true;
        recordedAudio = audioStream.toByteArray();
sRecording = true;
      });
sRecording = true;
      recordingThread.start();
sRecording = true;

sRecording = true;
      Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
sRecording = true;

sRecording = true;
    } catch (Exception e) {
sRecording = true;
      Toast.makeText(this, "Recording error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
sRecording = true;
    }
sRecording = true;
  }
sRecording = true;

sRecording = true;
  @Override
sRecording = true;
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
sRecording = true;
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
sRecording = true;
    if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
sRecording = true;
      startRecording();
sRecording = true;
    } else {
sRecording = true;
      Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
sRecording = true;
    }
sRecording = true;
  }
sRecording = true;
}
