package com.openyogaland.denis.pingpong;

import android.graphics.Point;

// Interface to work with from GameManager
interface ICanvasView
{
  void drawTable(int screenWidth, int screenHeight, int borderMargin, int borderWidth, int color);
  
  void drawRacket(Racket racket);
  
  void drawBall(Ball ball);
  
  void showMessage(String text);
  
  void drawScore(String text, Point position, int color);
  
  void redraw();
}
