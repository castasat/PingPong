package com.openyogaland.denis.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

// this class is a View, on which we can draw something
public class CanvasView extends View implements ICanvasView
{
  // поля
  private Paint  paint;  // "кисточка" для рисования
  private Canvas canvas; // "холст" для рисования
  
  private GameController gameController; // "логика" игры
  private Toast          toast;          // всплывающее сообщение
  
  // constructor
  public CanvasView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    initPaint(); // инициализируем "кисточку"
    gameController = new GameController(this, context);
  }
  
  // инициализируем "кисточку"
  private void initPaint()
  {
    paint = new Paint();
    paint.setAntiAlias(true); // сглаживание
  }
  
  // method onDraw()
  @Override
  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    this.canvas = canvas;
    gameController.onDraw();
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void redraw()
  {
    invalidate();
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void drawTable(int screenWidth, int screenHeight, int borderMargin, int borderWidth, int color)
  {
    paint.setStyle(Style.STROKE); // обводка
    paint.setStrokeWidth(borderWidth);
    paint.setColor(color);
    canvas.drawRect(borderMargin, borderMargin, (screenWidth - borderMargin),
        (screenHeight - borderMargin), paint);
    canvas.drawLine(screenWidth / 2, borderMargin, screenWidth / 2, (screenHeight - borderMargin),
        paint);
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void drawRacket(Racket racket)
  {
    paint.setStyle(Style.FILL); // заполнение
    paint.setColor(racket.getColor());
    canvas.drawRect(racket.getRect(), paint);
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void drawBall(Ball ball)
  {
    paint.setStyle(Style.FILL); // заполнение
    paint.setColor(ball.getColor());
    canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), paint);
  }
  
  // метод интерфейса ICanvasView
  @Override public void showMessage(String text)
  {
    if(toast != null) // если всплывающее сообщение на экране
    {
      toast.cancel();
    }
    toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }
}
