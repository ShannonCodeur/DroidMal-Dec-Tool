package com.geinimi.ads;

import android.content.Intent;
import com.geinimi.C0000r;

public final class j extends Advertisable {
    private String[] b = {"search_text"};

    protected j(String str) {
        super(str);
    }

    public final boolean b() {
        return true;
    }

    public final void c() {
        a("search_text", this.a.substring(C0000r.k.length()));
    }

    public final int d() {
        return 0;
    }

    public final int e() {
        return 17301600;
    }

    public final Intent f() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.WEB_SEARCH");
        intent.putExtra("query", a((String) "search_text"));
        return intent;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
