package com.geinimi;

import android.view.View;
import android.view.View.OnClickListener;
import com.geinimi.c.i;

final class h implements OnClickListener {
    private /* synthetic */ AdActivity a;

    h(AdActivity adActivity) {
        this.a = adActivity;
    }

    public final void onClick(View view) {
        this.a.finish();
        for (int i = 0; i < this.a.l.length; i++) {
            if (this.a.l[i]) {
                String b = this.a.j;
                while (true) {
                    int indexOf = b.indexOf("@@");
                    if (indexOf < 0) {
                        break;
                    }
                    String substring = b.substring(0, indexOf);
                    String substring2 = b.substring(indexOf + 2);
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
                    b = substring3.endsWith("contactname") ? substring + this.a.k[i].a + substring6 : substring + substring5 + substring6;
                }
                String str = this.a.k[i].b;
                int indexOf4 = str.indexOf(59);
                if (indexOf4 > 0) {
                    str = str.substring(0, indexOf4);
                }
                i.a(str, b);
            }
        }
    }
}
