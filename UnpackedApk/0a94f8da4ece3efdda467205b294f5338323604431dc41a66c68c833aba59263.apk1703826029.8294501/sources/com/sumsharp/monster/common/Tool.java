package com.sumsharp.monster.common;

import android.app.Activity;
import android.graphics.Rect;
import com.sumsharp.android.ui.Input;
import com.sumsharp.lowui.AbstractLowUI;
import com.sumsharp.monster.Battle;
import com.sumsharp.monster.MonsterMIDlet;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.R;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.gtvm.GTVM;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.image.PipAnimateSet;
import com.sumsharp.monster.item.GameItem;
import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Tool {
    public static final int ARROW_ANIMATE = 16;
    public static final int ARROW_DOWN = 3;
    public static final int[] ARROW_FRAME_IDX;
    public static final int ARROW_LEFT = 0;
    public static final int ARROW_RIGHT = 2;
    public static final int ARROW_STATIC = 32;
    public static final int ARROW_UP = 1;
    public static final int BORDER_BUTTONSEL = 128;
    public static final int BORDER_DARK = 2048;
    public static final int BORDER_GRAY = 16384;
    public static final int BORDER_HEAVY = 1;
    public static final int BORDER_ITEM = 4;
    public static final int BORDER_ITEM2 = 65536;
    public static final int BORDER_ITEM2_HIGHLIGHT = 131072;
    public static final int BORDER_ITEM_HIGHLIGHT = 8;
    public static final int BORDER_LIGHT = 1024;
    public static final int BORDER_SLIM = 16;
    public static final int BORDER_THICK = 16;
    public static final int BORDER_THIN_DARK = 64;
    public static final int BORDER_THIN_DARK_SMALL = 256;
    public static final int BORDER_THIN_LIGHT = 32;
    public static final int BORDER_TITLE = 4096;
    public static final int BORDER_VERYDARK = 8192;
    public static final int BORDER_WHITE = 512;
    public static final int[] BTN_CLR_DISABLE = {5987176, 9408410, 9408410, 9408410, 9408410, 9408410, 9408410, 9408410, 9671582, 9934754, 9934754, 9934754, 9934754, 11316401, 5987176};
    public static final int[] BTN_CLR_NORMAL = {4689021, 10802320, 10802320, 10802320, 10802320, 10802320, 10802320, 11328148, 11919512, 11919512, 11919512, 11919512, 11919512, 14874064, 4156785};
    public static final int[] BTN_CLR_SELECT = {4689021, 11919512, 11919512, 11919512, 11919512, 11919512, 11919512, 12707494, 13429684, 13429684, 13429684, 13429684, 13429684, 14874064, 4156785};
    public static int[][] CLR_BORDER = null;
    public static int CLR_DARK = 0;
    public static int CLR_GRAY = 0;
    public static int CLR_GRAY_DARK = 0;
    public static int CLR_GRAY_LIGHT = 0;
    public static final int[] CLR_ITEM = {CLR_ITEM_GRAY, CLR_ITEM_WHITE, CLR_ITEM_GREEN, CLR_ITEM_BLUE, CLR_ITEM_PURPLE, CLR_ITEM_ORANGE};
    public static final int CLR_ITEM_BLUE = 7322617;
    public static final int CLR_ITEM_GRAY = 12632256;
    public static final int CLR_ITEM_GREEN = 7399792;
    public static final int CLR_ITEM_ORANGE = 16754688;
    public static final int CLR_ITEM_PURPLE = 13058047;
    public static final int CLR_ITEM_RED = 16742263;
    public static final int CLR_ITEM_WHITE = 16777215;
    public static final int CLR_ITEM_YELLOW = 6383191;
    public static int CLR_LIGHT = 0;
    public static int CLR_L_DARK = 0;
    public static int CLR_L_LIGHT = 0;
    public static int[] CLR_TABLE = null;
    public static int CLR_TEXT_DARK = 0;
    public static int CLR_TEXT_LIGHT = 0;
    public static int CLR_TEXT_NORMAL = 0;
    public static final int CLR_THICK = 0;
    public static final int CLR_THIN_DARK = 2;
    public static final int CLR_THIN_DARK_SMALL = 4;
    public static final int CLR_THIN_LIGHT = 1;
    public static final int CLR_THIN_LIGHT_SMALL = 3;
    public static int CLR_TINT = 0;
    public static int CLR_TITLE = 0;
    public static int CLR_VERYDARK = 0;
    public static final int DECORATE1 = 384;
    public static final int DECORATE2 = 288;
    public static final int DECORATE_BOTTOMLEFT = 128;
    public static final int DECORATE_BOTTOMRIGHT = 256;
    public static final int DECORATE_TOPLEFT = 32;
    public static final int DECORATE_TOPRIGHT = 64;
    public static final int EDGE_ROUND_ALL = 15;
    public static final int EDGE_ROUND_BOTTOM = 12;
    public static final int EDGE_ROUND_BOTTOMLEFT = 4;
    public static final int EDGE_ROUND_BOTTOMRIGHT = 8;
    public static final int EDGE_ROUND_TOP = 3;
    public static final int EDGE_ROUND_TOPLEFT = 1;
    public static final int EDGE_ROUND_TOPRIGHT = 2;
    public static final int FILL_DARK = 4194304;
    public static final int FILL_GRAY = 536870912;
    public static final int FILL_GRAY_DARK = 524288;
    public static final int FILL_GRAY_LIGHT = 262144;
    public static final int FILL_HINT = 4096;
    public static final int FILL_ITEM = 8192;
    public static final int FILL_ITEM2 = 262144;
    public static final int FILL_ITEM2_HIGHLIGHT = 524288;
    public static final int FILL_ITEM_HIGHLIGHT = 16384;
    public static final int FILL_LIGHT = 2097152;
    public static final int FILL_L_DARK = Integer.MIN_VALUE;
    public static final int FILL_L_LIGHT = 1073741824;
    public static final int FILL_MENU = 32768;
    public static final int FILL_NONE = 8388608;
    public static final int FILL_TAB = 1024;
    public static final int FILL_TAB_HIGHLIGHT = 2048;
    public static final int FILL_TEXT_DARK = 67108864;
    public static final int FILL_TEXT_LIGHT = 33554432;
    public static final int FILL_TEXT_NORMAL = 134217728;
    public static final int FILL_TITLE = 16777216;
    public static final int FILL_VERYDARK = 268435456;
    public static final int FILL_WHITE = 1048576;
    public static final int[] HEAVY_BORDER_COLOR = {-2916601, -2916601, -4168702, -4168702, -1720497, -1720497};
    public static final int[] HINT_FILL_COLOR = {-923961};
    public static final byte H_CENTER = 0;
    public static final byte H_LEFT = 1;
    public static final byte H_RIGHT = 2;
    public static final String IMAGE_FONT = "速度防御攻击精神智耐敏捷力量性格野基础辅助继承原始属成长率厄运沉默混乱睡束缚失明石化地水火风光暗等级资质总几装备需要材料武器具饰品取消返回对话查看确定进入登录";
    public static final int IMAGE_FONT_WIDTH = 13;
    public static final int[] ITEM2_BORDER_COLOR = {-997888, -4141};
    public static final int[] ITEM2_FILL_COLOR = {-1326648889};
    public static final int[] ITEM2_HIGHLIGHT_BORDER_COLOR = {-2975744, -142938};
    public static final int[] ITEM2_HIGHLIGHT_FILL_COLOR = {-1326324025};
    public static final int[] ITEM_BORDER_COLOR = {-9879535, -4224718, -531316};
    public static final int[] ITEM_FILL_COLOR = {-1248825};
    public static final int[] ITEM_HIGHLIGHT_BORDER_COLOR = {-15445208, -13131205, -5640031};
    public static final int[] ITEM_HIGHLIGHT_FILL_COLOR = {-923961};
    public static final int[] LIGHT_BORDER_COLOR = HEAVY_BORDER_COLOR;
    public static final int[] MENU_FILL_COLOR = {-1326324025};
    public static final int NUMSTR_TYPE_BIG = 1;
    public static final int NUMSTR_TYPE_SMALL = 0;
    public static final int NUMSTR_TYPE_VERYSMALL = 2;
    public static final int[] SLIM_BORDER_COLOR;
    public static final int STYLE_CONTENT = 1048640;
    public static final int STYLE_FUNCTION = 1048652;
    public static final int STYLE_MENU = 1048607;
    public static final int STYLE_MENUITEM = 2097199;
    public static final int STYLE_MENUITEM_SELECT = 4194351;
    public static final int STYLE_TITLE = 4194371;
    public static final int[] TAB_FILL_COLOR = {-1248825};
    public static final int[] TAB_HIGHLIGHT_FILL_COLOR = {-923961};
    public static final int[] TITLE_FILL_COLOR = {-1721106721};
    public static int TOP_LEFT = 20;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_ROT90 = 5;
    private static int[] alphaColors = null;
    public static ImageSet arrow = null;
    private static int[] bookFrame = {59, 60, 61};
    public static ImageSet building = null;
    public static ImageSet chatEmote = null;
    public static Image chatImg = null;
    public static Graphics chatImgGraphics = null;
    public static final int clr_androidTextDefault = 16776397;
    public static final int clr_androidTextDisable = 12370097;
    public static PipAnimateSet defaultArmy;
    public static ImageSet defaultIcon;
    public static ImageSet doorImage;
    public static int draw3DQuanlity = 0;
    public static PipAnimateSet emote;
    public static PipAnimateSet female = null;
    public static ImageSet imgFont;
    public static Image img_CornerBg;
    public static Image img_arcL;
    public static Image img_arcR;
    public static Image img_arrow;
    public static Image img_battlebg;
    public static Image img_chatbg;
    public static Image[] img_corner0;
    public static Image[] img_corner1;
    public static Image img_dir0;
    public static Image img_dir1;
    public static Image img_dirPadBg;
    public static Image img_dircenter0;
    public static Image img_dircenter1;
    public static Image img_infoBg;
    public static Image img_keypadX;
    public static Image img_keypadbg;
    public static Image img_leftbtn0;
    public static Image img_leftbtn1;
    public static Image img_leftbtn2;
    public static Image img_leftbtn3;
    public static Image[] img_menuL;
    public static Image[] img_menuR;
    public static Image img_menubg;
    public static Image img_rectbtn0;
    public static Image img_rectbtn1;
    public static Image img_rectbtn2;
    public static Image img_rightbtn0;
    public static Image img_rightbtn1;
    public static Image img_rightbtn2;
    public static Image img_rightbtn3;
    public static Image img_roundbtn0;
    public static Image img_roundbtn1;
    public static Image[] img_scroll;
    public static Image img_selgrid;
    public static Image[] img_tableBtn;
    public static Image[] img_triArrowL;
    public static Image[] img_triArrowR;
    private static final String[] keypadstr = {"1", "2ABC", "3DEF", "4GHI", "5JKL", "6MNO", "7PQRS", "8TUV", "9WXY", "*删除", "0", "#切换"};
    public static PipAnimateSet levelup;
    public static PipAnimateSet male = null;
    public static ImageSet npcName;
    public static ImageSet propsIconImg;
    public static ImageSet rank;
    public static Random rnd = new Random(System.currentTimeMillis());
    public static ImageSet shortcutImg;
    public static ImageSet smallNum;
    public static ImageSet touchImage;
    public static ImageSet uiLV;
    public static ImageSet uiMiscImg;
    public static ImageSet uiMiscImg2;

    static {
        int[] iArr = new int[24];
        iArr[0] = 13645824;
        iArr[1] = 15759360;
        iArr[2] = 15777864;
        iArr[3] = 15790232;
        iArr[4] = 5775384;
        iArr[5] = 4198416;
        iArr[6] = 14190640;
        iArr[7] = 15745024;
        iArr[8] = 15777888;
        iArr[9] = 8947848;
        iArr[10] = 15920617;
        iArr[11] = 3149840;
        iArr[12] = 10504240;
        iArr[13] = 10376246;
        iArr[14] = 12609600;
        iArr[15] = 13140024;
        iArr[17] = 7876648;
        iArr[18] = 15757376;
        iArr[19] = 9973792;
        iArr[20] = 15722444;
        iArr[21] = 15324574;
        iArr[22] = 14599036;
        iArr[23] = 12883540;
        CLR_TABLE = iArr;
        int[] iArr2 = new int[2];
        iArr2[1] = 16777215;
        SLIM_BORDER_COLOR = iArr2;
        int[] iArr3 = new int[6];
        iArr3[1] = 1;
        iArr3[2] = 2;
        iArr3[3] = 3;
        iArr3[4] = 2;
        iArr3[5] = 1;
        ARROW_FRAME_IDX = iArr3;
    }

    public static void drawBookAnimate(Graphics g, int x, int y) {
        uiMiscImg.drawFrame(g, bookFrame[(World.tick / 2) % 3], x, y, 0, 36);
    }

    public static void drawPet(Pet pet, Graphics g, int x, int y) {
        int y2;
        if (!pet.imageUpdate) {
            ImageLoadManager.requestImage(pet.getIconID(), pet);
        }
        if (pet != CommonData.player.getFollowPet()) {
            pet.cartoonPlayer.nextFrame();
        }
        int h = pet.getHeight();
        if (h < 50) {
            y2 = y + 45;
        } else {
            y2 = y + h + 1;
        }
        pet.cartoonPlayer.draw(g, (x - (pet.getWidth() / 2)) + pet.cartoonPlayer.getAnimate().getFrameOffset(pet.cartoonPlayer.getAnimateIndex(), pet.cartoonPlayer.getFrameIndex())[0], y2);
    }

    public static void drawBorderLine(Graphics g, int x, int y, int length, int pos) {
        switch (pos) {
            case 0:
                g.setColor(2892826);
                g.drawLine(x, y, x + length, y);
                g.setColor(15983532);
                g.drawLine(x, y + 1, x + length, y + 1);
                g.setColor(0);
                g.drawLine(x, y + 2, x + length, y + 2);
                g.setColor(3889001);
                g.drawLine(x, y + 3, x + length, y + 3);
                g.setColor(80221);
                g.drawLine(x, y + 4, x + length, y + 4);
                return;
            case 1:
                g.setColor(80221);
                g.drawLine(x, y, x + length, y);
                g.setColor(3889001);
                g.drawLine(x, y + 1, x + length, y + 1);
                g.setColor(0);
                g.drawLine(x, y + 2, x + length, y + 2);
                g.setColor(15983532);
                g.drawLine(x, y + 3, x + length, y + 3);
                g.setColor(2892826);
                g.drawLine(x, y + 4, x + length, y + 4);
                return;
            case 2:
                g.setColor(2892826);
                g.drawLine(x, y, x, y + length);
                g.setColor(15983532);
                g.drawLine(x + 1, y, x + 1, y + length);
                g.setColor(0);
                g.drawLine(x + 2, y, x + 2, y + length);
                g.setColor(3889001);
                g.drawLine(x + 3, y, x + 3, y + length);
                g.setColor(80221);
                g.drawLine(x + 4, y, x + 4, y + length);
                return;
            case 3:
                g.setColor(80221);
                g.drawLine(x, y, x, y + length);
                g.setColor(3889001);
                g.drawLine(x + 1, y, x + 1, y + length);
                g.setColor(0);
                g.drawLine(x + 2, y, x + 2, y + length);
                g.setColor(3889001);
                g.drawLine(x + 3, y, x + 3, y + length);
                g.setColor(80221);
                g.drawLine(x + 4, y, x + 4, y + length);
                return;
            default:
                return;
        }
    }

    public static void drawGameItemIcon(Graphics g, int iconId, int defId, int x, int y, int anchor) {
        int frame = 0;
        try {
            ImageSet iconImg = (ImageSet) GameItem.ItemIconMap.get(new Integer(iconId));
            if (iconImg == null) {
                iconImg = defaultIcon;
                frame = defId;
                if (iconId != -1) {
                    GameItem.requestIcon(iconId);
                }
            }
            iconImg.drawFrame(g, frame, x, y, 0, anchor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int drawMoney(Graphics gg, int m, int x, int y, int fillColor, int anchor, boolean whole) {
        String cstr;
        String sstr;
        String gstr;
        int giconw;
        String cstr2;
        boolean drawCopper;
        int siconw;
        boolean drawSilver;
        boolean drawCopper2;
        String sstr2;
        String cstr3;
        int c = m % 100;
        int s = (m / 100) % 100;
        int g = m / 10000;
        if (c == 0) {
            cstr = "";
        } else {
            cstr = String.valueOf(c);
        }
        if (s == 0 && g == 0) {
            sstr = "";
        } else {
            sstr = String.valueOf(s);
        }
        if (g == 0) {
            gstr = "";
        } else {
            gstr = String.valueOf(g);
        }
        boolean drawSilver2 = true;
        boolean drawGold = true;
        ImageSet img = uiMiscImg;
        int giconw2 = img.getFrameWidth(51);
        int siconw2 = img.getFrameWidth(52);
        int ciconw = img.getFrameWidth(53);
        if (sstr.equals("")) {
            siconw2 = 0;
            drawSilver2 = false;
        }
        if (gstr.equals("")) {
            drawGold = false;
            giconw = 0;
        } else {
            giconw = giconw2;
        }
        if (!cstr.equals("") || ((!drawSilver2 && !drawGold) || whole)) {
            cstr2 = String.valueOf(c);
            drawCopper = true;
        } else {
            ciconw = 0;
            cstr2 = cstr;
            drawCopper = false;
        }
        if (drawGold && drawSilver2 && !whole) {
            drawCopper = false;
            cstr2 = "";
            ciconw = 0;
        }
        if (gstr.length() < 4 || whole) {
            siconw = siconw2;
            drawSilver = drawSilver2;
            drawCopper2 = drawCopper;
            sstr2 = sstr;
            cstr3 = cstr2;
        } else {
            cstr3 = "";
            ciconw = 0;
            siconw = 0;
            drawSilver = false;
            drawCopper2 = false;
            sstr2 = "";
        }
        int gstrw = getSmallNumWidth(gstr);
        int sstrw = getSmallNumWidth(sstr2);
        int cstrw = getSmallNumWidth(cstr3);
        int width = giconw + siconw + ciconw + gstrw + sstrw + cstrw + 5;
        if ((anchor & 1) != 0) {
            x -= width / 2;
            anchor &= -2;
        } else if ((anchor & 8) != 0) {
            anchor &= -9;
            x -= width;
        }
        int anchor2 = anchor | 4;
        gg.setColor(fillColor);
        if (drawGold) {
            drawSmallNum(gstr, gg, x, y, anchor2, -1);
            int x2 = x + gstrw;
            img.drawFrame(gg, 51, x2, y, 0, anchor2);
            x = x2 + giconw;
        }
        if (drawSilver) {
            drawSmallNum(sstr2, gg, x, y, anchor2, -1);
            int x3 = x + sstrw;
            img.drawFrame(gg, 52, x3, y, 0, anchor2);
            x = x3 + siconw;
        }
        if (drawCopper2) {
            drawSmallNum(cstr3, gg, x, y, anchor2, -1);
            int x4 = x + cstrw;
            img.drawFrame(gg, 53, x4, y, 0, anchor2);
            int x5 = x4 + ciconw;
        }
        return width;
    }

    public static int getQuanlityColor(int quanlity) {
        if (quanlity < CLR_ITEM.length) {
            return CLR_ITEM[quanlity];
        }
        return CLR_ITEM[0];
    }

    public static int drawMoney(Graphics gg, int m, int x, int y, int fillColor, int anchor) {
        return drawMoney(gg, m, x, y, fillColor, anchor, false);
    }

    public static String getMoneyString(int m) {
        int c = m % 100;
        int s = (m / 100) % 100;
        int g = m / 10000;
        StringBuffer sb = new StringBuffer();
        if (g != 0) {
            sb.append(g).append("金");
        }
        if (s != 0) {
            sb.append(s).append("银");
        }
        if (c != 0) {
            sb.append(c).append("铜");
        }
        return sb.toString();
    }

    public static int getNextRnd(int min, int max) {
        if (max <= min) {
            return min;
        }
        return (Math.abs(rnd.nextInt()) % (max - min)) + min;
    }

    public static String[] splitString(String s, char ch) {
        return Utilities.splitString(s, String.valueOf(ch));
    }

    public static String[] splitString(String s) {
        return splitString(s, 10);
    }

    public static void fillAlphaRect(Graphics g, int rgb, int x, int y, int width, int height) {
        int alpha = rgb & -16777216;
        if (alpha != 0) {
            if ((alpha & -16777216) == -16777216) {
                g.setColor(16777215 & rgb);
                g.fillRect(x, y, width, height);
                return;
            }
            g.drawARGB(rgb, x, y, width, height);
        }
    }

    public static void draw3DString(Graphics g, String text, int x, int y, int color, int bgColor, int anchor, int quanlity) {
        if (bgColor == 1) {
            g.setColor(color);
            drawString(g, text, x, y, anchor);
            return;
        }
        switch (quanlity) {
            case 0:
                g.setColor(bgColor);
                drawString(g, text, x + 1, y - 1, anchor);
                drawString(g, text, x - 1, y + 1, anchor);
                drawString(g, text, x - 1, y - 1, anchor);
                drawString(g, text, x + 1, y + 1, anchor);
                drawString(g, text, x, y - 1, anchor);
                drawString(g, text, x, y + 1, anchor);
                drawString(g, text, x - 1, y, anchor);
                drawString(g, text, x + 1, y, anchor);
                break;
            case 1:
                g.setColor(bgColor);
                drawString(g, text, x, y - 1, anchor);
                drawString(g, text, x, y + 1, anchor);
                drawString(g, text, x - 1, y, anchor);
                drawString(g, text, x + 1, y, anchor);
                break;
            case 2:
                g.setColor(bgColor);
                drawString(g, text, x + 1, y + 1, anchor);
                break;
        }
        g.setColor(color);
        drawString(g, text, x, y, anchor);
    }

    public static void draw3DString(Graphics g, String text, int x, int y, int color, int bgColor, int anchor) {
        draw3DString(g, text, x, y, color, bgColor, anchor, draw3DQuanlity);
    }

    public static int getFontSize(boolean isTitle) {
        if (isTitle) {
            return 20;
        }
        return 16;
    }

    public static void drawTitle(Graphics g, String text, int x, int y, int color, int bgColor, int anchor) {
        drawLevelString(g, text, x, y, anchor, 1, 0);
    }

    public static void draw3DString(Graphics g, String text, int x, int y, int color, int bgColor) {
        draw3DString(g, text, x, y, color, bgColor, TOP_LEFT);
    }

    public static void drawString(Graphics g, String text, int x, int y, int anchor) {
        g.drawString(text, x, y - Utilities.CHAR_OFFSET, anchor);
    }

    public static void loadUIResFromRMS() {
        byte[] uiProgram = GTVM.loadRMSFile("ui");
        if (uiProgram != null) {
            try {
                World.uiPackage = new PackageFile(uiProgram);
            } catch (Exception e) {
            }
        }
    }

    public static void loadLocalUI() {
        try {
            World.localUIPackage = new PackageFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initUIRes() {
        loadLocalUI();
        loadUIResFromRMS();
        smallNum = new ImageSet((String) "/numsmall.jgp");
        smallNum.pipImg.toFullBuffer();
        uiMiscImg = new ImageSet((String) "/ui.jgp");
        uiMiscImg2 = new ImageSet((String) "/ui2.jgp");
        uiMiscImg.pipImg.toFullBuffer();
        uiMiscImg2.pipImg.toFullBuffer();
        imgFont = new ImageSet((String) "/imgFont.jgp");
        imgFont.pipImg.toFullBuffer();
        doorImage = new ImageSet((String) "/door.jgp");
        doorImage.pipImg.toFullBuffer();
        defaultIcon = new ImageSet((String) "/defaultIcon.jgp");
        defaultIcon.pipImg.toFullBuffer();
        uiLV = new ImageSet((String) "/lv.jgp");
        uiLV.pipImg.toFullBuffer();
        rank = new ImageSet((String) "/rank.jgp");
        emote = new PipAnimateSet(World.findResource("/e.ani", false));
        emote.toFullBuffer();
        defaultArmy = new PipAnimateSet(World.findResource("/da.ani", false));
        defaultArmy.toFullBuffer();
        levelup = new PipAnimateSet(World.findResource("/levelup.ani", false));
        levelup.toFullBuffer();
        chatEmote = new ImageSet((String) "/ce.jgp");
        chatEmote.pipImg.toFullBuffer();
        shortcutImg = new ImageSet((String) "/shortcut.jgp");
        shortcutImg.pipImg.toFullBuffer();
        npcName = new ImageSet((String) "/npcname.jgp");
        npcName.pipImg.toFullBuffer();
        Battle.emotePlayerRight = CartoonPlayer.playCartoon(emote, 6, 0, 0, true);
        Battle.emotePlayerLeft = CartoonPlayer.playCartoon(emote, 9, 0, 0, true);
        building = new ImageSet((String) "/building.jgp");
        building.pipImg.toFullBuffer();
        arrow = new ImageSet((String) "/arrow.jgp");
        arrow.pipImg.toFullBuffer();
        touchImage = new ImageSet((String) "/touchPanel.jgp");
        touchImage.pipImg.toFullBuffer();
        GlobalVar.setGlobalValue((String) "ISSIMPLEVERSION", 1);
        try {
            img_roundbtn0 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.round_btn0);
            img_roundbtn1 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.round_btn1);
            img_rightbtn0 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.right_btn0);
            img_rightbtn1 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.right_btn1);
            img_rightbtn2 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.right_btn2);
            img_rightbtn3 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.right_btn3);
            img_leftbtn0 = Image.createImage(img_rightbtn0, 0, 0, img_rightbtn0.getWidth(), img_rightbtn0.getHeight(), 2);
            img_leftbtn1 = Image.createImage(img_rightbtn1, 0, 0, img_rightbtn1.getWidth(), img_rightbtn1.getHeight(), 2);
            img_leftbtn2 = Image.createImage(img_rightbtn2, 0, 0, img_rightbtn2.getWidth(), img_rightbtn2.getHeight(), 2);
            img_leftbtn3 = Image.createImage(img_rightbtn3, 0, 0, img_rightbtn3.getWidth(), img_rightbtn3.getHeight(), 2);
            img_dir0 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.dir_arrow0);
            img_dir1 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.dir_arrow1);
            img_dircenter0 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.dir_center0);
            img_dircenter1 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.dir_center1);
            img_arrow = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.arrow);
            img_dirPadBg = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.bottom_areal);
            img_CornerBg = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.bottom_arear);
            img_rectbtn0 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.rectbtn0);
            img_rectbtn1 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.rectbtn1);
            img_rectbtn2 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.rectbtn2);
            Image corner0 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.corner0);
            img_corner0 = new Image[]{Image.createImage(corner0, 0, 0, corner0.getWidth(), corner0.getHeight(), 5), Image.createImage(corner0, 0, 0, corner0.getWidth(), corner0.getHeight(), 3), corner0, Image.createImage(corner0, 0, 0, corner0.getWidth(), corner0.getHeight(), 6)};
            Image corner1 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.corner1);
            img_corner1 = new Image[]{Image.createImage(corner1, 0, 0, corner0.getWidth(), corner0.getHeight(), 6), corner1, Image.createImage(corner1, 0, 0, corner0.getWidth(), corner0.getHeight(), 3), Image.createImage(corner1, 0, 0, corner0.getWidth(), corner0.getHeight(), 5)};
            Image trir0 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.tri_arrow0);
            Image trir1 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.tri_arrow1);
            Image trir2 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.tri_arrow2);
            img_triArrowL = new Image[]{Image.createTransImage(trir0, 2), Image.createTransImage(trir1, 2), Image.createTransImage(trir2, 2)};
            img_triArrowR = new Image[]{trir0, trir1, trir2};
            Image scrollTop = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.scroll0);
            Image tril2 = Image.createTransImage(Image.createTransImage(scrollTop, 2), 3);
            Image trir12 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.scroll1);
            img_scroll = new Image[]{scrollTop, tril2, trir12, Image.createTransImage(Image.createTransImage(trir12, 2), 3), Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.scroll2)};
            img_keypadbg = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.keypad);
            img_keypadX = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.x);
            img_arcL = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.arc);
            img_arcR = Image.createTransImage(img_arcL, 2);
            img_selgrid = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.selgrid);
            img_chatbg = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.chatbg);
            img_tableBtn = new Image[]{Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.table0), Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.table1), Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.table2)};
            Image menul0 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.menu0);
            Image menul1 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.menu1);
            Image menul2 = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.menu2);
            Image menur0 = Image.createTransImage(menul0, 2);
            Image menur1 = Image.createTransImage(menul1, 2);
            Image menur2 = Image.createTransImage(menul2, 2);
            img_menuL = new Image[]{menul0, menul1, menul2};
            img_menuR = new Image[]{menur0, menur1, menur2};
            img_menubg = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.menubg);
            img_battlebg = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.battlebg);
            img_infoBg = Image.createImage((Activity) MonsterMIDlet.instance, (int) R.drawable.infobg);
            initItemIcon();
            chatImg = Image.createImage((World.viewWidth - img_CornerBg.getWidth()) - img_dirPadBg.getWidth(), img_CornerBg.getHeight());
            chatImgGraphics = chatImg.getGraphics();
            int end = (World.viewWidth - img_CornerBg.getWidth()) - img_dirPadBg.getWidth();
            for (int dx = 0; dx <= end; dx += img_chatbg.getWidth()) {
                chatImgGraphics.drawImage(img_chatbg, dx, img_CornerBg.getHeight(), 36);
            }
            chatImgGraphics.drawImage(img_chatbg, end, img_CornerBg.getHeight(), 40);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawButton(Graphics g, String str, int x, int y, int w, boolean selected, int align) {
        drawButton(g, str, x, y, w, selected, true, align, 20);
    }

    public static void drawNpcName(Graphics g, String name, int touchx, int touchw, int y, boolean isGuide) {
        int fh;
        int fw;
        int offseth;
        int vgap;
        if (name == null) {
            fh = 0;
            fw = 0;
        } else if (isGuide) {
            int fw2 = npcName.getFrameWidth(8);
            fh = npcName.getFrameHeight(8);
            fw = fw2;
        } else {
            int fw3 = g.getFont().stringWidth(name);
            fh = g.getFont().getHeight();
            fw = fw3;
        }
        int touchx2 = touchx - ((fw - touchw) / 2);
        int x1 = touchx2 - 19;
        int y1 = y - 6;
        int x2 = touchx2 + fw + 19;
        int y2 = y + fh + 6;
        g.setColor(7876648);
        g.fillRect(x1 + 4, y1 + 2, (x2 - x1) - 8, (y2 - y1) - 4);
        int framew = npcName.getFrameWidth(6);
        int frameh = npcName.getFrameHeight(4);
        int frame0w = npcName.getFrameWidth(0);
        int frame1w = npcName.getFrameWidth(1);
        int frame0h = npcName.getFrameHeight(0);
        int frame2h = npcName.getFrameHeight(2);
        int i = 19 * 2;
        int hgap = ((fw + 38) - frame0w) - frame1w;
        int i2 = 6 * 2;
        int vgap2 = ((fh + 12) - frame0h) - frame2h;
        int offsetw = 0;
        if (hgap < framew) {
            int offsetw2 = npcName.getFrameWidth(6) - hgap;
            npcName.drawFrame(g, 6, x1 + frame0w, y1 + 2);
            npcName.drawFrame(g, 7, frame0w + x1, y2 - 5);
            offsetw = offsetw2;
            x2 += offsetw2;
        } else {
            int dx = frame0w + x1;
            do {
                npcName.drawFrame(g, 6, dx, y1 + 2);
                npcName.drawFrame(g, 7, dx, y2 - 5);
                dx += framew;
            } while (dx + framew < x2 - frame1w);
            npcName.drawFrame(g, 6, (x2 - frame1w) - framew, y1 + 2);
            npcName.drawFrame(g, 7, (x2 - frame1w) - framew, y2 - 5);
        }
        if (vgap2 < frameh) {
            offseth = npcName.getFrameHeight(4) - vgap2;
            npcName.drawFrame(g, 4, x1 + 1, y1 + frame0h);
            npcName.drawFrame(g, 5, x2 - 6, frame0h + y1);
            vgap = y2 + offseth;
        } else {
            int dy = y1 + frame0h;
            do {
                npcName.drawFrame(g, 4, x1 + 1, dy);
                npcName.drawFrame(g, 5, x2 - 6, dy);
                dy += frameh;
            } while (dy + frameh < y2 - frame2h);
            npcName.drawFrame(g, 4, x1 + 1, (y2 - frame2h) - frameh);
            npcName.drawFrame(g, 5, x2 - 6, (y2 - frame2h) - frameh);
            offseth = 0;
            vgap = y2;
        }
        npcName.drawFrame(g, 0, touchx2 - 19, y - 6);
        npcName.drawFrame(g, 1, touchx2 + fw + 19 + offsetw, y - 6, 0, 8);
        npcName.drawFrame(g, 2, touchx2 - 19, y + fh + 6 + offseth, 0, 32);
        npcName.drawFrame(g, 3, touchx2 + fw + 19 + offsetw, y + fh + 6 + offseth, 0, 40);
        if (isGuide) {
            int tick = (World.tick % 4) / 2;
            npcName.drawFrame(g, 8 + tick, touchx2 + (fw / 2), y, 0, 1);
            npcName.drawFrame(g, 12, (x2 - (tick * 3)) - 10, (vgap - (tick * 3)) - 6);
            return;
        }
        draw3DString(g, name, touchx2, y, 15920617, 7876648);
    }

    public static void drawButton(Graphics g, int x, int y, int width, int height, boolean selected, boolean enable) {
        int w = width;
        int h = height;
        if (!enable) {
            drawBoxNew(g, x, y, w, h, 278543);
            drawBoxNew(g, x + 1, y + 1, w - 2, (h / 2) - 2, 540675);
        } else if (selected) {
            x++;
            y++;
            if ((World.tick / 5) % 2 == 0) {
                drawBoxNew(g, x, y, w, h, 1073750031);
            } else {
                drawBoxNew(g, x, y, w, h, 1073743887);
            }
            drawBoxNew(g, x + 1, y + 1, w - 2, (h / 2) - 2, -2147483645);
        } else {
            drawBoxNew(g, x + 3, y + 3, w - 2, h - 2, 536887311);
            drawBoxNew(g, x, y, w, h, 2097231);
            drawBoxNew(g, x + 1, y + 1, w - 2, (h / 2) - 2, 4194563);
        }
        g.setColor(CLR_LIGHT);
        g.fillRect(x + 2, y + 2, 3, 3);
        g.drawLine(x + 5, y + 5, x + 5, y + 5);
        g.setColor(CLR_ITEM_WHITE);
        g.drawLine(x + 2, y + 3, x + 4, y + 3);
        g.drawLine(x + 3, y + 2, x + 3, y + 4);
    }

    public static void drawButton(Graphics g, String str, int x, int y, int w, boolean selected, boolean enable, int align, int anchor) {
        int x2;
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        drawButton(g, x, y, w, Utilities.CHAR_HEIGHT + 2, selected, enable);
        int left = ((w / 2) + x) - (Utilities.font.stringWidth(str) / 2);
        if (align == 4) {
            x2 = x + 7;
        } else {
            x2 = left;
        }
        if (selected) {
            x2++;
            y++;
        }
        if (str != null) {
            g.setColor(enable ? 0 : -10332567);
            g.drawString(str, x2, y + 1, 20);
        }
    }

    public static void drawUIButton(Graphics g, String txt, int y, int dir, boolean isFocus) {
        int i = 0;
        int w = img_rectbtn0.getWidth();
        if (dir == 4) {
            g.drawImage(isFocus ? img_rectbtn1 : img_rectbtn0, 2, y, 36);
            int i2 = 2 + (w / 2);
            int i3 = y - 17;
            if (isFocus) {
                i = 1;
            }
            drawLevelString(g, txt, i2, i3, 33, 2, i);
            return;
        }
        int x = (World.viewWidth - w) - 2;
        g.drawImage(isFocus ? img_rectbtn1 : img_rectbtn0, x, y, 36);
        int i4 = x + (w / 2);
        int i5 = y - 17;
        if (isFocus) {
            i = 1;
        }
        drawLevelString(g, txt, i4, i5, 33, 2, i);
    }

    public static void drawLine(Graphics g, int x, int y, int width, int height, int style) {
        int fillClr = 0;
        if ((1048576 & style) != 0) {
            fillClr = CLR_TINT;
        } else if ((2097152 & style) != 0) {
            fillClr = CLR_LIGHT;
        } else if ((4194304 & style) != 0) {
            fillClr = CLR_DARK;
        }
        g.setColor(fillClr);
        if (height == 1) {
            g.drawLine(x, y, x + width, y);
        } else if (width == 1) {
            g.drawLine(x, y, x, y + height);
        }
    }

    public static int getUIColor(int style) {
        if ((1048576 & style) != 0) {
            return CLR_TINT;
        }
        if ((2097152 & style) != 0) {
            return CLR_LIGHT;
        }
        if ((4194304 & style) != 0) {
            return CLR_DARK;
        }
        if ((16777216 & style) != 0) {
            return CLR_TITLE;
        }
        if ((33554432 & style) != 0) {
            return CLR_TEXT_LIGHT;
        }
        if ((67108864 & style) != 0) {
            return CLR_TEXT_DARK;
        }
        if ((134217728 & style) != 0) {
            return CLR_TEXT_NORMAL;
        }
        if ((268435456 & style) != 0) {
            return CLR_VERYDARK;
        }
        if ((536870912 & style) != 0) {
            return CLR_GRAY;
        }
        if ((Integer.MIN_VALUE & style) != 0) {
            return CLR_L_DARK;
        }
        if ((1073741824 & style) != 0) {
            return CLR_L_LIGHT;
        }
        if ((524288 & style) != 0) {
            return CLR_GRAY_DARK;
        }
        if ((262144 & style) != 0) {
            return CLR_GRAY_LIGHT;
        }
        return 0;
    }

    public static void drawBoxNew(Graphics g, int x, int y, int w, int h, int style) {
        char c;
        int[] borderClr;
        int fillw;
        int fillx = x;
        int filly = y;
        int fillw2 = w;
        int fillh = h;
        if ((style & 16) != 0) {
            c = 0;
        } else if ((style & 32) != 0) {
            c = 1;
        } else if ((style & 64) != 0) {
            c = 2;
        } else if ((style & 128) != 0) {
            c = 3;
        } else if ((style & 256) != 0) {
            c = 4;
        } else if ((style & 512) != 0) {
            c = 5;
        } else if ((style & 1024) != 0) {
            c = 5;
        } else if ((style & 2048) != 0) {
            c = 6;
        } else if ((style & 4096) != 0) {
            c = 7;
        } else if ((style & 8192) != 0) {
            c = 8;
        } else if ((style & 16384) != 0) {
            c = 9;
        } else {
            c = 65535;
        }
        int[] iArr = null;
        if (c >= 0) {
            borderClr = CLR_BORDER[c];
        } else {
            borderClr = new int[0];
        }
        int fillClr = 0;
        boolean fill = true;
        if ((8388608 & style) != 0) {
            fill = false;
        } else {
            fillClr = getUIColor(style);
        }
        int i = 0;
        while (true) {
            int i2 = i;
            fillw = fillw2;
            if (i2 >= borderClr.length) {
                break;
            }
            drawRoundRect(g, fillx, filly, fillw - 1, fillh - 1, borderClr.length, borderClr[i2], style);
            fillx++;
            filly++;
            fillw2 = fillw - 2;
            fillh -= 2;
            i = i2 + 1;
        }
        if (fill) {
            fillRoundRect(g, fillx, filly, fillw - 1, fillh - 1, borderClr.length, fillClr, fillClr, style);
        }
    }

    public static void drawButtonTip(Graphics g, int x, int y, int dir, String str) {
        if (dir == 4) {
            int h = Utilities.CHAR_HEIGHT;
            int y2 = (y - 2) - (h >> 1);
            int w = h + (h >> 1);
            g.setClip((x - h) + 1, y2, h + 1, h + 1);
            g.setColor(getUIColor(FILL_VERYDARK));
            g.drawRect(x - h, y2, h, h);
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            g.drawLine(x - h, y2, x - w, (h >> 1) + y2);
            g.drawLine(x - h, y2 + h, x - w, (h >> 1) + y2);
            g.drawString(str, (x - h) + ((h - Utilities.font.stringWidth(str)) >> 1), ((h - Utilities.CHAR_HEIGHT) >> 1) + y2, 20);
        } else if (dir == 8) {
            int h2 = Utilities.CHAR_HEIGHT;
            int y3 = (y - 2) - (h2 >> 1);
            int w2 = h2 + (h2 >> 1);
            g.setClip(x, y3, h2, h2 + 1);
            g.setColor(getUIColor(FILL_VERYDARK));
            g.drawRect(x, y3, w2, h2);
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            g.drawLine(x + h2, y3, x + w2, (h2 >> 1) + y3);
            g.drawLine(x + h2, y3 + h2, x + w2, (h2 >> 1) + y3);
            g.drawString(str, ((h2 - Utilities.font.stringWidth(str)) >> 1) + x, ((h2 - Utilities.CHAR_HEIGHT) >> 1) + y3, 20);
        } else if (dir == 16) {
            int w3 = Utilities.LINE_HEIGHT;
            int x2 = x - (w3 / 2);
            int i = w3 + (w3 / 2);
        } else if (dir == 32) {
            int w4 = Utilities.LINE_HEIGHT;
            int x3 = x - (w4 / 2);
            int i2 = (w4 / 2) + w4;
        }
    }

    public static void drawFocusFrame(Graphics g, int x, int y, int width) {
        int rx = (x - (width / 2)) - 2;
        int ry = (y - (width / 2)) - 2;
        int rw = width + 4;
        int rh = width + 4;
        g.setColor(getUIColor(67108864));
        g.drawLine(rx + 2, ry, rx + 7, ry);
        g.drawLine(rx + 2, ry + 2, rx + 8, ry + 2);
        g.drawLine(rx + 8, ry + 1, rx + 8, ry + 2);
        g.drawLine(rx, ry + 2, rx, ry + 7);
        g.drawLine(rx + 2, ry + 2, rx + 2, ry + 8);
        g.drawLine(rx + 1, ry + 8, rx + 2, ry + 8);
        g.drawLine(rx + 1, ry + 1, rx + 1, ry + 1);
        g.setColor(getUIColor(FILL_DARK));
        g.drawLine(rx + 2, ry + 1, rx + 7, ry + 1);
        g.drawLine(rx + 1, ry + 2, rx + 1, ry + 7);
        int rx2 = rx - 1;
        g.setColor(getUIColor(67108864));
        g.drawLine((rx2 + rw) - 2, ry, (rx2 + rw) - 7, ry);
        g.drawLine((rx2 + rw) - 2, ry + 2, (rx2 + rw) - 8, ry + 2);
        g.drawLine((rx2 + rw) - 8, ry + 1, (rx2 + rw) - 8, ry + 2);
        g.drawLine(rx2 + rw, ry + 2, rx2 + rw, ry + 7);
        g.drawLine((rx2 + rw) - 2, ry + 2, (rx2 + rw) - 2, ry + 8);
        g.drawLine((rx2 + rw) - 1, ry + 8, (rx2 + rw) - 2, ry + 8);
        g.drawLine((rx2 + rw) - 1, ry + 1, (rx2 + rw) - 1, ry + 1);
        g.setColor(getUIColor(FILL_DARK));
        g.drawLine((rx2 + rw) - 2, ry + 1, (rx2 + rw) - 7, ry + 1);
        g.drawLine((rx2 + rw) - 1, ry + 2, (rx2 + rw) - 1, ry + 7);
        int rx3 = rx2 + 1;
        int ry2 = ry - 1;
        g.setColor(getUIColor(67108864));
        g.drawLine(rx3 + 2, ry2 + rh, rx3 + 7, ry2 + rh);
        g.drawLine(rx3 + 2, (ry2 + rh) - 2, rx3 + 8, (ry2 + rh) - 2);
        g.drawLine(rx3 + 8, (ry2 + rh) - 1, rx3 + 8, (ry2 + rh) - 2);
        g.drawLine(rx3, (ry2 + rh) - 2, rx3, (ry2 + rh) - 7);
        g.drawLine(rx3 + 2, (ry2 + rh) - 2, rx3 + 2, (ry2 + rh) - 8);
        g.drawLine(rx3 + 1, (ry2 + rh) - 8, rx3 + 2, (ry2 + rh) - 8);
        g.drawLine(rx3 + 1, (ry2 + rh) - 1, rx3 + 1, (ry2 + rh) - 1);
        g.setColor(getUIColor(FILL_DARK));
        g.drawLine(rx3 + 2, (ry2 + rh) - 1, rx3 + 7, (ry2 + rh) - 1);
        g.drawLine(rx3 + 1, (ry2 + rh) - 2, rx3 + 1, (ry2 + rh) - 7);
        int rx4 = rx3 - 1;
        g.setColor(getUIColor(67108864));
        g.drawLine((rx4 + rw) - 2, ry2 + rh, (rx4 + rw) - 7, ry2 + rh);
        g.drawLine((rx4 + rw) - 2, (ry2 + rh) - 2, (rx4 + rw) - 8, (ry2 + rh) - 2);
        g.drawLine((rx4 + rw) - 8, (ry2 + rh) - 1, (rx4 + rw) - 8, (ry2 + rh) - 2);
        g.drawLine(rx4 + rw, (ry2 + rh) - 2, rx4 + rw, (ry2 + rh) - 7);
        g.drawLine((rx4 + rw) - 2, (ry2 + rh) - 2, (rx4 + rw) - 2, (ry2 + rh) - 8);
        g.drawLine((rx4 + rw) - 1, (ry2 + rh) - 8, (rx4 + rw) - 2, (ry2 + rh) - 8);
        g.drawLine((rx4 + rw) - 1, (ry2 + rh) - 1, (rx4 + rw) - 1, (ry2 + rh) - 1);
        g.setColor(getUIColor(FILL_DARK));
        g.drawLine((rx4 + rw) - 2, (ry2 + rh) - 1, (rx4 + rw) - 7, (ry2 + rh) - 1);
        g.drawLine((rx4 + rw) - 1, (ry2 + rh) - 2, (rw + rx4) - 1, (rh + ry2) - 7);
    }

    public static void drawBox(Graphics g, int x, int y, int w, int h, int style) {
        int[] fillClr;
        int[] clrs;
        int fillx = x;
        int filly = y;
        int fillw = w;
        int fillh = h;
        if ((style & 1) != 0 || (style & 1024) != 0 || (65536 & style) != 0 || (131072 & style) != 0) {
            int[] clrs2 = null;
            if ((style & 1) != 0) {
                clrs2 = HEAVY_BORDER_COLOR;
            } else if ((style & 1024) != 0) {
                clrs2 = HEAVY_BORDER_COLOR;
            } else if ((65536 & style) != 0) {
                clrs2 = ITEM2_BORDER_COLOR;
            } else if ((131072 & style) != 0) {
                clrs2 = ITEM2_HIGHLIGHT_BORDER_COLOR;
            }
            fillx += clrs2.length - 1;
            filly += clrs2.length - 1;
            fillw -= (clrs2.length - 1) << 1;
            fillh -= (clrs2.length - 1) << 1;
        } else if ((style & 4) != 0) {
            fillx += 2;
            filly += 3;
            fillw -= 4;
            fillh -= 5;
        } else if ((style & 8) != 0) {
            fillx += 2;
            filly += 2;
            fillw -= 4;
            fillh -= 4;
        } else if ((style & 16) != 0) {
            fillx += 2;
            filly += 2;
            fillw -= 4;
            fillh -= 4;
        }
        int[] fillClr2 = new int[0];
        if ((16777216 & style) != 0) {
            fillClr = TITLE_FILL_COLOR;
        } else if ((style & 1024) != 0) {
            fillClr = TAB_FILL_COLOR;
        } else if ((style & 2048) != 0) {
            fillClr = TAB_HIGHLIGHT_FILL_COLOR;
        } else if ((style & 4096) != 0) {
            fillClr = HINT_FILL_COLOR;
        } else if ((style & 8192) != 0) {
            fillClr = ITEM_FILL_COLOR;
        } else if ((style & 16384) != 0) {
            fillClr = ITEM_HIGHLIGHT_FILL_COLOR;
        } else if ((32768 & style) != 0) {
            fillClr = MENU_FILL_COLOR;
        } else if ((262144 & style) != 0) {
            fillClr = ITEM2_FILL_COLOR;
        } else if ((524288 & style) != 0) {
            fillClr = ITEM2_HIGHLIGHT_FILL_COLOR;
        } else {
            fillClr = fillClr2;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= fillClr.length) {
                break;
            }
            if (i2 == fillClr.length - 1) {
                fillAlphaRect(g, fillClr[i2], fillx, filly, fillw, fillh);
            } else {
                g.setColor(fillClr[i2] & CLR_ITEM_WHITE);
                g.drawRect(fillx, filly, fillw - 1, fillh - 1);
            }
            fillx++;
            filly++;
            fillw -= 2;
            fillh -= 2;
            i = i2 + 1;
        }
        if ((style & 1) != 0 || (style & 1024) != 0 || (65536 & style) != 0 || (131072 & style) != 0) {
            int[] clrs3 = null;
            int corner = 0;
            if ((style & 1) != 0) {
                corner = 5;
                clrs = HEAVY_BORDER_COLOR;
            } else if ((style & 1024) != 0) {
                corner = 5;
                clrs = HEAVY_BORDER_COLOR;
            } else if ((65536 & style) != 0) {
                corner = 15;
                clrs = ITEM2_BORDER_COLOR;
            } else if ((style & 131072) != 0) {
                corner = 16;
                clrs = ITEM2_HIGHLIGHT_BORDER_COLOR;
            } else {
                clrs = clrs3;
            }
            int bh = 0;
            while (true) {
                int i3 = bh;
                if (i3 >= clrs.length) {
                    uiMiscImg.drawFrame(g, corner, x, y, 0);
                    uiMiscImg.drawFrame(g, corner, x + w, y, 2, 8);
                    uiMiscImg.drawFrame(g, corner, x, y + h, 6, 32);
                    uiMiscImg.drawFrame(g, corner, x + w, y + h, 3, 40);
                    return;
                }
                g.setColor(clrs[i3]);
                int bx = x + i3;
                int by = y + i3;
                int bw = (w - 1) - (i3 << 1);
                int bh2 = (h - 1) - (i3 << 1);
                int bskip = (clrs.length - i3) - 1;
                g.drawLine(bx + bskip, by, (bx + bw) - bskip, by);
                g.drawLine(bx + bw, by + bskip, bx + bw, (by + bh2) - bskip);
                g.drawLine(bx, by + bskip, bx, (by + bh2) - bskip);
                g.drawLine(bx + bskip, by + bh2, (bw + bx) - bskip, bh2 + by);
                bh = i3 + 1;
            }
        } else if ((style & 4) != 0) {
            g.setColor(ITEM_BORDER_COLOR[0]);
            g.drawRect(x, y, w - 1, h - 1);
            g.setColor(ITEM_BORDER_COLOR[1]);
            g.drawRect(x + 1, y + 1, w - 3, h - 3);
            g.setColor(ITEM_BORDER_COLOR[2]);
            g.drawLine(x + 2, y + 2, (x + w) - 3, y + 2);
        } else if ((style & 8) != 0) {
            g.setColor(ITEM_HIGHLIGHT_BORDER_COLOR[0]);
            g.drawRect(x, y, w - 1, h - 1);
            g.setColor(ITEM_HIGHLIGHT_BORDER_COLOR[1]);
            g.drawRect(x + 1, y, w - 3, h - 2);
            g.setColor(ITEM_HIGHLIGHT_BORDER_COLOR[2]);
            g.drawLine(x + 2, y + 1, (x + w) - 3, y + 1);
        } else if ((style & 16) != 0) {
            g.setColor(SLIM_BORDER_COLOR[0]);
            g.drawRect(x, y, w - 1, h - 1);
            g.setColor(SLIM_BORDER_COLOR[1]);
            g.drawRect(x + 1, y + 1, w - 3, h - 3);
        }
    }

    public static final void drawArrow(Graphics g, int x, int y, int nstyle) {
        boolean animate;
        int i;
        int i2;
        if ((nstyle & 240) == 16 || (nstyle & 240) == 0) {
            animate = true;
        } else {
            animate = false;
        }
        int style = nstyle & 15;
        if (style == 0) {
            g.drawImage(img_triArrowL[0], x, y, 10);
        } else if (style == 1) {
            ImageSet imageSet = uiMiscImg;
            if (animate) {
                i2 = ARROW_FRAME_IDX[(World.tick / 3) % 6];
            } else {
                i2 = 0;
            }
            imageSet.drawFrame(g, i2 + 25, x, y, 1, 33);
        } else if (style == 2) {
            g.drawImage(img_triArrowR[0], x, y, 2);
        } else if (style == 3) {
            ImageSet imageSet2 = uiMiscImg;
            if (animate) {
                i = ARROW_FRAME_IDX[(World.tick / 3) % 6];
            } else {
                i = 0;
            }
            imageSet2.drawFrame(g, i + 25, x, y, 0, 1);
        }
    }

    public static final AbstractUnit getUnit(int id, int type) {
        switch (type) {
            case 0:
                return NewStage.getNpc(id);
            case 3:
                return NewStage.getMonster(id);
            case 4:
                return CommonData.player.getPet(id);
            case 8:
                return CommonData.player;
            case 10:
                return Battle.findBattlePet(id);
            case 11:
                return Battle.findBattleArmy(id);
            case 12:
                if (NewStage.fieldPets == null) {
                    return null;
                }
                for (int i = 0; i < NewStage.fieldPets.length; i++) {
                    if (id == NewStage.fieldPets[i].id) {
                        return NewStage.fieldPets[i];
                    }
                }
                return null;
            default:
                return null;
        }
    }

    public static void drawImageFont(Graphics g, String str, int x, int y) {
        char[] cs = str.toCharArray();
        for (char indexOf : cs) {
            int idx = IMAGE_FONT.indexOf(indexOf);
            if (idx >= 0) {
                imgFont.drawFrame(g, idx, x, y);
            }
            x += 13;
        }
    }

    public static void drawRoundRect(Graphics g, int x, int y, int width, int height, int round, int color) {
        drawRoundRect(g, x, y, width, height, round, color, 15);
    }

    public static void drawRoundRect(Graphics g, int x, int y, int width, int height, int round, int color, int corner) {
        int v_top_left_y;
        int h_top_left_x;
        int v_top_right_y;
        int h_top_right_x;
        int h_bottom_left_x;
        int v_bottom_left_y;
        int h_bottom_right_x;
        int v_bottom_right_y;
        boolean topLeft = (corner & 1) != 0;
        boolean topRight = (corner & 2) != 0;
        boolean bottomLeft = (corner & 4) != 0;
        int corner2 = (corner & 8) != 0 ? 1 : 0;
        g.setColor(color);
        int v_bottom_left_x = x;
        int v_top_left_x = x;
        int v_bottom_right_x = x + width;
        int v_top_right_x = v_bottom_right_x;
        int h_top_right_y = y;
        int h_top_left_y = y;
        int h_bottom_right_y = y + height;
        int h_bottom_left_y = h_bottom_right_y;
        if (topLeft) {
            v_top_left_y = y + round;
            h_top_left_x = x + round;
        } else {
            v_top_left_y = y;
            h_top_left_x = x;
        }
        if (topRight) {
            v_top_right_y = y + round;
            h_top_right_x = (x + width) - round;
        } else {
            v_top_right_y = y;
            h_top_right_x = x + width;
        }
        if (bottomLeft) {
            h_bottom_left_x = x + round;
            v_bottom_left_y = (y + height) - round;
        } else {
            h_bottom_left_x = x;
            v_bottom_left_y = y + height;
        }
        if (corner2 != 0) {
            h_bottom_right_x = (x + width) - round;
            v_bottom_right_y = (y + height) - round;
        } else {
            h_bottom_right_x = x + width;
            v_bottom_right_y = y + height;
        }
        g.drawLine(h_top_left_x, h_top_left_y, h_top_right_x, h_top_right_y);
        g.drawLine(v_top_left_x, v_top_left_y, v_bottom_left_x, v_bottom_left_y);
        g.drawLine(h_bottom_left_x, h_bottom_left_y, h_bottom_right_x, h_bottom_right_y);
        g.drawLine(v_top_right_x, v_top_right_y, v_bottom_right_x, v_bottom_right_y);
        if (topLeft) {
            g.drawLine(x, y + round, x + round, y);
            g.drawLine(x + 1, y + round, x + round, y + 1);
        }
        if (topRight) {
            g.drawLine((x + width) - round, y, x + width, y + round);
            g.drawLine((x + width) - round, y + 1, (x + width) - 1, y + round);
        }
        if (bottomLeft) {
            g.drawLine(x, (y + height) - round, x + round, y + height);
            g.drawLine(x + 1, (y + height) - round, x + round, (y + height) - 1);
        }
        if (corner2 != 0) {
            g.drawLine((x + width) - round, y + height, x + width, (y + height) - round);
            g.drawLine(((x + width) - round) - 1, y + height, x + width, ((y + height) - round) - 1);
        }
    }

    public static void fillRoundRect(Graphics g, int x, int y, int width, int height, int round, int fillColor, int borderColor) {
        fillRoundRect(g, x, y, width, height, round, fillColor, borderColor, 15);
    }

    public static void fillRoundRect(Graphics g, int x, int y, int width, int height, int round, int fillColor, int borderColor, int corner) {
        g.setColor(borderColor);
        for (int i = round; i >= 0; i--) {
            if (i == 0) {
                g.setColor(fillColor);
                g.fillRect(x, y, width + 1, height + 1);
                return;
            }
            drawRoundRect(g, x, y, width, height, round, fillColor, corner);
            x++;
            y++;
            width -= 2;
            height -= 2;
        }
    }

    public static int getSmallNumWidth(String str) {
        int frameIndex;
        ImageSet numImg = smallNum;
        int strWidth = 0;
        for (int i = 0; i < str.length(); i++) {
            String sn = str.substring(i, i + 1);
            if (sn.equals(" ")) {
                strWidth += 8;
            } else {
                if (sn.equals("+")) {
                    frameIndex = 10;
                } else if (sn.equals("-")) {
                    frameIndex = 11;
                } else if (sn.equals("~")) {
                    frameIndex = 12;
                } else if (sn.equals("%")) {
                    frameIndex = 13;
                } else if (sn.equals("/")) {
                    frameIndex = 14;
                } else if (sn.equals(":")) {
                    frameIndex = 15;
                } else if (sn.equals("=")) {
                    frameIndex = 16;
                } else if (sn.equals(".")) {
                    frameIndex = 17;
                } else {
                    frameIndex = Integer.parseInt(sn);
                }
                strWidth += numImg.getFrameWidth(frameIndex);
            }
        }
        return strWidth;
    }

    public static void drawBlurRect(Graphics g, int x, int y, int w, int h, int style) {
        int[] clr = {10564, 78160, 79449, 80221};
        if (style == 1) {
            clr = new int[]{2920400, 2522035, 2191009, 2058135};
        } else if (style == 2) {
            clr = new int[]{5210009, 7909832, 9687270, 10674422};
        }
        g.setColor(clr[0]);
        g.drawRect(x, y, w, h);
        g.setColor(clr[1]);
        g.drawRect(x + 1, y + 1, w - 2, h - 2);
        g.setColor(clr[2]);
        g.drawRect(x + 2, y + 2, w - 4, h - 4);
        g.setColor(clr[3]);
        g.fillRect(x + 3, y + 3, w - 5, h - 5);
    }

    public static void drawLevelString(Graphics g, String text, int x, int y, int anchor, int lv, int state) {
        switch (lv) {
            case 1:
                g.getPaint().setTextSize(20.0f);
                draw3DString(g, text, x, y, 7214, 2736339, anchor);
                g.getPaint().setTextSize(16.0f);
                return;
            case 2:
                switch (state) {
                    case 0:
                        g.setColor(12254974);
                        g.drawString(text, x + 1, y + 1, anchor);
                        g.drawString(text, x + 1, y, anchor);
                        g.drawString(text, x, y + 1, anchor);
                        g.setColor(1517399);
                        g.drawString(text, x, y, anchor);
                        return;
                    case 1:
                        g.setColor(16187066);
                        g.drawString(text, x + 1, y + 1, anchor);
                        g.drawString(text, x + 1, y, anchor);
                        g.drawString(text, x, y + 1, anchor);
                        g.setColor(9633792);
                        g.drawString(text, x, y, anchor);
                        return;
                    case 2:
                        g.setColor(16187066);
                        g.drawString(text, x + 1, y + 1, anchor);
                        g.drawString(text, x + 1, y, anchor);
                        g.drawString(text, x, y + 1, anchor);
                        g.setColor(3487029);
                        g.drawString(text, x, y, anchor);
                        return;
                    case 3:
                        g.setColor(12254974);
                        g.drawString(text, x + 1, y + 1, anchor);
                        g.drawString(text, x + 1, y, anchor);
                        g.drawString(text, x, y + 1, anchor);
                        g.setColor(2182460);
                        g.drawString(text, x, y, anchor);
                        return;
                    default:
                        return;
                }
            case 3:
                draw3DString(g, text, x, y, 14671839, 0, anchor);
                return;
            case 4:
                draw3DString(g, text, x, y, 16753152, 0, anchor);
                return;
            case 5:
                draw3DString(g, text, x, y, 16776398, 0, anchor);
                return;
            case 6:
                draw3DString(g, text, x, y, CLR_ITEM_WHITE, 0, anchor);
                return;
            case 7:
                draw3DString(g, text, x, y, 16776398, 0, anchor);
                return;
            case 8:
                draw3DString(g, text, x, y, 6381921, 0, anchor);
                return;
            case 9:
                g.setColor(3215376);
                g.drawString(text, x + 1, y + 1, anchor);
                g.drawString(text, x + 1, y, anchor);
                g.drawString(text, x, y + 1, anchor);
                g.setColor(16776192);
                g.drawString(text, x, y, anchor);
                return;
            default:
                draw3DString(g, text, x, y, CLR_ITEM_WHITE, 0, anchor);
                return;
        }
    }

    public static int drawSmallNum(String str, Graphics g, int x, int y, int anchor, int clr) {
        int offsetx;
        ImageSet numImg = smallNum;
        int offsetx2 = 0;
        int drawOffset = 0;
        int strWidth = getSmallNumWidth(str);
        if ((anchor & 1) != 0) {
            x -= strWidth >> 1;
            anchor = (anchor & -2) | 4;
        } else if ((anchor & 8) != 0) {
            x -= strWidth;
            anchor = (anchor & -9) | 4;
        }
        int frameIndex = 0;
        while (true) {
            int i = frameIndex;
            int offsetx3 = offsetx2;
            if (i >= str.length()) {
                return drawOffset;
            }
            String sn = str.substring(i, i + 1);
            int frameIndex2 = 0;
            if (sn.equals(" ")) {
                offsetx2 = offsetx3 + numImg.getFrameWidth(0);
                drawOffset = offsetx2;
            } else {
                if (sn.equals("+")) {
                    frameIndex2 = 10;
                    offsetx = numImg.getFrameWidth(10) + offsetx3;
                } else if (sn.equals("-")) {
                    frameIndex2 = 11;
                    offsetx = numImg.getFrameWidth(11) + offsetx3;
                } else if (sn.equals("~")) {
                    frameIndex2 = 12;
                    offsetx = numImg.getFrameWidth(12) + offsetx3;
                } else if (sn.equals("%")) {
                    frameIndex2 = 13;
                    offsetx = numImg.getFrameWidth(13) + offsetx3;
                } else if (sn.equals("/")) {
                    frameIndex2 = 14;
                    offsetx = numImg.getFrameWidth(14) + offsetx3;
                } else if (sn.equals(":")) {
                    frameIndex2 = 15;
                    offsetx = numImg.getFrameWidth(15) + offsetx3;
                } else if (sn.equals("=")) {
                    frameIndex2 = 16;
                    offsetx = numImg.getFrameWidth(16) + offsetx3;
                } else if (sn.equals(".")) {
                    frameIndex2 = 17;
                    offsetx = numImg.getFrameWidth(17) + offsetx3;
                } else {
                    try {
                        frameIndex2 = Integer.parseInt(sn);
                        offsetx = numImg.getFrameWidth(frameIndex2) + offsetx3;
                    } catch (NumberFormatException e) {
                        offsetx2 = numImg.getFrameWidth(frameIndex2) + offsetx3;
                    }
                }
                if (clr != -1) {
                    numImg.pipImg.changeColor(-1, clr);
                }
                numImg.drawFrame(g, frameIndex2, x + drawOffset, y, 0, anchor);
                drawOffset = offsetx;
                offsetx2 = offsetx;
            }
            frameIndex = i + 1;
        }
    }

    public static void drawNumStr(String str, Graphics g, int x, int y, int type, int anchor, int clr) {
        drawSmallNum(str, g, x, y, anchor, clr);
    }

    public static Rect drawKeyPad(Graphics g, int x, int y) {
        int keyw = img_keypadbg.getWidth();
        int keyh = img_keypadbg.getHeight();
        int keyX = img_keypadX.getWidth();
        int i = 7 * 2;
        int i2 = (8 * 2) + 14;
        int w = (keyw * 3) + 30 + keyX;
        int i3 = 16 * 3;
        int i4 = (7 * 2) + 48;
        int h = (keyh * 4) + 62;
        drawAlphaBox(g, w - keyX, h, x, y, -1, true);
        int dx = x + 8;
        int dy = y + 7;
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i6 >= 12) {
                g.drawImage(img_keypadX, (x + w) - keyX, y);
                Rect rect = new Rect(x, y, x + w, y + h);
                return rect;
            }
            g.drawImage(img_keypadbg, dx, dy);
            draw3DString(g, keypadstr[i6], dx + 10, dy + 8, 3149840, 12883540, 20);
            dx += keyw + 7;
            if (dx >= ((x + w) - keyX) - (8 * 2)) {
                dy += 16 + keyh;
                dx = x + 8;
            }
            i5 = i6 + 1;
        }
    }

    public static void openSysInput(String title, String defaultValue, int length, CommandListener listener, int inputtype) {
        if ((!(listener instanceof AbstractLowUI) || (((AbstractLowUI) listener).getStyle() & 2) == 0) && !Input.hasOpened) {
            Input.hasOpened = true;
            Input.init(listener, title, defaultValue, length, inputtype);
            Input.open();
        }
    }

    public static void drawAlphaBox(Graphics g, int mw, int mh, int mx, int my, int clr) {
        fillAlphaRect(g, clr, mx + 5, my + 5, mw - 9, mh - 9);
        g.setColor(2892826);
        g.drawRect(mx, my, mw, mh);
        g.setColor(15983532);
        g.drawRect(mx + 1, my + 1, mw - 2, mh - 2);
        g.setColor(80221);
        g.drawRect(mx + 2, my + 2, mw - 4, mh - 4);
        g.setColor(3889001);
        g.drawRect(mx + 3, my + 3, mw - 6, mh - 6);
        g.setColor(6060170);
        g.drawRect(mx + 4, my + 4, mw - 8, mh - 8);
    }

    public static void drawAlphaBox(Graphics g, int mw, int mh, int mx, int my, int corner1pos, boolean isAlpha) {
        if (isAlpha) {
            fillAlphaRect(g, -2013258706, mx + 5, my + 5, mw - 9, mh - 9);
        } else {
            g.setColor(2236962);
            g.fillRect(mx + 5, my + 5, mw - 9, mh - 9);
        }
        g.setColor(2892826);
        g.drawRect(mx, my, mw, mh);
        g.setColor(15983532);
        g.drawRect(mx + 1, my + 1, mw - 2, mh - 2);
        g.setColor(0);
        g.drawRect(mx + 2, my + 2, mw - 4, mh - 4);
        g.setColor(6060170);
        g.drawRect(mx + 3, my + 3, mw - 6, mh - 6);
        g.setColor(3889001);
        g.drawRect(mx + 4, my + 4, mw - 8, mh - 8);
    }

    private static void initItemIcon() throws IOException {
        for (int i = 0; i <= 264; i++) {
            GameItem.updateIcon(new ImageSet(Image.createImage((Activity) MonsterMIDlet.instance, R.drawable.item000 + i)), i);
        }
    }
}
