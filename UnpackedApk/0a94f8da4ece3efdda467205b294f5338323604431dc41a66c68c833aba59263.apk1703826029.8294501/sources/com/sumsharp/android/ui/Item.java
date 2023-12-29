package com.sumsharp.android.ui;

import com.sumsharp.monster.common.Tool;
import javax.microedition.lcdui.Graphics;

/* compiled from: GridMenu */
class Item {
    public static int h;
    public static int w;
    public String content;
    public int disableId;
    public int function;
    public int id;
    public int imgId;
    public boolean isEnable;
    public boolean isSelected;
    public String name;
    public int x;
    public int y;

    Item() {
    }

    public void draw(Graphics g) {
        if (this.id != -1) {
            if (this.isSelected) {
                g.drawImage(Tool.img_roundbtn1, GridMenu.instance.x + this.x, GridMenu.instance.y + this.y, 20);
            } else {
                g.drawImage(Tool.img_roundbtn0, GridMenu.instance.x + this.x, GridMenu.instance.y + this.y, 20);
            }
            Tool.shortcutImg.drawFrame(g, this.isEnable ? this.imgId : this.disableId, ((GridMenu.instance.x + this.x) + (w / 2)) - 2, (h / 2) + GridMenu.instance.y + this.y, 0, 3);
            return;
        }
        g.drawImage(Tool.img_roundbtn0, GridMenu.instance.x + this.x, GridMenu.instance.y + this.y, 20);
    }

    public boolean handle(int px, int py) {
        if (px == -1 && py == -1) {
            return false;
        }
        int px2 = px - GridMenu.instance.x;
        int py2 = py - GridMenu.instance.y;
        if (px2 < this.x || px2 > this.x + 45 || py2 < this.y || py2 > this.y + 45) {
            return false;
        }
        if (!this.isEnable) {
            return false;
        }
        this.isSelected = true;
        return true;
    }

    public static void initWH() {
        w = Tool.img_roundbtn0.getWidth();
        h = Tool.img_roundbtn0.getHeight();
    }
}
