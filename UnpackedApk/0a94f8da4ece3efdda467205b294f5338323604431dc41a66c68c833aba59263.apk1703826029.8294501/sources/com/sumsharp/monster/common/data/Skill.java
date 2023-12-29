package com.sumsharp.monster.common.data;

import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;

public class Skill {
    public static final int ATKTYPE_ALL = 0;
    public static final int ATKTYPE_NONE = 0;
    public static final int ATKTYPE_PHY = 1;
    public static final int ATKTYPE_PRO_EARTH = 2;
    public static final int ATKTYPE_PRO_FIRE = 4;
    public static final int ATKTYPE_PRO_WATER = 3;
    public static final int ATKTYPE_PRO_WIND = 5;
    public static final int ATTACK_EFFTYPE_ADDHP = 1;
    public static final int ATTACK_EFFTYPE_ATK = 0;
    public static final int SKILLINFO_CHOOSEIDX_ENEMY = 4;
    public static final int SKILLINFO_CHOOSEIDX_ENEMY_ALL = 5;
    public static final int SKILLINFO_CHOOSEIDX_FRIEND = 1;
    public static final int SKILLINFO_CHOOSEIDX_FRIEND_ALL = 2;
    public static final int SKILLINFO_CHOOSEIDX_FRIEND_INCDIE = 3;
    public static final int SKILLINFO_CHOOSEIDX_NONE = 7;
    public static final int SKILLINFO_CHOOSEIDX_RANDOM = 6;
    public static final int SKILLINFO_CHOOSEIDX_SELF = 0;
    public static final int SKILL_TYPE_ACTIVE = 0;
    public static final int SKILL_TYPE_BUFF = 3;
    public static final int SKILL_TYPE_COUNTER = 2;
    public static final int SKILL_TYPE_DEBUFF = 4;
    public static final int SKILL_TYPE_PASSIVE = 1;
    public static Hashtable skillDescMap = new Hashtable();
    public static Hashtable skillDescRequestMap = new Hashtable();
    public byte atkBound;
    public String desc;
    public byte gridSize;
    public int id;
    public byte level;
    public short mp;
    public String name;
    public boolean requestDesc = false;
    public byte type;

    public void load(DataInputStream dis) {
        try {
            this.id = dis.readInt();
            this.type = dis.readByte();
            this.level = dis.readByte();
            this.mp = dis.readShort();
            this.gridSize = dis.readByte();
            this.name = dis.readUTF();
            if (this.type == 0) {
                this.atkBound = dis.readByte();
            }
        } catch (IOException e) {
        }
    }

    private void requestDesc() throws Exception {
        if (this.desc == null && !this.requestDesc) {
            requestDesc(this.id, this.level);
            this.requestDesc = true;
        }
    }

    public static void updateDesc(int skillId, byte skillLv, String desc2) {
        int reqid = getRequestId(skillId, skillLv);
        skillDescRequestMap.remove(new Integer(reqid));
        skillDescMap.put(new Integer(reqid), desc2);
    }

    public static void requestDesc(int id2, byte level2) throws Exception {
        if (skillDescRequestMap.get(new Integer(getRequestId(id2, level2))) == null) {
            UWAPSegment seg = new UWAPSegment(18, 26);
            seg.writeInt(id2);
            seg.writeByte(level2);
            Utilities.sendRequest(seg);
            skillDescRequestMap.put(new Integer(getRequestId(id2, level2)), new Integer((int) System.currentTimeMillis()));
        }
    }

    public static int getRequestId(int id2, int level2) {
        return (id2 << 8) | level2;
    }

    public String getDesc() {
        if (this.desc != null) {
            return this.desc;
        }
        String cache = (String) skillDescMap.get(new Integer(getRequestId(this.id, this.level)));
        if (cache == null) {
            try {
                requestDesc();
            } catch (Exception e) {
            }
            return "正在下载技能信息...";
        }
        this.desc = cache;
        return this.desc;
    }
}
