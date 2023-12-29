package com.geinimi;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

final class b implements OnCancelListener {
    private /* synthetic */ AdActivity a;

    b(AdActivity adActivity) {
        this.a = adActivity;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.a.finish();
    }
}
