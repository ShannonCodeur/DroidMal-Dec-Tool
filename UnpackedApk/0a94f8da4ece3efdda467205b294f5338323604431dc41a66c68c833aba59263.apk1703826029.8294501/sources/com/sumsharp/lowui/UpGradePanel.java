package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.item.GameItem;
import javax.microedition.lcdui.Graphics;

public class UpGradePanel extends UI {
    public static int[][] UPGRADE_COUNT = {new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}, new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, new int[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 9, 10}};
    public static int UPGRADE_ITEM_ID = 1127;
    private int contentHeight = (((((World.viewHeight - this.titleTopAni) - this.titleHeight) - this.descHeight) - 5) - DirectionPad.instance.getHeight());
    private int contentSpeed = ((this.contentTop - this.contentTopAni) >> 1);
    private int contentTop = World.viewHeight;
    private int contentTopAni = ((this.titleTopAni + this.titleHeight) + 5);
    private int descHeight = 36;
    private int descSpeed = ((this.descTop - this.descTopAni) >> 1);
    private int descTop = (this.contentTop + this.contentHeight);
    private int descTopAni = (this.contentTopAni + this.contentHeight);
    private int icon;
    GameItem item;
    private MovingString tip = new MovingString("点击选择需要的升级的装备，点击左下角按钮升级.", Tool.img_infoBg.getWidth() - 92, 1);
    private String title = "升级装备";
    private int titleHeight = Utilities.TITLE_HEIGHT;
    private int titleSpeed = ((this.titleTopAni - this.titleTop) >> 1);
    private int titleTop = (-this.titleHeight);
    private int titleTopAni = NewStage.screenY;
    private GameItem upgradeItem = new GameItem();
    private int x = 3;

    public UpGradePanel() {
        this.upgradeItem.id = UPGRADE_ITEM_ID;
        this.upgradeItem.itemId = UPGRADE_ITEM_ID;
        this.upgradeItem.name = "灵兽金星";
        this.upgradeItem.iconId = 197;
        this.upgradeItem.quanlity = 5;
        setCmd("升级", "关闭");
    }

    public void paint(Graphics g) {
        int idx;
        if (this.icon != -1) {
            int titlex = 3 + Tool.uiMiscImg.getFrameWidth(this.icon) + 2;
        }
        drawTitlePanel(g, this.title, 0, this.titleTop, World.viewWidth, Utilities.TITLE_HEIGHT, -1);
        drawRectPanel(g, this.x, this.contentTop, World.viewWidth - (this.x * 2), this.contentHeight + this.descHeight);
        int needx = this.x + 32;
        int startY = (drawItem(g, this.contentTop + 4) + 20) - (Utilities.LINE_HEIGHT / 2);
        Tool.draw3DString(g, "需要", needx, startY, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 20);
        int needx2 = needx + Utilities.font.stringWidth("需要5");
        int startY2 = startY - 5;
        this.upgradeItem.drawIcon(g, needx2 + 11, startY2 + 11, 3);
        int needx3 = needx2 + Tool.uiMiscImg2.getFrameWidth(1) + 2;
        if (this.item != null) {
            int[] u = UPGRADE_COUNT[this.item.start - 1];
            if (this.item.level == 1) {
                idx = 1;
            } else {
                idx = this.item.level / 10;
                if (idx >= u.length) {
                    idx = u.length - 1;
                }
            }
            int neednum = u[idx];
            int hasnum = CommonData.player.findAllItemCount(UPGRADE_ITEM_ID);
            Tool.drawNumStr(String.valueOf(neednum), g, needx3 + 16, startY2 + 21, 0, 40, hasnum > neednum ? -1 : -65536);
            startY2 += 5;
            int needx4 = needx3 + 24;
            Tool.draw3DString(g, this.upgradeItem.name, needx4, startY2, Tool.getQuanlityColor(this.upgradeItem.quanlity), Tool.CLR_TABLE[11], 20);
            Tool.draw3DString(g, "(拥有:" + hasnum + "个)", needx4 + g.getFont().stringWidth(this.upgradeItem.name) + 5, startY2, hasnum > neednum ? 16777215 : 16711680, Tool.CLR_TABLE[11], 20);
        }
        int needx5 = this.x + 32;
        int startY3 = startY2 + 24;
        Tool.draw3DString(g, "需要金币:", needx5, startY3, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 20);
        int needx6 = needx5 + Utilities.font.stringWidth("需要金币:10");
        int needmoney = 0;
        if (this.item != null) {
            needmoney = (this.item.level + 10) * this.item.start * (this.item.add + 1) * (this.item.add + 1);
        }
        Tool.drawMoney(g, needmoney, needx6, startY3 + 5, -1, 20, false);
        g.drawImage(Tool.img_infoBg, DirectionPad.instance.getWidth(), (World.viewHeight - CornerButton.instance.getHeight()) + 2, 36);
        this.tip.draw3D(g, DirectionPad.instance.getWidth() + 46, ((World.viewHeight - CornerButton.instance.getHeight()) - Tool.img_infoBg.getHeight()) + 7, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
        drawBtn(g, (this.descTop + this.descHeight) - (Utilities.LINE_HEIGHT >> 1));
    }

    public void cycle() {
        super.cycle();
        if (this.closed) {
            this.titleTop -= this.titleSpeed;
            if (this.descTop != this.descTopAni) {
                this.contentTop += this.contentSpeed;
            }
            this.descTop += this.descSpeed;
            if (this.contentTop > World.viewHeight) {
                this.show = false;
            }
        } else if (this.show) {
            moveBtn();
            if (this.tip != null) {
                this.tip.cycle();
            }
        } else {
            this.titleTop += this.titleSpeed;
            if (this.contentTop != World.viewHeight) {
                this.descTop -= this.descSpeed;
            }
            this.contentTop -= this.contentSpeed;
            if (this.descTop - this.descSpeed <= this.descTopAni) {
                this.descTop = this.descTopAni;
                this.contentTop = this.contentTopAni;
                this.titleTop = this.titleTopAni;
                this.show = true;
            }
        }
    }

    private int drawItem(Graphics g, int y) {
        if (this.item != null) {
            Tool.draw3DString(g, this.item.name, World.viewWidth >> 1, y, Tool.getQuanlityColor(this.item.quanlity), Tool.CLR_TABLE[11], 17);
        }
        int y2 = y + Utilities.CHAR_HEIGHT + 5;
        int gridX = (World.uiWidth - 50) >> 1;
        if (this.item == null) {
            g.setColor(Tool.CLR_TABLE[4]);
            Tool.drawBlurRect(g, gridX, y2, 50, 50, 0);
        } else {
            if (this.item.reqLevel > CommonData.player.level) {
                g.setColor(Tool.CLR_TABLE[7]);
            } else {
                g.setColor(Tool.CLR_TABLE[4]);
            }
            Tool.drawBlurRect(g, gridX, y2, 50, 50, 0);
        }
        g.setColor(Tool.CLR_TABLE[11]);
        g.drawRect(gridX + 2, y2 + 2, 50 - 4, 50 - 1);
        if (this.item != null) {
            g.setColor(Tool.getQuanlityColor(this.item.quanlity));
        } else {
            g.setColor(Tool.CLR_TABLE[2]);
        }
        g.drawRect(gridX + 1, y2 + 1, 50 - 2, 50 - 2);
        g.setColor(Tool.CLR_TABLE[11]);
        g.drawRect((World.uiWidth - 50) >> 1, y2, 50, 50);
        if (this.item != null) {
            this.item.drawIcon(g, (50 / 2) + gridX, (50 / 2) + y2, 3);
        }
        int i = 50 + 5;
        return y2 + 55;
    }

    public void setGameItem(GameItem itm) {
        this.item = itm;
    }
}
