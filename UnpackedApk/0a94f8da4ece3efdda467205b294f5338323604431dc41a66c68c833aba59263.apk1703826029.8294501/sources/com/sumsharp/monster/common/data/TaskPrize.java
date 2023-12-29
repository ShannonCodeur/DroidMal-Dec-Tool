package com.sumsharp.monster.common.data;

import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.item.GameItem;
import java.io.DataInputStream;
import java.io.IOException;

public class TaskPrize {
    public static final String[] PRIZE_TITLE = {"", "", "金币", "", "物品", "声望", "荣誉", "称号", "背包格", "银行包格", "", "", "公会资源-木材", "公会资源-矿石", "公会资金", "公会贡献点数"};
    public static final byte PRIZE_TYPE_BAGGRID = 8;
    public static final byte PRIZE_TYPE_BANKGRID = 9;
    public static final byte PRIZE_TYPE_CREDIT = 5;
    public static final byte PRIZE_TYPE_EXPBUFF = 11;
    public static final byte PRIZE_TYPE_EXPRESSION = 10;
    public static final byte PRIZE_TYPE_GUILDRESOURCE_MONEY = 14;
    public static final byte PRIZE_TYPE_GUILDRESOURCE_ORE = 13;
    public static final byte PRIZE_TYPE_GUILDRESOURCE_WOOD = 12;
    public static final byte PRIZE_TYPE_GUILD_CONTRIBUTIONPOINT = 15;
    public static final byte PRIZE_TYPE_HONNER = 6;
    public static final byte PRIZE_TYPE_ITEM = 4;
    public static final byte PRIZE_TYPE_MONEY = 2;
    public static final byte PRIZE_TYPE_TITLE = 7;
    public int count;
    public GameItem prizeItem;
    public String title;
    public byte type;

    public String getDesc() {
        switch (this.type) {
            case 4:
                return String.valueOf("") + this.prizeItem.name + " x " + this.count;
            case 5:
                return String.valueOf("") + "声望 " + this.title + " " + this.count + "点";
            case 6:
                return String.valueOf("") + "荣誉 " + this.count + " 点";
            case 7:
                return String.valueOf("") + "称号：???";
            case 8:
                return String.valueOf("") + "背包格 " + this.count + " 格";
            case 9:
                return String.valueOf("") + "银行包格 " + this.count + " 格";
            case 12:
                return String.valueOf("") + "公会资源-木材 " + this.count;
            case Tool.IMAGE_FONT_WIDTH /*13*/:
                return String.valueOf("") + "公会资源-矿石 " + this.count;
            case 14:
                return String.valueOf("") + "公会资金 " + this.count;
            case Tool.EDGE_ROUND_ALL /*15*/:
                return String.valueOf("") + "公会贡献点数 " + this.count + "点";
            default:
                return "";
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] toByte() {
        /*
            r3 = this;
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            java.io.DataOutputStream r1 = new java.io.DataOutputStream
            r1.<init>(r0)
            byte r2 = r3.type     // Catch:{ IOException -> 0x001f }
            r1.writeByte(r2)     // Catch:{ IOException -> 0x001f }
            byte r2 = r3.type     // Catch:{ IOException -> 0x001f }
            switch(r2) {
                case 2: goto L_0x0019;
                case 3: goto L_0x0014;
                case 4: goto L_0x0021;
                case 5: goto L_0x003c;
                case 6: goto L_0x0047;
                case 7: goto L_0x004d;
                case 8: goto L_0x0053;
                case 9: goto L_0x0059;
                case 10: goto L_0x0014;
                case 11: goto L_0x0014;
                case 12: goto L_0x0019;
                case 13: goto L_0x0019;
                case 14: goto L_0x0019;
                case 15: goto L_0x0019;
                default: goto L_0x0014;
            }
        L_0x0014:
            byte[] r2 = r0.toByteArray()
            return r2
        L_0x0019:
            int r2 = r3.count     // Catch:{ IOException -> 0x001f }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x001f }
            goto L_0x0014
        L_0x001f:
            r2 = move-exception
            goto L_0x0014
        L_0x0021:
            com.sumsharp.monster.item.GameItem r2 = r3.prizeItem     // Catch:{ IOException -> 0x001f }
            int r2 = r2.id     // Catch:{ IOException -> 0x001f }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x001f }
            com.sumsharp.monster.item.GameItem r2 = r3.prizeItem     // Catch:{ IOException -> 0x001f }
            int r2 = r2.iconId     // Catch:{ IOException -> 0x001f }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x001f }
            com.sumsharp.monster.item.GameItem r2 = r3.prizeItem     // Catch:{ IOException -> 0x001f }
            java.lang.String r2 = r2.name     // Catch:{ IOException -> 0x001f }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x001f }
            int r2 = r3.count     // Catch:{ IOException -> 0x001f }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x001f }
            goto L_0x0014
        L_0x003c:
            java.lang.String r2 = r3.title     // Catch:{ IOException -> 0x001f }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x001f }
            int r2 = r3.count     // Catch:{ IOException -> 0x001f }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x001f }
            goto L_0x0014
        L_0x0047:
            int r2 = r3.count     // Catch:{ IOException -> 0x001f }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x001f }
            goto L_0x0014
        L_0x004d:
            java.lang.String r2 = r3.title     // Catch:{ IOException -> 0x001f }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x001f }
            goto L_0x0014
        L_0x0053:
            int r2 = r3.count     // Catch:{ IOException -> 0x001f }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x001f }
            goto L_0x0014
        L_0x0059:
            int r2 = r3.count     // Catch:{ IOException -> 0x001f }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x001f }
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.common.data.TaskPrize.toByte():byte[]");
    }

    public void load(DataInputStream dis) {
        try {
            this.type = dis.readByte();
            switch (this.type) {
                case 2:
                case 12:
                case Tool.IMAGE_FONT_WIDTH /*13*/:
                case 14:
                case Tool.EDGE_ROUND_ALL /*15*/:
                    this.count = dis.readInt();
                    return;
                case 4:
                    this.prizeItem = new GameItem();
                    this.prizeItem.id = dis.readInt();
                    this.prizeItem.iconId = dis.readInt();
                    this.prizeItem.name = dis.readUTF();
                    this.count = dis.readInt();
                    return;
                case 5:
                    this.title = dis.readUTF();
                    this.count = dis.readInt();
                    return;
                case 6:
                    this.count = dis.readInt();
                    return;
                case 7:
                    this.title = dis.readUTF();
                    return;
                case 8:
                    this.count = dis.readInt();
                    return;
                case 9:
                    this.count = dis.readInt();
                    return;
                default:
                    return;
            }
        } catch (IOException e) {
        }
    }
}
