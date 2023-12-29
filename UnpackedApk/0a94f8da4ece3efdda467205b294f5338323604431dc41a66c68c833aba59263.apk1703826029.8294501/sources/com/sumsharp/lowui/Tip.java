package com.sumsharp.lowui;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.common.data.Skill;
import com.sumsharp.monster.item.GameItem;
import javax.microedition.lcdui.Graphics;

public class Tip {
    public static final int GUILD_DECLARE = 1;
    public static final int GUILD_PREPARE = 2;
    public static final int GUILD_STARTWAR = 3;
    public static final int LOADER_BAGPANEL = 1;
    public static final int LOADER_STRING = 2;
    public static final int LOADER_TABLE = 0;
    public static final int REMOVE_ALLY = 8;
    public static final int REQUEST_ALLY = 7;
    private TableItem append;
    private long checkTime;
    private GameItem gitem;
    private StringDraw guildString;
    private String[] guildTip;
    private long idleTime;
    private boolean isEquip;
    public boolean isMovingString;
    private boolean isTopTri;
    private boolean itemNeedsRefresh;
    private boolean needsRefresh;
    private StringDraw sd;
    private int selection;
    private long setTime;
    private int showLines = 3;
    private boolean skillNeedsRefresh;
    private String strContent;
    private TableItem tabItem;
    private int tick;
    private int tipLoaderType = -1;
    private int triX = -1;
    private int twidth;
    private int tx;
    private int ty;

    private Tip() {
    }

    public static Tip createTip(TableItem item, int width) {
        if (item == null) {
            return null;
        }
        if (item.items.length <= 0) {
            return null;
        }
        if (item.type == 8 || item.type == 16 || item.type == 18 || item.type == 10 || item.type == 14 || item.id.startsWith("desc") || item.id.startsWith("taskdesc") || item.id.startsWith("appenddesc:") || item.id.startsWith("desc:auction") || item.type == 19) {
            Tip tip = new Tip();
            tip.setTableItem(item);
            tip.setSelection(0);
            tip.twidth = width;
            return tip;
        } else if (item.type != 0 || !checkMovingString(item)) {
            return null;
        } else {
            Tip tip2 = new Tip();
            tip2.setTableItem(item);
            tip2.setSelection(0);
            tip2.twidth = width;
            tip2.isMovingString = true;
            return tip2;
        }
    }

    private static boolean checkMovingString(TableItem tabitm) {
        if (tabitm.type != 0) {
            return false;
        }
        for (Object obj : tabitm.items) {
            if (obj instanceof MovingString) {
                return true;
            }
        }
        return false;
    }

    public static Tip createTip(GameItem item, int width) {
        if (item == null) {
            return null;
        }
        Tip tip = new Tip();
        tip.tipLoaderType = 1;
        tip.twidth = width;
        tip.setSelection(0);
        tip.gitem = item;
        tip.needsRefresh = true;
        return tip;
    }

    public static Tip createTip(String content, int width) {
        if (content == null) {
            return null;
        }
        Tip tip = new Tip();
        tip.twidth = width;
        tip.tipLoaderType = 2;
        tip.needsRefresh = true;
        tip.strContent = content;
        return tip;
    }

    public int getWidth() {
        return this.twidth;
    }

    private Object getItem() {
        if (this.tipLoaderType == 0) {
            if (!(this.tabItem == null || this.tabItem.items == null || this.tabItem.items.length <= this.selection)) {
                return this.tabItem.items[this.selection];
            }
        } else if (this.tipLoaderType == 1) {
            return this.gitem;
        } else {
            if (this.tipLoaderType == 2) {
                return this.strContent;
            }
        }
        return null;
    }

    public int getHeight() {
        Object item = getItem();
        if (!(item instanceof GameItem)) {
            return (this.showLines * Utilities.LINE_HEIGHT) + 6;
        }
        GameItem gi = (GameItem) item;
        if (gi.type != 29) {
            return (this.showLines * Utilities.LINE_HEIGHT) + 6;
        }
        int height = Utilities.LINE_HEIGHT + ((Tool.imgFont.getFrameHeight(0) + 1) * 2);
        if (gi.attrAdd != null) {
            height += ((gi.attrAdd.length + 1) / 2) * (Tool.imgFont.getFrameHeight(0) + 1);
        }
        return height + 6;
    }

    private String getDesc() {
        String ret = null;
        Object item = getItem();
        if (this.tipLoaderType == 0) {
            if (this.tabItem == null) {
                ret = null;
            }
            if (this.tabItem.id.startsWith("appenddesc:")) {
                ret = (String) item;
            }
            if (!this.tabItem.id.startsWith("desc") && !this.tabItem.id.startsWith("appenddesc:") && !this.tabItem.id.startsWith("taskdesc:") && this.tabItem.type == 0 && (item instanceof MovingString)) {
                ret = ((MovingString) item).getString();
            }
            if (this.tabItem.type == 8 || this.tabItem.type == 16 || this.tabItem.type == 18) {
                ret = ((GameItem) item).getDesc();
                if (ret.startsWith("正在下载")) {
                    this.itemNeedsRefresh = true;
                } else {
                    this.itemNeedsRefresh = false;
                }
            } else if (this.tabItem.type == 10) {
                ret = ((Pet) item).getDesc(false);
            } else if (this.tabItem.type == 14) {
                String retDesc = ((Skill) item).getDesc();
                if (retDesc.startsWith("正在下载")) {
                    this.skillNeedsRefresh = true;
                } else {
                    this.skillNeedsRefresh = false;
                }
                ret = retDesc;
            } else if (this.tabItem.type == 19) {
                String[] tipstr = Utilities.splitString((String) item, ",");
                this.setTime = (long) Integer.parseInt(tipstr[0]);
                this.checkTime = System.currentTimeMillis();
                this.guildTip = new String[(tipstr.length - 1)];
                for (int i = 0; i < this.guildTip.length; i++) {
                    this.guildTip[i] = tipstr[i + 1];
                }
                String sdstr = "";
                int state = Integer.parseInt(this.guildTip[0]);
                if (state == 1) {
                    if (!CommonData.player.guild.equals(this.guildTip[1])) {
                        sdstr = "倒计时结束无响应则自动投降,并额外扣除5%公会资产";
                    }
                } else if (state == 2) {
                    sdstr = "备战中,倒计时结束公会战开始";
                } else if (state == 3) {
                    sdstr = "公会战进行中,倒计时结束停止公会战";
                } else if (state == 7) {
                    sdstr = String.valueOf(this.guildTip[1]) + "要和您的公会结盟,倒计时结束后自动拒绝";
                } else if (state == 8) {
                    sdstr = String.valueOf(this.guildTip[1]) + "要和您的公会解除盟约,倒计时结束后自动解盟";
                }
                this.guildString = new StringDraw(sdstr, this.twidth - 10, 2);
                this.needsRefresh = false;
            } else if (this.tabItem.id.startsWith("desc")) {
                ret = (String) item;
            } else if (this.tabItem.id.startsWith("taskdesc")) {
                ret = CommonData.player.findTask(Integer.parseInt((String) item)).toDesc();
            }
        } else if (this.tipLoaderType == 1) {
            ret = ((GameItem) item).getDesc();
            if (ret.startsWith("正在下载")) {
                this.itemNeedsRefresh = true;
            } else {
                this.itemNeedsRefresh = false;
            }
        }
        if (ret == null) {
            this.sd = null;
            return ret;
        } else if (this.append == null) {
            return ret;
        } else {
            return String.valueOf(ret) + "\n" + ((String) this.append.items[this.selection]);
        }
    }

    public void setTableItem(TableItem item) {
        this.tabItem = item;
        this.tipLoaderType = 0;
        this.needsRefresh = true;
    }

    public void cycle() {
        String tip;
        if (this.tipLoaderType == 2) {
            if (this.needsRefresh) {
                this.sd = new StringDraw(this.strContent, this.twidth - 10, -1);
                if (this.sd.length() < 3) {
                    setShowLines(this.sd.length());
                }
                refreshTicker();
                this.needsRefresh = false;
            }
        } else if (this.needsRefresh || this.skillNeedsRefresh || this.itemNeedsRefresh) {
            Object item = getItem();
            if (item != null) {
                if (this.tipLoaderType != 0) {
                    tip = getDesc();
                } else if (this.tabItem == null || !this.tabItem.id.startsWith("appenddesc:")) {
                    tip = getDesc();
                } else {
                    tip = String.valueOf("") + "\n" + getDesc();
                }
                if (tip != null) {
                    if (!(item instanceof GameItem) || ((GameItem) item).type != 29) {
                        this.sd = new StringDraw(tip, this.twidth - 10, -1);
                    } else {
                        this.isEquip = true;
                        GameItem itm = (GameItem) item;
                        if (itm.name.equals("")) {
                            setShowLines(3);
                        } else {
                            setShowLines((itm.attrAdd.length >> 1) + 4);
                            this.twidth = calcEquipMaxW(itm);
                        }
                    }
                    refreshTicker();
                    this.needsRefresh = false;
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        if (System.currentTimeMillis() - this.idleTime > 2000) {
            this.tick++;
        }
    }

    private void checkTime() {
        if (this.tabItem.type == 19) {
            this.setTime -= System.currentTimeMillis() - this.checkTime;
            this.checkTime = System.currentTimeMillis();
        }
    }

    public String getTimeString() {
        if (this.tabItem.type != 19) {
            return "";
        }
        checkTime();
        int t = (int) (this.setTime / 1000);
        int sec = t % 60;
        int minute = (t / 60) % 60;
        int hour = t / 3600;
        if (sec < 0) {
            sec = 0;
        }
        if (minute < 0) {
            minute = 0;
        }
        String m = String.valueOf(minute);
        String s = String.valueOf(sec);
        if (m.length() < 2) {
            m = "0" + m;
        }
        if (s.length() < 2) {
            s = "0" + s;
        }
        return String.valueOf(hour) + ":" + m + ":" + s;
    }

    private int calcEquipMaxW(GameItem item) {
        int w;
        int nameW = Utilities.font.stringWidth(item.name);
        int starW = item.start * 2 * Tool.uiMiscImg.getFrameWidth(43);
        int reqLvW = Utilities.font.stringWidth("   ") + 52 + (Tool.smallNum.getFrameWidth(0) * new Integer(item.reqLevel).toString().length());
        int[] attrW = new int[item.attrAdd.length];
        for (int i = 0; i < item.attrAdd.length; i += 2) {
            attrW[i] = 160;
        }
        if (nameW > starW) {
            w = nameW;
        } else {
            w = starW;
        }
        if (w <= reqLvW) {
            w = reqLvW;
        }
        for (int i2 = 0; i2 < item.attrAdd.length; i2++) {
            if (attrW[i2] > w) {
                w = attrW[i2];
            }
        }
        return w;
    }

    public void draw(Graphics g) {
        g.getCanvas().save();
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        Object item = getItem();
        if (this.isEquip) {
            GameItem itm = (GameItem) item;
            if (!this.isTopTri) {
                itm.drawEquipTip(g, this.tx, this.ty, this.twidth, getHeight(), this.triX, 16);
            } else if (this.tipLoaderType == 0) {
                itm.drawEquipTip(g, this.tx, this.ty, this.twidth, getHeight(), this.triX, 32);
            } else {
                itm.drawEquipTip(g, this.tx, this.ty, this.twidth, getHeight(), this.triX, 32);
            }
        } else if (this.sd != null) {
            int oriX = g.getClipX();
            int oriY = g.getClipY();
            int oriW = g.getClipWidth();
            int oriH = g.getClipHeight();
            if (this.tipLoaderType == 0) {
                int off = 10;
                int point = 16;
                if (this.isTopTri) {
                    off = 1;
                    point = 32;
                }
                UI.drawDialoguePanel(g, this.tx, this.ty + 1, this.twidth, (this.showLines * Utilities.LINE_HEIGHT) + 10, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[21], point);
                if (this.sd.length() <= this.showLines) {
                    g.setClip(this.tx, this.ty + off, this.twidth, this.showLines * Utilities.LINE_HEIGHT);
                    this.sd.drawShadow(g, this.tx + 4, this.ty + off, Tool.CLR_ITEM_WHITE, 0, false);
                    this.sd.draw(g, this.tx + 4, this.ty + off, Tool.CLR_ITEM_WHITE);
                } else {
                    int newPos = this.ty - this.tick;
                    if ((this.sd.length() * Utilities.LINE_HEIGHT) + newPos < this.ty) {
                        refreshTicker();
                    } else {
                        g.setClip(this.tx, this.ty + off, this.twidth, this.showLines * Utilities.LINE_HEIGHT);
                        this.sd.drawShadow(g, this.tx + 4, newPos + off, Tool.CLR_ITEM_WHITE, 0, false);
                    }
                }
            } else if (this.tipLoaderType == 1 || this.tipLoaderType == 2) {
                int off2 = 10;
                int point2 = 16;
                if (this.isTopTri) {
                    off2 = 2;
                    point2 = 32;
                }
                if (this.triX == -1) {
                    UI.drawDialoguePanel(g, this.tx, this.ty + 1, this.twidth, (this.showLines * Utilities.LINE_HEIGHT) + 10, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[21], point2);
                } else {
                    UI.drawDialoguePanel(g, this.tx, this.ty + 1, this.triX, this.twidth, (this.showLines * Utilities.LINE_HEIGHT) + 6, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[21], point2);
                }
                if (this.sd.length() <= this.showLines) {
                    g.setClip(this.tx, this.ty + off2, this.twidth, this.showLines * Utilities.LINE_HEIGHT);
                    this.sd.draw3D(g, this.tx + 4, this.ty + off2, Tool.CLR_ITEM_WHITE, 0);
                } else {
                    int newPos2 = this.ty - this.tick;
                    if ((this.sd.length() * Utilities.LINE_HEIGHT) + newPos2 < this.ty) {
                        refreshTicker();
                    } else {
                        g.setClip(this.tx, this.ty + off2, this.twidth, this.showLines * Utilities.LINE_HEIGHT);
                        this.sd.draw3D(g, this.tx + 4, newPos2 + off2, Tool.CLR_ITEM_WHITE, 0);
                    }
                }
            }
            g.setClip(oriX, oriY, oriW, oriH);
        } else if (this.tabItem.type == 19) {
            int point3 = 16;
            if (this.isTopTri) {
                point3 = 32;
            }
            UI.drawDialoguePanel(g, this.tx, this.ty + 1, this.twidth, (this.showLines * Utilities.LINE_HEIGHT) + 10, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[21], point3);
            String time = getTimeString();
            Tool.draw3DString(g, "等待响应时间", this.tx + 10, this.ty + 10, Tool.CLR_ITEM_WHITE, 0);
            Tool.draw3DString(g, time, this.tx + 100, this.ty + 10, Tool.CLR_ITEM_WHITE, 0);
            int oriX2 = g.getClipX();
            int oriY2 = g.getClipY();
            int oriW2 = g.getClipWidth();
            int oriH2 = g.getClipHeight();
            g.setClip(0, 0, 240, 320);
            this.guildString.draw3D(g, this.tx + 10, Utilities.LINE_HEIGHT + this.ty + 10, AbstractUnit.CLR_NAME_TAR_RED, 0);
            g.setClip(oriX2, oriY2, oriW2, oriH2);
        } else {
            return;
        }
        g.getCanvas().restore();
    }

    private void setBounds(int x, int y, int w) {
        this.tx = x;
        this.ty = y;
        this.twidth = w;
    }

    public int getTriX() {
        return this.triX;
    }

    public void setBounds(int x, int y, int w, int trix, boolean topTri) {
        this.isTopTri = topTri;
        Object item = getItem();
        if (item == null || !(item instanceof GameItem) || ((GameItem) item).type != 29) {
            this.triX = trix;
            setBounds(x, y, w);
            return;
        }
        if (!((GameItem) item).name.equals("") && this.tipLoaderType == 1) {
            while (trix - x > (calcEquipMaxW((GameItem) item) - 6) - 13) {
                x += 5;
            }
            this.triX = (trix - x) + 5;
        }
        setBounds(x, y, w);
    }

    private void refreshTicker() {
        this.tick = 0;
        this.idleTime = System.currentTimeMillis();
    }

    private void setShowLines(int line) {
        this.showLines = line;
    }

    public TableItem getTableItem() {
        if (this.tabItem != null) {
            return this.tabItem;
        }
        return null;
    }

    public void setTipLoaderType(int tipLoaderType2) {
        this.tipLoaderType = tipLoaderType2;
    }

    public void setAppend(TableItem append2) {
        this.append = append2;
    }

    public void setSelection(int sel) {
        if (sel != this.selection) {
            this.selection = sel;
            this.needsRefresh = true;
        }
    }
}
