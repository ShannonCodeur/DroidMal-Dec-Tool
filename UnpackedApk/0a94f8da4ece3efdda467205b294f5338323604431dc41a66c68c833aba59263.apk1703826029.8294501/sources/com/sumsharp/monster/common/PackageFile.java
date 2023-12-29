package com.sumsharp.monster.common;

import com.sumsharp.monster.MonsterMIDlet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;

public class PackageFile {
    private byte[] cache;
    private Hashtable files = new Hashtable();
    private byte mode = 0;
    private String pkgFile;

    public PackageFile() throws IOException {
        String[] files2 = MonsterMIDlet.instance.getAssets().list("GenericMidp2");
        this.mode = 1;
        for (String file : files2) {
            if (file.endsWith(".ui")) {
                try {
                    DataInputStream is = new DataInputStream(new GZIPInputStream(MonsterMIDlet.instance.getAssetStream("/" + file)));
                    is.skip(4);
                    this.files.put("/" + file, new Integer(is.readShort()));
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public PackageFile(byte[] data) throws IOException {
        this.files.clear();
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        in.skip(3);
        int offset = in.readInt();
        short size = in.readShort();
        for (int i = 0; i < size; i++) {
            int version = in.readShort();
            int pos = offset + in.readInt();
            int len = in.readInt();
            DataInputStream in1 = new DataInputStream(new ByteArrayInputStream(data));
            byte[] d = new byte[len];
            in1.skip((long) pos);
            in1.readFully(d);
            this.files.put("/" + in.readUTF(), new Object[]{new Integer(version), d});
        }
    }

    public Hashtable getVersionInfo() {
        Hashtable ret = new Hashtable();
        Enumeration e = this.files.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            ret.put(name, new Integer(getVersion(name)));
        }
        return ret;
    }

    public int getVersion(String name) {
        if (this.mode == 0) {
            Object[] o = (Object[]) this.files.get(name);
            if (o != null) {
                return ((Integer) o[0]).intValue();
            }
            return 0;
        }
        Integer v = (Integer) this.files.get(name);
        if (v == null) {
            return 0;
        }
        return v.intValue();
    }

    /* Debug info: failed to restart local var, previous not found, register: 4 */
    public byte[] getFile(String name) {
        if (this.mode == 0) {
            Object[] o = (Object[]) this.files.get(name);
            if (o == null) {
                return null;
            }
            return (byte[]) o[1];
        } else if (this.mode == 1) {
            return new byte[0];
        } else {
            return null;
        }
    }

    public void releaseFile(String name) {
        this.files.remove(name);
    }

    public void merge(PackageFile pkg) {
        if (this.mode != 1) {
            Enumeration e = pkg.files.keys();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                Object[] os = (Object[]) this.files.get(name);
                Integer v = (Integer) ((Object[]) pkg.files.get(name))[0];
                if (os == null) {
                    this.files.put(name, pkg.files.get(name));
                } else if (v.intValue() > ((Integer) os[0]).intValue()) {
                    this.files.put(name, pkg.files.get(name));
                }
            }
        }
    }

    public byte[] makePackage() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.write(new byte[]{80, 75, 71});
        dos.writeInt(0);
        dos.writeShort(this.files.size());
        int pos = 0;
        Enumeration e = this.files.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            Object[] o = (Object[]) this.files.get(name);
            byte[] data = (byte[]) o[1];
            dos.writeUTF(name);
            dos.writeShort(((Integer) o[0]).intValue());
            dos.writeInt(pos);
            dos.writeInt(data.length);
            pos += data.length;
        }
        Enumeration e2 = this.files.keys();
        while (e2.hasMoreElements()) {
            dos.write((byte[]) ((Object[]) this.files.get((String) e2.nextElement()))[1]);
        }
        int headLength = bos.toByteArray().length;
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        DataOutputStream dos1 = new DataOutputStream(bos1);
        dos1.writeInt(headLength);
        dos1.flush();
        byte[] hl = bos1.toByteArray();
        byte[] ret = bos.toByteArray();
        for (int i = 0; i < hl.length; i++) {
            ret[i + 3] = hl[i];
        }
        return ret;
    }
}
