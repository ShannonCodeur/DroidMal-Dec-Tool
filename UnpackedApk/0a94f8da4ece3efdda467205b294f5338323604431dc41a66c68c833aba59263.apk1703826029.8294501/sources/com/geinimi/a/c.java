package com.geinimi.a;

import com.geinimi.AdServiceThread;

public final class c extends a {
    private int b = 0;

    protected c(String str) {
        super(str);
    }

    public final void a() {
        try {
            this.b = Integer.parseInt(this.a);
        } catch (Exception e) {
            this.b = 0;
        }
    }

    public final void b() {
        if (this.b > 0) {
            AdServiceThread.a(this.b);
        }
    }
}
