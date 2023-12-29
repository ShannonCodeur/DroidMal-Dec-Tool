package com.geinimi.ads;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;

public abstract class Advertisable implements Parcelable {
    public static final Creator CREATOR = new b();
    String a = null;
    private Context b = null;
    /* access modifiers changed from: private */
    public HashMap c = null;

    protected Advertisable(String str) {
        this.a = str;
        this.c = new HashMap();
    }

    public final Context a() {
        return this.b;
    }

    public final String a(String str) {
        return (String) this.c.get(str);
    }

    public final void a(Context context) {
        this.b = context;
    }

    public final void a(String str, String str2) {
        this.c.put(str, str2);
    }

    public abstract boolean b();

    public abstract void c();

    public abstract int d();

    public int describeContents() {
        return 0;
    }

    public abstract int e();

    public abstract Intent f();

    public boolean g() {
        return true;
    }

    /* access modifiers changed from: protected */
    public abstract String[] h();

    public String toString() {
        return this.c == null ? "null" : this.c.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        String[] h = h();
        if (h != null) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < h.length) {
                    parcel.writeString((String) this.c.get(h[i3]));
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        }
    }
}
