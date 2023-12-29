package com.sumsharp.lowui;

import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.item.GameItem;

public class PanelItem {
    private long checkTime;
    private int clr;
    private Object item;
    private int needCount;
    private int page;
    private int startPage;
    private long time;
    private int type;

    public PanelItem(int page2, int type2, Object item2, int clr2) {
        this.page = page2;
        this.type = type2;
        this.item = item2;
        this.clr = clr2;
        this.startPage = page2;
        if (type2 == 5) {
            String[] sps = Utilities.splitString(item2.toString(), "##");
            int id = Integer.parseInt(sps[0]);
            int count = Integer.parseInt(sps[1]);
            String name = sps[2];
            String desc = sps[3];
            GameItem gi = new GameItem();
            gi.iconId = id;
            gi.name = name;
            gi.desc = desc;
            gi.count = (byte) count;
            this.item = gi;
            this.type = 1;
        }
    }

    public int getLineCount() {
        if (this.type == 3) {
            return ((StringDraw) this.item).length();
        }
        return this.type == 1 ? 2 : 1;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page2) {
        this.page = page2;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public Object getItem() {
        return this.item;
    }

    public void setItem(Object item2) {
        this.item = item2;
    }

    public int getClr() {
        return this.clr;
    }

    public void setClr(int clr2) {
        this.clr = clr2;
    }

    public int getStartPage() {
        return this.startPage;
    }

    public void setStartPage(int startPage2) {
        this.startPage = startPage2;
    }

    public int getNeedCount() {
        return this.needCount;
    }

    public void setNeedCount(int needCount2) {
        this.needCount = needCount2;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time2) {
        this.time = 1000 * time2;
        this.checkTime = System.currentTimeMillis();
    }

    public void checkTime() {
        if (this.type == 6) {
            this.time -= System.currentTimeMillis() - this.checkTime;
            this.checkTime = System.currentTimeMillis();
        }
    }

    public String getTimeStr() {
        if (this.type != 6) {
            return "";
        }
        checkTime();
        int t = (int) (this.time / 1000);
        int sec = t % 60;
        int minute = (t / 60) % 60;
        int hour = t / 3600;
        if (sec < 0) {
            sec = 0;
        }
        if (minute < 0) {
            minute = 0;
        }
        String m = String.valueOf(minute);
        String s = String.valueOf(sec);
        if (m.length() < 2) {
            m = "0" + m;
        }
        if (s.length() < 2) {
            s = "0" + s;
        }
        return String.valueOf(hour) + ":" + m + ":" + s;
    }
}
