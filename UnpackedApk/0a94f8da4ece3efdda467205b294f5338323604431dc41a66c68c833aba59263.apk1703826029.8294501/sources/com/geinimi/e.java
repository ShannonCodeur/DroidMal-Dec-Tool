package com.geinimi;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

final class e extends WebChromeClient {
    private /* synthetic */ AdActivity a;

    e(AdActivity adActivity) {
        this.a = adActivity;
    }

    public final void onCloseWindow(WebView webView) {
    }

    public final void onProgressChanged(WebView webView, int i) {
        this.a.b.setProgress(i);
        super.onProgressChanged(webView, i);
    }

    public final void onReceivedTitle(WebView webView, String str) {
        this.a.setTitle(str);
        super.onReceivedTitle(webView, str);
    }
}
