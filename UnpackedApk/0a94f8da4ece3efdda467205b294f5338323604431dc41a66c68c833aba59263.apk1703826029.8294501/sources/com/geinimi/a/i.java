package com.geinimi.a;

import com.geinimi.AdServiceThread;

public final class i extends a {
    private int b = 0;

    protected i(String str) {
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
        if (this.b != 0) {
            AdServiceThread.a((long) this.b);
        }
    }
}
