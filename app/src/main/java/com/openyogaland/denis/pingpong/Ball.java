package com.openyogaland.denis.pingpong;

// this class represents a ping-pong ball
class Ball
{
  // fields
  private int x;
  private int y;
  private int radius;
  private int color;
  
  // constructor
  Ball(int x, int y, int radius, int color)
  {
    this.x      = x;
    this.y      = y;
    this.radius = radius;
    this.color  = color;
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
}
