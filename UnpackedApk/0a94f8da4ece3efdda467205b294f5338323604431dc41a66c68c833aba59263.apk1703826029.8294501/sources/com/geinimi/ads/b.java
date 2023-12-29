package com.geinimi.ads;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class b implements Creator {
    b() {
    }

    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        Advertisable a = a.a(parcel.readString());
        String[] h = a.h();
        if (h != null) {
            for (String put : h) {
                a.c.put(put, parcel.readString());
            }
        }
        return a;
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new Advertisable[i];
    }
}
