package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

  private ImageView backgroundImage;
  private Button toggleButton;
  private Button exitButton;
  private Button recordButton;
  private Button toTextButton;
  private Button readTextButton;
  private EditText textOutput;

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

      toggleButton.setOnClickListener(v -> {
        try {
          int visibility = backgroundImage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
          backgroundImage.setVisibility(visibility);
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(this, "Toggle error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

      exitButton.setOnClickListener(v -> {
        try {
          finish();
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(this, "Exit error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });

      recordButton.setOnClickListener(v -> {
        try {
          Toast.makeText(this, "Recording started (not implemented)", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      toTextButton.setOnClickListener(v -> {
        try {
          textOutput.setText("Simulated speech-to-text");
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      readTextButton.setOnClickListener(v -> {
        try {
          String text = textOutput.getText().toString();
          Toast.makeText(this, "Reading text: " + text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(this, "Init error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }
}