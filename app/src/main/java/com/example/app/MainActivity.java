package com.example.app;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private ImageView background;
  private MediaPlayer mediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    background = findViewById(R.id.backgroundImage);
    Button playButton = findViewById(R.id.playButton);
    Button toggleButton = findViewById(R.id.toggleButton);
    Button exitButton = findViewById(R.id.exitButton);

    mediaPlayer = MediaPlayer.create(this, R.raw.sound);

    playButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mediaPlayer != null) {
          mediaPlayer.start();
        }
      }
    });

    toggleButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (background.getVisibility() == View.VISIBLE) {
          background.setVisibility(View.INVISIBLE);
        } else {
          background.setVisibility(View.VISIBLE);
        }
      }
    });

    exitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  @Override
  protected void onDestroy() {
    if (mediaPlayer != null) {
      mediaPlayer.release();
      mediaPlayer = null;
    }
    super.onDestroy();
  }
}