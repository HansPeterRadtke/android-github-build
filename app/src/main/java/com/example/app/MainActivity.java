package com.example.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.engine.Any;
import com.example.app.engine.TtsEngine;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}