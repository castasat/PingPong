package com.openyogaland.denis.pingpong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;

//this class is a View, on which we can draw something
public class CanvasView extends ViewCompat implements ICanvasView
{
  // константы
  private static int screenWidth;  // ширина экрана
  private static int screenHeight; // высота экрана
  
  // поля
  private Paint  paint;  // "кисточка" для рисования
  private Canvas canvas; // "холст" для рисования
}
