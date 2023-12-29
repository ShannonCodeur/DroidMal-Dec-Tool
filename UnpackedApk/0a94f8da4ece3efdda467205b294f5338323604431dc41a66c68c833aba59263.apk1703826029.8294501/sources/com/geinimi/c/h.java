package com.geinimi.c;

import android.location.LocationManager;

final class h extends Thread {
    h() {
    }

    public final void run() {
        ((LocationManager) k.a().getSystemService("location")).requestLocationUpdates("gps", 600000, 0.0f, d.a);
    }
}
