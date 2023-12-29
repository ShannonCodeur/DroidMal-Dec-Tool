package com.sumsharp.monster.common.data;

import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.common.CommonData;
import java.lang.reflect.Array;
import java.util.Stack;
import java.util.Vector;

public class SearchRoad {
    public static final byte DOWN = 3;
    public static final byte LEFT = 0;
    private static final int MAX_TRY = 100;
    public static final byte RIGHT = 1;
    public static final byte UP = 2;
    private int lastDir = -1;
    private Point[][] map;
    public Stack points;
    private int tryStep = 0;

    public SearchRoad(int[] collide) {
        int w = NewStage.mapWidth;
        int h = NewStage.mapHeight;
        this.map = (Point[][]) Array.newInstance(Point.class, new int[]{h, w});
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                this.map[j][i] = new Point((j * w) + i, i, j, -1);
                this.map[j][i].isWall = true;
                if (NewStage.canPass(i, j)) {
                    this.map[j][i].isWall = false;
                }
            }
        }
        init();
        if (CommonData.player.wpList != null) {
            CommonData.player.wpList = null;
            CommonData.player.setState(0);
        }
    }

    /* Debug info: failed to restart local var, previous not found, register: 14 */
    private boolean canPass(int x, int y, int dir) {
        Player p = CommonData.player;
        int w = p.getWidth();
        Vector tiles = new Vector();
        int xx = x * 16;
        int yy = y * 16;
        switch (dir) {
            case 0:
            case 1:
                for (int tmpy = yy; tmpy < yy + 15; tmpy++) {
                    Integer tile = new Integer((NewStage.mapWidth * (tmpy / 16)) + (((p.getDir() == 1 ? w : 0) + xx) / 16));
                    if (!tiles.contains(tile)) {
                        tiles.addElement(tile);
                    }
                }
                break;
            case 2:
            case 3:
                for (int tmpx = xx; tmpx < xx + w; tmpx++) {
                    Integer tile2 = new Integer((NewStage.mapWidth * (((p.getDir() == 3 ? 15 : 0) + yy) / 16)) + (tmpx / 16));
                    if (!tiles.contains(tile2)) {
                        tiles.addElement(tile2);
                    }
                }
                break;
        }
        for (int i = 0; i < tiles.size(); i++) {
            if (!NewStage.canPass(((Integer) tiles.elementAt(i)).intValue())) {
                return false;
            }
        }
        if (xx < 0 || p.getWidth() + xx > NewStage.mapWidth * 16) {
            return false;
        }
        return true;
    }

    private boolean moveTo(int dir) {
        Point cur = (Point) this.points.peek();
        int newX = cur.x;
        int newY = cur.y;
        switch (dir) {
            case 0:
                newX = cur.x - 1;
                break;
            case 1:
                newX = cur.x + 1;
                break;
            case 2:
                newY = cur.y - 1;
                break;
            case 3:
                newY = cur.y + 1;
                break;
        }
        boolean canMove = checkBounds(newX, newY);
        if (canMove) {
            if (this.map[newY][newX].isWall) {
                canMove = false;
            } else {
                canMove = true;
            }
        }
        if (canMove) {
            if (this.map[newY][newX].isVisited) {
                canMove = false;
            } else {
                canMove = true;
            }
        }
        if (canMove) {
            cur.dir = dir;
            cur.setDisableDir(dir);
            Point newPoint = getPoint(newX, newY);
            if (newPoint == null) {
                return false;
            }
            newPoint.setDisableDir(cur.getReverseDir());
            if (!checkBounds(newX, newY - 1) || this.map[newY - 1][newX].isVisited) {
                newPoint.setDisableDir(2);
            }
            if (!checkBounds(newX, newY + 1) || this.map[newY + 1][newX].isVisited) {
                newPoint.setDisableDir(3);
            }
            if (!checkBounds(newX - 1, newY) || this.map[newY][newX - 1].isVisited) {
                newPoint.setDisableDir(0);
            }
            if (!checkBounds(newX + 1, newY) || this.map[newY][newX + 1].isVisited) {
                newPoint.setDisableDir(1);
            }
            newPoint.isVisited = true;
            this.points.push(newPoint);
        }
        return canMove;
    }

    private boolean checkBounds(int newX, int newY) {
        boolean canMove;
        if (newX < 0 || newY < 0 || newY > this.map.length - 1 || newX > this.map[newY].length - 1) {
            return false;
        }
        if (this.map[newY][newX].isWall) {
            canMove = false;
        } else {
            canMove = true;
        }
        return canMove;
    }

    private void init() {
        this.points = new Stack();
        reset();
    }

    public boolean find(int startx, int starty, int endx, int endy) {
        if (Math.abs(startx - endx) < CommonData.player.getWidth() / 2 && endy < starty && starty - endy < 15) {
            return false;
        }
        int x1 = startx / 16;
        int y1 = starty / 16;
        int x2 = endx / 16;
        int y2 = endy / 16;
        if (y1 < 0 || y1 > this.map.length - 1 || y2 < 0 || y2 > this.map.length - 1) {
            return false;
        }
        if (this.map[y2][x2].isWall) {
            return false;
        }
        Point start = getPoint(x1, y1);
        if (start == null) {
            return false;
        }
        start.isSelected = true;
        start.isVisited = true;
        Point end = getPoint(x2, y2);
        this.points.push(start);
        while (0 == 0) {
            this.tryStep--;
            if (this.tryStep == 0) {
                return false;
            }
            if (this.points.empty()) {
                return false;
            }
            Point cur = (Point) this.points.peek();
            if (cur.x == x2 && cur.y == y2) {
                return true;
            }
            boolean pass = false;
            int i = 0;
            while (true) {
                if (i >= 4) {
                    break;
                }
                int canDir = getPreferDir(end);
                if (canDir != -1) {
                    pass = moveTo(canDir);
                    if (pass) {
                        this.lastDir = canDir;
                        break;
                    }
                    this.lastDir = -1;
                    cur.setDisableDir(canDir);
                } else {
                    pass = moveTo(i);
                    if (pass) {
                        this.lastDir = canDir;
                        break;
                    }
                    this.lastDir = -1;
                    cur.setDisableDir(i);
                }
                i++;
            }
            if (!pass) {
                if (this.points.empty()) {
                    return false;
                }
                this.points.pop();
            }
        }
        return false;
    }

    private Point getPoint(int x, int y) {
        for (int j = 0; j < this.map.length; j++) {
            for (int i = 0; i < this.map[j].length; i++) {
                if (this.map[j][i].x == x && this.map[j][i].y == y) {
                    return this.map[j][i];
                }
            }
        }
        return null;
    }

    public void reset() {
        if (this.points != null && this.points.size() > 0) {
            this.points.removeAllElements();
        }
        for (int j = 0; j < this.map.length; j++) {
            for (int i = 0; i < this.map[j].length; i++) {
                this.map[j][i].isVisited = false;
                this.map[j][i].dir = -1;
                this.map[j][i].deadDir = new int[4];
            }
        }
        this.tryStep = MAX_TRY;
        this.lastDir = -1;
    }

    public short[][] getPointPos() {
        if (this.points == null || this.points.size() == 0) {
            return null;
        }
        short[][] path = new short[this.points.size()][];
        Point start = (Point) this.points.elementAt(0);
        path[0] = new short[2];
        path[0][0] = (short) (start.x * 16);
        path[0][1] = (short) (start.y * 16);
        int offx = CommonData.player.pixelX - path[0][0];
        int offy = CommonData.player.pixelY - path[0][1];
        for (int i = 0; i < path.length; i++) {
            Point p = (Point) this.points.elementAt(i);
            path[i] = new short[2];
            path[i][0] = (short) ((p.x * 16) + offx);
            path[i][1] = (short) ((p.y * 16) + offy);
        }
        return deleteExtraNode(path);
    }

    public int getPreferDir(Point end) {
        int dy;
        int ex = end.x;
        int ey = end.y;
        Point cur = (Point) this.points.peek();
        int dx = ex - cur.x > 0 ? ex - cur.x : cur.x - ex;
        if (ey - cur.y > 0) {
            dy = ey - cur.y;
        } else {
            dy = cur.y - ey;
        }
        Point up = getPoint(cur.x, cur.y - 1);
        Point down = getPoint(cur.x, cur.y + 1);
        Point left = getPoint(cur.x - 1, cur.y);
        Point right = getPoint(cur.x + 1, cur.y);
        if (up == null || up.isWall) {
            cur.setDisableDir(2);
        }
        if (down == null || down.isWall) {
            cur.setDisableDir(3);
        }
        if (left == null || left.isWall) {
            cur.setDisableDir(0);
        }
        if (right == null || right.isWall) {
            cur.setDisableDir(1);
        }
        if (dx == 0) {
            if (ey > cur.y && cur.deadDir[3] != 1) {
                return 3;
            }
            if (ey < cur.y && cur.deadDir[2] != 1) {
                return 2;
            }
            if (cur.deadDir[0] != 1) {
                return 0;
            }
            if (cur.deadDir[1] != 1) {
                return 1;
            }
        } else if (dy == 0) {
            if (ex > cur.x && cur.deadDir[1] != 1) {
                return 1;
            }
            if (ex < cur.x && cur.deadDir[0] != 1) {
                return 0;
            }
            if (cur.deadDir[2] != 1) {
                return 2;
            }
            if (cur.deadDir[3] != 1) {
                return 3;
            }
        }
        if (dx > dy) {
            if (ex > cur.x) {
                if (cur.deadDir[1] != 1) {
                    return 1;
                }
            } else if (cur.deadDir[0] != 1) {
                return 0;
            }
            byte verticalDir = CommonData.player.getVerticalDir(cur.x * 16, cur.y * 16);
            if (verticalDir != -1) {
                return verticalDir;
            }
            if (ey > cur.y) {
                if (cur.deadDir[3] != 1) {
                    return 3;
                }
            } else if (cur.deadDir[2] != 1) {
                return 2;
            }
        } else if (dy > dx) {
            if (ey > cur.y) {
                if (this.lastDir != -1) {
                    if (this.lastDir == 0 && ex < cur.x) {
                        return 0;
                    }
                    if (this.lastDir == 1 && ex > cur.x) {
                        return 1;
                    }
                }
                if (cur.deadDir[3] != 1) {
                    return 3;
                }
                return getHorizontalDir(3);
            }
            if (this.lastDir != -1) {
                if (this.lastDir == 0 && ex < cur.x) {
                    return 0;
                }
                if (this.lastDir == 1 && ex > cur.x) {
                    return 1;
                }
            }
            if (cur.deadDir[2] != 1) {
                return 2;
            }
            return getHorizontalDir(2);
        } else if (dx == dy) {
            if (cur.x < ex) {
                if (cur.deadDir[1] != 1) {
                    return 1;
                }
            } else if (cur.deadDir[0] != 1) {
                return 0;
            }
            if (ey > cur.y) {
                if (cur.deadDir[3] != 1) {
                    return 3;
                }
            } else if (cur.deadDir[2] != 1) {
                return 2;
            }
        }
        return cur.getPreferDir();
    }

    private byte getHorizontalDir(byte dir) {
        int step = 0;
        Player p = CommonData.player;
        do {
            if (dir == 2) {
                if (p.canPass((p.pixelX + p.getWidth()) - (step * 6), (p.pixelY - p.getHeight()) - 5)) {
                    return 0;
                }
                if (p.canPass(p.pixelX + 5 + (step * 6), (p.pixelY - p.getHeight()) - 5)) {
                    return 1;
                }
            } else if (dir == 3) {
                if (p.canPass((p.pixelX + p.getWidth()) - (step * 6), p.pixelY + 5)) {
                    return 0;
                }
                if (p.canPass(p.pixelX + 5 + (step * 6), p.pixelY + 5)) {
                    return 1;
                }
            }
            step++;
        } while (step < 30);
        return -1;
    }

    private short[][] deleteExtraNode(short[][] path) {
        if (path.length <= 2) {
            return path;
        }
        Vector wp = new Vector();
        for (short[] addElement : path) {
            wp.addElement(addElement);
        }
        int i = 1;
        while (i + 1 < wp.size()) {
            short[] ir = (short[]) wp.elementAt(i + 1);
            short[] im = (short[]) wp.elementAt(i);
            short[] il = (short[]) wp.elementAt(i - 1);
            if ((il[1] == im[1] && im[1] == ir[1]) || (il[0] == im[0] && im[0] == ir[0])) {
                wp.removeElement(im);
            } else {
                i++;
            }
        }
        short[][] path2 = new short[wp.size()][];
        wp.copyInto(path2);
        return path2;
    }
}
