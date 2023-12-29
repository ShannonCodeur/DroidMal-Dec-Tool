package javax.microedition.lcdui;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import com.sumsharp.monster.common.Utilities;

public class Font {
    private static final Font DEFAULT_FONT = new Font(null, 16);
    public static final int FACE_SYSTEM = 0;
    public static final int SIZE_LARGE = 40;
    public static final int SIZE_TEXT = 16;
    public static final int SIZE_TITLE = 20;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_UNDERLINED = 4;
    private int baseline;
    private int face;
    private Rect fontBounds;
    private int height;
    private Typeface iTypeface = Typeface.DEFAULT;
    private int size;
    private int style;
    private int width;

    public void setWidth(int width2) {
        this.width = this.size;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public int getWidth() {
        return this.size;
    }

    public Typeface getTypeface() {
        return this.iTypeface;
    }

    public void setSize(int fontSize) {
        this.size = fontSize;
    }

    public int getSize() {
        return this.size;
    }

    public Font(Typeface aTypeface, int aSize) {
        if (this.iTypeface != null) {
            this.iTypeface = aTypeface;
        }
        this.size = aSize;
        this.height = this.size;
        if (this.fontBounds == null) {
            this.fontBounds = new Rect();
        }
    }

    public static Font getFont(int fontSepcifier) {
        switch (fontSepcifier) {
            case 16:
                return new Font(null, 16);
            case 20:
                return new Font(null, 20);
            case 40:
                return new Font(null, 40);
            default:
                return DEFAULT_FONT;
        }
    }

    public static Font getDefaultFont() {
        return DEFAULT_FONT;
    }

    public static Font getFont(int face2, int style2, int size2) {
        switch (style2) {
            case 0:
                return new Font(Typeface.defaultFromStyle(0), size2);
            case 1:
                return new Font(Typeface.defaultFromStyle(1), size2);
            case 2:
                return new Font(Typeface.defaultFromStyle(2), size2);
            default:
                return DEFAULT_FONT;
        }
    }

    public static Font getFontWithSize(int size2) {
        return getFont(0, 0, size2);
    }

    public int getHeight() {
        if (Utilities.graphics == null) {
            return 16;
        }
        Paint fontPaint = Utilities.graphics.getPaint();
        if (fontPaint == null) {
            return 16;
        }
        fontPaint.getTextBounds("你", 0, "你".length(), this.fontBounds);
        return this.fontBounds.bottom - this.fontBounds.top;
    }

    public int charWidth(char ch) {
        if (Utilities.graphics == null) {
            return 16;
        }
        Paint fontPaint = Utilities.graphics.getPaint();
        if (fontPaint == null) {
            return 16;
        }
        char[] chs = {ch};
        float[] widths = new float[1];
        fontPaint.getTextWidths(chs, 0, chs.length, widths);
        return (int) widths[0];
    }

    public int charsWidth(char[] ch, int offset, int length) {
        if (Utilities.graphics == null) {
            return 16;
        }
        Paint fontPaint = Utilities.graphics.getPaint();
        if (fontPaint == null) {
            return ch.length * 16;
        }
        fontPaint.getTextBounds(ch, offset, length, this.fontBounds);
        return this.fontBounds.right - this.fontBounds.left;
    }

    public int stringWidth(String str) {
        if (Utilities.graphics == null) {
            return str.length() * 16;
        }
        Paint fontPaint = Utilities.graphics.getPaint();
        if (fontPaint == null) {
            return str.length() * 16;
        }
        fontPaint.getTextBounds(str, 0, str.length(), this.fontBounds);
        return this.fontBounds.right - this.fontBounds.left;
    }

    public int substringWidth(String str, int offset, int len) {
        return stringWidth(str.substring(offset, offset + len));
    }
}
