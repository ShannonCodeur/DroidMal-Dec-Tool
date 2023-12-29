package com.sumsharp.lowui;

import com.sumsharp.monster.Battle;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.GlobalVar;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Npc;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.PipAnimateSet;
import com.sumsharp.monster.net.UWAPSegment;
import javax.microedition.lcdui.Graphics;

public class ChallengeArena {
    private static String[] INTRO = {"欢迎 %p 参加挑战竞技场的 %a ", "%a 第%r轮 比赛现在开始！由 %p 对 %n", " %w 在第%r轮比赛中战胜了 %l，晋级下一轮！"};
    public static final byte STATUS_BATTLE = 3;
    public static final byte STATUS_ENTER = 2;
    public static final byte STATUS_INTRO = 1;
    public static final byte STATUS_WAITING = 0;
    public static final byte STATUS_WIN = 4;
    private static short[] inPositionX = new short[6];
    private static short[] inPositionY = new short[6];
    private String arenaName;
    private boolean init = false;
    private byte introIdx = 0;
    private int introTimer = 0;
    private String lastName;
    private int moveCount = 0;
    private int moveMax = 40;
    private Npc npc;
    private int[] petIcon;
    private String[] petName;
    private Pet[] pets;
    private Npc player;
    private Pet[] playerPets;
    private byte round;
    private byte status = 1;
    private int viewX;

    static {
        int centerx = World.viewWidth / 2;
        int bottomy = World.viewHeight - 90;
        int hgap = World.viewWidth / 2;
        inPositionX[1] = (short) ((hgap / 2) + centerx);
        inPositionX[0] = (short) (inPositionX[1] + 10);
        inPositionX[2] = (short) (inPositionX[1] - 10);
        inPositionX[4] = (short) (centerx - (hgap / 2));
        inPositionX[3] = (short) (inPositionX[4] - 10);
        inPositionX[5] = (short) (inPositionX[4] + 10);
        inPositionY[0] = (short) bottomy;
        inPositionY[1] = (short) (inPositionY[0] - 35);
        inPositionY[2] = (short) (inPositionY[1] - 35);
        inPositionY[3] = inPositionY[0];
        inPositionY[4] = inPositionY[1];
        inPositionY[5] = inPositionY[2];
    }

    public static String getWelcomeStr(String playerName, String arenaName2) {
        return Utilities.replaceString("%a", arenaName2, Utilities.replaceString("%p", playerName, INTRO[0]));
    }

    public static String getRoundStr(String playerName, String arenaName2, int round2, String npcName) {
        return Utilities.replaceString("%n", npcName, Utilities.replaceString("%r", String.valueOf(round2), Utilities.replaceString("%a", arenaName2, Utilities.replaceString("%p", playerName, INTRO[1]))));
    }

    public static String getResultStr(String winnerName, String loserName, int round2) {
        return Utilities.replaceString("%r", String.valueOf(round2), Utilities.replaceString("%l", loserName, Utilities.replaceString("%w", winnerName, INTRO[2])));
    }

    public static void addSysMsg(String str) {
        Battle.addDialog(str);
    }

    public ChallengeArena(String arenaName2, String npcName, int npcIcon, String[] petName2, int[] petIcon2) {
        PipAnimateSet ani;
        this.arenaName = arenaName2;
        this.viewX = ((NewStage.mapWidth * 16) - World.viewWidth) >> 1;
        this.player = new Npc();
        this.player.visible = true;
        this.player.name = CommonData.player.name;
        this.player.imageUpdate = true;
        if (CommonData.player.sex == 0) {
            ani = Tool.female;
        } else {
            ani = Tool.male;
        }
        this.player.cartoonPlayer = CartoonPlayer.playCartoon(ani, this.player.getFaceto(), 0, 0, true);
        this.player.imageUpdate = false;
        this.player.pixelX = (short) ((World.viewWidth - 35) + this.viewX);
        this.player.pixelY = (short) (inPositionY[1] + NewStage.getMapDrawY());
        NewStage.scriptMoveMap = true;
        this.player.setState(0);
        this.round = 1;
        update(npcName, npcIcon, petName2, petIcon2);
    }

    public void update(String npcName, int npcIcon, String[] petName2, int[] petIcon2) {
        if (this.npc != null) {
            this.lastName = this.npc.name;
        }
        if (GlobalVar.getGlobalInt("BATTLEFLG") == 0 && Battle.battleID == -1) {
            CommonData.player.visible = false;
            if (CommonData.player.getFollowPet() != null) {
                CommonData.player.getFollowPet().visible = false;
            }
        }
        this.npc = new Npc();
        this.npc.visible = true;
        this.npc.setIconID(npcIcon);
        this.npc.imageUpdate = true;
        this.npc.name = npcName;
        this.npc.pixelX = (short) (this.viewX + 5);
        this.npc.pixelY = (short) (inPositionY[1] + NewStage.getMapDrawY());
        this.npc.go(0, 0);
        this.petName = petName2;
        this.petIcon = petIcon2;
    }

    public void nextRound() {
        this.round = (byte) (this.round + 1);
        this.introTimer = 0;
        this.status = 3;
    }

    private void init() {
        short s;
        short s2;
        this.npc.imageUpdate = false;
        this.playerPets = new Pet[CommonData.player.pets.size()];
        this.player.setDir(0);
        this.player.camp = 1;
        this.player.cartoonPlayer.setAnimateIndex(2);
        this.npc.setDir(1);
        for (int i = 0; i < this.playerPets.length; i++) {
            Pet pp = (Pet) CommonData.player.pets.elementAt(i);
            this.playerPets[i] = new Pet();
            this.playerPets[i].name = pp.name;
            this.playerPets[i].visible = true;
            this.playerPets[i].setIconID(pp.getIconID());
            Pet pet = this.playerPets[i];
            if (this.playerPets.length == 1) {
                s = inPositionX[1];
            } else {
                s = inPositionX[i];
            }
            pet.pixelX = (short) (s + NewStage.viewX);
            Pet pet2 = this.playerPets[i];
            if (this.playerPets.length == 1) {
                s2 = inPositionY[1];
            } else {
                s2 = inPositionY[i];
            }
            pet2.pixelY = (short) (s2 + NewStage.getMapDrawY());
            this.playerPets[i].setDir(0);
            this.playerPets[i].speed = 2;
        }
        this.pets = new Pet[this.petName.length];
        for (int i2 = 0; i2 < this.petName.length; i2++) {
            this.pets[i2] = new Pet();
            this.pets[i2].name = this.petName[i2];
            this.pets[i2].visible = true;
            this.pets[i2].setIconID(this.petIcon[i2]);
            this.pets[i2].pixelX = (short) (inPositionX[i2 + 3] + NewStage.viewX);
            this.pets[i2].pixelY = (short) (inPositionY[i2 + 3] + NewStage.getMapDrawY());
            this.pets[i2].setDir(1);
            this.pets[i2].speed = 2;
        }
        this.player.visible = true;
        this.npc.visible = true;
    }

    public void cycle() {
        short s;
        if (this.viewX != NewStage.viewX) {
            NewStage.scriptMoveMap = true;
            int dx = this.viewX - NewStage.viewX;
            int absDx = Math.abs(dx);
            if (absDx > this.player.speed) {
                absDx = this.player.speed;
            }
            NewStage.moveMap(dx > 0 ? absDx : -absDx);
        } else if (!this.init) {
            this.init = true;
            init();
        }
        int battleFlg = GlobalVar.getGlobalInt("BATTLEFLG");
        if (battleFlg == 0 && Battle.battleID == -1) {
            CommonData.player.visible = false;
            if (CommonData.player.getFollowPet() != null) {
                CommonData.player.getFollowPet().visible = false;
            }
        }
        if (battleFlg == 0 && this.viewX == NewStage.viewX && this.init) {
            switch (this.status) {
                case 1:
                    if (this.introTimer == 0) {
                        addSysMsg(getWelcomeStr(this.player.name, this.arenaName));
                    } else if (this.introTimer == 80) {
                        addSysMsg(getRoundStr(this.player.name, this.arenaName, this.round, this.npc.name));
                        this.status = 2;
                        this.moveCount = 0;
                    }
                    this.introTimer++;
                    break;
                case 2:
                    if (this.moveCount <= (this.moveMax >> 1)) {
                        for (int i = 0; i < this.pets.length; i++) {
                            this.pets[i].moveTo(this.pets[i].pixelX + (this.pets[i].getWidth() >> 1) + 2, inPositionY[i + 3] + NewStage.getMapDrawY());
                            this.pets[i].setState(1);
                        }
                        for (int i2 = 0; i2 < this.playerPets.length; i2++) {
                            Pet pet = this.playerPets[i2];
                            int width = (this.playerPets[i2].pixelX - (this.playerPets[i2].getWidth() >> 1)) - 2;
                            if (this.playerPets.length == 1) {
                                s = inPositionY[1];
                            } else {
                                s = inPositionY[i2];
                            }
                            pet.moveTo(width, s + NewStage.getMapDrawY());
                            this.playerPets[i2].setState(1);
                        }
                    } else if (this.moveCount == (this.moveMax >> 1) + 1) {
                        for (Pet state : this.pets) {
                            state.setState(0);
                        }
                        for (Pet state2 : this.playerPets) {
                            state2.setState(0);
                        }
                    }
                    this.moveCount++;
                    if (this.moveCount == this.moveMax) {
                        this.status = 0;
                        UWAPSegment segment = new UWAPSegment(20, 15);
                        segment.flush();
                        Utilities.sendRequest(segment);
                        break;
                    }
                    break;
                case 3:
                    if (this.introTimer != 0) {
                        if (this.introTimer == 40) {
                            this.status = 1;
                            this.init = false;
                            break;
                        }
                    } else {
                        addSysMsg(getResultStr(this.player.name, this.lastName, this.round - 1));
                    }
                    this.introTimer++;
                    break;
            }
            this.npc.doCycle();
            this.player.doCycle();
            for (int i3 = 0; i3 < this.pets.length; i3++) {
                this.pets[i3].doCycle();
                if (!this.pets[i3].imageUpdate) {
                    ImageLoadManager.requestImage(this.pets[i3].getIconID(), this.pets[i3]);
                }
            }
            for (int i4 = 0; i4 < this.playerPets.length; i4++) {
                this.playerPets[i4].doCycle();
                if (!this.playerPets[i4].imageUpdate) {
                    ImageLoadManager.requestImage(this.playerPets[i4].getIconID(), this.playerPets[i4]);
                }
            }
        }
    }

    public void paint(Graphics g) {
        if (GlobalVar.getGlobalInt("BATTLEFLG") == 0 && this.viewX == NewStage.viewX) {
            if (this.npc != null) {
                this.npc.draw(g);
                if (!this.npc.imageUpdate) {
                    ImageLoadManager.requestImage(this.npc.getIconID(), this.npc);
                }
            }
            this.player.draw(g);
            if (this.pets != null) {
                for (int i = this.pets.length - 1; i >= 0; i--) {
                    this.pets[i].draw(g);
                }
            }
            if (this.playerPets != null) {
                for (int i2 = this.playerPets.length - 1; i2 >= 0; i2--) {
                    this.playerPets[i2].draw(g);
                }
            }
        }
    }

    public void clear() {
        NewStage.scriptMoveMap = false;
        CommonData.player.visible = true;
        CommonData.player.resetFollowPetPosition();
    }
}
