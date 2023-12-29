package com.sumsharp.lowui;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import javax.microedition.lcdui.Graphics;

public class Tab1 {
    public static final byte TAB_MODE_FULL = 0;
    public static final byte TAB_MODE_PAGE = 1;
    private int freeTabWidth = 0;
    private TabHandler handle;
    private boolean hasArrow;
    private boolean isBgAlpha;
    private boolean isRoundBtn;
    private boolean isUp;
    private int itemWidth;
    private int tabEnd = 0;
    private int tabFirst = 0;
    public int tabHeight;
    private int tabIdx;
    private int tabMode;
    private String[] tabTitles;
    public int tabY;
    public int width;
    public int x;

    public Tab1(String name, String[] titles, int x2, int width2, boolean isRoundBtn2, TabHandler handle2) {
        this.tabTitles = titles;
        this.tabIdx = 0;
        this.x = x2;
        this.width = width2;
        this.isRoundBtn = isRoundBtn2;
        if (isRoundBtn2) {
            this.tabHeight = getHeight(true);
        } else {
            this.tabHeight = getHeight(false);
        }
        this.itemWidth = titles.length == 0 ? width2 : width2 / titles.length;
        int x3 = x2 + ((width2 - (this.itemWidth * titles.length)) / 2);
        this.handle = handle2;
        initTabTitle();
    }

    public void setBgAlpha(boolean alpha) {
        this.isBgAlpha = alpha;
    }

    public void setIsUp(boolean isup) {
        this.isUp = isup;
    }

    public int getIdx() {
        return this.tabIdx;
    }

    public void setTitles(String[] titles) {
        this.tabTitles = titles;
        initTabTitle();
    }

    private void coverContentsWithTab(Graphics g) {
        int off;
        if (this.hasArrow) {
            off = Tool.img_triArrowL[0].getWidth() + 5;
        } else {
            off = 0;
        }
        if (!this.isUp) {
            Tool.drawAlphaBox(g, this.itemWidth, this.tabHeight + 5, this.x + ((this.tabIdx - this.tabFirst) * this.itemWidth) + off, this.tabY - 5, -1, false);
            return;
        }
        Tool.drawAlphaBox(g, this.itemWidth, this.tabHeight, this.x + ((this.tabIdx - this.tabFirst) * this.itemWidth) + off, this.tabY, -1, false);
    }

    private void handleKey() {
        if (Utilities.isKeyPressed(22, true)) {
            this.tabIdx--;
            if (this.tabIdx < 0) {
                this.tabIdx = this.tabTitles.length - 1;
            }
            if (this.handle != null) {
                this.handle.handleCurrTab();
            }
        } else if (Utilities.isKeyPressed(21, true)) {
            this.tabIdx++;
            if (this.tabIdx >= this.tabTitles.length) {
                this.tabIdx = 0;
            }
            if (this.handle != null) {
                this.handle.handleCurrTab();
            }
        }
    }

    public void cycle() {
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

    public void setTabIndex(int idx) {
        if (idx < this.tabTitles.length && idx >= 0 && idx != this.tabIdx) {
            this.tabIdx = idx;
            if (this.handle != null) {
                this.handle.handleCurrTab();
            }
        }
    }

    public void initTabTitle() {
        if (this.tabIdx >= this.tabTitles.length) {
            this.tabIdx = 0;
        }
        int w = World.viewWidth - (this.x * 2);
        int tabWidth = this.itemWidth * 2;
        this.tabMode = 0;
        int i = 0;
        while (true) {
            if (i >= this.tabTitles.length) {
                break;
            }
            tabWidth += this.itemWidth;
            if (tabWidth > w) {
                this.tabMode = 1;
                break;
            }
            i++;
        }
        this.tabFirst = 0;
        this.tabMode = 1;
        if (this.tabMode != 1) {
            this.width = (this.itemWidth * 2) + (this.tabTitles.length * this.itemWidth);
        } else if (this.freeTabWidth > this.tabTitles.length * (Tool.img_rectbtn0.getWidth() + 10)) {
            this.hasArrow = false;
        } else {
            this.freeTabWidth = this.width - ((Tool.img_triArrowL[0].getWidth() + 5) * 2);
            this.hasArrow = true;
        }
    }

    public void paint(Graphics g) {
        int off = World.tick % 8;
        if (off > 4) {
            int off2 = 7 - off;
        }
        int dx = this.x;
        coverContentsWithTab(g);
        if (this.tabMode == 0) {
            for (int i = 0; i < this.tabTitles.length; i++) {
                int ftClr = Tool.CLR_TABLE[10];
                int bgClr = Tool.CLR_TABLE[11];
                if (i == this.tabIdx) {
                    bgClr = Tool.CLR_TABLE[7];
                    ftClr = Tool.CLR_TABLE[8];
                }
                Tool.draw3DString(g, this.tabTitles[i], dx, (this.tabHeight / 2) + this.tabY, ftClr, bgClr, 36);
                dx += Utilities.font.stringWidth(this.tabTitles[i]) + 5;
            }
            return;
        }
        int dw = 0;
        int dx2 = this.x;
        if (this.hasArrow) {
            g.drawImage(Tool.img_triArrowL[0], this.x, this.tabY, 20);
            g.drawImage(Tool.img_triArrowR[0], this.x + this.width, this.tabY, 24);
            dx2 += Tool.img_triArrowL[0].getWidth() + 5;
            Tool.drawBookAnimate(g, (this.x + this.width) - 10, (this.tabY + this.tabHeight) - 1);
            Tool.drawSmallNum(String.valueOf(this.tabIdx + 1) + "/" + this.tabTitles.length, g, this.x + this.width, (this.tabY + this.tabHeight) - 1, 40, -1);
        }
        int i2 = this.tabFirst;
        while (i2 < this.tabTitles.length) {
            if (i2 == this.tabFirst || Utilities.font.stringWidth(this.tabTitles[i2]) + dw <= this.freeTabWidth) {
                if (i2 == this.tabTitles.length - 1) {
                    this.tabEnd = i2;
                }
                int ftClr2 = Tool.CLR_TABLE[10];
                int bgClr2 = Tool.CLR_TABLE[11];
                int yoff = this.isUp ? 6 : -6;
                if (i2 == this.tabIdx) {
                    bgClr2 = Tool.CLR_TABLE[7];
                    ftClr2 = Tool.CLR_TABLE[8];
                    if (this.isRoundBtn) {
                        g.drawImage(Tool.img_roundbtn1, (this.itemWidth / 2) + dx2, this.tabY + yoff, 17);
                    } else {
                        g.drawImage(Tool.img_rectbtn1, (this.itemWidth / 2) + dx2, this.tabY + yoff, 17);
                    }
                } else if (this.isRoundBtn) {
                    g.drawImage(Tool.img_roundbtn0, (this.itemWidth / 2) + dx2, this.tabY + yoff, 17);
                } else {
                    g.drawImage(Tool.img_rectbtn0, (this.itemWidth / 2) + dx2, this.tabY + yoff, 17);
                }
                Tool.draw3DString(g, this.tabTitles[i2], dx2 + (this.itemWidth / 2), this.tabY + (this.tabHeight / 2) + yoff, ftClr2, bgClr2, 33);
                dx2 += this.itemWidth;
                dw += this.itemWidth;
                i2++;
            } else {
                this.tabEnd = i2 - 1;
                return;
            }
        }
    }

    private boolean hasPointerEvents(int px, int py) {
        if (py > this.tabY && py < this.tabY + getHeight() && px > this.x && px < this.x + this.width) {
            if (!this.hasArrow || this.tabMode != 1) {
                int dright = this.x;
                for (int i = this.tabFirst; i < this.tabEnd + 1; i++) {
                    int dleft = dright;
                    dright = dleft + this.itemWidth;
                    if (px > dleft && px < dright) {
                        setTabIndex(i);
                    }
                }
            } else if (px > this.x && px < this.x + Tool.img_triArrowL[0].getWidth() + 5) {
                Utilities.keyPressed(42, true);
                return true;
            } else if (px <= (this.x + this.width) - Tool.img_triArrowR[0].getWidth() || px >= this.x + this.width) {
                int dright2 = this.x + Tool.img_triArrowL[0].getWidth();
                for (int i2 = this.tabFirst; i2 < this.tabEnd + 1; i2++) {
                    int dleft2 = dright2;
                    dright2 = dleft2 + this.itemWidth;
                    if (px > dleft2 && px < dright2) {
                        setTabIndex(i2);
                    }
                }
            } else {
                Utilities.keyPressed(35, true);
                return true;
            }
        }
        return false;
    }

    public int getHeight() {
        return this.tabHeight;
    }

    public void setY(int y) {
        this.tabY = y;
    }

    public void refreshContent() {
        if (this.handle != null) {
            this.handle.handleCurrTab();
        }
    }

    public static int getHeight(boolean isRound) {
        if (isRound) {
            return Tool.img_roundbtn0.getHeight() + 5;
        }
        return Tool.img_rectbtn0.getHeight() + 5;
    }
}
