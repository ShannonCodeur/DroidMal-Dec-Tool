package com.sumsharp.monster;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import com.sumsharp.monster.common.GlobalVar;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.gtvm.GTVM;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Display;

public class MonsterMIDlet extends Activity {
    private static final float TARGET_HEAP_UTILIZATION = 0.99f;
    public static Display display;
    public static MonsterMIDlet instance;
    public static boolean isRun;
    World world;

    public MonsterMIDlet() {
        instance = this;
        initClrPlan();
    }

    private void initClrPlan() {
        Tool.CLR_TINT = -1;
        Tool.CLR_LIGHT = -75641;
        Tool.CLR_DARK = -14733;
        Tool.CLR_TITLE = -4443;
        Tool.CLR_TEXT_NORMAL = -6661544;
        Tool.CLR_TEXT_LIGHT = -1;
        Tool.CLR_TEXT_DARK = -16777216;
        Tool.CLR_VERYDARK = -6661544;
        Tool.CLR_GRAY = -7829368;
        Tool.CLR_L_DARK = -75641;
        Tool.CLR_L_LIGHT = -5202;
        Tool.CLR_GRAY_DARK = -6583659;
        Tool.CLR_GRAY_LIGHT = -5267036;
        Tool.CLR_BORDER = new int[][]{new int[]{-15850207, -605326, -15850207}, new int[]{Tool.CLR_LIGHT}, new int[]{Tool.CLR_VERYDARK}, new int[]{Tool.CLR_VERYDARK, Tool.CLR_TEXT_DARK, Tool.CLR_TINT}, new int[]{Tool.CLR_DARK}, new int[]{Tool.CLR_LIGHT}, new int[]{Tool.CLR_DARK}, new int[]{Tool.CLR_TITLE}, new int[]{Tool.CLR_VERYDARK}, new int[]{Tool.CLR_GRAY}};
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startApp();
    }

    /* access modifiers changed from: protected */
    public void startApp() {
        if (!isRun) {
            isRun = true;
            requestWindowFeature(1);
            this.world = new World(this);
            Utilities.setDisplay(display, this.world);
            new Thread(this.world).start();
            setContentView(this.world);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        this.world.setVisibility(hasFocus ? 0 : 4);
    }

    public void destroyApp(boolean arg0) {
        isRun = false;
        Utilities.closeConnection();
    }

    /* access modifiers changed from: protected */
    public void pauseApp() {
    }

    public static void exit() {
        try {
            Utilities.isExitGame = false;
            instance.destroyApp(true);
            if (World.uiPackage != null) {
                GTVM.saveRMSFile("ui", World.uiPackage.makePackage());
            }
            if (GlobalVar.getGlobalInt("openWapOnExit") == 1) {
                if (Utilities.wapURL != null && !Utilities.wapURL.equals("") && Utilities.wapURL.startsWith("http")) {
                    try {
                        instance.platformRequest(Utilities.wapURL);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            } else if (GlobalVar.getGlobalInt("openWapOnExit") == 2) {
                try {
                    instance.platformRequest(Utilities.downLoadPage);
                } catch (Throwable th2) {
                    th2.printStackTrace();
                }
            }
            instance.notifyDestroyed();
            instance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAppProperty(int rid) {
        return getString(rid);
    }

    public String getAppProperty(String key) {
        if (key.equals("URL")) {
            return getAppProperty((int) R.string.wapURL);
        }
        return null;
    }

    public boolean platformRequest(String url) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        return false;
    }

    private void notifyDestroyed() {
        Process.killProcess(Process.myPid());
    }

    public InputStream getAssetStream(String filename) throws IOException {
        String packagename;
        if (filename.indexOf(".ui", filename.length() - 4) == -1 && filename.indexOf(".etd", filename.length() - 4) == -1 && filename.indexOf(".etf", filename.length() - 4) == -1) {
            packagename = "240x320";
        } else {
            packagename = "GenericMidp2";
        }
        return getAssets().open(String.valueOf(packagename) + filename);
    }
}
