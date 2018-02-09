package com.openyogaland.denis.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

// this class is a View, on which we can draw something
public class CanvasView extends View implements ICanvasView
{
  // поля
  private Paint  paint;  // "кисточка" для рисования
  Canvas canvas; // "холст" для рисования
  
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
    canvas.drawLine(borderMargin, screenHeight / 2, screenWidth - borderMargin, screenHeight / 2,
        paint);
    redraw();
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void drawRacket(Racket racket)
  {
    paint.setStyle(Style.FILL); // заполнение
    paint.setColor(racket.getColor());
    canvas.drawRect(racket.getLeft(), racket.getTop(), racket.getRight(), racket.getBottom(), paint);
    redraw();
  }
  
  // метод интерфейса ICanvasView
  @Override
  public void drawBall(Ball ball)
  {
    paint.setStyle(Style.FILL); // заполнение
    paint.setColor(ball.getColor());
    canvas.drawCircle(ball.getX(), ball.getY(), Ball.BALL_RADIUS, paint);
    redraw();
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
    redraw();
  }
  
  // Метод для обработки прикосновения к экрану
  @Override
  public boolean onTouchEvent(MotionEvent motionEvent)
  {
    // получаем координаты касания
    int x = (int) motionEvent.getX();
    int y = (int) motionEvent.getY();
    
    if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
    {
      gameController.onTouchEvent(x, y);
      redraw(); // нужно, чтобы у объекта View обновилось положение
    }
    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
    {
      performClick();
      redraw();
    }
    if(motionEvent.getAction() == MotionEvent.ACTION_UP)
    {
      gameController.playerServe();
      redraw();
    }
    return true;
  }
  
  // for accessibility
  @Override
  public boolean performClick()
  {
    return super.performClick();
  }
}