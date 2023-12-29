package javax.microedition.io;

import java.io.IOException;

public interface SocketConnection extends StreamConnection {
    public static final byte DELAY = 0;
    public static final byte KEEPALIVE = 2;
    public static final byte LINGER = 1;
    public static final byte RCVBUF = 3;
    public static final byte SNDBUF = 4;

    String getAddress() throws IOException;

    String getLocalAddress() throws IOException;

    int getLocalPort() throws IOException;

    int getPort() throws IOException;

    int getSocketOption(byte b) throws IllegalArgumentException, IOException;

    void setSocketOption(byte b, int i) throws IllegalArgumentException, IOException;
}
