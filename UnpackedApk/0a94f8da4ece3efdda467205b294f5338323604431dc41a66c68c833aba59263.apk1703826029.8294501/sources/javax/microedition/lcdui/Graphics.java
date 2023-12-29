package javax.microedition.lcdui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;

public class Graphics {
    public static final int BASELINE = 64;
    public static final int BOTTOM = 32;
    public static final int DOTTED = 1;
    public static final int HCENTER = 1;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int SOLID = 0;
    public static final int TOP = 16;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_ROT90 = 5;
    public static final int VCENTER = 2;
    private Bitmap bitmap;
    private Canvas canvas;
    private Font font;
    private Paint paint = new Paint();

    public Graphics(Bitmap bmp) {
        this.bitmap = bmp;
        this.canvas = new Canvas(bmp);
        this.canvas.clipRect(0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public Graphics(Canvas canvas2) {
        this.canvas = canvas2;
        setFont(Font.getDefaultFont());
    }

    public Graphics() {
        setFont(Font.getDefaultFont());
    }

    public void UpdateCanvas(Canvas canvas2) {
        this.canvas = canvas2;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public boolean clipRect(int x, int y, int width, int height) {
        return this.canvas.clipRect(x, y, x + width, y + height);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        this.paint.setStyle(Style.STROKE);
        this.canvas.drawArc(new RectF((float) x, (float) y, (float) width, (float) height), (float) startAngle, (float) arcAngle, true, this.paint);
    }

    public void drawChar(char character, int x, int y, int anchor) {
        drawString(new String(new char[]{character}), x, y, anchor);
    }

    public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
        drawString(new String(data, offset, length), x, y, anchor);
    }

    public void drawImage(Image img, int x, int y) {
        Bitmap bmp = img.getBitmap();
        if (bmp != null) {
            this.canvas.drawBitmap(bmp, (float) x, (float) y, this.paint);
        }
    }

    public void drawImage(Image img, int x, int y, int anchor) {
        if ((anchor & 1) != 0) {
            x -= img.getWidth() / 2;
        }
        if ((anchor & 8) != 0) {
            x -= img.getWidth();
        }
        if ((anchor & 2) != 0) {
            y -= img.getHeight() / 2;
        }
        if ((anchor & 32) != 0) {
            y -= img.getHeight();
        }
        drawImage(img, x, y);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        if (x1 > x2) {
            x1++;
        } else {
            x2++;
        }
        if (y1 > y2) {
            y1++;
        } else {
            y2++;
        }
        this.canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, this.paint);
    }

    public void drawRect(int x, int y, int width, int height) {
        this.paint.setStyle(Style.STROKE);
        this.canvas.drawRect((float) x, (float) y, (float) (x + width), (float) (y + height), this.paint);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        this.paint.setStyle(Style.STROKE);
        this.canvas.drawRoundRect(new RectF((float) x, (float) y, (float) (x + width), (float) (y + height)), (float) arcWidth, (float) arcHeight, this.paint);
    }

    private void setTextAnchor(int anchor) {
        if ((anchor & 4) != 0) {
            this.paint.setTextAlign(Align.LEFT);
        }
        if ((anchor & 8) != 0) {
            this.paint.setTextAlign(Align.RIGHT);
        }
        if ((anchor & 1) != 0) {
            this.paint.setTextAlign(Align.CENTER);
        }
    }

    public void drawString(String str, int x, int y, int anchor) {
        setTextAnchor(anchor);
        if ((anchor & 16) != 0) {
            y += this.font.getHeight();
        }
        this.canvas.drawText(str, (float) x, (float) y, this.paint);
    }

    public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
        drawString(str.substring(offset, offset + len), x, y, anchor);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        this.paint.setStyle(Style.FILL);
        this.canvas.drawArc(new RectF((float) x, (float) y, (float) width, (float) height), (float) startAngle, (float) arcAngle, true, this.paint);
    }

    public void fillRect(int x, int y, int width, int height) {
        this.paint.setStyle(Style.FILL);
        this.canvas.drawRect((float) x, (float) y, (float) (x + width), (float) (y + height), this.paint);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        this.paint.setStyle(Style.FILL);
        this.canvas.drawRoundRect(new RectF((float) x, (float) y, (float) (x + width), (float) (y + height)), (float) arcWidth, (float) arcHeight, this.paint);
    }

    public int getBlueComponent() {
        return getColor() & AbstractUnit.CLR_NAME_TAR;
    }

    public int getClipHeight() {
        return this.canvas.getClipBounds().bottom - this.canvas.getClipBounds().top;
    }

    public int getClipWidth() {
        return this.canvas.getClipBounds().right - this.canvas.getClipBounds().left;
    }

    public int getClipX() {
        return this.canvas.getClipBounds().left;
    }

    public int getClipY() {
        return this.canvas.getClipBounds().top;
    }

    public int getColor() {
        return this.paint.getColor();
    }

    public Font getFont() {
        return this.font;
    }

    public int getGrayScale() {
        return ((getRedComponent() + getGreenComponent()) + getBlueComponent()) / 3;
    }

    public int getGreenComponent() {
        return (getColor() >> 8) & AbstractUnit.CLR_NAME_TAR;
    }

    public int getRedComponent() {
        return (getColor() >> 16) & AbstractUnit.CLR_NAME_TAR;
    }

    public void setClip(Rect r) {
        setClip(r.left, r.top, r.right - r.left, r.bottom - r.top);
    }

    public void setClip(RectF r) {
        setClip(r.left, r.top, r.right - r.left, r.bottom - r.top);
    }

    public void setClip(float x, float y, float width, float height) {
        Rect clip = this.canvas.getClipBounds();
        if (x != ((float) clip.left) || x + width != ((float) clip.right) || y != ((float) clip.top) || y + height != ((float) clip.bottom)) {
            this.canvas.clipRect(new RectF(x, y, x + width, y + height), Op.REPLACE);
        }
    }

    public void setClip(int x, int y, int width, int height) {
        Rect clip = this.canvas.getClipBounds();
        if (x != clip.left || x + width != clip.right || y != clip.top || y + height != clip.bottom) {
            this.canvas.clipRect(new Rect(x, y, x + width, y + height), Op.REPLACE);
        }
    }

    public void setColor(int RGB) {
        int alpha = (RGB >> 24) & AbstractUnit.CLR_NAME_TAR;
        int red = (RGB >> 16) & AbstractUnit.CLR_NAME_TAR;
        int green = (RGB >> 8) & AbstractUnit.CLR_NAME_TAR;
        int blue = RGB & AbstractUnit.CLR_NAME_TAR;
        if (alpha == 0) {
            this.paint.setARGB(AbstractUnit.CLR_NAME_TAR, red, green, blue);
        } else {
            this.paint.setARGB(alpha, red, green, blue);
        }
    }

    public void setColor(int red, int green, int blue) {
        this.paint.setARGB(AbstractUnit.CLR_NAME_TAR, red, green, blue);
    }

    public void setFont(Font font2) {
        this.font = font2;
        this.paint.setTextSize((float) font2.getSize());
        Utilities.CHAR_HEIGHT = font2.getHeight();
        Utilities.LINE_HEIGHT = Utilities.CHAR_HEIGHT + 2;
    }

    public void setGrayScale(int grey) {
        if (grey < 0 || grey > 255) {
            throw new IllegalArgumentException();
        }
        setColor(grey, grey, grey);
    }

    public void translate(int x, int y) {
        translate(x, y);
    }

    public void drawImage(Image img, int x, int y, int trans, int anchor) {
        drawRegion(img, 0, 0, img.getWidth(), img.getHeight(), trans, x, y, anchor);
    }

    public void drawRegion(Image src, int x_src, int y_src, int width, int height, int transform, int x_dst, int y_dst, int anchor) {
        if ((anchor & 2) != 0) {
            y_dst -= height / 2;
        } else if ((anchor & 32) != 0) {
            y_dst -= height;
        }
        if ((anchor & 8) != 0) {
            x_dst -= width;
        } else if ((anchor & 1) != 0) {
            x_dst -= width / 2;
        }
        this.canvas.save();
        setClip(x_dst, y_dst, width, height);
        if (transform == 0) {
            this.canvas.drawBitmap(src.getBitmap(), (float) (x_dst - x_src), (float) (y_dst - y_src), this.paint);
            this.canvas.restore();
            return;
        }
        Matrix mMatrix = new Matrix();
        Matrix temp = new Matrix();
        temp.setValues(new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f});
        switch (transform) {
            case 1:
                mMatrix.postConcat(temp);
                mMatrix.setRotate(180.0f, (float) (width / 2), (float) (height / 2));
                break;
            case 2:
                mMatrix.postConcat(temp);
                break;
            case 3:
                mMatrix.setRotate(180.0f, (float) (width / 2), (float) (height / 2));
                break;
            case 4:
                mMatrix.postConcat(temp);
                mMatrix.setRotate(270.0f, (float) (width / 2), (float) (height / 2));
                break;
            case 5:
                mMatrix.setRotate(90.0f, (float) (width / 2), (float) (height / 2));
                break;
            case 6:
                mMatrix.setRotate(270.0f, (float) (width / 2), (float) (height / 2));
                break;
            case 7:
                mMatrix.postConcat(temp);
                mMatrix.setRotate(90.0f, (float) (width / 2), (float) (height / 2));
                break;
        }
        mMatrix.setTranslate((float) x_dst, (float) y_dst);
        this.canvas.drawBitmap(src.getBitmap(), mMatrix, this.paint);
        this.canvas.restore();
    }

    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha) {
        if (rgbData == null) {
            throw new NullPointerException();
        } else if (width != 0 && height != 0) {
            int l = rgbData.length;
            if (width < 0 || height < 0 || offset < 0 || offset >= l || ((scanlength < 0 && (height - 1) * scanlength < 0) || (scanlength >= 0 && (((height - 1) * scanlength) + width) - 1 >= l))) {
                throw new ArrayIndexOutOfBoundsException();
            }
            this.canvas.drawBitmap(rgbData, offset, scanlength, x, y, width, height, processAlpha, this.paint);
        }
    }

    public void drawARGB(int rgb, int x, int y, int w, int h) {
        int cx = getClipX();
        int cy = getClipY();
        int cw = getClipWidth();
        int ch = getClipHeight();
        setClip(x, y, w, h);
        this.canvas.drawARGB((rgb >> 24) & AbstractUnit.CLR_NAME_TAR, (rgb >> 16) & AbstractUnit.CLR_NAME_TAR, (rgb >> 8) & AbstractUnit.CLR_NAME_TAR, rgb & AbstractUnit.CLR_NAME_TAR);
        setClip(cx, cy, cw, ch);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.paint.setStyle(Style.FILL);
        Path p = new Path();
        p.moveTo((float) x1, (float) y1);
        p.lineTo((float) x2, (float) y2);
        p.lineTo((float) x3, (float) y3);
        p.lineTo((float) x1, (float) y1);
        this.canvas.drawPath(p, this.paint);
    }

    public void setAntiAlias(boolean flag) {
        this.paint.setAntiAlias(flag);
    }

    public void setAlphaValue(int alpha) {
        this.paint.setAlpha(alpha);
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.paint.setStyle(Style.STROKE);
        Path p = new Path();
        p.moveTo((float) x1, (float) y1);
        p.lineTo((float) x2, (float) y2);
        p.lineTo((float) x3, (float) y3);
        p.lineTo((float) x1, (float) y1);
        this.canvas.drawPath(p, this.paint);
    }
}
