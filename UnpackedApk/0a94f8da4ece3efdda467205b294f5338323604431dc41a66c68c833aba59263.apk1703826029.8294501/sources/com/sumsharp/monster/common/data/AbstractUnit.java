package com.sumsharp.monster.common.data;

import com.sumsharp.monster.Battle;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.PipAnimateSet;
import com.sumsharp.monster.image.PipImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextField;

public abstract class AbstractUnit {
    public static final byte ACTIONSTATE_BATTLE = 3;
    public static final byte ACTIONSTATE_CHAT = 2;
    public static final byte ACTIONSTATE_INUI = 1;
    public static final byte ACTIONSTATE_LOADING = 4;
    public static final byte ACTIONSTATE_NONE = 0;
    public static byte[] ACTION_ANI_FRAME = null;
    public static final int CLR_NAME = 16776960;
    public static final int CLR_NAME_BG = 0;
    public static final int CLR_NAME_NPC = 65280;
    public static final int CLR_NAME_TAR = 255;
    public static final int CLR_NAME_TAR_BG = 16776960;
    public static final int CLR_NAME_TAR_RED = 16711680;
    public static final byte DOWN = 3;
    public static int[][] EMEMY_NAME_CLR = null;
    public static final String EMOTEID_TASK = "EMOTEID_TASK";
    public static final byte EMOTE_BATTLE = 8;
    public static final byte EMOTE_CHAT = 1;
    public static final byte EMOTE_CRY = 2;
    public static final byte EMOTE_DOINGTASK = 1;
    public static final byte EMOTE_DOT = 1;
    public static final byte EMOTE_HASTASK = 4;
    public static final byte EMOTE_INUI = 0;
    public static final byte LEFT = 0;
    public static final short NETPLAYER_SPACE = 40;
    public static final byte NPCTYPE_DIRECTACTIVE = 2;
    public static final byte NPCTYPE_INTERACTIVE = 1;
    public static final short NPC_SHOWNAME_SPACE = 80;
    public static final byte RIGHT = 1;
    public static final byte STATE_ATTACK = 5;
    public static final byte STATE_BATTLE = 6;
    public static final byte STATE_FOLLOW = 3;
    public static final byte STATE_GOBACK = 4;
    public static final byte STATE_MOVING = 1;
    public static final byte STATE_STAND = 0;
    public static final byte STATE_WAYPOINT = 2;
    public static final byte[] TASK_HIT_FRAME = {3, 3, 2, 4};
    public static final byte TYPE_BATTLE_ARMY = 11;
    public static final byte TYPE_BATTLE_PET = 10;
    public static final byte TYPE_FIELDPET = 12;
    public static final byte TYPE_MONSTER = 1;
    public static final byte TYPE_MONSTERGROUP = 3;
    public static final byte TYPE_NPC = 0;
    public static final byte TYPE_PET = 4;
    public static final byte TYPE_PLAYER = 8;
    public static final byte UP = 2;
    public static final byte WPSTATUS_END = 1;
    public static final byte WPSTATUS_START = 0;
    public static final byte WPSTATUS_WALK = 2;
    public static final byte WPTYPE_LINE = 1;
    public static final byte WPTYPE_ONCE = 2;
    public static final byte WPTYPE_ROUND = 0;
    protected byte actionState;
    public short backX;
    public short backY;
    public byte camp;
    public CartoonPlayer cartoonPlayer = createCartoonPlayer();
    public int color;
    private byte dir;
    public CartoonPlayer emotePlayer;
    public Vector emotePlayers = new Vector();
    private byte faceto;
    private boolean follow = false;
    private Pet followPet;
    private boolean followTeam = false;
    public String guild = "";
    protected int iconID;
    public int id;
    public PipImage image;
    public boolean imageUpdate = false;
    public boolean inBattle;
    public int lastPixelX;
    public int lastPixelY;
    public CartoonPlayer levelupPlayer = null;
    public short mapId;
    public int mirrorId;
    public String name = "";
    public byte npcType = 1;
    public short pixelX;
    public short pixelY;
    public boolean showDie = false;
    public int showLevel;
    public int speed = 6;
    private byte state;
    public long stayStarTime = -1;
    public int stayTime = -1;
    public AbstractUnit target;
    public byte teamState;
    public byte tileX;
    public byte tileY;
    public String title = "";
    public boolean touching = false;
    public byte type;
    public boolean visible;
    public byte wpDir;
    public Vector wpList;
    public int wpPointer;
    public byte wpType;

    static {
        int[] iArr = new int[2];
        iArr[1] = 16777215;
        EMEMY_NAME_CLR = new int[][]{new int[]{12851226, 16100955}, new int[]{13922308, 16768403}, iArr, new int[]{4689021, 11919512}, new int[]{6839403, 13421777}};
    }

    public void load(DataInputStream dis) throws IOException {
    }

    public void createEmote(String name2, byte type2, boolean loop) {
        if (this.emotePlayer != null && !this.emotePlayer.id.equals(name2)) {
            this.emotePlayers.addElement(this.emotePlayer);
        }
        this.emotePlayer = CartoonPlayer.playCartoon(Tool.emote, type2, 0, 0, loop);
        this.emotePlayer.id = name2;
    }

    public void clearEmote() {
        if (this.emotePlayer != null) {
            this.emotePlayer.animate.clear();
            this.emotePlayer = null;
        }
        for (int i = 0; i < this.emotePlayers.size(); i++) {
            ((CartoonPlayer) this.emotePlayers.elementAt(i)).animate.clear();
        }
        this.emotePlayers.removeAllElements();
    }

    public void clearEmote(String id2) {
        if (this.emotePlayer != null && this.emotePlayer.id.equals(id2)) {
            this.emotePlayer.animate.clear();
            this.emotePlayer = null;
            if (this.emotePlayers.size() != 0) {
                this.emotePlayer = (CartoonPlayer) this.emotePlayers.elementAt(0);
                this.emotePlayers.removeElementAt(0);
                setEmotePosition(null, (((this.pixelX + (this.cartoonPlayer.getWidth(0, 0) / 2)) - (this.emotePlayer.getWidth() / 2)) - NewStage.viewX) - 10, ((this.pixelY - this.cartoonPlayer.getHeight(0, 0)) - 2) - NewStage.getMapDrawY());
            }
        } else if (this.emotePlayers.size() != 0) {
            for (int i = 0; i < this.emotePlayers.size(); i++) {
                CartoonPlayer p = (CartoonPlayer) this.emotePlayers.elementAt(i);
                if (p.id.equals(id2)) {
                    p.animate.clear();
                    this.emotePlayers.removeElement(p);
                    return;
                }
            }
        }
    }

    public void setEmotePosition(String id2, int x, int y) {
        if (this.emotePlayer == null) {
            return;
        }
        if (id2 == null || this.emotePlayer.id.equals(id2)) {
            this.emotePlayer.setDrawPostion(x, y);
        }
    }

    public final void doCycle() {
        int dx;
        int dy;
        if (this.visible) {
            cycle();
            if (this.wpList == null && this.state == 2) {
                this.state = 0;
            }
            switch (this.state) {
                case 0:
                    cycleStand();
                    break;
                case 1:
                    cycleMoving();
                    break;
                case 2:
                    goWithWayPoint();
                    cycleWapPoint();
                    break;
                case 3:
                    cycleFollow();
                    break;
                case 4:
                    cycleGoBack();
                    break;
                case 5:
                    if (Battle.battleID == -1) {
                        cycleAttack();
                        break;
                    }
                    break;
                case 6:
                    cycleBattle();
                    break;
            }
            if (this.cartoonPlayer != null && ((this.npcType & 1) != 0 || this.imageUpdate)) {
                this.cartoonPlayer.setDrawPostion(getPixelX(), getPixelY());
                this.cartoonPlayer.nextFrame();
                if (!this.inBattle) {
                    int toDir = getFaceto();
                    if (this.state == 0 && (!(this instanceof Npc) || this.camp != 0)) {
                        toDir += 2;
                    }
                    if (this.state == 2 && this.stayStarTime != -1) {
                        toDir += 2;
                    }
                    if (this.cartoonPlayer.getAnimateIndex() != toDir) {
                        this.cartoonPlayer.setFrameIndex(0);
                        this.cartoonPlayer.setAnimateIndex(toDir);
                        if (this.state != 0) {
                            this.followTeam = false;
                            this.follow = false;
                        }
                    }
                }
            }
            if (this.inBattle) {
                this.follow = false;
                this.followTeam = false;
            }
            AbstractUnit au = this.followPet;
            AbstractUnit nextUnit = null;
            if (CommonData.team.id != -1) {
                nextUnit = CommonData.team.getNextMember(this);
                if (nextUnit != null && nextUnit.teamState == 1) {
                    au = CommonData.team.getNextMember(this);
                }
            }
            if (au != null && !this.inBattle) {
                int[] point = getFollowPoint(au.getWidth());
                if (getFaceto() == 0) {
                    dx = (this.pixelX + getWidth()) - au.pixelX;
                    dy = this.pixelY - au.pixelY;
                } else {
                    dx = this.pixelX - (au.pixelX + au.getWidth());
                    dy = this.pixelY - au.pixelY;
                }
                if ((dx * dx) + (dy * dy) > 400) {
                    if (CommonData.team.id == -1 || nextUnit == null || nextUnit.teamState != 1) {
                        this.follow = true;
                    } else {
                        this.followTeam = true;
                    }
                }
                if (this.followTeam || this.follow) {
                    if (!(this.followPet == null || au == this.followPet)) {
                        if (!this.followPet.moveTo(point[0], point[1])) {
                            this.followPet.setState(1);
                        } else {
                            this.followPet.setState(0);
                            this.followPet.setDir(getDir());
                        }
                    }
                    if (!au.moveTo(point[0], point[1])) {
                        au.setState(1);
                    } else {
                        au.setState(0);
                        au.setDir(getDir());
                        if (CommonData.team.id == -1 || nextUnit == null || nextUnit.teamState != 1) {
                            this.follow = false;
                        } else {
                            this.followTeam = false;
                        }
                    }
                }
            }
            if (this.emotePlayer != null) {
                this.emotePlayer.nextFrame();
                if (this.emotePlayer.die) {
                    clearEmote(this.emotePlayer.id);
                }
                if (this.emotePlayer != null) {
                    setEmotePosition(null, getPixelX() + (this.cartoonPlayer.getWidth(0, 0) / 2), (getPixelY() - this.cartoonPlayer.getHeight(0, 0)) - 2);
                }
            }
            if (this.levelupPlayer != null) {
                this.levelupPlayer.nextFrame();
                if (this.levelupPlayer.die) {
                    this.levelupPlayer = null;
                }
            }
        }
    }

    public int[] getFacePoint() {
        int[] ret = new int[2];
        if (getFaceto() == 0) {
            ret[0] = getPixelX() - getWidth();
            ret[1] = getPixelY() + 10;
        } else {
            ret[0] = getPixelX() + getWidth();
            ret[1] = getPixelY() + 10;
        }
        return ret;
    }

    public int[] getFollowPoint(int targetWidth) {
        int[] ret = new int[2];
        if (getFaceto() == 0) {
            ret[0] = this.pixelX + getWidth() + (targetWidth / 2) + 5;
            ret[1] = this.pixelY;
        } else {
            ret[0] = ((this.pixelX - targetWidth) + (targetWidth / 2)) - 5;
            ret[1] = this.pixelY;
        }
        return ret;
    }

    /* access modifiers changed from: protected */
    public void cycle() {
    }

    /* access modifiers changed from: protected */
    public void cycleBattle() {
    }

    /* access modifiers changed from: protected */
    public void cycleGoBack() {
        if (!moveTo(this.backX + (getWidth() / 2), this.backY)) {
            return;
        }
        if (this.wpList != null) {
            this.state = 2;
        } else {
            this.state = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void cycleStand() {
    }

    /* access modifiers changed from: protected */
    public void cycleMoving() {
    }

    /* access modifiers changed from: protected */
    public void cycleAttack() {
    }

    /* access modifiers changed from: protected */
    public void cycleFollow() {
    }

    /* access modifiers changed from: protected */
    public void cycleWapPoint() {
    }

    public void draw(Graphics g) {
        draw(g, false);
    }

    public void draw(Graphics g, boolean onlyName) {
        int nameClr;
        if (this.visible) {
            if ((this.npcType & 1) != 0 || this.imageUpdate) {
                g.setFont(Utilities.font);
                if (!onlyName) {
                    if (this.showDie) {
                        int trans = 0;
                        if (getDir() == 1) {
                            trans = 2;
                        }
                        Tool.uiMiscImg.drawFrame(g, 50, (getWidth() / 2) + this.pixelX, this.pixelY, trans, 33);
                    } else {
                        try {
                            this.cartoonPlayer.draw(g);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if ((this.npcType & 1) != 0 && (this.npcType & 2) == 0) {
                    int stringWidth = Utilities.font.stringWidth(this.name);
                    g.setFont(Utilities.font);
                    int px = CommonData.player.pixelX + (CommonData.player.getWidth() / 2);
                    int py = CommonData.player.pixelY;
                    int emoOffset = 0;
                    if (((px - (this.pixelX + (getWidth() / 2))) * (px - (this.pixelX + (getWidth() / 2)))) + ((py - this.pixelY) * (py - this.pixelY)) < 6400 && !this.inBattle && (((this instanceof NetPlayer) && this == CommonData.player.targetPlayer) || (this instanceof Npc) || (((NewStage.displayQuanlity == 1 || NewStage.displayQuanlity == 2) && (this instanceof Pet) && CommonData.player.targetPlayer != null && this == CommonData.player.targetPlayer.followPet) || NewStage.displayQuanlity == 0))) {
                        g.setFont(Utilities.font);
                        String drawName = this.name;
                        int nx = getPixelX();
                        int ny = getPixelY() - getHeight();
                        if (!isFriend(CommonData.player)) {
                            drawName = String.valueOf(this.showLevel) + "]" + drawName;
                        }
                        int fw = Utilities.font.stringWidth(drawName);
                        if (CommonData.team != null && CommonData.team.leader == this) {
                            fw += Tool.uiMiscImg.getFrameWidth(45) + 2;
                        }
                        int nx2 = nx - ((fw - getWidth()) / 2);
                        int nameClr2 = 16776960;
                        int nameBg = 0;
                        String tip1 = "";
                        int ntw = 0;
                        int tw1 = 0;
                        if (this.id == 36896775) {
                            tip1 = "靠近我!";
                            ntw = Utilities.font.stringWidth("请靠近我");
                            tw1 = Utilities.font.stringWidth(tip1);
                        }
                        if (this == CommonData.player.targetPlayer) {
                            nameClr2 = CLR_NAME_TAR;
                            if (this.camp != CommonData.player.camp) {
                                nameClr2 = CLR_NAME_TAR_RED;
                            }
                            nameBg = 16776960;
                        } else if ((this instanceof NetPlayer) && CommonData.player.getFriend(this.id) != null && this.camp == CommonData.player.camp) {
                            nameClr2 = 3835135;
                            nameBg = 0;
                        } else if (this.camp != CommonData.player.camp) {
                            nameClr2 = CLR_NAME_TAR_RED;
                            nameBg = 0;
                        } else if (this.touching) {
                            nameClr2 = 16724736;
                            nameBg = 0;
                            if (this.id == 36896775) {
                                tip1 = "按确定键对话";
                                ntw = Utilities.font.stringWidth("按确定键对话");
                                tw1 = Utilities.font.stringWidth(tip1);
                            }
                        } else if (this instanceof Npc) {
                            nameClr2 = CLR_NAME_NPC;
                        }
                        if (CommonData.team != null && CommonData.team.leader == this) {
                            Tool.uiMiscImg.drawFrame(g, 45, nx2, (ny - (Utilities.CHAR_HEIGHT / 2)) - 2, 0, 6);
                            nx2 += Tool.uiMiscImg.getFrameWidth(45) + 2;
                        }
                        if (isFriend(CommonData.player)) {
                            if (this.id == 36896775) {
                                if (!this.touching) {
                                    Tool.draw3DString(g, this.name, nx2 - ((ntw - fw) / 2), ny - Utilities.CHAR_HEIGHT, nameClr2, nameBg);
                                    Tool.draw3DString(g, tip1, (getPixelX() - 2) - tw1, ny + Utilities.CHAR_HEIGHT, nameClr2, nameBg);
                                    Tool.draw3DString(g, tip1, getPixelX() + getWidth() + 2, ny + Utilities.CHAR_HEIGHT, nameClr2, nameBg);
                                    Tool.draw3DString(g, tip1, nx2 - ((tw1 - fw) / 2), getPixelY() + Utilities.CHAR_HEIGHT, nameClr2, nameBg);
                                } else {
                                    Tool.draw3DString(g, this.name, nx2, (ny - (Utilities.CHAR_HEIGHT * 2)) - 4, CLR_NAME_NPC, nameBg);
                                }
                            } else if (!this.touching) {
                                Tool.draw3DString(g, drawName, nx2, ny - Utilities.CHAR_HEIGHT, nameClr2, nameBg);
                            }
                            if (this instanceof NetPlayer) {
                                NetPlayer np = (NetPlayer) this;
                                if (np.rank > 0) {
                                    Tool.rank.drawFrame(g, np.rank - 1, Utilities.font.stringWidth(drawName) + nx2 + 5, ny, 0, 36);
                                }
                            } else if (this instanceof Pet) {
                                if (((Pet) this).matingTimes > 0) {
                                    Tool.drawNumStr("+" + pet.matingTimes, g, nx2 + (Utilities.font.stringWidth(drawName) >> 1), (ny - Utilities.LINE_HEIGHT) - 1, 0, 33, -1);
                                }
                            }
                        } else {
                            int nameIdx = getNameClrIdx();
                            if (this instanceof NetPlayer) {
                                nameIdx = 0;
                            }
                            int[] clr = EMEMY_NAME_CLR[nameIdx];
                            int nx3 = nx2 - (Tool.uiLV.getFrameWidth(0) / 2);
                            int offw = Utilities.font.charWidth('[') + 1;
                            int nameClr3 = clr[1];
                            int bgClr = clr[0];
                            if (this == CommonData.player.targetPlayer) {
                                nameClr3 = CLR_NAME_TAR_RED;
                                bgClr = 0;
                            }
                            Tool.draw3DString(g, "[", nx3, ny - Utilities.CHAR_HEIGHT, nameClr3, bgClr);
                            int nx4 = nx3 + offw;
                            Tool.uiLV.drawFrame(g, nameIdx, nx4, ny, 0, 36);
                            Tool.draw3DString(g, drawName, nx4 + Tool.uiLV.getFrameWidth(0) + 1, ny - Utilities.CHAR_HEIGHT, nameClr3, bgClr);
                        }
                        if (!this.title.equals("")) {
                            Tool.draw3DString(g, "< " + this.title + " >", getPixelX() - ((Utilities.font.stringWidth("< " + this.title + " >") - getWidth()) / 2), (ny - Utilities.CHAR_HEIGHT) - Utilities.CHAR_HEIGHT, this.color, 0);
                            emoOffset = 0 - Utilities.CHAR_HEIGHT;
                        } else if (!this.guild.equals("")) {
                            int nx5 = getPixelX() - ((Utilities.font.stringWidth("< " + this.guild + " >") - getWidth()) / 2);
                            int ny2 = ny - Utilities.CHAR_HEIGHT;
                            int bgClr2 = 0;
                            if (this instanceof Npc) {
                                nameClr = CLR_NAME_NPC;
                            } else if (!(this instanceof NetPlayer) || this.camp == CommonData.player.camp) {
                                nameClr = 16776960;
                            } else {
                                int[] clr2 = EMEMY_NAME_CLR[0];
                                nameClr = clr2[1];
                                bgClr2 = clr2[0];
                                if (this == CommonData.player.targetPlayer) {
                                    nameClr = CLR_NAME_TAR_RED;
                                    bgClr2 = 0;
                                }
                            }
                            Tool.draw3DString(g, "< " + this.guild + " >", nx5, ny2 - Utilities.CHAR_HEIGHT, nameClr, bgClr2);
                            emoOffset = 0 - Utilities.CHAR_HEIGHT;
                        }
                        emoOffset -= Utilities.CHAR_HEIGHT;
                    }
                    if (!this.inBattle) {
                        if (this.emotePlayer != null) {
                            this.emotePlayer.draw(g, this.emotePlayer.drawX, this.emotePlayer.drawY + emoOffset);
                        }
                        if (this.levelupPlayer != null && !this.levelupPlayer.die) {
                            this.levelupPlayer.draw(g, (this.pixelX + (getWidth() >> 1)) - NewStage.viewX, this.pixelY - NewStage.getMapDrawY());
                        }
                    }
                }
            }
        }
    }

    public int getNameClrIdx() {
        int levelDiff = CommonData.player.battlePetMain.getAttribute(50) - this.showLevel;
        if (levelDiff <= -6) {
            return 0;
        }
        if (levelDiff <= -3) {
            return 1;
        }
        if (levelDiff <= 2) {
            return 2;
        }
        if (levelDiff <= 5) {
            return 3;
        }
        return 4;
    }

    public CartoonPlayer createCartoonPlayer() {
        return null;
    }

    public void updateImage(String name2) {
        PipAnimateSet ani = new PipAnimateSet(World.findResource("/" + name2, false));
        if (ani != null) {
            CartoonPlayer cp = CartoonPlayer.playCartoon(ani, getFaceto(), this.pixelX, this.pixelY, true);
            cp.setAnimateIndex(this.cartoonPlayer.getAnimateIndex());
            if (this.inBattle) {
                this.pixelX = (short) (this.pixelX + (getWidth() / 2));
            }
            this.cartoonPlayer = cp;
            this.imageUpdate = true;
            if (this.inBattle) {
                this.pixelX = (short) (this.pixelX - (getWidth() / 2));
            }
            ani.toFullBuffer();
        }
    }

    public int[] getCollisionBox() {
        int x1;
        int y1;
        int w1;
        int h1;
        int x = this.pixelX;
        int y = this.pixelY;
        if ((this.npcType & 2) != 0) {
            x1 = x;
            y1 = y - getHeight();
            w1 = getWidth();
            h1 = getHeight();
        } else {
            x1 = x;
            y1 = y - 16;
            w1 = getWidth();
            h1 = 16;
        }
        return new int[]{x1, y1, w1, h1};
    }

    public void clear() {
        if (this.cartoonPlayer != null) {
            this.cartoonPlayer.animate.clear();
        }
        clearEmote();
    }

    public int[] getTouchUnitBox() {
        int[] npcBox = getCollisionBox();
        int step = CommonData.player.speed;
        npcBox[0] = npcBox[0] - step;
        npcBox[1] = npcBox[1] - step;
        npcBox[2] = npcBox[2] + (step * 2);
        npcBox[3] = npcBox[3] + (step * 2);
        return npcBox;
    }

    public int getOriginX() {
        return this.pixelX + (getWidth() / 2);
    }

    public int getOriginY() {
        return this.pixelY;
    }

    public short getXPoint() {
        return (short) ((this.pixelX + (getWidth() / 2)) / 16);
    }

    public short getYPoint() {
        return (short) (this.pixelY / 16);
    }

    public int getPixelX() {
        if (this.inBattle) {
            return this.pixelX;
        }
        return this.pixelX - NewStage.viewX;
    }

    public int getPixelY() {
        if (this.inBattle) {
            return this.pixelY;
        }
        return this.pixelY - NewStage.getMapDrawY();
    }

    /* Debug info: failed to restart local var, previous not found, register: 17 */
    public int[] getCoverTiles(int pixelX2, int pixelY2) {
        Vector v = new Vector();
        int rw = getWidth() + (pixelX2 % 16);
        int rh = (getHeight() + 16) - (pixelY2 % 16);
        int cx = rw / 16;
        int cy = rh / 16;
        if (rw % 16 != 0) {
            cx++;
        }
        if (rh % 16 != 0) {
            cy++;
        }
        for (int y = cy - 1; y >= 0; y--) {
            for (int x = cx - 1; x >= 0; x--) {
                short tileIdx = (short) (((this.tileY - y) * NewStage.mapWidth) + x + this.tileX);
                if (tileIdx >= 0 && tileIdx < NewStage.mapData.length) {
                    short[] tileInfo = NewStage.mapData[tileIdx];
                    for (int layer = tileInfo.length - 1; layer >= 0; layer--) {
                        if ((tileIdx / NewStage.mapWidth) + NewStage.getTileHeight(tileInfo[layer]) > this.tileY) {
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
        int[] t1 = getCoverTiles(this.pixelX, this.pixelY);
        for (int i = 0; i < t1.length; i++) {
            if (t1[i] >= 0) {
                v.addElement(new Integer(t1[i]));
            }
        }
        return t1;
    }

    public int getCurrFrameWidth() {
        return this.cartoonPlayer.getWidth(this.cartoonPlayer.getAnimateIndex(), this.cartoonPlayer.getFrameIndex());
    }

    public int getCurrFrameHeight() {
        return this.cartoonPlayer.getHeight(this.cartoonPlayer.getAnimateIndex(), this.cartoonPlayer.getFrameIndex());
    }

    public int getWidth() {
        return this.cartoonPlayer.getWidth(0, 0);
    }

    public int getHeight() {
        return this.cartoonPlayer.getHeight(0, 0);
    }

    public byte goWithWayPoint() {
        byte status = 2;
        if (this.stayStarTime != -1) {
            if (System.currentTimeMillis() - this.stayStarTime < ((long) this.stayTime)) {
                return 0;
            }
            this.stayStarTime = -1;
            this.stayTime = -1;
        }
        int npcX = this.pixelX + (getWidth() / 2);
        int npcY = this.pixelY;
        int dest = ((Integer) this.wpList.elementAt(this.wpPointer)).intValue();
        int destx = (dest >> 16) + (getWidth() / 2);
        int desty = dest & TextField.CONSTRAINT_MASK;
        int diffy = desty - npcY;
        if (destx - npcX == 0 && diffy == 0) {
            this.wpPointer += this.wpDir;
            if (this.wpPointer == this.wpList.size()) {
                status = 1;
                switch (this.wpType) {
                    case 0:
                        this.wpPointer = 1;
                        break;
                    case 1:
                        this.wpPointer -= 2;
                        this.wpDir = -1;
                        break;
                    case 2:
                        this.wpList = null;
                        this.state = 0;
                        break;
                }
            } else if (this.wpPointer == -1) {
                status = 0;
                if (this.wpType == 1) {
                    this.wpPointer = 1;
                    this.wpDir = 1;
                }
            }
            if (this.stayStarTime == -1 && (this instanceof Npc)) {
                if (Utilities.random(1, 100) < 30) {
                    this.stayStarTime = System.currentTimeMillis();
                    this.stayTime = Utilities.random(3, 8) * 1000;
                }
                this.cartoonPlayer.setAnimateIndex(getFaceto() + 2);
            }
        } else {
            moveTo(destx, desty);
        }
        return status;
    }

    public void initWayPoint(boolean changeState) {
        this.wpList = new Vector();
        this.wpList.addElement(new Integer((this.pixelX << 16) | this.pixelY));
        this.wpPointer = 1;
        this.wpDir = 1;
        if (changeState) {
            this.state = 2;
        }
    }

    public void addWayPoint(short x, short y) {
        if (this.wpList == null) {
            initWayPoint(true);
        }
        addWayPointDirectly(x, y);
    }

    public void addWayPointDirectly(short x, short y) {
        if (this.wpList != null) {
            this.wpList.addElement(new Integer((x << 16) | y));
        }
    }

    public void addWayPoint(byte x, byte y) {
        addWayPoint((short) (x * 16), (short) ((y + 1) * 16));
    }

    public boolean moveTo(int destx, int desty) {
        int npcX = this.pixelX + (getWidth() / 2);
        int npcY = this.pixelY;
        int dx = this.speed;
        int dy = dx;
        int diffx = Math.abs(destx - npcX);
        int diffy = Math.abs(desty - npcY);
        if (diffx >= diffy && diffx != 0) {
            dy = (diffy * ((dx * 10000) / Math.abs(diffx))) / 10000;
        }
        if (diffx < diffy && diffy != 0) {
            dx = (diffx * ((dy * 10000) / Math.abs(diffy))) / 10000;
        }
        if (Math.abs(diffx) < dx) {
            dx = Math.abs(diffx);
        }
        if (Math.abs(diffy) < dy) {
            dy = Math.abs(diffy);
        }
        if (destx - npcX < 0) {
            dx = -dx;
        }
        if (desty - npcY < 0) {
            dy = -dy;
        }
        go(dx, dy);
        int difx = destx - npcX;
        byte dir2 = this.dir;
        if (difx > 0) {
            dir2 = 1;
        } else if (difx < 0) {
            dir2 = 0;
        }
        if (dir2 != this.dir) {
            setDir(dir2);
        }
        if (dx == 0 && dy == 0) {
            return true;
        }
        return false;
    }

    public void go(int dx, int dy) {
        this.lastPixelX = this.pixelX;
        this.lastPixelY = this.pixelY;
        this.pixelX = (short) (this.pixelX + dx);
        this.pixelY = (short) (this.pixelY + dy);
        this.tileX = (byte) (this.pixelX / 16);
        this.tileY = (byte) (this.pixelY / 16);
    }

    public void setPosition(int x, int y) {
        this.lastPixelX = this.pixelX;
        this.lastPixelY = this.pixelY;
        this.pixelX = (short) x;
        this.pixelY = (short) y;
        if (!this.inBattle) {
            this.pixelX = (short) (this.pixelX + NewStage.viewX);
            this.pixelY = (short) (this.pixelY + NewStage.getMapDrawY());
        }
        this.tileX = (byte) (this.pixelX / 16);
        this.tileY = (byte) (this.pixelY / 16);
    }

    public byte getState() {
        return this.state;
    }

    public void setState(byte state2) {
        this.state = state2;
    }

    public void savePosition() {
        this.backX = this.pixelX;
        this.backY = this.pixelY;
    }

    public void setDir(byte dir2) {
        this.dir = dir2;
        if (dir2 == 0 || dir2 == 1) {
            this.faceto = dir2;
        }
    }

    public byte getDir() {
        return this.dir;
    }

    public byte getFaceto() {
        return this.faceto;
    }

    public boolean isFriend(AbstractUnit au) {
        return au.camp == this.camp;
    }

    public void setActionState(byte state2) {
        this.actionState = state2;
        World.requestSendPosition();
    }

    public byte getActionState() {
        return this.actionState;
    }

    public static void initActionAniFrame(byte[] frame) {
        ACTION_ANI_FRAME = new byte[frame.length];
        System.arraycopy(frame, 0, ACTION_ANI_FRAME, 0, frame.length);
    }

    public static byte getActionAniFrame(int type2) {
        if (type2 < ACTION_ANI_FRAME.length) {
            return ACTION_ANI_FRAME[type2];
        }
        return -1;
    }

    public Pet getFollowPet(boolean force) {
        if (force || !CommonData.team.isTeamMode() || (this.teamState != 1 && (CommonData.team.leader != this || CommonData.team.getNextMember(this) == null))) {
            return this.followPet;
        }
        return null;
    }

    public Pet getFollowPet() {
        return getFollowPet(true);
    }

    public void setFollowPet(Pet followPet2) {
        this.followPet = followPet2;
        if (followPet2 != null) {
            followPet2.visible = true;
        }
        resetFollowPetPosition();
    }

    public void resetFollowPetPosition() {
        if (this.followPet != null) {
            int[] pt = getFollowPoint(this.followPet.getWidth());
            this.followPet.pixelX = (short) pt[0];
            this.followPet.pixelY = (short) pt[1];
            this.followPet.wpList = null;
            this.followPet.setState(0);
        }
    }

    public void drawHead(Graphics g, int x, int y) {
        this.cartoonPlayer.animate.drawHead(g, x, y);
    }

    public int getIconID() {
        return this.iconID;
    }

    public void setIconID(int iconID2) {
        if (this.iconID != iconID2) {
            this.iconID = iconID2;
            this.imageUpdate = false;
        }
    }

    public int[] getSelectBounds() {
        int[] off = this.cartoonPlayer.getAnimate().getFrameOffset(2, 0);
        int cw = getWidth() + 40;
        int y = (getPixelY() - 40) + 5;
        int x = (getPixelX() - off[0]) - 20;
        getDir();
        return new int[]{x, y, cw, 40};
    }
}
