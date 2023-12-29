package com.sumsharp.monster.ui;

import com.sumsharp.lowui.Form;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.gtvm.GTVM;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.net.GZIP;
import javax.microedition.lcdui.Graphics;

public class VMUI {
    public static final int CMDTYPE_ASSIGNTASK = 4;
    public static final int CMDTYPE_BUTTON_NORMAL = 2;
    public static final int CMDTYPE_CREATEACTOR = 64;
    public static final int CMDTYPE_DISABLE_LEFT = 65536;
    public static final int CMDTYPE_DISABLE_RIGHT = 131072;
    public static final int CMDTYPE_ICON = 1;
    public static final int CMDTYPE_MAIL = 512;
    public static final int CMDTYPE_MATE = 128;
    public static final int CMDTYPE_MENURETURN = 256;
    public static final int CMDTYPE_RETURN = 32;
    public static final int CMDTYPE_SUBMITTASK = 8;
    public static final int CMDTYPE_SUBMITTASK_UNFINISH = 16;
    public static final byte FEATURE_COMMEND = 0;
    public static final byte FEATURE_GOLD_PRICE = 3;
    public static final byte FEATURE_I_PRICE = 2;
    public static final byte FEATURE_REBATE = 1;
    public static final byte TIP_MOVING_STRING = 0;
    public static final byte TIP_SCROLL_TEXT = 1;
    public static int[] arrowDy = null;
    public static int csize = 0;
    public static long drawExecuteTime = 0;
    public static final int itemStep = 2;
    public String ID;
    private boolean bCloseable = true;
    private boolean bClosed;
    private boolean clearKeyStatus = true;
    private int contentDx;
    private int contentDy;
    private int contentHeight;
    private int contentWidth;
    private boolean drawBackground;
    private boolean drawMenuBar;
    public Form form;
    private boolean fullScreen;
    private GTVM gtvm;
    private int height;
    private boolean inited = false;
    public byte lastMessageBtnSel = 0;
    private int lineHeight = (Utilities.CHAR_HEIGHT + 6);
    private MessageDialogue message;
    private boolean transparent;
    private int width;
    private int x;
    private int y;

    static {
        int[] iArr = new int[4];
        iArr[1] = 2;
        iArr[2] = 3;
        iArr[3] = 2;
        arrowDy = iArr;
    }

    public VMUI(String id, byte[] data, byte[] etdData) throws Exception {
        this.ID = id;
        this.gtvm = new GTVM(this);
        this.gtvm.init(GZIP.inflate(data));
    }

    public void initVMUI(boolean hasContent, boolean transparent2, int fillClr, boolean clear) {
        this.x = Utilities.uiX;
        this.y = Utilities.uiY;
        this.width = Utilities.uiWidth;
        this.height = Utilities.uiHeight;
        this.transparent = transparent2;
        this.drawBackground = true;
        this.drawMenuBar = hasContent;
        this.clearKeyStatus = clear;
        this.inited = true;
        this.fullScreen = false;
        updateContentYBounds();
    }

    public void setCloseable(boolean ca) {
        this.bCloseable = ca;
    }

    public boolean isCloseable() {
        if (this.gtvm == null || !this.gtvm.isBlock()) {
            return this.bCloseable;
        }
        return false;
    }

    public void setBounds(int xx, int yy, int ww, int hh) {
        this.x = Utilities.uiX + xx;
        this.y = Utilities.uiY + yy;
        this.width = ww;
        this.height = hh;
    }

    public void setContentXBounds(int conX, int conW, boolean affectBox) {
        if (conX <= 0) {
            this.contentDx = 0;
        } else {
            this.contentDx = conX;
        }
        if (conW <= 0) {
            this.contentWidth = (this.width - this.contentDx) - 20;
        } else {
            this.contentWidth = conW;
        }
    }

    private void updateContentYBounds() {
    }

    public void processPacket() {
        if (this.gtvm != null && !this.bClosed) {
            this.gtvm.execute(2);
        }
    }

    public void cycleUI() {
        if (!this.bClosed) {
            if (this.message != null) {
                this.message.cycle();
                this.lastMessageBtnSel = (byte) (this.message.buttonSelect + 1);
                if (this.message.isClosed()) {
                    this.message = null;
                }
            } else {
                this.lastMessageBtnSel = 0;
            }
            if (this.form != null) {
                this.form.handleKey();
            }
            if (this.gtvm != null) {
                this.gtvm.execute(3);
            }
        }
    }

    public void cycle() {
        if (this.inited && !this.bClosed && this.gtvm != null) {
            this.gtvm.execute(1);
        }
    }

    public void destroy() {
        if (this.gtvm != null) {
            this.gtvm.execute(5);
            this.gtvm.destroy();
            this.gtvm = null;
        }
    }

    public void initGTVM() {
        this.gtvm.execute(0);
    }

    public void drawDefaultBackground(Graphics g) {
        for (int y2 = 0; y2 < World.viewHeight; y2++) {
            if ((y2 & 1) == 0) {
                g.setColor(3902163);
            } else {
                g.setColor(2784465);
            }
            g.fillRect(0, y2, World.viewWidth, 1);
        }
        Tool.drawBoxNew(g, 0, 0, World.viewWidth, World.viewHeight, Tool.STYLE_MENU);
    }

    public void draw(Graphics g) {
        if (!this.bClosed) {
            if (!CommonComponent.drawMessageOnly) {
                if (isFullScreen()) {
                    g.setClip(this.x, this.y, this.width, this.height);
                } else {
                    g.setClip(0, 0, World.viewWidth, World.viewHeight);
                }
                if (!this.transparent && this.drawBackground) {
                    drawDefaultBackground(g);
                }
                if (isFullScreen()) {
                    g.setClip(this.x, this.y, this.width, this.height);
                } else {
                    g.setClip(0, 0, World.viewWidth, World.viewHeight);
                }
                if (this.gtvm != null) {
                    this.gtvm.execute(4);
                }
            }
            if (this.message != null) {
                this.message.draw(g);
            }
        }
    }

    public void close() {
        this.bClosed = true;
        if (!this.transparent) {
            ImageLoadManager.release("/uiBg.png");
        }
    }

    public boolean isClosed() {
        return this.bClosed;
    }

    public int[] getContentBounds() {
        return new int[]{this.x + 3 + 3, this.y + 1 + 3, this.width - 6, this.height - 6};
    }

    public void showMessage(String title, int icon, String msg, int bClosable, boolean bAutoClose, boolean needNotify) {
        this.message = CommonComponent.createMessage(title, icon, msg, bClosable != 0, bClosable == 1, bAutoClose);
        if (needNotify) {
            this.message.setListenVM(this.gtvm);
        }
    }

    public void showMessage(String title, int icon, String msg, byte button, String[] text, boolean needNotify) {
        this.message = CommonComponent.createMessage(title, icon, msg, true, false, false, button, text);
        if (needNotify) {
            this.message.setListenVM(this.gtvm);
        }
    }

    public boolean isMessageShown() {
        return this.message != null;
    }

    public void closeMessage() {
        this.message = null;
    }

    public void setMessageLines(int lineNum, int my) {
        if (this.message != null) {
            this.message.setShowLines(lineNum, my);
        }
    }

    public void setNonModal() {
        this.message.setNonModal();
    }

    public boolean isTransparent() {
        return this.transparent;
    }

    public void setTransparent(boolean transparent2) {
        this.transparent = transparent2;
    }

    public void setDrawBackground(boolean drawBackground2) {
        this.drawBackground = drawBackground2;
    }

    public boolean isDrawMenuBar() {
        return this.drawMenuBar;
    }

    public void setDrawMenuBar(boolean drawMenuBar2) {
        this.drawMenuBar = drawMenuBar2;
    }

    public boolean isClearKeyStatus() {
        return this.clearKeyStatus;
    }

    public void setClearKeyStatus(boolean clearKeyStatus2) {
        this.clearKeyStatus = clearKeyStatus2;
    }

    public boolean isFullScreen() {
        return this.fullScreen;
    }

    public void setFullScreen(boolean fullScreen2) {
        this.fullScreen = fullScreen2;
    }
}
