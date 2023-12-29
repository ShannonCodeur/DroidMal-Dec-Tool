package com.geinimi.ads;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.geinimi.C0000r;
import com.geinimi.c.j;
import com.geinimi.c.l;
import java.io.File;
import java.net.URLDecoder;

public final class k extends Advertisable {
    private String b = null;
    private String c = null;
    private Bitmap d = null;
    private Advertisable e = null;

    protected k(String str) {
        super(str);
    }

    public final boolean b() {
        try {
            "Download icon = " + this.c;
            if (this.c != null) {
                File b2 = l.b(this.c, com.geinimi.c.k.c);
                "created file = " + b2.getAbsolutePath();
                if (b2 != null && b2.exists()) {
                    this.d = BitmapFactory.decodeFile(b2.getAbsolutePath());
                    b2.delete();
                }
            }
        } catch (Exception e2) {
            this.d = null;
        }
        if (this.d == null) {
            this.d = BitmapFactory.decodeResource(com.geinimi.c.k.a().getResources(), this.e.d());
        }
        Intent f = this.e.f();
        if (this.d != null) {
            f.putExtra("android.intent.extra.shortcut.ICON", this.d);
        }
        j.a(this.b, f, this.d);
        return false;
    }

    public final void c() {
        try {
            "strContent=" + this.a;
            String substring = this.a.substring(C0000r.m.length());
            this.b = URLDecoder.decode(substring.substring(0, substring.indexOf(";")), "UTF-8");
            String substring2 = substring.substring(substring.indexOf(";") + 1);
            "........content=" + substring2;
            this.c = substring2.substring(0, substring2.indexOf(";"));
            this.e = a.a(substring2.substring(substring2.indexOf(";") + 1));
            this.e.a(com.geinimi.c.k.a());
        } catch (Exception e2) {
        }
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
