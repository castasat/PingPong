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
  
  // constructor
  Table(int screenWidth, int screenHeight)
  {
    setScreenWidth(screenWidth);
    setScreenHeight(screenHeight);
  }
  
  // setter setScreenWidth()
  private void setScreenWidth(int screenWidth)
  {
    this.screenWidth = screenWidth;
  }
  // setter setScreenHeight
  private void setScreenHeight(int screenHeight)
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
    return BORDER_MARGIN + BORDER_WIDTH;
  }
  // get right border
  int getRightBorderX()
  {
    return screenWidth - BORDER_MARGIN - BORDER_WIDTH;
  }
  // get top border
  private int getTopBorderY()
  {
    return BORDER_MARGIN * 3 + BORDER_WIDTH;
  }
  // get bottom border
  private int getBottomBorderY()
  {
    return screenHeight - BORDER_MARGIN * 3 - BORDER_WIDTH;
  }
 
  
  // get initial opponentRacket position
  Point getInitOpponentRacketPosition()
  {
    Point point = new Point();
    point.x = (screenWidth - Racket.RACKET_WIDTH) / 2;
    point.y = getTopBorderY() + Racket.RACKET_PADDING;
    return point;
  }
  // get opponentScore text position
  Point getOpponentScoreTextPosition()
  {
    Point point = new Point();
    point.x = screenWidth / 2;
    point.y = screenHeight / 2 - BORDER_MARGIN * 2;
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
  // get playerScore text position
  Point getPlayerScoreTextPosition()
  {
    Point point = new Point();
    point.x = screenWidth / 2;
    point.y = screenHeight / 2 + BORDER_MARGIN * 4;
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
} 