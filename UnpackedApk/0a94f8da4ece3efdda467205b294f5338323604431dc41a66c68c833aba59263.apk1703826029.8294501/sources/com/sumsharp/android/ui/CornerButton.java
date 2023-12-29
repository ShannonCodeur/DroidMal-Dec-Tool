package com.sumsharp.android.ui;

import android.util.Log;
import com.sumsharp.monster.NewStage;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.NetPlayer;
import javax.microedition.lcdui.Graphics;

public class CornerButton {
    public static final String DEFAULT_LEFT = "";
    public static final String DEFAULT_RIGHT = "";
    private static final int SPLASH_TIME_MAX = 13;
    public static CornerButton instance = new CornerButton();
    private boolean[] btnstate = new boolean[2];
    public boolean canDraw = false;
    private boolean[] enable = {true, true};
    private int h;
    public boolean handled = false;
    private boolean isTwoSides;
    private String lcmd;
    private boolean needClearState = false;
    private String rcmd;
    private int splashtime;
    private int w;
    private int x;
    private int y;

    private CornerButton() {
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }

    public void init() {
        this.w = Tool.img_CornerBg.getWidth();
        this.h = Tool.img_CornerBg.getHeight();
        this.x = World.viewWidth - this.w;
        this.y = World.viewHeight - this.h;
        this.splashtime = -1;
    }

    public void setCmd(String left, String right) {
        this.lcmd = left;
        this.rcmd = right;
        if (this.lcmd == null) {
            this.lcmd = "";
        }
        if (this.rcmd == null) {
            this.rcmd = "";
        }
    }

    public void paintUICmd(Graphics g, String left, String right, boolean isTwoSides2) {
        this.lcmd = left;
        this.rcmd = right;
        if (this.lcmd == null) {
            this.lcmd = "";
        }
        if (this.rcmd == null) {
            this.rcmd = "关闭";
        }
        paint(g, isTwoSides2);
    }

    public void paint(Graphics g, boolean isTwoSides2) {
        if (this.canDraw) {
            g.setClip(0, this.y, World.viewWidth, this.h);
            drawBtn(g, isTwoSides2);
            this.canDraw = false;
        }
    }

    private void drawBtn(Graphics g, boolean isTwoSides2) {
        this.isTwoSides = isTwoSides2;
        if (isTwoSides2) {
            int tmpy = World.viewHeight - 25;
            if (!this.enable[0]) {
                g.drawImage(Tool.img_leftbtn2, 36, tmpy, 3);
            } else if (this.btnstate[0]) {
                g.drawImage(Tool.img_leftbtn1, 36, tmpy, 3);
            } else if (this.splashtime >= 0) {
                if ((this.splashtime / 3) % 2 == 0) {
                    g.drawImage(Tool.img_leftbtn3, 36, tmpy, 3);
                } else {
                    g.drawImage(Tool.img_leftbtn0, 36, tmpy, 3);
                }
                if (this.splashtime > 0) {
                    this.splashtime--;
                }
            } else {
                g.drawImage(Tool.img_leftbtn0, 36, tmpy, 3);
            }
            drawFont(g, this.lcmd, isTwoSides2, true);
            int tmpx = (this.x + this.w) - 36;
            int tmpy2 = (this.y + this.h) - 25;
            if (this.btnstate[1]) {
                g.drawImage(Tool.img_rightbtn1, tmpx, tmpy2, 3);
            } else {
                g.drawImage(Tool.img_rightbtn0, tmpx, tmpy2, 3);
            }
            drawFont(g, this.rcmd, isTwoSides2, false);
            return;
        }
        int tmpx2 = this.x + 40;
        int tmpy3 = this.y + 27;
        if (!this.enable[0]) {
            g.drawImage(Tool.img_rightbtn2, tmpx2, tmpy3, 3);
        } else if (this.btnstate[0]) {
            g.drawImage(Tool.img_rightbtn1, tmpx2, tmpy3, 3);
        } else if (this.splashtime >= 0) {
            if ((this.splashtime / 3) % 2 == 0) {
                g.drawImage(Tool.img_rightbtn3, tmpx2, tmpy3, 3);
            } else {
                g.drawImage(Tool.img_rightbtn0, tmpx2, tmpy3, 3);
            }
            if (this.splashtime > 0) {
                this.splashtime--;
            }
        } else {
            g.drawImage(Tool.img_rightbtn0, tmpx2, tmpy3, 3);
        }
        drawFont(g, this.lcmd, isTwoSides2, true);
        int tmpx3 = (this.x + this.w) - 36;
        int tmpy4 = (this.y + this.h) - 25;
        if (this.btnstate[1]) {
            g.drawImage(Tool.img_rightbtn1, tmpx3, tmpy4, 3);
        } else {
            g.drawImage(Tool.img_rightbtn0, tmpx3, tmpy4, 3);
        }
        drawFont(g, this.rcmd, isTwoSides2, false);
    }

    private void drawFont(Graphics g, String cmd, boolean isTwoSides2, boolean isfirst) {
        int midx;
        int midy;
        int i;
        int i2;
        int i3;
        int i4;
        if (!isfirst) {
            midx = (this.x + this.w) - 36;
            midy = (this.y + this.h) - 25;
        } else if (isTwoSides2) {
            midx = 36;
            midy = (this.y + this.h) - 25;
        } else {
            midx = this.x + 40;
            midy = this.y + 25;
        }
        char[] chs = cmd.toCharArray();
        if (!isfirst || this.enable[0]) {
            if (!isTwoSides2 || !isfirst) {
                boolean btnst = this.btnstate[0];
                if (!isfirst) {
                    btnst = this.btnstate[1];
                }
                if (chs.length == 2) {
                    String valueOf = String.valueOf(chs[0]);
                    int i5 = midx + 2;
                    int i6 = midy - 4;
                    if (btnst) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    Tool.drawLevelString(g, valueOf, i5, i6, 24, 2, i2);
                    Tool.drawLevelString(g, String.valueOf(chs[1]), midx, midy, 36, 2, btnst ? 1 : 0);
                } else if (chs.length == 4) {
                    Tool.drawLevelString(g, String.valueOf(chs[1]), midx, midy, 24, 2, btnst ? 1 : 0);
                    Tool.drawLevelString(g, String.valueOf(chs[2]), midx, midy, 36, 2, btnst ? 1 : 0);
                    int off = Utilities.font.getHeight();
                    String valueOf2 = String.valueOf(chs[0]);
                    int i7 = midx - off;
                    int i8 = midy + off;
                    if (btnst) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    Tool.drawLevelString(g, valueOf2, i7, i8, 24, 2, i);
                    Tool.drawLevelString(g, String.valueOf(chs[3]), midx + off, midy - off, 36, 2, btnst ? 1 : 0);
                } else if (chs.length == 3) {
                    Tool.drawLevelString(g, String.valueOf(chs[1]), midx, midy - 6, 17, 2, btnst ? 1 : 0);
                    Tool.drawLevelString(g, String.valueOf(chs[2]), midx, midy - 2, 36, 2, btnst ? 1 : 0);
                    Tool.drawLevelString(g, String.valueOf(chs[0]), midx - 8, midy, 24, 2, btnst ? 1 : 0);
                }
            } else if (chs.length == 2) {
                Tool.drawLevelString(g, String.valueOf(chs[0]), midx, midy, 40, 2, this.btnstate[0] ? 1 : 0);
                String valueOf3 = String.valueOf(chs[1]);
                int i9 = midy - 4;
                if (this.btnstate[0]) {
                    i4 = 1;
                } else {
                    i4 = 0;
                }
                Tool.drawLevelString(g, valueOf3, midx, i9, 20, 2, i4);
            } else if (chs.length == 4) {
                Tool.drawLevelString(g, String.valueOf(chs[1]), midx, midy, 40, 2, this.btnstate[0] ? 1 : 0);
                String valueOf4 = String.valueOf(chs[2]);
                if (this.btnstate[0]) {
                    i3 = 1;
                } else {
                    i3 = 0;
                }
                Tool.drawLevelString(g, valueOf4, midx, midy, 20, 2, i3);
                int off2 = Utilities.font.getHeight();
                Tool.drawLevelString(g, String.valueOf(chs[0]), midx - off2, midy - off2, 40, 2, this.btnstate[0] ? 1 : 0);
                Tool.drawLevelString(g, String.valueOf(chs[3]), midx + off2, midy + off2, 20, 2, this.btnstate[0] ? 1 : 0);
            } else if (chs.length == 3) {
                int off3 = Utilities.font.getWidth();
                Tool.drawLevelString(g, String.valueOf(chs[1]), midx + 10, midy - 10, 33, 2, this.btnstate[0] ? 1 : 0);
                Tool.drawLevelString(g, String.valueOf(chs[2]), midx + 10, midy - 10, 20, 2, this.btnstate[0] ? 1 : 0);
                Tool.drawLevelString(g, String.valueOf(chs[0]), (midx - off3) + 15, (midy - off3) - 15, 24, 2, this.btnstate[0] ? 1 : 0);
            }
        } else if (isTwoSides2) {
            Tool.drawLevelString(g, "菜", midx, midy, 40, 2, 2);
            Tool.drawLevelString(g, "单", midx, midy - 4, 20, 2, 2);
        } else {
            Tool.drawLevelString(g, "菜", midx + 2, midy - 4, 24, 2, 2);
            Tool.drawLevelString(g, "单", midx, midy, 36, 2, 2);
        }
    }

    private boolean handlePress() {
        if (!(World.pressedx == -1 || World.pressedy == -1)) {
            Log.d("press", "press");
            if (this.isTwoSides) {
                if (World.pressedy > (World.viewHeight - 3) - Tool.img_leftbtn0.getHeight() && World.pressedy < World.viewHeight - 3) {
                    if (World.pressedx <= 3 || World.pressedx >= Tool.img_leftbtn0.getWidth() + 3) {
                        if (World.pressedx < World.viewWidth - 3 && World.pressedx > (World.viewWidth - 3) - Tool.img_rightbtn0.getWidth()) {
                            this.btnstate[1] = true;
                        }
                    } else if (this.enable[0]) {
                        this.btnstate[0] = true;
                    }
                }
            } else if (World.pressedx > this.x && World.pressedx < this.x + this.w && World.pressedy > this.y && World.pressedy < this.y + this.h) {
                if (World.viewHeight - World.pressedy <= World.pressedx - this.x) {
                    this.btnstate[1] = true;
                } else if (this.enable[0]) {
                    this.btnstate[0] = true;
                }
                World.pressedx = -1;
                World.pressedy = -1;
            }
        }
        return false;
    }

    private boolean handleRelease() {
        if (this.btnstate[0] || this.btnstate[1]) {
            if (World.releasedx == -1 && World.releasedy == -1) {
                return false;
            }
            Log.d("release", "release");
            if (this.btnstate[0]) {
                this.btnstate[0] = false;
            }
            if (this.btnstate[1]) {
                this.btnstate[1] = false;
            }
            int px = World.releasedx;
            int py = World.releasedy;
            if (this.isTwoSides) {
                if (py > (World.viewHeight - 3) - Tool.img_leftbtn0.getHeight() && py < World.viewHeight - 3) {
                    if (px > 3 && px < Tool.img_leftbtn0.getWidth() + 3) {
                        if (this.lcmd.equals("对话") || this.lcmd.startsWith("查看")) {
                            Utilities.keyPressed(5, true);
                        } else {
                            Utilities.keyPressed(6, true);
                        }
                        if (this.splashtime != -1) {
                            this.splashtime = -1;
                        }
                        return true;
                    } else if (px < World.viewWidth - 3 && px > (World.viewWidth - 3) - Tool.img_rightbtn0.getWidth()) {
                        Utilities.keyPressed(7, true);
                        return true;
                    }
                }
            } else if (px > this.x && px < this.x + this.w && py > this.y && py < this.y + this.h) {
                if (World.viewHeight - py > px - this.x) {
                    if (this.lcmd.trim().equals("对话") || this.lcmd.trim().startsWith("查看")) {
                        Utilities.keyPressed(5, true);
                    } else {
                        Utilities.keyPressed(6, true);
                    }
                    if (this.splashtime != -1) {
                        this.splashtime = -1;
                    }
                } else {
                    Utilities.keyPressed(7, true);
                }
                return true;
            }
        }
        return false;
    }

    public void cycle() {
        if (!this.handled) {
            if (this.needClearState) {
                if (!this.lcmd.equals("对话") && !this.lcmd.equals("查看")) {
                    if (this.btnstate[0]) {
                        this.btnstate[0] = false;
                        Utilities.keyPressed(6, true);
                    }
                    if (this.btnstate[1]) {
                        this.btnstate[1] = false;
                        Utilities.keyPressed(7, true);
                    }
                    this.needClearState = false;
                }
            } else if (!(World.moveX == -1 || World.moveY == -1 || (!this.btnstate[0] && !this.btnstate[1]))) {
                this.needClearState = true;
            }
            if (CommonComponent.getUIPanel().hasUI(Utilities.VMUI_GAMEMENU)) {
                instance.setCmd(null, "关闭");
            } else if (!World.instance.isOnTop || World.fullChatUI != null) {
                if (this.splashtime >= 0) {
                    this.splashtime = -1;
                }
            } else if (CommonData.player.inBattle) {
                instance.setCmd("确定", "返回");
                if (this.splashtime != -1) {
                    this.splashtime = -1;
                }
            } else if (NewStage.touchNpc != null) {
                instance.setCmd("对话", "返回");
                if (this.splashtime == -1) {
                    this.splashtime = 13;
                }
            } else if (CommonData.player.targetPlayer == null) {
                instance.setCmd("菜单", "返回");
                if (this.splashtime >= 0) {
                    this.splashtime = -1;
                }
            } else if (CommonData.player.targetPlayer instanceof NetPlayer) {
                instance.setCmd("查看", "返回");
                if (this.splashtime == -1) {
                    this.splashtime = 13;
                }
            }
            if (this.lcmd.trim().equals("")) {
                this.enable[0] = false;
            } else {
                this.enable[0] = true;
            }
            handlePress();
            handleRelease();
            this.handled = true;
        }
    }
}
