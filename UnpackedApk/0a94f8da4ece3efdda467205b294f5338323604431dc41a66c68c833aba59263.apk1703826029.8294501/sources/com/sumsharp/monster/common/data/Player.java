package com.sumsharp.monster.common.data;

import com.sumsharp.monster.Battle;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.gtvm.GTVM;
import com.sumsharp.monster.image.CartoonPlayer;
import com.sumsharp.monster.image.PipAnimateSet;
import com.sumsharp.monster.item.GameItem;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Player extends AbstractUnit {
    public int arenaLose;
    public int arenaWin;
    public short bagSize;
    public Vector bank = new Vector();
    public short bankSize;
    public Pet battlePetMain;
    public Pet battlePetSecond;
    public Vector blackList;
    public Vector buffs = new Vector();
    private boolean canReachIcon = false;
    public boolean coinRefresh = false;
    public Vector coins = new Vector();
    public int contributionPoint;
    public Vector credits = new Vector();
    public int currTitle = -1;
    private int destTime = 0;
    private int destX = -1;
    private int destY = -1;
    public int doorId = -1;
    public long exp;
    public int expWidth;
    public Vector friends = new Vector();
    public int honor;
    public int honorIdx;
    public String honorTitle;
    public byte hr;
    public Vector itemBag = new Vector();
    public byte keepMoving = -1;
    public byte lastDir = -1;
    public short lastMapId;
    public short lastPositionX;
    public short lastPositionY;
    public int level;
    public long levelUpExp;
    public boolean loaded = false;
    public Vector mateMap = new Vector();
    public int money;
    public boolean needSetPosition = false;
    public Vector pets;
    public long pressTime = -1;
    private boolean pressingKey = false;
    private byte prevdir;
    public int runawayTime = 0;
    public int sessionId;
    public byte sex;
    private boolean startSearch = true;
    public AbstractUnit targetPlayer;
    public Vector tasks = new Vector();
    public Vector titles = new Vector();
    public String whisperName = "";

    public Player() {
        go(-100, -100);
        setDir(1);
    }

    public void initPlayerData(UWAPSegment seg) throws Exception {
        World.newMailX = Tool.FILL_L_DARK;
        World.mailFlg = -1;
        this.pets = new Vector();
        this.id = seg.readInt();
        this.name = seg.readString();
        this.mapId = seg.readShort();
        this.mirrorId = seg.readInt();
        this.pixelX = seg.readShort();
        this.pixelY = seg.readShort();
        byte[] data = seg.readBytes();
        this.sessionId = seg.readInt();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        this.sex = dataInputStream.readByte();
        this.hr = dataInputStream.readByte();
        this.level = dataInputStream.readInt();
        createCartoonPlayer();
        this.money = dataInputStream.readInt();
        this.contributionPoint = dataInputStream.readInt();
        this.bagSize = dataInputStream.readShort();
        this.bankSize = dataInputStream.readShort();
        this.honorIdx = dataInputStream.readByte();
        this.honor = dataInputStream.readInt();
        this.honorTitle = dataInputStream.readUTF();
        this.arenaWin = dataInputStream.readInt();
        this.arenaLose = dataInputStream.readInt();
        this.exp = dataInputStream.readLong();
        this.levelUpExp = dataInputStream.readLong();
        this.visible = true;
        this.titles.removeAllElements();
        int count = dataInputStream.readShort();
        for (int i = 0; i < count; i++) {
            Title title = new Title();
            title.load(dataInputStream);
            this.titles.addElement(title);
        }
        orderTitle();
        this.currTitle = dataInputStream.readInt();
        if (getCurrTitle() != null) {
            this.title = getCurrTitle().title;
            this.color = getCurrTitle().color;
        }
        this.tasks.removeAllElements();
        int count2 = dataInputStream.readByte();
        for (int i2 = 0; i2 < count2; i2++) {
            Task task = new Task();
            task.load(dataInputStream);
            this.tasks.addElement(task);
        }
        orderTasks();
        int count3 = dataInputStream.readShort();
        this.credits.removeAllElements();
        for (int i3 = 0; i3 < count3; i3++) {
            Credit credit = new Credit();
            credit.load(dataInputStream);
            this.credits.addElement(credit);
        }
        this.itemBag.removeAllElements();
        int count4 = dataInputStream.readShort();
        for (int i4 = 0; i4 < count4; i4++) {
            GameItem item = new GameItem();
            item.load(dataInputStream);
            if (item.type == 18) {
                this.coins.addElement(item);
            } else {
                this.itemBag.addElement(item);
            }
        }
        orderBag();
        this.bank.removeAllElements();
        int count5 = dataInputStream.readShort();
        for (int i5 = 0; i5 < count5; i5++) {
            GameItem item2 = new GameItem();
            item2.load(dataInputStream);
            this.bank.addElement(item2);
        }
        orderBank();
        this.friends.removeAllElements();
        int count6 = dataInputStream.readByte();
        for (int i6 = 0; i6 < count6; i6++) {
            Friend friend = new Friend();
            friend.id = dataInputStream.readInt();
            friend.name = dataInputStream.readUTF();
            friend.sex = dataInputStream.readByte();
            friend.state = dataInputStream.readByte();
            this.friends.addElement(friend);
        }
        orderFriends();
        int count7 = dataInputStream.readByte();
        for (int i7 = 0; i7 < count7; i7++) {
            int readInt = dataInputStream.readInt();
            dataInputStream.readUTF();
        }
        dataInputStream.readUTF();
        this.pets.removeAllElements();
        int count8 = dataInputStream.readByte();
        for (int i8 = 0; i8 < count8; i8++) {
            Pet pet = new Pet();
            pet.load(dataInputStream);
            this.pets.addElement(pet);
        }
        int battlePetMainId = dataInputStream.readInt();
        int battlePetSecondId = dataInputStream.readInt();
        int followPetId = dataInputStream.readInt();
        this.battlePetMain = getPet(battlePetMainId);
        if (this.battlePetMain != null) {
            this.battlePetMain.battleOpened = true;
        }
        this.battlePetSecond = getPet(battlePetSecondId);
        if (this.battlePetSecond != null) {
            this.battlePetSecond.battleOpened = true;
        }
        setFollowPet(getPet(followPetId));
        if (getFollowPet() != null) {
            getFollowPet().followOpened = true;
        }
        orderPets();
        this.guild = dataInputStream.readUTF();
        byte c = dataInputStream.readByte();
        for (int i9 = 0; i9 < c; i9++) {
            Buff buff = new Buff();
            buff.load(dataInputStream);
            addBuff(buff);
        }
        short cc = dataInputStream.readShort();
        for (int i10 = 0; i10 < cc; i10++) {
            MateInfo info = new MateInfo();
            info.load(dataInputStream);
            this.mateMap.addElement(info);
        }
        orderMateMap();
        NewStage.setting = new byte[6];
        dataInputStream.read(NewStage.setting);
        GTVM.saveRMSFile(NewStage.OTHER_SETTING_DBNAME, NewStage.setting);
        go(0, 0);
        this.loaded = true;
        refreshExpWidth();
    }

    public void refreshExpWidth() {
        if (this.levelUpExp == 0) {
            this.expWidth = 0;
        } else {
            this.expWidth = (int) ((this.exp * ((long) World.instance.uiExpWidth)) / this.levelUpExp);
        }
    }

    public Title getCurrTitle() {
        if (this.currTitle != -1) {
            return findTitle(this.currTitle);
        }
        return null;
    }

    public void addBuff(Buff buff) {
        Buff remove = null;
        Buff add = buff;
        if (buff.remainTime <= 0) {
            add = null;
        }
        int i = 0;
        while (true) {
            if (i >= this.buffs.size()) {
                break;
            }
            Buff b = (Buff) this.buffs.elementAt(i);
            if (b.type == buff.type) {
                remove = b;
                break;
            }
            i++;
        }
        if (remove != null) {
            this.buffs.removeElement(remove);
        }
        if (add != null) {
            this.buffs.addElement(add);
        }
    }

    public void orderBank() {
        Vector itemBag2 = this.bank;
        synchronized (itemBag2) {
            GameItem[] items = new GameItem[itemBag2.size()];
            itemBag2.copyInto(items);
            for (int i = 0; i < items.length; i++) {
                for (int j = i + 1; j < items.length; j++) {
                    GameItem tmp = items[i];
                    boolean swap = false;
                    if (items[j].type < items[i].type) {
                        swap = true;
                    } else if (items[j].type == items[i].type) {
                        if (items[j].quanlity > items[i].quanlity) {
                            swap = true;
                        } else if (items[j].quanlity == items[i].quanlity) {
                            if (items[j].reqLevel < items[i].reqLevel) {
                                swap = true;
                            } else if (items[j].reqLevel == items[i].reqLevel && items[j].id < items[i].id) {
                                swap = true;
                            }
                        }
                    }
                    if (swap) {
                        items[i] = items[j];
                        items[j] = tmp;
                    }
                }
            }
            itemBag2.removeAllElements();
            for (GameItem addElement : items) {
                itemBag2.addElement(addElement);
            }
        }
    }

    public void orderBag() {
        synchronized (this.itemBag) {
            GameItem[] items = new GameItem[this.itemBag.size()];
            this.itemBag.copyInto(items);
            for (int i = 0; i < items.length; i++) {
                for (int j = i + 1; j < items.length; j++) {
                    GameItem tmp = items[i];
                    boolean swap = false;
                    if (items[j].type < items[i].type) {
                        swap = true;
                    } else if (items[j].type == items[i].type) {
                        if (items[j].quanlity > items[i].quanlity) {
                            swap = true;
                        } else if (items[j].quanlity == items[i].quanlity) {
                            if (items[j].reqLevel < items[i].reqLevel) {
                                swap = true;
                            } else if (items[j].reqLevel == items[i].reqLevel && items[j].id < items[i].id) {
                                swap = true;
                            }
                        }
                    }
                    if (swap) {
                        items[i] = items[j];
                        items[j] = tmp;
                    }
                }
            }
            this.itemBag.removeAllElements();
            for (GameItem addElement : items) {
                this.itemBag.addElement(addElement);
            }
        }
    }

    public void orderPets() {
        Pet[] op = new Pet[this.pets.size()];
        this.pets.copyInto(op);
        this.pets.removeAllElements();
        for (int i = 0; i < op.length; i++) {
            for (int j = i; j < op.length; j++) {
                if (op[j].battleOpened && !op[i].battleOpened) {
                    Pet tmp = op[j];
                    op[j] = op[i];
                    op[i] = tmp;
                } else if (op[j].followOpened && !op[i].followOpened && !op[i].battleOpened) {
                    Pet tmp2 = op[j];
                    op[j] = op[i];
                    op[i] = tmp2;
                }
            }
        }
        for (Pet addElement : op) {
            this.pets.addElement(addElement);
        }
    }

    public void orderTasks() {
        Task[] ts = new Task[this.tasks.size()];
        this.tasks.copyInto(ts);
        this.tasks.removeAllElements();
        for (int i = 0; i < ts.length; i++) {
            for (int j = i; j < ts.length; j++) {
                if (ts[j].level < ts[i].level) {
                    Task tmp = ts[j];
                    ts[j] = ts[i];
                    ts[i] = tmp;
                }
            }
        }
        for (Task addElement : ts) {
            this.tasks.addElement(addElement);
        }
    }

    public boolean updatePetSwitchState(int mainId, int secId, int followId) {
        boolean modified = false;
        if (mainId != -2) {
            this.battlePetMain.battleOpened = false;
            this.battlePetMain = getPet(mainId);
            if (this.battlePetMain != null) {
                this.battlePetMain.battleOpened = true;
            }
            if (this.battlePetMain == this.battlePetSecond) {
                this.battlePetSecond = null;
            }
            modified = true;
        }
        if (secId != -2) {
            if (this.battlePetSecond != null) {
                this.battlePetSecond.battleOpened = false;
            }
            if (secId == -1) {
                this.battlePetSecond = null;
            } else {
                this.battlePetSecond = getPet(secId);
                if (this.battlePetSecond != null) {
                    this.battlePetSecond.battleOpened = true;
                }
            }
            modified = true;
        }
        if (followId != -2) {
            if (getFollowPet() != null) {
                getFollowPet().followOpened = false;
            }
            if (followId == -1) {
                setFollowPet(null);
            } else {
                setFollowPet(getPet(followId));
                if (getFollowPet() != null) {
                    getFollowPet().followOpened = true;
                }
            }
            modified = true;
        }
        if (modified) {
            orderPets();
        }
        return modified;
    }

    public Pet getPet(int id, boolean includeField) {
        for (int i = 0; i < this.pets.size(); i++) {
            Pet pet = (Pet) this.pets.elementAt(i);
            if (pet.id == id) {
                return pet;
            }
        }
        if (includeField && NewStage.fieldPets != null) {
            for (int i2 = 0; i2 < NewStage.fieldPets.length; i2++) {
                if (NewStage.fieldPets[i2].id == id) {
                    return NewStage.fieldPets[i2];
                }
            }
        }
        return null;
    }

    public int[] getTaskIds() {
        int[] ret = new int[this.tasks.size()];
        for (int i = 0; i < this.tasks.size(); i++) {
            ret[i] = ((Task) this.tasks.elementAt(i)).id;
        }
        return ret;
    }

    public Task findTask(int id) {
        for (int i = 0; i < this.tasks.size(); i++) {
            Task task = (Task) this.tasks.elementAt(i);
            if (task.id == id) {
                return task;
            }
        }
        return null;
    }

    public void addTask(Task task) {
        if (findTask(task.id) != null) {
            removeTask(task.id);
        }
        this.tasks.addElement(task);
        orderTasks();
    }

    public void removeTask(int id) {
        boolean removed = false;
        int i = 0;
        while (true) {
            if (i >= this.tasks.size()) {
                break;
            }
            Task task = (Task) this.tasks.elementAt(i);
            if (task.id == id) {
                this.tasks.removeElement(task);
                removed = true;
                break;
            }
            i++;
        }
        if (removed) {
            orderTasks();
        }
    }

    public Title findTitle(int id) {
        for (int i = 0; i < this.titles.size(); i++) {
            Title title = (Title) this.titles.elementAt(i);
            if (title.id == id) {
                return title;
            }
        }
        return null;
    }

    public boolean removeTitle(int id) {
        Title find = findTitle(id);
        if (find != null) {
            this.titles.removeElement(find);
        }
        orderTitle();
        return find != null;
    }

    public boolean addTitle(Title title) {
        Title find = findTitle(title.id);
        if (find != null) {
            this.titles.removeElement(find);
        }
        this.titles.addElement(title);
        orderTitle();
        return find == null;
    }

    public void orderTitle() {
        Title[] cs = new Title[this.titles.size()];
        this.titles.copyInto(cs);
        for (int i = 0; i < cs.length; i++) {
            for (int j = i + 1; j < cs.length; j++) {
                if (cs[j].id < cs[i].id) {
                    Title t = cs[j];
                    cs[j] = cs[i];
                    cs[i] = t;
                }
            }
        }
        this.titles.removeAllElements();
        for (Title addElement : cs) {
            this.titles.addElement(addElement);
        }
    }

    public Credit findCriedit(int id) {
        for (int i = 0; i < this.credits.size(); i++) {
            Credit credit = (Credit) this.credits.elementAt(i);
            if (credit.id == id) {
                return credit;
            }
        }
        return null;
    }

    public void addCredit(Credit credit) {
        Credit find = findCriedit(credit.id);
        if (find != null) {
            this.credits.removeElement(find);
        }
        this.credits.addElement(credit);
        orderCredit();
    }

    public void orderCredit() {
        Credit[] cs = new Credit[this.credits.size()];
        this.credits.copyInto(cs);
        for (int i = 0; i < cs.length; i++) {
            for (int j = i + 1; j < cs.length; j++) {
                if (cs[j].id < cs[i].id) {
                    Credit t = cs[j];
                    cs[j] = cs[i];
                    cs[i] = t;
                }
            }
        }
        this.credits.removeAllElements();
        for (Credit addElement : cs) {
            this.credits.addElement(addElement);
        }
    }

    public Pet getPet(int id) {
        return getPet(id, true);
    }

    public int[] getPetIDs() {
        int[] ret = new int[this.pets.size()];
        for (int i = 0; i < this.pets.size(); i++) {
            ret[i] = ((Pet) this.pets.elementAt(i)).id;
        }
        return ret;
    }

    public Pet addPet(Pet pet) {
        if (getPet(pet.id, false) != null) {
            return null;
        }
        this.pets.addElement(pet);
        return pet;
    }

    public Pet removePet(int id) {
        Pet pet = getPet(id, false);
        if (pet != null) {
            this.pets.removeElement(pet);
        }
        return pet;
    }

    public void addItem(GameItem item) {
        if (item.type == 18) {
            this.coins.addElement(item);
            this.coinRefresh = true;
            return;
        }
        this.itemBag.addElement(item);
    }

    public void updateItemCount(int itemId, int count) {
        GameItem item = findItem(itemId);
        if (item != null) {
            item.count = (byte) count;
        }
    }

    public GameItem removeItem(int itemId) {
        GameItem item = findItem(itemId);
        if (item != null) {
            this.itemBag.removeElement(item);
        } else {
            item = getCoin(itemId);
            if (item != null) {
                this.coins.removeElement(item);
                this.coinRefresh = true;
            }
        }
        return item;
    }

    public GameItem[] findItems(int itemId) {
        Vector ret = new Vector();
        for (int i = 0; i < this.itemBag.size(); i++) {
            GameItem item = (GameItem) this.itemBag.elementAt(i);
            if (item.itemId == itemId) {
                ret.addElement(item);
            }
        }
        if (ret.size() == 0) {
            GameItem item2 = getCoinItem(itemId);
            if (item2 != null) {
                ret.addElement(item2);
            }
        }
        GameItem[] g = new GameItem[ret.size()];
        ret.copyInto(g);
        return g;
    }

    public int findAllItemCount(int itemId) {
        int ret = 0;
        for (int i = 0; i < this.itemBag.size(); i++) {
            GameItem item = (GameItem) this.itemBag.elementAt(i);
            if (item.itemId == itemId) {
                ret += item.count;
            }
        }
        return ret;
    }

    public int findAllItemCount(GameItem item) {
        return findAllItemCount(item.itemId);
    }

    public GameItem findItem(int makeId) {
        for (int i = 0; i < this.itemBag.size(); i++) {
            GameItem item = (GameItem) this.itemBag.elementAt(i);
            if (item.id == makeId) {
                return item;
            }
        }
        return null;
    }

    public void addItemToBank(GameItem item) {
        this.bank.addElement(item);
    }

    public void updateItemCountInbank(int itemId, int count) {
        GameItem item = findItemInBank(itemId);
        if (item != null) {
            item.count = (byte) count;
        }
    }

    public GameItem removeItemInBank(int itemId) {
        GameItem item = findItemInBank(itemId);
        if (item != null) {
            this.bank.removeElement(item);
        }
        return item;
    }

    public int[] getTaskLevelClr(int taskLevel) {
        int idx;
        int levelDiff = this.level - taskLevel;
        if (levelDiff <= -6) {
            idx = 0;
        } else if (levelDiff <= -3) {
            idx = 1;
        } else if (levelDiff <= 2) {
            idx = 2;
        } else if (levelDiff <= 5) {
            idx = 3;
        } else {
            idx = 4;
        }
        return EMEMY_NAME_CLR[idx];
    }

    public GameItem findItemInBank(int itemId) {
        for (int i = 0; i < this.bank.size(); i++) {
            GameItem item = (GameItem) this.bank.elementAt(i);
            if (item.id == itemId) {
                return item;
            }
        }
        return null;
    }

    public CartoonPlayer createCartoonPlayer() {
        Tool.female = new PipAnimateSet(World.findResource("/female.ani", false));
        Tool.male = new PipAnimateSet(World.findResource("/male.ani", false));
        Tool.female.toFullBuffer();
        Tool.male.toFullBuffer();
        this.cartoonPlayer = CartoonPlayer.playCartoon(this.sex == 0 ? Tool.female : Tool.male, getFaceto(), this.pixelX, this.pixelY, true);
        return this.cartoonPlayer;
    }

    public void resetCartoonPlayer() {
        this.cartoonPlayer = CartoonPlayer.playCartoon(this.sex == 0 ? Tool.female : Tool.male, getFaceto(), this.pixelX, this.pixelY, true);
    }

    public void clearAutoRun() {
    }

    public void handleKey() {
        byte newDir = getDir();
        boolean moving = false;
        if ((CommonData.team.id == -1 || this.teamState != 1) && getState() != 4) {
            if (Utilities.isKeyPressed(0, false) || Utilities.isKeyPressed(13, false)) {
                newDir = 2;
                moving = true;
            } else if (Utilities.isKeyPressed(1, false) || Utilities.isKeyPressed(19, false)) {
                newDir = 3;
                moving = true;
            } else if (Utilities.isKeyPressed(2, false) || Utilities.isKeyPressed(15, false)) {
                newDir = 0;
                moving = true;
            } else if (Utilities.isKeyPressed(3, false) || Utilities.isKeyPressed(17, false)) {
                newDir = 1;
                moving = true;
            } else if (Utilities.isKeyPressed(4, false) || Utilities.isKeyPressed(16, false)) {
                if (this.targetPlayer != null && Battle.battleInstance == null && (this.targetPlayer instanceof NetPlayer)) {
                    if (this.targetPlayer.camp != this.camp) {
                        Utilities.sendRequest(20, 10, this.targetPlayer.id, (String) null);
                        Utilities.clearKeyStates(4);
                        Utilities.clearKeyStates(16);
                    } else {
                        CommonComponent.loadUI(Utilities.VMUI_PLAYERMENU);
                        Utilities.clearKeyStates(4);
                        Utilities.clearKeyStates(16);
                    }
                }
            } else if (World.pointerx == -1 || World.pointery == -1) {
                this.pressTime = -1;
            }
        }
        playerMove(newDir, moving);
    }

    public void handleAutoGo() {
        if (World.pressedx != -1 && World.pressedy != -1) {
            this.destX = World.pressedx + NewStage.viewX;
            this.destY = World.pressedy + NewStage.viewY;
            this.destTime = 12;
            boolean canGo = true;
            if (NewStage.search == null) {
                canGo = false;
            }
            if (canGo && (World.pressedx < (-NewStage.viewX) || World.pressedx > (-NewStage.viewX) + (NewStage.mapWidth * 16))) {
                canGo = false;
                World.pressedx = -1;
                World.pressedy = -1;
            }
            if (canGo) {
                NewStage.search.reset();
                canGo = NewStage.search.find(this.pixelX + (getWidth() / 2), (this.pixelY - 8) - NewStage.screenY, World.pressedx + NewStage.viewX, World.pressedy + NewStage.viewY);
            }
            if (canGo) {
                this.canReachIcon = true;
                this.wpType = 2;
                if (NewStage.search.points != null && NewStage.search.points.size() > 0) {
                    short[][] path = NewStage.search.getPointPos();
                    if (this.wpList != null && this.wpList.size() > 0) {
                        this.wpList = null;
                    }
                    for (int i = 0; i < path.length; i++) {
                        addWayPoint(path[i][0], path[i][1]);
                    }
                    return;
                }
                return;
            }
            this.canReachIcon = false;
        }
    }

    public void playerMove(byte newDir, boolean moving) {
        this.pressingKey = moving;
        if ((CommonData.team.id == -1 || this.teamState != 1) && getState() != 4) {
            if (this.keepMoving != -1) {
                if (newDir != getDir()) {
                    this.keepMoving = -1;
                    this.pressTime = -1;
                }
                setState(1);
            } else if (moving) {
                setState(1);
                if (newDir != getDir()) {
                    this.pressTime = -1;
                    this.keepMoving = -1;
                } else if (this.pressTime == -1) {
                    this.pressTime = System.currentTimeMillis();
                } else if (!(System.currentTimeMillis() - this.pressTime <= 2000 || NewStage.currentMapId == 560 || NewStage.currentMapId == 563)) {
                    this.keepMoving = newDir;
                    if (getDir() == 0 || getDir() == 1) {
                        this.startSearch = true;
                        this.prevdir = -1;
                    }
                }
            } else if (this.wpList == null || getState() != 2) {
                setState(0);
            }
        }
        if (newDir != getDir()) {
            setDir(newDir);
            this.cartoonPlayer.setFrameIndex(0);
            this.cartoonPlayer.setAnimateIndex(getFaceto());
        }
    }

    /* JADX WARNING: type inference failed for: r7v0 */
    /* JADX WARNING: type inference failed for: r7v1 */
    /* JADX WARNING: type inference failed for: r7v2, types: [com.sumsharp.monster.common.data.AbstractUnit] */
    /* JADX WARNING: type inference failed for: r5v1, types: [com.sumsharp.monster.common.data.NetPlayer] */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r7v4 */
    /* JADX WARNING: type inference failed for: r7v5 */
    /* JADX WARNING: type inference failed for: r7v6 */
    /* JADX WARNING: type inference failed for: r9v23, types: [com.sumsharp.monster.common.data.Pet[]] */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r7v8 */
    /* JADX WARNING: type inference failed for: r7v9 */
    /* JADX WARNING: type inference failed for: r7v10 */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r7v1
      assigns: []
      uses: []
      mth insns count: 79
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:104)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:97)
     */
    /* JADX WARNING: Unknown variable types count: 5 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cycle() {
        /*
            r11 = this;
            r3 = 0
        L_0x0001:
            java.util.Vector r9 = r11.buffs
            int r9 = r9.size()
            if (r3 < r9) goto L_0x0035
            r3 = 0
        L_0x000a:
            java.util.Vector r9 = r11.tasks
            int r9 = r9.size()
            if (r3 < r9) goto L_0x0043
            r4 = 1600(0x640, float:2.242E-42)
            r7 = 0
            com.sumsharp.monster.common.data.Pet[] r9 = com.sumsharp.monster.NewStage.fieldPets
            if (r9 == 0) goto L_0x001f
            r3 = 0
        L_0x001a:
            com.sumsharp.monster.common.data.Pet[] r9 = com.sumsharp.monster.NewStage.fieldPets
            int r9 = r9.length
            if (r3 < r9) goto L_0x0051
        L_0x001f:
            r3 = 0
        L_0x0020:
            java.util.Vector r9 = com.sumsharp.monster.NewStage.netPlayers
            int r9 = r9.size()
            if (r3 < r9) goto L_0x0075
            r11.targetPlayer = r7
            int r9 = r11.destTime
            if (r9 <= 0) goto L_0x0034
            int r9 = r11.destTime
            r10 = 1
            int r9 = r9 - r10
            r11.destTime = r9
        L_0x0034:
            return
        L_0x0035:
            java.util.Vector r9 = r11.buffs
            java.lang.Object r0 = r9.elementAt(r3)
            com.sumsharp.monster.common.data.Buff r0 = (com.sumsharp.monster.common.data.Buff) r0
            r0.decTime()
            int r3 = r3 + 1
            goto L_0x0001
        L_0x0043:
            java.util.Vector r9 = r11.tasks
            java.lang.Object r8 = r9.elementAt(r3)
            com.sumsharp.monster.common.data.Task r8 = (com.sumsharp.monster.common.data.Task) r8
            r8.checkTime()
            int r3 = r3 + 1
            goto L_0x000a
        L_0x0051:
            com.sumsharp.monster.common.data.Pet[] r9 = com.sumsharp.monster.NewStage.fieldPets
            r9 = r9[r3]
            short r9 = r9.pixelX
            short r10 = r11.pixelX
            int r1 = r9 - r10
            com.sumsharp.monster.common.data.Pet[] r9 = com.sumsharp.monster.NewStage.fieldPets
            r9 = r9[r3]
            short r9 = r9.pixelY
            short r10 = r11.pixelY
            int r2 = r9 - r10
            int r9 = r1 * r1
            int r10 = r2 * r2
            int r6 = r9 + r10
            if (r6 >= r4) goto L_0x0072
            r4 = r6
            com.sumsharp.monster.common.data.Pet[] r9 = com.sumsharp.monster.NewStage.fieldPets
            r7 = r9[r3]
        L_0x0072:
            int r3 = r3 + 1
            goto L_0x001a
        L_0x0075:
            java.util.Vector r9 = com.sumsharp.monster.NewStage.netPlayers
            java.lang.Object r5 = r9.elementAt(r3)
            com.sumsharp.monster.common.data.NetPlayer r5 = (com.sumsharp.monster.common.data.NetPlayer) r5
            short r9 = r5.pixelX
            short r10 = r11.pixelX
            int r1 = r9 - r10
            short r9 = r5.pixelY
            short r10 = r11.pixelY
            int r2 = r9 - r10
            int r9 = r1 * r1
            int r10 = r2 * r2
            int r6 = r9 + r10
            if (r6 >= r4) goto L_0x0093
            r4 = r6
            r7 = r5
        L_0x0093:
            int r3 = r3 + 1
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.common.data.Player.cycle():void");
    }

    /* access modifiers changed from: protected */
    public void cycleMoving() {
        if (CommonData.team.id == -1 || this.teamState != 1) {
            handleMove();
        }
    }

    private int[] getSpeed() {
        int x1 = 0;
        int y1 = 0;
        switch (getDir()) {
            case 0:
                x1 = -this.speed;
                y1 = 0;
                break;
            case 1:
                x1 = this.speed;
                y1 = 0;
                break;
            case 2:
                x1 = 0;
                y1 = -this.speed;
                break;
            case 3:
                x1 = 0;
                y1 = this.speed;
                break;
        }
        return new int[]{x1, y1};
    }

    public void draw(Graphics g) {
        drawDest(g);
        super.draw(g);
        int fid = 29 + ((World.tick % 6) / 3);
        int x = CommonData.player.getPixelX();
        int y = CommonData.player.getPixelY() - 15;
        boolean needTrans = false;
        if (this.keepMoving != -1) {
            if (getFaceto() == 0) {
                x += CommonData.player.getWidth();
                needTrans = true;
            }
            if (needTrans) {
                Tool.uiMiscImg2.drawFrame(g, fid, x, y, 2, 0);
            } else {
                Tool.uiMiscImg2.drawFrame(g, fid, x, y, 0, 8);
            }
        }
    }

    private void drawDest(Graphics g) {
        if (this.destX != -1 && this.destY != -1 && this.destTime > 0) {
            if (this.canReachIcon) {
                Graphics graphics = g;
                Tool.arrow.drawFrame(graphics, ((this.destTime / 3) % 2) + 0, this.destX - NewStage.viewX, (this.destY - NewStage.viewY) - 10, 0, 3);
                return;
            }
            Tool.arrow.drawFrame(g, 2, this.destX - NewStage.viewX, this.destY - NewStage.viewY, 0, 3);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:55:0x005d, code lost:
        continue;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleMove() {
        /*
            r12 = this;
            r6 = 0
            r8 = 0
            int[] r5 = r12.getSpeed()
            r10 = 0
            r6 = r5[r10]
            r10 = 1
            r8 = r5[r10]
            r3 = 0
            r2 = 0
            r4 = 0
            r1 = 0
            r0 = 1
        L_0x0011:
            short r10 = r12.pixelX
            int r7 = r6 + r10
            short r10 = r12.pixelY
            int r10 = r10 + r8
            r11 = 10
            int r9 = r10 - r11
            byte r10 = r12.getDir()
            r11 = 1
            if (r10 != r11) goto L_0x0068
            if (r7 >= 0) goto L_0x0068
            r3 = 1
            if (r2 == 0) goto L_0x0063
            int[] r5 = r12.getSpeed()
            r10 = 0
            r6 = r5[r10]
            r10 = 1
            r8 = r5[r10]
            short r10 = r12.pixelX
            int r7 = r6 + r10
            short r10 = r12.pixelY
            int r10 = r10 + r8
            r11 = 10
            int r9 = r10 - r11
            r10 = 3
            r12.setDir(r10)
        L_0x0041:
            boolean r0 = r12.canPass(r7, r9)
            if (r0 != 0) goto L_0x00f7
            byte r10 = r12.keepMoving
            r11 = -1
            if (r10 == r11) goto L_0x0054
            boolean r10 = r12.startSearch
            if (r10 == 0) goto L_0x0054
            boolean r0 = r12.changeDir(r7, r9)
        L_0x0054:
            if (r0 != 0) goto L_0x005d
            byte r10 = r12.getDir()
            switch(r10) {
                case 0: goto L_0x010e;
                case 1: goto L_0x0112;
                case 2: goto L_0x0106;
                case 3: goto L_0x010a;
                default: goto L_0x005d;
            }
        L_0x005d:
            if (r0 == 0) goto L_0x0011
            r12.go(r6, r8)
            return
        L_0x0063:
            r10 = 0
            r12.setDir(r10)
            goto L_0x0041
        L_0x0068:
            byte r10 = r12.getDir()
            if (r10 != 0) goto L_0x0096
            int r10 = com.sumsharp.monster.NewStage.mapWidth
            int r10 = r10 * 16
            if (r7 <= r10) goto L_0x0096
            r2 = 1
            if (r3 == 0) goto L_0x0091
            int[] r5 = r12.getSpeed()
            r10 = 0
            r6 = r5[r10]
            r10 = 1
            r8 = r5[r10]
            short r10 = r12.pixelX
            int r7 = r6 + r10
            short r10 = r12.pixelY
            int r10 = r10 + r8
            r11 = 10
            int r9 = r10 - r11
            r10 = 3
            r12.setDir(r10)
            goto L_0x0041
        L_0x0091:
            r10 = 1
            r12.setDir(r10)
            goto L_0x0041
        L_0x0096:
            byte r10 = r12.getDir()
            r11 = 3
            if (r10 != r11) goto L_0x00c1
            if (r9 >= 0) goto L_0x00c1
            r1 = 1
            if (r4 == 0) goto L_0x00bc
            r10 = 1
            r12.setDir(r10)
            int[] r5 = r12.getSpeed()
            r10 = 0
            r6 = r5[r10]
            r10 = 1
            r8 = r5[r10]
            short r10 = r12.pixelX
            int r7 = r6 + r10
            short r10 = r12.pixelY
            int r10 = r10 + r8
            r11 = 10
            int r9 = r10 - r11
            goto L_0x0041
        L_0x00bc:
            r10 = 2
            r12.setDir(r10)
            goto L_0x0041
        L_0x00c1:
            byte r10 = r12.getDir()
            r11 = 2
            if (r10 != r11) goto L_0x0041
            int r10 = com.sumsharp.monster.NewStage.mapHeight
            int r10 = r10 * 16
            int r11 = com.sumsharp.monster.NewStage.screenY
            int r10 = r10 + r11
            int r10 = r10 + 10
            if (r9 <= r10) goto L_0x0041
            r4 = 1
            if (r1 == 0) goto L_0x00f1
            r10 = 1
            r12.setDir(r10)
            int[] r5 = r12.getSpeed()
            r10 = 0
            r6 = r5[r10]
            r10 = 1
            r8 = r5[r10]
            short r10 = r12.pixelX
            int r7 = r6 + r10
            short r10 = r12.pixelY
            int r10 = r10 + r8
            r11 = 10
            int r9 = r10 - r11
            goto L_0x0041
        L_0x00f1:
            r10 = 3
            r12.setDir(r10)
            goto L_0x0041
        L_0x00f7:
            byte r10 = r12.keepMoving
            r11 = -1
            if (r10 == r11) goto L_0x0054
            boolean r10 = r12.startSearch
            if (r10 == 0) goto L_0x0054
            boolean r0 = r12.changeDir(r7, r9)
            goto L_0x0054
        L_0x0106:
            int r8 = r8 + 1
            goto L_0x005d
        L_0x010a:
            int r8 = r8 + -1
            goto L_0x005d
        L_0x010e:
            int r6 = r6 + 1
            goto L_0x005d
        L_0x0112:
            int r6 = r6 + -1
            goto L_0x005d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.common.data.Player.handleMove():void");
    }

    public byte getVerticalDir(int xx, int yy) {
        int step = 0;
        do {
            if (getDir() == 0) {
                if (canPass(xx - 5, yy - (step * 6)) && canPass(xx, (yy - (step * 6)) - 10)) {
                    return 2;
                }
                if (canPass(xx - 5, (step * 6) + yy) && canPass(xx, (step * 6) + yy + 10)) {
                    return 3;
                }
            } else if (getDir() == 1) {
                if (canPass(xx + 5, yy - (step * 6)) && canPass(xx, (yy - (step * 6)) - 10)) {
                    return 2;
                }
                if (canPass(xx + 5, (step * 6) + yy) && canPass(xx, (step * 6) + yy + 10)) {
                    return 3;
                }
            }
            step++;
        } while (step < 30);
        return -1;
    }

    private boolean changeDir(int xx, int yy) {
        if (getDir() == 0) {
            if (!canPass(xx, yy)) {
                if (this.pressingKey) {
                    return false;
                }
                if (this.prevdir == -1) {
                    this.prevdir = 0;
                }
                byte dir = getVerticalDir(xx, yy);
                if (dir == -1) {
                    return false;
                }
                setDir(dir);
            }
            return true;
        } else if (getDir() == 1) {
            if (!canPass(xx, yy)) {
                if (this.pressingKey) {
                    return false;
                }
                if (this.prevdir == -1) {
                    this.prevdir = 1;
                }
                byte dir2 = getVerticalDir(xx, yy);
                if (dir2 == -1) {
                    return false;
                }
                setDir(dir2);
            }
            return true;
        } else if (getDir() == 2) {
            if (this.pressingKey) {
                return false;
            }
            if (this.prevdir == 0) {
                if (canPass(xx, yy + 10)) {
                    setDir(this.prevdir);
                    this.startSearch = true;
                }
            } else if (this.prevdir != 1) {
                return false;
            } else {
                if (canPass(xx, yy + 10)) {
                    setDir(this.prevdir);
                    this.startSearch = true;
                }
            }
            return true;
        } else if (getDir() != 3) {
            return false;
        } else {
            if (this.pressingKey) {
                return false;
            }
            if (this.prevdir == 0) {
                if (canPass(xx, yy - 10)) {
                    setDir(this.prevdir);
                    this.startSearch = true;
                }
            } else if (this.prevdir != 1) {
                return false;
            } else {
                if (canPass(xx, yy - 10)) {
                    setDir(this.prevdir);
                    this.startSearch = true;
                }
            }
            return true;
        }
    }

    public boolean canPass(int xx, int yy) {
        int i;
        int i2;
        int w = getWidth();
        Vector tiles = new Vector();
        switch (getDir()) {
            case 0:
            case 1:
                for (int tmpy = yy; tmpy < yy + 10; tmpy++) {
                    if (getDir() == 1) {
                        i = w;
                    } else {
                        i = 0;
                    }
                    Integer tile = new Integer((NewStage.mapWidth * (tmpy / 16)) + ((i + xx) / 16));
                    if (!tiles.contains(tile)) {
                        tiles.addElement(tile);
                    }
                }
                break;
            case 2:
            case 3:
                for (int tmpx = xx; tmpx < xx + w; tmpx++) {
                    int tilex = tmpx / 16;
                    if (getDir() == 3) {
                        i2 = 10;
                    } else {
                        i2 = 0;
                    }
                    Integer tile2 = new Integer((NewStage.mapWidth * ((i2 + yy) / 16)) + tilex);
                    if (!tiles.contains(tile2)) {
                        tiles.addElement(tile2);
                    }
                }
                break;
        }
        for (int i3 = 0; i3 < tiles.size(); i3++) {
            if (!NewStage.canPass(((Integer) tiles.elementAt(i3)).intValue())) {
                return false;
            }
        }
        if (xx < 0 || getWidth() + xx > NewStage.mapWidth * 16) {
            return false;
        }
        return true;
    }

    public void addFriend(int id, String name, byte sex2, byte state) {
        Friend friend = new Friend();
        friend.id = id;
        friend.name = name;
        friend.sex = sex2;
        friend.state = state;
        this.friends.addElement(friend);
        orderFriends();
    }

    public void delFriend(int id) {
        for (int i = 0; i < this.friends.size(); i++) {
            if (((Friend) this.friends.elementAt(i)).id == id) {
                this.friends.removeElementAt(i);
                orderFriends();
                return;
            }
        }
    }

    public Friend getFriend(int id) {
        for (int i = 0; i < this.friends.size(); i++) {
            Friend f = (Friend) this.friends.elementAt(i);
            if (f.id == id) {
                return f;
            }
        }
        return null;
    }

    public int[] getFriendIDs() {
        int[] ret = new int[this.friends.size()];
        for (int i = 0; i < this.friends.size(); i++) {
            ret[i] = ((Friend) this.friends.elementAt(i)).id;
        }
        return ret;
    }

    public void orderFriends() {
        synchronized (this.friends) {
            Friend[] fs = new Friend[this.friends.size()];
            this.friends.copyInto(fs);
            for (int i = 0; i < fs.length; i++) {
                for (int j = i; j < fs.length; j++) {
                    if (fs[j].compareTo(fs[i]) < 0) {
                        Friend t = fs[j];
                        fs[j] = fs[i];
                        fs[i] = t;
                    }
                }
            }
            this.friends.removeAllElements();
            for (Friend addElement : fs) {
                this.friends.addElement(addElement);
            }
        }
    }

    public void orderMateMap() {
        synchronized (this.mateMap) {
            MateInfo[] infos = new MateInfo[this.mateMap.size()];
            this.mateMap.copyInto(infos);
            for (int i = 0; i < infos.length; i++) {
                for (int j = i; j < infos.length; j++) {
                    if (infos[j].id > infos[i].id) {
                        MateInfo t = infos[j];
                        infos[j] = infos[i];
                        infos[i] = t;
                    }
                }
            }
            this.mateMap.removeAllElements();
            for (MateInfo addElement : infos) {
                this.mateMap.addElement(addElement);
            }
        }
    }

    public boolean isBagFull() {
        return this.bagSize < this.itemBag.size();
    }

    public GameItem getCoin(int coinId) {
        for (int i = 0; i < this.coins.size(); i++) {
            GameItem item = (GameItem) this.coins.elementAt(i);
            if (item.id == coinId) {
                return item;
            }
        }
        return null;
    }

    public GameItem getCoinItem(int itemid) {
        for (int i = 0; i < this.coins.size(); i++) {
            GameItem item = (GameItem) this.coins.elementAt(i);
            if (item.itemId == itemid) {
                return item;
            }
        }
        return null;
    }
}
