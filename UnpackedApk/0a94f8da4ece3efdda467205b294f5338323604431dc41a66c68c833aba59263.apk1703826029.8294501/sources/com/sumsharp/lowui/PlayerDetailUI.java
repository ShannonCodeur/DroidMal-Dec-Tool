package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Credit;
import com.sumsharp.monster.common.data.Player;
import com.sumsharp.monster.common.data.Title;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.item.GameItem;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class PlayerDetailUI extends UI implements TabHandler {
    private static final int PAGE_COIN = 1;
    private static final int PAGE_COMPETE = 0;
    private static final int PAGE_CREDIT = 3;
    private static final int PAGE_SKILL = 2;
    private static final int PAGE_TITLE = 4;
    private int _commonY;
    private int _contentY;
    private int _tabY;
    private Table coinTable;
    private int commonHeight;
    private int commonSpeed;
    private int commonY;
    private int contentHeight;
    private int contentSpeed;
    private int contentY;
    private int creditCount = 0;
    private int creditHeight = 0;
    private int creditIdx = 0;
    private int creditShow = 0;
    private int creditStart = 0;
    private Table creditTable;
    private int freeTabWidth = 0;
    private int nameX;
    private int nameY;
    private int pageX;
    private ImageSet petIcon = ImageLoadManager.getImage("petIcons.jgp");
    private Player player = CommonData.player;
    private boolean showTitleMenu = false;
    private Table skillTable;
    private Tab tab;
    private int tabSpeed;
    private String[] tabTitles = {"竞技场", "货币", "技能", "声望", "称号"};
    private Menu titleMenu;
    private String[] titleMenus = {"设置称号", "取消称号"};
    private String[] titleSelect;
    private Table titleTable;
    private int width;
    private int x;

    public PlayerDetailUI() {
        init();
    }

    private void init() {
        this.x = 3;
        this.width = World.viewWidth - 6;
        this.commonY = NewStage.screenY;
        this.commonHeight = 60;
        int tabY = this.commonY + this.commonHeight;
        int tabHeight = Tab.getDefaultHeight();
        this.contentY = tabY + tabHeight;
        this.contentHeight = ((World.viewHeight - tabY) - CornerButton.instance.getHeight()) - Tab.getDefaultHeight();
        this.nameX = 0;
        this.nameY = (this.commonY - (tabHeight >> 1)) + 2;
        this._tabY = tabY;
        this._commonY = this.commonY;
        this._contentY = this.contentY;
        int[] widths = new int[5];
        widths[1] = -1;
        widths[2] = 35;
        widths[3] = Utilities.font.stringWidth("主动") + 10;
        widths[4] = 32;
        this.skillTable = new Table(this.x, this.contentY, World.viewWidth - 6, this.contentHeight, new String[]{"", "技能", "等级", "类型", "MP"}, widths);
        this.coinTable = new Table(this.x, this.contentY, World.viewWidth - 6, this.contentHeight, new String[]{"", "货币", "数量"}, new int[]{25, -1, 48});
        this.titleTable = new Table(this.x, this.contentY, World.viewWidth - 6, this.contentHeight, new String[]{"", "称号"}, new int[]{20, -1});
        initTitleTable();
        int tabY2 = World.viewHeight;
        this.tab = new Tab("tab", this.tabTitles, this.x, this.width, true, this);
        this.tab.setContentHeight(this.contentHeight);
        this.commonY = -this.commonHeight;
        this.contentY = World.viewHeight + tabHeight;
        this.commonSpeed = (this._commonY - this.commonY) >> 1;
        this.tabSpeed = (tabY2 - this._tabY) / 2;
        this.contentSpeed = (this.contentY - this._contentY) / 2;
        this.nameX -= (Utilities.font.stringWidth(this.player.name) + Utilities.LINE_HEIGHT) + 20;
        this.pageX = World.viewWidth;
        this.creditCount = CommonData.player.credits.size();
        this.creditIdx = 0;
        int cdh = this.contentHeight - 6;
        this.creditHeight = Utilities.LINE_HEIGHT + 12;
        this.creditShow = cdh / this.creditHeight;
        this.creditStart = 0;
        if (this.creditShow >= this.creditCount) {
            this.creditShow = this.creditCount;
        }
        this.titleMenu = new Menu("称号", this.titleMenus, null, 8);
        this.coinTable.setHasTab(true);
        this.titleTable.setHasTab(true);
        this.skillTable.setHasTab(true);
        CornerButton.instance.setCmd(null, "返回");
    }

    public void cycle() {
        super.cycle();
        if (this.closed) {
            this.commonY -= this.commonSpeed;
            if (this.contentY != this._contentY) {
                this.tab.setY(this.tab.getY() + this.tabSpeed);
            }
            this.contentY += this.contentSpeed;
            this.nameY = (this.commonY - (this.tab.getHeight() >> 1)) + 2;
            if (this.tab.getY() >= World.viewHeight) {
                this.show = false;
            }
        } else if (this.show) {
            int tabIdx = this.tab.getIdx();
            this.tab.cycle();
            if (tabIdx == 2 && this.skillTable != null) {
                this.skillTable.handlePoints(World.pressedx, World.pressedy);
            } else if (tabIdx == 4 && this.titleTable != null) {
                if (this.titleMenu != null && this.showTitleMenu && this.titleMenu.show) {
                    this.titleMenu.hasPointerEvents();
                }
                this.titleTable.handlePoints(World.pressedx, World.pressedy);
            } else if (tabIdx == 1 && this.coinTable != null) {
                this.coinTable.handlePoints(World.pressedx, World.pressedy);
            }
            if (this.pageX - 20 > World.viewWidth - 80) {
                this.pageX -= 20;
            } else {
                this.pageX = World.viewWidth - 80;
            }
            if (this.nameX + 20 < 0) {
                this.nameX += 20;
            } else {
                this.nameX = 0;
            }
            moveBtn();
            if (tabIdx == 2) {
                this.skillTable.cycle();
            } else if (tabIdx == 1) {
                if (CommonData.player.coinRefresh) {
                    CommonData.player.coinRefresh = false;
                    initCoinTable();
                }
                this.coinTable.cycle();
            } else if (tabIdx == 4) {
                if (this.showTitleMenu) {
                    if (this.titleMenu.show && Utilities.isKeyPressed(10, true)) {
                        this.titleMenu.close();
                    }
                    if (this.titleMenu.isClosed()) {
                        this.titleMenu.reset();
                        this.showTitleMenu = false;
                        return;
                    }
                    this.titleMenu.cycle();
                    if (CommonData.player.titles.size() > 0) {
                        Title title = (Title) CommonData.player.titles.elementAt(this.titleTable.getMenuSelection());
                        switch (this.titleMenu.getMenuSelection()) {
                            case 0:
                                if (title != null) {
                                    UWAPSegment segment = new UWAPSegment(18, 40);
                                    try {
                                        segment.writeInt(title.id);
                                    } catch (IOException e) {
                                    }
                                    Utilities.sendRequest(segment);
                                    CommonData.player.currTitle = title.id;
                                    refreshTitleStatus();
                                    this.titleMenu.close();
                                    return;
                                }
                                return;
                            case 1:
                                UWAPSegment segment2 = new UWAPSegment(18, 40);
                                try {
                                    segment2.writeInt(-1);
                                } catch (IOException e2) {
                                }
                                Utilities.sendRequest(segment2);
                                CommonData.player.currTitle = -1;
                                refreshTitleStatus();
                                this.titleMenu.close();
                                return;
                            default:
                                return;
                        }
                    }
                } else {
                    this.titleTable.cycle();
                    if (Utilities.isKeyPressed(9, true)) {
                        if (CommonData.player.titles.size() > 0) {
                            this.showTitleMenu = true;
                            boolean[] enable = new boolean[2];
                            if (((Title) CommonData.player.titles.elementAt(this.titleTable.getMenuSelection())).id == CommonData.player.currTitle) {
                                enable[0] = false;
                                enable[1] = true;
                            } else {
                                enable[0] = true;
                                enable[1] = CommonData.player.currTitle != -1;
                            }
                            this.titleMenu.setItemStatus(enable);
                        }
                    } else if ((Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(16, true)) && CommonData.player.titles.size() > 0) {
                        Title title2 = (Title) CommonData.player.titles.elementAt(this.titleTable.getMenuSelection());
                        if (title2 != null) {
                            UWAPSegment segment3 = new UWAPSegment(18, 40);
                            if (title2.id != CommonData.player.currTitle) {
                                try {
                                    segment3.writeInt(title2.id);
                                } catch (IOException e3) {
                                }
                                CommonData.player.currTitle = title2.id;
                            } else {
                                try {
                                    segment3.writeInt(-1);
                                } catch (IOException e4) {
                                }
                                CommonData.player.currTitle = -1;
                            }
                            Utilities.sendRequest(segment3);
                            refreshTitleStatus();
                        }
                    }
                }
            } else if (tabIdx == 3) {
                if (Utilities.isKeyPressed(0, true) || Utilities.isKeyPressed(13, true)) {
                    this.creditIdx--;
                    if (this.creditIdx < 0) {
                        this.creditIdx = this.creditCount - 1;
                    }
                } else if (Utilities.isKeyPressed(1, true) || Utilities.isKeyPressed(19, true)) {
                    this.creditIdx++;
                    if (this.creditIdx >= this.creditCount) {
                        this.creditIdx = 0;
                    }
                }
                if (this.creditStart + this.creditShow <= this.creditIdx) {
                    this.creditStart = (this.creditIdx - this.creditShow) + 1;
                }
                if (this.creditStart > this.creditIdx) {
                    this.creditStart = this.creditIdx;
                }
            }
        } else {
            this.commonY += this.commonSpeed;
            if (this.tab.getY() != World.viewHeight) {
                this.contentY -= this.contentSpeed;
            }
            this.tab.setY(this.tab.getY() - this.tabSpeed);
            if (this.tab.getY() < this._tabY) {
                this.tab.setY(this._tabY);
            }
            if (this.commonY > this._commonY) {
                this.commonY = this._commonY;
            }
            if (this.contentY < this._contentY) {
                this.contentY = this._contentY;
                this.show = true;
            }
        }
    }

    private void refreshTitleStatus() {
        String str;
        Vector ts = CommonData.player.titles;
        for (int i = 0; i < ts.size(); i++) {
            String[] strArr = this.titleSelect;
            if (((Title) ts.elementAt(i)).id == CommonData.player.currTitle) {
                str = "31";
            } else {
                str = "-1";
            }
            strArr[i] = str;
        }
        if (CommonData.player.getCurrTitle() != null) {
            CommonData.player.title = CommonData.player.getCurrTitle().title;
            CommonData.player.color = CommonData.player.getCurrTitle().color;
        }
    }

    private void initTitleTable() {
        String str;
        Vector ts = CommonData.player.titles;
        String[] titles = new String[ts.size()];
        this.titleSelect = new String[ts.size()];
        int[] clrs = new int[ts.size()];
        for (int i = 0; i < ts.size(); i++) {
            Title t = (Title) ts.elementAt(i);
            titles[i] = t.title;
            clrs[i] = t.color;
            String[] strArr = this.titleSelect;
            if (t.id == CommonData.player.currTitle) {
                str = "31";
            } else {
                str = "-1";
            }
            strArr[i] = str;
        }
        this.titleTable.addTableItem("称号", titles, 0, clrs, null);
        this.titleTable.addTableItem("", this.titleSelect, 2, null, null);
        this.titleTable.setOriPos();
        this.titleTable.setHasTab(true);
    }

    private void initSkillTable() {
    }

    private void initCoinTable() {
        String[] coins = getCoinsInfo();
        int len = coins.length / 2;
        String[] ids = new String[len];
        String[] names = new String[len];
        String[] nums = new String[len];
        for (int i = 0; i < len; i++) {
            GameItem item = CommonData.player.getCoinItem(Integer.parseInt(coins[i * 2]));
            ids[i] = String.valueOf(item.id) + "," + item.iconId;
            names[i] = item.name;
            nums[i] = coins[(i * 2) + 1];
        }
        this.coinTable.clear();
        this.coinTable.addTableItem("", ids, 18, null, null);
        this.coinTable.addTableItem("货币", names, 0, null, null);
        this.coinTable.addTableItem("数量", nums, 1, null, null);
        this.coinTable.show = this.show;
        this.coinTable.setOriPos();
        this.coinTable.setHasTab(true);
    }

    public void paint(Graphics g) {
        int cc;
        int win;
        int off = World.tick % 8;
        if (off > 4) {
            int off2 = 7 - off;
        }
        int dx = this.x + Tool.uiMiscImg.getFrameWidth(48) + Tool.uiMiscImg.getFrameWidth(65) + 6;
        int dy = this.commonY + 3;
        this.tab.paint(g);
        Tool.drawAlphaBox(g, this.width, this.commonHeight, this.x, this.commonY, -436200402);
        int tabIdx = this.tab.getIdx();
        g.setClip(this.x, this.commonY, World.viewWidth - (this.x * 2), this.commonHeight - 3);
        if (this.show) {
            this.player.cartoonPlayer.draw(g, this.x + 10, (this.commonY + this.commonHeight) - 5);
        }
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        Tool.draw3DString(g, this.player.name, this.x + 43, dy + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
        if (!this.show) {
            int i = dx;
            return;
        }
        int lvWidth = Utilities.font.stringWidth("等级 ") + 26;
        Title title = CommonData.player.getCurrTitle();
        int titleAdd = 0;
        if (title != null) {
            titleAdd = Utilities.font.stringWidth(title.title) + 10;
            if (titleAdd > World.viewWidth - 60) {
                titleAdd = World.viewWidth - 60;
            }
        }
        int titleWidth = Utilities.font.stringWidth("称号 ");
        int dx2 = this.x + 43;
        int dy2 = dy + Utilities.LINE_HEIGHT + 4;
        drawSmallPanel(g, dx2, (Utilities.LINE_HEIGHT + dy2) - 5, titleWidth + titleAdd, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
        Tool.draw3DString(g, "称号 ", dx2 + 5, dy2 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
        if (title != null) {
            Tool.draw3DString(g, title.title, dx2 + 5 + titleWidth, dy2 + Utilities.LINE_HEIGHT, title.color, Tool.CLR_TABLE[11], 36);
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
        }
        long percent = 0;
        if (CommonData.player.levelUpExp > 0) {
            percent = (CommonData.player.exp * 10000) / CommonData.player.levelUpExp;
        }
        int dx3 = dx2 + (Utilities.LINE_HEIGHT * 8) + 10;
        int dy3 = this.commonY + 3;
        drawSmallPanel(g, dx3, (Utilities.LINE_HEIGHT + dy3) - 5, lvWidth, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
        Tool.draw3DString(g, "等级", dx3 + 5, dy3 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
        Tool.drawNumStr(String.valueOf(this.player.level), g, Utilities.font.stringWidth("称号 ") + dx3 + 13, dy3 + Utilities.LINE_HEIGHT, 0, 33, -1);
        int dy4 = dy3 + Utilities.LINE_HEIGHT + 4;
        Tool.uiMiscImg.drawFrame(g, 74, dx3, dy4 + 13, 0, 36);
        String p = String.valueOf(percent);
        if (p.length() < 3) {
            int s = 3 - p.length();
            for (int i2 = 0; i2 < s; i2++) {
                p = "0" + p;
            }
        }
        Tool.drawNumStr(String.valueOf(p.substring(0, p.length() - 2)) + "." + p.substring(p.length() - 2) + "%", g, Tool.uiMiscImg.getFrameWidth(74) + dx3 + 5, dy4 + 13, 0, 36, -1);
        g.setColor(10296);
        int expw = (this.width - dx3) - 10;
        g.drawRect(dx3, dy4 + 15, expw + 1, 4);
        int v = 0;
        if (CommonData.player.levelUpExp > 0) {
            v = (int) ((CommonData.player.exp * ((long) expw)) / CommonData.player.levelUpExp);
        }
        if (v > 0) {
            g.setColor(16113524);
            g.fillRect(dx3 + 1, dy4 + 16, v, 3);
        }
        switch (tabIdx) {
            case 0:
                int dx4 = this.x + 5;
                int dy5 = this.contentY + 5;
                drawSmallPanel(g, dx4, (Utilities.LINE_HEIGHT + dy5) - 5, this.width >> 2, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                Tool.draw3DString(g, "军衔 " + this.player.honorTitle, dx4 + 5, dy5 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                int width2 = this.width - 10;
                int dx5 = dx4 + (width2 >> 1) + 5;
                if (this.player.honorIdx > 0) {
                    Tool.rank.drawFrame(g, this.player.honorIdx - 1, dx5, Utilities.LINE_HEIGHT + dy5 + 5, 0, 36);
                }
                int dx6 = this.x + 5;
                int dy6 = dy5 + Utilities.LINE_HEIGHT + 3;
                if (dy6 > (this.contentY + this.contentHeight) - Utilities.LINE_HEIGHT) {
                    dx6 += dx6 + (width2 >> 1);
                    dy6 = this.contentY + 5;
                }
                drawSmallPanel(g, dx6, (Utilities.LINE_HEIGHT + dy6) - 5, width2 >> 2, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                Tool.draw3DString(g, "荣誉  ", dx6 + 5, dy6 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                Tool.drawNumStr(String.valueOf(this.player.honor), g, Utilities.font.stringWidth("荣誉  ") + dx6 + 10, dy6 + Utilities.LINE_HEIGHT, 0, 36, -1);
                int dy7 = dy6 + Utilities.LINE_HEIGHT + 3;
                if (dy7 > (this.contentY + this.contentHeight) - Utilities.LINE_HEIGHT) {
                    dx6 += dx6 + (width2 >> 1);
                    dy7 = this.contentY + 5;
                }
                drawSmallPanel(g, dx6, (Utilities.LINE_HEIGHT + dy7) - 5, width2 >> 2, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                Tool.draw3DString(g, "胜利  ", dx6 + 5, dy7 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                Tool.drawNumStr(String.valueOf(this.player.arenaWin), g, Utilities.font.stringWidth("胜利  ") + dx6 + 10, dy7 + Utilities.LINE_HEIGHT, 0, 36, -1);
                int dy8 = this.contentY + 5;
                int dx7 = dx6 + (width2 >> 1);
                drawSmallPanel(g, dx7, (Utilities.LINE_HEIGHT + dy8) - 5, width2 >> 2, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                Tool.draw3DString(g, "失败  ", dx7 + 5, dy8 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                Tool.drawNumStr(String.valueOf(this.player.arenaLose), g, Utilities.font.stringWidth("失败  ") + dx7 + 10, dy8 + Utilities.LINE_HEIGHT, 0, 36, -1);
                int dy9 = dy8 + Utilities.LINE_HEIGHT + 3;
                int all = this.player.arenaLose + this.player.arenaWin;
                if (all == 0) {
                    win = 0;
                } else {
                    win = (this.player.arenaWin * 100) / all;
                }
                Tool.draw3DString(g, "获胜率 " + win + "%", dx7 + 5, dy9 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                int dy10 = dy9 + Utilities.LINE_HEIGHT + 3;
                g.setColor(10296);
                g.drawRect(dx7, dy10 + 3, 101, 7);
                if (win > 0) {
                    g.setColor(15245456);
                    g.fillRect(dx7 + 1, dy10 + 4, win, 6);
                    return;
                }
                return;
            case 1:
                if (!this.closed) {
                    this.coinTable.paint(g);
                    return;
                }
                return;
            case 2:
                if (!this.closed) {
                    this.skillTable.paint(g);
                    return;
                }
                return;
            case 3:
                if (this.creditCount != 0) {
                    int cdx = this.x + 6;
                    int cdy = this.contentY + 2;
                    int cdw = this.width - 12;
                    for (int i3 = 0; i3 < this.creditCount; i3++) {
                        Credit c = (Credit) CommonData.player.credits.elementAt(i3);
                        Tool.draw3DString(g, String.valueOf(c.title) + ": " + c.levelTitle, cdx, cdy + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                        int cdy2 = cdy + Utilities.LINE_HEIGHT + 2;
                        int a = c.levelUpExp;
                        int cc2 = c.value;
                        if (a == 0) {
                            cc = 0;
                        } else {
                            cc = ((cdw - 1) * cc2) / a;
                        }
                        g.setColor(Tool.CLR_ITEM_WHITE);
                        g.drawRect(cdx, cdy2, cdw, 10);
                        if (cc > 0) {
                            g.setColor(15245456);
                            g.fillRect(cdx + 1, cdy2 + 1, cc, 9);
                        }
                        Tool.drawSmallNum(String.valueOf(c.value) + "/" + c.levelUpExp, g, (cdx + cdw) - 2, cdy2 + 7, 40, -1);
                        cdy = cdy2 + 10;
                    }
                    return;
                }
                return;
            case 4:
                if (!this.closed) {
                    this.titleTable.paint(g);
                    if (this.showTitleMenu) {
                        this.titleMenu.paint(g);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    private String[] getCoinsInfo() {
        Vector vec = CommonData.player.coins;
        int[] nums = new int[vec.size()];
        int[] itemids = new int[vec.size()];
        int ptr = -1;
        for (int i = 0; i < vec.size(); i++) {
            GameItem item = (GameItem) vec.elementAt(i);
            int num = item.count;
            int itemid = item.itemId;
            if (ptr == -1) {
                ptr++;
                nums[ptr] = num;
                itemids[ptr] = itemid;
            } else {
                int j = 0;
                while (true) {
                    if (j > ptr) {
                        break;
                    } else if (itemids[j] == itemid) {
                        nums[j] = nums[j] + num;
                        break;
                    } else {
                        j++;
                    }
                }
                if (j > ptr) {
                    ptr++;
                    itemids[ptr] = itemid;
                    nums[ptr] = num;
                }
            }
        }
        String[] ret = new String[((ptr + 1) * 2)];
        for (int i2 = 0; i2 < ret.length; i2++) {
            if (i2 % 2 == 0) {
                ret[i2] = Integer.toString(itemids[i2 / 2]);
            } else {
                ret[i2] = Integer.toString(nums[i2 / 2]);
            }
        }
        return ret;
    }

    public void handleCurrTab() {
        int idx = this.tab.getIdx();
        if (idx == 4) {
            this.titleTable.show = true;
            if (this.titleTable.getItemCount() > 0) {
                CornerButton.instance.setCmd("菜单", "返回");
            } else {
                CornerButton.instance.setCmd(null, "返回");
            }
        } else {
            CornerButton.instance.setCmd(null, "返回");
            if (idx == 1) {
                initCoinTable();
            }
        }
    }
}
