package com.geinimi;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.HashMap;

final class n implements Creator {
    n() {
    }

    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        int readInt2 = parcel.readInt();
        HashMap hashMap = new HashMap();
        AdPushable adPushable = null;
        if (readInt == 1) {
            adPushable = new C0000r(hashMap);
        } else if (readInt == 2) {
            adPushable = new m(hashMap);
        }
        hashMap.put(AdPushable.a, String.valueOf(readInt));
        hashMap.put(AdPushable.b, String.valueOf(readInt2));
        String[] a = adPushable.a();
        for (String put : a) {
            hashMap.put(put, parcel.readString());
        }
        adPushable.a(hashMap);
        if (adPushable != null) {
            adPushable.c = readInt;
            adPushable.d = readInt2;
        }
        adPushable.b(parcel);
        "create Parcelable = " + adPushable;
        return adPushable;
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new AdPushable[i];
    }
}
