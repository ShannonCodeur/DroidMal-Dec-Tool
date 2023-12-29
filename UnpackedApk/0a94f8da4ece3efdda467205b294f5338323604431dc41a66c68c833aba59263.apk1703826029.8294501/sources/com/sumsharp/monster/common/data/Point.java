package com.sumsharp.monster.common.data;

/* compiled from: SearchRoad */
class Point {
    public int color;
    public int[] deadDir = new int[4];
    public int dir;
    public int id;
    public boolean isSelected;
    public boolean isVisited;
    public boolean isWall = false;
    public int x;
    public int y;

    public Point(int id2, int x2, int y2, int dir2) {
        this.id = id2;
        this.x = x2;
        this.y = y2;
        this.dir = dir2;
    }

    public int getPreferDir() {
        for (int i = 0; i < this.deadDir.length; i++) {
            if (this.deadDir[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    public void setDisableDir(int dir2) {
        if (dir2 == 2) {
            this.deadDir[2] = 1;
        } else if (dir2 == 3) {
            this.deadDir[3] = 1;
        } else if (dir2 == 0) {
            this.deadDir[0] = 1;
        } else if (dir2 == 1) {
            this.deadDir[1] = 1;
        }
    }

    public int getReverseDir() {
        switch (this.dir) {
            case 0:
                return 1;
            case 1:
                return 0;
            case 2:
                return 3;
            case 3:
                return 2;
            default:
                return -1;
        }
    }
}
