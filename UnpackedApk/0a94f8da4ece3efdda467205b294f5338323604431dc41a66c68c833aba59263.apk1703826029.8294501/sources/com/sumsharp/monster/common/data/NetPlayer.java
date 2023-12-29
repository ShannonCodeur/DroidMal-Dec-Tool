package com.sumsharp.monster.common.data;

import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.PipImage;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.IOException;

public class NetPlayer extends AbstractUnit {
    public static final String ACTIONSTATE_ID = "actionstate";
    public static final byte NETPLAYER_WAYPOINT_SIZE_MAX = 5;
    public int arenaLose = -1;
    public int arenaWin = -1;
    public Pet battlePet = new Pet();
    public int battlePetHp;
    public int battlePetHpMax;
    public int battlePetLevel;
    public int battlePetMp;
    public int battlePetMpMax;
    public int honorRank = 0;
    public String honorTitle;
    public int level;
    public short playerFlag;
    public int rank;
    public byte sex;
    public byte teamRole;

    public NetPlayer() {
        this.battlePet.iconID = -1;
    }

    public PipImage getImage() {
        return null;
    }

    public CartoonPlayer createCartoonPlayer() {
        this.cartoonPlayer = CartoonPlayer.playCartoon(Tool.female, getFaceto(), this.pixelX, this.pixelY, true);
        return this.cartoonPlayer;
    }

    public void update(UWAPSegment segment) throws IOException {
        this.rank = segment.readByte();
        this.playerFlag = segment.readShort();
        short newMapId = segment.readShort();
        int newMirrorId = segment.readInt();
        short x = segment.readShort();
        short y = segment.readShort();
        if (newMapId == this.mapId && newMirrorId == this.mirrorId) {
            addWayPoint(x, y);
            if (this.wpList.size() > 5) {
                this.wpList.removeElementAt(0);
            }
        } else {
            this.mapId = newMapId;
            this.mirrorId = newMirrorId;
            this.wpList = null;
            this.pixelX = x;
            this.pixelY = y;
            resetFollowPetPosition();
            go(0, 0);
        }
        byte newState = segment.readByte();
        int readInt = segment.readInt();
        this.level = readInt;
        this.showLevel = readInt;
        this.camp = segment.readByte();
        this.battlePetHp = segment.readInt();
        this.battlePetMp = segment.readInt();
        this.battlePetLevel = segment.readInt();
        this.battlePetHpMax = segment.readInt();
        this.battlePetMpMax = segment.readInt();
        updateState(newState);
    }

    public void load(UWAPSegment segment) throws IOException {
        this.visible = true;
        this.imageUpdate = true;
        this.wpType = 2;
        this.rank = segment.readByte();
        this.playerFlag = segment.readShort();
        short newMapId = segment.readShort();
        int newMirrorId = segment.readInt();
        short x = segment.readShort();
        short y = segment.readShort();
        if (this.mapId == newMapId && this.mirrorId == newMirrorId) {
            addWayPoint(x, y);
        } else {
            resetFollowPetPosition();
            this.mapId = newMapId;
            this.pixelX = x;
            this.pixelY = y;
        }
        this.mirrorId = newMirrorId;
        updateState(segment.readByte());
        int readInt = segment.readInt();
        this.level = readInt;
        this.showLevel = readInt;
        this.camp = segment.readByte();
        this.name = segment.readString();
        this.sex = segment.readByte();
        this.teamRole = segment.readByte();
        this.title = segment.readString();
        this.color = segment.readInt();
        if (this.sex == 1) {
            this.cartoonPlayer = CartoonPlayer.playCartoon(Tool.male, getFaceto(), this.pixelX, this.pixelY, true);
        }
        int petIconId = segment.readInt();
        String petName = segment.readString();
        int petLevel = segment.readShort();
        byte matingTimes = segment.readByte();
        if (petIconId == -1) {
            setFollowPet(null);
        } else {
            Pet pet = new Pet();
            pet.setIconID(petIconId);
            pet.setAttribute(50, petLevel);
            pet.name = petName;
            pet.matingTimes = matingTimes;
            setFollowPet(pet);
        }
        this.guild = segment.readString();
        this.battlePetHp = segment.readInt();
        this.battlePetMp = segment.readInt();
        this.battlePetLevel = segment.readInt();
        this.battlePetHpMax = segment.readInt();
        this.battlePetMpMax = segment.readInt();
        go(0, 0);
    }

    private void updateState(byte newState) {
        if (newState != this.actionState) {
            this.actionState = newState;
            clearEmote(ACTIONSTATE_ID);
            if (newState != 0) {
                byte ani = getActionAniFrame(this.actionState);
                if (ani != -1) {
                    createEmote(ACTIONSTATE_ID, ani, true);
                }
            }
        }
    }
}
