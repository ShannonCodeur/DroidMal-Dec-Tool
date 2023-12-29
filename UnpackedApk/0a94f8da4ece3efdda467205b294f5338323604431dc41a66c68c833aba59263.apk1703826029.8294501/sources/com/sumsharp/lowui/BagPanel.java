package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.item.GameItem;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class BagPanel extends UI {
    private int buyHeight;
    private int col;
    private int gridBound = 45;
    public boolean hasTab;
    private int height;
    private int infow = 0;
    private int infox = 0;
    private Vector items;
    private int lastCol = -1;
    private int lastRow = -1;
    private int lastSel = -1;
    private GameItem lastSelItem = null;
    private int maxGrid;
    private int refreshGrid = 0;
    private int row;
    private ScrollBar scroll;
    private int scrollHeight = 0;
    private int scrollUnitHeight = 0;
    private int scrollX;
    private int scrollY;
    private int selCol;
    private int selRow;
    private int showBuyInfo;
    private int[] showItem_area;
    private int showRow;
    private boolean showScroll = false;
    private int startRow;
    private long timer = 0;
    private Tip tip;
    private int tipMode = 0;
    private int width;
    private int x;
    private int y;

    public BagPanel(int x2, int y2, int width2, int height2, int maxGrid2, Vector items2, int showBuyInfo2) {
        this.x = x2;
        this.y = y2;
        this.showBuyInfo = showBuyInfo2;
        if (showBuyInfo2 != 0) {
            this.buyHeight = Utilities.LINE_HEIGHT + (Utilities.LINE_HEIGHT / 2);
            if (height2 < 50) {
                this.buyHeight -= 50 - height2;
                height2 = 50;
            }
            this.tipMode = 0;
        }
        this.width = width2 - 45;
        this.height = height2;
        setInput(maxGrid2, items2);
        this.hasTab = false;
        this.scrollX = width2 - 30;
        this.infox = DirectionPad.instance.getWidth() + 46;
        this.infow = 0;
    }

    private void updateItemArea() {
        int dx;
        if (this.needsUpdate && this.show) {
            this.showItem_area = new int[(this.row * this.col * 4)];
            int dx2 = this.x + 5;
            int dy = this.y + 5;
            if (this.showScroll) {
                dx2 -= 3;
            }
            for (int i = this.startRow; i < this.startRow + this.showRow; i++) {
                for (int j = 0; j < this.col; j++) {
                    int idx = (this.col * i) + j;
                    if (idx >= this.maxGrid) {
                        break;
                    }
                    this.showItem_area[(idx * 4) + 0] = dx;
                    this.showItem_area[(idx * 4) + 1] = dy;
                    this.showItem_area[(idx * 4) + 2] = this.gridBound;
                    this.showItem_area[(idx * 4) + 3] = this.gridBound;
                    dx += this.gridBound;
                }
                dx = this.x + 5;
                if (this.showScroll) {
                    dx -= 3;
                }
                dy += this.gridBound;
            }
            this.needsUpdate = false;
        }
    }

    public void setHasTab(boolean hasTab2) {
        this.hasTab = hasTab2;
        if (hasTab2) {
            this.height -= Tab.getDefaultHeight();
            if (this.height < 50) {
                this.buyHeight -= 50 - this.height;
                this.height = 50;
            }
        }
    }

    public void setInput(int maxGrid2, Vector items2) {
        this.items = items2;
        this.lastSel = -1;
        this.selRow = 0;
        this.selCol = 0;
        this.startRow = 0;
        this.col = (this.width - 10) / this.gridBound;
        this.showRow = (this.height - 10) / this.gridBound;
        this.refreshGrid = 0;
        if (maxGrid2 == -1) {
            maxGrid2 = items2.size();
            this.needsUpdate = true;
        }
        if (maxGrid2 == -2) {
            this.refreshGrid = 1;
            maxGrid2 = CommonData.player.bagSize;
            this.items = CommonData.player.itemBag;
            this.needsUpdate = true;
        }
        if (maxGrid2 == -3) {
            this.refreshGrid = 2;
            maxGrid2 = CommonData.player.bankSize;
            this.items = CommonData.player.bank;
            this.needsUpdate = true;
        }
        this.row = maxGrid2 / this.col;
        this.maxGrid = maxGrid2;
        if (maxGrid2 % this.col != 0) {
            this.row++;
        }
        this.startRow = 0;
        if (this.row <= this.showRow) {
            this.showRow = this.row;
        }
        checkScroll();
        this.needsUpdate = true;
        updateItemArea();
        this.timer = System.currentTimeMillis();
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    private GameItem getItem(int idx) {
        if (idx < 0 || idx >= this.items.size()) {
            return null;
        }
        return (GameItem) this.items.elementAt(idx);
    }

    public int getSelIdx() {
        return (this.selRow * this.col) + this.selCol;
    }

    public void cycle() {
        updateItemArea();
        if (this.refreshGrid == 1) {
            this.maxGrid = CommonData.player.bagSize;
            this.row = this.maxGrid / this.col;
            if (this.maxGrid % this.col != 0) {
                this.row++;
            }
            if (this.row <= this.showRow) {
                this.showRow = this.row;
            }
            checkScroll();
        } else if (this.refreshGrid == 2) {
            this.maxGrid = CommonData.player.bankSize;
            this.row = this.maxGrid / this.col;
            if (this.maxGrid % this.col != 0) {
                this.row++;
            }
            if (this.row <= this.showRow) {
                this.showRow = this.row;
            }
            checkScroll();
        }
        if (this.showScroll) {
            if (this.show && this.scroll == null) {
                this.scrollY = this.y + 8;
                this.scroll = new ScrollBar(this.scrollX, this.scrollY, this.scrollHeight, this.row, this.row * this.gridBound, this.showRow, this.showRow * this.gridBound, this.gridBound);
            }
            this.scroll.actioned = false;
            if (this.scroll != null) {
                this.scroll.handlePoints();
            }
        }
        if (this.show && !this.needsUpdate && World.pressedx > this.x + 5 && World.pressedx < this.x + 5 + (this.col * this.gridBound) && World.pressedy > this.y + 5 && World.pressedy < this.y + 5 + (this.showRow * this.gridBound)) {
            for (int i = 0; i < this.showRow; i++) {
                for (int j = 0; j < this.col; j++) {
                }
            }
            this.selCol = ((World.pressedx - this.x) - 5) / this.gridBound;
            this.selRow = (((World.pressedy - this.y) - 5) / this.gridBound) + this.startRow;
            if (this.lastRow == -1 && this.lastCol == -1) {
                this.lastRow = this.selRow;
                this.lastCol = this.selCol;
            } else if (this.lastRow == this.selRow && this.lastCol == this.selCol) {
                Utilities.keyPressed(5, true);
            } else {
                this.lastRow = this.selRow;
                this.lastCol = this.selCol;
                this.timer = System.currentTimeMillis();
            }
        }
        if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
            this.selCol++;
            if (getSelIdx() >= this.maxGrid) {
                this.selCol = 0;
                this.selRow = 0;
            } else if (this.selCol >= this.col) {
                this.selCol = 0;
                if (this.selRow < this.row - 1) {
                    this.selRow++;
                } else {
                    this.selRow = 0;
                }
            }
            if (this.showBuyInfo != 0) {
                this.tipMode = 0;
            }
            this.timer = System.currentTimeMillis();
        } else if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
            this.selCol--;
            if (getSelIdx() < 0) {
                this.selRow = this.row - 1;
                this.selCol = (this.maxGrid % this.col) - 1;
            } else if (this.selCol < 0) {
                this.selCol = this.col - 1;
                if (this.selRow > 0) {
                    this.selRow--;
                } else {
                    this.selRow = this.row - 1;
                }
            }
            if (this.showBuyInfo != 0) {
                this.tipMode = 0;
            }
            this.timer = System.currentTimeMillis();
        } else if (Utilities.isKeyPressed(0, true) || Utilities.isKeyPressed(13, true)) {
            if (this.selRow > 0) {
                this.selRow--;
            } else {
                this.selRow = this.row - 1;
                if (getSelIdx() >= this.maxGrid) {
                    this.selCol = (this.maxGrid % this.col) - 1;
                }
                if (this.showScroll) {
                    this.scroll.move(3);
                }
            }
            if (this.showBuyInfo != 0) {
                this.tipMode = 0;
            }
            this.timer = System.currentTimeMillis();
        } else if (Utilities.isKeyPressed(1, true) || Utilities.isKeyPressed(19, true)) {
            if (this.selRow < this.row - 1) {
                this.selRow++;
                if (getSelIdx() >= this.maxGrid) {
                    this.selCol = (this.maxGrid % this.col) - 1;
                }
            } else {
                this.selRow = 0;
                if (this.showScroll) {
                    this.scroll.move(2);
                }
            }
            if (this.showBuyInfo != 0) {
                this.tipMode = 0;
            }
            this.timer = System.currentTimeMillis();
        }
        GameItem item = getItem(getSelIdx());
        if (item != null) {
            item.tipMode = this.tipMode;
        }
        if (this.startRow + this.showRow <= this.selRow) {
            this.startRow = (this.selRow - this.showRow) + 1;
            this.needsUpdate = true;
            if (this.showScroll) {
                this.scroll.move(1);
            }
        }
        if (this.startRow > this.selRow) {
            this.startRow = this.selRow;
            this.needsUpdate = true;
            if (this.showScroll) {
                this.scroll.move(0);
            }
        }
        if (!(this.lastSel == getSelIdx() && this.lastSelItem == item)) {
            this.lastSel = getSelIdx();
            this.lastSelItem = item;
            if (item != null && this.show) {
                this.tip = Tip.createTip(item, this.width - 10);
                if (this.y + 9 + ((this.selRow - this.startRow) * this.gridBound) + this.tip.getHeight() > (this.y + getHeight()) - (Utilities.LINE_HEIGHT * 2)) {
                    this.tip.setBounds(this.x + 5, (((this.y + 2) + ((this.selRow - this.startRow) * this.gridBound)) - this.tip.getHeight()) + 5, this.width - 10, (((this.selCol * this.gridBound) + 11) - this.x) - 5, true);
                } else {
                    this.tip.setBounds(this.x + 5, ((this.y + this.gridBound) - 4) + ((this.selRow - this.startRow) * this.gridBound), this.width - 10, (((this.selCol * this.gridBound) + 11) - this.x) - 5, false);
                }
                this.infow = 0;
            }
        }
        if (Utilities.isKeyPressed(4, true)) {
            if (!(this.lastSel == getSelIdx() && this.lastSelItem == item)) {
                this.lastSel = getSelIdx();
                this.lastSelItem = item;
            }
            if (this.showBuyInfo != 0) {
                Utilities.keyPressed(6, true);
            } else {
                Utilities.keyPressed(5, true);
            }
        }
        if (this.tip != null) {
            this.tip.cycle();
        }
    }

    private void checkScroll() {
        if (this.row > this.showRow) {
            this.showScroll = true;
            this.scrollHeight = this.height - 25;
            this.scrollUnitHeight = this.scrollHeight / this.row;
            this.scrollHeight = this.scrollUnitHeight * this.row;
            return;
        }
        this.showScroll = false;
    }

    public void paint(Graphics g) {
        int dx;
        if (!this.hasTab) {
            drawRectPanel(g, this.x, this.y, this.width + 45, this.height);
        }
        int dx2 = this.x + 5;
        int dy = this.y + 5;
        if (this.showScroll) {
            dx2 -= 3;
        }
        for (int y2 = this.startRow; y2 < this.startRow + this.showRow; y2++) {
            int x2 = 0;
            while (true) {
                int dx3 = dx;
                if (x2 < this.col) {
                    int idx = (this.col * y2) + x2;
                    if (idx >= this.maxGrid) {
                        break;
                    }
                    GameItem item = getItem(idx);
                    if (item == null) {
                        g.setColor(Tool.CLR_TABLE[4]);
                    } else if (item.count <= 0 || item.reqLevel <= CommonData.player.level) {
                        g.setColor(Tool.CLR_TABLE[4]);
                    } else {
                        g.setColor(Tool.CLR_TABLE[7]);
                    }
                    if (y2 == this.selRow && x2 == this.selCol) {
                        Tool.drawBlurRect(g, dx3 + 2, dy + 2, this.gridBound - 4, this.gridBound - 4, 1);
                        if (item != null) {
                            g.setColor(Tool.getQuanlityColor(item.quanlity));
                        } else {
                            g.setColor(Tool.CLR_TABLE[2]);
                        }
                        g.drawRect(dx3 + 3, dy + 3, this.gridBound - 7, this.gridBound - 7);
                        g.drawImage(Tool.img_selgrid, dx3, dy);
                    } else {
                        Tool.drawBlurRect(g, dx3 + 2, dy + 2, this.gridBound - 4, this.gridBound - 4, 0);
                    }
                    if (item != null && item.count > 0) {
                        item.drawIcon(g, (this.gridBound >> 1) + dx3, (this.gridBound >> 1) + dy, 3);
                    }
                    if (item != null && item.count > 1) {
                        Tool.drawNumStr(String.valueOf(item.count), g, dx3 + this.gridBound, dy + this.gridBound, 0, 40, -1);
                    }
                    dx = dx3 + this.gridBound;
                    x2++;
                } else {
                    break;
                }
            }
            dx = this.x + 5;
            if (this.showScroll) {
                dx -= 3;
            }
            dy += this.gridBound;
        }
        int dx4 = this.x + 30;
        int dy2 = this.y + 5;
        if (this.showScroll && this.scroll != null) {
            this.scroll.paint(g);
        }
        GameItem selItem = getItem(getSelIdx());
        if (this.showBuyInfo != 0) {
            int prw = Utilities.font.stringWidth("价格") + 8;
            if (this.hasTab) {
                int dy3 = this.y + this.height + Tab.getDefaultHeight() + 10;
            } else {
                int dy4 = this.y + this.height;
            }
            int dy5 = (World.viewHeight - CornerButton.instance.getHeight()) - Tool.img_infoBg.getHeight();
            g.drawImage(Tool.img_infoBg, World.viewWidth - 3, dy5, 24);
            int clr = Tool.getQuanlityColor(selItem.quanlity);
            int bgClr = Tool.CLR_TABLE[11];
            if (selItem.count > 0 && selItem.reqLevel > CommonData.player.level) {
                clr = Tool.CLR_TABLE[7];
            }
            boolean calcw = false;
            int startx = DirectionPad.instance.getWidth() + 46;
            if (this.infow + startx > (DirectionPad.instance.getWidth() + Tool.img_infoBg.getWidth()) - 46) {
                this.infox -= 4;
            } else {
                this.infox = startx;
            }
            if (this.infow == 0) {
                calcw = true;
            }
            int namew = g.getFont().stringWidth(selItem.name);
            g.setClip(startx, dy5 - 2, Tool.img_infoBg.getWidth() - 92, Tool.img_infoBg.getHeight() + 4);
            int dy6 = dy5 + 2;
            int dx5 = this.infox;
            Tool.draw3DString(g, selItem.name, dx5, dy6 + Utilities.LINE_HEIGHT, clr, bgClr, 36);
            if (calcw) {
                this.infow += namew;
            }
            int prx = dx5 + namew + 10;
            Tool.draw3DString(g, "价格", prx, dy6 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], bgClr, 36);
            if (calcw) {
                this.infow += prw + 10;
            }
            if (selItem != null && selItem.count != 0) {
                if (this.showBuyInfo > 0) {
                    if (selItem.prices != null) {
                        int dx6 = prx + prw;
                        for (int i = 0; i < selItem.prices.length; i++) {
                            String s = selItem.prices[i].getPriceString();
                            int clr2 = AbstractUnit.CLR_NAME_NPC;
                            if (!selItem.prices[i].check(CommonData.player)) {
                                clr2 = AbstractUnit.CLR_NAME_TAR_RED;
                            }
                            switch (selItem.prices[i].type) {
                                case 0:
                                    int dw = Tool.drawMoney(g, selItem.prices[i].count, dx6, dy6 + Utilities.LINE_HEIGHT, -1, 36, false);
                                    dx6 += dw + 2;
                                    if (!calcw) {
                                        break;
                                    } else {
                                        this.infow += dw + 2;
                                        break;
                                    }
                                case 1:
                                    String name = selItem.prices[i].needItem.name;
                                    int fw = Utilities.font.stringWidth(name);
                                    GameItem[] gis = CommonData.player.findItems(selItem.prices[i].itemId);
                                    int have = 0;
                                    for (int gi = 0; gi < gis.length; gi++) {
                                        have += gis[gi].count;
                                    }
                                    selItem.prices[i].needItem.drawIcon(g, dx6, (Utilities.LINE_HEIGHT >> 1) + dy6, 6);
                                    Tool.drawNumStr(String.valueOf(have) + "/" + selItem.prices[i].count, g, dx6 + 12, (Utilities.LINE_HEIGHT >> 1) + dy6 + 10, 0, 36, -1);
                                    int w = Tool.getSmallNumWidth(String.valueOf(have) + "/" + selItem.prices[i].count) + 15;
                                    Tool.draw3DString(g, name, dx6 + w, dy6 + Utilities.LINE_HEIGHT, clr2, bgClr, 36);
                                    dx6 += w + fw + 5;
                                    if (!calcw) {
                                        break;
                                    } else {
                                        this.infow += w + fw + 5;
                                        break;
                                    }
                                    break;
                                case 2:
                                    Tool.draw3DString(g, "需要 " + s, dx6, dy6 + Utilities.LINE_HEIGHT, clr2, bgClr, 36);
                                    dx6 = this.x + prw;
                                    dy6 += Utilities.LINE_HEIGHT;
                                    if (!calcw) {
                                        break;
                                    } else {
                                        this.infow += prw;
                                        break;
                                    }
                                case 3:
                                case 4:
                                    int f = 0;
                                    if (selItem.prices[i].type == 3) {
                                        f = (World.tick % 30) + 1;
                                        if (f > 5) {
                                            f = 5;
                                        }
                                    }
                                    Tool.uiMiscImg2.drawFrame(g, f, dx6, dy6 + Utilities.LINE_HEIGHT, 0, 36);
                                    int dx7 = dx6 + 14;
                                    if (calcw) {
                                        this.infow += 14;
                                    }
                                    Tool.drawNumStr(String.valueOf(selItem.prices[i].count), g, dx7, dy6 + Utilities.LINE_HEIGHT, 0, 36, -1);
                                    int nw = Tool.getSmallNumWidth(String.valueOf(selItem.prices[i].count));
                                    int dx8 = dx7 + nw;
                                    if (calcw) {
                                        this.infow += nw;
                                    }
                                    Tool.draw3DString(g, " " + selItem.prices[i].getPriceString(), dx8, dy6 + Utilities.LINE_HEIGHT, clr2, bgClr, 36);
                                    int mw = Utilities.font.stringWidth(" " + selItem.prices[i].getPriceString()) + 4;
                                    dx6 = dx8 + mw;
                                    if (calcw) {
                                        this.infow += mw;
                                    }
                                    if (!(selItem.prices[i].type != 3 || selItem.prices[i].discount == 0 || (World.tick / 3) % 3 == 0)) {
                                        Tool.uiMiscImg2.drawFrame(g, 25, dx6, dy6 + Utilities.LINE_HEIGHT, 0, 36);
                                        if (!calcw) {
                                            break;
                                        } else {
                                            this.infow = Tool.uiMiscImg2.getFrameWidth(25);
                                            break;
                                        }
                                    }
                            }
                        }
                    }
                    if (this.infox + this.infow < DirectionPad.instance.getWidth() + 46) {
                        this.infox = (DirectionPad.instance.getWidth() + Tool.img_infoBg.getWidth()) - 46;
                    }
                } else {
                    Tool.drawMoney(g, selItem.price >> 2, dx5 + 3, dy6 + Utilities.LINE_HEIGHT, -1, 36, false);
                }
            } else {
                return;
            }
        }
        int dx9 = this.x + 5;
        int dy7 = this.y + 5;
        if (selItem != null && selItem.count > 0 && System.currentTimeMillis() - this.timer > 500 && this.show && this.y < World.viewHeight && this.tip != null) {
            this.tip.draw(g);
        }
    }

    public void switchTipMode() {
        if (getItem(getSelIdx()) != null) {
            this.tipMode = 1 - this.tipMode;
            this.lastSelItem = null;
        }
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width2) {
        this.width = width2;
    }

    public int getHeight() {
        if (this.hasTab) {
            return this.height + this.buyHeight + Utilities.LINE_HEIGHT;
        }
        return this.height + this.buyHeight;
    }

    public int getBagHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x2) {
        this.x = x2;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y2) {
        this.y = y2;
        if (this.scroll != null) {
            this.scroll.startBarY = this.scrollY + y2 + 16;
            this.scroll.move(-1);
        }
    }

    public int getMenuSelection() {
        int sel = getSelIdx();
        if (getItem(sel) == null) {
            return -1;
        }
        return sel;
    }
}
