package com.sumsharp.monster.item;

import com.sumsharp.monster.common.data.Credit;
import com.sumsharp.monster.common.data.Player;
import java.io.DataInputStream;
import java.io.IOException;

public class BuyPrice {
    public int count;
    public int discount;
    public int iconId;
    public int itemId;
    public GameItem needItem;
    public String title;
    public int type;

    public void load(DataInputStream dis) {
        try {
            this.type = dis.readByte();
            switch (this.type) {
                case 0:
                case 4:
                    this.count = dis.readInt();
                    return;
                case 1:
                    this.itemId = dis.readInt();
                    this.iconId = dis.readInt();
                    this.title = dis.readUTF();
                    this.count = dis.readInt();
                    this.needItem = new GameItem();
                    this.needItem.itemId = this.itemId;
                    this.needItem.id = this.itemId;
                    this.needItem.name = this.title;
                    this.needItem.iconId = this.iconId;
                    return;
                case 2:
                    this.itemId = dis.readInt();
                    this.title = dis.readUTF();
                    this.count = dis.readInt();
                    return;
                case 3:
                    this.count = dis.readInt();
                    this.discount = dis.readInt();
                    return;
                default:
                    return;
            }
        } catch (IOException e) {
        }
    }

    public boolean check(Player player) {
        if (this.type == 2) {
            Credit credit = player.findCriedit(this.itemId);
            if (credit == null) {
                return false;
            }
            return credit.value >= this.count;
        } else if (this.type == 1) {
            GameItem[] items = player.findItems(this.itemId);
            int total = 0;
            for (GameItem gameItem : items) {
                total += gameItem.count;
            }
            if (total >= this.count) {
                return true;
            }
            return false;
        } else if (this.type == 0) {
            return player.money >= this.count;
        } else {
            if (this.type == 3) {
                return true;
            }
            if (this.type == 4) {
                return true;
            }
            return false;
        }
    }

    public String getPriceString() {
        switch (this.type) {
            case 0:
                return "金币 ";
            case 1:
                return String.valueOf(this.title) + " x " + this.count;
            case 2:
                return String.valueOf(this.title) + " 声望" + this.count + "点";
            case 3:
                return "魔晶石";
            case 4:
                return "点券";
            default:
                return "";
        }
    }
}
