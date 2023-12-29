package com.sumsharp.monster.ui;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.net.UWAPSegment;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class UIManagerPanel implements Runnable {
    public static final byte STATE_IDLE = -1;
    public static final byte STATE_REQUESTING_VMUI = 1;
    public static final byte STATE_REQUEST_VMUI = 0;
    public static final byte UI_TYPE_ACTORCHOISE = 2;
    public static final byte UI_TYPE_GAMEHALL = 5;
    public static final byte UI_TYPE_MAINMENU = 3;
    public static final byte UI_TYPE_NULL = 0;
    public static final byte UI_TYPE_ROLLPANEL = 99;
    public static final byte UI_TYPE_USERLOGIN = 1;
    public static final byte UI_TYPE_VMUI = 4;
    public int height = 0;
    private Vector loadingVMID = new Vector();
    private UIManagerPanel parent = CommonComponent.getUIPanel();
    private byte state;
    private byte type = 4;
    private Vector vmuis = new Vector();
    public int width = 0;
    public int x = 0;
    public int y = 0;

    public UIManagerPanel() {
        setState(-1, "init");
    }

    public byte getType() {
        return this.type;
    }

    public void startLoading(String vmid) {
        this.loadingVMID.addElement(vmid);
        setState(0, "startLoading");
    }

    private void setState(byte state2, String from) {
        this.state = state2;
    }

    public int getState() {
        return this.state;
    }

    public void cycle() {
        int vmcount = this.vmuis.size();
        switch (this.state) {
            case -1:
                if (vmcount <= 0 || CommonComponent.getUIPanel() != this) {
                    CommonComponent.setUIPanel(this.parent);
                    return;
                }
                boolean cycleUI = false;
                VMUI chatUI = null;
                for (int i = vmcount - 1; i >= 0; i--) {
                    VMUI mn = (VMUI) this.vmuis.elementAt(i);
                    mn.cycle();
                    if (!mn.ID.toLowerCase().equals(Utilities.VMUI_CHAT) || mn.isFullScreen() || mn.form != null) {
                        if (!cycleUI) {
                            mn.cycleUI();
                            if (chatUI != null) {
                                chatUI.cycleUI();
                                chatUI = null;
                            }
                            if (mn.isClearKeyStatus()) {
                                Utilities.clearKeyStates();
                            }
                            cycleUI = true;
                        }
                        if (mn.isClosed()) {
                            mn.destroy();
                            this.vmuis.removeElementAt(i);
                        }
                    } else if (mn.isClosed()) {
                        mn.destroy();
                        this.vmuis.removeElementAt(i);
                    } else {
                        chatUI = mn;
                    }
                }
                if (chatUI != null) {
                    chatUI.cycleUI();
                    return;
                }
                return;
            case 0:
                CommonComponent.showMessage("系统提示", -1, "正在载入...", false, false, false);
                setState(1, "STATE_REQUEST_VMUI");
                new Thread(this).start();
                return;
            default:
                return;
        }
    }

    public int getFirstNotTransUI() {
        int firstIndex = this.vmuis.size() - 1;
        while (firstIndex >= 0 && ((VMUI) this.vmuis.elementAt(firstIndex)).isTransparent()) {
            firstIndex--;
        }
        return firstIndex;
    }

    public int getFirstFullScrUI() {
        int firstIndex = this.vmuis.size() - 1;
        while (firstIndex >= 0 && !((VMUI) this.vmuis.elementAt(firstIndex)).isFullScreen()) {
            firstIndex--;
        }
        return firstIndex;
    }

    public int getUICount() {
        return this.vmuis.size();
    }

    public void draw(Graphics g) {
        int uiCount = this.vmuis.size();
        int firstIndex = getFirstNotTransUI();
        if (firstIndex < 0) {
            if (this.parent != null) {
                this.parent.draw(g);
            }
            firstIndex = 0;
        }
        while (firstIndex < uiCount) {
            ((VMUI) this.vmuis.elementAt(firstIndex)).draw(g);
            firstIndex++;
        }
    }

    public void handleSegment(UWAPSegment segment) {
        int i = this.vmuis.size() - 1;
        while (i >= 0) {
            segment.reset();
            ((VMUI) this.vmuis.elementAt(i)).processPacket();
            if (!segment.handled) {
                i--;
            } else {
                return;
            }
        }
        if (this.parent != null) {
            this.parent.handleSegment(segment);
        }
    }

    public void run() {
        int i = 0;
        while (i < this.loadingVMID.size()) {
            try {
                String str = (String) this.loadingVMID.elementAt(i);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                CommonComponent.showMessage("系统提示", -1, "载入界面错误。", true, true, true);
            }
        }
        while (this.loadingVMID.size() > 0) {
            this.loadingVMID.removeElementAt(0);
            addUI((String) this.loadingVMID.elementAt(0));
        }
        CommonComponent.closeMessage();
        setState(-1, "RUN");
    }

    private void addUI(String uiID) throws Exception {
        String etfFile;
        String etdFile;
        if (uiID.endsWith(".ui")) {
            etfFile = "/" + uiID;
            etdFile = "/" + uiID.substring(0, uiID.length() - 7) + ".etd";
        } else {
            etfFile = "/" + uiID + ".ui";
            etdFile = "/" + uiID + ".etd";
        }
        addUI(uiID, World.findResource(etfFile, true), World.findResource(etdFile, true));
    }

    public void addUI(String uiID, byte[] etfData, byte[] etdData) throws Exception {
        VMUI mn = new VMUI(uiID, etfData, etdData);
        byte[] etfData2 = null;
        byte[] etdData2 = null;
        mn.initGTVM();
        if (this.vmuis.size() <= 0) {
            this.vmuis.addElement(mn);
        } else if (((VMUI) this.vmuis.elementAt(this.vmuis.size() - 1)).ID.equals(Utilities.VMUI_CHAT)) {
            this.vmuis.insertElementAt(mn, this.vmuis.size() - 1);
        } else {
            this.vmuis.addElement(mn);
        }
        if (uiID.startsWith("ui_teammenu") && World.pressedx != -1 && World.pressedy != -1) {
            World.pressedx = -1;
            World.pressedy = -1;
        }
    }

    public boolean hasUI(String uiID) {
        for (int i = this.vmuis.size() - 1; i >= 0; i--) {
            VMUI vmui = (VMUI) this.vmuis.elementAt(i);
            if (vmui.ID.equals(uiID) && !vmui.isClosed()) {
                return true;
            }
        }
        return false;
    }

    public void closeAllUI() {
        closeAllUI(false);
    }

    public void closeAllUI(boolean includeChat) {
        for (int i = this.vmuis.size() - (includeChat ? 1 : 2); i >= 0; i--) {
            ((VMUI) this.vmuis.elementAt(i)).close();
        }
    }
}
