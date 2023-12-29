package com.sumsharp.lowui;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Tab {
    private byte[] arrowState;
    private int contentHeight;
    private int focusTabWidth;
    private int freeTabWidth = 0;
    private int freeTabx;
    private int h;
    private TabHandler handler;
    private boolean isChatTab;
    private boolean isUp;
    private int noFocusTabWidth;
    private Image oriImg;
    private int tabEnd = 0;
    private int tabFirst = 0;
    private int tabIdx;
    private String[] tabTitles;
    private String title;
    private Image transImg;
    private int w;
    private int x;
    private int y;

    public void setIsUp(boolean isup) {
        this.isUp = isup;
    }

    public int getX() {
        return this.x;
    }

    public void setIsChat(boolean ischat) {
        this.isChatTab = ischat;
        if (ischat) {
            this.h = Tool.img_roundbtn0.getHeight() + 5;
            this.freeTabWidth = this.w;
            this.freeTabx = 0;
            this.focusTabWidth = Tool.img_roundbtn0.getWidth() - 12;
            this.noFocusTabWidth = this.focusTabWidth;
        }
    }

    public Tab(String name, String[] titles, int x2, int width, boolean isUp2, TabHandler handle) {
        this.title = name;
        this.tabTitles = titles;
        this.tabIdx = 0;
        this.x = x2;
        this.w = width;
        this.h = Tool.img_triArrowL[0].getHeight() - 10;
        this.handler = handle;
        this.isUp = isUp2;
        init();
    }

    public void setY(int y2) {
        this.y = y2;
    }

    public int getIdx() {
        return this.tabIdx;
    }

    public void init() {
        this.tabIdx = 0;
        this.tabFirst = 0;
        this.tabEnd = 0;
        this.freeTabWidth = this.w - ((Tool.img_triArrowL[0].getWidth() + 5) * 2);
        this.freeTabx = this.isChatTab ? 0 : this.x + Tool.img_triArrowL[0].getWidth();
        this.focusTabWidth = (this.tabTitles[0].length() * 22) + 16;
        this.noFocusTabWidth = 80;
        if (!this.isChatTab) {
            byte[] bArr = new byte[2];
            bArr[0] = 2;
            this.arrowState = bArr;
        }
    }

    public void setContentHeight(int h2) {
        this.contentHeight = h2;
    }

    private void handleKey() {
        if (this.isChatTab) {
            return;
        }
        if (Utilities.isKeyPressed(22, true)) {
            this.tabIdx--;
            if (!this.isUp) {
                this.transImg = null;
                this.oriImg = null;
            }
            if (this.tabIdx < 0) {
                this.tabIdx = this.tabTitles.length - 1;
            }
            if (this.handler != null) {
                this.handler.handleCurrTab();
            }
        } else if (Utilities.isKeyPressed(21, true)) {
            this.tabIdx++;
            if (!this.isUp) {
                this.transImg = null;
                this.oriImg = null;
            }
            if (this.tabIdx >= this.tabTitles.length) {
                this.tabIdx = 0;
            }
            if (this.handler != null) {
                this.handler.handleCurrTab();
            }
        }
    }

    public void setTabIndex(int idx) {
        if (idx < this.tabTitles.length && idx >= 0 && idx != this.tabIdx) {
            this.tabIdx = idx;
            if (!this.isUp) {
                this.transImg = null;
                this.oriImg = null;
            }
            if (this.handler != null) {
                this.handler.handleCurrTab();
            }
        }
    }

    private boolean hasPointerEvents(int px, int py) {
        int i;
        if (py > this.y && py < this.y + this.h && px > this.x && px < this.x + this.w) {
            if (this.isChatTab) {
                int dright = this.x + 5;
                int i2 = this.tabFirst;
                while (i2 < this.tabEnd + 1) {
                    int dleft = dright;
                    dright = dleft + this.focusTabWidth;
                    if (px <= dleft || px >= dright) {
                        i2++;
                    } else {
                        setTabIndex(i2);
                        return true;
                    }
                }
            } else if (px > this.x && px < this.x + Tool.img_triArrowL[0].getWidth() + 5) {
                this.arrowState[0] = 1;
                return true;
            } else if (px <= ((this.x + this.w) - Tool.img_triArrowR[0].getWidth()) - 5 || px >= this.x + this.w) {
                int dright2 = this.x + Tool.img_triArrowL[0].getWidth() + 5;
                int i3 = this.tabFirst;
                while (i3 < this.tabEnd + 1) {
                    int dleft2 = dright2;
                    if (i3 == this.tabIdx) {
                        i = this.focusTabWidth;
                    } else {
                        i = this.noFocusTabWidth;
                    }
                    dright2 = dleft2 + i;
                    if (px <= dleft2 || px >= dright2) {
                        i3++;
                    } else {
                        setTabIndex(i3);
                        return true;
                    }
                }
            } else {
                this.arrowState[1] = 1;
                return true;
            }
        }
        return false;
    }

    public int getY() {
        return this.y;
    }

    public int getHeight() {
        return this.h;
    }

    private void cycleArrow() {
        this.arrowState = new byte[2];
        if (World.moveY > this.y - 5 && World.moveY < this.y + this.h + 5) {
            if (World.moveX > this.x - 10 && World.moveX < this.x + Tool.img_triArrowL[0].getWidth() + 5) {
                this.arrowState[0] = 1;
            } else if (World.moveX > ((this.x + this.w) - Tool.img_triArrowR[0].getWidth()) - 5 && World.moveX < this.x + this.w + 10) {
                this.arrowState[1] = 1;
            }
        }
        if (World.releasedy > this.y && World.releasedy < this.y + this.h) {
            if (World.releasedx > this.x - 10 && World.releasedx < this.x + Tool.img_triArrowL[0].getWidth() + 5) {
                Utilities.keyPressed(42, true);
            } else if (World.releasedx > ((this.x + this.w) - Tool.img_triArrowR[0].getWidth()) - 5 && World.releasedx < this.x + this.w + 10) {
                Utilities.keyPressed(35, true);
            }
        }
    }

    public void cycle() {
        if (!this.isChatTab) {
            cycleArrow();
        }
        if (hasPointerEvents(World.pressedx, World.pressedy)) {
            World.pressedx = -1;
            World.pressedy = -1;
        }
        if (this.tabIdx > this.tabEnd) {
            this.tabFirst++;
        } else if (this.tabIdx < this.tabFirst) {
            this.tabFirst = this.tabIdx;
        }
        handleKey();
    }

    public static int getDefaultHeight() {
        return Tool.img_triArrowL[0].getHeight() - 10;
    }

    public void paint(Graphics g) {
        int i;
        int i2;
        Image image;
        int i3;
        Image image2;
        int i4;
        int dw = 0;
        int dx = this.x + this.freeTabx;
        if (!this.isChatTab) {
            if (this.arrowState[0] == 1) {
                image = Tool.img_triArrowL[1];
            } else {
                image = Tool.img_triArrowL[0];
            }
            int i5 = this.x;
            if (this.isUp) {
                i3 = this.y + (this.h / 2);
            } else {
                i3 = this.y + (this.h / 2) + 9;
            }
            g.drawImage(image, i5, i3, 6);
            if (this.arrowState[1] == 1) {
                image2 = Tool.img_triArrowR[1];
            } else {
                image2 = Tool.img_triArrowR[0];
            }
            int i6 = this.x + this.w;
            if (this.isUp) {
                i4 = this.y + (this.h / 2);
            } else {
                i4 = this.y + (this.h / 2) + 9;
            }
            g.drawImage(image2, i6, i4, 10);
            Tool.drawSmallNum(String.valueOf(this.tabIdx + 1) + "/ " + this.tabTitles.length, g, ((this.x + this.w) + 2) - 12, this.y + (this.h / 2) + 4, 9, -1);
        }
        int i7 = this.tabFirst;
        while (i7 < this.tabTitles.length) {
            if (i7 == this.tabFirst || getCurTabWidth(i7) + dw <= this.freeTabWidth) {
                if (i7 == this.tabTitles.length - 1) {
                    this.tabEnd = i7;
                }
                int ftClr = 16776960;
                if (i7 == this.tabIdx) {
                    ftClr = AbstractUnit.CLR_NAME_TAR_RED;
                }
                drawTab(g, i7, dx);
                g.setColor(ftClr);
                if (i7 == this.tabIdx) {
                    if (this.isChatTab) {
                        g.drawImage(Tool.img_roundbtn1, (getCurTabWidth(i7) / 2) + dx, this.y + (this.h / 2), 3);
                    }
                } else if (this.isChatTab) {
                    g.drawImage(Tool.img_roundbtn0, (getCurTabWidth(i7) / 2) + dx, this.y + (this.h / 2), 3);
                }
                if (this.isChatTab) {
                    Tool.drawLevelString(g, this.tabTitles[i7], dx + (getCurTabWidth(i7) / 2), (this.y + this.h) - 33, 33, 2, i7 == this.tabIdx ? 1 : 0);
                } else if (this.isUp) {
                    String str = this.tabTitles[i7];
                    int curTabWidth = dx + (getCurTabWidth(i7) / 2);
                    int i8 = (this.y + this.h) - 8;
                    if (i7 == this.tabIdx) {
                        i2 = 1;
                    } else {
                        i2 = 3;
                    }
                    Tool.drawLevelString(g, str, curTabWidth, i8, 33, i2, 0);
                } else {
                    String str2 = this.tabTitles[i7];
                    int curTabWidth2 = dx + (getCurTabWidth(i7) / 2);
                    int i9 = (this.y + this.h) - 4;
                    if (i7 == this.tabIdx) {
                        i = 1;
                    } else {
                        i = 3;
                    }
                    Tool.drawLevelString(g, str2, curTabWidth2, i9, 33, i, 0);
                }
                dx += getCurTabWidth(i7);
                dw += getCurTabWidth(i7);
                i7++;
            } else {
                this.tabEnd = i7 - 1;
                return;
            }
        }
    }

    private void drawTab(Graphics g, int cur, int start) {
        int i;
        int i2;
        g.getCanvas().save();
        int height = this.h;
        if (cur == this.tabIdx) {
            int top = this.y;
            int cw = Tool.img_corner0[0].getWidth();
            if (this.isUp) {
                drawTabDetail(g, cur, start, top, height, cw);
            } else if (this.oriImg == null && this.transImg == null) {
                if (this.isChatTab) {
                    this.oriImg = Image.createImage(World.viewWidth, this.h + this.contentHeight);
                } else {
                    this.oriImg = Image.createImage(this.w, this.h + this.contentHeight);
                }
                Graphics transG = this.oriImg.getGraphics();
                if (this.isChatTab) {
                    i2 = start;
                } else {
                    i2 = (start + 0) - this.x;
                }
                drawTabDetail(transG, cur, i2, this.isChatTab ? this.h / 2 : 0, this.isChatTab ? this.h / 2 : this.h, cw);
                this.transImg = Image.createTransImage(Image.createTransImage(this.oriImg, 2), 3);
            } else {
                g.drawImage(this.transImg, this.isChatTab ? 0 : this.x, top + height + 8, 32);
            }
        } else {
            if (cur < this.tabIdx) {
                g.setClip(0, 0, World.viewWidth, World.viewHeight);
                int height2 = this.h - 5;
                if (this.isUp) {
                    Tool.drawAlphaBox(g, this.noFocusTabWidth, height2, start, (this.y + this.h) - height2, -872408018);
                } else if (!this.isChatTab) {
                    Tool.drawAlphaBox(g, this.noFocusTabWidth, height2, start, this.y + 8, -872408018);
                }
            } else {
                if (cur > this.tabIdx) {
                    g.setClip(0, 0, World.viewWidth, World.viewHeight);
                    int height3 = this.h - 5;
                    if (this.isUp) {
                        int i3 = this.noFocusTabWidth;
                        if (this.isChatTab) {
                            i = height3 / 2;
                        } else {
                            i = height3;
                        }
                        Tool.drawAlphaBox(g, i3, i, start, (this.y + this.h) - height3, -872408018);
                    } else if (!this.isChatTab) {
                        Tool.drawAlphaBox(g, this.noFocusTabWidth, height3, start, this.y + 8, -872408018);
                    }
                }
            }
        }
        g.getCanvas().restore();
    }

    private void drawTabDetail(Graphics g, int cur, int start, int top, int height, int cw) {
        int i;
        int i2;
        int i3;
        int i4;
        if (this.isChatTab) {
            this.w = World.viewWidth;
        }
        int[] clr = {2892826, 15983532, 80221, 3889001};
        g.setColor(clr[0]);
        g.drawLine(start, top, (getCurTabWidth(cur) + start) - 1, top);
        g.setColor(clr[1]);
        g.drawLine(start, top + 1, (getCurTabWidth(cur) + start) - 2, top + 1);
        g.setColor(clr[2]);
        g.drawLine(start + 2, top + 2, (getCurTabWidth(cur) + start) - 3, top + 2);
        g.setColor(clr[3]);
        g.drawLine(start + 3, top + 3, (getCurTabWidth(cur) + start) - 4, top + 3);
        g.setColor(clr[0]);
        g.drawLine(start, top, start, top + height);
        g.setColor(clr[1]);
        g.drawLine(start + 1, top + 1, start + 1, top + height + 1);
        g.setColor(clr[2]);
        g.drawLine(start + 2, top + 2, start + 2, top + height + 2);
        g.setColor(clr[3]);
        g.drawLine(start + 3, top + 3, start + 3, top + height + 3);
        g.setColor(clr[0]);
        g.drawLine((getCurTabWidth(cur) + start) - 1, top, (getCurTabWidth(cur) + start) - 1, top + height);
        g.setColor(clr[1]);
        g.drawLine((getCurTabWidth(cur) + start) - 2, top + 1, (getCurTabWidth(cur) + start) - 2, top + height + 1);
        g.setColor(clr[2]);
        g.drawLine((getCurTabWidth(cur) + start) - 3, top + 2, (getCurTabWidth(cur) + start) - 3, top + height + 2);
        g.setColor(clr[3]);
        g.drawLine((getCurTabWidth(cur) + start) - 4, top + 3, (getCurTabWidth(cur) + start) - 4, top + height + 3);
        g.setColor(clr[0]);
        g.drawLine(this.isChatTab ? 0 : this.x, top + height, start, top + height);
        g.setColor(clr[1]);
        g.drawLine(this.isChatTab ? 0 : this.x, top + height + 1, start + 1, top + height + 1);
        g.setColor(clr[2]);
        g.drawLine(this.isChatTab ? 0 : this.x, top + height + 2, start + 2, top + height + 2);
        g.setColor(clr[3]);
        g.drawLine(this.isChatTab ? 0 : this.x, top + height + 3, start + 3, top + height + 3);
        g.setColor(clr[0]);
        g.drawLine(getCurTabWidth(cur) + start, top + height, this.w + 0, top + height);
        g.setColor(clr[1]);
        g.drawLine((getCurTabWidth(cur) + start) - 1, top + height + 1, this.w + 0, top + height + 1);
        g.setColor(clr[2]);
        g.drawLine((getCurTabWidth(cur) + start) - 2, top + height + 2, this.w + 0, top + height + 2);
        g.setColor(clr[3]);
        g.drawLine((getCurTabWidth(cur) + start) - 3, top + height + 3, this.w + 0, top + height + 3);
        g.setColor(clr[0]);
        g.drawLine(this.isChatTab ? 0 : this.x, top + height, this.isChatTab ? 0 : this.x, top + height + this.contentHeight);
        g.setColor(clr[1]);
        g.drawLine(this.isChatTab ? 1 : this.x + 1, top + height + 1, this.isChatTab ? 1 : this.x + 1, top + height + this.contentHeight);
        g.setColor(clr[2]);
        g.drawLine(this.isChatTab ? 2 : this.x + 2, top + height + 2, this.isChatTab ? 2 : this.x + 2, top + height + this.contentHeight);
        g.setColor(clr[3]);
        g.drawLine(this.isChatTab ? 3 : this.x + 3, top + height + 3, this.isChatTab ? 3 : this.x + 3, top + height + this.contentHeight);
        g.setColor(clr[0]);
        g.drawLine(this.isChatTab ? this.w - 1 : (this.x + this.w) - 1, top + height, this.isChatTab ? this.w - 1 : (this.x + this.w) - 1, top + height + this.contentHeight);
        g.setColor(clr[1]);
        g.drawLine(this.isChatTab ? this.w - 2 : (this.x + this.w) - 2, top + height + 1, this.isChatTab ? this.w - 2 : (this.x + this.w) - 2, ((top + height) + this.contentHeight) - 1);
        g.setColor(clr[2]);
        g.drawLine(this.isChatTab ? this.w - 3 : (this.x + this.w) - 3, top + height + 2, this.isChatTab ? this.w - 3 : (this.x + this.w) - 3, ((top + height) + this.contentHeight) - 2);
        g.setColor(clr[3]);
        g.drawLine(this.isChatTab ? this.w - 4 : (this.x + this.w) - 4, top + height + 3, this.isChatTab ? this.w - 4 : (this.x + this.w) - 4, ((top + height) + this.contentHeight) - 3);
        g.setColor(clr[0]);
        g.drawLine(this.isChatTab ? 0 : this.x, ((top + height) + this.contentHeight) - 1, this.isChatTab ? this.w - 1 : (this.x + this.w) - 1, ((top + height) + this.contentHeight) - 1);
        g.setColor(clr[1]);
        int i5 = this.isChatTab ? 1 : this.x + 1;
        int i6 = ((top + height) + this.contentHeight) - 2;
        if (this.isChatTab) {
            i = this.w - 2;
        } else {
            i = (this.x + this.w) - 2;
        }
        g.drawLine(i5, i6, i, ((top + height) + this.contentHeight) - 2);
        g.setColor(clr[2]);
        int i7 = this.isChatTab ? 2 : this.x + 2;
        int i8 = ((top + height) + this.contentHeight) - 3;
        if (this.isChatTab) {
            i2 = this.w - 3;
        } else {
            i2 = (this.x + this.w) - 3;
        }
        g.drawLine(i7, i8, i2, ((top + height) + this.contentHeight) - 3);
        g.setColor(clr[3]);
        int i9 = this.isChatTab ? 3 : this.x + 3;
        int i10 = ((top + height) + this.contentHeight) - 4;
        if (this.isChatTab) {
            i3 = this.w - 4;
        } else {
            i3 = (this.x + this.w) - 4;
        }
        g.drawLine(i9, i10, i3, ((top + height) + this.contentHeight) - 4);
        Tool.fillAlphaRect(g, -2013258706, start + 4, top + 4, getCurTabWidth(cur) - 7, height);
        int i11 = this.isChatTab ? 4 : this.x + 4;
        int i12 = top + height + 4;
        if (this.isChatTab) {
            i4 = (this.w + 0) - 9;
        } else {
            i4 = (this.x + this.w) - 9;
        }
        Tool.fillAlphaRect(g, -2013258706, i11, i12, i4, this.contentHeight - 7);
    }

    public void refreshContent() {
        if (this.handler != null) {
            this.handler.handleCurrTab();
        }
    }

    private int getCurTabWidth(int cur) {
        if (this.isChatTab) {
            return this.focusTabWidth;
        }
        if (cur == this.tabIdx) {
            return (this.tabTitles[cur].length() * 22) + 16;
        }
        return 80;
    }

    public int getPageNum() {
        return this.tabTitles.length;
    }
}
