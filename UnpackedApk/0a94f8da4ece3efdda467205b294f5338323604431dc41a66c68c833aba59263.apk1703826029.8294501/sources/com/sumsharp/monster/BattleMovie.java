package com.sumsharp.monster;

import com.sumsharp.lowui.UI;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.image.CartoonPlayer;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class BattleMovie {
    public static final byte CLRIDX_GREEN = 0;
    public static final byte CLRIDX_RED = 1;
    public static final int MOVIE_ATTACK = 1;
    public static final int MOVIE_BATTLERESULT = 9;
    public static final int MOVIE_CHANGEPROPERTY = 5;
    public static final int MOVIE_DIE = 8;
    public static final int MOVIE_FLYSTRING = 6;
    public static final int MOVIE_HURT = 7;
    public static final int MOVIE_MOVEBACK = 2;
    public static final int MOVIE_MOVETO = 0;
    public static final int MOVIE_RUNAWAY = 10;
    public static final int MOVIE_USEITEM = 4;
    public static final int MOVIE_USESKILL = 3;
    public static final byte STATE_ATK = 5;
    public static final byte STATE_CACHE = 2;
    public static final byte STATE_CACHE_FAILED = 4;
    public static final byte STATE_CACHE_SUCC = 3;
    public static final byte STATE_IMMUNITY = 10;
    public static final boolean[] STATE_JUMP;
    public static final byte STATE_MAG = 6;
    public static final byte STATE_MISS = 1;
    public static final byte STATE_RESIST = 11;
    public static final byte STATE_RUNAWAY = 7;
    public static final byte STATE_RUNAWAY_FAILED = 9;
    public static final byte STATE_RUNAWAY_SUCC = 8;
    public static final int[] flyStrOffy;
    public int atkType;
    public int backAnimateIdx;
    public int changeValue;
    public int color;
    public int currentFrame = 0;
    public String dialogue;
    public boolean die = false;
    public int drawx;
    public int drawy;
    public int frameLength;
    public int h = 0;
    public boolean isCri;
    public boolean isHit;
    public Vector players = new Vector();
    public byte[] proChg;
    public int[] proChgSpeed;
    public int[] proChgValue;
    public boolean show = false;
    public boolean showDieImg = false;
    public String showText;
    public Pet src;
    public int startFrame;
    public boolean started = false;
    public int stateString;
    public Pet[] target;
    public int type;
    public int vx = 0;
    public int vy = 0;
    public int w = 0;
    public boolean win = false;

    static {
        boolean[] zArr = new boolean[13];
        zArr[0] = true;
        zArr[1] = true;
        zArr[10] = true;
        zArr[11] = true;
        STATE_JUMP = zArr;
        int[] iArr = new int[18];
        iArr[0] = -5;
        iArr[1] = -5;
        iArr[2] = -3;
        iArr[3] = -1;
        iArr[4] = 1;
        iArr[5] = 3;
        iArr[6] = 5;
        iArr[7] = 5;
        iArr[8] = -3;
        iArr[9] = -2;
        iArr[10] = 2;
        iArr[11] = 3;
        iArr[12] = -2;
        iArr[13] = 2;
        iArr[14] = -1;
        iArr[15] = 1;
        flyStrOffy = iArr;
    }

    public void start() {
        if (!this.started && !this.die) {
            this.started = true;
            this.currentFrame = 0;
        }
    }

    private void calcMoveToVxy() {
        int tarx = 0;
        int tary = 0;
        for (Pet facePoint : this.target) {
            int[] xy = facePoint.getFacePoint();
            tarx += xy[0];
            tary += xy[1];
        }
        int tarx2 = tarx / this.target.length;
        int tary2 = tary / this.target.length;
        this.vx = (tarx2 - this.src.pixelX) / this.frameLength;
        this.vy = (tary2 - this.src.pixelY) / this.frameLength;
        this.src.savePosition();
    }

    private void calcMoveBackVxy() {
        int tarx = this.src.backX;
        int tary = this.src.backY;
        this.vx = (tarx - this.src.pixelX) / this.frameLength;
        this.vy = (tary - this.src.pixelY) / this.frameLength;
    }

    private void cycleMoveTo() {
        if (this.currentFrame == 0) {
            calcMoveToVxy();
        }
        this.src.go(this.vx, this.vy);
    }

    private void cycleMoveBack() {
        if (this.currentFrame == 0) {
            calcMoveBackVxy();
        }
        this.src.go(this.vx, this.vy);
        if (isLastFrame()) {
            this.src.setPosition(this.src.backX, this.src.backY);
        }
    }

    private void cycleRunaway() {
        if (this.currentFrame == 0) {
            this.backAnimateIdx = this.src.cartoonPlayer.getAnimateIndex();
            this.src.cartoonPlayer.setAnimateIndex((this.src.getFaceto() + 1) % 2);
            this.src.cartoonPlayer.setFrameIndex(0);
        }
        this.src.cartoonPlayer.nextFrame();
        if (this.currentFrame >= 10 && this.win) {
            int step = 10;
            if (this.src.getFaceto() == 1) {
                step = -10;
            }
            this.src.go(step, 0);
        }
        if (isLastFrame()) {
            this.src.cartoonPlayer.setAnimateIndex(this.backAnimateIdx);
            if (this.win) {
                this.src.visible = false;
            }
        }
    }

    private void cycleAttack() {
        if (this.currentFrame == 0) {
            for (Pet pet : this.target) {
                pet.isTarget = true;
            }
            this.backAnimateIdx = this.src.cartoonPlayer.getAnimateIndex();
            this.src.cartoonPlayer.setAnimateIndex(this.src.getFaceto() + 4);
            this.src.cartoonPlayer.setFrameIndex(0);
            if (this.atkType != 1 && this.src.cartoonPlayer.getAnimate().getAnimateLength() > 7) {
                this.src.cartoonPlayer.setAnimateIndex(this.src.getFaceto() + 6);
            }
        }
        if (isLastFrame()) {
            this.src.cartoonPlayer.setAnimateIndex(this.backAnimateIdx);
        }
    }

    private boolean isLastFrame() {
        return this.currentFrame == this.frameLength - 1;
    }

    private void cycleFlyString() {
        this.src.isTarget = !isLastFrame();
        if (this.currentFrame == 0) {
            this.drawx = this.src.getPixelX() + (this.src.getWidth() >> 1) + 7;
            this.drawy = (this.src.getPixelY() - (this.src.getHeight() >> 1)) + 14;
            if (this.showText != null) {
                this.drawx -= Utilities.font.stringWidth(this.showText) >> 1;
            }
            if (!STATE_JUMP[this.stateString]) {
                this.vx = 30;
                this.vy = 0;
                return;
            }
            this.vx = Utilities.random(1, 3);
            if (this.isCri) {
                this.vx = 4;
            }
        } else if (!STATE_JUMP[this.stateString]) {
            if (this.currentFrame < 3) {
                this.vx -= 15;
            } else if (this.currentFrame < 6) {
                this.vy += 30;
            } else if (this.currentFrame < 11) {
                this.vy -= 18;
            } else if (this.currentFrame < 13) {
                this.vx -= 15;
            }
        } else if (this.color == 0 && this.stateString == 0) {
            this.drawy -= 4;
        } else {
            if (this.src.getFaceto() == 1) {
                this.drawx -= 2;
            } else {
                this.drawx += 2;
            }
            this.drawy += flyStrOffy[this.currentFrame - 1] * this.vx;
            int length = flyStrOffy.length;
        }
    }

    public void drawPlayers(Graphics g) {
        for (int i = 0; i < this.players.size(); i++) {
            ((CartoonPlayer) this.players.elementAt(i)).draw(g);
        }
    }

    public void cyclePlayers() {
        Vector remove = new Vector();
        for (int i = 0; i < this.players.size(); i++) {
            CartoonPlayer cp = (CartoonPlayer) this.players.elementAt(i);
            cp.nextFrame();
            if (cp.die) {
                remove.addElement(cp);
            } else if (this.currentFrame == 0) {
                cp.setDrawPostion(this.src.getPixelX() + (this.src.getWidth() / 2), this.src.getPixelY() - (this.src.getHeight() / 2));
            }
        }
        for (int i2 = 0; i2 < remove.size(); i2++) {
            this.players.removeElement((CartoonPlayer) this.players.elementAt(i2));
        }
    }

    private void cycleBattleResult() {
        if (this.currentFrame < 5) {
            this.vx += (World.viewWidth >> 1) / 5;
        } else if (this.h < 17) {
            this.h += 4;
        } else {
            this.w += 4;
        }
    }

    public void cycle(int battleFrame) {
        boolean z;
        if (this.currentFrame >= this.frameLength) {
            this.die = true;
        }
        if (this.started) {
            cyclePlayers();
        }
        String msg = "";
        if (this.currentFrame == 0) {
            msg = "[战斗信息]" + battleFrame + "帧：";
        } else if (this.currentFrame == this.frameLength) {
            msg = "[结束信息]" + battleFrame + "帧：";
        }
        if (this.started && !this.die) {
            switch (this.type) {
                case 0:
                    String msg2 = String.valueOf(msg) + this.src.name + "[移动]";
                    cycleMoveTo();
                    break;
                case 1:
                    String msg3 = String.valueOf(msg) + this.src.name + "[攻击]";
                    for (int i = 0; i < this.target.length; i++) {
                        msg3 = String.valueOf(msg3) + this.target[i].name + " ";
                    }
                    cycleAttack();
                    break;
                case 2:
                    String msg4 = String.valueOf(msg) + this.src.name + "[回到原位置]";
                    cycleMoveBack();
                    break;
                case 3:
                    String msg5 = String.valueOf(msg) + this.src.name + "[使用技能][" + this.showText + "]";
                    break;
                case 4:
                    String msg6 = String.valueOf(msg) + this.src.name + "[使用物品] [" + this.showText + "] 对 ";
                    for (int i2 = 0; i2 < this.target.length; i2++) {
                        msg6 = String.valueOf(msg6) + this.target[i2].name + " ";
                    }
                    break;
                case 5:
                    String msg7 = String.valueOf(msg) + this.src.name;
                    for (int i3 = 0; i3 < this.proChg.length; i3++) {
                        String msg8 = String.valueOf(msg7) + " [属性][" + Pet.ATTR_TEXT[this.proChg[i3]] + "]";
                        if (this.proChgValue[i3] > 0) {
                            msg8 = String.valueOf(msg8) + "+";
                        }
                        msg7 = String.valueOf(msg8) + this.proChgValue[i3];
                    }
                    for (int i4 = 0; i4 < this.proChg.length; i4++) {
                        this.src.changeAttr(this.proChg[i4], this.proChgSpeed[i4]);
                    }
                    if (isLastFrame()) {
                        for (int i5 = 0; i5 < this.proChg.length; i5++) {
                            this.src.changeAttr(this.proChg[i5], this.proChgValue[i5] - (this.proChgSpeed[i5] * this.frameLength));
                        }
                        break;
                    }
                    break;
                case 6:
                    String msg9 = String.valueOf(msg) + this.src.name + " [FLYSTRING] " + this.showText;
                    cycleFlyString();
                    break;
                case 7:
                    String msg10 = String.valueOf(msg) + this.src.name + " [被攻击]";
                    if (this.atkType == 0) {
                        if (this.currentFrame % 2 == 0) {
                            this.src.go(-3, 0);
                        } else {
                            this.src.go(3, 0);
                        }
                    }
                    if (this.dialogue != null && !this.dialogue.equals("")) {
                        Battle.addDialog(this.dialogue);
                        break;
                    }
                case 8:
                    String msg11 = String.valueOf(msg) + this.src.name + " [挂了...]";
                    this.src.showHp = false;
                    if (this.vy == this.vx) {
                        this.vy = 0;
                        this.vx--;
                        if (this.vx < 1) {
                            this.vx = 1;
                        }
                    }
                    if (this.vy == 0) {
                        if (this.showDieImg) {
                            this.src.showDie = !this.src.showDie;
                        } else {
                            this.src.visible = !this.src.visible;
                        }
                    }
                    this.vy++;
                    if (isLastFrame() || this.frameLength - this.currentFrame < 3) {
                        if (!this.showDieImg) {
                            this.src.showDie = false;
                            this.src.visible = this.show;
                            this.src.showHp = this.show;
                            break;
                        } else {
                            this.src.showDie = this.show;
                            Pet pet = this.src;
                            if (this.show) {
                                z = false;
                            } else {
                                z = true;
                            }
                            pet.showHp = z;
                            break;
                        }
                    }
                case 9:
                    String msg12 = String.valueOf(msg) + "[战斗结束...]";
                    cycleBattleResult();
                    break;
                case 10:
                    cycleRunaway();
                    break;
            }
            isLastFrame();
            this.currentFrame++;
        }
    }

    private void drawBattleResult(Graphics g) {
        g.setColor(Tool.CLR_ITEM_WHITE);
        g.drawLine(this.drawx, this.drawy, this.drawx - this.vx, this.drawy);
        g.drawLine(this.drawx, this.drawy, this.drawx + this.vx, this.drawy);
        int fid = 12;
        if (!this.win) {
            fid = 13;
        }
        g.setClip(0, this.drawy - this.h, World.viewWidth, this.h);
        Battle.battleText.drawFrame(g, fid, this.drawx, this.drawy - this.h, 0, 17);
    }

    private void drawFlyString(Graphics g) {
        int drawy2;
        int dx;
        if (this.stateString != 0) {
            Battle.battleText.pipImg.lighter(this.vx);
            int w2 = Battle.battleText.getFrameWidth(this.stateString - 1);
            int h2 = Battle.battleText.getFrameHeight(this.stateString - 1);
            if (!STATE_JUMP[this.stateString]) {
                g.setClip((this.drawx - this.vx) - (w2 >> 1), this.drawy - h2, w2, (h2 >> 1) + 1);
                Battle.battleText.pipImg.lighter(this.vy);
                Battle.battleText.drawFrame(g, this.stateString - 1, this.drawx - this.vx, this.drawy, 0, 33);
                g.setClip((this.drawx + this.vx) - (w2 >> 1), this.drawy - (h2 >> 1), w2, h2 >> 1);
                Battle.battleText.pipImg.lighter(this.vy);
                Battle.battleText.drawFrame(g, this.stateString - 1, this.vx + this.drawx, this.drawy, 0, 33);
                g.setClip(0, 0, World.viewWidth, World.viewHeight);
                return;
            }
            Battle.battleText.drawFrame(g, this.stateString - 1, this.vx + this.drawx, this.drawy, 0, 33);
            return;
        }
        int drawy3 = this.drawy;
        if (this.color == 0) {
            if (this.isCri) {
                Battle.battleNum.drawFrame(g, 24, this.drawx, drawy3 - 7, 0, 3);
                drawy2 = drawy3;
            }
            drawy2 = drawy3;
        } else {
            if (this.isCri) {
                Battle.battleNum.drawFrame(g, 24, this.drawx, drawy3, 0, 33);
                drawy2 = drawy3 - ((Battle.battleNum.getFrameHeight(24) >> 1) - 8);
            }
            drawy2 = drawy3;
        }
        String s = String.valueOf(Math.abs(this.changeValue));
        if (this.changeValue >= 0) {
            s = "+" + s;
        }
        Battle.drawNumber(g, s, this.drawx, drawy2, this.color, 33);
        if (this.atkType >= 2 && this.atkType <= 8) {
            int frame = this.atkType + 23;
            int i = this.drawx;
            if (this.src.getFaceto() == 1) {
                dx = this.drawx + ((s.length() * 11) >> 1) + 4;
            } else {
                dx = (this.drawx - ((s.length() * 11) >> 1)) - 24;
            }
            Battle.battleNum.drawFrame(g, frame, dx, drawy2, 0, 36);
        }
    }

    public void drawTextShow(Graphics g) {
        UI.drawDialoguePanel(g, this.drawx, this.drawy, this.w, this.h, Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], Tool.CLR_TABLE[21], 0);
        g.setColor(Tool.CLR_ITEM_WHITE);
        g.drawString(this.showText, this.drawx + 4, this.drawy + 4, 20);
    }

    public void paint(Graphics g) {
        switch (this.type) {
            case 3:
                drawTextShow(g);
                break;
            case 4:
                drawTextShow(g);
                break;
            case 6:
                drawFlyString(g);
                break;
            case 9:
                drawBattleResult(g);
                break;
        }
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        drawPlayers(g);
    }

    public static BattleMovie parseBattleMovie(byte[] data) {
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
            switch (dis.readByte()) {
                case 0:
                    return parseMoveToMovie(dis);
                case 1:
                    return parseAttackMovie(dis);
                case 2:
                    return parseMoveBackMovie(dis);
                case 3:
                    return parseUseSkillMovie(dis);
                case 4:
                    return parseUseItemMovie(dis);
                case 5:
                    return parseChangePropertyMovie(dis);
                case 6:
                    return parseFlyStringMovie(dis);
                case 7:
                    return parseHurtMovie(dis);
                case 8:
                    return parseDieMovie(dis);
                case 9:
                    return parseEndMovie(dis);
                case 10:
                    return parseRunawayMovie(dis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BattleMovie parseRunawayMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 10);
        bm.win = dis.readBoolean();
        return bm;
    }

    public static BattleMovie parseHurtMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 7);
        byte atkType2 = dis.readByte();
        byte effType = dis.readByte();
        bm.dialogue = dis.readUTF();
        bm.atkType = effType;
        bm.players.addElement(Battle.createHitPlayer(atkType2, effType, bm.src.getDir(), bm.src.getPixelX() + (bm.src.getWidth() / 2), bm.src.getPixelY() - (bm.src.getHeight() / 2)));
        return bm;
    }

    public static BattleMovie parseFlyStringMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 6);
        bm.changeValue = dis.readInt();
        bm.stateString = dis.readByte();
        bm.isCri = dis.readBoolean();
        bm.color = dis.readByte();
        bm.atkType = dis.readByte();
        if (STATE_JUMP[bm.stateString]) {
            bm.frameLength += 5;
            bm.showText = String.valueOf(bm.changeValue);
        }
        return bm;
    }

    public static BattleMovie parseChangePropertyMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 5);
        byte count = dis.readByte();
        bm.proChg = new byte[count];
        bm.proChgValue = new int[count];
        bm.proChgSpeed = new int[count];
        for (int i = 0; i < count; i++) {
            bm.proChg[i] = dis.readByte();
            bm.proChgValue[i] = dis.readInt();
            bm.proChgSpeed[i] = bm.proChgValue[i] / bm.frameLength;
        }
        return bm;
    }

    public static BattleMovie parseUseSkillMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 3);
        bm.showText = dis.readUTF();
        bm.w = Utilities.font.stringWidth(bm.showText) + 10;
        bm.h = Utilities.LINE_HEIGHT + 4;
        bm.drawx = (World.viewWidth - bm.w) / 2;
        bm.drawy = (World.viewHeight - bm.h) / 2;
        byte atkType2 = dis.readByte();
        byte effType = dis.readByte();
        bm.atkType = effType;
        bm.players.addElement(Battle.createCastPlayer(atkType2, effType, bm.src.getDir(), bm.src.getPixelX() + (bm.src.getWidth() / 2), bm.src.getPixelY() - (bm.src.getHeight() / 2)));
        return bm;
    }

    public static BattleMovie parseUseItemMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 4);
        readBattleMovieTarget(dis, bm);
        bm.showText = dis.readUTF();
        bm.w = Utilities.font.stringWidth(bm.showText) + 10;
        bm.h = Utilities.LINE_HEIGHT + 4;
        bm.drawx = (World.viewWidth - bm.w) / 2;
        bm.drawy = (World.viewHeight - bm.h) / 2;
        return bm;
    }

    public static BattleMovie parseMoveBackMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 2);
        return bm;
    }

    public static BattleMovie parseEndMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        bm.type = 9;
        bm.startFrame = dis.readShort();
        bm.frameLength = dis.readShort();
        bm.win = dis.readBoolean();
        bm.vx = 0;
        bm.vy = 0;
        bm.drawx = World.viewWidth >> 1;
        bm.drawy = World.viewHeight >> 1;
        return bm;
    }

    public static BattleMovie parseDieMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 8);
        bm.show = dis.readBoolean();
        bm.showDieImg = dis.readBoolean();
        bm.vx = 5;
        return bm;
    }

    public static BattleMovie parseAttackMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 1);
        readBattleMovieTarget(dis, bm);
        bm.atkType = dis.readByte();
        bm.isHit = dis.readBoolean();
        return bm;
    }

    public static BattleMovie parseMoveToMovie(DataInputStream dis) throws IOException {
        BattleMovie bm = new BattleMovie();
        readBattleMovieBaseInfo(dis, bm, 0);
        readBattleMovieTarget(dis, bm);
        return bm;
    }

    private static void readBattleMovieBaseInfo(DataInputStream dis, BattleMovie bm, int type2) throws IOException {
        bm.type = type2;
        bm.startFrame = dis.readShort();
        bm.frameLength = dis.readShort();
        bm.src = Battle.findBattleUnit(dis.readInt());
    }

    private static void readBattleMovieTarget(DataInputStream dis, BattleMovie bm) throws IOException {
        byte count = dis.readByte();
        bm.target = new Pet[count];
        for (int i = 0; i < count; i++) {
            bm.target[i] = Battle.findBattleUnit(dis.readInt());
        }
    }
}
