package com.sumsharp.lowui;

import android.graphics.Rect;
import com.sumsharp.monster.MonsterMIDlet;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.gtvm.GTVM;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

public class TextField extends AbstractLowUI implements CommandListener {
    private static final int LIST_COUNT = 3;
    private static final int LIST_ITEM_MARGIN = 2;
    private static final int LIST_VMARGIN = 6;
    private static int[] Soft_key = new int[48];
    private static int[] key_arr = new int[12];
    private boolean canSwitchInputMode = true;
    private int clrBg = Tool.CLR_DARK;
    private int clrBgSel = Tool.CLR_TEXT_LIGHT;
    private int currInputKey;
    private int currInputKeyCount;
    private boolean drawScroll;
    private int editLocal = 0;
    private int gridBlank = 3;
    private boolean hasKeyPad = false;
    private boolean hasList = false;
    private int[] inputSwitchBound;
    private boolean isKeyPadUpTurn = false;
    Vector itemList = new Vector();
    private int keypadOffset = 0;
    private Rect keypadRect;
    private long lastInputTime = -1;
    private int length;
    private int listFirstRow;
    private int listHeight;
    private int listItemHeight;
    private int listSelect;
    private int listShowCount;
    private int listWidth;
    private GTVM listenVM;
    private boolean multiLine = false;
    boolean newinput = false;
    boolean replace = false;
    private int scbox;
    private int scboxh;
    private int scboxy;
    private int sch;
    private int scx;
    private int scy;
    public boolean showKeypad = false;
    private boolean showList = false;
    private String text = "";

    public TextField(String name, AbstractLowUI parent, int length2, int style) {
        boolean z;
        super(name, parent, style);
        int[] iArr = new int[4];
        iArr[1] = 1;
        iArr[2] = 2;
        iArr[3] = 3;
        this.inputSwitchBound = iArr;
        this.borderColor = Tool.CLR_VERYDARK;
        if ((style & 64) != 0) {
            setInputMode(2);
            this.canSwitchInputMode = false;
        } else if ((style & 2) != 0) {
            setInputMode(0);
            this.canSwitchInputMode = false;
        } else {
            setInputMode(0);
            if (!((style & 32) == 0 && (33554432 & style) == 0)) {
                int[] iArr2 = new int[3];
                iArr2[1] = 1;
                iArr2[2] = 2;
                this.inputSwitchBound = iArr2;
            }
        }
        if ((style & 524288) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.multiLine = z;
        this.length = length2;
        if ((262144 & style) != 0) {
            this.hasList = true;
        }
        if ((style & 2) == 0 && (style & 524288) == 0) {
            this.hasKeyPad = true;
        }
    }

    private void computeListSize() {
        String str;
        this.listWidth = this.width;
        if (this.listShowCount < this.itemList.size()) {
        }
        for (int i = 0; i < this.itemList.size(); i++) {
            if (this.itemList.elementAt(i) instanceof String) {
                str = (String) this.itemList.elementAt(i);
            } else {
                str = ((MovingString) this.itemList.elementAt(i)).getString();
            }
            if (Utilities.font.stringWidth(str) + ((this.borderWidth + this.hMargin) * 2) > this.listWidth) {
                this.itemList.setElementAt(new MovingString(str, (this.listWidth - 8) - 12, 2), i);
            }
        }
        if (this.itemList.size() <= 3) {
            this.listShowCount = this.itemList.size();
        } else {
            this.listShowCount = 3;
        }
        this.listFirstRow = 0;
        this.listSelect = 0;
        this.listItemHeight = Utilities.LINE_HEIGHT + 4 + 10;
        this.listHeight = (this.listShowCount * this.listItemHeight) + 12 + 10;
    }

    /* access modifiers changed from: protected */
    public void layout() {
        if (this.hasList) {
            computeListSize();
        }
        int dx = this.x + this.borderWidth;
        if (Tool.touchImage.getFrameWidth(0) + dx > World.viewWidth) {
            this.keypadOffset = (World.viewWidth - Tool.touchImage.getFrameWidth(0)) - dx;
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
        this.editLocal = text2.length();
        computSize();
    }

    public void saveCurrText() {
        if (this.hasList && (this.style & 2) == 0 && !this.itemList.contains(this.text)) {
            this.itemList.addElement(this.text);
        }
    }

    public void addListItem(String item) {
        if (this.hasList && !this.itemList.contains(item)) {
            this.itemList.insertElementAt(item, 0);
            computeListSize();
        }
    }

    public void switchInputMode() {
        this.inputMode++;
        if (this.inputMode >= this.inputSwitchBound.length) {
            this.inputMode = 0;
        }
    }

    public void handleKey() {
        boolean z;
        long time = System.currentTimeMillis();
        if (!this.showList) {
            if (Utilities.isKeyPressed(4, false) && this.hasList && this.itemList.size() > 0) {
                if (this.showList) {
                    z = false;
                } else {
                    z = true;
                }
                this.showList = z;
                Utilities.clearKeyStates(4);
            }
            if ((this.style & 2) == 0) {
                int keyCode = 11;
                while (true) {
                    if (keyCode > 49) {
                        break;
                    } else if (keyCode == 21 || keyCode == 22 || ((getInputMode() == 2 && keyCode > 20) || !Utilities.isKeyPressed(keyCode, false))) {
                        keyCode++;
                    } else {
                        if (this.currInputKey != keyCode || time - this.lastInputTime >= 1000 || (this.style & 64) != 0 || getInputMode() == 2) {
                            this.currInputKey = keyCode;
                            this.currInputKeyCount = 0;
                            this.replace = false;
                            this.newinput = true;
                        } else {
                            this.replace = true;
                            this.currInputKeyCount++;
                        }
                        this.lastInputTime = time;
                        if (this.text.length() >= this.length) {
                            this.currInputKey = -1;
                            return;
                        }
                        char c = getKey(keyCode, this.currInputKeyCount);
                        if (this.text.equals("")) {
                            this.text = String.valueOf(this.text) + c;
                            if (!this.replace) {
                                this.editLocal++;
                            }
                        } else {
                            String ftxt = this.text.substring(0, this.replace ? this.editLocal - 1 : this.editLocal);
                            String etxt = "";
                            if (this.editLocal != this.text.length()) {
                                etxt = this.text.substring(this.editLocal, this.text.length());
                            }
                            this.text = String.valueOf(ftxt) + c + etxt;
                            if (!this.replace) {
                                this.editLocal++;
                            }
                        }
                    }
                }
                if (Utilities.isKeyPressed(8, true)) {
                    if (this.editLocal != 0) {
                        this.replace = false;
                        this.lastInputTime = -1;
                        String ftxt2 = this.text.substring(0, this.editLocal - 1);
                        String etxt2 = "";
                        if (this.editLocal != this.text.length()) {
                            etxt2 = this.text.substring(this.editLocal, this.text.length());
                        }
                        this.text = String.valueOf(ftxt2) + etxt2;
                        this.editLocal--;
                    }
                } else if (Utilities.isKeyPressed(4, true)) {
                    Tool.openSysInput("", this.text, this.length, this, getInputMode());
                }
            }
        } else if (Utilities.isKeyPressed(0, true)) {
            this.listSelect--;
            if (this.listSelect < 0) {
                this.listSelect = this.itemList.size() - 1;
            }
            if (this.listSelect < this.listFirstRow) {
                this.listFirstRow = this.listSelect;
            }
            if (this.listSelect >= this.listFirstRow + this.listShowCount) {
                this.listFirstRow = (this.listSelect - this.listShowCount) + 1;
            }
        } else if (Utilities.isKeyPressed(1, true)) {
            this.listSelect++;
            if (this.listSelect >= this.itemList.size()) {
                this.listSelect = 0;
            }
            if (this.listSelect < this.listFirstRow) {
                this.listFirstRow = this.listSelect;
            }
            if (this.listSelect >= this.listFirstRow + this.listShowCount) {
                this.listFirstRow = (this.listSelect - this.listShowCount) + 1;
            }
        } else if (Utilities.isKeyPressed(10, true)) {
            this.showList = false;
        } else if (Utilities.isKeyPressed(9, true) || Utilities.isKeyPressed(4, true)) {
            if (this.itemList.elementAt(this.listSelect) instanceof String) {
                this.text = (String) this.itemList.elementAt(this.listSelect);
            } else {
                this.text = ((MovingString) this.itemList.elementAt(this.listSelect)).getString();
            }
            this.showList = false;
            this.editLocal = this.text.length();
        }
    }

    private boolean openKeyBoard(int px, int py) {
        int offIcon = 0;
        if (this.hasList) {
            offIcon = 12;
        }
        if (!this.hasKeyPad || this.showKeypad || px < ((this.x + this.width) - 17) - offIcon || px > (this.x + this.width) - offIcon || py < this.y || py > this.y + this.height) {
            return false;
        }
        this.showKeypad = true;
        initSoftKey();
        if (this.showList) {
            this.showList = false;
        }
        return true;
    }

    private boolean pointList(int px, int py) {
        if (this.listShowCount < this.itemList.size()) {
            this.drawScroll = true;
        }
        int starty = this.y + this.height;
        if ((this.style & 134217728) != 0) {
            starty = (starty - this.height) - this.listHeight;
        }
        if (px >= this.x && px <= (this.x + this.listWidth) - 18 && py > starty && py < this.listHeight + starty) {
            this.listSelect = this.listFirstRow + ((py - starty) / (this.listItemHeight + 2));
            if (this.listSelect < 0) {
                this.listSelect = 0;
            } else if (this.listSelect >= this.itemList.size()) {
                this.listSelect = this.itemList.size() - 1;
            }
            if (this.itemList.elementAt(this.listSelect) instanceof String) {
                this.text = (String) this.itemList.elementAt(this.listSelect);
            } else {
                this.text = ((MovingString) this.itemList.elementAt(this.listSelect)).getString();
            }
            Utilities.keyPressed(5, true);
            return true;
        } else if (px < (this.x + this.listWidth) - 18 || px > this.x + this.listWidth || !this.drawScroll || py <= this.scy || py >= this.sch + this.scy) {
            return false;
        } else {
            if (py < this.scboxy) {
                Utilities.keyPressed(1, true);
            } else if (py > this.scboxy + this.scboxh) {
                Utilities.keyPressed(2, true);
            }
            return true;
        }
    }

    private boolean pointKeyBoard(int px, int py) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void paint(Graphics g, int offx, int offy) {
        String menuStr;
        int i;
        g.setClip(this.x + offx, this.y + offy, this.width + 1, this.height + 1);
        int x = this.x + offx;
        int y = this.y + offy;
        if (this.hasFocus) {
            this.borderColor = Tool.CLR_TABLE[11];
            this.frontColor = 16645839;
            this.bgColor = 0;
        } else {
            this.frontColor = 14928143;
            this.bgColor = 0;
        }
        String text2 = new String(this.text);
        if ((this.style & 32) != 0) {
            text2 = "";
            int fin = this.text.length();
            if (this.newinput || this.replace) {
                fin--;
            }
            if (fin < 0) {
                fin = 0;
            }
            for (int i2 = 0; i2 < fin; i2++) {
                text2 = String.valueOf(text2) + "*";
            }
            if (this.text.length() > 0 && (this.newinput || this.replace)) {
                text2 = String.valueOf(text2) + this.text.charAt(fin);
            }
        }
        if ((this.replace || this.newinput) && System.currentTimeMillis() - this.lastInputTime > 1000) {
            this.replace = false;
            this.newinput = false;
        }
        int dx = x + this.borderWidth;
        int dy = y + this.borderWidth;
        int dw = this.width - (this.borderWidth * 2);
        int dh = this.height - (this.borderWidth * 2);
        if (this.hasFocus) {
        }
        Tool.drawBlurRect(g, dx, dy, dw, dh, this.hasFocus ? 1 : 0);
        g.setColor(this.frontColor);
        g.setClip(this.borderWidth + x + this.hMargin, this.borderWidth + y + this.vMargin, ((this.width - (this.hMargin * 2)) - (this.borderWidth * 2)) - (this.hasList ? 20 : 0), (this.height - (this.vMargin * 2)) - (this.borderWidth * 2));
        int lineHeight = Utilities.CHAR_HEIGHT + (this.vMargin * 2);
        if (this.multiLine) {
            String[] ds = Utilities.formatText(text2, this.width - 5, Utilities.font);
            for (int i3 = 0; i3 < ds.length; i3++) {
                g.drawString(ds[i3], this.borderWidth + x + this.hMargin, this.vMargin + y + this.borderWidth + (lineHeight * i3), 20);
            }
        } else {
            g.drawString(text2, this.borderWidth + x + this.hMargin, this.vMargin + y + this.borderWidth, 20);
        }
        if (this.hasFocus && !this.showList && (this.style & 2) == 0) {
            int cursorx = this.borderWidth + x + this.hMargin + this.font.stringWidth(text2.substring(0, this.editLocal));
            int time = (int) (System.currentTimeMillis() / 400);
            int cury = this.vMargin + y + this.borderWidth + 1;
            if (time % 2 == 0) {
                if (this.multiLine) {
                    String[] ds2 = Utilities.formatText(text2, this.width - 5, Utilities.font);
                    if (ds2.length > 0) {
                        cursorx = this.borderWidth + x + this.hMargin + this.font.stringWidth(ds2[ds2.length - 1]);
                        y += (ds2.length - 1) * lineHeight;
                    }
                }
                g.fillRect(cursorx, cury, 1, Utilities.CHAR_HEIGHT);
            }
        }
        if (this.hasList && this.itemList.size() > 0) {
            g.setClip((this.width + x) - 20, y, this.width - (this.width - 20), this.height);
            int i4 = (this.width + x) - (20 / 2);
            int i5 = (this.height / 2) + y;
            if (this.hasFocus) {
                i = 16;
            } else {
                i = 32;
            }
            Tool.drawArrow(g, i4, i5, i | 3);
        }
        if (this.showList) {
            int ly = y + this.height;
            if ((this.style & 134217728) != 0 && this.listHeight + ly > this.parent.y + this.parent.height) {
                ly = y - this.listHeight;
            }
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            UI.drawRectPanel(g, x, ly, this.listWidth, this.listHeight, true);
            int miy = ly + 6;
            int listWidth2 = this.listWidth;
            if (this.listShowCount < this.itemList.size()) {
                this.drawScroll = true;
                int listWidth3 = listWidth2 - 18;
            } else {
                this.drawScroll = false;
            }
            int i6 = this.listFirstRow;
            while (i6 < this.listFirstRow + this.listShowCount) {
                boolean isMSString = false;
                if (this.itemList.elementAt(i6) instanceof String) {
                    menuStr = (String) this.itemList.elementAt(i6);
                } else {
                    menuStr = ((MovingString) this.itemList.elementAt(i6)).getString();
                    isMSString = true;
                }
                int ftClr = Tool.CLR_TABLE[10];
                int bgClr = Tool.CLR_TABLE[11];
                if (i6 == this.listSelect) {
                    bgClr = Tool.CLR_TABLE[7];
                    ftClr = Tool.CLR_TABLE[8];
                }
                int dlx = x + 6 + 5;
                int dly = miy + 2;
                UI.drawRadio(g, dlx, (dly - 3) + (this.listItemHeight >> 1), i6 == this.listSelect, 4);
                int dlx2 = dlx + 8;
                if (isMSString) {
                    MovingString ms = (MovingString) this.itemList.elementAt(i6);
                    if (i6 == this.listSelect) {
                        ms.cycle();
                    } else {
                        ms.reset();
                    }
                    ms.draw3D(g, dlx2, dly, ftClr, bgClr, 20);
                } else {
                    Tool.draw3DString(g, menuStr, dlx2, dly, ftClr, bgClr, 20);
                }
                miy += this.listItemHeight;
                i6++;
            }
            if (this.drawScroll) {
                this.scy = ly + 3;
                this.scx = (this.width + x) - 17;
                this.sch = this.listHeight;
                this.scbox = (this.sch - 32) / this.itemList.size();
                this.scboxh = (this.scbox * this.listShowCount) + 1;
                this.scboxy = this.scy + (this.scbox * this.listFirstRow);
                int i7 = Tool.CLR_TABLE[6];
                int i8 = Tool.CLR_TABLE[4];
                if (this.listFirstRow == this.itemList.size() - this.listShowCount) {
                    this.scboxy = ((this.scy + this.sch) - this.scboxh) - 16;
                } else {
                    this.scboxy += (this.listFirstRow * ((((this.sch - 32) - (this.scbox * this.itemList.size())) * 1000) / (this.itemList.size() - this.listShowCount))) / 1000;
                    this.scboxy += 16;
                }
                g.drawImage(Tool.img_scroll[0], this.scx, this.scy);
                g.drawImage(Tool.img_scroll[1], this.scx, this.scy + this.sch, 32);
                Tool.drawBlurRect(g, this.scx, this.scy + 16, 16, this.sch - 32, 1);
                ScrollBar.drawBlock(g, this.scx, this.scboxy, this.scboxh);
            }
        }
        if (!this.hasFocus || (this.style & 2) != 0 || (this.style & 524288) == 0) {
        }
    }

    public void initSoftKey() {
        Tool.openSysInput("", this.text, this.length, this, getInputMode());
    }

    public Rect computSize() {
        if (!this.uilLayout.hGrib) {
            this.width = this.font.stringWidth(String.valueOf(this.text) + " ") + (this.borderWidth * 2) + (this.hMargin * 2);
        }
        if (!this.uilLayout.vGrib) {
            if (this.multiLine) {
                this.height = (Utilities.LINE_HEIGHT + (this.borderWidth * 2) + (this.vMargin * 2)) * 2;
            } else {
                this.height = Utilities.LINE_HEIGHT + (this.borderWidth * 2) + (this.vMargin * 2);
            }
        }
        if (this.hasList && !this.uilLayout.hGrib) {
            this.width += 25;
        }
        return new Rect(this.width, this.height);
    }

    public void setListenVM(GTVM listenVM2) {
        this.listenVM = listenVM2;
    }

    public void commandAction(Command cmd, Displayable display) {
        if (cmd.getCommandType() == 4) {
            this.text = cmd.getInputText();
            this.editLocal = this.text.length();
        }
        if (this.listenVM != null) {
            this.listenVM.continueProcess(0);
        }
        MonsterMIDlet.instance.setContentView(World.instance);
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length2) {
        this.length = length2;
    }

    public int getInputMode() {
        return this.inputSwitchBound[this.inputMode];
    }

    public void lostFocus() {
        this.showList = false;
        if (this.hasKeyPad) {
            this.showKeypad = true;
        }
    }

    public void setShowList(boolean show) {
        this.showList = show;
        if (this.showKeypad) {
            this.showKeypad = false;
        }
    }

    private boolean openList(int px, int py) {
        if (!this.hasList || this.showList || px <= this.x || px >= this.x + this.width || py <= this.y || py >= this.y + this.height) {
            return false;
        }
        setShowList(true);
        return true;
    }

    public boolean pointerCheck(int px, int py) {
        boolean ret1;
        if (this.x > px || px > this.x + this.width || this.y - Utilities.LINE_HEIGHT > py || py > this.y + this.height) {
            ret1 = false;
        } else {
            ret1 = true;
        }
        boolean ret2 = false;
        if (!this.hasFocus) {
            return super.pointerCheck(px, py);
        }
        if ((this.style & 67108864) != 0) {
            if (ret1) {
                Utilities.keyPressed(5, true);
            }
        } else if (this.showList) {
            ret2 = openKeyBoard(px, py);
            if (!ret2) {
                ret2 = pointList(px, py);
            }
        } else if (this.showKeypad) {
            if (this.itemList.size() > 0) {
                ret2 = openList(px, py);
            }
            if (!ret2 && ret1) {
                Tool.openSysInput("", getText(), getLength(), this, getInputMode());
            }
        } else {
            ret2 = openKeyBoard(px, py);
            if (!ret2 && this.itemList.size() > 0) {
                ret2 = openList(px, py);
            }
            if (!ret2 && ret1) {
                Tool.openSysInput("", getText(), getLength(), this, getInputMode());
            }
        }
        if (ret2 || ret1) {
            return true;
        }
        return false;
    }
}
