package com.geinimi.ads;

import android.app.Activity;
import android.content.Intent;
import com.geinimi.AdActivity;
import com.geinimi.C0000r;
import com.geinimi.c.k;

public final class o extends Advertisable {
    private String[] b = {"dialogue_title", "dialogue_content", "sms_content"};

    protected o(String str) {
        super(str);
    }

    public final boolean b() {
        k.i();
        return true;
    }

    public final void c() {
        String substring = this.a.substring(C0000r.t.length());
        int indexOf = substring.indexOf(59);
        String substring2 = substring.substring(0, indexOf);
        String substring3 = substring.substring(indexOf + 1);
        int indexOf2 = substring3.indexOf(59);
        String substring4 = substring3.substring(0, indexOf2);
        String substring5 = substring3.substring(indexOf2 + 1);
        a("dialogue_title", substring2);
        a("dialogue_content", substring4);
        a("sms_content", substring5);
    }

    public final int d() {
        return 0;
    }

    public final int e() {
        return 17301629;
    }

    public final Intent f() {
        if (!(!(a() instanceof Activity) || a((String) "dialogue_content") == null || a((String) "sms_content") == null)) {
            a(AdActivity.STR_PARA_SUGGEST_CONTENT_KEY, a((String) "dialogue_content"));
            a(AdActivity.STR_PARA_SUGGEST_SMS_KEY, a((String) "sms_content"));
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
