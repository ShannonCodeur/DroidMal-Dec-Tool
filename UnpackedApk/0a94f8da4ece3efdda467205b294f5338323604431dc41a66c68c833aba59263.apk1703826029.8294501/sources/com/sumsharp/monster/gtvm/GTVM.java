package com.sumsharp.monster.gtvm;

import com.sumsharp.android.ui.ChatUI;
import com.sumsharp.android.ui.GridMenu;
import com.sumsharp.lowui.AbstractLowUI;
import com.sumsharp.lowui.ActorUI;
import com.sumsharp.lowui.BlessPanel;
import com.sumsharp.lowui.Button;
import com.sumsharp.lowui.Form;
import com.sumsharp.lowui.GuildUI;
import com.sumsharp.lowui.Label;
import com.sumsharp.lowui.MatingPanel;
import com.sumsharp.lowui.Menu;
import com.sumsharp.lowui.NetPlayerInfoUI;
import com.sumsharp.lowui.NpcUI;
import com.sumsharp.lowui.PanelUI;
import com.sumsharp.lowui.PetDetailUI;
import com.sumsharp.lowui.PlayerDetailUI;
import com.sumsharp.lowui.StringDraw;
import com.sumsharp.lowui.TableUI;
import com.sumsharp.lowui.Tip;
import com.sumsharp.lowui.UI;
import com.sumsharp.lowui.UILayout;
import com.sumsharp.lowui.UpGradePanel;
import com.sumsharp.monster.Battle;
import com.sumsharp.monster.BattleMovie;
import com.sumsharp.monster.GetItem;
import com.sumsharp.monster.MapLoader;
import com.sumsharp.monster.MonsterMIDlet;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.GlobalVar;
import com.sumsharp.monster.common.PackageFile;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.common.data.Building;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.common.data.Door;
import com.sumsharp.monster.common.data.Friend;
import com.sumsharp.monster.common.data.NetPlayer;
import com.sumsharp.monster.common.data.Npc;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.common.data.Player;
import com.sumsharp.monster.common.data.Skill;
import com.sumsharp.monster.common.data.Task;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.image.PipAnimateSet;
import com.sumsharp.monster.item.GameItem;
import com.sumsharp.monster.net.Network;
import com.sumsharp.monster.net.UWAPSegment;
import com.sumsharp.monster.ui.VMUI;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.media.Manager;
import javax.microedition.rms.SharedPreferencesHelper;

public class GTVM implements GTVMConstants, CommandListener {
    public static final int CYCLE = 1;
    public static final int CYCLEUI = 3;
    public static final int DESTROY = 5;
    public static final int FALSE = 0;
    public static final int INIT = 0;
    public static final int PAINT = 4;
    public static final int PROCESSPACKET = 2;
    protected static final int TEMP_OBJECT_COUNT = 32;
    public static final int TRUE = 1;
    protected int[] blockPosition;
    protected boolean blocked;
    protected byte[] codeData;
    protected int currentFunc;
    protected Object[] dynamicHeap;
    protected int eip;
    protected int esp;
    protected int freeHead;
    protected short[] freeSpaceList;
    protected int funcBase;
    protected int[] functions;
    private String lastFormSelection;
    protected int nextTemp;
    protected VMUI owner;
    protected boolean resumeFlag;
    protected int[] stack;
    protected int stackBase;
    protected int[] staticHeap;
    protected String[] stringTable;

    public GTVM(VMUI ui) {
        this.owner = ui;
    }

    public boolean isBlock() {
        return this.blocked;
    }

    public void pauseProcess() {
        this.blocked = true;
    }

    public void continueProcess(int returnValue) {
        this.resumeFlag = true;
        if (this.blockPosition != null) {
            this.blockPosition[this.blockPosition.length - 1] = returnValue;
        }
    }

    /* access modifiers changed from: protected */
    public void saveStack() {
        this.blockPosition = new int[(this.esp + 4)];
        this.blockPosition[0] = this.stackBase;
        this.blockPosition[1] = this.eip;
        this.blockPosition[2] = this.currentFunc;
        if (this.esp >= 0) {
            System.arraycopy(this.stack, 0, this.blockPosition, 3, this.esp + 1);
        }
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
            }
        }
    }

    public void init(byte[] data) throws IOException {
        loadETF(data);
        this.esp = -1;
        this.stackBase = -1;
        this.eip = 0;
        this.currentFunc = 0;
        this.dynamicHeap = new Object[128];
        this.freeSpaceList = new short[128];
        for (int i = 31; i < 127; i++) {
            this.freeSpaceList[i] = (short) (i + 1);
        }
        this.freeSpaceList[127] = 31;
        this.freeHead = 31;
    }

    public void loadETF(byte[] data) throws IOException {
        String[] etfStringTable;
        int length;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        if (dataInputStream.readInt() != 1162300416) {
            throw new IOException("Invalid ETF file!");
        }
        dataInputStream.skip(8);
        short heapSize = dataInputStream.readShort();
        short taskAttr = dataInputStream.readShort();
        readUTF16(dataInputStream);
        readUTF16(dataInputStream);
        dataInputStream.readInt();
        short tk = dataInputStream.readShort();
        if (tk == 21332) {
            short count = dataInputStream.readShort();
            if (count <= 0) {
                throw new IOException("Invalid ETF file!");
            }
            etfStringTable = new String[count];
            int len = dataInputStream.readShort() & 65535;
            int i = 0;
            while (len > 0) {
                String s = readUTF16(dataInputStream);
                if (s.length() < 128) {
                    length = (s.length() * 2) + 1;
                } else {
                    length = (s.length() * 2) + 2;
                }
                len -= length;
                etfStringTable[i] = s;
                i = (short) (i + 1);
            }
            if (len == 0 && i == etfStringTable.length) {
                tk = dataInputStream.readShort();
            } else {
                throw new IOException("Invalid ETF file!");
            }
        } else {
            etfStringTable = new String[0];
        }
        if (tk == 17236) {
            short count2 = dataInputStream.readShort();
            if (count2 <= 0) {
                throw new IOException("Invalid ETF file!");
            }
            int[] etfFunctions = new int[(count2 * 3)];
            int len2 = dataInputStream.readInt();
            byte[] etfCode = new byte[len2];
            int codePos = 0;
            int base = 0;
            for (int i2 = 0; i2 < count2; i2++) {
                int paramCount = dataInputStream.readByte() & AbstractUnit.CLR_NAME_TAR;
                dataInputStream.skip((long) paramCount);
                int localVariables = dataInputStream.readShort() & TextField.CONSTRAINT_MASK;
                int funcLen = dataInputStream.readInt();
                etfFunctions[base] = (paramCount << 16) | localVariables;
                etfFunctions[base + 1] = codePos;
                dataInputStream.read(etfCode, codePos, funcLen);
                etfFunctions[base + 2] = codePos + funcLen;
                codePos += funcLen;
                len2 = ((((len2 - 1) - paramCount) - 2) - 4) - funcLen;
                base += 3;
            }
            if (len2 != 0) {
                throw new IOException("Invalid ETF file!");
            }
            this.staticHeap = new int[(65535 & heapSize)];
            this.stack = new int[(65535 & taskAttr)];
            this.stringTable = etfStringTable;
            this.functions = etfFunctions;
            this.codeData = etfCode;
            return;
        }
        throw new IOException("Invalid ETF file!");
    }

    public static String readUTF16(DataInputStream is) throws IOException {
        int slen = is.readByte();
        if ((slen & 128) != 0) {
            slen = ((slen & 127) << 8) + (is.readByte() & AbstractUnit.CLR_NAME_TAR);
        }
        char[] buf = new char[slen];
        for (int i = 0; i < slen; i++) {
            buf[i] = is.readChar();
        }
        return new String(buf);
    }

    public void destroy() {
        this.staticHeap = null;
        this.stack = null;
        this.dynamicHeap = null;
        this.freeSpaceList = null;
        this.stringTable = null;
        this.functions = null;
    }

    /* access modifiers changed from: protected */
    public int heapAlloc() {
        if (this.freeSpaceList[this.freeHead] == this.freeHead) {
            int expandSize = this.dynamicHeap.length / 2;
            Object[] newarr = new Object[(this.dynamicHeap.length + expandSize)];
            short[] newarr2 = new short[(this.dynamicHeap.length + expandSize)];
            System.arraycopy(this.dynamicHeap, 0, newarr, 0, this.dynamicHeap.length);
            System.arraycopy(this.freeSpaceList, 0, newarr2, 0, this.dynamicHeap.length);
            for (int i = this.dynamicHeap.length; i < newarr2.length; i++) {
                newarr2[i] = (short) (i + 1);
            }
            newarr2[newarr2.length - 1] = this.freeSpaceList[this.freeHead];
            newarr2[this.freeHead] = (short) this.dynamicHeap.length;
            this.dynamicHeap = newarr;
            this.freeSpaceList = newarr2;
        }
        int next = this.freeSpaceList[this.freeHead] & TextField.CONSTRAINT_MASK;
        this.freeSpaceList[this.freeHead] = this.freeSpaceList[next];
        return next;
    }

    /* access modifiers changed from: protected */
    public void heapFree(int addr) {
        if ((addr & 4095) >= 32) {
            Object obj = this.dynamicHeap[addr];
            if (obj instanceof ImageSet) {
                ImageLoadManager.release(((ImageSet) obj).name);
            }
            if (obj instanceof PipAnimateSet) {
                ImageLoadManager.release(((PipAnimateSet) obj).name);
            }
            this.dynamicHeap[addr] = null;
            short tmp = this.freeSpaceList[this.freeHead];
            this.freeSpaceList[this.freeHead] = (short) addr;
            this.freeSpaceList[addr] = tmp;
        }
    }

    public static short getShort(byte[] data, int off) {
        return (short) (((data[off] & 255) << 8) | (data[off + 1] & 255));
    }

    public static void setShort(byte[] data, int off, short value) {
        data[off] = (byte) ((value >> 8) & AbstractUnit.CLR_NAME_TAR);
        data[off + 1] = (byte) (value & 255);
    }

    public static int getInt(byte[] data, int off) {
        return ((data[off] & 255) << 24) | ((data[off + 1] & 255) << 16) | ((data[off + 2] & 255) << 8) | (data[off + 3] & 255);
    }

    public static void setInt(byte[] data, int off, int value) {
        data[off] = (byte) ((value >> 24) & AbstractUnit.CLR_NAME_TAR);
        data[off + 1] = (byte) ((value >> 16) & AbstractUnit.CLR_NAME_TAR);
        data[off + 2] = (byte) ((value >> 8) & AbstractUnit.CLR_NAME_TAR);
        data[off + 3] = (byte) (value & AbstractUnit.CLR_NAME_TAR);
    }

    /* access modifiers changed from: protected */
    public int memLoad(int addr) {
        if ((Integer.MIN_VALUE & addr) == 0) {
            return this.staticHeap[addr & 1073741823];
        }
        return this.stack[this.stackBase + (1073741823 & addr)];
    }

    /* access modifiers changed from: protected */
    public void memSave(int addr, int value) {
        if ((Integer.MIN_VALUE & addr) == 0) {
            this.staticHeap[addr & 1073741823] = value;
        } else {
            this.stack[this.stackBase + (1073741823 & addr)] = value;
        }
    }

    /* access modifiers changed from: protected */
    public int arrLoad(int addr, int offset) {
        int pointer = addr;
        int dataType = (pointer >> 26) & 15;
        if (dataType > 3) {
            return (offset << 12) | pointer | 33554432;
        }
        Object obj = this.dynamicHeap[pointer & 4095];
        switch (dataType) {
            case 0:
                return ((boolean[]) obj)[offset] ? 1 : 0;
            case 1:
                return ((byte[]) obj)[offset];
            case 2:
                return ((short[]) obj)[offset];
            case 3:
                return ((int[]) obj)[offset];
            default:
                return 0;
        }
    }

    /* access modifiers changed from: protected */
    public void arrSave(int addr, int offset, int value) {
        int pointer = addr;
        int dataType = (pointer >> 26) & 15;
        Object obj = this.dynamicHeap[pointer & 4095];
        if (dataType > 3) {
            ((Object[]) obj)[offset] = followPointer(value);
            return;
        }
        switch (dataType) {
            case 0:
                ((boolean[]) obj)[offset] = value != 0;
                return;
            case 1:
                ((byte[]) obj)[offset] = (byte) value;
                return;
            case 2:
                ((short[]) obj)[offset] = (short) value;
                return;
            case 3:
                ((int[]) obj)[offset] = value;
                return;
            default:
                return;
        }
    }

    public Object followPointer(int pointer) {
        if (pointer == 0) {
            return null;
        }
        if ((Integer.MIN_VALUE & pointer) != 0) {
            return this.stringTable[Integer.MAX_VALUE & pointer];
        }
        int dataType = (pointer >> 26) & 31;
        if (dataType >= 4 && dataType <= 19) {
            return this.dynamicHeap[pointer & 4095];
        }
        if (dataType < 20) {
            return null;
        }
        Object[] arr = (Object[]) this.dynamicHeap[pointer & 4095];
        return (33554432 & pointer) != 0 ? arr[(pointer >> 12) & 8191] : arr;
    }

    /* access modifiers changed from: protected */
    public String[] getStringArrayFromParams(int pointer) {
        String[] strs = null;
        Object[] obs = (Object[]) followPointer(pointer);
        if (obs != null) {
            strs = new String[obs.length];
            for (int i = 0; i < strs.length; i++) {
                strs[i] = (String) obs[i];
            }
        }
        return strs;
    }

    /* access modifiers changed from: protected */
    public int alloc(byte dataType, int length) {
        int ret = heapAlloc();
        switch (dataType) {
            case 0:
                this.dynamicHeap[ret] = new boolean[length];
                break;
            case 1:
                this.dynamicHeap[ret] = new byte[length];
                break;
            case 2:
                this.dynamicHeap[ret] = new short[length];
                break;
            case 3:
                this.dynamicHeap[ret] = new int[length];
                break;
            default:
                this.dynamicHeap[ret] = new Object[length];
                break;
        }
        return ((dataType + 16) << 26) | ret;
    }

    /* access modifiers changed from: protected */
    public void free(int addr) {
        if ((-2113929216 & addr) == 0) {
            heapFree(addr & 4095);
        }
    }

    public int makeTempObject(Object obj) {
        if (obj == null) {
            return 0;
        }
        this.dynamicHeap[this.nextTemp] = obj;
        int addr = this.nextTemp;
        this.nextTemp = (this.nextTemp + 1) & 31;
        if (obj instanceof boolean[]) {
            return 1073741824 | addr;
        }
        if (obj instanceof byte[]) {
            return 1140850688 | addr;
        }
        if (obj instanceof short[]) {
            return 1207959552 | addr;
        }
        if (obj instanceof int[]) {
            return 1275068416 | addr;
        }
        if (obj instanceof Object[]) {
            return 1342177280 | addr;
        }
        return 268435456 | addr;
    }

    /* JADX WARNING: type inference failed for: r0v109, types: [int[]] */
    /* JADX WARNING: type inference failed for: r24v27, types: [byte[]] */
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
            r8 = r22[r23]
        L_0x0010:
            r0 = r27
            int r0 = r0.eip
            r22 = r0
            r0 = r22
            r1 = r8
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
            r0 = r27
            byte[] r0 = r0.codeData
            r22 = r0
            r0 = r27
            int r0 = r0.eip
            r23 = r0
            byte r11 = r22[r23]
            long r20 = java.lang.System.currentTimeMillis()
            switch(r11) {
                case 1: goto L_0x008b;
                case 2: goto L_0x00c0;
                case 3: goto L_0x00f6;
                case 4: goto L_0x012c;
                case 5: goto L_0x0162;
                case 6: goto L_0x0198;
                case 7: goto L_0x01d5;
                case 8: goto L_0x0212;
                case 9: goto L_0x0248;
                case 10: goto L_0x027e;
                case 11: goto L_0x02b4;
                case 12: goto L_0x003f;
                case 13: goto L_0x003f;
                case 14: goto L_0x003f;
                case 15: goto L_0x003f;
                case 16: goto L_0x003f;
                case 17: goto L_0x02ea;
                case 18: goto L_0x0329;
                case 19: goto L_0x0368;
                case 20: goto L_0x003f;
                case 21: goto L_0x003f;
                case 22: goto L_0x003f;
                case 23: goto L_0x003f;
                case 24: goto L_0x003f;
                case 25: goto L_0x003f;
                case 26: goto L_0x003f;
                case 27: goto L_0x003f;
                case 28: goto L_0x003f;
                case 29: goto L_0x003f;
                case 30: goto L_0x003f;
                case 31: goto L_0x003f;
                case 32: goto L_0x003f;
                case 33: goto L_0x03a7;
                case 34: goto L_0x03d8;
                case 35: goto L_0x0429;
                case 36: goto L_0x047a;
                case 37: goto L_0x0582;
                case 38: goto L_0x0603;
                case 39: goto L_0x069f;
                case 40: goto L_0x003f;
                case 41: goto L_0x003f;
                case 42: goto L_0x003f;
                case 43: goto L_0x003f;
                case 44: goto L_0x003f;
                case 45: goto L_0x003f;
                case 46: goto L_0x003f;
                case 47: goto L_0x003f;
                case 48: goto L_0x003f;
                case 49: goto L_0x0747;
                case 50: goto L_0x076d;
                case 51: goto L_0x07de;
                case 52: goto L_0x07ba;
                case 53: goto L_0x0798;
                case 54: goto L_0x0802;
                case 55: goto L_0x0840;
                case 56: goto L_0x087f;
                case 57: goto L_0x08b7;
                case 58: goto L_0x08ce;
                case 59: goto L_0x0900;
                case 60: goto L_0x0945;
                default: goto L_0x003f;
            }
        L_0x003f:
            r22 = 39
            r0 = r11
            r1 = r22
            if (r0 != r1) goto L_0x005c
            int r22 = com.sumsharp.monster.ui.VMUI.csize
            r23 = 4
            r0 = r22
            r1 = r23
            if (r0 != r1) goto L_0x005c
            long r22 = com.sumsharp.monster.ui.VMUI.drawExecuteTime
            long r24 = java.lang.System.currentTimeMillis()
            long r24 = r24 - r20
            long r22 = r22 + r24
            com.sumsharp.monster.ui.VMUI.drawExecuteTime = r22
        L_0x005c:
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            byte[] r23 = STACK_EFFECT
            r0 = r11
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
            r0 = r11
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            byte r23 = r23[r24]
            int r22 = r22 + r23
            r0 = r22
            r1 = r27
            r1.eip = r0
            goto L_0x0010
        L_0x008b:
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
            goto L_0x003f
        L_0x00c0:
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
            goto L_0x003f
        L_0x00f6:
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
            goto L_0x003f
        L_0x012c:
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
            goto L_0x003f
        L_0x0162:
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
            goto L_0x003f
        L_0x0198:
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
            if (r24 == 0) goto L_0x01d2
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r24 = r24[r25]
            if (r24 == 0) goto L_0x01d2
            r24 = 1
        L_0x01ce:
            r22[r23] = r24
            goto L_0x003f
        L_0x01d2:
            r24 = 0
            goto L_0x01ce
        L_0x01d5:
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
            if (r24 != 0) goto L_0x0209
            r0 = r27
            int[] r0 = r0.stack
            r24 = r0
            r0 = r27
            int r0 = r0.esp
            r25 = r0
            r24 = r24[r25]
            if (r24 == 0) goto L_0x020f
        L_0x0209:
            r24 = 1
        L_0x020b:
            r22[r23] = r24
            goto L_0x003f
        L_0x020f:
            r24 = 0
            goto L_0x020b
        L_0x0212:
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
            goto L_0x003f
        L_0x0248:
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
            goto L_0x003f
        L_0x027e:
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
            goto L_0x003f
        L_0x02b4:
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
            goto L_0x003f
        L_0x02ea:
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
            if (r0 != r1) goto L_0x0326
            r24 = 1
        L_0x0322:
            r22[r23] = r24
            goto L_0x003f
        L_0x0326:
            r24 = 0
            goto L_0x0322
        L_0x0329:
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
            if (r0 <= r1) goto L_0x0365
            r24 = 1
        L_0x0361:
            r22[r23] = r24
            goto L_0x003f
        L_0x0365:
            r24 = 0
            goto L_0x0361
        L_0x0368:
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
            if (r0 >= r1) goto L_0x03a4
            r24 = 1
        L_0x03a0:
            r22[r23] = r24
            goto L_0x003f
        L_0x03a4:
            r24 = 0
            goto L_0x03a0
        L_0x03a7:
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
        L_0x03d8:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22 = r22[r23]
            if (r22 == 0) goto L_0x003f
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
        L_0x0429:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22 = r22[r23]
            if (r22 != 0) goto L_0x003f
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
        L_0x047a:
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
            r15 = r0
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
            int r22 = r22 - r15
            int r14 = r22 + 1
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
            int r10 = r22 + 1
        L_0x04c7:
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 + r12
            r0 = r10
            r1 = r22
            if (r0 <= r1) goto L_0x0574
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
            r0 = r14
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
            r8 = r22[r23]
            goto L_0x0010
        L_0x0574:
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r23 = 0
            r22[r10] = r23
            int r10 = r10 + 1
            goto L_0x04c7
        L_0x0582:
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            r23 = 5
            r0 = r22
            r1 = r23
            if (r0 <= r1) goto L_0x001b
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
            r14 = r0
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
            int r22 = r14 - r22
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
            r8 = r22[r23]
            goto L_0x0010
        L_0x0603:
            r0 = r27
            int r0 = r0.currentFunc
            r22 = r0
            r23 = 5
            r0 = r22
            r1 = r23
            if (r0 <= r1) goto L_0x001b
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r18 = r22[r23]
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
            r14 = r0
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
            r0 = r14
            r1 = r27
            r1.esp = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            r22[r23] = r18
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
            r8 = r22[r23]
            goto L_0x0010
        L_0x069f:
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
            r15 = r0
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
            if (r0 != r1) goto L_0x073e
            r22 = 1
            r9 = r22
        L_0x06e2:
            r0 = r15
            int[] r0 = new int[r0]
            r16 = r0
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 - r15
            int r23 = r23 + 1
            r24 = 0
            r0 = r22
            r1 = r23
            r2 = r16
            r3 = r24
            r4 = r15
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 - r15
            r0 = r22
            r1 = r27
            r1.esp = r0
            r0 = r27
            r1 = r6
            r2 = r16
            int r17 = r0.syscall(r1, r2)     // Catch:{ Exception -> 0x0743 }
        L_0x071c:
            if (r9 == 0) goto L_0x003f
            r0 = r27
            int[] r0 = r0.stack
            r22 = r0
            r0 = r27
            int r0 = r0.esp
            r23 = r0
            int r23 = r23 + 1
            r22[r23] = r17
            r0 = r27
            int r0 = r0.esp
            r22 = r0
            int r22 = r22 + 1
            r0 = r22
            r1 = r27
            r1.esp = r0
            goto L_0x003f
        L_0x073e:
            r22 = 0
            r9 = r22
            goto L_0x06e2
        L_0x0743:
            r7 = move-exception
            r17 = 0
            goto L_0x071c
        L_0x0747:
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
            goto L_0x003f
        L_0x076d:
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
            goto L_0x003f
        L_0x0798:
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
            goto L_0x003f
        L_0x07ba:
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
            goto L_0x003f
        L_0x07de:
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
            goto L_0x003f
        L_0x0802:
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
            goto L_0x003f
        L_0x0840:
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
            goto L_0x003f
        L_0x087f:
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
            goto L_0x003f
        L_0x08b7:
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
            goto L_0x003f
        L_0x08ce:
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
            goto L_0x003f
        L_0x0900:
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
            goto L_0x003f
        L_0x0945:
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
            r19 = r22[r23]
            r22 = 1073741823(0x3fffffff, float:1.9999999)
            r22 = r22 & r13
            r5[r22] = r19
            goto L_0x003f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.gtvm.GTVM.processInst(boolean):void");
    }

    public synchronized void execute(int funcID) {
        try {
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
                if (funcID == 4) {
                    VMUI.csize = 4;
                    VMUI.drawExecuteTime = 0;
                } else {
                    VMUI.csize = -1;
                }
                processInst(this.blocked);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /* Debug info: failed to restart local var, previous not found, register: 135 */
    public int syscall(short funcID, int[] params) throws Exception {
        boolean z;
        String str;
        PetDetailUI ret;
        int dir;
        boolean isTemp;
        switch (funcID) {
            case 1:
                if (Utilities.isKeyPressed(params[0], params[1] != 0)) {
                    return 1;
                }
                return 0;
            case 2:
                return Utilities.isAnyKeyPressed() ? 0 : 1;
            case 3:
                Utilities.clearKeyStates();
                break;
            case 4:
                return Tool.getNextRnd(0, 10000);
            case 5:
                return Utilities.getTimeStamp();
            case 6:
                String str2 = (String) followPointer(params[0]);
                if (str2.equals("")) {
                    str2 = "0";
                }
                return Integer.parseInt(str2);
            case 7:
                return makeTempObject(String.valueOf(params[0]));
            case 8:
                return makeTempObject(OpenSound((String) followPointer(params[0])));
            case 9:
                PlaySound(followPointer(params[0]), params[1]);
                break;
            case 10:
                CloseSound(followPointer(params[0]));
                break;
            case 11:
                Vibra();
                break;
            case 12:
                return followPointer(params[0]) == null ? 1 : 0;
            case Tool.IMAGE_FONT_WIDTH /*13*/:
                Object obj = followPointer(params[0]);
                if (obj == null) {
                    return 0;
                }
                if ((params[0] & Tool.FILL_L_DARK) != 0 || (params[0] & 4095) >= 32) {
                    isTemp = false;
                } else {
                    isTemp = true;
                }
                if (isTemp) {
                    this.dynamicHeap[params[0] & 4095] = null;
                }
                int newaddr = heapAlloc();
                this.dynamicHeap[newaddr] = obj;
                return (params[0] & -4096) | newaddr;
            case 16:
                return makeTempObject(new Integer(params[0]));
            case 17:
                return ((Integer) followPointer(params[0])).intValue();
            case 18:
                return Length(followPointer(params[0]));
            case 19:
                for (int i = 31; i >= 0; i--) {
                    this.dynamicHeap[i] = null;
                }
                System.gc();
                break;
            case 20:
                Utilities utilities = new Utilities(Utilities.entryURL, 0, this);
                new Thread(utilities).start();
                break;
            case 21:
                AbstractUnit.initActionAniFrame((byte[]) followPointer(params[0]));
                break;
            case 22:
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((byte[]) followPointer(params[0]));
                return makeTempObject(new DataInputStream(byteArrayInputStream));
            case 23:
                return makeTempObject(new ByteArrayOutputStream());
            case 24:
                return ((DataInputStream) followPointer(params[0])).readInt();
            case 25:
                return ((DataInputStream) followPointer(params[0])).readShort();
            case 26:
                return ((DataInputStream) followPointer(params[0])).readByte();
            case 27:
                if (((DataInputStream) followPointer(params[0])).readBoolean()) {
                    return 1;
                }
                return 0;
            case 28:
                return makeTempObject(((DataInputStream) followPointer(params[0])).readUTF());
            case 29:
                DataOutputStream dataOutputStream = new DataOutputStream((ByteArrayOutputStream) followPointer(params[0]));
                dataOutputStream.writeInt(params[1]);
                break;
            case 30:
                DataOutputStream dataOutputStream2 = new DataOutputStream((ByteArrayOutputStream) followPointer(params[0]));
                dataOutputStream2.writeShort((short) params[1]);
                break;
            case 31:
                DataOutputStream dataOutputStream3 = new DataOutputStream((ByteArrayOutputStream) followPointer(params[0]));
                dataOutputStream3.writeUTF((String) followPointer(params[1]));
                break;
            case 32:
                DataOutputStream dataOutputStream4 = new DataOutputStream((ByteArrayOutputStream) followPointer(params[0]));
                dataOutputStream4.writeByte((byte) params[1]);
                break;
            case 33:
                DataOutputStream dataOutputStream5 = new DataOutputStream((ByteArrayOutputStream) followPointer(params[0]));
                dataOutputStream5.writeBoolean(params[1] != 0);
                break;
            case 34:
                ((DataInputStream) followPointer(params[0])).readFully((byte[]) followPointer(params[1]));
                break;
            case 35:
                DataOutputStream dataOutputStream6 = new DataOutputStream((ByteArrayOutputStream) followPointer(params[0]));
                dataOutputStream6.write((byte[]) followPointer(params[1]));
                break;
            case 36:
                return ((ByteArrayOutputStream) followPointer(params[0])).size();
            case 37:
                return makeTempObject(((ByteArrayOutputStream) followPointer(params[0])).toByteArray());
            case 38:
                if (((UWAPSegment) followPointer(params[0])).isErrorPacket()) {
                    return 1;
                }
                return 0;
            case 39:
                return ((UWAPSegment) followPointer(params[0])).subType;
            case 40:
                return makeTempObject(new UWAPSegment((byte) params[0], (byte) params[1]));
            case 41:
                return ((UWAPSegment) followPointer(params[0])).mainType;
            case 42:
                ((UWAPSegment) followPointer(params[0])).reset();
                break;
            case 43:
                return ((UWAPSegment) followPointer(params[0])).readInt();
            case 44:
                return ((UWAPSegment) followPointer(params[0])).readShort();
            case GridMenu.gridBound /*45*/:
                return ((UWAPSegment) followPointer(params[0])).readByte();
            case 46:
                if (((UWAPSegment) followPointer(params[0])).readBoolean()) {
                    return 1;
                }
                return 0;
            case 47:
                return makeTempObject(((UWAPSegment) followPointer(params[0])).readString());
            case 48:
                ((UWAPSegment) followPointer(params[0])).writeInt(params[1]);
                break;
            case 49:
                ((UWAPSegment) followPointer(params[0])).writeShort((short) params[1]);
                break;
            case 50:
                ((UWAPSegment) followPointer(params[0])).writeString((String) followPointer(params[1]));
                break;
            case 51:
                ((UWAPSegment) followPointer(params[0])).writeByte((byte) params[1]);
                break;
            case 52:
                ((UWAPSegment) followPointer(params[0])).writeBoolean(params[1] != 0);
                break;
            case 53:
                return makeTempObject(((UWAPSegment) followPointer(params[0])).readInts());
            case 54:
                return makeTempObject(((UWAPSegment) followPointer(params[0])).readShorts());
            case 55:
                return makeTempObject(((UWAPSegment) followPointer(params[0])).readBytes());
            case 56:
                return makeTempObject(((UWAPSegment) followPointer(params[0])).readBooleans());
            case 57:
                return makeTempObject(((UWAPSegment) followPointer(params[0])).readStrings());
            case 58:
                ((UWAPSegment) followPointer(params[0])).writeInts((int[]) followPointer(params[1]));
                break;
            case 59:
                ((UWAPSegment) followPointer(params[0])).writeShorts((short[]) followPointer(params[1]));
                break;
            case GTVMConstants.INSTRUCTION_MAX /*60*/:
                ((UWAPSegment) followPointer(params[0])).writeStrings((Object[]) followPointer(params[1]));
                break;
            case 61:
                ((UWAPSegment) followPointer(params[0])).writeBooleans((boolean[]) followPointer(params[1]));
                break;
            case 62:
                ((UWAPSegment) followPointer(params[0])).writeBytes((byte[]) followPointer(params[1]));
                break;
            case 63:
                return Utilities.sendRequest((UWAPSegment) followPointer(params[0]));
            case 64:
                return makeTempObject(World.instance.nextPacket);
            case 65:
                ((UWAPSegment) followPointer(params[0])).handled = true;
                break;
            case 66:
                UWAPSegment segment = (UWAPSegment) followPointer(params[0]);
                segment.flush();
                Utilities.segments.addElement(segment);
                break;
            case 67:
                return ((UWAPSegment) followPointer(params[0])).serial;
            case 68:
                ((UWAPSegment) followPointer(params[0])).needResponse = params[1] == 1;
                break;
            case 69:
                return makeTempObject(new Vector());
            case 70:
                return ((Vector) followPointer(params[0])).size();
            case 71:
                ((Vector) followPointer(params[0])).addElement(new Integer(params[1]));
                break;
            case 72:
                Object obj2 = ((Vector) followPointer(params[0])).elementAt(params[1]);
                if (obj2 instanceof Integer) {
                    free(((Integer) obj2).intValue());
                }
                ((Vector) followPointer(params[0])).removeElementAt(params[1]);
                break;
            case 73:
                return makeTempObject(vector_get((Vector) followPointer(params[0]), params[1]));
            case 74:
                Vector vec = (Vector) followPointer(params[0]);
                for (int i2 = 0; i2 < vec.size(); i2++) {
                    if (vec.elementAt(i2) instanceof Integer) {
                        free(((Integer) vec.elementAt(i2)).intValue());
                    }
                }
                ((Vector) followPointer(params[0])).removeAllElements();
                break;
            case World.MILLIS_PRE_UPDATE /*75*/:
                return makeTempObject(new Hashtable());
            case 76:
                ((Hashtable) followPointer(params[0])).put(followPointer(params[1]), followPointer(params[2]));
                break;
            case 77:
                return makeTempObject(((Hashtable) followPointer(params[0])).get(followPointer(params[1])));
            case 78:
                return makeTempObject(loadRMSFile((String) followPointer(params[0])));
            case 79:
                return saveRMSFile((String) followPointer(params[0]), (byte[]) followPointer(params[1])) ? 1 : 0;
            case GridMenu.SPEED /*80*/:
                deleteRMSFile((String) followPointer(params[0]));
                break;
            case 81:
                GlobalVar.deleteGlobalVar((String) followPointer(params[0]));
                break;
            case 82:
                GlobalVar.setGlobalValue((String) followPointer(params[0]), params[1]);
                break;
            case 83:
                GlobalVar.setGlobalValue((String) followPointer(params[0]), (Object) (String) followPointer(params[1]));
                break;
            case 84:
                return GlobalVar.getGlobalInt((String) followPointer(params[0]));
            case 85:
                return makeTempObject(GlobalVar.getGlobalString((String) followPointer(params[0])));
            case 86:
                return makeTempObject(GlobalVar.getGlobalObject((String) followPointer(params[0])));
            case 87:
                GlobalVar.setGlobalValue((String) followPointer(params[0]), followPointer(params[1]));
                break;
            case 88:
                if (params[0] != 1) {
                    Utilities.isExitGame = true;
                    break;
                } else {
                    Utilities.exitToMenu(params[1] == 1);
                    break;
                }
            case 89:
                Utilities.closeConnection();
                break;
            case 90:
                MonsterMIDlet.instance.platformRequest((String) followPointer(params[0]));
                break;
            case 91:
                Utilities utilities2 = new Utilities((String) followPointer(params[0]), 1, this);
                new Thread(utilities2).start();
                break;
            case 92:
                Utilities utilities3 = new Utilities((String) followPointer(params[0]), 0, this);
                new Thread(utilities3).start();
                break;
            case 93:
                byte[] dd = Utilities.lastDownloadData;
                Utilities.lastDownloadData = null;
                return makeTempObject(dd);
            case 94:
                ((Graphics) followPointer(params[0])).fillRect(params[1], params[2], params[3], params[4]);
                break;
            case 95:
                DrawString((Graphics) followPointer(params[0]), (String) followPointer(params[1]), params[2], params[3], params[4]);
                break;
            case 96:
                ((Graphics) followPointer(params[0])).setColor(params[1]);
                break;
            case 97:
                DrawRect((Graphics) followPointer(params[0]), params[1], params[2], params[3], params[4]);
                break;
            case 98:
                Object obj3 = followPointer(params[0]);
                if (obj3 instanceof Graphics) {
                    SetClip((Graphics) obj3, params[1], params[2], params[3], params[4]);
                    break;
                }
                break;
            case 99:
                return World.viewWidth;
            case 100:
                return World.viewHeight;
            case 101:
                ((Graphics) followPointer(params[0])).fillArc(params[1], params[2], params[3], params[4], params[5], params[6]);
                break;
            case 102:
                return makeTempObject(Utilities.graphics);
            case 103:
                return makeTempObject(Image.createImage(params[0], params[1]));
            case 104:
                return makeTempObject(((Image) followPointer(params[0])).getGraphics());
            case 105:
                ((Graphics) followPointer(params[0])).drawImage((Image) followPointer(params[1]), params[2], params[3], params[4]);
                break;
            case 106:
                DrawCircle((Graphics) followPointer(params[0]), params[1], params[2], params[3]);
                break;
            case 107:
                Tool.fillAlphaRect((Graphics) followPointer(params[0]), params[1], params[2], params[3], params[4], params[5]);
                break;
            case 108:
                AbstractUnit unit = Tool.getUnit(params[0], params[1]);
                if (unit != null) {
                    return makeTempObject(unit.cartoonPlayer.animate);
                }
                break;
            case 109:
                Object[] objs = (Object[]) followPointer(params[0]);
                ImageSet[] imgs = new ImageSet[objs.length];
                System.arraycopy(objs, 0, imgs, 0, objs.length);
                PipAnimateSet pipAnimateSet = new PipAnimateSet(imgs, World.findResource((String) followPointer(params[1]), false));
                return makeTempObject(pipAnimateSet);
            case 110:
                ((PipAnimateSet) followPointer(params[0])).drawFrame((Graphics) followPointer(params[1]), params[2], params[3], params[4]);
                break;
            case 111:
                ((PipAnimateSet) followPointer(params[0])).drawAnimateFrame((Graphics) followPointer(params[1]), params[2], params[3], params[4], params[5]);
                break;
            case 112:
                return ((PipAnimateSet) followPointer(params[0])).getAnimateLength(params[1]);
            case 113:
                ((PipAnimateSet) followPointer(params[0])).drawHead((Graphics) followPointer(params[1]), params[2], params[3]);
                break;
            case 114:
                PipAnimateSet set = ImageLoadManager.getAnimate((String) followPointer(params[0]));
                if (set == null) {
                    return 0;
                }
                return makeTempObject(set);
            case 115:
                ImageLoadManager.release((String) followPointer(params[0]));
                break;
            case 116:
                String s = (String) followPointer(params[0]);
                if (World.findResource(s, false) != null) {
                    ImageSet imageSet = new ImageSet(s);
                    if (imageSet.pipImg != null) {
                        imageSet.pipImg.toFullBuffer();
                    }
                    return makeTempObject(imageSet);
                }
                break;
            case 117:
                ImageSet imageSet2 = new ImageSet((String) followPointer(params[0]), params[1], params[2]);
                return makeTempObject(imageSet2);
            case 118:
                ImageSet imageSet3 = new ImageSet((String) followPointer(params[0]), params[1], params[2], 0);
                return makeTempObject(imageSet3);
            case 119:
                ImageSet imageSet4 = new ImageSet((byte[]) followPointer(params[0]));
                return makeTempObject(imageSet4);
            case 120:
                ((ImageSet) followPointer(params[0])).drawFrame((Graphics) followPointer(params[1]), params[2], params[3], params[4], params[5]);
                break;
            case 121:
                return ((ImageSet) followPointer(params[0])).getFrameWidth(params[1]);
            case 122:
                return ((ImageSet) followPointer(params[0])).getFrameHeight(params[1]);
            case 123:
                ImageSet fs = (ImageSet) followPointer(params[0]);
                if (fs.pipImg != null) {
                    fs.pipImg.gray();
                    break;
                }
                break;
            case 124:
                ImageSet fs2 = (ImageSet) followPointer(params[0]);
                if (fs2.pipImg != null) {
                    fs2.pipImg.darker(params[1]);
                    break;
                }
                break;
            case 125:
                ImageSet fs3 = (ImageSet) followPointer(params[0]);
                if (fs3.pipImg != null) {
                    fs3.pipImg.darker(params[1]);
                    break;
                }
                break;
            case 126:
                ImageSet fs4 = (ImageSet) followPointer(params[0]);
                if (fs4.pipImg != null) {
                    fs4.pipImg.mask(params[1]);
                    break;
                }
                break;
            case 127:
                ((ImageSet) followPointer(params[0])).drawFrame((Graphics) followPointer(params[1]), params[2], params[3], params[4], params[5], params[6]);
                break;
            case 128:
                World.drawUI = true;
                break;
            case 129:
                World.drawUI = false;
                break;
            case 130:
                boolean ret2 = Utilities.toActorSelect;
                if (ret2) {
                    Utilities.toActorSelect = false;
                }
                return ret2 ? 1 : 0;
            case 131:
                return Tool.getUIColor(params[0]);
            case 132:
                return Utilities.CHAR_HEIGHT;
            case 133:
                return Utilities.LINE_HEIGHT;
            case 134:
                return Utilities.font.stringWidth((String) followPointer(params[0]));
            case 135:
                String str3 = new String((String) followPointer(params[0]));
                return makeTempObject(str3);
            case 136:
                return makeTempObject(String.valueOf((String) followPointer(params[0])) + ((String) followPointer(params[1])));
            case 137:
                return ((String) followPointer(params[0])).length();
            case 138:
                return makeTempObject(((String) followPointer(params[0])).substring(params[1], params[1] + params[2]));
            case 139:
                return ((String) followPointer(params[0])).indexOf((String) followPointer(params[1]), params[2]);
            case 140:
                return makeTempObject(String.valueOf((String) followPointer(params[0])) + params[1]);
            case 141:
                return ((String) followPointer(params[0])).equals((String) followPointer(params[1])) ? 1 : 0;
            case 142:
                return ((String) followPointer(params[0])).charAt(params[1]);
            case 143:
                return makeTempObject(((String) followPointer(params[0])).trim());
            case 144:
                String str4 = new String((byte[]) followPointer(params[0]), (String) followPointer(params[1]));
                return makeTempObject(str4);
            case 145:
                return makeTempObject(((String) followPointer(params[0])).replace(((String) followPointer(params[1])).charAt(0), ((String) followPointer(params[2])).charAt(0)));
            case 146:
                return makeTempObject(Utilities.formatText((String) followPointer(params[0]), params[1], Utilities.font));
            case 147:
                return makeTempObject(Tool.splitString((String) followPointer(params[0]), ((String) followPointer(params[1])).charAt(0)));
            case 148:
                return makeTempObject(Tool.splitString((String) followPointer(params[0])));
            case 149:
                return makeTempObject(Tool.splitString((String) followPointer(params[0]), ((String) followPointer(params[1])).charAt(0)));
            case 150:
                if (params[3] != 1 && params[4] != 1) {
                    Tool.drawBoxNew((Graphics) followPointer(params[0]), params[1], params[2], params[3], params[4], params[5]);
                    break;
                } else {
                    Tool.drawLine((Graphics) followPointer(params[0]), params[1], params[2], params[3], params[4], params[5]);
                    break;
                }
                break;
            case 151:
                Tool.drawArrow((Graphics) followPointer(params[0]), params[1], params[2], params[3]);
                break;
            case 152:
                Graphics g = (Graphics) followPointer(params[0]);
                String txt = (String) followPointer(params[1]);
                int y = params[3];
                if (params[5] == 1) {
                    dir = 4;
                } else {
                    dir = 8;
                }
                Tool.drawUIButton(g, txt, y, dir, false);
                break;
            case 154:
                Tool.drawImageFont((Graphics) followPointer(params[0]), (String) followPointer(params[1]), params[2], params[3]);
                break;
            case 155:
                return makeTempObject(CartoonPlayer.playCartoon(ImageLoadManager.getAnimate((String) followPointer(params[0])), params[1], params[2], params[3], params[4] == 1));
            case 156:
                CartoonPlayer cp = (CartoonPlayer) followPointer(params[0]);
                if (cp != null) {
                    cp.nextFrame();
                    break;
                }
                break;
            case 157:
                CartoonPlayer cp2 = (CartoonPlayer) followPointer(params[0]);
                Graphics g2 = (Graphics) followPointer(params[1]);
                g2.setClip(0, 0, World.viewWidth, World.viewHeight);
                if (cp2 != null) {
                    cp2.draw(g2);
                    break;
                }
                break;
            case 158:
                CartoonPlayer cp3 = (CartoonPlayer) followPointer(params[0]);
                if (cp3 != null) {
                    cp3.setDrawPostion(params[1], params[2]);
                    break;
                }
                break;
            case 159:
                CartoonPlayer cp4 = (CartoonPlayer) followPointer(params[0]);
                if (cp4 != null) {
                    cp4.setAnimateIndex(params[1]);
                    break;
                }
                break;
            case 256:
                NewStage.initPackageAndStage((byte[]) followPointer(params[0]));
                break;
            case 258:
                NewStage.isMapLoadOk = false;
                NewStage.scriptMoveMap = false;
                break;
            case 259:
                return CommonData.player.mapId;
            case 260:
                return CommonData.player.pixelX;
            case 261:
                return CommonData.player.pixelY;
            case 262:
                return NewStage.areaId;
            case 263:
                return NewStage.currentMapId;
            case 264:
                int id = params[0];
                if (id != -1) {
                    NetPlayer np = NewStage.getNetPlayer(id);
                    if (np != null) {
                        return np.teamState;
                    }
                    return -1;
                } else if (CommonData.team.isTeamMode()) {
                    return CommonData.player.teamState;
                } else {
                    return -1;
                }
            case 265:
                int id2 = params[0];
                if (id2 != -1) {
                    NetPlayer np2 = NewStage.getNetPlayer(id2);
                    if (np2 == null) {
                        return 0;
                    }
                    if (np2.teamRole == 1) {
                        return 1;
                    }
                    return 2;
                } else if (!CommonData.team.isTeamMode()) {
                    return 0;
                } else {
                    if (CommonData.player == CommonData.team.leader) {
                        return 1;
                    }
                    return 2;
                }
            case 266:
                int mapId = params[0];
                if (mapId == -1) {
                    mapId = CommonData.player.mapId & 15;
                }
                NewStage.loadMap(mapId);
                break;
            case 267:
                NewStage.areaId = (short) params[0];
                break;
            case 268:
                NewStage.currentMapId = (short) params[0];
                break;
            case 269:
                return CommonData.player.money;
            case 270:
                return CommonData.player.id;
            case 271:
                return makeTempObject(CommonData.player.name);
            case 272:
                CommonData.player.setActionState((byte) params[0]);
                break;
            case 273:
                if (params[0] == 0) {
                    return CommonData.player.bagSize;
                }
                return CommonData.player.bankSize;
            case 274:
                if (CommonData.player.targetPlayer == null) {
                    return -1;
                }
                return CommonData.player.targetPlayer.id;
            case 275:
                if (CommonData.player.targetPlayer != null) {
                    return makeTempObject(CommonData.player.targetPlayer.name);
                }
                break;
            case 276:
                return makeTempObject(CommonData.player.whisperName);
            case 277:
                String str5 = (String) followPointer(params[0]);
                if (str5 == null) {
                    str5 = "";
                }
                Player player = CommonData.player;
                String str6 = new String(str5);
                player.whisperName = str6;
                break;
            case 278:
                AbstractUnit au = Tool.getUnit(params[0], params[1]);
                if (au != null) {
                    String str7 = new String((String) followPointer(params[2]));
                    au.guild = str7;
                    break;
                }
                break;
            case 279:
                return CommonData.player.sex;
            case 280:
                return CommonData.player.hr;
            case 281:
                NetPlayer np3 = NewStage.getNetPlayer(params[0]);
                if (np3 != null) {
                    return np3.level;
                }
                return 1;
            case 282:
                return CommonData.player.pets.size();
            case 283:
                Pet ppp = (Pet) followPointer(params[0]);
                if (ppp != null) {
                    return ppp.id;
                }
                break;
            case 284:
                try {
                    return makeTempObject(CommonData.player.getPet(params[0]).name);
                } catch (Exception e) {
                    Exception exc = e;
                    break;
                }
            case 285:
                return CommonData.player.getPet(params[0]).sex;
            case 286:
                int i3 = params[0];
                return CommonData.player.getPet(params[0]).getAttribute((byte) params[1]);
            case 287:
                return makeTempObject(CommonData.player.getPetIDs());
            case Tool.DECORATE2 /*288*/:
                return CommonData.player.getPet(params[0]).matingTimes;
            case 289:
                Pet pet = CommonData.player.getPet(params[0]);
                if (pet != null) {
                    return pet.battleOpened ? 1 : 0;
                }
                return 0;
            case 290:
                Pet pet2 = CommonData.player.getPet(params[0]);
                if (pet2 != null) {
                    return pet2.followOpened ? 1 : 0;
                }
                return 0;
            case 291:
                return CommonData.player.updatePetSwitchState(params[0], params[1], params[2]) ? 1 : 0;
            case 292:
                Pet pet3 = CommonData.player.getPet(params[1]);
                if (pet3 != null) {
                    Tool.drawPet(pet3, (Graphics) followPointer(params[0]), params[2], params[3]);
                    break;
                }
                break;
            case 295:
                return Battle.battleType;
            case 296:
                Pet pet4 = new Pet();
                ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream((byte[]) followPointer(params[0]));
                pet4.load(new DataInputStream(byteArrayInputStream2));
                return makeTempObject(pet4);
            case 297:
                Pet pet5 = Battle.findBattleUnit(params[0]);
                if (pet5 != null) {
                    return pet5.battleStatus;
                }
                break;
            case 298:
                CommonData.bLogined = params[0] == 1;
                break;
            case 299:
                return makeTempObject(CommonData.player.getFriendIDs());
            case HttpConnection.HTTP_MULT_CHOICE /*300*/:
                Friend f = CommonData.player.getFriend(params[0]);
                if (f == null) {
                    return 0;
                }
                return makeTempObject(f.name);
            case HttpConnection.HTTP_MOVED_PERM /*301*/:
                Friend f2 = CommonData.player.getFriend(params[0]);
                if (f2 != null) {
                    return f2.sex;
                }
                return 0;
            case HttpConnection.HTTP_MOVED_TEMP /*302*/:
                Friend f3 = CommonData.player.getFriend(params[0]);
                if (f3 != null) {
                    return f3.state;
                }
                return 0;
            case HttpConnection.HTTP_SEE_OTHER /*303*/:
                return makeTempObject(GetItem.getItem((UWAPSegment) followPointer(params[0])));
            case HttpConnection.HTTP_NOT_MODIFIED /*304*/:
                int id3 = params[0];
                int type = params[1];
                Graphics g3 = (Graphics) followPointer(params[2]);
                int dx = params[3];
                int dy = params[4];
                AbstractUnit unit2 = Tool.getUnit(id3, type);
                if (unit2 != null) {
                    unit2.drawHead(g3, dx, dy);
                    break;
                }
                break;
            case HttpConnection.HTTP_USE_PROXY /*305*/:
                Battle.select(Battle.findBattleUnit(params[0]));
                break;
            case 306:
                if (Battle.battle_target != null) {
                    return Battle.battle_target.id;
                }
                return -1;
            case HttpConnection.HTTP_TEMP_REDIRECT /*307*/:
                Battle.clearBattle();
                break;
            case 308:
                Battle.readInitSegment((UWAPSegment) followPointer(params[0]));
                break;
            case 309:
                Battle.initBattlePet(params[0], (int[]) followPointer(params[1]), getStringArrayFromParams(params[2]), (int[]) followPointer(params[3]), (int[]) followPointer(params[4]), (int[]) followPointer(params[5]), (int[]) followPointer(params[6]), (int[]) followPointer(params[7]), (int[]) followPointer(params[8]), (int[]) followPointer(params[9]));
                break;
            case 311:
                return makeTempObject(BattleMovie.parseBattleMovie((byte[]) followPointer(params[0])));
            case 313:
                ((BattleMovie) followPointer(params[0])).cycle(params[1]);
                break;
            case 314:
                ((BattleMovie) followPointer(params[0])).paint((Graphics) followPointer(params[1]));
                break;
            case 315:
                BattleMovie movie = (BattleMovie) followPointer(params[0]);
                if (movie != null) {
                    return movie.die ? 1 : 0;
                }
                return 0;
            case 316:
                BattleMovie movie2 = (BattleMovie) followPointer(params[0]);
                if (movie2 != null) {
                    return movie2.started ? 1 : 0;
                }
                return 0;
            case 317:
                return Battle.battleID;
            case 318:
                return Battle.round;
            case 319:
                return makeTempObject(Battle.petId);
            case 320:
                return makeTempObject(Battle.armyId);
            case 321:
                return Battle.isMyPet(params[0]) ? 1 : 0;
            case 322:
                Battle.readRoundEnd(params[0], (short) params[1]);
                break;
            case 323:
                return makeTempObject(CommonData.account.name);
            case 324:
                return CommonData.player.doorId;
            case 325:
                return NewStage.loadProgress;
            case 326:
                int v = params[0];
                int max = params[1];
                int type2 = params[2];
                if (type2 != -1) {
                    if (type2 != 1) {
                        NewStage.setLoadProgress(v, max);
                        break;
                    } else {
                        NewStage.incLoadProgress(max);
                        break;
                    }
                } else {
                    NewStage.loadProgress = 0;
                    break;
                }
            case 327:
                AbstractUnit unit3 = Tool.getUnit(params[0], params[1]);
                if (unit3 != null) {
                    unit3.savePosition();
                    break;
                }
                break;
            case 328:
                Pet pet6 = Battle.findBattleUnit(params[0]);
                if (pet6 != null) {
                    pet6.addStatus(params[1]);
                    break;
                }
                break;
            case 329:
                Pet pet7 = Battle.findBattleUnit(params[0]);
                if (pet7 != null) {
                    pet7.removeStatus(params[1]);
                    break;
                }
                break;
            case 330:
                if (World.addGamePackage((byte[]) followPointer(params[0]))) {
                    return 1;
                }
                return 0;
            case 331:
                World.clearGamePackages();
                break;
            case 332:
                return makeTempObject(LoadFile((String) followPointer(params[0])));
            case 333:
                Tool.loadUIResFromRMS();
                break;
            case 334:
                int id4 = params[0];
                int type3 = params[1];
                int dx2 = params[2];
                int dy2 = params[3];
                boolean direct = params[4] == 1;
                AbstractUnit unit4 = Tool.getUnit(id4, type3);
                if (unit4 != null) {
                    if (!direct) {
                        unit4.moveTo(unit4.pixelX + dx2, unit4.pixelY + dy2);
                        break;
                    } else {
                        unit4.go(dx2, dy2);
                        break;
                    }
                }
                break;
            case 335:
                int id5 = params[0];
                int type4 = params[1];
                int x = params[2];
                int y2 = params[3];
                AbstractUnit unit5 = Tool.getUnit(id5, type4);
                if (unit5 != null) {
                    unit5.setPosition(x - (unit5.getWidth() / 2), y2);
                    break;
                }
                break;
            case 336:
                AbstractUnit unit6 = Tool.getUnit(params[0], params[1]);
                if (unit6 != null) {
                    if (unit6.inBattle) {
                        return unit6.pixelX;
                    }
                    return unit6.pixelX - NewStage.viewX;
                }
                break;
            case 337:
                AbstractUnit unit7 = Tool.getUnit(params[0], params[1]);
                if (unit7 != null) {
                    if (unit7.inBattle) {
                        return unit7.pixelY;
                    }
                    return unit7.pixelY - NewStage.getMapDrawY();
                }
                break;
            case 338:
                NewStage.moveMap(params[0]);
                NewStage.scriptMoveMap = true;
                break;
            case 339:
                String eid = (String) followPointer(params[0]);
                int etype = params[1];
                AbstractUnit unit8 = Tool.getUnit(params[2], params[3]);
                if (unit8 != null) {
                    unit8.createEmote(eid, (byte) etype, true);
                    break;
                }
                break;
            case 340:
                String eid2 = (String) followPointer(params[0]);
                AbstractUnit unit9 = Tool.getUnit(params[1], params[2]);
                if (unit9 != null) {
                    unit9.clearEmote(eid2);
                    break;
                }
                break;
            case 341:
                AbstractUnit unit10 = Tool.getUnit(params[0], params[1]);
                if (!(unit10 == null || unit10.cartoonPlayer.getAnimateIndex() == params[2])) {
                    unit10.cartoonPlayer.setAnimateIndex(params[2]);
                    unit10.cartoonPlayer.setFrameIndex(params[3]);
                    break;
                }
            case 342:
                Pet pet8 = CommonData.player.getPet(params[0]);
                if (pet8 != null) {
                    return makeTempObject(pet8.getSkillIDs(params[1]));
                }
                break;
            case 343:
                Pet pet9 = CommonData.player.getPet(params[0]);
                if (pet9 != null) {
                    Skill skill = pet9.getSkill(params[1]);
                    if (skill != null) {
                        return makeTempObject(skill.name);
                    }
                }
                break;
            case 344:
                Pet pet10 = CommonData.player.getPet(params[0]);
                if (pet10 != null) {
                    Skill skill2 = pet10.getSkill(params[1]);
                    if (skill2 != null) {
                        return skill2.mp;
                    }
                }
                break;
            case 345:
                Pet pet11 = CommonData.player.getPet(params[0]);
                if (pet11 != null) {
                    Skill skill3 = pet11.getSkill(params[1]);
                    if (skill3 != null) {
                        return skill3.level;
                    }
                }
                break;
            case 346:
                Pet pet12 = CommonData.player.getPet(params[0]);
                if (pet12 != null) {
                    Skill skill4 = pet12.getSkill(params[1]);
                    if (skill4 != null) {
                        return skill4.atkBound;
                    }
                }
                break;
            case 347:
                Pet pet13 = CommonData.player.getPet(params[0]);
                if (pet13 != null) {
                    Skill skill5 = pet13.getSkill(params[1]);
                    if (skill5 != null) {
                        return skill5.type;
                    }
                }
                break;
            case 348:
                int i4 = 0;
                while (true) {
                    if (i4 >= NewStage.fieldPets.length) {
                        break;
                    } else if (NewStage.fieldPets[i4].id == params[0]) {
                        int[] v2 = getGameItemBag(-6);
                        if (v2.length > 0) {
                            NewStage.fieldPets[i4].flyy = 0;
                            NewStage.fieldPets[i4].addValue = Utilities.random(4, 9);
                            GameItem item = CommonData.player.findItem(v2[0]);
                            item.count = (byte) (item.count - 1);
                            if (item.count == 0) {
                                CommonData.player.removeItem(item.id);
                                break;
                            }
                        }
                    } else {
                        i4++;
                    }
                }
                break;
            case 349:
                Tool.drawMoney((Graphics) followPointer(params[0]), params[1], params[2], params[3], params[4], params[5]);
                break;
            case 350:
                this.owner.initVMUI(params[0] == 1, params[1] == 1, params[2], params[3] == 1);
                break;
            case 351:
                this.owner.setBounds(params[0], params[1], params[2], params[3]);
                break;
            case 352:
                this.owner.setContentXBounds(params[0], params[1], params[2] == 1);
                break;
            case 358:
                this.owner.close();
                break;
            case 362:
                if (!(params[1] == 0 || this.owner == null)) {
                    this.owner.close();
                }
                CommonComponent.loadUI((String) followPointer(params[0]));
                break;
            case 369:
                Tool.drawFocusFrame((Graphics) followPointer(params[0]), params[1], params[2], params[3]);
                break;
            case 370:
                Tool.drawButtonTip((Graphics) followPointer(params[0]), params[1], params[2], params[3], (String) followPointer(params[4]));
                break;
            case 373:
                return makeTempObject(this.owner.getContentBounds());
            case 374:
                this.owner.setCloseable(params[0] == 1);
                break;
            case 375:
                this.owner.setDrawBackground(params[0] == 1);
                break;
            case 377:
                this.owner.setDrawMenuBar(params[0] == 1);
                break;
            case 379:
                CommonComponent.closeAllUI();
                break;
            case 381:
                pauseProcess();
                break;
            case 382:
                continueProcess(params[0]);
                break;
            case 383:
                if (this.owner != null) {
                    this.owner.showMessage((String) followPointer(params[0]), params[1], (String) followPointer(params[2]), params[3], params[4] == 1, params[5] == 1);
                    break;
                }
                break;
            case Tool.DECORATE1 /*384*/:
                if (this.owner != null) {
                    this.owner.showMessage((String) followPointer(params[0]), params[1], (String) followPointer(params[2]), (byte) params[3], getStringArrayFromParams(params[4]), params[5] == 1);
                    break;
                }
                break;
            case 385:
                if (this.owner != null) {
                    return this.owner.lastMessageBtnSel;
                }
                break;
            case 386:
                if (this.owner != null) {
                    this.owner.closeMessage();
                }
                CommonComponent.closeMessage();
                break;
            case 387:
                this.owner.setMessageLines(params[0], params[1]);
                break;
            case 388:
                return makeTempObject(Tool.uiMiscImg);
            case 392:
                this.owner.setClearKeyStatus(params[0] == 1);
                break;
            case 393:
                Tool.drawNumStr((String) followPointer(params[1]), (Graphics) followPointer(params[0]), params[2], params[3], params[4], params[5], params[6]);
                break;
            case 394:
                Tool.draw3DString((Graphics) followPointer(params[0]), (String) followPointer(params[1]), params[2], params[3], params[4], params[5]);
                break;
            case 395:
                this.owner.setFullScreen(params[0] == 1);
                break;
            case 396:
                Tool.drawButton((Graphics) followPointer(params[0]), (String) followPointer(params[1]), params[2], params[3], params[4], params[5] == 1, params[6] == 1, 1, 20);
                break;
            case 398:
                Form form = new Form((String) followPointer(params[0]), params[4] == 1);
                this.owner.form = form;
                form.setOwner(this.owner);
                form.setX(params[1]);
                form.setY(params[2]);
                form.setWidth(params[3]);
                return makeTempObject(form);
            case 399:
                String name = (String) followPointer(params[1]);
                String value = (String) followPointer(params[2]);
                com.sumsharp.lowui.TextField textField = new com.sumsharp.lowui.TextField(name, (Form) followPointer(params[0]), params[4], params[5]);
                if ((params[5] & 16777216) != 0) {
                    textField.setUilLayout(new UILayout(params[3], false, false));
                } else {
                    textField.setUilLayout(new UILayout(params[3], true, false));
                }
                textField.setName(name);
                textField.setText(value);
                return makeTempObject(textField);
            case HttpConnection.HTTP_BAD_REQUEST /*400*/:
                Form form2 = (Form) followPointer(params[0]);
                String name2 = (String) followPointer(params[1]);
                String value2 = (String) followPointer(params[2]);
                Button button = new Button(name2, form2, params[4]);
                UILayout uILayout = new UILayout(params[3], (params[4] & 16777216) == 0, false);
                button.setUilLayout(uILayout);
                button.setName(name2);
                button.setText(value2);
                form2.layout();
                return makeTempObject(button);
            case HttpConnection.HTTP_UNAUTHORIZED /*401*/:
                String name3 = (String) followPointer(params[1]);
                String value3 = (String) followPointer(params[2]);
                Label label = new Label(name3, (Form) followPointer(params[0]), 0);
                if (name3.equals("icon")) {
                    label.setName(name3);
                    label.setIconId(Integer.parseInt(value3));
                } else {
                    label.setName(name3);
                    label.setText(value3);
                }
                label.setUilLayout(new UILayout(params[3], params[4] == 1, false));
                break;
            case HttpConnection.HTTP_FORBIDDEN /*403*/:
                Form form3 = (Form) followPointer(params[0]);
                if (form3 != null) {
                    this.owner.form = form3;
                    form3.setOwner(this.owner);
                    break;
                }
                break;
            case HttpConnection.HTTP_NOT_FOUND /*404*/:
                this.owner.form = null;
                break;
            case HttpConnection.HTTP_BAD_METHOD /*405*/:
                Form form4 = (Form) followPointer(params[0]);
                if (form4 != null) {
                    form4.paint((Graphics) followPointer(params[1]), 0, 0);
                    break;
                }
                break;
            case HttpConnection.HTTP_NOT_ACCEPTABLE /*406*/:
                Form form5 = (Form) followPointer(params[0]);
                if (form5 != null) {
                    return form5.getHeight();
                }
                return 0;
            case HttpConnection.HTTP_PROXY_AUTH /*407*/:
                Form form6 = (Form) followPointer(params[0]);
                AbstractLowUI ui = (AbstractLowUI) followPointer(params[1]);
                if (form6 == null) {
                    return 0;
                }
                if (ui == form6.getFocusUI()) {
                    return 1;
                }
                return 0;
            case HttpConnection.HTTP_CLIENT_TIMEOUT /*408*/:
                com.sumsharp.lowui.TextField tf = (com.sumsharp.lowui.TextField) followPointer(params[0]);
                if (tf != null) {
                    return makeTempObject(tf.getText());
                }
                return 0;
            case HttpConnection.HTTP_CONFLICT /*409*/:
                com.sumsharp.lowui.TextField tf2 = (com.sumsharp.lowui.TextField) followPointer(params[0]);
                if (tf2 != null) {
                    String str8 = new String((String) followPointer(params[1]));
                    tf2.setText(str8);
                    break;
                }
                break;
            case HttpConnection.HTTP_GONE /*410*/:
                com.sumsharp.lowui.TextField tf3 = (com.sumsharp.lowui.TextField) followPointer(params[0]);
                if (tf3 != null) {
                    String str9 = new String((String) followPointer(params[1]));
                    tf3.addListItem(str9);
                    break;
                }
                break;
            case HttpConnection.HTTP_LENGTH_REQUIRED /*411*/:
                Button btn = (Button) followPointer(params[0]);
                if (btn == null) {
                    return 0;
                }
                return btn.isSelection() ? 1 : 0;
            case HttpConnection.HTTP_PRECON_FAILED /*412*/:
                Button btn2 = (Button) followPointer(params[0]);
                if (btn2 == null) {
                    return 0;
                }
                return btn2.isPressed() ? 1 : 0;
            case HttpConnection.HTTP_REQ_TOO_LONG /*414*/:
                GameItem item2 = CommonData.player.findItem(params[0]);
                if (item2 != null) {
                    return makeTempObject(item2);
                }
                break;
            case HttpConnection.HTTP_UNSUPPORTED_TYPE /*415*/:
                return makeTempObject(getGameItemBag(params[0]));
            case 512:
                GameItem item3 = (GameItem) followPointer(params[0]);
                if (item3 != null) {
                    return item3.count;
                }
                return 0;
            case 513:
                GameItem item4 = (GameItem) followPointer(params[0]);
                if (item4 != null) {
                    return item4.quanlity;
                }
                return 0;
            case 514:
                GameItem item5 = (GameItem) followPointer(params[0]);
                if (item5 != null) {
                    return item5.id;
                }
                return 0;
            case 515:
                GameItem item6 = (GameItem) followPointer(params[0]);
                if (item6 != null) {
                    return item6.canUseSelf() ? 1 : 0;
                }
                return 0;
            case 516:
                GameItem item7 = (GameItem) followPointer(params[0]);
                if (item7 != null) {
                    return item7.canUseOther() ? 1 : 0;
                }
                return 0;
            case 517:
                Vector vec2 = new Vector();
                for (int i5 = 0; i5 < CommonData.player.itemBag.size(); i5++) {
                    GameItem item8 = (GameItem) CommonData.player.itemBag.elementAt(i5);
                    if (item8.canUseSelf()) {
                        vec2.addElement(item8);
                    }
                }
                return makeTempObject(vec2);
            case 518:
                GameItem item9 = (GameItem) followPointer(params[0]);
                if (item9 != null) {
                    return item9.canuseBattle() ? 1 : 0;
                }
                return 0;
            case 519:
                GameItem item10 = (GameItem) followPointer(params[0]);
                if (item10 != null) {
                    return item10.getUseTarget();
                }
                return 0;
            case 520:
                GameItem item11 = (GameItem) followPointer(params[0]);
                if (item11 != null) {
                    return item11.canSellToNpc() ? 1 : 0;
                }
                return 0;
            case 521:
                GameItem item12 = (GameItem) followPointer(params[0]);
                if (item12 != null) {
                    return item12.canTradeWithPlayer() ? 1 : 0;
                }
                return 0;
            case 522:
                GameItem item13 = (GameItem) followPointer(params[0]);
                if (item13 != null) {
                    return makeTempObject(item13.name);
                }
                break;
            case 523:
                GameItem item14 = CommonData.player.findItem(params[1]);
                if (item14 != null) {
                    item14.drawIcon((Graphics) followPointer(params[0]), params[2], params[3], params[4]);
                    break;
                }
                break;
            case 524:
                Tool.drawGameItemIcon((Graphics) followPointer(params[0]), params[1], params[2] - 1, params[3], params[4], params[5]);
                break;
            case 525:
                GameItem item15 = (GameItem) followPointer(params[0]);
                if (item15 == null) {
                    return 0;
                }
                return makeTempObject(item15.desc);
            case 526:
                GameItem item16 = (GameItem) followPointer(params[0]);
                if (item16 != null) {
                    return item16.canUseSelfPet() ? 1 : 0;
                }
                return 0;
            case 527:
                GameItem item17 = (GameItem) followPointer(params[0]);
                if (item17 != null) {
                    return item17.canUseOtherPet() ? 1 : 0;
                }
                return 0;
            case 528:
                return Tool.getQuanlityColor(params[0]);
            case 529:
                NewStage.hasNewMail = params[0];
                break;
            case 530:
                return makeTempObject(new Menu((String) followPointer(params[0]), (Menu) followPointer(params[1]), getStringArrayFromParams(params[2]), getStringArrayFromParams(params[3]), params[4]));
            case 531:
                UI ui2 = (UI) followPointer(params[1]);
                if (ui2 != null) {
                    ui2.paint((Graphics) followPointer(params[0]));
                    break;
                }
                break;
            case 532:
                UI ui3 = (UI) followPointer(params[0]);
                if (ui3 != null) {
                    ui3.cycle();
                    break;
                }
                break;
            case 533:
                UI ui4 = (UI) followPointer(params[0]);
                if (ui4 != null) {
                    return ui4.getMenuSelection();
                }
                break;
            case 534:
                UI ui5 = (UI) followPointer(params[0]);
                if (ui5 != null) {
                    ui5.close();
                    break;
                }
                break;
            case 535:
                return ((UI) followPointer(params[0])).isClosed() ? 1 : 0;
            case 536:
                ((UI) followPointer(params[0])).reset();
                break;
            case 537:
                NpcUI npcUI = new NpcUI(params[0], (String) followPointer(params[1]), getStringArrayFromParams(params[2]), getStringArrayFromParams(params[3]));
                return makeTempObject(npcUI);
            case 538:
                return makeTempObject(new TableUI(params[0], (String) followPointer(params[1]), null, null, getStringArrayFromParams(params[2]), (int[]) followPointer(params[3]), params[4]));
            case 539:
                TableUI tableUI = (TableUI) followPointer(params[0]);
                if (tableUI != null) {
                    tableUI.addTableItem((String) followPointer(params[1]), getStringArrayFromParams(params[2]), params[3], (int[]) followPointer(params[4]), null);
                    break;
                }
                break;
            case 540:
                UI ui6 = (UI) followPointer(params[0]);
                if (ui6 != null) {
                    ui6.setItemStatus((boolean[]) followPointer(params[1]));
                    break;
                }
                break;
            case 541:
                PanelUI panelUI = new PanelUI(params[0], (String) followPointer(params[1]));
                return makeTempObject(panelUI);
            case 543:
                PanelUI panelUI2 = (PanelUI) followPointer(params[0]);
                if (panelUI2 != null) {
                    panelUI2.addNormalData(params[1], params[2], params[3], (String) followPointer(params[4]), params[5], params[6], params[7], params[8]);
                    break;
                }
                break;
            case 544:
                PanelUI panelUI3 = (PanelUI) followPointer(params[0]);
                if (panelUI3 != null) {
                    panelUI3.refresh();
                    break;
                }
                break;
            case 545:
                UI ui7 = (UI) followPointer(params[0]);
                if (ui7 != null) {
                    ui7.remove(params[1]);
                    break;
                }
                break;
            case 546:
                UI ui8 = (UI) followPointer(params[0]);
                if (ui8 != null) {
                    ui8.setCmd((String) followPointer(params[1]), (String) followPointer(params[2]));
                    break;
                }
                break;
            case 547:
                UI ui9 = (UI) followPointer(params[0]);
                if (ui9 != null) {
                    ui9.clear();
                    break;
                }
                break;
            case 548:
                UI ui10 = (UI) followPointer(params[0]);
                if (ui10 != null) {
                    return ui10.isShow() ? 1 : 0;
                }
                break;
            case 549:
                ((Menu) followPointer(params[0])).setItemStatus(params[1], params[2] == 1);
                break;
            case 550:
                Object[] pets = (Object[]) followPointer(params[0]);
                if (pets == null) {
                    ret = new PetDetailUI();
                } else {
                    ret = new PetDetailUI(pets);
                }
                int currTab = params[1];
                int currPet = params[2];
                if (currPet >= 0) {
                    ret.setPageNo(currPet);
                } else {
                    ret.setCurrSelId(-currPet);
                }
                ret.setTabIdx(currTab);
                return makeTempObject(ret);
            case 551:
                UI ui11 = (UI) followPointer(params[0]);
                if (ui11 != null) {
                    return ui11.getClose() ? 1 : 0;
                }
                break;
            case 552:
                TableUI tableUI2 = new TableUI(params[0], (String) followPointer(params[1]), getStringArrayFromParams(params[4]), (int[]) followPointer(params[5]), getStringArrayFromParams(params[2]), (int[]) followPointer(params[3]), params[6]);
                return makeTempObject(tableUI2);
            case 553:
                TableUI tableUI3 = (TableUI) followPointer(params[0]);
                if (tableUI3 != null) {
                    return tableUI3.getTabSelection();
                }
                break;
            case 554:
                Object[] objs2 = (Object[]) followPointer(params[0]);
                Pet[] pets2 = new Pet[objs2.length];
                System.arraycopy(objs2, 0, pets2, 0, pets2.length);
                MatingPanel matingPanel = new MatingPanel(pets2, params[1], (String) followPointer(params[2]));
                return makeTempObject(matingPanel);
            case 555:
                MatingPanel mp = (MatingPanel) followPointer(params[0]);
                if (mp != null) {
                    return makeTempObject(mp.getMatingPetIds());
                }
                break;
            case 556:
                Object[] objs3 = (Object[]) followPointer(params[1]);
                Pet[] pets3 = new Pet[objs3.length];
                System.arraycopy(objs3, 0, pets3, 0, pets3.length);
                ((MatingPanel) followPointer(params[0])).addPets(pets3);
                break;
            case 557:
                ((MatingPanel) followPointer(params[0])).clearSelection();
                break;
            case 558:
                int idx = params[0];
                if (idx < Tool.CLR_TABLE.length) {
                    return Tool.CLR_TABLE[idx];
                }
                return Tool.CLR_TABLE[0];
            case 559:
                TableUI tableUI4 = (TableUI) followPointer(params[0]);
                if (tableUI4 != null) {
                    tableUI4.addTableItem((String) followPointer(params[1]), getStringArrayFromParams(params[2]), params[3], (int[]) followPointer(params[4]), (int[]) followPointer(params[5]));
                    break;
                }
                break;
            case 560:
                return NewStage.screenY;
            case 561:
                Npc.touchingNpc = null;
                Npc.showConnection = false;
                break;
            case 562:
                Npc.showConnection = params[0] == 1;
                break;
            case 563:
                MapLoader.loadFinished = 0;
                break;
            case 564:
                return MapLoader.loadFinished;
            case 565:
                MapLoader mapLoader = new MapLoader((byte[]) followPointer(params[0]), (short) params[1], (short) params[2]);
                break;
            case 566:
                Pet pet14 = CommonData.player.getPet(params[0]);
                if (pet14 != null) {
                    Skill skill6 = pet14.getSkill(params[1]);
                    if (skill6 != null) {
                        return skill6.gridSize;
                    }
                }
                break;
            case 567:
                TableUI tableUI5 = (TableUI) followPointer(params[0]);
                if (tableUI5 != null) {
                    tableUI5.updateCrystal(params[1]);
                    break;
                }
                break;
            case 568:
                NewStage.isMapLoadOk = params[0] == 1;
                break;
            case 569:
                Door door = NewStage.getDoor(params[0]);
                if (door == null) {
                    return makeTempObject("");
                }
                return makeTempObject(door.tipStr);
            case 570:
                return World.drawUI ? 1 : 0;
            case 571:
                MatingPanel panel = (MatingPanel) followPointer(params[0]);
                if (panel != null) {
                    if (params[1] != 1) {
                        panel.stopAni();
                        break;
                    } else {
                        panel.playerAni();
                        break;
                    }
                }
                break;
            case 572:
                return makeTempObject(GetItem.getBattleGetItemMsg());
            case 573:
                return makeTempObject(new ActorUI());
            case 574:
                ActorUI actor = (ActorUI) followPointer(params[0]);
                if (actor != null) {
                    actor.addActorInfo((String) followPointer(params[1]), (byte) params[2], (String) followPointer(params[3]), params[4]);
                    break;
                }
                break;
            case 575:
                ActorUI actor2 = (ActorUI) followPointer(params[0]);
                if (actor2 != null) {
                    return actor2.getActorSelection();
                }
                break;
            case 576:
                ActorUI actor3 = (ActorUI) followPointer(params[0]);
                if (actor3 != null) {
                    actor3.setActorStatus((byte) params[1]);
                    break;
                }
                break;
            case 577:
                ActorUI actor4 = (ActorUI) followPointer(params[0]);
                if (actor4 != null) {
                    actor4.setNewActorName((String) followPointer(params[1]));
                    break;
                }
                break;
            case 578:
                ActorUI actor5 = (ActorUI) followPointer(params[0]);
                if (actor5 != null) {
                    actor5.setActorSelection(params[1]);
                    break;
                }
                break;
            case 579:
                return NewStage.getRequestStageVersion(params[0]);
            case 580:
                Door door2 = NewStage.getDoor(params[0]);
                if (door2 != null) {
                    return door2.targetMap;
                }
                return -1;
            case 581:
                ActorUI actor6 = (ActorUI) followPointer(params[0]);
                if (actor6 != null) {
                    return actor6.getActorSex();
                }
                break;
            case 582:
                Form form7 = (Form) followPointer(params[0]);
                if (form7 != null) {
                    form7.addTip((String) followPointer(params[1]));
                    break;
                }
                break;
            case 583:
                World.addNotifyMsg((String) followPointer(params[0]), params[1], params[2] == 1);
                break;
            case 584:
                return CommonComponent.getUIPanel().getUICount();
            case 585:
                return World.isSupportFileSystem() ? 1 : 0;
            case 586:
                if (World.fullChatUI == null) {
                    ChatUI.open();
                    break;
                }
                break;
            case 587:
                String name4 = (String) followPointer(params[0]);
                if (!name4.equals("") && !name4.equals(CommonData.player.name)) {
                    if (ChatUI.tarList.contains(name4)) {
                        ChatUI.tarList.removeElement(name4);
                    }
                    ChatUI.tarList.insertElementAt(name4, 0);
                    if (params[1] == 1) {
                        ChatUI.instance.setTarPlayer(name4);
                        break;
                    }
                }
                break;
            case 588:
                if (this.owner != null) {
                    this.owner.showMessage((String) followPointer(params[0]), params[1], (String) followPointer(params[2]), params[3], params[4] == 1, params[5] == 1);
                    this.owner.setNonModal();
                    break;
                }
                break;
            case 589:
                GameItem item18 = (GameItem) followPointer(params[0]);
                if (item18 != null) {
                    return item18.hatchPrice;
                }
                break;
            case 590:
                GameItem item19 = (GameItem) followPointer(params[0]);
                String desc = (String) followPointer(params[1]);
                if (item19 != null) {
                    item19.desc = desc;
                    break;
                }
                break;
            case 607:
                GameItem item20 = CommonData.player.findItem(params[0]);
                if (item20 != null) {
                    return item20.petState;
                }
                break;
            case 608:
                GameItem item21 = CommonData.player.findItem(params[0]);
                if (item21 != null) {
                    item21.petState = params[1];
                    break;
                }
                break;
            case 609:
                BlessPanel blessPanel = new BlessPanel(params[0], params[1], (String) followPointer(params[2]));
                return makeTempObject(blessPanel);
            case 610:
                BlessPanel panel2 = (BlessPanel) followPointer(params[0]);
                if (panel2 != null) {
                    panel2.setRateStone(params[1]);
                    break;
                }
                break;
            case 611:
                BlessPanel panel3 = (BlessPanel) followPointer(params[0]);
                if (panel3 != null) {
                    panel3.setAttrStone(params[1]);
                    break;
                }
                break;
            case 612:
                BlessPanel panel4 = (BlessPanel) followPointer(params[0]);
                if (panel4 != null) {
                    return panel4.getAttrCount();
                }
                break;
            case 613:
                BlessPanel panel5 = (BlessPanel) followPointer(params[0]);
                if (panel5 != null) {
                    return panel5.getRateCount();
                }
                break;
            case 614:
                UI.drawDialoguePanel((Graphics) followPointer(params[0]), params[1], params[2], params[3], params[4], Tool.CLR_TABLE[16], Tool.CLR_TABLE[20], params[5], params[6]);
                break;
            case 615:
                return makeTempObject(new PlayerDetailUI());
            case 616:
                BlessPanel panel6 = (BlessPanel) followPointer(params[0]);
                if (panel6 != null) {
                    panel6.clear();
                    break;
                }
                break;
            case 617:
                GameItem item22 = (GameItem) followPointer(params[0]);
                if (item22 != null) {
                    return item22.type;
                }
                break;
            case 618:
                return makeTempObject(new NetPlayerInfoUI(params[0]));
            case 619:
                Battle.autoBattle = (byte) params[0];
                break;
            case 620:
                return Battle.autoBattle;
            case 621:
                UI.drawRectTipPanel((Graphics) followPointer(params[0]), params[1], params[2], params[3], params[4], params[5] == 1);
                break;
            case 622:
                return CommonData.player.level;
            case 623:
                Pet pet15 = Battle.findBattleUnit(params[0]);
                if (pet15 != null) {
                    return pet15.getAttribute((byte) params[1]);
                }
                break;
            case 624:
                if (Battle.battleID != -1) {
                    return Battle.battleFlg;
                }
                break;
            case 625:
                GameItem item23 = (GameItem) followPointer(params[0]);
                if (item23 != null) {
                    return item23.reqLevel;
                }
                return 0;
            case 626:
                if (Utilities.wapURL == null) {
                    return makeTempObject("");
                }
                return makeTempObject(Utilities.wapURL);
            case 627:
                if (Utilities.wapTitle == null) {
                    return makeTempObject("");
                }
                return makeTempObject(Utilities.wapTitle);
            case 628:
                return makeTempObject(Utilities.officialStr);
            case 629:
                UI.drawTipMsgBox((Graphics) followPointer(params[0]), (String) followPointer(params[1]), params[2], params[3]);
                break;
            case 630:
                MatingPanel mp2 = (MatingPanel) followPointer(params[0]);
                if (mp2 == null) {
                    return 0;
                }
                return mp2.getMateInfoId();
            case 631:
                GameItem item24 = (GameItem) followPointer(params[0]);
                if (item24 == null) {
                    return makeTempObject("");
                }
                return makeTempObject(item24.useTip);
            case 632:
                return CommonData.player.isBagFull() ? 1 : 0;
            case 633:
                Pet pet16 = Battle.findBattleUnit(params[0]);
                if (pet16 != null) {
                    return pet16.canCatch ? 1 : 0;
                }
                break;
            case 634:
                TableUI tableUI6 = new TableUI(params[0], (String) followPointer(params[1]), getStringArrayFromParams(params[4]), (int[]) followPointer(params[5]), (Object[]) followPointer(params[3]), params[2], params[6], params[7], params[8]);
                return makeTempObject(tableUI6);
            case 635:
                GameItem item25 = new GameItem();
                item25.load((DataInputStream) followPointer(params[0]));
                return makeTempObject(item25);
            case 636:
                GameItem item26 = (GameItem) followPointer(params[0]);
                if (item26 == null) {
                    return 0;
                }
                return makeTempObject(item26.useTip);
            case 637:
                Object[] items = (Object[]) followPointer(params[2]);
                TableUI table = (TableUI) followPointer(params[0]);
                if (table != null) {
                    Vector vec3 = new Vector();
                    if (items != null) {
                        for (int i6 = 0; i6 < items.length; i6++) {
                            vec3.addElement(items[i6]);
                        }
                        break;
                    }
                    table.setBagInput(params[1], vec3);
                    break;
                }
                break;
            case 638:
                GameItem item27 = (GameItem) followPointer(params[0]);
                if (item27 != null) {
                    return item27.max;
                }
                return 0;
            case 639:
                if (params[0] != 0) {
                    CommonData.player.orderBank();
                    break;
                } else {
                    CommonData.player.orderBag();
                    break;
                }
            case 640:
                if (params[0] == 0) {
                    return CommonData.player.itemBag.size();
                }
                return CommonData.player.bank.size();
            case 641:
                GameItem item28 = CommonData.player.findItemInBank(params[0]);
                if (item28 != null) {
                    return makeTempObject(item28);
                }
                break;
            case 642:
                Chat.addMsg((String) followPointer(params[0]), (String) followPointer(params[1]), params[2], params[3]);
                break;
            case 643:
                return makeTempObject(CommonData.player.getTaskIds());
            case 644:
                Task ret3 = CommonData.player.findTask(params[0]);
                if (ret3 != null) {
                    return makeTempObject(ret3);
                }
                break;
            case 645:
                Task ret4 = CommonData.player.findTask(params[0]);
                if (ret4 != null) {
                    return ret4.level;
                }
                break;
            case 646:
                Task ret5 = CommonData.player.findTask(params[0]);
                if (ret5 != null) {
                    StringBuilder sb = new StringBuilder(String.valueOf(ret5.title));
                    if (ret5.taskState == 3) {
                        str = "(完成)";
                    } else {
                        str = "";
                    }
                    return makeTempObject(sb.append(str).toString());
                }
                break;
            case 647:
                Task ret6 = CommonData.player.findTask(params[0]);
                if (ret6 != null) {
                    return ret6.taskState;
                }
                break;
            case 648:
                Task ret7 = CommonData.player.findTask(params[0]);
                if (ret7 != null) {
                    return makeTempObject(Task.TASK_TYPE_NAME[ret7.type]);
                }
                break;
            case 649:
                Task ret8 = CommonData.player.findTask(params[0]);
                if (ret8 != null) {
                    return makeTempObject(ret8.toBytes());
                }
                break;
            case 650:
                return makeTempObject(CommonData.player.getTaskLevelClr(params[0]));
            case 651:
                PetDetailUI petDetailUI = new PetDetailUI((Pet) followPointer(params[0]), params[1] == 1);
                return makeTempObject(petDetailUI);
            case 652:
                TableUI table2 = (TableUI) followPointer(params[0]);
                if (table2 != null) {
                    table2.setTitles(getStringArrayFromParams(params[1]));
                    break;
                }
                break;
            case 653:
                TableUI table3 = (TableUI) followPointer(params[0]);
                if (table3 != null) {
                    table3.switchBagTipMode();
                    break;
                }
                break;
            case 654:
                return makeTempObject(new int[]{World.pressedx, World.pressedy});
            case 655:
                AbstractUnit unit11 = Tool.getUnit(params[0], params[1]);
                if (unit11 != null) {
                    return unit11.getWidth();
                }
                break;
            case 656:
                Pet unit12 = Battle.findBattleUnit(params[0]);
                if (unit12 != null) {
                    return makeTempObject(unit12.getSelectBounds());
                }
                break;
            case 657:
                StringDraw stringDraw = new StringDraw((String) followPointer(params[0]), params[1], params[2]);
                return makeTempObject(stringDraw);
            case 658:
                StringDraw sd = (StringDraw) followPointer(params[0]);
                if (sd != null) {
                    return sd.length();
                }
                break;
            case 659:
                Graphics g4 = (Graphics) followPointer(params[0]);
                StringDraw sd2 = (StringDraw) followPointer(params[1]);
                if (!(g4 == null || sd2 == null)) {
                    sd2.draw3D(g4, params[2], params[3], params[4], params[5]);
                    break;
                }
            case 660:
                UI.drawSmallPanel((Graphics) followPointer(params[0]), params[1], params[2], params[3], params[4], Tool.CLR_TABLE[14], Tool.CLR_TABLE[18]);
                break;
            case 661:
                Calendar cal = Calendar.getInstance();
                return makeTempObject(String.valueOf(cal.get(1)) + (cal.get(2) + 1));
            case 662:
                return makeTempObject(World.getUpdateData());
            case 663:
                PackageFile packageFile = new PackageFile((byte[]) followPointer(params[0]));
                if (World.uiPackage != null) {
                    World.uiPackage.merge(packageFile);
                    break;
                } else {
                    World.uiPackage = packageFile;
                    break;
                }
            case 664:
                return makeTempObject(new UpGradePanel());
            case 665:
                ((UpGradePanel) followPointer(params[1])).setGameItem(CommonData.player.findItem(params[0]));
                break;
            case 669:
                return makeTempObject(((AbstractLowUI) followPointer(params[0])).getName());
            case 670:
                return makeTempObject(Utilities.downLoadPage);
            case 671:
                com.sumsharp.lowui.TextField tf4 = (com.sumsharp.lowui.TextField) followPointer(params[0]);
                tf4.setListenVM(this);
                if (params[1] != -1) {
                    Tool.openSysInput("", tf4.getText(), params[1], tf4, tf4.getInputMode());
                    break;
                } else {
                    Tool.openSysInput("", tf4.getText(), tf4.getLength(), tf4, tf4.getInputMode());
                    break;
                }
            case 672:
                int i7 = params[0];
                String str10 = (String) followPointer(params[1]);
                String[] stringArrayFromParams = getStringArrayFromParams(params[4]);
                int[] iArr = (int[]) followPointer(params[5]);
                String[] stringArrayFromParams2 = getStringArrayFromParams(params[2]);
                int[] iArr2 = (int[]) followPointer(params[3]);
                int i8 = params[6];
                if (params[7] == 1) {
                    z = true;
                } else {
                    z = false;
                }
                TableUI tableUI7 = new TableUI(i7, str10, stringArrayFromParams, iArr, stringArrayFromParams2, iArr2, i8, z);
                return makeTempObject(tableUI7);
            case 673:
                return makeTempObject(getEquipId(params[0]));
            case 674:
                return makeTempObject(((Pet) followPointer(params[0])).items);
            case 675:
                PetDetailUI petui = (PetDetailUI) followPointer(params[0]);
                if (params[1] != -1) {
                    if (params[1] == 1) {
                        petui.nextPage();
                        break;
                    }
                } else {
                    petui.prevPage();
                    break;
                }
                break;
            case 676:
                return ((PetDetailUI) followPointer(params[0])).getTabIdx();
            case 677:
                Pet pet17 = CommonData.player.getPet(params[0]);
                boolean[] ret9 = new boolean[pet17.items.length];
                if (!pet17.items[0].name.equals("")) {
                    ret9[0] = true;
                }
                if (!pet17.items[1].name.equals("")) {
                    ret9[1] = true;
                }
                if (!pet17.items[2].name.equals("")) {
                    ret9[2] = true;
                }
                if (!pet17.items[3].name.equals("")) {
                    ret9[3] = true;
                }
                if (!pet17.items[4].name.equals("")) {
                    ret9[4] = true;
                }
                if (!pet17.items[5].name.equals("")) {
                    ret9[5] = true;
                }
                return makeTempObject(ret9);
            case 678:
                return CommonData.player.getPet(params[0]).items[params[1]].id;
            case 679:
                Pet pet18 = CommonData.player.getPet(params[0]);
                if (params[3] != 1) {
                    pet18.unloadItem(params[2], params[1]);
                    break;
                } else {
                    pet18.loadItem(params[2], params[1]);
                    break;
                }
            case 680:
                byte[] bytes = (byte[]) followPointer(params[0]);
                if (bytes != null) {
                    GameItem gi = new GameItem();
                    ByteArrayInputStream byteArrayInputStream3 = new ByteArrayInputStream(bytes);
                    gi.load(new DataInputStream(byteArrayInputStream3));
                    return makeTempObject(gi);
                }
                break;
            case 681:
                drawItemInfo((Graphics) followPointer(params[0]), (GameItem) followPointer(params[1]), params[2], params[3], params[4], params[5], params[6], this.owner);
                break;
            case 682:
                Graphics g5 = (Graphics) followPointer(params[0]);
                GameItem gi2 = (GameItem) followPointer(params[1]);
                if (g5 != null) {
                    if (params[5] == 1) {
                        Tool.drawBlurRect(g5, params[2] - 1, params[3] - 1, 40, 40, 1);
                    } else if (params[5] == 3) {
                        Tool.drawBlurRect(g5, params[2] - 1, params[3] - 1, 40, 40, 1);
                        if (gi2 != null) {
                            g5.setColor(Tool.getQuanlityColor(gi2.quanlity));
                        } else {
                            g5.setColor(Tool.CLR_TABLE[2]);
                        }
                        g5.drawRect(params[2] - 1, params[3] - 1, 39, 39);
                        g5.setColor(Tool.CLR_TABLE[11]);
                        g5.drawRect(params[2] - 2, params[3] - 2, 41, 41);
                    }
                }
                if (!(g5 == null || gi2 == null)) {
                    gi2.drawIcon(g5, params[2] + 20, params[3] + 20, 3);
                    if (params[4] == 1) {
                        Tool.drawNumStr(String.valueOf(gi2.count), g5, params[2] + 20, params[3] + 20, 0, 40, -1);
                        break;
                    }
                }
                break;
            case 683:
                GameItem gi3 = (GameItem) followPointer(params[0]);
                if (gi3 != null) {
                    return CommonData.player.findAllItemCount(gi3);
                }
                break;
            case 684:
                PetDetailUI ui12 = (PetDetailUI) followPointer(params[0]);
                if (ui12 != null) {
                    return ui12.getSkillSelection();
                }
                break;
            case 685:
                int[] ret10 = new int[7];
                GameItem gi4 = (GameItem) followPointer(params[0]);
                if (gi4 != null) {
                    for (int i9 = 0; i9 < 7; i9++) {
                        ret10[i9] = gi4.getPetRate(i9);
                    }
                    return makeTempObject(ret10);
                }
                break;
            case 686:
            case 687:
                StringDraw sd3 = (StringDraw) followPointer(params[0]);
                if (sd3 != null) {
                    return sd3.getWidth();
                }
                return -1;
            case 688:
                return ((PanelUI) followPointer(params[0])).getPageSize();
            case 689:
                return ((PanelUI) followPointer(params[0])).getCurPage();
            case 690:
                ((PanelUI) followPointer(params[0])).nextPage();
                break;
            case 691:
                return makeTempObject(new GuildUI());
            case 692:
                ((TableUI) followPointer(params[0])).setTableHeight(params[1]);
                break;
            case 693:
                GuildUI guild = (GuildUI) followPointer(params[0]);
                UWAPSegment segment2 = (UWAPSegment) followPointer(params[1]);
                int id6 = segment2.readInt();
                String name5 = segment2.readString();
                String cr = segment2.readString();
                String lead = segment2.readString();
                short amount = segment2.readShort();
                short amount_m = segment2.readShort();
                short acn = segment2.readShort();
                short acn_m = segment2.readShort();
                short act = segment2.readShort();
                short act_m = segment2.readShort();
                int wo = segment2.readInt();
                int wo_m = segment2.readInt();
                int mi = segment2.readInt();
                int mi_m = segment2.readInt();
                int mo = segment2.readInt();
                int mo_m = segment2.readInt();
                int[] al = segment2.readInts();
                String[] alna = segment2.readStrings();
                int[] en = segment2.readInts();
                String[] enna = segment2.readStrings();
                ByteArrayInputStream byteArrayInputStream4 = new ByteArrayInputStream(segment2.readBytes());
                DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream4);
                byte len = dataInputStream.readByte();
                byte[] buid = new byte[len];
                byte[] bulv = new byte[len];
                for (int i10 = 0; i10 < len; i10++) {
                    buid[i10] = dataInputStream.readByte();
                    bulv[i10] = dataInputStream.readByte();
                }
                guild.updateInfo(id6, name5, cr, lead, amount, amount_m, acn, acn_m, act, act_m, wo, wo_m, mi, mi_m, mo, mo_m, al, alna, en, enna, buid, bulv);
                break;
            case 694:
                return Building.isReady() ? 1 : 0;
            case 695:
                Building.initBuildingMap((UWAPSegment) followPointer(params[0]));
                break;
            case 696:
                return makeTempObject(CommonData.player.guild);
            case 697:
                Network.detectNetwork(this);
                break;
            case 698:
                GuildUI g6 = (GuildUI) followPointer(params[0]);
                if (g6 != null) {
                    g6.refreshUpgradeState();
                    break;
                }
                break;
            case 699:
                ((TableUI) followPointer(params[0])).setTabIdx(params[1]);
                break;
            case 700:
                GameItem g7 = (GameItem) followPointer(params[0]);
                if (g7 != null) {
                    if (g7.prices != null && g7.prices.length != 0) {
                        for (int i11 = 0; i11 < g7.prices.length; i11++) {
                            if (g7.prices[i11].type == 3) {
                                return g7.prices[i11].count;
                            }
                        }
                        break;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean KeyPressed(byte keyCode, byte clear) {
        return Utilities.isKeyPressed(keyCode, clear != 0);
    }

    /* access modifiers changed from: protected */
    public boolean NoKeyPressed() {
        return !Utilities.isAnyKeyPressed();
    }

    /* access modifiers changed from: protected */
    public Object OpenSound(String fileName) {
        try {
            javax.microedition.media.Player p = Manager.createPlayer(new ByteArrayInputStream(World.findResource(fileName, false)), "audio/midi");
            p.prefetch();
            return p;
        } catch (Exception e) {
            Exception exc = e;
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void PlaySound(Object soundObj, int loop) {
        try {
            javax.microedition.media.Player p = (javax.microedition.media.Player) soundObj;
            p.setLoopCount(loop);
            p.start();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    public void CloseSound(Object soundObj) {
        try {
            ((javax.microedition.media.Player) soundObj).close();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    public void Vibra() {
    }

    /* access modifiers changed from: protected */
    public void FillRect(Graphics g, int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
    }

    /* access modifiers changed from: protected */
    public void DrawCircle(Graphics g, int x, int y, int r) {
        g.drawArc(x - r, y - r, r * 2, r * 2, 0, 360);
    }

    /* access modifiers changed from: protected */
    public void DrawString(Graphics g, String str, int x, int y, int anchor) {
        Tool.drawString(g, str, x, y, anchor);
    }

    /* access modifiers changed from: protected */
    public void SetColor(Graphics g, int color) {
        g.setColor(color);
    }

    /* access modifiers changed from: protected */
    public void DrawRect(Graphics g, int x, int y, int width, int height) {
        if (height == 1) {
            g.drawLine(x, y, x + width, y);
        } else if (width == 1) {
            g.drawLine(x, y, x, y + height);
        } else {
            g.drawRect(x, y, width, height);
        }
    }

    /* access modifiers changed from: protected */
    public void SetClip(Graphics g, int x, int y, int width, int height) {
        g.setClip(x, y, width, height);
    }

    /* access modifiers changed from: protected */
    public void FillArc(Graphics g, int x, int y, int width, int height, int sa, int ea) {
        g.fillArc(x, y, width, height, sa, ea);
    }

    /* access modifiers changed from: protected */
    public int GetScreenWidth() {
        return World.viewWidth;
    }

    /* access modifiers changed from: protected */
    public int GetScreenHeight() {
        return World.viewHeight;
    }

    /* access modifiers changed from: protected */
    public int Length(Object o) {
        if (o instanceof boolean[]) {
            return ((boolean[]) o).length;
        }
        if (o instanceof byte[]) {
            return ((byte[]) o).length;
        }
        if (o instanceof short[]) {
            return ((short[]) o).length;
        }
        if (o instanceof int[]) {
            return ((int[]) o).length;
        }
        if (o instanceof Object[]) {
            return ((Object[]) o).length;
        }
        return 1;
    }

    /* access modifiers changed from: protected */
    public byte[] LoadFile(String name) {
        return World.findResource(name, false);
    }

    public static int[] getGameItemBag(int type) {
        Vector vec;
        if (type == 0) {
            vec = CommonData.player.itemBag;
        } else if (type == 500) {
            vec = CommonData.player.bank;
        } else {
            vec = new Vector();
            for (int i = 0; i < CommonData.player.itemBag.size(); i++) {
                GameItem item = (GameItem) CommonData.player.itemBag.elementAt(i);
                if (type > 1000) {
                    if ((item.sellFlag & (type - 1000)) != 0) {
                        vec.addElement(item);
                    }
                } else if (type > 0) {
                    if ((item.useFlag & type) != 0) {
                        vec.addElement(item);
                    }
                } else if (item.type == (-type)) {
                    vec.addElement(item);
                }
            }
        }
        int[] ret = new int[vec.size()];
        for (int i2 = 0; i2 < vec.size(); i2++) {
            ret[i2] = ((GameItem) vec.elementAt(i2)).id;
        }
        return ret;
    }

    public void commandAction(Command cmd, Displayable d) {
        this.lastFormSelection = cmd.getLabel();
        Utilities.display.setCurrent(Utilities.canvas);
        if (isBlock()) {
            continueProcess(1);
        }
    }

    public Object vector_get(Vector vec, int idx) {
        Object obj = vec.elementAt(idx);
        if (obj instanceof Integer) {
            return followPointer(((Integer) obj).intValue());
        }
        return obj;
    }

    public static byte[] loadRMSFile(String dbName) {
        try {
            return new SharedPreferencesHelper(MonsterMIDlet.instance, "db").getData(dbName);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean saveRMSFile(String dbName, byte[] data) {
        if (dbName.equals(NewStage.OTHER_SETTING_DBNAME)) {
            NewStage.setting = new byte[data.length];
            System.arraycopy(data, 0, NewStage.setting, 0, data.length);
        }
        if (dbName.equals(NewStage.DISPLAY_QUANLITY_DBNAME)) {
            NewStage.displayQuanlity = data[0];
            if (data.length > 1) {
                NewStage.useFileConnection = data[1];
            }
        }
        if (dbName.equals(NewStage.NETWORKS_SETTING)) {
            Network.detected = data[0];
        }
        return new SharedPreferencesHelper(MonsterMIDlet.instance, "db").saveData(dbName, data);
    }

    public void deleteRMSFile(String dbName) {
    }

    private int[] getEquipId(int partid) {
        int[] equip = getGameItemBag(-29);
        int[] tmp = new int[equip.length];
        int retIdx = 0;
        for (int i = 0; i < equip.length; i++) {
            tmp[i] = -1;
            GameItem item = CommonData.player.findItem(equip[i]);
            if (item.partId == partid) {
                tmp[retIdx] = item.id;
                retIdx++;
            }
        }
        int max = 0;
        while (max < tmp.length && tmp[max] != -1) {
            max++;
        }
        int[] ret = new int[max];
        System.arraycopy(tmp, 0, ret, 0, max);
        return ret;
    }

    public static void drawItemInfo(Graphics g, GameItem gi, int x, int y, int width, int height, int point, VMUI owner2) {
        boolean showBg;
        int height2;
        if (height != -1) {
            showBg = true;
        } else {
            showBg = false;
        }
        if (g != null && gi != null) {
            if (gi.type == 29) {
                Tip tip = Tip.createTip(gi, width - 10);
                tip.setBounds(x, y, width, -1, false);
                tip.setTipLoaderType(1);
                tip.cycle();
                int triX = tip.getTriX();
                if (showBg) {
                    height2 = tip.getHeight();
                } else {
                    height2 = -1;
                }
                if (width == -1) {
                    width = tip.getWidth();
                }
                if (y + height2 > World.viewHeight) {
                    y -= Utilities.LINE_HEIGHT + height2;
                }
                gi.drawEquipTip(g, x, y, width, height2, triX, point);
                return;
            }
            if (width == -1) {
                if (owner2 != null) {
                    width = owner2.getContentBounds()[2];
                } else {
                    width = World.viewWidth - 20;
                }
            }
            gi.updateTip(x, y, -1, width, height);
            gi.drawTip(g, x, y);
        }
    }
}
