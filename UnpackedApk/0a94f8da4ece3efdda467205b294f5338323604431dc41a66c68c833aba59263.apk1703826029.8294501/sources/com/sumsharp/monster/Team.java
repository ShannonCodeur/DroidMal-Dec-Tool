package com.sumsharp.monster;

import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.common.data.NetPlayer;
import com.sumsharp.monster.net.UWAPSegment;
import java.util.Vector;

public class Team {
    public static final int TEAM_FOLLOW = 1;
    public static final int TEAM_LEAVED = 2;
    public static final int TEAM_NONE = -1;
    public static final int TEAM_NORMAL = 0;
    public static final int TEAM_ROLE_LEADER = 1;
    public static final int TEAM_ROLE_MEMBER = 2;
    public static final int TEAM_ROLE_NONE = 0;
    public Vector followMember = new Vector();
    public Vector freeMember = new Vector();
    public int id = -1;
    public AbstractUnit leader;

    public boolean isTeamMode() {
        return (this.id == -1 || this.leader == null) ? false : true;
    }

    public void clear() {
        this.id = -1;
        this.leader = null;
        this.followMember = new Vector();
        this.freeMember = new Vector();
    }

    public void clearWpinfo() {
        if (this.leader != null) {
            this.leader.wpList = null;
        }
        for (int i = 0; i < this.followMember.size(); i++) {
            ((AbstractUnit) this.followMember.elementAt(i)).wpList = null;
        }
    }

    public void addMember(AbstractUnit member, int state) {
        switch (state) {
            case 0:
                addFreeMember(member);
                return;
            case 1:
                addFollowMember(member);
                return;
            default:
                return;
        }
    }

    public void addFollowMember(AbstractUnit member) {
        synchronized (this.followMember) {
            this.followMember.addElement(member);
        }
    }

    public void addFreeMember(AbstractUnit member) {
        synchronized (this.freeMember) {
            this.freeMember.addElement(member);
        }
    }

    public void removeMember(int id2) {
        for (int i = 0; i < this.followMember.size(); i++) {
            AbstractUnit au = (AbstractUnit) this.followMember.elementAt(i);
            if (au.id == id2) {
                this.followMember.removeElementAt(i);
                au.teamState = -1;
                au.wpList = null;
                return;
            }
        }
        for (int i2 = 0; i2 < this.freeMember.size(); i2++) {
            AbstractUnit au2 = (AbstractUnit) this.freeMember.elementAt(i2);
            if (au2.id == id2) {
                this.freeMember.removeElementAt(i2);
                au2.teamState = -1;
                au2.wpList = null;
                return;
            }
        }
    }

    public AbstractUnit getMember(int id2) {
        if (this.leader != null && id2 == this.leader.id) {
            return this.leader;
        }
        for (int i = 0; i < this.followMember.size(); i++) {
            AbstractUnit au = (AbstractUnit) this.followMember.elementAt(i);
            if (au.id == id2) {
                return au;
            }
        }
        for (int i2 = 0; i2 < this.freeMember.size(); i2++) {
            AbstractUnit au2 = (AbstractUnit) this.freeMember.elementAt(i2);
            if (au2.id == id2) {
                return au2;
            }
        }
        return null;
    }

    /* Debug info: failed to restart local var, previous not found, register: 4 */
    public AbstractUnit getNextMember(AbstractUnit me) {
        if (me == this.leader && this.followMember.size() > 0) {
            return (AbstractUnit) this.followMember.elementAt(0);
        }
        for (int i = 0; i < this.followMember.size(); i++) {
            if (((AbstractUnit) this.followMember.elementAt(i)).id == me.id && i < this.followMember.size() - 1) {
                return (AbstractUnit) this.followMember.elementAt(i + 1);
            }
        }
        return null;
    }

    public void changeMemberState(UWAPSegment segment) {
        int id2 = segment.readInt();
        int state = segment.readInt();
        AbstractUnit au = getMember(id2);
        if (au != null) {
            au.teamState = (byte) state;
            if (au.teamState == 1) {
                synchronized (this.freeMember) {
                    if (this.freeMember.removeElement(au)) {
                        this.followMember.addElement(au);
                    }
                }
                return;
            }
            synchronized (this.followMember) {
                if (this.followMember.removeElement(au)) {
                    this.freeMember.addElement(au);
                    au.wpList = null;
                    au.setState(0);
                    if (au == CommonData.player) {
                        au.backX = this.leader.pixelX;
                        au.backY = this.leader.pixelY;
                        au.setState(4);
                    }
                }
            }
        }
    }

    public void leave(UWAPSegment segment) {
        byte disband = segment.readByte();
        int[] leaveid = segment.readInts();
        if (disband == 1) {
            this.leader.teamState = -1;
            int[] leaveid2 = new int[(this.followMember.size() + this.freeMember.size())];
            for (int i = 0; i < this.followMember.size(); i++) {
                leaveid2[i] = ((AbstractUnit) this.followMember.elementAt(i)).id;
            }
            for (int i2 = 0; i2 < this.freeMember.size(); i2++) {
                leaveid2[this.followMember.size() + i2] = ((AbstractUnit) this.freeMember.elementAt(i2)).id;
            }
            for (int removeMember : leaveid2) {
                removeMember(removeMember);
            }
            clear();
            return;
        }
        for (int removeMember2 : leaveid) {
            removeMember(removeMember2);
        }
    }

    public void syncTeamInfo(UWAPSegment segment) {
        byte refresh = segment.readByte();
        this.id = segment.readInt();
        int[] ids = segment.readInts();
        String[] names = segment.readStrings();
        int[] states = segment.readInts();
        short[] mapids = segment.readShorts();
        for (int i = 0; i < ids.length; i++) {
            AbstractUnit unit = getUnit(ids[i]);
            if (unit == null) {
                unit = new NetPlayer();
                NetPlayer np = (NetPlayer) unit;
                np.id = ids[i];
                np.name = names[i];
                np.teamState = (byte) states[i];
                np.mapId = mapids[i];
            }
            if (i == 0 && refresh == 1) {
                this.leader = unit;
            } else {
                addMember(unit, states[i]);
                unit.teamState = (byte) states[i];
            }
        }
    }

    private AbstractUnit getUnit(int id2) {
        if (id2 == CommonData.player.id) {
            return CommonData.player;
        }
        return NewStage.getNetPlayer(id2);
    }
}
