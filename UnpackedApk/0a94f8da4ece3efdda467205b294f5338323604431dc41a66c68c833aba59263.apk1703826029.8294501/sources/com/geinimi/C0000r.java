package com.geinimi;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import com.geinimi.ads.Advertisable;
import com.geinimi.ads.a;
import com.geinimi.c.m;
import com.sumsharp.monster.common.Tool;
import java.util.HashMap;

/* renamed from: com.geinimi.r  reason: case insensitive filesystem */
public final class C0000r extends AdPushable {
    public static final String c = m.a(14);
    public static final String d = m.a(15);
    public static final String e = m.a(16);
    public static final String f = m.a(13);
    public static final String g = m.a(18);
    public static final String h = m.a(19);
    public static final String i = m.a(20);
    public static final String j = m.a(21);
    public static final String k = m.a(22);
    public static final String l = m.a(23);
    public static final String m = m.a(24);
    public static final String n = m.a(25);
    public static final String o = m.a(26);
    public static final String p = m.a(27);
    public static final String q = m.a(28);
    public static final String r = m.a(29);
    public static final String s = m.a(30);
    public static final String t = m.a(58);
    public static final String u = m.a(59);
    public static final String v = m.a(62);
    public static final String w = m.a(31);
    private static String x = m.a(17);
    private static final String[] z = {c, d, e, x, f};
    private Advertisable y;

    public C0000r(HashMap hashMap) {
        super(hashMap);
        this.y = null;
        this.y = a.a(a(x));
    }

    /* access modifiers changed from: 0000 */
    public final void a(Parcel parcel) {
        parcel.writeParcelable(this.y, 0);
    }

    public final boolean a(Context context) {
        if (this.y != null) {
            this.y.a(context);
            Intent f2 = this.y.f();
            if (f2 == null) {
                return false;
            }
            f2.addFlags(Tool.FILL_VERYDARK);
            context.startActivity(f2);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final String[] a() {
        return z;
    }

    /* access modifiers changed from: 0000 */
    public final void b(Parcel parcel) {
        this.y = (Advertisable) parcel.readParcelable(Advertisable.class.getClassLoader());
        "gAdvertisable ===" + this.y;
    }

    public final boolean b() {
        "Advertisement prepareAction gAdvertisable = " + this.y;
        if (this.y != null) {
            return this.y.b();
        }
        return false;
    }

    public final Advertisable f() {
        return this.y;
    }

    public final int g() {
        if (this.y != null) {
            return this.y.e();
        }
        return 17301647;
    }

    public final boolean h() {
        if (this.y != null) {
            return this.y.g();
        }
        return false;
    }
}
