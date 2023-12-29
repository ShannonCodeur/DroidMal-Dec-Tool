package com.sumsharp.monster.ui;

import com.sumsharp.lowui.MovingString;
import com.sumsharp.lowui.StringDraw;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.gtvm.GTVM;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class MessageDialogue {
    public static final byte BUTTON_ALL = 7;
    public static final byte BUTTON_CANCEL = 4;
    public static final byte BUTTON_NO = 2;
    public static final byte BUTTON_NONE = 0;
    public static final String[] BUTTON_TEXT_DEFAULT = {"是", "否", "取消"};
    public static final byte BUTTON_YES = 1;
    public static final byte BUTTON_YESNO = 3;
    private static int CLOSE_DISABLE = 0;
    private static int CLOSE_ON_ANY_KEY = 1;
    private static int CLOSE_ON_BACK_KEY = 2;
    public static int TXT_INDENT_X = 5;
    public static int TXT_INDENT_Y = 5;
    private byte alignType;
    int arrowX;
    int arrowY;
    int backBtnX;
    int backBtnY;
    public int btnSpace;
    private boolean[] btnState;
    public int btnWidth;
    public int btnx;
    public int btny;
    public byte buttonSelect;
    public String[] buttonText;
    public byte[] buttons;
    public int closeMode;
    private boolean closed;
    private Font font;
    public boolean handleArrowKey;
    public boolean handleFire;
    private int icon;
    public IMessageDialogKeyHandler keyHandler;
    private int lineHeight;
    private int lineShowFirst;
    private int lineShowNum;
    private GTVM listenVM;
    private boolean modal;
    private String name;
    private boolean show;
    private StringDraw sw;
    public String text;
    private long timeout;
    private MovingString title;
    private int width;
    private int x;
    private int xscale;
    private int xstep;
    private int y;
    private int yscale;
    private int ystep;

    public MessageDialogue(String title2, int icon2, String str, int mx, int my, int itsn, int width2, int lineHeight2, Font fn, byte buttons2, String[] buttonText2) {
        this(str, mx, my, itsn, width2, lineHeight2, fn);
        this.icon = icon2;
        this.text = str;
        this.width = width2;
        MovingString movingString = new MovingString(title2, this.icon != -1 ? (this.width - TXT_INDENT_X) - ((Tool.uiMiscImg.getFrameWidth(this.icon) + 2) << 1) : titleWidth, 2);
        this.title = movingString;
        int btnCount = getButtonCount(buttons2);
        this.buttons = new byte[btnCount];
        this.buttonText = new String[btnCount];
        int idx = 0;
        this.btnWidth = 0;
        for (int i = 0; i < 3; i++) {
            if (((buttons2 >> i) & 1) != 0) {
                this.buttons[idx] = (byte) (1 << i);
                if (buttonText2 == null || idx >= buttonText2.length) {
                    this.buttonText[idx] = BUTTON_TEXT_DEFAULT[i];
                } else {
                    this.buttonText[idx] = buttonText2[idx];
                }
                this.btnWidth += Tool.img_rectbtn0.getWidth();
                idx++;
            }
        }
        for (int i2 = 0; i2 < this.buttonText.length; i2++) {
            int w = 45;
            w = 45 < 35 ? 35 : w;
            if (w > this.btnWidth) {
                this.btnWidth = w;
            }
        }
        this.x = ((Utilities.uiWidth - getWidth()) * mx) / 100;
        this.y = ((Utilities.uiHeight - getHeight()) * my) / 100;
        this.btnSpace = 5;
        this.btnWidth += (Tool.img_rectbtn0.getWidth() + this.btnSpace) * (btnCount - 1);
        this.btnx = ((this.width - this.btnWidth) >> 1) + this.x;
        this.btny = ((Utilities.uiY + this.y) + getHeight()) - 2;
        this.buttonSelect = (byte) (btnCount - 1);
        this.btnState = new boolean[2];
    }

    public int getButtonCount(byte buttons2) {
        int btnCount = 0;
        if ((buttons2 & 1) != 0) {
            btnCount = 0 + 1;
        }
        if ((buttons2 & 2) != 0) {
            btnCount++;
        }
        if ((buttons2 & 4) != 0) {
            return btnCount + 1;
        }
        return btnCount;
    }

    public MessageDialogue(String str, int mx, int my, int itsn, int width2, int lineHeight2, Font fn) {
        int i;
        this.font = Utilities.font;
        this.name = null;
        this.show = false;
        this.icon = -1;
        this.xstep = 0;
        this.ystep = 0;
        this.xscale = 0;
        this.yscale = 0;
        this.modal = true;
        this.closeMode = CLOSE_DISABLE;
        this.handleArrowKey = true;
        this.handleFire = false;
        this.buttonSelect = 0;
        this.backBtnY = -1;
        this.backBtnX = -1;
        this.arrowY = -1;
        this.arrowX = -1;
        if (str != null) {
            this.text = str;
            if (fn != null) {
                this.font = fn;
            }
            this.width = width2;
            this.lineHeight = lineHeight2;
            this.lineShowFirst = 0;
            this.sw = new StringDraw(str, width2 - (TXT_INDENT_X << 1), lineHeight2 * itsn);
            if (this.sw.length() == 1) {
                int linew = Utilities.font.stringWidth(this.sw.getLineString(0)) + (TXT_INDENT_X << 1) + 10;
                int titlew = (TXT_INDENT_X * 2) + 88;
                if (linew > titlew) {
                    i = linew;
                } else {
                    i = titlew;
                }
                this.width = i;
            }
            this.lineShowNum = itsn > this.sw.length() ? this.sw.length() : itsn;
            this.x = ((Utilities.uiWidth - getWidth()) * mx) / 100;
            this.y = ((Utilities.uiHeight - getHeight()) * my) / 100;
            this.alignType = 0;
            this.xstep = width2 / 3;
            this.ystep = (getHeight() + lineHeight2) / 3;
        }
    }

    public MessageDialogue(String title2, int icon2, String str, int mx, int my, int itsn, int width2, int lineHeight2, Font fn) {
        int i;
        this.font = Utilities.font;
        this.name = null;
        this.show = false;
        this.icon = -1;
        this.xstep = 0;
        this.ystep = 0;
        this.xscale = 0;
        this.yscale = 0;
        this.modal = true;
        this.closeMode = CLOSE_DISABLE;
        this.handleArrowKey = true;
        this.handleFire = false;
        this.buttonSelect = 0;
        this.backBtnY = -1;
        this.backBtnX = -1;
        this.arrowY = -1;
        this.arrowX = -1;
        if (str != null) {
            this.text = str;
            if (fn != null) {
                this.font = fn;
            }
            this.title = new MovingString(title2, width2 - TXT_INDENT_X, 2);
            this.icon = icon2;
            this.width = width2;
            this.lineHeight = lineHeight2;
            this.lineShowFirst = 0;
            this.sw = new StringDraw(str, width2 - (TXT_INDENT_X << 1), lineHeight2 * itsn);
            if (this.sw.length() == 1) {
                int linew = Utilities.font.stringWidth(this.sw.getLineString(0)) + (TXT_INDENT_X << 1) + 10;
                int titlew = (TXT_INDENT_X * 2) + 88;
                if (linew > titlew) {
                    i = linew;
                } else {
                    i = titlew;
                }
                this.width = i;
            }
            this.lineShowNum = itsn > this.sw.length() ? this.sw.length() : itsn;
            this.x = ((Utilities.uiWidth - getWidth()) * mx) / 100;
            this.y = ((Utilities.uiHeight - getHeight()) * my) / 100;
            this.alignType = 0;
            this.xstep = width2 / 3;
            this.ystep = (getHeight() + lineHeight2) / 3;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public void setAlignType(byte atp) {
        this.alignType = atp;
    }

    private int getWidth() {
        return this.width;
    }

    private int getHeight() {
        int height = (this.lineHeight * this.lineShowNum) + TXT_INDENT_Y;
        if (this.buttons != null) {
            height += Tool.img_rectbtn0.getHeight() + 5;
        }
        if (this.sw.getPageSize() > 1 || (this.closeMode == CLOSE_ON_BACK_KEY && this.buttons == null)) {
            return height + Tool.img_rectbtn0.getHeight();
        }
        return height;
    }

    public void setKeyHandler(IMessageDialogKeyHandler keyHandler2) {
        this.keyHandler = keyHandler2;
    }

    public void cycle() {
        boolean z;
        if (!this.show && !this.closed) {
            this.xscale += this.xstep;
            this.yscale += this.ystep;
            if (this.xscale > this.width || this.yscale > getHeight() + this.lineHeight) {
                this.show = true;
            } else if (this.xscale < 0 || this.yscale < 0) {
                innerClose();
            }
        }
        if (!this.closed) {
            try {
                if (this.timeout == 0 || System.currentTimeMillis() <= this.timeout) {
                    this.btnState = new boolean[2];
                    handleMove();
                    handleRelease();
                    if (this.handleArrowKey) {
                        if (this.sw.getPageSize() > this.sw.getPageNo() + 1) {
                            Utilities.isKeyPressed(10, true);
                        }
                        if (this.buttons == null) {
                            if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
                                this.sw.prevPage();
                                if (this.keyHandler != null) {
                                    this.keyHandler.keyPressed(2);
                                }
                            } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
                                this.sw.nextPage();
                                if (this.keyHandler != null) {
                                    this.keyHandler.keyPressed(3);
                                }
                            } else if (Utilities.isKeyPressed(16, true) || Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(9, true)) {
                                if (this.sw.getPageNo() != this.sw.getPageSize() - 1 || this.closeMode == CLOSE_DISABLE) {
                                    this.sw.nextPage();
                                } else {
                                    innerClose();
                                }
                            }
                        } else if (!Utilities.isKeyPressed(0, true)) {
                            Utilities.isKeyPressed(1, true);
                        }
                    }
                    if (this.buttons != null) {
                        if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
                            this.buttonSelect = (byte) (this.buttonSelect - 1);
                            if (this.buttonSelect < 0) {
                                this.buttonSelect = (byte) (this.buttons.length - 1);
                            }
                        } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
                            this.buttonSelect = (byte) (this.buttonSelect + 1);
                            if (this.buttonSelect >= this.buttons.length) {
                                this.buttonSelect = 0;
                            }
                        }
                        if (Utilities.isKeyPressed(9, true)) {
                            if (this.keyHandler != null) {
                                this.keyHandler.keyPressed(9);
                            }
                            innerClose();
                        }
                        if (Utilities.isKeyPressed(10, true)) {
                            if (this.keyHandler != null) {
                                this.keyHandler.keyPressed(10);
                            }
                            this.buttonSelect = (byte) (this.buttons.length - 1);
                            innerClose();
                        }
                        if (Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(16, true)) {
                            innerClose();
                            if (this.keyHandler != null) {
                                this.keyHandler.keyPressed(4);
                            }
                        }
                    }
                    if (this.keyHandler != null) {
                        if (Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(16, true)) {
                            this.keyHandler.keyPressed(4);
                        }
                        if (Utilities.isKeyPressed(9, true)) {
                            this.keyHandler.keyPressed(9);
                            close();
                        }
                    }
                    if (this.closeMode == CLOSE_ON_ANY_KEY) {
                        if (!(World.pressedx == -1 || World.pressedy == -1)) {
                            Utilities.keyPressed(5, true);
                        }
                        if (Utilities.isAnyKeyPressed()) {
                            close();
                        }
                    }
                    if (this.closeMode != CLOSE_ON_BACK_KEY || !Utilities.isKeyPressed(10, true)) {
                        if (this.title != null) {
                            this.title.cycle();
                        }
                        if (this.modal) {
                            Utilities.clearKeyStates();
                            if (World.pressedx != -1 || World.pressedy != -1) {
                                World.pressedy = -1;
                                World.pressedx = -1;
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (this.keyHandler != null) {
                        this.keyHandler.keyPressed(10);
                    }
                    close();
                } else {
                    close();
                }
                if (!z) {
                    return;
                }
            } finally {
                if (this.modal) {
                    Utilities.clearKeyStates();
                    if (!(World.pressedx == -1 && World.pressedy == -1)) {
                        World.pressedy = -1;
                        World.pressedx = -1;
                    }
                }
            }
        }
    }

    private void handleMove() {
        if (this.buttons == null) {
            if (World.moveY > this.arrowY - (Tool.img_triArrowL[0].getHeight() / 2) && World.moveY < this.arrowY + (Tool.img_triArrowL[0].getHeight() / 2)) {
                if (World.moveX > this.x && World.moveX < this.x + Tool.img_triArrowL[0].getWidth()) {
                    this.btnState[0] = true;
                } else if (World.pressedx > (this.x + this.width) - Tool.img_triArrowL[0].getWidth() && World.pressedx < this.x + this.width) {
                    this.btnState[1] = true;
                }
            }
            if (this.backBtnY != -1) {
                int w = Tool.img_rectbtn0.getWidth();
                if (World.moveY > this.backBtnY && World.moveY < this.backBtnY + Tool.img_rectbtn0.getHeight() && World.moveX > (World.viewWidth / 2) - (w / 2) && World.moveX < (World.viewWidth / 2) + (w / 2)) {
                    this.btnState[0] = true;
                    return;
                }
                return;
            }
            return;
        }
        int by = this.btny + 32;
        if (World.moveY > by && World.moveY < Tool.img_rectbtn0.getHeight() + by) {
            if (World.moveX <= this.x + 5 || World.moveX >= this.x + 5 + Tool.img_rectbtn0.getWidth()) {
                if (World.moveX > ((this.x + this.width) - Tool.img_rectbtn0.getWidth()) - 5 && World.moveX < (this.x + this.width) - 5) {
                    this.btnState[1] = true;
                    return;
                }
                return;
            }
            this.btnState[0] = true;
        }
    }

    private void handleRelease() {
        if (this.buttons == null) {
            if (World.releasedy > this.arrowY - (Tool.img_triArrowL[0].getHeight() / 2) && World.releasedy < this.arrowY + (Tool.img_triArrowL[0].getHeight() / 2)) {
                if (World.releasedx > this.x && World.releasedx < this.x + Tool.img_triArrowL[0].getWidth()) {
                    Utilities.keyPressed(3, true);
                } else if (World.releasedx > (this.x + this.width) - Tool.img_triArrowL[0].getWidth() && World.releasedx < this.x + this.width) {
                    Utilities.keyPressed(4, true);
                }
            }
            if (this.backBtnY != -1) {
                int w = Tool.img_rectbtn0.getWidth();
                if (World.releasedy > this.backBtnY && World.releasedy < this.backBtnY + Tool.img_rectbtn0.getHeight() && World.releasedx > (World.viewWidth / 2) - (w / 2) && World.releasedx < (World.viewWidth / 2) + (w / 2)) {
                    Utilities.keyPressed(6, true);
                    return;
                }
                return;
            }
            return;
        }
        int by = this.btny + 32;
        if (World.releasedy > by && World.releasedy < Tool.img_rectbtn0.getHeight() + by) {
            if (World.releasedx <= this.x + 5 || World.releasedx >= this.x + 5 + Tool.img_rectbtn0.getWidth()) {
                if (World.releasedx > ((this.x + this.width) - Tool.img_rectbtn0.getWidth()) - 5 && World.releasedx < (this.x + this.width) - 5) {
                    Utilities.keyPressed(7, true);
                    return;
                }
                return;
            }
            this.buttonSelect = 0;
            Utilities.keyPressed(6, true);
        }
    }

    private void drawArrow(Graphics g, int y2) {
        int i = this.x;
        Tool.drawSmallNum(String.valueOf(this.sw.getPageNo() + 1) + "/" + this.sw.getPageSize(), g, World.viewWidth / 2, y2 - 15, 3, -1);
        g.drawImage(Tool.img_triArrowL[0], this.x, y2, 6);
        g.drawImage(Tool.img_triArrowR[0], this.x + this.width, y2, 10);
        if (this.arrowX == -1) {
            this.arrowX = ((World.viewWidth / 2) - 10) - Tool.uiMiscImg.getFrameWidth(82);
        }
        if (this.arrowY == -1) {
            this.arrowY = y2;
        }
    }

    public void draw(Graphics g) {
        Image image;
        int i;
        Image image2;
        Image image3;
        int i2;
        g.setFont(this.font);
        Utilities.setUIClip(g);
        int rx = Utilities.uiX + this.x;
        int ry = Utilities.uiY + this.y;
        int rw = this.width;
        int rh = getHeight();
        if (this.sw.getPageSize() > 1) {
            rh += this.lineHeight >> 1;
        }
        int cliph = rh + this.lineHeight;
        if (this.buttons != null) {
            cliph += Tool.img_rectbtn0.getHeight();
        }
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        Tool.drawAlphaBox(g, rw, cliph + 16, rx, ry - 8, -1, true);
        int tx = rx + TXT_INDENT_X;
        if (this.icon != -1) {
            tx += Tool.uiMiscImg.getFrameWidth(this.icon) + 4;
        }
        try {
            if (this.title != null) {
                Tool.drawLevelString(g, this.title.getString(), tx, ry - 2, 20, 1, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int icony = ry - 4;
        int ry2 = ry + (this.lineHeight - 1);
        int drawHeight = 0 + (this.lineHeight - 1);
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        this.sw.draw3D(g, this.x + TXT_INDENT_X + 5, ry2 + TXT_INDENT_Y, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
        int ry3 = ry2 + rh;
        if (this.buttons != null) {
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            int i3 = this.btnx;
            int dy = this.btny + 32;
            if (this.buttons.length == 1) {
                g.drawImage(Tool.img_rectbtn0, this.x + (this.width / 2), dy, 17);
            } else if (this.buttons.length == 2) {
                if (this.btnState[0]) {
                    image2 = Tool.img_rectbtn1;
                } else {
                    image2 = Tool.img_rectbtn0;
                }
                g.drawImage(image2, this.x + 5, dy, 20);
                Tool.drawLevelString(g, this.buttonText[0], this.x + 5 + (Tool.img_rectbtn0.getWidth() / 2), dy + 13, 17, 2, this.btnState[0] ? 1 : 0);
                if (this.btnState[1]) {
                    image3 = Tool.img_rectbtn1;
                } else {
                    image3 = Tool.img_rectbtn0;
                }
                g.drawImage(image3, (this.x + this.width) - 5, dy, 24);
                String str = this.buttonText[1];
                int width2 = ((this.x + this.width) - 5) - (Tool.img_rectbtn0.getWidth() / 2);
                int i4 = dy + 13;
                if (this.btnState[1]) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                Tool.drawLevelString(g, str, width2, i4, 17, 2, i2);
            }
        }
        if (this.icon != -1) {
            Tool.uiMiscImg.drawFrame(g, this.icon, rx + TXT_INDENT_X, icony, 0);
        }
        if (this.sw.getPageSize() > 1) {
            drawArrow(g, ry3 - Utilities.CHAR_HEIGHT);
        }
        if (this.closeMode == CLOSE_ON_BACK_KEY && this.buttons == null) {
            String btn = "点击继续";
            if (this.sw.getPageSize() <= 1 || this.sw.getPageSize() <= this.sw.getPageNo() + 1) {
                btn = "关    闭 ";
            }
            int w = Utilities.font.stringWidth(btn);
            int y2 = (ry3 - (this.lineHeight >> 1)) + 3;
            if (this.btnState[0]) {
                image = Tool.img_rectbtn1;
            } else {
                image = Tool.img_rectbtn0;
            }
            g.drawImage(image, World.viewWidth / 2, y2 + 9, 33);
            int i5 = (World.viewWidth - w) / 2;
            int i6 = y2 - 8;
            if (this.btnState[0]) {
                i = 1;
            } else {
                i = 0;
            }
            Tool.drawLevelString(g, btn, i5, i6, 36, 2, i);
            if (this.backBtnY == -1) {
                this.backBtnY = y2 - Tool.img_rectbtn0.getHeight();
            }
            if (this.backBtnX == -1) {
                this.backBtnX = ((World.viewWidth - w) + Utilities.CHAR_HEIGHT) / 2;
            }
        }
    }

    public void setCloseOnAnyKey() {
        this.closeMode = CLOSE_ON_ANY_KEY;
    }

    public void setCloseOnBackKey() {
        this.closeMode = CLOSE_ON_BACK_KEY;
    }

    public void setTimeout(long t) {
        this.timeout = System.currentTimeMillis() + t;
    }

    public void setListenVM(GTVM vm) {
        this.listenVM = vm;
    }

    public void setNonModal() {
        this.modal = false;
    }

    private void innerClose() {
        this.closed = true;
        if (this.listenVM != null) {
            this.listenVM.continueProcess(0);
        }
    }

    public void close() {
        this.show = false;
        innerClose();
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setShowLines(int lineNum, int my) {
        this.lineShowNum = lineNum;
        this.y = ((Utilities.uiHeight - getHeight()) * my) / 100;
        this.sw = new StringDraw(this.text, this.width - (TXT_INDENT_X << 1), this.lineHeight * this.lineShowNum);
        if (this.sw.length() == 1) {
            this.width = Utilities.font.stringWidth(this.sw.getLineString(0)) + (TXT_INDENT_X << 1) + 10;
        }
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon2) {
        this.icon = icon2;
    }
}
