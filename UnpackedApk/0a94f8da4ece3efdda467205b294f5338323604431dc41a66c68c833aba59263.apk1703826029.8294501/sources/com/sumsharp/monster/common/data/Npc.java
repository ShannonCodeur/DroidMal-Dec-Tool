package com.sumsharp.monster.common.data;

import com.sumsharp.monster.Battle;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.PipAnimateSet;
import com.sumsharp.monster.net.UWAPSegment;
import com.sumsharp.monster.ui.IMessageDialogKeyHandler;
import java.io.DataInputStream;
import java.io.IOException;

public class Npc extends AbstractUnit implements IMessageDialogKeyHandler {
    public static boolean showConnection = false;
    public static Npc touchingNpc = null;
    public short alertRange = 8;
    public byte flag;
    public int frame;
    public boolean hide;
    public int refreshTime;
    public boolean serverRefresh;
    public byte taskState;
    public short viewRange = 16;

    public void parseFlag() {
        boolean z;
        boolean z2;
        boolean z3;
        if ((this.flag & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.serverRefresh = z;
        if ((this.flag & 128) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.visible = z2;
        if ((this.flag & 2) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.hide = z3;
        this.frame = 0;
    }

    /* access modifiers changed from: protected */
    public void cycleAttack() {
        int ox = CommonData.player.getOriginX();
        int oy = CommonData.player.getOriginY();
        int dx = ox - (this.pixelX + (getWidth() / 2));
        int dy = oy - this.pixelY;
        int dest = (dx * dx) + (dy * dy);
        moveTo(ox, oy);
        if (dest <= this.alertRange * this.alertRange) {
            Battle.startBattle(this);
            setState(4);
            this.speed = 2;
        }
        if (CommonData.player.runawayTime != 0 || Battle.battleID != -1) {
            setState(4);
            this.speed = 2;
        }
    }

    public void cycle() {
        if (isFriend(CommonData.player)) {
            super.cycle();
            return;
        }
        if (this.emotePlayer != null) {
            clearEmote();
        }
        int ox = CommonData.player.getOriginX();
        int oy = CommonData.player.getOriginY();
        int dx = ox - this.pixelX;
        int dy = oy - this.pixelY;
        int dest = (dx * dx) + (dy * dy);
        if (World.GOD) {
            return;
        }
        if ((getState() != 0 && getState() != 2) || getState() == 4 || this.viewRange <= 0 || dest >= this.viewRange * this.viewRange || CommonData.player.runawayTime != 0) {
            return;
        }
        if (CommonData.player.teamState != 1 || CommonData.team.id == -1) {
            savePosition();
            setState(5);
            this.speed = 5;
        }
    }

    public CartoonPlayer createCartoonPlayer() {
        PipAnimateSet ani = new PipAnimateSet(World.findResource("/npc.ani", false));
        ani.toFullBuffer();
        this.cartoonPlayer = CartoonPlayer.playCartoon(ani, getFaceto(), this.pixelX, this.pixelY, true);
        return this.cartoonPlayer;
    }

    public void load(DataInputStream dis) throws IOException {
        this.id = dis.readInt();
        this.name = dis.readUTF();
        setIconID(dis.readShort());
        this.tileX = dis.readByte();
        this.tileY = dis.readByte();
        this.pixelX = (short) (this.tileX * 16);
        this.pixelY = (short) ((this.tileY + 1) * 16);
        setDir(dis.readByte());
        this.camp = dis.readByte();
        this.showLevel = dis.readInt();
        byte count = dis.readByte();
        for (int i = 0; i < count; i++) {
            addWayPoint(dis.readByte(), dis.readByte());
        }
        if (this.wpList != null) {
            if (((Integer) this.wpList.elementAt(0)).intValue() == ((Integer) this.wpList.elementAt(this.wpList.size() - 1)).intValue()) {
                this.wpType = 0;
            } else {
                this.wpType = 1;
            }
            this.wpPointer = 1;
            this.wpDir = 1;
        }
        if (!(this.camp == 0 && this.wpList == null)) {
            PipAnimateSet ani = new PipAnimateSet(World.findResource("/da.ani", false));
            ani.toFullBuffer();
            this.cartoonPlayer = CartoonPlayer.playCartoon(ani, getFaceto(), this.pixelX, this.pixelY, true);
        }
        this.visible = true;
        this.npcType = dis.readByte();
        int idx = this.name.indexOf("&");
        if (idx >= 0) {
            this.guild = this.name.substring(idx + 1);
            this.name = this.name.substring(0, idx);
        }
        this.speed = 2;
        this.taskState = -1;
    }

    public void keyPressed(int key) {
        if (key == 4) {
            try {
                if (touchingNpc == null) {
                    UWAPSegment segment = new UWAPSegment(17, 14);
                    segment.writeInt(this.id);
                    segment.writeByte(0);
                    segment.writeInt(0);
                    segment.flush();
                    Utilities.sendRequest(segment);
                    touchingNpc = this;
                    showConnection = true;
                    CommonData.player.clearAutoRun();
                    Utilities.clearKeyStates();
                }
            } catch (IOException e) {
            }
        }
    }
}
