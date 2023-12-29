package javax.microedition.io.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.StreamConnection;

public interface FileConnection extends StreamConnection {
    long availableSize();

    boolean canRead();

    boolean canWrite();

    void create() throws IOException;

    void delete() throws IOException;

    long directorySize(boolean z) throws IOException;

    boolean exists();

    long fileSize() throws IOException;

    String getName();

    String getPath();

    String getURL();

    boolean isDirectory();

    boolean isHidden();

    boolean isOpen();

    long lastModified();

    Enumeration list() throws IOException;

    Enumeration list(String str, boolean z) throws IOException;

    void mkdir() throws IOException;

    DataInputStream openDataInputStream() throws IOException;

    DataOutputStream openDataOutputStream() throws IOException;

    InputStream openInputStream() throws IOException;

    OutputStream openOutputStream() throws IOException;

    OutputStream openOutputStream(long j) throws IOException;

    void rename(String str) throws IOException;

    void setFileConnection(String str) throws IOException;

    void setHidden(boolean z) throws IOException;

    void setReadable(boolean z) throws IOException;

    void setWritable(boolean z) throws IOException;

    long totalSize();

    void truncate(long j) throws IOException;

    long usedSize();
}
