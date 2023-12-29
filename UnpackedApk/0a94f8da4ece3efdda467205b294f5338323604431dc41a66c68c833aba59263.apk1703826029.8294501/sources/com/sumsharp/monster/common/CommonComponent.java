package com.sumsharp.monster.common;

import com.sumsharp.monster.net.UWAPSegment;
import com.sumsharp.monster.ui.MessageDialogue;
import com.sumsharp.monster.ui.UIManagerPanel;
import javax.microedition.lcdui.Graphics;

public class CommonComponent {
    public static boolean drawMessageOnly = false;
    public static MessageDialogue message;
    private static UIManagerPanel uiPanel;

    public static void cycle() {
        if (message != null) {
            message.cycle();
            if (message.isClosed()) {
                message = null;
            }
        }
        if (uiPanel != null) {
            uiPanel.cycle();
        }
    }

    public static void draw(Graphics g) {
        if (uiPanel != null) {
            uiPanel.draw(g);
        }
        if (message != null) {
            message.draw(g);
        }
    }

    public static void handleSegment(UWAPSegment segment) {
        if (uiPanel != null) {
            uiPanel.handleSegment(segment);
        }
    }

    public static void setUIPanel(UIManagerPanel ui) {
        uiPanel = ui;
    }

    public static UIManagerPanel getUIPanel() {
        return uiPanel;
    }

    public static void showMessage(String title, int icon, String msg, boolean bClosable, boolean bAnyKeyClose, boolean bAutoClose) {
        message = createMessage(title, icon, msg, bClosable, bAnyKeyClose, bAutoClose);
    }

    public static void showMessage(String msg, byte buttons, String[] buttonText) {
        message = createMessage("测试", -1, msg, true, false, false, buttons, buttonText);
    }

    public static MessageDialogue createMessage(String title, int icon, String msg, boolean bClosable, boolean bAnyKeyClose, boolean bAutoClose, byte buttons, String[] buttonText) {
        MessageDialogue ret = new MessageDialogue(title, icon, msg, 50, 30, 5, Utilities.uiWidth - 20, Utilities.LINE_HEIGHT, null, buttons, buttonText);
        ret.setAlignType(1);
        if (bClosable) {
            if (bAnyKeyClose) {
                ret.setCloseOnAnyKey();
            } else {
                ret.setCloseOnBackKey();
            }
        }
        if (bAutoClose) {
            ret.setTimeout(3000);
        }
        return ret;
    }

    public static MessageDialogue createMessage(String title, int icon, String msg, boolean bClosable, boolean bAnyKeyClose, boolean bAutoClose) {
        MessageDialogue ret = new MessageDialogue(title, icon, msg, 50, 50, 6, Utilities.uiWidth - 20, Utilities.LINE_HEIGHT, null);
        ret.setAlignType(1);
        if (bClosable) {
            if (bAnyKeyClose) {
                ret.setCloseOnAnyKey();
            } else {
                ret.setCloseOnBackKey();
            }
        }
        if (bAutoClose) {
            ret.setTimeout(3000);
        }
        return ret;
    }

    public static void closeMessage() {
        message = null;
    }

    public static void loadUI(String uiID) {
        if (uiPanel == null || !(uiPanel instanceof UIManagerPanel)) {
            UIManagerPanel panel = new UIManagerPanel();
            setUIPanel(panel);
            panel.startLoading(uiID);
            return;
        }
        uiPanel.startLoading(uiID);
    }

    public static void loadUI(String uiID, byte[] etf) throws Exception {
        if (uiPanel == null || !(uiPanel instanceof UIManagerPanel)) {
            UIManagerPanel panel = new UIManagerPanel();
            setUIPanel(panel);
            panel.addUI(uiID, etf, null);
            return;
        }
        uiPanel.addUI(uiID, etf, null);
    }

    public static void closeAllUI() {
        closeAllUI(false);
    }

    public static void closeAllUI(boolean includeChat) {
        if (uiPanel != null && (uiPanel instanceof UIManagerPanel)) {
            uiPanel.closeAllUI(includeChat);
        }
    }

    public static boolean hasUI(String uiID) {
        if (uiPanel == null || !(uiPanel instanceof UIManagerPanel)) {
            return false;
        }
        return uiPanel.hasUI(uiID);
    }

    public static boolean doMapCycle() {
        if (uiPanel == null) {
            return true;
        }
        return uiPanel.getUICount() == 0 || uiPanel.getFirstNotTransUI() < 0;
    }

    public static boolean doDrawMap() {
        if (uiPanel == null) {
            return true;
        }
        return uiPanel.getUICount() == 0 || uiPanel.getFirstFullScrUI() < 0;
    }
}
