package com.sumsharp.monster.item;

import com.sumsharp.monster.common.data.Credit;
import com.sumsharp.monster.common.data.Player;
import java.io.DataInputStream;
import java.io.IOException;

public class BuyCondition {
    public static final String[] HONOR_NAME = {"新兵", "三级士兵", "二级士兵", "一级士兵", "列兵", "三级下士", "二级下士", "一级下士", "三级中士", "二级中士", "一级中士", "三级上士", "二级上士", "一级上士", "三级少尉", "二级少尉", "一级少尉", "三级中尉", "二级中尉", "一级中尉", "三级上尉", "二级上尉", "一级上尉", "三级少校", "二级少校", "一级少校", "三级中校", "二级中校", "一级中校", "三级上校", "二级上校", "一级上校", "三级少将", "二级少将", "一级少将", "三级中将", "二级中将", "一级中将", "三级上将", "二级上将", "一级上将"};
    public int count;
    public String desc;
    public int itemId;
    public String title;
    public int type;

    public void load(DataInputStream dis) {
        try {
            this.type = dis.readByte();
            switch (this.type) {
                case 0:
                    this.itemId = dis.readInt();
                    this.title = dis.readUTF();
                    this.count = dis.readInt();
                    return;
                case 1:
                    this.itemId = dis.readInt();
                    return;
                default:
                    return;
            }
        } catch (IOException e) {
        }
    }

    public boolean check(Player player) {
        if (this.type == 0) {
            Credit credit = player.findCriedit(this.itemId);
            if (credit == null) {
                return false;
            }
            return credit.value >= this.count;
        } else if (this.type == 1) {
            return player.honorIdx > this.itemId;
        } else {
            return false;
        }
    }

    public String getConString() {
        switch (this.type) {
            case 0:
                return this.title;
            case 1:
                return "军衔： " + HONOR_NAME[this.itemId];
            default:
                return "";
        }
    }
}
