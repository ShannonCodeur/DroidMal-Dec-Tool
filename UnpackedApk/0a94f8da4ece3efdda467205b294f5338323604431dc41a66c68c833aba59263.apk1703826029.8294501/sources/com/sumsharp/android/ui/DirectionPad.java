package com.sumsharp.android.ui;

import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Region.Op;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class DirectionPad {
    public static DirectionPad instance = new DirectionPad();
    private Region down;
    private int h;
    private Image img_down_nopress;
    private Image img_down_press;
    private Image img_left_nopress;
    private Image img_left_press;
    private Image img_right_nopress;
    private Image img_right_press;
    private Image img_up_nopress;
    private Image img_up_press;
    private Region left;
    public int moveX;
    public int moveY;
    private Region right;
    public boolean showPad = true;
    public int state;
    private Region up;
    private int w;
    private int x;
    private int y;

    private DirectionPad() {
    }

    public void init() {
        this.w = Tool.img_dirPadBg.getWidth();
        this.h = Tool.img_dirPadBg.getHeight();
        this.x = 0;
        this.y = World.viewHeight - this.h;
        this.state = -1;
        this.img_up_nopress = Tool.img_dir0;
        this.img_up_press = Tool.img_dir1;
        this.img_down_nopress = Image.createImage(this.img_up_nopress, 0, 0, this.img_up_nopress.getWidth(), this.img_up_nopress.getHeight(), 3);
        this.img_down_press = Image.createImage(this.img_up_press, 0, 0, this.img_up_press.getWidth(), this.img_up_press.getHeight(), 3);
        this.img_left_nopress = Image.createImage(this.img_up_nopress, 0, 0, this.img_up_nopress.getWidth(), this.img_up_nopress.getHeight(), 6);
        this.img_left_press = Image.createImage(this.img_up_press, 0, 0, this.img_up_press.getWidth(), this.img_up_press.getHeight(), 6);
        this.img_right_nopress = Image.createImage(this.img_up_nopress, 0, 0, this.img_up_nopress.getWidth(), this.img_up_nopress.getHeight(), 5);
        this.img_right_press = Image.createImage(this.img_up_press, 0, 0, this.img_up_press.getWidth(), this.img_up_press.getHeight(), 5);
        initRegion();
    }

    private void initRegion() {
        Rect rup = new Rect(this.x + ((this.w - 58) / 2), this.y + 5, this.x + ((this.w - 58) / 2) + 58, this.y + 40 + 5);
        Rect rdown = new Rect(this.x + ((this.w - 58) / 2), (this.y + this.h) - 40, this.x + ((this.w - 58) / 2) + 58, this.y + this.h);
        Rect rleft = new Rect(0, (this.y + ((this.h - 40) / 2)) - 5, 40, ((this.y + ((this.h - 40) / 2)) + 58) - 5);
        Rect rright = new Rect((this.w - 40) - 1, (this.y + ((this.h - 40) / 2)) - 5, ((this.w - 40) + 40) - 1, ((this.y + ((this.h - 40) / 2)) + 58) - 5);
        this.up = new Region(rup);
        this.up.op(rup.left, rup.top, rup.left + 10, rup.top + 10, Op.XOR);
        this.up.op(rup.right - 10, rup.top, rup.right, rup.top + 10, Op.XOR);
        this.up.op(rup.left, rup.bottom - 10, rup.left + 10, rup.bottom, Op.XOR);
        this.up.op(rup.right - 10, rup.bottom - 10, rup.right, rup.bottom, Op.XOR);
        this.down = new Region(rdown);
        this.down.op(rdown.left, rdown.top, rdown.left + 10, rdown.top + 10, Op.XOR);
        this.down.op(rdown.right - 10, rdown.top, rdown.right, rdown.top + 10, Op.XOR);
        this.down.op(rdown.left, rdown.bottom - 10, rdown.left + 10, rdown.bottom, Op.XOR);
        this.down.op(rdown.right - 10, rdown.bottom - 10, rdown.right, rdown.bottom, Op.XOR);
        this.left = new Region(rleft);
        this.left.op(rleft.left, rleft.top, rleft.left + 10, rleft.top + 10, Op.XOR);
        this.left.op(rleft.right - 10, rleft.top, rleft.right, rleft.top + 10, Op.XOR);
        this.left.op(rleft.left, rleft.bottom - 10, rleft.left + 10, rleft.bottom, Op.XOR);
        this.left.op(rleft.right - 10, rleft.bottom - 10, rleft.right, rleft.bottom, Op.XOR);
        this.right = new Region(rright);
        this.right.op(rright.left, rright.top, rright.left + 10, rright.top + 10, Op.XOR);
        this.right.op(rright.right - 10, rright.top, rright.right, rright.top + 10, Op.XOR);
        this.right.op(rright.left, rright.bottom - 10, rright.left + 10, rright.bottom, Op.XOR);
        this.right.op(rright.right - 10, rright.bottom - 10, rright.right, rright.bottom, Op.XOR);
    }

    public boolean release() {
        if (this.state == -1) {
            return false;
        }
        switch (this.state) {
            case 0:
                Utilities.keyReleased(1);
                break;
            case 1:
                Utilities.keyReleased(2);
                break;
            case 2:
                Utilities.keyReleased(3);
                break;
            case 3:
                Utilities.keyReleased(4);
                break;
        }
        this.state = -1;
        return true;
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }

    public void paint(Graphics g) {
        if (this.showPad) {
            g.setClip(0, 0, 320, 480);
            if (this.state == 0) {
                g.drawImage(this.img_up_press, this.x + (this.w / 2), this.y, 17);
                g.drawImage(this.img_left_nopress, this.x - 3, this.y + (this.h / 2) + 5, 6);
                g.drawImage(this.img_right_nopress, this.x + this.w + 1, this.y + (this.h / 2) + 5, 10);
                g.drawImage(this.img_down_nopress, this.x + (this.w / 2), this.y + this.h + 5, 33);
            } else if (this.state == 1) {
                g.drawImage(this.img_up_nopress, this.x + (this.w / 2), this.y, 17);
                g.drawImage(this.img_left_nopress, this.x - 3, this.y + (this.h / 2) + 5, 6);
                g.drawImage(this.img_right_nopress, this.x + this.w + 1, this.y + (this.h / 2) + 5, 10);
                g.drawImage(this.img_down_press, this.x + (this.w / 2), this.y + this.h + 5, 33);
            } else if (this.state == 3) {
                g.drawImage(this.img_up_nopress, this.x + (this.w / 2), this.y, 17);
                g.drawImage(this.img_left_nopress, this.x - 3, this.y + (this.h / 2) + 5, 6);
                g.drawImage(this.img_right_press, this.x + this.w + 1, this.y + (this.h / 2) + 5, 10);
                g.drawImage(this.img_down_nopress, this.x + (this.w / 2), this.y + this.h + 5, 33);
            } else if (this.state == 2) {
                g.drawImage(this.img_up_nopress, this.x + (this.w / 2), this.y, 17);
                g.drawImage(this.img_left_press, this.x - 3, this.y + (this.h / 2) + 5, 6);
                g.drawImage(this.img_right_nopress, this.x + this.w + 1, this.y + (this.h / 2) + 5, 10);
                g.drawImage(this.img_down_nopress, this.x + (this.w / 2), this.y + this.h + 5, 33);
            } else {
                g.drawImage(this.img_up_nopress, this.x + (this.w / 2), this.y, 17);
                g.drawImage(this.img_left_nopress, this.x - 3, this.y + (this.h / 2) + 5, 6);
                g.drawImage(this.img_right_nopress, this.x + this.w + 1, this.y + (this.h / 2) + 5, 10);
                g.drawImage(this.img_down_nopress, this.x + (this.w / 2), this.y + this.h + 5, 33);
            }
            if (this.state == -1) {
                g.drawImage(Tool.img_dircenter0, this.x + (this.w / 2), this.y + (this.h / 2) + 5, 3);
            } else {
                g.drawImage(Tool.img_dircenter1, this.x + (this.w / 2), this.y + (this.h / 2) + 5, 3);
            }
        }
    }

    public void setMovePos() {
        if (World.moveX <= this.x || World.moveX >= this.x + this.w || World.moveY <= this.y || World.moveY >= instance.y + instance.h) {
            this.moveX = -1;
            this.moveY = -1;
            return;
        }
        this.moveX = World.moveX;
        this.moveY = World.moveY;
    }

    public void cycle() {
        int newstate;
        setMovePos();
        if (World.pressedx != -1 && World.pressedy != -1 && World.pressedx > this.x && World.pressedx < this.x + this.w && World.pressedy > this.y && World.pressedy < this.y + this.h) {
            this.moveX = World.pressedx;
            this.moveY = World.pressedy;
        }
        if (!(World.releasedx == -1 || World.releasedx == -1) || this.moveX <= this.x || this.moveX >= this.x + this.w || this.moveY <= this.y || this.moveY >= this.y + this.h) {
            this.moveX = -1;
            this.moveY = -1;
        }
        if (this.moveX == -1 && this.moveY == -1) {
            release();
            return;
        }
        int i = this.moveX - this.x;
        int i2 = this.moveY - this.y;
        if (this.up.contains(World.moveX, World.moveY)) {
            newstate = 0;
        } else if (this.down.contains(World.moveX, World.moveY)) {
            newstate = 1;
        } else if (this.left.contains(World.moveX, World.moveY)) {
            newstate = 2;
        } else if (this.right.contains(World.moveX, World.moveY)) {
            newstate = 3;
        } else {
            release();
            return;
        }
        if (newstate != -1 && newstate != this.state) {
            release();
            this.state = newstate;
            switch (newstate) {
                case 0:
                    Utilities.keyPressed(1, true);
                    break;
                case 1:
                    Utilities.keyPressed(2, true);
                    break;
                case 2:
                    Utilities.keyPressed(3, true);
                    break;
                case 3:
                    Utilities.keyPressed(4, true);
                    break;
            }
            World.pressedx = -1;
            World.pressedy = -1;
        }
    }
}
