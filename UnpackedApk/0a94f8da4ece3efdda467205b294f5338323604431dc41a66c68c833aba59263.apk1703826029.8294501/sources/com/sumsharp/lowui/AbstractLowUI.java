package com.sumsharp.lowui;

import com.sumsharp.android.ui.GridMenu;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public abstract class AbstractLowUI {
    public static final String[] KEYS = {"0", ".,-?!'@:;/()\"=+1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9"};
    public static final int STYLE_BACKBTN = 512;
    public static final int STYLE_BORDER = 1;
    public static final int STYLE_BUTTON = 67108864;
    public static final int STYLE_CENTERTEXT = 16;
    public static final int STYLE_CHECK = 4;
    public static final int STYLE_COMBO = 262144;
    public static final int STYLE_MENUBTN = 256;
    public static final int STYLE_MULTILINE = 524288;
    public static final int STYLE_NOHGRIB = 16777216;
    public static final int STYLE_NONE = 0;
    public static final int STYLE_NOSYSTEM = 33554432;
    public static final int STYLE_NUMBER = 64;
    public static final int STYLE_PASSWORD = 32;
    public static final int STYLE_RADIO = 8;
    public static final int STYLE_READONLY = 2;
    public static final int STYLE_SELECTED = 65536;
    public static final int STYLE_TRANS = 131072;
    public static final int STYLE_UPTURN = 134217728;
    public final String[] INPUT_MODE = {"abc", "ABC", "123", "系统"};
    public final int INPUT_MODE_LOWERCHAR = 0;
    public final int INPUT_MODE_NUMBER = 2;
    public final int INPUT_MODE_SYSTEM = 3;
    public final int INPUT_MODE_UPPERCHAR = 1;
    protected int bgColor = Tool.CLR_TITLE;
    protected int borderColor = Tool.CLR_DARK;
    public int borderWidth = 6;
    protected Vector elements = new Vector();
    protected boolean enabled = true;
    protected Font font = Utilities.font;
    protected int frontColor = 0;
    protected int gridx;
    protected int gridy;
    public int hMargin = 2;
    protected boolean hasFocus = false;
    protected int height;
    protected int inputMode = 0;
    protected String name;
    protected AbstractLowUI parent;
    protected boolean relayouted = false;
    public int spaceWIdth = 1;
    protected int style = 0;
    protected UILayout uilLayout = new UILayout(1, true, true);
    public int vMargin = 3;
    protected int width;
    protected int x;
    protected int y;

    public abstract void handleKey();

    /* access modifiers changed from: protected */
    public abstract void layout();

    /* access modifiers changed from: protected */
    public void paint(Graphics g, int offx, int offy) {
        int x2 = this.x + offx;
        int y2 = this.y + offy;
        g.setClip(x2, y2, this.width, this.height);
        g.setFont(this.font);
        for (int i = 0; i < 1; i++) {
            Tool.drawRoundRect(g, this.spaceWIdth + x2 + i, this.spaceWIdth + y2 + i, (this.width - (this.spaceWIdth * 2)) - (i * 2), (this.height - (this.spaceWIdth * 2)) - (i * 2), 2, this.borderColor);
        }
    }

    public void switchInputMode() {
        this.inputMode++;
        if (this.inputMode >= this.INPUT_MODE.length) {
            this.inputMode = 0;
        }
    }

    public AbstractLowUI(String name2, AbstractLowUI parent2, int style2) {
        this.name = name2;
        this.style = style2;
        this.parent = parent2;
        if (parent2 != null) {
            parent2.add(this);
        }
    }

    public void add(AbstractLowUI component) {
        this.elements.addElement(component);
    }

    public char getKey(int keyCode, int idx) {
        int mode = getInputMode();
        switch (keyCode) {
            case 11:
                if (mode == 2) {
                    return '0';
                }
                break;
            case 12:
                if (mode == 2) {
                    return '1';
                }
                break;
            case Tool.IMAGE_FONT_WIDTH /*13*/:
                if (mode == 2) {
                    return '2';
                }
                break;
            case 14:
                if (mode == 2) {
                    return '3';
                }
                break;
            case Tool.EDGE_ROUND_ALL /*15*/:
                if (mode == 2) {
                    return '4';
                }
                break;
            case 16:
                if (mode == 2) {
                    return '5';
                }
                break;
            case 17:
                if (mode == 2) {
                    return '6';
                }
                break;
            case 18:
                if (mode == 2) {
                    return '7';
                }
                break;
            case 19:
                if (mode == 2) {
                    return '8';
                }
                break;
            case 20:
                if (mode == 2) {
                    return '9';
                }
                break;
            case 23:
                if (mode == 0) {
                    return 'q';
                }
                if (mode == 1) {
                    return 'Q';
                }
                break;
            case 24:
                if (mode == 0) {
                    return 'w';
                }
                if (mode == 1) {
                    return 'W';
                }
                break;
            case 25:
                if (mode == 0) {
                    return 'e';
                }
                if (mode == 1) {
                    return 'E';
                }
                break;
            case 26:
                if (mode == 0) {
                    return 'r';
                }
                if (mode == 1) {
                    return 'R';
                }
                break;
            case 27:
                if (mode == 0) {
                    return 't';
                }
                if (mode == 1) {
                    return 'T';
                }
                break;
            case 28:
                if (mode == 0) {
                    return 'y';
                }
                if (mode == 1) {
                    return 'Y';
                }
                break;
            case 29:
                if (mode == 0) {
                    return 'u';
                }
                if (mode == 1) {
                    return 'U';
                }
                break;
            case 30:
                if (mode == 0) {
                    return 'i';
                }
                if (mode == 1) {
                    return 'I';
                }
                break;
            case 31:
                if (mode == 0) {
                    return 'o';
                }
                if (mode == 1) {
                    return 'O';
                }
                break;
            case 32:
                if (mode == 0) {
                    return 'p';
                }
                if (mode == 1) {
                    return 'P';
                }
                break;
            case 33:
                if (mode == 0) {
                    return 'a';
                }
                if (mode == 1) {
                    return 'A';
                }
                break;
            case 34:
                if (mode == 0) {
                    return 's';
                }
                if (mode == 1) {
                    return 'S';
                }
                break;
            case 35:
                if (mode == 0) {
                    return 'd';
                }
                if (mode == 1) {
                    return 'D';
                }
                break;
            case 36:
                if (mode == 0) {
                    return 'f';
                }
                if (mode == 1) {
                    return 'F';
                }
                break;
            case 37:
                if (mode == 0) {
                    return 'g';
                }
                if (mode == 1) {
                    return 'G';
                }
                break;
            case 38:
                if (mode == 0) {
                    return 'h';
                }
                if (mode == 1) {
                    return 'H';
                }
                break;
            case 39:
                if (mode == 0) {
                    return 'j';
                }
                if (mode == 1) {
                    return 'J';
                }
                break;
            case 40:
                if (mode == 0) {
                    return 'k';
                }
                if (mode == 1) {
                    return 'K';
                }
                break;
            case 41:
                if (mode == 0) {
                    return 'l';
                }
                if (mode == 1) {
                    return 'L';
                }
                break;
            case 42:
                if (mode == 0) {
                    return 'z';
                }
                if (mode == 1) {
                    return 'Z';
                }
                break;
            case 43:
                if (mode == 0) {
                    return 'x';
                }
                if (mode == 1) {
                    return 'X';
                }
                break;
            case 44:
                if (mode == 0) {
                    return 'c';
                }
                if (mode == 1) {
                    return 'C';
                }
                break;
            case GridMenu.gridBound /*45*/:
                if (mode == 0) {
                    return 'v';
                }
                if (mode == 1) {
                    return 'V';
                }
                break;
            case 46:
                if (mode == 0) {
                    return 'b';
                }
                if (mode == 1) {
                    return 'B';
                }
                break;
            case 47:
                if (mode == 0) {
                    return 'n';
                }
                if (mode == 1) {
                    return 'N';
                }
                break;
            case 48:
                if (mode == 0) {
                    return 'm';
                }
                if (mode == 1) {
                    return 'M';
                }
                break;
            case 49:
                return ' ';
        }
        if (mode == 0) {
            int l = keyCode - 11;
            return KEYS[l].toLowerCase().charAt(idx % KEYS[l].length());
        } else if (mode != 1) {
            return ' ';
        } else {
            return KEYS[keyCode - 11].toUpperCase().charAt(idx);
        }
    }

    public AbstractLowUI getParent() {
        return this.parent;
    }

    public int getStyle() {
        return this.style;
    }

    public void setStyle(int style2) {
        this.style = style2;
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(int borderColor2) {
        this.borderColor = borderColor2;
    }

    public int getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(int bgColor2) {
        this.bgColor = bgColor2;
    }

    public int getFrontColor() {
        return this.frontColor;
    }

    public void setFrontColor(int frontColor2) {
        this.frontColor = frontColor2;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font2) {
        this.font = font2;
    }

    public boolean isHasFocus() {
        return this.hasFocus;
    }

    public void setHasFocus(boolean hasFocus2) {
        this.hasFocus = hasFocus2;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled2) {
        this.enabled = enabled2;
    }

    public UILayout getUilLayout() {
        return this.uilLayout;
    }

    public void setUilLayout(UILayout uilLayout2) {
        this.uilLayout = uilLayout2;
        this.parent.layout();
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x2) {
        this.x = x2;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y2) {
        this.y = Utilities.TITLE_HEIGHT + y2;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width2) {
        this.width = width2;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public Rect computSize() {
        return new Rect(this.width, this.height);
    }

    public int getInputMode() {
        return this.inputMode;
    }

    public void setInputMode(int inputMode2) {
        this.inputMode = inputMode2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public boolean pointerCheck(int px, int py) {
        return this.x <= px && px <= this.x + this.width && this.y <= py && py <= this.y + this.height;
    }

    public void lostFocus() {
    }
}
