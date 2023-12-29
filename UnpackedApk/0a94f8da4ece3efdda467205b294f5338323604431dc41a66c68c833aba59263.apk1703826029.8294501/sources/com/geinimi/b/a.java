package com.geinimi.b;

public final class a {
    private Object a = null;

    public a(Object obj) {
        this.a = obj;
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof a)) {
            return false;
        }
        return ((a) obj).a.equals(this.a);
    }
}
