package com.sumsharp.lowui;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.MateInfo;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.image.PipAnimateSet;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class MatingPanel extends UI implements TabHandler {
    private static final int ANISTATUS_EGG = 2;
    private static final int ANISTATUS_HEART = 1;
    private static final int ANISTATUS_MOVING = 0;
    private static final int STATUS_CHOOSE = 2;
    private static final int STATUS_NORMAL = 0;
    private static final int STATUS_PLAYANI = 1;
    private int _tabley;
    private int _tipy;
    private int _titley;
    private int aniStatus = 0;
    private ImageSet bg;
    private int bgx;
    private int bgy;
    private Table chooseTable;
    private CartoonPlayer eggAni;
    private int freeTabWidth;
    private int height;
    private int icon;
    private MateInfo info = null;
    private MovingString lessName;
    private Pet lesser;
    private Pet main;
    private MovingString mainName;
    private Table mainPetTable;
    private int mainheight;
    private int mainwidth;
    private int mainx;
    private int mainy;
    private PipAnimateSet matingAni;
    private CartoonPlayer matingLeftAni;
    private CartoonPlayer matingRightAni;
    private ImageSet petIcon = ImageLoadManager.getImage("petIcons.jgp");
    private int petRectSpeed;
    private Pet[] pets;
    private boolean playerInit = false;
    private int secheight;
    private int secwidth;
    private int secx;
    private int secy;
    private Vector selMatMap = new Vector();
    private String[] selectFlg;
    private Pet[] selectPets;
    private int status = 0;
    private Tab tab;
    private int tabEnd = 0;
    private int tabFirst = 0;
    private int tabIdx = 0;
    private String[] tabTitles;
    private int tableHSpeed;
    private int tableHeight;
    private int tableSpeed;
    private int tableWidth;
    private int tabley;
    private StringDraw tipDraw;
    private int tipSpeed;
    private int tipheight;
    private int tipx;
    private int tipy;
    private String title;
    private int titleSpeed;
    private int titleY;
    private int toStatus = -1;
    private int width = (World.viewWidth - 6);
    private int x = 3;

    private void initImg() {
        this.bg = new ImageSet((String) "/matingbg.png");
        int w = this.bg.getFrameWidth(0);
        int h = this.bg.getFrameHeight(0);
        this.bgx = (World.viewWidth - w) >> 1;
        this.bgy = (World.viewHeight - h) >> 1;
        this.matingAni = ImageLoadManager.getAnimate("/mating.ani");
        this.matingLeftAni = CartoonPlayer.playCartoon(this.matingAni, 0, 0, 0, false);
        this.matingRightAni = CartoonPlayer.playCartoon(this.matingAni, 1, 0, 0, false);
        this.eggAni = CartoonPlayer.playCartoon(this.matingAni, 2, World.viewWidth >> 1, this.bgy + 144, false);
    }

    public MatingPanel(Pet[] pets2, int icon2, String title2) {
        this.pets = pets2;
        this.status = 2;
        this.tipheight = 0;
        this.tipy = World.viewHeight;
        this._tipy = World.viewHeight - CornerButton.instance.getHeight();
        this.titleY = NewStage.screenY;
        this._titley = NewStage.screenY;
        this.tabley = World.viewHeight;
        this._tabley = this._titley + Utilities.TITLE_HEIGHT;
        this.height = ((World.viewHeight - Utilities.TITLE_HEIGHT) - CornerButton.instance.getHeight()) - Tab.getDefaultHeight();
        this.tableWidth = this.width - 140;
        this.tableHeight = this.height - Tab.getDefaultHeight();
        this.icon = icon2;
        this.title = title2;
        this.mainx = World.viewWidth;
        this.mainy = this._tabley + Tab.getDefaultHeight();
        this.mainwidth = this.width - this.tableWidth;
        this.mainheight = this.tableHeight >> 1;
        this.secx = this.mainx;
        this.secy = this.mainy + this.mainheight;
        this.secwidth = this.width - this.tableWidth;
        this.secheight = this.tableHeight - this.mainheight;
        this.tableHSpeed = this.tableWidth >> 1;
        this.freeTabWidth = (((((World.viewWidth - 1) - Tool.uiMiscImg.getFrameWidth(49)) - Tool.uiMiscImg.getFrameWidth(48)) - Tool.uiMiscImg.getFrameWidth(64)) - Tool.uiMiscImg.getFrameWidth(65)) - 56;
        createPetTable();
        createChooseTable();
        initImg();
        this.tabIdx = 0;
        this.tipx = DirectionPad.instance.getWidth() + 46;
    }

    private void createChooseTable() {
        this.chooseTable = new Table(this.x, this.tabley, this.tableWidth, this.tableHeight, new String[]{"选择图谱"}, new int[]{-1});
        this.chooseTable.show = true;
    }

    private Pet[] findPetByBaseRace(int raceId) {
        Vector vec = new Vector();
        for (int i = 0; i < this.pets.length; i++) {
            if (this.pets[i].getAttribute(52) == raceId) {
                vec.addElement(this.pets[i]);
            }
        }
        Pet[] p = new Pet[vec.size()];
        vec.copyInto(p);
        return p;
    }

    private Pet[] findPetByBaseId(int baseId) {
        Vector vec = new Vector();
        for (int i = 0; i < this.pets.length; i++) {
            if (this.pets[i].baseMonsterId == baseId) {
                vec.addElement(this.pets[i]);
            }
        }
        Pet[] p = new Pet[vec.size()];
        vec.copyInto(p);
        return p;
    }

    private void createPetTable() {
        int[] widths = new int[5];
        widths[0] = 0;
        widths[1] = -1;
        widths[2] = Utilities.font.stringWidth("性别") + 6;
        widths[3] = 11;
        this.tipy = World.viewHeight;
        this.tabley = World.viewHeight;
        this.titleY = -Utilities.LINE_HEIGHT;
        this.titleSpeed = (this._titley - this.titleY) / 2;
        this.tableSpeed = (this.tabley - this._tabley) / 2;
        this.tipSpeed = this.tableSpeed;
        this.petRectSpeed = this.mainwidth >> 1;
        this.mainPetTable = new Table(this.x + 50, this.tabley + Tab.getDefaultHeight(), this.tableWidth, this.tableHeight, new String[]{"desc:petdesc", "名字", "性别", ""}, widths);
        this.mainPetTable.show = true;
        this.mainPetTable.setItemArea(3);
    }

    public void addPets(Pet[] pets2) {
        this.pets = pets2;
        refreshChooseTab();
        refreshChooseTable();
    }

    private void refreshChooseTab() {
        Vector tabs = new Vector();
        for (int i = 0; i < CommonData.player.mateMap.size(); i++) {
            String race = Pet.MONSTER_CLASS_NAME[((MateInfo) CommonData.player.mateMap.elementAt(i)).childRace];
            if (tabs.indexOf(race) == -1) {
                tabs.addElement(race);
            }
        }
        this.tabTitles = new String[(tabs.size() + 1)];
        if (tabs.size() == 0) {
            this.tabTitles = null;
            return;
        }
        this.tabTitles[0] = "可用";
        for (int i2 = 0; i2 < tabs.size(); i2++) {
            this.tabTitles[i2 + 1] = (String) tabs.elementAt(i2);
        }
        this.tab = new Tab("宠物合成", this.tabTitles, 3, this.tableWidth, true, this);
        this.tab.setY(this.tabley - Tab.getDefaultHeight());
        this.tab.setContentHeight(this.chooseTable.getHeight());
        this.chooseTable.setHasTab(true);
    }

    private void refreshChooseTable() {
        boolean z;
        this.selMatMap = new Vector();
        for (int i = 0; i < CommonData.player.mateMap.size(); i++) {
            MateInfo info2 = (MateInfo) CommonData.player.mateMap.elementAt(i);
            boolean hasMain = true;
            if (info2.mainType == 0) {
                if (findPetByBaseId(info2.mainId).length == 0) {
                    hasMain = false;
                }
            } else if (findPetByBaseRace(info2.mainId).length == 0) {
                hasMain = false;
            }
            boolean hasLess = true;
            if (info2.lessType == 0) {
                if (findPetByBaseId(info2.lessId).length == 0) {
                    hasLess = false;
                }
            } else if (findPetByBaseRace(info2.lessId).length == 0) {
                hasLess = false;
            }
            if (!hasMain || !hasLess) {
                z = false;
            } else {
                z = true;
            }
            info2.canMate = z;
            info2.hasMain = hasMain;
            info2.hasLess = hasLess;
            if (this.tabIdx != 0) {
                String r = this.tabTitles[this.tabIdx];
                int idx = 0;
                while (idx < Pet.MONSTER_CLASS_NAME.length && !Pet.MONSTER_CLASS_NAME[idx].equals(r)) {
                    idx++;
                }
                if (info2.childRace == idx) {
                    this.selMatMap.addElement(info2);
                }
            } else if (info2.canMate) {
                this.selMatMap.addElement(info2);
            }
        }
        String[] infos = new String[this.selMatMap.size()];
        boolean[] status2 = new boolean[this.selMatMap.size()];
        for (int i2 = 0; i2 < this.selMatMap.size(); i2++) {
            MateInfo info3 = (MateInfo) this.selMatMap.elementAt(i2);
            infos[i2] = info3.getTitle();
            status2[i2] = info3.canMate;
        }
        this.chooseTable.addTableItem("选择图谱", infos, 0, null, null);
        this.chooseTable.setItemStatus(status2);
    }

    private void refreshSelectPets(boolean isMain) {
        if (this.info != null) {
            int type = isMain ? this.info.mainType : this.info.lessType;
            int id = isMain ? this.info.mainId : this.info.lessId;
            if (type == 0) {
                this.selectPets = findPetByBaseId(id);
            } else {
                this.selectPets = findPetByBaseRace(id);
            }
            int len = this.selectPets.length;
            String[] descs = new String[len];
            String[] names = new String[len];
            String[] sexs = new String[len];
            String[] lvs = new String[len];
            this.selectFlg = new String[len];
            for (int i = 0; i < len; i++) {
                names[i] = this.selectPets[i].name;
                sexs[i] = this.selectPets[i].sex == 0 ? "♀" : "♂";
                lvs[i] = String.valueOf(this.selectPets[i].getAttribute(50));
                descs[i] = this.selectPets[i].getDesc(false);
                this.selectFlg[i] = "-1";
            }
            this.mainPetTable.addTableItem("名字", names, 0, null, null);
            this.mainPetTable.addTableItem("性别", sexs, 0, null, null);
            this.mainPetTable.addTableItem("", this.selectFlg, 2, null, null);
            this.mainPetTable.addTableItem("desc:petdesc", descs, 0, null, null);
        }
    }

    public void cycle() {
        boolean z;
        super.cycle();
        if (this.status == 0 || this.status == 2) {
            if (this.closed) {
                this.titleY -= this.titleSpeed;
                boolean finished = true;
                if (this.titleY > (-Utilities.LINE_HEIGHT)) {
                    finished = false;
                }
                if (this.tabley > this._tabley) {
                    this.tipy += this.tipSpeed;
                    if (this.tipy < World.viewHeight) {
                        finished = false;
                    }
                }
                this.tabley += this.tableSpeed;
                if (this.tabley < World.viewHeight) {
                    finished = false;
                }
                this.mainx += this.petRectSpeed;
                this.secx += this.petRectSpeed;
                this.mainPetTable.setY(this.tabley);
                this.chooseTable.setY(this.tabley + Utilities.LINE_HEIGHT);
                if (this.tab != null) {
                    this.tab.setY(((this.tabley + Utilities.TITLE_HEIGHT) - Tab.getDefaultHeight()) - (Utilities.LINE_HEIGHT * 2));
                }
                if (finished) {
                    z = false;
                } else {
                    z = true;
                }
                this.show = z;
            } else if (this.show) {
                if (this.chooseTable != null) {
                    int y = this.chooseTable.getY() - Utilities.LINE_HEIGHT;
                    int i = Utilities.LINE_HEIGHT;
                    if (this.tab != null) {
                        this.tab.cycle();
                    }
                }
                if (this.mainPetTable != null) {
                    this.mainPetTable.handlePoints(World.pressedx, World.pressedy);
                }
                if (this.chooseTable != null) {
                    this.chooseTable.handlePoints(World.pressedx, World.pressedy);
                }
                if (this.toStatus == -1) {
                    this.mainx -= this.petRectSpeed;
                    if (this.mainx < this.x + this.tableWidth) {
                        this.mainx = this.x + this.tableWidth;
                    }
                    if (this.main == null && this.lesser == null) {
                        this.secx += this.petRectSpeed;
                        if (this.secx > World.viewWidth) {
                            this.secx = World.viewWidth;
                        }
                    } else {
                        this.secx -= this.petRectSpeed;
                        if (this.secx < this.x + this.tableWidth) {
                            this.secx = this.x + this.tableWidth;
                        }
                    }
                }
                moveBtn();
                String tipStr = "";
                if (this.status == 2) {
                    if (this.toStatus == 0) {
                        this.mainx += this.petRectSpeed;
                        this.chooseTable.setX(this.chooseTable.getX() - this.tableHSpeed);
                        if (this.mainx > World.viewWidth && this.chooseTable.getX() + this.tableWidth < 0) {
                            this.secx = this.mainx;
                            setCmd(null, "返回");
                            this.status = this.toStatus;
                            this.toStatus = -1;
                            this.mainPetTable.setX(-this.tableWidth);
                        }
                    } else {
                        if (this.chooseTable.getX() + this.tableHSpeed < 3) {
                            this.chooseTable.setX(this.chooseTable.getX() + this.tableHSpeed);
                        } else {
                            this.chooseTable.setX(53);
                        }
                        MateInfo info2 = null;
                        int sel = this.chooseTable.getMenuSelection();
                        if (this.selMatMap.size() > 0) {
                            info2 = (MateInfo) this.selMatMap.elementAt(sel);
                        }
                        this.chooseTable.cycle();
                        if (info2 == null) {
                            if (this.tabIdx == 0) {
                                tipStr = "没有可用的合成图谱";
                            } else {
                                tipStr = "你还没有学会合成灵兽的方法";
                            }
                        } else if (info2.canMate) {
                            tipStr = "合成：" + info2.childTitle + " 需要 " + info2.mainTitle + " + " + info2.lessTitle;
                        } else {
                            tipStr = "合成：" + info2.childTitle + " " + info2.getMissingString();
                        }
                        setCmd("下一步", "关闭");
                        int oldTabIdx = this.tabIdx;
                        if (Utilities.isKeyPressed(9, true) || Utilities.isKeyPressed(9, true) || Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(16, true)) {
                            if (!(info2 == null || sel == -1 || !info2.canMate)) {
                                this.toStatus = 0;
                                this.info = info2;
                                refreshSelectPets(true);
                            }
                        } else if (Utilities.isKeyPressed(22, true)) {
                            this.tabIdx--;
                            if (this.tabIdx >= 0) {
                                if (this.tabIdx < this.tabFirst) {
                                    this.tabFirst--;
                                }
                            } else if (this.tabTitles != null) {
                                this.tabIdx = this.tabTitles.length - 1;
                                checkVisiAreafromEnd();
                            } else {
                                this.tabIdx = 0;
                            }
                        } else if (Utilities.isKeyPressed(21, true)) {
                            this.tabIdx++;
                            if (this.tabTitles != null) {
                                if (this.tabIdx > this.tabTitles.length - 1) {
                                    this.tabIdx = 0;
                                    this.tabFirst = 0;
                                } else {
                                    if (this.tabIdx > this.tabEnd) {
                                        checkVisiAreafromEnd();
                                    }
                                }
                            } else {
                                this.tabIdx = 0;
                            }
                        }
                        if (this.tabIdx != oldTabIdx) {
                            refreshChooseTable();
                        }
                    }
                } else if (this.toStatus == 2) {
                    this.mainx += this.petRectSpeed;
                    this.secx += this.petRectSpeed;
                    this.mainPetTable.setX(this.mainPetTable.getX() - this.tableHSpeed);
                    if (this.mainx > World.viewWidth && this.chooseTable.getX() + this.tableWidth < 0) {
                        this.secx = this.mainx;
                        this.status = this.toStatus;
                        this.toStatus = -1;
                        this.chooseTable.setX(-this.tableWidth);
                    }
                } else {
                    if (this.mainPetTable.getX() + this.tableHSpeed < 3) {
                        this.mainPetTable.setX(this.mainPetTable.getX() + this.tableHSpeed);
                    } else {
                        this.mainPetTable.setX(53);
                        this.mainPetTable.forceUpdate(true, this.mainPetTable.getItemCount());
                    }
                    this.mainPetTable.cycle();
                    Pet selPet = null;
                    if (this.selectPets.length > 0) {
                        selPet = this.selectPets[this.mainPetTable.getMenuSelection()];
                    }
                    if (this.info != null) {
                        tipStr = this.info.getTitle();
                    }
                    if (this.selectPets != null && this.selectPets.length == 0) {
                        tipStr = String.valueOf(tipStr) + " 没有找到合成需要的灵兽！";
                    } else if (this.main == null) {
                        tipStr = String.valueOf(tipStr) + " 请选择<c00ffff>基础灵兽[" + this.info.mainTitle + "]</c>并按OK确定。";
                    } else if (this.lesser == null) {
                        tipStr = String.valueOf(tipStr) + " 请选择<c00ffff>辅助灵兽[" + this.info.lessTitle + "]</c>并按OK确定。";
                    }
                    if (Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(16, true)) {
                        if (this.main == selPet) {
                            this.main = null;
                            this.mainName = null;
                            this.selectFlg[this.mainPetTable.getMenuSelection()] = "-1";
                        } else if (this.lesser == selPet) {
                            this.lesser = null;
                            this.lessName = null;
                            this.selectFlg[this.mainPetTable.getMenuSelection()] = "-1";
                        } else if (this.main == null) {
                            this.main = selPet;
                            this.mainName = makeMovingString(this.main.name, this.mainwidth);
                            this.selectFlg[this.mainPetTable.getMenuSelection()] = "31";
                            refreshSelectPets(false);
                        } else if (this.lesser == null) {
                            this.lesser = selPet;
                            this.lessName = makeMovingString(this.lesser.name, this.secwidth);
                            this.selectFlg[this.mainPetTable.getMenuSelection()] = "31";
                        }
                    } else if (Utilities.isKeyPressed(10, true)) {
                        this.toStatus = 2;
                        clearSelection();
                        this.main = null;
                        this.lesser = null;
                        this.info = null;
                    }
                    if (this.mainName != null) {
                        this.mainName.cycle();
                    }
                    if (this.lessName != null) {
                        this.lessName.cycle();
                    }
                }
                if (!(this.main == null || this.lesser == null)) {
                    if (this.main.sex == this.lesser.sex) {
                        if (tipStr.equals("")) {
                            tipStr = "两只合成的灵兽不能为同性";
                        } else {
                            tipStr = String.valueOf(tipStr) + " 两只合成的灵兽不能为同性";
                        }
                    } else if (tipStr.equals("")) {
                        tipStr = "按左软键开始合成";
                    } else {
                        tipStr = String.valueOf(tipStr) + " 按左软键开始合成";
                    }
                }
                if (tipStr.equals("")) {
                    this.tipDraw = null;
                    return;
                }
                StringDraw stringDraw = new StringDraw(tipStr, 1000, -1);
                this.tipDraw = stringDraw;
            } else {
                this.titleY += this.titleSpeed;
                boolean finished2 = true;
                if (this.titleY > this._titley) {
                    this.titleY = this._titley;
                } else {
                    finished2 = false;
                }
                if (this.tipy < World.viewHeight) {
                    this.tabley -= this.tableSpeed;
                    if (this.tabley < this._tabley) {
                        this.tabley = this._tabley;
                    } else {
                        finished2 = false;
                    }
                }
                this.tipy -= this.tipSpeed;
                if (this.tipy < this._tipy) {
                    this.tipy = this._tipy;
                } else {
                    finished2 = false;
                }
                this.mainPetTable.setY(this.tabley);
                if (this.tab != null) {
                    this.tab.setY((this.tabley + Utilities.TITLE_HEIGHT) - Tab.getDefaultHeight());
                    this.chooseTable.setY(this.tabley + Tab.getDefaultHeight());
                } else {
                    this.chooseTable.setY(this.tabley + Utilities.TITLE_HEIGHT);
                }
                this.show = finished2;
            }
        } else if (this.status == 1) {
            int mid = World.viewWidth >> 1;
            short maindx = (short) ((mid - 13) + NewStage.viewX);
            short dy = (short) ((this.bgy + 144) - NewStage.screenY);
            short lesserdx = (short) (mid + 13 + NewStage.viewX);
            if (!this.playerInit) {
                Pet m = CommonData.player.getPet(this.main.id, true);
                Pet l = CommonData.player.getPet(this.lesser.id, true);
                this.main.selfMove = false;
                this.main.setState(1);
                this.main.pixelX = (short) (this.bgx + NewStage.viewX);
                this.main.pixelY = dy;
                this.main.speed = 4;
                if (m != null) {
                    this.main.cartoonPlayer = m.cartoonPlayer;
                }
                this.main.go(0, 0);
                this.main.setDir(1);
                this.lesser.selfMove = false;
                this.lesser.setState(1);
                this.lesser.pixelX = (short) (((this.bgx + this.bg.getFrameWidth(0)) - this.lesser.getWidth()) + NewStage.viewX);
                this.lesser.pixelY = dy;
                this.lesser.speed = 4;
                if (l != null) {
                    this.lesser.cartoonPlayer = l.cartoonPlayer;
                }
                this.lesser.go(0, 0);
                refreshChooseTable();
                this.playerInit = true;
            }
            this.main.doCycle();
            this.lesser.doCycle();
            if (!this.main.moveTo(maindx, dy) && !this.lesser.moveTo(lesserdx, dy)) {
                return;
            }
            if (!this.matingLeftAni.die) {
                this.aniStatus = 1;
                this.main.setState(0);
                this.lesser.setState(0);
                this.matingLeftAni.nextFrame();
                this.matingRightAni.nextFrame();
            } else if (!this.eggAni.die) {
                this.aniStatus = 2;
                this.eggAni.nextFrame();
            } else {
                stopAni();
            }
        }
    }

    private void checkVisiAreafromEnd() {
        this.tabEnd = this.tabIdx;
        int dw = 0;
        int i = this.tabEnd;
        while (i >= this.tabFirst) {
            dw += Utilities.font.stringWidth(this.tabTitles[i]) + 5;
            if (Utilities.font.stringWidth(this.tabTitles[i]) + dw <= this.freeTabWidth) {
                i--;
            } else if (i != this.tabEnd) {
                this.tabFirst = i + 1;
                return;
            } else {
                this.tabFirst = this.tabEnd;
                return;
            }
        }
    }

    public int[] getMatingPetIds() {
        int mid = -1;
        if (this.main != null) {
            mid = this.main.id;
        }
        int lid = -1;
        if (this.lesser != null) {
            lid = this.lesser.id;
        }
        return new int[]{mid, lid};
    }

    public void clearSelection() {
        for (int i = 0; i < this.selectFlg.length; i++) {
            this.selectFlg[i] = "-1";
        }
        this.mainPetTable.addTableItem("", this.selectFlg, 2, null, null);
    }

    public void paint(Graphics g) {
        if (this.status == 2) {
            MateInfo info2 = null;
            if (this.selMatMap.size() > 0) {
                if (this.chooseTable.getMenuSelection() >= this.selMatMap.size()) {
                    this.chooseTable.setSelection(0);
                }
                info2 = (MateInfo) this.selMatMap.elementAt(this.chooseTable.getMenuSelection());
            }
            UI.drawTitlePanel(g, this.title, 0, this.titleY, World.viewWidth, Utilities.TITLE_HEIGHT, 8);
            if (this.tab != null) {
                this.tab.paint(g);
            }
            this.chooseTable.paint(g);
            int mainy2 = (this.mainy + Utilities.LINE_HEIGHT) - 8;
            int mainheight2 = this.mainheight - (Utilities.LINE_HEIGHT >> 1);
            int secy2 = (this.secy + (Utilities.LINE_HEIGHT >> 1)) - 8;
            int secheight2 = this.secheight - (Utilities.LINE_HEIGHT >> 1);
            drawRectPanel(g, this.mainx, mainy2, this.mainwidth, mainheight2);
            Tool.draw3DString(g, "基础灵兽", (this.mainwidth / 2) + this.mainx, mainy2 + 3, Tool.clr_androidTextDefault, 0, 17);
            drawRectPanel(g, this.mainx, secy2, this.secwidth, secheight2);
            Tool.draw3DString(g, "辅助灵兽", (this.secwidth / 2) + this.mainx, secy2 + 3, Tool.clr_androidTextDefault, 0, 17);
            g.setColor(12896707);
            g.drawLine(this.mainx + 3, mainy2 + 3 + Utilities.LINE_HEIGHT, (this.mainx + this.mainwidth) - 6, mainy2 + 3 + Utilities.LINE_HEIGHT);
            g.drawLine(this.mainx + 3, secy2 + 3 + Utilities.LINE_HEIGHT, (this.mainx + this.mainwidth) - 6, secy2 + 3 + Utilities.LINE_HEIGHT);
            if (info2 != null) {
                if (info2.mainType == 0) {
                    Tool.draw3DString(g, info2.mainTitle, this.mainx + 5, mainy2 + ((mainheight2 - Utilities.LINE_HEIGHT) >> 1), Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
                } else {
                    this.petIcon.drawFrame(g, info2.mainId, this.mainx + 5, mainy2 + (mainheight2 >> 1), 0, 6);
                    Tool.draw3DString(g, info2.mainTitle, this.mainx + 22, mainy2 + ((mainheight2 - Utilities.LINE_HEIGHT) >> 1), Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
                }
                if (info2.lessType == 0) {
                    Tool.draw3DString(g, info2.lessTitle, this.mainx + 5, secy2 + ((secheight2 - Utilities.LINE_HEIGHT) >> 1), Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
                } else {
                    this.petIcon.drawFrame(g, info2.lessId, this.mainx + 5, secy2 + (secheight2 >> 1), 0, 6);
                    Tool.draw3DString(g, info2.lessTitle, this.mainx + 22, secy2 + ((secheight2 - Utilities.LINE_HEIGHT) >> 1), Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
                }
            }
            g.drawImage(Tool.img_infoBg, DirectionPad.instance.getWidth(), World.viewHeight - CornerButton.instance.getHeight(), 36);
            if (this.tipDraw != null) {
                int startx = DirectionPad.instance.getWidth() + 46;
                int endx = (Tool.img_infoBg.getWidth() + startx) - 92;
                int drawy = ((World.viewHeight - CornerButton.instance.getHeight()) - Tool.img_infoBg.getHeight()) + 7;
                if (this.tipDraw.getMaxWidth() <= endx - startx) {
                    this.tipx = startx;
                } else if (this.tipx + this.tipDraw.getMaxWidth() < startx) {
                    this.tipx = endx;
                } else {
                    this.tipx -= 5;
                }
                g.setClip(startx, drawy - 4, endx - startx, Tool.img_infoBg.getHeight() + 8);
                this.tipDraw.draw3D(g, this.tipx, ((World.viewHeight - CornerButton.instance.getHeight()) - Tool.img_infoBg.getHeight()) + 7, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
            }
            drawBtn(g, (this.tabley + this.tableHeight) - (Utilities.LINE_HEIGHT >> 1));
        }
        if (this.status == 0) {
            UI.drawTitlePanel(g, this.title, 3, this.titleY, World.viewWidth - 6, Utilities.TITLE_HEIGHT, 8);
            this.mainPetTable.paint(g);
            drawRectPanel(g, this.mainx, this.mainy, this.mainwidth, this.mainheight);
            Tool.draw3DString(g, "基础灵兽", (this.mainwidth / 2) + this.mainx, this.mainy + 3, Tool.clr_androidTextDefault, 0, 17);
            if (this.main != null) {
                drawPetInfo(g, this.main, this.mainx, this.mainy + 5, this.mainwidth, this.mainName);
            }
            if (!(this.main == null && this.lesser == null && this.secx >= World.viewWidth)) {
                drawRectPanel(g, this.secx, this.secy, this.secwidth, this.secheight);
                g.drawLine(this.mainx + 3, this.secy + 3 + Utilities.LINE_HEIGHT, (this.mainx + this.mainwidth) - 6, this.secy + 3 + Utilities.LINE_HEIGHT);
                Tool.draw3DString(g, "辅助灵兽", (this.secwidth / 2) + this.mainx, this.secy + 5, Tool.clr_androidTextDefault, 0, 17);
                if (this.lesser != null) {
                    drawPetInfo(g, this.lesser, this.secx, this.secy + 5, this.secwidth, this.lessName);
                }
            }
            g.setColor(12896707);
            g.drawLine(this.mainx + 3, this.mainy + 3 + Utilities.LINE_HEIGHT, (this.mainx + this.mainwidth) - 6, this.mainy + 3 + Utilities.LINE_HEIGHT);
            g.drawImage(Tool.img_infoBg, DirectionPad.instance.getWidth(), World.viewHeight - CornerButton.instance.getHeight(), 36);
            int startx2 = DirectionPad.instance.getWidth() + 46;
            int endx2 = (Tool.img_infoBg.getWidth() + startx2) - 92;
            int drawy2 = ((World.viewHeight - CornerButton.instance.getHeight()) - Tool.img_infoBg.getHeight()) + 7;
            if (this.tipDraw.getMaxWidth() <= endx2 - startx2) {
                this.tipx = startx2;
            } else if (this.tipx + this.tipDraw.getMaxWidth() < startx2) {
                this.tipx = endx2;
            } else {
                this.tipx -= 5;
            }
            g.setClip(startx2, drawy2 - 4, endx2 - startx2, Tool.img_infoBg.getHeight() + 8);
            this.tipDraw.draw3D(g, this.tipx, ((World.viewHeight - CornerButton.instance.getHeight()) - Tool.img_infoBg.getHeight()) + 7, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11]);
            drawBtn(g, (this.tabley + this.tableHeight) - (Utilities.LINE_HEIGHT >> 1));
        } else if (this.status == 1) {
            g.setColor(0);
            g.fillRect(0, 0, World.viewWidth, World.viewHeight);
            this.bg.drawFrame(g, 0, this.bgx, this.bgy);
            this.main.draw(g);
            this.lesser.draw(g);
            if (this.aniStatus == 1 || this.aniStatus == 2) {
                this.matingLeftAni.draw(g, (this.main.pixelX + (this.main.getWidth() >> 1)) - NewStage.viewX, this.main.pixelY - this.main.getHeight());
                this.matingRightAni.draw(g, (this.lesser.pixelX + (this.lesser.getWidth() >> 1)) - NewStage.viewX, this.lesser.pixelY - this.lesser.getHeight());
            }
            if (this.aniStatus == 2) {
                this.eggAni.draw(g);
            }
        }
    }

    private void drawPetInfo(Graphics g, Pet pet, int x2, int y, int width2, MovingString name) {
        int dy = Utilities.LINE_HEIGHT + y + (Utilities.LINE_HEIGHT >> 1) + 4;
        int dx = x2 + 3;
        name.draw3D(g, dx, dy, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 36);
        Tool.draw3DString(g, pet.sex == 0 ? "♀" : "♂", (x2 + width2) - 3, dy, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 40);
        int race = pet.getAttribute(52);
        int dy2 = dy + this.petIcon.getFrameHeight(race) + 3;
        this.petIcon.drawFrame(g, race, dx, dy2, 0, 36);
        Tool.drawNumStr("+" + pet.matingTimes, g, dx + this.petIcon.getFrameWidth(race), dy2, 0, 36, -1);
        int dx2 = (x2 + width2) - 2;
        Tool.drawNumStr(String.valueOf(pet.getAttribute(50)), g, dx2, dy2, 0, 40, -1);
        Tool.uiMiscImg.drawFrame(g, 32, dx2 - Tool.uiMiscImg.getFrameWidth(32), dy2, 0, 40);
    }

    private MovingString makeMovingString(String name, int width2) {
        return new MovingString(name, width2 - 6, 2);
    }

    public void playerAni() {
        this.status = 1;
        DirectionPad.instance.showPad = false;
    }

    public void stopAni() {
        this.playerInit = false;
        this.matingLeftAni.reset();
        this.matingRightAni.reset();
        this.main.setState(0);
        this.lesser.setState(0);
        this.main.speed = 10;
        this.lesser.speed = 10;
        this.main.selfMove = true;
        this.lesser.selfMove = true;
        this.eggAni.reset();
        this.main = null;
        this.lesser = null;
        this.status = 0;
        DirectionPad.instance.showPad = true;
    }

    public int getMenuSelection() {
        return this.status;
    }

    public int getMateInfoId() {
        if (this.info == null) {
            return 0;
        }
        return this.info.id;
    }

    public void clear() {
        ImageLoadManager.release("/mating.ani");
    }

    public void handleCurrTab() {
        if (this.tab != null) {
            this.tabIdx = this.tab.getIdx();
            refreshChooseTable();
        }
    }
}
