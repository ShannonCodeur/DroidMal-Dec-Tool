package com.sumsharp.monster.common.data;

import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.lowui.StringDraw;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.CommonProcessor;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.item.GameItem;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Chat {
    public static final int CHANNEL_GUILD = -3;
    public static final int CHANNEL_MAP = -2;
    public static final int CHANNEL_NEWS = -9;
    public static final int CHANNEL_PRIVATE = -8;
    public static final int CHANNEL_RAID = -4;
    public static final int CHANNEL_SYSTEM = -7;
    public static final int CHANNEL_TEAM = -5;
    public static final int CHANNEL_WORLD = -1;
    public static int[] CHAT_CHANNEL_BACK_COLORS;
    public static int[] CHAT_CHANNEL_FRONT_COLORS;
    public static int[] CHAT_CHANNEL_FULLIDX;
    public static String[] CHAT_CHANNEL_NAMES = {"世", "区", "会", "队", "队", "", "系", "密", "系"};
    public static String[] CHAT_FULL_CHANNELS = {"综", "私", "队", "会", "系"};
    public static int chatEnd;
    public static int chatHeight = 0;
    public static int chatWidth;
    public static int chatX;
    public static int chatY;
    public static Vector[] chlCache = new Vector[4];
    public static boolean[] chlNotify = new boolean[5];
    public static boolean drawBack;
    public static boolean isPaintOver = true;
    public static byte[] lock = new byte[0];
    public static Vector msgCache = new Vector();
    public static Vector showMsgs = new Vector();
    public static int tick = -1;
    public boolean addToMsgCache = true;
    public int bgColor;
    public int channel;
    public String chlStr;
    public StringDraw context;
    public int dest;
    public String destName;
    public int drawHeight;
    public int drawY;
    public int frontColor;
    public boolean hovering = false;
    public String msg;
    public int offy = -1;
    public int show = 0;
    public int src;
    public String srcName;

    static {
        for (int i = 0; i < chlCache.length; i++) {
            chlCache[i] = new Vector();
        }
        int[] iArr = new int[9];
        iArr[2] = 3;
        iArr[3] = 2;
        iArr[4] = 2;
        iArr[6] = 4;
        iArr[7] = 1;
        iArr[8] = 4;
        CHAT_CHANNEL_FULLIDX = iArr;
        int[] iArr2 = new int[9];
        iArr2[0] = 16777215;
        iArr2[1] = 13999484;
        iArr2[2] = 65298;
        iArr2[3] = 16187136;
        iArr2[4] = 16749568;
        iArr2[6] = 16776960;
        iArr2[7] = 16711794;
        iArr2[8] = 16776960;
        CHAT_CHANNEL_FRONT_COLORS = iArr2;
        int[] iArr3 = new int[9];
        iArr3[0] = 4013645;
        iArr3[1] = 5187601;
        iArr3[2] = 214784;
        iArr3[3] = 8669952;
        iArr3[4] = 8654592;
        iArr3[7] = 5570637;
        CHAT_CHANNEL_BACK_COLORS = iArr3;
    }

    public static Chat getLastPrivateChat(String name) {
        for (int i = 0; i < msgCache.size(); i++) {
            Chat chat = (Chat) msgCache.elementAt(i);
            if (chat.channel == -8 && chat.src != CommonData.player.id && (name == null || chat.srcName.equals(name))) {
                return chat;
            }
        }
        return null;
    }

    public static void clear() {
        msgCache.removeAllElements();
        showMsgs.removeAllElements();
        for (Vector removeAllElements : chlCache) {
            removeAllElements.removeAllElements();
        }
    }

    public static void recvChatMsg(int src2, String srcName2, int dest2, String destName2, String context2) {
        recvChatMsg(src2, srcName2, dest2, destName2, context2, true);
    }

    public static void recvChatMsg(int src2, String srcName2, int dest2, String destName2, String context2, boolean addToMsgCache2) {
        int channel2;
        int channel3;
        boolean z;
        if (dest2 < 0) {
            channel2 = dest2;
        } else {
            channel2 = -8;
        }
        if (dest2 != -9 && src2 == -1) {
            channel2 = -7;
        }
        Chat chat = new Chat();
        chat.channel = channel2;
        chat.src = src2;
        chat.dest = dest2;
        chat.srcName = srcName2;
        chat.destName = destName2;
        chat.msg = context2;
        if (dest2 == -9 || dest2 == -1) {
            String str = chat.toString(true, true);
            channel3 = 0 - (chat.channel + 1);
            int i = CHAT_CHANNEL_FRONT_COLORS[channel3];
            if (chat.dest == -9) {
                z = true;
            } else {
                z = false;
            }
            World.addNotifyMsg(str, i, z);
        } else {
            channel3 = 0 - (chat.channel + 1);
        }
        chat.frontColor = CHAT_CHANNEL_FRONT_COLORS[channel3];
        chat.bgColor = CHAT_CHANNEL_BACK_COLORS[channel3];
        chat.addToMsgCache = addToMsgCache2;
        addMsg(chat);
        chat.moveUp(chat.context.length());
    }

    public static void addMsg(Chat chat) {
        chat.context = new StringDraw(chat.toString(true, true), chatWidth, -1);
        chat.drawHeight = chat.context.length() * Utilities.LINE_HEIGHT;
        chat.drawY = chatEnd - chat.drawHeight;
        synchronized (lock) {
            if (chat.dest != -9) {
                showMsgs.insertElementAt(chat, 0);
                isPaintOver = false;
            }
            if (chat.addToMsgCache) {
                msgCache.insertElementAt(chat, 0);
            }
            int idx = CHAT_CHANNEL_FULLIDX[chat.getChannelIdx()];
            if (chat.channel != -8) {
                chlNotify[idx] = true;
            } else if (chat.src != CommonData.player.id) {
                chlNotify[idx] = true;
            }
            if (idx > 0) {
                chlCache[idx - 1].insertElementAt(chat, 0);
            }
            if (showMsgs.size() > 5) {
                showMsgs.removeElementAt(showMsgs.size() - 1);
            }
        }
    }

    public static void addMsg(String channel2, String context2, int frontColor2, int bgColr) {
    }

    public static boolean isShowingMsg() {
        synchronized (lock) {
        }
        return false;
    }

    public static void cycle() {
        tick++;
        synchronized (lock) {
            if (msgCache.size() > 100) {
                msgCache.removeElementAt(msgCache.size() - 1);
            }
            for (int i = 0; i < chlCache.length; i++) {
                if (chlCache[i].size() > 40) {
                    chlCache[i].removeElementAt(chlCache[i].size() - 1);
                }
            }
        }
    }

    public static void paint(Graphics g) {
        int h3;
        int h2;
        int h32;
        int h4;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (lock) {
            if (showMsgs.size() > 0 && !isPaintOver) {
                int allHeight = 0;
                int n = 0;
                int n2 = 0;
                int h5 = 0;
                int h42 = 0;
                int h33 = 0;
                while (n < showMsgs.size()) {
                    allHeight += ((Chat) showMsgs.elementAt(n)).drawHeight;
                    if (n == 2) {
                        h32 = h5;
                        h4 = n2;
                        h3 = h33;
                        h2 = ((Chat) showMsgs.elementAt(n)).drawHeight;
                    } else if (n == 1) {
                        h3 = ((Chat) showMsgs.elementAt(n)).drawHeight;
                        h2 = h42;
                        h32 = h5;
                        h4 = n2;
                    } else if (n == 0) {
                        int i = ((Chat) showMsgs.elementAt(n)).drawHeight;
                        h3 = h33;
                        h2 = h42;
                        h32 = h5;
                        h4 = n2;
                    } else if (n == 3) {
                        h4 = n2;
                        h3 = h33;
                        h2 = h42;
                        h32 = ((Chat) showMsgs.elementAt(n)).drawHeight;
                    } else if (n == 4) {
                        h3 = h33;
                        h2 = h42;
                        h32 = h5;
                        h4 = ((Chat) showMsgs.elementAt(n)).drawHeight;
                    } else {
                        h3 = h33;
                        h2 = h42;
                        h32 = h5;
                        h4 = n2;
                    }
                    n++;
                    n2 = h4;
                    h5 = h32;
                    h42 = h2;
                    h33 = h3;
                }
                int end = (World.viewWidth - Tool.img_CornerBg.getWidth()) - Tool.img_dirPadBg.getWidth();
                for (int dx = 0; dx <= end; dx += Tool.img_chatbg.getWidth()) {
                    Tool.chatImgGraphics.drawImage(Tool.img_chatbg, dx, 0, 20);
                }
                Tool.chatImgGraphics.drawImage(Tool.img_chatbg, end, 0, 24);
                for (int m = showMsgs.size(); m > 0; m--) {
                    Chat chat = (Chat) showMsgs.elementAt(m - 1);
                    int paintY = 0;
                    switch (m) {
                        case 1:
                            paintY = (-(allHeight - Tool.img_CornerBg.getHeight())) + h42 + h33 + h5 + n2;
                            break;
                        case 2:
                            paintY = (-(allHeight - Tool.img_CornerBg.getHeight())) + h42 + h5 + n2;
                            break;
                        case 3:
                            paintY = (-(allHeight - Tool.img_CornerBg.getHeight())) + h5 + n2;
                            break;
                        case 4:
                            paintY = (-(allHeight - Tool.img_CornerBg.getHeight())) + n2;
                            break;
                        case 5:
                            paintY = -(allHeight - Tool.img_CornerBg.getHeight());
                            break;
                    }
                    chat.drawChat(Tool.chatImgGraphics, paintY - 6);
                }
            }
        }
        g.drawImage(Tool.chatImg, DirectionPad.instance.getWidth(), chatY + 5, 20);
        isPaintOver = true;
    }

    public void drawChat(Graphics g, int Y) {
        this.context.draw3D(g, 2, Y, this.frontColor, this.bgColor);
    }

    public int getDrawChatYinImg(int row) {
        this.drawY = getDrawY();
        return this.drawY;
    }

    public void moveUp(int row) {
        int oldy = this.offy;
        this.offy++;
        if (oldy / Utilities.LINE_HEIGHT != this.offy / Utilities.LINE_HEIGHT) {
            this.offy = (this.offy / Utilities.LINE_HEIGHT) * Utilities.LINE_HEIGHT;
        }
    }

    public int getDrawY() {
        if (this.offy == -1) {
            return this.drawY;
        }
        return this.drawY - this.offy;
    }

    public boolean needHover() {
        int showY = chatEnd - (((chatEnd - chatY) / Utilities.LINE_HEIGHT) * Utilities.LINE_HEIGHT);
        if ((this.show != 0 || (getDrawY() + this.drawHeight > chatEnd && getDrawY() != showY)) && (this.show != 1 || getDrawY() + this.drawHeight > chatEnd || this.drawHeight <= chatEnd - showY)) {
            return false;
        }
        this.hovering = true;
        return true;
    }

    public boolean over() {
        return getDrawY() + this.drawHeight < 0;
    }

    public String toString() {
        return toString(true, true);
    }

    public String toString(boolean withChannel, boolean withTag) {
        String ret;
        String ret2;
        String ret3 = "";
        if (withChannel) {
            ret3 = "[";
        }
        if (this.dest == -9) {
            if (withChannel) {
                ret2 = "[系] ";
            } else {
                ret2 = "";
            }
            return String.valueOf(ret2) + this.msg;
        }
        boolean addSpeak = true;
        if (0 - (this.channel + 1) == 7) {
            if (this.src == CommonData.player.id) {
                ret = String.valueOf("") + "[密] " + this.destName + "：";
            } else {
                ret = String.valueOf("") + "[密] " + this.srcName + " 悄悄的说：";
            }
            addSpeak = false;
        } else {
            if (withChannel) {
                ret3 = String.valueOf(ret3) + CHAT_CHANNEL_NAMES[channel] + "] ";
            }
            if (this.channel != -7) {
                ret = String.valueOf(ret) + this.srcName;
            }
        }
        if (this.src != -1 && addSpeak) {
            ret = String.valueOf(ret) + " 说：";
        }
        if (withTag) {
            return String.valueOf(ret) + this.msg;
        }
        return this.context.getWholeString();
    }

    public int getChannelIdx() {
        return 0 - (this.channel + 1);
    }

    public GameItem getItemInfo() {
        String infos = this.context.getActiveInfo();
        if (!infos.equals("")) {
            String[] info = Tool.splitString(infos, ':');
            int q = Integer.parseInt(info[0]);
            int pid = Integer.parseInt(info[1]);
            int mid = Integer.parseInt(info[2]);
            GameItem gi = CommonData.player.findItem(mid);
            if (gi != null) {
                return gi;
            }
            GameItem gi2 = (GameItem) CommonProcessor.itemTable.get(new Integer(mid));
            if (gi2 != null) {
                return gi2;
            }
            GameItem gi3 = new GameItem();
            gi3.name = "";
            gi3.quanlity = (byte) q;
            gi3.id = mid;
            gi3.desc = "正在下载物品信息，请稍候";
            CommonProcessor.itemTable.put(new Integer(mid), gi3);
            try {
                UWAPSegment segment = new UWAPSegment(18, 59);
                segment.writeInt(pid);
                segment.writeInt(mid);
                Utilities.sendRequest(segment);
            } catch (IOException e) {
            }
        }
        return null;
    }

    public static void toFullMode() {
        chatX = 3;
        chatWidth = (World.viewWidth - 6) - 16;
        fitNewWidth();
    }

    public static void toBottomMode() {
        chatX = 128;
        chatWidth = ((World.viewWidth - chatX) - 99) - 5;
        fitNewWidth();
    }

    private static void fitNewWidth() {
        if (showMsgs != null && showMsgs.size() > 0) {
            for (int i = 0; i < showMsgs.size(); i++) {
                Chat c = (Chat) showMsgs.elementAt(i);
                c.context = new StringDraw(c.toString(), chatWidth, -1);
                c.drawHeight = c.context.length() * Utilities.LINE_HEIGHT;
                c.drawY = chatEnd - c.drawHeight;
            }
        }
        if (msgCache != null && msgCache.size() > 0) {
            for (int i2 = 0; i2 < msgCache.size(); i2++) {
                Chat c2 = (Chat) msgCache.elementAt(i2);
                c2.context = new StringDraw(c2.toString(), chatWidth, -1);
                c2.drawHeight = c2.context.length() * Utilities.LINE_HEIGHT;
                c2.drawY = chatEnd - c2.drawHeight;
            }
        }
    }
}
