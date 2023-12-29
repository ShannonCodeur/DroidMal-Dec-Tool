package com.sumsharp.lowui;

import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Friend;
import com.sumsharp.monster.common.data.NetPlayer;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.common.data.Skill;
import com.sumsharp.monster.item.GameItem;
import javax.microedition.lcdui.Graphics;

public class TableItem {
    public int[] bgclr;
    public long[] checkTime;
    public int[] clr;
    private int[] curr;
    public String id;
    public Object[] items;
    private int[] max;
    private UI parent;
    public int selection;
    public int type;

    public TableItem(String id2, String[] items2, int type2, int width) {
        this(id2, null, items2, type2, width);
    }

    public TableItem(String id2, UI parent2, String[] items2, int type2, int width) {
        GameItem item;
        this.type = 0;
        this.parent = parent2;
        this.items = new Object[items2.length];
        this.checkTime = new long[items2.length];
        if (type2 == 1 || type2 == 2 || type2 == 12) {
            this.items = items2;
        } else {
            System.arraycopy(items2, 0, this.items, 0, items2.length);
        }
        this.type = type2;
        this.id = id2;
        this.curr = new int[items2.length];
        this.max = new int[items2.length];
        for (int i = 0; i < this.items.length; i++) {
            if (type2 == 0 && !id2.startsWith("desc") && !id2.startsWith("appenddesc") && !id2.startsWith("taskdesc")) {
                String str = this.items[i].toString();
                if (Utilities.font.stringWidth(str) > width - 2) {
                    MovingString movingString = new MovingString(str, width - 15, 2);
                    this.items[i] = movingString;
                }
            } else if (type2 == 8 || type2 == 9 || type2 == 16 || type2 == 18) {
                String[] str2 = Utilities.splitString(items2[i], ",");
                int itemId = Integer.parseInt(str2[0]);
                int iconId = Integer.parseInt(str2[1]);
                int petid = -1;
                petid = str2.length == 3 ? Integer.parseInt(str2[2]) : petid;
                if (type2 == 16) {
                    Pet pet = CommonData.player.getPet(petid);
                    pet = pet == null ? (Pet) parent2.findData(0, petid) : pet;
                    if (pet != null) {
                        item = pet.findItem(itemId);
                    } else {
                        item = new GameItem();
                        item.name = "";
                        item.type = 29;
                        item.desc = "";
                    }
                } else if (type2 == 18) {
                    item = CommonData.player.getCoin(itemId);
                    if (item == null) {
                        item = new GameItem();
                        item.id = itemId;
                        item.iconId = iconId;
                        item.type = 18;
                    }
                } else {
                    item = CommonData.player.findItem(itemId);
                    if (item == null) {
                        item = new GameItem();
                        item.id = itemId;
                        item.iconId = iconId;
                        item.type = 1;
                    }
                }
                this.items[i] = item;
            } else if (type2 == 13) {
                String[] str3 = Utilities.splitString(items2[i], ",");
                this.curr[i] = Integer.parseInt(str3[0]);
                this.max[i] = Integer.parseInt(str3[1]);
                int proWidth = width - 8;
                this.curr[i] = (this.curr[i] * proWidth) / this.max[i];
                this.max[i] = proWidth;
                this.items[i] = items2[0];
            } else if (type2 == 10) {
                this.items[i] = CommonData.player.getPet(Integer.parseInt(items2[i]));
            } else if (type2 == 14) {
                String[] si = Utilities.splitString(items2[i], ",");
                int skillId = Integer.parseInt(si[0]);
                int skillLv = Integer.parseInt(si[1]);
                Skill skill = new Skill();
                skill.id = skillId;
                skill.level = (byte) skillLv;
                this.items[i] = skill;
            } else if (type2 == 20) {
                this.checkTime[i] = System.currentTimeMillis();
            }
        }
    }

    public void remove(int index) {
        if (index < this.items.length) {
            Object[] newItems = new Object[(this.items.length - 1)];
            System.arraycopy(this.items, 0, newItems, 0, index);
            System.arraycopy(this.items, 0, newItems, index + 1, (this.items.length - index) - 1);
            this.items = newItems;
        }
    }

    public int[] getClr(int idx, boolean enable) {
        int ftClr = Tool.CLR_TABLE[10];
        if (this.clr != null) {
            ftClr = this.clr[idx];
        }
        int bgClr = Tool.CLR_TABLE[11];
        if (this.bgclr != null) {
            bgClr = this.bgclr[idx];
        }
        if (!enable) {
            ftClr = Tool.CLR_TABLE[9];
        }
        return new int[]{ftClr, bgClr};
    }

    public void draw(Graphics g, int x, int y, int width, int height, int idx, boolean enable, boolean isFocus) {
        String ts;
        String ts2;
        int ftClr;
        if (idx >= 0 && idx < this.items.length) {
            switch (this.type) {
                case 0:
                    int[] clrs = getClr(idx, enable);
                    int ftClr2 = clrs[0];
                    int bgClr = clrs[1];
                    if (this.items[idx] instanceof MovingString) {
                        ((MovingString) this.items[idx]).draw3D(g, x + 2, y + 1, ftClr2, bgClr);
                        return;
                    } else {
                        Tool.draw3DString(g, this.items[idx].toString(), x + 2, y + 1, ftClr2, bgClr);
                        return;
                    }
                case 1:
                    Tool.drawSmallNum(this.items[idx].toString(), g, x + (width >> 1), y + 11, 3, -1);
                    return;
                case 2:
                    String iconname = (String) this.items[idx];
                    if (iconname.startsWith("building")) {
                        int id2 = Integer.parseInt(iconname.substring("building".length()));
                        if (id2 >= 0) {
                            Tool.building.drawFrame(g, id2, x + (width >> 1), y + 11, 0, 3);
                            return;
                        }
                        return;
                    }
                    int id3 = Integer.parseInt(this.items[idx].toString());
                    if (id3 >= 0) {
                        Tool.uiMiscImg.drawFrame(g, id3, x + (width >> 1), y + 11, 0, 3);
                        return;
                    }
                    return;
                case 3:
                    Tool.drawMoney(g, Integer.parseInt(this.items[idx].toString()), (x + width) - 1, y + 11, 0, 10);
                    return;
                case 4:
                    NetPlayer np = NewStage.getNetPlayer(Integer.parseInt(this.items[idx].toString()));
                    if (np != null) {
                        np.drawHead(g, x, y - 2);
                        return;
                    }
                    return;
                case 5:
                    NetPlayer np2 = NewStage.getNetPlayer(Integer.parseInt(this.items[idx].toString()));
                    if (np2 != null) {
                        int dx = x;
                        if (np2.teamRole == 1) {
                            Tool.uiMiscImg.drawFrame(g, 45, dx, y + 11, 0, 6);
                            dx += Tool.uiMiscImg.getFrameWidth(45) + 1;
                        }
                        switch (np2.getActionState()) {
                            case 1:
                                Tool.emote.drawFrame(g, 0, dx, y + 20);
                                return;
                            case 2:
                                Tool.emote.drawFrame(g, 3, dx, y + 20);
                                return;
                            case 3:
                                Tool.emote.drawFrame(g, 19, dx, y + 20);
                                return;
                            default:
                                return;
                        }
                    } else {
                        return;
                    }
                case 6:
                    (Integer.parseInt(this.items[idx].toString()) == 0 ? Tool.female : Tool.male).drawHead(g, x, y - 2);
                    return;
                case 7:
                    Friend friend = CommonData.player.getFriend(Integer.parseInt(this.items[idx].toString()));
                    if (friend != null) {
                        (friend.sex == 0 ? Tool.female : Tool.male).drawHead(g, x, y - 2);
                        return;
                    }
                    return;
                case 8:
                case 16:
                case 18:
                    ((GameItem) this.items[idx]).drawIcon(g, x, y + 11, 6);
                    return;
                case 9:
                    GameItem item = (GameItem) this.items[idx];
                    int c = 0;
                    if (item != null) {
                        c = item.count;
                    }
                    Tool.drawSmallNum(String.valueOf(c), g, x, y + (height >> 1), 6, -1);
                    return;
                case 10:
                    if (this.items[idx] != null) {
                        ((Pet) this.items[idx]).drawHead(g, x, y - 2);
                        return;
                    } else {
                        Tool.defaultArmy.drawHead(g, x, y - 2);
                        return;
                    }
                case 11:
                    int dx2 = x;
                    Tool.uiMiscImg.drawFrame(g, 32, dx2, y + 13, 0, 6);
                    Tool.drawNumStr(this.items[idx].toString(), g, dx2 + Tool.uiMiscImg.getFrameWidth(32) + 2, y + 13, 0, 6, -1);
                    return;
                case 12:
                    int sw = Utilities.font.stringWidth(this.items[idx].toString());
                    Tool.drawSmallNum(this.items[idx].toString(), g, x + (width >> 1), y + (height >> 1), 3, -1);
                    Tool.drawArrow(g, (((width - sw) >> 1) + x) - 7, y - 11, idx == this.selection ? 0 : 32);
                    Tool.drawArrow(g, (x + width) - 7, y - 11, (idx == this.selection ? 0 : 32) | 2);
                    return;
                case Tool.IMAGE_FONT_WIDTH /*13*/:
                    g.setColor(10296);
                    g.drawRect(x + 2, ((height - 7) >> 1) + y, this.max[idx], 6);
                    if (this.curr[idx] > 1) {
                        g.setColor(this.clr[idx]);
                        g.fillRect(x + 3, ((height - 7) >> 1) + y + 1, this.curr[idx] - 1, 5);
                        return;
                    }
                    return;
                case Tool.EDGE_ROUND_ALL /*15*/:
                    int[] clrs2 = getClr(idx, enable);
                    int i = clrs2[0];
                    int i2 = clrs2[1];
                    if (isFocus) {
                        ftClr = 16645839;
                    } else {
                        ftClr = 14928143;
                    }
                    int sw2 = Utilities.font.stringWidth(this.items[idx].toString());
                    Tool.draw3DString(g, this.items[idx].toString(), x + ((width - sw2) >> 1), y + 1, ftClr, 0);
                    Tool.drawArrow(g, (((width - sw2) >> 1) + x) - 2, y + 11, idx == this.selection ? 0 : 32);
                    Tool.drawArrow(g, (x + width) - Tool.img_triArrowR[0].getWidth(), y + 11, (idx == this.selection ? 0 : 32) | 2);
                    return;
                case 20:
                    int t = (int) (((long) Integer.parseInt(this.items[idx].toString())) - (System.currentTimeMillis() - this.checkTime[idx]));
                    if (t < 0) {
                        t = 0;
                    }
                    int t2 = t / 1000;
                    int s = t2 % 60;
                    String ts3 = String.valueOf(t2 / 3600) + ":";
                    if ((t2 % 3600) / 60 < 10) {
                        ts = String.valueOf(ts3) + "0" + minute + ":";
                    } else {
                        ts = String.valueOf(ts3) + minute + ":";
                    }
                    if (s < 10) {
                        ts2 = String.valueOf(ts) + "0" + s;
                    } else {
                        ts2 = String.valueOf(ts) + s;
                    }
                    Tool.drawSmallNum(ts2, g, x + (width >> 1), y + (height >> 1), 3, -1);
                    return;
                default:
                    return;
            }
        }
    }

    public void cycle(int selectIndex) {
    }
}
