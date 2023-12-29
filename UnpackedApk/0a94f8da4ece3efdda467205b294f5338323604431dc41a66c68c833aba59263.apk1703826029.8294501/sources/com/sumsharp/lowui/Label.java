package com.sumsharp.lowui;

import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import javax.microedition.lcdui.Graphics;

public class Label extends AbstractLowUI {
    private int iconId;
    private String text = "";

    public Label(String name, AbstractLowUI parent, int style) {
        super(name, parent, style);
        this.borderColor = Tool.CLR_VERYDARK;
        this.bgColor = Tool.CLR_VERYDARK;
        this.frontColor = Tool.CLR_TEXT_LIGHT;
        setEnabled(false);
    }

    /* access modifiers changed from: protected */
    public void layout() {
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public void handleKey() {
    }

    public Rect computSize() {
        if (!this.uilLayout.hGrib) {
            if (this.name == null || !this.name.equals("icon")) {
                this.width = this.font.stringWidth(this.text) + (this.borderWidth * 2) + (this.hMargin * 2) + 5;
            } else {
                this.width = Tool.uiMiscImg.getFrameWidth(this.iconId) + (this.borderWidth * 2) + (this.hMargin * 2);
            }
        }
        if (!this.uilLayout.vGrib) {
            this.height = Utilities.CHAR_HEIGHT + (this.borderWidth * 2) + (this.vMargin * 2);
        }
        return new Rect(this.width, this.height);
    }

    /* access modifiers changed from: protected */
    public void paint(Graphics g, int offx, int offy) {
        int x = this.x + offx;
        int y = (this.y + offy) - 4;
        if (this.name == null || !this.name.equals("icon")) {
            g.setClip(x, y, this.width, this.height);
            Graphics graphics = g;
            UI.drawSmallPanel(graphics, x + 1, (this.height + y) - 10, this.width - 2, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
            g.setColor(this.frontColor);
            Tool.draw3DString(g, this.text, this.borderWidth + x + this.hMargin + 1, (this.height + y) - 5, Tool.clr_androidTextDefault, 0, 36);
            return;
        }
        g.setClip(x, y, this.width, this.height);
        Tool.uiMiscImg.drawFrame(g, this.iconId, x + (this.width >> 1), y + (this.height >> 1), 0, 3);
    }

    public int getIconId() {
        return this.iconId;
    }

    public void setIconId(int iconId2) {
        this.iconId = iconId2;
    }
}
