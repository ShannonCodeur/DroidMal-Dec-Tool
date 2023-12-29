package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.item.GameItem;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class PanelUI extends UI {
    private int contentHeight = (((World.viewHeight - this.contentY) - CornerButton.instance.getHeight()) - 5);
    private int contentSpeed;
    private int contentY = (this.titleY + this.titleHeight);
    private int currPage = 0;
    private int currPageSelection = 0;
    private int gix = 0;
    private int giy = 0;
    private int icon;
    private Vector items = new Vector();
    private int pageBarX;
    private int pageSize = 0;
    private int pageSpeed;
    private int[] selLinCount;
    private int showContentLine = ((this.contentHeight - 10) / Utilities.LINE_HEIGHT);
    private boolean showName = true;
    private Tip tip;
    private long tipTimer = -1;
    private String title;
    private int titleHeight = Utilities.TITLE_HEIGHT;
    private int titleSpeed;
    private int titleY = NewStage.screenY;
    private int width = (World.viewWidth - 8);
    private int x = 4;

    public PanelUI(int icon2, String title2) {
        this.icon = icon2;
        this.title = title2;
        this.titleSpeed = (this.titleHeight + NewStage.screenY) >> 1;
        this.contentSpeed = this.contentHeight >> 1;
        this.titleY = -this.titleHeight;
        this.contentY = World.viewHeight;
        this.tipTimer = -1;
        this.pageBarX = World.viewWidth;
        this.tip = null;
    }

    public void addNormalData(int page, int id, int iconId, String item, int count, int needCount, int itemType, int clr) {
        if (itemType == 1) {
            addGameItemData(page, id, iconId, item, count, needCount, clr);
        } else if (itemType == 3) {
            StringDraw stringDraw = new StringDraw(item, this.width - 20, this.contentHeight - 10);
            PanelItem panelItem = new PanelItem(page, itemType, stringDraw, clr);
            this.items.addElement(panelItem);
        } else if (itemType == 4) {
            PanelItem panelItem2 = new PanelItem(page, itemType, item, clr);
            MovingString movingString = new MovingString(item, this.width - 20, 2);
            panelItem2.setItem(movingString);
            this.items.addElement(panelItem2);
        } else if (itemType == 6) {
            PanelItem panelItem3 = new PanelItem(page, itemType, item, clr);
            panelItem3.setTime((long) count);
            this.items.addElement(panelItem3);
        } else {
            PanelItem panelItem4 = new PanelItem(page, itemType, item, clr);
            if (panelItem4.getType() != 1 && Utilities.font.stringWidth(item) > this.width - 20) {
                panelItem4.setType(4);
                MovingString movingString2 = new MovingString(item, this.width - 20, 2);
                panelItem4.setItem(movingString2);
            }
            this.items.addElement(panelItem4);
        }
    }

    public void addGameItemData(int page, int id, int iconId, String name, int count, int needCount, int clr) {
        GameItem gi = new GameItem();
        gi.id = id;
        gi.iconId = iconId;
        gi.name = name;
        gi.type = 1;
        gi.count = (byte) count;
        PanelItem pi = new PanelItem(page, 1, gi, clr);
        pi.setNeedCount(needCount);
        this.items.addElement(pi);
    }

    public void refresh() {
        this.currPage = 0;
        int lines = 0;
        int currPage2 = -1;
        Vector needAdd = new Vector();
        for (int i = 0; i < this.items.size(); i++) {
            PanelItem pi = (PanelItem) this.items.elementAt(i);
            if (currPage2 == -1) {
                currPage2 = pi.getPage();
            }
            if (pi.getPage() == currPage2) {
                while (pi.getLineCount() + lines > this.showContentLine) {
                    if (pi.getType() == 3) {
                        StringDraw newsd = ((StringDraw) pi.getItem()).split(this.showContentLine - lines);
                        PanelItem panelItem = new PanelItem(pi.getPage(), pi.getType(), newsd, pi.getClr());
                        needAdd.addElement(panelItem);
                    }
                    for (int j = i; j < this.items.size(); j++) {
                        PanelItem pp = (PanelItem) this.items.elementAt(j);
                        pp.setPage(pp.getPage() + 1);
                        pp.setStartPage(pp.getPage());
                    }
                    currPage2++;
                    lines = 0;
                }
                lines += pi.getLineCount();
            } else {
                lines = 1;
                currPage2++;
            }
        }
        this.pageSize = currPage2 + 1;
        for (int i2 = 0; i2 < needAdd.size(); i2++) {
            this.items.addElement((PanelItem) needAdd.elementAt(i2));
        }
        this.selLinCount = new int[this.pageSize];
        int p = 0;
        int c = 0;
        for (int i3 = 0; i3 < this.items.size(); i3++) {
            PanelItem pi2 = (PanelItem) this.items.elementAt(i3);
            if (pi2.getPage() != p) {
                this.selLinCount[p] = c;
                c = 0;
                p = pi2.getPage();
            }
            if (pi2.getType() == 1 || pi2.getType() == 2 || pi2.getType() == 4) {
                c++;
            }
            if (i3 == this.items.size() - 1 && this.selLinCount[p] < c) {
                this.selLinCount[p] = c;
            }
        }
    }

    public void clear() {
        this.items.removeAllElements();
    }

    public void cycle() {
        super.cycle();
        if (this.closed) {
            this.tipTimer = -1;
            this.titleY -= this.titleSpeed;
            this.contentY += this.contentSpeed;
            if (this.titleY < 0) {
                this.show = false;
                this.pageBarX = World.viewWidth;
            }
        } else if (this.show) {
            if (this.pageSize <= 1 || this.pageBarX <= World.viewWidth - 80) {
                this.pageBarX = World.viewWidth - 80;
            } else {
                this.pageBarX -= 20;
            }
            moveBtn();
            if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
                prevPage();
                this.tipTimer = -1;
                this.tip = null;
            } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
                nextPage();
                this.tipTimer = -1;
                this.tip = null;
            } else if (Utilities.isKeyPressed(0, true) || Utilities.isKeyPressed(13, true)) {
                this.currPageSelection--;
                if (this.currPageSelection < 0) {
                    this.currPageSelection = 0;
                }
                this.tipTimer = -1;
                this.tip = null;
            } else if (Utilities.isKeyPressed(1, true) || Utilities.isKeyPressed(19, true)) {
                this.currPageSelection++;
                if (this.selLinCount != null && this.currPageSelection >= this.selLinCount[this.currPage]) {
                    this.currPageSelection = this.selLinCount[this.currPage] - 1;
                }
                this.tipTimer = -1;
                this.tip = null;
            }
            int c = -1;
            if (this.currPageSelection < this.items.size()) {
                for (int i = 0; i < this.items.size(); i++) {
                    PanelItem pi = (PanelItem) this.items.elementAt(i);
                    if (pi.getType() == 1 || pi.getType() == 2 || pi.getType() == 4) {
                        c++;
                        if (pi.getType() == 4) {
                            if (c == this.currPageSelection) {
                                ((MovingString) pi.getItem()).cycle();
                            } else {
                                ((MovingString) pi.getItem()).reset();
                            }
                        } else if (pi.getType() == 1 && c == this.currPageSelection && this.tip == null) {
                            this.tip = Tip.createTip((GameItem) pi.getItem(), this.width - 20);
                        }
                    }
                }
            }
            if (this.tip != null) {
                this.tip.cycle();
            }
        } else {
            this.titleY += this.titleSpeed;
            this.contentY -= this.contentSpeed;
            int setShow = 0;
            if (this.titleY > NewStage.screenY + 5) {
                this.titleY = NewStage.screenY + 5;
                setShow = 0 + 1;
            }
            int titleY2 = NewStage.screenY + 5;
            if (this.contentY < this.titleHeight + titleY2) {
                this.contentY = this.titleHeight + titleY2;
                setShow++;
            }
            if (setShow == 2) {
                this.tipTimer = -1;
                this.show = true;
            }
        }
    }

    public void nextPage() {
        int cp = this.currPage;
        this.currPage++;
        if (this.currPage >= this.pageSize) {
            this.currPage = this.pageSize - 1;
        }
        if (cp != this.currPage) {
            this.currPageSelection = 0;
        }
    }

    public void prevPage() {
        int cp = this.currPage;
        this.currPage--;
        if (this.currPage < 0) {
            this.currPage = 0;
        }
        if (cp != this.currPage) {
            this.currPageSelection = 0;
        }
    }

    public void paint(Graphics g) {
        int dx;
        String str;
        Tool.drawAlphaBox(g, World.viewWidth, this.titleHeight, 0, this.titleY, -436200402);
        Tool.drawAlphaBox(g, this.width, this.contentHeight, this.x, this.contentY, -1, true);
        int dx2 = this.x + 8;
        if (this.icon != -1) {
            Tool.uiMiscImg.drawFrame(g, this.icon, dx2 + 3, (this.titleY + this.titleHeight) - 2, 0, 36);
            dx = dx2 + Tool.uiMiscImg.getFrameWidth(this.icon);
        } else {
            dx = dx2;
        }
        Tool.drawLevelString(g, this.title, dx, (this.titleY + this.titleHeight) - 8, 36, 1, 0);
        int dy = this.contentY + 5;
        int selection = 0;
        for (int i = 0; i < this.items.size(); i++) {
            int dx3 = this.x + 10;
            PanelItem pi = (PanelItem) this.items.elementAt(i);
            if (pi.getPage() == this.currPage) {
                switch (pi.getType()) {
                    case 0:
                        Tool.draw3DString(g, pi.getItem().toString(), dx3, (Utilities.LINE_HEIGHT + dy) - 2, pi.getClr(), Tool.CLR_TABLE[11], 36);
                        dy += Utilities.LINE_HEIGHT;
                        break;
                    case 1:
                        if (this.currPageSelection == selection) {
                            Tool.drawBlurRect(g, dx3 - 5, dy + 2, this.width - 10, Utilities.CHAR_HEIGHT + Utilities.CHAR_HEIGHT, 1);
                        } else {
                            Tool.drawBlurRect(g, dx3 - 5, dy + 2, this.width - 10, Utilities.CHAR_HEIGHT + Utilities.CHAR_HEIGHT, 0);
                        }
                        if (pi.getNeedCount() > 0) {
                            Tool.draw3DString(g, "获得", dx3, (Utilities.LINE_HEIGHT + dy) - 2, pi.getClr(), Tool.CLR_TABLE[11], 36);
                            dx3 += Utilities.font.stringWidth("获得") + 2;
                        }
                        GameItem gi = (GameItem) pi.getItem();
                        if (gi.type > 0) {
                            gi.drawIcon(g, dx3, Utilities.LINE_HEIGHT + dy, 6);
                        }
                        if (pi.getNeedCount() > 0) {
                            int c = gi.count;
                            if (c > pi.getNeedCount()) {
                                c = pi.getNeedCount();
                            }
                            str = String.valueOf("") + " [" + c + "/" + pi.getNeedCount() + "] " + (c == pi.getNeedCount() ? "(完成)" : "");
                        } else {
                            str = String.valueOf("") + " " + gi.count;
                        }
                        if (((this.width - 10) - Utilities.font.stringWidth(str)) - dx3 > Utilities.font.stringWidth(gi.name)) {
                            str = gi.name + " " + str;
                            this.showName = false;
                        }
                        Tool.draw3DString(g, str, dx3 + 40, ((Utilities.LINE_HEIGHT + dy) - 2) + 5, pi.getClr(), Tool.CLR_TABLE[11], 36);
                        dy += Utilities.LINE_HEIGHT + Utilities.LINE_HEIGHT;
                        if (this.currPageSelection == selection && this.tipTimer == -1) {
                            this.tipTimer = System.currentTimeMillis();
                            this.gix = this.x + 10;
                            this.giy = dy;
                        }
                        selection++;
                        break;
                    case 2:
                        if (this.currPageSelection == selection) {
                            Tool.drawBlurRect(g, dx3 - 5, dy + 2, this.width - 10, Utilities.CHAR_HEIGHT, 1);
                        } else {
                            Tool.drawBlurRect(g, dx3 - 5, dy + 2, this.width - 10, Utilities.CHAR_HEIGHT, 0);
                        }
                        Tool.drawMoney(g, Integer.parseInt(pi.getItem().toString()), dx3, (Utilities.LINE_HEIGHT + dy) - 2, pi.getClr(), 36);
                        dy += Utilities.LINE_HEIGHT;
                        selection++;
                        break;
                    case 3:
                        StringDraw gi2 = (StringDraw) pi.getItem();
                        gi2.setPageNo(pi.getPage() - pi.getStartPage());
                        gi2.draw3D(g, dx3, dy + 2, pi.getClr(), Tool.CLR_TABLE[11]);
                        dy += gi2.currPageLength() * Utilities.LINE_HEIGHT;
                        break;
                    case 4:
                        if (this.currPageSelection == selection) {
                            drawSmallPanel(g, dx3 - 5, dy + 2, this.width - 10, Utilities.CHAR_HEIGHT, Tool.CLR_TABLE[14], Tool.CLR_TABLE[18]);
                        } else {
                            drawSmallPanel(g, dx3 - 5, dy + 2, this.width - 10, Utilities.CHAR_HEIGHT, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                        }
                        ((MovingString) pi.getItem()).draw3D(g, dx3, dy + 2, pi.getClr(), Tool.CLR_TABLE[11]);
                        dy += Utilities.LINE_HEIGHT;
                        selection++;
                        break;
                    case 6:
                        if (this.currPageSelection == selection) {
                            drawSmallPanel(g, dx3 - 5, dy + 2, this.width - 10, Utilities.CHAR_HEIGHT, Tool.CLR_TABLE[14], Tool.CLR_TABLE[18]);
                        } else {
                            drawSmallPanel(g, dx3 - 5, dy + 2, this.width - 10, Utilities.CHAR_HEIGHT, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                        }
                        String str2 = pi.getItem().toString();
                        Tool.draw3DString(g, str2, dx3, (Utilities.LINE_HEIGHT + dy) - 2, pi.getClr(), Tool.CLR_TABLE[11], 36);
                        Tool.drawNumStr(pi.getTimeStr(), g, dx3 + Utilities.font.stringWidth(str2), (Utilities.LINE_HEIGHT + dy) - 2, 0, 36, -1);
                        dy += Utilities.LINE_HEIGHT;
                        break;
                }
            }
        }
        if (!(this.tipTimer == -1 || this.tip == null || System.currentTimeMillis() - this.tipTimer <= 1500)) {
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            int tipy = this.giy + 1;
            boolean reverseTip = false;
            if (this.tip.getHeight() + tipy + 6 > World.viewHeight - Chat.chatHeight) {
                tipy -= this.tip.getHeight() + 15;
                reverseTip = true;
            }
            if (this.tip != null) {
                this.tip.setBounds(this.gix, tipy, this.width - 20, -1, reverseTip);
                this.tip.draw(g);
            }
        }
        int keyHelpHeight = Utilities.CHAR_HEIGHT;
        if (this.pageSize > 1) {
            drawPageBar(g, this.pageBarX, this.contentY - (keyHelpHeight >> 1), 80, keyHelpHeight, this.currPage + 1, this.pageSize, 8);
            Tool.drawSmallNum(String.valueOf(this.currPage + 1) + "/" + this.pageSize, g, World.viewWidth / 2, (this.contentY + this.contentHeight) - keyHelpHeight, 33, -1);
        }
        drawBtn(g, (this.contentY + this.contentHeight) - (keyHelpHeight >> 1));
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getCurPage() {
        return this.currPage;
    }
}
