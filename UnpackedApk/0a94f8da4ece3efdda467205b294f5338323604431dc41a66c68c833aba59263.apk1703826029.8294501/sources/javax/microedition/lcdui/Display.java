package javax.microedition.lcdui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.sumsharp.monster.MonsterMIDlet;
import com.sumsharp.monster.World;

public class Display {
    private static Activity cur;

    public static void init() {
        cur = MonsterMIDlet.instance;
    }

    public void setCurrent(Activity activity) {
        cur.startActivity(new Intent(cur, activity.getClass()));
        cur = activity;
    }

    public void setCurrent(World instance) {
        cur.startActivity(new Intent(cur, MonsterMIDlet.class));
    }

    public void setCurrent(View canvas) {
    }

    public void setCurrent(Form form) {
    }

    public void updateCommands() {
    }

    public void repaint(Displayable displayable, int x, int y, int width, int height) {
    }

    public static int getGameAction(int keyCode) {
        return 0;
    }

    public void setScrollUp(boolean b) {
    }

    public void setScrollDown(boolean b) {
    }

    public static int getKeyCode(int gameAction) {
        return 0;
    }

    public static String getKeyName(int keyCode) {
        return null;
    }

    public void serviceRepaints() {
    }
}
