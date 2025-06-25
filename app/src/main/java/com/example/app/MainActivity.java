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
  private boolean useAltBackground = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    setContentView(layout);

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

    Button toggleBgBtn = new Button(this);
    toggleBgBtn.setText("Toggle Background");
    toggleBgBtn.setOnClickListener(v -> {
      layout.setBackgroundColor(useAltBackground ? 0xFFFFFFFF : 0xFFDDDDDD);
      useAltBackground = !useAltBackground;
    });
    layout.addView(toggleBgBtn);

    Button exitBtn = new Button(this);
    exitBtn.setText("Exit");
    exitBtn.setOnClickListener(v -> finishAffinity());
    layout.addView(exitBtn);
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