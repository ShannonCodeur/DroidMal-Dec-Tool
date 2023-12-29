package com.geinimi;

import com.geinimi.ads.Advertisable;
import com.geinimi.ads.s;

final class g extends Thread {
    private /* synthetic */ Advertisable a;
    private /* synthetic */ AdActivity b;

    g(AdActivity adActivity, Advertisable advertisable) {
        this.b = adActivity;
        this.a = advertisable;
    }

    public final void run() {
        try {
            if (this.a.b()) {
                this.b.startActivity(this.a.f());
            }
            if (this.a instanceof com.geinimi.ads.g) {
                AdActivity.f = false;
            }
            if (this.a instanceof s) {
                AdActivity.g = false;
            }
        } catch (Exception e) {
        }
    }
}
