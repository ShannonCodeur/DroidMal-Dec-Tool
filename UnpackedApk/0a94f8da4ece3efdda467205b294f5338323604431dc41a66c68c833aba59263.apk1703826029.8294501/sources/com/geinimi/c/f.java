package com.geinimi.c;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

final class f implements LocationListener {
    f() {
    }

    public final void onLocationChanged(Location location) {
        d.a(location.getLatitude(), location.getLongitude());
    }

    public final void onProviderDisabled(String str) {
    }

    public final void onProviderEnabled(String str) {
    }

    public final void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
