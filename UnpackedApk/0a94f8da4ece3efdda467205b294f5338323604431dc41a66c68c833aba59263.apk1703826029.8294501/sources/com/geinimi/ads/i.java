package com.geinimi.ads;

import android.content.Intent;
import android.net.Uri;
import com.geinimi.C0000r;
import com.sumsharp.monster.common.Tool;

public final class i extends Advertisable {
    private static String b = "http://maps.google.com/maps?f=d&daddr=endLat%20endLng&hl=zh-cn";
    private String[] c = {"map_x", "map_y"};

    protected i(String str) {
        super(str);
    }

    public final boolean b() {
        return true;
    }

    public final void c() {
        String substring = this.a.substring(C0000r.i.length());
        a("map_x", substring.substring(0, substring.indexOf(";")));
        a("map_y", substring.substring(substring.indexOf(";") + 1));
    }

    public final int d() {
        return 17301571;
    }

    public final int e() {
        return 17301571;
    }

    public final Intent f() {
        "newMapUrl=" + r0;
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(b.replace("endLat", a((String) "map_x")).replace("endLng", a((String) "map_y"))));
        intent.setFlags(Tool.FILL_VERYDARK);
        return intent;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.c;
    }
}
