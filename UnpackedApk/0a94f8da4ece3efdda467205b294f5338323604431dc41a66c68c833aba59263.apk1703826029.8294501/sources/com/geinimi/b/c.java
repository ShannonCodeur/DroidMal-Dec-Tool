package com.geinimi.b;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class c {
    public String a;
    private Method b;
    private Object c;

    public c() {
    }

    public c(String str, Object obj) {
        this.b = null;
        this.a = null;
        this.c = null;
        this.a = str;
        this.c = obj;
        try {
            "Load Method: " + this.a;
            this.b = obj.getClass().getDeclaredMethod(this.a, new Class[0]);
            this.b.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
        }
    }

    public final void a() {
        if (this.b != null && this.c != null) {
            try {
                this.b.invoke(this.c, new Object[0]);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
        }
    }
}
