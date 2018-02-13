package com.openyogaland.denis.pingpong;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

// represents Controller of the game and it's logic
class GameController implements Runnable
{
  // constants
  private final static int PLAYER_COLOR   = Color.BLUE;
  private final static int OPPONENT_COLOR = Color.RED;
          final static int WHITE_COLOR    = Color.WHITE;
          final static int PLAYER_WINS    = 0;
          final static int OPPONENT_WINS  = 1;
  
  // fields
  private CanvasView canvasView;        // View, on which we can draw something
  private Table      table;             // ping-pong game table
  private Ball       ball;              // ping-pong ball
  private Racket     opponentRacket;    // opponent racket
  private Racket     playerRacket;      // player racket
          Thread     worker;            // thread
  private int        playerScore;       // счёт игрока
  private int        opponentScore;     // счёт оппонента
  
  private boolean    running = false;   // флаг запущенного потока
  
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
      setRunning(true);
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
      ball.setSpeedX(getRandomInt(Ball.MIN_SPEED / 2, Ball.MAX_SPEED / 2));
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
    opponentRacket = new Racket(table.getInitOpponentRacketPosition(), OPPONENT_COLOR);

    // устанавливаем случайно флаг направления начального движения ракетки оппонента
    opponentRacket.isMovingLeft = getRandomInt(0, 1) == 0;
  }
  
  // player racket initialization
  private void initPlayerRacket()
  {
    playerRacket = new Racket(table.getInitPlayerRacketPosition(), PLAYER_COLOR);
  }
  
  // ping-pong ball initialization
  private void initBall()
  {
    ball = new Ball(table.getInitBallPosition(), WHITE_COLOR);
  }
  
  // this method draws game scene on CanvasView
  void onDraw()
  {
    canvasView.drawTable(table.getScreenWidth(), table.getScreenHeight(), Table.BORDER_MARGIN,
        Table.BORDER_WIDTH, WHITE_COLOR);
    // отобразить счёт игрока
    canvasView
        .drawScore(String.valueOf(playerScore), table.getPlayerScoreTextPosition(), PLAYER_COLOR);
    // отобразить счёт оппонента
    canvasView.drawScore(String.valueOf(opponentScore), table.getOpponentScoreTextPosition(),
        OPPONENT_COLOR);
    canvasView.drawRacket(playerRacket);
    canvasView.drawBall(ball);
    canvasView.drawRacket(opponentRacket);
  }
  
  // this method moves player racket according to screen touch event X coordinate
  // and moves ball with the player racket if it has not been served yet
  void onTouchEvent(int x)
  {
    // если касание левее левого края ракетки игрока
    // и левый край ракетки правее (координата X больше) левого края стола
    if(x < playerRacket.getLeft() && playerRacket.getLeft() >= table.getLeftBorderX())
    {
      // передвигаем ракетку игрока влево
      playerRacket.setX(playerRacket.getX() - Racket.RACKET_SPEED);
      
      // передвигаем мяч влево вместе с ракеткой, если он ещё не был подан
      if (!ball.isServed)
      {
        ball.setX(ball.getX() - Racket.RACKET_SPEED);
      }
    }
    // иначе если касание правее правого края ракетки игрока
    // и правый край ракетки левее (координата X меньше) правого края стола
    else if(x >= playerRacket.getRight() && playerRacket.getRight() <= table.getRightBorderX())
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
    while (running)
    {
      try
      {
        // задержка
        int sleepTime = 30;
        Thread.sleep(sleepTime);
  
        // Передвигаем ракетку оппонента вправо или влево в зависимости от значения флага
        if(opponentRacket.isMovingLeft)
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
          // она начинает двигаться вправо
          opponentRacket.isMovingLeft = false;
        }
        // если ракетка достигла правой границы стола
        else if(opponentRacket.getRight() >= table.getRightBorderX())
        {
          // она начинает двигаться влево
          opponentRacket.isMovingLeft = true;
        }
  
        // Если мяч подан
        else if(ball.isServed)
        {
          // Передвигаем ракетку оппонента вслед за мячом, чтобы отбить его
          // если левая граница ракетки оппонента правее мяча
          if(ball.getRight() < opponentRacket.getLeft())
          {
            // она начинает двигаться влево
            opponentRacket.isMovingLeft = true;
          }
          // если правая граница ракетки оппонента левее мяча
          else if(ball.getLeft() > opponentRacket.getRight())
          {
            // она начинает двигаться вправо
            opponentRacket.isMovingLeft = false;
          }
          
          // Задаём мячу смещение по оси X
          if(ball.isMovingLeft)
          {
            // если мяч движется влево
            ball.setX(ball.getX() - ball.getSpeedX());
            // и достиг левого края стола
            if (ball.getLeft() <= table.getLeftBorderX())
            {
              ball.bounceFromVertical(); // мяч отскакивает от края стола
            }
          }
          else
          {
            // если мяч движется право
            ball.setX(ball.getX() + ball.getSpeedX());
            // и достиг правого края стола
            if (ball.getRight() >= table.getRightBorderX())
            {
              ball.bounceFromVertical(); // мяч отскакивает от края стола
            }
          }
          
          // Задаём мячу смещение по оси Y
          int winningScore = 21;
          if(ball.isMovingUp)
          {
            // если мяч движется вверх
            ball.setY(ball.getY() - ball.getSpeedY());
            // и сталкивается с ракеткой оппонента
            if (opponentRacket.isTouching(ball))
            {
              ball.bounceFromHorizontal(); // мяч отскакивает от ракетки оппонента
              // задаём случайную скорость мяча по координатам X и Y из интервала
              ball.setSpeedY(getRandomInt(Ball.MIN_SPEED, Ball.MAX_SPEED));
              ball.setSpeedX(getRandomInt(Ball.MIN_SPEED / 2, Ball.MAX_SPEED / 2));
            }
            // если не отбит ракеткой оппонента
            else if (ball.getTop() <= 0)
            {
              // оппонент проигрывает раунд
              playerScore++;
              // проверяем условие победы игрока
              if (playerScore >= winningScore)
              {
                // завершение игры
                canvasView.handler.sendEmptyMessage(PLAYER_WINS);
                gameOver();
              }
              else
              {
                // если игра не окончена, стартуем следующий раунд
                newRound();
              }
            }
          }
          else
          {
            // если мяч движется вниз
            ball.setY(ball.getY() + ball.getSpeedY());
            // и сталкивается с ракеткой игрока
            if (playerRacket.isTouching(ball))
            {
              ball.bounceFromHorizontal(); // мяч отскакивает от ракетки игрока
              // задаём случайную скорость мяча по координатам X и Y из интервала
              ball.setSpeedY(getRandomInt(Ball.MIN_SPEED, Ball.MAX_SPEED));
              ball.setSpeedX(getRandomInt(Ball.MIN_SPEED / 2, Ball.MAX_SPEED / 2));
            }
            else if (ball.getBottom() >= table.getScreenHeight())
            {
              // игрок проигрывает раунд
              opponentScore++;
              // проверяем условие победы оппонента
              if(opponentScore >= winningScore)
              {
                // завершение игры
                canvasView.handler.sendEmptyMessage(OPPONENT_WINS);
                gameOver();
              }
              else
              {
                // если игра не окончена, стартуем следующий раунд
                newRound();
              }
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
  
  // stat running cycle
  void setRunning(boolean running)
  {
    this.running = running;
  }
  
  // reinitialize ball and scores at the end of the game
  private void gameOver()
  {
    playerScore   = 0;
    opponentScore = 0;
    newRound();
  }
  
  // начинаем новый раунд игры, для чего
  private void newRound()
  {
    // реинициализируем мяч
    ball.isServed = false;
    ball.setX(playerRacket.getCenterX());
    ball.setY(table.getInitBallPosition().y);
  }
}