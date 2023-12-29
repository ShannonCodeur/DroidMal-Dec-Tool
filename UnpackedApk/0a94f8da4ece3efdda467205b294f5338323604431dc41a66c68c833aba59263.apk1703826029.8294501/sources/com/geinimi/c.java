package com.geinimi;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class c implements OnClickListener {
    private /* synthetic */ AdActivity a;

    c(AdActivity adActivity) {
        this.a = adActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.finish();
    }
}
