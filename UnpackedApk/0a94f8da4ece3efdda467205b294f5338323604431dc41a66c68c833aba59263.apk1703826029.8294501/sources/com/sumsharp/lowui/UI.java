package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import javax.microedition.lcdui.Graphics;

public abstract class UI {
    private static byte[] arrowState = new byte[2];
    private static int arrowX = -1;
    private static int arrowY = -1;
    private static int pagebarW = -1;
    protected boolean closed = false;
    protected int leftBtnSpeed;
    protected int leftBtnWidth;
    protected int leftBtnX;
    protected String leftCmd = null;
    protected boolean needsUpdate = true;
    protected int pointBtnY = -1;
    protected int rightBtnSpeed;
    protected int rightBtnWidth;
    protected int rightBtnX;
    protected String rightCmd = null;
    protected boolean show = false;

    public abstract void paint(Graphics graphics);

    public void cycle() {
        arrowState[0] = 0;
        arrowState[1] = 0;
        CornerButton.instance.cycle();
        if (this.show && arrowX != -1 && arrowY != -1 && pagebarW != -1) {
            if (World.moveY > arrowY && World.moveY < arrowY + 45) {
                if (World.moveX > arrowX && World.moveX < arrowX + 45) {
                    arrowState[0] = 1;
                    return;
                } else if (World.moveX > (arrowX + pagebarW) - 45 && World.moveX < arrowX + pagebarW) {
                    arrowState[1] = 1;
                    return;
                }
            }
            if (World.releasedy > arrowY && World.releasedy < arrowY + 45) {
                if (World.releasedx > arrowX && World.releasedx < arrowX + 45) {
                    Utilities.keyPressed(3, true);
                } else if (World.releasedx > (arrowX + pagebarW) - 45 && World.releasedx < arrowX + pagebarW) {
                    Utilities.keyPressed(4, true);
                }
            }
        }
    }

    public Object findData(int type, int id) {
        return null;
    }

    public int getX() {
        return 0;
    }

    public int getY() {
        return 0;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public void clear() {
    }

    public boolean isShow() {
        return this.show;
    }

    public void setCmd(String left, String right) {
        initCmd(left, right);
    }

    /* access modifiers changed from: protected */
    public void initCmd(String leftCmd2, String rightCmd2) {
        if (leftCmd2 != null) {
            this.leftBtnWidth = Utilities.font.stringWidth(leftCmd2) + 40;
        }
        if (rightCmd2 != null) {
            this.rightBtnWidth = Utilities.font.stringWidth(rightCmd2) + 40;
        }
        if (leftCmd2 != null && this.leftCmd == null) {
            this.leftBtnX = -this.leftBtnWidth;
            this.leftBtnSpeed = this.leftBtnWidth >> 1;
        }
        if (rightCmd2 != null && this.rightCmd == null) {
            this.rightBtnX = World.viewWidth + Utilities.CHAR_HEIGHT;
            this.rightBtnSpeed = this.rightBtnWidth >> 1;
        }
        this.leftCmd = leftCmd2;
        this.rightCmd = rightCmd2;
    }

    /* access modifiers changed from: protected */
    public void moveBtn() {
        if (this.leftCmd != null) {
            if (this.leftBtnX + this.leftBtnSpeed >= 0) {
                this.leftBtnX = 0;
            } else {
                this.leftBtnX += this.leftBtnSpeed;
            }
        }
        if (this.rightCmd == null) {
            return;
        }
        if (((this.rightBtnX + this.rightBtnWidth) - Utilities.CHAR_HEIGHT) - this.rightBtnSpeed <= World.viewWidth) {
            this.rightBtnX = (World.viewWidth - this.rightBtnWidth) + Utilities.CHAR_HEIGHT;
        } else {
            this.rightBtnX -= this.rightBtnSpeed;
        }
    }

    /* access modifiers changed from: protected */
    public void drawBtn(Graphics g, int y) {
        if (CommonComponent.getUIPanel().hasUI(Utilities.VMUI_CHAT)) {
            CornerButton.instance.paintUICmd(g, this.leftCmd, this.rightCmd, false);
        } else {
            CornerButton.instance.paintUICmd(g, this.leftCmd, this.rightCmd, true);
        }
    }

    public void reset() {
        this.show = false;
        this.closed = false;
        String lc = this.leftCmd;
        String rc = this.rightCmd;
        initCmd(null, null);
        initCmd(lc, rc);
    }

    public void close() {
        this.closed = true;
    }

    public boolean getClose() {
        return this.closed;
    }

    public boolean isClosed() {
        return !this.show && this.closed;
    }

    public void remove(int index) {
    }

    public int getMenuSelection() {
        return -1;
    }

    public void setItemStatus(boolean[] enable) {
    }

    public static void drawRadio(Graphics g, int x, int y, boolean sel, int dir, int worldtick) {
        int step = (worldtick % 6) / 2;
        if (sel) {
            Tool.uiMiscImg2.drawFrame(g, step + 31, x, y + 3, 0, 33);
            return;
        }
        g.setColor(Tool.CLR_TABLE[4]);
        g.fillRect(x - 3, y - 3, 6, 6);
        g.setColor(Tool.CLR_TABLE[11]);
        g.fillRect(x - 2, y - 3, 4, 6);
        g.fillRect(x - 3, y - 2, 6, 4);
        g.setColor(Tool.CLR_TABLE[4]);
        g.fillRect(x - 2, y - 2, 4, 4);
    }

    public static void drawRadio(Graphics g, int x, int y, boolean sel, int dir) {
        if (sel) {
            g.setColor(Tool.CLR_TABLE[0]);
            g.fillRect(x - 3, y - 3, 6, 6);
            g.fillRect(x - 4, y - 2, 8, 4);
            g.fillRect(x - 2, y - 4, 4, 8);
            g.setColor(Tool.CLR_TABLE[1]);
            g.fillRect(x - 2, y - 3, 4, 6);
            g.fillRect(x - 3, y - 2, 6, 4);
            g.setColor(Tool.CLR_TABLE[2]);
            g.fillRect(x - 1, y - 2, 2, 4);
            g.fillRect(x - 2, y - 1, 4, 2);
            g.setColor(Tool.CLR_TABLE[3]);
            g.fillRect(x - 1, y - 1, 2, 2);
            return;
        }
        g.setColor(Tool.CLR_TABLE[4]);
        g.fillRect(x - 3, y - 3, 6, 6);
        g.setColor(Tool.CLR_TABLE[11]);
        g.fillRect(x - 2, y - 3, 4, 6);
        g.fillRect(x - 3, y - 2, 6, 4);
        g.setColor(Tool.CLR_TABLE[4]);
        g.fillRect(x - 2, y - 2, 4, 4);
    }

    public static void drawPageBar(Graphics g, int x, int y, int w, int h, int pageNo, int pageSize, int dir) {
        if (arrowState[0] == -1) {
            g.drawImage(Tool.img_triArrowL[2], x, (h / 2) + y, 6);
        } else if (arrowState[0] == 0) {
            g.drawImage(Tool.img_triArrowL[0], x, (h / 2) + y, 6);
        } else if (arrowState[0] == 1) {
            g.drawImage(Tool.img_triArrowL[1], x, (h / 2) + y, 6);
        }
        if (arrowState[1] == -1) {
            g.drawImage(Tool.img_triArrowR[2], x + w, (h / 2) + y, 10);
        } else if (arrowState[1] == 0) {
            g.drawImage(Tool.img_triArrowR[0], x + w, (h / 2) + y, 10);
        } else if (arrowState[1] == 1) {
            g.drawImage(Tool.img_triArrowR[1], x + w, (h / 2) + y, 10);
        }
        int pagew = Tool.drawSmallNum(String.valueOf(pageNo) + "/" + pageSize, g, ((w / 2) + x) - 15, (h / 2) + y + 5, 33, -1);
        int freew = w - (Tool.img_triArrowL[0].getWidth() * 2);
        if (freew > pagew + 30) {
            Tool.drawAlphaBox(g, freew - 30, 20, (World.viewWidth / 2) - ((freew - 30) / 2), ((h / 2) + y) - 10, -1, true);
            Tool.drawSmallNum(String.valueOf(pageNo) + "/" + pageSize, g, ((w / 2) + x) - 15, (h / 2) + y + 5, 33, -1);
            Tool.drawBookAnimate(g, (((w / 2) + x) + pagew) - 10, (h / 2) + y + 5);
        }
        arrowX = x;
        arrowY = y;
        pagebarW = w;
    }

    public static void drawUIPanel(Graphics g, int x, int y, int width, int height, int dir, boolean alpha) {
        drawUIBorder(g, 0, x, y, width, height, 6, false, dir, alpha);
        if (dir == 4) {
            drawUIBorder(g, Tool.CLR_TABLE[12], x + 1, y + 1, width - 1, height - 2, 5, true, dir, alpha);
            drawUIBorder(g, Tool.CLR_TABLE[14], x + 2, y + 2, width - 2, height - 4, 5, false, dir, alpha);
            drawUIBorder(g, Tool.CLR_TABLE[15], x + 1, y + 1, width - 1, height - 2, 6, false, dir, alpha);
            return;
        }
        drawUIBorder(g, Tool.CLR_TABLE[12], x, y + 1, width - 1, height - 2, 5, true, dir, alpha);
        drawUIBorder(g, Tool.CLR_TABLE[14], x, y + 2, width - 2, height - 4, 5, false, dir, alpha);
        drawUIBorder(g, Tool.CLR_TABLE[15], x, y + 1, width - 1, height - 2, 6, false, dir, alpha);
    }

    public static void drawUIPanel(Graphics g, int x, int y, int width, int height, int dir) {
        Tool.drawAlphaBox(g, width, height, x, y, -1, true);
    }

    public static void drawUIBorder(Graphics g, int color, int x, int y, int width, int height, int corner, boolean fill, int dir, boolean alpha) {
        int color2;
        int color3;
        if (dir == 4) {
            g.setColor(color);
            g.drawLine(x + 1, y, x + width, y);
            g.drawLine(x, y + 1, x, (y + height) - corner);
            g.drawLine(x + corner, y + height, x + width, y + height);
            g.drawLine(x + 1, ((y + height) - corner) + 2, (x + corner) - 2, (y + height) - 1);
            g.drawLine(x + 1, ((y + height) - corner) + 1, x + 1, ((y + height) - corner) + 2);
            g.drawLine((x + corner) - 2, (y + height) - 1, (x + corner) - 1, (y + height) - 1);
            if (fill) {
                if (alpha) {
                    color3 = color | -536870912;
                } else {
                    color3 = color | -16777216;
                }
                Tool.fillAlphaRect(g, color3, x, y, width + 1, (height - corner) + 1);
                Tool.fillAlphaRect(g, color3, x + 2, ((y + height) - corner) + 1, width - 1, 1);
                for (int i = 0; i < corner - 3; i++) {
                    Tool.fillAlphaRect(g, color3, x + 2 + i, ((y + height) - corner) + 2 + i, width - 1, 1);
                }
                return;
            }
            return;
        }
        g.setColor(color);
        g.drawLine(x, y, (x + width) - 1, y);
        g.drawLine(x + width, y + 1, x + width, (y + height) - corner);
        g.drawLine(x, y + height, (x + width) - corner, y + height);
        g.drawLine((x + width) - 1, ((y + height) - corner) + 2, ((x + width) - corner) + 2, (y + height) - 1);
        g.drawLine((x + width) - 1, ((y + height) - corner) + 1, (x + width) - 1, ((y + height) - corner) + 2);
        g.drawLine(((x + width) - corner) + 2, (y + height) - 1, ((x + width) - corner) + 1, (y + height) - 1);
        if (fill) {
            if (alpha) {
                color2 = color | -536870912;
            } else {
                color2 = color | -16777216;
            }
            Tool.fillAlphaRect(g, color2, x, y, width + 1, (height - corner) + 1);
            Tool.fillAlphaRect(g, color2, x, ((y + height) - corner) + 1, width - 1, 1);
            g.setColor(color2);
            for (int i2 = 0; i2 < corner - 3; i2++) {
                Tool.fillAlphaRect(g, color2, x, ((y + height) - corner) + 2 + i2, (width - 1) - i2, 1);
            }
        }
    }

    public static void drawRectPanel(Graphics g, int x, int y, int width, int height) {
        Tool.drawAlphaBox(g, width, height, x, y, -1, true);
    }

    public static void drawRectPanel(Graphics g, int x, int y, int width, int height, boolean alpha) {
        Tool.drawAlphaBox(g, width, height, x, y, -1, alpha);
    }

    public static void drawRectPanel(Graphics g, int x, int y, int width, int height, int bgClr, boolean alpha) {
        int clr;
        g.setColor(Tool.CLR_TABLE[16]);
        g.drawRect(x, y, width, height);
        g.setColor(Tool.CLR_TABLE[15]);
        g.drawRect(x + 1, y + 1, width - 2, height - 2);
        g.setColor(Tool.CLR_TABLE[14]);
        g.drawRect(x + 2, y + 2, width - 4, height - 4);
        g.setColor(Tool.CLR_TABLE[13]);
        g.drawRect(x + 3, y + 3, width - 6, height - 6);
        int clr2 = bgClr;
        if (alpha) {
            clr = clr2 | -268435456;
        } else {
            clr = clr2 | -16777216;
        }
        Tool.fillAlphaRect(g, clr, x + 4, y + 4, width - 7, height - 7);
    }

    public static void drawDialoguePanel(Graphics g, int x, int y, int width, int height, int borderClr, int lightBorderClr, int fillClr, int point) {
        drawDialoguePanel(g, x, y, 10, width, height, borderClr, lightBorderClr, fillClr, point);
    }

    public static void drawDialoguePanel(Graphics g, int x, int y, int triangleX, int width, int height, int borderClr, int lightBorderClr, int fillClr, int point) {
        if (point == 16) {
            y += 5;
        } else if (point == 32) {
            y -= 5;
        }
        Tool.drawAlphaBox(g, width, height, x, y, -1, true);
        if (point == 16) {
            g.setAlphaValue(136);
            g.setColor(7214);
            g.fillTriangle(x + triangleX, y + 2, x + triangleX + 7, y - 5, x + triangleX + 13, y + 2);
            g.setAlphaValue(AbstractUnit.CLR_NAME_TAR);
            g.setColor(2892826);
            g.drawLine(x + triangleX + 1, y, x + triangleX + 6, y - 5);
            g.drawLine(x + triangleX + 12, y, x + triangleX + 7, y - 5);
            g.setColor(15983532);
            g.drawLine(x + triangleX + 2, y + 1, x + triangleX + 7, y - 4);
            g.drawLine(x + triangleX + 13, y + 1, x + triangleX + 8, y - 4);
        } else if (point == 32) {
            g.setAlphaValue(136);
            g.setColor(7214);
            g.fillTriangle(x + triangleX, (y + height) - 2, x + triangleX + 7, y + height + 5, x + triangleX + 13, (y + height) - 2);
            g.setAlphaValue(AbstractUnit.CLR_NAME_TAR);
            g.setColor(borderClr);
            g.drawLine(x + triangleX + 1, y + height, x + triangleX + 6, y + height + 5);
            g.drawLine(x + triangleX + 12, y + height, x + triangleX + 7, y + height + 4);
            g.setColor(borderClr);
            g.drawLine(x + triangleX + 2, y + height + 1, x + triangleX + 7, y + height + 6);
            g.drawLine(x + triangleX + 13, y + height + 1, x + triangleX + 8, y + height + 5);
        }
    }

    public static void drawRectTipPanel(Graphics g, int x, int y, int width, int height, boolean alpha) {
        Tool.drawAlphaBox(g, width, height, x, y, 1, true);
    }

    public static void drawSmallTip(Graphics g, int x, int y, int width, int height, int clrType) {
        int x2 = x - height;
        int width2 = width + height;
        g.setColor(Tool.CLR_TABLE[6]);
        g.fillRoundRect(x2, y, width2, height, height, height);
        g.setColor(Tool.CLR_TABLE[4]);
        g.drawRoundRect(x2, y, width2, height, height, height);
        g.setColor(Tool.CLR_TABLE[8]);
        g.drawRoundRect(x2 + 1, y + 1, width2 - 2, height - 2, height - 2, height - 2);
        g.setColor(Tool.CLR_TABLE[6]);
        g.drawRoundRect(x2 + 2, y + 2, width2 - 4, height - 4, height - 4, height - 4);
    }

    public static void drawSmallPanel(Graphics g, int x, int y, int width, int height, int borderClr, int fillClr) {
        g.setColor(fillClr);
        g.fillRoundRect(x, y, width, height, height, height);
        g.setColor(borderClr);
        g.drawRoundRect(x, y, width, height, height, height);
    }

    public static void drawTalkPanel(Graphics g, int x, int y, int width, int height) {
        int height2 = Tool.img_arcL.getHeight();
        g.drawImage(Tool.img_arcL, x, y);
        g.drawImage(Tool.img_arcR, x + width, y, 8);
        g.drawLine(Tool.img_arcL.getWidth() + x, y, (x + width) - Tool.img_arcL.getWidth(), y);
        Tool.drawBorderLine(g, Tool.img_arcL.getWidth() + x, y, width - (Tool.img_arcL.getWidth() * 2), 0);
        Tool.drawBorderLine(g, Tool.img_arcL.getWidth() + x, (y + height2) - 5, width - (Tool.img_arcL.getWidth() * 2), 1);
        Tool.fillAlphaRect(g, -872408018, x + Tool.img_arcL.getWidth(), y + 5, width - (Tool.img_arcL.getWidth() * 2), height2 - 10);
    }

    public static void drawTalkPanel(Graphics g, int x, int y, int width, int height, boolean forNpcUI) {
        if (forNpcUI) {
            drawTalkPanel(g, x, y, width, height);
        } else {
            drawTalkPanel(g, x, y, width, height);
        }
    }

    private static void drawTitlePanelBorder(Graphics g, int x, int y, int width, int height, int dir, int clr, boolean fill) {
        g.setColor(clr);
        if (dir == 4) {
            int corLength = height >> 1;
            g.setClip(x, y, width + 1, height);
            g.drawLine(x, y, x + corLength, y);
            int drawLen = corLength;
            int i = 0;
            while (true) {
                if (i > corLength) {
                    break;
                }
                int dy = y;
                int dyt = (i * 2) + y;
                if (!fill) {
                    dy += i * 2;
                    dyt = dy + 1;
                }
                g.drawLine(x + i, dy, x + i, dyt);
                if (dyt - y > height) {
                    drawLen = i;
                    break;
                }
                i++;
            }
            g.setClip(x + drawLen, y, (width - drawLen) + 2, height + 1);
            if (fill) {
                g.fillRoundRect(x - height, y, width + height, height, height, height);
                int y2 = i;
                int x2 = drawLen;
                int i2 = corLength;
                return;
            }
            g.drawRoundRect(x - height, y, width + height, height, height, height);
            int y3 = i;
            int x3 = drawLen;
            int i3 = corLength;
            return;
        }
        int corLength2 = height >> 1;
        g.setClip(x, y, width + 1, height);
        g.drawLine(x + width, y, (x + width) - corLength2, y);
        int drawLen2 = corLength2;
        int i4 = 0;
        while (true) {
            if (i4 > corLength2) {
                break;
            }
            int dy2 = y;
            int dyt2 = (i4 * 2) + y;
            if (!fill) {
                dy2 += i4 * 2;
                dyt2 = dy2 + 1;
            }
            g.drawLine((x + width) - i4, dy2, (x + width) - i4, dyt2);
            if (dyt2 - y > height) {
                drawLen2 = i4;
                break;
            }
            i4++;
        }
        g.setClip(x, y, width - drawLen2, height + 1);
        if (fill) {
            g.fillRoundRect(x, y, width + height, height, height, height);
            int y4 = i4;
            int x4 = drawLen2;
            int i5 = corLength2;
            return;
        }
        g.drawRoundRect(x, y, width + height, height, height, height);
        int y5 = i4;
        int x5 = drawLen2;
        int i6 = corLength2;
    }

    public static void drawTitlePanel(Graphics g, String str, int x, int y, int width, int height, int dir) {
        int i = Tool.CLR_TABLE[11];
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        Tool.drawAlphaBox(g, width, height, x, y, -436200402);
        int i2 = (height >> 1) + x + 1;
        Graphics graphics = g;
        String str2 = str;
        Tool.drawLevelString(graphics, str2, x + 8, (y + height) - 10, 36, 1, 0);
    }

    public static void drawTipMsgBox(Graphics g, String msg, int x, int y) {
        int width = World.viewWidth - (x * 2);
        StringDraw sd = new StringDraw(msg, width - 5, -1);
        drawRectTipPanel(g, x, y, width, (Utilities.LINE_HEIGHT * sd.length()) + 4, true);
        sd.draw(g, x + 4, y + 2, Tool.CLR_TABLE[10]);
    }
}
