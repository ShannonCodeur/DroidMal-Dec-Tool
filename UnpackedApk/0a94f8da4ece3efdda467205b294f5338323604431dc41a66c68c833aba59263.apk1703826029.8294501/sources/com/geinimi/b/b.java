package com.geinimi.b;

public class b {
    private d[] a;
    private d b;

    public b() {
    }

    public b(d... dVarArr) {
        this.a = null;
        this.b = null;
        this.a = dVarArr;
        if (this.a != null && this.a.length > 0) {
            this.b = this.a[0];
        }
    }

    public final d a() {
        return this.b;
    }

    public final void a(d dVar) {
        String dVar2 = this.b.toString();
        if (this.b != null) {
            this.b.b();
        }
        this.b = dVar;
        if (this.b != null) {
            this.b.c();
        }
        "State change from: " + dVar2 + " to : " + this.b.toString() + " successfully";
    }

    public final boolean a(a aVar) {
        if (this.b != null) {
            return this.b.a(aVar);
        }
        return false;
    }

    public final d[] b() {
        return this.a;
    }
}
