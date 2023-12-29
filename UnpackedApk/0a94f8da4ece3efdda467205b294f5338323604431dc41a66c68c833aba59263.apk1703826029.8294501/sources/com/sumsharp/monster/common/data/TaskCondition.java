package com.sumsharp.monster.common.data;

import com.sumsharp.monster.item.GameItem;
import java.io.DataInputStream;
import java.io.IOException;

public class TaskCondition {
    public static final byte CONDITION_TYPE_AUTO_TIME = 7;
    public static final byte CONDITION_TYPE_CATCHMONSTER = 3;
    public static final byte CONDITION_TYPE_CHOOSEPET = 5;
    public static final byte CONDITION_TYPE_FINDNPC = 0;
    public static final byte CONDITION_TYPE_GATHERITEM = 6;
    public static final byte CONDITION_TYPE_KILLMONSTER = 1;
    public static final byte CONDITION_TYPE_NPCFIGHT = 4;
    public static final byte CONDITION_TYPE_SENDITEM = 2;
    public static final byte TASK_STATE_ASSIGN = 2;
    public static final byte TASK_STATE_DOING = 1;
    public static final byte TASK_STATE_FINISH = 3;
    public static final byte TASK_STATE_LEVEL = 0;
    private int baseTime;
    public byte finished;
    public int haveCount;
    public int id;
    public int needCount;
    public GameItem needItem;
    public String npcMapName;
    public short npcx;
    public short npcy;
    public String prefix;
    public String title;
    public byte type;

    public String getDesc() {
        switch (this.type) {
            case 0:
                return String.valueOf(this.prefix) + "[" + this.npcMapName + "]的[" + this.title + "]";
            case 1:
                String ret = "杀死 " + this.title + " [" + this.haveCount + "/" + this.needCount + "]";
                if (this.finished == 3) {
                    return String.valueOf(ret) + "(完成)";
                }
                return ret;
            case 2:
            case 6:
                String ret2 = String.valueOf(this.needItem.name) + " [" + this.haveCount + "/" + this.needCount + "]";
                if (this.finished == 3) {
                    return String.valueOf(ret2) + "(完成)";
                }
                return ret2;
            case 3:
                String ret3 = "捕捉 " + this.title + " [" + this.haveCount + "/" + this.needCount + "]";
                if (this.finished == 3) {
                    return String.valueOf(ret3) + "(完成)";
                }
                return ret3;
            case 4:
                String ret4 = String.valueOf(this.prefix) + " " + this.title;
                if (this.finished == 3) {
                    return String.valueOf(ret4) + "(完成)";
                }
                return ret4;
            case 5:
                return this.title;
            case 7:
                if (getTimeString().equals("")) {
                    return "完成";
                }
                return String.valueOf(getTimeString()) + " 后完成";
            default:
                return "";
        }
    }

    private String getTimeString() {
        int time = this.haveCount - getPassTime();
        if (time < 0) {
            time = 0;
        }
        int s = time % 60;
        int m = (time / 60) % 60;
        String t = "";
        if (time / 3600 > 0) {
            t = String.valueOf(t) + h + "小时";
        }
        if (m > 0) {
            t = String.valueOf(t) + m + "分";
        }
        if (s > 0) {
            return String.valueOf(t) + s + "秒";
        }
        return t;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] toByte() {
        /*
            r4 = this;
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            java.io.DataOutputStream r1 = new java.io.DataOutputStream
            r1.<init>(r0)
            int r2 = r4.id     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            byte r2 = r4.type     // Catch:{ IOException -> 0x003d }
            r1.writeByte(r2)     // Catch:{ IOException -> 0x003d }
            byte r2 = r4.finished     // Catch:{ IOException -> 0x003d }
            r1.writeByte(r2)     // Catch:{ IOException -> 0x003d }
            byte r2 = r4.type     // Catch:{ IOException -> 0x003d }
            switch(r2) {
                case 0: goto L_0x0023;
                case 1: goto L_0x003f;
                case 2: goto L_0x004f;
                case 3: goto L_0x006f;
                case 4: goto L_0x007f;
                case 5: goto L_0x008a;
                case 6: goto L_0x004f;
                case 7: goto L_0x0090;
                default: goto L_0x001e;
            }
        L_0x001e:
            byte[] r2 = r0.toByteArray()
            return r2
        L_0x0023:
            java.lang.String r2 = r4.prefix     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            java.lang.String r2 = r4.npcMapName     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            java.lang.String r2 = r4.title     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            short r2 = r4.npcx     // Catch:{ IOException -> 0x003d }
            r1.writeShort(r2)     // Catch:{ IOException -> 0x003d }
            short r2 = r4.npcy     // Catch:{ IOException -> 0x003d }
            r1.writeShort(r2)     // Catch:{ IOException -> 0x003d }
            goto L_0x001e
        L_0x003d:
            r2 = move-exception
            goto L_0x001e
        L_0x003f:
            java.lang.String r2 = r4.title     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            int r2 = r4.haveCount     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            int r2 = r4.needCount     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            goto L_0x001e
        L_0x004f:
            com.sumsharp.monster.item.GameItem r2 = r4.needItem     // Catch:{ IOException -> 0x003d }
            int r2 = r2.id     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            com.sumsharp.monster.item.GameItem r2 = r4.needItem     // Catch:{ IOException -> 0x003d }
            int r2 = r2.iconId     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            com.sumsharp.monster.item.GameItem r2 = r4.needItem     // Catch:{ IOException -> 0x003d }
            java.lang.String r2 = r2.name     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            int r2 = r4.haveCount     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            int r2 = r4.needCount     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            goto L_0x001e
        L_0x006f:
            java.lang.String r2 = r4.title     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            int r2 = r4.haveCount     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            int r2 = r4.needCount     // Catch:{ IOException -> 0x003d }
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            goto L_0x001e
        L_0x007f:
            java.lang.String r2 = r4.prefix     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            java.lang.String r2 = r4.title     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            goto L_0x001e
        L_0x008a:
            java.lang.String r2 = r4.title     // Catch:{ IOException -> 0x003d }
            r1.writeUTF(r2)     // Catch:{ IOException -> 0x003d }
            goto L_0x001e
        L_0x0090:
            int r2 = r4.haveCount     // Catch:{ IOException -> 0x003d }
            int r3 = r4.getPassTime()     // Catch:{ IOException -> 0x003d }
            int r2 = r2 - r3
            r1.writeInt(r2)     // Catch:{ IOException -> 0x003d }
            goto L_0x001e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.common.data.TaskCondition.toByte():byte[]");
    }

    private int getPassTime() {
        if (this.type != 7) {
            return -1;
        }
        return (int) ((System.currentTimeMillis() / 1000) - ((long) this.baseTime));
    }

    public boolean checkTime() {
        return this.haveCount - getPassTime() <= 0;
    }

    public void load(DataInputStream dis) {
        try {
            this.id = dis.readInt();
            this.type = dis.readByte();
            this.finished = dis.readByte();
            switch (this.type) {
                case 0:
                    this.prefix = dis.readUTF();
                    this.npcMapName = dis.readUTF();
                    this.title = dis.readUTF();
                    this.npcx = dis.readShort();
                    this.npcy = dis.readShort();
                    return;
                case 1:
                    this.title = dis.readUTF();
                    this.haveCount = dis.readInt();
                    this.needCount = dis.readInt();
                    return;
                case 2:
                case 6:
                    int itemid = dis.readInt();
                    int iconid = dis.readInt();
                    String title2 = dis.readUTF();
                    this.needItem = new GameItem();
                    this.needItem.id = itemid;
                    this.needItem.name = title2;
                    this.needItem.iconId = iconid;
                    this.haveCount = dis.readInt();
                    this.needCount = dis.readInt();
                    return;
                case 3:
                    this.title = dis.readUTF();
                    this.haveCount = dis.readInt();
                    this.needCount = dis.readInt();
                    return;
                case 4:
                    this.prefix = dis.readUTF();
                    this.title = dis.readUTF();
                    return;
                case 5:
                    this.title = dis.readUTF();
                    return;
                case 7:
                    this.haveCount = dis.readInt();
                    this.baseTime = (int) (System.currentTimeMillis() / 1000);
                    return;
                default:
                    return;
            }
        } catch (IOException e) {
        }
    }

    public boolean isFinished() {
        if (this.type == 7 && checkTime()) {
            this.finished = 3;
        }
        return this.finished == 3;
    }
}
