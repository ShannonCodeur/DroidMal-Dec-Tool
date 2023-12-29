package com.sumsharp.lowui;

public class Font {
    /* JADX WARNING: type inference failed for: r0v43, types: [int] */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void drawChar(javax.microedition.lcdui.Graphics r9, int r10, int r11, int r12) {
        /*
            r0 = 22
            char[] r2 = new char[r0]
            r0 = 0
            r1 = 65
            r2[r0] = r1
            r0 = 2
            r1 = 127(0x7f, float:1.78E-43)
            r2[r0] = r1
            r0 = 4
            r1 = 129(0x81, float:1.81E-43)
            r2[r0] = r1
            r0 = 5
            r1 = 224(0xe0, float:3.14E-43)
            r2[r0] = r1
            r0 = 6
            r1 = 127(0x7f, float:1.78E-43)
            r2[r0] = r1
            r0 = 7
            r1 = 64
            r2[r0] = r1
            r0 = 8
            r1 = 85
            r2[r0] = r1
            r0 = 9
            r1 = 64
            r2[r0] = r1
            r0 = 10
            r1 = 255(0xff, float:3.57E-43)
            r2[r0] = r1
            r0 = 11
            r1 = 64
            r2[r0] = r1
            r0 = 12
            r1 = 85
            r2[r0] = r1
            r0 = 13
            r1 = 64
            r2[r0] = r1
            r0 = 14
            r1 = 84
            r2[r0] = r1
            r0 = 15
            r1 = 128(0x80, float:1.794E-43)
            r2[r0] = r1
            r0 = 16
            r1 = 126(0x7e, float:1.77E-43)
            r2[r0] = r1
            r0 = 17
            r1 = 128(0x80, float:1.794E-43)
            r2[r0] = r1
            r0 = 18
            r1 = 5
            r2[r0] = r1
            r0 = 19
            r1 = 64
            r2[r0] = r1
            r0 = 20
            r1 = 26
            r2[r0] = r1
            r0 = 21
            r1 = 32
            r2[r0] = r1
            r0 = 196(0xc4, float:2.75E-43)
            int[] r1 = new int[r0]
            r0 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r12 = r12 | r0
            r0 = 0
            r3 = r0
        L_0x007e:
            int r0 = r2.length
            if (r3 < r0) goto L_0x0090
            r2 = 0
            r3 = 14
            r6 = 14
            r7 = 14
            r8 = 1
            r0 = r9
            r4 = r10
            r5 = r11
            r0.drawRGB(r1, r2, r3, r4, r5, r6, r7, r8)
            return
        L_0x0090:
            int r0 = r3 >> 1
            int r6 = r0 * 14
            int r0 = r3 % 2
            int r5 = r0 * 8
            int r0 = r3 % 2
            if (r0 == 0) goto L_0x00a6
            r0 = 1
            r7 = r0
        L_0x009e:
            r0 = 7
            r4 = r0
        L_0x00a0:
            if (r4 >= 0) goto L_0x00a9
            int r0 = r3 + 1
            r3 = r0
            goto L_0x007e
        L_0x00a6:
            r0 = 0
            r7 = r0
            goto L_0x009e
        L_0x00a9:
            if (r7 == 0) goto L_0x00b2
            r0 = 5
            if (r4 >= r0) goto L_0x00b2
        L_0x00ae:
            int r0 = r4 + -1
            r4 = r0
            goto L_0x00a0
        L_0x00b2:
            char r0 = r2[r3]
            int r0 = r0 >> r4
            r0 = r0 & 1
            r8 = 1
            if (r0 != r8) goto L_0x00c4
            r0 = 1
        L_0x00bb:
            if (r0 == 0) goto L_0x00ae
            r0 = 7
            int r0 = r0 - r4
            int r0 = r0 + r5
            int r0 = r0 + r6
            r1[r0] = r12
            goto L_0x00ae
        L_0x00c4:
            r0 = 0
            goto L_0x00bb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.lowui.Font.drawChar(javax.microedition.lcdui.Graphics, int, int, int):void");
    }
}
