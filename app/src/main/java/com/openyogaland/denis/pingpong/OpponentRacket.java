package com.openyogaland.denis.pingpong;

// this class represents an opponent ping-pong racket
class OpponentRacket extends Racket
{
  OpponentRacket(int x, int y, int width, int height, int color)
  {
    super(x, y, width, height);
    setColor(color);
  }
}
