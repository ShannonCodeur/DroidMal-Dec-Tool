package com.sumsharp.monster.common.data;

import com.sumsharp.monster.Battle;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.PipImage;
import com.sumsharp.monster.item.GameItem;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Pet extends AbstractUnit {
    public static final byte ATTR_ACTIONVALUE = 27;
    public static final byte ATTR_AGI = 3;
    public static final byte ATTR_CLASS = 53;
    public static final byte ATTR_HEALTH = 55;
    public static final byte ATTR_HELTHADD = 18;
    public static final byte ATTR_HP = 23;
    public static final byte ATTR_HPMAX = 25;
    public static final byte ATTR_HUNGRY = 57;
    public static final byte ATTR_INT = 4;
    public static final byte ATTR_LEVEL = 50;
    public static final byte ATTR_LOYAL = 54;
    public static final byte ATTR_MATK_MAX = 13;
    public static final byte ATTR_MATK_MIN = 12;
    public static final byte ATTR_MCRI = 17;
    public static final byte ATTR_MDEF = 14;
    public static final byte ATTR_MHIT = 15;
    public static final byte ATTR_MP = 24;
    public static final byte ATTR_MPMAX = 26;
    public static final byte ATTR_MRES = 16;
    public static final byte ATTR_PATK_MAX = 7;
    public static final byte ATTR_PATK_MIN = 6;
    public static final byte ATTR_PCRI = 11;
    public static final byte ATTR_PDEF = 8;
    public static final byte ATTR_PDOD = 10;
    public static final byte ATTR_PHIT = 9;
    public static final byte ATTR_PHYCRIRATE = 28;
    public static final byte ATTR_PRO_BLIND = 37;
    public static final byte ATTR_PRO_CONFUSION = 34;
    public static final byte ATTR_PRO_DARK = 30;
    public static final byte ATTR_PRO_DEATH = 32;
    public static final byte ATTR_PRO_EARTH = 19;
    public static final byte ATTR_PRO_FIRE = 21;
    public static final byte ATTR_PRO_LIGHT = 29;
    public static final byte ATTR_PRO_NONE = 31;
    public static final byte ATTR_PRO_SEALMAGIC = 33;
    public static final byte ATTR_PRO_SEALPHY = 36;
    public static final byte ATTR_PRO_SLEEP = 35;
    public static final byte ATTR_PRO_STONE = 38;
    public static final byte ATTR_PRO_WATER = 20;
    public static final byte ATTR_PRO_WIND = 22;
    public static final byte ATTR_QUANLITY = 58;
    public static final byte ATTR_RACE = 52;
    public static final byte[] ATTR_RESISTS = {19, 20, 21, 22, 29, 30, 32, 33, 34, 35, 36, 37, 38};
    public static final String[] ATTR_RESIST_TEXT = {"地", "水", "火", "风", "光", "暗", "厄运", "沉默", "混乱", "沉睡", "束缚", "失明", "石化"};
    public static final byte ATTR_SEX = 51;
    public static final byte ATTR_SPI = 5;
    public static final byte ATTR_STR = 1;
    public static final String[] ATTR_TEXT = {"", "力量", "耐力", "敏捷", "智力", "精神", "攻击", "攻击", "防御", "物理命中率", "物理闪躲", "物理暴击", "魔法攻击力(下限)", "魔法攻击力(上限)", "魔法防御力", "魔法命中", "魔法闪躲", "魔法暴击", "治疗效果", "地抗性", "水属性", "火属性", "风属性", "HP", "MP", "HP上限", "MP上限", "速度", "暴击倍数", "光属性", "暗属性", "无属性", "死亡宣告抗性", "魔法封印抗性", "混乱抗性", "沉睡抗性", "物理封印抗性", "失明抗性", "石化抗性"};
    public static final byte ATTR_VIT = 2;
    public static final String[] MONSTER_CLASS_NAME = {"不死系", "植物系", "恶魔系", "野兽系", "鸟系", "龙系", "虫系", "灵体系"};
    public static final String[] MONSTER_CLASS_TEXT = {"死", "植", "魔", "兽", "鸟", "龙", "虫", "灵"};
    public static String[] RATE_TEXT = {"总资质", "力量", "耐力", "敏捷", "智力", "精神", "防御"};
    public static final int STATUS_CATCHED = 4;
    public static final int STATUS_CONFUSE = 65536;
    public static final int STATUS_DIE = 1;
    public static final int STATUS_FAINT = 2048;
    public static final int STATUS_IMMUNITY_MAG = 16384;
    public static final int STATUS_IMMUNITY_PHY = 8192;
    public static final int STATUS_RUNAWAY = 2;
    public static final int STATUS_SLEEP = 32768;
    public static final int STATUS_STEAL_ALL = 8;
    public static final int STATUS_STEAL_DARK = 1024;
    public static final int STATUS_STEAL_EARTH = 32;
    public static final int STATUS_STEAL_FIRE = 128;
    public static final int STATUS_STEAL_LIGHT = 512;
    public static final int STATUS_STEAL_PHY = 16;
    public static final int STATUS_STEAL_WATER = 64;
    public static final int STATUS_STEAL_WIND = 256;
    public static final int STATUS_TEMPT = 4096;
    public static GameItem[] defaultItems = new GameItem[6];
    public int addValue = 0;
    public Hashtable attribute = new Hashtable();
    public int[] baseMonsterAttr = new int[6];
    public int baseMonsterId;
    public boolean battleOpened = false;
    public int battleStatus;
    public boolean canCatch = true;
    public long exp;
    public int expWidth;
    public int flyy = -1;
    public boolean followOpened = false;
    public int[] growUp = new int[6];
    public int hpWidth;
    public int[] inheritLess = new int[6];
    public int[] inheritMain = new int[6];
    public boolean isTarget = false;
    public GameItem[] items;
    public long levelupExp;
    public byte matingTimes;
    public int maxLevel;
    public int mpWidth;
    public int ownerId;
    public int[] rates = new int[7];
    public Vector resistChange = new Vector();
    private long restTime;
    public boolean selected = false;
    public boolean selfMove = false;
    public byte sex;
    public boolean show = true;
    public boolean showHp = true;
    public boolean showName = true;
    public Vector skills = new Vector();
    private short targetx = -1;
    private short targety = -1;

    static {
        for (int i = 0; i < defaultItems.length; i++) {
            defaultItems[i] = new GameItem();
            defaultItems[i].id = i;
            defaultItems[i].type = 29;
            defaultItems[i].name = "";
        }
        defaultItems[0].part = "武器";
        defaultItems[0].desc = "请在此处装备武器,单击菜单或者确定键选择装备";
        defaultItems[0].iconId = 232;
        defaultItems[1].part = "武器";
        defaultItems[1].desc = "请在此处装备武器,单击菜单或者确定键选择装备";
        defaultItems[1].iconId = 232;
        defaultItems[2].part = "防具";
        defaultItems[2].desc = "请在此处装备防具,单击菜单或者确定键选择装备";
        defaultItems[2].iconId = 233;
        defaultItems[3].part = "防具";
        defaultItems[3].desc = "请在此处装备防具,单击菜单或者确定键选择装备";
        defaultItems[3].iconId = 233;
        defaultItems[4].part = "饰品";
        defaultItems[4].desc = "请在此处装备饰品,单击菜单或者确定键选择装备";
        defaultItems[4].iconId = 234;
        defaultItems[5].part = "饰品";
        defaultItems[5].desc = "请在此处装备饰品,单击菜单或者确定键选择装备";
        defaultItems[5].iconId = 234;
    }

    public GameItem findItem(int itemid) {
        for (int i = 0; i < this.items.length; i++) {
            if (itemid == this.items[i].id) {
                return this.items[i];
            }
        }
        return null;
    }

    public boolean loadItem(int id, int pos) {
        if (pos < 0 || pos >= this.items.length) {
            return false;
        }
        GameItem item = CommonData.player.findItem(id);
        if (item == null) {
            return false;
        }
        this.items[pos] = item;
        return true;
    }

    public boolean unloadItem(int id, int pos) {
        if (pos < 0 || pos >= this.items.length) {
            return false;
        }
        if (findItem(id) == null) {
            return false;
        }
        this.items[pos] = defaultItems[pos];
        return true;
    }

    public Pet() {
        this.visible = true;
        initItems();
    }

    private void initItems() {
        this.items = new GameItem[6];
        for (int i = 0; i < this.items.length; i++) {
            this.items[i] = new GameItem();
        }
        this.items[0] = defaultItems[0];
        this.items[1] = defaultItems[1];
        this.items[2] = defaultItems[2];
        this.items[3] = defaultItems[3];
        this.items[4] = defaultItems[4];
        this.items[5] = defaultItems[5];
    }

    public GameItem findPetItem(int itemid) {
        return null;
    }

    public PipImage getImage() {
        return null;
    }

    public int[] getSkillIDs(int skillType) {
        Vector sel = new Vector();
        for (int i = 0; i < this.skills.size(); i++) {
            Skill skill = (Skill) this.skills.elementAt(i);
            if (skillType == -1 || skill.type == skillType) {
                sel.addElement(skill);
            }
        }
        int[] ret = new int[sel.size()];
        for (int i2 = 0; i2 < sel.size(); i2++) {
            ret[i2] = ((Skill) sel.elementAt(i2)).id;
        }
        return ret;
    }

    public void updateSkillLevel(int id, int level, short mp) {
        Skill skill = getSkill(id);
        if (skill == null) {
            return;
        }
        if (level == 0) {
            this.skills.removeElement(skill);
            return;
        }
        skill.level = (byte) level;
        skill.desc = null;
        skill.mp = mp;
        skill.requestDesc = false;
    }

    public void addSkill(byte[] skillInfo) {
        Skill skill = new Skill();
        skill.load(new DataInputStream(new ByteArrayInputStream(skillInfo)));
        this.skills.addElement(skill);
    }

    public Skill getSkill(int id) {
        for (int i = 0; i < this.skills.size(); i++) {
            Skill skill = (Skill) this.skills.elementAt(i);
            if (skill.id == id) {
                return skill;
            }
        }
        return null;
    }

    public void load(DataInputStream dis) throws IOException {
        int[] p;
        this.id = dis.readInt();
        this.name = dis.readUTF();
        this.title = dis.readUTF();
        this.matingTimes = dis.readByte();
        this.exp = dis.readLong();
        this.levelupExp = dis.readLong();
        setIconID(dis.readInt());
        this.maxLevel = dis.readInt();
        this.baseMonsterId = dis.readInt();
        for (int i = 0; i < 7; i++) {
            this.rates[i] = dis.readInt();
        }
        byte count = dis.readByte();
        for (int i2 = 0; i2 < count; i2++) {
            Skill skill = new Skill();
            skill.load(dis);
            this.skills.addElement(skill);
        }
        byte count2 = dis.readByte();
        for (int i3 = 0; i3 < count2; i3++) {
            this.attribute.put(new Byte(dis.readByte()), new Integer(dis.readInt()));
        }
        this.sex = (byte) getAttribute(51);
        int[] iArr = null;
        for (int t = 0; t < 12; t++) {
            if (t < 6) {
                p = this.inheritMain;
            } else {
                p = this.inheritLess;
            }
            p[t % 6] = dis.readInt();
        }
        for (int i4 = 0; i4 < 6; i4++) {
            this.baseMonsterAttr[i4] = dis.readInt();
        }
        for (int i5 = 0; i5 < 6; i5++) {
            this.growUp[i5] = dis.readInt();
        }
        byte c = dis.readByte();
        for (int i6 = 0; i6 < c; i6++) {
            this.resistChange.addElement(new Byte(dis.readByte()));
        }
        for (int i7 = 0; i7 < 6; i7++) {
            if (dis.readByte() != 0) {
                GameItem item = new GameItem();
                item.load(dis);
                this.items[i7] = item;
            }
        }
        refreshHpWidth();
        refreshMpWidth();
        refreshHpWidth();
    }

    public void setAttribute(byte attr, int value) {
        this.attribute.put(new Byte(attr), new Integer(value));
        switch (attr) {
            case 23:
            case 25:
                refreshHpWidth();
                return;
            case 24:
            case 26:
                refreshMpWidth();
                return;
            case 51:
                this.sex = (byte) value;
                return;
            default:
                return;
        }
    }

    public void refreshExpWidth() {
        if (this.levelupExp == 0) {
            this.expWidth = 0;
        } else {
            this.expWidth = (int) ((this.exp * ((long) World.instance.uiExpWidth)) / this.levelupExp);
        }
    }

    public void refreshHpWidth() {
        if (getAttribute(25) == 0) {
            this.hpWidth = 0;
        } else {
            this.hpWidth = (getAttribute(23) * World.instance.uiExpWidth) / getAttribute(25);
        }
    }

    public void refreshMpWidth() {
        if (getAttribute(26) == 0) {
            this.mpWidth = 0;
        } else {
            this.mpWidth = (getAttribute(24) * World.instance.uiExpWidth) / getAttribute(26);
        }
    }

    public int getAttribute(byte attr) {
        Integer ret = (Integer) this.attribute.get(new Byte(attr));
        if (ret == null) {
            return 0;
        }
        return ret.intValue();
    }

    public CartoonPlayer createCartoonPlayer() {
        this.cartoonPlayer = CartoonPlayer.playCartoon(Tool.defaultArmy, getFaceto(), this.pixelX, this.pixelY, true);
        return this.cartoonPlayer;
    }

    public String toString() {
        return String.valueOf(this.name) + " [LV" + getAttribute(50) + "]";
    }

    public String getDesc(boolean withName) {
        return String.valueOf(String.valueOf(String.valueOf(String.valueOf(withName ? String.valueOf(this.name) + "\n" : "") + "性别：" + (this.sex == 0 ? "♀" : "♂") + "  种族：" + MONSTER_CLASS_TEXT[getAttribute(52)] + "\n") + "等级：" + getAttribute(50) + "  合成：+" + this.matingTimes + "\n") + "HP: " + getAttribute(23) + "/" + getAttribute(25) + "\n") + "MP: " + getAttribute(24) + "/" + getAttribute(26);
    }

    public boolean isDie() {
        return (this.battleStatus & 1) != 0;
    }

    public boolean isRelife() {
        return !isDie() && this.showDie;
    }

    public boolean isRunaway() {
        return (this.battleStatus & 2) != 0;
    }

    public void clearStatus() {
        this.battleStatus = 0;
    }

    public void addStatus(int status) {
        this.battleStatus |= status;
    }

    public void removeStatus(int status) {
        this.battleStatus &= status ^ -1;
    }

    public int getAnimateState() {
        if (isDie()) {
            return 1;
        }
        if (isRelife()) {
            return 2;
        }
        return 0;
    }

    public void setDie() {
        this.showDie = true;
        this.showHp = false;
        setAttribute(23, 0);
        setAttribute(24, 0);
    }

    public void changeAttr(byte attr, int chg) {
        int v = getAttribute(attr) + chg;
        if (attr == 23) {
            if (v > getAttribute(25)) {
                v = getAttribute(25);
            }
            if (v < 0) {
                v = 0;
            }
        } else if (attr == 24) {
            if (v > getAttribute(26)) {
                v = getAttribute(26);
            }
            if (v < 0) {
                v = 0;
            }
        }
        setAttribute(attr, v);
    }

    public void draw(Graphics g) {
        if (this.visible) {
            if (this.inBattle && this.selected) {
                int[] off = this.cartoonPlayer.getAnimate().getFrameOffset(2, 0);
                int cw = getWidth();
                int ch = getHeight();
                int bw = Tool.uiMiscImg.getFrameWidth(84);
                int frameHeight = (ch - Tool.uiMiscImg.getFrameHeight(84)) + (ch >> 2);
                Tool.uiMiscImg.drawFrame(g, 84, (getPixelX() + ((cw - bw) >> 1)) - off[0], getPixelY(), 0, 6);
            }
            super.draw(g);
            if (this.selected) {
                int pixelY = (getPixelY() - getHeight()) - 10;
                int left = getPixelX() + (getWidth() / 2);
                String drawName = this.name;
                int nx = left - (Utilities.font.stringWidth(drawName) >> 1);
                int ny = (getPixelY() - getHeight()) - 4;
                if (this.ownerId == CommonData.player.id) {
                    ny -= 5;
                }
                Tool.draw3DString(g, drawName, nx, ny - Utilities.CHAR_HEIGHT, 16776960, 0);
                int top = getPixelY() - (getHeight() >> 1);
                int left2 = getPixelX() - 10;
                if (getFaceto() == 0) {
                    Battle.emotePlayerRight.draw(g, left2, top);
                } else {
                    Battle.emotePlayerLeft.draw(g, getWidth() + left2 + 10, top);
                }
            }
            if (this.inBattle && this.showHp && (this.selected || this.isTarget)) {
                int top2 = (getPixelY() - getHeight()) - 3;
                int left3 = (getPixelX() + (getWidth() / 2)) - 12;
                if (this.ownerId == CommonData.player.id) {
                    top2 -= 5;
                }
                drawHPBar(g, left3, top2, 25, 4, true, false);
                if (this.ownerId == CommonData.player.id) {
                    drawMPBar(g, left3, top2 + 4, 25, 4, true, false);
                }
            }
            if (this.flyy != -1) {
                Tool.draw3DString(g, "快乐度 +" + this.addValue, getPixelX() - ((Utilities.font.stringWidth("快乐度 +" + this.addValue) - getWidth()) / 2), ((getPixelY() - getHeight()) - 20) - this.flyy, AbstractUnit.CLR_NAME_TAR, 16776960);
            }
        }
    }

    public void drawExpBar(Graphics g, int left, int top, int width, int height, boolean drawBorder) {
        drawBar(g, left, top, width, height, this.exp, this.levelupExp, 16771851, 16771851, 16771851, drawBorder);
    }

    public void drawHPBar(Graphics g, int left, int top, int width, int height, boolean drawBorder, boolean drawPercent) {
        drawBar(g, left, top, width, height, (long) getAttribute(23), (long) getAttribute(25), 16729156, 16364724, 13833239, drawBorder);
        if (drawPercent) {
            int x = left + width + 4;
            Graphics graphics = g;
            Tool.drawNumStr(String.valueOf((getAttribute(23) * 100) / getAttribute(25)) + "%", graphics, x, top - 1, 2, 20, -1);
        }
    }

    public void drawMPBar(Graphics g, int left, int top, int width, int height, boolean drawBorder, boolean drawPercent) {
        drawBar(g, left, top, width, height, (long) getAttribute(24), (long) getAttribute(26), 4885503, 12178172, 7022580, drawBorder);
        if (drawPercent) {
            int mp = getAttribute(24);
            int mpMax = getAttribute(26);
            if (mpMax > 0) {
                int x = left + width + 4;
                Graphics graphics = g;
                Tool.drawNumStr(String.valueOf((mp * 100) / mpMax) + "%", graphics, x, top - 1, 2, 20, -1);
            }
        }
    }

    public int getWidth() {
        return this.cartoonPlayer.getWidth(2, 0);
    }

    public int getHeight() {
        return this.cartoonPlayer.getHeight(2, 0);
    }

    public static void drawBar(Graphics g, int x, int y, int width, int height, long value, long max, int col1, int col2, int col3, boolean drawBorder) {
        if (max != 0) {
            int len = (int) ((value * ((long) width)) / max);
            if (drawBorder) {
                g.setColor(0);
                g.drawLine(x + 1, y - 1, x + width, y - 1);
                g.drawLine(x + 1, y + 3, x + width, y + 3);
                g.drawLine(x, y, x, (y + height) - 2);
                g.drawLine(x + width + 1, y, width + x + 1, (y + height) - 2);
            }
            if (len > 0) {
                g.setColor(col1);
                g.drawLine(x + 1, y, x + len, y);
                g.setColor(col2);
                g.fillRect(x + 1, y + 1, len, height - 2);
                g.setColor(col3);
                g.drawLine(x + 1, (y + height) - 2, x + len, (y + height) - 2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void cycle() {
        if (this.selfMove) {
            if (this.flyy != -1) {
                this.flyy++;
            }
            if (this.flyy > 15) {
                this.flyy = -1;
            }
            if (this.targetx == -1) {
                this.targetx = (short) Utilities.random(360, 1000);
                this.targety = (short) Utilities.random(160, 240);
            }
            if (this.targetx == -2) {
                if (System.currentTimeMillis() - this.restTime > ((long) (this.targety * 1000))) {
                    this.targetx = (short) Utilities.random(360, 1000);
                    this.targety = (short) Utilities.random(160, 240);
                }
                setState(0);
            } else if (CommonData.player.targetPlayer == this) {
                setState(0);
            } else {
                setState(1);
                if (moveTo(this.targetx, this.targety)) {
                    this.targetx = (short) Utilities.random(360, 1000);
                    this.targety = (short) Utilities.random(160, 240);
                    if (Utilities.random(1, 100) < 30) {
                        this.targetx = -2;
                        this.targety = (short) Utilities.random(3, 10);
                        this.restTime = System.currentTimeMillis();
                    }
                }
            }
        }
    }
}
