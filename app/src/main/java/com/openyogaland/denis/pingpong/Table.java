package com.openyogaland.denis.pingpong;

import android.graphics.Point;

class Table
{
  // constants
  final static int BORDER_MARGIN  = 35; // отступ рамки от края экрана
  final static int BORDER_WIDTH   = 5;  // толщина линии рамки стола
  
  // fields
  private int screenWidth;  // ширина экрана
  private int screenHeight; // высота экрана
  
  private int leftBorderX;   // левая граница стола
  private int rightBorderX;  // правая граница стола
  private int topBorderY;    // верхняя граница стола
  private int bottomBorderY; // нижняя граница стола
  
  // constructor
  Table(int screenWidth, int screenHeight)
  {
    setScreenWidth(screenWidth);
    setScreenHeight(screenHeight);
  }
  
  // setter setScreenWidth()
  void setScreenWidth(int screenWidth)
  {
    this.screenWidth = screenWidth;
  }
  // setter setScreenHeight
  void setScreenHeight(int screenHeight)
  {
    this.screenHeight = screenHeight;
  }
  
  // getter getScreenWidth()
  int getScreenWidth()
  {
    return screenWidth;
  }
  // getter getScreenHeight()
  int getScreenHeight()
  {
    return screenHeight;
  }
  
  // get left border
  int getLeftBorderX()
  {
    leftBorderX = BORDER_MARGIN + BORDER_WIDTH;
    return leftBorderX;
  }
  // get right border
  int getRightBorderX()
  {
    rightBorderX = screenWidth - BORDER_MARGIN - BORDER_WIDTH;
    return rightBorderX;
  }
  // get top border
  int getTopBorderY()
  {
    topBorderY = BORDER_MARGIN * 3 + BORDER_WIDTH;
    return topBorderY;
  }
  // get bottom border
  int getBottomBorderY()
  {
    bottomBorderY = screenHeight - BORDER_MARGIN * 3 - BORDER_WIDTH;
    return bottomBorderY;
  }
 
  
  // get initial opponentRacket position
  Point getInitOpponentRacketPosition()
  {
    Point point = new Point();
    point.x = (screenWidth - Racket.RACKET_WIDTH) / 2;;
    point.y = getTopBorderY() + Racket.RACKET_PADDING;
    return point;
  }
  // get initial playerRacket position
  Point getInitPlayerRacketPosition()
  {
    Point point = new Point();
    point.x = (screenWidth - Racket.RACKET_WIDTH) / 2;
    point.y = getBottomBorderY() - Racket.RACKET_PADDING - Racket.RACKET_HEIGHT;
    return point;
  }
  // get initial ball position
  Point getInitBallPosition()
  {
    Point point = new Point();
    point.x = screenWidth / 2;
    point.y = getInitPlayerRacketPosition().y - Ball.RADIUS;
    return point;
  }
  
  // is on top border
  boolean isOnTopBorder(int x, int y)
  {
    return (y <= getTopBorderY());
  }
  // is on opponent left border
  boolean isOnOpponentLeftBorder(int x, int y)
  {
    return (x <= getLeftBorderX()) && (y <= screenHeight/2);
  }
  // is on opponent right border
  boolean isOnOpponentRightBorder(int x, int y)
  {
    return (x >= getRightBorderX()) && (y <= screenHeight/2);
  }
  
  // is on bottom border
  boolean isOnBottomBorder(int x, int y)
  {
    return (y >= getBottomBorderY());
  }
  // is on player left border
  boolean isOnPlayerLeftBorder(int x, int y)
  {
    return (x <= getLeftBorderX()) && (y > screenHeight / 2);
  }
  // is on player right border
  boolean isOnOPlayerRightBorder(int x, int y)
  {
    return (x >= getRightBorderX()) && (y > screenHeight / 2);
  }
} 