package com.geinimi.ads;

import android.content.ContentValues;
import android.content.Intent;
import android.provider.Browser;
import com.geinimi.C0000r;
import com.geinimi.c.k;
import java.util.Date;

public final class c extends Advertisable {
    private String[] b = {"bookmark_title", "bookmark_url"};

    protected c(String str) {
        super(str);
    }

    public final boolean b() {
        String a = a((String) "bookmark_title");
        String a2 = a((String) "bookmark_url");
        ContentValues contentValues = new ContentValues();
        contentValues.put("bookmark", Integer.valueOf(1));
        contentValues.put("created", Long.valueOf(new Date().getTime()));
        contentValues.put("date", Long.valueOf(new Date().getTime()));
        contentValues.put("visits", Integer.valueOf(1));
        contentValues.put("title", a);
        contentValues.put("url", a2);
        k.a().getContentResolver().insert(Browser.BOOKMARKS_URI, contentValues);
        return false;
    }

    public final void c() {
        String substring = this.a.substring(C0000r.p.length());
        a("bookmark_title", substring.substring(0, substring.indexOf(";")));
        a("bookmark_url", substring.substring(substring.indexOf(";") + 1));
    }

    public final int d() {
        return 0;
    }

    public final int e() {
        return 0;
    }

    public final Intent f() {
        return null;
    }

    /* access modifiers changed from: protected */
    public final String[] h() {
        return this.b;
    }
}
