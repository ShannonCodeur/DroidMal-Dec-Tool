package com.geinimi.ads;

import com.geinimi.C0000r;
import com.geinimi.b.c;

public class a {
    private com.geinimi.b.a a;
    private c b;

    public a() {
    }

    public a(com.geinimi.b.a aVar, c cVar) {
        this.a = null;
        this.a = aVar;
        this.b = cVar;
    }

    public static Advertisable a(String str) {
        Advertisable advertisable;
        if (str == null) {
            return null;
        }
        "createAdvertisable=" + str;
        if (str.startsWith(C0000r.q)) {
            advertisable = new d(str);
        } else if (str.startsWith(C0000r.g)) {
            advertisable = new e(str);
        } else if (str.startsWith(C0000r.h)) {
            advertisable = new h(str);
        } else if (str.startsWith(C0000r.i)) {
            advertisable = new i(str);
        } else if (str.startsWith(C0000r.j)) {
            advertisable = new m(str);
        } else if (str.startsWith(C0000r.l)) {
            advertisable = new g(str);
        } else if (str.startsWith(C0000r.k)) {
            advertisable = new j(str);
        } else if (str.startsWith(C0000r.n)) {
            advertisable = new f(str);
        } else if (str.startsWith(C0000r.p)) {
            advertisable = new c(str);
        } else if (str.startsWith(C0000r.o)) {
            advertisable = new s(str);
        } else if (str.startsWith(C0000r.m)) {
            advertisable = new k(str);
        } else if (str.startsWith(C0000r.q)) {
            advertisable = new d(str);
        } else if (str.startsWith(C0000r.r)) {
            advertisable = new q(str);
        } else if (str.startsWith(C0000r.s)) {
            advertisable = new n(str);
        } else if (str.startsWith(C0000r.t)) {
            advertisable = new o(str);
        } else if (str.startsWith(C0000r.u)) {
            advertisable = new l(str);
        } else if (str.startsWith(C0000r.v)) {
            advertisable = new p(str);
        } else {
            "Can't find advertisable as content : " + str;
            advertisable = null;
        }
        if (advertisable == null) {
            return advertisable;
        }
        advertisable.c();
        return advertisable;
    }

    public void a() {
        if (this.b != null) {
            this.b.a();
        }
    }

    public com.geinimi.b.a b() {
        return this.a;
    }
}
