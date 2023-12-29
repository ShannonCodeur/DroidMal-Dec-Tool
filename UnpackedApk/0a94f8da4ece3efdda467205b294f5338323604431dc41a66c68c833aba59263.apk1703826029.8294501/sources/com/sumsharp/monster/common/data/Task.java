package com.sumsharp.monster.common.data;

import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class Task {
    public static final byte TASK_TYPE_DAY = 2;
    public static final String[] TASK_TYPE_NAME = {"", "循环任务", "日常任务", "服务器收集任务"};
    public static final byte TASK_TYPE_NORMAL = 0;
    public static final byte TASK_TYPE_REPEAT = 1;
    public static final byte TASK_TYPE_SERVERGATHER = 3;
    public Vector conditions = new Vector();
    public String dialogue;
    public int id;
    private long lastSendTime = -1;
    public int level;
    public Vector prizes = new Vector();
    public String taskDest;
    public String taskRequest;
    public byte taskState;
    public int taskType;
    public String title;
    public int type = 0;

    public String toDesc() {
        String ret;
        String ret2 = TASK_TYPE_NAME[this.type];
        if (this.type != 0) {
            ret2 = String.valueOf(ret2) + "\n";
        }
        for (int i = 0; i < this.conditions.size(); i++) {
            TaskCondition c = (TaskCondition) this.conditions.elementAt(i);
            String clr = Integer.toHexString(Tool.CLR_TABLE[9]);
            if (c.isFinished()) {
                clr = Integer.toHexString(Tool.CLR_TABLE[10]);
            }
            ret = String.valueOf(ret) + "<c" + clr + ">" + c.getDesc() + "</c>";
            if (i < this.conditions.size() - 1) {
                ret = String.valueOf(ret) + "\n";
            }
        }
        return ret;
    }

    public void checkTime() {
        for (int i = 0; i < this.conditions.size(); i++) {
            TaskCondition c = (TaskCondition) this.conditions.elementAt(i);
            if (c.type == 7 && c.checkTime() && System.currentTimeMillis() - this.lastSendTime > 30000) {
                this.lastSendTime = System.currentTimeMillis();
                try {
                    UWAPSegment segment = new UWAPSegment(17, 13);
                    segment.writeInt(this.id);
                    Utilities.sendRequest(segment);
                } catch (IOException e) {
                }
            }
        }
    }

    public TaskCondition findCondition(int conid) {
        for (int i = 0; i < this.conditions.size(); i++) {
            TaskCondition c = (TaskCondition) this.conditions.elementAt(i);
            if (c.id == conid) {
                return c;
            }
        }
        return null;
    }

    public byte[] toBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(this.id);
            dos.writeByte(this.taskState);
            dos.writeUTF(this.title);
            dos.writeUTF(this.taskDest);
            dos.writeUTF(this.taskRequest);
            dos.writeUTF(this.dialogue);
            dos.writeByte(this.conditions.size());
            for (int i = 0; i < this.conditions.size(); i++) {
                dos.write(((TaskCondition) this.conditions.elementAt(i)).toByte());
            }
            dos.writeByte(this.prizes.size());
            for (int i2 = 0; i2 < this.prizes.size(); i2++) {
                dos.write(((TaskPrize) this.prizes.elementAt(i2)).toByte());
            }
        } catch (IOException e) {
        }
        return bos.toByteArray();
    }

    public void load(DataInputStream dis) {
        try {
            this.id = dis.readInt();
            this.level = dis.readInt();
            this.taskType = dis.readByte();
            this.type = dis.readByte();
            this.taskState = dis.readByte();
            this.title = dis.readUTF();
            this.taskDest = dis.readUTF();
            this.taskRequest = dis.readUTF();
            this.dialogue = dis.readUTF();
            byte count = dis.readByte();
            for (int i = 0; i < count; i++) {
                TaskCondition c = new TaskCondition();
                c.load(dis);
                this.conditions.addElement(c);
            }
            byte count2 = dis.readByte();
            for (int i2 = 0; i2 < count2; i2++) {
                TaskPrize p = new TaskPrize();
                p.load(dis);
                this.prizes.addElement(p);
            }
        } catch (IOException e) {
        }
    }
}
