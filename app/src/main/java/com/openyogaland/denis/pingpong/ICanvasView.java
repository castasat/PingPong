package com.openyogaland.denis.pingpong;

// Interface to work with from GameManager
interface ICanvasView
{
  void drawCircle();
  
  void drawRectahgle();
  
  void showMessage(String text);
  
  void redraw();
}
