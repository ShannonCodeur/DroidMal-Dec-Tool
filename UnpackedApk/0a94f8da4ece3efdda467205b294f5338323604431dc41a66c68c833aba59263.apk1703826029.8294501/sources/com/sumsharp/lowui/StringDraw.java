package com.sumsharp.lowui;

import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class StringDraw {
    private String activeInfo = "";
    private int height;
    private int[][] lineClrs;
    private String[][] lineInfo;
    private int linePerPage;
    private String[][] lineStrs;
    private int maxWidth;
    private int pageNo;
    private int pageSize;
    private int width;

    public StringDraw() {
    }

    public StringDraw(String string, int width2, int height2) {
        this.width = width2;
        this.height = height2;
        init(string, width2, height2);
    }

    private void init(String string, int width2, int height2) {
        formatString(string, width2);
        compute(height2);
    }

    private void compute(int height2) {
        if (height2 == -1) {
            this.linePerPage = this.lineStrs.length;
        } else {
            this.linePerPage = height2 / Utilities.LINE_HEIGHT;
        }
        if (this.linePerPage == 0) {
            this.pageSize = 0;
        } else {
            this.pageSize = this.lineStrs.length / this.linePerPage;
            if (this.lineStrs.length % this.linePerPage != 0) {
                this.pageSize++;
            }
        }
        this.pageNo = 0;
        this.maxWidth = 0;
        for (int i = 0; i < this.lineStrs.length; i++) {
            int fw = Utilities.font.stringWidth(getLineString(i));
            if (fw > this.maxWidth) {
                this.maxWidth = fw;
            }
        }
    }

    public StringDraw split(int line) {
        StringDraw ret = new StringDraw();
        int oldLine = line;
        int newLine = length() - line;
        int[][] oldClr = new int[oldLine][];
        String[][] oldStr = new String[oldLine][];
        String[][] oldInfo = new String[oldLine][];
        int[][] newClr = new int[newLine][];
        String[][] newStr = new String[newLine][];
        String[][] newInfo = new String[newLine][];
        for (int i = 0; i < oldLine; i++) {
            int[] lc = new int[this.lineClrs[i].length];
            String[] ls = new String[this.lineStrs[i].length];
            String[] li = new String[this.lineInfo[i].length];
            for (int j = 0; j < lc.length; j++) {
                lc[j] = this.lineClrs[i][j];
                ls[j] = this.lineStrs[i][j];
                li[j] = this.lineInfo[i][j];
            }
            oldClr[i] = lc;
            oldStr[i] = ls;
            oldInfo[i] = li;
        }
        for (int i2 = 0; i2 < newLine; i2++) {
            int[] lc2 = new int[this.lineClrs[oldLine + i2].length];
            String[] ls2 = new String[this.lineStrs[oldLine + i2].length];
            String[] li2 = new String[this.lineInfo[oldLine + i2].length];
            for (int j2 = 0; j2 < lc2.length; j2++) {
                lc2[j2] = this.lineClrs[oldLine + i2][j2];
                ls2[j2] = this.lineStrs[oldLine + i2][j2];
                li2[j2] = this.lineInfo[oldLine + i2][j2];
            }
            newClr[i2] = lc2;
            newStr[i2] = ls2;
            newInfo[i2] = li2;
        }
        this.lineClrs = newClr;
        this.lineStrs = newStr;
        this.lineInfo = newInfo;
        ret.lineClrs = oldClr;
        ret.lineStrs = oldStr;
        ret.lineInfo = oldInfo;
        compute(-1);
        ret.compute(-1);
        return ret;
    }

    public String getLineString(int line) {
        if (line >= this.lineClrs.length) {
            return "";
        }
        String ret = "";
        for (int i = 0; i < this.lineStrs[line].length; i++) {
            ret = String.valueOf(ret) + this.lineStrs[line][i];
        }
        return ret;
    }

    public int length() {
        return this.lineStrs.length;
    }

    public void draw(Graphics g, int x, int y, int color) {
        draw(g, x, y, color, 0, false, false, false);
    }

    public void drawShadow(Graphics g, int x, int y, int color, int bgColor) {
        drawShadow(g, x, y, color, bgColor, false);
    }

    public void drawShadow(Graphics g, int x, int y, int color, int bgColor, boolean blod) {
        draw(g, x + 1, y + 1, bgColor, bgColor, false, true, blod);
        draw(g, x, y, color, color, false, false, blod);
    }

    public void draw3D(Graphics g, int x, int y, int color, int bgColor) {
        draw(g, x, y, color, bgColor, true, false, false);
    }

    private void draw(Graphics g, int x, int y, int color, int bgColor, boolean draw3d, boolean forceBgClr, boolean blod) {
        int dx;
        int dx2 = x;
        int dy = y;
        int startLine = this.pageNo * this.linePerPage;
        int endLine = startLine + this.linePerPage;
        if (endLine > this.lineClrs.length) {
            endLine = this.lineClrs.length;
        }
        for (int i = startLine; i < endLine; i++) {
            for (int j = 0; j < this.lineClrs[i].length; j++) {
                int clr = this.lineClrs[i][j];
                String str = this.lineStrs[i][j];
                String info = this.lineInfo[i][j];
                if (str.startsWith("#") && str.length() == 2) {
                    try {
                        int idx = Integer.parseInt(String.valueOf(str.charAt(1)));
                        Tool.chatEmote.drawFrame(g, ((clr / 3) % 3) + (idx * 3), dx, dy + (Utilities.LINE_HEIGHT / 2), 0, 6);
                        int[] iArr = this.lineClrs[i];
                        iArr[j] = iArr[j] + 1;
                        dx += 14;
                    } catch (NumberFormatException e) {
                    }
                }
                if (clr == -1) {
                    if (draw3d) {
                        Tool.draw3DString(g, str, dx, dy, color, bgColor);
                    } else {
                        g.setColor(color);
                        g.drawString(str, dx, dy, 20);
                    }
                    if (!this.activeInfo.equals("") && this.activeInfo.equals(info)) {
                        g.setColor(color);
                        g.drawLine(dx, (Utilities.LINE_HEIGHT + dy) - 3, Utilities.font.stringWidth(str) + dx, (Utilities.LINE_HEIGHT + dy) - 3);
                    }
                    dx += Utilities.font.stringWidth(str);
                } else {
                    if (forceBgClr) {
                        g.setColor(bgColor);
                    } else {
                        g.setColor(clr);
                    }
                    if (blod) {
                        int q = Tool.draw3DQuanlity;
                        if (q == 2) {
                            q = 1;
                        }
                        Tool.draw3DString(g, str, dx, dy, clr, bgColor, 20, q);
                    } else if (draw3d) {
                        Tool.draw3DString(g, str, dx, dy, clr, bgColor);
                    } else {
                        g.drawString(str, dx, dy, 20);
                    }
                    if (!this.activeInfo.equals("") && this.activeInfo.equals(info)) {
                        g.setColor(clr);
                        g.drawLine(dx, (Utilities.LINE_HEIGHT + dy) - 3, Utilities.font.stringWidth(str) + dx, (Utilities.LINE_HEIGHT + dy) - 3);
                    }
                    dx += Utilities.font.stringWidth(str) + 4;
                }
            }
            dx2 = x;
            dy += Utilities.LINE_HEIGHT;
        }
    }

    public void nextPage() {
        this.pageNo++;
        if (this.pageNo >= this.pageSize) {
            this.pageNo = this.pageSize - 1;
        }
    }

    public void prevPage() {
        this.pageNo--;
        if (this.pageNo < 0) {
            this.pageNo = 0;
        }
    }

    public void setPageNo(int pageNo2) {
        this.pageNo = pageNo2;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public int currPageLength() {
        int startLine = this.pageNo * this.linePerPage;
        int endLine = startLine + this.linePerPage;
        if (endLine > this.lineClrs.length) {
            endLine = this.lineClrs.length;
        }
        return endLine - startLine;
    }

    public int getLinePerPage() {
        return this.linePerPage;
    }

    private void putClr(Hashtable table, int line, int color) {
        Vector vec = (Vector) table.get(new Integer(line));
        if (vec == null) {
            vec = new Vector();
            table.put(new Integer(line), vec);
        }
        vec.addElement(new Integer(color));
    }

    private void putStr(Hashtable table, int line, String str) {
        Vector vec = (Vector) table.get(new Integer(line));
        if (vec == null) {
            vec = new Vector();
            table.put(new Integer(line), vec);
        }
        vec.addElement(str);
    }

    public void formatString(String s, int width2) {
        String currStr;
        int charCount = s.length();
        int lineWidth = 0;
        Hashtable lineClrs2 = new Hashtable();
        Hashtable lineStrs2 = new Hashtable();
        Hashtable lineInfs = new Hashtable();
        int lineStart = 0;
        int currClr = -1;
        String currInfo = "";
        int currLine = 0;
        int i = 0;
        while (i < charCount) {
            char ch = s.charAt(i);
            int chWidth = Utilities.font.charWidth(ch);
            if (ch == 10) {
                if (i <= 0 || s.charAt(i - 1) != 13) {
                    currStr = s.substring(lineStart, i);
                } else {
                    currStr = s.substring(lineStart, i - 1);
                }
                if (!currStr.equals("")) {
                    putClr(lineClrs2, currLine, currClr);
                    putStr(lineStrs2, currLine, currStr);
                    putStr(lineInfs, currLine, currInfo);
                }
                lineStart = i + 1;
                lineWidth = 0;
                currLine++;
            } else if (ch != '<') {
                if (ch == '#' && s.length() > i + 1) {
                    try {
                        Integer.parseInt(String.valueOf(s.charAt(i + 1)));
                        String currStr2 = s.substring(lineStart, i);
                        if (!currStr2.equals("")) {
                            putClr(lineClrs2, currLine, currClr);
                            putStr(lineStrs2, currLine, currStr2);
                            putStr(lineInfs, currLine, currInfo);
                        }
                        lineStart = i + 2;
                        if (lineWidth == 0 || lineWidth + 14 <= width2) {
                            lineWidth += 14;
                        } else {
                            lineWidth = 14;
                            currLine++;
                        }
                        putClr(lineClrs2, currLine, currClr);
                        putStr(lineStrs2, currLine, "#" + idx);
                        putStr(lineInfs, currLine, "");
                        i += 2;
                    } catch (NumberFormatException e) {
                    }
                }
                if (lineWidth == 0 || lineWidth + chWidth <= width2) {
                    lineWidth += chWidth;
                } else {
                    String currStr3 = s.substring(lineStart, i);
                    if (!currStr3.equals("")) {
                        putClr(lineClrs2, currLine, currClr);
                        putStr(lineStrs2, currLine, currStr3);
                        putStr(lineInfs, currLine, currInfo);
                    }
                    lineStart = i;
                    lineWidth = chWidth;
                    currLine++;
                }
            } else if (s.charAt(i + 1) == 'i') {
                String currStr4 = s.substring(lineStart, i);
                if (!currStr4.equals("")) {
                    putClr(lineClrs2, currLine, currClr);
                    putStr(lineStrs2, currLine, currStr4);
                    putStr(lineInfs, currLine, currInfo);
                }
                int i2 = i + 2;
                String infoStr = "";
                while (s.charAt(i2) != '>') {
                    infoStr = String.valueOf(infoStr) + s.charAt(i2);
                    i2++;
                }
                i = i2 + 1;
                lineStart = i;
                currInfo = infoStr;
                int x = infoStr.indexOf(":");
                if (x != -1) {
                    currClr = Tool.getQuanlityColor(Integer.parseInt(infoStr.substring(0, x)));
                }
            } else if (s.charAt(i + 1) == 'c') {
                String currStr5 = s.substring(lineStart, i);
                if (!currStr5.equals("")) {
                    putClr(lineClrs2, currLine, currClr);
                    putStr(lineStrs2, currLine, currStr5);
                    putStr(lineInfs, currLine, currInfo);
                }
                int i3 = i + 2;
                String clrStr = "";
                while (s.charAt(i3) != '>') {
                    clrStr = String.valueOf(clrStr) + s.charAt(i3);
                    i3++;
                }
                i = i3 + 1;
                lineStart = i;
                currClr = new Integer(Integer.parseInt(clrStr, 16)).intValue();
            } else if (s.charAt(i + 1) == '/') {
                String currStr6 = s.substring(lineStart, i);
                if (!currStr6.equals("")) {
                    putClr(lineClrs2, currLine, currClr);
                    putStr(lineStrs2, currLine, currStr6);
                    putStr(lineInfs, currLine, currInfo);
                }
                currInfo = "";
                i += 4;
                lineStart = i;
                currClr = -1;
            }
            i++;
        }
        if (lineWidth > 0) {
            String currStr7 = s.substring(lineStart);
            putClr(lineClrs2, currLine, currClr);
            putStr(lineStrs2, currLine, currStr7);
            putStr(lineInfs, currLine, currInfo);
        }
        this.lineClrs = new int[lineClrs2.size()][];
        this.lineStrs = new String[lineStrs2.size()][];
        this.lineInfo = new String[lineInfs.size()][];
        int i4 = 0;
        while (i4 <= currLine && i4 < this.lineClrs.length) {
            Integer num = new Integer(i4);
            Vector vec = (Vector) lineClrs2.get(num);
            Integer num2 = new Integer(i4);
            Vector vecStr = (Vector) lineStrs2.get(num2);
            Integer num3 = new Integer(i4);
            Vector vecInfo = (Vector) lineInfs.get(num3);
            if (vec == null) {
                this.lineClrs[i4] = new int[0];
                this.lineStrs[i4] = new String[0];
                this.lineInfo[i4] = new String[0];
            } else {
                this.lineClrs[i4] = new int[vec.size()];
                this.lineStrs[i4] = new String[vec.size()];
                this.lineInfo[i4] = new String[vec.size()];
                for (int j = 0; j < vec.size(); j++) {
                    this.lineClrs[i4][j] = ((Integer) vec.elementAt(j)).intValue();
                    this.lineStrs[i4][j] = (String) vecStr.elementAt(j);
                    this.lineInfo[i4][j] = (String) vecInfo.elementAt(j);
                }
            }
            i4++;
        }
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public String getActiveInfo() {
        return this.activeInfo;
    }

    public String getWholeString() {
        String ret = "";
        for (String[] length : this.lineStrs) {
            for (int j = 0; j < length.length; j++) {
                ret = String.valueOf(ret) + this.lineStrs[i][j];
            }
        }
        return ret;
    }

    public void setActiveInfo(String activeInfo2) {
        this.activeInfo = activeInfo2;
    }

    public void nextActiveInfo() {
        boolean found = false;
        for (int i = 0; i < this.lineInfo.length; i++) {
            int j = 0;
            while (j < this.lineInfo[i].length) {
                if (this.activeInfo.equals("") || (!this.activeInfo.equals("") && this.lineInfo[i][j].equals(this.activeInfo))) {
                    found = true;
                }
                if (!found || this.lineInfo[i][j].equals("") || this.lineInfo[i][j].equals(this.activeInfo)) {
                    j++;
                } else {
                    this.activeInfo = this.lineInfo[i][j];
                    return;
                }
            }
        }
        this.activeInfo = "";
    }

    public int getWidth() {
        return this.width;
    }
}
