package com.geinimi;

import android.app.NotificationManager;
import com.geinimi.ads.a;
import com.geinimi.b.b;
import com.geinimi.b.c;
import com.geinimi.b.d;
import com.geinimi.c.k;
import java.util.Date;
import org.apache.http.util.ByteArrayBuffer;

public class AdServiceThread extends Thread {
    private static int a = 150000;
    private static int i = 0;
    private static String j = null;
    private static int k = 0;
    private static Date l = null;
    private static Date m = null;
    private static long n = 0;
    private static long o = 0;
    private int b = 0;
    private AdService c = null;
    private boolean d = true;
    private b e = null;
    private ByteArrayBuffer f = new ByteArrayBuffer(1024);
    private AdPushable g = null;
    private NotificationManager h = null;

    public AdServiceThread(AdService adService) {
        this.c = adService;
        this.h = (NotificationManager) adService.getSystemService("notification");
        this.e = new b(new d(q.START, null, null, new a(new com.geinimi.b.a(p.ACTION_START_OK), new c("processSTARTAction", this))), new d(q.IDLE, null, null, new a(new com.geinimi.b.a(p.ACTION_IDLE_TO_DOWNLOAD), new c("processIDLEAction", this))), new d(q.DOWNLOAD, null, null, new a(new com.geinimi.b.a(p.ACTION_DOWNLOAD_OK), new c("processDOWNLOAD_OK_Action", this)), new a(new com.geinimi.b.a(p.ACTION_DOWNLOAD_FAILUE), new c("processDOWNLOAD_FAILUE_Action", this))), new d(q.PARSE, null, null, new a(new com.geinimi.b.a(p.ACTION_PARSE_OK), new c("processPARSE_OK_Action", this)), new a(new com.geinimi.b.a(p.ACTION_PARSE_FAILUE), new c("processPARSE_FAILUE_Action", this))), new d(q.TRANSACT, null, null, new a(new com.geinimi.b.a(p.ACTION_TRANSACT_OK), new c("processTRANSACT_OK_Action", this)), new a(new com.geinimi.b.a(p.ACTION_TRANSACT_FAILUE), new c("processTRANSACT_FAILUE_Action", this))));
        n = 0;
        o = 0;
        try {
            n = Long.parseLong(k.a(k.a(), "SKIP_START_TIME"));
            o = Long.parseLong(k.a(k.a(), "SKIP_TIME"));
        } catch (Exception e2) {
        }
        try {
            a = Integer.parseInt(k.a(k.a(), "POOL_TIME_EVERY_ONCE"));
        } catch (Exception e3) {
        }
        com.geinimi.c.d.a(a);
        a(p.ACTION_START_OK);
    }

    public static void a(int i2) {
        a = i2;
        k.a(k.a(), "POOL_TIME_EVERY_ONCE", "" + a);
    }

    public static void a(long j2) {
        o = 60 * j2 * 1000;
        n = System.currentTimeMillis();
        k.a(k.a(), "SKIP_START_TIME", "" + n);
        k.a(k.a(), "SKIP_TIME", "" + o);
    }

    private void a(p pVar) {
        this.e.a(new com.geinimi.b.a(pVar));
    }

    private void a(q qVar) {
        d[] b2 = this.e.b();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < b2.length) {
                if (((q) b2[i3].a()).equals(qVar)) {
                    this.e.a(b2[i3]);
                }
                i2 = i3 + 1;
            } else {
                return;
            }
        }
    }

    public static void a(String str, int i2, Date date, Date date2) {
        j = str;
        i = i2;
        l = date;
        m = date2;
    }

    private void processDOWNLOAD_FAILUE_Action() {
        a(q.IDLE);
    }

    private void processDOWNLOAD_OK_Action() {
        a(q.PARSE);
    }

    private void processIDLEAction() {
        a(q.DOWNLOAD);
    }

    private void processPARSE_FAILUE_Action() {
        a(q.IDLE);
    }

    private void processPARSE_OK_Action() {
        a(q.TRANSACT);
    }

    private void processSTARTAction() {
        a(q.IDLE);
    }

    private void processTRANSACT_FAILUE_Action() {
        a(q.IDLE);
    }

    private void processTRANSACT_OK_Action() {
        a(q.IDLE);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        java.lang.Thread.sleep(5000);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0029, code lost:
        if (java.lang.System.currentTimeMillis() >= (n + o)) goto L_0x0081;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002b, code lost:
        java.lang.Thread.sleep(o);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r6 = this;
            java.lang.String r0 = ""
        L_0x0002:
            boolean r0 = r6.d
            if (r0 == 0) goto L_0x038a
            com.geinimi.b.b r0 = r6.e
            com.geinimi.b.d r0 = r0.a()
            java.lang.Object r0 = r0.a()
            com.geinimi.q r0 = (com.geinimi.q) r0
            int[] r1 = com.geinimi.o.a
            int r0 = r0.ordinal()
            r0 = r1[r0]
            switch(r0) {
                case 1: goto L_0x0002;
                case 2: goto L_0x001e;
                case 3: goto L_0x0097;
                case 4: goto L_0x019c;
                case 5: goto L_0x01c8;
                default: goto L_0x001d;
            }
        L_0x001d:
            goto L_0x0002
        L_0x001e:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x0087 }
            long r2 = n     // Catch:{ InterruptedException -> 0x0087 }
            long r4 = o     // Catch:{ InterruptedException -> 0x0087 }
            long r2 = r2 + r4
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 >= 0) goto L_0x0081
            long r0 = o     // Catch:{ InterruptedException -> 0x0087 }
            java.lang.Thread.sleep(r0)     // Catch:{ InterruptedException -> 0x0087 }
        L_0x0030:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x007f }
            long r2 = n     // Catch:{ Exception -> 0x007f }
            long r4 = o     // Catch:{ Exception -> 0x007f }
            long r2 = r2 + r4
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x001e
            int r0 = r6.b     // Catch:{ Exception -> 0x007f }
            int r0 = r0 + 5000
            r6.b = r0     // Catch:{ Exception -> 0x007f }
            int r0 = i     // Catch:{ Exception -> 0x007f }
            if (r0 == 0) goto L_0x0070
            java.lang.String r0 = j     // Catch:{ Exception -> 0x007f }
            if (r0 == 0) goto L_0x0070
            java.util.Date r0 = l     // Catch:{ Exception -> 0x007f }
            if (r0 == 0) goto L_0x0070
            java.util.Date r0 = m     // Catch:{ Exception -> 0x007f }
            if (r0 == 0) goto L_0x0070
            int r0 = k     // Catch:{ Exception -> 0x007f }
            int r0 = r0 + 5000
            k = r0     // Catch:{ Exception -> 0x007f }
            int r1 = i     // Catch:{ Exception -> 0x007f }
            if (r0 < r1) goto L_0x0070
            r0 = 0
            k = r0     // Catch:{ Exception -> 0x007f }
            java.util.Date r0 = new java.util.Date     // Catch:{ Exception -> 0x007f }
            r0.<init>()     // Catch:{ Exception -> 0x007f }
            java.util.Date r1 = m     // Catch:{ Exception -> 0x007f }
            boolean r1 = r0.after(r1)     // Catch:{ Exception -> 0x007f }
            if (r1 == 0) goto L_0x0089
            r0 = 0
            i = r0     // Catch:{ Exception -> 0x007f }
        L_0x0070:
            int r0 = r6.b     // Catch:{ Exception -> 0x007f }
            int r1 = a     // Catch:{ Exception -> 0x007f }
            if (r0 < r1) goto L_0x0002
            r0 = 0
            r6.b = r0     // Catch:{ Exception -> 0x007f }
            com.geinimi.p r0 = com.geinimi.p.ACTION_IDLE_TO_DOWNLOAD     // Catch:{ Exception -> 0x007f }
            r6.a(r0)     // Catch:{ Exception -> 0x007f }
            goto L_0x0002
        L_0x007f:
            r0 = move-exception
            goto L_0x0002
        L_0x0081:
            r0 = 5000(0x1388, double:2.4703E-320)
            java.lang.Thread.sleep(r0)     // Catch:{ InterruptedException -> 0x0087 }
            goto L_0x0030
        L_0x0087:
            r0 = move-exception
            goto L_0x0030
        L_0x0089:
            java.util.Date r1 = l     // Catch:{ Exception -> 0x007f }
            boolean r0 = r0.after(r1)     // Catch:{ Exception -> 0x007f }
            if (r0 == 0) goto L_0x0070
            java.lang.String r0 = j     // Catch:{ Exception -> 0x007f }
            com.geinimi.c.d.a(r0)     // Catch:{ Exception -> 0x007f }
            goto L_0x0070
        L_0x0097:
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Exception -> 0x016f }
            r0.<init>()     // Catch:{ Exception -> 0x016f }
            r1 = 70
            java.lang.String r1 = com.geinimi.c.m.a(r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r2 = com.geinimi.c.k.f     // Catch:{ Exception -> 0x016f }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x016f }
            r1 = 71
            java.lang.String r1 = com.geinimi.c.m.a(r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r2 = com.geinimi.c.k.s     // Catch:{ Exception -> 0x016f }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x016f }
            r1 = 72
            java.lang.String r1 = com.geinimi.c.m.a(r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r2 = com.geinimi.c.k.b()     // Catch:{ Exception -> 0x016f }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x016f }
            r1 = 73
            java.lang.String r1 = com.geinimi.c.m.a(r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r2 = com.geinimi.c.k.c()     // Catch:{ Exception -> 0x016f }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x016f }
            r1 = 74
            java.lang.String r1 = com.geinimi.c.m.a(r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r2 = com.geinimi.c.k.d()     // Catch:{ Exception -> 0x016f }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x016f }
            r1 = 75
            java.lang.String r1 = com.geinimi.c.m.a(r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r2 = com.geinimi.c.k.e()     // Catch:{ Exception -> 0x016f }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x016f }
            r1 = 76
            java.lang.String r1 = com.geinimi.c.m.a(r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r2 = com.geinimi.c.k.f()     // Catch:{ Exception -> 0x016f }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x016f }
            r1 = 77
            java.lang.String r1 = com.geinimi.c.m.a(r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r2 = com.geinimi.c.k.g()     // Catch:{ Exception -> 0x016f }
            r0.put(r1, r2)     // Catch:{ Exception -> 0x016f }
            r1 = 2
            double[] r1 = new double[r1]     // Catch:{ Exception -> 0x016f }
            com.geinimi.c.d.a(r1)     // Catch:{ Exception -> 0x016f }
            r2 = 78
            java.lang.String r2 = com.geinimi.c.m.a(r2)     // Catch:{ Exception -> 0x016f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x016f }
            r3.<init>()     // Catch:{ Exception -> 0x016f }
            java.lang.String r4 = ""
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x016f }
            r4 = 0
            r4 = r1[r4]     // Catch:{ Exception -> 0x016f }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x016f }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x016f }
            r0.put(r2, r3)     // Catch:{ Exception -> 0x016f }
            r2 = 79
            java.lang.String r2 = com.geinimi.c.m.a(r2)     // Catch:{ Exception -> 0x016f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x016f }
            r3.<init>()     // Catch:{ Exception -> 0x016f }
            java.lang.String r4 = ""
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x016f }
            r4 = 1
            r4 = r1[r4]     // Catch:{ Exception -> 0x016f }
            java.lang.StringBuilder r1 = r3.append(r4)     // Catch:{ Exception -> 0x016f }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x016f }
            r0.put(r2, r1)     // Catch:{ Exception -> 0x016f }
            java.lang.String r1 = com.geinimi.c.k.h()     // Catch:{ Exception -> 0x016f }
            org.apache.http.util.ByteArrayBuffer r2 = r6.f     // Catch:{ Exception -> 0x016f }
            com.geinimi.c.l.a(r1, r0, r2)     // Catch:{ Exception -> 0x016f }
            org.apache.http.util.ByteArrayBuffer r0 = r6.f     // Catch:{ Exception -> 0x016f }
            byte[] r0 = r0.toByteArray()     // Catch:{ Exception -> 0x016f }
            byte[] r0 = com.geinimi.c.m.a(r0)     // Catch:{ Exception -> 0x016f }
            org.apache.http.util.ByteArrayBuffer r1 = r6.f     // Catch:{ Exception -> 0x016f }
            r1.clear()     // Catch:{ Exception -> 0x016f }
            org.apache.http.util.ByteArrayBuffer r1 = r6.f     // Catch:{ Exception -> 0x016f }
            r2 = 0
            int r3 = r0.length     // Catch:{ Exception -> 0x016f }
            r1.append(r0, r2, r3)     // Catch:{ Exception -> 0x016f }
            if (r0 == 0) goto L_0x0168
            int r0 = r0.length     // Catch:{ Exception -> 0x016f }
            if (r0 != 0) goto L_0x0195
        L_0x0168:
            com.geinimi.p r0 = com.geinimi.p.ACTION_DOWNLOAD_FAILUE     // Catch:{ Exception -> 0x016f }
            r6.a(r0)     // Catch:{ Exception -> 0x016f }
            goto L_0x0002
        L_0x016f:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018d }
            r1.<init>()     // Catch:{ Exception -> 0x018d }
            java.lang.String r2 = "Download Error:"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x018d }
            java.lang.String r0 = r0.getMessage()     // Catch:{ Exception -> 0x018d }
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Exception -> 0x018d }
            r0.toString()     // Catch:{ Exception -> 0x018d }
            com.geinimi.p r0 = com.geinimi.p.ACTION_DOWNLOAD_FAILUE     // Catch:{ Exception -> 0x018d }
            r6.a(r0)     // Catch:{ Exception -> 0x018d }
            goto L_0x0002
        L_0x018d:
            r0 = move-exception
            com.geinimi.p r0 = com.geinimi.p.ACTION_DOWNLOAD_FAILUE
            r6.a(r0)
            goto L_0x0002
        L_0x0195:
            com.geinimi.p r0 = com.geinimi.p.ACTION_DOWNLOAD_OK     // Catch:{ Exception -> 0x016f }
            r6.a(r0)     // Catch:{ Exception -> 0x016f }
            goto L_0x0002
        L_0x019c:
            java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x01b8 }
            org.apache.http.util.ByteArrayBuffer r1 = r6.f     // Catch:{ Exception -> 0x01b8 }
            byte[] r1 = r1.toByteArray()     // Catch:{ Exception -> 0x01b8 }
            r0.<init>(r1)     // Catch:{ Exception -> 0x01b8 }
            java.util.HashMap r0 = com.geinimi.c.o.a(r0)     // Catch:{ Exception -> 0x01b8 }
            com.geinimi.AdPushable r0 = com.geinimi.AdPushable.b(r0)     // Catch:{ Exception -> 0x01b8 }
            r6.g = r0     // Catch:{ Exception -> 0x01b8 }
            com.geinimi.p r0 = com.geinimi.p.ACTION_PARSE_OK     // Catch:{ Exception -> 0x01b8 }
            r6.a(r0)     // Catch:{ Exception -> 0x01b8 }
            goto L_0x0002
        L_0x01b8:
            r0 = move-exception
            com.geinimi.p r0 = com.geinimi.p.ACTION_PARSE_FAILUE     // Catch:{ Exception -> 0x01c0 }
            r6.a(r0)     // Catch:{ Exception -> 0x01c0 }
            goto L_0x0002
        L_0x01c0:
            r0 = move-exception
            com.geinimi.p r0 = com.geinimi.p.ACTION_PARSE_FAILUE
            r6.a(r0)
            goto L_0x0002
        L_0x01c8:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02ab }
            r0.<init>()     // Catch:{ Exception -> 0x02ab }
            java.lang.String r1 = "Begin Transact pushable = "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Exception -> 0x02ab }
            com.geinimi.AdPushable r1 = r6.g     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Exception -> 0x02ab }
            r0.toString()     // Catch:{ Exception -> 0x02ab }
            com.geinimi.AdPushable r0 = r6.g     // Catch:{ Exception -> 0x02ab }
            if (r0 == 0) goto L_0x0383
            com.geinimi.AdPushable r0 = r6.g     // Catch:{ Exception -> 0x02ab }
            boolean r0 = r0.b()     // Catch:{ Exception -> 0x02ab }
            if (r0 == 0) goto L_0x02b3
            com.geinimi.AdPushable r0 = r6.g     // Catch:{ Exception -> 0x02ab }
            boolean r0 = r0 instanceof com.geinimi.C0000r     // Catch:{ Exception -> 0x02ab }
            if (r0 == 0) goto L_0x02a4
            com.geinimi.AdPushable r0 = r6.g     // Catch:{ Exception -> 0x02ab }
            com.geinimi.r r0 = (com.geinimi.C0000r) r0     // Catch:{ Exception -> 0x02ab }
            boolean r0 = r0.h()     // Catch:{ Exception -> 0x02ab }
            if (r0 == 0) goto L_0x02a4
            com.geinimi.AdPushable r0 = r6.g     // Catch:{ Exception -> 0x02ab }
            com.geinimi.r r0 = (com.geinimi.C0000r) r0     // Catch:{ Exception -> 0x02ab }
            android.app.Notification r1 = new android.app.Notification     // Catch:{ Exception -> 0x02ab }
            int r2 = r0.g()     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.C0000r.c     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = r0.a(r3)     // Catch:{ Exception -> 0x02ab }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x02ab }
            r1.<init>(r2, r3, r4)     // Catch:{ Exception -> 0x02ab }
            r2 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x038b }
            r3.<init>()     // Catch:{ ClassNotFoundException -> 0x038b }
            java.lang.String r4 = "com.geinimi.custom.Ad"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ ClassNotFoundException -> 0x038b }
            java.lang.String r4 = com.geinimi.AdService.b()     // Catch:{ ClassNotFoundException -> 0x038b }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ ClassNotFoundException -> 0x038b }
            java.lang.String r4 = "_"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ ClassNotFoundException -> 0x038b }
            java.lang.String r4 = com.geinimi.AdService.c()     // Catch:{ ClassNotFoundException -> 0x038b }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ ClassNotFoundException -> 0x038b }
            java.lang.String r3 = r3.toString()     // Catch:{ ClassNotFoundException -> 0x038b }
            java.lang.Class r2 = java.lang.Class.forName(r3)     // Catch:{ ClassNotFoundException -> 0x038b }
        L_0x0239:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02ab }
            r3.<init>()     // Catch:{ Exception -> 0x02ab }
            java.lang.String r4 = "advertisement = "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r4 = " ,gAdservice = "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x02ab }
            com.geinimi.AdService r4 = r6.c     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x02ab }
            r3.toString()     // Catch:{ Exception -> 0x02ab }
            android.content.Intent r3 = new android.content.Intent     // Catch:{ Exception -> 0x02ab }
            com.geinimi.AdService r4 = r6.c     // Catch:{ Exception -> 0x02ab }
            r3.<init>(r4, r2)     // Catch:{ Exception -> 0x02ab }
            com.geinimi.ads.Advertisable r2 = r0.f()     // Catch:{ Exception -> 0x02ab }
            boolean r2 = r2 instanceof com.geinimi.ads.p     // Catch:{ Exception -> 0x02ab }
            if (r2 == 0) goto L_0x038e
            android.content.Intent r2 = new android.content.Intent     // Catch:{ Exception -> 0x02ab }
            r2.<init>()     // Catch:{ Exception -> 0x02ab }
        L_0x026b:
            com.geinimi.c.n.a(r0)     // Catch:{ Exception -> 0x02ab }
            android.os.Bundle r3 = new android.os.Bundle     // Catch:{ Exception -> 0x02ab }
            r3.<init>()     // Catch:{ Exception -> 0x02ab }
            java.lang.String r4 = "ACTIVITY_PARAM_KEY"
            r3.putParcelable(r4, r0)     // Catch:{ Exception -> 0x02ab }
            r2.putExtras(r3)     // Catch:{ Exception -> 0x02ab }
            com.geinimi.AdService r3 = r6.c     // Catch:{ Exception -> 0x02ab }
            int r4 = r0.e()     // Catch:{ Exception -> 0x02ab }
            r5 = 134217728(0x8000000, float:3.85186E-34)
            android.app.PendingIntent r2 = android.app.PendingIntent.getActivity(r3, r4, r2, r5)     // Catch:{ Exception -> 0x02ab }
            com.geinimi.AdService r3 = r6.c     // Catch:{ Exception -> 0x02ab }
            java.lang.String r4 = com.geinimi.C0000r.d     // Catch:{ Exception -> 0x02ab }
            java.lang.String r4 = r0.a(r4)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r5 = com.geinimi.C0000r.e     // Catch:{ Exception -> 0x02ab }
            java.lang.String r5 = r0.a(r5)     // Catch:{ Exception -> 0x02ab }
            r1.setLatestEventInfo(r3, r4, r5, r2)     // Catch:{ Exception -> 0x02ab }
            r2 = 1
            r1.defaults = r2     // Catch:{ Exception -> 0x02ab }
            android.app.NotificationManager r2 = r6.h     // Catch:{ Exception -> 0x02ab }
            int r0 = r0.e()     // Catch:{ Exception -> 0x02ab }
            r2.notify(r0, r1)     // Catch:{ Exception -> 0x02ab }
        L_0x02a4:
            com.geinimi.p r0 = com.geinimi.p.ACTION_TRANSACT_OK     // Catch:{ Exception -> 0x02ab }
            r6.a(r0)     // Catch:{ Exception -> 0x02ab }
            goto L_0x0002
        L_0x02ab:
            r0 = move-exception
            com.geinimi.p r0 = com.geinimi.p.ACTION_TRANSACT_FAILUE
            r6.a(r0)
            goto L_0x0002
        L_0x02b3:
            com.geinimi.AdPushable r0 = r6.g     // Catch:{ Exception -> 0x02ab }
            boolean r0 = r0 instanceof com.geinimi.C0000r     // Catch:{ Exception -> 0x02ab }
            if (r0 == 0) goto L_0x02bf
            com.geinimi.AdPushable r0 = r6.g     // Catch:{ Exception -> 0x02ab }
            com.geinimi.c.n.a(r0)     // Catch:{ Exception -> 0x02ab }
            goto L_0x02a4
        L_0x02bf:
            com.geinimi.AdPushable r0 = r6.g     // Catch:{ Exception -> 0x02ab }
            java.lang.String r1 = r0.c()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02ab }
            r2.<init>()     // Catch:{ Exception -> 0x02ab }
            r3 = 42
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.c.k.f     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 43
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.c.k.s     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 45
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.c.k.b()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 46
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.c.k.c()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 47
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.c.k.d()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 67
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.c.k.e()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 68
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.c.k.f()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 69
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r3 = com.geinimi.c.k.g()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 44
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            int r3 = r0.e()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            r3 = 48
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x02ab }
            int r0 = r0.d()     // Catch:{ Exception -> 0x02ab }
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ Exception -> 0x02ab }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x02ab }
            com.geinimi.c.l.a(r1, r0)     // Catch:{ Exception -> 0x02ab }
            goto L_0x02a4
        L_0x0383:
            com.geinimi.p r0 = com.geinimi.p.ACTION_TRANSACT_FAILUE     // Catch:{ Exception -> 0x02ab }
            r6.a(r0)     // Catch:{ Exception -> 0x02ab }
            goto L_0x0002
        L_0x038a:
            return
        L_0x038b:
            r3 = move-exception
            goto L_0x0239
        L_0x038e:
            r2 = r3
            goto L_0x026b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.geinimi.AdServiceThread.run():void");
    }
}
