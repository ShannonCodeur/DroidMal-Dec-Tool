package com.sumsharp.lowui;

import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import javax.microedition.lcdui.Graphics;

public class Button extends AbstractLowUI {
    private int clrBg = Tool.CLR_LIGHT;
    private int clrBgSel = Tool.CLR_VERYDARK;
    private int clrBorder = Tool.CLR_VERYDARK;
    private int clrBorderSel = Tool.CLR_TINT;
    private int clrText = Tool.CLR_TEXT_DARK;
    private int clrTextSel = Tool.CLR_TEXT_LIGHT;
    private boolean isPressed;
    private boolean selection;
    private String text = "按钮";

    public Button(String name, AbstractLowUI parent, int style) {
        super(name, parent, style);
        this.selection = (65536 & style) != 0;
    }

    /* access modifiers changed from: protected */
    public void layout() {
    }

    /* access modifiers changed from: protected */
    public void paint(Graphics g, int offx, int offy) {
        int i;
        int x = this.x + offx;
        int y = this.y + offy;
        if (this.hasFocus) {
            this.frontColor = Tool.CLR_TABLE[8];
            this.bgColor = Tool.CLR_TABLE[7];
        } else {
            this.frontColor = Tool.clr_androidTextDefault;
            this.bgColor = Tool.CLR_TABLE[11];
        }
        g.setClip(x, y, this.width, this.height);
        g.setFont(this.font);
        g.setColor(this.frontColor);
        g.setClip(x, y, this.width, this.height + 10);
        if ((this.style & 16) != 0) {
            if (isCheck()) {
                g.setColor(this.frontColor);
                int width = this.width;
                int dx = x;
                Tool.draw3DString(g, this.text, dx + (width / 2), this.borderWidth + this.vMargin + y, this.frontColor, this.bgColor, 17);
                int dx2 = (((width >> 1) + dx) - (Utilities.font.stringWidth(this.text) >> 1)) - 12;
                g.drawRect(dx2, ((this.height / 2) + y) - 5, 10, 10);
                if (this.selection) {
                    Tool.uiMiscImg.drawFrame(g, 31, dx2 + 1, ((this.height / 2) + y) - 4);
                }
            } else {
                int width2 = this.width;
                int dx3 = x;
                int dy = y;
                if (this.hasFocus) {
                    g.drawImage(Tool.img_rectbtn1, (width2 / 2) + dx3, this.vMargin + dy + this.borderWidth, 1);
                } else {
                    g.drawImage(Tool.img_rectbtn0, (width2 / 2) + dx3, this.vMargin + dy + this.borderWidth, 1);
                }
                Tool.drawLevelString(g, this.text, dx3 + (width2 / 2), this.vMargin + dy + this.borderWidth + 12, 17, 2, this.hasFocus ? 1 : 0);
            }
        } else if (isCheck()) {
            g.setColor(this.frontColor);
            int rectx = x + 2;
            int dx4 = x + 8;
            g.drawRect(rectx, ((this.height / 2) + y) - 5, 10, 10);
            if (this.selection) {
                Tool.uiMiscImg.drawFrame(g, 31, rectx + 1, ((this.height / 2) + y) - 4);
            }
            Tool.draw3DString(g, this.text, this.borderWidth + dx4 + this.hMargin + 6, this.vMargin + y + this.borderWidth + 3, this.frontColor, this.bgColor, 20);
        } else {
            int dx5 = x + 8;
            int dy2 = y;
            if (this.hasFocus) {
                g.drawImage(Tool.img_rectbtn1, dx5, this.vMargin + dy2 + this.borderWidth, 1);
            } else {
                g.drawImage(Tool.img_rectbtn0, dx5, this.vMargin + dy2 + this.borderWidth, 1);
            }
            String str = this.text;
            int i2 = this.borderWidth + dx5 + this.hMargin + 6;
            int i3 = this.borderWidth + this.vMargin + dy2;
            if (this.hasFocus) {
                i = 1;
            } else {
                i = 0;
            }
            Tool.drawLevelString(g, str, i2, i3, 20, 2, i);
        }
        int dx6 = x;
        int dy3 = y;
        if (this.hasFocus) {
            dx6++;
            dy3++;
        }
        if (!isCheck() && isRadio()) {
            if (this.hasFocus) {
                g.setColor(this.clrBg);
            } else {
                g.setColor(this.clrBgSel);
            }
            int rectx2 = this.borderWidth + dx6 + this.hMargin + this.font.stringWidth(this.text) + 2;
            if ((this.style & 16) != 0) {
                rectx2 = ((this.width / 2) + dx6) - 5;
            }
            g.drawRoundRect(rectx2, ((this.height / 2) + dy3) - 5, 10, 10, 10, 10);
            if (this.selection) {
                g.setColor(AbstractUnit.CLR_NAME_NPC);
                g.fillRoundRect(rectx2 + 2, ((this.height / 2) + dy3) - 3, 6, 6, 6, 6);
            }
        }
    }

    public void handleKey() {
        if (Utilities.isKeyPressed(4, true)) {
            uiKeyPressed(4);
        }
    }

    public boolean pointerCheck(int px, int py) {
        if (this.x > px || px > this.x + this.width || this.y > py || py > this.y + this.height) {
            return false;
        }
        uiKeyPressed(4);
        return true;
    }

    public boolean uiKeyPressed(int keyCode) {
        if (keyCode != 4) {
            return false;
        }
        if (isCheck()) {
            this.selection = !this.selection;
        }
        if (isRadio() || isMenuBtn() || isBackBtn()) {
            this.selection = true;
            for (int i = 0; i < this.parent.elements.size(); i++) {
                if (this.parent.elements.elementAt(i) instanceof Button) {
                    Button btn = (Button) this.parent.elements.elementAt(i);
                    if (btn.isRadio() && btn != this) {
                        btn.selection = false;
                    }
                }
            }
        }
        this.isPressed = true;
        return true;
    }

    public String getText() {
        return this.text;
    }

    public boolean isSelection() {
        return this.selection;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    private boolean isCheck() {
        return (this.style & 4) != 0;
    }

    public boolean isMenuBtn() {
        return (this.style & 256) != 0;
    }

    public boolean isBackBtn() {
        return (this.style & 512) != 0;
    }

    private boolean isRadio() {
        return (this.style & 8) != 0;
    }

    public Rect computSize() {
        if (!this.uilLayout.hGrib) {
            this.width = this.font.stringWidth(this.text) + (this.borderWidth * 2) + (this.hMargin * 2) + 6;
            if (isCheck() || isRadio()) {
                this.width += 15;
            }
        }
        if (!this.uilLayout.vGrib) {
            this.height = 44;
        }
        return new Rect(this.width, this.height);
    }

    public boolean isPressed() {
        boolean ret = this.isPressed;
        if (this.isPressed) {
            this.isPressed = false;
        }
        return ret;
    }

    public void setPressed(boolean isPressed2) {
        this.isPressed = isPressed2;
    }
}
