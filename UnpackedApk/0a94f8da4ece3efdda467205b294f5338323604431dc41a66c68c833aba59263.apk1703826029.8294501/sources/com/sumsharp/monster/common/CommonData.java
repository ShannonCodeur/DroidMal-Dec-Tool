package com.sumsharp.monster.common;

import com.sumsharp.monster.Team;
import com.sumsharp.monster.common.data.AccountInfo;
import com.sumsharp.monster.common.data.Player;

public class CommonData {
    public static AccountInfo account = new AccountInfo();
    public static boolean bLogined;
    public static boolean bagChangeNotifyFlag = false;
    public static String modelString = "Android";
    public static Player player = new Player();
    public static int serviceCode = 1;
    public static Team team = new Team();
    public static String version = "2.00";
    public static String versionString = "2.00-ACCQW002";

    public static void initGlobalVars() {
        GlobalVar.setGlobalValue((String) Utilities.CLIENT_MODEL, (Object) modelString);
        GlobalVar.setGlobalValue((String) Utilities.CLIENT_VERSION, (Object) versionString);
        GlobalVar.setGlobalValue((String) Utilities.CLIENT_VER, (Object) version);
    }
}
