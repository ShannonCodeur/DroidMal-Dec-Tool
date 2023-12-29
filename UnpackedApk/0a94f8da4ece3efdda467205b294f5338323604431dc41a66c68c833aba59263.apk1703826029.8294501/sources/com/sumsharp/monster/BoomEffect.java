package com.sumsharp.monster;

import com.sumsharp.monster.common.Utilities;
import java.lang.reflect.Array;
import java.util.Random;
import javax.microedition.lcdui.TextField;

public class BoomEffect {
    private static final int[][] COLORS;
    public static int FRAME = 64;
    public static int RANDER = 32;
    public static int SIZE = 80;
    private short[][] canves;
    private int clr;
    private int frame;
    private int frameCount;
    private int[][] hot;
    private Random randGen;
    private int rander;
    private int size;
    private int step;

    static {
        int[] iArr = new int[16];
        iArr[1] = -16773120;
        iArr[2] = -16765952;
        iArr[3] = -16758784;
        iArr[4] = -16751360;
        iArr[5] = -16744192;
        iArr[6] = -16736000;
        iArr[7] = -16727552;
        iArr[8] = -16719360;
        iArr[9] = -16711936;
        iArr[10] = -16711835;
        iArr[11] = -16711787;
        iArr[12] = -16711738;
        iArr[13] = -16711681;
        iArr[14] = -8519681;
        iArr[15] = -1;
        int[] iArr2 = new int[16];
        iArr2[1] = -15728640;
        iArr2[2] = -13893632;
        iArr2[3] = -12058624;
        iArr2[4] = -10158080;
        iArr2[5] = -8323072;
        iArr2[6] = -6225920;
        iArr2[7] = -4063232;
        iArr2[8] = -1966080;
        iArr2[9] = -65536;
        iArr2[10] = -39680;
        iArr2[11] = -27392;
        iArr2[12] = -14848;
        iArr2[13] = -256;
        iArr2[14] = -131;
        iArr2[15] = -1;
        int[] iArr3 = new int[16];
        iArr3[1] = -16777200;
        iArr3[2] = -16777172;
        iArr3[3] = -16777144;
        iArr3[4] = -16777115;
        iArr3[5] = -16777087;
        iArr3[6] = -16777055;
        iArr3[7] = -16777022;
        iArr3[8] = -16776990;
        iArr3[9] = -16776961;
        iArr3[10] = -10157825;
        iArr3[11] = -7012097;
        iArr3[12] = -3800833;
        iArr3[13] = -65281;
        iArr3[14] = -33281;
        iArr3[15] = -1;
        COLORS = new int[][]{iArr, iArr2, iArr3};
    }

    public BoomEffect() {
        this.randGen = new Random(System.currentTimeMillis());
        this.step = 3;
        this.clr = Utilities.random(0, COLORS.length - 1);
        this.frameCount = FRAME;
        this.size = SIZE;
        this.rander = RANDER;
        init();
    }

    public BoomEffect(int clrIdx, int frameCount2, int size2, int rander2) {
        this.randGen = new Random(System.currentTimeMillis());
        this.step = 3;
        if (clrIdx < 0 || clrIdx >= COLORS.length) {
            this.clr = Utilities.random(0, COLORS.length - 1);
        }
        this.frameCount = frameCount2;
        this.size = size2;
        this.rander = rander2;
        init();
    }

    public int getSize() {
        return this.size;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public int getRander() {
        return this.rander;
    }

    public void setStep(int step2) {
        this.step = step2;
    }

    public int getStep() {
        return this.step;
    }

    private void init() {
        this.hot = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.rander, 4});
        this.canves = (short[][]) Array.newInstance(Short.TYPE, new int[]{this.size, this.size});
        this.frame = 0;
        for (int c1 = 0; c1 < this.rander; c1++) {
            int[] iArr = this.hot[c1];
            int[] iArr2 = this.hot[c1];
            int i = (this.size / 2) << 16;
            iArr2[1] = i;
            iArr[0] = i;
            this.hot[c1][2] = (short) (this.randGen.nextInt() % TextField.CONSTRAINT_MASK);
            this.hot[c1][3] = (short) (this.randGen.nextInt() % TextField.CONSTRAINT_MASK);
        }
    }

    private void extend(int t) {
        for (int i = 0; i < t; i++) {
            for (int c2 = 0; c2 < this.rander; c2++) {
                int[] iArr = this.hot[c2];
                iArr[0] = iArr[0] + this.hot[c2][2];
                int[] iArr2 = this.hot[c2];
                iArr2[1] = iArr2[1] + this.hot[c2][3];
            }
        }
    }

    public int[] nextFrame() {
        int c = this.frame;
        if (this.frame >= this.frameCount) {
            return null;
        }
        int color = (c < 16 ? c * 4 : 80 - c) >> 2;
        for (int c2 = 0; c2 < this.rander; c2++) {
            for (int x = -6; x <= 6; x++) {
                for (int y = -6; y <= 6; y++) {
                    int xx = (this.hot[c2][0] >> 16) + x;
                    int yy = (this.hot[c2][1] >> 16) + y;
                    if (xx > 0 && yy > 0 && xx < this.size && yy < this.size) {
                        short[] sArr = this.canves[yy];
                        sArr[xx] = (short) (sArr[xx] + (color >> ((Math.abs(x) + Math.abs(y)) / 3)));
                        if (this.canves[yy][xx] > 63) {
                            this.canves[yy][xx] = 63;
                        }
                    }
                }
            }
            int[] iArr = this.hot[c2];
            iArr[0] = iArr[0] + this.hot[c2][2];
            int[] iArr2 = this.hot[c2];
            iArr2[1] = iArr2[1] + this.hot[c2][3];
        }
        int[] m = new int[(this.size * this.size)];
        for (int x2 = 0; x2 < this.size; x2++) {
            for (int y2 = 0; y2 < this.size; y2++) {
                short c22 = this.canves[y2][x2];
                if (c22 < 8) {
                    this.canves[y2][x2] = 0;
                } else {
                    this.canves[y2][x2] = (short) (c22 / 4);
                }
                m[(this.size * y2) + x2] = COLORS[this.clr][this.canves[y2][x2]];
            }
        }
        this.frame += this.step;
        extend(this.step);
        return m;
    }
}
