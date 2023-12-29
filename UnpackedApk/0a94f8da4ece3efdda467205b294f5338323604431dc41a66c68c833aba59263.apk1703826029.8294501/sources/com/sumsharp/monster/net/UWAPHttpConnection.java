package com.sumsharp.monster.net;

import com.sumsharp.monster.common.Utilities;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.HttpConnection;

public class UWAPHttpConnection implements Runnable, UWAPConnection {
    public static int TIME_SEND_TIMEOUT = 6000;
    private static Vector _segments = new Vector();
    public static Hashtable segmentsDoingQueue = new Hashtable();
    private static int serial = 1;
    public static final Integer severSegmentSerial = new Integer(-1);
    protected final byte[] VERSION = {83, 83, 83, 80};
    private byte checkSum = 0;
    private boolean closeSignalSent = false;
    private boolean closed = false;
    private boolean cut;
    int id = -1;
    private long lastSendTime = System.currentTimeMillis();
    public String url = null;

    public UWAPHttpConnection(String url2) throws IOException {
        url2 = !url2.endsWith("/") ? String.valueOf(url2) + "/" : url2;
        this.url = url2;
        segmentsDoingQueue.clear();
        HttpConnection conn = UWAPSegment.getConnection(url2, Network.useProxy());
        conn.setRequestMethod(HttpConnection.GET);
        if (conn.getResponseCode() != 200) {
            throw new IOException();
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public static void cycleSegmentsDoingQueue() {
        if (segmentsDoingQueue.size() > 0) {
            Vector tmpArray = new Vector();
            Enumeration emu = segmentsDoingQueue.keys();
            int currTime = Utilities.getTimeStamp();
            while (emu.hasMoreElements()) {
                Integer serialSend = (Integer) emu.nextElement();
                if (currTime - ((int[]) segmentsDoingQueue.get(serialSend))[2] > TIME_SEND_TIMEOUT) {
                    tmpArray.addElement(serialSend);
                }
            }
            for (int i = 0; i < tmpArray.size(); i++) {
                Integer serialSend2 = (Integer) tmpArray.elementAt(i);
                int[] segmentData = (int[]) segmentsDoingQueue.get(serialSend2);
                segmentsDoingQueue.remove(serialSend2);
                sendSegmentTimeOut((byte) segmentData[0], (byte) segmentData[1], serialSend2.intValue());
            }
        }
    }

    public static void sendSegmentTimeOut(byte type, byte subType, int serialNo) {
        try {
            UWAPSegment errorSegment = new UWAPSegment(type, subType);
            errorSegment.flag = 1;
            errorSegment.writeInt(0);
            errorSegment.writeString("服务器无回应，请稍后重试。");
            errorSegment.flush();
            errorSegment.serial = serialNo;
            Utilities.addSegment(errorSegment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean processSegmentsDoingQueue(UWAPSegment segment) {
        segmentsDoingQueue.remove(new Integer(segment.serial));
        return true;
    }

    public static boolean segmentHasResponse(UWAPSegment segment) {
        return segment.needResponse;
    }

    public static void addSegmentsDoingQueue(UWAPSegment segment) {
        if (segmentHasResponse(segment)) {
            Integer serialSend = new Integer(segment.serial);
            int[] segmentData = {segment.mainType & 255, segment.subType & 255, Utilities.getTimeStamp()};
            boolean flg = true;
            int t = (segment.mainType << 8) | segment.subType;
            int i = 0;
            while (true) {
                if (i >= Utilities.ignoreReturn.length) {
                    break;
                } else if (t == Utilities.ignoreReturn[i]) {
                    flg = false;
                    break;
                } else {
                    i++;
                }
            }
            if (flg) {
                segmentsDoingQueue.put(serialSend, segmentData);
            }
        }
    }

    public static int writeSegment(UWAPSegment segment, boolean createSerial) {
        if (createSerial) {
            synchronized (_segments) {
                serial++;
                if (serial < 0) {
                    serial = 0;
                }
                segment.serial = serial;
            }
        }
        addSegmentsDoingQueue(segment);
        _segments.addElement(segment);
        return serial;
    }

    public int writeSegment(UWAPSegment segment) {
        return writeSegment(segment, true);
    }

    private static void makeAllTimeout() {
        if (segmentsDoingQueue.size() > 0) {
            Enumeration emu = segmentsDoingQueue.keys();
            while (emu.hasMoreElements()) {
                Integer serialSend = (Integer) emu.nextElement();
                int[] segmentData = (int[]) segmentsDoingQueue.get(serialSend);
                segmentsDoingQueue.remove(serialSend);
                sendSegmentTimeOut((byte) segmentData[0], (byte) segmentData[1], serialSend.intValue());
            }
        }
    }

    public void close() {
        this.closed = true;
        makeAllTimeout();
        writeSegment(new UWAPSegment(64, 3));
    }

    /* access modifiers changed from: 0000 */
    public void check(short shortVal) {
        this.checkSum = (byte) (this.checkSum ^ ((byte) (shortVal >> 8)));
        this.checkSum = (byte) (this.checkSum ^ ((byte) shortVal));
    }

    /* access modifiers changed from: 0000 */
    public void check(int i) {
        this.checkSum = (byte) (this.checkSum ^ ((byte) (i >> 24)));
        this.checkSum = (byte) (this.checkSum ^ ((byte) (i >> 16)));
        this.checkSum = (byte) (this.checkSum ^ ((byte) (i >> 8)));
        this.checkSum = (byte) (this.checkSum ^ ((byte) i));
    }

    private void check(byte[] byteArray) {
        if (byteArray != null) {
            for (byte b : byteArray) {
                this.checkSum = (byte) (this.checkSum ^ b);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void writeSegment(UWAPSegment seg, DataOutputStream out) throws Exception {
        seg.flush();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        int length = 18 + seg.data.length;
        this.checkSum = 0;
        dos.write(this.VERSION);
        check(this.VERSION);
        dos.writeInt(this.id);
        check(this.id);
        dos.writeInt(seg.serial);
        check(serial);
        dos.writeInt(length);
        check(length);
        dos.writeShort(1);
        check(1);
        dos.write(seg.data);
        check(seg.data);
        dos.write(this.checkSum);
        out.write(bos.toByteArray());
    }

    private UWAPSegment[] readSegment(DataInputStream in) throws Exception {
        in.skipBytes(4);
        int i = in.readInt();
        if (this.id == -1) {
            this.id = i;
        }
        int serial2 = in.readInt();
        in.skip(4);
        short segmentNum = in.readShort();
        UWAPSegment[] r = new UWAPSegment[segmentNum];
        for (int i2 = 0; i2 < segmentNum; i2++) {
            r[i2] = new UWAPSegment(in);
            r[i2].serial = serial2;
        }
        in.skip(1);
        return r;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:115:0x0195, code lost:
        if (r11 != null) goto L_0x0197;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:?, code lost:
        r11.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:0x019a, code lost:
        if (r0 != null) goto L_0x019c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x019c, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x019f, code lost:
        if (r4 != null) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x01a1, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x01c1, code lost:
        r16 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x01c2, code lost:
        r9 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x01ca, code lost:
        r16 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x01cb, code lost:
        r6 = r16;
        r9 = r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x0174 A[SYNTHETIC, Splitter:B:102:0x0174] */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0179 A[Catch:{ Throwable -> 0x01bc }] */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x017e A[Catch:{ Throwable -> 0x01bc }] */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x01c1 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:76:0x012e] */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x01ca A[ExcHandler: Throwable (r16v26 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:76:0x012e] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0150 A[SYNTHETIC, Splitter:B:89:0x0150] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0155 A[Catch:{ Throwable -> 0x01c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x015a A[Catch:{ Throwable -> 0x01c4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r20 = this;
        L_0x0000:
            r4 = 0
            r9 = 0
            r11 = 0
            r5 = 0
            r16 = 500(0x1f4, double:2.47E-321)
            java.lang.Thread.sleep(r16)     // Catch:{ Throwable -> 0x00cc }
            java.util.Vector r16 = _segments     // Catch:{ Throwable -> 0x00cc }
            int r16 = r16.size()     // Catch:{ Throwable -> 0x00cc }
            if (r16 != 0) goto L_0x0045
            long r16 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00cc }
            r0 = r20
            long r0 = r0.lastSendTime     // Catch:{ Throwable -> 0x00cc }
            r18 = r0
            long r16 = r16 - r18
            r18 = 3000(0xbb8, double:1.482E-320)
            int r16 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r16 >= 0) goto L_0x0045
            if (r11 == 0) goto L_0x0028
            r11.close()     // Catch:{ Throwable -> 0x01d3 }
        L_0x0028:
            if (r9 == 0) goto L_0x002d
            r9.close()     // Catch:{ Throwable -> 0x01d3 }
        L_0x002d:
            if (r4 == 0) goto L_0x0032
            r4.close()     // Catch:{ Throwable -> 0x01d3 }
        L_0x0032:
            r0 = r20
            boolean r0 = r0.closed     // Catch:{ Throwable -> 0x01d3 }
            r16 = r0
            if (r16 == 0) goto L_0x0000
            if (r5 == 0) goto L_0x0044
            r0 = r20
            boolean r0 = r0.closeSignalSent     // Catch:{ Throwable -> 0x01d3 }
            r16 = r0
            if (r16 == 0) goto L_0x0000
        L_0x0044:
            return
        L_0x0045:
            long r16 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00cc }
            r0 = r16
            r2 = r20
            r2.lastSendTime = r0     // Catch:{ Throwable -> 0x00cc }
            r5 = 0
            r0 = r20
            java.lang.String r0 = r0.url     // Catch:{ Throwable -> 0x00cc }
            r16 = r0
            boolean r17 = com.sumsharp.monster.net.Network.useProxy()     // Catch:{ Throwable -> 0x00cc }
            javax.microedition.io.HttpConnection r4 = com.sumsharp.monster.net.UWAPSegment.getConnection(r16, r17)     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r16 = "POST"
            r0 = r4
            r1 = r16
            r0.setRequestMethod(r1)     // Catch:{ Throwable -> 0x00cc }
            r5 = 1
            java.io.DataOutputStream r12 = new java.io.DataOutputStream     // Catch:{ Throwable -> 0x00cc }
            java.io.OutputStream r16 = r4.openOutputStream()     // Catch:{ Throwable -> 0x00cc }
            r0 = r12
            r1 = r16
            r0.<init>(r1)     // Catch:{ Throwable -> 0x00cc }
            java.util.Vector r16 = _segments     // Catch:{ Throwable -> 0x010d, all -> 0x01be }
            monitor-enter(r16)     // Catch:{ Throwable -> 0x010d, all -> 0x01be }
            java.util.Vector r17 = _segments     // Catch:{ all -> 0x010a }
            int r17 = r17.size()     // Catch:{ all -> 0x010a }
            if (r17 != 0) goto L_0x0092
            com.sumsharp.monster.net.UWAPSegment r15 = new com.sumsharp.monster.net.UWAPSegment     // Catch:{ all -> 0x010a }
            r17 = 64
            r18 = 1
            r0 = r15
            r1 = r17
            r2 = r18
            r0.<init>(r1, r2)     // Catch:{ all -> 0x010a }
            r0 = r20
            r1 = r15
            r0.writeSegment(r1)     // Catch:{ all -> 0x010a }
        L_0x0092:
            java.util.Vector r17 = _segments     // Catch:{ all -> 0x010a }
            int r17 = r17.size()     // Catch:{ all -> 0x010a }
            r0 = r17
            com.sumsharp.monster.net.UWAPSegment[] r0 = new com.sumsharp.monster.net.UWAPSegment[r0]     // Catch:{ all -> 0x010a }
            r13 = r0
            java.util.Vector r17 = _segments     // Catch:{ all -> 0x010a }
            r0 = r17
            r1 = r13
            r0.copyInto(r1)     // Catch:{ all -> 0x010a }
            java.util.Vector r17 = _segments     // Catch:{ all -> 0x010a }
            r18 = 0
            r17.setSize(r18)     // Catch:{ all -> 0x010a }
            monitor-exit(r16)     // Catch:{ all -> 0x010a }
            r8 = 0
        L_0x00ae:
            r0 = r13
            int r0 = r0.length     // Catch:{ Throwable -> 0x010d, all -> 0x01be }
            r16 = r0
            r0 = r8
            r1 = r16
            if (r0 < r1) goto L_0x0112
            r12.close()     // Catch:{ Throwable -> 0x010d, all -> 0x01be }
            r11 = 0
            int r3 = r4.getResponseCode()     // Catch:{ Throwable -> 0x00cc }
            r16 = 200(0xc8, float:2.8E-43)
            r0 = r3
            r1 = r16
            if (r0 == r1) goto L_0x011f
            java.io.IOException r16 = new java.io.IOException     // Catch:{ Throwable -> 0x00cc }
            r16.<init>()     // Catch:{ Throwable -> 0x00cc }
            throw r16     // Catch:{ Throwable -> 0x00cc }
        L_0x00cc:
            r16 = move-exception
            r6 = r16
        L_0x00cf:
            r6.printStackTrace()     // Catch:{ all -> 0x0171 }
            r0 = r6
            boolean r0 = r0 instanceof java.io.IOException     // Catch:{ all -> 0x0171 }
            r16 = r0
            if (r16 == 0) goto L_0x014e
            r0 = r20
            boolean r0 = r0.closed     // Catch:{ Exception -> 0x0148 }
            r16 = r0
            if (r16 != 0) goto L_0x014e
            com.sumsharp.monster.common.Utilities.closeConnection()     // Catch:{ Exception -> 0x0148 }
            r20.tryReconnect()     // Catch:{ Exception -> 0x0148 }
            if (r11 == 0) goto L_0x00ec
            r11.close()     // Catch:{ Throwable -> 0x01c7 }
        L_0x00ec:
            if (r9 == 0) goto L_0x00f1
            r9.close()     // Catch:{ Throwable -> 0x01c7 }
        L_0x00f1:
            if (r4 == 0) goto L_0x00f6
            r4.close()     // Catch:{ Throwable -> 0x01c7 }
        L_0x00f6:
            r0 = r20
            boolean r0 = r0.closed     // Catch:{ Throwable -> 0x01c7 }
            r16 = r0
            if (r16 == 0) goto L_0x0044
            if (r5 == 0) goto L_0x0044
            r0 = r20
            boolean r0 = r0.closeSignalSent     // Catch:{ Throwable -> 0x01c7 }
            r16 = r0
            if (r16 == 0) goto L_0x0044
            goto L_0x0044
        L_0x010a:
            r17 = move-exception
            monitor-exit(r16)     // Catch:{ all -> 0x010a }
            throw r17     // Catch:{ Throwable -> 0x010d, all -> 0x01be }
        L_0x010d:
            r16 = move-exception
            r6 = r16
            r11 = r12
            goto L_0x00cf
        L_0x0112:
            r16 = r13[r8]     // Catch:{ Throwable -> 0x010d, all -> 0x01be }
            r0 = r20
            r1 = r16
            r2 = r12
            r0.writeSegment(r1, r2)     // Catch:{ Throwable -> 0x010d, all -> 0x01be }
            int r8 = r8 + 1
            goto L_0x00ae
        L_0x011f:
            java.io.DataInputStream r10 = new java.io.DataInputStream     // Catch:{ Throwable -> 0x00cc }
            java.io.InputStream r16 = r4.openInputStream()     // Catch:{ Throwable -> 0x00cc }
            r0 = r10
            r1 = r16
            r0.<init>(r1)     // Catch:{ Throwable -> 0x00cc }
        L_0x012b:
            r0 = r20
            r1 = r10
            com.sumsharp.monster.net.UWAPSegment[] r14 = r0.readSegment(r1)     // Catch:{ Exception -> 0x0194, Throwable -> 0x01ca, all -> 0x01c1 }
            r8 = 0
        L_0x0133:
            r0 = r14
            int r0 = r0.length     // Catch:{ Exception -> 0x0194, Throwable -> 0x01ca, all -> 0x01c1 }
            r16 = r0
            r0 = r8
            r1 = r16
            if (r0 >= r1) goto L_0x012b
            r16 = r14[r8]     // Catch:{ Exception -> 0x01d0, Throwable -> 0x01ca, all -> 0x01c1 }
            r0 = r20
            r1 = r16
            r0.processSegment(r1)     // Catch:{ Exception -> 0x01d0, Throwable -> 0x01ca, all -> 0x01c1 }
        L_0x0145:
            int r8 = r8 + 1
            goto L_0x0133
        L_0x0148:
            r16 = move-exception
            r7 = r16
            r7.printStackTrace()     // Catch:{ all -> 0x0171 }
        L_0x014e:
            if (r11 == 0) goto L_0x0153
            r11.close()     // Catch:{ Throwable -> 0x01c4 }
        L_0x0153:
            if (r9 == 0) goto L_0x0158
            r9.close()     // Catch:{ Throwable -> 0x01c4 }
        L_0x0158:
            if (r4 == 0) goto L_0x015d
            r4.close()     // Catch:{ Throwable -> 0x01c4 }
        L_0x015d:
            r0 = r20
            boolean r0 = r0.closed     // Catch:{ Throwable -> 0x01c4 }
            r16 = r0
            if (r16 == 0) goto L_0x0000
            if (r5 == 0) goto L_0x0044
            r0 = r20
            boolean r0 = r0.closeSignalSent     // Catch:{ Throwable -> 0x01c4 }
            r16 = r0
            if (r16 == 0) goto L_0x0000
            goto L_0x0044
        L_0x0171:
            r16 = move-exception
        L_0x0172:
            if (r11 == 0) goto L_0x0177
            r11.close()     // Catch:{ Throwable -> 0x01bc }
        L_0x0177:
            if (r9 == 0) goto L_0x017c
            r9.close()     // Catch:{ Throwable -> 0x01bc }
        L_0x017c:
            if (r4 == 0) goto L_0x0181
            r4.close()     // Catch:{ Throwable -> 0x01bc }
        L_0x0181:
            r0 = r20
            boolean r0 = r0.closed     // Catch:{ Throwable -> 0x01bc }
            r17 = r0
            if (r17 == 0) goto L_0x0193
            if (r5 == 0) goto L_0x0044
            r0 = r20
            boolean r0 = r0.closeSignalSent     // Catch:{ Throwable -> 0x01bc }
            r17 = r0
            if (r17 != 0) goto L_0x0044
        L_0x0193:
            throw r16
        L_0x0194:
            r16 = move-exception
            if (r11 == 0) goto L_0x019a
            r11.close()     // Catch:{ Throwable -> 0x01b9 }
        L_0x019a:
            if (r10 == 0) goto L_0x019f
            r10.close()     // Catch:{ Throwable -> 0x01b9 }
        L_0x019f:
            if (r4 == 0) goto L_0x01a4
            r4.close()     // Catch:{ Throwable -> 0x01b9 }
        L_0x01a4:
            r0 = r20
            boolean r0 = r0.closed     // Catch:{ Throwable -> 0x01b9 }
            r16 = r0
            if (r16 == 0) goto L_0x0000
            if (r5 == 0) goto L_0x01b6
            r0 = r20
            boolean r0 = r0.closeSignalSent     // Catch:{ Throwable -> 0x01b9 }
            r16 = r0
            if (r16 == 0) goto L_0x0000
        L_0x01b6:
            r9 = r10
            goto L_0x0044
        L_0x01b9:
            r16 = move-exception
            goto L_0x0000
        L_0x01bc:
            r17 = move-exception
            goto L_0x0193
        L_0x01be:
            r16 = move-exception
            r11 = r12
            goto L_0x0172
        L_0x01c1:
            r16 = move-exception
            r9 = r10
            goto L_0x0172
        L_0x01c4:
            r16 = move-exception
            goto L_0x0000
        L_0x01c7:
            r16 = move-exception
            goto L_0x0044
        L_0x01ca:
            r16 = move-exception
            r6 = r16
            r9 = r10
            goto L_0x00cf
        L_0x01d0:
            r16 = move-exception
            goto L_0x0145
        L_0x01d3:
            r16 = move-exception
            goto L_0x0000
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.net.UWAPHttpConnection.run():void");
    }

    public void processSegment(UWAPSegment segment) throws IOException {
        processSegmentsDoingQueue(segment);
        Utilities.addSegment(segment);
    }

    public void tryReconnect() {
        Utilities.tryReconnect();
    }

    public void cut(boolean cut2) {
        this.cut = cut2;
    }
}
