package com.openyogaland.denis.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

//this class is a View, on which we can draw something
public class CanvasView extends View implements ICanvasView
{
  // константы
  private static int screenWidth;  // ширина экрана
  private static int screenHeight; // высота экрана
  
  // поля
  private Paint  paint;  // "кисточка" для рисования
  private Canvas canvas; // "холст" для рисования
  
  private GameController gameController; // "логика" игры
  private Toast          toast;          // всплывающее сообщение
  
  // constructor
  public CanvasView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    initWidthAndHeight(context); // инициализируем ширину и высоту экрана
    initPaint();                 // инициализируем "кисточку"
    gameController = new GameController(this, screenWidth, screenHeight);
  }
  
  // инициализируем ширину и высоту экрана
  private void initWidthAndHeight(Context context)
  {
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    if(windowManager != null)
    {
      Display display = windowManager.getDefaultDisplay();
      Point   point   = new Point();
      display.getSize(point); // точка приобретает координаты правой нижней точки экрана
      screenWidth  = point.x;
      screenHeight = point.y;
    }
  }
  
  // инициализируем "кисточку"
  private void initPaint()
  {
    paint = new Paint();
    paint.setAntiAlias(true);   // сглаживание
    paint.setStyle(Style.FILL); // заливка
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void showMessage(String text)
  {
    if(toast != null) // если всплывающее сообщение на экране
    {
      toast.cancel();
    }
    toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void redraw()
  {
    invalidate();
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void drawCircle()
  {
  
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void drawRectahgle()
  {
  
  }
}
