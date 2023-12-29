package com.sumsharp.monster.common;

import com.sumsharp.monster.MonsterMIDlet;
import java.util.Hashtable;

public class GlobalVar {
    private static Hashtable globalVars = new Hashtable();

    public static void setGlobalValue(String varName, int varValue) {
        globalVars.put(varName, new Integer(varValue));
    }

    public static void setGlobalValue(String varName, Object varValue) {
        globalVars.put(varName, varValue);
    }

    public static void deleteGlobalVar(String varName) {
        globalVars.remove(varName);
    }

    public static int getGlobalInt(String varName) {
        if (globalVars.containsKey(varName)) {
            return ((Integer) globalVars.get(varName)).intValue();
        }
        return 0;
    }

    public static String getGlobalString(String varName) {
        if (varName.startsWith("MIDlet")) {
            return MonsterMIDlet.instance.getAppProperty(varName.substring(6));
        } else if (globalVars.containsKey(varName)) {
            return (String) globalVars.get(varName);
        } else {
            return "";
        }
    }

    public static Object getGlobalObject(String varName) {
        return globalVars.get(varName);
    }
}
