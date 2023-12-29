package com.geinimi.a;

import com.geinimi.c.i;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class j extends a {
    private String b = null;
    private String c = null;

    protected j(String str) {
        super(str);
    }

    public final void a() {
        String str = null;
        try {
            str = URLDecoder.decode(this.a, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        this.b = str.substring(0, str.indexOf(";"));
        this.c = str.substring(str.indexOf(";") + 1);
    }

    public final void b() {
        i.a(this.b, this.c);
    }
}
