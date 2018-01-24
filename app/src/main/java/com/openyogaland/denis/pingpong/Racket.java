package com.openyogaland.denis.pingpong;

import android.graphics.Rect;

// This class represents a common ping-pong racket
class Racket
{
  // fields
  private int x;
  private int y;
  private int width;
  private int height;
  private int color;
  private Rect rect;
  
  // constructor
  Racket(int x, int y, int width, int height, int color)
  {
    this.x = x;
    this.y = y;
    this.width  = width;
    this.height = height;
    this.color  = color;
    
    rect = new Rect();
    rect.set(x, y, x + width, y + height);
  }
 
  // getter getX()
  int getX()
  {
    return x;
  }
  // getter getY()
  int getY()
  {
    return y;
  }
  // getter getWidth()
  int getWidth()
  {
    return width;
  }
  // getter getHeight()
  int getHeight()
  {
    return height;
  }
  // getter getColor()
  int getColor()
  {
    return color;
  }
  // getter getRect()
  Rect getRect()
  {
    return rect;
  }
  
  // check if the ball is touching the X coordinate of the racket
  private boolean isTouchingX(Ball ball)
  {
    // модуль разности между координатами X мяча и ракетки меньше или равен радиусу мяча
    return Math.abs(ball.getX() - this.getX()) <= ball.getRadius();
  }
  
  // check if the ball is touching the Y coordinate of the racket
  private boolean isTouchingY(Ball ball)
  {
    // координата Y мяча + радиус мяча больше или равна minY ракетки
    // координата Y мяча - радиус мяча меньше или равна maxY ракетки
    return ball.getY() + ball.getRadius() >= this.getY()
           && ball.getY() - ball.getRadius() <= this.getY() + this.getHeight();
  }
  
  // check if the ball is touching the racket
  boolean isTouching(Ball ball)
  {
    return isTouchingX(ball) && isTouchingY(ball);
  }
  
  // check if the racket misses the ball
  boolean isMissing(Ball ball)
  {
    return isTouchingX(ball) && !isTouchingY(ball);
  }
}
