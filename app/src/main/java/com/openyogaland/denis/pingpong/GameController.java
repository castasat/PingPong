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
      // задаём направление смещения мяча по оси X установкой флага
      ball.isMovingLeft = getRandomInt(0, 1) != 0;
      // устанавливаем флаг, что мяч движется вверх
      ball.isMovingUp = true;
      // устанавливаем флаг, что мяч подан
      ball.isServed = true;
      
      // задаём случайную скорость мяча по координатам X и Y из интервала
      ball.setSpeedY(getRandomInt(Ball.MIN_SPEED, Ball.MAX_SPEED));
      ball.setSpeedX(getRandomInt(Ball.MIN_SPEED, Ball.MAX_SPEED));
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

    // устанавливаем случайно флаг направления начального движения ракетки оппонента
    opponentRacket.isMovingLeft = getRandomInt(0, 1) == 0;
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
  
  // this method moves player racket according to screen touch event X coordinate
  // and moves ball with the player racket if it has not been served yet
  void onTouchEvent(int x, int y)
  {
    // если касание левее (координата X меньше) середины ракетки
    // и левый край ракетки правее (координата X больше) левого края стола
    if(x < playerRacket.getCenterX() && playerRacket.getLeft() >= table.getLeftBorderX())
    {
      // передвигаем ракетку игрока влево
      playerRacket.setX(playerRacket.getX() - Racket.RACKET_SPEED);
      
      // передвигаем мяч влево вместе с ракеткой, если он ещё не был подан
      if (!ball.isServed)
      {
        ball.setX(ball.getX() - Racket.RACKET_SPEED);
      }
    }
    // иначе если касание правее (координата X больше) середины ракетки
    // и правый край ракетки левее (координата X меньше) правого края стола
    else if(x >= playerRacket.getCenterX() && playerRacket.getRight() <= table.getRightBorderX())
    {
      // передвигаем ракетку игрока вправо
      playerRacket.setX(playerRacket.getX() + Racket.RACKET_SPEED);
      
      // передвигаем мяч вправо вместе с ракеткой, если он ещё не был подан
      if(!ball.isServed)
      {
        ball.setX(ball.getX() + Racket.RACKET_SPEED);
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

        // Передвигаем ракетку вправо или влево в зависимости от значения флага
        if (opponentRacket.isMovingLeft)
        {
          // перемещаем ракетку оппонента влево
          opponentRacket.setX(opponentRacket.getX() - Racket.RACKET_SPEED);
        }
        else
        {
          // перемещаем ракетку оппонента вправо
          opponentRacket.setX(opponentRacket.getX() + Racket.RACKET_SPEED);
        }
        
        // Если ракетка оппонента достигла левой границы стола
        if(opponentRacket.getLeft() <= table.getLeftBorderX())
        {
          opponentRacket.isMovingLeft = false;
        }
        // если ракетка достигла правой границы стола
        else if(opponentRacket.getRight() >= table.getRightBorderX())
        {
          opponentRacket.isMovingLeft = true;
        }
        
        // Если мяч подан
        if(ball.isServed)
        {
          // устанавливаем смещение мяча по оси Y в пределах стола
          if (ball.getY() - Ball.RADIUS > Table.BORDER_MARGIN + Table.BORDER_WIDTH && ball.getY() +
              Ball.RADIUS < table.getScreenHeight() - Table.BORDER_MARGIN - Table.BORDER_WIDTH)
          {
            ball.setY(ball.getY() + ball.getSpeedY());
          }
          
          // устанавливаем смещение мяча по оси X
          // если мяч движется вправо и не пересек правую границу стола
          if (!ball.isMovingLeft && ball.getX() + Ball.RADIUS < table.getScreenWidth() -
              Table.BORDER_MARGIN - Table.BORDER_WIDTH)
          {
            ball.setX(ball.getX() + ball.getSpeedX());
            
            // если мяч отбит оппонентом
            if (opponentRacket.isTouching(ball))
            {
              ball.isMovingLeft = true;
              // устанавливаем новую скорость мяча по осям X и Y
              ball.setSpeedX(getRandomInt(Ball.MIN_SPEED, Ball.MAX_SPEED));
              ball.setSpeedY(getRandomInt(Ball.MIN_SPEED, Ball.MAX_SPEED));
            }
            // TODO если мяч пропущен оппонентом
          }
          // если мяч движется влево и не пересёк левую границу стола
          else if (ball.isMovingLeft && ball.getX() - Ball.RADIUS > Table.BORDER_MARGIN +
                   Table.BORDER_WIDTH)
          {
            ball.setX(ball.getX() - ball.getSpeedX());
            
            // если мяч отбит игроком
            if (playerRacket.isTouching(ball))
            {
              ball.isMovingLeft = false;
              // устанавливаем новую скорость мяча по осям X и Y
              ball.setSpeedX(getRandomInt(Ball.MIN_SPEED, Ball.MAX_SPEED));
              ball.setSpeedY(getRandomInt(Ball.MIN_SPEED, Ball.MAX_SPEED));
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