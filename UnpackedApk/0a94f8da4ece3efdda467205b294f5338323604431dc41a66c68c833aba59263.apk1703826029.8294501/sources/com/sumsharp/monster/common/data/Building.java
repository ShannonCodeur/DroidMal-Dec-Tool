package com.sumsharp.monster.common.data;

import java.util.Hashtable;

public class Building {
    private static Hashtable DICTIONARY = new Hashtable();
    public static byte[] fortressNeed;
    public static boolean isReady = false;
    public byte id;
    public int lv;
    public int lv_max;
    public int[] mineralNeed = null;
    public int[] moneyNeed = null;
    public String name;
    public int[] woodNeed = null;

    public static boolean needCheckBuilding(int id2) {
        for (byte b : fortressNeed) {
            if (b == id2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isReady() {
        return isReady;
    }

    public static void AddBuilding(Building b) {
        DICTIONARY.put(new Integer(b.id), b);
    }

    public void upgrade() {
        if (this.lv < this.lv_max) {
            this.lv++;
        }
    }

    public static int howManyWood(int bid, int lv2) {
        Building b = (Building) DICTIONARY.get(new Integer(bid));
        if (b != null) {
            return b.woodNeed[lv2 - 1];
        }
        return 0;
    }

    public static int howManyMineral(int bid, int lv2) {
        Building b = (Building) DICTIONARY.get(new Integer(bid));
        if (b != null) {
            return b.mineralNeed[lv2 - 1];
        }
        return 0;
    }

    public static int howManyMoney(int bid, int lv2) {
        Building b = (Building) DICTIONARY.get(new Integer(bid));
        if (b != null) {
            return b.moneyNeed[lv2 - 1];
        }
        return 0;
    }

    public Building(byte id2) {
        this.id = id2;
    }

    public static Building getBuilding(int bid) {
        return (Building) DICTIONARY.get(new Integer(bid));
    }

    /* JADX WARNING: type inference failed for: r9v6, types: [int] */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r9v9, types: [int] */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void initBuildingMap(com.sumsharp.monster.net.UWAPSegment r9) {
        /*
            byte[] r1 = r9.readBytes()
            int r5 = r1.length
            com.sumsharp.monster.common.data.Building[] r0 = new com.sumsharp.monster.common.data.Building[r5]
            java.lang.String[] r3 = r9.readStrings()
            byte[] r2 = r9.readBytes()
            byte[] r6 = r9.readBytes()
            r4 = 0
        L_0x0014:
            if (r4 < r5) goto L_0x0032
            fortressNeed = r6
            byte[] r9 = r9.readBytes()
            java.io.DataInputStream r1 = new java.io.DataInputStream
            java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream
            r0.<init>(r9)
            r1.<init>(r0)
            byte r4 = r1.readByte()     // Catch:{ IOException -> 0x0092 }
            r9 = 0
            r2 = r9
        L_0x002c:
            if (r2 < r4) goto L_0x004f
        L_0x002e:
            r9 = 1
            isReady = r9
            return
        L_0x0032:
            com.sumsharp.monster.common.data.Building r7 = new com.sumsharp.monster.common.data.Building
            byte r8 = r1[r4]
            r7.<init>(r8)
            r0[r4] = r7
            r7 = r0[r4]
            r8 = r3[r4]
            r7.name = r8
            r7 = r0[r4]
            byte r8 = r2[r4]
            r7.lv_max = r8
            r7 = r0[r4]
            AddBuilding(r7)
            int r4 = r4 + 1
            goto L_0x0014
        L_0x004f:
            byte r0 = r1.readByte()     // Catch:{ IOException -> 0x0092 }
            r9 = 0
            r3 = r9
        L_0x0055:
            if (r3 < r0) goto L_0x005b
            int r9 = r2 + 1
            r2 = r9
            goto L_0x002c
        L_0x005b:
            byte r9 = r1.readByte()     // Catch:{ IOException -> 0x0092 }
            com.sumsharp.monster.common.data.Building r9 = getBuilding(r9)     // Catch:{ IOException -> 0x0092 }
            int[] r5 = r9.woodNeed     // Catch:{ IOException -> 0x0092 }
            if (r5 != 0) goto L_0x0073
            int[] r5 = new int[r4]     // Catch:{ IOException -> 0x0092 }
            r9.woodNeed = r5     // Catch:{ IOException -> 0x0092 }
            int[] r5 = new int[r4]     // Catch:{ IOException -> 0x0092 }
            r9.mineralNeed = r5     // Catch:{ IOException -> 0x0092 }
            int[] r5 = new int[r4]     // Catch:{ IOException -> 0x0092 }
            r9.moneyNeed = r5     // Catch:{ IOException -> 0x0092 }
        L_0x0073:
            int[] r5 = r9.woodNeed     // Catch:{ IOException -> 0x0092 }
            int r6 = r1.readInt()     // Catch:{ IOException -> 0x0092 }
            r5[r2] = r6     // Catch:{ IOException -> 0x0092 }
            int[] r5 = r9.mineralNeed     // Catch:{ IOException -> 0x0092 }
            int r6 = r1.readInt()     // Catch:{ IOException -> 0x0092 }
            r5[r2] = r6     // Catch:{ IOException -> 0x0092 }
            int[] r5 = r9.moneyNeed     // Catch:{ IOException -> 0x0092 }
            int r6 = r1.readInt()     // Catch:{ IOException -> 0x0092 }
            r5[r2] = r6     // Catch:{ IOException -> 0x0092 }
            AddBuilding(r9)     // Catch:{ IOException -> 0x0092 }
            int r9 = r3 + 1
            r3 = r9
            goto L_0x0055
        L_0x0092:
            r9 = move-exception
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.common.data.Building.initBuildingMap(com.sumsharp.monster.net.UWAPSegment):void");
    }
}
