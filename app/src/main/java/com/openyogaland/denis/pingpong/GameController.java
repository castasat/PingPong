package com.openyogaland.denis.pingpong;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

// represents Controller of the game and it's logic
class GameController implements Runnable
{
  // parameters
  private int minBallSpeed  = 10;  // минимальная скорость мяча
  private int maxBallSpeed  = 60;  // максимальная скорость мяча
  private int minBallSlideY = -20; // минимальное отклонение мяча по оси Y
  private int maxBallSlideY = 20;  // максимальное отклонение мяча по оси Y
  private int sleepTime     = 30;  // время приостановки потока в мсек
  private int winningScore  = 21;  // выигрышный счёт
  
  // цвета мяча, игрока и оппонента
  private int ballColor     = Color.WHITE;
  private int playerColor   = Color.BLUE;
  private int opponentColor = Color.RED;
  
  // fields
  private CanvasView canvasView;        // View, on which we can draw something
  private Table      table;             // ping-pong game table
  private Ball       ball;              // ping-pong ball
  private Racket     opponentRacket;    // opponent racket
  private Racket     playerRacket;      // player racket
  private Thread     worker;            // thread
  private int        playerScore;       // счёт игрока
  private int        opponentScore;     // счёт оппонента
  
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
      table = new Table(point.x, point.y);
    
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
    if (!ball.isServed)
    {
      ball.isServed = true;
      // задаём произвольную скорость мяча и произвольное смещение мяча по оси Y
      ball.setSpeedX(getRandomInt(minBallSpeed, maxBallSpeed));
      ball.setSpeedY(getRandomInt(minBallSlideY, maxBallSlideY));
    }
  }
  
  // returns random int value between minInt and maxInt values
  private int getRandomInt(int minInt, int maxInt)
  {
    return (int) (minInt + Math.round(Math.random() * (maxInt-minInt)));
  }
  
  // opponent racket initialization
  private void initOpponentRacket()
  {
    opponentRacket = new Racket(table.getInitOpponentRacketPosition(), opponentColor);
  }
  
  // player racket initialization
  private void initPlayerRacket()
  {
    playerRacket = new Racket(table.getInitPlayerRacketPosition(), playerColor);
  }
  
  // ping-pong ball initialization
  private void initBall()
  {
    ball = new Ball(table.getInitBallPosition(), ballColor);
  }
  
  // this method draws game scene on CanvasView
  void onDraw()
  {
    canvasView.drawTable(table.getScreenWidth(), table.getScreenHeight(), Table.BORDER_MARGIN,
        Table.BORDER_WIDTH, Color.WHITE);
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
    if(y < playerRacket.getY() && playerRacket.getY() > Table.BORDER_MARGIN + Table.BORDER_WIDTH)
    {
      // передвигаем ракетку игрока вверх
      playerRacket.setY(playerRacket.getY() - Racket.RACKET_SPEED);
      
      // передвигаем мяч вверх вместе с ракеткой, если он ещё не был подан
      if (!ball.isServed)
      {
        ball.setY(ball.getY() - Racket.RACKET_SPEED);
      }
    }
    // иначе если нижний край ракетки игрока выше (координата Y меньше) нижнего края поля
    else if(playerRacket.getY() + Racket.RACKET_HEIGHT < table.getScreenHeight() - Table
        .BORDER_MARGIN - Table.BORDER_WIDTH)
    {
      // передвигаем ракетку игрока вниз
      playerRacket.setY(playerRacket.getY() + Racket.RACKET_SPEED);
      
      // передвигаем мяч вниз вместе с ракеткой, если он ещё не был подан
      if(!ball.isServed)
      {
        ball.setY(ball.getY() + Racket.RACKET_SPEED);
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
        if(ball.getY() < opponentRacket.getY() + (Racket.RACKET_HEIGHT / 2) &&
            opponentRacket.getY() > Table.BORDER_MARGIN + Table.BORDER_WIDTH)
        {
          // передвигаем ракетку оппонента вверх
          opponentRacket.setY(opponentRacket.getY() - Racket.RACKET_SPEED);
        }
        // если мяч ниже середины ракетки оппонента
        if(ball.getY() > opponentRacket.getY() + (Racket.RACKET_HEIGHT / 2) &&
           opponentRacket.getY() + Racket.RACKET_HEIGHT < table.getScreenHeight() -
           Table.BORDER_MARGIN - Table.BORDER_WIDTH)
        {
          // передвигаем ракетку опонента вниз
          opponentRacket.setY(opponentRacket.getY() + Racket.RACKET_SPEED);
        }
        
        // Если мяч подан
        if(ball.isServed)
        {
          // устанавливаем смещение мяча по оси Y в пределах стола
          if (ball.getY() - Ball.BALL_RADIUS > Table.BORDER_MARGIN + Table.BORDER_WIDTH && ball.getY() +
              Ball.BALL_RADIUS < table.getScreenHeight() - Table.BORDER_MARGIN - Table.BORDER_WIDTH)
          {
            ball.setY(ball.getY() + ball.getSpeedY());
          }
          
          // устанавливаем смещение мяча по оси X
          // если мяч движется вправо и не пересек правую границу стола
          if (!ball.isMovingLeft && ball.getX() + Ball.BALL_RADIUS < table.getScreenWidth() -
              Table.BORDER_MARGIN - Table.BORDER_WIDTH)
          {
            ball.setX(ball.getX() + ball.getSpeedX());
            
            // если мяч отбит оппонентом
            if (opponentRacket.isTouching(ball))
            {
              ball.isMovingLeft = true;
              // устанавливаем новую скорость мяча
              ball.setSpeedX(getRandomInt(minBallSpeed, maxBallSpeed));
              // устанавливаем новое смещение мяча
              ball.setSpeedY(getRandomInt(minBallSlideY, maxBallSlideY));
            }
            // TODO если мяч пропущен оппонентом
          }
          // если мяч движется влево и не пересёк левую границу стола
          else if (ball.isMovingLeft && ball.getX() - Ball.BALL_RADIUS > Table.BORDER_MARGIN +
                   Table.BORDER_WIDTH)
          {
            ball.setX(ball.getX() - ball.getSpeedX());
            
            // если мяч отбит игроком
            if (playerRacket.isTouching(ball))
            {
              ball.isMovingLeft = false;
              // устанавливаем новую скорость мяча
              ball.setSpeedX(getRandomInt(minBallSpeed, maxBallSpeed));
              // устанавливаем новое смещение мяча
              ball.setSpeedY(getRandomInt(minBallSlideY, maxBallSlideY));
            }
            // TODO если мяч пропущен игроком
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