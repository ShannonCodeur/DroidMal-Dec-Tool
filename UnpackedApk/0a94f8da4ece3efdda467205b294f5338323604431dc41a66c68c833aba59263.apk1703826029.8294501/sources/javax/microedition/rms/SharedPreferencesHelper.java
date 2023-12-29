package javax.microedition.rms;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {
    Context context;
    Editor editor = this.sp.edit();
    SharedPreferences sp;

    public SharedPreferencesHelper(Context c, String name) {
        this.context = c;
        this.sp = this.context.getSharedPreferences(name, 0);
    }

    private boolean putString(String key, String value) {
        this.editor = this.sp.edit();
        this.editor.putString(key, value);
        this.editor.commit();
        return true;
    }

    private String getString(String key) {
        return this.sp.getString(key, null);
    }

    public byte[] getData(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }
        String[] values = value.split(",");
        byte[] b = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            b[i] = Byte.valueOf(values[i]).byteValue();
        }
        return b;
    }

    public boolean saveData(String key, byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte append : data) {
            sb.append(append);
            sb.append(",");
        }
        return putString(key, sb.toString());
    }
}
