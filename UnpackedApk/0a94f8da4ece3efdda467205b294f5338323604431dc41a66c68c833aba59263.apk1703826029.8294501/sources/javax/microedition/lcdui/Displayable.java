package javax.microedition.lcdui;

import android.view.SurfaceView;
import com.sumsharp.monster.MonsterMIDlet;
import java.util.Vector;

public abstract class Displayable extends SurfaceView {
    private Vector commands = new Vector();
    Display currentDisplay = null;
    boolean fullScreenMode = false;
    private int height = -1;
    private CommandListener listener = null;
    Ticker ticker;
    private String title;
    Displayable ui;
    int viewPortHeight;
    int viewPortY;
    private int width = -1;

    /* access modifiers changed from: 0000 */
    public abstract void paint(Graphics graphics);

    Displayable(String title2) {
        super(MonsterMIDlet.instance);
        this.title = title2;
    }

    /* access modifiers changed from: 0000 */
    public void setUI(Displayable ui2) {
        this.ui = ui2;
    }

    public void addCommand(Command cmd) {
        int i = 0;
        while (i < this.commands.size()) {
            if (cmd != ((Command) this.commands.elementAt(i))) {
                i++;
            } else {
                return;
            }
        }
        boolean inserted = false;
        int i2 = 0;
        while (true) {
            if (i2 >= this.commands.size()) {
                break;
            } else if (cmd.getPriority() < ((Command) this.commands.elementAt(i2)).getPriority()) {
                this.commands.insertElementAt(cmd, i2);
                inserted = true;
                break;
            } else {
                i2++;
            }
        }
        if (!inserted) {
            this.commands.addElement(cmd);
        }
        this.ui.addCommandUI(cmd.ui);
        if (isShown()) {
            this.currentDisplay.updateCommands();
        }
    }

    private void addCommandUI(Command ui2) {
    }

    public void removeCommand(Command cmd) {
        this.commands.removeElement(cmd);
        this.ui.removeCommandUI(cmd.ui);
        if (isShown()) {
            this.currentDisplay.updateCommands();
        }
    }

    private void removeCommandUI(Command ui2) {
    }

    public boolean isShown() {
        if (this.currentDisplay == null) {
            return false;
        }
        return super.isShown();
    }

    public Ticker getTicker() {
        return this.ticker;
    }

    public void setTicker(Ticker ticker2) {
        this.ticker = ticker2;
        repaint();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String s) {
        this.title = s;
        this.ui.invalidate();
    }

    public void setCommandListener(CommandListener l) {
        this.listener = l;
        this.ui.setCommandListener(l);
    }

    /* access modifiers changed from: 0000 */
    public CommandListener getCommandListener() {
        return this.listener;
    }

    /* access modifiers changed from: 0000 */
    public Vector getCommands() {
        return this.commands;
    }

    /* access modifiers changed from: 0000 */
    public void hideNotify() {
    }

    /* access modifiers changed from: 0000 */
    public final void hideNotify(Display d) {
        this.ui.hideNotify();
        hideNotify();
    }

    /* access modifiers changed from: 0000 */
    public void keyPressed(int keyCode) {
    }

    /* access modifiers changed from: 0000 */
    public void keyRepeated(int keyCode) {
    }

    /* access modifiers changed from: 0000 */
    public void keyReleased(int keyCode) {
    }

    /* access modifiers changed from: 0000 */
    public void pointerPressed(int x, int y) {
    }

    /* access modifiers changed from: 0000 */
    public void pointerReleased(int x, int y) {
    }

    /* access modifiers changed from: 0000 */
    public void pointerDragged(int x, int y) {
    }

    /* access modifiers changed from: 0000 */
    public void repaint() {
        if (this.currentDisplay != null) {
            repaint(0, 0, getWidth(), getHeight());
        }
    }

    /* access modifiers changed from: 0000 */
    public void repaint(int x, int y, int width2, int height2) {
        if (this.currentDisplay != null) {
            this.currentDisplay.repaint(this, x, y, width2, height2);
        }
    }

    /* access modifiers changed from: protected */
    public void sizeChanged(int w, int h) {
    }

    /* access modifiers changed from: 0000 */
    public final void sizeChanged(Display d) {
        this.width = getWidth();
        this.height = getHeight();
        sizeChanged(this.width, this.height);
    }

    /* access modifiers changed from: 0000 */
    public void showNotify() {
    }

    /* access modifiers changed from: 0000 */
    public final void showNotify(Display d) {
        this.currentDisplay = d;
        this.viewPortY = 0;
        this.viewPortHeight = (getHeight() - new StringComponent(getTitle()).getHeight()) - 1;
        if (this.ticker != null) {
            this.viewPortHeight -= this.ticker.getHeight();
        }
        int w = getWidth();
        int h = getHeight();
        if (!(this.width == w && this.height == h)) {
            sizeChanged(d);
        }
        showNotify();
        this.ui.showNotify();
    }
}
