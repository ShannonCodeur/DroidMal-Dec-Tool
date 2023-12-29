package com.sumsharp.monster.common;

import com.sumsharp.lowui.ChallengeArena;
import com.sumsharp.monster.Battle;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.data.Buff;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.common.data.Friend;
import com.sumsharp.monster.common.data.NetPlayer;
import com.sumsharp.monster.common.data.Npc;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.common.data.Skill;
import com.sumsharp.monster.gtvm.GTVM;
import com.sumsharp.monster.image.ImageSet;
import com.sumsharp.monster.item.GameItem;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Hashtable;

public class CommonProcessor {
    public static final byte ATTACHMENT_GOLD = 2;
    public static final byte ATTACHMENT_NULL = 0;
    public static final byte ATTACHMENT_PROPS = 1;
    public static Hashtable itemTable = new Hashtable();

    /* JADX WARNING: type inference failed for: r1v9, types: [int] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r0v33, types: [int] */
    /* JADX WARNING: type inference failed for: r1v27 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void handleActionMsg(com.sumsharp.monster.net.UWAPSegment r10) {
        /*
            byte r0 = r10.subType
            switch(r0) {
                case 4: goto L_0x01c0;
                case 5: goto L_0x0279;
                case 6: goto L_0x0190;
                case 7: goto L_0x0005;
                case 8: goto L_0x0005;
                case 9: goto L_0x0005;
                case 10: goto L_0x0005;
                case 11: goto L_0x0005;
                case 12: goto L_0x0005;
                case 13: goto L_0x0005;
                case 14: goto L_0x0005;
                case 15: goto L_0x0116;
                case 16: goto L_0x0005;
                case 17: goto L_0x0141;
                case 18: goto L_0x015a;
                case 19: goto L_0x0013;
                case 20: goto L_0x0005;
                case 21: goto L_0x0005;
                case 22: goto L_0x0005;
                case 23: goto L_0x0005;
                case 24: goto L_0x0005;
                case 25: goto L_0x0005;
                case 26: goto L_0x00b3;
                case 27: goto L_0x006f;
                case 28: goto L_0x007d;
                case 29: goto L_0x00aa;
                case 30: goto L_0x008c;
                case 31: goto L_0x0068;
                case 32: goto L_0x0005;
                case 33: goto L_0x0213;
                case 34: goto L_0x0005;
                case 35: goto L_0x0005;
                case 36: goto L_0x0005;
                case 37: goto L_0x0005;
                case 38: goto L_0x0005;
                case 39: goto L_0x0005;
                case 40: goto L_0x0005;
                case 41: goto L_0x0029;
                case 42: goto L_0x0006;
                default: goto L_0x0005;
            }
        L_0x0005:
            return
        L_0x0006:
            java.lang.String r0 = r10.readString()
            com.sumsharp.monster.NewStage.mapPrefix = r0
            int r10 = r10.readInt()
            com.sumsharp.monster.NewStage.mapNameColor = r10
            goto L_0x0005
        L_0x0013:
            java.lang.String[] r0 = com.sumsharp.monster.GetItem.getItem(r10)
            r10 = 0
        L_0x0018:
            int r1 = r0.length
            if (r10 >= r1) goto L_0x0005
            r1 = -1
            java.lang.String r2 = ""
            r3 = -7
            java.lang.String r4 = ""
            r5 = r0[r10]
            com.sumsharp.monster.common.data.Chat.recvChatMsg(r1, r2, r3, r4, r5)
            int r10 = r10 + 1
            goto L_0x0018
        L_0x0029:
            int r0 = r10.readInt()
            com.sumsharp.monster.common.data.Player r1 = com.sumsharp.monster.common.CommonData.player
            com.sumsharp.monster.item.GameItem r1 = r1.findItem(r0)
            if (r1 == 0) goto L_0x0047
            java.io.DataInputStream r0 = new java.io.DataInputStream
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream
            byte[] r10 = r10.readBytes()
            r2.<init>(r10)
            r0.<init>(r2)
            r1.load(r0)
            goto L_0x0005
        L_0x0047:
            java.util.Hashtable r1 = itemTable
            java.lang.Integer r2 = new java.lang.Integer
            r2.<init>(r0)
            java.lang.Object r0 = r1.get(r2)
            com.sumsharp.monster.item.GameItem r0 = (com.sumsharp.monster.item.GameItem) r0
            if (r0 == 0) goto L_0x0005
            java.io.DataInputStream r1 = new java.io.DataInputStream
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream
            byte[] r10 = r10.readBytes()
            r2.<init>(r10)
            r1.<init>(r2)
            r0.load(r1)
            goto L_0x0005
        L_0x0068:
            r10 = 0
            com.sumsharp.monster.common.data.Npc.touchingNpc = r10
            r10 = 0
            com.sumsharp.monster.common.data.Npc.showConnection = r10
            goto L_0x0005
        L_0x006f:
            int r10 = r10.readInt()
            com.sumsharp.monster.common.data.Npc r10 = com.sumsharp.monster.NewStage.getNpc(r10)
            if (r10 == 0) goto L_0x0005
            r0 = 0
            r10.visible = r0
            goto L_0x0005
        L_0x007d:
            int r10 = r10.readInt()
            com.sumsharp.monster.common.data.Npc r10 = com.sumsharp.monster.NewStage.getNpc(r10)
            if (r10 == 0) goto L_0x0005
            r0 = 1
            r10.visible = r0
            goto L_0x0005
        L_0x008c:
            byte[] r0 = r10.readBytes()
            com.sumsharp.monster.common.data.Npc r10 = new com.sumsharp.monster.common.data.Npc
            r10.<init>()
            java.io.DataInputStream r1 = new java.io.DataInputStream     // Catch:{ IOException -> 0x00a7 }
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x00a7 }
            r2.<init>(r0)     // Catch:{ IOException -> 0x00a7 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x00a7 }
            r10.load(r1)     // Catch:{ IOException -> 0x00a7 }
            com.sumsharp.monster.NewStage.addNpc(r10)     // Catch:{ IOException -> 0x00a7 }
            goto L_0x0005
        L_0x00a7:
            r10 = move-exception
            goto L_0x0005
        L_0x00aa:
            int r10 = r10.readInt()
            com.sumsharp.monster.NewStage.removeNpc(r10)
            goto L_0x0005
        L_0x00b3:
            byte r0 = r10.readByte()
            r2 = r0 & 255(0xff, float:3.57E-43)
            com.sumsharp.monster.common.data.Npc[] r0 = new com.sumsharp.monster.common.data.Npc[r2]
            com.sumsharp.monster.NewStage.npcs = r0
            r0 = 0
            r1 = r0
        L_0x00bf:
            if (r1 < r2) goto L_0x00ef
            byte r0 = r10.readByte()
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.sumsharp.monster.common.data.Door[] r1 = new com.sumsharp.monster.common.data.Door[r0]
            com.sumsharp.monster.NewStage.doors = r1
            r1 = 0
        L_0x00cc:
            if (r1 >= r0) goto L_0x0005
            com.sumsharp.monster.common.data.Door[] r2 = com.sumsharp.monster.NewStage.doors
            com.sumsharp.monster.common.data.Door r3 = new com.sumsharp.monster.common.data.Door
            r3.<init>()
            r2[r1] = r3
            com.sumsharp.monster.common.data.Door[] r2 = com.sumsharp.monster.NewStage.doors
            r2 = r2[r1]
            java.io.DataInputStream r3 = new java.io.DataInputStream
            java.io.ByteArrayInputStream r4 = new java.io.ByteArrayInputStream
            byte[] r5 = r10.readBytes()
            r4.<init>(r5)
            r3.<init>(r4)
            r2.load(r3)
            int r1 = r1 + 1
            goto L_0x00cc
        L_0x00ef:
            com.sumsharp.monster.common.data.Npc[] r0 = com.sumsharp.monster.NewStage.npcs
            com.sumsharp.monster.common.data.Npc r3 = new com.sumsharp.monster.common.data.Npc
            r3.<init>()
            r0[r1] = r3
            com.sumsharp.monster.common.data.Npc[] r0 = com.sumsharp.monster.NewStage.npcs     // Catch:{ IOException -> 0x0111 }
            r0 = r0[r1]     // Catch:{ IOException -> 0x0111 }
            java.io.DataInputStream r3 = new java.io.DataInputStream     // Catch:{ IOException -> 0x0111 }
            java.io.ByteArrayInputStream r4 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0111 }
            byte[] r5 = r10.readBytes()     // Catch:{ IOException -> 0x0111 }
            r4.<init>(r5)     // Catch:{ IOException -> 0x0111 }
            r3.<init>(r4)     // Catch:{ IOException -> 0x0111 }
            r0.load(r3)     // Catch:{ IOException -> 0x0111 }
        L_0x010d:
            int r0 = r1 + 1
            r1 = r0
            goto L_0x00bf
        L_0x0111:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x010d
        L_0x0116:
            com.sumsharp.monster.common.CommonComponent.closeMessage()
            java.lang.String r0 = r10.readString()
            java.lang.String r1 = "close"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x012a
            com.sumsharp.monster.common.CommonComponent.closeAllUI()
            goto L_0x0005
        L_0x012a:
            byte[] r10 = r10.readBytes()
            int r1 = r10.length     // Catch:{ Exception -> 0x0136 }
            if (r1 != 0) goto L_0x013c
            com.sumsharp.monster.common.CommonComponent.loadUI(r0)     // Catch:{ Exception -> 0x0136 }
            goto L_0x0005
        L_0x0136:
            r10 = move-exception
            r10.printStackTrace()
            goto L_0x0005
        L_0x013c:
            com.sumsharp.monster.common.CommonComponent.loadUI(r0, r10)     // Catch:{ Exception -> 0x0136 }
            goto L_0x0005
        L_0x0141:
            short r1 = r10.readShort()
            short r2 = r10.readShort()
            short r3 = r10.readShort()
            byte r0 = r10.readByte()
            int r10 = r10.readInt()
            com.sumsharp.monster.NewStage.gotoMap(r1, r2, r3, r10, r0)
            goto L_0x0005
        L_0x015a:
            com.sumsharp.monster.common.CommonComponent.closeMessage()
            java.lang.String r0 = r10.readString()
            java.lang.String r2 = r10.readString()
            byte r6 = r10.readByte()
            int r10 = r10.readInt()
            java.lang.String r1 = ""
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0177
            java.lang.String r0 = "系统消息"
        L_0x0177:
            r1 = -1
            r3 = 1
            r4 = 1
            if (r10 <= 0) goto L_0x018e
            r5 = 1
        L_0x017d:
            com.sumsharp.monster.common.CommonComponent.showMessage(r0, r1, r2, r3, r4, r5)
            com.sumsharp.monster.ui.MessageDialogue r0 = com.sumsharp.monster.common.CommonComponent.message
            r0.closeMode = r6
            if (r10 <= 0) goto L_0x0005
            com.sumsharp.monster.ui.MessageDialogue r0 = com.sumsharp.monster.common.CommonComponent.message
            long r1 = (long) r10
            r0.setTimeout(r1)
            goto L_0x0005
        L_0x018e:
            r5 = 0
            goto L_0x017d
        L_0x0190:
            boolean r0 = r10.readBoolean()
            int[] r1 = r10.readInts()
            byte[] r2 = r10.readBytes()
            if (r0 == 0) goto L_0x01a1
            com.sumsharp.monster.NewStage.clearNpcTaskState()
        L_0x01a1:
            r10 = 0
        L_0x01a2:
            int r0 = r1.length
            if (r10 >= r0) goto L_0x0005
            r0 = r1[r10]
            com.sumsharp.monster.common.data.Npc r0 = com.sumsharp.monster.NewStage.getNpc(r0)
            if (r0 == 0) goto L_0x01bd
            java.lang.String r3 = "EMOTEID_TASK"
            byte[] r4 = com.sumsharp.monster.common.data.AbstractUnit.TASK_HIT_FRAME
            byte r5 = r2[r10]
            byte r4 = r4[r5]
            r5 = 1
            r0.createEmote(r3, r4, r5)
            byte r3 = r2[r10]
            r0.taskState = r3
        L_0x01bd:
            int r10 = r10 + 1
            goto L_0x01a2
        L_0x01c0:
            int r3 = r10.readInt()
            byte[] r0 = r10.readBytes()
            java.io.ByteArrayInputStream r10 = new java.io.ByteArrayInputStream
            r10.<init>(r0)
            java.io.DataInputStream r0 = new java.io.DataInputStream
            r0.<init>(r10)
            r2 = 0
            byte r10 = r0.readByte()     // Catch:{ IOException -> 0x02c5 }
            r1 = 0
            r9 = r1
            r1 = r2
            r2 = r9
        L_0x01db:
            if (r2 < r10) goto L_0x01e9
            com.sumsharp.monster.image.ImageLoadManager.updateImage(r3, r1)     // Catch:{ IOException -> 0x01e2 }
            goto L_0x0005
        L_0x01e2:
            r10 = move-exception
            r0 = r1
        L_0x01e4:
            r10.printStackTrace()
            goto L_0x0005
        L_0x01e9:
            java.lang.String r5 = r0.readUTF()     // Catch:{ IOException -> 0x01e2 }
            if (r1 != 0) goto L_0x02d0
            r1 = r5
            r4 = r1
        L_0x01f1:
            int r1 = r0.readInt()     // Catch:{ IOException -> 0x02c9 }
            byte[] r1 = new byte[r1]     // Catch:{ IOException -> 0x02c9 }
            r0.read(r1)     // Catch:{ IOException -> 0x02c9 }
            java.util.Hashtable r6 = com.sumsharp.monster.World.localResourceCache     // Catch:{ IOException -> 0x02c9 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x02c9 }
            java.lang.String r8 = "/"
            r7.<init>(r8)     // Catch:{ IOException -> 0x02c9 }
            java.lang.StringBuilder r5 = r7.append(r5)     // Catch:{ IOException -> 0x02c9 }
            java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x02c9 }
            r6.put(r5, r1)     // Catch:{ IOException -> 0x02c9 }
            int r1 = r2 + 1
            r2 = r1
            r1 = r4
            goto L_0x01db
        L_0x0213:
            int r4 = r10.readInt()
            java.lang.String r2 = r10.readString()
            int r0 = r10.readInt()
            byte r1 = r10.readByte()
            java.lang.String r10 = ""
            boolean r10 = r2.equals(r10)
            if (r10 != 0) goto L_0x0231
            r10 = -1
            if (r0 == r10) goto L_0x0231
            com.sumsharp.monster.GetItem.addTaskTip(r4, r2)
        L_0x0231:
            r10 = -1
            if (r1 == r10) goto L_0x0005
            r10 = r4
            r3 = -1
            if (r0 == r3) goto L_0x02cd
            int r10 = r10 >> 16
            r3 = r10
        L_0x023b:
            r10 = 65535(0xffff, float:9.1834E-41)
            r10 = r10 & r4
            com.sumsharp.monster.common.data.Player r5 = com.sumsharp.monster.common.CommonData.player
            com.sumsharp.monster.common.data.Task r3 = r5.findTask(r3)
            if (r3 == 0) goto L_0x0005
            r5 = -1
            if (r0 != r5) goto L_0x026d
            r3.taskState = r1
            r3.dialogue = r2
            byte r10 = r3.taskState
            r0 = 3
            if (r10 != r0) goto L_0x0005
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r0 = r3.title
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r10.<init>(r0)
            java.lang.String r0 = "(完成)"
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r10 = r10.toString()
            com.sumsharp.monster.GetItem.addTaskTip(r4, r10)
            goto L_0x0005
        L_0x026d:
            com.sumsharp.monster.common.data.TaskCondition r10 = r3.findCondition(r10)
            if (r10 == 0) goto L_0x0005
            r10.haveCount = r0
            r10.finished = r1
            goto L_0x0005
        L_0x0279:
            int r1 = r10.readInt()
            byte[] r0 = r10.readBytes()
            byte r10 = r10.readByte()
            if (r10 != 0) goto L_0x02be
            com.sumsharp.monster.common.data.Task r10 = new com.sumsharp.monster.common.data.Task
            r10.<init>()
            java.io.DataInputStream r1 = new java.io.DataInputStream
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream
            r2.<init>(r0)
            r1.<init>(r2)
            r10.load(r1)
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player
            r0.addTask(r10)
            int r0 = r10.taskType
            if (r0 != 0) goto L_0x0005
            java.lang.String r3 = "系统信息"
            r4 = -1
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "接受任务："
            r0.<init>(r1)
            java.lang.String r10 = r10.title
            java.lang.StringBuilder r10 = r0.append(r10)
            java.lang.String r5 = r10.toString()
            r6 = 1
            r7 = 1
            r8 = 1
            com.sumsharp.monster.common.CommonComponent.showMessage(r3, r4, r5, r6, r7, r8)
            goto L_0x0005
        L_0x02be:
            com.sumsharp.monster.common.data.Player r10 = com.sumsharp.monster.common.CommonData.player
            r10.removeTask(r1)
            goto L_0x0005
        L_0x02c5:
            r10 = move-exception
            r0 = r2
            goto L_0x01e4
        L_0x02c9:
            r10 = move-exception
            r0 = r4
            goto L_0x01e4
        L_0x02cd:
            r3 = r10
            goto L_0x023b
        L_0x02d0:
            r4 = r1
            goto L_0x01f1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.common.CommonProcessor.handleActionMsg(com.sumsharp.monster.net.UWAPSegment):void");
    }

    private static void handleChannelMsg(UWAPSegment segment) {
        switch (segment.subType) {
            case 1:
                Chat.recvChatMsg(segment.readInt(), segment.readString(), segment.readInt(), segment.readString(), segment.readString());
                return;
            default:
                return;
        }
    }

    private static void handleAccountMsg(UWAPSegment segment) {
        switch (segment.subType) {
            case 11:
                CommonData.account.accountId = segment.readInt();
                CommonData.account.name = segment.readString();
                CommonData.account.password = segment.readString();
                CommonData.account.phone = segment.readString();
                CommonData.account.modifyPasswordTimes = segment.readInt();
                CommonData.account.gameMoney = segment.readInt();
                CommonData.account.isMonth = segment.readBoolean();
                CommonData.account.isSubscribe = segment.readBoolean();
                return;
            case Tool.IMAGE_FONT_WIDTH /*13*/:
                try {
                    CommonData.player.initPlayerData(segment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("player init ok");
                NewStage.playerLogined = true;
                GlobalVar.setGlobalValue((String) "PLAYERLOGIN", (Object) "true");
                return;
            default:
                return;
        }
    }

    private static void handlePlayerMsg(UWAPSegment segment) {
        switch (segment.subType) {
            case 7:
                CommonData.player.addFriend(segment.readInt(), segment.readString(), segment.readByte(), segment.readByte());
                return;
            case 11:
                NewStage.netPlayerPosition(segment);
                return;
            case 12:
                NewStage.netPlayerPositionDetail(segment);
                return;
            case Tool.IMAGE_FONT_WIDTH /*13*/:
                NewStage.netPlayerLeave(segment.readInt());
                return;
            case Tool.EDGE_ROUND_ALL /*15*/:
                GameItem.updateDesc(segment.readString(), segment.readInt());
                return;
            case 17:
                GameItem.updateIcon(new ImageSet(segment.readBytes()), segment.readInt());
                return;
            case 19:
                CommonData.player.updatePetSwitchState(segment.readInt(), segment.readInt(), segment.readInt());
                return;
            case 22:
                int[] ids = segment.readInts();
                byte[] states = segment.readBytes();
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= ids.length) {
                        CommonData.player.orderFriends();
                        return;
                    }
                    Friend f = CommonData.player.getFriend(ids[i2]);
                    if (f != null) {
                        f.state = states[i2];
                    }
                    i = i2 + 1;
                }
            case 23:
                int i3 = segment.readInt();
                int iconId = segment.readInt();
                Pet pet = CommonData.player.getPet(i3);
                if (pet != null) {
                    pet.setIconID(iconId);
                    return;
                }
                return;
            case 27:
                Skill.updateDesc(segment.readInt(), segment.readByte(), segment.readString());
                return;
            case 29:
                byte[] buff = segment.readBytes();
                Buff b = new Buff();
                b.load(new DataInputStream(new ByteArrayInputStream(buff)));
                CommonData.player.addBuff(b);
                return;
            case 32:
                NetPlayer np = NewStage.getNetPlayer(segment.readInt());
                if (np != null) {
                    np.honorRank = segment.readByte();
                    np.honorTitle = segment.readString();
                    np.arenaWin = segment.readInt();
                    np.arenaLose = segment.readInt();
                    np.battlePet.name = segment.readString();
                    np.battlePet.sex = segment.readByte();
                    np.battlePet.matingTimes = segment.readByte();
                    np.battlePet.setAttribute(52, segment.readByte());
                    np.battlePet.setAttribute(58, segment.readByte());
                    np.battlePet.setIconID(segment.readInt());
                    return;
                }
                return;
            case 46:
                int id = segment.readInt();
                if (id != -1) {
                    CommonData.player.delFriend(id);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private static void handleGroupMsg(UWAPSegment segment) {
        switch (segment.subType) {
            case 6:
                CommonData.team.leave(segment);
                return;
            case 7:
                CommonData.team.changeMemberState(segment);
                return;
            case 9:
                CommonData.team.syncTeamInfo(segment);
                return;
            case 38:
                CommonData.player.guild = segment.readString();
                return;
            default:
                return;
        }
    }

    private static void handleBattleMsg(UWAPSegment segment) {
        switch (segment.subType) {
            case 2:
                byte type = segment.readByte();
                segment.reset();
                if (type == 1) {
                    CommonComponent.closeAllUI();
                    Battle.initBattle(segment);
                    return;
                }
                CommonComponent.closeAllUI();
                Battle.initBattle(segment);
                return;
            case 3:
                if (Battle.battleInstance != null) {
                    Battle.battleInstance.result(segment);
                    return;
                }
                return;
            case 4:
                if (Battle.battleInstance != null) {
                    Battle.battleInstance.roundEnd(segment);
                    return;
                }
                return;
            case 14:
                String arenaName = segment.readString();
                String npcName = segment.readString();
                int npcIcon = segment.readInt();
                String[] petNames = segment.readStrings();
                int[] petIcons = segment.readInts();
                boolean init = segment.readBoolean();
                if (World.arena == null && init) {
                    World.arena = new ChallengeArena(arenaName, npcName, npcIcon, petNames, petIcons);
                    return;
                } else if (World.arena != null && init) {
                    World.arena.update(npcName, npcIcon, petNames, petIcons);
                    World.arena.nextRound();
                    return;
                } else {
                    return;
                }
            case 16:
                if (World.arena != null) {
                    World.arena.clear();
                }
                World.arena = null;
                return;
            default:
                return;
        }
    }

    private static void handleMailMsg(UWAPSegment segment) {
        switch (segment.subType) {
            case 2:
                byte incType = segment.readByte();
                int count = segment.readInt();
                if (incType == 0) {
                    NewStage.hasNewMail = count;
                    return;
                } else if (incType == 1) {
                    NewStage.hasNewMail += count;
                    Chat.addMsg("[系]", "您收到了一封新的邮件", 16776960, 0);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private static void handleSkillMsg(UWAPSegment segment) {
        switch (segment.subType) {
            case 5:
                int petId = segment.readInt();
                byte[] skillInfo = segment.readBytes();
                Pet pet = CommonData.player.getPet(petId);
                if (pet != null) {
                    pet.addSkill(skillInfo);
                    return;
                }
                return;
            case 6:
                int petId2 = segment.readInt();
                int skillId = segment.readInt();
                byte toLevel = segment.readByte();
                short mp = (short) segment.readInt();
                Pet pet2 = CommonData.player.getPet(petId2);
                if (pet2 != null) {
                    pet2.updateSkillLevel(skillId, toLevel, mp);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* JADX WARNING: type inference failed for: r1v13, types: [int] */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r1v40, types: [int] */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void handleFieldMsg(com.sumsharp.monster.net.UWAPSegment r8) {
        /*
            byte r0 = r8.subType
            switch(r0) {
                case 2: goto L_0x010b;
                case 7: goto L_0x003f;
                case 8: goto L_0x00ed;
                case 10: goto L_0x002e;
                case 18: goto L_0x0006;
                default: goto L_0x0005;
            }
        L_0x0005:
            return
        L_0x0006:
            int r1 = r8.readInt()
            java.lang.String r0 = r8.readString()
            byte[] r0 = r8.readBytes()
            com.sumsharp.monster.common.data.Player r2 = com.sumsharp.monster.common.CommonData.player
            com.sumsharp.monster.item.GameItem r1 = r2.findItem(r1)
            if (r1 == 0) goto L_0x0027
            java.io.DataInputStream r2 = new java.io.DataInputStream
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream
            r3.<init>(r0)
            r2.<init>(r3)
            r1.loadPetRate(r2)
        L_0x0027:
            java.lang.String r8 = r8.readString()
            r1.desc = r8
            goto L_0x0005
        L_0x002e:
            int r0 = r8.readInt()
            java.lang.String r8 = r8.readString()
            com.sumsharp.monster.common.data.Player r1 = com.sumsharp.monster.common.CommonData.player
            com.sumsharp.monster.common.data.Pet r0 = r1.getPet(r0)
            r0.name = r8
            goto L_0x0005
        L_0x003f:
            byte[] r0 = r8.readBytes()
            java.io.ByteArrayInputStream r8 = new java.io.ByteArrayInputStream
            r8.<init>(r0)
            java.io.DataInputStream r0 = new java.io.DataInputStream
            r0.<init>(r8)
            r8 = 0
            byte r8 = r0.readByte()     // Catch:{ IOException -> 0x0155 }
        L_0x0052:
            java.util.Vector r4 = new java.util.Vector
            r4.<init>()
            if (r8 <= 0) goto L_0x0005
            r1 = 0
            r3 = r1
        L_0x005b:
            if (r3 < r8) goto L_0x007b
            com.sumsharp.monster.common.data.Pet[] r8 = com.sumsharp.monster.NewStage.fieldPets
            int r8 = r8.length
            int r0 = r4.size()
            int r8 = r8 + r0
            com.sumsharp.monster.common.data.Pet[] r0 = new com.sumsharp.monster.common.data.Pet[r8]
            com.sumsharp.monster.common.data.Pet[] r8 = com.sumsharp.monster.NewStage.fieldPets
            r1 = 0
            r2 = 0
            com.sumsharp.monster.common.data.Pet[] r3 = com.sumsharp.monster.NewStage.fieldPets
            int r3 = r3.length
            java.lang.System.arraycopy(r8, r1, r0, r2, r3)
            r8 = 0
        L_0x0072:
            int r1 = r4.size()
            if (r8 < r1) goto L_0x00c5
            com.sumsharp.monster.NewStage.fieldPets = r0
            goto L_0x0005
        L_0x007b:
            com.sumsharp.monster.common.data.Pet r5 = new com.sumsharp.monster.common.data.Pet
            r5.<init>()
            r5.load(r0)     // Catch:{ IOException -> 0x0094 }
        L_0x0083:
            r2 = 0
            r1 = 0
        L_0x0085:
            com.sumsharp.monster.common.data.Pet[] r6 = com.sumsharp.monster.NewStage.fieldPets
            int r6 = r6.length
            if (r1 < r6) goto L_0x0099
            r1 = r2
        L_0x008b:
            if (r1 != 0) goto L_0x0090
            r4.addElement(r5)
        L_0x0090:
            int r1 = r3 + 1
            r3 = r1
            goto L_0x005b
        L_0x0094:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0083
        L_0x0099:
            com.sumsharp.monster.common.data.Pet[] r6 = com.sumsharp.monster.NewStage.fieldPets
            r6 = r6[r1]
            int r6 = r6.id
            int r7 = r5.id
            if (r6 != r7) goto L_0x00c2
            com.sumsharp.monster.common.data.Pet[] r2 = com.sumsharp.monster.NewStage.fieldPets
            r2[r1] = r5
            int r1 = r3 * 50
            int r1 = r1 + 320
            short r1 = (short) r1
            r5.pixelX = r1
            r1 = 220(0xdc, float:3.08E-43)
            r5.pixelY = r1
            r1 = 1
            r5.visible = r1
            r1 = 1
            r5.selfMove = r1
            r1 = 2
            r5.speed = r1
            r1 = 0
            r2 = 0
            r5.go(r1, r2)
            r1 = 1
            goto L_0x008b
        L_0x00c2:
            int r1 = r1 + 1
            goto L_0x0085
        L_0x00c5:
            java.lang.Object r1 = r4.elementAt(r8)
            com.sumsharp.monster.common.data.Pet r1 = (com.sumsharp.monster.common.data.Pet) r1
            int r2 = r8 * 50
            int r2 = r2 + 320
            short r2 = (short) r2
            r1.pixelX = r2
            r2 = 220(0xdc, float:3.08E-43)
            r1.pixelY = r2
            r2 = 1
            r1.visible = r2
            r2 = 1
            r1.selfMove = r2
            r2 = 2
            r1.speed = r2
            r2 = 0
            r3 = 0
            r1.go(r2, r3)
            com.sumsharp.monster.common.data.Pet[] r2 = com.sumsharp.monster.NewStage.fieldPets
            int r2 = r2.length
            int r2 = r2 + r8
            r0[r2] = r1
            int r8 = r8 + 1
            goto L_0x0072
        L_0x00ed:
            int r0 = r8.readInt()
            r8 = 0
        L_0x00f2:
            com.sumsharp.monster.common.data.Pet[] r1 = com.sumsharp.monster.NewStage.fieldPets
            int r1 = r1.length
            if (r8 >= r1) goto L_0x0005
            com.sumsharp.monster.common.data.Pet[] r1 = com.sumsharp.monster.NewStage.fieldPets
            r1 = r1[r8]
            int r1 = r1.id
            if (r1 != r0) goto L_0x0108
            com.sumsharp.monster.common.data.Pet[] r0 = com.sumsharp.monster.NewStage.fieldPets
            r8 = r0[r8]
            r0 = 0
            r8.visible = r0
            goto L_0x0005
        L_0x0108:
            int r8 = r8 + 1
            goto L_0x00f2
        L_0x010b:
            byte r0 = r8.readByte()
            com.sumsharp.monster.common.data.Pet[] r1 = new com.sumsharp.monster.common.data.Pet[r0]
            com.sumsharp.monster.NewStage.fieldPets = r1
            if (r0 <= 0) goto L_0x0005
            r1 = 0
            r2 = r1
        L_0x0117:
            if (r2 >= r0) goto L_0x0005
            com.sumsharp.monster.common.data.Pet r3 = new com.sumsharp.monster.common.data.Pet
            r3.<init>()
            byte[] r1 = r8.readBytes()
            java.io.DataInputStream r4 = new java.io.DataInputStream     // Catch:{ IOException -> 0x0150 }
            java.io.ByteArrayInputStream r5 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0150 }
            r5.<init>(r1)     // Catch:{ IOException -> 0x0150 }
            r4.<init>(r5)     // Catch:{ IOException -> 0x0150 }
            r3.load(r4)     // Catch:{ IOException -> 0x0150 }
        L_0x012f:
            int r1 = r2 * 50
            int r1 = r1 + 320
            short r1 = (short) r1
            r3.pixelX = r1
            r1 = 220(0xdc, float:3.08E-43)
            r3.pixelY = r1
            r1 = 1
            r3.visible = r1
            r1 = 1
            r3.selfMove = r1
            r1 = 2
            r3.speed = r1
            r1 = 0
            r4 = 0
            r3.go(r1, r4)
            com.sumsharp.monster.common.data.Pet[] r1 = com.sumsharp.monster.NewStage.fieldPets
            r1[r2] = r3
            int r1 = r2 + 1
            r2 = r1
            goto L_0x0117
        L_0x0150:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x012f
        L_0x0155:
            r1 = move-exception
            goto L_0x0052
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.common.CommonProcessor.handleFieldMsg(com.sumsharp.monster.net.UWAPSegment):void");
    }

    public static void handleSegment(UWAPSegment segment) {
        segment.reset();
        if (segment.isErrorPacket()) {
            int readInt = segment.readInt();
            String readString = segment.readString();
            if (segment.mainType == 17 && segment.subType == 14) {
                Npc.touchingNpc = null;
                Npc.showConnection = false;
                return;
            }
            return;
        }
        switch (segment.mainType) {
            case 16:
                handleAccountMsg(segment);
                return;
            case 17:
                handleActionMsg(segment);
                return;
            case 18:
                handlePlayerMsg(segment);
                return;
            case 19:
                handleChannelMsg(segment);
                return;
            case 20:
                handleBattleMsg(segment);
                return;
            case 21:
                handleFieldMsg(segment);
                return;
            case 23:
                handleGroupMsg(segment);
                return;
            case 25:
                handleMailMsg(segment);
                return;
            case 26:
                handleSkillMsg(segment);
                return;
            default:
                return;
        }
    }

    public static int syscall(GTVM vm, short funcID, int[] params) {
        return 0;
    }
}
