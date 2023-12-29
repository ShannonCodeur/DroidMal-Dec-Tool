package com.sumsharp.monster.gtvm;

import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.ui.VMUI;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.lcdui.TextField;

public class DebugVM extends GTVM implements Runnable {
    public static final int COMMAND_ADDBREAKPOINT = 4;
    public static final int COMMAND_ALLOCTRACE = 9;
    public static final int COMMAND_DELBREAKPOINT = 5;
    public static final int COMMAND_HEAP = 8;
    public static final int COMMAND_INFO = 1;
    public static final int COMMAND_INTERRUPT = 0;
    public static final int COMMAND_MODECHANGE = 3;
    public static final int COMMAND_QUERY = 2;
    public static final int COMMAND_REQUESTTRACE = 6;
    public static final int COMMAND_STATE = 11;
    public static final int COMMAND_SYNCSTATE = 10;
    public static final int COMMAND_TRACE = 7;
    public static final int MODE_RUN = 0;
    public static final int MODE_STEP = 1;
    public static final int MODE_STEPOUT = 3;
    public static final int MODE_STEPOVER = 2;
    public static final int TOKEN = 305419896;
    private Hashtable allocTrace = new Hashtable();
    private Vector breakPoints = new Vector();
    SocketConnection connection;
    private int debugMode = 1;
    DataInputStream dis;
    DataOutputStream dos;
    private boolean running;
    private int stepStartFunc;
    private int stepStartStackBase;

    private static class BreakPoint {
        int end;
        int funcID;
        int start;

        private BreakPoint() {
        }

        /* synthetic */ BreakPoint(BreakPoint breakPoint) {
            this();
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof BreakPoint)) {
                return false;
            }
            BreakPoint p = (BreakPoint) o;
            return this.funcID == p.funcID && this.start == p.start && this.end == p.end;
        }
    }

    public DebugVM(VMUI ui) {
        super(ui);
    }

    public void init(byte[] etfContent, byte[] etdContent) throws Exception {
        init(etfContent);
        this.allocTrace = new Hashtable();
        try {
            this.connection = (SocketConnection) Connector.open("socket://127.0.0.1:32167", 3, true);
            this.dos = this.connection.openDataOutputStream();
            this.dis = this.connection.openDataInputStream();
            this.dos.writeInt(etfContent.length);
            this.dos.write(etfContent);
            this.dos.writeInt(etdContent.length);
            this.dos.write(etdContent);
            this.dis.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    /* access modifiers changed from: protected */
    public int heapAlloc() {
        int ret = super.heapAlloc();
        this.allocTrace.put(new Integer(ret), getCurrentTrace());
        return ret;
    }

    /* JADX WARNING: type inference failed for: r2v3, types: [short[]] */
    /* JADX WARNING: type inference failed for: r0v2, types: [short] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r0v2, types: [short] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void heapFree(int r6) {
        /*
            r5 = this;
            java.util.Hashtable r2 = r5.allocTrace
            java.lang.Integer r3 = new java.lang.Integer
            r3.<init>(r6)
            r2.remove(r3)
            r2 = r6 & 4095(0xfff, float:5.738E-42)
            r3 = 32
            if (r2 >= r3) goto L_0x0011
        L_0x0010:
            return
        L_0x0011:
            int r0 = r5.freeHead
        L_0x0013:
            if (r0 != r6) goto L_0x001d
            java.lang.RuntimeException r2 = new java.lang.RuntimeException
            java.lang.String r3 = "错误：试图重复free一个内存单元。"
            r2.<init>(r3)
            throw r2
        L_0x001d:
            short[] r2 = r5.freeSpaceList
            short r0 = r2[r0]
            int r2 = r5.freeHead
            if (r0 != r2) goto L_0x0013
            java.lang.Object[] r2 = r5.dynamicHeap
            r3 = 0
            r2[r6] = r3
            short[] r2 = r5.freeSpaceList
            int r3 = r5.freeHead
            short r1 = r2[r3]
            short[] r2 = r5.freeSpaceList
            int r3 = r5.freeHead
            short r4 = (short) r6
            r2[r3] = r4
            short[] r2 = r5.freeSpaceList
            r2[r6] = r1
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.gtvm.DebugVM.heapFree(int):void");
    }

    /* JADX WARNING: type inference failed for: r16v37, types: [short[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r19 = this;
        L_0x0000:
            r0 = r19
            javax.microedition.io.SocketConnection r0 = r0.connection
            r16 = r0
            if (r16 != 0) goto L_0x0009
            return
        L_0x0009:
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r13 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = 305419896(0x12345678, float:5.6904566E-28)
            r0 = r13
            r1 = r16
            if (r0 != r1) goto L_0x0000
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r4 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            if (r4 != 0) goto L_0x0041
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r12 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            monitor-enter(r19)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r19.notifyAll()     // Catch:{ all -> 0x0037 }
            monitor-exit(r19)     // Catch:{ all -> 0x0037 }
            goto L_0x0000
        L_0x0037:
            r16 = move-exception
            monitor-exit(r19)     // Catch:{ all -> 0x0037 }
            throw r16     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
        L_0x003a:
            r16 = move-exception
            r5 = r16
            r19.closeConnection()
            goto L_0x0000
        L_0x0041:
            r16 = 2
            r0 = r4
            r1 = r16
            if (r0 != r1) goto L_0x00a3
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r3 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            r1 = r3
            int r15 = r0.memLoad(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            java.lang.String r9 = java.lang.String.valueOf(r15)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = 1073741824(0x40000000, float:2.0)
            r16 = r16 & r3
            if (r16 == 0) goto L_0x0071
            r0 = r19
            r1 = r15
            java.lang.Object r10 = r0.followPointer(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            r1 = r10
            java.lang.String r9 = r0.printObject(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
        L_0x0071:
            monitor-enter(r19)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ all -> 0x0098 }
            r16 = r0
            r17 = 305419896(0x12345678, float:5.6904566E-28)
            r16.writeInt(r17)     // Catch:{ all -> 0x0098 }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ all -> 0x0098 }
            r16 = r0
            r17 = 1
            r16.writeInt(r17)     // Catch:{ all -> 0x0098 }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ all -> 0x0098 }
            r16 = r0
            r0 = r16
            r1 = r9
            r0.writeUTF(r1)     // Catch:{ all -> 0x0098 }
            monitor-exit(r19)     // Catch:{ all -> 0x0098 }
            goto L_0x0000
        L_0x0098:
            r16 = move-exception
            monitor-exit(r19)     // Catch:{ all -> 0x0098 }
            throw r16     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
        L_0x009b:
            r16 = move-exception
            r5 = r16
            r5.printStackTrace()
            goto L_0x0000
        L_0x00a3:
            r16 = 3
            r0 = r4
            r1 = r16
            if (r0 != r1) goto L_0x00f0
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r16 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r16
            r1 = r19
            r1.debugMode = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            int r0 = r0.debugMode     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = 2
            r0 = r16
            r1 = r17
            if (r0 == r1) goto L_0x00d6
            r0 = r19
            int r0 = r0.debugMode     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = 3
            r0 = r16
            r1 = r17
            if (r0 != r1) goto L_0x0000
        L_0x00d6:
            r0 = r19
            int r0 = r0.currentFunc     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r16
            r1 = r19
            r1.stepStartFunc = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            int r0 = r0.stackBase     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r16
            r1 = r19
            r1.stepStartStackBase = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            goto L_0x0000
        L_0x00f0:
            r16 = 4
            r0 = r4
            r1 = r16
            if (r0 != r1) goto L_0x0177
            com.sumsharp.monster.gtvm.DebugVM$BreakPoint r11 = new com.sumsharp.monster.gtvm.DebugVM$BreakPoint     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = 0
            r0 = r11
            r1 = r16
            r0.<init>(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r16 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r16
            r1 = r11
            r1.funcID = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r16 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            int[] r0 = r0.functions     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r0 = r11
            int r0 = r0.funcID     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r18 = r0
            int r18 = r18 * 3
            int r18 = r18 + 1
            r17 = r17[r18]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            int r16 = r16 + r17
            r0 = r16
            r1 = r11
            r1.start = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r16 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            int[] r0 = r0.functions     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r0 = r11
            int r0 = r0.funcID     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r18 = r0
            int r18 = r18 * 3
            int r18 = r18 + 1
            r17 = r17[r18]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            int r16 = r16 + r17
            r0 = r16
            r1 = r11
            r1.end = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.util.Vector r0 = r0.breakPoints     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r16
            r1 = r11
            int r16 = r0.indexOf(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = -1
            r0 = r16
            r1 = r17
            if (r0 != r1) goto L_0x0000
            r0 = r19
            java.util.Vector r0 = r0.breakPoints     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r16
            r1 = r11
            r0.addElement(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            goto L_0x0000
        L_0x0177:
            r16 = 5
            r0 = r4
            r1 = r16
            if (r0 != r1) goto L_0x01e9
            com.sumsharp.monster.gtvm.DebugVM$BreakPoint r11 = new com.sumsharp.monster.gtvm.DebugVM$BreakPoint     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = 0
            r0 = r11
            r1 = r16
            r0.<init>(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r16 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r16
            r1 = r11
            r1.funcID = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r16 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            int[] r0 = r0.functions     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r0 = r11
            int r0 = r0.funcID     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r18 = r0
            int r18 = r18 * 3
            int r18 = r18 + 1
            r17 = r17[r18]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            int r16 = r16 + r17
            r0 = r16
            r1 = r11
            r1.start = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r16 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            int[] r0 = r0.functions     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r0 = r11
            int r0 = r0.funcID     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r18 = r0
            int r18 = r18 * 3
            int r18 = r18 + 1
            r17 = r17[r18]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            int r16 = r16 + r17
            r0 = r16
            r1 = r11
            r1.end = r0     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.util.Vector r0 = r0.breakPoints     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r16
            r1 = r11
            r0.removeElement(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            goto L_0x0000
        L_0x01e9:
            r16 = 6
            r0 = r4
            r1 = r16
            if (r0 != r1) goto L_0x0258
            r0 = r19
            int r0 = r0.currentFunc     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = -1
            r0 = r16
            r1 = r17
            if (r0 == r1) goto L_0x0000
            int[][] r14 = r19.getCurrentTrace()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            monitor-enter(r19)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ all -> 0x0234 }
            r16 = r0
            r17 = 305419896(0x12345678, float:5.6904566E-28)
            r16.writeInt(r17)     // Catch:{ all -> 0x0234 }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ all -> 0x0234 }
            r16 = r0
            r17 = 7
            r16.writeInt(r17)     // Catch:{ all -> 0x0234 }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ all -> 0x0234 }
            r16 = r0
            r0 = r14
            int r0 = r0.length     // Catch:{ all -> 0x0234 }
            r17 = r0
            r16.writeInt(r17)     // Catch:{ all -> 0x0234 }
            r8 = 0
        L_0x0228:
            r0 = r14
            int r0 = r0.length     // Catch:{ all -> 0x0234 }
            r16 = r0
            r0 = r8
            r1 = r16
            if (r0 < r1) goto L_0x0237
            monitor-exit(r19)     // Catch:{ all -> 0x0234 }
            goto L_0x0000
        L_0x0234:
            r16 = move-exception
            monitor-exit(r19)     // Catch:{ all -> 0x0234 }
            throw r16     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
        L_0x0237:
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ all -> 0x0234 }
            r16 = r0
            r17 = r14[r8]     // Catch:{ all -> 0x0234 }
            r18 = 0
            r17 = r17[r18]     // Catch:{ all -> 0x0234 }
            r16.writeInt(r17)     // Catch:{ all -> 0x0234 }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ all -> 0x0234 }
            r16 = r0
            r17 = r14[r8]     // Catch:{ all -> 0x0234 }
            r18 = 1
            r17 = r17[r18]     // Catch:{ all -> 0x0234 }
            r16.writeInt(r17)     // Catch:{ all -> 0x0234 }
            int r8 = r8 + 1
            goto L_0x0228
        L_0x0258:
            r16 = 8
            r0 = r4
            r1 = r16
            if (r0 != r1) goto L_0x030b
            r0 = r19
            java.lang.Object[] r0 = r0.dynamicHeap     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r16
            int r0 = r0.length     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r16
            boolean[] r0 = new boolean[r0]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r6 = r0
            r8 = 0
        L_0x0270:
            r0 = r6
            int r0 = r0.length     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r8
            r1 = r16
            if (r0 < r1) goto L_0x02f5
            r0 = r19
            int r0 = r0.freeHead     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r7 = r0
        L_0x027e:
            r0 = r19
            short[] r0 = r0.freeSpaceList     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            short r16 = r16[r7]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            int r0 = r0.freeHead     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r0 = r16
            r1 = r17
            if (r0 != r1) goto L_0x02fd
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = 305419896(0x12345678, float:5.6904566E-28)
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = 8
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r19
            java.lang.Object[] r0 = r0.dynamicHeap     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r0 = r17
            int r0 = r0.length     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r8 = 0
        L_0x02be:
            r0 = r19
            java.lang.Object[] r0 = r0.dynamicHeap     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r16
            int r0 = r0.length     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r8
            r1 = r16
            if (r0 >= r1) goto L_0x0000
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            boolean r17 = r6[r8]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16.writeBoolean(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r19
            java.lang.Object[] r0 = r0.dynamicHeap     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r17 = r17[r8]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            r1 = r17
            java.lang.String r17 = r0.printObject(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16.writeUTF(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            int r8 = r8 + 1
            goto L_0x02be
        L_0x02f5:
            r16 = 1
            r6[r8] = r16     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            int r8 = r8 + 1
            goto L_0x0270
        L_0x02fd:
            r0 = r19
            short[] r0 = r0.freeSpaceList     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            short r7 = r16[r7]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = 0
            r6[r7] = r16     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            goto L_0x027e
        L_0x030b:
            r16 = 9
            r0 = r4
            r1 = r16
            if (r0 != r1) goto L_0x0381
            r0 = r19
            java.io.DataInputStream r0 = r0.dis     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            int r2 = r16.readInt()     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.util.Hashtable r0 = r0.allocTrace     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            java.lang.Integer r17 = new java.lang.Integer     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r17
            r1 = r2
            r0.<init>(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            java.lang.Object r14 = r16.get(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            int[][] r14 = (int[][]) r14     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            if (r14 == 0) goto L_0x0000
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = 305419896(0x12345678, float:5.6904566E-28)
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = 9
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r14
            int r0 = r0.length     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r17 = r0
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r8 = 0
        L_0x0357:
            r0 = r14
            int r0 = r0.length     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r8
            r1 = r16
            if (r0 >= r1) goto L_0x0000
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = r14[r8]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r18 = 0
            r17 = r17[r18]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = r14[r8]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r18 = 1
            r17 = r17[r18]     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            int r8 = r8 + 1
            goto L_0x0357
        L_0x0381:
            r16 = 10
            r0 = r4
            r1 = r16
            if (r0 != r1) goto L_0x0000
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = 305419896(0x12345678, float:5.6904566E-28)
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r17 = 11
            r16.writeInt(r17)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r0 = r19
            java.io.DataOutputStream r0 = r0.dos     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            r16 = r0
            r0 = r19
            r1 = r16
            r0.writeState(r1)     // Catch:{ IOException -> 0x003a, Exception -> 0x009b }
            goto L_0x0000
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.gtvm.DebugVM.run():void");
    }

    private void writeState(DataOutputStream dos2) throws IOException {
        dos2.writeInt(this.staticHeap.length);
        for (int writeInt : this.staticHeap) {
            dos2.writeInt(writeInt);
        }
        dos2.writeInt(this.stack.length);
        for (int writeInt2 : this.stack) {
            dos2.writeInt(writeInt2);
        }
        dos2.writeInt(this.esp);
        dos2.writeInt(this.stackBase);
        boolean[] flag = new boolean[this.dynamicHeap.length];
        for (int i = 0; i < flag.length; i++) {
            flag[i] = true;
        }
        int head = this.freeHead;
        while (this.freeSpaceList[head] != this.freeHead) {
            short head2 = this.freeSpaceList[head];
            flag[head2] = false;
            head = head2;
        }
        dos2.writeInt(this.dynamicHeap.length);
        for (int i2 = 0; i2 < this.dynamicHeap.length; i2++) {
            dos2.writeBoolean(flag[i2]);
            dumpObject(dos2, this.dynamicHeap[i2]);
        }
    }

    private void dumpObject(DataOutputStream dos2, Object obj) throws IOException {
        if (obj == null) {
            dos2.writeByte(AbstractUnit.CLR_NAME_TAR);
        } else if (obj instanceof boolean[]) {
            boolean[] arr = (boolean[]) obj;
            dos2.writeByte(1);
            dos2.writeInt(arr.length);
            for (boolean writeBoolean : arr) {
                dos2.writeBoolean(writeBoolean);
            }
        } else if (obj instanceof byte[]) {
            byte[] arr2 = (byte[]) obj;
            dos2.writeByte(2);
            dos2.writeInt(arr2.length);
            dos2.write(arr2);
        } else if (obj instanceof short[]) {
            short[] arr3 = (short[]) obj;
            dos2.writeByte(3);
            dos2.writeInt(arr3.length);
            for (short writeShort : arr3) {
                dos2.writeShort(writeShort);
            }
        } else if (obj instanceof int[]) {
            int[] arr4 = (int[]) obj;
            dos2.writeByte(4);
            dos2.writeInt(arr4.length);
            for (int writeInt : arr4) {
                dos2.writeInt(writeInt);
            }
        } else if (obj instanceof String) {
            dos2.writeByte(5);
            dos2.writeUTF((String) obj);
        } else if (obj instanceof String[]) {
            String[] arr5 = (String[]) obj;
            dos2.writeByte(6);
            dos2.writeInt(arr5.length);
            for (String dumpObject : arr5) {
                dumpObject(dos2, dumpObject);
            }
        } else if (obj instanceof Hashtable) {
            dos2.writeByte(7);
            dos2.writeInt(((Hashtable) obj).size());
            Enumeration ee = ((Hashtable) obj).keys();
            while (ee.hasMoreElements()) {
                Object key = ee.nextElement();
                Object value = ((Hashtable) obj).get(key);
                dumpObject(dos2, key);
                dumpObject(dos2, value);
            }
        } else if (obj instanceof Vector) {
            Vector v = (Vector) obj;
            dos2.writeByte(8);
            dos2.writeInt(v.size());
            for (int i = 0; i < v.size(); i++) {
                dumpObject(dos2, v.elementAt(i));
            }
        } else if (obj instanceof Object[]) {
            Object[] arr6 = (Object[]) obj;
            dos2.writeByte(9);
            dos2.writeInt(arr6.length);
            for (Object dumpObject2 : arr6) {
                dumpObject(dos2, dumpObject2);
            }
        } else {
            dos2.writeByte(10);
            dos2.writeUTF(String.valueOf(obj));
        }
    }

    /* Debug info: failed to restart local var, previous not found, register: 15 */
    private int[][] getCurrentTrace() {
        Vector ret = new Vector();
        int thisFunc = this.currentFunc;
        int thisEip = this.eip - this.functions[(this.currentFunc * 3) + 1];
        int thisStackBase = this.stackBase;
        ret.addElement(new int[]{thisFunc, thisEip});
        while (thisFunc > 5) {
            int parCount = this.functions[thisFunc * 3] >> 16;
            int pos = thisStackBase + parCount + (this.functions[thisFunc * 3] & TextField.CONSTRAINT_MASK);
            thisStackBase = this.stack[pos];
            thisFunc = this.stack[pos + 1];
            ret.addElement(new int[]{thisFunc, (this.stack[pos + 2] - 4) - this.functions[(thisFunc * 3) + 1]});
        }
        int[][] ret1 = new int[ret.size()][];
        for (int i = 0; i < ret1.length; i++) {
            ret1[i] = (int[]) ret.elementAt(i);
        }
        return ret1;
    }

    private String printObject(Object o) {
        if (o == null) {
            return "null";
        }
        if (o instanceof boolean[]) {
            return printBooleans((boolean[]) o);
        }
        if (o instanceof byte[]) {
            return printBytes((byte[]) o);
        }
        if (o instanceof short[]) {
            return printShorts((short[]) o);
        }
        if (o instanceof int[]) {
            return printInts((int[]) o);
        }
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof Integer) {
            return o.toString();
        }
        if (o instanceof Vector) {
            Vector v = (Vector) o;
            Object[] arr = new Object[v.size()];
            v.copyInto(arr);
            return printObjects(arr);
        } else if (o instanceof Object[]) {
            return printObjects((Object[]) o);
        } else {
            if (o instanceof Hashtable) {
                return printHashtable((Hashtable) o);
            }
            return String.valueOf(o.getClass().getName()) + ": " + o.toString();
        }
    }

    private String printBooleans(boolean[] arr) {
        StringBuffer buf = new StringBuffer();
        buf.append("boolean[] {");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(arr[i]);
        }
        buf.append(" }");
        return buf.toString();
    }

    private String printBytes(byte[] arr) {
        StringBuffer buf = new StringBuffer();
        buf.append("byte[] {");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append("0x");
            buf.append(Integer.toHexString(arr[i] & 255));
        }
        buf.append(" }");
        return buf.toString();
    }

    private String printShorts(short[] arr) {
        StringBuffer buf = new StringBuffer();
        buf.append("short[] {");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(arr[i]);
        }
        buf.append(" }");
        return buf.toString();
    }

    private String printInts(int[] arr) {
        StringBuffer buf = new StringBuffer();
        buf.append("int[] {");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(arr[i]);
        }
        buf.append(" }");
        return buf.toString();
    }

    private String printObjects(Object[] arr) {
        StringBuffer buf = new StringBuffer();
        buf.append("Object[] {");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                buf.append("\n");
            }
            buf.append(printObject(arr[i]));
        }
        buf.append("\n}");
        return buf.toString();
    }

    private String printHashtable(Hashtable t) {
        Enumeration enum1 = t.keys();
        StringBuffer buf = new StringBuffer();
        buf.append("Hashtable[] {");
        int i = 0;
        while (enum1.hasMoreElements()) {
            Object key = enum1.nextElement();
            Object value = t.get(key);
            if (i > 0) {
                buf.append("\n");
            }
            buf.append(printObject(key));
            buf.append(" = ");
            buf.append(printObject(value));
            i++;
        }
        buf.append("\n}");
        return buf.toString();
    }

    private void closeConnection() {
        try {
            this.dis.close();
        } catch (Exception e) {
        }
        this.dis = null;
        try {
            this.dos.close();
        } catch (Exception e2) {
        }
        this.dos = null;
        try {
            this.connection.close();
        } catch (Exception e3) {
        }
        this.connection = null;
    }

    public void destroy() {
        super.destroy();
        this.breakPoints.removeAllElements();
        this.debugMode = 1;
        closeConnection();
    }

    /* access modifiers changed from: protected */
    public void generateInterrupt(int code) throws Exception {
        if (this.dos != null) {
            synchronized (this) {
                this.dos.writeInt(TOKEN);
                this.dos.writeInt(0);
                this.dos.writeInt(code);
                this.dos.writeInt(this.eip - this.functions[(this.currentFunc * 3) + 1]);
                this.dos.writeInt(this.currentFunc);
                wait();
            }
        }
    }

    private boolean isBreakPoint(int funcID, int ip) {
        for (int i = 0; i < this.breakPoints.size(); i++) {
            BreakPoint p = (BreakPoint) this.breakPoints.elementAt(i);
            if (p.funcID == funcID && ip >= p.start && ip < p.end) {
                return true;
            }
        }
        return false;
    }

    private int getHeapFree() {
        int heapFreeCount = 0;
        int temp = this.freeHead;
        while (this.freeSpaceList[temp] != this.freeHead) {
            heapFreeCount++;
            temp = this.freeSpaceList[temp];
        }
        return heapFreeCount;
    }

    /* access modifiers changed from: protected */
    public void resume() {
        this.blocked = false;
        if (this.blockPosition != null) {
            int[] bp = this.blockPosition;
            this.blockPosition = null;
            this.stackBase = bp[0];
            this.eip = bp[1];
            this.currentFunc = bp[2];
            this.funcBase = this.currentFunc * 3;
            this.esp = bp.length - 4;
            if (this.esp >= 0) {
                System.arraycopy(bp, 3, this.stack, 0, this.esp + 1);
            }
            try {
                processInst(false);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    generateInterrupt(7);
                } catch (Exception e2) {
                }
                this.eip = this.functions[this.funcBase + 1];
                int parCount = this.functions[this.funcBase] >> 16;
                int localParamCount = this.functions[this.funcBase] & TextField.CONSTRAINT_MASK;
                this.esp = (((this.stackBase + parCount) + localParamCount) + 3) - 1;
                for (int ii = 0; ii < localParamCount; ii++) {
                    this.stack[(this.esp - 3) - ii] = 0;
                }
            }
        }
    }

    /* JADX WARNING: type inference failed for: r0v109, types: [int[]] */
    /* JADX WARNING: type inference failed for: r24v27, types: [byte[]] */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0051, code lost:
        if (isBreakPoint(r27.currentFunc, r27.eip) != false) goto L_0x0053;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processInst(boolean r28) throws java.lang.Exception {
        /*
            r27 = this;
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            int r23 = r23 + 2
            r7 = r22[r23]
        L_0x0010:
            r0 = r27
            int r0 = r0.eip
            r22 = r0
            r0 = r22
            r1 = r7
            if (r0 < r1) goto L_0x001c
        L_0x001b:
            return
        L_0x001c:
            if (r28 != 0) goto L_0x002a
            r0 = r27
            boolean r0 = r0.blocked
            r22 = r0
            if (r22 == 0) goto L_0x002a
            r27.saveStack()
            goto L_0x001b
        L_0x002a:
            r11 = 0
            if (r11 != 0) goto L_0x005d
            r0 = r27
            int r0 = r0.debugMode
            r22 = r0
            r23 = 1
            r0 = r22
            r1 = r23
            if (r0 == r1) goto L_0x0053
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            r0 = r27
            int r0 = r0.eip
            r23 = r0
            r0 = r27
            r1 = r22
            r2 = r23
            boolean r22 = r0.isBreakPoint(r1, r2)
            if (r22 == 0) goto L_0x005d
        L_0x0053:
            r22 = 3
            r0 = r27
            r1 = r22
            r0.generateInterrupt(r1)
            r11 = 1
        L_0x005d:
            if (r11 != 0) goto L_0x009f
            r0 = r27
            int r0 = r0.debugMode
            r22 = r0
            r23 = 2
            r0 = r22
            r1 = r23
            if (r0 != r1) goto L_0x009f
            r14 = 0
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            r0 = r27
            int r0 = r0.stepStartFunc
            r23 = r0
            r0 = r22
            r1 = r23
            if (r0 != r1) goto L_0x010c
            r0 = r27
            int r0 = r0.stackBase
            r22 = r0
            r0 = r27
            int r0 = r0.stepStartStackBase
            r23 = r0
            r0 = r22
            r1 = r23
            if (r0 != r1) goto L_0x010c
            r14 = 1
        L_0x0093:
            if (r14 == 0) goto L_0x009f
            r22 = 4
            r0 = r27
            r1 = r22
            r0.generateInterrupt(r1)
            r11 = 1
        L_0x009f:
            if (r11 != 0) goto L_0x00cb
            r0 = r27
            int r0 = r0.debugMode
            r22 = r0
            r23 = 3
            r0 = r22
            r1 = r23
            if (r0 != r1) goto L_0x00cb
            r0 = r27
            int r0 = r0.stackBase
            r22 = r0
            r0 = r27
            int r0 = r0.stepStartStackBase
            r23 = r0
            r0 = r22
            r1 = r23
            if (r0 >= r1) goto L_0x00cb
            r22 = 4
            r0 = r27
            r1 = r22
            r0.generateInterrupt(r1)
            r11 = 1
        L_0x00cb:
            r0 = r27
            byte[] r0 = r0.codeData
            r22 = r0
            r0 = r27
            int r0 = r0.eip
            r23 = r0
            byte r10 = r22[r23]
            switch(r10) {
                case 1: goto L_0x0121;
                case 2: goto L_0x0156;
                case 3: goto L_0x018c;
                case 4: goto L_0x01c2;
                case 5: goto L_0x01f8;
                case 6: goto L_0x022e;
                case 7: goto L_0x026b;
                case 8: goto L_0x02a8;
                case 9: goto L_0x02de;
                case 10: goto L_0x0314;
                case 11: goto L_0x034a;
                case 12: goto L_0x00dc;
                case 13: goto L_0x00dc;
                case 14: goto L_0x00dc;
                case 15: goto L_0x00dc;
                case 16: goto L_0x00dc;
                case 17: goto L_0x0380;
                case 18: goto L_0x03bf;
                case 19: goto L_0x03fe;
                case 20: goto L_0x00dc;
                case 21: goto L_0x00dc;
                case 22: goto L_0x00dc;
                case 23: goto L_0x00dc;
                case 24: goto L_0x00dc;
                case 25: goto L_0x00dc;
                case 26: goto L_0x00dc;
                case 27: goto L_0x00dc;
                case 28: goto L_0x00dc;
                case 29: goto L_0x00dc;
                case 30: goto L_0x00dc;
                case 31: goto L_0x00dc;
                case 32: goto L_0x00dc;
                case 33: goto L_0x043d;
                case 34: goto L_0x046e;
                case 35: goto L_0x04bf;
                case 36: goto L_0x0510;
                case 37: goto L_0x0619;
                case 38: goto L_0x06f4;
                case 39: goto L_0x07e6;
                case 40: goto L_0x00dc;
                case 41: goto L_0x00dc;
                case 42: goto L_0x00dc;
                case 43: goto L_0x00dc;
                case 44: goto L_0x00dc;
                case 45: goto L_0x00dc;
                case 46: goto L_0x00dc;
                case 47: goto L_0x00dc;
                case 48: goto L_0x00dc;
                case 49: goto L_0x088d;
                case 50: goto L_0x08b3;
                case 51: goto L_0x0924;
                case 52: goto L_0x0900;
                case 53: goto L_0x08de;
                case 54: goto L_0x0948;
                case 55: goto L_0x0986;
                case 56: goto L_0x09c5;
                case 57: goto L_0x09fd;
                case 58: goto L_0x0a14;
                case 59: goto L_0x0a46;
                case 60: goto L_0x0a8b;
                default: goto L_0x00dc;
            }
        L_0x00dc:
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            byte[] r23 = STACK_EFFECT
            r0 = r10
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            byte r23 = r23[r24]
            int r22 = r22 + r23
            r0 = r22
            r1 = r27
            r1.esp = r0
            r0 = r27
            int r0 = r0.eip
            r22 = r0
            byte[] r23 = INSTRUCTION_LENGTH
            r0 = r10
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            byte r23 = r23[r24]
            int r22 = r22 + r23
            r0 = r22
            r1 = r27
            r1.eip = r0
            goto L_0x0010
        L_0x010c:
            r0 = r27
            int r0 = r0.stackBase
            r22 = r0
            r0 = r27
            int r0 = r0.stepStartStackBase
            r23 = r0
            r0 = r22
            r1 = r23
            if (r0 >= r1) goto L_0x0093
            r14 = 1
            goto L_0x0093
        L_0x0121:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            int r24 = r24 + r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x0156:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            int r24 = r24 - r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x018c:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            int r24 = r24 * r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x01c2:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            int r24 = r24 / r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x01f8:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            int r24 = r24 % r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x022e:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            if (r24 == 0) goto L_0x0268
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r24 = r24[r25]
            if (r24 == 0) goto L_0x0268
            r24 = 1
        L_0x0264:
            r22[r23] = r24
            goto L_0x00dc
        L_0x0268:
            r24 = 0
            goto L_0x0264
        L_0x026b:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            if (r24 != 0) goto L_0x029f
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r24 = r24[r25]
            if (r24 == 0) goto L_0x02a5
        L_0x029f:
            r24 = 1
        L_0x02a1:
            r22[r23] = r24
            goto L_0x00dc
        L_0x02a5:
            r24 = 0
            goto L_0x02a1
        L_0x02a8:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            r24 = r24 & r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x02de:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            r24 = r24 | r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x0314:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            int r24 = r24 << r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x034a:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            int r24 = r24 >> r25
            r22[r23] = r24
            goto L_0x00dc
        L_0x0380:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x03bc
            r24 = 1
        L_0x03b8:
            r22[r23] = r24
            goto L_0x00dc
        L_0x03bc:
            r24 = 0
            goto L_0x03b8
        L_0x03bf:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            r0 = r24
            r1 = r25
            if (r0 <= r1) goto L_0x03fb
            r24 = 1
        L_0x03f7:
            r22[r23] = r24
            goto L_0x00dc
        L_0x03fb:
            r24 = 0
            goto L_0x03f7
        L_0x03fe:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            r0 = r24
            r1 = r25
            if (r0 >= r1) goto L_0x043a
            r24 = 1
        L_0x0436:
            r22[r23] = r24
            goto L_0x00dc
        L_0x043a:
            r24 = 0
            goto L_0x0436
        L_0x043d:
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            int r23 = r23 + 1
            r22 = r22[r23]
            r0 = r27
            byte[] r0 = r0.codeData
            r23 = r0
            r0 = r27
            int r0 = r0.eip
            r24 = r0
            int r24 = r24 + 1
            short r23 = getShort(r23, r24)
            r24 = 65535(0xffff, float:9.1834E-41)
            r23 = r23 & r24
            int r22 = r22 + r23
            r0 = r22
            r1 = r27
            r1.eip = r0
            goto L_0x0010
        L_0x046e:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22 = r22[r23]
            if (r22 == 0) goto L_0x00dc
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            int r23 = r23 + 1
            r22 = r22[r23]
            r0 = r27
            byte[] r0 = r0.codeData
            r23 = r0
            r0 = r27
            int r0 = r0.eip
            r24 = r0
            int r24 = r24 + 1
            short r23 = getShort(r23, r24)
            r24 = 65535(0xffff, float:9.1834E-41)
            r23 = r23 & r24
            int r22 = r22 + r23
            r0 = r22
            r1 = r27
            r1.eip = r0
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            r23 = 1
            int r22 = r22 - r23
            r0 = r22
            r1 = r27
            r1.esp = r0
            goto L_0x0010
        L_0x04bf:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22 = r22[r23]
            if (r22 != 0) goto L_0x00dc
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            int r23 = r23 + 1
            r22 = r22[r23]
            r0 = r27
            byte[] r0 = r0.codeData
            r23 = r0
            r0 = r27
            int r0 = r0.eip
            r24 = r0
            int r24 = r24 + 1
            short r23 = getShort(r23, r24)
            r24 = 65535(0xffff, float:9.1834E-41)
            r23 = r23 & r24
            int r22 = r22 + r23
            r0 = r22
            r1 = r27
            r1.eip = r0
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            r23 = 1
            int r22 = r22 - r23
            r0 = r22
            r1 = r27
            r1.esp = r0
            goto L_0x0010
        L_0x0510:
            r0 = r27
            byte[] r0 = r0.codeData
            r22 = r0
            r0 = r27
            int r0 = r0.eip
            r23 = r0
            int r23 = r23 + 1
            byte r22 = r22[r23]
            r0 = r22
            r0 = r0 & 255(0xff, float:3.57E-43)
            r16 = r0
            r0 = r27
            byte[] r0 = r0.codeData
            r22 = r0
            r0 = r27
            int r0 = r0.eip
            r23 = r0
            int r23 = r23 + 2
            short r22 = getShort(r22, r23)
            r23 = 65535(0xffff, float:9.1834E-41)
            r6 = r22 & r23
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 - r16
            int r15 = r22 + 1
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            int r23 = r6 * 3
            r22 = r22[r23]
            r23 = 65535(0xffff, float:9.1834E-41)
            r12 = r22 & r23
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r9 = r22 + 1
        L_0x055e:
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 + r12
            r0 = r9
            r1 = r22
            if (r0 <= r1) goto L_0x060b
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 + r12
            r0 = r22
            r1 = r27
            r1.esp = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 1
            r0 = r27
            int r0 = r0.stackBase
            r24 = r0
            r22[r23] = r24
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 2
            r0 = r27
            int r0 = r0.currentFunc
            r24 = r0
            r22[r23] = r24
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 3
            r0 = r27
            int r0 = r0.eip
            r24 = r0
            int r24 = r24 + 4
            r22[r23] = r24
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 + 3
            r0 = r22
            r1 = r27
            r1.esp = r0
            r0 = r15
            r1 = r27
            r1.stackBase = r0
            r0 = r6
            r1 = r27
            r1.currentFunc = r0
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            int r22 = r22 * 3
            r0 = r22
            r1 = r27
            r1.funcBase = r0
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            int r23 = r23 + 1
            r22 = r22[r23]
            r0 = r22
            r1 = r27
            r1.eip = r0
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            int r23 = r23 + 2
            r7 = r22[r23]
            goto L_0x0010
        L_0x060b:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r23 = 0
            r22[r9] = r23
            int r9 = r9 + 1
            goto L_0x055e
        L_0x0619:
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            r23 = 5
            r0 = r22
            r1 = r23
            if (r0 <= r1) goto L_0x001b
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            r22 = r22[r23]
            int r16 = r22 >> 16
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            r22 = r22[r23]
            r23 = 65535(0xffff, float:9.1834E-41)
            r12 = r22 & r23
            r0 = r27
            int r0 = r0.stackBase
            r22 = r0
            int r22 = r22 + r16
            int r22 = r22 + r12
            int r22 = r22 + 3
            r23 = 1
            int r20 = r22 - r23
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            r0 = r22
            r1 = r20
            if (r0 == r1) goto L_0x0681
            java.lang.Exception r22 = new java.lang.Exception
            java.lang.StringBuilder r23 = new java.lang.StringBuilder
            java.lang.String r24 = "从函数返回时栈不为空，函数ID："
            r23.<init>(r24)
            r0 = r27
            int r0 = r0.currentFunc
            r24 = r0
            java.lang.StringBuilder r23 = r23.append(r24)
            java.lang.String r23 = r23.toString()
            r22.<init>(r23)
            throw r22
        L_0x0681:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22 = r22[r23]
            r0 = r22
            r1 = r27
            r1.eip = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r22 = r22[r23]
            r0 = r22
            r1 = r27
            r1.currentFunc = r0
            r0 = r27
            int r0 = r0.stackBase
            r15 = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 2
            int r23 = r23 - r24
            r22 = r22[r23]
            r0 = r22
            r1 = r27
            r1.stackBase = r0
            r22 = 1
            int r22 = r15 - r22
            r0 = r22
            r1 = r27
            r1.esp = r0
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            int r22 = r22 * 3
            r0 = r22
            r1 = r27
            r1.funcBase = r0
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            int r23 = r23 + 2
            r7 = r22[r23]
            goto L_0x0010
        L_0x06f4:
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            r23 = 5
            r0 = r22
            r1 = r23
            if (r0 <= r1) goto L_0x001b
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            r22 = r22[r23]
            int r16 = r22 >> 16
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            r22 = r22[r23]
            r23 = 65535(0xffff, float:9.1834E-41)
            r12 = r22 & r23
            r0 = r27
            int r0 = r0.stackBase
            r22 = r0
            int r22 = r22 + r16
            int r22 = r22 + r12
            int r20 = r22 + 3
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            r0 = r22
            r1 = r20
            if (r0 == r1) goto L_0x0758
            java.lang.Exception r22 = new java.lang.Exception
            java.lang.StringBuilder r23 = new java.lang.StringBuilder
            java.lang.String r24 = "从函数返回时栈不为空，函数ID："
            r23.<init>(r24)
            r0 = r27
            int r0 = r0.currentFunc
            r24 = r0
            java.lang.StringBuilder r23 = r23.append(r24)
            java.lang.String r23 = r23.toString()
            r22.<init>(r23)
            throw r22
        L_0x0758:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r19 = r22[r23]
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r22 = r22[r23]
            r0 = r22
            r1 = r27
            r1.eip = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 2
            int r23 = r23 - r24
            r22 = r22[r23]
            r0 = r22
            r1 = r27
            r1.currentFunc = r0
            r0 = r27
            int r0 = r0.stackBase
            r15 = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 3
            int r23 = r23 - r24
            r22 = r22[r23]
            r0 = r22
            r1 = r27
            r1.stackBase = r0
            r0 = r15
            r1 = r27
            r1.esp = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22[r23] = r19
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            int r22 = r22 * 3
            r0 = r22
            r1 = r27
            r1.funcBase = r0
            r0 = r27
            int[] r0 = r0.functions
            r22 = r0
            r0 = r27
            int r0 = r0.funcBase
            r23 = r0
            int r23 = r23 + 2
            r7 = r22[r23]
            goto L_0x0010
        L_0x07e6:
            r0 = r27
            byte[] r0 = r0.codeData
            r22 = r0
            r0 = r27
            int r0 = r0.eip
            r23 = r0
            int r23 = r23 + 1
            short r6 = getShort(r22, r23)
            r0 = r27
            byte[] r0 = r0.codeData
            r22 = r0
            r0 = r27
            int r0 = r0.eip
            r23 = r0
            int r23 = r23 + 3
            byte r22 = r22[r23]
            r0 = r22
            r0 = r0 & 255(0xff, float:3.57E-43)
            r16 = r0
            r0 = r27
            byte[] r0 = r0.codeData
            r22 = r0
            r0 = r27
            int r0 = r0.eip
            r23 = r0
            int r23 = r23 + 4
            byte r22 = r22[r23]
            r23 = 1
            r0 = r22
            r1 = r23
            if (r0 != r1) goto L_0x0888
            r22 = 1
            r8 = r22
        L_0x082a:
            r0 = r16
            int[] r0 = new int[r0]
            r17 = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 - r16
            int r23 = r23 + 1
            r24 = 0
            r0 = r22
            r1 = r23
            r2 = r17
            r3 = r24
            r4 = r16
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 - r16
            r0 = r22
            r1 = r27
            r1.esp = r0
            r0 = r27
            r1 = r6
            r2 = r17
            int r18 = r0.syscall(r1, r2)
            if (r8 == 0) goto L_0x00dc
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 1
            r22[r23] = r18
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 + 1
            r0 = r22
            r1 = r27
            r1.esp = r0
            goto L_0x00dc
        L_0x0888:
            r22 = 0
            r8 = r22
            goto L_0x082a
        L_0x088d:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r24 = r24[r25]
            r0 = r27
            r1 = r24
            int r24 = r0.memLoad(r1)
            r22[r23] = r24
            goto L_0x00dc
        L_0x08b3:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22 = r22[r23]
            r0 = r27
            int[] r0 = r0.stack
            r23 = r0
            r0 = r27
            int r0 = r0.esp
            r24 = r0
            r25 = 1
            int r24 = r24 - r25
            r23 = r23[r24]
            r0 = r27
            r1 = r22
            r2 = r23
            r0.memSave(r1, r2)
            goto L_0x00dc
        L_0x08de:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 1
            r0 = r27
            byte[] r0 = r0.codeData
            r24 = r0
            r0 = r27
            int r0 = r0.eip
            r25 = r0
            int r25 = r25 + 1
            byte r24 = r24[r25]
            r22[r23] = r24
            goto L_0x00dc
        L_0x0900:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 1
            r0 = r27
            byte[] r0 = r0.codeData
            r24 = r0
            r0 = r27
            int r0 = r0.eip
            r25 = r0
            int r25 = r25 + 1
            short r24 = getShort(r24, r25)
            r22[r23] = r24
            goto L_0x00dc
        L_0x0924:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 1
            r0 = r27
            byte[] r0 = r0.codeData
            r24 = r0
            r0 = r27
            int r0 = r0.eip
            r25 = r0
            int r25 = r25 + 1
            int r24 = getInt(r24, r25)
            r22[r23] = r24
            goto L_0x00dc
        L_0x0948:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 1
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            r0 = r27
            r1 = r24
            r2 = r25
            int r24 = r0.arrLoad(r1, r2)
            r22[r23] = r24
            goto L_0x00dc
        L_0x0986:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r22 = r22[r23]
            r0 = r27
            int[] r0 = r0.stack
            r23 = r0
            r0 = r27
            int r0 = r0.esp
            r24 = r0
            r23 = r23[r24]
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r26 = 2
            int r25 = r25 - r26
            r24 = r24[r25]
            r0 = r27
            r1 = r22
            r2 = r23
            r3 = r24
            r0.arrSave(r1, r2, r3)
            goto L_0x00dc
        L_0x09c5:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r0 = r27
            byte[] r0 = r0.codeData
            r24 = r0
            r0 = r27
            int r0 = r0.eip
            r25 = r0
            int r25 = r25 + 1
            byte r24 = r24[r25]
            r0 = r27
            int[] r0 = r0.stack
            r25 = r0
            r0 = r27
            int r0 = r0.esp
            r26 = r0
            r25 = r25[r26]
            r0 = r27
            r1 = r24
            r2 = r25
            int r24 = r0.alloc(r1, r2)
            r22[r23] = r24
            goto L_0x00dc
        L_0x09fd:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22 = r22[r23]
            r0 = r27
            r1 = r22
            r0.free(r1)
            goto L_0x00dc
        L_0x0a14:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 1
            r0 = r27
            byte[] r0 = r0.codeData
            r24 = r0
            r0 = r27
            int r0 = r0.eip
            r25 = r0
            int r25 = r25 + 1
            short r24 = getShort(r24, r25)
            r0 = r24
            int[] r0 = new int[r0]
            r24 = r0
            r0 = r27
            r1 = r24
            int r24 = r0.makeTempObject(r1)
            r22[r23] = r24
            goto L_0x00dc
        L_0x0a46:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r22 = r22[r23]
            r0 = r27
            r1 = r22
            java.lang.Object r5 = r0.followPointer(r1)
            int[] r5 = (int[]) r5
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r24 = r24[r25]
            r25 = 1073741823(0x3fffffff, float:1.9999999)
            r24 = r24 & r25
            r24 = r5[r24]
            r22[r23] = r24
            goto L_0x00dc
        L_0x0a8b:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 1
            int r23 = r23 - r24
            r22 = r22[r23]
            r0 = r27
            r1 = r22
            java.lang.Object r5 = r0.followPointer(r1)
            int[] r5 = (int[]) r5
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r13 = r22[r23]
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r24 = 2
            int r23 = r23 - r24
            r21 = r22[r23]
            r22 = 1073741823(0x3fffffff, float:1.9999999)
            r22 = r22 & r13
            r5[r22] = r21
            goto L_0x00dc
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.gtvm.DebugVM.processInst(boolean):void");
    }

    /* JADX INFO: finally extract failed */
    public synchronized void execute(int funcID) {
        if (!this.running) {
            try {
                this.running = true;
                if (this.resumeFlag && funcID == 3) {
                    this.resumeFlag = false;
                    resume();
                } else if (!this.blocked || funcID != 3) {
                    this.currentFunc = funcID;
                    this.funcBase = this.currentFunc * 3;
                    this.esp = (this.functions[this.funcBase] & TextField.CONSTRAINT_MASK) - 1;
                    this.stackBase = 0;
                    for (int i = 0; i < (this.functions[this.funcBase] & TextField.CONSTRAINT_MASK); i++) {
                        this.stack[i] = 0;
                    }
                    this.eip = this.functions[this.funcBase + 1];
                    processInst(this.blocked);
                }
                this.running = false;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    generateInterrupt(7);
                } catch (Exception e2) {
                }
                this.eip = this.functions[this.funcBase + 1];
                int parCount = this.functions[this.funcBase] >> 16;
                int localParamCount = this.functions[this.funcBase] & TextField.CONSTRAINT_MASK;
                this.esp = (((this.stackBase + parCount) + localParamCount) + 3) - 1;
                for (int ii = 0; ii < localParamCount; ii++) {
                    this.stack[(this.esp - 3) - ii] = 0;
                }
                this.running = false;
            } catch (Throwable th) {
                this.running = false;
                throw th;
            }
        }
        return;
    }
}
