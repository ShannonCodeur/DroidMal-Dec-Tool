package com.geinimi.b;

import com.geinimi.ads.a;

public final class d {
    private Object a = null;
    private a[] b = null;
    private c c = null;
    private c d = null;

    public d(Object obj, c cVar, c cVar2, a... aVarArr) {
        this.a = obj;
        this.b = aVarArr;
        this.c = null;
        this.d = null;
    }

    public final Object a() {
        return this.a;
    }

    public final boolean a(a aVar) {
        if (this.b != null) {
            for (int i = 0; i < this.b.length; i++) {
                if (aVar.equals(this.b[i].b())) {
                    this.b[i].a();
                    return true;
                }
            }
        }
        return false;
    }

    public final void b() {
        if (this.d != null) {
            "Call:" + this.d.a;
            this.d.a();
        }
    }

    public final void c() {
        if (this.c != null) {
            "Call:" + this.c.a;
            this.c.a();
        }
    }

    public final String toString() {
        return this.a == null ? "null" : this.a.toString();
    }
}
