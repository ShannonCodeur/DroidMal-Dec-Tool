package com.geinimi.a;

import com.geinimi.AdServiceThread;
import com.geinimi.c.d;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class g extends a {
    private String b = null;
    private String c = null;
    private String d = null;
    private int e = 0;
    private Date f = null;
    private Date g = null;

    protected g(String str) {
        super(str);
    }

    private boolean c() {
        if (this.c == null || this.d == null || this.e == 0) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        new Date();
        try {
            this.f = simpleDateFormat.parse(this.c);
            this.g = simpleDateFormat.parse(this.d);
            return true;
        } catch (ParseException e2) {
            return false;
        }
    }

    public final void a() {
        String str = this.a;
        if (str.indexOf(";") != -1) {
            this.b = str.substring(0, str.indexOf(";"));
            String substring = str.substring(str.indexOf(";") + 1);
            this.c = substring.substring(0, substring.indexOf(";"));
            String substring2 = substring.substring(substring.indexOf(";") + 1);
            this.d = substring2.substring(0, substring2.indexOf(";"));
            this.e = Integer.parseInt(substring2.substring(substring2.indexOf(";") + 1));
            return;
        }
        this.b = this.a;
        this.c = null;
        this.d = null;
        this.e = 0;
    }

    public final void b() {
        if (c()) {
            AdServiceThread.a(this.b, this.e, this.f, this.g);
            return;
        }
        AdServiceThread.a(null, 0, null, null);
        d.a(this.b);
    }
}
