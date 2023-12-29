package com.sumsharp.monster.common;

import android.view.View;
import com.sumsharp.monster.Battle;
import com.sumsharp.monster.MonsterMIDlet;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.R;
import com.sumsharp.monster.Team;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.data.Npc;
import com.sumsharp.monster.common.data.Player;
import com.sumsharp.monster.gtvm.GTVM;
import com.sumsharp.monster.net.Network;
import com.sumsharp.monster.net.UWAPConnection;
import com.sumsharp.monster.net.UWAPHttpConnection;
import com.sumsharp.monster.net.UWAPSegment;
import com.sumsharp.monster.net.UWAPSocketConnection;
import com.sumsharp.monster.protocol.Protocol;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Utilities extends Protocol implements Runnable {
    public static final byte BACKSPACE_PRESSED = 8;
    public static final String BOOLEAN_SHOWCHAT_ON_EXIT = "varShowChatOnExit";
    public static final byte BUTTON_BACK_PRESSED = 10;
    public static final byte BUTTON_MENU_PRESSED = 9;
    public static final byte BUTTON_OK_PRESSED = 9;
    public static int CHAR_HEIGHT = font.getHeight();
    public static int CHAR_OFFSET = 0;
    public static int CHAR_WIDTH = font.stringWidth("啊");
    public static final String CLIENT_MODEL = "varModel";
    public static final String CLIENT_VER = "ver";
    public static final String CLIENT_VERSION = "varVersion";
    public static final int CLR_NAME = 16776960;
    public static final String DEFAULT_BACKGROUND_IMAGE = "varDefaultImg";
    public static final String DEFAULT_ITEM_ICON = "varDefaultItemIcon";
    public static final String DEFAULT_NUMBER_IMAGE = "varDefaultNumberImg";
    public static boolean DIRECT_CONNECT = false;
    public static final byte DOWN_PRESSED = 1;
    public static final byte FIRE_PRESSED = 4;
    public static final String FRIEND_ID_LOOKEDOVER = "varFriendID";
    public static final String GUILD_INVITATION = "varGuildInvitation";
    public static final String IS_SUPPORTPOINTER = "pointer";
    public static final byte KEY_A_PRESSED = 33;
    public static final byte KEY_BACK = 8;
    public static final byte KEY_B_PRESSED = 46;
    public static final byte KEY_C_PRESSED = 44;
    public static final byte KEY_DOWN = 2;
    public static final byte KEY_D_PRESSED = 35;
    public static final byte KEY_EXIT = 10;
    public static final byte KEY_EXIT_PRESSED = 50;
    public static final byte KEY_E_PRESSED = 25;
    public static final byte KEY_FIRE = 5;
    public static final byte KEY_F_PRESSED = 36;
    public static final byte KEY_G_PRESSED = 37;
    public static final byte KEY_H_PRESSED = 38;
    public static final byte KEY_I_PRESSED = 30;
    public static final byte KEY_J_PRESSED = 39;
    public static final byte KEY_K_PRESSED = 40;
    public static final byte KEY_LEFT = 3;
    public static final byte KEY_LEFT_SOFT = 6;
    public static final byte KEY_L_PRESSED = 41;
    public static final byte KEY_M_PRESSED = 48;
    public static final int KEY_NUM0 = 48;
    public static final byte KEY_NUM0_PRESSED = 11;
    public static final int KEY_NUM1 = 49;
    public static final byte KEY_NUM1_PRESSED = 12;
    public static final int KEY_NUM2 = 50;
    public static final byte KEY_NUM2_PRESSED = 13;
    public static final int KEY_NUM3 = 51;
    public static final byte KEY_NUM3_PRESSED = 14;
    public static final int KEY_NUM4 = 52;
    public static final byte KEY_NUM4_PRESSED = 15;
    public static final int KEY_NUM5 = 53;
    public static final byte KEY_NUM5_PRESSED = 16;
    public static final int KEY_NUM6 = 54;
    public static final byte KEY_NUM6_PRESSED = 17;
    public static final int KEY_NUM7 = 55;
    public static final byte KEY_NUM7_PRESSED = 18;
    public static final int KEY_NUM8 = 56;
    public static final byte KEY_NUM8_PRESSED = 19;
    public static final int KEY_NUM9 = 57;
    public static final byte KEY_NUM9_PRESSED = 20;
    public static final int[] KEY_NUMS = {49, 50, 51, 52, 53, 54, 55, 56, 57, 48};
    public static final byte KEY_N_PRESSED = 47;
    public static final byte KEY_O_PRESSED = 31;
    public static final int KEY_POUND = 35;
    public static final byte KEY_POUND_PRESSED = 21;
    public static final byte KEY_P_PRESSED = 32;
    public static final byte KEY_Q_PRESSED = 23;
    public static final byte KEY_RIGHT = 4;
    public static final byte KEY_RIGHT_SOFT = 7;
    public static final byte KEY_R_PRESSED = 26;
    public static final byte KEY_SPACE = 9;
    public static final byte KEY_SPACE_PRESSED = 49;
    public static final int KEY_STAR = 42;
    public static final byte KEY_STAR_PRESSED = 22;
    public static final byte KEY_S_PRESSED = 34;
    public static final byte KEY_T_PRESSED = 27;
    public static final byte KEY_UP = 1;
    public static final byte KEY_U_PRESSED = 29;
    public static final byte KEY_V_PRESSED = 45;
    public static final byte KEY_W_PRESSED = 24;
    public static final byte KEY_X_PRESSED = 43;
    public static final byte KEY_Y_PRESSED = 28;
    public static final byte KEY_Z_PRESSED = 42;
    public static final byte LEFT_PRESSED = 2;
    public static int LINE_HEIGHT = (CHAR_HEIGHT + 2);
    public static final String NEED_LOAD_GAME = "varNeedLoadGame";
    public static final String NEED_RELOGIN_GAME = "varNeedReloginGame";
    public static final byte RIGHT_PRESSED = 3;
    public static final int SERVER_LOADS_MAX = 3;
    public static final String SKILL_CHOICE_DATA = "varSkillChoice";
    private static final byte SOFT_FIRST_PRESSED = 9;
    private static final byte SOFT_LAST_PRESSED = 10;
    public static final int THREAD_HTTP = 0;
    public static final int THREAD_UWAP = 1;
    public static int TITLE_HEIGHT = 36;
    public static final String UI_EMOTE = "uiEmote";
    public static final String UPGRADE_MESSAGE = "varUpgradeMsg";
    public static final String UPGRADE_PACKAGE = "varUpgradePackage";
    public static final byte UP_PRESSED = 0;
    public static final String VAR_IN_GAME_PLAYING = "varInGamePlaying";
    public static final String VMUI_AUCTIONHOUSE = "ui_auctionhouse";
    public static final String VMUI_BAG = "ui_bag";
    public static final String VMUI_BATTLE = "ui_battle";
    public static final String VMUI_CHAT = "ui_chat";
    public static final String VMUI_CHOOSE_SKILL = "ui_choose_skill";
    public static final String VMUI_CITY_HALL = "ui_city_hall";
    public static final String VMUI_CLIENT_UPGRADE = "ui_client_upgrade";
    public static final String VMUI_FIELDPETMENU = "ui_fieldpetmenu";
    public static final String VMUI_FRIEND_INFORMATION = "ui_friend_info";
    public static final String VMUI_GAMEMENU = "ui_gamemenu";
    public static final String VMUI_GAME_HALL = "ui_game_hall";
    public static final String VMUI_GUILD_INVITATION = "ui_guild_invitation";
    public static final String VMUI_LOADBATTLE = "ui_load_battle";
    public static final String VMUI_LOADING = "ui_loading";
    public static final String VMUI_LOGIN = "ui_login";
    public static final String VMUI_LOGO = "ui_logo";
    public static final String VMUI_PETLIST = "ui_petlist";
    public static final String VMUI_PETMATE = "ui_petmate";
    public static final String VMUI_PLAYERMENU = "ui_playermenu";
    public static final String VMUI_PLAYER_SPACE = "ui_user_home";
    public static final String VMUI_RELOGIN = "ui_relogin";
    public static final String VMUI_SHOP = "ui_shop";
    public static final String VMUI_VIEWPET = "ui_viewpet";
    public static long appStartTime = System.currentTimeMillis();
    public static View canvas = null;
    public static String cmccMoreUrl = "http://wap.xjoys.com/wap/jump.do?s=220121913000&c=15150000&t=1";
    public static String cmccUserIdUrl = "http://g.ifeng.com/gmptest/Login?chid=";
    public static UWAPConnection connection = null;
    public static Display display = null;
    public static String downLoadPage = "http://www.joy-game.com/wap/download.aspx";
    public static String entryURL = MonsterMIDlet.instance.getString(R.string.entryUrl);
    public static final Font font = Font.getFont(0, 0, 16);
    public static Graphics graphics = null;
    public static boolean isExitGame = false;
    public static long keyFlag = 0;
    public static long keyFlag2 = 0;
    public static long keyFlag3 = 0;
    public static long keyFlag4 = 0;
    public static byte[] lastDownloadData = null;
    public static final String maintenanceCode = "none=";
    public static String officialStr = "游戏官网：\nwww.joy-game.com\nwap官网：\nwww.joy-game.com/wap/\n \n";
    public static String openOnExit = "http://wap.joy-game.com/";
    private static final String punctation = ",.?:\"!;，。？：“”！；";
    public static Random randGen = new Random();
    public static Vector segments = new Vector();
    public static String[] serverGroups;
    public static byte[][] serverLoads;
    public static String serverName;
    public static String[][] serverURLs;
    public static boolean toActorSelect;
    public static int uiHeight;
    public static int uiWidth;
    public static int uiX;
    public static int uiY;
    public static String url = "socket://sumsharp.vicp.net:33333";
    public static String wapTitle = "访问官网";
    public static String wapURL = "http://www.joy-game.com/wap/";
    private GTVM listenVM;
    private String requestURL;
    private int threadMode;

    static {
        GlobalVar.setGlobalValue((String) "CMCC_USER_ID_URL", (Object) cmccUserIdUrl);
        GlobalVar.setGlobalValue((String) "CMCC_CHANNEL_ID", (Object) "1");
    }

    public static int getTimeStamp() {
        long now = System.currentTimeMillis();
        if (now < appStartTime) {
            appStartTime = now;
        }
        return (int) (now - appStartTime);
    }

    public static boolean isButtonMenuOnLeft() {
        return true;
    }

    public static boolean isButtonOkOnLeft() {
        return true;
    }

    public static boolean isButtonBackOnLeft() {
        return false;
    }

    public static void initUIBounds(int x, int y, int width, int height) {
        uiX = x;
        uiY = y;
        uiWidth = width;
        uiHeight = height;
    }

    public static void setDisplay(Display dp, View cn) {
        display = dp;
        canvas = cn;
    }

    public static String downloadServerList() throws Exception {
        String s;
        HttpConnection httpConnection = null;
        String ret = null;
        boolean success = false;
        int j = 0;
        while (true) {
            if (j >= 2) {
                break;
            }
            httpConnection.setRequestMethod(HttpConnection.GET);
            int code = httpConnection.getResponseCode();
            if (code == 200 || code == 302) {
                DataInputStream in = httpConnection.openDataInputStream();
                try {
                    s = new String(getBytesFromInput(in), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    s = "xml";
                }
                in.close();
                if (s.indexOf("xml") >= 0 || s.indexOf("wml") >= 0) {
                    j++;
                } else {
                    int idx = s.indexOf("\n");
                    if (idx > 0) {
                        s.substring(0, idx);
                    }
                    success = true;
                    ret = s;
                }
            } else {
                throw new IOException();
            }
        }
        if (!success) {
            try {
                throw new IOException();
            } catch (Exception e2) {
                Exception exc = e2;
                throw new Exception("取服务器列表出错");
            } catch (Throwable th) {
                if (httpConnection != null) {
                    try {
                        httpConnection.close();
                    } catch (IOException e3) {
                    }
                }
                throw th;
            }
        } else {
            if (httpConnection != null) {
                try {
                    httpConnection.close();
                } catch (IOException e4) {
                }
            }
            return ret;
        }
    }

    public static void initServerList() {
        serverGroups = new String[0];
        serverURLs = new String[0][];
        serverLoads = new byte[0][];
        serverName = "";
    }

    public static String splitServerList(String list) {
        if (list == null || list.trim().length() == 0) {
            initServerList();
            return null;
        } else if (list.indexOf(maintenanceCode) >= 0) {
            String tmp = list.substring(maintenanceCode.length() + list.indexOf(maintenanceCode));
            int idx = tmp.indexOf("\n");
            if (idx >= 0) {
                tmp = tmp.substring(0, idx);
            }
            initServerList();
            return tmp;
        } else {
            int groupCount = 0;
            String tmp2 = list;
            int idx2 = tmp2.indexOf(61);
            while (idx2 >= 0) {
                groupCount++;
                tmp2 = tmp2.substring(idx2 + 1);
                idx2 = tmp2.indexOf(61);
            }
            serverGroups = new String[groupCount];
            serverURLs = new String[groupCount][];
            serverLoads = new byte[groupCount][];
            String tmp3 = list;
            for (int i = 0; i < groupCount; i++) {
                int idx3 = tmp3.indexOf(10);
                if (idx3 >= 0) {
                    serverGroups[i] = tmp3.substring(0, idx3);
                    tmp3 = tmp3.substring(idx3 + 1);
                } else {
                    serverGroups[i] = tmp3;
                }
            }
            for (int i2 = 0; i2 < serverGroups.length; i2++) {
                String tmp4 = serverGroups[i2].trim();
                int idx4 = tmp4.indexOf(61);
                serverGroups[i2] = tmp4.substring(0, idx4);
                String tmp5 = tmp4.substring(idx4 + 1);
                String tmp1 = tmp5;
                int idx5 = tmp1.indexOf(44);
                int urlCount = 0;
                while (idx5 >= 0) {
                    tmp1 = tmp1.substring(idx5 + 1);
                    idx5 = tmp1.indexOf(44);
                    if (idx5 >= 0) {
                        tmp1 = tmp1.substring(idx5 + 1);
                        idx5 = tmp1.indexOf(44);
                    }
                    urlCount++;
                }
                serverURLs[i2] = new String[urlCount];
                serverLoads[i2] = new byte[urlCount];
                for (int j = 0; j < urlCount; j++) {
                    int idx6 = tmp5.indexOf(44);
                    serverURLs[i2][j] = tmp5.substring(0, idx6);
                    tmp5 = tmp5.substring(idx6 + 1);
                    int idx7 = tmp5.indexOf(44);
                    if (idx7 >= 0) {
                        serverLoads[i2][j] = (byte) Integer.parseInt(tmp5.substring(0, idx7));
                        tmp5 = tmp5.substring(idx7 + 1);
                    } else {
                        serverLoads[i2][j] = (byte) Integer.parseInt(tmp5);
                    }
                }
            }
            return null;
        }
    }

    public static String getMinLoadsServer() {
        if (serverURLs.length < 1) {
            return null;
        }
        int gpid = 0;
        int urlid = 0;
        for (int i = 0; i < serverGroups.length; i++) {
            for (int j = 0; j < serverURLs[i].length; j++) {
                if (serverLoads[i][j] < 3) {
                    gpid = i;
                    urlid = j;
                }
            }
        }
        return serverURLs[gpid][urlid];
    }

    public Utilities(String uu, int tm, GTVM lvm) {
        this.requestURL = uu;
        this.threadMode = tm;
        this.listenVM = lvm;
    }

    public void run() {
        boolean result;
        String s;
        try {
            if (this.threadMode == 0) {
                HttpConnection httpConnection = null;
                boolean success = false;
                String entryUrl = entryURL;
                if (this.requestURL != null) {
                    entryUrl = this.requestURL;
                }
                int j = 0;
                while (true) {
                    if (j >= 2) {
                        break;
                    }
                    while (entryUrl.endsWith("\t")) {
                        entryUrl = entryUrl.substring(0, entryUrl.length() - 1);
                    }
                    httpConnection = UWAPSegment.getConnection(entryUrl, Network.useProxy());
                    httpConnection.setRequestMethod(HttpConnection.GET);
                    int code = 0;
                    try {
                        code = httpConnection.getResponseCode();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    if (code == 200 || code == 302) {
                        try {
                            DataInputStream in = httpConnection.openDataInputStream();
                            byte[] data = getBytesFromInput(in);
                            try {
                                s = new String(data, "UTF-8");
                            } catch (Exception e) {
                                e.printStackTrace();
                                s = "xml";
                            }
                            in.close();
                            if (s.indexOf("xml") < 0 && s.indexOf("wml") < 0) {
                                lastDownloadData = data;
                                success = true;
                                break;
                            }
                            j++;
                        } catch (Throwable th) {
                            if (httpConnection != null) {
                                try {
                                    httpConnection.close();
                                } catch (IOException e2) {
                                }
                            }
                            throw th;
                        }
                    } else {
                        throw new IOException();
                    }
                }
                if (!success) {
                    throw new IOException();
                } else if (httpConnection != null) {
                    try {
                        httpConnection.close();
                    } catch (IOException e3) {
                    }
                }
            } else if (this.threadMode == 1) {
                if (this.requestURL != null) {
                    url = this.requestURL;
                }
                createConnection();
            }
            Thread.sleep(1000);
            result = true;
        } catch (Throwable th2) {
            Throwable th3 = th2;
            result = false;
        }
        if (this.listenVM != null) {
            this.listenVM.continueProcess(result ? 1 : 0);
        }
    }

    public static void createConnection() throws Exception {
        if (connection == null) {
            if (url.startsWith("http")) {
                connection = new UWAPHttpConnection(url);
                System.out.println("use http connection");
            } else {
                connection = new UWAPSocketConnection(url);
                System.out.println("use socket connection");
            }
            connection.start();
        }
    }

    public static void tryReconnect() {
        closeConnection();
        CommonComponent.loadUI(VMUI_RELOGIN);
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                UWAPConnection temp = connection;
                connection = null;
                temp.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.gc();
    }

    public static int sendRequest(byte type, byte subtype, String param1, int param2) {
        try {
            UWAPSegment segment = new UWAPSegment(type, subtype);
            segment.writeString(param1);
            segment.writeInt(param2);
            return sendRequest(segment);
        } catch (Exception e) {
            Exception exc = e;
            return -1;
        }
    }

    public static int sendRequest(byte type, byte subtype, int param1, String param2) {
        try {
            UWAPSegment segment = new UWAPSegment(type, subtype);
            segment.writeInt(param1);
            if (param2 != null) {
                segment.writeString(param2);
            }
            return sendRequest(segment);
        } catch (Exception e) {
            Exception exc = e;
            return -1;
        }
    }

    public static int sendRequestWithoutResponse(byte type, byte subtype, int param1, String param2) {
        try {
            UWAPSegment segment = new UWAPSegment(type, subtype);
            segment.needResponse = false;
            segment.writeInt(param1);
            if (param2 != null) {
                segment.writeString(param2);
            }
            return sendRequest(segment);
        } catch (Exception e) {
            Exception exc = e;
            return -1;
        }
    }

    public static int sendRequest(byte type, byte subtype, String param1) {
        try {
            UWAPSegment segment = new UWAPSegment(type, subtype);
            segment.writeString(param1);
            return sendRequest(segment);
        } catch (Exception e) {
            Exception exc = e;
            return -1;
        }
    }

    public static int sendRequest(byte type, byte subtype) {
        try {
            return sendRequest(new UWAPSegment(type, subtype));
        } catch (Exception e) {
            Exception exc = e;
            return -1;
        }
    }

    public static int sendRequest(byte type, byte subtype, byte param1, String param2) {
        try {
            UWAPSegment segment = new UWAPSegment(type, subtype);
            segment.writeByte(param1);
            if (param2 != null) {
                segment.writeString(param2);
            }
            return sendRequest(segment);
        } catch (Exception e) {
            Exception exc = e;
            return -1;
        }
    }

    public static int sendRequest(UWAPSegment segment) {
        if (connection != null) {
            connection.writeSegment(segment);
            return segment.serial;
        }
        UWAPSocketConnection.sendSegmentTimeOut(segment.mainType, segment.subType, Integer.MAX_VALUE);
        return Integer.MAX_VALUE;
    }

    public static void addSegment(UWAPSegment segment) {
        segments.addElement(segment);
    }

    public static int getGameKeyCode(int keyCode) {
        int keyCode2 = Math.abs(keyCode);
        int id = keyToGame(keyCode2);
        if (id < 0) {
            return keyToNum(keyCode2);
        }
        return id;
    }

    public static void keyPressed(int keyCode) {
        keyPressed(keyCode, false);
    }

    public static void keyPressed(int keyCode, boolean syncKey2) {
        if (!Npc.showConnection || Npc.touchingNpc == null) {
            int keyCode2 = Math.abs(keyCode);
            try {
                int id = keyToGame(keyCode2);
                if (id >= 0) {
                    keyFlag |= 3 << (id << 1);
                    if (syncKey2) {
                        keyFlag2 |= 3 << (id << 1);
                    }
                }
                int id2 = keyToNum(keyCode2);
                if (id2 >= 0) {
                    if (id2 >= 23) {
                        int id3 = id2 - 23;
                        keyFlag3 |= 3 << (id3 << 1);
                        if (syncKey2) {
                            keyFlag4 |= 3 << (id3 << 1);
                        }
                    } else {
                        keyFlag |= 3 << (id2 << 1);
                        if (syncKey2) {
                            keyFlag2 |= 3 << (id2 << 1);
                        }
                    }
                }
                World.pressedx = -1;
                World.pressedy = -1;
                World.pointerx = -1;
                World.pointery = -1;
                World.releasedx = -1;
                World.releasedy = -1;
                World.onCommand = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void keyReleased(int keyCode) {
        int keyCode2 = Math.abs(keyCode);
        try {
            int id = keyToGame(keyCode2);
            if (id >= 0) {
                keyFlag &= (2 << (id << 1)) ^ -1;
            }
            int id2 = keyToNum(keyCode2);
            if (id2 >= 0) {
                keyFlag &= (2 << (id2 << 1)) ^ -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int keyToGame(int keyCode) {
        switch (keyCode) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 8:
                return 8;
            case 9:
                return 49;
            default:
                return -1;
        }
    }

    public static int keyToNum(int keyCode) {
        switch (keyCode) {
            case 6:
                return 9;
            case 7:
                return 10;
            case 10:
                return 50;
            case 35:
                return 21;
            case 42:
                return 22;
            case 97:
                return 33;
            case 98:
                return 46;
            case 99:
                return 44;
            case 100:
                return 35;
            case 101:
                return 25;
            case 102:
                return 36;
            case 103:
                return 37;
            case 104:
                return 38;
            case 105:
                return 30;
            case 106:
                return 39;
            case 107:
                return 40;
            case 108:
                return 41;
            case 109:
                return 48;
            case 110:
                return 47;
            case 111:
                return 31;
            case 112:
                return 32;
            case 113:
                return 23;
            case 114:
                return 26;
            case 115:
                return 34;
            case 116:
                return 27;
            case 117:
                return 29;
            case 118:
                return 45;
            case 119:
                return 24;
            case 120:
                return 43;
            case 121:
                return 28;
            case 122:
                return 42;
            default:
                if (keyCode < 48 || keyCode > 57) {
                    return -1;
                }
                return (keyCode - 48) + 11;
        }
    }

    public static final boolean isAnyKeyPressed() {
        return (keyFlag2 == 0 && keyFlag3 == 0) ? false : true;
    }

    public static final boolean isKeyPressed(int key, boolean clear) {
        long k;
        boolean ret;
        if (key >= 23) {
            key -= 23;
            k = 3 << (key << 1);
            ret = (keyFlag4 & k) != 0;
        } else {
            k = 3 << (key << 1);
            ret = (keyFlag2 & k) != 0;
        }
        if (clear && ret) {
            if (key > 23) {
                keyFlag3 &= k ^ -1;
                keyFlag4 &= k ^ -1;
            } else {
                keyFlag &= k ^ -1;
                keyFlag2 &= k ^ -1;
            }
        }
        return ret;
    }

    public static final void clearKeyStates(int key) {
        isKeyPressed(key, true);
    }

    public static void clearKeyStates() {
        keyFlag2 = 0;
        keyFlag = 0;
        keyFlag4 = 0;
        keyFlag3 = 0;
    }

    public static void setUIClip(Graphics g) {
        g.setClip(uiX, uiY, uiWidth, uiHeight);
    }

    public static byte[] getBytesFromInput(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buf = new byte[64];
        while (true) {
            int rd = in.read(buf);
            if (rd == -1) {
                byte[] rt = out.toByteArray();
                out.close();
                return rt;
            }
            len += rd;
            out.write(buf, 0, rd);
        }
    }

    public static String replaceString(String from, String to, String source) {
        if (source == null || from == null || to == null) {
            return null;
        }
        StringBuffer bf = new StringBuffer();
        while (true) {
            int index = source.indexOf(from);
            if (index == -1) {
                bf.append(source);
                return bf.toString();
            }
            bf.append(String.valueOf(source.substring(0, index)) + to);
            source = source.substring(from.length() + index);
        }
    }

    public static String[] splitString(String str, String regex) {
        int idx;
        if (str.equals("")) {
            return new String[0];
        }
        Vector ret = new Vector();
        int start = 0;
        do {
            idx = str.indexOf(regex, start);
            if (idx != -1) {
                String s = str.substring(start, idx);
                start = idx + regex.length();
                ret.addElement(s);
                continue;
            } else if (start == 0) {
                ret.addElement(str);
                continue;
            } else {
                ret.addElement(str.substring(start));
                continue;
            }
        } while (idx != -1);
        String[] retarr = new String[ret.size()];
        ret.copyInto(retarr);
        return retarr;
    }

    public static String[] formatText(String text, int width, Font font2) {
        Vector vec = new Vector();
        int lineStart = 0;
        int lineWid = 0;
        int charCount = text.length();
        for (int i = 0; i < charCount; i++) {
            char ch = text.charAt(i);
            if (ch == 10) {
                if (i <= 0 || text.charAt(i - 1) != 13) {
                    vec.addElement(text.substring(lineStart, i));
                } else {
                    vec.addElement(text.substring(lineStart, i - 1));
                }
                lineStart = i + 1;
                lineWid = 0;
            } else {
                int charWid = font2.charWidth(ch);
                if (lineWid == 0 || lineWid + charWid <= width) {
                    lineWid += charWid;
                } else {
                    vec.addElement(text.substring(lineStart, i));
                    lineStart = i;
                    lineWid = charWid;
                }
            }
        }
        if (lineWid > 0) {
            vec.addElement(text.substring(lineStart));
        }
        String[] ret = new String[vec.size()];
        vec.copyInto(ret);
        return ret;
    }

    public static String getName(String name, int width) {
        int w = font.stringWidth(name);
        boolean changed = false;
        while (w > width) {
            name = name.substring(0, name.length() - 1);
            w = font.stringWidth(String.valueOf(name) + "..");
            changed = true;
        }
        if (changed) {
            return String.valueOf(name) + "..";
        }
        return name;
    }

    public static int random(int min, int max) {
        int value = randGen.nextInt() % ((max - min) + 1);
        if (value < 0) {
            value = -value;
        }
        return min + value;
    }

    public static void exitToMenu(boolean toActor) {
        CommonComponent.closeAllUI(true);
        NewStage.isMapLoadOk = false;
        NewStage.playerLogined = false;
        GlobalVar.setGlobalValue((String) "PLAYERLOGIN", (Object) "false");
        NewStage.clear();
        Battle.clearBattle();
        World.drawUI = false;
        toActorSelect = toActor;
        CommonData.team = new Team();
        CommonData.player = new Player();
        CommonComponent.loadUI(VMUI_LOGIN);
        if (World.fullChatUI != null) {
            World.fullChatUI = null;
        }
    }
}
