package com.sumsharp.monster.common.data;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.PipAnimateSet;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

public class Monster extends AbstractUnit {
    public static final int NETPLAYER_WAYPOINT_SIZE_MAX = 3;
    public short alertRange;
    public byte flag;
    public boolean hide;
    public byte[] moveRange;
    public short refreshTime;
    public boolean serverRefresh;
    public short viewRange;

    public void load(DataInputStream dis) throws IOException {
        this.speed = 2;
        setDir((byte) Tool.getNextRnd(0, 1));
        this.id = dis.readInt();
        setIconID(dis.readShort());
        this.flag = dis.readByte();
        this.name = dis.readUTF();
        byte readByte = dis.readByte();
        this.serverRefresh = (this.flag & 1) != 0;
        this.hide = (this.flag & 2) != 0;
        if (this.serverRefresh) {
            this.visible = true;
        } else {
            this.visible = (this.flag & 128) != 0;
        }
        this.refreshTime = dis.readShort();
        if (this.refreshTime == 0) {
            this.refreshTime = 240;
        }
        this.tileX = dis.readByte();
        this.tileY = dis.readByte();
        this.pixelX = (short) (this.tileX * 16);
        this.pixelY = (short) ((this.tileY + 1) * 16);
        this.backX = this.pixelX;
        this.backY = this.pixelY;
        parseRange(dis.readByte());
        this.moveRange = new byte[4];
        dis.read(this.moveRange);
        int len = dis.readByte();
        if (len != 0) {
            this.wpList = new Vector();
        }
        for (int i = 0; i < len; i++) {
            int dest = dis.readShort();
            addWayPoint((byte) (dest >> 8), (byte) (dest & AbstractUnit.CLR_NAME_TAR));
        }
        if (len == 1) {
            this.wpList = null;
        }
        if (this.wpList != null) {
            if (((Integer) this.wpList.elementAt(0)).intValue() == ((Integer) this.wpList.elementAt(this.wpList.size() - 1)).intValue()) {
                this.wpType = 0;
            } else {
                this.wpType = 1;
            }
            this.wpPointer = 1;
            this.wpDir = 1;
            setState(2);
        }
        int size = dis.readByte();
        for (int i2 = 0; i2 < size; i2++) {
            dis.readByte();
            dis.readShort();
        }
    }

    public void parseRange(byte value) {
        this.viewRange = (byte) (value >> 3);
        this.alertRange = (byte) (value & 7);
        this.viewRange = (short) ((this.viewRange * 16) / 2);
        this.alertRange = (short) ((this.alertRange * 16) / 2);
    }

    public CartoonPlayer createCartoonPlayer() {
        this.cartoonPlayer = CartoonPlayer.playCartoon(new PipAnimateSet(World.findResource("/da.ani", false)), getFaceto(), this.pixelX, this.pixelY, true);
        return this.cartoonPlayer;
    }

    public void cycleAttack() {
        int ox = CommonData.player.getOriginX();
        int oy = CommonData.player.getOriginY();
        int dx = ox - this.pixelX;
        int dy = oy - this.pixelY;
        int dest = (dx * dx) + (dy * dy);
        if (inMoveRange(ox, oy)) {
            moveTo(ox, oy);
            if (dest <= this.alertRange * this.alertRange) {
                setState(4);
                this.speed = 2;
                return;
            }
            return;
        }
        setState(4);
        this.speed = 2;
    }

    public void cycle() {
        int ox = CommonData.player.getOriginX();
        int oy = CommonData.player.getOriginY();
        int dx = ox - this.pixelX;
        int dy = oy - this.pixelY;
        int dest = (dx * dx) + (dy * dy);
        if ((getState() == 0 || getState() == 2) && getState() != 4 && inMoveRange(ox, oy) && this.viewRange > 0 && dest < this.viewRange * this.viewRange) {
            setState(5);
            this.speed = 5;
        }
    }

    public boolean inMoveRange(int dx, int dy) {
        int tx = dx / 16;
        int ty = dy / 16;
        if (this.moveRange[0] == 0 && this.moveRange[1] == 0 && this.moveRange[2] == 0 && this.moveRange[3] == 0) {
            return true;
        }
        if (tx < this.moveRange[0] || tx >= this.moveRange[0] + this.moveRange[2] || ty < this.moveRange[1] || ty >= this.moveRange[1] + this.moveRange[3]) {
            return false;
        }
        return true;
    }
}
