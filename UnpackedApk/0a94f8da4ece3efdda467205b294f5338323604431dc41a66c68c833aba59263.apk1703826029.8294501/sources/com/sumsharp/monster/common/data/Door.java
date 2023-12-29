package com.sumsharp.monster.common.data;

import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Door {
    private static int[] doorFrameSequence;
    public int id;
    public String mapName;
    public boolean needTip;
    public short targetMap;
    public short targetX;
    public short targetY;
    public String tipStr;
    public short x;
    public short y;

    static {
        int[] iArr = new int[8];
        iArr[2] = 1;
        iArr[3] = 1;
        iArr[4] = 2;
        iArr[5] = 2;
        iArr[6] = 1;
        iArr[7] = 1;
        doorFrameSequence = iArr;
    }

    /* Debug info: failed to restart local var, previous not found, register: 20 */
    public int[] getCoverTiles(int pixelX, int pixelY) {
        Vector v = new Vector();
        int rw = getWidth() + (pixelX % 16);
        int rh = (getHeight() + 16) - (pixelY % 16);
        int tileX = pixelX / 16;
        int tileY = pixelY / 16;
        int cx = rw / 16;
        int cy = rh / 16;
        if (rw % 16 != 0) {
            cx++;
        }
        if (rh % 16 != 0) {
            cy++;
        }
        for (int y2 = cy - 1; y2 >= 0; y2--) {
            for (int x2 = cx - 1; x2 >= 0; x2--) {
                short tileIdx = (short) (((tileY - y2) * NewStage.mapWidth) + x2 + tileX);
                if (tileIdx >= 0 && tileIdx < NewStage.mapData.length) {
                    short[] tileInfo = NewStage.mapData[tileIdx];
                    for (int layer = tileInfo.length - 1; layer >= 0; layer--) {
                        if ((tileIdx / NewStage.mapWidth) + NewStage.getTileHeight(tileInfo[layer]) > tileY) {
                            v.addElement(new Integer((layer << 16) | tileIdx));
                        }
                    }
                }
            }
        }
        int[] ret = new int[v.size()];
        for (int i = 0; i < v.size(); i++) {
            ret[i] = ((Integer) v.elementAt(i)).intValue();
        }
        return ret;
    }

    public int[] getRedrawTiles() {
        Vector v = new Vector();
        int[] t1 = getCoverTiles(((this.x * 16) + 8) - (getWidth() / 2), (this.y * 16) + 16);
        for (int i = 0; i < t1.length; i++) {
            if (t1[i] >= 0) {
                v.addElement(new Integer(t1[i]));
            }
        }
        return t1;
    }

    public void load(DataInputStream dis) {
        try {
            this.id = dis.readInt();
            this.x = dis.readShort();
            this.y = dis.readShort();
            this.targetMap = dis.readShort();
            this.mapName = dis.readUTF();
            this.targetX = dis.readShort();
            this.targetY = dis.readShort();
            this.tipStr = dis.readUTF();
            this.needTip = !this.tipStr.equals("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return Tool.doorImage.getFrameWidth((World.tick / 2) % 5);
    }

    public int getHeight() {
        return Tool.doorImage.getFrameHeight((World.tick / 2) % 5);
    }

    public void drawName(Graphics g) {
        int px = CommonData.player.pixelX + (CommonData.player.getWidth() / 2);
        int py = CommonData.player.pixelY;
        int doorx = (this.x * 16) + 8;
        int doory = (this.y * 16) + 16;
        if (((px - doorx) * (px - doorx)) + ((py - doory) * (py - doory)) < 6400) {
            g.setFont(Utilities.font);
            int nx = doorx - NewStage.viewX;
            int ny = doory - NewStage.viewY;
            int nx2 = nx - (Utilities.font.stringWidth(this.mapName) / 2);
            if (nx2 <= 1) {
                nx2 = 1;
            }
            Tool.draw3DString(g, this.mapName, nx2, (ny - Utilities.CHAR_HEIGHT) - Tool.doorImage.getFrameHeight(0), 16776960, 0);
        }
    }

    public void draw(Graphics g) {
        int dx = ((this.x * 16) - NewStage.viewX) + 8;
        Tool.doorImage.drawFrame(g, doorFrameSequence[World.tick % doorFrameSequence.length], dx, ((this.y * 16) - NewStage.getMapDrawY()) + 16, 0, 33);
    }
}
