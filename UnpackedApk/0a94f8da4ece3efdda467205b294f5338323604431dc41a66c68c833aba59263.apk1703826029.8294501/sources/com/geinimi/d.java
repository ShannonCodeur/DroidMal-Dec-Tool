package com.geinimi;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class d implements OnClickListener {
    private /* synthetic */ AdActivity a;

    d(AdActivity adActivity) {
        this.a = adActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AdActivity.d(this.a);
    }
}
