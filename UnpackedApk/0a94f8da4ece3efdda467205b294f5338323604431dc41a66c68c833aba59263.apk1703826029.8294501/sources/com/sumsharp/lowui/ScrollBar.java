package com.sumsharp.lowui;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import javax.microedition.lcdui.Graphics;

public class ScrollBar {
    public static final int DOWN = 1;
    public static final int TO_BOTTOM = 3;
    public static final int TO_TOP = 2;
    public static final int UP = 0;
    public boolean actioned = false;
    private int barHeight;
    private int barY;
    private int h;
    private int itemHeight;
    private int leftCount;
    private int offIdx = 0;
    private int poolHeight;
    private int showCount;
    private int showHeight;
    int startBarY;
    private int step;
    private int totalCount;
    private int totalHeight;
    private int w = 16;
    private int x;
    public int y;

    public ScrollBar(int x2, int y2, int h2, int totalCount2, int totalHeight2, int showCount2, int showHeight2, int itemHeight2) {
        this.x = x2;
        this.y = y2;
        this.h = h2;
        this.totalHeight = totalHeight2;
        this.showHeight = showHeight2;
        this.poolHeight = h2 - 32;
        this.barHeight = (((showHeight2 << 10) / totalHeight2) * this.poolHeight) >> 10;
        this.startBarY = y2 + 16;
        this.barY = this.startBarY;
        this.itemHeight = itemHeight2;
        this.showCount = showCount2;
        this.totalCount = totalCount2;
        int leftHeight = this.poolHeight - this.barHeight;
        this.leftCount = totalCount2 - showCount2;
        this.step = leftHeight / this.leftCount;
    }

    public int getWidth() {
        return this.w;
    }

    public void paint(Graphics g) {
        g.getCanvas().save();
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        int drawy = this.startBarY - 16;
        g.drawImage(Tool.img_scroll[0], this.x, drawy);
        g.drawImage(Tool.img_scroll[1], this.x, (this.h + drawy) - 16);
        drawPool(g, this.startBarY, this.h - 32);
        drawBlock(g, this.x, this.barY, this.barHeight);
        g.getCanvas().restore();
    }

    private void drawPool(Graphics g, int start, int height) {
        Tool.drawBlurRect(g, this.x, start, 16, height, 2);
    }

    public static void drawBlock(Graphics g, int x2, int start, int height) {
        int dy = start;
        int end = start + height;
        g.getCanvas().save();
        g.setClip(x2, start, 16, height);
        g.drawImage(Tool.img_scroll[2], x2, dy);
        int height2 = Tool.img_scroll[2].getHeight();
        while (true) {
            dy += height2;
            if (Tool.img_scroll[3].getHeight() + dy > end) {
                g.drawImage(Tool.img_scroll[3], x2, end, 36);
                g.getCanvas().restore();
                return;
            }
            g.drawImage(Tool.img_scroll[4], x2, dy);
            height2 = Tool.img_scroll[4].getHeight();
        }
    }

    private void up() {
        if (this.offIdx > 0) {
            this.offIdx--;
        }
        if (this.barY < this.y + 8) {
            toBottom();
        }
    }

    private void down() {
        if (this.offIdx < this.totalCount - this.showCount) {
            this.offIdx++;
        }
        if (this.barY + this.barHeight > (this.y + this.h) - 8) {
            toTop();
        }
    }

    private void toTop() {
        this.barY = this.y + 8;
        this.offIdx = 0;
    }

    private void toBottom() {
        this.barY = ((this.y + this.h) - 8) - this.barHeight;
        this.offIdx = this.leftCount;
    }

    public void move(int action) {
        if (!this.actioned) {
            if (action == 0) {
                up();
                this.actioned = true;
            } else if (action == 1) {
                down();
                this.actioned = true;
            } else if (action == 2) {
                toTop();
                this.actioned = true;
            } else if (action == 3) {
                toBottom();
                this.actioned = true;
            }
            this.barY = this.startBarY + (this.offIdx * this.step) + getOff();
        }
    }

    public boolean handlePoints() {
        int px = World.pressedx;
        int py = World.pressedy;
        if (px == -1 && py == -1) {
            return false;
        }
        if (px <= this.x || px >= this.x + this.w || py <= this.y || py >= this.y + this.h) {
            return false;
        }
        if (py < this.y + 16) {
            Utilities.keyPressed(1, true);
        } else if (py > (this.y + this.h) - 16) {
            Utilities.keyPressed(2, true);
        }
        return true;
    }

    private int getOff() {
        return (this.offIdx * ((this.poolHeight - this.barHeight) % this.leftCount)) / this.leftCount;
    }
}
