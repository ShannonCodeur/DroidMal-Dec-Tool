package com.sumsharp.lowui;

import com.sumsharp.android.ui.ChatUI;
import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.gtvm.GTVM;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class FullChatUI extends UI implements TabHandler {
    private static String[] TIPS = {"按“确定”键可以在当前频道发言", "用数字键“1”到“5”可以选择频道", "可以在信息栏插入表情和物品信息。", "用方向键左右移动光标，按“确定”键查看物品信息。"};
    private boolean[] btnState;
    private int chatOffY;
    private int[] chatScale = null;
    private int currChannel;
    private int currSelection;
    private int height;
    private boolean lockSelection = false;
    private Menu menu;
    private String[] menuItems;
    private Chat selection;
    private boolean showItemInfo = false;
    private int showLine;
    private boolean showMenu = false;
    private int startIndex;
    private Tab tab;
    private int tipIdx = -1;
    private long tipTime;
    private int tipWidth;
    private int tipX = 0;
    private int titleHeight;
    private int titleWidth = 0;
    private int width;
    private int x;
    private int y;

    public FullChatUI(int currChannel2) {
        CommonData.player.orderBag();
        CommonData.player.setActionState(2);
        this.x = 0;
        this.y = Utilities.LINE_HEIGHT + 5;
        this.width = World.viewWidth;
        this.titleHeight = Utilities.LINE_HEIGHT + 5;
        this.height = (((World.viewHeight - this.y) - 1) - Utilities.LINE_HEIGHT) - DirectionPad.instance.getHeight();
        this.currChannel = currChannel2;
        this.chatOffY = 0;
        this.startIndex = 0;
        this.showLine = (this.height / Utilities.LINE_HEIGHT) + 1;
        for (int i = 0; i < Chat.chlNotify.length; i++) {
            Chat.chlNotify[i] = false;
        }
        setCmd(null, null);
        for (String stringWidth : Chat.CHAT_FULL_CHANNELS) {
            this.titleWidth += Utilities.font.stringWidth(stringWidth) + 14;
        }
        nextTip();
        this.tab = new Tab("全屏聊天", Chat.CHAT_FULL_CHANNELS, DirectionPad.instance.getWidth() - 4, ((World.viewWidth - DirectionPad.instance.getWidth()) - CornerButton.instance.getWidth()) + 20, false, this);
        this.tab.setY(World.viewHeight - DirectionPad.instance.getHeight());
        this.tab.setIsChat(true);
        this.tab.setContentHeight((World.viewHeight - DirectionPad.instance.getHeight()) - Utilities.LINE_HEIGHT);
        this.btnState = new boolean[2];
        Chat.toFullMode();
    }

    private void cycleTip() {
        if (this.tipWidth > this.width - 10) {
            long t = System.currentTimeMillis() - this.tipTime;
            if (t > 3000) {
                this.tipX--;
            }
            if (this.tipX + this.tipWidth >= (this.x + this.width) - 10) {
                return;
            }
            if (t < 10000) {
                this.tipX = ((this.x + this.width) - 10) - this.tipWidth;
            } else {
                nextTip();
            }
        } else if (System.currentTimeMillis() - this.tipTime > 10000) {
            nextTip();
        }
    }

    private void nextTip() {
        this.tipIdx = (this.tipIdx + 1) % TIPS.length;
        this.tipWidth = Utilities.font.stringWidth(TIPS[this.tipIdx]);
        if (this.tipWidth > this.width - 10) {
            this.tipX = 12;
        } else {
            this.tipX = ((this.width - 10) - this.tipWidth) / 2;
        }
        this.tipTime = System.currentTimeMillis();
    }

    private void initMenu() {
        this.menuItems = null;
        Chat chat = getCurrSelChat();
        Vector cmds = new Vector();
        if (this.currChannel == 0 || this.currChannel == 4) {
            cmds.addElement("地区频道");
            cmds.addElement("世界频道");
            cmds.addElement("队伍频道");
            cmds.addElement("公会频道");
        } else if (this.currChannel == 1) {
            if (chat != null) {
                int i = chat.src;
            }
        } else if (this.currChannel == 2) {
            cmds.addElement("队伍频道");
        } else if (this.currChannel == 3) {
            cmds.addElement("公会频道");
        }
        if (!(chat == null || chat.src == -1)) {
            if (chat.channel == -8) {
                if (chat.src == CommonData.player.id) {
                    cmds.addElement("悄悄话");
                } else {
                    cmds.addElement("回复");
                }
            } else if (chat.src != CommonData.player.id) {
                cmds.addElement("悄悄话");
            }
            if (chat.src != CommonData.player.id && CommonData.player.getFriend(chat.src) == null) {
                cmds.addElement("加为好友");
            }
            if (chat.src != CommonData.player.id) {
                cmds.addElement("邀请组队");
            }
            if (chat.src != CommonData.player.id) {
                cmds.addElement("公会邀请");
            }
        }
        cmds.addElement("关闭");
        this.menuItems = new String[cmds.size()];
        cmds.copyInto(this.menuItems);
        String[] mi = new String[this.menuItems.length];
        System.arraycopy(this.menuItems, 0, mi, 0, mi.length);
        this.menu = new Menu("菜单", mi, null, 8);
    }

    private int getSelectionTop() {
        int ret = 0;
        synchronized (Chat.lock) {
            for (int i = this.startIndex; i < this.currSelection; i++) {
                ret += ((Chat) getCurrCache().elementAt(i)).drawHeight;
            }
        }
        return ret;
    }

    private Chat getCurrSelChat() {
        Vector c = getCurrCache();
        if (this.currSelection < c.size()) {
            return (Chat) c.elementAt(this.currSelection);
        }
        return null;
    }

    private Vector getCurrCache() {
        if (this.currChannel == 0) {
            return Chat.msgCache;
        }
        return Chat.chlCache[this.currChannel - 1];
    }

    public void cycle() {
        String destName;
        if (this.closed) {
            this.show = false;
        } else if (this.show) {
            CornerButton.instance.cycle();
            cycleTip();
            moveBtn();
            this.lockSelection = true;
            if (this.selection != null && this.lockSelection) {
                int i = 0;
                while (true) {
                    if (i >= getCurrCache().size()) {
                        break;
                    }
                    if (((Chat) getCurrCache().elementAt(i)) == this.selection) {
                        this.currSelection = i;
                        break;
                    }
                    i++;
                }
            }
            if (this.showMenu) {
                this.menu.cycle();
                int menuSelection = this.menu.getMenuSelection();
                if (menuSelection != -1) {
                    boolean hasOpened = false;
                    String menu2 = this.menuItems[menuSelection];
                    boolean goChatForm = false;
                    if (ChatUI.instance == null) {
                        ChatUI.open();
                        hasOpened = true;
                    }
                    do {
                    } while (ChatUI.instance == null);
                    if (menu2.equals("地区频道")) {
                        ChatUI.instance.setTitle("地区频道", "", -2);
                        ChatUI.instance.setChatChannel(-2);
                        goChatForm = true;
                    } else if (menu2.equals("世界频道")) {
                        ChatUI.instance.setTitle("世界频道", "", -1);
                        ChatUI.instance.setChatChannel(-1);
                        goChatForm = true;
                    } else if (menu2.equals("队伍频道")) {
                        ChatUI.instance.setTitle("队伍频道", "", -5);
                        ChatUI.instance.setChatChannel(-5);
                        goChatForm = true;
                    } else if (menu2.equals("公会频道")) {
                        ChatUI.instance.setTitle("公会频道", "", -3);
                        ChatUI.instance.setChatChannel(-3);
                        goChatForm = true;
                    } else if (menu2.equals("悄悄话") || menu2.equals("回复")) {
                        Chat chat = getCurrSelChat();
                        if (chat.src == CommonData.player.id) {
                            destName = chat.destName;
                        } else {
                            destName = chat.srcName;
                        }
                        CommonData.player.whisperName = destName;
                        ChatUI.instance.setTitle("聊天", chat.toString(true, false), -8);
                        ChatUI.instance.setChatChannel(-8);
                        goChatForm = true;
                    } else if (menu2.equals("邀请组队")) {
                        Chat chat2 = getCurrSelChat();
                        try {
                            UWAPSegment uWAPSegment = new UWAPSegment(23, 1);
                            uWAPSegment.writeInt(chat2.src);
                            Utilities.sendRequest(uWAPSegment);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (menu2.equals("公会邀请")) {
                        Chat chat3 = getCurrSelChat();
                        try {
                            UWAPSegment uWAPSegment2 = new UWAPSegment(23, 34);
                            uWAPSegment2.writeInt(chat3.src);
                            Utilities.sendRequest(uWAPSegment2);
                        } catch (IOException e2) {
                        }
                    } else if (menu2.equals("加为好友")) {
                        Chat chat4 = getCurrSelChat();
                        try {
                            UWAPSegment uWAPSegment3 = new UWAPSegment(18, 6);
                            uWAPSegment3.writeInt(chat4.src);
                            Utilities.sendRequest(uWAPSegment3);
                        } catch (IOException e3) {
                        }
                    }
                    this.menu.close();
                    this.showMenu = false;
                    if (goChatForm) {
                        ChatUI.instance.setContent("");
                        if (!hasOpened) {
                            ChatUI.open();
                        }
                    }
                }
                if (!Utilities.isKeyPressed(9, true) && !Utilities.isKeyPressed(9, true) && Utilities.isKeyPressed(10, true)) {
                    this.menu.close();
                    this.showMenu = false;
                }
            } else {
                this.tab.cycle();
                handlePoint();
                for (int i2 = 12; i2 <= 20; i2++) {
                    if (Utilities.isKeyPressed(i2, true)) {
                        int idx = (i2 - 1) - 11;
                        if (idx >= 0 && idx < Chat.CHAT_FULL_CHANNELS.length) {
                            this.currChannel = idx;
                        }
                    }
                }
                if (!Utilities.isKeyPressed(22, true)) {
                    if (Utilities.isKeyPressed(21, true)) {
                        this.showItemInfo = false;
                    } else if (Utilities.isKeyPressed(0, true)) {
                        this.currSelection--;
                        if (this.currSelection < 0) {
                            this.currSelection = getCurrCache().size() - 1;
                        }
                        this.showItemInfo = false;
                    } else if (Utilities.isKeyPressed(1, true)) {
                        this.currSelection++;
                        if (this.currSelection >= getCurrCache().size()) {
                            this.currSelection = 0;
                        }
                        this.showItemInfo = false;
                    } else if (Utilities.isKeyPressed(3, true)) {
                        Chat chat5 = getCurrSelChat();
                        if (chat5 != null) {
                            chat5.context.nextActiveInfo();
                        }
                        if (!chat5.context.getActiveInfo().equals("")) {
                            this.showItemInfo = true;
                        } else {
                            this.showItemInfo = false;
                        }
                    } else if (Utilities.isKeyPressed(2, true)) {
                        Chat chat6 = getCurrSelChat();
                        if (chat6 != null) {
                            chat6.context.setActiveInfo("");
                        }
                        this.showItemInfo = false;
                    } else if (Utilities.isKeyPressed(10, true)) {
                        close();
                        World.fullChatUI = null;
                        this.showItemInfo = false;
                        CommonData.player.setActionState(0);
                        Chat.toBottomMode();
                    } else if (Utilities.isKeyPressed(9, true) || Utilities.isKeyPressed(9, true)) {
                        initMenu();
                        this.showMenu = true;
                        this.showItemInfo = false;
                    } else if (Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(16, true)) {
                        Chat chat7 = getCurrSelChat();
                        if (chat7 != null) {
                            if (!chat7.context.getActiveInfo().equals("")) {
                                this.showItemInfo = true;
                            } else {
                                gotoForm(chat7);
                            }
                        }
                    }
                }
            }
            World.pressedx = -1;
            World.pressedy = -1;
            Chat.chlNotify[this.currChannel] = false;
            if (this.currSelection < 0 || this.currSelection >= getCurrCache().size()) {
                this.currSelection = 0;
            }
            if (this.currSelection < this.startIndex) {
                this.startIndex = this.currSelection;
                this.chatOffY = 0;
            }
            if (this.startIndex + this.showLine < this.currSelection) {
                this.startIndex = (this.currSelection - this.showLine) + 1;
                this.chatOffY = 0;
            }
            if (getCurrCache().size() > 0) {
                int t = getSelectionTop();
                Chat chat8 = (Chat) getCurrCache().elementAt(this.currSelection);
                this.selection = chat8;
                int height2 = this.height - 6;
                int y2 = height2;
                if (chat8.drawHeight + t > height2) {
                    int idx2 = this.currSelection;
                    while (idx2 >= 0 && y2 >= 0) {
                        y2 -= ((Chat) getCurrCache().elementAt(idx2)).drawHeight;
                        idx2--;
                    }
                    this.chatOffY = -y2;
                    this.startIndex = idx2 + 1;
                } else if (t - this.chatOffY < 0) {
                    this.chatOffY = 0;
                }
            }
        } else {
            this.show = true;
        }
    }

    private void gotoForm(Chat chat) {
        String destName;
        String contentTitle;
        boolean hasOpened = false;
        if (ChatUI.instance == null) {
            ChatUI.open();
            hasOpened = true;
        }
        do {
        } while (ChatUI.instance == null);
        switch (chat.channel) {
            case Chat.CHANNEL_PRIVATE /*-8*/:
                if (chat.src == CommonData.player.id) {
                    destName = chat.destName;
                    contentTitle = "密 " + destName;
                } else {
                    destName = chat.srcName;
                    contentTitle = "回复 " + destName;
                }
                CommonData.player.whisperName = destName;
                ChatUI.instance.changeContentTitle(contentTitle);
                ChatUI.instance.setTitle("聊天", chat.toString(true, false), -8);
                break;
            case Chat.CHANNEL_TEAM /*-5*/:
                ChatUI.instance.setTitle("队伍频道", -5);
                break;
            case Chat.CHANNEL_GUILD /*-3*/:
                ChatUI.instance.setTitle("公会频道", -3);
                break;
            case Chat.CHANNEL_MAP /*-2*/:
                ChatUI.instance.setTitle("地区频道", -2);
                break;
            case -1:
                ChatUI.instance.setTitle("世界频道", -1);
                break;
        }
        ChatUI.instance.setContent("");
        if (!hasOpened) {
            ChatUI.open();
        }
    }

    private void handlePoint() {
        if (World.pressedx != -1 || World.pressedy != -1) {
            if (this.showMenu) {
                this.menu.hasPointerEvents();
            }
            if (World.pressedy > this.y && World.pressedy < World.viewHeight - DirectionPad.instance.getHeight()) {
                if (World.pressedx <= this.x || World.pressedx >= (this.x + this.width) - 16) {
                    handleScroll();
                } else {
                    handleActiveInfo();
                }
            }
        }
    }

    private void handleActiveInfo() {
        int i = 0;
        while (i < this.showLine && i <= getCurrCache().size() - 1) {
            Chat chat = (Chat) getCurrCache().elementAt(this.startIndex + i);
            if (this.chatScale[i] < World.pressedy && this.chatScale[i + 1] > World.pressedy) {
                boolean focusChanged = true;
                int newSel = this.startIndex + i;
                if (newSel == this.currSelection) {
                    focusChanged = false;
                } else {
                    this.currSelection = newSel;
                }
                if (!focusChanged) {
                    if (chat != null) {
                        chat.context.nextActiveInfo();
                    }
                    if (!chat.context.getActiveInfo().equals("")) {
                        this.showItemInfo = true;
                    } else {
                        this.showItemInfo = false;
                        gotoForm(chat);
                    }
                }
            }
            i++;
        }
    }

    private void handleScroll() {
        int sy = this.y + 16;
        int sh = this.height - 16;
        if (World.pressedy > this.y && World.pressedy < this.y + 16) {
            Utilities.keyPressed(1, true);
        } else if (World.pressedy > sy + sh && World.pressedy < sy + sh + 16) {
            Utilities.keyPressed(2, true);
        }
    }

    public void paint(Graphics g) {
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        g.setColor(0);
        g.fillRect(0, 0, World.viewWidth, World.viewHeight);
        this.tab.paint(g);
        Graphics graphics = g;
        Tool.draw3DString(graphics, TIPS[this.tipIdx], this.tipX, Utilities.LINE_HEIGHT, Tool.CLR_TABLE[8], 0, 36);
        int selY = 0;
        if (getCurrCache().size() > 0) {
            g.setClip(this.x, this.y + 6, this.width, this.height - 6);
            int dy = (this.y + 10) - this.chatOffY;
            int dx = this.x + 3;
            int idx = this.startIndex;
            int line = 0;
            this.chatScale = new int[(this.showLine + 1)];
            while (line < this.showLine && idx < getCurrCache().size()) {
                Chat chat = (Chat) getCurrCache().elementAt(idx);
                if (idx == this.currSelection) {
                    g.setClip(0, 0, World.viewWidth, World.viewHeight);
                    g.setColor(7144195);
                    g.fillRect(3, dy - 2, Chat.chatWidth, chat.drawHeight + 2);
                    selY = dy;
                    g.setClip(this.x, this.y + 3, this.width, this.height + 5 + Utilities.LINE_HEIGHT);
                } else {
                    chat.context.setActiveInfo("");
                }
                int scaleIdx = idx - this.startIndex;
                this.chatScale[scaleIdx] = dy;
                chat.context.draw(g, dx, dy, chat.frontColor);
                for (int i = 0; i < chat.context.length(); i++) {
                    if ((Utilities.LINE_HEIGHT * i) + dy > 0) {
                        line++;
                    }
                }
                dy += Utilities.LINE_HEIGHT * chat.context.length();
                if (line == this.showLine - 1 || idx == getCurrCache().size() - 1) {
                    this.chatScale[scaleIdx + 1] = dy;
                }
                if (idx != this.currSelection) {
                    g.setColor(4473924);
                    g.drawLine(0, dy - 1, World.viewWidth, dy - 1);
                }
                idx++;
            }
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            int i2 = Tool.CLR_TABLE[6];
            int i3 = Tool.CLR_TABLE[4];
            int totalLine = getCurrCache().size();
            int sx = (World.viewWidth - 16) - 5;
            int sy = this.y + 8 + 16;
            int sh = this.height - 16;
            int scrollUnitHeight = ((sh - 30) * 100) / totalLine;
            if (1 != 0) {
                g.drawImage(Tool.img_scroll[0], sx, sy - 16);
                int top = sy + ((this.currSelection * scrollUnitHeight) / 100);
                Tool.drawBlurRect(g, sx, sy, 16, sh, 2);
                if (this.currSelection == totalLine - 1) {
                    top = (sy + sh) - 30;
                }
                ScrollBar.drawBlock(g, sx, top, 30);
                g.drawImage(Tool.img_scroll[1], sx, sy + sh);
            }
        }
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        if (this.showItemInfo) {
            Chat chat2 = getCurrSelChat();
            if (chat2 != null) {
                GTVM.drawItemInfo(g, chat2.getItemInfo(), this.x + 5, selY + Utilities.LINE_HEIGHT, this.width - 10, this.height - selY, 0, null);
            }
        }
        DirectionPad.instance.paint(g);
        CornerButton.instance.paintUICmd(g, "菜单", "关闭", false);
        if (this.showMenu) {
            this.menu.paint(g);
        }
    }

    public void handleCurrTab() {
        int cur = this.tab.getIdx();
        if (!this.showMenu) {
            Utilities.keyPressed(Utilities.KEY_NUMS[cur], true);
        }
    }
}
