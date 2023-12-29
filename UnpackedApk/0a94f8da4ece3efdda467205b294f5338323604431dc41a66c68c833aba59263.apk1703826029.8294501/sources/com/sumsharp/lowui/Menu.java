package com.sumsharp.lowui;

import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import java.util.Hashtable;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Graphics;

public class Menu extends UI {
    private static final int fw = 64;
    private static boolean[] hasIndent = null;
    private static final int offw = 25;
    private static final int starty = 120;
    private int[] ITEM_AREA;
    private boolean _hasSetItemArea = false;
    private int[] cmdIcons;
    private Menu current = null;
    private int dir;
    private boolean[] enable;
    private int firstRow = 0;
    private int height;
    private int hgap;
    private int indent = offw;
    private boolean isTaskMenu;
    private int itemHeight;
    private int itemOffset = 16;
    private int itemWidth;
    private int maxnum = 10;
    private Object[] menuItems;
    private Hashtable menuMap = new Hashtable();
    private String menuSelCmd = null;
    private int menuSelIdx = 0;
    private int menuSelection = -1;
    private String[] menuStrItems;
    private int mh = HttpConnection.HTTP_OK;
    private int mw = 254;
    private int mx = ((World.viewWidth / 2) - (this.mw / 2));
    private Menu parent;
    private int radioOffset = 6;
    private int scrollHeight = 0;
    private int scrollUnitHeight = 0;
    private int scrollX = 0;
    private int scrollY = 0;
    private int scrolling = 0;
    private int showCount = 6;
    private boolean showScroll = false;
    private int showStep;
    private int subId;
    private String title;
    private Menu toSub = null;
    private int vgap;
    private int width;
    private int x;
    private int y;

    public Menu(String title2, Menu parent2, String[] menuItems2, String[] cmdIcons2, int dir2) {
        this.showCount = (World.viewHeight > 210 ? 210 : World.viewHeight - 50) / Utilities.LINE_HEIGHT;
        int[] icons = null;
        if (cmdIcons2 != null) {
            icons = new int[cmdIcons2.length];
            for (int i = 0; i < cmdIcons2.length; i++) {
                icons[i] = Integer.parseInt(cmdIcons2[i]);
            }
        }
        init(title2, menuItems2, icons, dir2);
        this.parent = parent2;
        if (parent2 != null) {
            parent2.addSubMenu(title2, this);
        }
    }

    public Menu(String title2, Menu parent2, String[] menuItems2, String[] cmdIcons2, int dir2, boolean isTaskMenu2) {
        this.showCount = (World.viewHeight > 210 ? 210 : World.viewHeight - 50) / Utilities.LINE_HEIGHT;
        int[] icons = null;
        if (cmdIcons2 != null) {
            icons = new int[cmdIcons2.length];
            for (int i = 0; i < cmdIcons2.length; i++) {
                icons[i] = Integer.parseInt(cmdIcons2[i]);
            }
        }
        this.isTaskMenu = isTaskMenu2;
        init(title2, menuItems2, icons, dir2);
        this.parent = parent2;
        if (parent2 != null) {
            parent2.addSubMenu(title2, this);
        }
    }

    public void addSubMenu(String title2, Menu menu) {
        this.menuMap.put(title2, menu);
        menu.subId = this.menuMap.size();
    }

    public Menu(String title2, String[] menuItems2, int[] cmdIcons2, int dir2) {
        init(title2, menuItems2, cmdIcons2, dir2);
    }

    public void setItemStatus(int idx, boolean enable2) {
        if (idx >= 0 && idx < this.enable.length) {
            this.enable[idx] = enable2;
        }
    }

    public void setItemStatus(boolean[] enable2) {
        for (int i = 0; i < enable2.length; i++) {
            setItemStatus(i, enable2[i]);
        }
    }

    private void init(String title2, String[] menuItems2, int[] cmdIcons2, int dir2) {
        this.dir = dir2;
        this.cmdIcons = cmdIcons2;
        if (!this.isTaskMenu) {
            for (int i = 0; i < menuItems2.length; i++) {
                menuItems2[i] = menuItems2[i];
            }
        }
        this.menuItems = new Object[menuItems2.length];
        for (int i2 = 0; i2 < menuItems2.length; i2++) {
            this.menuItems[i2] = menuItems2[i2];
        }
        this.menuStrItems = menuItems2;
        this.enable = new boolean[menuItems2.length];
        boolean hasIcon = false;
        if (cmdIcons2 != null) {
            int i3 = 0;
            while (true) {
                if (i3 >= cmdIcons2.length) {
                    break;
                } else if (cmdIcons2[i3] >= 0) {
                    hasIcon = true;
                    break;
                } else {
                    i3++;
                }
            }
        }
        this.title = title2;
        this.itemHeight = 44;
        if (this.isTaskMenu) {
            this.width = World.viewWidth;
        } else {
            this.width = Utilities.font.stringWidth(title2);
        }
        if (menuItems2.length > this.showCount) {
            this.showScroll = true;
            this.width += 5;
        } else {
            this.showCount = menuItems2.length;
        }
        for (int i4 = 0; i4 < menuItems2.length; i4++) {
            int sw = Utilities.font.stringWidth(menuItems2[i4]);
            if (sw > this.width) {
                this.width = sw;
            }
            this.enable[i4] = true;
        }
        this.width += this.itemOffset + 8;
        if (hasIcon) {
            this.width += 20;
        }
        if (this.isTaskMenu) {
            if (this.width > (World.viewWidth >> 1)) {
                this.width = World.viewWidth;
                for (int i5 = 0; i5 < menuItems2.length; i5++) {
                    String str = menuItems2[i5];
                    int msWidth = 64;
                    if (hasIcon && cmdIcons2.length > i5 && cmdIcons2[i5] != -1) {
                        msWidth = 64 - 20;
                    }
                    if (Utilities.font.stringWidth(str) > msWidth) {
                        this.menuItems[i5] = new MovingString(str, 64, 1);
                    }
                }
            }
        } else if (this.width > (World.viewWidth >> 1)) {
            this.width = World.viewWidth >> 1;
            int strWidth = 64;
            if (this.isTaskMenu) {
                strWidth = 64 - 12;
            }
            for (int i6 = 0; i6 < menuItems2.length; i6++) {
                String str2 = menuItems2[i6];
                int msWidth2 = strWidth;
                if (hasIcon && cmdIcons2.length > i6 && cmdIcons2[i6] != -1) {
                    msWidth2 -= 20;
                }
                if (Utilities.font.stringWidth(str2) > msWidth2) {
                    this.menuItems[i6] = new MovingString(str2, 64, 1);
                }
            }
        }
        this.height = (this.itemHeight * (this.showCount + 1)) + 11;
        this.showStep = this.width >> 1;
        if (!this.isTaskMenu) {
            this.y = World.viewHeight - 20;
            if (World.viewHeight > 240) {
                this.y -= 50;
            }
            this.y -= this.height;
        }
        if (this.dir == 4) {
            this.x = World.viewWidth - this.width;
        } else {
            this.showStep = -this.showStep;
            this.x = 0;
        }
        if (this.showScroll) {
            this.scrollHeight = (this.height - this.itemHeight) - 8;
            this.scrollUnitHeight = this.scrollHeight / menuItems2.length;
            this.scrollHeight = this.scrollUnitHeight * menuItems2.length;
            this.scrollX = this.width - 6;
            this.scrollY = this.y + this.itemHeight + 6;
        }
        this._hasSetItemArea = false;
        this.itemWidth = Utilities.font.stringWidth("四字菜单") + 45;
        this.maxnum = 10;
        this.mw = 254;
        this.mh = HttpConnection.HTTP_OK;
        this.mx = (World.viewWidth / 2) - (this.mw / 2);
    }

    public void paint(Graphics g) {
        if (this.current == null || this.current == this) {
            drawUIMenu(g);
        } else {
            this.current.paint(g);
        }
    }

    private void setSelectCmd(String cmd) {
        this.menuSelCmd = cmd;
        if (this.parent != null) {
            this.parent.setSelectCmd(cmd);
        }
    }

    private void setParentClose() {
        if (this.parent != null) {
            this.parent.setParentClose();
        } else {
            this.closed = true;
        }
    }

    public void cycle() {
        if (this.current != null && this.current != this) {
            this.current.cycle();
            if (!this.current.isClosed()) {
                return;
            }
            if (this.current.menuSelection != -1) {
                setSelectCmd(this.menuStrItems[this.current.menuSelection]);
                this.current = null;
                setParentClose();
                return;
            }
            this.current.reset();
            this.current = this.current.parent;
        } else if (this.closed || this.toSub != null) {
            this.show = false;
            if (this.toSub != null) {
                reset();
                this.current = this.toSub;
                this.toSub = null;
            }
        } else if (this.show) {
            if (!this._hasSetItemArea) {
                initItemArea();
                this._hasSetItemArea = true;
            }
            hasPointerEvents();
            World.moveX = -1;
            World.moveY = -1;
            for (int i = 0; i < this.menuItems.length; i++) {
                if (this.menuItems[i] instanceof MovingString) {
                    if (i == this.menuSelIdx) {
                        ((MovingString) this.menuItems[i]).cycle();
                    } else {
                        ((MovingString) this.menuItems[i]).reset();
                    }
                }
            }
            if (this.scrolling > 0) {
                this.scrolling--;
            } else if (this.scrolling < 0) {
                this.scrolling++;
            }
            int scrollDir = 0;
            if (Utilities.isKeyPressed(0, true)) {
                scrollDir = turnUp();
            } else if (Utilities.isKeyPressed(1, true)) {
                scrollDir = turnDown();
            } else if (Utilities.isKeyPressed(2, true)) {
                scrollDir = turnLeft();
            } else if (Utilities.isKeyPressed(3, true)) {
                scrollDir = turnRight();
            } else if (!Utilities.isKeyPressed(9, true) && !Utilities.isKeyPressed(4, true)) {
                for (int i2 = 0; i2 <= 9; i2++) {
                    int key = i2 + 12;
                    if (i2 == 9) {
                        key = 11;
                    }
                    if (Utilities.isKeyPressed(key, true)) {
                        selectItem(i2);
                    }
                }
            } else if (this.enable[this.menuSelIdx]) {
                Menu menu = (Menu) this.menuMap.get(this.menuStrItems[this.menuSelIdx].substring(2));
                if (menu != null) {
                    this.toSub = menu;
                } else {
                    this.menuSelection = this.menuSelIdx;
                    if (this.parent != null) {
                        close();
                    }
                }
            }
            if (this.firstRow + this.showCount <= this.menuSelIdx) {
                this.firstRow = (this.menuSelIdx - this.showCount) + 1;
                this.scrolling = scrollDir;
            }
            if (this.firstRow > this.menuSelIdx) {
                this.firstRow = this.menuSelIdx;
                this.scrolling = scrollDir;
            }
        } else {
            if (this.dir == 4) {
                this.x = World.viewWidth - this.width;
            } else {
                this.x = 0;
            }
            this.show = true;
        }
    }

    private int turnUp() {
        int scrollDir = 0;
        int off = this.menuSelIdx % 2;
        this.menuSelIdx -= 2;
        if (this.menuSelIdx < 0) {
            this.menuSelIdx = (this.menuItems.length - 1) - (1 - off);
            if (this.menuSelIdx < 0) {
                this.menuSelIdx = 0;
            }
        } else {
            scrollDir = -1;
        }
        this.menuSelection = -1;
        return scrollDir;
    }

    private int turnDown() {
        int scrollDir = 0;
        int off = this.menuSelIdx % 2;
        this.menuSelIdx += 2;
        if (this.menuSelIdx >= this.menuItems.length) {
            this.menuSelIdx = (1 - off) + 0;
            if (this.menuSelIdx >= this.menuItems.length) {
                this.menuSelIdx = this.menuItems.length - 1;
            }
        } else {
            scrollDir = 1;
        }
        this.menuSelection = -1;
        return scrollDir;
    }

    private int turnLeft() {
        int scrollDir = 0;
        this.menuSelIdx--;
        if (this.menuSelIdx < 0) {
            this.menuSelIdx = this.menuItems.length - 1;
        } else {
            scrollDir = -1;
        }
        this.menuSelection = -1;
        return scrollDir;
    }

    private int turnRight() {
        int scrollDir = 0;
        this.menuSelIdx++;
        if (this.menuSelIdx >= this.menuItems.length) {
            this.menuSelIdx = 0;
        } else {
            scrollDir = 1;
        }
        this.menuSelection = -1;
        return scrollDir;
    }

    private void initItemArea() {
        int count = this.menuItems.length;
        this.ITEM_AREA = new int[(count * 4)];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < 4; j++) {
                this.ITEM_AREA[(i * 4) + 0] = this.x + 6;
                this.ITEM_AREA[(i * 4) + 1] = (((this.y + 3) + this.itemHeight) + (this.itemHeight * i)) - 2;
                this.ITEM_AREA[(i * 4) + 2] = this.width - 12;
                this.ITEM_AREA[(i * 4) + 3] = this.itemHeight;
            }
        }
    }

    private boolean pointerCheck(int px, int py) {
        return this.x <= px && px <= (this.x + this.width) + 4 && this.y <= py && py <= this.y + this.height;
    }

    public boolean hasPointerEvents() {
        int px = World.pressedx;
        int py = World.pressedy;
        if (px == -1 || py == -1) {
            return false;
        }
        if (handleUIMenu(px, py)) {
            World.pressedx = -1;
            World.pressedy = -1;
            return true;
        }
        if (NewStage.touchNpc == null) {
            if (!this.title.equals("灵兽世界") && !this.title.equals("角色选择")) {
                if (this.title.equals("系统菜单")) {
                    close();
                } else {
                    Utilities.keyPressed(7, true);
                }
            }
        } else if (this.title.equals("系统菜单")) {
            close();
        }
        return false;
    }

    private boolean calcSelected(int x2, int y2) {
        if (x2 > this.ITEM_AREA[0] && x2 < this.ITEM_AREA[2] + this.ITEM_AREA[0]) {
            if (this.showScroll) {
                y2 += (((this.scrollY + (this.firstRow * this.scrollUnitHeight)) - ((this.y + this.itemHeight) + 6)) / this.scrollUnitHeight) * this.itemHeight;
            }
            int i = 0;
            while (i < this.ITEM_AREA.length / 4) {
                if (this.ITEM_AREA[(i * 4) + 1] >= y2 || this.ITEM_AREA[(i * 4) + 1] + this.ITEM_AREA[(i * 4) + 3] <= y2) {
                    i++;
                } else {
                    Utilities.keyPressed(Utilities.KEY_NUMS[i], true);
                    return true;
                }
            }
        } else if (this.showScroll && x2 > this.ITEM_AREA[2] + this.ITEM_AREA[0] && y2 > this.scrollY - 8 && y2 < this.scrollHeight + this.scrollY + 8) {
            int top = this.scrollY + (this.firstRow * this.scrollUnitHeight);
            if (y2 < top) {
                Utilities.keyPressed(1, true);
                return true;
            } else if (y2 > (this.showCount * this.scrollUnitHeight) + top) {
                Utilities.keyPressed(2, true);
                return true;
            }
        }
        return false;
    }

    public void setY(int y2) {
        this.y = y2;
    }

    private void selectItem(int i) {
        if (i < this.menuItems.length) {
            this.menuSelIdx = i;
            if (this.enable[this.menuSelIdx]) {
                Menu menu = (Menu) this.menuMap.get(this.menuStrItems[this.menuSelIdx]);
                if (menu != null) {
                    this.toSub = menu;
                    return;
                }
                this.menuSelection = this.menuSelIdx;
                if (this.parent != null) {
                    close();
                }
            }
        }
    }

    public int getMenuSelection() {
        int ret = this.menuSelection;
        this.menuSelection = -1;
        return ret;
    }

    public String getMenuSelCmd() {
        String cmd = this.menuSelCmd;
        this.menuSelCmd = null;
        return cmd;
    }

    public Menu getParent() {
        return this.parent;
    }

    public void setParent(Menu parent2) {
        this.parent = parent2;
    }

    public void reset() {
        super.reset();
        if (this.dir == 4) {
            this.x = World.viewWidth;
        } else {
            this.x = -this.width;
        }
    }

    public void close() {
        if (this.current == null || this.current == this) {
            this.closed = true;
        } else {
            this.current.close();
        }
    }

    public void setSelIdx(int idx) {
        if (idx >= 0 && idx < this.menuItems.length) {
            this.menuSelIdx = idx;
        }
    }

    private boolean handleUIMenu(int px, int py) {
        int selline;
        if (px <= (World.viewWidth / 2) - this.itemWidth || px >= (World.viewWidth / 2) + this.itemWidth || py <= starty || py >= (this.itemHeight * 5) + starty) {
            return false;
        }
        int selrow = ((py - starty) - 5) / this.itemHeight;
        if (px > World.viewWidth / 2) {
            selline = 1;
        } else {
            selline = 0;
        }
        Utilities.keyPressed(Utilities.KEY_NUMS[(selrow * 2) + selline], true);
        return true;
    }

    static {
        boolean[] zArr = new boolean[10];
        zArr[2] = true;
        zArr[3] = true;
        zArr[6] = true;
        zArr[7] = true;
        hasIndent = zArr;
    }

    private int[] getClr(int i) {
        if (this.enable[i]) {
            if (i == this.menuSelIdx) {
                return new int[]{9633792, 16187066};
            }
            return new int[]{1517399, 12254974};
        } else if (i == this.menuSelIdx) {
            return new int[]{9633792, 16187066};
        } else {
            return new int[]{3487029, 16187066};
        }
    }

    private void drawUIMenu(Graphics g) {
        g.getCanvas().save();
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        int vgap2 = (this.mh - 10) / 5;
        this.itemHeight = vgap2;
        this.mh = (this.itemHeight * 5) + 10;
        int btnw = Tool.img_rectbtn0.getWidth();
        int btnh = Tool.img_rectbtn0.getHeight();
        g.drawImage(Tool.img_menubg, World.viewWidth / 2, (this.mh / 2) + starty, 3);
        int i = 0;
        while (i < this.maxnum) {
            int hgap2 = (hasIndent[i] ? this.indent : 0) + 5;
            if (i % 2 == 0) {
                if (i < this.menuStrItems.length) {
                    if (this.enable[i]) {
                        if (i == this.menuSelIdx) {
                            g.drawImage(Tool.img_menuL[1], this.mx + hgap2, ((i / 2) * vgap2) + 123);
                        } else {
                            g.drawImage(Tool.img_menuL[0], this.mx + hgap2, ((i / 2) * vgap2) + 123);
                        }
                    } else if (i == this.menuSelIdx) {
                        g.drawImage(Tool.img_menuL[2], this.mx + hgap2, ((i / 2) * vgap2) + 123);
                    } else {
                        g.drawImage(Tool.img_menuL[2], this.mx + hgap2, ((i / 2) * vgap2) + 123);
                    }
                    if (this.menuItems[i] instanceof String) {
                        Tool.drawLevelString(g, this.menuStrItems[i], this.mx + hgap2 + (btnw / 2), ((i / 2) * vgap2) + 123 + (btnh / 2) + 7, 33, this.enable[i] ? 2 : 8, i == this.menuSelIdx ? 1 : 0);
                    } else {
                        ((MovingString) this.menuItems[i]).draw3D(g, (((this.mx + hgap2) + btnw) - offw) - 64, ((i / 2) * vgap2) + 123 + (btnh / 2) + 7, getClr(i)[0], getClr(i)[1], 36);
                    }
                } else {
                    g.drawImage(Tool.img_menuL[2], this.mx + hgap2, ((i / 2) * vgap2) + 123);
                }
            } else if (i < this.menuStrItems.length) {
                if (this.enable[i]) {
                    if (i == this.menuSelIdx) {
                        g.drawImage(Tool.img_menuR[1], (this.mx + this.mw) - hgap2, ((i / 2) * vgap2) + 123, 8);
                    } else {
                        g.drawImage(Tool.img_menuR[0], (this.mx + this.mw) - hgap2, ((i / 2) * vgap2) + 123, 8);
                    }
                } else if (i == this.menuSelIdx) {
                    g.drawImage(Tool.img_menuR[2], (this.mx + this.mw) - hgap2, ((i / 2) * vgap2) + 123, 8);
                } else {
                    g.drawImage(Tool.img_menuR[2], (this.mx + this.mw) - hgap2, ((i / 2) * vgap2) + 123, 8);
                }
                if (this.menuItems[i] instanceof String) {
                    Tool.drawLevelString(g, this.menuStrItems[i], ((this.mx + this.mw) - hgap2) - (btnw / 2), ((i / 2) * vgap2) + 123 + (btnh / 2) + 7, 33, this.enable[i] ? 2 : 8, i == this.menuSelIdx ? 1 : 0);
                } else {
                    ((MovingString) this.menuItems[i]).draw3D(g, (((this.mx + this.mw) - hgap2) - btnw) + offw, ((i / 2) * vgap2) + 123 + (btnh / 2) + 7, getClr(i)[0], getClr(i)[1], 36);
                }
            } else {
                g.drawImage(Tool.img_menuR[2], (this.mx + this.mw) - hgap2, ((i / 2) * vgap2) + 123, 8);
            }
            i++;
        }
        g.getCanvas().restore();
    }
}
