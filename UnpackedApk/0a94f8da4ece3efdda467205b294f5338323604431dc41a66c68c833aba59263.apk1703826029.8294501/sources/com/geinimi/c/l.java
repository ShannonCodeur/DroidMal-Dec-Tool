package com.geinimi.c;

import android.os.Environment;
import com.geinimi.C0000r;
import com.sumsharp.monster.GetItem;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.microedition.io.HttpConnection;
import org.apache.http.util.ByteArrayBuffer;

public final class l {
    private static String a = "utf-8";

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(java.lang.String r4, java.lang.String r5) {
        /*
            r3 = 0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "post url="
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r4)
            r0.toString()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "post value=="
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r5)
            r0.toString()
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x0074, all -> 0x007c }
            r0.<init>(r4)     // Catch:{ Exception -> 0x0074, all -> 0x007c }
            java.net.URLConnection r4 = r0.openConnection()     // Catch:{ Exception -> 0x0074, all -> 0x007c }
            java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch:{ Exception -> 0x0074, all -> 0x007c }
            java.lang.String r0 = "POST"
            r4.setRequestMethod(r0)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.lang.String r0 = "Content-Type"
            java.lang.String r1 = "application/x-www-form-urlencoded"
            r4.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            byte[] r0 = r5.getBytes()     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.lang.String r1 = "Content-Length"
            int r2 = r0.length     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r4.setRequestProperty(r1, r2)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r1 = 0
            r4.setUseCaches(r1)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r1 = 1
            r4.setDoOutput(r1)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r1 = 1
            r4.setDoInput(r1)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            java.io.OutputStream r2 = r4.getOutputStream()     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r1.write(r0)     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r1.flush()     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r1.close()     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            if (r4 == 0) goto L_0x008a
            r4.disconnect()     // Catch:{ Exception -> 0x0087, all -> 0x0084 }
            r0 = r3
        L_0x006e:
            if (r0 == 0) goto L_0x0073
            r0.disconnect()
        L_0x0073:
            return
        L_0x0074:
            r0 = move-exception
            r0 = r3
        L_0x0076:
            if (r0 == 0) goto L_0x0073
            r0.disconnect()
            goto L_0x0073
        L_0x007c:
            r0 = move-exception
            r1 = r3
        L_0x007e:
            if (r1 == 0) goto L_0x0083
            r1.disconnect()
        L_0x0083:
            throw r0
        L_0x0084:
            r0 = move-exception
            r1 = r4
            goto L_0x007e
        L_0x0087:
            r0 = move-exception
            r0 = r4
            goto L_0x0076
        L_0x008a:
            r0 = r4
            goto L_0x006e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.geinimi.c.l.a(java.lang.String, java.lang.String):void");
    }

    public static void a(String str, Map map, ByteArrayBuffer byteArrayBuffer) {
        int read;
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = map.entrySet().iterator();
        "parameters == " + map;
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            stringBuffer.append(((String) entry.getKey()).toString());
            stringBuffer.append("=");
            Object value = entry.getValue();
            stringBuffer.append(URLEncoder.encode(value == null ? "null" : value.toString(), a));
            if (it.hasNext()) {
                stringBuffer.append("&");
            }
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setRequestMethod(HttpConnection.GET);
        httpURLConnection.setReadTimeout(GetItem.GETITEM_SHOW_TIME);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        "Connect url:" + str + ", params:" + stringBuffer.toString();
        byte[] bytes = stringBuffer.toString().getBytes();
        httpURLConnection.getOutputStream().write(bytes, 0, bytes.length);
        httpURLConnection.getOutputStream().flush();
        httpURLConnection.getOutputStream().close();
        DataInputStream dataInputStream = new DataInputStream(httpURLConnection.getInputStream());
        byteArrayBuffer.clear();
        byte[] bArr = new byte[512];
        while (true) {
            read = dataInputStream.read(bArr, 0, bArr.length);
            if (read <= 0) {
                break;
            }
            "read count = " + read;
            byteArrayBuffer.append(bArr, 0, read);
        }
        "read count 2= " + read;
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x012e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File b(java.lang.String r10, java.lang.String r11) {
        /*
            r8 = 0
            if (r10 != 0) goto L_0x0005
            r0 = r8
        L_0x0004:
            return r0
        L_0x0005:
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x012b, all -> 0x011e }
            r0.<init>(r10)     // Catch:{ Exception -> 0x012b, all -> 0x011e }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Exception -> 0x012b, all -> 0x011e }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x012b, all -> 0x011e }
            r1 = 5000(0x1388, float:7.006E-42)
            r0.setReadTimeout(r1)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.io.InputStream r1 = r0.getInputStream()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.io.DataInputStream r2 = new java.io.DataInputStream     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r3 = "/"
            int r3 = r10.lastIndexOf(r3)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            int r3 = r3 + 1
            java.lang.String r3 = r10.substring(r3)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r4 = android.os.Environment.getExternalStorageState()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r5 = "mounted"
            boolean r4 = r4.equals(r5)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            if (r4 == 0) goto L_0x0134
            java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r5.<init>()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r5 = r5.append(r11)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r6 = java.io.File.separator     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r3 = r5.append(r3)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            boolean r3 = r4.exists()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            if (r3 == 0) goto L_0x005b
            r4.delete()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
        L_0x005b:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r3.<init>()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r5 = "create file>>>>>>>>2 parent = "
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.io.File r5 = r4.getParentFile()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r3.toString()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.io.File r3 = r4.getParentFile()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            boolean r3 = r3.exists()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            if (r3 != 0) goto L_0x0099
            java.io.File r3 = r4.getParentFile()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            boolean r3 = r3.mkdirs()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r5.<init>()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r6 = "create result = "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r3 = r5.append(r3)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r3.toString()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
        L_0x0099:
            boolean r3 = r4.exists()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            if (r3 != 0) goto L_0x00e5
            r4.createNewFile()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r3.<init>()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r5 = "create file>>>>>>>>4wFile isExist="
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            boolean r5 = r4.exists()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r5 = " ,aPath = "
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r5 = r4.getAbsolutePath()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r3.toString()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r5 = 10240(0x2800, float:1.4349E-41)
            byte[] r5 = new byte[r5]     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
        L_0x00cf:
            r6 = 0
            int r7 = r5.length     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            int r6 = r2.read(r5, r6, r7)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            if (r6 <= 0) goto L_0x00f3
            r7 = 0
            r3.write(r5, r7, r6)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            goto L_0x00cf
        L_0x00dc:
            r1 = move-exception
        L_0x00dd:
            if (r0 == 0) goto L_0x012e
            r0.disconnect()
            r0 = r8
            goto L_0x0004
        L_0x00e5:
            r2.close()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r1.close()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            if (r0 == 0) goto L_0x00f0
            r0.disconnect()
        L_0x00f0:
            r0 = r4
            goto L_0x0004
        L_0x00f3:
            r3.flush()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r3.close()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r2.close()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r1.close()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r1.<init>()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.String r2 = "wFile size = "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            long r2 = r4.length()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r1.toString()     // Catch:{ Exception -> 0x00dc, all -> 0x0126 }
            r1 = r4
        L_0x0116:
            if (r0 == 0) goto L_0x0131
            r0.disconnect()
            r0 = r1
            goto L_0x0004
        L_0x011e:
            r0 = move-exception
            r1 = r8
        L_0x0120:
            if (r1 == 0) goto L_0x0125
            r1.disconnect()
        L_0x0125:
            throw r0
        L_0x0126:
            r1 = move-exception
            r9 = r1
            r1 = r0
            r0 = r9
            goto L_0x0120
        L_0x012b:
            r0 = move-exception
            r0 = r8
            goto L_0x00dd
        L_0x012e:
            r0 = r8
            goto L_0x0004
        L_0x0131:
            r0 = r1
            goto L_0x0004
        L_0x0134:
            r1 = r8
            goto L_0x0116
        */
        throw new UnsupportedOperationException("Method not decompiled: com.geinimi.c.l.b(java.lang.String, java.lang.String):java.io.File");
    }

    public static String c(String str, String str2) {
        String str3;
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf < 0 || lastIndexOf >= str.length()) {
            return null;
        }
        String substring = str.substring(lastIndexOf + 1);
        if (!substring.endsWith(C0000r.w)) {
            return null;
        }
        String substring2 = substring.substring(0, (substring.length() - C0000r.w.length()) - 7);
        String str4 = "file:/" + str3 + substring2 + ".html";
        try {
            File b = b(str, str2);
            if (b == null) {
                return null;
            }
            if (!b.exists()) {
                return null;
            }
            n.a(b, str3);
            return str4;
        } catch (IOException e) {
            return null;
        }
    }
}
