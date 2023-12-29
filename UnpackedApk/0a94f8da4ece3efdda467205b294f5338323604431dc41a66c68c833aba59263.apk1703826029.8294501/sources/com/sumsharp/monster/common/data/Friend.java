package com.sumsharp.monster.common.data;

public class Friend {
    public int id;
    public String name;
    public byte sex;
    public byte state;

    public int compareTo(Friend f) {
        if (f.state > this.state) {
            return 1;
        }
        if (f.state < this.state) {
            return -1;
        }
        return this.name.hashCode() - f.name.hashCode();
    }
}
