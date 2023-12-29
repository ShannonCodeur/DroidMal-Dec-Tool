package com.sumsharp.monster.image;

import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.net.GZIP;
import com.sumsharp.monster.protocol.Protocol;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

public class PipImage {
    private static final byte COLOR_OP_CHANGE = 4;
    private static final byte COLOR_OP_DARKER = 2;
    private static final byte COLOR_OP_GRAY = 5;
    private static final byte COLOR_OP_LIGHTER = 1;
    private static final byte COLOR_OP_MASK = 3;
    private static final byte COLOR_OP_NONE = 0;
    private static final byte[] DATA_HEAD = {Protocol.TRADE_BuyItem, Protocol.TRADE_GetStoreClass, 84, Protocol.TRADE_GetStoreClass};
    private static final byte[] DUNZ_HEAD = {Protocol.TRADE_BuyItem, 85, 78, 90};
    private static final byte[] HEAD = {80, 73, 80};
    private static final byte[] HEAD_BIG = {83, 83, 76};
    private static final byte[] HEAD_SIMPLE = {83, 83, 80};
    public static final int MODE_FULLBUFFER = 2;
    public static final int MODE_HALFBUFFER = 1;
    public static final int MODE_NORMAL = 0;
    private static final byte[] PALETTE_HEAD = {80, 76, 84, Protocol.TRADE_SendStoreClass};
    private Object[] buffer;
    private int colorParam1;
    private int colorParam2;
    private int[] frameCollision;
    private byte[][] frameData;
    private int[] frameInfo;
    private int mode = 0;
    private byte nextColorOp;
    private int[][] palette;
    private Hashtable transBuffer;

    public PipImage(InputStream is) throws IOException {
        load(new DataInputStream(is));
        if (this.mode == 2) {
            toFullBuffer();
        }
    }

    public void toFullBuffer() {
        if (this.mode != 2) {
            this.mode = 2;
            this.buffer = new Object[(this.palette.length * this.frameData.length)];
            for (int i = 0; i < this.palette.length; i++) {
                for (int j = 0; j < this.frameData.length; j++) {
                    this.buffer[(this.frameData.length * i) + j] = make(i, j);
                }
            }
            this.frameData = null;
        }
    }

    public void drawRGB(byte[] g, int scanlines, int frame, int x, int y, int trans) {
        byte[] useData = this.frameData[frame % this.frameInfo.length];
        int fw = (this.frameInfo[frame] >> 10) & 1023;
        int fh = this.frameInfo[frame] & 1023;
        for (int h = 0; h < fh; h++) {
            for (int w = 0; w < fw; w++) {
                int dx = ((y + h) * scanlines) + x + w;
                if (useData[(h * fw) + w] != 0) {
                    g[dx] = useData[(h * fw) + w];
                }
            }
        }
    }

    public void drawRGB(int[] g, int scanlines, int frame, int x, int y, int trans) {
        int f = frame % this.frameInfo.length;
        int p = frame / this.frameInfo.length;
        byte[] useData = this.frameData[f];
        int fw = (this.frameInfo[f] >> 10) & 1023;
        int fh = this.frameInfo[f] & 1023;
        int[] draw = makeRGB(fw, fh, p, useData);
        if (trans == 1) {
            for (int h = 0; h < fh; h++) {
                for (int w = 0; w < fw; w++) {
                    int dx = ((((y + fh) - h) - 1) * scanlines) + x + w;
                    if ((-16777216 & draw[(h * fw) + w]) != 0) {
                        g[dx] = draw[(h * fw) + w];
                    }
                }
            }
        } else if (trans == 2) {
            for (int h2 = 0; h2 < fh; h2++) {
                for (int w2 = 0; w2 < fw; w2++) {
                    int dx2 = ((y + h2) * scanlines) + x + ((fw - w2) - 1);
                    if ((-16777216 & draw[(h2 * fw) + w2]) != 0) {
                        g[dx2] = draw[(h2 * fw) + w2];
                    }
                }
            }
        } else if (trans == 0) {
            for (int h3 = 0; h3 < fh; h3++) {
                for (int w3 = 0; w3 < fw; w3++) {
                    int dx3 = ((y + h3) * scanlines) + x + w3;
                    if ((-16777216 & draw[(h3 * fw) + w3]) != 0) {
                        g[dx3] = draw[(h3 * fw) + w3];
                    }
                }
            }
        }
    }

    public void draw(Graphics g, int frame, int x, int y, int trans) {
        draw(g, frame, x, y, trans, 20);
    }

    public void draw(Graphics g, int frame, int x, int y, int trans, int anchor) {
        try {
            Object drawData = getFrameData(frame);
            int fid = frame % this.frameInfo.length;
            int w = (this.frameInfo[fid] >> 10) & 1023;
            int h = this.frameInfo[fid] & 1023;
            if (trans == 7) {
                trans = 4;
            } else if (trans == 4) {
                trans = 7;
            }
            if (trans < 4) {
                if ((anchor & 1) > 0) {
                    x -= w / 2;
                } else if ((anchor & 8) > 0) {
                    x -= w;
                }
                if ((anchor & 2) > 0) {
                    y -= h / 2;
                } else if ((anchor & 32) > 0) {
                    y -= h;
                }
            } else {
                if ((anchor & 1) > 0) {
                    x -= h / 2;
                } else if ((anchor & 8) > 0) {
                    x -= h;
                }
                if ((anchor & 2) > 0) {
                    y -= w / 2;
                } else if ((anchor & 32) > 0) {
                    y -= w;
                }
            }
            if (trans == 0) {
                g.drawImage((Image) drawData, x, y, 20);
                return;
            }
            if (this.transBuffer == null) {
                this.transBuffer = new Hashtable();
            }
            Integer key = new Integer((frame << 3) | (trans & 7));
            Image frameImg = (Image) this.transBuffer.get(key);
            if (frameImg == null) {
                frameImg = Image.createImage((Image) drawData, 0, 0, w, h, trans);
                this.transBuffer.put(key, frameImg);
            }
            g.drawImage(frameImg, x, y, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getFrameData(int frame) {
        if (this.mode == 2) {
            return this.buffer[frame];
        }
        return make(frame / this.frameInfo.length, frame % this.frameInfo.length);
    }

    public int[] makeRGB(int w, int h, int p, byte[] useData) {
        int[] rgb = new int[(w * h)];
        if (p >= this.palette.length) {
            p = 0;
        }
        int[] usePal = this.palette[p];
        if (this.nextColorOp != 0) {
            usePal = performColorOp(usePal);
        }
        int id = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                rgb[id] = usePal[useData[id] & 255];
                id++;
            }
        }
        return rgb;
    }

    public int[] makeRGB(int palette2, int frame) {
        return makeRGB((this.frameInfo[frame] >> 10) & 1023, this.frameInfo[frame] & 1023, palette2, this.frameData[frame]);
    }

    private Object make(int p, int f) {
        int w = (this.frameInfo[f] >> 10) & 1023;
        int h = this.frameInfo[f] & 1023;
        return Image.createRGBImage(makeRGB(w, h, p, this.frameData[f]), w, h, true);
    }

    private boolean isSimpleVersion(byte[] head) {
        for (int i = 0; i < head.length; i++) {
            if (head[i] != HEAD_SIMPLE[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean isBigViersion(byte[] head) {
        for (int i = 0; i < head.length; i++) {
            if (head[i] != HEAD_BIG[i]) {
                return false;
            }
        }
        return true;
    }

    private void load(DataInputStream dis) throws IOException {
        int size;
        byte[] head = new byte[3];
        dis.read(head);
        byte c = dis.readByte() & 255;
        this.palette = new int[c][];
        for (int i = 0; i < c; i++) {
            this.palette[i] = readPalette(dis);
        }
        if (isSimpleVersion(head)) {
            short size2 = dis.readShort() & 65535;
            byte[] head2 = new byte[4];
            dis.read(head2);
            byte[] imgData = new byte[dis.readInt()];
            dis.read(imgData);
            if (head2[1] == 65) {
                imgData = GZIP.inflate(imgData);
            }
            DataInputStream dis2 = new DataInputStream(new ByteArrayInputStream(imgData));
            this.frameInfo = new int[size2];
            this.frameData = new byte[size2][];
            this.frameCollision = new int[size2];
            for (int i2 = 0; i2 < size2; i2++) {
                readSimple(dis2, i2);
            }
            return;
        }
        if (isBigViersion(head)) {
            size = dis.readShort() & TextField.CONSTRAINT_MASK;
        } else {
            size = dis.readByte() & AbstractUnit.CLR_NAME_TAR;
        }
        this.frameInfo = new int[size];
        this.frameData = new byte[size][];
        this.frameCollision = new int[size];
        for (int i3 = 0; i3 < size; i3++) {
            readFrame(dis, i3);
        }
    }

    public void save(DataOutputStream dos) throws IOException {
        dos.write(HEAD);
        dos.writeByte(this.palette.length);
        for (int[] writePalette : this.palette) {
            writePalette(dos, writePalette);
        }
        dos.writeByte(this.frameInfo.length);
        for (int i = 0; i < this.frameInfo.length; i++) {
            writeFrame(dos, i);
        }
    }

    public int getWidth(int frame) {
        return (this.frameInfo[frame % this.frameInfo.length] >> 10) & 1023;
    }

    public int getHeight(int frame) {
        return this.frameInfo[frame % this.frameInfo.length] & 1023;
    }

    public int getFrameCount() {
        return this.frameInfo.length * this.palette.length;
    }

    public int getBlockCount() {
        return this.frameInfo.length;
    }

    public int getPaletteCount() {
        return this.palette.length;
    }

    public void replacePalette(int index, int[] data) {
        this.palette[index] = data;
    }

    private int[] readPalette(DataInputStream dis) throws IOException {
        int[] ret = new int[dis.readInt()];
        dis.skip(4);
        for (int i = 0; i < ret.length; i++) {
            ret[i] = dis.readInt();
        }
        return ret;
    }

    private void writePalette(DataOutputStream dos, int[] pdata) throws IOException {
        dos.writeInt(pdata.length);
        dos.write(PALETTE_HEAD);
        for (int writeInt : pdata) {
            dos.writeInt(writeInt);
        }
    }

    public void readSimple(DataInputStream dis, int index) throws IOException {
        int flip = dis.readByte();
        int frame = dis.readByte();
        int width = dis.readShort();
        int height = dis.readShort();
        int collision = 0;
        if (dis.readByte() == 1) {
            collision = dis.readInt();
        }
        byte[] data = new byte[dis.readInt()];
        dis.readFully(data);
        this.frameData[index] = data;
        this.frameInfo[index] = ((flip & 7) << 28) | ((frame & AbstractUnit.CLR_NAME_TAR) << 20) | ((width & 1023) << 10) | (height & 1023);
        this.frameCollision[index] = collision;
    }

    private void readFrame(DataInputStream dis, int index) throws IOException {
        int len = dis.readInt() - 6;
        byte[] head = new byte[4];
        dis.readFully(head);
        int flip = dis.readByte();
        int frame = dis.readByte();
        int width = dis.readShort();
        int height = dis.readShort();
        int collision = 0;
        if (dis.readByte() == 1) {
            collision = dis.readInt();
        }
        byte[] data = new byte[len];
        dis.readFully(data);
        if (head[1] == 65) {
            data = GZIP.inflate(data);
        }
        this.frameData[index] = data;
        this.frameInfo[index] = ((flip & 7) << 28) | ((frame & AbstractUnit.CLR_NAME_TAR) << 20) | ((width & 1023) << 10) | (height & 1023);
        this.frameCollision[index] = collision;
    }

    private void writeFrame(DataOutputStream dos, int index) throws IOException {
        dos.writeInt(this.frameData[index].length + 6);
        dos.write(DUNZ_HEAD);
        int info = this.frameInfo[index];
        dos.writeByte(info >> 28);
        dos.writeByte(info >> 20);
        dos.writeShort((info >> 10) & 1023);
        dos.writeShort(info & 1023);
        if (this.frameCollision[index] == 0) {
            dos.writeByte(0);
        } else {
            dos.writeByte(0);
            dos.writeInt(this.frameCollision[index]);
        }
        dos.write(this.frameData[index]);
    }

    private static final int toRGB(int a, int r, int g, int b) {
        if (a > 255) {
            a = AbstractUnit.CLR_NAME_TAR;
        }
        if (a < 0) {
            a = 0;
        }
        if (r > 255) {
            r = AbstractUnit.CLR_NAME_TAR;
        }
        if (g > 255) {
            g = AbstractUnit.CLR_NAME_TAR;
        }
        if (b > 255) {
            b = AbstractUnit.CLR_NAME_TAR;
        }
        if (r < 0) {
            r = 0;
        }
        if (g < 0) {
            g = 0;
        }
        if (b < 0) {
            b = 0;
        }
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static final int[] parseRGB(int rgb) {
        return new int[]{(rgb >> 24) & AbstractUnit.CLR_NAME_TAR, (rgb >> 16) & AbstractUnit.CLR_NAME_TAR, (rgb >> 8) & AbstractUnit.CLR_NAME_TAR, rgb & AbstractUnit.CLR_NAME_TAR};
    }

    private static final int lighter(int rgb, int v) {
        int[] c = parseRGB(rgb);
        c[1] = c[1] + (v * 3);
        c[2] = c[2] + (v * 3);
        c[3] = c[3] + (v * 3);
        return toRGB(c[0], c[1], c[2], c[3]);
    }

    private static final int darker(int rgb, int v) {
        int[] c = parseRGB(rgb);
        c[1] = c[1] - (v * 3);
        c[2] = c[2] - (v * 3);
        c[3] = c[3] - (v * 3);
        return toRGB(c[0], c[1], c[2], c[3]);
    }

    private static final int gray(int rgb) {
        int[] c = parseRGB(rgb);
        int y = ((c[1] * 299) / 1000) + ((c[2] * 587) / 1000) + ((c[3] * 114) / 1000);
        c[1] = y;
        c[2] = y;
        c[3] = y;
        return toRGB(c[0], c[1], c[2], c[3]);
    }

    public void lighter(int value) {
        this.nextColorOp = 1;
        this.colorParam1 = value;
    }

    public void darker(int value) {
        this.nextColorOp = 2;
        this.colorParam1 = value;
    }

    public void mask(int value) {
        this.nextColorOp = 3;
        this.colorParam1 = value;
    }

    public void changeColor(int src, int dest) {
        this.nextColorOp = 4;
        this.colorParam1 = src;
        this.colorParam2 = dest;
    }

    public void gray() {
        this.nextColorOp = 5;
    }

    private int[] performColorOp(int[] pal) {
        return performColorOp(pal, true);
    }

    private int[] performColorOp(int[] pal, boolean reset) {
        int[] ret = new int[pal.length];
        for (int i = 0; i < ret.length; i++) {
            switch (this.nextColorOp) {
                case 1:
                    ret[i] = lighter(pal[i], this.colorParam1);
                    break;
                case 2:
                    ret[i] = darker(pal[i], this.colorParam1);
                    break;
                case 3:
                    ret[i] = pal[i] | this.colorParam1;
                    break;
                case 4:
                    if (pal[i] != this.colorParam1) {
                        ret[i] = pal[i];
                        break;
                    } else {
                        ret[i] = this.colorParam2;
                        break;
                    }
                case 5:
                    ret[i] = gray(pal[i]);
                    break;
            }
        }
        if (reset) {
            this.nextColorOp = 0;
        }
        return ret;
    }

    public static int[] transit(int[] rgb, int w, int h, int trans) {
        int[][] ret;
        if (trans == 0) {
            return rgb;
        }
        if (trans < 4) {
            ret = (int[][]) Array.newInstance(Integer.TYPE, new int[]{h, w});
        } else {
            ret = (int[][]) Array.newInstance(Integer.TYPE, new int[]{w, h});
        }
        int srcpos = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                switch (trans) {
                    case 1:
                        ret[(h - 1) - y][x] = rgb[srcpos];
                        break;
                    case 2:
                        ret[y][(w - 1) - x] = rgb[srcpos];
                        break;
                    case 3:
                        ret[(h - 1) - y][(w - 1) - x] = rgb[srcpos];
                        break;
                    case 4:
                        ret[(w - 1) - x][(h - 1) - y] = rgb[srcpos];
                        break;
                    case 5:
                        ret[x][(h - 1) - y] = rgb[srcpos];
                        break;
                    case 6:
                        ret[(w - 1) - x][y] = rgb[srcpos];
                        break;
                    case 7:
                        ret[x][y] = rgb[srcpos];
                        break;
                }
                srcpos++;
            }
        }
        int[] ret2 = new int[(w * h)];
        int tarpos = 0;
        for (int i = 0; i < ret.length; i++) {
            System.arraycopy(ret[i], 0, ret2, tarpos, ret[i].length);
            tarpos += ret[i].length;
        }
        return ret2;
    }

    public static void drawRGB(int[] src, int srcwidth, int[] dest, int destwidth, int dx, int dy) {
        int i = 0;
        while (i < src.length) {
            System.arraycopy(src, i, dest, (dy * destwidth) + dx, srcwidth);
            i += srcwidth;
        }
    }

    public static void drawRGB(byte[] srcData, PipImage srcImg, int srcwidth, int offset, int length, int[] dest, int destwidth, int dx, int dy) {
        drawRGB(srcData, srcImg, srcwidth, offset, 0, srcwidth, length, dest, destwidth, dx, dy);
    }

    public static void drawRGB(byte[] srcData, PipImage srcImg, int srcwidth, int offset, int copyOffset, int copyLength, int length, int[] dest, int destwidth, int dx, int dy) {
        if (srcwidth != 0 && destwidth != 0 && offset < srcData.length) {
            if (length == 0) {
                length = srcData.length;
            }
            int[] src = srcImg.makeRGB(srcwidth, srcData.length / srcwidth, 0, srcData);
            int i = offset;
            while (i < length && i < srcData.length) {
                try {
                    System.arraycopy(src, i + copyOffset, dest, ((((i - offset) / srcwidth) + dy) * destwidth) + dx, copyLength);
                } catch (Exception e) {
                }
                i += srcwidth;
            }
        }
    }

    public static void drawRGB(byte[] srcData, PipImage srcImg, int srcwidth, int[] dest, int destwidth, int dx, int dy) {
        if (srcwidth != 0 && destwidth != 0) {
            int[] src = srcImg.makeRGB(srcwidth, srcData.length / srcwidth, 0, srcData);
            int i = 0;
            while (i < src.length) {
                System.arraycopy(src, i, dest, (((i / srcwidth) + dy) * destwidth) + dx, srcwidth);
                i += srcwidth;
            }
        }
    }

    public static int alpha(int c1, int c2, int alpha) {
        int[] cc1 = parseRGB(c1);
        int[] cc2 = parseRGB(c2);
        return toRGB(AbstractUnit.CLR_NAME_TAR, ((cc1[1] * (AbstractUnit.CLR_NAME_TAR - alpha)) + (cc2[1] * alpha)) / AbstractUnit.CLR_NAME_TAR, ((cc1[2] * (AbstractUnit.CLR_NAME_TAR - alpha)) + (cc2[2] * alpha)) / AbstractUnit.CLR_NAME_TAR, ((cc1[3] * (AbstractUnit.CLR_NAME_TAR - alpha)) + (cc2[3] * alpha)) / AbstractUnit.CLR_NAME_TAR);
    }
}
