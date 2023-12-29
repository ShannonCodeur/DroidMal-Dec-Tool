package com.sumsharp.monster.common.data;

import java.io.DataInputStream;
import java.io.IOException;

public class Buff {
    public static final byte PLAYER_BUFF_EXP = 0;
    public static final byte PLAYER_BUFF_HPUFFER = 1;
    public static final byte PLAYER_BUFF_MPUFFER = 2;
    public long checkTime = System.currentTimeMillis();
    public String name;
    public long remainTime;
    public byte type;

    public String getRemainTimeStr() {
        if (this.type == 1) {
            return String.valueOf(this.remainTime);
        }
        int t = ((int) ((this.remainTime / 1000) / 60)) + 1;
        int hour = t / 60;
        String m = String.valueOf(t % 60);
        if (m.length() < 2) {
            m = "0" + m;
        }
        return String.valueOf(hour) + ":" + m;
    }

    public void decAmount(int amount) {
        this.remainTime -= (long) amount;
    }

    public void decTime() {
        if (this.type != 1) {
            this.remainTime -= System.currentTimeMillis() - this.checkTime;
            this.checkTime = System.currentTimeMillis();
        }
    }

    public void load(DataInputStream dis) {
        try {
            this.type = dis.readByte();
            this.name = dis.readUTF();
            this.remainTime = dis.readLong();
        } catch (IOException e) {
        }
    }
}
