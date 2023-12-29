package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.GlobalVar;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.common.data.Building;
import com.sumsharp.monster.common.data.Guild;
import com.sumsharp.monster.common.data.Player;
import com.sumsharp.monster.common.data.Title;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;

public class GuildUI extends UI implements TabHandler {
    private static final int PAGE_ALLIANCE = 2;
    private static final int PAGE_BUILDING = 3;
    private static final int PAGE_PROPERTY = 0;
    private static final int PAGE_RES = 1;
    private int _commonY;
    private int _contentY;
    private int _tabY;
    private StringDraw allied;
    private Table buildingTable;
    private int commonHeight;
    private int commonSpeed;
    private int commonY;
    private int contentHeight;
    private int contentSpeed;
    private int contentY;
    private int creditCount = 0;
    private int creditHeight = 0;
    private int creditIdx = 0;
    private int creditShow = 0;
    private int creditStart = 0;
    private int curUpgrade = -1;
    private StringDraw enemy;
    private int freeTabWidth = 0;
    private Guild guild;
    private int idx;
    private int nameX;
    private int nameY;
    private int pageX;
    private Player player;
    private Table resTable;
    private Tab tab;
    private int tabSpeed;
    private String[] tabTitles = {"属性", "资源", "联盟", "建筑"};
    private String[] titleSelect;
    private int width;
    private int x;

    public GuildUI() {
        init();
    }

    private void init() {
        this.x = 3;
        this.width = World.viewWidth - 6;
        this.commonY = NewStage.screenY;
        this.commonHeight = Utilities.TITLE_HEIGHT;
        int tabY = this.commonY + this.commonHeight;
        int tabHeight = Tab.getDefaultHeight();
        this.contentY = tabY + tabHeight;
        this.contentHeight = ((World.viewHeight - this.contentY) - CornerButton.instance.getHeight()) - 5;
        this.nameX = 0;
        this.nameY = (this.commonY - (tabHeight >> 1)) + 2;
        this._tabY = tabY;
        this._commonY = this.commonY;
        this._contentY = this.contentY;
        int[] widths = new int[4];
        widths[0] = 20;
        widths[1] = -1;
        widths[2] = 48;
        this.resTable = new Table(this.x, this.contentY, World.viewWidth - 6, this.contentHeight, new String[]{"", "名称", "数量", "desc:tip"}, widths);
        this.resTable.setHasTab(true);
        int[] widths2 = new int[4];
        widths2[0] = 80;
        widths2[1] = 20;
        widths2[2] = -1;
        this.buildingTable = new Table(this.x, this.contentY, World.viewWidth - 6, this.contentHeight, new String[]{"建筑名称", "lv", "状态", "desc:tip"}, widths2);
        this.buildingTable.setHasTab(true);
        int tabY2 = World.viewHeight;
        this.tab = new Tab("tab", this.tabTitles, this.x, this.width, true, this);
        this.tab.setContentHeight(this.contentHeight);
        this.commonY = -this.commonHeight;
        this.contentY = World.viewHeight + tabHeight;
        this.commonSpeed = (this._commonY - this.commonY) >> 1;
        this.tabSpeed = (tabY2 - this._tabY) / 2;
        this.contentSpeed = (this.contentY - this._contentY) / 2;
        this.nameX -= (Utilities.font.stringWidth("公会规模") + Utilities.LINE_HEIGHT) + 20;
        this.pageX = World.viewWidth;
        this.creditCount = CommonData.player.credits.size();
        this.creditIdx = 0;
        int cdh = this.contentHeight - 6;
        this.creditHeight = Utilities.LINE_HEIGHT + 12;
        this.creditShow = cdh / this.creditHeight;
        this.creditStart = 0;
        if (this.creditShow >= this.creditCount) {
            this.creditShow = this.creditCount;
        }
        this.guild = new Guild();
        setCmd(null, "关闭");
        this.tab.setTabIndex(GlobalVar.getGlobalInt("GuildUI_StartIdx"));
        this.curUpgrade = -1;
    }

    public void updateInfo(int id, String name, String cr, String le, short am, short am_max, short acn, short acn_max, short act, short act_max, int wo, int wo_max, int mi, int mi_max, int mo, int mo_max, int[] alid, String[] alna, int[] en, String[] enna, byte[] buid, byte[] bulv) {
        this.guild = new Guild(id, name, cr, le, am, am_max, acn, acn_max, act, act_max, wo, wo_max, mi, mi_max, mo, mo_max, alid, alna, en, enna, buid, bulv);
        initResTable();
        initBuildingTable();
        if (this.tab.getIdx() != 3) {
            setCmd(null, "关闭");
        } else if (checkCanUpgrade(this.guild.buildings[0], null)) {
            setCmd("升级", "关闭");
        } else {
            setCmd(null, "关闭");
        }
    }

    private void initResTable() {
        String[] res = {"矿石", "木材", "公会资金"};
        int length = res.length;
        String[] strArr = {"0", "0", "0"};
        String[] nums = {new StringBuilder(String.valueOf(this.guild.mineral)).toString(), new StringBuilder(String.valueOf(this.guild.wood)).toString(), new StringBuilder(String.valueOf(this.guild.money)).toString()};
        String[] maxs = new String[3];
        for (int i = 0; i < 3; i++) {
            maxs[i] = "当前资源:矿石\n当前数量:" + this.guild.mineral + "\n资源上限:" + this.guild.mineral_max + "\n";
        }
        this.resTable.clear();
        this.resTable.addTableItem("", new String[]{"building1", "building0", "building2"}, 2, null, null);
        this.resTable.addTableItem("名称", res, 0, null, null);
        this.resTable.addTableItem("数量", nums, 1, null, null);
        this.resTable.addTableItem("desc:tip", maxs, 1, null, null);
        this.resTable.show = this.show;
    }

    private void initBuildingTable() {
        String str;
        if (this.guild.buildings != null && this.guild.buildings.length != 0) {
            int len = this.guild.buildings.length;
            String[] strArr = new String[len];
            String[] names = new String[len];
            String[] lv = new String[len];
            String[] states = new String[len];
            String[] tips = new String[len];
            for (int i = 0; i < len; i++) {
                Building b = this.guild.buildings[i];
                names[i] = b.name;
                lv[i] = new StringBuilder(String.valueOf(b.lv)).toString();
                boolean[] flag = {true, true, true, true};
                states[i] = checkCanUpgrade(b, flag) ? "可升级" : "";
                if (b.lv == b.lv_max) {
                    tips[i] = "已经到达顶级,不能再升级了";
                } else {
                    StringBuilder append = new StringBuilder("升级到").append(b.lv + 1).append("级").append(this.guild.buildings[i].name).append("\n需要资源:").append(!flag[0] ? "<cff0000>" : "").append(Building.howManyWood(b.id, b.lv)).append("木头,").append(!flag[0] ? "</c>" : "").append(!flag[1] ? "<cff0000>" : "").append(Building.howManyMineral(b.id, b.lv)).append("矿石,").append(!flag[1] ? "</c>" : "").append(!flag[2] ? "<cff0000>" : "").append(Building.howManyMoney(b.id, b.lv)).append("公会资金");
                    if (!flag[1]) {
                        str = "</c>";
                    } else {
                        str = "";
                    }
                    tips[i] = append.append(str).toString();
                    if (this.guild.buildings[i].name.equals("要塞")) {
                        String needBuilding = "\n需要建筑:";
                        boolean needNotify = false;
                        for (int j = 0; j < this.guild.buildings.length; j++) {
                            if (Building.needCheckBuilding(this.guild.buildings[j].id)) {
                                if (this.guild.buildings[j].lv <= this.guild.buildings[i].lv) {
                                    needBuilding = String.valueOf(needBuilding) + " <cff0000>" + this.guild.buildings[j].name + "(" + (this.guild.buildings[i].lv + 1) + "级)</c>";
                                    needNotify = true;
                                } else {
                                    needBuilding = String.valueOf(needBuilding) + this.guild.buildings[j].name + "(" + (this.guild.buildings[i].lv + 1) + "级)";
                                    needNotify = true;
                                }
                            }
                        }
                        if (needNotify) {
                            tips[i] = String.valueOf(tips[i]) + needBuilding;
                        }
                    } else if (!flag[3]) {
                        tips[i] = String.valueOf(tips[i]) + "\n<cff0000>需要要塞(" + this.guild.buildings[i].lv + "级)</c>";
                    }
                }
            }
            this.buildingTable.clear();
            this.buildingTable.addTableItem("建筑名称", names, 0, null, null);
            this.buildingTable.addTableItem("lv", lv, 1, null, null);
            this.buildingTable.addTableItem("状态", states, 0, null, null);
            this.buildingTable.addTableItem("desc:tip", tips, 0, null, null);
            this.buildingTable.show = this.show;
            if (checkCanUpgrade(this.guild.buildings[0], null)) {
                setCmd("升级", "关闭");
            } else {
                setCmd(null, "关闭");
            }
        }
    }

    public void cycle() {
        super.cycle();
        if (this.closed) {
            this.commonY -= this.commonSpeed;
            if (this.contentY != this._contentY) {
                this.tab.setY(this.tab.getY() + this.tabSpeed);
            }
            this.contentY += this.contentSpeed;
            this.nameY = (this.commonY - (this.tab.getHeight() >> 1)) + 2;
            if (this.tab.getY() >= World.viewHeight) {
                this.show = false;
            }
        } else if (this.show) {
            int tabIdx = this.tab.getIdx();
            this.tab.cycle();
            if (tabIdx == 1 && this.resTable != null) {
                this.resTable.handlePoints(World.pressedx, World.pressedy);
            } else if (tabIdx == 3 && this.buildingTable != null) {
                this.buildingTable.handlePoints(World.pressedx, World.pressedy);
            }
            if (this.pageX - 20 > World.viewWidth - 80) {
                this.pageX -= 20;
            } else {
                this.pageX = World.viewWidth - 80;
            }
            if (this.nameX + 20 < 0) {
                this.nameX += 20;
            } else {
                this.nameX = 0;
            }
            moveBtn();
            if (tabIdx == 3) {
                int nowidx = this.buildingTable.getMenuSelection();
                if (!(nowidx == this.idx || nowidx == -1)) {
                    if (checkCanUpgrade(this.guild.buildings[nowidx], null)) {
                        setCmd("升级", "关闭");
                    } else {
                        setCmd(null, "关闭");
                    }
                    this.idx = nowidx;
                }
            }
            if (tabIdx == 1) {
                this.resTable.cycle();
            } else if (tabIdx == 3) {
                this.buildingTable.cycle();
                if (Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(9, true)) {
                    int idx2 = this.buildingTable.getMenuSelection();
                    if (checkCanUpgrade(this.guild.buildings[idx2], null) && this.guild.buildings != null && this.guild.buildings.length > 0 && this.guild.buildings[idx2] != null) {
                        this.curUpgrade = idx2;
                        UWAPSegment segment = new UWAPSegment(23, 71);
                        try {
                            segment.writeByte(this.guild.buildings[idx2].id);
                        } catch (IOException e) {
                        }
                        Utilities.sendRequest(segment);
                        CommonComponent.showMessage("系统消息", -1, "正在升级" + this.guild.buildings[idx2].name, false, false, true);
                    }
                }
            }
        } else {
            this.commonY += this.commonSpeed;
            if (this.tab.getY() != World.viewHeight) {
                this.contentY -= this.contentSpeed;
            }
            this.tab.setY(this.tab.getY() - this.tabSpeed);
            if (this.tab.getY() < this._tabY) {
                this.tab.setY(this._tabY);
            }
            if (this.commonY > this._commonY) {
                this.commonY = this._commonY;
            }
            if (this.contentY < this._contentY) {
                this.contentY = this._contentY;
                this.show = true;
            }
        }
    }

    public void paint(Graphics g) {
        int i;
        int i2;
        int height = this.tab.getHeight();
        int off = World.tick % 8;
        if (off > 4) {
            int off2 = 7 - off;
        }
        int dx = this.x + Tool.uiMiscImg.getFrameWidth(48) + Tool.uiMiscImg.getFrameWidth(65) + 6;
        this.tab.paint(g);
        Tool.drawAlphaBox(g, World.viewWidth, this.commonHeight, 0, this.commonY, -436200402);
        int tabIdx = this.tab.getIdx();
        if (!(tabIdx == 1 || tabIdx == 3 || tabIdx == 2) || !this.show || this.closed) {
            g.setClip(this.x, this.commonY, 50, this.commonHeight - 3);
        }
        if (this.show) {
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
        }
        Tool.drawLevelString(g, "公会规模", this.x + 5, this.commonY + 5, 20, 1, 0);
        if (!this.show) {
            int i3 = dx;
            return;
        }
        int dx2 = this.x + (Tool.getFontSize(true) * 4) + 10;
        int dy = this.commonY + 12;
        int lvWidth = Utilities.font.stringWidth("公会名称 ");
        drawSmallPanel(g, dx2, (Utilities.LINE_HEIGHT + dy) - 5, lvWidth, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
        Tool.draw3DString(g, "公会名称 ", dx2 + 5, dy + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
        int dx3 = dx2 + lvWidth + 10;
        Tool.draw3DString(g, this.guild.name, dx3, dy + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
        Title currTitle = CommonData.player.getCurrTitle();
        int dx4 = dx3 + Utilities.font.stringWidth(this.guild.name) + 10;
        drawSmallPanel(g, dx4, (Utilities.LINE_HEIGHT + dy) - 5, Utilities.font.stringWidth("创始人 "), 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
        Tool.draw3DString(g, "创始人 ", dx4 + 5, dy + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
        Tool.draw3DString(g, this.guild.creator, dx4 + 10 + lvWidth, dy + Utilities.LINE_HEIGHT, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
        switch (tabIdx) {
            case 0:
                int dy2 = this.contentY + Utilities.LINE_HEIGHT + 10;
                String[] names = {"公会会长", "公会级别", "公会人数", "行动力", "人气度"};
                int dx5 = this.x + 10;
                for (int i4 = 0; i4 < 5; i4++) {
                    if (i4 > 2) {
                        dx5 = this.x + 10 + 169;
                    }
                    String nstr = names[i4];
                    drawSmallPanel(g, dx5, dy2 + ((i4 % 3) * (Utilities.LINE_HEIGHT + 10)), Utilities.font.stringWidth(nstr) + 10, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                    Tool.draw3DString(g, nstr, dx5 + 5, ((i4 % 3) * (Utilities.LINE_HEIGHT + 10)) + dy2 + 5, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                }
                int dx6 = this.x + 10;
                Tool.draw3DString(g, this.guild.leader, dx6 + lvWidth + 10, dy2 + 5, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                int dy3 = dy2 + Utilities.LINE_HEIGHT + 10;
                Tool.draw3DString(g, new StringBuilder(String.valueOf(this.guild.level)).toString(), dx6 + lvWidth + 10, dy3 + 5, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                Tool.draw3DString(g, String.valueOf(this.guild.amounts) + "/" + this.guild.amounts_max, dx6 + lvWidth + 10, dy3 + Utilities.LINE_HEIGHT + 10 + 5, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
                int dy4 = this.contentY + Utilities.LINE_HEIGHT + 10;
                int dx7 = dx6 + 169;
                String str = String.valueOf(this.guild.actions) + "/" + this.guild.actions_max;
                int i5 = dx7 + lvWidth + 10;
                int i6 = dy4 + 5;
                if (this.guild.actions < 20) {
                    i = 16711680;
                } else {
                    i = Tool.CLR_TABLE[10];
                }
                Tool.draw3DString(g, str, i5, i6, i, Tool.CLR_TABLE[11], 36);
                int dy5 = dy4 + Utilities.LINE_HEIGHT + 10;
                String str2 = String.valueOf(this.guild.active) + "/" + this.guild.active_max;
                int i7 = dx7 + lvWidth + 10;
                int i8 = dy5 + 5;
                if (this.guild.active < 50) {
                    i2 = 16711680;
                } else {
                    i2 = Tool.CLR_TABLE[10];
                }
                Tool.draw3DString(g, str2, i7, i8, i2, Tool.CLR_TABLE[11], 36);
                int i9 = dy5;
                break;
            case 1:
                if (!this.closed) {
                    this.resTable.paint(g);
                    break;
                }
                break;
            case 2:
                int dy6 = this.contentY + 10 + 15;
                int tw = ("联盟公会".length() * Tool.getFontSize(true)) + 10;
                int dx8 = this.x + 10;
                drawSmallPanel(g, dx8, dy6 - 5, tw, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                Tool.drawTitle(g, "联盟公会", dx8 + 5, dy6, AbstractUnit.CLR_NAME_NPC, 0, 36);
                int dy7 = dy6 + 10;
                if (this.guild.allied_ids != null && this.guild.allied_ids.length > 0) {
                    if (this.allied == null) {
                        StringBuffer sb = new StringBuffer();
                        for (int i10 = 0; i10 < this.guild.allied_ids.length; i10++) {
                            sb.append("<").append(this.guild.allied_names[i10]).append(">  ");
                        }
                        this.allied = new StringDraw(sb.toString(), this.width - 30, this.contentHeight / 2);
                    } else {
                        this.allied.draw3D(g, dx8, dy7, AbstractUnit.CLR_NAME_NPC, 0);
                    }
                }
                g.setColor(8947848);
                g.drawLine(this.x + 3, (this.contentHeight / 2) + this.contentY, (this.x + this.width) - 6, (this.contentHeight / 2) + this.contentY);
                int dy8 = (this.contentHeight / 2) + this.contentY + Utilities.LINE_HEIGHT;
                int dx9 = this.x + 10;
                drawSmallPanel(g, dx9, dy8 - 5, tw, 8, Tool.CLR_TABLE[17], Tool.CLR_TABLE[19]);
                Tool.drawTitle(g, "敌对公会", dx9 + 5, dy8, AbstractUnit.CLR_NAME_TAR_RED, 0, 36);
                int dy9 = dy8 + 10;
                if (this.guild.enemy_ids != null && this.guild.enemy_ids.length > 0) {
                    if (this.enemy != null) {
                        this.enemy.draw3D(g, dx9, dy9, AbstractUnit.CLR_NAME_TAR_RED, 0);
                        break;
                    } else {
                        StringBuffer sb2 = new StringBuffer();
                        for (int i11 = 0; i11 < this.guild.enemy_ids.length; i11++) {
                            sb2.append("<").append(this.guild.enemy_names[i11]).append(">  ");
                        }
                        this.enemy = new StringDraw(sb2.toString(), this.width - 30, this.contentHeight / 2);
                        break;
                    }
                }
            case 3:
                if (!this.closed) {
                    this.buildingTable.paint(g);
                    break;
                }
                break;
        }
        drawBtn(g, (this.contentY + this.contentHeight) - (Utilities.CHAR_HEIGHT >> 1));
    }

    public void handleCurrTab() {
        if (this.tab.getIdx() != 3) {
            setCmd(null, "关闭");
        } else if (this.guild.buildings != null) {
            if (checkCanUpgrade(this.guild.buildings[0], null)) {
                setCmd("升级", "关闭");
            } else {
                setCmd(null, "关闭");
            }
        }
    }

    public void upgradeGuildProperty(Building src) {
        this.guild.wood -= Building.howManyMoney(src.id, src.lv);
        this.guild.mineral -= Building.howManyMineral(src.id, src.lv);
        this.guild.money -= Building.howManyMoney(src.id, src.lv);
        upgradeBuildings(src);
    }

    private void upgradeBuildings(Building src) {
        for (int i = 0; i < this.guild.buildings.length; i++) {
            if (this.guild.buildings[i].id == src.id) {
                this.guild.buildings[i].upgrade();
            }
        }
    }

    public boolean checkCanUpgrade(Building src, boolean[] flag) {
        boolean ret = true;
        if (src.lv == src.lv_max) {
            return false;
        }
        if (this.guild.wood - Building.howManyWood(src.id, src.lv) < 0) {
            ret = false;
            if (flag != null) {
                flag[0] = false;
            }
        }
        if (this.guild.mineral - Building.howManyMineral(src.id, src.lv) < 0) {
            ret = false;
            if (flag != null) {
                flag[1] = false;
            }
        }
        if (this.guild.money - Building.howManyMoney(src.id, src.lv) < 0) {
            ret = false;
            if (flag != null) {
                flag[2] = false;
            }
        }
        if (!src.name.equals("要塞")) {
            int i = 0;
            while (true) {
                if (i >= this.guild.buildings.length) {
                    break;
                }
                Building b = this.guild.buildings[i];
                if (!b.name.equals("要塞") || src.lv <= b.lv) {
                    i++;
                } else {
                    if (flag != null) {
                        flag[3] = false;
                    }
                    ret = false;
                }
            }
        } else {
            for (int i2 = 0; i2 < this.guild.buildings.length; i2++) {
                if (Building.needCheckBuilding(this.guild.buildings[i2].id) && this.guild.buildings[i2].lv <= src.lv) {
                    ret = false;
                }
            }
        }
        return ret;
    }

    public void refreshUpgradeState() {
        if (this.curUpgrade != -1) {
            initBuildingTable();
            this.curUpgrade = -1;
        }
    }
}
