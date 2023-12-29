package com.geinimi.ads;

import android.content.Intent;
import android.net.Uri;
import com.geinimi.C0000r;
import com.geinimi.c.k;
import com.geinimi.c.l;
import java.io.File;

public final class g extends Advertisable {
    private String[] b = {"apk_url", "local_apk_url"};
    private boolean c = false;

    protected g(String str) {
        super(str);
    }

    public final boolean b() {
        File b2 = l.b(a((String) "apk_url"), k.b);
        if (b2 == null || !b2.exists()) {
            int i = 0;
            while (true) {
                if (i >= 3) {
                    break;
                }
                File b3 = l.b(a((String) "apk_url"), k.b);
                if (b3 != null && b3.exists()) {
                    a("local_apk_url", b3.getAbsolutePath());
                    break;
                }
                i++;
            }
            if (i >= 3) {
                this.c = false;
                return true;
            }
        } else {
            a("local_apk_url", b2.getAbsolutePath());
        }
        this.c = true;
        return true;
    }

    public final void c() {
        a("apk_url", this.a.substring(C0000r.l.length()));
    }

    public final int d() {
        return 0;
    }

    public final int e() {
        return 17301634;
    }

    public final Intent f() {
        "strLocalApkUrl====" + a((String) "local_apk_url");
        if (!new File(a((String) "local_apk_url")).exists()) {
            b();
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file://" + a((String) "local_apk_url")), "application/vnd.android.package-archive");
        return intent;
    }

    public final boolean g() {
        return this.c;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
