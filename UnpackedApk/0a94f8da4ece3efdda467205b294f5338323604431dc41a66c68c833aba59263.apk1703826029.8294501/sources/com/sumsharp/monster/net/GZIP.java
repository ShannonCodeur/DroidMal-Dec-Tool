package com.sumsharp.monster.net;

import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.ui.UIManagerPanel;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.TextField;

public class GZIP {
    private static final int BTYPE_DYNAMIC = 2;
    private static final int BTYPE_FIXED = 1;
    private static final int BTYPE_NONE = 0;
    private static final int BTYPE_RESERVED = 3;
    private static final int EOB_CODE = 256;
    private static final int FCOMMENT_MASK = 16;
    private static final int FEXTRA_MASK = 4;
    private static final int FHCRC_MASK = 2;
    private static final int FNAME_MASK = 8;
    private static final int FTEXT_MASK = 1;
    private static final int MAX_BITS = 16;
    private static final int MAX_CODE_DISTANCES = 31;
    private static final int MAX_CODE_LENGTHS = 18;
    private static final int MAX_CODE_LITERALS = 287;
    private static byte[] buffer;
    private static int buffer_bit;
    private static int buffer_byte;
    private static int buffer_index;
    private static byte[] distance_extra_bits;
    private static short[] distance_values;
    private static byte[] dynamic_length_order;
    private static byte[] length_extra_bits;
    private static short[] length_values;
    private static byte[] uncompressed;
    private static int uncompressed_index;

    public static void clearObject() {
        buffer = null;
        uncompressed = null;
        length_extra_bits = null;
        length_extra_bits = null;
        length_values = null;
        distance_extra_bits = null;
        distance_values = null;
        dynamic_length_order = null;
    }

    public static DataInputStream openDataInputStream(byte[] gzip) throws IOException {
        return new DataInputStream(new ByteArrayInputStream(inflate(gzip)));
    }

    public static synchronized byte[] inflate(byte[] gzip) throws IOException {
        int bfinal;
        byte[] bArr;
        byte[] bArr2;
        int i;
        byte[] bArr3;
        int i2;
        synchronized (GZIP.class) {
            try {
                buffer = gzip;
                if (readBits(16) == 35615 && readBits(8) == 8) {
                    int flg = readBits(8);
                    buffer_index += 6;
                    if ((flg & 4) != 0) {
                        buffer_index += readBits(16);
                    }
                    if ((flg & 8) != 0) {
                        do {
                            bArr3 = buffer;
                            i2 = buffer_index;
                            buffer_index = i2 + 1;
                        } while (bArr3[i2] != 0);
                    }
                    if ((flg & 16) != 0) {
                        do {
                            bArr2 = buffer;
                            i = buffer_index;
                            buffer_index = i + 1;
                        } while (bArr2[i] != 0);
                    }
                    if ((flg & 2) != 0) {
                        buffer_index += 2;
                    }
                    int index = buffer_index;
                    buffer_index = buffer.length - 4;
                    uncompressed = new byte[(readBits(16) | (readBits(16) << 16))];
                    buffer_index = index;
                    byte[] bArr4 = new byte[MAX_CODE_DISTANCES];
                    bArr4[8] = 1;
                    bArr4[9] = 1;
                    bArr4[10] = 1;
                    bArr4[11] = 1;
                    bArr4[12] = 2;
                    bArr4[13] = 2;
                    bArr4[14] = 2;
                    bArr4[15] = 2;
                    bArr4[16] = 3;
                    bArr4[17] = 3;
                    bArr4[MAX_CODE_LENGTHS] = 3;
                    bArr4[19] = 3;
                    bArr4[20] = 4;
                    bArr4[21] = 4;
                    bArr4[22] = 4;
                    bArr4[23] = 4;
                    bArr4[24] = 5;
                    bArr4[25] = 5;
                    bArr4[26] = 5;
                    bArr4[27] = 5;
                    bArr4[29] = UIManagerPanel.UI_TYPE_ROLLPANEL;
                    bArr4[30] = UIManagerPanel.UI_TYPE_ROLLPANEL;
                    length_extra_bits = bArr4;
                    short[] sArr = new short[MAX_CODE_DISTANCES];
                    sArr[0] = 3;
                    sArr[1] = 4;
                    sArr[2] = 5;
                    sArr[3] = 6;
                    sArr[4] = 7;
                    sArr[5] = 8;
                    sArr[6] = 9;
                    sArr[7] = 10;
                    sArr[8] = 11;
                    sArr[9] = 13;
                    sArr[10] = 15;
                    sArr[11] = 17;
                    sArr[12] = 19;
                    sArr[13] = 23;
                    sArr[14] = 27;
                    sArr[15] = 31;
                    sArr[16] = 35;
                    sArr[17] = 43;
                    sArr[MAX_CODE_LENGTHS] = 51;
                    sArr[19] = 59;
                    sArr[20] = 67;
                    sArr[21] = 83;
                    sArr[22] = 99;
                    sArr[23] = 115;
                    sArr[24] = 131;
                    sArr[25] = 163;
                    sArr[26] = 195;
                    sArr[27] = 227;
                    sArr[28] = 258;
                    length_values = sArr;
                    byte[] bArr5 = new byte[30];
                    bArr5[4] = 1;
                    bArr5[5] = 1;
                    bArr5[6] = 2;
                    bArr5[7] = 2;
                    bArr5[8] = 3;
                    bArr5[9] = 3;
                    bArr5[10] = 4;
                    bArr5[11] = 4;
                    bArr5[12] = 5;
                    bArr5[13] = 5;
                    bArr5[14] = 6;
                    bArr5[15] = 6;
                    bArr5[16] = 7;
                    bArr5[17] = 7;
                    bArr5[MAX_CODE_LENGTHS] = 8;
                    bArr5[19] = 8;
                    bArr5[20] = 9;
                    bArr5[21] = 9;
                    bArr5[22] = 10;
                    bArr5[23] = 10;
                    bArr5[24] = 11;
                    bArr5[25] = 11;
                    bArr5[26] = 12;
                    bArr5[27] = 12;
                    bArr5[28] = 13;
                    bArr5[29] = 13;
                    distance_extra_bits = bArr5;
                    distance_values = new short[]{1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577};
                    byte[] bArr6 = new byte[19];
                    bArr6[0] = 16;
                    bArr6[1] = 17;
                    bArr6[2] = 18;
                    bArr6[4] = 8;
                    bArr6[5] = 7;
                    bArr6[6] = 9;
                    bArr6[7] = 6;
                    bArr6[8] = 10;
                    bArr6[9] = 5;
                    bArr6[10] = 11;
                    bArr6[11] = 4;
                    bArr6[12] = 12;
                    bArr6[13] = 3;
                    bArr6[14] = 13;
                    bArr6[15] = 2;
                    bArr6[16] = 14;
                    bArr6[17] = 1;
                    bArr6[MAX_CODE_LENGTHS] = 15;
                    dynamic_length_order = bArr6;
                    do {
                        bfinal = readBits(1);
                        int btype = readBits(2);
                        if (btype == 0) {
                            inflateStored();
                            continue;
                        } else if (btype == 1) {
                            inflateFixed();
                            continue;
                        } else if (btype == 2) {
                            inflateDynamic();
                            continue;
                        } else {
                            throw new IOException("Invalid GZIP block");
                        }
                    } while (bfinal == 0);
                    bArr = uncompressed;
                    uncompressed_index = 0;
                    buffer_bit = 0;
                    buffer_byte = 0;
                    buffer_index = 0;
                    dynamic_length_order = null;
                    distance_extra_bits = null;
                    length_extra_bits = null;
                    uncompressed = null;
                    buffer = null;
                    distance_values = null;
                    length_values = null;
                } else {
                    throw new IOException("Invalid GZIP format");
                }
            } catch (Throwable th) {
                uncompressed_index = 0;
                buffer_bit = 0;
                buffer_byte = 0;
                buffer_index = 0;
                dynamic_length_order = null;
                distance_extra_bits = null;
                length_extra_bits = null;
                uncompressed = null;
                buffer = null;
                distance_values = null;
                length_values = null;
                throw th;
            }
        }
        return bArr;
    }

    private static void inflateStored() {
        buffer_bit = 0;
        int len = readBits(16);
        int readBits = readBits(16);
        System.arraycopy(buffer, buffer_index, uncompressed, uncompressed_index, len);
        buffer_index += len;
        uncompressed_index += len;
    }

    private static void inflateFixed() {
        byte[] literal_bits = new byte[Tool.DECORATE2];
        for (int i = 0; i < 144; i++) {
            literal_bits[i] = 8;
        }
        for (int i2 = 144; i2 < 256; i2++) {
            literal_bits[i2] = 9;
        }
        for (int i3 = 256; i3 < 280; i3++) {
            literal_bits[i3] = 7;
        }
        for (int i4 = 280; i4 < 288; i4++) {
            literal_bits[i4] = 8;
        }
        int[] literal_tree = createHuffmanTree(literal_bits, MAX_CODE_LITERALS);
        byte[] distance_bits = new byte[32];
        for (int i5 = 0; i5 < distance_bits.length; i5++) {
            distance_bits[i5] = 5;
        }
        inflateBlock(literal_tree, createHuffmanTree(distance_bits, MAX_CODE_DISTANCES));
    }

    private static void inflateDynamic() {
        int hlit = readBits(5) + 257;
        int hdist = readBits(5) + 1;
        int hclen = readBits(4) + 4;
        byte[] length_bits = new byte[19];
        for (int i = 0; i < hclen; i++) {
            length_bits[dynamic_length_order[i]] = (byte) readBits(3);
        }
        int[] length_tree = createHuffmanTree(length_bits, MAX_CODE_LENGTHS);
        inflateBlock(createHuffmanTree(decodeCodeLengths(length_tree, hlit), hlit - 1), createHuffmanTree(decodeCodeLengths(length_tree, hdist), hdist - 1));
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r2v0, types: [short, int] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r5v0, types: [short, int] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void inflateBlock(int[] r12, int[] r13) {
        /*
            r11 = 256(0x100, float:3.59E-43)
            r0 = 0
            r4 = 0
            r1 = 0
        L_0x0005:
            int r0 = readCode(r12)
            if (r0 != r11) goto L_0x000c
            return
        L_0x000c:
            if (r0 <= r11) goto L_0x004c
            int r0 = r0 + -257
            short[] r7 = length_values
            short r5 = r7[r0]
            byte[] r7 = length_extra_bits
            byte r4 = r7[r0]
            if (r4 <= 0) goto L_0x001f
            int r7 = readBits(r4)
            int r5 = r5 + r7
        L_0x001f:
            int r0 = readCode(r13)
            short[] r7 = distance_values
            short r2 = r7[r0]
            byte[] r7 = distance_extra_bits
            byte r1 = r7[r0]
            if (r1 <= 0) goto L_0x0032
            int r7 = readBits(r1)
            int r2 = r2 + r7
        L_0x0032:
            r3 = 0
            int r7 = uncompressed_index
            int r6 = r7 - r2
        L_0x0037:
            if (r3 >= r5) goto L_0x0005
            byte[] r7 = uncompressed
            int r8 = uncompressed_index
            int r9 = r8 + 1
            uncompressed_index = r9
            byte[] r9 = uncompressed
            int r10 = r6 + r3
            byte r9 = r9[r10]
            r7[r8] = r9
            int r3 = r3 + 1
            goto L_0x0037
        L_0x004c:
            byte[] r7 = uncompressed
            int r8 = uncompressed_index
            int r9 = r8 + 1
            uncompressed_index = r9
            byte r9 = (byte) r0
            r7[r8] = r9
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.net.GZIP.inflateBlock(int[], int[]):void");
    }

    private static int readBits(int n) {
        int data;
        if (buffer_bit == 0) {
            byte[] bArr = buffer;
            int i = buffer_index;
            buffer_index = i + 1;
            byte b = bArr[i] & 255;
            buffer_byte = b;
            data = b;
        } else {
            data = buffer_byte >> buffer_bit;
        }
        for (int i2 = 8 - buffer_bit; i2 < n; i2 += 8) {
            byte[] bArr2 = buffer;
            int i3 = buffer_index;
            buffer_index = i3 + 1;
            buffer_byte = bArr2[i3] & 255;
            data |= buffer_byte << i2;
        }
        buffer_bit = (buffer_bit + n) & 7;
        return ((1 << n) - 1) & data;
    }

    private static int readCode(int[] tree) {
        int node = tree[0];
        while (node >= 0) {
            if (buffer_bit == 0) {
                byte[] bArr = buffer;
                int i = buffer_index;
                buffer_index = i + 1;
                buffer_byte = bArr[i] & 255;
            }
            node = (buffer_byte & (1 << buffer_bit)) == 0 ? tree[node >> 16] : tree[node & TextField.CONSTRAINT_MASK];
            buffer_bit = (buffer_bit + 1) & 7;
        }
        return node & TextField.CONSTRAINT_MASK;
    }

    private static int[] createHuffmanTree(byte[] bits, int max_code) {
        int tree_insert;
        int node;
        int node2;
        int[] bl_count = new int[17];
        for (byte i : bits) {
            bl_count[i] = bl_count[i] + 1;
        }
        int code = 0;
        bl_count[0] = 0;
        int[] next_code = new int[17];
        for (int i2 = 1; i2 <= 16; i2++) {
            code = (code + bl_count[i2 - 1]) << 1;
            next_code[i2] = code;
        }
        int[] tree = new int[((max_code << 1) + 16)];
        int tree_insert2 = 1;
        int i3 = 0;
        int i4 = code;
        while (i3 <= max_code) {
            byte len = bits[i3];
            if (len != 0) {
                int code2 = next_code[len];
                next_code[len] = code2 + 1;
                int tree_insert3 = 0;
                int bit = len - 1;
                while (bit >= 0) {
                    if (((1 << bit) & code2) == 0) {
                        int left = tree[tree_insert3] >> 16;
                        if (left == 0) {
                            tree[tree_insert3] = tree[tree_insert3] | (tree_insert2 << 16);
                            node2 = tree_insert2 + 1;
                            node = tree_insert2;
                        } else {
                            node = left;
                            node2 = tree_insert2;
                        }
                    } else {
                        int right = tree[tree_insert3] & TextField.CONSTRAINT_MASK;
                        if (right == 0) {
                            tree[tree_insert3] = tree[tree_insert3] | tree_insert2;
                            node2 = tree_insert2 + 1;
                            node = tree_insert2;
                        } else {
                            node = right;
                            node2 = tree_insert2;
                        }
                    }
                    bit--;
                    tree_insert2 = node2;
                    tree_insert3 = node;
                }
                tree[tree_insert3] = Integer.MIN_VALUE | i3;
                tree_insert = tree_insert2;
                int i5 = code2;
            } else {
                tree_insert = tree_insert2;
            }
            i3++;
            tree_insert2 = tree_insert;
        }
        return tree;
    }

    private static byte[] decodeCodeLengths(int[] length_tree, int count) {
        int i;
        int repeat;
        int repeat2;
        byte[] bits = new byte[count];
        int last = 0;
        int i2 = 0;
        while (i2 < count) {
            int code = readCode(length_tree);
            if (code >= 16) {
                if (code == 16) {
                    code = last;
                    repeat = readBits(2) + 3;
                } else {
                    if (code == 17) {
                        repeat2 = readBits(3) + 3;
                    } else {
                        repeat2 = readBits(7) + 11;
                    }
                    code = 0;
                    repeat = repeat2;
                }
                while (true) {
                    int repeat3 = repeat - 1;
                    if (repeat <= 0) {
                        break;
                    }
                    bits[i2] = (byte) code;
                    repeat = repeat3;
                    i2++;
                }
                i = i2;
            } else if (code != 0) {
                i = i2 + 1;
                bits[i2] = (byte) code;
            } else {
                i = i2 + 1;
            }
            last = code;
            i2 = i;
        }
        return bits;
    }
}
