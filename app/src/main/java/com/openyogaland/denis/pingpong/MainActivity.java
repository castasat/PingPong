package com.openyogaland.denis.pingpong;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class MainActivity extends AppCompatActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    // прячем window title
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    
    // прячем action bar
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
    {
      actionBar.hide();
    }
    
    // применяем XML layout-файл
    setContentView(R.layout.activity_main);
  }
}
