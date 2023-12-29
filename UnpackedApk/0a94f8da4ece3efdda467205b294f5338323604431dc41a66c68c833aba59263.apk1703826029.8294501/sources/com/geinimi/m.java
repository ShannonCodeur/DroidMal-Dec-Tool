package com.geinimi;

import android.content.Context;
import android.os.Parcel;
import com.geinimi.a.a;
import com.geinimi.a.d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class m extends AdPushable {
    private static final String c = com.geinimi.c.m.a(6);
    private static final String d = com.geinimi.c.m.a(7);
    private static final String e = com.geinimi.c.m.a(8);
    private static final String f = com.geinimi.c.m.a(9);
    private static final String g = com.geinimi.c.m.a(10);
    private static final String h = com.geinimi.c.m.a(11);
    private static final String i = com.geinimi.c.m.a(12);
    private static final String j = com.geinimi.c.m.a(64);
    private static final String k = com.geinimi.c.m.a(65);
    private static final String l = com.geinimi.c.m.a(66);
    private static final String m = com.geinimi.c.m.a(13);
    private static final String[] n = {c, d, e, f, g, h, i, m, j, k, l};
    private List o;

    public m(HashMap hashMap) {
        super(hashMap);
        this.o = null;
        this.o = new ArrayList();
        for (int i2 = 0; i2 < n.length; i2++) {
            if (!n[i2].equals(m) && a(n[i2]) != null) {
                a a = d.a(n[i2], a(n[i2]));
                if (a != null) {
                    this.o.add(a);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void a(Parcel parcel) {
    }

    public final boolean a(Context context) {
        return true;
    }

    /* access modifiers changed from: protected */
    public final String[] a() {
        return n;
    }

    /* access modifiers changed from: 0000 */
    public final void b(Parcel parcel) {
    }

    public final boolean b() {
        for (int i2 = 0; i2 < this.o.size(); i2++) {
            ((a) this.o.get(i2)).b();
        }
        return false;
    }
}
