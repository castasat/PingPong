package com.openyogaland.denis.pingpong;

import android.graphics.Rect;

// This class represents a common ping-pong racket
public class Racket
{
  // fields
  private int x;
  private int y;
  private int width;
  private int height;
  private int color;
  private Rect rect;
  
  // constructor
  Racket(int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width  = width;
    this.height = height;
    
    rect = new Rect();
    rect.set(x, y, x + width, y + height);
  }
  
  // TODO check intersection of ball and racket
  
  // setter setColor()   
  public void setColor(int color)
  {
    this.color = color;
  }
  
  // getter getX()
  public int getX()
  {
    return x;
  }
  // getter getY()
  public int getY()
  {
    return y;
  }
  // getter getWidth()
  public int getWidth()
  {
    return width;
  }
  // getter getHeight()
  public int getHeight()
  {
    return height;
  }
  // getter getColor()
  public int getColor()
  {
    return color;
  }
  // getter getRect()
  Rect getRect()
  {
    return rect;
  }
}
