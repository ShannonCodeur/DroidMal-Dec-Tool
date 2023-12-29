package com.geinimi.c;

import android.content.Intent;
import android.graphics.Bitmap;

public final class j {
    public static final String a = m.a(32);
    public static final String b = m.a(33);

    static {
        m.a(34);
    }

    public static void a(String str, Intent intent, Bitmap bitmap) {
        Intent intent2 = new Intent(a.a);
        intent2.putExtra("android.intent.extra.shortcut.NAME", str);
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent2.putExtra("android.intent.extra.shortcut.ICON", bitmap);
        k.a().sendBroadcast(intent2);
    }
}
