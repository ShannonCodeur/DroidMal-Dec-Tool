package com.sumsharp.monster.image;

import javax.microedition.lcdui.Graphics;

public class CartoonPlayer {
    public PipAnimateSet animate;
    private int animateIndex;
    public int cType;
    private int[][] currentFrameRGB;
    public boolean die;
    public int drawX;
    public int drawY;
    private int frameIndex;
    public String id;
    private int lastAnimateIndex = -1;
    private int lastFrameIndex = -1;
    public boolean loop;

    private void init(PipAnimateSet ct, int ai, int dx, int dy) {
        this.animate = ct;
        this.animateIndex = ai;
        this.drawX = dx;
        this.drawY = dy;
        this.frameIndex = -1;
    }

    public void reset() {
        this.frameIndex = -1;
        this.die = false;
    }

    public int nextFrame() {
        if (this.die) {
            return -1;
        }
        this.currentFrameRGB = null;
        this.frameIndex++;
        if (this.animateIndex > this.animate.getAnimateLength()) {
            this.animateIndex = 0;
        }
        if (this.frameIndex >= this.animate.getAnimateLength(this.animateIndex)) {
            if (this.loop) {
                this.frameIndex = 0;
            } else {
                this.die = true;
                return -1;
            }
        }
        return this.frameIndex;
    }

    public void setDrawPostion(int dx, int dy) {
        this.drawX = dx;
        this.drawY = dy;
    }

    public int[][] getRGB() {
        if (this.currentFrameRGB == null) {
            this.currentFrameRGB = this.animate.getAnimageFrameRGB(this.animateIndex, this.frameIndex);
        }
        return this.currentFrameRGB;
    }

    public void draw(Graphics g, int x, int y) {
        if (!this.die && this.frameIndex >= 0) {
            this.animate.drawAnimateFrame(g, this.animateIndex, this.frameIndex, x, y);
        }
    }

    public void draw(Graphics g) {
        draw(g, this.drawX, this.drawY);
    }

    public static CartoonPlayer playCartoon(PipAnimateSet ct, int ai, int dx, int dy, boolean loop2) {
        CartoonPlayer cp = new CartoonPlayer();
        cp.loop = loop2;
        cp.init(ct, ai, dx, dy);
        return cp;
    }

    public int getWidth() {
        return getWidth(this.animateIndex, this.frameIndex);
    }

    public int getHeight() {
        return getHeight(this.animateIndex, this.frameIndex);
    }

    public int getWidth(int animateIndex2, int time) {
        return this.animate.getWidth(animateIndex2, time);
    }

    public int getHeight(int animateIndex2, int time) {
        return this.animate.getHeight(animateIndex2, time);
    }

    public int getAnimateIndex() {
        return this.animateIndex;
    }

    public void setAnimateIndex(int animateIndex2) {
        if (animateIndex2 != this.lastAnimateIndex) {
            this.currentFrameRGB = null;
            this.lastFrameIndex = animateIndex2;
        }
        this.animateIndex = animateIndex2;
    }

    public PipAnimateSet getAnimate() {
        return this.animate;
    }

    public void setAnimate(PipAnimateSet animate2) {
        this.animate = animate2;
    }

    public int getFrameIndex() {
        return this.frameIndex;
    }

    public void setFrameIndex(int frameIndex2) {
        if (frameIndex2 != this.lastFrameIndex) {
            this.currentFrameRGB = null;
            this.lastFrameIndex = frameIndex2;
        }
        this.frameIndex = frameIndex2;
    }
}
