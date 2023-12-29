package com.geinimi.custom;

import com.geinimi.AdService;
import com.geinimi.ads.q;
import com.geinimi.c.k;

public class AdShow {
    public static void show() {
        AdService adService = (AdService) k.a();
        if (adService != null) {
            adService.a(q.c);
        }
    }
}
