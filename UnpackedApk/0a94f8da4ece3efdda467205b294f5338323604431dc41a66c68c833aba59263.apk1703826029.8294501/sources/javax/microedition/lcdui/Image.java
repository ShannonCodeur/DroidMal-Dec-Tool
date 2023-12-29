package javax.microedition.lcdui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import java.io.IOException;
import java.io.InputStream;

public class Image {
    private Bitmap bitmap;
    private boolean isMutable;

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    private Image() {
    }

    public static Image createImage(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }
        Image ret = new Image();
        ret.bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        ret.isMutable = true;
        return ret;
    }

    public static Image createImage(String name) throws IOException {
        Image img = null;
        Bitmap bmp = BitmapFactory.decodeFile(name);
        if (bmp != null) {
            img = new Image();
            img.bitmap = bmp;
        }
        img.isMutable = false;
        return img;
    }

    public static Image createImage(Activity act, int resID) throws IOException {
        Image img = null;
        Bitmap bmp = BitmapFactory.decodeResource(act.getResources(), resID);
        if (bmp != null) {
            img = new Image();
            img.bitmap = bmp;
        }
        img.isMutable = false;
        return img;
    }

    public static Image createImage(Image source) {
        if (source == null) {
            return null;
        }
        Image img = new Image();
        img.bitmap = source.bitmap;
        img.isMutable = false;
        return img;
    }

    public static Image createImage(byte[] imageData, int imageOffset, int imageLength) {
        Image img = null;
        if (imageData == null) {
            return null;
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(imageData, imageOffset, imageLength);
        if (bmp != null) {
            img = new Image();
            img.bitmap = bmp;
        }
        img.isMutable = false;
        return img;
    }

    public Graphics getGraphics() {
        if (!this.isMutable) {
            return null;
        }
        Graphics g = new Graphics(this.bitmap);
        g.setColor(-1);
        g.setFont(Font.getDefaultFont());
        return g;
    }

    public int getHeight() {
        return this.bitmap.getHeight();
    }

    public int getWidth() {
        return this.bitmap.getWidth();
    }

    public boolean isMutable() {
        return this.isMutable;
    }

    public void getRGB(int[] argb, int offset, int scanlenght, int x, int y, int width, int height) {
    }

    public static Image createImage(InputStream stream) throws IOException {
        Bitmap bmp = BitmapFactory.decodeStream(stream);
        if (bmp == null) {
            return null;
        }
        Image img = new Image();
        img.bitmap = bmp;
        img.isMutable = false;
        return img;
    }

    public static Image createTransImage(Image img, int Transform) {
        return createImage(img, 0, 0, img.getWidth(), img.getHeight(), Transform);
    }

    public static Image createImage(Image image, int x, int y, int width, int height, int transform) {
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
        Bitmap bmp = image.bitmap;
        if (width > bmp.getWidth()) {
            width = bmp.getWidth();
        }
        if (height > bmp.getHeight()) {
            height = bmp.getHeight();
        }
        Image ret = new Image();
        ret.bitmap = Bitmap.createBitmap(bmp, x, y, width, height, mMatrix, true);
        return ret;
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        Bitmap bmp = Bitmap.createBitmap(rgb, width, height, Config.ARGB_8888);
        if (bmp == null) {
            return null;
        }
        Image img = new Image();
        img.bitmap = bmp;
        return img;
    }
}
