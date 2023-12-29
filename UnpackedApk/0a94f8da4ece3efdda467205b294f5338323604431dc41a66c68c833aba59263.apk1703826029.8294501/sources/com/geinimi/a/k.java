package com.geinimi.a;

import com.geinimi.c.i;

public final class k extends a {
    private String b = null;
    private String c = null;
    private String d = null;

    protected k(String str) {
        super(str);
    }

    public final void a() {
        String str = this.a;
        if (str.indexOf(";") != -1) {
            this.b = str.substring(0, str.indexOf(";"));
            String substring = str.substring(str.indexOf(";") + 1);
            this.c = substring.substring(0, substring.indexOf(";"));
            this.d = substring.substring(substring.indexOf(";") + 1);
            return;
        }
        this.b = this.a;
    }

    public final void b() {
        i.a(this.b, this.c, this.d);
    }
}
