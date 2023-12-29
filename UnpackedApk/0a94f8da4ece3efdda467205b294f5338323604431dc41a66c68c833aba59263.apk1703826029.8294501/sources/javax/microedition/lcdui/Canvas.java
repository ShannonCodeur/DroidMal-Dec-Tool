package javax.microedition.lcdui;

public abstract class Canvas extends Displayable {
    public static final int DOWN = 2;
    public static final int FIRE = 5;
    public static final int GAME_A = 9;
    public static final int GAME_B = 10;
    public static final int GAME_C = 11;
    public static final int GAME_D = 12;
    public static final int KEY_NUM0 = 48;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM2 = 50;
    public static final int KEY_NUM3 = 51;
    public static final int KEY_NUM4 = 52;
    public static final int KEY_NUM5 = 53;
    public static final int KEY_NUM6 = 54;
    public static final int KEY_NUM7 = 55;
    public static final int KEY_NUM8 = 56;
    public static final int KEY_NUM9 = 57;
    public static final int KEY_POUND = 35;
    public static final int KEY_STAR = 42;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int UP = 1;

    /* access modifiers changed from: protected */
    public abstract void paint(Graphics graphics);

    protected Canvas() {
        super(null);
    }

    public int getGameAction(int keyCode) {
        return Display.getGameAction(keyCode);
    }

    public int getKeyCode(int gameAction) {
        return Display.getKeyCode(gameAction);
    }

    public String getKeyName(int keyCode) throws IllegalArgumentException {
        return Display.getKeyName(keyCode);
    }

    /* access modifiers changed from: protected */
    public void hideNotify() {
    }

    public boolean isDoubleBuffered() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void keyPressed(int keyCode) {
    }

    /* access modifiers changed from: protected */
    public void keyRepeated(int keyCode) {
    }

    /* access modifiers changed from: protected */
    public void keyReleased(int keyCode) {
    }

    /* access modifiers changed from: protected */
    public void pointerPressed(int x, int y) {
    }

    /* access modifiers changed from: protected */
    public void pointerReleased(int x, int y) {
    }

    /* access modifiers changed from: protected */
    public void pointerDragged(int x, int y) {
    }

    public final void repaint() {
        super.repaint();
    }

    public final void repaint(int x, int y, int width, int height) {
        super.repaint(x, y, width, height);
    }

    public final void serviceRepaints() {
        if (this.currentDisplay != null) {
            this.currentDisplay.serviceRepaints();
        }
    }

    public void setFullScreenMode(boolean mode) {
        if (this.fullScreenMode != mode) {
            this.fullScreenMode = mode;
            if (this.currentDisplay != null) {
                sizeChanged(this.currentDisplay);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sizeChanged(int w, int h) {
    }

    /* access modifiers changed from: protected */
    public void showNotify() {
    }
}
