package com.example.app;

import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
  private MediaPlayer mediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);

    AssetManager assetManager = getAssets();
    try {
      String[] files = assetManager.list("");
      if (files != null) {
        for (String file : files) {
          if (file.endsWith(".mp3")) {
            Button btn = new Button(this);
            btn.setText(file);
            btn.setOnClickListener(v -> playAudio(file));
            layout.addView(btn);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    LinearLayout rootLayout = findViewById(android.R.id.content);
    rootLayout.addView(layout);

    Button exitBtn = findViewById(R.id.exitButton);
    if (exitBtn != null) {
      exitBtn.setOnClickListener(v -> finishAffinity());
    }
  }

  private void playAudio(String filename) {
    try {
      if (mediaPlayer != null) {
        mediaPlayer.stop();
        mediaPlayer.release();
      }
      AssetManager assetManager = getAssets();
      mediaPlayer = new MediaPlayer();
      mediaPlayer.setDataSource(assetManager.openFd(filename).getFileDescriptor(),
        assetManager.openFd(filename).getStartOffset(),
        assetManager.openFd(filename).getLength());
      mediaPlayer.prepare();
      mediaPlayer.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}