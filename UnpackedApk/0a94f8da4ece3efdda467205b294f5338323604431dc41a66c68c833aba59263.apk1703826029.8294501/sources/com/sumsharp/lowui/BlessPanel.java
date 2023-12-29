package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.item.GameItem;
import javax.microedition.lcdui.Graphics;

public class BlessPanel extends UI {
    private static int[] RATE_TABLE;
    private static int[] VALUE_TABLE;
    public static final String[] tips = {"点击资质灵石框放入资质灵石,点击左右箭头改变灵石数量,放入更多的资质灵石可在祝福中获得更多的资质提高", "点击成功率灵石框放入成功率灵石,点击左右箭头改变灵石数量,放入更多的成功率灵石可增加祝福成功率.", "点击合成按钮,进行祝福"};
    private int arrowlx;
    private int arrowrx;
    private int attrAdd;
    private int attrCount;
    private GameItem attrStone;
    private int attry;
    private int btnh;
    private int btny;
    private int contentHeight;
    private int contentSpeed;
    private int contentTop;
    private int contentTopAni;
    private int descHeight;
    private int descSpeed;
    private int descTop;
    private int descTopAni;
    private GameItem egg;
    private ImageSet eggIcon = ImageLoadManager.getImage("egg.jgp");
    private int gridx;
    private int icon;
    private MovingString movestr;
    private int rate;
    private int rateCount;
    private GameItem rateStone;
    private int ratey;
    private StringDraw sdAttr;
    private StringDraw sdRate;
    private int selection;
    private int stoneh;
    private String title;
    private int titleHeight;
    private int titleSpeed;
    private int titleTop;
    private int titleTopAni;
    private int x;

    static {
        int[] iArr = new int[11];
        iArr[1] = 11;
        iArr[2] = 36;
        iArr[3] = 51;
        iArr[4] = 62;
        iArr[5] = 71;
        iArr[6] = 79;
        iArr[7] = 85;
        iArr[8] = 90;
        iArr[9] = 95;
        iArr[10] = 100;
        RATE_TABLE = iArr;
        int[] iArr2 = new int[11];
        iArr2[1] = 34;
        iArr2[2] = 108;
        iArr2[3] = 153;
        iArr2[4] = 187;
        iArr2[5] = 213;
        iArr2[6] = 236;
        iArr2[7] = 255;
        iArr2[8] = 271;
        iArr2[9] = 286;
        iArr2[10] = 300;
        VALUE_TABLE = iArr2;
    }

    public BlessPanel(int eggId, int icon2, String title2) {
        this.egg = CommonData.player.findItem(eggId);
        this.icon = icon2;
        this.x = 3;
        this.titleTopAni = NewStage.screenY + 5;
        this.titleHeight = Utilities.TITLE_HEIGHT;
        this.titleTop = -this.titleHeight;
        this.descHeight = 22;
        this.contentTopAni = this.titleTopAni + this.titleHeight + 5;
        this.contentTop = World.viewHeight;
        this.contentHeight = (((World.viewHeight - this.titleTopAni) - this.titleHeight) - this.descHeight) - CornerButton.instance.getHeight();
        this.descTopAni = this.contentTopAni + this.contentHeight;
        this.descTop = this.contentTop + this.contentHeight;
        this.titleSpeed = (this.titleTopAni - this.titleTop) >> 1;
        this.contentSpeed = (this.contentTop - this.contentTopAni) >> 1;
        this.descSpeed = (this.descTop - this.descTopAni) >> 1;
        this.title = title2;
        this.movestr = new MovingString(tips[0], World.viewWidth - 16, 3);
    }

    private void drawTip(Graphics g) {
    }

    public void cycle() {
        super.cycle();
        if (this.closed) {
            this.titleTop -= this.titleSpeed;
            if (this.descTop != this.descTopAni) {
                this.contentTop += this.contentSpeed;
            }
            this.descTop += this.descSpeed;
            if (this.contentTop > World.viewHeight) {
                this.show = false;
            }
        } else if (this.show) {
            moveBtn();
            if (World.pressedx != -1 && World.pressedy != -1 && World.pressedx > 3 && World.pressedx < World.uiWidth - 6 && World.pressedy > this.attry && World.pressedy < this.btny + this.btnh) {
                int newsel = -1;
                if (World.pressedy < this.attry + this.stoneh) {
                    newsel = 0;
                } else if (World.pressedy < this.ratey + this.stoneh) {
                    newsel = 1;
                }
                if (newsel != this.selection) {
                    this.selection = newsel;
                    this.movestr = new MovingString(tips[this.selection], World.viewWidth - 16, 3);
                } else if (newsel == 0 || newsel == 1) {
                    if (World.pressedx > this.gridx && World.pressedx < this.gridx + 50) {
                        Utilities.keyPressed(5, true);
                    } else if (World.pressedx > this.arrowlx && World.pressedx < this.arrowlx + Tool.img_triArrowL[0].getWidth()) {
                        Utilities.keyPressed(3, true);
                    } else if (World.pressedx > this.arrowrx && World.pressedx < this.arrowrx + Tool.img_triArrowR[0].getWidth()) {
                        Utilities.keyPressed(4, true);
                    }
                }
                World.pressedx = -1;
                World.pressedy = -1;
            }
            if (Utilities.isKeyPressed(0, true) || Utilities.isKeyPressed(13, true)) {
                this.selection--;
                if (this.selection < 0) {
                    this.selection = 0;
                }
                this.movestr = new MovingString(tips[this.selection], World.viewWidth - 16, 3);
            } else if (Utilities.isKeyPressed(1, true) || Utilities.isKeyPressed(19, true)) {
                this.selection++;
                if (this.selection > 1) {
                    this.selection = 1;
                }
                this.movestr = new MovingString(tips[this.selection], World.viewWidth - 16, 3);
            } else if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
                if (this.selection == 0) {
                    if (this.attrStone != null) {
                        this.attrCount--;
                        if (this.attrCount < 1) {
                            this.attrCount = 1;
                        }
                    }
                } else if (this.rateStone != null) {
                    this.rateCount--;
                    if (this.rateCount < 1) {
                        this.rateCount = 1;
                    }
                }
            } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
                if (this.selection == 0) {
                    if (this.attrStone != null) {
                        this.attrCount++;
                        if (this.attrCount >= this.attrStone.count) {
                            this.attrCount = this.attrStone.count;
                        }
                        if (this.attrCount > 10) {
                            this.attrCount = 10;
                        }
                    }
                } else if (this.rateStone != null) {
                    this.rateCount++;
                    if (this.rateCount >= this.rateStone.count) {
                        this.rateCount = this.rateStone.count;
                    }
                    if (this.rateCount > 10) {
                        this.rateCount = 10;
                    }
                }
            }
            if (this.movestr != null) {
                this.movestr.cycle();
            }
        } else {
            this.titleTop += this.titleSpeed;
            if (this.contentTop != World.viewHeight) {
                this.descTop -= this.descSpeed;
            }
            this.contentTop -= this.contentSpeed;
            if (this.descTop - this.descSpeed <= this.descTopAni) {
                this.descTop = this.descTopAni;
                this.contentTop = this.contentTopAni;
                this.titleTop = this.titleTopAni;
                this.show = true;
                initPos();
            }
        }
    }

    public void setRateStone(int itemId) {
        this.rateStone = CommonData.player.findItem(itemId);
        if (this.rateStone != null) {
            this.rateCount = 1;
            this.rate = RATE_TABLE[this.rateCount];
        }
    }

    public void setAttrStone(int itemId) {
        this.attrStone = CommonData.player.findItem(itemId);
        if (this.attrStone != null) {
            this.attrCount = 1;
            this.attrAdd = VALUE_TABLE[this.attrCount];
        }
    }

    public void clear() {
        this.rateStone = null;
        this.attrStone = null;
        this.attrCount = 0;
        this.rateCount = 0;
    }

    private void initPos() {
        this.attry = this.contentTop + 10;
        this.stoneh = 56;
        this.ratey = this.attry + this.stoneh;
        this.btny = this.ratey + this.stoneh;
        this.btnh = Tool.img_rectbtn0.getHeight() + 10;
        this.arrowlx = (World.viewWidth / 2) + 20;
        this.arrowrx = this.arrowlx + Tool.img_triArrowL[0].getWidth() + 70;
        this.gridx = ((this.x + (World.viewWidth / 2)) - 20) - 50;
    }

    public void paint(Graphics g) {
        drawTitlePanel(g, this.title, 3, this.titleTop, World.viewWidth - 6, Utilities.TITLE_HEIGHT, -1);
        drawRectPanel(g, this.x, this.contentTop, World.viewWidth - (this.x * 2), this.contentHeight);
        drawRectPanel(g, this.x, this.descTop, World.viewWidth - (this.x * 2), this.descHeight);
        Tool.draw3DString(g, this.egg.name, World.viewWidth >> 1, this.titleTop + 9, Tool.clr_androidTextDefault, Tool.CLR_TABLE[11], 17);
        if (this.show) {
            if (this.selection == 0) {
                Tool.fillAlphaRect(g, -2013265920, 10, this.attry - 2, World.viewWidth - 20, this.stoneh + 4);
            } else if (this.selection == 1) {
                Tool.fillAlphaRect(g, -2013265920, 10, this.ratey - 2, World.viewWidth - 20, this.stoneh + 4);
            }
            Tool.drawBlurRect(g, this.gridx, this.attry, 50, 50, 1 - this.selection);
            if (this.attrStone != null) {
                this.attrStone.drawIcon(g, this.gridx + 25, this.attry + 25, 3);
            }
            g.drawImage(Tool.img_triArrowL[0], this.arrowlx, this.attry, 20);
            Tool.drawLevelString(g, String.valueOf(this.attrCount), this.arrowlx + Tool.img_triArrowL[0].getWidth() + 30, Utilities.LINE_HEIGHT + this.attry, 6, 3, 0);
            g.drawImage(Tool.img_triArrowR[0], this.arrowrx, this.attry, 20);
            Tool.drawBlurRect(g, this.gridx, this.ratey, 50, 50, this.selection);
            if (this.rateStone != null) {
                this.rateStone.drawIcon(g, this.gridx + 25, this.ratey + 25, 3);
            }
            g.drawImage(Tool.img_triArrowL[0], this.arrowlx, this.ratey, 20);
            Tool.drawLevelString(g, String.valueOf(this.rateCount), this.arrowlx + Tool.img_triArrowL[0].getWidth() + 30, Utilities.LINE_HEIGHT + this.ratey, 6, 3, 0);
            g.drawImage(Tool.img_triArrowR[0], this.arrowrx, this.ratey, 20);
            if (this.attrStone == null) {
                Tool.draw3DString(g, "请放入资质灵石", this.x + 30, this.attry + 33, Tool.clr_androidTextDefault, 0, 36);
            } else {
                int idx = this.attrStone.type - 9;
                String msg = Pet.RATE_TEXT[idx];
                int v = this.egg.getPetRate(idx);
                int w = Utilities.font.stringWidth(msg);
                Tool.draw3DString(g, msg, this.x + 30, this.attry + 10, Tool.clr_androidTextDefault, 0, 0);
                Tool.drawSmallNum(String.valueOf(v) + "--" + (VALUE_TABLE[this.attrCount] + v), g, this.x + 30 + w, (this.attry + Utilities.LINE_HEIGHT) - 4, 36, -1);
            }
            Tool.draw3DString(g, "成功几率", this.x + 30, this.ratey + 33, Tool.clr_androidTextDefault, 0, 0);
            Tool.drawLevelString(g, String.valueOf(RATE_TABLE[this.rateCount]) + "%", this.x + 120, this.ratey + 33, 0, 3, 0);
        }
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        if (this.movestr != null) {
            this.movestr.draw(g, 8, this.descTop + 2, Tool.clr_androidTextDefault);
        }
        drawBtn(g, -1);
    }

    public int getMenuSelection() {
        return this.selection;
    }

    public int getRateCount() {
        if (this.rateStone == null) {
            return -1;
        }
        return this.rateCount;
    }

    public int getAttrCount() {
        if (this.attrStone == null) {
            return -1;
        }
        return this.attrCount;
    }

    public int getX() {
        return this.x;
    }
}
