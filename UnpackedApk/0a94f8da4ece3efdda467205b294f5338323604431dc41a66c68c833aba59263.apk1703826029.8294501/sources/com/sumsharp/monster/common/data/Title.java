package com.sumsharp.monster.common.data;

import java.io.DataInputStream;
import java.io.IOException;

public class Title {
    public int color;
    public int id;
    public String title;

    public void load(DataInputStream dis) {
        try {
            this.id = dis.readInt();
            this.color = dis.readInt();
            this.title = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
