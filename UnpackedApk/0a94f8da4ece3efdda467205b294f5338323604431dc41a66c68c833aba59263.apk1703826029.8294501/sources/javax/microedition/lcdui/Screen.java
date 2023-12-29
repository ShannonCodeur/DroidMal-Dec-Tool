package javax.microedition.lcdui;

import com.sumsharp.monster.common.data.AbstractUnit;

public abstract class Screen extends Displayable {
    /* access modifiers changed from: 0000 */
    public abstract int paintContent(Graphics graphics);

    /* access modifiers changed from: 0000 */
    public abstract int traverse(int i, int i2, int i3);

    Screen(String title) {
        super(title);
    }

    /* access modifiers changed from: 0000 */
    public void scroll(int gameKeyCode) {
        this.viewPortY += traverse(gameKeyCode, this.viewPortY, this.viewPortY + this.viewPortHeight);
        repaint();
    }

    /* access modifiers changed from: 0000 */
    public void keyPressed(int keyCode) {
        int gameKeyCode = Display.getGameAction(keyCode);
        if (gameKeyCode == 1 || gameKeyCode == 2) {
            this.viewPortY += traverse(gameKeyCode, this.viewPortY, this.viewPortY + this.viewPortHeight);
            repaint();
        }
    }

    /* access modifiers changed from: 0000 */
    public void hideNotify() {
        super.hideNotify();
    }

    /* access modifiers changed from: 0000 */
    public void keyRepeated(int keyCode) {
        keyPressed(keyCode);
    }

    /* access modifiers changed from: 0000 */
    public final void paint(Graphics g) {
        int contentHeight = 0;
        if (this.viewPortY == 0) {
            this.currentDisplay.setScrollUp(false);
        } else {
            this.currentDisplay.setScrollUp(true);
        }
        g.setGrayScale(AbstractUnit.CLR_NAME_TAR);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setGrayScale(0);
        if (getTicker() != null) {
            contentHeight = 0 + getTicker().paintContent(g);
        }
        g.translate(0, contentHeight);
        int translatedY = contentHeight;
        StringComponent title = new StringComponent(getTitle());
        int contentHeight2 = contentHeight + title.paint(g);
        g.drawLine(0, title.getHeight(), getWidth(), title.getHeight());
        int contentHeight3 = contentHeight2 + 1;
        g.translate(0, contentHeight3 - translatedY);
        int translatedY2 = contentHeight3;
        g.setClip(0, 0, getWidth(), getHeight() - contentHeight3);
        g.translate(0, -this.viewPortY);
        int contentHeight4 = contentHeight3 + paintContent(g);
        g.translate(0, this.viewPortY);
        if (contentHeight4 - this.viewPortY > getHeight()) {
            this.currentDisplay.setScrollDown(true);
        } else {
            this.currentDisplay.setScrollDown(false);
        }
        g.translate(0, -translatedY2);
    }

    /* access modifiers changed from: 0000 */
    public void repaint() {
        super.repaint();
    }

    /* access modifiers changed from: 0000 */
    public void showNotify() {
        this.viewPortY = 0;
        super.showNotify();
    }
}
