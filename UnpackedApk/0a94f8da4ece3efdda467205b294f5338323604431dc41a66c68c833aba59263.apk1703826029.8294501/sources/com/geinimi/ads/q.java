package com.geinimi.ads;

import android.content.Intent;
import android.widget.Toast;
import com.geinimi.C0000r;
import com.geinimi.c.k;

public final class q extends Advertisable {
    static Toast b = null;
    public static r c = new r();
    static String d = null;
    private String e = null;

    protected q(String str) {
        super(str);
    }

    public final boolean b() {
        return false;
    }

    public final void c() {
        this.e = this.a.substring(C0000r.r.length());
        if (this.e != null) {
            d = this.e;
            k.a(k.a(), "TOAST", d);
        }
        "strToastString=" + this.e;
    }

    public final int d() {
        return 0;
    }

    public final int e() {
        return 0;
    }

    public final Intent f() {
        return null;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return null;
    }
}
