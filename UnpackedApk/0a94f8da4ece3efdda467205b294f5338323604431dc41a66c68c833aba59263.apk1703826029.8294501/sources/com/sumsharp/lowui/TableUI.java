package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.item.GameItem;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class TableUI extends UI implements TabHandler {
    private static final int MAX_NOTIFY_TIME = 15;
    private BagPanel bg;
    private boolean hasTab;
    private int icon;
    private int moneyspeed;
    private int moneywidth;
    private int moneyx = World.viewWidth;
    private byte[] needsNotify = new byte[5];
    private int showBuyInfo;
    private int showCrystal;
    private GameItem showItem;
    private int showItemId;
    private boolean showMoney;
    private int showToken;
    private Tab tab;
    private int[] tabIcons;
    private String[] tabTitles;
    private Table table;
    private int tableAniY;
    private int tableSpeed;
    private int tableY;
    private String title;
    private int titleAniY;
    private int titleSpeed;
    private int titleY;
    private String[] titles;
    private boolean toFullScreen;
    private int[] widths;

    public TableUI(int icon2, String title2, String[] tabTitles2, int[] tabIcons2, Object[] items, int gridSize, int attachInfo, int showBuyInfo2, int moneyType) {
        init(icon2, title2, tabTitles2, tabIcons2, null, null, attachInfo, moneyType);
        Vector vec = new Vector();
        if (items == null) {
            vec = null;
        } else {
            for (Object addElement : items) {
                vec.addElement(addElement);
            }
        }
        this.bg = new BagPanel(3, this.tableY + 5, World.viewWidth - 6, ((World.viewHeight - this.tableAniY) - DirectionPad.instance.getHeight()) - 10, gridSize, vec, showBuyInfo2);
        this.tableSpeed = this.bg.getHeight() >> 1;
        this.showBuyInfo = showBuyInfo2;
        this.bg.setHasTab(this.hasTab);
        if (this.hasTab) {
            this.tab.setIsUp(false);
            this.tab.setContentHeight(this.bg.getHeight() - (gridSize == -3 ? 2 : 36));
        }
    }

    public TableUI(int icon2, String title2, String[] tabTitles2, int[] tabIcons2, String[] titles2, int[] widths2, int attachInfo) {
        init(icon2, title2, tabTitles2, tabIcons2, titles2, widths2, attachInfo, 0);
        this.table = new Table(3, this.tableY, World.viewWidth - 6, (World.viewHeight - this.tableAniY) - CornerButton.instance.getHeight(), titles2, widths2);
        this.table.setHasTab(this.hasTab);
        if (this.hasTab) {
            this.tab.setContentHeight(this.table.getHeight());
        }
        this.tableSpeed = this.table.getHeight() >> 1;
    }

    public TableUI(int icon2, String title2, String[] tabTitles2, int[] tabIcons2, String[] titles2, int[] widths2, int attachInfo, boolean full) {
        this.toFullScreen = full;
        init(icon2, title2, tabTitles2, tabIcons2, titles2, widths2, attachInfo, 0);
        this.table = new Table(3, this.tableY, World.viewWidth - 6, this.toFullScreen ? ((World.viewHeight - this.tableAniY) - Utilities.LINE_HEIGHT) - 2 : (((World.viewHeight - this.tableAniY) - Chat.chatHeight) - (Utilities.LINE_HEIGHT >> 1)) - 2, titles2, widths2);
        this.table.setHasTab(this.hasTab);
        if (this.hasTab) {
            this.tab.setContentHeight(this.table.getHeight());
        }
        this.tableSpeed = this.table.getHeight() >> 1;
    }

    private boolean isStore() {
        return this.icon == 14;
    }

    private void init(int icon2, String title2, String[] tabTitles2, int[] tabIcons2, String[] titles2, int[] widths2, int attachInfo, int moneyType) {
        this.tabTitles = tabTitles2;
        this.tabIcons = tabIcons2;
        this.titles = titles2;
        this.widths = widths2;
        this.icon = icon2;
        this.title = title2;
        this.showCrystal = -1;
        this.showToken = -1;
        this.hasTab = true;
        this.showMoney = attachInfo > 0 || moneyType > 0;
        if (attachInfo < 0) {
            this.showItemId = -attachInfo;
        }
        if (attachInfo > 1 || moneyType > 0) {
            if (moneyType == 0) {
                this.showCrystal = attachInfo;
            } else {
                this.showToken = attachInfo;
            }
        }
        this.hasTab = tabTitles2 != null;
        if (this.toFullScreen) {
            this.titleAniY = 1;
        } else {
            this.titleAniY = NewStage.screenY;
        }
        this.tableAniY = this.titleAniY + Utilities.TITLE_HEIGHT;
        if (this.hasTab) {
            this.tableAniY += Tab.getDefaultHeight();
            if (isStore()) {
                this.tableAniY -= Tab.getDefaultHeight();
            }
        }
        if (this.showMoney) {
            this.tableAniY += 8;
            String strMoney = String.valueOf(CommonData.player.money);
            if (this.showCrystal != -1) {
                this.moneywidth = Tool.getSmallNumWidth(String.valueOf(this.showCrystal)) + Utilities.CHAR_HEIGHT + 14;
            } else if (this.showToken != -1) {
                this.moneywidth = Tool.getSmallNumWidth(String.valueOf(this.showToken)) + Utilities.CHAR_HEIGHT + 14;
            } else {
                int iconCount = (strMoney.length() + 1) >> 1;
                if (iconCount > 3) {
                    iconCount = 3;
                }
                this.moneywidth = Tool.getSmallNumWidth(strMoney) + (iconCount * 13) + Utilities.CHAR_HEIGHT;
            }
            this.moneyspeed = this.moneywidth / 2;
            if (this.moneyspeed == 0) {
                this.moneyspeed = 1;
            }
        }
        if (this.showItemId > 0) {
            this.tableAniY += 8;
            this.showItem = CommonData.player.findItem(this.showItemId);
            if (this.showItem != null) {
                this.moneywidth = Utilities.font.stringWidth(this.showItem.name) + 38 + Tool.getSmallNumWidth(String.valueOf(this.showItem.count));
                this.moneyspeed = this.moneywidth / 2;
            }
        }
        if (tabTitles2 == null || tabTitles2.length == 0) {
            this.hasTab = false;
        }
        if (this.hasTab) {
            this.tab = new Tab("tab", tabTitles2, 1, World.viewWidth - 2, true, this);
        }
        this.titleY = -Utilities.LINE_HEIGHT;
        this.tableY = World.viewHeight;
        this.titleSpeed = (this.titleAniY + Utilities.LINE_HEIGHT) >> 1;
        this.pointBtnY = -1;
    }

    private void checkNotify(int idx) {
        if (this.needsNotify[idx] >= 0) {
            this.needsNotify[idx] = -1;
        }
    }

    public void addTableItem(String id, String[] items, int type, int[] clr, int[] bgClr) {
        if (this.table != null) {
            this.table.addTableItem(id, items, type, clr, bgClr);
        }
    }

    public void remove(int index) {
        if (this.table != null) {
            this.table.remove(index);
        }
    }

    public void setTitles(String[] titles2) {
        if (this.table != null) {
            this.table.setTitles(titles2);
        }
    }

    public void cycle() {
        super.cycle();
        if (this.closed) {
            this.titleY -= this.titleSpeed;
            this.tableY += this.tableSpeed;
            if (this.table != null) {
                this.table.tipTimer = -1;
                this.table.setY(this.tableY);
            } else if (this.bg != null) {
                this.bg.setY(this.tableY);
            }
            if (this.tableY > World.viewHeight) {
                this.show = false;
                this.moneyx = World.viewWidth;
            }
        } else if (this.show) {
            if ((this.showMoney || this.showItem != null) && (this.moneyx + this.moneywidth) - this.moneyspeed > World.viewWidth) {
                this.moneyx -= this.moneyspeed;
            } else {
                this.moneyx = World.viewWidth - this.moneywidth;
            }
            int px = World.pressedx;
            int py = World.pressedy;
            if (this.table != null && this.show && this.table.show) {
                this.table.handlePoints(px, py);
            }
            if (this.table != null && this.show && !this.table.show) {
                this.table.show = this.show;
                this.table.setOriPos();
            }
            moveBtn();
            UI ui = getUI();
            if (ui != null) {
                ui.cycle();
            }
            if (this.hasTab) {
                this.tab.cycle();
            }
        } else {
            this.titleY += this.titleSpeed;
            this.tableY -= this.tableSpeed;
            if (this.tableY - this.tableSpeed < this.tableAniY) {
                this.tableY = this.tableAniY;
                this.titleY = this.titleAniY;
                this.show = true;
                if (this.bg != null) {
                    this.bg.show = true;
                }
            }
            if (this.table != null) {
                this.table.setY(this.tableY);
            } else if (this.bg != null) {
                this.bg.setY(this.tableY);
            }
            if (!this.hasTab) {
                return;
            }
            if (!isStore()) {
                this.tab.setY(this.titleY + Utilities.TITLE_HEIGHT);
            } else if (this.tableY == this.tableAniY) {
                this.tab.setY(this.bg.getY() + this.bg.getBagHeight());
            }
        }
    }

    public void paint(Graphics g) {
        int i;
        Tool.drawAlphaBox(g, World.viewWidth, Utilities.TITLE_HEIGHT, 0, this.titleY, -436200402);
        int titlex = 10;
        if (this.icon != -1) {
            if (this.icon > 0) {
                Tool.uiMiscImg.drawFrame(g, this.icon, 10, this.titleY + 27, 0, 36);
                titlex = 10 + Tool.uiMiscImg.getFrameWidth(this.icon) + 2;
            } else {
                CommonData.player.getPet(-(this.icon + 2)).drawHead(g, 10, this.titleY + ((Utilities.LINE_HEIGHT - 20) >> 1));
                titlex = 10 + 24;
            }
        }
        Tool.drawLevelString(g, this.title, titlex, (this.titleY + Utilities.TITLE_HEIGHT) - 10, 36, 1, 0);
        if (this.hasTab) {
            int off = World.tick % 8;
            if (off > 4) {
                int off2 = 7 - off;
            }
            this.tab.paint(g);
        }
        UI ui = getUI();
        if (ui != null) {
            ui.paint(g);
        }
        if (this.showMoney) {
            drawTitlePanel(g, "", this.moneyx, (this.tableY - Utilities.CHAR_HEIGHT) + 3, this.moneywidth + 20, Utilities.CHAR_HEIGHT, 4);
            if (this.showCrystal >= 0 || this.showToken >= 0) {
                int f = 0;
                if (this.showCrystal >= 0) {
                    f = (World.tick % 30) + 1;
                    if (f > 5) {
                        f = 5;
                    }
                }
                Tool.uiMiscImg2.drawFrame(g, f, this.moneyx + 9, this.tableY + 2, 0, 36);
                if (this.showCrystal >= 0) {
                    i = this.showCrystal;
                } else {
                    i = this.showToken;
                }
                Tool.drawNumStr(String.valueOf(i), g, this.moneyx + 22, this.tableY + 2, 0, 36, -1);
            } else {
                Tool.drawMoney(g, CommonData.player.money, this.moneyx + 9, this.tableY + 2, 0, 36, true);
            }
        } else if (this.showItem != null) {
            drawTitlePanel(g, "", this.moneyx, (this.tableY - Utilities.CHAR_HEIGHT) + 3, this.moneywidth + 20, Utilities.CHAR_HEIGHT, 4);
            this.showItem.drawIcon(g, this.moneyx + 9, this.tableY + 2, 36);
            Graphics graphics = g;
            Tool.draw3DString(graphics, this.showItem.name, this.moneyx + 30, this.tableY + 1, Tool.getQuanlityColor(this.showItem.quanlity), Tool.CLR_TABLE[11], 36);
            Graphics graphics2 = g;
            Tool.drawNumStr(String.valueOf(this.showItem.count), graphics2, this.moneyx + 32 + Utilities.font.stringWidth(this.showItem.name), this.tableY + 1, 0, 36, -1);
        }
        if (ui != null) {
            drawBtn(g, (ui.getY() + ui.getHeight()) - (Utilities.LINE_HEIGHT >> 1));
        }
    }

    public void updateCrystal(int crystal) {
        this.moneywidth = Tool.getSmallNumWidth(String.valueOf(crystal)) + Utilities.CHAR_HEIGHT + 14;
        this.moneyx = World.viewWidth - this.moneywidth;
        if (this.showCrystal != -1) {
            this.showCrystal = crystal;
        } else if (this.showToken != -1) {
            this.showToken = crystal;
        }
    }

    public int getTabSelection() {
        if (this.hasTab) {
            return this.tab.getIdx();
        }
        return -1;
    }

    private UI getUI() {
        UI ui = this.table;
        if (this.table == null) {
            return this.bg;
        }
        return ui;
    }

    public void setTableHeight(int height) {
        UI ui = getUI();
        if (ui instanceof Table) {
            ((Table) ui).setHeight(height);
        }
    }

    public int getMenuSelection() {
        UI ui = getUI();
        if (ui != null) {
            return ui.getMenuSelection();
        }
        return 0;
    }

    public void setItemStatus(boolean[] enable) {
        UI ui = getUI();
        if (ui != null) {
            ui.setItemStatus(enable);
        }
    }

    public void setBagInput(int maxGrid, Vector items) {
        if (this.bg != null) {
            this.bg.setInput(maxGrid, items);
        }
    }

    public void switchBagTipMode() {
        if (this.bg != null) {
            this.bg.switchTipMode();
        }
    }

    public void handleCurrTab() {
        if (this.table != null) {
            this.table.clear();
        }
    }

    public void setTabIdx(int idx) {
        if (this.tab != null) {
            this.tab.setTabIndex(idx);
        }
    }

    public void clear() {
        getUI().clear();
    }

    public Table getTable() {
        return this.table;
    }

    public BagPanel getBg() {
        return this.bg;
    }
}
