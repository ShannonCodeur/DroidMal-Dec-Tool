package com.sumsharp.monster.common.data;

import java.io.DataInputStream;
import java.io.IOException;

public class Credit {
    public int id;
    public String levelTitle;
    public int levelUpExp;
    public String title;
    public int value;

    public void load(DataInputStream dis) {
        try {
            this.id = dis.readInt();
            this.title = dis.readUTF();
            this.levelTitle = dis.readUTF();
            this.levelUpExp = dis.readInt();
            this.value = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
