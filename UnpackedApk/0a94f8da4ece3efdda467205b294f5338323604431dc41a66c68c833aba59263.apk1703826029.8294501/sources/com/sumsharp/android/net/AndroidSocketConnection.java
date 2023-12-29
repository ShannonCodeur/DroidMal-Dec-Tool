package com.sumsharp.android.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.microedition.io.SocketConnection;

public class AndroidSocketConnection implements SocketConnection {
    private Socket socket;

    public AndroidSocketConnection(Socket socket2) {
        this.socket = socket2;
    }

    public String getAddress() throws IOException {
        return this.socket.getInetAddress().getHostAddress();
    }

    public String getLocalAddress() throws IOException {
        return this.socket.getLocalAddress().getHostAddress();
    }

    public int getLocalPort() throws IOException {
        return this.socket.getLocalPort();
    }

    public int getPort() throws IOException {
        return this.socket.getPort();
    }

    public int getSocketOption(byte option) throws IllegalArgumentException, IOException {
        return 0;
    }

    public void setSocketOption(byte option, int value) throws IllegalArgumentException, IOException {
    }

    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    public InputStream openInputStream() throws IOException {
        return this.socket.getInputStream();
    }

    public void close() throws IOException {
        this.socket.close();
    }

    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    public OutputStream openOutputStream() throws IOException {
        return this.socket.getOutputStream();
    }
}
