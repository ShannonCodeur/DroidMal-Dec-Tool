package com.sumsharp.android.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import javax.microedition.io.file.FileConnection;

public class AndroidFileConnection implements FileConnection {
    private File file;
    private FileInputStream input;
    private FileOutputStream output;

    public AndroidFileConnection(File file2, FileInputStream input2) {
        this.file = file2;
        this.input = input2;
    }

    public AndroidFileConnection(File file2, FileOutputStream output2) {
        this.file = file2;
        this.output = output2;
    }

    public long availableSize() {
        return 0;
    }

    public boolean canRead() {
        return this.file.canRead();
    }

    public boolean canWrite() {
        return this.file.canWrite();
    }

    public void create() throws IOException {
        this.file.createNewFile();
    }

    public void delete() throws IOException {
        this.file.delete();
    }

    public long directorySize(boolean bool) throws IOException {
        return 0;
    }

    public boolean exists() {
        return this.file.exists();
    }

    public long fileSize() throws IOException {
        return this.file.length();
    }

    public String getName() {
        return this.file.getName();
    }

    public String getPath() {
        return this.file.getPath();
    }

    public String getURL() {
        String url = null;
        try {
            return this.file.toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return url;
        }
    }

    public boolean isDirectory() {
        return this.file.isDirectory();
    }

    public boolean isHidden() {
        return this.file.isHidden();
    }

    public boolean isOpen() {
        return false;
    }

    public long lastModified() {
        return this.file.lastModified();
    }

    public Enumeration list() throws IOException {
        return null;
    }

    public Enumeration list(String string, boolean bool) throws IOException {
        return null;
    }

    public void mkdir() throws IOException {
        this.file.mkdir();
    }

    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    public InputStream openInputStream() throws IOException {
        return this.input;
    }

    public OutputStream openOutputStream() throws IOException {
        return this.output;
    }

    public OutputStream openOutputStream(long l) throws IOException {
        return null;
    }

    public void rename(String string) throws IOException {
        this.file.renameTo(new File(this.file.getPath(), string));
    }

    public void setFileConnection(String string) throws IOException {
    }

    public void setHidden(boolean bool) throws IOException {
    }

    public void setReadable(boolean bool) throws IOException {
    }

    public void setWritable(boolean bool) throws IOException {
    }

    public long totalSize() {
        return 0;
    }

    public void truncate(long l) throws IOException {
    }

    public long usedSize() {
        return 0;
    }

    public void close() throws IOException {
        if (this.input != null) {
            this.input.close();
        }
        if (this.output != null) {
            this.output.close();
        }
        this.input = null;
        this.output = null;
        this.file = null;
    }
}
