package com.sumsharp.lowui;

public class Rect {
    public int h;
    public int w;
    public int x;
    public int y;

    public Rect(int w2, int h2) {
        this.x = w2;
        this.y = h2;
    }

    public Rect(int x2, int y2, int w2, int h2) {
        this.x = x2;
        this.y = y2;
        this.w = w2;
        this.h = w2;
    }
}
