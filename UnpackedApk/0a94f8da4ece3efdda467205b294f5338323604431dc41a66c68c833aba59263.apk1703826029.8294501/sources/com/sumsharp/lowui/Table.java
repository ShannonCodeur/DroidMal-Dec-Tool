package com.sumsharp.lowui;

import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;

public class Table extends UI {
    private static final String TABLE_ICON_ID = "icon";
    private int[] Item_Area;
    private boolean autoExpend = true;
    private int calcIdx = 0;
    private boolean decScrollWidth = false;
    private boolean[] enable;
    private int firstRow;
    private int flingLen;
    private int gix = 0;
    private int giy = 0;
    private boolean hasTab;
    private int height;
    private boolean isSticked;
    private int itemCount;
    private int itemHeight;
    private Hashtable itemTable = new Hashtable();
    private int lastOffy;
    private MovingString ms = null;
    private int oriY;
    private UI parent;
    private int preclick = -1;
    private int rh;
    private ScrollBar scroll;
    private int scrollBoxHeight;
    private int scrollHeight = 0;
    private int scrollOffset;
    private int scrollUnitHeight = 0;
    private int scrollX = 0;
    private int scrollY = 0;
    private int scrolling = 0;
    private int selection;
    private int showCount;
    private boolean showName = true;
    private boolean showScroll = false;
    private Tip tip;
    protected long tipTimer = -1;
    private String[] titles;
    private int width;
    private int[] widths;
    private int x;
    private int y;

    public Table(int x2, int y2, int width2, int height2, String[] titles2, int[] widths2) {
        this.x = x2 + 50;
        this.y = y2;
        this.width = width2 - 90;
        this.height = height2;
        this.titles = titles2;
        this.widths = widths2;
        this.calcIdx = -1;
        this.showScroll = false;
        this.hasTab = false;
        int allWidth = 0;
        for (int i = 0; i < widths2.length; i++) {
            if (widths2[i] != -1) {
                allWidth += widths2[i];
            } else {
                this.calcIdx = i;
            }
        }
        if (this.calcIdx >= 0) {
            this.widths[this.calcIdx] = (this.width - allWidth) - 20;
        }
        if (this.calcIdx == -1) {
            this.calcIdx = 0;
            for (int i2 = 1; i2 < widths2.length; i2++) {
                if (widths2[i2] > widths2[this.calcIdx]) {
                    this.calcIdx = i2;
                }
            }
        }
        this.itemHeight = Tool.img_tableBtn[0].getHeight();
        if (this.itemHeight < 20) {
            this.itemHeight = 20;
        }
        this.showCount = ((height2 - 4) - Utilities.LINE_HEIGHT) / this.itemHeight;
        this.lastOffy = -1;
        this.isSticked = false;
    }

    private int getTabWidth(String title) {
        if (title.equals(TABLE_ICON_ID)) {
            title = "";
        }
        for (int i = 0; i < this.titles.length; i++) {
            if (this.titles[i].equals(title)) {
                return this.widths[i];
            }
        }
        return -1;
    }

    public void setItemStatus(boolean[] enable2) {
        this.enable = enable2;
    }

    public void addTableItem(String id, String[] items, int type, int[] clr, int[] bgclr) {
        TableItem item = new TableItem(id, this, items, type, getTabWidth(id));
        if (clr == null) {
            item.clr = null;
        } else {
            item.clr = new int[clr.length];
            System.arraycopy(clr, 0, item.clr, 0, clr.length);
        }
        if (bgclr == null) {
            item.bgclr = null;
        } else {
            item.bgclr = new int[bgclr.length];
            System.arraycopy(bgclr, 0, item.bgclr, 0, clr.length);
        }
        this.itemTable.put(id, item);
        this.itemCount = items.length;
        if (this.itemCount > this.showCount) {
            this.showScroll = true;
            this.scrollHeight = this.height - Utilities.LINE_HEIGHT;
            this.scrollUnitHeight = this.scrollHeight / this.itemCount;
            this.scrollBoxHeight = (this.scrollHeight * this.showCount) / this.itemCount;
            this.rh = ((this.scrollHeight - (this.scrollUnitHeight * this.itemCount)) * 1000) / (this.itemCount - this.showCount);
            this.scrollX = this.width + 75;
            this.scrollY = this.y + Utilities.LINE_HEIGHT;
            if (!this.decScrollWidth) {
                int[] iArr = this.widths;
                int i = this.calcIdx;
                iArr[i] = iArr[i] - 10;
                this.decScrollWidth = true;
            }
        } else {
            this.showScroll = false;
            if (this.decScrollWidth) {
                int[] iArr2 = this.widths;
                int i2 = this.calcIdx;
                iArr2[i2] = iArr2[i2] + 10;
                this.decScrollWidth = false;
            }
        }
        this.tipTimer = -1;
        if (this.selection >= item.items.length) {
            return;
        }
        if (!id.startsWith("appenddesc:")) {
            Tip t = Tip.createTip(item, (this.width - 25) - 10);
            if (t == null) {
                return;
            }
            if (this.tip == null) {
                this.tip = t;
                this.tip.setSelection(this.selection);
            } else if (this.tip.isMovingString) {
                this.tip = t;
                this.tip.setSelection(this.selection);
            }
        } else if (this.tip != null) {
            this.tip.setAppend(item);
        }
    }

    public void setTitles(String[] titles2) {
        this.titles = titles2;
    }

    public void setHasTab(boolean hasTab2) {
        this.hasTab = hasTab2;
    }

    public void paint(Graphics g) {
        boolean e;
        if (!this.hasTab) {
            Tool.drawAlphaBox(g, this.width + 90, this.height, this.x - 50, this.y, -1727973027);
        }
        Tool.fillAlphaRect(g, -16696995, this.x - 42, this.y + 6, this.width + 77, 18);
        int dx = this.x + 50;
        boolean[] drawSelection = new boolean[this.itemCount];
        this.ms = null;
        boolean hasItem = false;
        g.getCanvas().save();
        for (int i = 0; i < this.titles.length; i++) {
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            int itemHeight2 = this.itemHeight;
            int dy = this.y + 2;
            if (!this.titles[i].equals("") && !this.titles[i].startsWith("hide:") && !this.titles[i].startsWith("desc:") && !this.titles[i].startsWith("taskdesc:") && !this.titles[i].startsWith("appenddesc:")) {
                String str = this.titles[i];
                if (str.toLowerCase().equals("hp")) {
                    Tool.uiMiscImg.drawFrame(g, 72, dx + (this.widths[i] >> 1), (Utilities.CHAR_HEIGHT >> 1) + dy + 3, 0, 3);
                } else if (str.toLowerCase().equals("mp")) {
                    Tool.uiMiscImg.drawFrame(g, 73, dx + (this.widths[i] >> 1), (Utilities.CHAR_HEIGHT >> 1) + dy + 3, 0, 3);
                } else if (str.toLowerCase().equals("lv")) {
                    Tool.uiMiscImg.drawFrame(g, 32, dx + (this.widths[i] >> 1), (Utilities.CHAR_HEIGHT >> 1) + dy + 3, 0, 3);
                } else {
                    Tool.drawLevelString(g, this.titles[i], dx + ((this.widths[i] - Utilities.font.stringWidth(this.titles[i])) >> 1), dy + 3, 20, 4, 0);
                }
            }
            int dy2 = this.y + 20 + Utilities.LINE_HEIGHT;
            TableItem item = (TableItem) this.itemTable.get(this.titles[i]);
            if (item != null) {
                if (this.show) {
                    g.setClip(0, this.y + 6 + Utilities.LINE_HEIGHT + 2, World.viewWidth, this.showCount * this.itemHeight);
                }
                int start = this.firstRow;
                int end = this.firstRow + this.showCount;
                int idx = start;
                while (idx < end && idx < item.items.length) {
                    hasItem = true;
                    item.selection = this.selection;
                    if (this.enable == null || this.enable.length <= idx) {
                        e = true;
                    } else {
                        e = this.enable[idx];
                    }
                    if (!drawSelection[idx]) {
                        if (idx == this.selection) {
                            Tool.drawBlurRect(g, (this.x + 3) - 30, dy2 + 3, ((this.width - 6) - ((this.scroll == null || !this.showScroll) ? 0 : this.scroll.getWidth())) + 60, itemHeight2 - 20, 1);
                            if (this.preclick == idx) {
                                g.drawImage(Tool.img_tableBtn[1], this.x - 50, dy2 - 8);
                            } else {
                                g.drawImage(Tool.img_tableBtn[0], this.x - 50, dy2 - 8);
                            }
                            drawSelection[idx] = true;
                        } else {
                            Tool.drawBlurRect(g, (this.x + 3) - 30, dy2 + 3, ((this.width - 6) - ((this.scroll == null || !this.showScroll) ? 0 : this.scroll.getWidth())) + 60, itemHeight2 - 20, 0);
                            if (this.preclick == idx) {
                                g.drawImage(Tool.img_tableBtn[1], this.x - 50, dy2 - 8);
                            } else {
                                g.drawImage(Tool.img_tableBtn[0], this.x - 50, dy2 - 8);
                            }
                            drawSelection[idx] = true;
                        }
                    }
                    if (!this.titles[i].startsWith("desc") && !this.titles[i].startsWith("appenddesc:") && !this.titles[i].startsWith("taskdesc:")) {
                        item.draw(g, dx, dy2 + 6, this.widths[i], itemHeight2, idx, e, idx == this.selection);
                        if (item.type == 0 && (item.items[idx] instanceof MovingString)) {
                            if (idx == this.selection) {
                                this.ms = (MovingString) item.items[idx];
                                this.gix = this.x + 9;
                                this.giy = dy2 + itemHeight2;
                            } else {
                                ((MovingString) item.items[idx]).reset();
                            }
                        }
                    }
                    if (idx == this.selection && this.tipTimer != -1) {
                        if (item.type == 8 || item.type == 16 || item.type == 18 || item.type == 19) {
                            this.gix = this.x + 9;
                            this.giy = dy2 + itemHeight2;
                        } else if (item.type == 10) {
                            this.gix = this.x + 9;
                            this.giy = dy2 + itemHeight2;
                        } else if (item.type == 14) {
                            this.gix = this.x + 9;
                            this.giy = dy2 + itemHeight2;
                        } else if (item.id.startsWith("desc")) {
                            this.gix = this.x + 9;
                            this.giy = dy2 + itemHeight2;
                        } else if (item.id.startsWith("taskdesc")) {
                            this.gix = this.x + 9;
                            this.giy = dy2 + itemHeight2;
                        }
                        this.titles[i].startsWith("appenddesc:");
                    }
                    dy2 += itemHeight2;
                    idx++;
                }
            }
            dx += this.widths[i];
        }
        int i2 = Tool.CLR_TABLE[6];
        int i3 = Tool.CLR_TABLE[4];
        if (this.show && this.showScroll && this.scroll != null) {
            this.scroll.paint(g);
        }
        if (this.tipTimer != -1 && hasItem) {
            long sub = System.currentTimeMillis() - this.tipTimer;
            int tipWidth = (this.width - 25) - (this.showScroll ? 10 : 0);
            if (sub > 500) {
                g.setClip(0, 0, World.viewWidth, World.viewHeight);
                if (this.show && this.tip != null && !this.isSticked) {
                    if ((this.giy - 5) + this.tip.getHeight() <= this.y + this.height || this.y + this.height <= World.viewHeight - DirectionPad.instance.getHeight()) {
                        this.tip.setBounds(this.gix + 4, this.giy - 18, tipWidth - 8, -1, false);
                    } else {
                        this.tip.setBounds(this.gix + 4, ((this.giy - this.itemHeight) - this.tip.getHeight()) + 5, tipWidth - 8, -1, true);
                    }
                    this.tip.draw(g);
                }
            }
        }
        g.getCanvas().restore();
    }

    public void remove(int index) {
        for (String str : this.titles) {
            ((TableItem) this.itemTable.get(str)).remove(index);
        }
    }

    public void setOriPos() {
        this.oriY = this.y;
    }

    private boolean inArea(int px, int py) {
        if (px <= this.x - 50 || px >= this.x + this.width || py <= this.y || py >= this.y + this.height) {
            return false;
        }
        return true;
    }

    public void notifySticked(boolean isstick) {
        this.isSticked = isstick;
        this.lastOffy = World.pressedy - this.y;
        this.flingLen = 0;
    }

    private boolean handleMoveEvents() {
        if (inArea(World.moveX, World.moveY)) {
            int woff = 0;
            if (this.showScroll) {
                woff = -16;
            }
            if (World.moveX > this.Item_Area[0] - 80 && World.moveX < this.Item_Area[2] + this.Item_Area[0] + woff) {
                int i = 0;
                while (i < this.Item_Area.length / 4) {
                    if (this.Item_Area[(i * 4) + 1] >= World.moveY || this.Item_Area[(i * 4) + 1] + this.Item_Area[(i * 4) + 3] <= World.moveY) {
                        i++;
                    } else {
                        this.preclick = i;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean handleReleaseEvents() {
        if (World.releasedx != -1 && World.releasedy != -1 && inArea(World.releasedx, World.releasedy) && this.flingLen == 0) {
            int rx = World.releasedx;
            int ry = World.releasedy;
            if (rx > this.x - 50 && rx < this.x + this.width && ry > this.y && ry < this.y + this.height) {
                int woff = 0;
                if (this.showScroll) {
                    woff = -16;
                }
                if (rx > this.Item_Area[0] - 80 && rx < this.Item_Area[2] + this.Item_Area[0] + woff) {
                    int old = this.selection;
                    boolean selChanged = false;
                    for (int i = 0; i < this.Item_Area.length / 4; i++) {
                        if (this.Item_Area[(i * 4) + 1] - 5 < ry && this.Item_Area[(i * 4) + 1] + this.Item_Area[(i * 4) + 3] + 5 > ry) {
                            this.selection = this.firstRow + i;
                            selChanged = true;
                            for (int j = 0; j < this.itemTable.size(); j++) {
                                if (((TableItem) this.itemTable.get(this.titles[j])).type == 15) {
                                    int ix = getItemX(j);
                                    int iw = this.widths[j];
                                    if (World.pressedx > ix - 10 && World.pressedx < ix + iw + 10) {
                                        Utilities.keyPressed(4, true);
                                    }
                                }
                            }
                        }
                    }
                    if (old == this.selection && selChanged) {
                        Utilities.keyPressed(5, true);
                    }
                    this.preclick = -1;
                } else if (this.showScroll && rx > this.Item_Area[2] + this.Item_Area[0] + woff && ry > this.y + this.scrollY && ry < this.y + this.scrollHeight + this.scrollY) {
                    int mid = this.y + this.scrollY + (this.scrollHeight >> 1);
                    if (ry <= mid) {
                        this.selection = this.firstRow - 1;
                        if (this.selection < 0) {
                            this.selection = 0;
                        }
                    } else if (ry > mid) {
                        this.selection = this.firstRow + this.showCount;
                        if (this.selection >= this.itemCount) {
                            this.selection = this.itemCount - 1;
                        }
                    }
                    this.needsUpdate = true;
                }
                if (this.tip != null) {
                    this.tip.setSelection(this.selection);
                    this.tipTimer = -1;
                }
                return true;
            }
        }
        return false;
    }

    private void clearMoveData() {
        this.isSticked = false;
        this.lastOffy = -1;
        this.flingLen = 0;
        if (this.tip != null) {
            this.tip.setSelection(this.selection);
            this.tipTimer = -1;
        }
    }

    public void cycle() {
        super.cycle();
        if (this.show) {
            boolean handleScroll = false;
            if (this.showScroll && this.scroll != null) {
                handleScroll = this.scroll.handlePoints();
            }
            if (!handleScroll) {
                handlePressEvents();
                handleMoveEvents();
                handleReleaseEvents();
            } else {
                this.needsUpdate = true;
            }
            if (this.showScroll) {
                if (this.scroll == null) {
                    this.scrollY = this.y + Utilities.LINE_HEIGHT + 20;
                    this.scroll = new ScrollBar(this.scrollX, this.scrollY, this.scrollHeight - 32, this.itemCount, this.itemCount * this.itemHeight, this.showCount, this.showCount * this.itemHeight, this.itemHeight);
                }
                this.scroll.actioned = false;
            }
        }
        if (Utilities.isKeyPressed(0, true) || Utilities.isKeyPressed(13, true)) {
            this.selection--;
            if (this.selection < 0) {
                this.selection = this.itemCount - 1;
                if (this.showScroll) {
                    this.scroll.move(3);
                }
            }
            this.tipTimer = -1;
            if (this.tip != null) {
                this.tip.setSelection(this.selection);
            }
        } else if (Utilities.isKeyPressed(1, true) || Utilities.isKeyPressed(19, true)) {
            this.selection++;
            if (this.selection >= this.itemCount) {
                this.selection = 0;
                if (this.showScroll) {
                    this.scroll.move(2);
                }
            }
            this.tipTimer = -1;
            if (this.tip != null) {
                this.tip.setSelection(this.selection);
            }
        }
        if (this.tipTimer == -1) {
            this.tipTimer = System.currentTimeMillis();
        }
        if (this.firstRow + this.showCount <= this.selection) {
            this.firstRow = (this.selection - this.showCount) + 1;
            this.needsUpdate = true;
            if (this.showScroll) {
                this.scroll.move(1);
            }
        }
        if (this.firstRow > this.selection) {
            this.firstRow = this.selection;
            this.needsUpdate = true;
            if (this.showScroll) {
                this.scroll.move(0);
            }
        }
        if (this.firstRow < 0) {
            this.firstRow = 0;
        }
        if (this.ms != null) {
            this.ms.cycle();
        }
        if (this.show && this.needsUpdate) {
            setItemArea(this.showCount);
        }
        if (this.show && this.tip != null) {
            this.tip.cycle();
        }
    }

    public int getMenuSelection() {
        return this.selection;
    }

    public void setSelection(int sel) {
        this.selection = sel;
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
            this.scrollY = Utilities.LINE_HEIGHT + y2 + 20;
            this.scroll.startBarY = this.scrollY + 16;
            this.scroll.move(-1);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width2) {
        this.width = width2;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public void setAutoExpend(boolean autoExpend2) {
        this.autoExpend = autoExpend2;
    }

    public void forceUpdate(boolean force, int count) {
        this.needsUpdate = force;
        setItemArea(count);
    }

    public void setItemArea(int count) {
        int i;
        if (this.needsUpdate) {
            this.Item_Area = new int[(count * 4)];
            for (int i2 = 0; i2 < count; i2++) {
                this.Item_Area[(i2 * 4) + 0] = this.x + 20;
                this.Item_Area[(i2 * 4) + 1] = this.y + 10 + Utilities.LINE_HEIGHT + (this.itemHeight * i2);
                int[] iArr = this.Item_Area;
                int i3 = (i2 * 4) + 2;
                int i4 = this.width - 6;
                if (this.showScroll) {
                    i = 10;
                } else {
                    i = 0;
                }
                iArr[i3] = i4 - i;
                this.Item_Area[(i2 * 4) + 3] = this.itemHeight - 2;
            }
            this.needsUpdate = false;
        }
    }

    public void handlePoints(int x2, int y2) {
    }

    private boolean handlePressEvents() {
        this.preclick = -1;
        if (World.pressedx == -1 && World.pressedy == -1) {
            return false;
        }
        int px = World.pressedx;
        int py = World.pressedy;
        if (this.Item_Area == null) {
            return false;
        }
        if (px == -1 && py == -1) {
            return false;
        }
        if (this.itemCount <= 0) {
            return false;
        }
        return false;
    }

    private int getItemX(int index) {
        if (this.itemTable == null || this.itemTable.size() == 0) {
            return -1;
        }
        if (index < 0 || index > this.itemTable.size()) {
            return -1;
        }
        int off = this.x + 50;
        for (int i = 0; i < index; i++) {
            off += this.widths[i];
        }
        return off;
    }

    public int getItemCount() {
        return this.itemCount;
    }

    public void setParent(UI parent2) {
        this.parent = parent2;
    }

    public Object findData(int type, int id) {
        if (this.parent != null) {
            return this.parent.findData(type, id);
        }
        return null;
    }

    public void clear() {
        if (this.itemTable != null && this.itemTable.size() > 0) {
            this.itemTable = new Hashtable();
        }
        clearTipStatus();
        this.selection = 0;
        if (this.showScroll) {
            this.showScroll = false;
            this.scroll = null;
        }
    }

    private void clearTipStatus() {
        if (this.tip != null) {
            this.tip = null;
        }
    }
}
