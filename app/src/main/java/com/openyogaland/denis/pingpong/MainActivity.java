package com.openyogaland.denis.pingpong;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class MainActivity extends AppCompatActivity
{
  // fields
  CanvasView canvasView;
  
  // вызывается при создании главной активости
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
    
    // находим canvasView по id
    canvasView = findViewById(R.id.canvas_view);
    
    // применяем XML layout-файл
    setContentView(R.layout.activity_main);
  }
  
  // очищаем память от всех обработчиков сообщений и сообщений, чтобы не держали ссылок
  @Override
  protected void onDestroy()
  {
    boolean retry = true;
    if(canvasView != null)
    {
      canvasView.gameController.setRunning(false);
      while (retry)
      {
        try
        {
          canvasView.gameController.worker.join();
          if(canvasView.handler != null)
          {
            canvasView.handler.removeCallbacksAndMessages(null);
          }
          retry = false;
        }
        catch(InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    }
    super.onDestroy();
  }
}
