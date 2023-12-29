package com.sumsharp.monster.common.data;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.TextField;

public class MateInfo {
    public boolean canMate;
    public byte childRace;
    public String childTitle;
    public boolean hasLess;
    public boolean hasMain;
    public int id;
    public int lessId;
    public String lessTitle;
    public int lessType;
    public int mainId;
    public String mainTitle;
    public int mainType;

    public void load(DataInputStream dis) {
        try {
            this.id = dis.readInt();
            this.mainType = this.id >> 30;
            this.mainId = (this.id >> 16) & 16383;
            this.lessType = (this.id & TextField.CONSTRAINT_MASK) >> 14;
            this.lessId = this.id & 16383;
            this.mainTitle = dis.readUTF();
            this.lessTitle = dis.readUTF();
            this.childRace = dis.readByte();
            this.childTitle = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return "合成：" + this.childTitle;
    }

    public String getMissingString() {
        String ret = "<cff0000>合成条件不满足，缺少 ";
        if (!this.hasMain) {
            ret = String.valueOf(ret) + this.mainTitle + " ";
            if (!this.hasLess) {
                ret = String.valueOf(ret) + "和 ";
            }
        }
        if (!this.hasLess) {
            ret = String.valueOf(ret) + this.lessTitle + " ";
        }
        return String.valueOf(ret) + "</c>";
    }
}
