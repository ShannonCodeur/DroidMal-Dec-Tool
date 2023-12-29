package com.geinimi;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.geinimi.ads.Advertisable;
import com.geinimi.ads.a;
import com.geinimi.ads.g;
import com.geinimi.ads.s;
import com.geinimi.c.k;
import com.geinimi.c.l;
import com.geinimi.c.m;
import java.net.URLDecoder;

final class f extends WebViewClient {
    private /* synthetic */ C0000r a;
    private /* synthetic */ AdActivity b;

    f(AdActivity adActivity, C0000r rVar) {
        this.b = adActivity;
        this.a = rVar;
    }

    public final void onReceivedError(WebView webView, int i, String str, String str2) {
        Toast.makeText(webView.getContext(), "Oh no! " + str, 0).show();
    }

    public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
        C0000r rVar;
        if (str == null) {
            return true;
        }
        try {
            String decode = URLDecoder.decode(str, "UTF-8");
            if (decode.startsWith(C0000r.q)) {
                webView.loadUrl(decode);
                return false;
            }
            String str2 = (String) this.b.a.get(str);
            if (str2 == null) {
                str2 = "0";
            }
            l.a(rVar.c(), m.a(60) + k.f + m.a(43) + k.s + m.a(44) + this.a.e() + m.a(45) + k.b() + m.a(46) + k.c() + m.a(47) + k.d() + m.a(67) + k.e() + m.a(68) + k.f() + m.a(69) + k.g() + m.a(61) + str2);
            "shouldOverrideUrlLoading:" + str;
            Advertisable a2 = a.a(str);
            if (a2 instanceof g) {
                if (!AdActivity.f) {
                    AdActivity.f = true;
                    new g(this.b, a2).start();
                }
            } else if (a2 instanceof s) {
                if (!AdActivity.g) {
                    AdActivity.g = true;
                    new g(this.b, a2).start();
                }
            } else if (a2.b()) {
                this.b.startActivity(a2.f());
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
