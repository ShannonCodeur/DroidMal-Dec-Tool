package com.sumsharp.monster.image;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.data.AbstractUnit;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class PipAnimateSet {
    public static final int HEAD_HEIGHT = 20;
    public static final int HEAD_WIDTH = 20;
    private int[][] animates;
    public byte attKeyFrame;
    private Image[] frameBuffer;
    private int[] frameWH;
    private int[][] frames;
    public byte headFrame;
    public short headIconX;
    public short headIconY;
    public String name;
    private String[] needFile;
    public ImageSet[] sourceImages;

    public PipAnimateSet(ImageSet[] images, byte[] def) {
        this.sourceImages = images;
        try {
            load(new DataInputStream(new ByteArrayInputStream(def)), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PipAnimateSet(byte[] def) {
        try {
            load(new DataInputStream(new ByteArrayInputStream(def)), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load(DataInputStream dis, boolean loadImgFile) throws IOException {
        this.headFrame = dis.readByte();
        this.headIconX = dis.readShort();
        this.headIconY = dis.readShort();
        this.attKeyFrame = dis.readByte();
        read(dis, true);
        read(dis, false);
        byte count = dis.readByte();
        if (loadImgFile) {
            this.needFile = new String[count];
            this.sourceImages = new ImageSet[count];
            for (int i = 0; i < count; i++) {
                this.needFile[i] = dis.readUTF();
                this.sourceImages[i] = ImageLoadManager.getImage(this.needFile[i]);
            }
        }
    }

    public void toFullBuffer() {
        for (ImageSet imageSet : this.sourceImages) {
            imageSet.pipImg.toFullBuffer();
        }
    }

    private void read(DataInputStream dis, boolean isFrame) throws IOException {
        byte count1 = dis.readByte() & 255;
        int[][] info = new int[count1][];
        int[] wh = new int[count1];
        for (int i = 0; i < count1; i++) {
            byte count2 = dis.readByte() & 255;
            info[i] = new int[count2];
            for (int j = 0; j < count2; j++) {
                info[i][j] = dis.readInt();
            }
            if (isFrame) {
                wh[i] = dis.readInt();
            }
        }
        if (isFrame) {
            this.frames = info;
            this.frameWH = wh;
            return;
        }
        this.animates = info;
    }

    public void drawFrame(Graphics g, int frame, int x, int y) {
        if (this.frameBuffer != null) {
            if (this.frameBuffer[frame] == null) {
                int w = this.frameWH[frame] >> 16;
                int h = this.frameWH[frame] & AbstractUnit.CLR_NAME_TAR;
                int[] rgb = new int[(w * h)];
                drawRGB(rgb, frame, w, 0, 0);
                this.frameBuffer[frame] = Image.createRGBImage(rgb, w, h, true);
            }
            int[] offset = getFrameOffset(frame);
            g.drawImage(this.frameBuffer[frame], x - offset[0], y - offset[1], 20);
            return;
        }
        int[] pieces = this.frames[frame];
        for (int i = 0; i < pieces.length; i++) {
            int pd = pieces[i];
            int imageID = (pd >> 29) & 7;
            int iframe = (pd >> 21) & AbstractUnit.CLR_NAME_TAR;
            int transition = (pd >> 18) & 7;
            int dx = (pd >> 9) & 511;
            if (dx > 255) {
                dx -= 512;
            }
            int dy = pd & 511;
            if (dy > 255) {
                dy -= 512;
            }
            this.sourceImages[imageID].drawFrame(g, iframe, x + dx, y + dy, transition);
        }
    }

    public void drawHead(Graphics g, int x, int y) {
        g.setClip(x, y, 20, 20);
        drawFrame(g, this.headFrame, x - this.headIconX, (y - this.headIconY) + 20);
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
    }

    public int[] getFrameOffset(int index, int time) {
        return getFrameOffset(getCurrentFrame(index, time));
    }

    public int[] getFrameOffset(int frame) {
        int[] pieces = this.frames[frame];
        int offsetX = 512;
        int offsetY = 512;
        for (int pd : pieces) {
            int dx = (pd >> 9) & 511;
            if (dx > 255) {
                dx -= 512;
            }
            int dy = pd & 511;
            if (dy > 255) {
                dy -= 512;
            }
            if (offsetX > dx) {
                offsetX = dx;
            }
            if (offsetY > dy) {
                offsetY = dy;
            }
        }
        return new int[]{-offsetX, -offsetY};
    }

    public void drawRGB(int[] rgb, int frame, int scanlines, int x, int y) {
        int[] offset = getFrameOffset(frame);
        int offsetX = offset[0];
        int offsetY = offset[1];
        int[] pieces = this.frames[frame];
        for (int pd : pieces) {
            int imageID = (pd >> 29) & 7;
            int iframe = (pd >> 21) & AbstractUnit.CLR_NAME_TAR;
            int transition = (pd >> 18) & 7;
            int dx = (pd >> 9) & 511;
            if (dx > 255) {
                dx -= 512;
            }
            int dy = pd & 511;
            if (dy > 255) {
                dy -= 512;
            }
            if (this.sourceImages[imageID].pipImg != null) {
                this.sourceImages[imageID].pipImg.drawRGB(rgb, scanlines, iframe, x + dx + offsetX, y + dy + offsetY, transition);
            }
        }
    }

    public int getAnimateFrameLength(int index) {
        int ret = 0;
        for (int i : this.animates[index]) {
            ret += i & 15;
        }
        return ret;
    }

    public void drawAnimateFrame(Graphics g, int index, int time, int x, int y) {
        int[] animate = this.animates[index];
        int tick = 0;
        int i = 0;
        while (i < animate.length) {
            int delay = animate[i] & 15;
            if (time < tick || time >= tick + delay) {
                tick += delay;
                i++;
            } else {
                int fr = (animate[i] >> 24) & AbstractUnit.CLR_NAME_TAR;
                int dx = (animate[i] >> 14) & 1023;
                if (dx > 511) {
                    dx -= 1024;
                }
                int dy = (animate[i] >> 4) & 1023;
                if (dy > 511) {
                    dy -= 1024;
                }
                drawFrame(g, fr, x + dx, y + dy);
                return;
            }
        }
    }

    public int[][] getAnimageFrameRGB(int index, int time) {
        int[][] ret = new int[2][];
        int[] rgb = new int[(getWidth(index, time) * getHeight(index, time))];
        int[] animate = this.animates[index];
        int[] aniOffset = {512, 512};
        int tick = 0;
        int i = 0;
        while (true) {
            if (i >= animate.length) {
                break;
            }
            int delay = animate[i] & 15;
            if (time < tick || time >= tick + delay) {
                tick += delay;
                i++;
            } else {
                int fr = (animate[i] >> 24) & AbstractUnit.CLR_NAME_TAR;
                int dx = (animate[i] >> 14) & 1023;
                if (dx > 511) {
                    dx -= 1024;
                }
                int dy = (animate[i] >> 4) & 1023;
                if (dy > 511) {
                    dy -= 1024;
                }
                if (dx < aniOffset[0]) {
                    aniOffset[0] = dx;
                }
                if (dy < aniOffset[1]) {
                    aniOffset[1] = dy;
                }
                drawRGB(rgb, fr, getWidth(index, time), 0, 0);
            }
        }
        ret[0] = rgb;
        ret[1] = aniOffset;
        return ret;
    }

    public int getAnimateLength() {
        return this.animates.length;
    }

    public int getAnimateLength(int index) {
        int ret = 0;
        for (int i : this.animates[index]) {
            ret += i & 15;
        }
        return ret;
    }

    public void clear() {
        if (this.needFile != null) {
            for (String release : this.needFile) {
                ImageLoadManager.release(release);
            }
            this.needFile = null;
        }
    }

    private int getCurrentFrame(int index, int time) {
        int[] animate = this.animates[index];
        int tick = 0;
        for (int i = 0; i < animate.length; i++) {
            int delay = animate[i] & 15;
            if (time >= tick && time < tick + delay) {
                return (animate[i] >> 24) & AbstractUnit.CLR_NAME_TAR;
            }
            tick += delay;
        }
        return -1;
    }

    public int getWidth(int index, int time) {
        int frame = getCurrentFrame(index, time);
        if (frame < 0) {
            return 0;
        }
        return this.frameWH[frame] >> 16;
    }

    public int getHeight(int index, int time) {
        int frame = getCurrentFrame(index, time);
        if (frame < 0) {
            return 0;
        }
        return this.frameWH[frame] & AbstractUnit.CLR_NAME_TAR;
    }
}
