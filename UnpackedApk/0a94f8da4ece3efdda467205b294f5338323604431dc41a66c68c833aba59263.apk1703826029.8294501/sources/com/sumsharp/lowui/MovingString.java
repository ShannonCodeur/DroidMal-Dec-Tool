package com.sumsharp.lowui;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import javax.microedition.lcdui.Graphics;

public class MovingString {
    private int offsetx = 0;
    private int speed;
    private int strWidth;
    private String string;
    private int width;

    public MovingString(String string2, int width2, int speed2) {
        this.string = string2;
        this.width = width2;
        this.speed = speed2;
        this.strWidth = Utilities.font.stringWidth(string2);
    }

    public void draw3D(Graphics g, int x, int y, int color, int bgColor, int anchor) {
        int cx = x;
        int cy = y;
        if ((anchor & 32) != 0) {
            cy -= Utilities.LINE_HEIGHT - 1;
        }
        g.setClip(cx, cy, this.width, Utilities.LINE_HEIGHT);
        Tool.draw3DString(g, this.string, this.offsetx + x + 2, y, color, bgColor, anchor);
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
    }

    public void draw(Graphics g, int x, int y, int color, int anchor) {
        int cx = x;
        int cy = y;
        if ((anchor & 32) != 0) {
            cy -= Utilities.LINE_HEIGHT;
        }
        g.setClip(cx, cy, this.width, Utilities.LINE_HEIGHT);
        g.setColor(color);
        g.drawString(this.string, this.offsetx + x, y, anchor);
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
    }

    public void draw(Graphics g, int x, int y, int color) {
        draw(g, x, y, color, 20);
    }

    public void draw3D(Graphics g, int x, int y, int color, int bgColor) {
        draw3D(g, x, y, color, bgColor, 20);
    }

    public void cycle() {
        if (this.strWidth >= this.width) {
            this.offsetx -= this.speed;
            if (this.offsetx + this.strWidth < (this.width >> 1)) {
                this.offsetx = this.width >> 1;
            }
        }
    }

    public void reset() {
        this.offsetx = 0;
    }

    public String getString() {
        return this.string;
    }

    public int getWidth() {
        return this.width;
    }
}
