package javax.microedition.lcdui;

import com.sumsharp.monster.MonsterMIDlet;
import com.sumsharp.monster.common.data.AbstractUnit;

public class StringComponent {
    private int[] breaks;
    private boolean invertPaint;
    private int numOfBreaks;
    private String text;
    private int width;
    private int widthDecreaser;

    public StringComponent() {
        this(null);
    }

    public StringComponent(String text2) {
        this.breaks = new int[4];
        this.invertPaint = false;
        synchronized (this) {
            this.width = -1;
            this.widthDecreaser = 0;
            setText(text2);
        }
    }

    public int getCharHeight() {
        return Font.getDefaultFont().getHeight();
    }

    public int getCharPositionX(int num) {
        int substringWidth;
        synchronized (this) {
            if (this.numOfBreaks == -1) {
                updateBreaks();
            }
            int prevIndex = 0;
            Font f = Font.getDefaultFont();
            int i = 0;
            while (i < this.numOfBreaks && num >= this.breaks[i]) {
                prevIndex = this.breaks[i];
                i++;
            }
            substringWidth = f.substringWidth(this.text, prevIndex, num - prevIndex);
        }
        return substringWidth;
    }

    public int getCharPositionY(int num) {
        int y = 0;
        synchronized (this) {
            if (this.numOfBreaks == -1) {
                updateBreaks();
            }
            Font f = Font.getDefaultFont();
            int i = 0;
            while (i < this.numOfBreaks && num >= this.breaks[i]) {
                y += f.getHeight();
                i++;
            }
        }
        return y;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getHeight() {
        /*
            r5 = this;
            r4 = 1
            monitor-enter(r5)
            int r2 = r5.numOfBreaks     // Catch:{ all -> 0x001f }
            r3 = -1
            if (r2 != r3) goto L_0x000a
            r5.updateBreaks()     // Catch:{ all -> 0x001f }
        L_0x000a:
            javax.microedition.lcdui.Font r0 = javax.microedition.lcdui.Font.getDefaultFont()     // Catch:{ all -> 0x001f }
            java.lang.String r2 = r5.text     // Catch:{ all -> 0x001f }
            if (r2 != 0) goto L_0x0015
            monitor-exit(r5)     // Catch:{ all -> 0x001f }
            r2 = 0
        L_0x0014:
            return r2
        L_0x0015:
            int r2 = r5.numOfBreaks     // Catch:{ all -> 0x001f }
            if (r2 != 0) goto L_0x0022
            int r2 = r0.getHeight()     // Catch:{ all -> 0x001f }
            monitor-exit(r5)     // Catch:{ all -> 0x001f }
            goto L_0x0014
        L_0x001f:
            r2 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x001f }
            throw r2
        L_0x0022:
            int r2 = r5.numOfBreaks     // Catch:{ all -> 0x001f }
            int r3 = r0.getHeight()     // Catch:{ all -> 0x001f }
            int r1 = r2 * r3
            int[] r2 = r5.breaks     // Catch:{ all -> 0x001f }
            int r3 = r5.numOfBreaks     // Catch:{ all -> 0x001f }
            int r3 = r3 - r4
            r2 = r2[r3]     // Catch:{ all -> 0x001f }
            java.lang.String r3 = r5.text     // Catch:{ all -> 0x001f }
            int r3 = r3.length()     // Catch:{ all -> 0x001f }
            int r3 = r3 - r4
            if (r2 != r3) goto L_0x004b
            java.lang.String r2 = r5.text     // Catch:{ all -> 0x001f }
            java.lang.String r3 = r5.text     // Catch:{ all -> 0x001f }
            int r3 = r3.length()     // Catch:{ all -> 0x001f }
            int r3 = r3 - r4
            char r2 = r2.charAt(r3)     // Catch:{ all -> 0x001f }
            r3 = 10
            if (r2 == r3) goto L_0x0050
        L_0x004b:
            int r2 = r0.getHeight()     // Catch:{ all -> 0x001f }
            int r1 = r1 + r2
        L_0x0050:
            monitor-exit(r5)     // Catch:{ all -> 0x001f }
            r2 = r1
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.microedition.lcdui.StringComponent.getHeight():int");
    }

    public String getText() {
        return this.text;
    }

    public void invertPaint(boolean state) {
        synchronized (this) {
            this.invertPaint = state;
        }
    }

    public int paint(Graphics g) {
        int y;
        if (this.text == null) {
            return 0;
        }
        synchronized (this) {
            if (this.numOfBreaks == -1) {
                updateBreaks();
            }
            Font f = Font.getDefaultFont();
            y = 0;
            int prevIndex = 0;
            for (int i = 0; i < this.numOfBreaks; i++) {
                if (this.invertPaint) {
                    g.setGrayScale(0);
                } else {
                    g.setGrayScale(AbstractUnit.CLR_NAME_TAR);
                }
                g.fillRect(0, y, this.width, f.getHeight());
                if (this.invertPaint) {
                    g.setGrayScale(AbstractUnit.CLR_NAME_TAR);
                } else {
                    g.setGrayScale(0);
                }
                g.drawSubstring(this.text, prevIndex, this.breaks[i] - prevIndex, 0, y, 0);
                prevIndex = this.breaks[i];
                y += f.getHeight();
            }
            if (prevIndex != this.text.length()) {
                if (this.invertPaint) {
                    g.setGrayScale(0);
                } else {
                    g.setGrayScale(AbstractUnit.CLR_NAME_TAR);
                }
                g.fillRect(0, y, this.width, f.getHeight());
                if (this.invertPaint) {
                    g.setGrayScale(AbstractUnit.CLR_NAME_TAR);
                } else {
                    g.setGrayScale(0);
                }
                g.drawSubstring(this.text, prevIndex, this.text.length() - prevIndex, 0, y, 0);
                y += f.getHeight();
            }
        }
        return y;
    }

    public void setText(String text2) {
        synchronized (this) {
            this.text = text2;
            this.numOfBreaks = -1;
        }
    }

    public void setWidthDecreaser(int widthDecreaser2) {
        synchronized (this) {
            this.widthDecreaser = widthDecreaser2;
            this.numOfBreaks = -1;
        }
    }

    private void insertBreak(int pos) {
        int i = 0;
        while (i < this.numOfBreaks && pos >= this.breaks[i]) {
            i++;
        }
        if (this.numOfBreaks + 1 == this.breaks.length) {
            int[] newbreaks = new int[(this.breaks.length + 4)];
            System.arraycopy(this.breaks, 0, newbreaks, 0, this.numOfBreaks);
            this.breaks = newbreaks;
        }
        System.arraycopy(this.breaks, i, this.breaks, i + 1, this.numOfBreaks - i);
        this.breaks[i] = pos;
        this.numOfBreaks++;
    }

    private void updateBreaks() {
        if (this.text != null) {
            this.width = MonsterMIDlet.instance.getWindowManager().getDefaultDisplay().getWidth() - this.widthDecreaser;
            int prevIndex = 0;
            int canBreak = 0;
            this.numOfBreaks = 0;
            Font f = Font.getDefaultFont();
            int i = 0;
            while (i < this.text.length()) {
                if (this.text.charAt(i) == ' ') {
                    canBreak = i + 1;
                }
                if (this.text.charAt(i) == 10) {
                    insertBreak(i);
                    canBreak = 0;
                    prevIndex = i + 1;
                } else if (f.substringWidth(this.text, prevIndex, (i - prevIndex) + 1) > this.width) {
                    if (canBreak != 0) {
                        insertBreak(canBreak);
                        i = canBreak;
                        prevIndex = i;
                    } else {
                        insertBreak(i);
                        prevIndex = i + 1;
                    }
                    canBreak = 0;
                }
                i++;
            }
        }
    }
}
