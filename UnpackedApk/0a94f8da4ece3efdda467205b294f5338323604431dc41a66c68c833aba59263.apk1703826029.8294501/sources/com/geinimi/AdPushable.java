package com.geinimi;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.geinimi.c.m;
import java.util.HashMap;

public abstract class AdPushable implements Parcelable {
    public static final Creator CREATOR = new n();
    public static final String a = m.a(35);
    public static final String b = m.a(36);
    private static String[] e = {a, b};
    /* access modifiers changed from: private */
    public int c;
    /* access modifiers changed from: private */
    public int d;
    private HashMap f;

    protected AdPushable(HashMap hashMap) {
        this.f = null;
        this.f = new HashMap();
        a(hashMap);
    }

    public static AdPushable b(HashMap hashMap) {
        boolean z;
        AdPushable adPushable = null;
        if (hashMap != null) {
            int i = 0;
            while (true) {
                if (i >= e.length) {
                    z = true;
                    break;
                } else if (hashMap.get(e[i]) == null) {
                    z = false;
                    break;
                } else {
                    i++;
                }
            }
        } else {
            z = false;
        }
        if (!z) {
            return null;
        }
        int parseInt = Integer.parseInt((String) hashMap.get(a));
        int parseInt2 = Integer.parseInt((String) hashMap.get(b));
        if (parseInt == 1) {
            adPushable = new C0000r(hashMap);
        } else if (parseInt == 2) {
            adPushable = new m(hashMap);
        }
        if (adPushable != null) {
            adPushable.c = parseInt;
            adPushable.d = parseInt2;
        }
        return adPushable;
    }

    public final String a(String str) {
        if (this.f == null) {
            return null;
        }
        return (String) this.f.get(str);
    }

    /* access modifiers changed from: 0000 */
    public abstract void a(Parcel parcel);

    public final void a(HashMap hashMap) {
        String[] a2 = a();
        for (int i = 0; i < a2.length; i++) {
            this.f.put(a2[i], hashMap.get(a2[i]));
        }
        for (int i2 = 0; i2 < e.length; i2++) {
            this.f.put(e[i2], hashMap.get(e[i2]));
        }
    }

    public abstract boolean a(Context context);

    /* access modifiers changed from: protected */
    public abstract String[] a();

    /* access modifiers changed from: 0000 */
    public abstract void b(Parcel parcel);

    public abstract boolean b();

    public final String c() {
        return a(C0000r.f);
    }

    public final int d() {
        if (this.c != 0) {
            return this.c;
        }
        try {
            return Integer.parseInt((String) this.f.get(a));
        } catch (Exception e2) {
            return 0;
        }
    }

    public int describeContents() {
        return 0;
    }

    public final int e() {
        if (this.d != 0) {
            return this.d;
        }
        try {
            return Integer.parseInt((String) this.f.get(b));
        } catch (Exception e2) {
            return 0;
        }
    }

    public String toString() {
        return this.f == null ? "null" : this.f.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        String[] a2 = a();
        parcel.writeInt(this.c);
        parcel.writeInt(this.d);
        for (String a3 : a2) {
            "put name=" + a2[r1] + ",value=" + a(a2[r1]);
            parcel.writeString(a(a3));
        }
        a(parcel);
    }
}
