package com.openyogaland.denis.pingpong;

// this class represents a ping-pong ball
class Ball
{
  // fields
  private int x;
  private int y;
  private int radius;
  private int color;
  
  // flags
  boolean isMovingLeft;
  boolean isServed;
  
  // constructor
  Ball(int x, int y, int radius, int color)
  {
    this.x      = x;
    this.y      = y;
    this.radius = radius;
    this.color  = color;
    
    isMovingLeft = false;
    isServed     = false;
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
  // getter getRadius()
  int getRadius()
  {
    return radius;
  }
  // getter getColor()
  int getColor()
  {
    return color;
  }
  
  // setter setX()
  void setX(int x)
  {
    this.x = x;
  }
  // setter setY()
  void setY(int y)
  {
    this.y = y;
  }
}
