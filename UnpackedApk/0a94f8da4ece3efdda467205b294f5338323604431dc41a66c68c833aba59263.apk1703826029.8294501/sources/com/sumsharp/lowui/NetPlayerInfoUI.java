package com.sumsharp.lowui;

import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.common.data.NetPlayer;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.ImageSet;
import javax.microedition.lcdui.Graphics;

public class NetPlayerInfoUI extends UI {
    private NetPlayer np;
    private int petAniTop;
    private int petHeight;
    private ImageSet petIcon = ImageLoadManager.getImage("petIcons.jgp");
    private int petSpeed;
    private int petTop;
    private int playerAniTop;
    private int playerHeight;
    private int playerSpeed;
    private int playerTop;
    private int width;
    private int x;

    public NetPlayerInfoUI(int id) {
        this.np = NewStage.getNetPlayer(id);
        this.playerAniTop = NewStage.screenY + 5;
        this.petHeight = Utilities.LINE_HEIGHT * 4;
        this.playerHeight = (((World.viewHeight - this.playerAniTop) - Chat.chatHeight) - this.petHeight) - (Utilities.LINE_HEIGHT >> 1);
        this.petAniTop = this.playerAniTop + this.playerHeight;
        this.playerTop = -this.playerHeight;
        this.petTop = World.viewHeight;
        this.playerSpeed = (this.playerAniTop - this.playerTop) / 2;
        this.petSpeed = (this.petTop - this.petAniTop) / 2;
        this.x = 3;
        this.width = World.viewWidth - 6;
    }

    public void cycle() {
        super.cycle();
        if (this.closed) {
            this.playerTop -= this.playerSpeed;
            this.petTop += this.petSpeed;
            if (this.petTop > World.viewHeight) {
                this.show = false;
            }
        } else if (this.show) {
            this.np.battlePet.doCycle();
            moveBtn();
        } else {
            this.playerTop += this.playerSpeed;
            this.petTop -= this.petSpeed;
            if (this.playerTop > this.playerAniTop) {
                this.playerTop = this.playerAniTop;
                this.petTop = this.petAniTop;
                this.show = true;
            }
        }
    }

    public void paint(Graphics g) {
        String str;
        String str2;
        String str3;
        int win;
        int clr;
        String str4;
        drawRectPanel(g, this.x, this.playerTop, this.width, this.playerHeight);
        drawRectPanel(g, this.x, this.petTop, this.width, this.petHeight);
        Tool.drawLevelString(g, this.np.name, this.x + 5, this.playerTop + 5, 20, 1, 0);
        Tool.drawLevelString(g, " 参战灵兽", this.x + 5, this.petTop + 5, 20, 1, 0);
        if (this.show) {
            int dy = this.playerTop + 15 + 20;
            this.np.cartoonPlayer.draw(g, this.x + 5, this.np.getHeight() + dy);
            int dx = this.np.getWidth() + this.x + 10;
            int strWidth = Utilities.font.stringWidth("等级 ");
            drawSmallPanel(g, dx, (Utilities.LINE_HEIGHT + dy) - 5, Tool.getSmallNumWidth(String.valueOf(this.np.level)) + strWidth + 10, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
            Tool.draw3DString(g, "等级 ", dx + 5, dy + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
            Tool.drawNumStr(String.valueOf(this.np.level), g, dx + 5 + strWidth, dy + Utilities.LINE_HEIGHT, 0, 36, -1);
            int dy2 = dy + Utilities.LINE_HEIGHT + 5;
            if (this.np.honorTitle == null) {
                str = String.valueOf("军衔 ") + "读取中...";
            } else {
                str = String.valueOf("军衔 ") + this.np.honorTitle;
            }
            int strWidth2 = Utilities.font.stringWidth(str) + 10;
            drawSmallPanel(g, dx, (Utilities.LINE_HEIGHT + dy2) - 5, strWidth2, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
            Tool.draw3DString(g, str, dx + 5, dy2 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
            if (this.np.rank > 0) {
                Tool.rank.drawFrame(g, this.np.rank - 1, dx + strWidth2 + 10, Utilities.LINE_HEIGHT + dy2 + 5, 0, 36);
            }
            int dy3 = this.playerTop + 15 + 20 + this.np.getHeight() + 10;
            int dx2 = this.x + 5;
            if (this.np.arenaWin == -1) {
                str2 = String.valueOf("胜 ") + "读取中...";
            } else {
                str2 = "胜 ";
            }
            int strWidth3 = Utilities.font.stringWidth(str2);
            drawSmallPanel(g, dx2, (Utilities.LINE_HEIGHT + dy3) - 5, strWidth3 + 50, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
            Tool.draw3DString(g, str2, dx2 + 5, dy3 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
            if (this.np.arenaWin != -1) {
                Tool.drawNumStr(String.valueOf(this.np.arenaWin), g, dx2 + 5 + strWidth3, dy3 + Utilities.LINE_HEIGHT, 0, 36, -1);
            }
            int dx3 = dx2 + (this.width >> 1) + 5;
            if (this.np.arenaLose == -1) {
                str3 = String.valueOf("败 ") + "读取中...";
            } else {
                str3 = "败 ";
            }
            int strWidth4 = Utilities.font.stringWidth(str3);
            drawSmallPanel(g, dx3, (Utilities.LINE_HEIGHT + dy3) - 5, strWidth4 + 50, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
            Tool.draw3DString(g, str3, dx3 + 5, dy3 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
            if (this.np.arenaWin != -1) {
                Tool.drawNumStr(String.valueOf(this.np.arenaLose), g, dx3 + 5 + strWidth4, dy3 + Utilities.LINE_HEIGHT, 0, 36, -1);
            }
            int dx4 = this.x + 5;
            int dy4 = dy3 + Utilities.LINE_HEIGHT + 5;
            int all = this.np.arenaLose + this.np.arenaWin;
            if (all == 0) {
                win = 0;
            } else {
                win = (this.np.arenaWin * 100) / all;
            }
            Tool.draw3DString(g, "获胜率 " + win + "%", dx4 + 5, dy4 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
            int dy5 = dy4 + Utilities.LINE_HEIGHT + 3;
            g.setColor(10296);
            g.drawRect(dx4, dy5 + 3, 101, 7);
            if (win > 0) {
                g.setColor(15245456);
                g.fillRect(dx4 + 1, dy5 + 4, win, 6);
            }
            if (!this.np.battlePet.imageUpdate && this.np.battlePet.getIconID() != -1) {
                ImageLoadManager.requestImage(this.np.battlePet.getIconID(), this.np.battlePet);
            }
            int dx5 = dx4 + DirectionPad.instance.getWidth();
            this.np.battlePet.cartoonPlayer.draw(g, dx5, this.petTop + 60);
            int dx6 = dx5 + this.np.battlePet.getWidth() + 10;
            int dy6 = this.petTop + 5;
            if ((World.tick % 7) / 5 == 0) {
                clr = 16776960;
            } else {
                clr = Tool.CLR_ITEM_WHITE;
            }
            Tool.draw3DString(g, "点击查看详情", dx6 + 180, dy6 + 4, clr, 0);
            int dy7 = dy6 + Utilities.LINE_HEIGHT + 5;
            if (this.np.battlePet.name == null || this.np.battlePet.name.equals("")) {
                str4 = String.valueOf("名称 ") + "读取中...";
            } else {
                str4 = String.valueOf("名称 ") + this.np.battlePet.name;
            }
            drawSmallPanel(g, dx6, (Utilities.LINE_HEIGHT + dy7) - 5, Utilities.font.stringWidth(str4) + 10, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
            Tool.draw3DString(g, str4, dx6 + 5, dy7 + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
            int dy8 = dy7 + Utilities.LINE_HEIGHT;
            int dx7 = this.x + this.np.battlePet.getWidth() + 15;
            Tool.uiMiscImg.drawFrame(g, 32, dx7, dy8 + Utilities.LINE_HEIGHT, 0, 36);
            int dx8 = dx7 + 18;
            Tool.drawNumStr(String.valueOf(this.np.battlePetLevel), g, dx8, dy8 + Utilities.LINE_HEIGHT, 0, 36, -1);
            int dx9 = dx8 + Tool.getSmallNumWidth(String.valueOf(this.np.battlePetLevel)) + 3;
            String str5 = "+" + this.np.battlePet.matingTimes;
            Tool.drawNumStr(str5, g, dx9, dy8 + Utilities.LINE_HEIGHT, 0, 36, -1);
            int dx10 = dx9 + Tool.getSmallNumWidth(str5) + 3;
            int q = this.np.battlePet.getAttribute(58);
            int w = Tool.uiMiscImg.getFrameWidth(43);
            int dc = q + 1;
            for (int i = 0; i <= dc; i++) {
                int trans = 0;
                if (i % 2 != 0) {
                    trans = 2;
                }
                Tool.uiMiscImg.drawFrame(g, 43, dx10, dy8 + Utilities.LINE_HEIGHT, trans, 36);
                dx10 += w;
            }
        }
    }
}
