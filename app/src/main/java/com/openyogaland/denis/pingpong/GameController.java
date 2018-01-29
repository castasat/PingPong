package com.openyogaland.denis.pingpong;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

// represents Controller of the game and it's logic
class GameController implements Runnable
{
  private int screenWidth;  // ширина экрана
  private int screenHeight; // высота экрана
 
  // parameters
  private int borderMargin  = 35;  // отступ рамки от края экрана
  private int borderWidth   = 5;   // толщина линии рамки стола
  private int racketPadding = 30;  // отступ ракетки от края рамки стола
  private int ballRadius    = 10;  // радиус мяча
  private int ballSlideY    = 2;   // вертикальное смещение мяча
  private int racketWidth   = 20;  // ширина ракетки
  private int racketHeight  = 100; // высота ракетки
  private int racketSpeed   = 10;  // скорость ракетки
  private int ballSpeed     = 30;  // скорость мяча
  private int sleepTime     = 30;  // время приостановки потока в мсек
  private int winningScore  = 21;  // выигрышный счёт
  
  // цвета мяча, игрока и оппонента
  private int ballColor     = Color.WHITE;
  private int playerColor   = Color.BLUE;
  private int opponentColor = Color.RED;
  
  // fields
  private CanvasView     canvasView;        // View, on which we can draw something
  private Ball           ball;              // ping-pong ball
  private OpponentRacket opponentRacket;    // opponent racket
  private PlayerRacket   playerRacket;      // player racket
  private Thread         worker;            // thread
  private int            playerScore;       // счёт игрока
  private int            opponentScore;     // счёт оппонента
  
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
      screenWidth = point.x;
      screenHeight = point.y;
    
      // initializing rackets and ball
      initOpponentRacket();
      initPlayerRacket();
      initBall();
    
      // setting start score
      playerScore = 0;
      opponentScore = 0;
    
      // starting thread
      worker = new Thread(this);
      worker.start();
    }
  }
  
  // ball starts to move
  void playerServe()
  {
    ball.isServed = true;
    // TODO реализовать произвольное смещение при подаче
  }
  
  // opponent racket initialization
  private void initOpponentRacket()
  {
    opponentRacket = new OpponentRacket(screenWidth - borderMargin - borderWidth - racketPadding
        - racketWidth,(screenHeight-racketHeight)/2, racketWidth, racketHeight,
        opponentColor);
  }
  
  // player racket initialization
  private void initPlayerRacket()
  {
    playerRacket = new PlayerRacket(borderMargin + borderWidth + racketPadding,
        (screenHeight-racketHeight)/2, racketWidth, racketHeight, playerColor);
  }
  
  // ping-pong ball initialization
  private void initBall()
  {
    ball = new Ball(borderMargin + borderWidth + racketPadding + racketWidth + ballRadius,
        screenHeight/2, ballRadius, ballColor);
  }
  
  // this method draws game scene on CanvasView
  void onDraw()
  {
    canvasView.drawTable(screenWidth, screenHeight, borderMargin, borderWidth, Color.WHITE);
    canvasView.drawRacket(playerRacket);
    canvasView.drawBall(ball);
    canvasView.drawRacket(opponentRacket);
  }
  
  // this method moves player racket according to screen touch event Y coordinate
  // and moves ball with the player racket if it has not been served yet
  void onTouchEvent(int x, int y)
  {
    // если касание выше (координата Y меньше) верхнего края ракетки
    // и верхний край ракетки ниже (координата Y больше) верхнего края поля
    if(y < playerRacket.getY() && playerRacket.getY() > borderMargin + borderWidth)
    {
      // передвигаем ракетку игрока вверх
      playerRacket.setY(playerRacket.getY() - racketSpeed);
      
      // передвигаем мяч вверх вместе с ракеткой, если он ещё не был подан
      if (!ball.isServed)
      {
        ball.setY(ball.getY() - racketSpeed);
      }
    }
    // иначе если нижний край ракетки игрока выше (координата Y меньше) нижнего края поля
    else if(playerRacket.getY() + racketHeight < screenHeight - borderMargin - borderWidth)
    {
      // передвигаем ракетку игрока вниз
      playerRacket.setY(playerRacket.getY() + racketSpeed);
      
      // передвигаем мяч вниз вместе с ракеткой, если он ещё не был подан
      if(!ball.isServed)
      {
        ball.setY(ball.getY() + racketSpeed);
      }
    }
  }
  
  // method from Runnable interface
  @Override
  public void run()
  {
    while (true)
    {
      try
      {
        Thread.sleep(sleepTime); // задержка
    
        // Ракетку оппонента стараемся переместить напротив мяча
        // если мяч выше середины ракетки оппонента
        if(ball.getY() < opponentRacket.getY() + (opponentRacket.getHeight() / 2) &&
            opponentRacket.getY() > borderMargin + borderWidth)
        {
          // передвигаем ракетку оппонента вверх
          opponentRacket.setY(opponentRacket.getY() - racketSpeed);
        }
        // если мяч ниже середины ракетки оппонента
        if(ball.getY() > opponentRacket.getY() + (opponentRacket.getHeight() / 2) &&
           opponentRacket.getY() + racketHeight < screenHeight - borderMargin - borderWidth)
        {
          // передвигаем ракетку опонента вниз
          opponentRacket.setY(opponentRacket.getY() + racketSpeed);
        }
        
        // Если мяч подан
        if(ball.isServed)
        {
          // устанавливаем смещение мяча по оси Y
          ball.setY(ball.getY() + ballSlideY);
          
          // устанавливаем смещение мяча по оси X
          // если мяч движется вправо и не пересек правую границу стола
          if (!ball.isMovingLeft && ball.getX() < screenWidth - borderMargin - borderWidth)
          {
            ball.setX(ball.getX() + ballSpeed);
            
            // если мяч отбит оппонентом
            if (opponentRacket.isTouching(ball))
            {
              ball.isMovingLeft = true;
            }
            // если мяч пропущен оппонентом
            if (opponentRacket.isMissing(ball))
            {
              // TODO
              playerScore++;
              break;
            }
          }
          // если мяч движется влево и не пересёк левую границу стола
          else if (ball.isMovingLeft && ball.getX() > borderMargin + borderWidth)
          {
            ball.setX(ball.getX() - ballSpeed);
            
            // если мяч отбит игроком
            if (playerRacket.isTouching(ball))
            {
              ball.isMovingLeft = false;
            }
            // если мяч пропущен игроком
            if (playerRacket.isMissing(ball))
            {
              // TODO
              opponentScore++;
              break;
            }
          }
        }
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
      finally
      {
        worker = null;
      }
    }
  }
}
