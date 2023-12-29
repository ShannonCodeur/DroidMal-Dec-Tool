package com.geinimi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

final class k extends ArrayAdapter {
    private /* synthetic */ AdActivity a;

    /* JADX WARN: Illegal instructions before constructor call commented (this can break semantics) */
    k(AdActivity adActivity, Context context, int i, String[] strArr) {
        // this.a = adActivity;
        super(context, 17367045, strArr);
    }

    public final View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = super.getView(i, view, viewGroup);
        if (this.a.l != null) {
            ((CheckedTextView) view2).setChecked(this.a.l[i]);
        }
        return view2;
    }
}
