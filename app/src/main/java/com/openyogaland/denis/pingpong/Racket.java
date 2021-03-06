package com.openyogaland.denis.pingpong;

import android.graphics.Point;

// This class represents a common ping-pong racket
class Racket
{
  // constants
  final static int RACKET_HEIGHT  = 20;
  final static int RACKET_WIDTH   = 100;
  final static int RACKET_PADDING = 30; // расстояние от рамки стола до ракетки
  final static int RACKET_SPEED   = 15;
  
  // fields
  private int x;
  private int y;
  private int color;
  
  // flags
  boolean isMovingLeft = false;
  
  // default constructor
  private Racket(int x, int y, int color)
  {
    this.x = x;
    this.y = y;
    this.color  = color;
  }
  
  // alternative constructor
  Racket(Point point, int color)
  {
    this(point.x, point.y, color);
  }
  
  // setter setX()
  void setX(int x)
  {
    this.x = x;
  }
  
  // getter getX()
  int getX()
  {
    return x;
  }

  // getter getColor()
  int getColor()
  {
    return color;
  }
  
  // getters to draw a racket
  int getLeft()
  {
    return x;
  }
  int getTop()
  {
    return y;
  }
  int getRight()
  {
    return x + RACKET_WIDTH;
  }
  int getBottom()
  {
    return y + RACKET_HEIGHT;
  }
  
  // getter returning the X coordinate of Racket center
  int getCenterX()
  {
    return x + RACKET_WIDTH / 2;
  }
  
  // check if the ball is inside the X interval of the racket
  private boolean isTouchingX(Ball ball)
  {
    return (ball.getRight() >= this.getLeft()) && (ball.getLeft() <= this.getRight());
  }
  // check if the ball is inside the Y coordinate of the racket
  private boolean isTouchingY(Ball ball)
  {
    return (ball.getBottom() >= this.getTop()) && (ball.getTop() <= this.getBottom());
  }
  // check if the ball is touching the racket
  boolean isTouching(Ball ball)
  {
    return isTouchingX(ball) && isTouchingY(ball);
  }
}