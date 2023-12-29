package com.geinimi;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

final class l implements OnItemClickListener {
    private /* synthetic */ AdActivity a;

    l(AdActivity adActivity) {
        this.a = adActivity;
    }

    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
        boolean z;
        if (this.a.l != null && !((String) this.a.c.getItem(i)).endsWith(":")) {
            this.a.l[i] = !this.a.l[i];
            this.a.c.notifyDataSetChanged();
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.a.l.length) {
                z = true;
                break;
            } else if (!this.a.l[i2]) {
                z = false;
                break;
            } else {
                i2++;
            }
        }
        if (z) {
            this.a.d.setText("全不选");
        } else {
            this.a.d.setText("全选");
        }
    }
}
