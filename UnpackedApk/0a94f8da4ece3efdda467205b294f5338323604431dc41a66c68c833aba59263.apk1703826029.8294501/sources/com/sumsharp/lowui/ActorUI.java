package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.image.CartoonPlayer;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class ActorUI extends UI {
    public static final byte STATUS_ACTORLIST = 0;
    public static final byte STATUS_CREATEACTOR = 1;
    private static int actorCount = 0;
    private int[] ITEM_AREA;
    private Vector actorInfo = new Vector();
    private StringDraw context;
    private ActorInfo createInfo;
    private int createSelection;
    private CartoonPlayer female = CartoonPlayer.playCartoon(Tool.female, 2, 0, 0, true);
    private int height = 68;
    private boolean isInputBoxOpen = false;
    private int keyHelpHeight;
    private int keyHelpWidth;
    private int keyHelpX;
    private int keyHelpY;
    private CartoonPlayer male = CartoonPlayer.playCartoon(Tool.male, 2, 0, 0, true);
    private Menu menu = new Menu("角色选择", new String[]{"进入游戏", "创建角色", "删除角色", "返回登录"}, null, 8);
    private int npcLeft;
    private String npcName;
    private int npcNameHeight;
    private int npcNameWidth;
    private int npcNameY;
    private int npcNamex;
    private int selection = 0;
    private byte status = 0;
    private int width = 298;
    private int x = ((World.viewWidth / 2) - (this.width / 2));
    private int y = (NewStage.screenY + 15);

    private class ActorInfo {
        int hr;
        String map;
        String name;
        byte sex;

        public ActorInfo(String name2, byte sex2, String map2, int hr2) {
            this.name = name2;
            this.sex = sex2;
            this.map = map2;
            this.hr = hr2;
        }
    }

    public ActorUI() {
        this.menu.setItemStatus(0, false);
        this.menu.setItemStatus(2, false);
        this.menu.setSelIdx(1);
        initInfo();
    }

    public void addActorInfo(String name, byte sex, String map, int hr) {
        this.actorInfo.addElement(new ActorInfo(name, sex, map, hr));
        this.menu.setItemStatus(0, true);
        this.menu.setItemStatus(2, true);
        this.menu.setSelIdx(0);
        initInfo();
        actorCount = this.actorInfo.size();
        if (actorCount >= 4) {
            this.menu.setItemStatus(1, false);
        } else {
            this.menu.setItemStatus(1, true);
        }
    }

    private ActorInfo getSelected() {
        if (this.actorInfo.size() > 0) {
            return (ActorInfo) this.actorInfo.elementAt(this.selection);
        }
        return null;
    }

    private void initInfo() {
        String str;
        ActorInfo info = getSelected();
        this.npcName = info == null ? "" : info.name;
        this.npcNameHeight = Utilities.CHAR_HEIGHT;
        this.npcNameWidth = Utilities.font.stringWidth(this.npcName) + (this.npcNameHeight << 1);
        this.npcNamex = (((this.x + this.width) - this.npcNameWidth) + this.npcNameHeight) - 40;
        this.npcNameY = (this.y + this.height) - (this.npcNameHeight >> 1);
        if (info == null) {
            str = "请创建角色";
        } else {
            str = "所在地区：" + info.map + "\n" + "级别：" + info.hr;
        }
        this.context = new StringDraw(str, ((this.width - 10) - (this.height >> 1)) - this.male.getWidth(), -1);
        this.keyHelpHeight = Tool.img_triArrowL[0].getHeight();
        this.keyHelpWidth = 182;
        this.keyHelpX = (World.viewWidth / 2) - (this.keyHelpWidth / 2);
        this.keyHelpY = this.y + this.height;
        if (info != null) {
            this.npcLeft = ((this.x + this.width) - this.male.getWidth(2, 0)) - 40;
        }
    }

    public void removeActorInfo(String name) {
        for (int i = 0; i < this.actorInfo.size(); i++) {
            ActorInfo info = (ActorInfo) this.actorInfo.elementAt(i);
            if (info.name.equals(name)) {
                this.actorInfo.removeElement(info);
                return;
            }
        }
    }

    public void cycle() {
        super.cycle();
        if (this.closed) {
            this.show = false;
        } else if (this.show) {
            handlePointEvents(World.pressedx, World.pressedy);
            if (this.status == 0) {
                if (this.actorInfo.size() > 0) {
                    if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
                        this.selection--;
                        if (this.selection < 0) {
                            this.selection = this.actorInfo.size() - 1;
                        }
                        initInfo();
                    } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
                        this.selection++;
                        if (this.selection >= this.actorInfo.size()) {
                            this.selection = 0;
                        }
                        initInfo();
                    }
                }
                this.menu.cycle();
                ActorInfo info = getSelected();
                if (info != null) {
                    int f = ((World.tick / 100) % 2) << 1;
                    if (info.sex == 0) {
                        if (f != this.female.getAnimateIndex()) {
                            this.female.setAnimateIndex(f);
                            this.female.reset();
                        }
                        this.female.nextFrame();
                        return;
                    }
                    if (f != this.male.getAnimateIndex()) {
                        this.male.setAnimateIndex(f);
                        this.male.reset();
                    }
                    this.male.nextFrame();
                }
            } else if (this.status == 1) {
                moveBtn();
                CartoonPlayer p = this.male;
                if (this.createInfo.sex == 0) {
                    p = this.female;
                }
                int f2 = ((World.tick / 100) % 2) << 1;
                if (f2 != p.getAnimateIndex()) {
                    p.setAnimateIndex(f2);
                    p.reset();
                }
                p.nextFrame();
                if (Utilities.isKeyPressed(0, true) || Utilities.isKeyPressed(13, true)) {
                    this.createSelection--;
                    if (this.createSelection < 0) {
                        this.createSelection = 1;
                    }
                    initInfo();
                } else if (Utilities.isKeyPressed(1, true) || Utilities.isKeyPressed(19, true)) {
                    this.createSelection++;
                    if (this.createSelection >= 2) {
                        this.createSelection = 0;
                    }
                    initInfo();
                } else if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
                    if (this.createInfo != null) {
                        switch (this.createSelection) {
                            case 0:
                                this.createInfo.sex = (byte) (1 - this.createInfo.sex);
                                return;
                            default:
                                return;
                        }
                    }
                } else if ((Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) && this.createInfo != null) {
                    switch (this.createSelection) {
                        case 0:
                            this.createInfo.sex = (byte) (1 - this.createInfo.sex);
                            return;
                        default:
                            return;
                    }
                }
            }
        } else {
            this.show = true;
        }
    }

    public void paint(Graphics g) {
        int i;
        if (this.status == 0) {
            this.menu.paint(g);
            drawTalkPanel(g, (World.viewWidth / 2) - (this.width / 2), this.y, this.width, this.height);
            int i2 = this.keyHelpX;
            int i3 = this.keyHelpY;
            int i4 = this.keyHelpWidth;
            int i5 = this.keyHelpHeight;
            if (this.actorInfo.size() == 0) {
                i = 0;
            } else {
                i = this.selection + 1;
            }
            drawPageBar(g, i2, i3, i4, i5, i, this.actorInfo.size(), 4);
            this.context.drawShadow(g, this.x + 31, this.y + 15, 16776398, 0);
            ActorInfo info = getSelected();
            if (info != null) {
                if (info.sex == 0) {
                    this.female.draw(g, this.npcLeft, (this.y + this.height) - 15);
                } else {
                    this.male.draw(g, this.npcLeft, (this.y + this.height) - 15);
                }
            }
            Tool.draw3DString(g, this.npcName, this.npcNamex, this.npcNameY, Tool.clr_androidTextDefault, 0, 4);
            CornerButton.instance.paintUICmd(g, "进入", "返回", true);
        } else if (this.status == 1) {
            CartoonPlayer p = this.male;
            if (this.createInfo.sex == 0) {
                p = this.female;
            }
            int off3 = 0 + 0;
            UI.drawTitlePanel(g, "创建角色", 3, (NewStage.screenY + 5) - 0, World.viewWidth - 6, Utilities.TITLE_HEIGHT, 4);
            int dy = ((NewStage.screenY + 10) + Utilities.TITLE_HEIGHT) - off3;
            int h = this.male.getHeight(0, 0) + 10;
            drawTalkPanel(g, h, dy, World.viewWidth - (h * 2), h);
            p.draw(g, (World.viewWidth >> 1) - 10, ((dy + h) - 5) - 0);
            int dy2 = dy + ((h + 10) - (off3 + 0));
            int lineHeight = Utilities.LINE_HEIGHT + 4;
            int w = Utilities.font.stringWidth(" 性　　别") + 20;
            int vw = World.viewWidth - w;
            drawRectTipPanel(g, h, dy2, World.viewWidth - (h * 2), 48, true);
            Tool.draw3DString(g, " 性　　别", h + 10, dy2 + 30, Tool.clr_androidTextDefault, 0, 4);
            int dw = Utilities.font.stringWidth("男");
            int dx = ((vw >> 1) + w) - (dw >> 1);
            if (this.createSelection == 0) {
                int off = World.tick % 8;
                if (off > 4) {
                    off = 7 - off;
                }
                g.drawImage(Tool.img_triArrowL[0], (dx - 15) + off, (lineHeight >> 1) + dy2 + 10, 10);
                g.drawImage(Tool.img_triArrowR[0], ((dx + dw) + 15) - off, (lineHeight >> 1) + dy2 + 10, 6);
            }
            g.setColor(Tool.CLR_TABLE[10]);
            g.drawString(this.createInfo.sex == 0 ? "女" : "男", dx, dy2 + lineHeight + 10, 36);
            int dy3 = dy2 + lineHeight + lineHeight;
            drawRectTipPanel(g, h, dy3, World.viewWidth - (h * 2), 48, true);
            Tool.draw3DString(g, " 角色名称", h + 10, dy3 + 30, Tool.clr_androidTextDefault, 0, 4);
            String drawName = this.createInfo.name;
            int i6 = Tool.CLR_TABLE[11];
            int i7 = Tool.CLR_TABLE[23];
            Tool.draw3DString(g, drawName, ((vw >> 1) + w) - (Utilities.font.stringWidth(drawName) >> 1), dy3 + lineHeight + 10, Tool.clr_androidTextDefault, 0, 36);
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
            drawBtn(g, (World.viewHeight - Chat.chatHeight) - (Utilities.LINE_HEIGHT >> 1));
        }
    }

    public int getMenuSelection() {
        if (this.status == 0) {
            return this.menu.getMenuSelection();
        }
        return this.createSelection;
    }

    public int getActorSelection() {
        return this.selection;
    }

    public void setActorSelection(int sel) {
        this.selection = sel;
        initInfo();
    }

    public void setNewActorName(String name) {
        if (this.createInfo != null) {
            this.createInfo.name = name;
            if (name.equals("")) {
                this.createInfo.name = "点击输入名称";
            }
        }
    }

    public void setActorStatus(byte status2) {
        this.status = status2;
        if (status2 == 1) {
            this.createInfo = new ActorInfo("点击输入名称", 0, "", 1);
            this.createSelection = 0;
            initItemArea();
            CornerButton.instance.setCmd("创建", "返回");
        }
    }

    public int getActorSex() {
        if (this.createInfo != null) {
            return this.createInfo.sex;
        }
        return 0;
    }

    public int getX() {
        return this.x;
    }

    private void initItemArea() {
        this.ITEM_AREA = new int[(2 * 4)];
        for (int i = 0; i < 2; i++) {
            this.ITEM_AREA[(i * 4) + 0] = 0;
            this.ITEM_AREA[(i * 4) + 1] = NewStage.screenY + 10 + Utilities.TITLE_HEIGHT + this.male.getHeight(0, 0) + 10 + 10 + (i * 45);
            this.ITEM_AREA[(i * 4) + 2] = World.viewWidth;
            this.ITEM_AREA[(i * 4) + 3] = 45;
        }
    }

    private void handlePointEvents(int x2, int y2) {
        int w;
        if (this.status == 1 && y2 >= this.ITEM_AREA[1] && y2 <= this.ITEM_AREA[(((this.ITEM_AREA.length / 4) - 1) * 4) + 1] + this.ITEM_AREA[(((this.ITEM_AREA.length / 4) - 1) * 4) + 3]) {
            for (int i = 0; i < this.ITEM_AREA.length / 4; i++) {
                if (y2 > this.ITEM_AREA[(i * 4) + 1] && y2 < this.ITEM_AREA[(i * 4) + 1] + this.ITEM_AREA[(i * 4) + 3]) {
                    if (i == 0) {
                        w = Utilities.font.stringWidth(" 男  ") + 5;
                    } else {
                        w = Utilities.font.stringWidth(" 发型  01 ") + 5;
                    }
                    int titleW = Utilities.font.stringWidth("头发颜色") + 16;
                    int midx = ((World.viewWidth - titleW) / 2) + titleW + 5;
                    int leftX = (midx - (w / 2)) - 36;
                    int rightX = (w / 2) + midx + 36;
                    if (i == 1) {
                        int strw = Utilities.font.stringWidth("按OK输入名称");
                        int rightX2 = midx + (strw / 2);
                        if (x2 > midx - (strw / 2) && x2 < rightX2) {
                            Utilities.keyPressed(5, true);
                            this.isInputBoxOpen = true;
                        }
                    } else if (this.createSelection == i && x2 > leftX && x2 < rightX && (x2 > (w / 2) + midx || x2 < midx - (w / 2))) {
                        Utilities.keyPressed(3, true);
                    }
                    this.createSelection = i;
                }
            }
        }
    }
}
