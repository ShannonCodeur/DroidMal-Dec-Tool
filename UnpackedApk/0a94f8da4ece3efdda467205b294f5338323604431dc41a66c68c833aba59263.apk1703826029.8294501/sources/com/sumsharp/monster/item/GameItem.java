package com.sumsharp.monster.item;

import com.sumsharp.lowui.StringDraw;
import com.sumsharp.lowui.UI;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;

public class GameItem {
    public static final int BLESS_PRO_AGI = 8;
    public static final int BLESS_PRO_DEF = 64;
    public static final int BLESS_PRO_GROWUP = 1;
    public static final int BLESS_PRO_INT = 16;
    public static final int BLESS_PRO_SPI = 32;
    public static final int BLESS_PRO_STR = 2;
    public static final int BLESS_PRO_VIT = 4;
    public static final String[] ITEM_QUANLITY = {"劣质", "普通", "优良", "精良", "史诗", "传说"};
    public static final byte ITEM_SELLFLAG_TONPC = 2;
    public static final byte ITEM_SELLFLAG_TOPLAYER = 1;
    public static final byte ITEM_USETYPE_ARMYS = 32;
    public static final byte ITEM_USETYPE_BATTLE = 4;
    public static final byte ITEM_USETYPE_FRIENDS = 16;
    public static final byte ITEM_USETYPE_OTHER = 64;
    public static final byte ITEM_USETYPE_OTHERPET = 2;
    public static final byte ITEM_USETYPE_SELF = 8;
    public static final byte ITEM_USETYPE_SELFPET = 1;
    public static Hashtable ItemDescMap = new Hashtable();
    public static Hashtable ItemDescRequestMap = new Hashtable();
    public static Hashtable ItemIconMap = new Hashtable();
    public static Hashtable ItemIconRequestMap = new Hashtable();
    public static final int STOREITEM_BUYCON_CREDIT = 0;
    public static final int STOREITEM_BUYCON_HONOR = 1;
    public static final int STOREITEM_PRIZE_CREDIT = 2;
    public static final int STOREITEM_PRIZE_CRYSTAL = 3;
    public static final int STOREITEM_PRIZE_ITEM = 1;
    public static final int STOREITEM_PRIZE_MONEY = 0;
    public static final int STOREITEM_PRIZE_TOKEN = 4;
    public int add;
    public int agiRate;
    public byte[] attrAdd;
    public int[] attrAddValue;
    public short blessFlag;
    public BuyCondition[] cons;
    public byte count;
    public int defRate;
    public String desc;
    public int growupRate;
    public int hatchPrice;
    public int iconId;
    public int id;
    public int intRate;
    public int itemId;
    public short level;
    public byte max;
    public String name;
    public String part;
    public byte partId;
    public int petRace;
    public int petState;
    public int price;
    public BuyPrice[] prices;
    public byte quanlity;
    public short reqLevel;
    public boolean requestDesc = false;
    public boolean requestIcon = false;
    public byte sellFlag;
    public int spiRate;
    public int start;
    public int strRate;
    public int tipBoundHeight;
    public int tipBoundWidth;
    public int tipBoundX;
    public int tipBoundY;
    public int tipDir;
    public StringDraw tipDraw = null;
    public int tipHeight;
    public int tipMode = 0;
    public int tipWidth;
    public int triX;
    public byte type;
    public byte useFlag;
    public String useTip = "";
    public int vitRate;

    public boolean canUseSelfPet() {
        return (this.useFlag & 1) != 0;
    }

    public boolean canUseOtherPet() {
        return (this.useFlag & 2) != 0;
    }

    public boolean canUseSelf() {
        return (this.useFlag & 8) != 0;
    }

    public boolean canUseOther() {
        return (this.useFlag & 64) != 0;
    }

    public boolean canuseBattle() {
        return (this.useFlag & 4) != 0;
    }

    public boolean canSellToNpc() {
        return (this.sellFlag & 2) != 0;
    }

    public boolean canTradeWithPlayer() {
        return (this.sellFlag & 1) != 0;
    }

    public int getUseTarget() {
        if ((this.useFlag & 16) != 0) {
            return 16;
        }
        if ((this.useFlag & 32) != 0) {
            return 32;
        }
        return 0;
    }

    private void updateTip() {
        int maxWidth = this.tipBoundWidth;
        this.tipDraw = new StringDraw(getDesc(), maxWidth - 5, -1);
        int twidth = this.tipDraw.getMaxWidth();
        if (maxWidth - twidth > 10) {
            maxWidth = twidth + 10;
        }
        this.tipWidth = maxWidth;
        this.tipHeight = (this.tipDraw.length() * Utilities.LINE_HEIGHT) + 6;
        if (this.triX == -1) {
            this.triX = 10;
        }
    }

    public void updateTip(int x, int y, int triX2, int maxWidth, int maxHeight) {
        this.tipBoundX = x;
        this.tipBoundY = y;
        this.tipDir = 16;
        this.triX = triX2;
        this.tipBoundWidth = maxWidth;
        this.tipBoundHeight = maxHeight;
        updateTip();
    }

    public void drawEquipTip(Graphics g, int tx, int ty, int width, int height, int triX2, int point) {
        int y;
        int x = tx + 5;
        int y2 = ty + Utilities.CHAR_HEIGHT;
        if (height != -1) {
            if (triX2 == -1) {
                UI.drawDialoguePanel(g, tx, ty, width, height, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[21], point);
            } else {
                UI.drawDialoguePanel(g, tx, ty, triX2, width, height, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[21], point);
            }
        }
        if (this.name.equals("")) {
            StringDraw sd = new StringDraw(getDesc(), width - 5, -1);
            if (point == 16) {
                sd.draw3D(g, x, ty + 10, Tool.CLR_ITEM_WHITE, 0);
                int i = y2;
                int i2 = x;
            } else if (point == 32) {
                sd.draw3D(g, x, ty, Tool.CLR_ITEM_WHITE, 0);
                int i3 = y2;
                int i4 = x;
            } else {
                int i5 = x;
            }
        } else {
            int clr = Tool.getQuanlityColor(this.quanlity);
            if (point == 16) {
                y = y2 + 9;
            } else {
                y = y2;
            }
            Tool.draw3DString(g, this.name, x, y, clr, 0, 36);
            int y3 = y + 2;
            Tool.drawImageFont(g, this.part, x, y3);
            int w = Tool.uiMiscImg.getFrameWidth(43);
            int dx = x + 30;
            int dc = this.start << 1;
            for (int i6 = 0; i6 < dc; i6++) {
                int trans = 0;
                if (i6 % 2 != 0) {
                    trans = 2;
                }
                Tool.uiMiscImg.drawFrame(g, 43, dx, y3 + 5, trans, 6);
                dx += w;
            }
            int y4 = y3 + 14;
            Tool.drawImageFont(g, "需要等级", x, y4);
            Tool.drawNumStr(" " + this.reqLevel, g, x + 52, y4 + 2, 0, 0, -1);
            int y5 = y4 + 14;
            int i7 = 0;
            int x2 = x;
            while (i7 < this.attrAdd.length && i7 < this.attrAddValue.length) {
                if (i7 % 2 == 0) {
                    x2 = tx + 5;
                }
                Tool.drawImageFont(g, Pet.ATTR_TEXT[this.attrAdd[i7]], x2, y5);
                int x3 = x2 + 27;
                Tool.drawNumStr("+", g, x3, y5 + 7, 0, 6, -1);
                int x4 = x3 + 9;
                Tool.drawNumStr(String.valueOf(this.attrAddValue[i7]), g, x4, y5 + 7, 0, 6, -1);
                x2 = x4 + 43;
                if (i7 % 2 != 0) {
                    y5 += Utilities.LINE_HEIGHT;
                }
                i7++;
            }
        }
    }

    public void drawTip(Graphics g, int x, int y) {
        int dx = x;
        int dy = y;
        if (this.tipHeight + dy > this.tipBoundY + this.tipBoundHeight) {
            dy -= this.tipHeight + 20;
            this.tipDir = 32;
        }
        int triOffset = 0;
        if (this.tipWidth < this.tipBoundWidth) {
            triOffset = (this.triX - this.tipWidth) + (this.tipWidth >> 1) + 6;
            dx += triOffset;
            if (this.tipWidth + dx > this.tipBoundX + this.tipBoundWidth) {
                int sub = (this.tipWidth + dx) - (this.tipBoundX + this.tipBoundWidth);
                dx -= sub;
                triOffset -= sub;
            } else if (dx < this.tipBoundX) {
                int sub2 = this.tipBoundX - dx;
                dx += sub2;
                triOffset += sub2;
            }
        }
        UI.drawDialoguePanel(g, dx, dy, this.triX - triOffset, this.tipWidth, this.tipHeight, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[21], this.tipDir);
        int off = 0;
        if (this.tipDir == 16) {
            off = 10;
        }
        if (this.tipDraw != null) {
            this.tipDraw.drawShadow(g, dx + 4, dy + off, Tool.CLR_ITEM_WHITE, 0, true);
        }
    }

    public void drawIcon(Graphics g, int x, int y, int anchor) {
        int fid;
        ImageSet icon = (ImageSet) ItemIconMap.get(new Integer(this.iconId));
        if (icon == null) {
            try {
                requestIcon();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.type - 1 > 0) {
                fid = this.type - 1;
            } else {
                fid = 0;
            }
            if (Tool.defaultIcon.getFrameCount() < this.type) {
                fid = ((byte) Tool.defaultIcon.getFrameCount()) - 1;
            }
            Tool.defaultIcon.drawFrame(g, fid, x, y, 0, anchor);
            return;
        }
        icon.drawFrame(g, 0, x, y, 0, anchor);
    }

    public static void updateIcon(ImageSet img, int iconId2) {
        ItemIconRequestMap.remove(new Integer(iconId2));
        ItemIconMap.put(new Integer(iconId2), img);
        if (img.pipImg != null) {
            img.pipImg.toFullBuffer();
        }
    }

    public static void updateDesc(String str, int itemId2) {
        ItemDescRequestMap.remove(new Integer(itemId2));
        ItemDescMap.put(new Integer(itemId2), str);
    }

    public void loadPetRate(DataInputStream dis) {
        try {
            this.growupRate = dis.readInt();
            this.strRate = dis.readInt();
            this.vitRate = dis.readInt();
            this.agiRate = dis.readInt();
            this.intRate = dis.readInt();
            this.spiRate = dis.readInt();
            this.defRate = dis.readInt();
            this.blessFlag = dis.readShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPetRate(int idx) {
        switch (idx) {
            case 0:
                return this.growupRate;
            case 1:
                return this.strRate;
            case 2:
                return this.vitRate;
            case 3:
                return this.agiRate;
            case 4:
                return this.intRate;
            case 5:
                return this.spiRate;
            case 6:
                return this.defRate;
            default:
                return 0;
        }
    }

    public void load(DataInputStream dis) {
        try {
            this.id = dis.readInt();
            this.count = dis.readByte();
            try {
                this.name = dis.readUTF();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.type = dis.readByte();
            this.useFlag = dis.readByte();
            this.level = dis.readShort();
            this.reqLevel = dis.readShort();
            this.quanlity = dis.readByte();
            this.price = dis.readInt();
            this.max = dis.readByte();
            this.sellFlag = dis.readByte();
            this.useTip = dis.readUTF();
            this.desc = dis.readUTF();
            this.iconId = dis.readInt();
            int code = dis.readInt();
            if (this.type == 29) {
                this.start = dis.readByte();
                this.add = dis.readByte();
                this.partId = dis.readByte();
                this.part = dis.readUTF();
                byte c = dis.readByte();
                this.attrAdd = new byte[c];
                this.attrAddValue = new int[c];
                for (int i = 0; i < c; i++) {
                    this.attrAdd[i] = dis.readByte();
                    this.attrAddValue[i] = dis.readInt();
                }
            }
            if (code != -1) {
                this.itemId = code;
                if (this.type == 8) {
                    this.hatchPrice = dis.readInt();
                    this.petState = dis.readByte();
                    this.petRace = dis.readByte();
                    loadPetRate(dis);
                }
            } else if (code == -1) {
                byte conCount = dis.readByte();
                this.cons = new BuyCondition[conCount];
                for (int i2 = 0; i2 < conCount; i2++) {
                    this.cons[i2] = new BuyCondition();
                    this.cons[i2].load(dis);
                }
                byte priCount = dis.readByte();
                this.prices = new BuyPrice[priCount];
                for (int i3 = 0; i3 < priCount; i3++) {
                    this.prices[i3] = new BuyPrice();
                    this.prices[i3].load(dis);
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public String getDesc() {
        String reqStr;
        String s;
        if (this.type == 29) {
            return this.desc;
        }
        if (this.desc == null) {
            String cache = (String) ItemDescMap.get(new Integer(this.id));
            if (cache == null) {
                try {
                    requestDesc();
                } catch (Exception e) {
                }
                return "正在下载物品信息...";
            }
            this.desc = cache;
            updateTip();
        }
        String title = "<c" + Integer.toHexString(Tool.getQuanlityColor(this.quanlity)) + ">" + this.name + "</c>";
        if (this.type == 8) {
            title = String.valueOf(title) + "\n";
        }
        String reqStr2 = "";
        if (this.reqLevel > 1) {
            if (this.reqLevel > CommonData.player.level) {
                reqStr2 = "\n<cff0000>需要级别(" + this.reqLevel + ")</c>";
            } else {
                reqStr2 = "\n需要级别(" + this.reqLevel + ")";
            }
        }
        if (this.cons != null && this.cons.length > 0) {
            if (!reqStr.endsWith("\n")) {
                reqStr = String.valueOf(reqStr) + "\n";
            }
            for (int i = 0; i < this.cons.length; i++) {
                String s2 = this.cons[i].getConString();
                if (this.cons[i].check(CommonData.player)) {
                    s = "<c00ff00>" + s2 + "</c>";
                } else {
                    s = "<cff0000>" + s2 + "</c>";
                }
                reqStr = String.valueOf(reqStr) + s;
                if (i < this.cons.length - 1) {
                    reqStr = String.valueOf(reqStr) + "\n";
                }
            }
        }
        String ret = String.valueOf(title) + this.desc + reqStr;
        if (this.tipMode != 0) {
            ret = String.valueOf(title) + "\n按OK键查看详细信息";
        }
        return ret;
    }

    private void requestIcon() throws Exception {
        if (this.iconId != -1 && !this.requestIcon) {
            requestIcon(this.iconId);
            this.requestIcon = true;
        }
    }

    public static void requestIcon(int iconId2) throws Exception {
        if (ItemIconRequestMap.get(new Integer(iconId2)) == null) {
            UWAPSegment seg = new UWAPSegment(18, 16);
            seg.writeInt(iconId2);
            Utilities.sendRequest(seg);
            ItemIconRequestMap.put(new Integer(iconId2), new Integer((int) System.currentTimeMillis()));
        }
    }

    private void requestDesc() throws Exception {
        if (this.desc == null && !this.requestDesc) {
            requestDesc(this.id);
            this.requestDesc = true;
        }
    }

    public static void requestDesc(int iconId2) throws Exception {
        if (ItemDescRequestMap.get(new Integer(iconId2)) == null) {
            UWAPSegment seg = new UWAPSegment(18, 14);
            seg.writeInt(iconId2);
            Utilities.sendRequest(seg);
            ItemDescRequestMap.put(new Integer(iconId2), new Integer((int) System.currentTimeMillis()));
        }
    }
}
