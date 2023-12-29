package com.sumsharp.monster.net;

import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.data.AbstractUnit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class UWAPSegment {
    public long asyncSign;
    public byte[] data;
    byte[] dataHeadBuf;
    public byte flag;
    boolean flushed;
    public boolean handled;
    public byte mainType;
    public boolean needResponse;
    public byte paramNum;
    int pos;
    ByteArrayOutputStream segCash;
    DataOutputStream segOut;
    public int serial;
    public byte subType;
    public int timeStamp;

    public static HttpConnection getConnection(String url, boolean proxyFlag) throws IOException {
        String proxyUrl;
        if (proxyFlag) {
            proxyUrl = "10.0.0.172:80";
        } else {
            proxyUrl = null;
        }
        String requestUrl = url;
        String realHost = null;
        if (proxyUrl != null) {
            int ind = url.indexOf(47, 7);
            if (ind >= 0) {
                requestUrl = String.valueOf(url.substring(0, 7)) + proxyUrl + url.substring(ind);
                realHost = url.substring(7, ind);
            }
        }
        HttpConnection conn = null;
        try {
            conn = Connector.open(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (realHost != null) {
            conn.setRequestProperty("X-Online-Host", realHost);
            conn.setRequestProperty("Accept", "*/*");
        }
        return conn;
    }

    public UWAPSegment() {
        this.flag = 0;
        this.needResponse = true;
        this.asyncSign = 0;
        this.mainType = -1;
        this.subType = -1;
        this.paramNum = 0;
        this.dataHeadBuf = new byte[8];
        this.segCash = null;
        this.segOut = null;
        this.flushed = false;
        this.pos = 8;
        try {
            this.segCash = new ByteArrayOutputStream(10);
            this.segOut = new DataOutputStream(this.segCash);
            this.segOut.write(this.dataHeadBuf);
        } catch (IOException e) {
        }
    }

    public UWAPSegment(byte type, byte subType2) {
        this();
        this.mainType = type;
        this.subType = subType2;
    }

    public UWAPSegment(byte[] buf, int startPos, int len) {
        this.flag = 0;
        this.needResponse = true;
        this.asyncSign = 0;
        this.mainType = -1;
        this.subType = -1;
        this.paramNum = 0;
        this.dataHeadBuf = new byte[8];
        this.segCash = null;
        this.segOut = null;
        this.flushed = false;
        this.pos = 8;
        if ((len >>> 24) != 0) {
        }
        int len2 = len & Tool.CLR_ITEM_WHITE;
        this.data = new byte[len2];
        System.arraycopy(buf, startPos, this.data, 0, len2);
        this.flag = (byte) ((int) getNumber(this.data, 0, 1));
        this.mainType = (byte) ((int) getNumber(this.data, 1, 1));
        this.subType = (byte) ((int) getNumber(this.data, 2, 1));
        this.paramNum = this.data[7];
    }

    public UWAPSegment(DataInputStream in) throws Exception {
        this.flag = 0;
        this.needResponse = true;
        this.asyncSign = 0;
        this.mainType = -1;
        this.subType = -1;
        this.paramNum = 0;
        this.dataHeadBuf = new byte[8];
        this.segCash = null;
        this.segOut = null;
        this.flushed = false;
        this.pos = 8;
        this.flag = in.readByte();
        this.mainType = in.readByte();
        this.subType = in.readByte();
        int len = in.readInt();
        if ((len >>> 24) != 0) {
        }
        int len2 = len & Tool.CLR_ITEM_WHITE;
        if (len2 < 0 || len2 > 100000) {
            throw new Exception("Invalid packet: too large segment");
        }
        this.data = new byte[len2];
        try {
            in.readFully(this.data, 7, len2 - 7);
            this.paramNum = this.data[7];
            this.data[0] = this.flag;
            this.data[1] = this.mainType;
            this.data[2] = this.subType;
            setNumber(this.data.length, this.data, 3, 4);
        } catch (EOFException e) {
            EOFException eOFException = e;
            throw new Exception("数据验证错误!");
        }
    }

    public void writeBoolean(boolean b) throws IOException {
        this.segOut.writeByte(2);
        this.segOut.writeByte(b ? 1 : 0);
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeByte(byte b) throws IOException {
        this.segOut.writeByte(1);
        this.segOut.writeByte(b);
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeInt(int n) throws IOException {
        this.segOut.writeByte(4);
        this.segOut.writeByte((byte) (n >> 24));
        this.segOut.writeByte((byte) (n >> 16));
        this.segOut.writeByte((byte) (n >> 8));
        this.segOut.writeByte((byte) n);
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeShort(short shortValue) throws IOException {
        this.segOut.writeByte(3);
        this.segOut.writeByte((byte) (shortValue >> 8));
        this.segOut.writeByte((byte) shortValue);
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeLong(long longValue) throws IOException {
        this.segOut.writeByte(5);
        this.segOut.writeByte((byte) ((int) (longValue >> 56)));
        this.segOut.writeByte((byte) ((int) (longValue >> 48)));
        this.segOut.writeByte((byte) ((int) (longValue >> 40)));
        this.segOut.writeByte((byte) ((int) (longValue >> 32)));
        this.segOut.writeByte((byte) ((int) (longValue >> 24)));
        this.segOut.writeByte((byte) ((int) (longValue >> 16)));
        this.segOut.writeByte((byte) ((int) (longValue >> 8)));
        this.segOut.writeByte((byte) ((int) longValue));
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeString(String s) throws IOException {
        String str;
        this.segOut.writeByte(6);
        DataOutputStream dataOutputStream = this.segOut;
        if (s == null) {
            str = "";
        } else {
            str = s;
        }
        dataOutputStream.writeUTF(str);
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeBooleans(boolean[] b) throws IOException {
        int i;
        this.segOut.writeByte(130);
        if (b == null) {
            this.segOut.writeByte(0);
            this.segOut.writeByte(0);
        } else {
            this.segOut.writeByte((byte) (b.length >> 8));
            this.segOut.writeByte((byte) b.length);
            for (boolean z : b) {
                DataOutputStream dataOutputStream = this.segOut;
                if (z) {
                    i = 1;
                } else {
                    i = 0;
                }
                dataOutputStream.writeByte(i);
            }
        }
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeBytes(byte[] b) throws IOException {
        this.segOut.writeByte(129);
        if (b == null) {
            this.segOut.writeByte(0);
            this.segOut.writeByte(0);
            this.segOut.writeByte(0);
            this.segOut.writeByte(0);
        } else {
            this.segOut.writeByte((byte) (b.length >> 24));
            this.segOut.writeByte((byte) (b.length >> 16));
            this.segOut.writeByte((byte) (b.length >> 8));
            this.segOut.writeByte((byte) b.length);
            for (byte writeByte : b) {
                this.segOut.writeByte(writeByte);
            }
        }
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeInts(int[] n) throws IOException {
        this.segOut.writeByte(132);
        if (n == null) {
            this.segOut.writeByte(0);
            this.segOut.writeByte(0);
        } else {
            this.segOut.writeByte((byte) (n.length >> 8));
            this.segOut.writeByte((byte) n.length);
            for (int i = 0; i < n.length; i++) {
                this.segOut.writeByte((byte) (n[i] >> 24));
                this.segOut.writeByte((byte) (n[i] >> 16));
                this.segOut.writeByte((byte) (n[i] >> 8));
                this.segOut.writeByte((byte) n[i]);
            }
        }
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeLongs(long[] longArray) throws IOException {
        this.segOut.writeByte(133);
        if (longArray == null) {
            this.segOut.writeByte(0);
            this.segOut.writeByte(0);
        } else {
            this.segOut.writeByte((byte) (longArray.length >> 8));
            this.segOut.writeByte((byte) longArray.length);
            for (int i = 0; i < longArray.length; i++) {
                this.segOut.writeByte((byte) ((int) (longArray[i] >> 56)));
                this.segOut.writeByte((byte) ((int) (longArray[i] >> 48)));
                this.segOut.writeByte((byte) ((int) (longArray[i] >> 40)));
                this.segOut.writeByte((byte) ((int) (longArray[i] >> 32)));
                this.segOut.writeByte((byte) ((int) (longArray[i] >> 24)));
                this.segOut.writeByte((byte) ((int) (longArray[i] >> 16)));
                this.segOut.writeByte((byte) ((int) (longArray[i] >> 8)));
                this.segOut.writeByte((byte) ((int) longArray[i]));
            }
        }
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeShorts(short[] shortArray) throws IOException {
        this.segOut.writeByte(131);
        if (shortArray == null) {
            this.segOut.writeByte(0);
            this.segOut.writeByte(0);
        } else {
            this.segOut.writeByte((byte) (shortArray.length >> 8));
            this.segOut.writeByte((byte) shortArray.length);
            for (int i = 0; i < shortArray.length; i++) {
                this.segOut.writeByte((byte) (shortArray[i] >> 8));
                this.segOut.writeByte((byte) shortArray[i]);
            }
        }
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void writeStrings(Object[] s) throws IOException {
        this.segOut.writeByte(134);
        if (s == null) {
            this.segOut.writeByte(0);
            this.segOut.writeByte(0);
        } else {
            this.segOut.writeByte((byte) (s.length >> 8));
            this.segOut.writeByte((byte) s.length);
            for (int i = 0; i < s.length; i++) {
                this.segOut.writeUTF(s[i] == null ? "" : s[i]);
            }
        }
        this.paramNum = (byte) (this.paramNum + 1);
    }

    public void flush() {
        if (!this.flushed) {
            this.flushed = true;
            try {
                this.segOut.flush();
            } catch (Exception e) {
            }
            this.data = this.segCash.toByteArray();
            try {
                this.segOut.close();
            } catch (IOException e2) {
            }
            this.segCash = null;
            this.segOut = null;
            this.data[0] = this.flag;
            this.data[1] = this.mainType;
            this.data[2] = this.subType;
            this.data[7] = this.paramNum;
            setNumber(this.data.length, this.data, 3, 4);
        }
    }

    public void reset() {
        this.pos = 8;
    }

    public byte getNextParaType() {
        if (this.pos >= this.data.length - 1) {
            return -1;
        }
        return this.data[this.pos];
    }

    public boolean readBoolean() {
        this.pos += 2;
        return (this.data[this.pos - 1] & 1) == 1;
    }

    public byte readByte() {
        this.pos += 2;
        return this.data[this.pos - 1];
    }

    public char readChar() {
        this.pos += 3;
        return (char) ((int) getNumber(this.data, this.pos - 2, 2));
    }

    public int readInt() {
        this.pos += 5;
        return (int) getNumber(this.data, this.pos - 4, 4);
    }

    public long readLong() {
        this.pos += 9;
        return getNumber(this.data, this.pos - 8, 8);
    }

    public short readShort() {
        this.pos += 3;
        return (short) ((int) getNumber(this.data, this.pos - 2, 2));
    }

    public String readString() {
        byte md = this.data[this.pos];
        this.pos++;
        return readUTFString(md);
    }

    /* access modifiers changed from: 0000 */
    public String readUTFString(byte md) {
        try {
            int len = ((int) getNumber(this.data, this.pos, 2)) + 2;
            int start = this.pos;
            this.pos += len;
            if (md == 6) {
                return new DataInputStream(new ByteArrayInputStream(this.data, start, len)).readUTF();
            }
            this.pos += 2;
            int chars = (len - 4) / 2;
            if (chars == 0) {
                return "";
            }
            char[] carr = new char[chars];
            int cindex = 0;
            int i = start + 4;
            while (cindex < chars) {
                carr[cindex] = (char) ((this.data[i] & 255) + ((this.data[i + 1] & 255) << 8));
                cindex++;
                i += 2;
            }
            return new String(carr);
        } catch (Exception e) {
            Exception exc = e;
            return "";
        }
    }

    public boolean[] readBooleans() {
        this.pos++;
        int len = (int) getNumber(this.data, this.pos, 2);
        this.pos += 2;
        boolean[] ret = new boolean[len];
        for (int i = 0; i < len; i++) {
            byte[] bArr = this.data;
            int i2 = this.pos;
            this.pos = i2 + 1;
            ret[i] = (bArr[i2] & 1) == 1;
        }
        return ret;
    }

    public byte[] readBytes() {
        this.pos++;
        int len = (int) getNumber(this.data, this.pos, 4);
        this.pos += 4;
        byte[] ret = new byte[len];
        System.arraycopy(this.data, this.pos, ret, 0, len);
        this.pos += len;
        return ret;
    }

    public int[] readInts() {
        this.pos++;
        int len = (int) getNumber(this.data, this.pos, 2);
        this.pos += 2;
        int[] ret = new int[len];
        int i = 0;
        while (i < len) {
            ret[i] = ((this.data[this.pos] & 255) << 24) | ((this.data[this.pos + 1] & 255) << 16) | ((this.data[this.pos + 2] & 255) << 8) | (this.data[this.pos + 3] & 255);
            i++;
            this.pos += 4;
        }
        return ret;
    }

    public long[] readLongs() {
        this.pos++;
        int len = (int) getNumber(this.data, this.pos, 2);
        this.pos += 2;
        long[] ret = new long[len];
        int i = 0;
        while (i < len) {
            ret[i] = (long) (((this.data[this.pos] & 255) << 56) | ((this.data[this.pos + 1] & 255) << 48) | ((this.data[this.pos + 2] & 255) << 40) | ((this.data[this.pos + 3] & 255) << 32) | ((this.data[this.pos + 4] & 255) << 24) | ((this.data[this.pos + 5] & 255) << 16) | ((this.data[this.pos + 6] & 255) << 8) | (this.data[this.pos + 7] & 255));
            i++;
            this.pos += 8;
        }
        return ret;
    }

    public short[] readShorts() {
        this.pos++;
        int len = (int) getNumber(this.data, this.pos, 2);
        this.pos += 2;
        short[] ret = new short[len];
        int i = 0;
        while (i < len) {
            ret[i] = (short) (((this.data[this.pos] & 255) << 8) | (this.data[this.pos + 1] & 255));
            i++;
            this.pos += 2;
        }
        return ret;
    }

    public String[] readStrings() {
        byte md = (byte) (this.data[this.pos] & Byte.MAX_VALUE);
        this.pos++;
        int len = (int) getNumber(this.data, this.pos, 2);
        this.pos += 2;
        String[] ret = new String[len];
        for (int i = 0; i < len; i++) {
            ret[i] = readUTFString(md);
        }
        return ret;
    }

    public void skip(int i) {
        while (i > 0) {
            byte[] bArr = this.data;
            int i2 = this.pos;
            this.pos = i2 + 1;
            switch (bArr[i2]) {
                case -125:
                    this.pos += (((int) getNumber(this.data, this.pos, 2)) * 2) + 2;
                    break;
                case -124:
                    this.pos += (((int) getNumber(this.data, this.pos, 2)) * 4) + 2;
                    break;
                case -123:
                    this.pos += (((int) getNumber(this.data, this.pos, 2)) * 8) + 2;
                    break;
                case -121:
                case -120:
                    int len = (int) getNumber(this.data, this.pos, 2);
                    for (int j = 0; j < len; j++) {
                        this.pos += ((int) getNumber(this.data, this.pos, 2)) + 2;
                    }
                    break;
                case 1:
                case 2:
                    this.pos++;
                    break;
                case 3:
                    this.pos += 2;
                    break;
                case 4:
                    this.pos += 4;
                    break;
                case 5:
                    this.pos += 8;
                    break;
                case 7:
                case 8:
                case 17:
                case 18:
                    this.pos += ((int) getNumber(this.data, this.pos, 2)) + 2;
                    break;
            }
            i--;
        }
    }

    public static long getNumber(byte[] buf, int off, int len) {
        long longVal = 0;
        for (int i = 0; i < len; i++) {
            longVal = (longVal << 8) | ((long) (buf[off + i] & 255));
        }
        return longVal;
    }

    public static void setNumber(int num, byte[] buf, int off, int len) {
        for (int i = len - 1; i >= 0; i--) {
            buf[off + i] = (byte) (num & AbstractUnit.CLR_NAME_TAR);
            num >>= 8;
        }
    }

    public void release() {
        this.data = null;
        try {
            if (this.segOut != null) {
                this.segOut.close();
            }
        } catch (Exception e) {
        }
        this.segCash = null;
        this.segOut = null;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UWAPSegment)) {
            return false;
        }
        return ((UWAPSegment) obj).asyncSign == this.asyncSign;
    }

    public boolean isErrorPacket() {
        return (this.flag & 1) != 0;
    }
}
