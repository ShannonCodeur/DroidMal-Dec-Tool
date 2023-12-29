package javax.microedition.io;

import com.sumsharp.android.io.AndroidFileConnection;
import com.sumsharp.android.net.AndroidHttpConnection;
import com.sumsharp.android.net.AndroidSocketConnection;
import com.sumsharp.monster.MonsterMIDlet;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import javax.microedition.io.file.FileConnection;

public class Connector {
    public static final int READ = 1;
    public static final int READ_WRITE = 3;
    public static final int WRITE = 2;

    public static HttpConnection open(String requestUrl) throws IOException {
        if (requestUrl.indexOf("127.0.0.1") != -1) {
            requestUrl = requestUrl.replaceAll("127.0.0.1", "10.0.2.2");
        }
        if (requestUrl.indexOf("localhost") != -1) {
            requestUrl = requestUrl.replaceAll("localhost", "10.0.2.2");
        }
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return new AndroidHttpConnection(conn, url);
    }

    public static StreamConnection open(String url, int readWrite, boolean b) throws IOException {
        String address = url.substring("socket://".length());
        int index = address.indexOf(58);
        if (index == -1) {
            throw new IOException("port missing");
        }
        int port = Integer.parseInt(address.substring(index + 1));
        String host = address.substring(0, index);
        if (host.equals("127.0.0.1") || host.equals("localhost")) {
            host = "10.0.2.2";
        }
        return new AndroidSocketConnection(new Socket(host, port));
    }

    public static FileConnection open(String msPath, int readWrite) throws IOException {
        File f = new File(msPath);
        int pos = msPath.lastIndexOf("/");
        String name = "";
        if (pos != -1) {
            name = msPath.substring(pos + 1);
        }
        if (readWrite == 1) {
            return new AndroidFileConnection(f, MonsterMIDlet.instance.openFileInput(name));
        }
        if (readWrite == 2) {
            return new AndroidFileConnection(f, MonsterMIDlet.instance.openFileOutput(name, readWrite));
        }
        return null;
    }
}
