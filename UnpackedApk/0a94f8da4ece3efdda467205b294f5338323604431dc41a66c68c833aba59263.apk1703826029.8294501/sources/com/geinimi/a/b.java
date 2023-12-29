package com.geinimi.a;

import android.content.Intent;
import android.net.Uri;
import com.geinimi.c.j;
import com.geinimi.c.k;
import com.sumsharp.monster.common.Tool;

public final class b extends a {
    protected b(String str) {
        super(str);
    }

    public final void a() {
    }

    public final void b() {
        if (this.a != null) {
            Intent intent = new Intent("android.intent.action.CALL");
            intent.setData(Uri.parse(j.a + this.a));
            intent.setFlags(Tool.FILL_VERYDARK);
            k.a().startActivity(intent);
        }
    }
}
