package com.openyogaland.denis.pingpong;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

// represents Controller of the game and it's logic
class GameController
{
  private int screenWidth;  // ширина экрана
  private int screenHeight; // высота экрана
 
  // parameters
  private int borderMargin  = 35;  // отступ рамки от края экрана
  private int borderWidth   = 5;   // толщина линии рамки стола
  private int racketPadding = 30;  // отступ ракетки от края рамки стола
  private int ballRadius    = 15;  // радиус мяча
  private int racketWidth   = 30;  // ширина ракетки
  private int racketHeight  = 210; // высота ракетки
  
  // fields
  private CanvasView     canvasView;     // View, on which we can draw something
  private Ball           ball;           // ping-pong ball
  private OpponentRacket opponentRacket; // opponent racket
  private PlayerRacket   playerRacket;   // player racket
  
  // constructor
  GameController(CanvasView canvasView, Context context)
  {
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    if(windowManager != null)
    {
      this.canvasView = canvasView;
      Display display = windowManager.getDefaultDisplay();
      Point   point   = new Point();
      display.getSize(point); // точка приобретает координаты правой нижней точки экрана
      
      // set screenWidth and screenHeight
      screenWidth  = point.x;
      screenHeight = point.y;
      
      initOpponentRacket();
      initPlayerRacket();
      initBall();
    }
  }
  
  // opponent racket initialization
  private void initOpponentRacket()
  {
    opponentRacket = new OpponentRacket(screenWidth - borderMargin - borderWidth - racketPadding
        - racketWidth,(screenHeight-racketHeight)/2, racketWidth, racketHeight, Color.YELLOW);
  }
  
  // player racket initialization
  private void initPlayerRacket()
  {
    playerRacket = new PlayerRacket(borderMargin + racketPadding, (screenHeight-racketHeight)/2,
        racketWidth, racketHeight, Color.BLUE);
  }
  
  // ping-pong ball initialization
  private void initBall()
  {
    ball = new Ball(borderMargin + racketPadding + racketWidth + ballRadius,
        screenHeight/2, ballRadius, Color.WHITE);
  }
  
  // This method draws game scene on CanvasView
  void onDraw()
  {
    canvasView.drawTable(screenWidth, screenHeight, borderMargin, borderWidth, Color.WHITE);
    canvasView.drawRacket(opponentRacket);
    canvasView.drawRacket(playerRacket);
    canvasView.drawBall(ball);
  }
  
  // TODO
  void onTouchEvent(int x, int y)
  {
  }
}
