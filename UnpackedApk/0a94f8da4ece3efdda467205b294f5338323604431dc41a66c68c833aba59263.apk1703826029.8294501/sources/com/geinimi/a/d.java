package com.geinimi.a;

import com.geinimi.c.m;

public final class d {
    private static final String a = m.a(6);
    private static final String b = m.a(7);
    private static final String c = m.a(8);
    private static final String d = m.a(9);
    private static final String e = m.a(10);
    private static final String f = m.a(11);
    private static final String g = m.a(12);
    private static final String h = m.a(64);
    private static final String i = m.a(65);
    private static final String j = m.a(66);

    public static a a(String str, String str2) {
        a aVar = null;
        if (a.equals(str)) {
            aVar = new e(str2);
        } else if (b.equals(str)) {
            aVar = new k(str2);
        } else if (c.equals(str)) {
            aVar = new f(str2);
        } else if (d.equals(str)) {
            aVar = new g(str2);
        } else if (e.equals(str)) {
            aVar = new j(str2);
        } else if (f.equals(str)) {
            aVar = new h(str2);
        } else if (g.equals(str)) {
            aVar = new b(str2);
        } else if (h.equals(str)) {
            aVar = new l(str2);
        } else if (i.equals(str)) {
            aVar = new i(str2);
        } else if (j.equals(str)) {
            aVar = new c(str2);
        } else {
            "Can't find the Adcommandable as key = " + str + ",value=" + str2;
        }
        if (aVar != null) {
            aVar.a();
        }
        return aVar;
    }
}
