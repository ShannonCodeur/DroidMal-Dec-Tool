package com.sumsharp.lowui;

import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Npc;
import javax.microedition.lcdui.Graphics;

public class NpcUI extends UI {
    private int[] cmdIcons;
    private StringDraw context;
    private int height;
    private String[] items;
    private int keyHelpHeight;
    private int keyHelpSpeed;
    private int keyHelpWidth;
    private int keyHelpX;
    private int keyHelpY;
    private Menu menu;
    private Npc npc;
    private int npcId;
    private int npcLeft;
    private String npcName;
    private int npcNameHeight;
    private int npcNameWidth;
    private int npcNameY;
    private int npcNamex;
    private int npcStep;
    private boolean showKeyHelp = false;
    private int width;
    private int x;
    private int y;

    public NpcUI(int npcId2, String context2, String[] items2, String[] cmdIcons2) {
        this.npcId = npcId2;
        this.items = items2;
        this.width = World.viewWidth - 10;
        this.x = World.viewWidth;
        this.npcStep = this.width >> 2;
        this.npc = NewStage.getNpc(npcId2);
        int sw = this.width - 10;
        if (this.npc != null) {
            this.npcLeft = (sw - (this.npc.getWidth() + 10)) + 10;
        }
        this.y = NewStage.screenY + 15;
        this.height = (Utilities.LINE_HEIGHT * 3) + 10 + (Utilities.LINE_HEIGHT / 2);
        if (this.npc != null && this.height < this.npc.getHeight()) {
            this.height = this.npc.getHeight() + 10;
        }
        this.npcName = this.npc == null ? "" : this.npc.name;
        this.menu = new Menu(this.npcName, null, this.items, cmdIcons2, 8, true);
        this.npcNameHeight = 36;
        this.npcNameWidth = Utilities.font.stringWidth(this.npcName) + (this.npcNameHeight << 1);
        this.npcNamex = ((this.x + this.width) - this.npcNameWidth) + this.npcNameHeight;
        this.npcNameY = ((this.y + this.height) - (this.npcNameHeight >> 1)) + 1;
        this.menu.setY(this.npcNameY + this.npcNameHeight + 5);
        this.context = new StringDraw(context2, (this.width - 10) - (this.height >> 1), this.height - 10);
        if (this.context.getPageSize() > 1) {
            this.showKeyHelp = true;
            this.keyHelpHeight = Utilities.CHAR_HEIGHT;
            this.keyHelpWidth = 80;
            this.keyHelpX = World.uiWidth;
            this.keyHelpY = this.y + this.height + (this.keyHelpHeight >> 1);
            this.keyHelpSpeed = this.keyHelpWidth >> 1;
        }
    }

    public void cycle() {
        this.menu.cycle();
        if (this.closed) {
            this.menu.closed = true;
        }
        if (this.closed) {
            this.x += this.npcStep;
            this.npcNamex += this.npcStep;
            if (this.showKeyHelp) {
                this.keyHelpX += this.keyHelpSpeed;
            }
            if (this.x > World.viewWidth) {
                this.show = false;
            }
        } else if (this.show) {
            int px = World.pressedx;
            int py = World.pressedy;
            if (!(px == -1 || py == -1)) {
                if (py <= this.keyHelpY - 7 || py >= this.keyHelpY + 10) {
                    if (px < this.x || px > this.x + this.width || py < this.y || py > this.height + this.y) {
                        Utilities.keyPressed(7, true);
                    }
                } else if (px > this.keyHelpX && px < this.keyHelpX + 14) {
                    Utilities.keyPressed(3, true);
                } else if (px > this.keyHelpX + 17 && px < this.keyHelpX + 17 + 14) {
                    Utilities.keyPressed(4, true);
                }
            }
            if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
                this.context.prevPage();
            } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
                this.context.nextPage();
            }
        } else {
            this.x -= this.npcStep;
            this.npcNamex -= this.npcStep;
            if (this.showKeyHelp) {
                this.keyHelpX -= this.keyHelpSpeed;
            }
            if (this.width - (World.viewWidth - this.x) < this.npcStep) {
                this.x = World.viewWidth - this.width;
                this.npcNamex = ((this.x + this.width) - this.npcNameWidth) + this.npcNameHeight;
                this.show = true;
            }
        }
    }

    public int getMenuSelection() {
        return this.menu.getMenuSelection();
    }

    public void paint(Graphics g) {
        Graphics graphics = g;
        drawTalkPanel(graphics, this.x, this.npcNameHeight + this.y, this.width, this.height, true);
        int dy = this.y + 5 + this.npcNameHeight + (Utilities.LINE_HEIGHT / 2);
        this.context.drawShadow(g, this.x + 5 + (this.height >> 1), dy, 16776398, 0);
        if (this.npc != null) {
            this.npc.cartoonPlayer.draw(g, this.x + 12, ((this.y + this.height) - 7) + this.npcNameHeight);
        }
        drawTitlePanel(g, this.npcName, this.x + 3, this.y + (Utilities.LINE_HEIGHT / 2), this.npcNameWidth, this.npcNameHeight, 8);
        this.menu.paint(g);
        if (this.showKeyHelp) {
            drawPageBar(g, this.keyHelpX, this.keyHelpY, this.keyHelpWidth, this.keyHelpHeight, this.context.getPageNo() + 1, this.context.getPageSize(), 8);
        }
    }
}
