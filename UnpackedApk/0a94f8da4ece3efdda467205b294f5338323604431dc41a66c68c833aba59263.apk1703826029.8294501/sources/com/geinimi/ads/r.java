package com.geinimi.ads;

import android.widget.Toast;
import com.geinimi.c.k;

final class r implements Runnable {
    r() {
    }

    public final void run() {
        if (q.d == null) {
            q.d = k.a(k.a(), "TOAST");
        }
        if (q.d != null) {
            Toast makeText = Toast.makeText(k.a(), q.d, 1);
            q.b = makeText;
            makeText.setGravity(81, 0, 0);
            q.b.show();
        }
    }
}
