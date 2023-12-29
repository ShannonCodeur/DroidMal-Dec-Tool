package com.sumsharp.monster.common.data;

public class Guild {
    public short actions;
    public short actions_max;
    public short active;
    public short active_max;
    public int[] allied_ids;
    public String[] allied_names;
    public short amounts;
    public short amounts_max;
    public Building[] buildings;
    public String creator;
    public int[] enemy_ids;
    public String[] enemy_names;
    public int id;
    public String leader;
    public int level;
    public int mineral;
    public int mineral_max;
    public int money;
    public int money_max;
    public String name;
    public int wood;
    public int wood_max;

    public Guild(int id2, String name2, String cr, String le, short am, short am_max, short acn, short acn_max, short act, short act_max, int wo, int wo_max, int mi, int mi_max, int mo, int mo_max, int[] al, String[] alname, int[] en, String[] enname, byte[] buid, byte[] bulv) {
        this.name = "";
        this.creator = "";
        this.leader = "";
        this.id = id2;
        this.name = name2;
        this.level = bulv[0];
        this.creator = cr;
        this.leader = le;
        this.amounts = am;
        this.amounts_max = am_max;
        this.actions = acn;
        this.actions_max = acn_max;
        this.active = act;
        this.active_max = act_max;
        this.wood = wo;
        this.wood_max = wo_max;
        this.mineral = mi;
        this.mineral_max = mi_max;
        this.money = mo;
        this.money_max = mo_max;
        this.allied_ids = al;
        this.allied_names = alname;
        this.enemy_ids = en;
        this.enemy_names = enname;
        this.buildings = new Building[buid.length];
        for (int i = 0; i < buid.length; i++) {
            Building b = Building.getBuilding(buid[i]);
            b.lv = bulv[i];
            this.buildings[i] = b;
        }
    }

    public Guild() {
        this.name = "";
        this.creator = "";
        this.leader = "";
        this.name = "";
        this.creator = "";
        this.leader = "";
    }

    public int getGuildId() {
        return this.id;
    }
}
