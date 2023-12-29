package com.sumsharp.android.ui;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import javax.microedition.lcdui.Graphics;

public class GridMenu {
    public static int CLOSE = 0;
    public static int CLOSING = 3;
    public static int OPEN = 1;
    public static int OPENING = 2;
    public static final int SPEED = 80;
    public static final int gridBound = 45;
    public static GridMenu instance = new GridMenu();
    private int arrowX;
    private int arrowY;
    private int border = 5;
    private int closeX;
    private int height;
    private Item[] items;
    private int openX;
    private int state = 0;
    private int width;
    public int x;
    public int y;

    private GridMenu() {
    }

    public void init() {
        this.x = World.viewWidth - 10;
        this.closeX = this.x;
        this.y = 54;
        this.width = (this.border * 4) + 135;
        this.height = this.width;
        initItems();
        this.state = 0;
        this.arrowX = (this.x - Tool.img_arrow.getWidth()) + 13;
        this.arrowY = this.y + 13;
        this.openX = (World.viewWidth - this.width) + 5;
        Item.initWH();
    }

    private void initItems() {
        this.items = new Item[9];
        for (int i = 0; i < 9; i++) {
            this.items[i] = new Item();
            this.items[i].id = -1;
            this.items[i].x = this.border + ((this.border + 45) * (i / 3));
            this.items[i].y = this.border + ((this.border + 45) * (i % 3));
            this.items[i].isEnable = true;
        }
        this.items[0].id = 0;
        this.items[0].function = 6;
        this.items[0].imgId = 0;
        this.items[0].disableId = 10;
        this.items[0].content = Utilities.VMUI_GAMEMENU;
        this.items[0].name = "主菜单";
        this.items[1].id = 1;
        this.items[1].name = "商城";
        this.items[1].imgId = 9;
        this.items[1].disableId = 19;
        this.items[1].content = "ui_store";
        this.items[2].id = 2;
        this.items[2].name = "背包";
        this.items[2].function = 51;
        this.items[2].imgId = 2;
        this.items[2].disableId = 12;
        this.items[2].content = Utilities.VMUI_BAG;
        this.items[3].id = 3;
        this.items[3].function = 49;
        this.items[3].imgId = 22;
        this.items[3].disableId = 25;
        this.items[3].content = "ui_playerlist";
        this.items[3].name = "周围玩家";
        this.items[4].id = 4;
        this.items[4].function = 57;
        this.items[4].imgId = 1;
        this.items[4].disableId = 11;
        this.items[4].content = "ui_tasklist";
        this.items[4].name = "任务列表";
        this.items[5].id = 5;
        this.items[5].imgId = 3;
        this.items[5].disableId = 13;
        this.items[5].content = "ui_pet";
        this.items[5].name = "宠物";
        this.items[6].id = 6;
        this.items[6].function = 55;
        this.items[6].imgId = 8;
        this.items[6].disableId = 18;
        this.items[6].content = "chat";
        this.items[6].name = "全屏聊天";
        this.items[7].id = 7;
        this.items[7].imgId = 5;
        this.items[7].disableId = 15;
        this.items[7].content = "ui_guildlist";
        this.items[7].name = "公会";
        this.items[8].id = 8;
        this.items[8].function = 42;
        this.items[8].imgId = 7;
        this.items[8].disableId = 17;
        this.items[8].content = "quality";
        this.items[8].name = "显示";
    }

    public void paint(Graphics g) {
        for (int i = 0; i < 9; i++) {
            this.items[i].draw(g);
        }
        drawArrow(g);
    }

    private void drawArrow(Graphics g) {
        g.setColor(16776960);
        g.drawImage(Tool.img_arrow, this.arrowX, this.arrowY, 20);
    }

    public void toBattleMode() {
        if (this.items != null && this.items.length > 0) {
            for (Item item : this.items) {
                item.isEnable = false;
            }
        }
    }

    public void toNormalMode() {
        if (this.items != null && this.items.length > 0) {
            for (Item item : this.items) {
                item.isEnable = true;
            }
        }
    }

    private boolean cycleArrow(int px, int py) {
        int toWidth = this.state == CLOSE ? World.viewWidth : this.x;
        if (px <= this.arrowX - 20 || px >= toWidth || py <= this.arrowY - 40 || py >= this.arrowY + 40) {
            return false;
        }
        if (this.state == CLOSE) {
            this.state = OPENING;
        } else if (this.state == OPEN) {
            this.state = CLOSING;
        }
        return true;
    }

    public void cycle() {
        if (this.state == OPENING) {
            this.x -= 80;
            this.arrowX = (this.x - Tool.img_arrow.getWidth()) + 8;
            if (this.x <= this.openX) {
                this.x = this.openX;
                this.arrowX = (this.x - Tool.img_arrow.getWidth()) + 8;
                this.state = OPEN;
            }
        } else if (this.state == CLOSING) {
            this.x += 80;
            this.arrowX = (this.x - Tool.img_arrow.getWidth()) + 8;
            if (this.x >= this.closeX) {
                this.x = this.closeX;
                this.arrowX = (this.x - Tool.img_arrow.getWidth()) + 8;
                int i = 0;
                while (true) {
                    if (i >= 9) {
                        break;
                    } else if (this.items[i].isSelected) {
                        this.items[i].isSelected = false;
                        if (this.items[i].name.equals("商城")) {
                            CommonComponent.loadUI(this.items[i].content);
                        } else if (this.items[i].name.equals("宠物")) {
                            CommonComponent.loadUI(this.items[i].content);
                        } else if (this.items[i].name.equals("公会")) {
                            CommonComponent.loadUI(this.items[i].content);
                        } else {
                            Utilities.keyPressed(this.items[i].function, true);
                        }
                    } else {
                        i++;
                    }
                }
                this.state = CLOSE;
            }
        } else {
            if (World.pressedx != -1 && World.pressedy != -1 && World.pressedx > this.arrowX && World.pressedy > this.y && World.pressedx < this.x + this.width && World.pressedy < this.y + this.height && !cycleArrow(World.pressedx, World.pressedy)) {
                for (int i2 = 0; i2 < 9; i2++) {
                    if (this.items[i2].id != -1 && this.items[i2].handle(World.pressedx, World.pressedy)) {
                        this.state = CLOSING;
                        World.pressedx = -1;
                        World.pressedy = -1;
                    }
                }
            }
            if (this.state == OPEN && Utilities.isKeyPressed(10, true)) {
                this.state = CLOSING;
            }
        }
    }
}
