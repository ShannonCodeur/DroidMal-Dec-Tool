package com.sumsharp.monster;

import com.sumsharp.android.ui.CornerButton;
import com.sumsharp.android.ui.DirectionPad;
import com.sumsharp.android.ui.GridMenu;
import com.sumsharp.lowui.ChallengeArena;
import com.sumsharp.lowui.StringDraw;
import com.sumsharp.lowui.TableUI;
import com.sumsharp.lowui.UI;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.GlobalVar;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.common.data.Npc;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.common.data.Skill;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.image.PipAnimateSet;
import com.sumsharp.monster.item.GameItem;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Graphics;

public class Battle {
    private static int ACTION_ALL_ATTACK = 1;
    private static int ACTION_ALL_DEFENCE = 2;
    private static int ACTION_FREE = 7;
    private static int ACTION_RUNAWAY = 8;
    private static int ACTION_SINGLE_ATTACK = 3;
    private static int ACTION_SINGLE_DEFENCE = 6;
    private static int ACTION_SINGLE_ITEM = 5;
    private static int ACTION_SINGLE_SKILL = 4;
    public static final int BATTLEID_INIT = 0;
    public static final int BATTLEID_NONE = -1;
    public static final int BATTLE_FLAG_PK = 1073741824;
    private static String MENU_ALL = "群体";
    private static String MENU_FREE = "自动";
    private static String MENU_RUNAWAY = "逃跑";
    private static String MENU_SINGLE = "单体";
    private static int MENU_X_INIT = 54;
    private static int MENU_X_SPACE = 50;
    private static int MENU_X_SPEED = 36;
    private static int MENU_Y = 100;
    public static final byte STATE_INIT = 0;
    public static final byte STATE_MENU = 2;
    public static final byte STATE_PLAY = 6;
    public static final byte STATE_PLAYMOVIE = 9;
    public static final byte STATE_PROCESSMOVIE = 7;
    public static final byte STATE_SELECTSKILL = 10;
    public static final byte STATE_SELECTTARGET = 3;
    public static final byte STATE_SHOW = 4;
    public static final byte STATE_SHOWITEM = 8;
    public static final byte STATE_SHOWMENU = 1;
    public static final byte STATE_WAIT = 5;
    private static String SUB_ALL_ATTACK = "进攻";
    private static String SUB_ALL_DEFENCE = "保命";
    private static String SUB_SINGLE_ATTACK = "攻击";
    private static String SUB_SINGLE_DEFENCE = "防御";
    private static String SUB_SINGLE_ITEM = "道具";
    private static String SUB_SINGLE_SKILL = "技能";
    public static PipAnimateSet addCast;
    public static PipAnimateSet addHit;
    public static int[] armyId;
    public static byte autoBattle = -1;
    public static Pet[] battleArmy;
    public static short battleFlg = 0;
    public static int battleID = -1;
    public static Battle battleInstance = null;
    public static Vector battleMovie = new Vector();
    public static ImageSet battleNum;
    public static Pet[] battlePet;
    public static ImageSet battleText;
    public static byte battleType = 0;
    public static ImageSet battleUI;
    public static Npc battle_target;
    public static ChallengeArena clearArena = null;
    public static Vector dialogList = new Vector();
    public static byte dialogStatus = 0;
    public static int dialogTop = dialogTopDefault;
    public static int dialogTopDefault = ((-(Utilities.LINE_HEIGHT << 1)) - 13);
    public static CartoonPlayer emotePlayerLeft;
    public static CartoonPlayer emotePlayerRight;
    public static PipAnimateSet magCast;
    public static PipAnimateSet magHit;
    public static int[] petId;
    public static PipAnimateSet phyCast;
    public static PipAnimateSet phyHit;
    public static int playFrame;
    public static Vector playObjects = new Vector();
    public static short round;
    private int action;
    private String[] allMenu;
    private TableUI bag;
    private int battleFrame = 0;
    private boolean battleInited = false;
    private boolean battleOver = false;
    public byte battleStatus;
    private GameItem[] canUseItems;
    private int closeBtnW;
    private int closeBtnX;
    private int closeBtnY;
    private String[] currMenu;
    private boolean exitBattle = false;
    private int findIdxStart;
    private int[] inPositionX;
    private int[] inPositionY;
    private boolean includeDie = false;
    private boolean isCatch = false;
    public int lastMov = 0;
    private int menuIdx;
    private int[] menuX;
    private int petSelIdx;
    private int pressx;
    private int pressy;
    private int[] skillIds;
    private boolean skillSelected = false;
    private TableUI skillTable;
    private boolean[] skill_status;
    public int[] stealSkills;
    private int subAction;
    private int[] tar;
    private int target;
    private int timer = -1;

    public static void startBattle(Npc monster) {
        battle_target = monster;
        battleID = 0;
        CommonComponent.closeAllUI();
        battleInstance = new Battle();
    }

    public static void addDialog(String dialog) {
        dialogList.addElement(new Object[]{dialog, new Long(System.currentTimeMillis())});
    }

    public static void paintDialog(Graphics g) {
        if (dialogStatus != 0) {
            g.setColor(11612202);
            g.fillRect(0, dialogTop, World.viewWidth, Utilities.LINE_HEIGHT << 1);
            Tool.uiMiscImg.drawFrame(g, 85, 0, dialogTop);
            Tool.uiMiscImg.drawFrame(g, 86, 0, dialogTop + (Utilities.LINE_HEIGHT << 1));
            g.setColor(8398617);
            g.fillRect(49, dialogTop + (Utilities.LINE_HEIGHT << 1), World.viewWidth - 49, 2);
            int drawLine = 0;
            for (int i = dialogList.size() - 1; i >= 0 && drawLine < 2; i--) {
                StringDraw sd = new StringDraw((String) ((Object[]) dialogList.elementAt(i))[0], World.viewWidth - 50, -1);
                sd.drawShadow(g, 50, dialogTop + 1, 16369735, 7413775);
                drawLine += sd.getLinePerPage();
            }
        }
    }

    public static void cycleDialog() {
        if (dialogList.size() > 0) {
            dialogStatus = 1;
        } else if (dialogTop <= dialogTopDefault) {
            dialogStatus = 0;
            dialogTop = dialogTopDefault;
        } else {
            dialogStatus = 2;
        }
        switch (dialogStatus) {
            case 1:
                if (dialogTop + 8 >= 0) {
                    dialogTop = 0;
                    break;
                } else {
                    dialogTop += 8;
                    break;
                }
            case 2:
                dialogTop -= 8;
                break;
        }
        Vector needRemove = new Vector();
        for (int i = 0; i < dialogList.size(); i++) {
            Object[] item = (Object[]) dialogList.elementAt(i);
            if (System.currentTimeMillis() - ((Long) item[1]).longValue() > 5000) {
                needRemove.addElement(item);
            }
        }
        for (int i2 = 0; i2 < needRemove.size(); i2++) {
            dialogList.removeElement(needRemove.elementAt(i2));
        }
    }

    public static void select(Pet pet) {
        boolean z;
        boolean z2;
        for (int i = 0; i < battlePet.length; i++) {
            Pet pet2 = battlePet[i];
            if (pet == battlePet[i]) {
                z2 = true;
            } else {
                z2 = false;
            }
            pet2.selected = z2;
        }
        for (int i2 = 0; i2 < battleArmy.length; i2++) {
            Pet pet3 = battleArmy[i2];
            if (pet == battleArmy[i2]) {
                z = true;
            } else {
                z = false;
            }
            pet3.selected = z;
        }
    }

    public static void clearBattle() {
        if (clearArena != null) {
            clearArena.clear();
            clearArena = null;
        }
        if (battle_target != null) {
            battle_target.setState(4);
        }
        battle_target = null;
        battleID = -1;
        CommonData.player.runawayTime = 2000;
        NewStage.scriptMoveMap = false;
        int i = 0;
        while (battlePet != null && i < battlePet.length) {
            battlePet[i].inBattle = false;
            i++;
        }
        battlePet = null;
        battleArmy = null;
        CommonData.player.inBattle = false;
        CommonData.player.resetFollowPetPosition();
        GridMenu.instance.toNormalMode();
        int i2 = 0;
        while (CommonData.player.pets != null && i2 < CommonData.player.pets.size()) {
            Pet pet = (Pet) CommonData.player.pets.elementAt(i2);
            pet.clearEmote();
            pet.visible = true;
            pet.showDie = false;
            pet.clearStatus();
            i2++;
        }
        GetItem.processBattleGetItem();
        if (phyCast != null) {
            ImageLoadManager.release(phyCast.name);
        }
        if (magCast != null) {
            ImageLoadManager.release(magCast.name);
        }
        if (addCast != null) {
            ImageLoadManager.release(addCast.name);
        }
        if (phyHit != null) {
            ImageLoadManager.release(phyHit.name);
        }
        if (magHit != null) {
            ImageLoadManager.release(magHit.name);
        }
        if (addHit != null) {
            ImageLoadManager.release(addHit.name);
        }
        if (battleNum != null) {
            ImageLoadManager.release(battleNum.name);
        }
        if (battleText != null) {
            ImageLoadManager.release(battleText.name);
        }
        if (battleUI != null) {
            ImageLoadManager.release(battleUI.name);
        }
        dialogList.removeAllElements();
        dialogTop = dialogTopDefault;
        dialogStatus = 0;
        GlobalVar.setGlobalValue((String) "BATTLEFLG", 0);
        CommonData.player.setActionState(0);
        battleMovie.removeAllElements();
        battleInstance = null;
    }

    private static PipAnimateSet getImageSet(byte atkType, byte effType, byte aniType) {
        if (effType == 1) {
            if (aniType == 0) {
                return addCast;
            }
            return addHit;
        } else if (atkType == 1) {
            if (aniType == 0) {
                return phyCast;
            }
            return phyHit;
        } else if (aniType == 0) {
            return magCast;
        } else {
            return magHit;
        }
    }

    public static CartoonPlayer createCastPlayer(byte atkType, byte effType, byte dir, int x, int y) {
        PipAnimateSet ani = getImageSet(atkType, effType, 0);
        int frame = 0;
        if (dir == 0 && ani.getAnimateLength() > 1) {
            frame = 1;
        }
        return CartoonPlayer.playCartoon(ani, frame, x, y, false);
    }

    public static CartoonPlayer createHitPlayer(byte atkType, byte effType, byte dir, int x, int y) {
        PipAnimateSet ani = getImageSet(atkType, effType, 1);
        int frame = 0;
        if (dir == 0 && ani.getAnimateLength() > 1) {
            frame = 1;
        }
        return CartoonPlayer.playCartoon(ani, frame, x, y, false);
    }

    public static void readInitSegment(UWAPSegment segment) {
        battleType = segment.readByte();
        battleID = segment.readInt();
        round = segment.readShort();
        initBattle(battleID);
        byte armyCount = segment.readByte();
        armyId = segment.readInts();
        String[] armyName = segment.readStrings();
        int[] armyHp = segment.readInts();
        int[] armyHpMax = segment.readInts();
        initBattleArmy(armyCount, armyId, armyName, armyHp, segment.readInts(), armyHpMax, segment.readInts(), segment.readInts(), segment.readInts(), segment.readBooleans());
        byte petCount = segment.readByte();
        petId = segment.readInts();
        String[] petName = segment.readStrings();
        int[] petOwner = segment.readInts();
        int[] petHp = segment.readInts();
        int[] petHpMax = segment.readInts();
        initBattlePet(petCount, petId, petName, petOwner, petHp, segment.readInts(), petHpMax, segment.readInts(), segment.readInts(), segment.readInts());
        autoBattle = segment.readByte();
        battleFlg = segment.readShort();
        if (battleInstance == null) {
            battleInstance = new Battle();
        } else {
            Utilities.sendRequest(20, 6);
        }
        battleInstance.initUnitPosition();
        battleInstance.battleStatus = 4;
        CommonComponent.closeMessage();
    }

    public static void initBattle(UWAPSegment segment) {
        Npc.touchingNpc = null;
        Npc.showConnection = false;
        readInitSegment(segment);
        loadBattleImage();
        GridMenu.instance.toBattleMode();
    }

    public static void initBattle(int bId) {
        Npc.touchingNpc = null;
        battleID = bId;
        loadBattleImage();
        CommonData.player.inBattle = true;
        GlobalVar.setGlobalValue((String) "BATTLEFLG", 1);
        CommonData.player.setActionState(3);
    }

    public static int getCharIdx(char c) {
        int idx = c - '0';
        if (c == '+') {
            return 10;
        }
        if (c == '-') {
            return 11;
        }
        return idx;
    }

    public static void drawNumber(Graphics g, String str, int x, int y, int clrIdx, int anchor) {
        char[] chars = str.toCharArray();
        int strWidth = 0;
        for (char charIdx : chars) {
            strWidth += battleNum.getFrameWidth(getCharIdx(charIdx) + (clrIdx * 12));
        }
        if ((anchor & 1) != 0) {
            x -= strWidth >> 1;
            anchor = (anchor & -2) | 4;
        }
        if ((anchor & 8) != 0) {
            x -= strWidth;
            anchor = (anchor & -2) | 4;
        }
        for (char charIdx2 : chars) {
            int idx = getCharIdx(charIdx2) + (clrIdx * 12);
            battleNum.drawFrame(g, idx, x, y, 0, anchor);
            x += battleNum.getFrameWidth(idx);
        }
    }

    public static void loadBattleImage() {
        phyCast = ImageLoadManager.getAnimate("/phycast.ani");
        addCast = ImageLoadManager.getAnimate("/addcast.ani");
        magCast = ImageLoadManager.getAnimate("/magcast.ani");
        phyHit = ImageLoadManager.getAnimate("/phyhit.ani");
        addHit = ImageLoadManager.getAnimate("/addhit.ani");
        magHit = ImageLoadManager.getAnimate("/maghit.ani");
        battleNum = ImageLoadManager.getImage("battleNum.jgp");
        battleText = ImageLoadManager.getImage("battleText.jgp");
        phyCast.toFullBuffer();
        addCast.toFullBuffer();
        magCast.toFullBuffer();
        phyHit.toFullBuffer();
        addHit.toFullBuffer();
        magHit.toFullBuffer();
        battleNum.pipImg.toFullBuffer();
    }

    public static void initBattlePet(int amount, int[] id, String[] name, int[] owner, int[] hp, int[] mp, int[] hpmax, int[] mpmax, int[] level, int[] icon) {
        battlePet = new Pet[amount];
        for (int i = 0; i < amount; i++) {
            if (owner[i] == CommonData.player.id) {
                battlePet[i] = CommonData.player.getPet(id[i]);
            }
            if (battlePet[i] == null) {
                battlePet[i] = new Pet();
            }
            battlePet[i].id = id[i];
            battlePet[i].ownerId = owner[i];
            battlePet[i].setAttribute(23, hp[i]);
            battlePet[i].setAttribute(24, mp[i]);
            battlePet[i].setAttribute(25, hpmax[i]);
            battlePet[i].setAttribute(26, mpmax[i]);
            battlePet[i].setAttribute(50, level[i]);
            battlePet[i].name = name[i];
            battlePet[i].visible = true;
            battlePet[i].inBattle = true;
            battlePet[i].showHp = true;
            battlePet[i].setIconID((short) icon[i]);
            battlePet[i].setDir(0);
        }
    }

    public static void initBattleArmy(int amount, int[] id, String[] name, int[] hp, int[] mp, int[] hpmax, int[] mpmax, int[] level, int[] icon, boolean[] canCatch) {
        battleArmy = new Pet[amount];
        for (int i = 0; i < amount; i++) {
            battleArmy[i] = new Pet();
            battleArmy[i].id = id[i];
            battleArmy[i].setAttribute(23, hp[i]);
            battleArmy[i].setAttribute(24, mp[i]);
            battleArmy[i].setAttribute(25, hpmax[i]);
            battleArmy[i].setAttribute(26, mpmax[i]);
            battleArmy[i].setAttribute(50, level[i]);
            battleArmy[i].name = name[i];
            battleArmy[i].visible = true;
            battleArmy[i].inBattle = true;
            battleArmy[i].showHp = true;
            battleArmy[i].setIconID((short) icon[i]);
            battleArmy[i].setDir(1);
            battleArmy[i].canCatch = canCatch[i];
        }
    }

    public static Pet findBattlePet(int id) {
        if (battlePet != null) {
            for (int i = 0; i < battlePet.length; i++) {
                if (battlePet[i].id == id) {
                    return battlePet[i];
                }
            }
        }
        return null;
    }

    public static Pet findBattleArmy(int id) {
        if (battleArmy != null) {
            for (int i = 0; i < battleArmy.length; i++) {
                if (battleArmy[i].id == id) {
                    return battleArmy[i];
                }
            }
        }
        return null;
    }

    public static Pet findBattleUnit(int id) {
        Pet ret = findBattlePet(id);
        if (ret == null) {
            return findBattleArmy(id);
        }
        return ret;
    }

    public static void readRoundEnd(int battleId, short round2) {
        battleID = battleId;
        round = round2;
        battleMovie = new Vector();
    }

    public static boolean isMyPet(int id) {
        Pet pet = findBattleUnit(id);
        return pet != null && pet.ownerId == CommonData.player.id;
    }

    public static boolean isPetDie(int id) {
        Pet pet = findBattleUnit(id);
        return pet != null && pet.isDie();
    }

    public static boolean isPetRunAway(int id) {
        Pet pet = findBattleUnit(id);
        return pet != null && pet.isRunaway();
    }

    public Battle() {
        initBattle();
    }

    public void cycleBattle() {
        int endy;
        if (this.exitBattle) {
            clearBattle();
            CommonComponent.closeAllUI();
        }
        this.pressx = World.pressedx;
        this.pressy = World.pressedy;
        GridMenu.instance.cycle();
        handleCloseBtn();
        if (this.battleStatus == 0) {
            if (World.fullChatUI == null) {
                CommonComponent.showMessage("系统消息", -1, "正在载入战斗...", false, false, false);
            }
            this.battleStatus = 5;
        } else if (this.battleStatus == 1) {
            cycleShowMenu();
        } else if (this.battleStatus == 2) {
            if (!cycleMenu() && World.pressedx != -1 && World.pressedy != -1) {
                this.tar = armyId;
                int idx = getTargetPointIdx();
                if (idx > -1) {
                    this.target = idx;
                    select(findBattleUnit(this.tar[this.target]));
                    this.action = ACTION_SINGLE_ATTACK;
                    fight();
                }
            }
        } else if (this.battleStatus == 4) {
            cycleShow();
        } else if (this.battleStatus == 3) {
            cycleSelectTarget();
        } else if (this.battleStatus == 5) {
        } else {
            if (this.battleStatus == 9) {
                cycleMovie();
                if (autoBattle == 1) {
                    boolean sel = false;
                    if (!(this.pressx == -1 || this.pressy == -1)) {
                        if (World.viewHeight <= 240) {
                            endy = (World.viewHeight - ((Utilities.LINE_HEIGHT * 2) + 4)) + 2;
                        } else {
                            endy = (World.viewHeight - CornerButton.instance.getHeight()) - 5;
                        }
                        int y = endy - Tool.img_rectbtn0.getHeight();
                        int w = Utilities.font.stringWidth("关闭自动战斗") + Utilities.CHAR_HEIGHT;
                        int x = World.viewWidth - w;
                        if (this.pressx >= x && this.pressx <= x + w && this.pressy >= y && this.pressy <= endy) {
                            sel = true;
                            World.pressedy = -1;
                            World.pressedx = -1;
                        }
                    }
                    if (sel || Utilities.isKeyPressed(10, true)) {
                        Utilities.sendRequest(20, 19);
                        autoBattle = 0;
                    }
                }
            } else if (this.battleStatus == 8) {
                this.bag.cycle();
                if (Utilities.isKeyPressed(9, true) || Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(16, true)) {
                    int idx2 = this.bag.getMenuSelection();
                    if (idx2 >= 0 && idx2 < this.canUseItems.length) {
                        GameItem item = this.canUseItems[idx2];
                        if (item.reqLevel > CommonData.player.level) {
                            CommonComponent.showMessage("系统消息", -1, "级别不足无法使用", true, true, false);
                            return;
                        }
                        this.tar = petId;
                        if (item != null) {
                            if (item.type == 5) {
                                if (CommonData.player.isBagFull()) {
                                    CommonComponent.showMessage("系统消息", -1, "您的背包已满，不能再捕捉了！", true, false, false);
                                    this.battleStatus = 1;
                                    return;
                                }
                                this.isCatch = true;
                            }
                            this.subAction = item.id;
                            if (item.getUseTarget() == 32) {
                                this.tar = armyId;
                            }
                        }
                        this.target = 0;
                        select(findBattleUnit(this.tar[this.target]));
                        this.battleStatus = 3;
                    }
                } else if (Utilities.isKeyPressed(10, true)) {
                    this.battleStatus = 2;
                }
            } else if (this.battleStatus == 10) {
                this.skillTable.cycle();
                if (this.skill_status[this.skillTable.getMenuSelection()]) {
                    this.skillTable.setCmd("使用", "返回");
                } else {
                    this.skillTable.setCmd(null, "返回");
                }
                Pet pet = findBattlePet(petId[this.petSelIdx]);
                if (this.skillTable.isClosed()) {
                    if (this.skillSelected) {
                        int skillId = this.skillIds[this.skillTable.getMenuSelection()];
                        Skill skill = pet.getSkill(skillId);
                        if (!isSkillStealed(pet.id, skillId)) {
                            this.subAction = skillId;
                            int atkBound = skill.atkBound;
                            this.target = 0;
                            if (atkBound == 1 || atkBound == 3) {
                                this.tar = petId;
                                select(findBattleUnit(this.tar[this.target]));
                                this.battleStatus = 3;
                                this.includeDie = false;
                                if (atkBound == 3) {
                                    this.includeDie = true;
                                }
                            } else if (atkBound == 4) {
                                this.tar = armyId;
                                select(findBattleUnit(this.tar[this.target]));
                                this.battleStatus = 3;
                            } else {
                                this.target = -1;
                                fight();
                            }
                        }
                    } else {
                        this.battleStatus = 2;
                    }
                    this.skillSelected = false;
                } else if (Utilities.isKeyPressed(9, true) || Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(16, true)) {
                    if (this.skill_status[this.skillTable.getMenuSelection()]) {
                        if (!isSkillStealed(pet.id, this.skillIds[this.skillTable.getMenuSelection()])) {
                            this.skillSelected = true;
                            this.skillTable.close();
                        }
                    }
                } else if (Utilities.isKeyPressed(10, true)) {
                    this.skillTable.close();
                }
            }
        }
    }

    private boolean isSkillStealed(int petid, int skillId) {
        if (this.stealSkills == null) {
            return false;
        }
        for (int i = 0; i < this.stealSkills.length; i += 2) {
            int pid = this.stealSkills[i];
            if (this.stealSkills[i + 1] == skillId && pid == petid) {
                return true;
            }
        }
        return false;
    }

    private void battleMovieCheck() {
        if (this.battleFrame == 0) {
            int frameChange = 0;
            for (int i = 0; i < battleMovie.size(); i++) {
                BattleMovie movie = (BattleMovie) battleMovie.elementAt(i);
                movie.startFrame += frameChange;
                if (movie.type == 1 && movie.atkType == 1) {
                    Pet src = movie.src;
                    int length = src.cartoonPlayer.animate.getAnimateFrameLength(src.getFaceto() + 4);
                    if (movie.isHit) {
                        frameChange += src.cartoonPlayer.animate.attKeyFrame - movie.frameLength;
                    } else {
                        frameChange += length - movie.frameLength;
                    }
                    movie.frameLength = length - 1;
                }
            }
        }
        for (int i2 = 0; i2 < battleMovie.size(); i2++) {
            BattleMovie movie2 = (BattleMovie) battleMovie.elementAt(i2);
            if (movie2.startFrame == this.battleFrame && !movie2.started) {
                movie2.start();
            }
        }
    }

    private boolean isRoundOver() {
        for (int i = 0; i < battleMovie.size(); i++) {
            if (!((BattleMovie) battleMovie.elementAt(i)).die) {
                return false;
            }
        }
        return true;
    }

    private void cycleMovie() {
        battleMovieCheck();
        for (int i = 0; i < battleMovie.size(); i++) {
            ((BattleMovie) battleMovie.elementAt(i)).cycle(this.battleFrame);
        }
        this.battleFrame++;
        if (isRoundOver()) {
            this.currMenu = this.allMenu;
            initMenuLocation();
            this.menuIdx = 1;
            this.battleStatus = 1;
            if (this.battleOver) {
                clearBattle();
            }
            Utilities.sendRequest(20, 18);
        }
        this.findIdxStart = 0;
    }

    private int getCommandIcon(String command, boolean select) {
        int icon = 0;
        if (command.equals(MENU_FREE)) {
            icon = 0;
        } else if (command.equals(MENU_ALL)) {
            icon = 4;
        } else if (command.equals(MENU_SINGLE)) {
            icon = 2;
        } else if (command.equals(MENU_RUNAWAY)) {
            icon = 16;
        } else if (command.equals(SUB_ALL_ATTACK)) {
            icon = 6;
        } else if (command.equals(SUB_ALL_DEFENCE)) {
            icon = 12;
        } else if (command.equals(SUB_SINGLE_ATTACK)) {
            icon = 19;
        } else if (command.equals(SUB_SINGLE_SKILL)) {
            icon = 14;
        } else if (command.equals(SUB_SINGLE_ITEM)) {
            icon = 8;
        } else if (command.equals(SUB_SINGLE_DEFENCE)) {
            icon = 10;
        }
        if (!select) {
            return icon + 1;
        }
        return icon;
    }

    private void cycleSelectTarget() {
        if (this.tar != null) {
            checkTargetSelect();
            boolean psel = false;
            int idx = getTargetPointIdx();
            if (idx > -1) {
                psel = true;
                this.target = idx;
                select(findBattleUnit(this.tar[this.target]));
            }
            if (Utilities.isKeyPressed(10, true)) {
                this.battleStatus = 2;
            } else if (Utilities.isKeyPressed(0, true) || Utilities.isKeyPressed(13, true)) {
                this.lastMov = 0;
                prevTarget();
            } else if (Utilities.isKeyPressed(1, true) || Utilities.isKeyPressed(19, true)) {
                this.lastMov = 1;
                nextTarget();
            } else if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
                if (isEnemySelected()) {
                    this.lastMov = 1;
                    nextTarget();
                } else {
                    this.lastMov = 0;
                    prevTarget();
                }
            } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
                if (isEnemySelected()) {
                    this.lastMov = 0;
                    prevTarget();
                } else {
                    this.lastMov = 1;
                    nextTarget();
                }
            } else if (psel || Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(9, true) || Utilities.isKeyPressed(16, true)) {
                if (this.isCatch) {
                    int lv = CommonData.player.level;
                    Pet unit = findBattleUnit(this.tar[this.target]);
                    int tarLv = unit.getAttribute(50);
                    if (!unit.canCatch) {
                        CommonComponent.showMessage("系统消息", -1, "目标无法捕捉！", true, false, true);
                    } else if (tarLv - lv > 5) {
                        CommonComponent.showMessage("系统消息", -1, "不能捕捉高于自己5级的灵兽", true, false, true);
                    } else {
                        select(null);
                        fight();
                    }
                } else {
                    select(null);
                    fight();
                }
            }
            checkTargetSelect();
        }
    }

    private boolean isEnemySelected() {
        boolean isEnemySel = false;
        Pet unit = findBattleUnit(this.tar[this.target]);
        for (Pet equals : battleArmy) {
            if (equals.equals(unit)) {
                isEnemySel = true;
            }
        }
        return isEnemySel;
    }

    private void nextTarget() {
        this.target++;
        if (this.target >= this.tar.length) {
            this.target = 0;
        }
        select(findBattleUnit(this.tar[this.target]));
    }

    private void prevTarget() {
        this.target--;
        if (this.target < 0) {
            this.target = this.tar.length - 1;
        }
        select(findBattleUnit(this.tar[this.target]));
    }

    private void checkTargetSelect() {
        if (this.target - 1 < 0) {
            int endIdx = this.tar.length - 1;
        }
        while (true) {
            Pet pet = findBattleUnit(this.tar[this.target]);
            if (!canSelect(pet == null ? -1 : pet.battleStatus)) {
                if (this.lastMov == 1) {
                    nextTarget();
                } else {
                    prevTarget();
                }
                select(findBattleUnit(this.tar[this.target]));
            } else {
                return;
            }
        }
    }

    private boolean canSelect(int status) {
        if (((status & 1) == 0 || this.includeDie) && (status & 4) == 0) {
            return true;
        }
        return false;
    }

    private void cycleShow() {
        this.battleStatus = 1;
        savePosition();
    }

    private void savePosition() {
        for (int i = 0; i < battlePet.length; i++) {
            if (battlePet[i] != null) {
                battlePet[i].savePosition();
            }
        }
        for (int i2 = 0; i2 < battleArmy.length; i2++) {
            if (battleArmy[i2] != null) {
                battleArmy[i2].savePosition();
            }
        }
    }

    private int getTargetPointIdx() {
        if (this.pressx == -1 || this.pressy == -1) {
            return -1;
        }
        for (int i = 0; i < this.tar.length; i++) {
            Pet unit = findBattleUnit(this.tar[i]);
            int[] bound = unit.getSelectBounds();
            if (this.pressx >= bound[0] && this.pressx <= bound[0] + bound[2] && this.pressy >= bound[1] && this.pressy <= bound[1] + bound[3] && canSelect(unit.battleStatus)) {
                return i;
            }
        }
        return -1;
    }

    private int getPointMenuIdx() {
        if (this.pressx == -1 || this.pressy == -1) {
            return -1;
        }
        int i = this.currMenu.length - 1;
        while (i >= 0) {
            int icon = getCommandIcon(this.currMenu[i], i == this.menuIdx);
            int h = battleUI.getFrameHeight(icon);
            int w = battleUI.getFrameWidth(icon);
            int x = this.menuX[i] - (w / 2);
            int y = MENU_Y - h;
            if (this.pressx >= x && this.pressx <= x + w && this.pressy >= y && this.pressy <= y + h) {
                return i;
            }
            i--;
        }
        return -1;
    }

    private boolean cycleMenu() {
        boolean psel = false;
        int idx = getPointMenuIdx();
        if (idx > -1) {
            if (this.menuIdx == idx) {
                psel = true;
            } else {
                this.menuIdx = idx;
                if (this.menuIdx < 0) {
                    this.menuIdx = this.currMenu.length - 1;
                }
                if (this.menuIdx >= this.currMenu.length) {
                    this.menuIdx = 0;
                }
            }
        }
        if (Utilities.isKeyPressed(20, true)) {
            useSkill();
        }
        if (Utilities.isKeyPressed(2, true) || Utilities.isKeyPressed(15, true)) {
            this.menuIdx--;
            if (this.menuIdx < 0) {
                this.menuIdx = this.currMenu.length - 1;
            }
        } else if (Utilities.isKeyPressed(3, true) || Utilities.isKeyPressed(17, true)) {
            this.menuIdx++;
            if (this.menuIdx >= this.currMenu.length) {
                this.menuIdx = 0;
            }
        } else if (Utilities.isKeyPressed(4, true) || Utilities.isKeyPressed(9, true) || Utilities.isKeyPressed(16, true) || psel) {
            String cmd = this.currMenu[this.menuIdx];
            if (cmd.equals(MENU_FREE)) {
                if (CommonData.player.level < 10) {
                    CommonComponent.showMessage("系统消息", -1, "当您10级时，可以免费使用自动战斗功能", true, false, true);
                } else if (autoBattle == 0) {
                    Utilities.sendRequest(20, 19);
                    autoBattle = 1;
                    this.battleStatus = 5;
                } else {
                    CommonComponent.showMessage("系统消息", -1, "本次战斗禁止使用自动战斗", true, false, true);
                }
            } else if (!cmd.equals(MENU_ALL)) {
                if (cmd.equals(MENU_SINGLE)) {
                    this.currMenu = this.allMenu;
                    initMenuLocation();
                    this.battleStatus = 1;
                } else if (cmd.equals(MENU_RUNAWAY)) {
                    this.action = ACTION_RUNAWAY;
                    this.target = -1;
                    fight();
                } else if (cmd.equals(SUB_ALL_ATTACK)) {
                    this.action = ACTION_ALL_ATTACK;
                    this.target = 0;
                    this.tar = armyId;
                    select(findBattleUnit(armyId[this.target]));
                    this.battleStatus = 3;
                    this.petSelIdx = -1;
                } else if (cmd.equals(SUB_SINGLE_ATTACK)) {
                    this.action = ACTION_SINGLE_ATTACK;
                    this.target = 0;
                    this.tar = armyId;
                    select(findBattleUnit(armyId[this.target]));
                    this.battleStatus = 3;
                } else if (cmd.equals(SUB_SINGLE_ITEM)) {
                    if ((battleFlg & 1) != 0) {
                        CommonComponent.showMessage("系统消息", -1, "本次战斗禁止使用物品", true, false, true);
                    } else {
                        this.action = ACTION_SINGLE_ITEM;
                        this.battleStatus = 8;
                        initBag();
                    }
                } else if (cmd.equals(SUB_SINGLE_SKILL)) {
                    useSkill();
                } else if (cmd.equals(SUB_SINGLE_DEFENCE)) {
                    this.action = ACTION_SINGLE_DEFENCE;
                    this.target = -1;
                    fight();
                }
            }
        } else if (Utilities.isKeyPressed(10, true) && this.currMenu != this.allMenu) {
            this.currMenu = this.allMenu;
            initMenuLocation();
            this.menuIdx = 1;
            this.battleStatus = 1;
        }
        return psel;
    }

    public void useSkill() {
        initSkill(findBattlePet(petId[this.petSelIdx]));
        if (this.skillIds.length == 0) {
            CommonComponent.showMessage("系统消息", -1, "无可用技能！", true, false, true);
            return;
        }
        this.action = ACTION_SINGLE_SKILL;
        this.battleStatus = 10;
    }

    private void fight() {
        UWAPSegment segment = new UWAPSegment(20, 5);
        try {
            segment.writeShort(round);
            if (this.petSelIdx < 0) {
                segment.writeInt(-1);
            } else {
                segment.writeInt(petId[this.petSelIdx]);
            }
            segment.writeInt(this.action);
            segment.writeInt(this.subAction);
            if (this.target >= 0) {
                segment.writeInt(this.tar[this.target]);
            } else {
                segment.writeInt(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utilities.sendRequest(segment);
        this.findIdxStart = this.petSelIdx + 1;
        int nextId = nextMyPetID();
        if (nextId != -1) {
            this.battleStatus = 2;
            this.petSelIdx = nextId;
            select(findBattleUnit(petId[this.petSelIdx]));
            return;
        }
        this.battleStatus = 5;
        this.findIdxStart = 0;
    }

    private int nextMyPetID() {
        for (int i = this.petSelIdx + 1; i < petId.length; i = i + 1 + 1) {
            if (isMyPet(petId[i]) && !isPetDie(petId[i]) && !isPetRunAway(petId[i])) {
                return i;
            }
        }
        return -1;
    }

    public void cycleShowMenu() {
        boolean moving = false;
        getMyPetID();
        if (this.petSelIdx == -1 || autoBattle == 1) {
            this.battleStatus = 5;
        }
        for (int idx = 1; idx < this.currMenu.length; idx++) {
            if (this.menuX[idx] - this.menuX[idx - 1] < MENU_X_SPACE) {
                int[] iArr = this.menuX;
                iArr[idx] = iArr[idx] + MENU_X_SPEED;
                if (this.menuX[idx] - this.menuX[idx - 1] > MENU_X_SPACE) {
                    this.menuX[idx] = this.menuX[idx - 1] + MENU_X_SPACE;
                }
                for (int i = idx + 1; i < this.currMenu.length; i++) {
                    this.menuX[i] = this.menuX[i - 1];
                }
                moving = true;
            }
            if (moving) {
                break;
            }
        }
        if (!moving) {
            getMyPetID();
            this.battleStatus = 2;
        }
    }

    public void getMyPetID() {
        boolean found = false;
        int i = this.findIdxStart;
        while (true) {
            if (i < petId.length) {
                if (isMyPet(petId[i]) && !isPetDie(petId[i]) && !isPetRunAway(petId[i])) {
                    this.petSelIdx = i;
                    found = true;
                    select(findBattleUnit(petId[i]));
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (!found) {
            this.petSelIdx = -1;
        }
        this.isCatch = false;
    }

    public void initBattle() {
        if (!this.battleInited) {
            battleUI = ImageLoadManager.getImage("battle.jgp");
            battleUI.pipImg.toFullBuffer();
            this.battleInited = true;
            this.battleStatus = 0;
            initMenu();
            initPoint();
            if (battleID == -1 || battleID == 0) {
                this.battleStatus = 0;
                requestBattle();
                return;
            }
            this.findIdxStart = 0;
            getMyPetID();
            initUnitPosition();
            this.battleStatus = 4;
            Utilities.sendRequest(20, 6);
        }
    }

    private void initUnitPosition() {
        for (int i = 0; i < petId.length; i++) {
            AbstractUnit unit = Tool.getUnit(petId[i], 10);
            if (unit != null) {
                unit.setPosition(this.inPositionX[i] - (unit.getWidth() / 2), this.inPositionY[i]);
                unit.cartoonPlayer.setAnimateIndex(2);
            }
        }
        for (int i2 = 0; i2 < armyId.length; i2++) {
            AbstractUnit unit2 = Tool.getUnit(armyId[i2], 11);
            if (unit2 != null) {
                unit2.setPosition(this.inPositionX[i2 + 3] - (unit2.getWidth() / 2), this.inPositionY[i2 + 3]);
                unit2.cartoonPlayer.setAnimateIndex(3);
            }
        }
    }

    private void initMenu() {
        this.allMenu = new String[6];
        this.allMenu[0] = MENU_FREE;
        this.allMenu[1] = SUB_SINGLE_ATTACK;
        this.allMenu[2] = SUB_SINGLE_SKILL;
        this.allMenu[3] = SUB_SINGLE_ITEM;
        this.allMenu[4] = SUB_SINGLE_DEFENCE;
        this.allMenu[5] = MENU_RUNAWAY;
        this.currMenu = this.allMenu;
        initMenuLocation();
        this.menuIdx = 1;
    }

    public void initMenuLocation() {
        int frameWidth = battleUI.getFrameWidth(21);
        int initMenuX = ((World.viewWidth - (this.currMenu.length * 46)) / 2) + 5;
        this.menuX = new int[this.currMenu.length];
        for (int i = 0; i < this.menuX.length; i++) {
            this.menuX[i] = initMenuX;
        }
        this.menuIdx = 0;
    }

    public void initPoint() {
        this.inPositionX = new int[6];
        this.inPositionY = new int[6];
        int centerx = World.viewWidth / 2;
        int bottomy = (World.viewHeight - DirectionPad.instance.getHeight()) - 10;
        this.inPositionX[1] = (short) ((120 / 2) + centerx);
        this.inPositionX[0] = (short) (this.inPositionX[1] + 10);
        this.inPositionX[2] = (short) (this.inPositionX[1] - 10);
        this.inPositionX[4] = (short) (centerx - (120 / 2));
        this.inPositionX[3] = (short) (this.inPositionX[4] - 10);
        this.inPositionX[5] = (short) (this.inPositionX[4] + 10);
        this.inPositionY[0] = (short) bottomy;
        this.inPositionY[1] = (short) (this.inPositionY[0] - 35);
        this.inPositionY[2] = (short) (this.inPositionY[1] - 35);
        this.inPositionY[3] = this.inPositionY[0];
        this.inPositionY[4] = this.inPositionY[1];
        this.inPositionY[5] = this.inPositionY[2];
    }

    public void requestBattle() {
        UWAPSegment seg = new UWAPSegment(20, 1);
        try {
            seg.writeInt(battle_target == null ? -1 : battle_target.id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utilities.sendRequest(seg);
    }

    public void drawBattle(Graphics g) {
        int endy;
        if (this.battleStatus == 1) {
            getMyPetID();
            if (this.petSelIdx != -1) {
                drawBattleMenu(g);
            }
        } else if (this.battleStatus == 2) {
            drawBattleMenu(g);
        } else if (this.battleStatus == 9) {
            for (int i = 0; i < battleMovie.size(); i++) {
                BattleMovie bm = (BattleMovie) battleMovie.elementAt(i);
                if (!bm.die && bm.started) {
                    bm.paint(g);
                }
            }
            if (autoBattle == 1) {
                if (World.viewHeight <= 240) {
                    endy = (World.viewHeight - ((Utilities.LINE_HEIGHT * 2) + 4)) + 2;
                } else {
                    endy = World.viewHeight - CornerButton.instance.getHeight();
                }
                Tool.drawUIButton(g, "关闭自动", endy - 5, 8, false);
            }
        } else if (this.battleStatus == 10) {
            this.skillTable.paint(g);
        } else if (this.battleStatus == 8) {
            this.bag.paint(g);
            drawCloseBtn(g, this.closeBtnY);
        }
    }

    private void drawCloseBtn(Graphics g, int y) {
        int w = Utilities.font.stringWidth("关闭");
        int x = ((World.viewWidth - (w * 2)) - Utilities.CHAR_HEIGHT) + 10;
        UI.drawSmallTip(g, x, y, w * 2, Utilities.CHAR_HEIGHT, 0);
        int off = (w * 2) + 10;
        Tool.draw3DString(g, "关闭", ((x + off) - 30) - Utilities.CHAR_HEIGHT, (Utilities.CHAR_HEIGHT + y) - 1, Tool.CLR_TABLE[10], Tool.CLR_TABLE[11], 40);
        Tool.uiMiscImg.drawFrame(g, 63, ((x + off) - Utilities.CHAR_HEIGHT) - 1, (Utilities.CHAR_HEIGHT + y) - 1, 0, 40);
    }

    private GameItem[] getGameItemBag(int type) {
        Vector vec;
        if (type == 0) {
            vec = CommonData.player.itemBag;
        } else if (type == 500) {
            vec = CommonData.player.bank;
        } else {
            vec = new Vector();
            for (int i = 0; i < CommonData.player.itemBag.size(); i++) {
                GameItem item = (GameItem) CommonData.player.itemBag.elementAt(i);
                if (type > 1000) {
                    if ((item.sellFlag & (type - 1000)) != 0) {
                        vec.addElement(item);
                    }
                } else if (type > 0) {
                    if ((item.useFlag & type) != 0) {
                        vec.addElement(item);
                    }
                } else if (item.type == (-type)) {
                    vec.addElement(item);
                }
            }
        }
        GameItem[] ret = new GameItem[vec.size()];
        vec.copyInto(ret);
        return ret;
    }

    private void initBag() {
        this.canUseItems = getGameItemBag(4);
        this.bag = new TableUI(14, "使用物品", null, null, this.canUseItems, this.canUseItems.length, 1, 0, 0);
        this.closeBtnY = (World.viewHeight - Chat.chatHeight) - Utilities.LINE_HEIGHT;
        this.closeBtnW = Utilities.font.stringWidth("关闭菜单");
        this.closeBtnX = ((World.viewWidth - this.closeBtnW) - Utilities.CHAR_HEIGHT) + 10;
    }

    private void handleCloseBtn() {
        if (this.battleStatus == 8 && World.pressedx != -1 && World.pressedy != -1 && World.pressedx > this.closeBtnX && World.pressedx < this.closeBtnX + this.closeBtnW && World.pressedy > this.closeBtnY && World.pressedy < this.closeBtnY + 20) {
            Utilities.keyPressed(7, true);
        }
    }

    private void initSkill(Pet pet) {
        boolean checkStealSkill;
        boolean z;
        int[] widths = new int[4];
        widths[1] = -1;
        widths[2] = 35;
        widths[3] = 32;
        this.skillTable = new TableUI(-1, "使用技能", null, null, new String[]{"", "技能名称", "等级", "MP"}, widths, 0);
        this.skillIds = pet.getSkillIDs(0);
        int length = this.skillIds.length;
        this.skill_status = new boolean[length];
        for (int i = 0; i < this.skillIds.length; i++) {
            boolean checkMp = pet.getSkill(this.skillIds[i]).mp <= pet.getAttribute(24);
            if (isSkillStealed(pet.id, this.skillIds[i])) {
                checkStealSkill = false;
            } else {
                checkStealSkill = true;
            }
            boolean[] zArr = this.skill_status;
            if (!checkMp || !checkStealSkill) {
                z = false;
            } else {
                z = true;
            }
            zArr[i] = z;
        }
        if (length > 0) {
            String[] skill_ids = new String[length];
            String[] names = new String[length];
            String[] lvs = new String[length];
            String[] mps = new String[length];
            for (int i2 = 0; i2 < length; i2++) {
                Skill s = pet.getSkill(this.skillIds[i2]);
                skill_ids[i2] = String.valueOf(this.skillIds[i2]) + "," + s.level;
                names[i2] = s.name;
                if (isSkillStealed(pet.id, this.skillIds[i2])) {
                    names[i2] = String.valueOf(names[i2]) + "(被封印)";
                }
                lvs[i2] = String.valueOf(s.level);
                mps[i2] = String.valueOf(s.mp);
            }
            this.skillTable.addTableItem("", skill_ids, 14, null, null);
            this.skillTable.addTableItem("技能名称", names, 0, null, null);
            this.skillTable.addTableItem("等级", lvs, 11, null, null);
            this.skillTable.addTableItem("MP", mps, 1, null, null);
            this.skillTable.setItemStatus(this.skill_status);
        }
    }

    private void drawBattleMenu(Graphics g) {
        g.drawImage(Tool.img_battlebg, (World.viewWidth / 2) - (battleUI.getFrameWidth(21) / 2), MENU_Y - 10, 17);
        int i = this.currMenu.length - 1;
        while (i >= 0) {
            boolean select = i == this.menuIdx;
            int icon = getCommandIcon(this.currMenu[i], select);
            int h = battleUI.getFrameHeight(icon);
            battleUI.drawFrame(g, icon, this.menuX[i] - (battleUI.getFrameWidth(icon) / 2), MENU_Y - h, 0);
            if (select) {
                int sw = Utilities.font.stringWidth(this.currMenu[i]);
                int bw = sw + 14;
                int bh = Utilities.LINE_HEIGHT + 4;
                int bx = this.menuX[i] - (bw / 2);
                int by = ((MENU_Y - h) - bh) - 5;
                Tool.drawAlphaBox(g, bw, bh, bx, by, -1, true);
                Tool.drawLevelString(g, this.currMenu[i], bx + ((bw - sw) / 2), by, 20, 9, -1);
                int t = Utilities.getTimeStamp() / HttpConnection.HTTP_INTERNAL_ERROR;
                if (t != this.timer) {
                    this.timer = t;
                }
                int ffw = 48 - ((this.timer % 2) * 2);
                battleUI.drawFrame(g, 21, this.menuX[i] - (ffw / 2), MENU_Y - h, 0, 20);
                battleUI.drawFrame(g, 21, this.menuX[i] - (ffw / 2), MENU_Y, 6, 36);
                battleUI.drawFrame(g, 21, this.menuX[i] + (ffw / 2), MENU_Y - h, 2, 24);
                battleUI.drawFrame(g, 21, this.menuX[i] + (ffw / 2), MENU_Y, 3, 40);
            }
            i--;
        }
    }

    public void roundEnd(UWAPSegment segment) {
        select(null);
        int battleId = segment.readInt();
        short round2 = segment.readShort();
        int[] ids = segment.readInts();
        int[] status = segment.readInts();
        this.stealSkills = segment.readInts();
        for (int idx = 0; idx < ids.length; idx++) {
            Pet pet = findBattleUnit(ids[idx]);
            if (pet != null) {
                pet.addStatus(status[idx]);
            }
        }
        int size = segment.readByte() & AbstractUnit.CLR_NAME_TAR;
        readRoundEnd(battleId, round2);
        this.battleFrame = 0;
        for (int i = 0; i < size; i++) {
            battleMovie.addElement(BattleMovie.parseBattleMovie(segment.readBytes()));
        }
        this.battleOver = segment.readBoolean();
        this.battleStatus = 9;
        CommonComponent.closeMessage();
    }

    public void result(UWAPSegment segment) {
        byte readByte = segment.readByte();
        String result = segment.readString();
        CommonComponent.closeMessage();
        CommonComponent.showMessage("系统消息", -1, result, true, true, true);
        this.exitBattle = true;
    }
}
