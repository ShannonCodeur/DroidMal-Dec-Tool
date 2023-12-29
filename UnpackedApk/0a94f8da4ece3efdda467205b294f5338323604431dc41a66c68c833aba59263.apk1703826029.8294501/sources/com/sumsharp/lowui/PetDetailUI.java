package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.common.data.Skill;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.item.GameItem;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Graphics;

public class PetDetailUI extends UI implements TabHandler {
    private static int[] resistBounds = new int[(Pet.ATTR_RESIST_TEXT.length * 2)];
    private static boolean resistBoundsInit = false;
    private int _commonY;
    private int _contentY;
    private int _tabY;
    private int commonHeight;
    private int commonSpeed;
    private int commonY;
    private int contentHeight;
    private int contentSpeed;
    private int contentY;
    private ImageSet img;
    private Menu itemMenu;
    private Table itemTable;
    String[] itemTitles;
    private int nameX;
    private int nameY;
    private int pageNo;
    private int pageSize;
    private int pageX;
    private ImageSet petIcon;
    private GameItem[] petItems;
    private Pet[] pets;
    private int resistIdx;
    private boolean resistTipShow;
    private boolean simpleMode;
    private int[] skillIds;
    private Table skillTable;
    String[] skillTitles;
    Tab tab;
    private int tabSpeed;
    private String[] tabTitles;
    private Tip tip;
    private int width;
    private int x;

    public PetDetailUI(Pet pet, boolean simple) {
        this.tabTitles = null;
        this.petIcon = ImageLoadManager.getImage("petIcons.jgp");
        this.skillTitles = new String[]{"", "技能", "等级", "类型", "MP"};
        this.itemTitles = new String[]{"", "装备名称", "部位"};
        this.simpleMode = false;
        this.resistTipShow = false;
        this.simpleMode = simple;
        this.pets = new Pet[1];
        this.pets[0] = pet;
        this.pageSize = 1;
        this.pageNo = 0;
        init();
    }

    public PetDetailUI(Object[] pet) {
        this.tabTitles = null;
        this.petIcon = ImageLoadManager.getImage("petIcons.jgp");
        this.skillTitles = new String[]{"", "技能", "等级", "类型", "MP"};
        this.itemTitles = new String[]{"", "装备名称", "部位"};
        this.simpleMode = false;
        this.resistTipShow = false;
        if (pet == null) {
            this.pageSize = 1;
        } else {
            this.pets = new Pet[pet.length];
            System.arraycopy(pet, 0, this.pets, 0, this.pets.length);
            this.pageSize = pet.length;
        }
        this.pageNo = 0;
        init();
    }

    public PetDetailUI() {
        this.tabTitles = null;
        this.petIcon = ImageLoadManager.getImage("petIcons.jgp");
        this.skillTitles = new String[]{"", "技能", "等级", "类型", "MP"};
        this.itemTitles = new String[]{"", "装备名称", "部位"};
        this.simpleMode = false;
        this.resistTipShow = false;
        this.pageSize = CommonData.player.getPetIDs().length;
        this.pageNo = 0;
        this.pets = new Pet[this.pageSize];
        CommonData.player.pets.copyInto(this.pets);
        init();
    }

    public void reset() {
        int idx = this.tab.getIdx();
        if (idx == 2 && this.itemTable != null) {
            this.pets = new Pet[this.pageSize];
            CommonData.player.pets.copyInto(this.pets);
            initItemTable(this.pets[this.pageNo].items);
        }
        if (idx == 1) {
            initSkillTable();
        }
        super.reset();
    }

    public int getMenuSelection() {
        int idx = this.tab.getIdx();
        if (idx == 1 && this.skillTable != null) {
            return this.skillTable.getMenuSelection();
        }
        if (idx != 2 || this.itemTable == null) {
            return -1;
        }
        return this.itemTable.getMenuSelection();
    }

    private void init() {
        this.x = 3;
        this.width = World.viewWidth - (this.x * 2);
        this.tabTitles = new String[]{"属性", "技能", "装备", "抗性", "成长"};
        if (this.pets[this.pageNo].matingTimes > 0) {
            this.tabTitles = new String[]{"属性", "技能", "装备", "抗性", "成长", "继承"};
        }
        if (this.simpleMode) {
            this.tabTitles = new String[]{"继承"};
        }
        this.tab = new Tab("tab", this.tabTitles, this.x, this.width, true, this);
        this.commonY = NewStage.screenY;
        this.commonHeight = 52;
        int tabY = this.commonY + this.commonHeight;
        int tabHeight = this.tab.getHeight();
        this.tab.setY(tabY);
        this.contentY = tabY + tabHeight;
        this.contentHeight = ((World.viewHeight - this.contentY) - CornerButton.instance.getHeight()) - 2;
        this.tab.setContentHeight(this.contentHeight);
        this.nameX = 0;
        this.nameY = (this.commonY - (tabHeight >> 1)) + 2;
        this._tabY = this.tab.getY();
        this._commonY = this.commonY;
        this._contentY = this.contentY;
        int[] widths = new int[5];
        widths[1] = -1;
        widths[2] = 35;
        widths[3] = Utilities.font.stringWidth("主动") + 10;
        widths[4] = 32;
        this.skillTable = new Table(this.x, this.contentY, World.viewWidth - 6, this.contentHeight, this.skillTitles, widths);
        this.itemTable = new Table(this.x, this.contentY, World.viewWidth - 6, this.contentHeight, this.itemTitles, new int[]{45, -1, Utilities.font.stringWidth("部位") + 10});
        this.itemTable.setParent(this);
        this.tab.setY(World.viewHeight);
        this.commonY = -this.commonHeight;
        this.contentY = World.viewHeight + tabHeight;
        this.commonSpeed = (this._commonY - this.commonY) >> 1;
        this.tabSpeed = (this.tab.getY() - this._tabY) / 2;
        this.contentSpeed = (this.contentY - this._contentY) / 2;
        this.nameX -= (Utilities.font.stringWidth(this.pets[this.pageNo].name) + Utilities.LINE_HEIGHT) + 20;
        this.pageX = World.viewWidth;
        this.show = false;
        this.skillTable.setHasTab(true);
        this.itemTable.setHasTab(true);
    }

    private boolean handleSelPet() {
        if (World.pressedx == -1 && World.pressedy == -1) {
            return false;
        }
        int px = World.pressedx - this.x;
        int py = World.pressedy - this.commonY;
        if (py <= 0 || py >= 50 || px <= 0 || px >= CommonData.player.pets.size() * 50 || 0 >= CommonData.player.pets.size()) {
            return true;
        }
        setPageNo(px / 50);
        return true;
    }

    private void handlePoints() {
        handleSelPet();
        this.tab.cycle();
        if (World.pressedx != -1 || World.pressedy != -1) {
            int px = World.pressedx;
            int py = World.pressedy;
            if (0 == 0) {
                switch (this.tab.getIdx()) {
                    case 1:
                        if (this.skillTable != null) {
                            if (this.show && !this.skillTable.show) {
                                this.skillTable.show = this.show;
                            }
                            this.skillTable.handlePoints(px, py);
                            return;
                        }
                        return;
                    case 2:
                        if (this.itemTable != null) {
                            if (this.show && !this.itemTable.show) {
                                this.itemTable.show = this.show;
                            }
                            this.itemTable.handlePoints(World.pressedx, World.pressedy);
                            return;
                        }
                        return;
                    case 3:
                        handleResistTouch(px, py);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void cycle() {
        String content;
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
            if (this.pageX - 20 > World.viewWidth - 80) {
                this.pageX -= 20;
            } else {
                this.pageX = World.viewWidth - 80;
            }
            this.nameY = (this.commonY - (this.tab.getHeight() >> 1)) + 2;
            if (this.nameX + 20 < 0) {
                this.nameX += 20;
            } else {
                this.nameX = 0;
            }
            handlePoints();
            moveBtn();
            int idx = this.tab.getIdx();
            if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(13, true)) {
                prevPage();
            } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(15, true)) {
                nextPage();
            }
            if (idx == 1) {
                if (this.show && !this.skillTable.show) {
                    this.skillTable.show = this.show;
                }
                this.skillTable.cycle();
            } else if (idx == 2) {
                if (this.show && !this.itemTable.show) {
                    this.itemTable.show = this.show;
                }
                this.itemTable.cycle();
            } else if (idx == 3) {
                int len = Pet.ATTR_RESIST_TEXT.length;
                if (this.resistTipShow && this.tip != null) {
                    this.tip.cycle();
                }
                if (Utilities.isKeyPressed(1, true)) {
                    this.resistIdx++;
                    if (this.resistIdx > len - 1) {
                        this.resistIdx = 0;
                    }
                    this.resistTipShow = false;
                } else if (Utilities.isKeyPressed(0, true)) {
                    this.resistIdx--;
                    if (this.resistIdx < 0) {
                        this.resistIdx = len - 1;
                    }
                    this.resistTipShow = false;
                } else if (Utilities.isKeyPressed(4, true)) {
                    this.resistTipShow = !this.resistTipShow;
                    if (this.show) {
                        int num = this.pets[this.pageNo].getAttribute(Pet.ATTR_RESISTS[this.resistIdx]);
                        int num2 = (num * 10000) / (num + HttpConnection.HTTP_MULT_CHOICE);
                        int n1 = num2 / 100;
                        int n2 = num2 % 100;
                        if (this.resistIdx < 6) {
                            content = String.valueOf(Pet.ATTR_RESIST_TEXT[this.resistIdx]) + "属性魔法伤害减少";
                        } else {
                            content = "敌人对你施放" + Pet.ATTR_RESIST_TEXT[this.resistIdx] + "命中减低";
                        }
                        this.tip = Tip.createTip(String.valueOf(content) + (n1 + (n2 > 50 ? 1 : 0)) + "%", this.width - 10);
                        int dx = resistBounds[this.resistIdx * 2];
                        int dy = resistBounds[(this.resistIdx * 2) + 1];
                        if (resistBounds != null && this.resistTipShow) {
                            this.tip.setBounds(9, dy + 3, World.viewWidth - 18, dx, false);
                        }
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
                this.nameX = 0;
            }
        }
    }

    private void initSkillTable() {
        Pet pet = this.pets[this.pageNo];
        int len = pet.skills.size();
        String[] names = new String[len];
        String[] lvs = new String[len];
        String[] types = new String[len];
        String[] mps = new String[len];
        String[] ids = new String[len];
        this.skillIds = new int[len];
        for (int i = 0; i < len; i++) {
            Skill skill = (Skill) pet.skills.elementAt(i);
            names[i] = skill.name;
            lvs[i] = String.valueOf(skill.level);
            if (skill.type == 0) {
                types[i] = "主动";
            } else if (skill.type == 1) {
                types[i] = "被动";
            } else if (skill.type == 2) {
                types[i] = "反击";
            }
            mps[i] = String.valueOf(skill.mp);
            ids[i] = String.valueOf(skill.id) + "," + skill.level;
            this.skillIds[i] = skill.id;
        }
        this.skillTable.addTableItem("", ids, 14, null, null);
        this.skillTable.addTableItem("技能", names, 0, null, null);
        this.skillTable.addTableItem("等级", lvs, 11, null, null);
        this.skillTable.addTableItem("类型", types, 0, null, null);
        this.skillTable.addTableItem("MP", mps, 1, null, null);
        this.skillTable.setOriPos();
        setCmd("菜单", "返回");
    }

    public void initItemTable(GameItem[] items) {
        int len = items.length;
        String[] ids = new String[len];
        String[] names = new String[len];
        String[] types = new String[len];
        for (int i = 0; i < len; i++) {
            ids[i] = String.valueOf(items[i].id) + "," + items[i].iconId + "," + this.pets[this.pageNo].id;
            names[i] = items[i].name;
            types[i] = new StringBuilder(String.valueOf(items[i].part)).toString();
        }
        this.itemTable.addTableItem(this.itemTitles[0], ids, 16, null, null);
        this.itemTable.addTableItem(this.itemTitles[1], names, 0, null, null);
        this.itemTable.addTableItem(this.itemTitles[2], types, 0, null, null);
        this.itemTable.setOriPos();
        this.itemMenu = new Menu("装备", new String[]{"卸下装备", "选择装备"}, null, 8);
    }

    public void nextPage() {
        this.pageNo++;
        if (this.pageNo >= this.pageSize) {
            this.pageNo = this.pageSize - 1;
        }
        this.tab.init();
        this.tab.refreshContent();
    }

    public void prevPage() {
        this.pageNo--;
        if (this.pageNo < 0) {
            this.pageNo = 0;
        }
        this.tab.init();
        this.tab.refreshContent();
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void paint(javax.microedition.lcdui.Graphics r69) {
        /*
            r68 = this;
            r0 = r68
            com.sumsharp.monster.common.data.Pet[] r0 = r0.pets
            r5 = r0
            r0 = r68
            int r0 = r0.pageNo
            r6 = r0
            r55 = r5[r6]
            r0 = r68
            com.sumsharp.lowui.Tab r0 = r0.tab
            r5 = r0
            int r62 = r5.getHeight()
            int r5 = com.sumsharp.monster.World.tick
            int r51 = r5 % 8
            r5 = 4
            r0 = r51
            r1 = r5
            if (r0 <= r1) goto L_0x0022
            r5 = 7
            int r51 = r5 - r51
        L_0x0022:
            r0 = r68
            int r0 = r0.x
            r5 = r0
            com.sumsharp.monster.image.ImageSet r6 = com.sumsharp.monster.common.Tool.uiMiscImg
            r7 = 48
            int r6 = r6.getFrameWidth(r7)
            int r5 = r5 + r6
            com.sumsharp.monster.image.ImageSet r6 = com.sumsharp.monster.common.Tool.uiMiscImg
            r7 = 65
            int r6 = r6.getFrameWidth(r7)
            int r5 = r5 + r6
            int r40 = r5 + 6
            r41 = 0
            r22 = 0
            r46 = 0
            r0 = r68
            com.sumsharp.lowui.Tab r0 = r0.tab
            r5 = r0
            r0 = r5
            r1 = r69
            r0.paint(r1)
            int r6 = com.sumsharp.monster.World.viewWidth
            r0 = r68
            int r0 = r0.commonHeight
            r7 = r0
            r8 = 0
            r0 = r68
            int r0 = r0.commonY
            r9 = r0
            r10 = -436200402(0xffffffffe6001c2e, float:-1.5124568E23)
            r5 = r69
            com.sumsharp.monster.common.Tool.drawAlphaBox(r5, r6, r7, r8, r9, r10)
            r0 = r68
            com.sumsharp.lowui.Tab r0 = r0.tab
            r5 = r0
            int r47 = r5.getIdx()
            r0 = r68
            boolean r0 = r0.show
            r5 = r0
            if (r5 == 0) goto L_0x007e
            r46 = 0
        L_0x0073:
            r0 = r68
            com.sumsharp.monster.common.data.Pet[] r0 = r0.pets
            r5 = r0
            int r5 = r5.length
            r0 = r46
            r1 = r5
            if (r0 < r1) goto L_0x0099
        L_0x007e:
            r5 = 0
            r6 = 0
            int r7 = com.sumsharp.monster.World.viewWidth
            int r8 = com.sumsharp.monster.World.viewHeight
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r7
            r4 = r8
            r0.setClip(r1, r2, r3, r4)
            r0 = r68
            boolean r0 = r0.show
            r5 = r0
            if (r5 != 0) goto L_0x00fd
            r8 = r41
            r7 = r40
        L_0x0098:
            return
        L_0x0099:
            r0 = r68
            int r0 = r0.pageNo
            r5 = r0
            r0 = r46
            r1 = r5
            if (r0 != r1) goto L_0x00e1
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r5 = r5 + 5
            int r6 = r46 * 45
            int r6 = r6 + r5
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            int r7 = r5 + 6
            r8 = 40
            r9 = 40
            r10 = 1
            r5 = r69
            com.sumsharp.monster.common.Tool.drawBlurRect(r5, r6, r7, r8, r9, r10)
        L_0x00be:
            r0 = r68
            com.sumsharp.monster.common.data.Pet[] r0 = r0.pets
            r5 = r0
            r5 = r5[r46]
            r0 = r68
            int r0 = r0.x
            r6 = r0
            int r6 = r6 + 25
            int r7 = r46 * 45
            int r6 = r6 + r7
            r0 = r68
            int r0 = r0.commonY
            r7 = r0
            r8 = 5
            int r7 = r7 - r8
            r0 = r5
            r1 = r69
            r2 = r6
            r3 = r7
            com.sumsharp.monster.common.Tool.drawPet(r0, r1, r2, r3)
            int r46 = r46 + 1
            goto L_0x0073
        L_0x00e1:
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r5 = r5 + 5
            int r6 = r46 * 45
            int r6 = r6 + r5
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            int r7 = r5 + 6
            r8 = 40
            r9 = 40
            r10 = 0
            r5 = r69
            com.sumsharp.monster.common.Tool.drawBlurRect(r5, r6, r7, r8, r9, r10)
            goto L_0x00be
        L_0x00fd:
            r0 = r68
            boolean r0 = r0.simpleMode
            r5 = r0
            if (r5 == 0) goto L_0x01f3
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r7 = r5 + 52
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            int r6 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r5 = r5 + r6
            int r8 = r5 + 8
            java.lang.String r6 = "抗性继承"
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r9 = 10
            r9 = r5[r9]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r10 = 11
            r10 = r5[r10]
            r11 = 36
            r5 = r69
            com.sumsharp.monster.common.Tool.draw3DString(r5, r6, r7, r8, r9, r10, r11)
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            int r6 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r5 = r5 + r6
            int r8 = r5 + 22
            r0 = r55
            java.util.Vector r0 = r0.resistChange
            r5 = r0
            int r5 = r5.size()
            if (r5 != 0) goto L_0x0185
            java.lang.String r6 = "无"
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r9 = 10
            r9 = r5[r9]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r10 = 11
            r10 = r5[r10]
            r11 = 36
            r5 = r69
            com.sumsharp.monster.common.Tool.draw3DString(r5, r6, r7, r8, r9, r10, r11)
            r67 = r22
        L_0x0156:
            r0 = r68
            boolean r0 = r0.simpleMode
            r5 = r0
            if (r5 == 0) goto L_0x0166
            r0 = r68
            com.sumsharp.lowui.Tab r0 = r0.tab
            r5 = r0
            r6 = 4
            r5.setTabIndex(r6)
        L_0x0166:
            switch(r47) {
                case 0: goto L_0x034d;
                case 1: goto L_0x063c;
                case 2: goto L_0x0652;
                case 3: goto L_0x0668;
                case 4: goto L_0x074c;
                case 5: goto L_0x0896;
                default: goto L_0x0169;
            }
        L_0x0169:
            r22 = r67
        L_0x016b:
            r0 = r68
            int r0 = r0.contentY
            r5 = r0
            r0 = r68
            int r0 = r0.contentHeight
            r6 = r0
            int r5 = r5 + r6
            int r6 = com.sumsharp.monster.common.Utilities.CHAR_HEIGHT
            int r6 = r6 >> 1
            int r5 = r5 - r6
            r0 = r68
            r1 = r69
            r2 = r5
            r0.drawBtn(r1, r2)
            goto L_0x0098
        L_0x0185:
            r46 = 0
        L_0x0187:
            byte[] r5 = com.sumsharp.monster.common.data.Pet.ATTR_RESISTS
            int r5 = r5.length
            r0 = r46
            r1 = r5
            if (r0 < r1) goto L_0x0192
            r67 = r22
            goto L_0x0156
        L_0x0192:
            r57 = 0
        L_0x0194:
            r0 = r55
            java.util.Vector r0 = r0.resistChange
            r5 = r0
            int r5 = r5.size()
            r0 = r57
            r1 = r5
            if (r0 < r1) goto L_0x01a5
            int r46 = r46 + 1
            goto L_0x0187
        L_0x01a5:
            r0 = r55
            java.util.Vector r0 = r0.resistChange
            r5 = r0
            r0 = r5
            r1 = r57
            java.lang.Object r36 = r0.elementAt(r1)
            java.lang.Byte r36 = (java.lang.Byte) r36
            byte r5 = r36.byteValue()
            byte[] r6 = com.sumsharp.monster.common.data.Pet.ATTR_RESISTS
            byte r6 = r6[r46]
            if (r5 != r6) goto L_0x01f0
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String[] r6 = com.sumsharp.monster.common.data.Pet.ATTR_RESIST_TEXT
            r6 = r6[r46]
            java.lang.String r6 = java.lang.String.valueOf(r6)
            r5.<init>(r6)
            java.lang.String r6 = "抗性提高"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = r5.toString()
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r9 = 10
            r9 = r5[r9]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r10 = 11
            r10 = r5[r10]
            r11 = 36
            r5 = r69
            com.sumsharp.monster.common.Tool.draw3DString(r5, r6, r7, r8, r9, r10, r11)
            javax.microedition.lcdui.Font r5 = com.sumsharp.monster.common.Utilities.font
            int r5 = r5.stringWidth(r6)
            int r5 = r5 + 5
            int r7 = r7 + r5
        L_0x01f0:
            int r57 = r57 + 1
            goto L_0x0194
        L_0x01f3:
            r0 = r55
            java.lang.String r0 = r0.name
            r10 = r0
            int r5 = com.sumsharp.monster.World.viewWidth
            int r11 = r5 / 2
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            r0 = r68
            int r0 = r0.commonHeight
            r6 = r0
            int r6 = r6 / 2
            int r5 = r5 + r6
            r6 = 5
            int r12 = r5 - r6
            r13 = 16776960(0xffff00, float:2.3509528E-38)
            r14 = 0
            r15 = 40
            r9 = r69
            com.sumsharp.monster.common.Tool.draw3DString(r9, r10, r11, r12, r13, r14, r15)
            java.lang.String[] r5 = com.sumsharp.monster.common.data.Pet.MONSTER_CLASS_NAME
            r6 = 52
            r0 = r55
            r1 = r6
            int r6 = r0.getAttribute(r1)
            r10 = r5[r6]
            int r5 = com.sumsharp.monster.World.viewWidth
            int r11 = r5 / 2
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            r0 = r68
            int r0 = r0.commonHeight
            r6 = r0
            int r6 = r6 / 2
            int r5 = r5 + r6
            int r12 = r5 + 5
            r13 = 16776960(0xffff00, float:2.3509528E-38)
            r14 = 0
            r15 = 24
            r9 = r69
            com.sumsharp.monster.common.Tool.draw3DString(r9, r10, r11, r12, r13, r14, r15)
            int r5 = com.sumsharp.monster.World.viewWidth
            int r5 = r5 / 2
            int r7 = r5 + 10
            java.lang.String r10 = "等级:"
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            r0 = r68
            int r0 = r0.commonHeight
            r6 = r0
            int r6 = r6 / 2
            int r5 = r5 + r6
            r6 = 5
            int r12 = r5 - r6
            r13 = 16776960(0xffff00, float:2.3509528E-38)
            r14 = 0
            r15 = 36
            r9 = r69
            r11 = r7
            com.sumsharp.monster.common.Tool.draw3DString(r9, r10, r11, r12, r13, r14, r15)
            int r5 = com.sumsharp.monster.World.viewWidth
            int r5 = r5 / 2
            javax.microedition.lcdui.Font r6 = r69.getFont()
            java.lang.String r7 = "等级:"
            int r6 = r6.stringWidth(r7)
            int r5 = r5 + r6
            int r7 = r5 + 10
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r6 = 50
            r0 = r55
            r1 = r6
            int r6 = r0.getAttribute(r1)
            java.lang.String r6 = java.lang.String.valueOf(r6)
            r5.<init>(r6)
            java.lang.String r6 = "/"
            java.lang.StringBuilder r5 = r5.append(r6)
            r0 = r55
            int r0 = r0.maxLevel
            r6 = r0
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r9 = r5.toString()
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            r0 = r68
            int r0 = r0.commonHeight
            r6 = r0
            int r6 = r6 / 2
            int r5 = r5 + r6
            r6 = 5
            int r12 = r5 - r6
            r13 = 0
            r14 = 36
            r15 = -1
            r10 = r69
            r11 = r7
            com.sumsharp.monster.common.Tool.drawNumStr(r9, r10, r11, r12, r13, r14, r15)
            int r5 = com.sumsharp.monster.common.Tool.getSmallNumWidth(r9)
            int r7 = r7 + r5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "+"
            r5.<init>(r6)
            r0 = r55
            byte r0 = r0.matingTimes
            r6 = r0
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r10 = r5.toString()
            int r12 = r7 + 3
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            r0 = r68
            int r0 = r0.commonHeight
            r6 = r0
            int r6 = r6 / 2
            int r5 = r5 + r6
            r6 = 5
            int r13 = r5 - r6
            r14 = 0
            r15 = 36
            r16 = -1
            r11 = r69
            com.sumsharp.monster.common.Tool.drawNumStr(r10, r11, r12, r13, r14, r15, r16)
            r0 = r68
            int r0 = r0.commonY
            r5 = r0
            int r6 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r5 = r5 + r6
            int r8 = r5 + 25
            int r5 = com.sumsharp.monster.World.viewWidth
            int r5 = r5 / 2
            int r7 = r5 + 10
            r5 = 58
            r0 = r55
            r1 = r5
            int r56 = r0.getAttribute(r1)
            com.sumsharp.monster.image.ImageSet r5 = com.sumsharp.monster.common.Tool.uiMiscImg
            r6 = 43
            int r67 = r5.getFrameWidth(r6)
            r46 = 0
            int r38 = r56 + 1
        L_0x0310:
            r0 = r46
            r1 = r38
            if (r0 <= r1) goto L_0x0332
            r18 = 0
            r0 = r55
            byte r0 = r0.sex
            r5 = r0
            if (r5 != 0) goto L_0x034a
            r18 = 42
        L_0x0321:
            com.sumsharp.monster.image.ImageSet r16 = com.sumsharp.monster.common.Tool.uiMiscImg
            r21 = 0
            r22 = 6
            r17 = r69
            r19 = r7
            r20 = r8
            r16.drawFrame(r17, r18, r19, r20, r21, r22)
            goto L_0x0156
        L_0x0332:
            r15 = 0
            int r5 = r46 % 2
            if (r5 == 0) goto L_0x0338
            r15 = 2
        L_0x0338:
            com.sumsharp.monster.image.ImageSet r10 = com.sumsharp.monster.common.Tool.uiMiscImg
            r12 = 43
            r16 = 6
            r11 = r69
            r13 = r7
            r14 = r8
            r10.drawFrame(r11, r12, r13, r14, r15, r16)
            int r7 = r7 + r67
            int r46 = r46 + 1
            goto L_0x0310
        L_0x034a:
            r18 = 41
            goto L_0x0321
        L_0x034d:
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r7 = r5 + 8
            r0 = r68
            int r0 = r0.contentY
            r5 = r0
            int r8 = r5 + 3
            r45 = 150(0x96, float:2.1E-43)
            r5 = 8
            r0 = r5
            byte[] r0 = new byte[r0]
            r35 = r0
            r35 = {1, 2, 3, 4, 5, 7, 8, 27} // fill-array
            r33 = 0
        L_0x0369:
            r0 = r35
            int r0 = r0.length
            r5 = r0
            r0 = r33
            r1 = r5
            if (r0 < r1) goto L_0x05af
            int r7 = r7 + 92
            r0 = r68
            int r0 = r0.contentY
            r5 = r0
            int r8 = r5 + 15
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r6 = 23
            r0 = r55
            r1 = r6
            int r6 = r0.getAttribute(r1)
            java.lang.String r6 = java.lang.String.valueOf(r6)
            r5.<init>(r6)
            java.lang.String r6 = "/"
            java.lang.StringBuilder r5 = r5.append(r6)
            r6 = 25
            r0 = r55
            r1 = r6
            int r6 = r0.getAttribute(r1)
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r44 = r5.toString()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r6 = 24
            r0 = r55
            r1 = r6
            int r6 = r0.getAttribute(r1)
            java.lang.String r6 = java.lang.String.valueOf(r6)
            r5.<init>(r6)
            java.lang.String r6 = "/"
            java.lang.StringBuilder r5 = r5.append(r6)
            r6 = 26
            r0 = r55
            r1 = r6
            int r6 = r0.getAttribute(r1)
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r50 = r5.toString()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r0 = r55
            long r0 = r0.exp
            r10 = r0
            java.lang.String r6 = java.lang.String.valueOf(r10)
            r5.<init>(r6)
            java.lang.String r6 = "/"
            java.lang.StringBuilder r5 = r5.append(r6)
            r0 = r55
            long r0 = r0.levelupExp
            r10 = r0
            java.lang.StringBuilder r5 = r5.append(r10)
            java.lang.String r42 = r5.toString()
            com.sumsharp.monster.image.ImageSet r19 = com.sumsharp.monster.common.Tool.uiMiscImg
            r21 = 72
            r24 = 0
            r25 = 36
            r20 = r69
            r22 = r7
            r23 = r8
            r19.drawFrame(r20, r21, r22, r23, r24, r25)
            int r21 = r7 + 26
            r23 = 0
            r24 = 36
            r25 = -1
            r19 = r44
            r20 = r69
            r22 = r8
            com.sumsharp.monster.common.Tool.drawNumStr(r19, r20, r21, r22, r23, r24, r25)
            r5 = 10296(0x2838, float:1.4428E-41)
            r0 = r69
            r1 = r5
            r0.setColor(r1)
            int r5 = r8 + 3
            r6 = 101(0x65, float:1.42E-43)
            r9 = 7
            r0 = r69
            r1 = r7
            r2 = r5
            r3 = r6
            r4 = r9
            r0.drawRect(r1, r2, r3, r4)
            r5 = 23
            r0 = r55
            r1 = r5
            int r5 = r0.getAttribute(r1)
            int r5 = r5 * 100
            r6 = 25
            r0 = r55
            r1 = r6
            int r6 = r0.getAttribute(r1)
            int r65 = r5 / r6
            if (r65 <= 0) goto L_0x0456
            r5 = 15245456(0xe8a090, float:2.1363434E-38)
            r0 = r69
            r1 = r5
            r0.setColor(r1)
            int r5 = r7 + 1
            int r6 = r8 + 4
            r9 = 6
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r65
            r4 = r9
            r0.fillRect(r1, r2, r3, r4)
        L_0x0456:
            int r8 = r8 + 24
            com.sumsharp.monster.image.ImageSet r19 = com.sumsharp.monster.common.Tool.uiMiscImg
            r21 = 73
            r24 = 0
            r25 = 36
            r20 = r69
            r22 = r7
            r23 = r8
            r19.drawFrame(r20, r21, r22, r23, r24, r25)
            int r21 = r7 + 26
            r23 = 0
            r24 = 36
            r25 = -1
            r19 = r50
            r20 = r69
            r22 = r8
            com.sumsharp.monster.common.Tool.drawNumStr(r19, r20, r21, r22, r23, r24, r25)
            r5 = 10296(0x2838, float:1.4428E-41)
            r0 = r69
            r1 = r5
            r0.setColor(r1)
            int r5 = r8 + 3
            r6 = 101(0x65, float:1.42E-43)
            r9 = 7
            r0 = r69
            r1 = r7
            r2 = r5
            r3 = r6
            r4 = r9
            r0.drawRect(r1, r2, r3, r4)
            r5 = 24
            r0 = r55
            r1 = r5
            int r5 = r0.getAttribute(r1)
            int r5 = r5 * 100
            r6 = 26
            r0 = r55
            r1 = r6
            int r6 = r0.getAttribute(r1)
            int r65 = r5 / r6
            if (r65 <= 0) goto L_0x04c0
            r5 = 4761768(0x48a8a8, float:6.672658E-39)
            r0 = r69
            r1 = r5
            r0.setColor(r1)
            int r5 = r7 + 1
            int r6 = r8 + 4
            r9 = 6
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r65
            r4 = r9
            r0.fillRect(r1, r2, r3, r4)
        L_0x04c0:
            int r8 = r8 + 24
            r53 = 0
            r0 = r55
            long r0 = r0.levelupExp
            r10 = r0
            r12 = 0
            int r5 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r5 <= 0) goto L_0x04de
            r0 = r55
            long r0 = r0.exp
            r10 = r0
            r12 = 10000(0x2710, double:4.9407E-320)
            long r10 = r10 * r12
            r0 = r55
            long r0 = r0.levelupExp
            r12 = r0
            long r53 = r10 / r12
        L_0x04de:
            java.lang.String r52 = java.lang.String.valueOf(r53)
            int r5 = r52.length()
            r6 = 3
            if (r5 >= r6) goto L_0x04f8
            r5 = 3
            int r6 = r52.length()
            int r58 = r5 - r6
            r39 = 0
        L_0x04f2:
            r0 = r39
            r1 = r58
            if (r0 < r1) goto L_0x0626
        L_0x04f8:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r6 = 0
            int r9 = r52.length()
            r10 = 2
            int r9 = r9 - r10
            r0 = r52
            r1 = r6
            r2 = r9
            java.lang.String r6 = r0.substring(r1, r2)
            java.lang.String r6 = java.lang.String.valueOf(r6)
            r5.<init>(r6)
            java.lang.String r6 = "."
            java.lang.StringBuilder r5 = r5.append(r6)
            int r6 = r52.length()
            r9 = 2
            int r6 = r6 - r9
            r0 = r52
            r1 = r6
            java.lang.String r6 = r0.substring(r1)
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "%"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r52 = r5.toString()
            com.sumsharp.monster.image.ImageSet r19 = com.sumsharp.monster.common.Tool.uiMiscImg
            r21 = 74
            r24 = 0
            r25 = 36
            r20 = r69
            r22 = r7
            r23 = r8
            r19.drawFrame(r20, r21, r22, r23, r24, r25)
            com.sumsharp.monster.image.ImageSet r5 = com.sumsharp.monster.common.Tool.uiMiscImg
            r6 = 74
            int r5 = r5.getFrameWidth(r6)
            int r5 = r5 + r7
            int r21 = r5 + 5
            r23 = 0
            r24 = 36
            r25 = -1
            r19 = r52
            r20 = r69
            r22 = r8
            com.sumsharp.monster.common.Tool.drawNumStr(r19, r20, r21, r22, r23, r24, r25)
            r5 = 10296(0x2838, float:1.4428E-41)
            r0 = r69
            r1 = r5
            r0.setColor(r1)
            int r5 = r8 + 3
            r6 = 101(0x65, float:1.42E-43)
            r9 = 7
            r0 = r69
            r1 = r7
            r2 = r5
            r3 = r6
            r4 = r9
            r0.drawRect(r1, r2, r3, r4)
            r65 = 0
            r0 = r55
            long r0 = r0.levelupExp
            r10 = r0
            r12 = 0
            int r5 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r5 <= 0) goto L_0x0591
            r0 = r55
            long r0 = r0.exp
            r10 = r0
            r12 = 100
            long r10 = r10 * r12
            r0 = r55
            long r0 = r0.levelupExp
            r12 = r0
            long r10 = r10 / r12
            r0 = r10
            int r0 = (int) r0
            r65 = r0
        L_0x0591:
            if (r65 <= 0) goto L_0x0169
            r5 = 16113524(0xf5df74, float:2.2579856E-38)
            r0 = r69
            r1 = r5
            r0.setColor(r1)
            int r5 = r7 + 1
            int r6 = r8 + 4
            r9 = 6
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r65
            r4 = r9
            r0.fillRect(r1, r2, r3, r4)
            r22 = r67
            goto L_0x016b
        L_0x05af:
            java.lang.String[] r5 = com.sumsharp.monster.common.data.Pet.ATTR_TEXT
            byte r6 = r35[r33]
            r59 = r5[r6]
            int r5 = com.sumsharp.monster.World.viewHeight
            com.sumsharp.android.ui.DirectionPad r6 = com.sumsharp.android.ui.DirectionPad.instance
            int r6 = r6.getHeight()
            int r5 = r5 - r6
            int r6 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r6 = r6 + 2
            int r5 = r5 - r6
            if (r8 <= r5) goto L_0x05d3
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r7 = r5 + 92
            r0 = r68
            int r0 = r0.contentY
            r5 = r0
            int r8 = r5 + 3
        L_0x05d3:
            r5 = 4
            int r20 = r7 - r5
            int r21 = r8 + 7
            r22 = 80
            r23 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r24 = r5[r6]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r25 = r5[r6]
            r19 = r69
            drawSmallPanel(r19, r20, r21, r22, r23, r24, r25)
            r23 = 16776397(0xfffccd, float:2.350874E-38)
            r24 = 0
            r19 = r69
            r20 = r59
            r21 = r7
            r22 = r8
            com.sumsharp.monster.common.Tool.draw3DString(r19, r20, r21, r22, r23, r24)
            byte r5 = r35[r33]
            r0 = r55
            r1 = r5
            int r5 = r0.getAttribute(r1)
            java.lang.String r19 = java.lang.String.valueOf(r5)
            int r5 = com.sumsharp.monster.common.Utilities.CHAR_HEIGHT
            int r5 = r5 * 2
            int r21 = r7 + r5
            int r22 = r8 + 12
            r23 = 0
            r24 = 36
            r25 = -1
            r20 = r69
            com.sumsharp.monster.common.Tool.drawNumStr(r19, r20, r21, r22, r23, r24, r25)
            int r5 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r5 = r5 + 2
            int r8 = r8 + r5
            int r33 = r33 + 1
            goto L_0x0369
        L_0x0626:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "0"
            r5.<init>(r6)
            r0 = r5
            r1 = r52
            java.lang.StringBuilder r5 = r0.append(r1)
            java.lang.String r52 = r5.toString()
            int r39 = r39 + 1
            goto L_0x04f2
        L_0x063c:
            r0 = r68
            boolean r0 = r0.closed
            r5 = r0
            if (r5 != 0) goto L_0x0169
            r0 = r68
            com.sumsharp.lowui.Table r0 = r0.skillTable
            r5 = r0
            r0 = r5
            r1 = r69
            r0.paint(r1)
            r22 = r67
            goto L_0x016b
        L_0x0652:
            r0 = r68
            boolean r0 = r0.closed
            r5 = r0
            if (r5 != 0) goto L_0x0169
            r0 = r68
            com.sumsharp.lowui.Table r0 = r0.itemTable
            r5 = r0
            r0 = r5
            r1 = r69
            r0.paint(r1)
            r22 = r67
            goto L_0x016b
        L_0x0668:
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r7 = r5 + 10
            r0 = r68
            int r0 = r0.contentY
            r5 = r0
            int r8 = r5 + 3
            int r49 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            javax.microedition.lcdui.Font r5 = com.sumsharp.monster.common.Utilities.font
            java.lang.String r6 = "抗性 "
            int r61 = r5.stringWidth(r6)
            int r5 = r61 + 32
            int r22 = r5 + 10
            r5 = 6
            r0 = r5
            int[] r0 = new int[r0]
            r37 = r0
            r37 = {3274504, 59391, 12386304, 16771996, 16776960, 4869631} // fill-array
            r46 = 0
        L_0x068f:
            java.lang.String[] r5 = com.sumsharp.monster.common.data.Pet.ATTR_RESIST_TEXT
            int r5 = r5.length
            r0 = r46
            r1 = r5
            if (r0 < r1) goto L_0x06b2
            r0 = r68
            com.sumsharp.lowui.Tip r0 = r0.tip
            r5 = r0
            if (r5 == 0) goto L_0x016b
            r0 = r68
            boolean r0 = r0.resistTipShow
            r5 = r0
            if (r5 == 0) goto L_0x016b
            r0 = r68
            com.sumsharp.lowui.Tip r0 = r0.tip
            r5 = r0
            r0 = r5
            r1 = r69
            r0.draw(r1)
            goto L_0x016b
        L_0x06b2:
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r25 = r5[r6]
            r0 = r68
            int r0 = r0.resistIdx
            r5 = r0
            r0 = r46
            r1 = r5
            if (r0 != r1) goto L_0x06c8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 18
            r25 = r5[r6]
        L_0x06c8:
            r5 = 4
            int r20 = r7 - r5
            int r5 = r8 + r49
            r6 = 7
            int r21 = r5 - r6
            r23 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r24 = r5[r6]
            r19 = r69
            drawSmallPanel(r19, r20, r21, r22, r23, r24, r25)
            r0 = r37
            int r0 = r0.length
            r5 = r0
            r0 = r46
            r1 = r5
            if (r0 >= r1) goto L_0x0739
            java.lang.String[] r5 = com.sumsharp.monster.common.data.Pet.ATTR_RESIST_TEXT
            r27 = r5[r46]
            int r29 = r8 + 3
            r30 = r37[r46]
            r31 = 0
            r26 = r69
            r28 = r7
            com.sumsharp.monster.common.Tool.draw3DString(r26, r27, r28, r29, r30, r31)
        L_0x06f7:
            byte[] r5 = com.sumsharp.monster.common.data.Pet.ATTR_RESISTS
            byte r5 = r5[r46]
            r0 = r55
            r1 = r5
            int r5 = r0.getAttribute(r1)
            java.lang.String r26 = java.lang.String.valueOf(r5)
            int r28 = r7 + r61
            int r5 = r8 + r49
            r6 = 1
            int r29 = r5 - r6
            r30 = 0
            r31 = 36
            r32 = -1
            r27 = r69
            com.sumsharp.monster.common.Tool.drawNumStr(r26, r27, r28, r29, r30, r31, r32)
            int r8 = r8 + r49
            int r5 = r8 + r49
            int r6 = com.sumsharp.monster.World.viewHeight
            com.sumsharp.android.ui.DirectionPad r9 = com.sumsharp.android.ui.DirectionPad.instance
            int r9 = r9.getHeight()
            int r6 = r6 - r9
            r9 = 5
            int r6 = r6 - r9
            if (r5 <= r6) goto L_0x0735
            r0 = r68
            int r0 = r0.contentY
            r5 = r0
            int r8 = r5 + 3
            int r5 = com.sumsharp.monster.World.viewWidth
            int r5 = r5 / 3
            int r7 = r7 + r5
        L_0x0735:
            int r46 = r46 + 1
            goto L_0x068f
        L_0x0739:
            java.lang.String[] r5 = com.sumsharp.monster.common.data.Pet.ATTR_RESIST_TEXT
            r27 = r5[r46]
            int r29 = r8 + 3
            r30 = 16777215(0xffffff, float:2.3509886E-38)
            r31 = 0
            r26 = r69
            r28 = r7
            com.sumsharp.monster.common.Tool.draw3DString(r26, r27, r28, r29, r30, r31)
            goto L_0x06f7
        L_0x074c:
            r0 = r68
            int r0 = r0.x
            r5 = r0
            r0 = r68
            int r0 = r0.width
            r6 = r0
            r7 = 166(0xa6, float:2.33E-43)
            int r6 = r6 - r7
            int r6 = r6 >> 1
            int r5 = r5 + r6
            int r5 = r5 + 5
            r6 = 40
            int r7 = r5 - r6
            r0 = r68
            int r0 = r0.contentY
            r5 = r0
            int r8 = r5 + 3
            r49 = 22
            r66 = 124(0x7c, float:1.74E-43)
            int r5 = com.sumsharp.monster.World.viewWidth
            int r5 = r5 / 2
            int r6 = com.sumsharp.monster.common.Utilities.CHAR_HEIGHT
            int r6 = r6 * 4
            int r27 = r5 - r6
            int r5 = r8 + r49
            r6 = 5
            int r28 = r5 - r6
            int r5 = com.sumsharp.monster.common.Utilities.CHAR_HEIGHT
            int r29 = r5 * 8
            r30 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r31 = r5[r6]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r32 = r5[r6]
            r26 = r69
            drawSmallPanel(r26, r27, r28, r29, r30, r31, r32)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "总资质:  "
            r5.<init>(r6)
            r0 = r55
            int[] r0 = r0.rates
            r6 = r0
            r9 = 0
            r6 = r6[r9]
            java.lang.String r6 = java.lang.String.valueOf(r6)
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r27 = r5.toString()
            int r5 = com.sumsharp.monster.World.viewWidth
            int r28 = r5 / 2
            int r5 = r8 + r49
            r6 = 13
            int r29 = r5 - r6
            r30 = 16777215(0xffffff, float:2.3509886E-38)
            r31 = 0
            r32 = 17
            r26 = r69
            com.sumsharp.monster.common.Tool.draw3DString(r26, r27, r28, r29, r30, r31, r32)
            int r8 = r8 + r49
            r64 = r8
            r5 = 3
            r0 = r5
            java.lang.String[] r0 = new java.lang.String[r0]
            r63 = r0
            r5 = 0
            java.lang.String r6 = "属性"
            r63[r5] = r6
            r5 = 1
            java.lang.String r6 = "成长等级"
            r63[r5] = r6
            r5 = 2
            java.lang.String r6 = "资质"
            r63[r5] = r6
            r46 = 0
        L_0x07df:
            r5 = 3
            r0 = r46
            r1 = r5
            if (r0 < r1) goto L_0x07f1
            r48 = 1
        L_0x07e7:
            r5 = 7
            r0 = r48
            r1 = r5
            if (r0 < r1) goto L_0x0833
            r22 = r67
            goto L_0x016b
        L_0x07f1:
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r27 = r5 + 5
            int r5 = r8 + r49
            r6 = 5
            int r28 = r5 - r6
            r0 = r68
            int r0 = r0.width
            r5 = r0
            r6 = 10
            int r29 = r5 - r6
            r30 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r31 = r5[r6]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r32 = r5[r6]
            r26 = r69
            drawSmallPanel(r26, r27, r28, r29, r30, r31, r32)
            r27 = r63[r46]
            r28 = 50
            int r29 = r8 + 10
            r30 = 16777215(0xffffff, float:2.3509886E-38)
            r31 = 0
            r32 = 17
            r26 = r69
            com.sumsharp.monster.common.Tool.draw3DString(r26, r27, r28, r29, r30, r31, r32)
            int r5 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r5 = r5 + 5
            int r8 = r8 + r5
            int r46 = r46 + 1
            goto L_0x07df
        L_0x0833:
            r8 = r64
            java.lang.String[] r5 = com.sumsharp.monster.common.data.Pet.RATE_TEXT
            r59 = r5[r48]
            int r5 = r7 + 2
            int r6 = r8 + r49
            r9 = 13
            int r6 = r6 - r9
            r0 = r69
            r1 = r59
            r2 = r5
            r3 = r6
            com.sumsharp.monster.common.Tool.drawImageFont(r0, r1, r2, r3)
            r0 = r55
            int[] r0 = r0.growUp
            r5 = r0
            r6 = 1
            int r6 = r48 - r6
            r65 = r5[r6]
            r5 = 6
            r0 = r48
            r1 = r5
            if (r0 != r1) goto L_0x085d
            int r5 = r65 * 2
            int r65 = r5 / 5
        L_0x085d:
            int r5 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r5 = r5 + 5
            int r8 = r8 + r5
            int r5 = r7 + 2
            int r6 = r8 + r49
            r0 = r68
            r1 = r69
            r2 = r65
            r3 = r5
            r4 = r6
            r0.drawGrowUpRank(r1, r2, r3, r4)
            int r5 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r5 = r5 + 5
            int r8 = r8 + r5
            r0 = r55
            int[] r0 = r0.rates
            r5 = r0
            r5 = r5[r48]
            java.lang.String r26 = java.lang.String.valueOf(r5)
            int r29 = r8 + r49
            r30 = 0
            r31 = 36
            r32 = -1
            r27 = r69
            r28 = r7
            com.sumsharp.monster.common.Tool.drawNumStr(r26, r27, r28, r29, r30, r31, r32)
            int r7 = r7 + 50
            int r48 = r48 + 1
            goto L_0x07e7
        L_0x0896:
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r7 = r5 + 5
            int r5 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r43 = r5 + 5
            r0 = r68
            int r0 = r0.contentY
            r5 = r0
            int r5 = r5 + 3
            int r6 = com.sumsharp.monster.common.Utilities.LINE_HEIGHT
            int r8 = r5 + r6
            r64 = r8
            r0 = r68
            int r0 = r0.width
            r5 = r0
            int r6 = r7 * 2
            int r29 = r5 - r6
            r30 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r31 = r5[r6]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r32 = r5[r6]
            r26 = r69
            r27 = r7
            r28 = r8
            drawSmallPanel(r26, r27, r28, r29, r30, r31, r32)
            java.lang.String r5 = "属性"
            int r6 = r7 + 2
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r8
            com.sumsharp.monster.common.Tool.drawImageFont(r0, r1, r2, r3)
            int r5 = r43 + 15
            int r8 = r8 + r5
            r0 = r68
            int r0 = r0.width
            r5 = r0
            int r6 = r7 * 2
            int r29 = r5 - r6
            r30 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r31 = r5[r6]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r32 = r5[r6]
            r26 = r69
            r27 = r7
            r28 = r8
            drawSmallPanel(r26, r27, r28, r29, r30, r31, r32)
            java.lang.String r5 = "基础"
            int r6 = r7 + 2
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r8
            com.sumsharp.monster.common.Tool.drawImageFont(r0, r1, r2, r3)
            int r8 = r8 + r43
            r0 = r68
            int r0 = r0.width
            r5 = r0
            int r6 = r7 * 2
            int r29 = r5 - r6
            r30 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r31 = r5[r6]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r32 = r5[r6]
            r26 = r69
            r27 = r7
            r28 = r8
            drawSmallPanel(r26, r27, r28, r29, r30, r31, r32)
            java.lang.String r5 = "辅助"
            int r6 = r7 + 2
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r8
            com.sumsharp.monster.common.Tool.drawImageFont(r0, r1, r2, r3)
            int r8 = r8 + r43
            r0 = r68
            int r0 = r0.width
            r5 = r0
            int r6 = r7 * 2
            int r29 = r5 - r6
            r30 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r31 = r5[r6]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r32 = r5[r6]
            r26 = r69
            r27 = r7
            r28 = r8
            drawSmallPanel(r26, r27, r28, r29, r30, r31, r32)
            java.lang.String r5 = "继承"
            int r6 = r7 + 2
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r8
            com.sumsharp.monster.common.Tool.drawImageFont(r0, r1, r2, r3)
            int r8 = r8 + r43
            r0 = r68
            int r0 = r0.width
            r5 = r0
            int r6 = r7 * 2
            int r29 = r5 - r6
            r30 = 8
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 17
            r31 = r5[r6]
            int[] r5 = com.sumsharp.monster.common.Tool.CLR_TABLE
            r6 = 19
            r32 = r5[r6]
            r26 = r69
            r27 = r7
            r28 = r8
            drawSmallPanel(r26, r27, r28, r29, r30, r31, r32)
            java.lang.String r5 = "原始"
            int r6 = r7 + 2
            r0 = r69
            r1 = r5
            r2 = r6
            r3 = r8
            com.sumsharp.monster.common.Tool.drawImageFont(r0, r1, r2, r3)
            r0 = r68
            int r0 = r0.x
            r5 = r0
            int r7 = r5 + 150
            r8 = r64
            r5 = 6
            r0 = r5
            java.lang.String[] r0 = new java.lang.String[r0]
            r60 = r0
            r5 = 0
            java.lang.String r6 = "力"
            r60[r5] = r6
            r5 = 1
            java.lang.String r6 = "耐"
            r60[r5] = r6
            r5 = 2
            java.lang.String r6 = "敏"
            r60[r5] = r6
            r5 = 3
            java.lang.String r6 = "智"
            r60[r5] = r6
            r5 = 4
            java.lang.String r6 = "精"
            r60[r5] = r6
            r5 = 5
            java.lang.String r6 = "防"
            r60[r5] = r6
            r34 = 0
        L_0x09c2:
            r5 = 6
            r0 = r34
            r1 = r5
            if (r0 >= r1) goto L_0x0169
            r59 = r60[r34]
            int r5 = r8 + 1
            r0 = r69
            r1 = r59
            r2 = r7
            r3 = r5
            com.sumsharp.monster.common.Tool.drawImageFont(r0, r1, r2, r3)
            int r5 = r43 + 15
            int r8 = r8 + r5
            r0 = r55
            int[] r0 = r0.inheritMain
            r5 = r0
            r5 = r5[r34]
            java.lang.String r26 = java.lang.String.valueOf(r5)
            int r28 = r7 + 12
            int r29 = r8 + 13
            r30 = 33
            r31 = -1
            r27 = r69
            com.sumsharp.monster.common.Tool.drawSmallNum(r26, r27, r28, r29, r30, r31)
            int r8 = r8 + r43
            r0 = r55
            int[] r0 = r0.inheritLess
            r5 = r0
            r5 = r5[r34]
            java.lang.String r26 = java.lang.String.valueOf(r5)
            int r28 = r7 + 12
            int r29 = r8 + 13
            r30 = 33
            r31 = -1
            r27 = r69
            com.sumsharp.monster.common.Tool.drawSmallNum(r26, r27, r28, r29, r30, r31)
            int r8 = r8 + r43
            r0 = r55
            int[] r0 = r0.inheritMain
            r5 = r0
            r5 = r5[r34]
            r0 = r55
            int[] r0 = r0.inheritLess
            r6 = r0
            r6 = r6[r34]
            int r5 = r5 + r6
            java.lang.String r26 = java.lang.String.valueOf(r5)
            int r28 = r7 + 12
            int r29 = r8 + 13
            r30 = 33
            r31 = -1
            r27 = r69
            com.sumsharp.monster.common.Tool.drawSmallNum(r26, r27, r28, r29, r30, r31)
            int r8 = r8 + r43
            r0 = r55
            int[] r0 = r0.baseMonsterAttr
            r5 = r0
            r5 = r5[r34]
            java.lang.String r26 = java.lang.String.valueOf(r5)
            int r28 = r7 + 12
            int r29 = r8 + 13
            r30 = 33
            r31 = -1
            r27 = r69
            com.sumsharp.monster.common.Tool.drawSmallNum(r26, r27, r28, r29, r30, r31)
            int r7 = r7 + 40
            r8 = r64
            int r34 = r34 + 1
            goto L_0x09c2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.lowui.PetDetailUI.paint(javax.microedition.lcdui.Graphics):void");
    }

    private void drawGrowUpRank(Graphics g, int growRank, int x2, int y) {
        int frame;
        int count = 1;
        if (growRank < 10) {
            Tool.uiMiscImg2.drawFrame(g, 14, x2, y, 0, 36);
            int x3 = x2 + Tool.uiMiscImg2.getFrameWidth(14) + 1;
            Tool.uiMiscImg2.drawFrame(g, 15, x3, y - Tool.uiMiscImg2.getFrameHeight(14), 0, 20);
            return;
        }
        if (growRank < 20) {
            frame = 14;
        } else if (growRank < 30) {
            frame = 13;
        } else if (growRank < 40) {
            frame = 12;
        } else if (growRank < 60) {
            frame = 11;
        } else if (growRank < 80) {
            frame = 10;
        } else if (growRank < 110) {
            frame = 9;
        } else if (growRank < 150) {
            frame = 9;
            count = 2;
        } else if (growRank < 200) {
            frame = 8;
            count = 3;
        } else if (growRank < 250) {
            frame = 7;
        } else {
            frame = 6;
        }
        for (int i = 0; i < count; i++) {
            Tool.uiMiscImg2.drawFrame(g, frame, x2, y, 0, 36);
            x2 += Tool.uiMiscImg2.getFrameWidth(frame) + 1;
        }
    }

    public void setTabIdx(int tabIdx) {
        this.tab.setTabIndex(tabIdx);
    }

    public int getTabIdx() {
        return this.tab.getIdx();
    }

    public int getSkillSelection() {
        if (this.skillIds == null || this.tab.getIdx() != 1) {
            return -1;
        }
        if (this.skillTable.getMenuSelection() >= this.skillIds.length) {
            return -1;
        }
        return this.skillIds[this.skillTable.getMenuSelection()];
    }

    public void setPageNo(int pageNo2) {
        if (pageNo2 < this.pageSize) {
            this.pageNo = pageNo2;
            this.tab.init();
            this.tab.refreshContent();
        }
    }

    public void setCurrSelId(int petId) {
        for (int i = 0; i < this.pets.length; i++) {
            if (this.pets[i].id == petId) {
                setPageNo(i);
                return;
            }
        }
    }

    public Object findData(int type, int id) {
        for (int i = 0; i < this.pets.length; i++) {
            if (this.pets[i].id == id) {
                return this.pets[i];
            }
        }
        return null;
    }

    private void resetResist() {
        this.resistIdx = 0;
        this.resistTipShow = false;
        if (!resistBoundsInit) {
            resistBounds = new int[(Pet.ATTR_RESIST_TEXT.length * 2)];
            int dx = this.x + 10;
            int dy = this.contentY + 3;
            for (int i = 0; i < Pet.ATTR_RESIST_TEXT.length; i++) {
                resistBounds[i * 2] = dx - 4;
                resistBounds[(i * 2) + 1] = (dy + 16) - 11;
                dy += 16;
                if (dy + 16 > (this.contentY + this.contentHeight) - 5) {
                    dy = this.contentY + 3;
                    dx += World.viewWidth >> 1;
                }
            }
            resistBoundsInit = true;
        }
    }

    public void handleCurrTab() {
        int idx = this.tab.getIdx();
        if (idx == 0) {
            setCmd(null, "返回");
        } else if (idx == 1) {
            initSkillTable();
            if (this.skillTable.getItemCount() <= 0) {
                setCmd(null, "返回");
            }
        } else if (idx == 2) {
            initItemTable(this.pets[this.pageNo].items);
            this.itemTable.setY(this.contentY);
            setCmd("菜单", "返回");
        } else if (idx == 3) {
            resetResist();
            setCmd("查看", "返回");
        } else {
            setCmd(null, "返回");
        }
    }

    private void handleResistTouch(int px, int py) {
        if (px != -1 && py != -1) {
            int w = Utilities.font.stringWidth("抗性 ") + 32 + 10;
            int i = 0;
            while (i < Pet.ATTR_RESIST_TEXT.length) {
                if (px <= resistBounds[i * 2] || py <= resistBounds[(i * 2) + 1] || px >= resistBounds[i * 2] + w || py >= resistBounds[(i * 2) + 1] + 12) {
                    i++;
                } else if (this.resistIdx == i) {
                    Utilities.keyPressed(5, true);
                    return;
                } else {
                    this.resistIdx = i;
                    this.resistTipShow = false;
                    return;
                }
            }
        }
    }
}
