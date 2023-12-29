package com.geinimi;

import android.view.View;
import android.view.View.OnClickListener;

final class j implements OnClickListener {
    private /* synthetic */ AdActivity a;

    j(AdActivity adActivity) {
        this.a = adActivity;
    }

    public final void onClick(View view) {
        this.a.finish();
    }
}
