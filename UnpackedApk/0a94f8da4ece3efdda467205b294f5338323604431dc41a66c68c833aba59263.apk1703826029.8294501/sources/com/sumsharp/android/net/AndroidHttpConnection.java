package com.sumsharp.android.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.microedition.io.HttpConnection;

public class AndroidHttpConnection implements HttpConnection {
    private HttpURLConnection conn;
    private URL url;

    public AndroidHttpConnection(HttpURLConnection conn2, URL url2) {
        this.conn = conn2;
        this.url = url2;
    }

    public long getDate() throws IOException {
        return this.conn.getDate();
    }

    public long getExpiration() throws IOException {
        return this.conn.getExpiration();
    }

    public String getFile() {
        return this.url.getFile();
    }

    public String getHeaderField(String name) throws IOException {
        return this.conn.getHeaderField(name);
    }

    public String getHeaderField(int n) throws IOException {
        return this.conn.getHeaderField(n);
    }

    public long getHeaderFieldDate(String name, long def) throws IOException {
        return this.conn.getHeaderFieldDate(name, def);
    }

    public int getHeaderFieldInt(String name, int def) throws IOException {
        return 0;
    }

    public String getHeaderFieldKey(int n) throws IOException {
        return this.conn.getHeaderFieldKey(n);
    }

    public String getHost() {
        return this.url.getHost();
    }

    public long getLastModified() throws IOException {
        return this.conn.getLastModified();
    }

    public int getPort() {
        return this.url.getPort();
    }

    public String getProtocol() {
        return this.url.getProtocol();
    }

    public String getQuery() {
        return this.url.getQuery();
    }

    public String getRef() {
        return this.url.getRef();
    }

    public String getRequestMethod() {
        return this.conn.getRequestMethod();
    }

    public String getRequestProperty(String key) {
        return this.conn.getRequestProperty(key);
    }

    public int getResponseCode() throws IOException {
        return this.conn.getResponseCode();
    }

    public String getResponseMessage() throws IOException {
        return this.conn.getResponseMessage();
    }

    public String getURL() {
        return this.url.getPath();
    }

    public void setRequestMethod(String method) throws IOException {
        this.conn.setRequestMethod(method);
    }

    public void setRequestProperty(String key, String value) throws IOException {
        this.conn.setRequestProperty(key, value);
    }

    public String getEncoding() {
        return null;
    }

    public long getLength() {
        return 0;
    }

    public String getType() {
        return null;
    }

    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    public InputStream openInputStream() throws IOException {
        return this.conn.getInputStream();
    }

    public void close() throws IOException {
        this.conn.disconnect();
    }

    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    public OutputStream openOutputStream() throws IOException {
        return this.conn.getOutputStream();
    }
}
