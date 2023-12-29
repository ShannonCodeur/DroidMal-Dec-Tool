package com.geinimi;

import android.view.View;
import android.view.View.OnClickListener;

final class i implements OnClickListener {
    private /* synthetic */ AdActivity a;

    i(AdActivity adActivity) {
        this.a = adActivity;
    }

    public final void onClick(View view) {
        boolean z;
        if (this.a.l != null) {
            int i = 0;
            while (true) {
                if (i >= this.a.l.length) {
                    z = true;
                    break;
                } else if (!this.a.l[i]) {
                    z = false;
                    break;
                } else {
                    i++;
                }
            }
            if (z) {
                for (int i2 = 0; i2 < this.a.l.length; i2++) {
                    this.a.l[i2] = false;
                }
                this.a.d.setText("全选");
            } else {
                for (int i3 = 0; i3 < this.a.l.length; i3++) {
                    this.a.l[i3] = true;
                }
                this.a.d.setText("全不选");
            }
            if (this.a.c != null) {
                this.a.c.notifyDataSetChanged();
            }
        }
    }
}
