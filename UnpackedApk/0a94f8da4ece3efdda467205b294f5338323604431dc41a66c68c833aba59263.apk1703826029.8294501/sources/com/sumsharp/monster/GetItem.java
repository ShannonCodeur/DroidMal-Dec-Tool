package com.sumsharp.monster;

import com.sumsharp.lowui.StringDraw;
import com.sumsharp.lowui.UI;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Credit;
import com.sumsharp.monster.common.data.MateInfo;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.common.data.Title;
import com.sumsharp.monster.item.GameItem;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class GetItem {
    public static final int GETITEM_SHOW_TIME = 5000;
    public static final byte GUILD_MONEY = 25;
    public static final byte GUILD_ORE = 24;
    public static final byte GUILD_WOOD = 23;
    public static final byte PET_LEVELUP = 18;
    public static final byte PLAYER_ARENALOSE = 13;
    public static final byte PLAYER_ARENAWIN = 12;
    public static final byte PLAYER_BAGSIZE = 19;
    public static final byte PLAYER_BANKSIZE = 20;
    public static final byte PLAYER_CONTRIBUTIONPOINT = 22;
    public static final byte PLAYER_CURR_EXP = 7;
    public static final byte PLAYER_EXP = 6;
    public static final byte PLAYER_HONOR = 10;
    public static final byte PLAYER_HONORIDX = 15;
    public static final byte PLAYER_HONOR_TTITLE = 11;
    public static final byte PLAYER_HPBUFFER = 14;
    public static final byte PLAYER_LEVEL = 9;
    public static final byte PLAYER_LEVELUP = 17;
    public static final byte PLAYER_LEVELUPMSG = 16;
    public static final byte PLAYER_LEVELUP_EXP = 8;
    public static final byte PLAYER_SEX = 21;
    public static final byte PRO_BATTLE_ATTR = 4;
    public static final byte PRO_CURR_EXP = 3;
    public static final byte PRO_EXP = 2;
    public static final byte PRO_LEVELUP_EXP = 5;
    public static final byte PRO_MONEY = 1;
    public static final int TASKTIP_SHOW_DELAY = 3000;
    public static final byte TYPE_BANKITEM = 6;
    public static final byte TYPE_CREDIT = 8;
    public static final byte TYPE_ITEM = 2;
    public static final byte TYPE_MATEINFO = 5;
    public static final byte TYPE_PET = 100;
    public static final byte TYPE_PROPERTY = 1;
    public static final byte TYPE_REMOVEBANKITEM = 7;
    public static final byte TYPE_REMOVEITEM = 3;
    public static final byte TYPE_REMOVEPET = 101;
    public static final byte TYPE_REMOVETITLE = 10;
    public static final byte TYPE_STRING = Byte.MAX_VALUE;
    public static final byte TYPE_TITLE = 9;
    public static final byte TYPE_UPDATE_ITEMCOUNT = 4;
    public static Vector battleGetItem = new Vector();
    public static Vector battleGetItemMsg = new Vector();
    public static GetItem getItem;
    private static String lvupMsg = null;
    public static long taskShowTime;
    public static int taskTipHeight;
    public static StringDraw taskTipMsg;
    public static Vector taskTipMsgs = new Vector();
    public static int taskTipWidth;
    public static int taskTipX;
    public static int taskTipY;
    private Hashtable expChgMap = new Hashtable();
    private int height;
    private Hashtable itemChgMap = new Hashtable();
    private int line;
    private int lineHeight = Utilities.LINE_HEIGHT;
    private int moneyChg = 0;
    private long timer;
    private int width;
    private int x;
    private int y;

    public static void addTaskTip(int id, String msg) {
        taskShowTime = -1;
        boolean found = false;
        int i = 0;
        while (true) {
            if (i >= taskTipMsgs.size()) {
                break;
            }
            Object[] item = (Object[]) taskTipMsgs.elementAt(i);
            if (((Integer) item[0]).intValue() == id) {
                item[1] = msg;
                found = true;
                break;
            }
            i++;
        }
        if (!found) {
            taskTipMsgs.addElement(new Object[]{new Integer(id), msg});
        }
        taskTipHeight = ((taskTipMsgs.size() + 1) * Utilities.LINE_HEIGHT) + 10;
        String tmp = "";
        for (int i2 = 0; i2 < taskTipMsgs.size(); i2++) {
            tmp = String.valueOf(tmp) + ((String) ((Object[]) taskTipMsgs.elementAt(i2))[1]);
            if (i2 < taskTipMsgs.size() - 1) {
                tmp = String.valueOf(tmp) + "\n";
            }
        }
        taskTipMsg = new StringDraw(tmp, World.viewWidth - 30, -1);
        for (int i3 = 0; i3 < taskTipMsg.length(); i3++) {
            int w = Utilities.font.stringWidth(taskTipMsg.getLineString(i3));
            if (w + 25 > taskTipWidth) {
                taskTipWidth = w + 25;
            }
        }
        taskTipY = (World.viewHeight - taskTipHeight) >> 1;
        taskTipX = (World.viewWidth - taskTipWidth) >> 1;
    }

    public static void clearTaskTip() {
        taskTipMsgs.removeAllElements();
    }

    public static void paintTaskTip(Graphics g) {
        if (taskTipMsgs.size() != 0) {
            if (taskShowTime == -1) {
                taskShowTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - taskShowTime > 3000) {
                clearTaskTip();
            }
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            UI.drawDialoguePanel(g, taskTipX, taskTipY, taskTipWidth, taskTipHeight, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[12], 0);
            int dx = taskTipX + 5;
            int dy = taskTipY + 5;
            Tool.draw3DString(g, "任务进度", dx, dy, Tool.CLR_TABLE[6], Tool.CLR_TABLE[5]);
            int dy2 = dy + Utilities.LINE_HEIGHT;
            Graphics graphics = g;
            taskTipMsg.draw3D(graphics, dx + 10, dy2, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
        }
    }

    public static GetItem get() {
        if (getItem == null) {
            getItem = new GetItem();
        }
        return getItem;
    }

    public static void processBattleGetItem() {
        Object[] getItems;
        Object[] objArr = null;
        synchronized (battleGetItem) {
            getItems = new Object[battleGetItem.size()];
            battleGetItem.copyInto(getItems);
            battleGetItem.removeAllElements();
        }
        for (Object obj : getItems) {
            String[] ret = parseGetItem((UWAPSegment) obj);
            for (String addElement : ret) {
                battleGetItemMsg.addElement(addElement);
            }
        }
    }

    public static String[] getBattleGetItemMsg() {
        String[] ret = new String[battleGetItemMsg.size()];
        for (int i = 0; i < battleGetItemMsg.size(); i++) {
            ret[i] = (String) battleGetItemMsg.elementAt(i);
        }
        battleGetItemMsg.removeAllElements();
        return ret;
    }

    public static String[] getItem(UWAPSegment segment) {
        if (segment.readByte() != 1 || !CommonData.player.inBattle) {
            segment.reset();
            return parseGetItem(segment);
        }
        battleGetItem.addElement(segment);
        segment.reset();
        return new String[0];
    }

    public static String[] parseGetItem(UWAPSegment segment) {
        String ret = "";
        segment.readByte();
        byte count = segment.readByte();
        for (int i = 0; i < count; i++) {
            byte[] item = segment.readBytes();
            switch (item[0]) {
                case 1:
                    ret = String.valueOf(ret) + getProperty(item);
                    break;
                case 2:
                    ret = String.valueOf(ret) + getItem(item);
                    break;
                case 3:
                    ret = String.valueOf(ret) + getRemoveItem(item);
                    break;
                case 4:
                    ret = String.valueOf(ret) + getItemUpdate(item);
                    break;
                case 5:
                    ret = String.valueOf(ret) + getMateInfo(item);
                    break;
                case 6:
                    getBankItem(item);
                    break;
                case 7:
                    getRemoveBankItem(item);
                    break;
                case 8:
                    ret = String.valueOf(ret) + getCredit(item);
                    break;
                case 9:
                    ret = String.valueOf(ret) + getTitle(item);
                    break;
                case 10:
                    ret = String.valueOf(ret) + getRemoveTitle(item);
                    break;
                case 100:
                    ret = String.valueOf(ret) + getPet(item);
                    break;
                case 101:
                    ret = String.valueOf(ret) + getRemovePet(item);
                    break;
                case Byte.MAX_VALUE:
                    ret = String.valueOf(ret) + getString(item);
                    break;
            }
        }
        while (ret.endsWith("\n")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        System.out.println(ret);
        return Tool.splitString(ret, 10);
    }

    private static String getTitle(byte[] bytes) {
        String ret = "";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte size = dis.readByte();
            for (int i = 0; i < size; i++) {
                Title title = new Title();
                title.load(dis);
                if (CommonData.player.addTitle(title)) {
                    ret = "你获得了一个新的称号：" + title.title + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getCredit(byte[] bytes) {
        String ret = "";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte size = dis.readByte();
            for (int i = 0; i < size; i++) {
                Credit credit = new Credit();
                credit.load(dis);
                Credit old = CommonData.player.findCriedit(credit.id);
                int oldv = 0;
                if (old != null) {
                    oldv = old.value;
                }
                int newv = credit.value;
                CommonData.player.addCredit(credit);
                int sub = newv - oldv;
                String m = "增加 ";
                if (sub < 0) {
                    m = "减少 ";
                    sub = -sub;
                }
                ret = String.valueOf(credit.title) + " 声望 " + m + sub + " 点\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getMateInfo(byte[] bytes) {
        String ret = "";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            short c = dis.readShort();
            for (int i = 0; i < c; i++) {
                MateInfo info = new MateInfo();
                info.load(dis);
                synchronized (CommonData.player.mateMap) {
                    CommonData.player.mateMap.addElement(info);
                }
                CommonData.player.orderMateMap();
                ret = String.valueOf(ret) + "你学会了 [合成 " + info.childTitle + "]\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /* JADX WARNING: type inference failed for: r14v12, types: [int] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r14v14, types: [int] */
    /* JADX WARNING: type inference failed for: r7v2 */
    /* JADX WARNING: type inference failed for: r14v94, types: [int] */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0022, code lost:
        r4 = r4 + 1;
        r8 = r0;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getProperty(byte[] r14) {
        /*
            java.lang.String r2 = ""
            java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream
            r0.<init>(r14)
            java.io.DataInputStream r1 = new java.io.DataInputStream
            r1.<init>(r0)
            r1.readByte()     // Catch:{ IOException -> 0x0357 }
            byte r9 = r1.readByte()     // Catch:{ IOException -> 0x0357 }
            r14 = 0
            r4 = r14
            r8 = r2
        L_0x0016:
            if (r4 < r9) goto L_0x001a
            r14 = r8
        L_0x0019:
            return r14
        L_0x001a:
            byte r5 = r1.readByte()     // Catch:{ IOException -> 0x035f }
            switch(r5) {
                case 1: goto L_0x0259;
                case 2: goto L_0x029c;
                case 3: goto L_0x029c;
                case 4: goto L_0x0306;
                case 5: goto L_0x029c;
                case 6: goto L_0x020a;
                case 7: goto L_0x01fa;
                case 8: goto L_0x0230;
                case 9: goto L_0x0240;
                case 10: goto L_0x01a0;
                case 11: goto L_0x01cd;
                case 12: goto L_0x01d8;
                case 13: goto L_0x01e9;
                case 14: goto L_0x016e;
                case 15: goto L_0x0195;
                case 16: goto L_0x0167;
                case 17: goto L_0x012b;
                case 18: goto L_0x0144;
                case 19: goto L_0x0102;
                case 20: goto L_0x00d9;
                case 21: goto L_0x00c8;
                case 22: goto L_0x0090;
                case 23: goto L_0x004a;
                case 24: goto L_0x006d;
                case 25: goto L_0x0027;
                default: goto L_0x0021;
            }     // Catch:{ IOException -> 0x035f }
        L_0x0021:
            r0 = r8
        L_0x0022:
            int r14 = r4 + 1
            r4 = r14
            r8 = r0
            goto L_0x0016
        L_0x0027:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r0.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "公会资金增加 "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r0.append(r14)     // Catch:{ IOException -> 0x035f }
            java.lang.String r0 = "\n"
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
            r0 = r14
            goto L_0x0022
        L_0x004a:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r0.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "公会资源-木材 增加 "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r0.append(r14)     // Catch:{ IOException -> 0x035f }
            java.lang.String r0 = "\n"
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
            r0 = r14
            goto L_0x0022
        L_0x006d:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r0.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "公会资源-矿石 增加 "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r0.append(r14)     // Catch:{ IOException -> 0x035f }
            java.lang.String r0 = "\n"
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
            r0 = r14
            goto L_0x0022
        L_0x0090:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            int r2 = r0.contributionPoint     // Catch:{ IOException -> 0x035f }
            int r2 = r2 + r14
            r0.contributionPoint = r2     // Catch:{ IOException -> 0x035f }
            if (r14 <= 0) goto L_0x0373
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r0.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "获得 公会贡献点数 "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r0.append(r14)     // Catch:{ IOException -> 0x035f }
            java.lang.String r0 = " 点\n"
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
        L_0x00ba:
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x0362 }
            int r0 = r0.contributionPoint     // Catch:{ IOException -> 0x0362 }
            if (r0 >= 0) goto L_0x0370
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x0362 }
            r2 = 0
            r0.contributionPoint = r2     // Catch:{ IOException -> 0x0362 }
            r0 = r14
            goto L_0x0022
        L_0x00c8:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            byte r14 = (byte) r14     // Catch:{ IOException -> 0x035f }
            r0.sex = r14     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            r14.resetCartoonPlayer()     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x00d9:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            short r2 = (short) r14     // Catch:{ IOException -> 0x035f }
            r0.bankSize = r2     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r0.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "银行扩充为 "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r0.append(r14)     // Catch:{ IOException -> 0x035f }
            java.lang.String r0 = " 格\n"
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
            r0 = r14
            goto L_0x0022
        L_0x0102:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            short r2 = (short) r14     // Catch:{ IOException -> 0x035f }
            r0.bagSize = r2     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r0.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "背包扩充为 "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r0.append(r14)     // Catch:{ IOException -> 0x035f }
            java.lang.String r0 = " 格\n"
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
            r0 = r14
            goto L_0x0022
        L_0x012b:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.NetPlayer r14 = com.sumsharp.monster.NewStage.getNetPlayer(r14)     // Catch:{ IOException -> 0x035f }
            if (r14 == 0) goto L_0x0021
            com.sumsharp.monster.image.PipAnimateSet r0 = com.sumsharp.monster.common.Tool.levelup     // Catch:{ IOException -> 0x035f }
            r2 = 0
            r3 = 0
            r5 = 0
            r6 = 0
            com.sumsharp.monster.image.CartoonPlayer r0 = com.sumsharp.monster.image.CartoonPlayer.playCartoon(r0, r2, r3, r5, r6)     // Catch:{ IOException -> 0x035f }
            r14.levelupPlayer = r0     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x0144:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.NetPlayer r14 = com.sumsharp.monster.NewStage.getNetPlayer(r14)     // Catch:{ IOException -> 0x035f }
            if (r14 == 0) goto L_0x0021
            com.sumsharp.monster.common.data.Pet r0 = r14.getFollowPet()     // Catch:{ IOException -> 0x035f }
            if (r0 == 0) goto L_0x0021
            com.sumsharp.monster.common.data.Pet r14 = r14.getFollowPet()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.image.PipAnimateSet r0 = com.sumsharp.monster.common.Tool.levelup     // Catch:{ IOException -> 0x035f }
            r2 = 0
            r3 = 0
            r5 = 0
            r6 = 0
            com.sumsharp.monster.image.CartoonPlayer r0 = com.sumsharp.monster.image.CartoonPlayer.playCartoon(r0, r2, r3, r5, r6)     // Catch:{ IOException -> 0x035f }
            r14.levelupPlayer = r0     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x0167:
            java.lang.String r14 = r1.readUTF()     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x016e:
            r14 = 0
        L_0x016f:
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            java.util.Vector r0 = r0.buffs     // Catch:{ IOException -> 0x035f }
            int r0 = r0.size()     // Catch:{ IOException -> 0x035f }
            if (r14 < r0) goto L_0x017c
            r0 = r8
            goto L_0x0022
        L_0x017c:
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            java.util.Vector r0 = r0.buffs     // Catch:{ IOException -> 0x035f }
            java.lang.Object r0 = r0.elementAt(r14)     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Buff r0 = (com.sumsharp.monster.common.data.Buff) r0     // Catch:{ IOException -> 0x035f }
            byte r2 = r0.type     // Catch:{ IOException -> 0x035f }
            r3 = 1
            if (r2 != r3) goto L_0x0192
            int r2 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            r0.decAmount(r2)     // Catch:{ IOException -> 0x035f }
        L_0x0192:
            int r14 = r14 + 1
            goto L_0x016f
        L_0x0195:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            r0.honorIdx = r14     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x01a0:
            int r0 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            if (r0 <= 0) goto L_0x0021
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r14.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "您获得了 "
            java.lang.StringBuilder r14 = r14.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = " 点帝国荣誉\n"
            java.lang.StringBuilder r14 = r14.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r2 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x0362 }
            int r3 = r2.honor     // Catch:{ IOException -> 0x0362 }
            int r0 = r0 + r3
            r2.honor = r0     // Catch:{ IOException -> 0x0362 }
            r0 = r14
            goto L_0x0022
        L_0x01cd:
            java.lang.String r14 = r1.readUTF()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            r0.honorTitle = r14     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x01d8:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            if (r14 <= 0) goto L_0x0021
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            int r0 = r14.arenaWin     // Catch:{ IOException -> 0x035f }
            int r0 = r0 + 1
            r14.arenaWin = r0     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x01e9:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            if (r14 <= 0) goto L_0x0021
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            int r0 = r14.arenaLose     // Catch:{ IOException -> 0x035f }
            int r0 = r0 + 1
            r14.arenaLose = r0     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x01fa:
            long r2 = r1.readLong()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            r14.exp = r2     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            r14.refreshExpWidth()     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x020a:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            if (r14 <= 0) goto L_0x0021
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r0.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "您获得了 "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r0.append(r14)     // Catch:{ IOException -> 0x035f }
            java.lang.String r0 = " 点经验值\n"
            java.lang.StringBuilder r14 = r14.append(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
            r0 = r14
            goto L_0x0022
        L_0x0230:
            long r2 = r1.readLong()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            r14.levelUpExp = r2     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            r14.refreshExpWidth()     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x0240:
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            r0.level = r14     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.image.PipAnimateSet r0 = com.sumsharp.monster.common.Tool.levelup     // Catch:{ IOException -> 0x035f }
            r2 = 0
            r3 = 0
            r5 = 0
            r6 = 0
            com.sumsharp.monster.image.CartoonPlayer r0 = com.sumsharp.monster.image.CartoonPlayer.playCartoon(r0, r2, r3, r5, r6)     // Catch:{ IOException -> 0x035f }
            r14.levelupPlayer = r0     // Catch:{ IOException -> 0x035f }
            r0 = r8
            goto L_0x0022
        L_0x0259:
            int r0 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            int r2 = r14.money     // Catch:{ IOException -> 0x035f }
            int r2 = r2 + r0
            r14.money = r2     // Catch:{ IOException -> 0x035f }
            if (r0 <= 0) goto L_0x036d
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = java.lang.String.valueOf(r8)     // Catch:{ IOException -> 0x035f }
            r14.<init>(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "您获得了 "
            java.lang.StringBuilder r14 = r14.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = com.sumsharp.monster.common.Tool.getMoneyString(r0)     // Catch:{ IOException -> 0x035f }
            java.lang.StringBuilder r14 = r14.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r2 = "\n"
            java.lang.StringBuilder r14 = r14.append(r2)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = r14.toString()     // Catch:{ IOException -> 0x035f }
        L_0x0287:
            com.sumsharp.monster.common.data.Player r2 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x0362 }
            int r2 = r2.money     // Catch:{ IOException -> 0x0362 }
            if (r2 >= 0) goto L_0x0292
            com.sumsharp.monster.common.data.Player r2 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x0362 }
            r3 = 0
            r2.money = r3     // Catch:{ IOException -> 0x0362 }
        L_0x0292:
            com.sumsharp.monster.GetItem r2 = get()     // Catch:{ IOException -> 0x0362 }
            r2.addMoneyChg(r0)     // Catch:{ IOException -> 0x0362 }
            r0 = r14
            goto L_0x0022
        L_0x029c:
            byte r6 = r1.readByte()     // Catch:{ IOException -> 0x035f }
            r14 = 0
            r7 = r8
        L_0x02a2:
            if (r14 < r6) goto L_0x02a7
            r0 = r7
            goto L_0x0022
        L_0x02a7:
            int r0 = r1.readInt()     // Catch:{ IOException -> 0x0367 }
            long r2 = r1.readLong()     // Catch:{ IOException -> 0x0367 }
            r8 = 3
            if (r5 != r8) goto L_0x02c4
            com.sumsharp.monster.common.data.Player r8 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x0367 }
            com.sumsharp.monster.common.data.Pet r0 = r8.getPet(r0)     // Catch:{ IOException -> 0x0367 }
            if (r0 == 0) goto L_0x036a
            r0.exp = r2     // Catch:{ IOException -> 0x0367 }
            r0.refreshExpWidth()     // Catch:{ IOException -> 0x0367 }
            r2 = r7
        L_0x02c0:
            int r14 = r14 + 1
            r7 = r2
            goto L_0x02a2
        L_0x02c4:
            r8 = 5
            if (r5 != r8) goto L_0x02d6
            com.sumsharp.monster.common.data.Player r8 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x0367 }
            com.sumsharp.monster.common.data.Pet r0 = r8.getPet(r0)     // Catch:{ IOException -> 0x0367 }
            if (r0 == 0) goto L_0x036a
            r0.levelupExp = r2     // Catch:{ IOException -> 0x0367 }
            r0.refreshExpWidth()     // Catch:{ IOException -> 0x0367 }
            r2 = r7
            goto L_0x02c0
        L_0x02d6:
            com.sumsharp.monster.common.data.Player r8 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x0367 }
            com.sumsharp.monster.common.data.Pet r0 = r8.getPet(r0)     // Catch:{ IOException -> 0x0367 }
            r10 = 0
            int r8 = (r2 > r10 ? 1 : (r2 == r10 ? 0 : -1))
            if (r8 <= 0) goto L_0x036a
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0367 }
            java.lang.String r10 = java.lang.String.valueOf(r7)     // Catch:{ IOException -> 0x0367 }
            r8.<init>(r10)     // Catch:{ IOException -> 0x0367 }
            java.lang.String r10 = r0.name     // Catch:{ IOException -> 0x0367 }
            java.lang.StringBuilder r8 = r8.append(r10)     // Catch:{ IOException -> 0x0367 }
            java.lang.String r10 = " 获得了 "
            java.lang.StringBuilder r8 = r8.append(r10)     // Catch:{ IOException -> 0x0367 }
            java.lang.StringBuilder r2 = r8.append(r2)     // Catch:{ IOException -> 0x0367 }
            java.lang.String r3 = " 点经验值\n"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ IOException -> 0x0367 }
            java.lang.String r2 = r2.toString()     // Catch:{ IOException -> 0x0367 }
            goto L_0x02c0
        L_0x0306:
            byte r3 = r1.readByte()     // Catch:{ IOException -> 0x035f }
            r14 = 0
            r0 = r14
        L_0x030c:
            if (r0 >= r3) goto L_0x0021
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            byte r6 = r1.readByte()     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Player r2 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Pet r2 = r2.getPet(r14)     // Catch:{ IOException -> 0x035f }
            r14 = 0
            r7 = r14
        L_0x031e:
            if (r7 < r6) goto L_0x0324
            int r14 = r0 + 1
            r0 = r14
            goto L_0x030c
        L_0x0324:
            byte r5 = r1.readByte()     // Catch:{ IOException -> 0x035f }
            int r14 = r1.readInt()     // Catch:{ IOException -> 0x035f }
            if (r2 == 0) goto L_0x0353
            r2.setAttribute(r5, r14)     // Catch:{ IOException -> 0x035f }
            java.lang.String r14 = ""
            r10 = 28
            if (r5 > r10) goto L_0x033b
            java.lang.String[] r14 = com.sumsharp.monster.common.data.Pet.ATTR_TEXT     // Catch:{ IOException -> 0x035f }
            r14 = r14[r5]     // Catch:{ IOException -> 0x035f }
        L_0x033b:
            r14 = 50
            if (r5 != r14) goto L_0x0353
            com.sumsharp.monster.common.data.Player r14 = com.sumsharp.monster.common.CommonData.player     // Catch:{ IOException -> 0x035f }
            com.sumsharp.monster.common.data.Pet r14 = r14.getFollowPet()     // Catch:{ IOException -> 0x035f }
            if (r2 != r14) goto L_0x0353
            com.sumsharp.monster.image.PipAnimateSet r14 = com.sumsharp.monster.common.Tool.levelup     // Catch:{ IOException -> 0x035f }
            r5 = 0
            r10 = 0
            r11 = 0
            r12 = 0
            com.sumsharp.monster.image.CartoonPlayer r14 = com.sumsharp.monster.image.CartoonPlayer.playCartoon(r14, r5, r10, r11, r12)     // Catch:{ IOException -> 0x035f }
            r2.levelupPlayer = r14     // Catch:{ IOException -> 0x035f }
        L_0x0353:
            int r14 = r7 + 1
            r7 = r14
            goto L_0x031e
        L_0x0357:
            r14 = move-exception
            r0 = r2
        L_0x0359:
            r14.printStackTrace()
            r14 = r0
            goto L_0x0019
        L_0x035f:
            r14 = move-exception
            r0 = r8
            goto L_0x0359
        L_0x0362:
            r0 = move-exception
            r13 = r0
            r0 = r14
            r14 = r13
            goto L_0x0359
        L_0x0367:
            r14 = move-exception
            r0 = r7
            goto L_0x0359
        L_0x036a:
            r2 = r7
            goto L_0x02c0
        L_0x036d:
            r14 = r8
            goto L_0x0287
        L_0x0370:
            r0 = r14
            goto L_0x0022
        L_0x0373:
            r14 = r8
            goto L_0x00ba
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.GetItem.getProperty(byte[]):java.lang.String");
    }

    private static String getItemUpdate(byte[] bytes) {
        GameItem item;
        String ret = "";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                byte location = dis.readByte();
                int id = dis.readInt();
                int c = dis.readByte();
                if (location == 0) {
                    item = CommonData.player.findItem(id);
                    if (item == null) {
                        item = CommonData.player.getCoin(id);
                    }
                } else {
                    item = CommonData.player.findItemInBank(id);
                }
                int change = c - item.count;
                if (change > 0 && location == 0) {
                    get().addItemChg(item, change);
                }
                if (item != null) {
                    item.count = (byte) c;
                    CommonData.player.coinRefresh = true;
                    if (location == 0) {
                        if (change > 0) {
                            ret = String.valueOf(ret) + "获得 " + item.name + " x " + Math.abs(change);
                        } else if (change < 0) {
                            ret = String.valueOf(ret) + "失去 " + item.name + " x " + Math.abs(change);
                        }
                        ret = String.valueOf(ret) + "\n";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getString(byte[] bytes) {
        String ret = "";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            for (int i = 0; i < dis.readByte(); i++) {
                ret = String.valueOf(ret) + dis.readUTF() + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getBankItem(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                GameItem item = new GameItem();
                item.load(dis);
                CommonData.player.addItemToBank(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getItem(byte[] bytes) {
        String ret = "";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                GameItem item = new GameItem();
                item.load(dis);
                CommonData.player.addItem(item);
                ret = String.valueOf(String.valueOf(ret) + "获得 " + item.name + " x " + item.count) + "\n";
                get().addItemChg(item, item.count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getPet(byte[] bytes) {
        String ret = "";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                Pet pet = new Pet();
                pet.load(dis);
                if (CommonData.player.addPet(pet) != null) {
                    ret = String.valueOf(ret) + "获得 宠物 " + pet + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getRemovePet(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                CommonData.player.removePet(dis.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getRemoveTitle(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                int id = dis.readInt();
                CommonData.player.removeTitle(id);
                if (CommonData.player.currTitle == id) {
                    CommonData.player.currTitle = -1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getRemoveBankItem(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                GameItem item = CommonData.player.removeItemInBank(dis.readInt());
                if (item != null) {
                    item.count = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getRemoveItem(byte[] bytes) {
        String ret = "";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            dis.readByte();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                GameItem item = CommonData.player.removeItem(dis.readInt());
                if (item != null) {
                    ret = String.valueOf(ret) + "失去 " + item.name + "\n";
                    item.count = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void computeSize() {
        if (this.lineHeight < 20) {
            this.lineHeight = 20;
        }
        this.line = 0;
        if (this.moneyChg != 0) {
            this.line++;
        }
        this.line += this.expChgMap.size();
        if (this.itemChgMap.size() > 0) {
            this.line += ((this.itemChgMap.size() - 1) / 3) + 1;
        }
        this.width = (Utilities.CHAR_WIDTH * 6) + 30 + 4 + Utilities.font.stringWidth("EXP + 99999");
        this.height = ((this.line + 1) * this.lineHeight) + 10;
        this.x = (World.viewWidth - this.width) / 2;
        this.y = NewStage.screenY + 20;
    }

    public void addMoneyChg(int money) {
        this.moneyChg += money;
        computeSize();
        this.timer = -1;
    }

    public void addExpChg(Pet pet, int expChg) {
        if (expChg != 0) {
            int v = 0;
            if (this.expChgMap.get(pet) != null) {
                v = 0 + ((Integer) this.expChgMap.get(pet)).intValue();
            }
            int v2 = v + expChg;
            this.expChgMap.put(pet, new Integer(expChg));
            computeSize();
            this.timer = -1;
        }
    }

    public void addItemChg(GameItem item, int count) {
        int v = 0;
        if (this.itemChgMap.get(item) != null) {
            v = 0 + ((Integer) this.itemChgMap.get(item)).intValue();
        }
        this.itemChgMap.put(item, new Integer(v + count));
        computeSize();
        this.timer = -1;
    }

    public void paint(Graphics g) {
    }
}
