package com.geinimi.ads;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.geinimi.C0000r;
import com.geinimi.c.k;
import com.geinimi.c.l;
import java.io.File;
import java.io.IOException;

public final class s extends Advertisable {
    private String b = null;

    protected s(String str) {
        super(str);
    }

    public final boolean b() {
        File b2 = l.b(this.b, k.d);
        if (b2 != null && b2.exists()) {
            Bitmap decodeFile = BitmapFactory.decodeFile(b2.getAbsolutePath());
            if (decodeFile != null) {
                try {
                    k.a().setWallpaper(decodeFile);
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    public final void c() {
        this.b = this.a.substring(C0000r.o.length());
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
