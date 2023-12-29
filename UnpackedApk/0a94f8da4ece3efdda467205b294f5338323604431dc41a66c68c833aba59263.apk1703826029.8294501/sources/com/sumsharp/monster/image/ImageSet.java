package com.sumsharp.monster.image;

import com.sumsharp.monster.World;
import java.io.ByteArrayInputStream;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ImageSet {
    Image img;
    public String name;
    public PipImage pipImg;
    int tileNum;
    public long[] tileRects;
    Hashtable transBuffer;

    public ImageSet(byte[] data, int rows, int cols) {
        Image img2 = Image.createImage(data, 0, data.length);
        initSimpleFrameSet(img2, img2.getWidth() / cols, img2.getHeight() / rows, rows, cols);
    }

    public ImageSet(String imgFile, int rows, int cols) {
        this(World.findResource(imgFile, false), rows, cols);
    }

    public ImageSet(String imgFile, int fw, int fh, int ns) {
        byte[] data = World.findResource(imgFile, false);
        Image img2 = Image.createImage(data, 0, data.length);
        initSimpleFrameSet(img2, fw, fh, img2.getHeight() / fh, img2.getWidth() / fw);
    }

    private void initSimpleFrameSet(Image img2, int fw, int fh, int rows, int cols) {
        this.img = img2;
        this.tileNum = rows * cols;
        this.tileRects = new long[this.tileNum];
        long tileWidth = (long) fw;
        long tileHeight = (long) fh;
        for (int i = 0; i < this.tileNum; i++) {
            this.tileRects[i] = ((((long) (i % cols)) * tileWidth) << 48) | ((((long) (i / cols)) * tileHeight) << 32) | (tileWidth << 16) | tileHeight;
        }
    }

    public ImageSet(String imgFile) {
        try {
            byte[] data = World.findResource(imgFile, false);
            if (data[0] == -119 && data[1] == 80 && data[2] == 78 && data[3] == 71) {
                Image img2 = Image.createImage(data, 0, data.length);
                initSimpleFrameSet(img2, img2.getWidth(), img2.getHeight(), 1, 1);
                return;
            }
            this.pipImg = new PipImage(new ByteArrayInputStream(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageSet(Image img2) {
        try {
            initSimpleFrameSet(img2, img2.getWidth(), img2.getHeight(), 1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageSet(byte[] data) {
        try {
            if (data[0] == -119 && data[1] == 80 && data[2] == 78 && data[3] == 71) {
                Image img2 = Image.createImage(data, 0, data.length);
                initSimpleFrameSet(img2, img2.getWidth(), img2.getHeight(), 1, 1);
                return;
            }
            this.pipImg = new PipImage(new ByteArrayInputStream(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawFrame(Graphics g, int fid, int x, int y) {
        drawFrame(g, fid, x, y, 0, 20);
    }

    public void drawFrame(Graphics g, int fid, int x, int y, int trans) {
        drawFrame(g, fid, x, y, trans, 20);
    }

    public void drawFrame(Graphics g, int fid, int x, int y, int trans, int anchor) {
        int mx = x;
        int my = y;
        if (this.pipImg != null) {
            this.pipImg.draw(g, fid, x, y, trans, anchor);
            return;
        }
        long rct = this.tileRects[fid];
        int fx = (short) ((int) (rct >> 48));
        int fy = (short) ((int) (rct >> 32));
        int fw = (short) ((int) (rct >> 16));
        int fh = (short) ((int) rct);
        if (trans < 4) {
            if ((anchor & 1) > 0) {
                mx -= fw / 2;
            } else if ((anchor & 8) > 0) {
                mx -= fw;
            }
            if ((anchor & 2) > 0) {
                my -= fh / 2;
            } else if ((anchor & 32) > 0) {
                my -= fh;
            }
        } else {
            if ((anchor & 1) > 0) {
                mx -= fh / 2;
            } else if ((anchor & 8) > 0) {
                mx -= fh;
            }
            if ((anchor & 2) > 0) {
                my -= fw / 2;
            } else if ((anchor & 32) > 0) {
                my -= fw;
            }
        }
        drawPNGFrame(g, this.img, fid, fx, fy, fw, fh, trans, mx, my);
    }

    public int getFrameCount() {
        if (this.pipImg != null) {
            return this.pipImg.getFrameCount();
        }
        return this.tileNum;
    }

    public int getFrameWidth(int frame) {
        if (this.pipImg != null) {
            return this.pipImg.getWidth(frame);
        }
        if (this.tileRects == null || this.tileRects.length <= frame) {
            return 0;
        }
        return (int) ((this.tileRects[0] >> 16) & 65535);
    }

    public int getFrameHeight(int frame) {
        if (this.pipImg != null) {
            return this.pipImg.getHeight(frame);
        }
        if (this.tileRects == null || this.tileRects.length <= frame) {
            return 0;
        }
        return (int) (this.tileRects[0] & 65535);
    }

    public void drawPNGFrame(Graphics g, Image image, int fid, int x_src, int y_src, int width, int height, int trans, int x_dest, int y_dest) {
        if (trans != 0) {
            if (this.transBuffer == null) {
                this.transBuffer = new Hashtable();
            }
            Integer key = new Integer((fid << 3) | (trans & 7));
            Image frameImg = (Image) this.transBuffer.get(key);
            if (frameImg == null) {
                frameImg = Image.createImage(image, x_src, y_src, width, height, trans);
                this.transBuffer.put(key, frameImg);
            }
            g.drawImage(frameImg, x_dest, y_dest, 20);
            return;
        }
        g.clipRect(x_dest, y_dest, width, height);
        g.drawImage(image, x_dest - x_src, y_dest - y_src, 20);
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
    }
}
