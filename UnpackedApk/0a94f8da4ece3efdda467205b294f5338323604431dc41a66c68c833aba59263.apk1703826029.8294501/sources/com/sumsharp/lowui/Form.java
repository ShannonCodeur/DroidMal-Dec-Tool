package com.sumsharp.lowui;

import android.util.Log;
import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.ui.VMUI;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Form extends AbstractLowUI {
    private Button backBtn;
    private int[] bgClr = {Tool.CLR_VERYDARK, Tool.CLR_TITLE};
    private int[] borderClr = Tool.CLR_BORDER[0];
    private int boxy;
    private boolean[] btnState;
    private int firstBtnWidth;
    private int firstBtnX;
    private int firstBtny;
    private String firstCmd;
    private int firstRow = 0;
    private AbstractLowUI focusUI = null;
    private Rect grid = new Rect(0, 0);
    private int lastBtnWidth;
    private int lastBtnX;
    private int lastBtny;
    private String lastCmd;
    private Button menuBtn;
    private int offsety = 0;
    private VMUI owner;
    private int scboxh;
    private int sch;
    private boolean scroll;
    private int scx;
    private int scy;
    private Hashtable table = new Hashtable();
    private int tipIdx;
    private int tipWidth = -1;
    private Vector tips = new Vector();
    private int tipx = (World.viewWidth >> 1);
    private boolean transparent;
    private int wholeHeight;

    public Form(String name, boolean transparent2) {
        super(name, null, 0);
        this.hMargin = 4;
        this.vMargin = 6;
        this.borderWidth = 3;
        this.height = 0;
        this.wholeHeight = 0;
        this.transparent = transparent2;
        this.frontColor = Tool.clr_androidTextDefault;
        this.btnState = new boolean[2];
    }

    public void addTip(String tip) {
        this.tips.addElement(tip);
    }

    public TextField getTextField(String name) {
        for (int i = 0; i < this.elements.size(); i++) {
            if (this.elements.elementAt(i) instanceof TextField) {
                TextField ui = (TextField) this.elements.elementAt(i);
                if (ui.getName().equals(name)) {
                    return ui;
                }
            }
        }
        return null;
    }

    private AbstractLowUI findPointerUI() {
        if (this.firstCmd != null && World.releasedx > this.firstBtnX && World.releasedx < this.firstBtnX + this.firstBtnWidth && World.releasedy > this.firstBtny && World.releasedy < this.firstBtny + Tool.img_leftbtn0.getHeight()) {
            Log.d("firstCmd119", "firstCmd119");
            this.menuBtn.uiKeyPressed(4);
            Utilities.clearKeyStates(9);
            CornerButton.instance.handled = true;
        }
        if (this.lastCmd != null && World.releasedx > this.lastBtnX && World.releasedx < this.lastBtnX + this.lastBtnWidth && World.releasedy > this.lastBtny && World.releasedy < this.lastBtny + Tool.img_rightbtn0.getHeight()) {
            Log.d("lastCmd132", "lastCmd132");
            this.backBtn.uiKeyPressed(4);
            Utilities.clearKeyStates(10);
            CornerButton.instance.handled = true;
        }
        if (!(World.pressedx == -1 || World.pressedy == -1)) {
            int i = 0;
            while (true) {
                if (i >= this.elements.size()) {
                    break;
                }
                AbstractLowUI ui = (AbstractLowUI) this.elements.elementAt(i);
                if (!ui.hasFocus) {
                    i++;
                } else if (ui.pointerCheck(World.pressedx, World.pressedy - this.offsety)) {
                    World.pressedx = -1;
                    World.pressedy = -1;
                    return ui;
                }
            }
            for (int i2 = 0; i2 < this.elements.size(); i2++) {
                AbstractLowUI ui2 = (AbstractLowUI) this.elements.elementAt(i2);
                if (ui2.pointerCheck(World.pressedx, World.pressedy - this.offsety)) {
                    World.pressedx = -1;
                    World.pressedy = -1;
                    return ui2;
                }
            }
        }
        return null;
    }

    public void handleKey() {
        AbstractLowUI pui = findPointerUI();
        if (pui != null && pui.isEnabled() && !pui.equals(this.focusUI)) {
            removeForce();
            this.focusUI = pui;
            setForce();
        }
        if (this.scroll && World.pressedx > this.scx && World.pressedx < this.scx + 16 && World.pressedy > this.scy && World.pressedy < this.scy + this.sch + 16) {
            if (World.pressedy < this.boxy) {
                Utilities.keyPressed(1, true);
            }
            if (World.pressedy > this.boxy + this.scboxh) {
                Utilities.keyPressed(2, true);
            }
            World.pressedx = -1;
            World.pressedy = -1;
        }
        if (this.focusUI != null) {
            this.focusUI.handleKey();
        }
        if (Utilities.isKeyPressed(0, false)) {
            if (this.focusUI != null) {
                AbstractLowUI ui = null;
                for (int i = 1; this.focusUI.gridy - i >= 0 && ui == null; i++) {
                    ui = getCanForceUIByGrid(this.focusUI.gridy - i, 0);
                }
                if (ui != null) {
                    removeForce();
                    this.focusUI = ui;
                    setForce();
                }
                if (this.scroll && ui != null && ui.y + this.offsety < this.y) {
                    this.offsety += ui.height;
                }
                Utilities.clearKeyStates(0);
            }
        } else if (Utilities.isKeyPressed(1, false)) {
            if (this.focusUI != null) {
                AbstractLowUI ui2 = null;
                for (int i2 = 1; this.focusUI.gridy + i2 < this.grid.y && ui2 == null; i2++) {
                    ui2 = getCanForceUIByGrid(this.focusUI.gridy + i2, 0);
                }
                if (ui2 != null) {
                    removeForce();
                    this.focusUI = ui2;
                    setForce();
                }
                if (this.scroll && ui2 != null && ui2.y + ui2.height + this.offsety > this.y + this.height) {
                    this.offsety -= ui2.height;
                }
                Utilities.clearKeyStates(1);
            }
        } else if (Utilities.isKeyPressed(2, false)) {
            if (this.focusUI != null) {
                AbstractLowUI ui3 = getCanForceUIByGrid(this.focusUI.gridy, this.focusUI.gridx, -1);
                if (ui3 != null) {
                    removeForce();
                    this.focusUI = ui3;
                    setForce();
                }
                Utilities.clearKeyStates(2);
            }
        } else if (Utilities.isKeyPressed(3, false)) {
            if (this.focusUI != null) {
                AbstractLowUI ui4 = getCanForceUIByGrid(this.focusUI.gridy, this.focusUI.gridx, 1);
                if (ui4 != null) {
                    removeForce();
                    this.focusUI = ui4;
                    setForce();
                }
                Utilities.clearKeyStates(2);
            }
        } else if (Utilities.isKeyPressed(9, false)) {
            if (this.firstCmd != null) {
                this.menuBtn.uiKeyPressed(4);
                Utilities.clearKeyStates(9);
            }
        } else if (Utilities.isKeyPressed(10, false) && this.lastCmd != null) {
            this.backBtn.uiKeyPressed(4);
            Utilities.clearKeyStates(10);
        }
    }

    private AbstractLowUI getCanForceUIByGrid(int gridy, int gridx) {
        for (int i = 0; i < this.elements.size(); i++) {
            AbstractLowUI ui = (AbstractLowUI) this.elements.elementAt(i);
            if (ui.gridy == gridy && ui.enabled && ui.gridx >= gridx) {
                return ui;
            }
        }
        return null;
    }

    private AbstractLowUI getUIByGrid(int gridy, int gridx) {
        for (int i = 0; i < this.elements.size(); i++) {
            AbstractLowUI ui = (AbstractLowUI) this.elements.elementAt(i);
            if (ui.gridy == gridy && ui.gridx == gridx) {
                return ui;
            }
        }
        return null;
    }

    private AbstractLowUI getCanForceUIByGrid(int gridy, int startx, int dir) {
        AbstractLowUI baseUI = null;
        int i = 0;
        while (true) {
            if (i < this.elements.size()) {
                AbstractLowUI ui = (AbstractLowUI) this.elements.elementAt(i);
                if (ui.gridy == gridy && ui.gridx == startx) {
                    baseUI = ui;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (baseUI != null) {
            AbstractLowUI ui2 = baseUI;
            while (ui2 != null && ui2.gridy == baseUI.gridy) {
                ui2 = getUIByGrid(baseUI.gridy, baseUI.gridx + dir);
                if (ui2 != null && ui2.enabled) {
                    return ui2;
                }
                dir += dir;
            }
        }
        return null;
    }

    private AbstractLowUI getGridMaxHeight(int y) {
        AbstractLowUI max = null;
        for (int x = 0; x <= this.grid.x; x++) {
            AbstractLowUI ui = (AbstractLowUI) this.table.get(new Integer(getUILayoutKey(y, x)));
            if (ui != null) {
                if (max == null) {
                    max = ui;
                } else if (max.height < ui.height) {
                    max = ui;
                }
            }
        }
        return max;
    }

    private void removeForce() {
        if (this.focusUI != null) {
            this.focusUI.hasFocus = false;
            this.focusUI.lostFocus();
        }
    }

    private void setForce() {
        if (this.focusUI != null) {
            this.focusUI.hasFocus = true;
        }
    }

    public void layout() {
        removeForce();
        initLayoutInfo();
        for (int y = 0; y < this.grid.y; y++) {
            int splitWidth = (this.width - (this.hMargin * 2)) - (this.borderWidth * 2);
            int needGrib = 0;
            for (int x = 0; x < this.grid.x; x++) {
                AbstractLowUI ui = (AbstractLowUI) this.table.get(new Integer(getUILayoutKey(y + 1, x + 1)));
                if (ui != null) {
                    if (!ui.getUilLayout().hGrib) {
                        splitWidth -= ui.width;
                    } else {
                        needGrib++;
                    }
                }
            }
            if (needGrib > 0) {
                int avgWidth = splitWidth / needGrib;
                int mod = splitWidth % needGrib;
                for (int x2 = 0; x2 < this.grid.x; x2++) {
                    AbstractLowUI ui2 = (AbstractLowUI) this.table.get(new Integer(getUILayoutKey(y + 1, x2 + 1)));
                    if (ui2 != null && ui2.getUilLayout().hGrib) {
                        ui2.width = avgWidth;
                        if (x2 == this.grid.x - 1) {
                            ui2.width += mod;
                        }
                    }
                }
            }
        }
        for (int x3 = 0; x3 < this.grid.x; x3++) {
            int splitHeight = (this.height - (this.vMargin * 2)) - (this.borderWidth * 2);
            int needGrib2 = 0;
            for (int y2 = 0; y2 < this.grid.y; y2++) {
                AbstractLowUI ui3 = (AbstractLowUI) this.table.get(new Integer(getUILayoutKey(y2 + 1, x3 + 1)));
                if (ui3 != null) {
                    if (!ui3.getUilLayout().vGrib) {
                        splitHeight -= ui3.height;
                    } else {
                        needGrib2++;
                    }
                }
            }
            if (needGrib2 > 0) {
                int avgHeight = splitHeight / needGrib2;
                int mod2 = splitHeight % needGrib2;
                for (int y3 = 0; y3 < this.grid.y; y3++) {
                    AbstractLowUI ui4 = (AbstractLowUI) this.table.get(new Integer(getUILayoutKey(y3 + 1, x3 + 1)));
                    if (ui4 != null && ui4.getUilLayout().vGrib) {
                        ui4.height = avgHeight;
                        if (y3 == this.grid.y - 1) {
                            ui4.height += mod2;
                        }
                    }
                }
            }
        }
        int topy = 0;
        for (int y4 = 0; y4 < this.grid.y; y4++) {
            for (int x4 = 0; x4 < this.grid.x; x4++) {
                AbstractLowUI ui5 = (AbstractLowUI) this.table.get(new Integer(getUILayoutKey(y4 + 1, x4 + 1)));
                if (ui5 != null) {
                    if (x4 == 0) {
                        ui5.x = this.x + this.hMargin + this.borderWidth;
                    } else {
                        AbstractLowUI left = (AbstractLowUI) this.table.get(new Integer(getUILayoutKey(y4 + 1, x4)));
                        ui5.x = left.x + left.width;
                    }
                    if (y4 == 0) {
                        ui5.y = this.y + this.vMargin + this.borderWidth;
                        topy = ui5.y + ui5.height;
                    } else {
                        AbstractLowUI top = getGridMaxHeight(y4);
                        if (top == null) {
                            top = (AbstractLowUI) this.table.get(new Integer(getUILayoutKey(y4, x4)));
                            if (top == null) {
                            }
                        }
                        ui5.y = top.y + top.height;
                        if (topy < ui5.y + ui5.height) {
                            topy = ui5.y + ui5.height;
                        }
                    }
                    ui5.gridx = x4;
                    ui5.gridy = y4;
                }
            }
        }
        this.height = ((this.vMargin + topy) + this.borderWidth) - this.y;
        this.wholeHeight = this.height;
        if (this.transparent) {
            int[] info = this.owner.getContentBounds();
            this.height = (((info[1] - this.y) + info[3]) - this.vMargin) - this.borderWidth;
            if (this.height < this.wholeHeight - 10 && !this.scroll) {
                this.scroll = true;
                this.width -= 16;
            }
        }
        this.focusUI = null;
        for (int i = 0; i < this.elements.size(); i++) {
            AbstractLowUI ui6 = (AbstractLowUI) this.elements.elementAt(i);
            if (this.focusUI == null && ui6 != null && ui6.enabled) {
                this.focusUI = ui6;
            }
            ui6.layout();
        }
        setForce();
        this.table.clear();
        if (this.menuBtn != null) {
            this.firstCmd = this.menuBtn.getText();
        }
        if (this.backBtn != null) {
            this.lastCmd = this.backBtn.getText();
        }
        initCmd(this.firstCmd, this.lastCmd);
    }

    /* access modifiers changed from: protected */
    public void initCmd(String leftCmd, String rightCmd) {
        if (leftCmd != null) {
            this.firstBtnWidth = Tool.img_leftbtn0.getWidth();
        }
        if (rightCmd != null) {
            this.lastBtnWidth = Tool.img_rightbtn0.getWidth();
        }
        if (CommonComponent.getUIPanel().hasUI(Utilities.VMUI_CHAT)) {
            int tmpx = World.viewWidth - 36;
            int tmpy = World.viewHeight - 25;
            this.lastBtnX = tmpx - (this.lastBtnWidth / 2);
            this.lastBtny = tmpy - (Tool.img_leftbtn0.getHeight() / 2);
            int tmpx2 = (World.viewWidth - CornerButton.instance.getWidth()) + 40;
            int tmpy2 = (World.viewHeight - CornerButton.instance.getHeight()) + 27;
            this.firstBtnX = tmpx2 - (this.firstBtnWidth / 2);
            this.firstBtny = tmpy2 - (Tool.img_leftbtn0.getHeight() / 2);
        } else {
            int tmpy3 = World.viewHeight - 25;
            this.firstBtnX = 36 - (this.firstBtnWidth / 2);
            this.lastBtnX = (World.viewWidth - 36) - (this.lastBtnWidth / 2);
            this.firstBtny = tmpy3 - (Tool.img_leftbtn0.getHeight() / 2);
            this.lastBtny = this.firstBtny;
        }
        this.firstCmd = leftCmd;
        this.lastCmd = rightCmd;
        CornerButton.instance.setCmd(leftCmd, rightCmd);
    }

    public void paint(Graphics g, int offx, int offy) {
        int x = this.x + offx;
        int y = this.y + offy;
        if (this.scroll) {
            int[] info = this.owner.getContentBounds();
            g.setClip(info[0], info[1], info[2], info[3]);
            this.scy = y + 10;
            this.scx = (this.width + x) - 4;
            this.sch = this.height - 3;
            this.sch -= 16 * 2;
            int scale = (this.sch * 100) / this.wholeHeight;
            int boxh = (Utilities.LINE_HEIGHT * scale) / 100;
            this.scboxh = (this.height * scale) / 100;
            int yoff = ((-this.offsety) * scale) / 100;
            this.boxy = this.scy + 16 + yoff;
            int left = (this.sch - this.scboxh) - yoff;
            if (left < boxh) {
                this.scboxh += left;
            }
            this.scboxh -= 18;
            Tool.drawBlurRect(g, this.scx, this.scy + 16, 16, this.sch - 21, 0);
            g.drawImage(Tool.img_scroll[0], this.scx, this.scy);
            g.drawImage(Tool.img_scroll[1], this.scx, (this.scy + this.sch) - 4);
            ScrollBar.drawBlock(g, this.scx, this.boxy - 2, this.scboxh);
        }
        g.setClip(x, y - 10, this.width + 1, this.height + 20);
        if (!this.transparent) {
            UI.drawTitlePanel(g, this.name, x, y - Utilities.TITLE_HEIGHT, this.width, Utilities.TITLE_HEIGHT, 8);
            UI.drawUIPanel(g, x, y, this.width, this.height + 10, 8);
            if (CommonComponent.getUIPanel().hasUI(Utilities.VMUI_CHAT)) {
                CornerButton.instance.paintUICmd(g, this.firstCmd, this.lastCmd, false);
            } else {
                CornerButton.instance.paintUICmd(g, this.firstCmd, this.lastCmd, true);
            }
        }
        AbstractLowUI forceUI = null;
        for (int i = 0; i < this.elements.size(); i++) {
            AbstractLowUI ui = (AbstractLowUI) this.elements.elementAt(i);
            if (!this.scroll || ui.y + this.offsety >= y) {
                if (this.scroll && ui.y + ui.height + this.offsety > this.height + y) {
                    break;
                } else if (ui.isHasFocus()) {
                    forceUI = ui;
                } else {
                    ui.paint(g, offx, this.offsety);
                }
            }
        }
        if (forceUI != null) {
            forceUI.paint(g, offx, this.offsety);
        }
        if (this.tips.size() > 0) {
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            int tipy = NewStage.screenY + y + this.height + 10;
            UI.drawRectTipPanel(g, 3, tipy, World.viewWidth - 6, Utilities.LINE_HEIGHT + 8, true);
            g.setClip(8, 0, World.viewWidth - 16, World.viewHeight);
            g.setColor(Tool.CLR_TABLE[10]);
            String str = (String) this.tips.elementAt(this.tipIdx);
            g.drawString(str, this.tipx, Utilities.LINE_HEIGHT + tipy + 2, 36);
            if (this.tipWidth == -1) {
                this.tipWidth = Utilities.font.stringWidth(str);
            }
            this.tipx -= 2;
            if (this.tipx + this.tipWidth < 10) {
                this.tipx = World.viewWidth >> 1;
                this.tipIdx++;
                if (this.tipIdx >= this.tips.size()) {
                    this.tipIdx = 0;
                }
            }
        }
    }

    private void initLayoutInfo() {
        this.table.clear();
        Hashtable tmp = new Hashtable();
        int maxCol = 0;
        int maxRow = 0;
        for (int i = 0; i < this.elements.size(); i++) {
            AbstractLowUI comp = (AbstractLowUI) this.elements.elementAt(i);
            comp.computSize();
            UILayout layout = comp.getUilLayout();
            Integer c = (Integer) tmp.get(new Integer(layout.row));
            if (c == null) {
                c = new Integer(0);
            }
            Integer c2 = new Integer(c.intValue() + 1);
            tmp.put(new Integer(layout.row), c2);
            if (layout.row > maxRow) {
                maxRow = layout.row;
            }
            if (c2.intValue() > maxCol) {
                maxCol = c2.intValue();
            }
            this.table.put(new Integer(getUILayoutKey(layout.row, c2.intValue())), comp);
        }
        this.grid.x = maxCol;
        this.grid.y = maxRow;
    }

    private int getUILayoutKey(int row, int col) {
        return (row << 16) | col;
    }

    public void add(AbstractLowUI component) {
        if (component instanceof Button) {
            Button btn = (Button) component;
            if (btn.isMenuBtn() && this.menuBtn == null) {
                this.menuBtn = btn;
                return;
            } else if (btn.isBackBtn() && this.backBtn == null) {
                this.backBtn = btn;
                return;
            }
        }
        super.add(component);
    }

    public boolean isTransparent() {
        return this.transparent;
    }

    public void setTransparent(boolean transparent2) {
        this.transparent = transparent2;
    }

    public AbstractLowUI getFocusUI() {
        return this.focusUI;
    }

    public void setOwner(VMUI owner2) {
        this.owner = owner2;
    }

    public void setX(int x) {
        super.setX(x);
    }
}
