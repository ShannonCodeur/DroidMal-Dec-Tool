package com.sumsharp.monster;

import android.util.Log;
import com.sumsharp.android.ui.ChatUI;
import com.sumsharp.lowui.UI;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.CommonProcessor;
import com.sumsharp.monster.common.Tool;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.AbstractUnit;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.common.data.Door;
import com.sumsharp.monster.common.data.Monster;
import com.sumsharp.monster.common.data.NetPlayer;
import com.sumsharp.monster.common.data.Npc;
import com.sumsharp.monster.common.data.Pet;
import com.sumsharp.monster.common.data.Player;
import com.sumsharp.monster.common.data.SearchRoad;
import com.sumsharp.monster.image.ImageLoadManager;
import com.sumsharp.monster.image.PipImage;
import com.sumsharp.monster.net.UWAPSegment;
import dalvik.system.VMRuntime;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

public class NewStage {
    public static final String DISPLAY_QUANLITY_DBNAME = "MONSTER_DISPLAY_QUANLIT";
    public static final int DISPLAY_QUANLITY_HIGH = 0;
    public static final int DISPLAY_QUANLITY_LOW = 2;
    public static final int DISPLAY_QUANLITY_MEDIUM = 1;
    public static final String[] DISPLAY_QUANLITY_TEXT = {"高", "中", "低"};
    private static final byte DRAW_BATTLE_ARMY = 9;
    private static final byte DRAW_BATTLE_PET = 8;
    private static final byte DRAW_ITEMS_DOOR = 5;
    private static final byte DRAW_ITEMS_FIELD_PET = 13;
    private static final byte DRAW_ITEMS_MONSTERICON = 3;
    private static final byte DRAW_ITEMS_NET_PET = 12;
    private static final byte DRAW_ITEMS_NPC = 2;
    private static final byte DRAW_ITEMS_PLAYERPET = 11;
    private static final byte DRAW_ITEMS_RESOURCE = 4;
    private static final byte DRAW_ITEMS_TILE = 10;
    private static final byte DRAW_ITMES_MAPNPC = 1;
    private static final byte DRAW_ITMES_PLAYER = 0;
    private static final byte DRAW_NETPLAYER = 6;
    private static final byte DRAW_TEAMMEMBER = 7;
    public static int MINIMAP_H = 33;
    public static int MINIMAP_W = 120;
    public static int MINIMAP_X = 138;
    public static int MINIMAP_Y = -6;
    public static final String NETWORKS_SETTING = "MONSTER_NETWORK_SETTING";
    public static String NPC_TIPMSG_NAME = "NPC_TIP";
    public static final String OTHER_SETTING_DBNAME = "MONSTER_OTHER_SETTING";
    public static final int TILE_HEIGHT = 16;
    public static final int TILE_WIDTH = 16;
    private static Hashtable _packageCache = new Hashtable();
    public static byte[] alphaLine = new byte[16];
    public static short areaId = -1;
    public static int backImgX = 0;
    public static Image bgBuffer = null;
    public static int bgHeight = 0;
    public static short[] bgMapData = null;
    public static int bgTop = 0;
    public static int bgWidth = 0;
    public static int[] collection = null;
    public static int currentMapId = 0;
    public static int displayQuanlity = 1;
    public static Door[] doors = null;
    public static Pet[] fieldPets = null;
    public static int hasNewMail = 0;
    public static boolean isMapLoadOk = false;
    public static int lastViewX = -1;
    public static int loadProgress = 0;
    public static Image mapBuffer = null;
    public static short[][] mapData = null;
    private static short[][] mapDoor = null;
    public static int mapDrawHeight = 0;
    public static int mapDrawWidth = 0;
    public static int mapDrawX = 0;
    public static int mapDrawY = 0;
    public static int mapHeight = 0;
    public static String mapName = null;
    public static int mapNameColor = 0;
    public static String mapPrefix = null;
    public static short mapSplitX = 0;
    public static short mapSplitY = 0;
    public static int mapWidth = 0;
    public static int miniMapWidth = 0;
    public static int miniMapX = 0;
    public static Monster[] monsters = null;
    private static int moveMapAdd = 0;
    public static String msg = "";
    public static Vector netPlayers = new Vector();
    public static Npc[] npcs = null;
    public static int oldMapDrawX = 0;
    public static int oldMapDrawY = 0;
    public static int oldMapEndX = 0;
    public static int oldMapEndY = 0;
    public static boolean playerLogined = false;
    public static int screenY = 0;
    public static boolean scriptMoveMap = false;
    public static final int scrollSpeed = 4;
    public static SearchRoad search;
    public static byte[] setting;
    private static int[] sortTable = {1, 4, 10, 23, 57, 132, HttpConnection.HTTP_MOVED_PERM, 701, 1577, 3548, 7983, 17961, 40412, 90927, 204585, 460316, 1035711, 2330349};
    public static int titleAniFrame = 0;
    public static PipImage titleImg = null;
    public static Door touchDoor = null;
    public static Npc touchNpc = null;
    public static int useFileConnection = 0;
    private static short viewMaxX = -1;
    private static short viewMaxY = -1;
    public static short viewX;
    public static short viewY;
    private static short[] yOrder = null;
    public static int yOrderCount = 0;

    public static void initDefaultImage() throws IOException {
    }

    public static void loadMap(int id) throws IOException {
        viewX = 0;
        viewY = 0;
        isMapLoadOk = false;
        if (search != null) {
            search = null;
        }
        netPlayers.removeAllElements();
        CommonData.team.clearWpinfo();
        fieldPets = null;
        mapBuffer = null;
        bgBuffer = null;
        mapSplitY = 0;
        mapSplitX = 0;
        CommonData.player.visible = true;
        DataInputStream in = new DataInputStream(getPackageFileAsStream("map_" + id + ".m"));
        currentMapId = id | (areaId << 4);
        mapName = in.readUTF();
        mapPrefix = "";
        mapNameColor = 0;
        mapWidth = in.readShort();
        mapHeight = in.readShort();
        miniMapWidth = (mapWidth * 16) / 10;
        if (miniMapWidth <= MINIMAP_W) {
            miniMapX = (MINIMAP_W - miniMapWidth) / 2;
        } else {
            miniMapX = 0;
        }
        bgTop = in.readByte();
        bgWidth = in.readByte();
        bgHeight = (in.readByte() - bgTop) + 1;
        incLoadProgress(97);
        mapDrawWidth = World.viewWidth / 16;
        mapDrawHeight = World.viewHeight / 16;
        if (mapDrawHeight > 15) {
            mapDrawHeight = 15;
        }
        oldMapDrawX = 0;
        mapDrawX = 0;
        oldMapDrawY = 0;
        mapDrawY = 0;
        mapData = new short[(mapWidth * mapHeight)][];
        if (bgHeight * bgWidth <= 0) {
            bgMapData = new short[0];
            bgHeight = 0;
            bgWidth = 0;
        } else {
            bgMapData = new short[(bgHeight * bgWidth)];
        }
        in.readInt();
        int y = 0;
        while (true) {
            int y2 = y;
            if (y2 >= mapHeight) {
                break;
            }
            int x = 0;
            while (true) {
                int x2 = x;
                if (x2 >= mapWidth) {
                    break;
                }
                int idx = (mapWidth * y2) + x2;
                int layers = in.readByte();
                if (layers == 0) {
                    mapData[idx] = new short[0];
                } else {
                    Vector v = new Vector();
                    for (int i = 0; i < layers; i++) {
                        short tile = in.readShort();
                        if (isBgTile(tile)) {
                            bgMapData[(bgWidth * y2) + x2] = (short) getTileId(tile);
                        } else {
                            v.addElement(new Integer(tile));
                        }
                    }
                    mapData[idx] = new short[v.size()];
                    for (int i2 = 0; i2 < v.size(); i2++) {
                        mapData[idx][i2] = ((Integer) v.elementAt(i2)).shortValue();
                    }
                }
                x = x2 + 1;
            }
            incLoadProgress(97);
            y = y2 + 1;
        }
        short colCount = in.readShort();
        collection = new int[colCount];
        for (int i3 = 0; i3 < colCount; i3++) {
            collection[i3] = in.readInt();
        }
        search = new SearchRoad(collection);
        if (CommonData.player.getState() == 1 && CommonData.player.keepMoving != -1) {
            CommonData.player.keepMoving = -1;
            CommonData.player.pressTime = -1;
            CommonData.player.setState(0);
        }
        incLoadProgress(99);
        byte npcCount = in.readByte();
        npcs = new Npc[npcCount];
        for (int i4 = 0; i4 < npcCount; i4++) {
            npcs[i4] = new Npc();
            npcs[i4].load(in);
        }
        byte doorCount = in.readByte();
        doors = new Door[doorCount];
        for (int i5 = 0; i5 < doorCount; i5++) {
            doors[i5] = new Door();
            doors[i5].load(in);
        }
        yOrder = null;
        yOrderCount = 0;
        viewMaxX = -1;
        viewMaxY = -1;
        moveMap();
        yOrderCount = (npcs.length * 8) + 264 + (doors.length * 4) + (fieldPets == null ? 0 : fieldPets.length * 4);
        yOrder = new short[yOrderCount];
        setLoadProgress(100, 100);
        isMapLoadOk = true;
        Log.d("LoadMap", "gcSoftReferences()");
        VMRuntime.getRuntime().gcSoftReferences();
    }

    public static int getTileId(short tileInfo) {
        return tileInfo & 511;
    }

    public static int getTileHeight(short tileInfo) {
        return (tileInfo >> 9) & 15;
    }

    public static boolean isBgTile(int tileInfo) {
        return (tileInfo & 8192) != 0;
    }

    public static PipImage getTileImg() {
        return (PipImage) _packageCache.get("tile.pip");
    }

    public static void drawMiniMap(Graphics g) {
        int miniMapOffX;
        int y;
        int y2;
        int clr;
        int clr2;
        int x = MINIMAP_X - 4;
        int playerx = (CommonData.player.pixelX + (CommonData.player.getWidth() / 2)) / 10;
        int playery = CommonData.player.pixelY / 10;
        g.setClip(MINIMAP_X - 1, 3, MINIMAP_W + 2, MINIMAP_H + 2);
        int miniMapOffX2 = 0;
        if (miniMapWidth > MINIMAP_W) {
            if (playerx > (MINIMAP_W >> 1)) {
                miniMapOffX2 = (MINIMAP_W >> 1) - playerx;
            }
            if (miniMapWidth + miniMapOffX2 < MINIMAP_W) {
                miniMapOffX = MINIMAP_W - miniMapWidth;
            } else {
                miniMapOffX = miniMapOffX2;
            }
        } else {
            miniMapOffX = miniMapX;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (npcs == null || i2 >= npcs.length) {
                int i3 = 0;
            } else {
                int npcx = (npcs[i2].pixelX + (npcs[i2].getWidth() / 2)) / 10;
                int npcy = npcs[i2].pixelY / 10;
                if ((npcs[i2].npcType & 1) != 0 && (npcs[i2].npcType & 2) == 0) {
                    if (npcs[i2].taskState >= 0) {
                        int f = 0;
                        switch (npcs[i2].taskState) {
                            case 1:
                                f = 78;
                                break;
                            case 2:
                                f = 80;
                                break;
                            case 3:
                                f = 79;
                                break;
                        }
                        Tool.uiMiscImg.drawFrame(g, f, MINIMAP_X + miniMapOffX + npcx, npcy + 3, 0, 3);
                    } else {
                        if (npcs[i2].isFriend(CommonData.player)) {
                            clr2 = 16759296;
                        } else {
                            clr2 = AbstractUnit.CLR_NAME_TAR_RED;
                        }
                        drawMiniMapIcon(g, npcx + MINIMAP_X + miniMapOffX, 3 + npcy, clr2);
                    }
                }
                i = i2 + 1;
            }
        }
        int i32 = 0;
        while (doors != null && i32 < doors.length) {
            int dx = (doors[i32].x * 16) / 10;
            int dy = ((doors[i32].y * 16) / 10) - 1;
            int dx2 = dx + MINIMAP_X + miniMapOffX;
            int i4 = 3 + 1;
            int dy2 = dy + 4;
            if (dx2 < 0) {
                dx2 = 0;
            }
            Tool.uiMiscImg.drawFrame(g, 81, dx2, dy2, 0, 3);
            i32++;
        }
        int i5 = 0;
        int y3 = 0;
        int x2 = x;
        while (i5 < netPlayers.size()) {
            AbstractUnit au = (AbstractUnit) netPlayers.elementAt(i5);
            if (au.mapId == CommonData.player.mapId) {
                int x3 = (au.pixelX + (au.getWidth() / 2)) / 10;
                int y4 = au.pixelY / 10;
                if (!CommonData.team.isTeamMode() || CommonData.team.getMember(au.id) == null) {
                    clr = 252;
                } else {
                    clr = 52224;
                }
                drawMiniMapIcon(g, MINIMAP_X + miniMapOffX + x3, 3 + y4, clr);
                x2 = x3;
                y3 = y4;
            }
            i5++;
        }
        if (fieldPets != null) {
            int x4 = y3;
            int i6 = x2;
            int i7 = 0;
            while (i7 < fieldPets.length) {
                if (!fieldPets[i7].visible) {
                    y2 = x4;
                    y = i6;
                } else {
                    AbstractUnit au2 = fieldPets[i7];
                    y = (au2.pixelX + (au2.getWidth() / 2)) / 10;
                    int y5 = au2.pixelY / 10;
                    drawMiniMapIcon(g, MINIMAP_X + miniMapOffX + y, 3 + y5, 252);
                    y2 = y5;
                }
                i7++;
                i6 = y;
                x4 = y2;
            }
            int i8 = i7;
            int x5 = i8;
        } else {
            int i9 = i5;
            int i10 = y3;
        }
        int px = MINIMAP_X + miniMapOffX + playerx;
        int off = (World.tick / 5) % 2;
        g.setColor(Tool.CLR_ITEM_WHITE);
        g.fillRect((px - 1) - off, ((3 + playery) - 1) - off, (off * 2) + 2, (off * 2) + 2);
        g.setClip(0, 0, World.viewWidth, World.viewHeight);
        String pt = String.valueOf(CommonData.player.tileX + 1) + " " + (CommonData.player.tileY + 1);
        int ptw = Tool.getSmallNumWidth(pt) + 8;
        int ptx = World.viewWidth - ptw;
        int dpty = MINIMAP_H + 3 + Utilities.LINE_HEIGHT + 8;
        String mapName2 = mapName;
        if (CommonData.player.mirrorId > 0) {
            mapName2 = String.valueOf(mapName2) + "第" + CommonData.player.mirrorId + "层";
        }
        if (mapPrefix != null && mapPrefix.length() > 0) {
            mapName2 = String.valueOf(mapPrefix) + " " + mapName2;
        }
        if (mapName2 != null) {
            UI.drawSmallTip(g, (World.viewWidth - Utilities.font.stringWidth(mapName2)) - 2, MINIMAP_H + 3 + Utilities.LINE_HEIGHT, Utilities.font.stringWidth(mapName2) + 5, 6, 0);
            int mapClr = Tool.CLR_ITEM_WHITE;
            if (mapNameColor != 0) {
                mapClr = mapNameColor;
            }
            Tool.draw3DString(g, mapName2, World.viewWidth - 3, MINIMAP_H + 3 + 4 + Utilities.LINE_HEIGHT, mapClr, 0, 40);
            UI.drawSmallTip(g, ptx + 8, dpty + 5, ptw, 6, 0);
            Tool.drawNumStr(pt, g, World.viewWidth - 1, dpty + 2, 0, 24, -1);
        }
    }

    public static void drawMiniMapIcon(Graphics g, int x, int y, int color) {
        g.setColor(color);
        g.fillRect(x - 1, y - 1, 2, 2);
    }

    public static void drawMap(Graphics g) {
        calculateMapLocation();
        if (mapBuffer == null) {
            int[] map = new int[(mapWidth * 16 * mapHeight * 16)];
            drawMapCell(map, 0, 0, mapWidth, mapHeight);
            mapBuffer = Image.createRGBImage(map, mapWidth * 16, mapHeight * 16, true);
            oldMapDrawX = mapDrawX;
            oldMapDrawY = mapDrawY;
            oldMapEndX = mapDrawX + mapDrawWidth;
            oldMapEndY = mapDrawY + mapDrawHeight;
        }
        if (bgBuffer == null && bgMapData.length > 0) {
            PipImage tile = getTileImg();
            bgBuffer = Image.createImage(bgWidth * 16, bgHeight * 16);
            Graphics gg = bgBuffer.getGraphics();
            for (int y = 0; y < bgHeight; y++) {
                for (int x = 0; x < bgWidth; x++) {
                    tile.draw(gg, bgMapData[(bgWidth * y) + x], x * 16, y * 16, 0);
                }
            }
        }
        for (int i = backImgX; i < World.viewWidth && bgMapData.length > 0; i += bgWidth * 16) {
            g.drawImage(bgBuffer, i, ((bgTop * 16) - getMapDrawY()) + (viewY >> 1), 20);
        }
        for (int i2 = backImgX; i2 > 0 && bgMapData.length > 0; i2 -= bgWidth * 16) {
            g.drawImage(bgBuffer, i2, ((bgTop * 16) - getMapDrawY()) + (viewY >> 1), 24);
        }
        g.setColor(0);
        g.fillRect(0, 0, -viewX, World.viewHeight);
        g.fillRect((-viewX) + (mapWidth * 16), 0, World.viewWidth, World.viewHeight);
        g.drawImage(mapBuffer, -viewX, (-viewY) + screenY, 20);
    }

    public static void drawMapNoBuffer(Graphics g) {
    }

    private static int[] mergeTile(int[] t1, int[] t2) {
        int[] ret = new int[(t1.length + t2.length)];
        System.arraycopy(t1, 0, ret, 0, t1.length);
        int count = t1.length;
        for (int i = t1.length; i < t1.length + t2.length; i++) {
            boolean found = false;
            for (int j = 0; j < t1.length; j++) {
                if (ret[j] == t2[i - t1.length]) {
                    found = true;
                }
            }
            if (!found) {
                ret[count] = t2[i - t1.length];
                count++;
            }
        }
        int[] newret = new int[count];
        System.arraycopy(ret, 0, newret, 0, count);
        return newret;
    }

    public static void createYOrder() {
        int startX;
        int startY;
        int endX;
        int endY;
        int endX2;
        int i;
        int yOrderPoint;
        int[] redrawTiles;
        int[] redraw;
        int count;
        int yOrderPoint2;
        int[] redrawTiles2;
        int[] redrawTiles3;
        int draw;
        int[] redrawTiles4;
        int[] redrawTiles5;
        int draw2;
        int yOrderPoint3;
        int count2 = 0;
        int v2 = viewX / 16;
        if (0 > v2) {
            startX = 0;
        } else {
            startX = v2;
        }
        int v22 = getMapDrawY() / 16;
        if (0 > v22) {
            startY = 0;
        } else {
            startY = v22;
        }
        int v1 = mapWidth;
        int v23 = (((short) (viewX + World.viewWidth)) / 16) + 1;
        if (v1 < v23) {
            endX = v1;
        } else {
            endX = v23;
        }
        int v12 = mapHeight;
        int v24 = (((short) (getMapDrawY() + World.viewHeight)) / 16) + 1;
        if (v12 < v24) {
            endY = v12;
        } else {
            endY = v24;
        }
        int[] redrawTiles6 = new int[0];
        if (Battle.battleID == -1) {
            int yOrderPoint4 = 0 + 1;
            yOrder[0] = 0;
            int yOrderPoint5 = yOrderPoint4 + 1;
            yOrder[yOrderPoint4] = 0;
            int yOrderPoint6 = yOrderPoint5 + 1;
            yOrder[yOrderPoint5] = CommonData.player.pixelY;
            int yOrderPoint7 = yOrderPoint6 + 1;
            yOrder[yOrderPoint6] = 0;
            int count3 = 0 + 1;
            int[] redraw2 = CommonData.player.getRedrawTiles();
            int[] redrawTiles7 = mergeTile(redrawTiles6, redraw2);
            if (CommonData.player.getFollowPet(false) != null) {
                int yOrderPoint8 = yOrderPoint7 + 1;
                yOrder[yOrderPoint7] = 11;
                int yOrderPoint9 = yOrderPoint8 + 1;
                yOrder[yOrderPoint8] = 0;
                int yOrderPoint10 = yOrderPoint9 + 1;
                yOrder[yOrderPoint9] = CommonData.player.getFollowPet().pixelY;
                yOrderPoint7 = yOrderPoint10 + 1;
                yOrder[yOrderPoint10] = 0;
                count3++;
                redraw2 = CommonData.player.getFollowPet().getRedrawTiles();
                redrawTiles7 = mergeTile(redrawTiles7, redraw2);
                if (!CommonData.player.getFollowPet().imageUpdate) {
                    ImageLoadManager.requestImage(CommonData.player.getFollowPet().getIconID(), CommonData.player.getFollowPet());
                }
            }
            int yOrderPoint11 = yOrderPoint7;
            int[] redrawTiles8 = redrawTiles7;
            int[] redraw3 = redraw2;
            for (int i2 = 0; i2 < npcs.length; i2++) {
                if (npcs[i2] != null && npcs[i2].tileX >= startX && npcs[i2].tileX < endX + 1 && npcs[i2].tileY >= startY && npcs[i2].tileY < endY + 2 && npcs[i2].visible) {
                    count3++;
                    int yOrderPoint12 = yOrderPoint11 + 1;
                    yOrder[yOrderPoint11] = 2;
                    int yOrderPoint13 = yOrderPoint12 + 1;
                    yOrder[yOrderPoint12] = (short) i2;
                    int yOrderPoint14 = yOrderPoint13 + 1;
                    yOrder[yOrderPoint13] = npcs[i2].pixelY;
                    yOrderPoint11 = yOrderPoint14 + 1;
                    yOrder[yOrderPoint14] = 0;
                    if (!npcs[i2].imageUpdate) {
                        if (npcs[i2].camp == CommonData.player.camp && setting[0] == 0) {
                            ImageLoadManager.requestImage(npcs[i2].getIconID(), npcs[i2]);
                        } else if (npcs[i2].camp != CommonData.player.camp && setting[1] == 0) {
                            ImageLoadManager.requestImage(npcs[i2].getIconID(), npcs[i2]);
                        }
                    }
                    redraw3 = npcs[i2].getRedrawTiles();
                    redrawTiles8 = mergeTile(redrawTiles8, redraw3);
                }
            }
            if (fieldPets != null) {
                for (int i3 = 0; i3 < fieldPets.length; i3++) {
                    if (fieldPets[i3] != null && fieldPets[i3].tileX >= startX && fieldPets[i3].tileX < endX && fieldPets[i3].tileY >= startY && fieldPets[i3].tileY < endY && fieldPets[i3].visible) {
                        count3++;
                        int yOrderPoint15 = yOrderPoint11 + 1;
                        yOrder[yOrderPoint11] = 13;
                        int yOrderPoint16 = yOrderPoint15 + 1;
                        yOrder[yOrderPoint15] = (short) i3;
                        int yOrderPoint17 = yOrderPoint16 + 1;
                        yOrder[yOrderPoint16] = fieldPets[i3].pixelY;
                        yOrderPoint11 = yOrderPoint17 + 1;
                        yOrder[yOrderPoint17] = 0;
                        if (!fieldPets[i3].imageUpdate && setting[1] == 0) {
                            ImageLoadManager.requestImage(fieldPets[i3].getIconID(), fieldPets[i3]);
                        }
                        redraw3 = fieldPets[i3].getRedrawTiles();
                        redrawTiles8 = mergeTile(redrawTiles8, redraw3);
                    }
                }
            }
            int i4 = 0;
            while (true) {
                yOrderPoint2 = yOrderPoint;
                redrawTiles2 = redrawTiles;
                redrawTiles3 = redraw;
                if (i4 >= doors.length) {
                    break;
                }
                if (doors[i4].x < startX || doors[i4].x >= endX || doors[i4].y < startY || doors[i4].y >= endY) {
                    redraw = redrawTiles3;
                    redrawTiles = redrawTiles2;
                    yOrderPoint = yOrderPoint2;
                } else {
                    count++;
                    int yOrderPoint18 = yOrderPoint2 + 1;
                    yOrder[yOrderPoint2] = 5;
                    int yOrderPoint19 = yOrderPoint18 + 1;
                    yOrder[yOrderPoint18] = (short) i4;
                    int yOrderPoint20 = yOrderPoint19 + 1;
                    yOrder[yOrderPoint19] = (short) ((doors[i4].y + 1) * 16);
                    int yOrderPoint21 = yOrderPoint20 + 1;
                    yOrder[yOrderPoint20] = 0;
                    redraw = doors[i4].getRedrawTiles();
                    redrawTiles = mergeTile(redrawTiles2, redraw);
                    yOrderPoint = yOrderPoint21;
                }
                i4++;
            }
            int max = getMaxNetPlayer();
            int[] redraw4 = redrawTiles3;
            int[] redrawTiles9 = redrawTiles2;
            int yOrderPoint22 = yOrderPoint2;
            int i5 = 0;
            int draw3 = 1;
            while (true) {
                draw = count;
                if (i5 >= netPlayers.size()) {
                    break;
                }
                NetPlayer np = (NetPlayer) netPlayers.elementAt(i5);
                int mapX = np.tileX;
                int mapY = np.tileY;
                if (np.mapId != currentMapId || mapX < startX || mapX > endX || mapY < startY || mapY > endY || (CommonData.team.getMember(np.id) == null && draw3 >= max)) {
                    redrawTiles4 = redraw4;
                    redrawTiles5 = redrawTiles9;
                    count = draw;
                    draw2 = draw3;
                    yOrderPoint3 = yOrderPoint22;
                } else {
                    count = draw + 1;
                    int yOrderPoint23 = yOrderPoint22 + 1;
                    yOrder[yOrderPoint22] = 6;
                    int yOrderPoint24 = yOrderPoint23 + 1;
                    yOrder[yOrderPoint23] = (short) i5;
                    int yOrderPoint25 = yOrderPoint24 + 1;
                    yOrder[yOrderPoint24] = np.pixelY;
                    yOrderPoint3 = yOrderPoint25 + 1;
                    yOrder[yOrderPoint25] = 0;
                    draw2 = draw3 + 1;
                    int[] redraw5 = np.getRedrawTiles();
                    int[] redrawTiles10 = mergeTile(redrawTiles9, redraw5);
                    if (np.getFollowPet(false) != null) {
                        int yOrderPoint26 = yOrderPoint3 + 1;
                        yOrder[yOrderPoint3] = 12;
                        int yOrderPoint27 = yOrderPoint26 + 1;
                        yOrder[yOrderPoint26] = (short) i5;
                        int yOrderPoint28 = yOrderPoint27 + 1;
                        yOrder[yOrderPoint27] = np.getFollowPet().pixelY;
                        yOrderPoint3 = yOrderPoint28 + 1;
                        yOrder[yOrderPoint28] = 0;
                        count++;
                        redraw5 = np.getFollowPet().getRedrawTiles();
                        redrawTiles10 = mergeTile(redrawTiles10, redraw5);
                        if (!np.getFollowPet().imageUpdate && setting[1] == 0) {
                            ImageLoadManager.requestImage(np.getFollowPet().getIconID(), np.getFollowPet());
                        }
                    }
                    redrawTiles5 = redrawTiles10;
                    redrawTiles4 = redraw5;
                }
                i5++;
                redrawTiles9 = redrawTiles5;
                yOrderPoint22 = yOrderPoint3;
                draw3 = draw2;
                redraw4 = redrawTiles4;
            }
            int i6 = 0;
            int yOrderPoint29 = yOrderPoint22;
            count2 = draw;
            while (i6 < redrawTiles9.length) {
                int idx = 65535 & redrawTiles9[i6];
                int layer = 65535 & (redrawTiles9[i6] >> 16);
                int height = getTileHeight(mapData[idx][layer]);
                int y = idx / mapWidth;
                int yOrderPoint30 = yOrderPoint29 + 1;
                yOrder[yOrderPoint29] = 10;
                int yOrderPoint31 = yOrderPoint30 + 1;
                yOrder[yOrderPoint30] = (short) idx;
                int yOrderPoint32 = yOrderPoint31 + 1;
                yOrder[yOrderPoint31] = (short) ((height + y + 1) * 16);
                yOrder[yOrderPoint32] = (short) layer;
                count2++;
                i6++;
                yOrderPoint29 = yOrderPoint32 + 1;
            }
            int[] iArr = redrawTiles9;
            i = yOrderPoint29;
        } else {
            if (Battle.battlePet != null) {
                endX2 = 0;
                for (int i7 = 0; i7 < Battle.battlePet.length; i7++) {
                    int yOrderPoint33 = endX2 + 1;
                    yOrder[endX2] = 8;
                    int yOrderPoint34 = yOrderPoint33 + 1;
                    yOrder[yOrderPoint33] = (short) i7;
                    int yOrderPoint35 = yOrderPoint34 + 1;
                    yOrder[yOrderPoint34] = (short) Battle.battlePet[i7].getPixelY();
                    endX2 = yOrderPoint35 + 1;
                    yOrder[yOrderPoint35] = 0;
                    if (!Battle.battlePet[i7].imageUpdate && setting[1] == 0) {
                        ImageLoadManager.requestImage(Battle.battlePet[i7].getIconID(), Battle.battlePet[i7]);
                    }
                }
            } else {
                endX2 = 0;
            }
            if (Battle.battleArmy != null) {
                for (int i8 = 0; i8 < Battle.battleArmy.length; i8++) {
                    int yOrderPoint36 = endX2 + 1;
                    yOrder[endX2] = 9;
                    int yOrderPoint37 = yOrderPoint36 + 1;
                    yOrder[yOrderPoint36] = (short) i8;
                    int yOrderPoint38 = yOrderPoint37 + 1;
                    yOrder[yOrderPoint37] = (short) Battle.battleArmy[i8].getPixelY();
                    endX2 = yOrderPoint38 + 1;
                    yOrder[yOrderPoint38] = 0;
                    if (!Battle.battleArmy[i8].imageUpdate && setting[1] == 0) {
                        ImageLoadManager.requestImage(Battle.battleArmy[i8].getIconID(), Battle.battleArmy[i8]);
                    }
                }
            }
            int[] iArr2 = redrawTiles6;
            i = endX2;
        }
        if (count2 == -1) {
            yOrderCount = 0;
            return;
        }
        yOrderCount = i;
        sort(yOrder, yOrderCount);
    }

    public static void drawYOrder(Graphics g) {
        int dy;
        int dy2;
        int yoff;
        int off;
        Vector vec = new Vector();
        for (int i = 0; i < yOrderCount; i += 4) {
            short type = yOrder[i];
            short idx = yOrder[i + 1];
            switch (type) {
                case 0:
                    CommonData.player.draw(g);
                    break;
                case 2:
                    if (idx >= npcs.length) {
                        break;
                    } else {
                        npcs[idx].draw(g);
                        break;
                    }
                case 5:
                    doors[idx].draw(g);
                    vec.addElement(doors[idx]);
                    break;
                case 6:
                    if (idx >= netPlayers.size()) {
                        break;
                    } else {
                        ((NetPlayer) netPlayers.elementAt(idx)).draw(g);
                        break;
                    }
                case 8:
                    if (Battle.battlePet != null && idx < Battle.battlePet.length) {
                        Battle.battlePet[idx].draw(g);
                        break;
                    }
                case 9:
                    if (Battle.battleArmy != null && idx < Battle.battleArmy.length) {
                        Battle.battleArmy[idx].draw(g);
                        break;
                    }
                case 10:
                    redrawTile(g, idx, yOrder[i + 3]);
                    break;
                case 11:
                    if (CommonData.player.getFollowPet() == null) {
                        break;
                    } else {
                        CommonData.player.getFollowPet().draw(g);
                        break;
                    }
                case 12:
                    if (idx >= netPlayers.size()) {
                        break;
                    } else {
                        NetPlayer np = (NetPlayer) netPlayers.elementAt(idx);
                        if (np.getFollowPet() == null) {
                            break;
                        } else {
                            np.getFollowPet().draw(g);
                            break;
                        }
                    }
                case Tool.IMAGE_FONT_WIDTH /*13*/:
                    fieldPets[idx].draw(g);
                    break;
            }
        }
        if (CommonData.player.inBattle == 0 && CommonData.player.visible) {
            Player p = CommonData.player;
            int yoff2 = Utilities.LINE_HEIGHT - 5;
            int off2 = World.tick % 10;
            if (!p.title.equals("") || !p.guild.equals("")) {
                yoff = yoff2 + (Utilities.LINE_HEIGHT - 5);
            } else {
                yoff = yoff2;
            }
            if (off2 > 5) {
                off = 9 - off2;
            } else {
                off = off2;
            }
            Tool.uiMiscImg2.drawFrame(g, 28, p.getPixelX() + (p.getWidth() >> 1), (((p.getPixelY() - p.getHeight()) - off) - yoff) - 1, 0, 33);
        }
        if (Battle.battleID == -1) {
            if (touchNpc != null) {
                String name = touchNpc.name.trim();
                if (name != null && !name.equals("")) {
                    int tx = touchNpc.getPixelX();
                    int ny = touchNpc.getPixelY() - touchNpc.getHeight();
                    int stringWidth = Utilities.font.stringWidth(touchNpc.name);
                    int tw = touchNpc.getWidth();
                    if (touchNpc.id == 36896775) {
                        Tool.drawNpcName(g, touchNpc.name, tx, tw, ny - Utilities.CHAR_HEIGHT, true);
                    } else {
                        Tool.drawNpcName(g, touchNpc.name, tx, tw, ny - Utilities.CHAR_HEIGHT, false);
                    }
                }
            }
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < vec.size()) {
                    ((Door) vec.elementAt(i3)).drawName(g);
                    i2 = i3 + 1;
                }
            }
        }
        int titley = (World.viewHeight >> 1) + (World.viewHeight / 10);
        if (titleImg != null) {
            if (titleAniFrame >= 10) {
                if (titleAniFrame < 10 + 5) {
                    g.setColor(Tool.CLR_ITEM_WHITE);
                    g.drawLine(World.viewWidth >> 1, titley, ((World.viewWidth >> 1) - (((World.viewWidth >> 1) / 6) * (titleAniFrame - 10))) - 1, titley);
                    g.drawLine(World.viewWidth >> 1, titley, (World.viewWidth >> 1) + (((World.viewWidth >> 1) / 6) * (titleAniFrame - 10)) + 1, titley);
                    g.setColor(0);
                    g.drawLine(World.viewWidth >> 1, titley - 1, ((World.viewWidth >> 1) - (((World.viewWidth >> 1) / 6) * (titleAniFrame - 10))) - 1, titley - 1);
                    g.drawLine(World.viewWidth >> 1, titley - 1, (World.viewWidth >> 1) + (((World.viewWidth >> 1) / 6) * (titleAniFrame - 10)) + 1, titley - 1);
                    g.setColor(0);
                    g.drawLine(World.viewWidth >> 1, titley + 1, ((World.viewWidth >> 1) - (((World.viewWidth >> 1) / 6) * (titleAniFrame - 10))) - 1, titley + 1);
                    g.drawLine(World.viewWidth >> 1, titley + 1, ((titleAniFrame - 10) * ((World.viewWidth >> 1) / 6)) + (World.viewWidth >> 1) + 1, titley + 1);
                } else if (titleAniFrame < 10 + 45) {
                    g.setColor(Tool.CLR_ITEM_WHITE);
                    g.drawLine((World.viewWidth >> 1) / 6, titley, World.viewWidth - ((World.viewWidth >> 1) / 6), titley);
                    g.setColor(0);
                    g.drawLine((World.viewWidth >> 1) / 6, titley - 1, World.viewWidth - ((World.viewWidth >> 1) / 6), titley - 1);
                    g.drawLine((World.viewWidth >> 1) / 6, titley + 1, World.viewWidth - ((World.viewWidth >> 1) / 6), titley + 1);
                    g.setColor(0);
                    int center = World.viewWidth >> 1;
                    int dy3 = titley;
                    g.setClip(0, (dy3 - titleImg.getHeight(0)) - 2, World.viewWidth, titleImg.getHeight(0) + 2);
                    int disty = ((titleAniFrame - 10) - 5) << 1;
                    if (disty < titleImg.getHeight(0) + 2) {
                        dy = dy3 - disty;
                    } else {
                        dy = (dy3 - titleImg.getHeight(0)) - 2;
                    }
                    titleImg.draw(g, 0, center, dy, 0, 17);
                    int dy4 = titley;
                    g.setClip(0, dy4, World.viewWidth, titleImg.getHeight(1) + 2);
                    int disty2 = (titleAniFrame - 10) - 5;
                    if (disty2 < titleImg.getHeight(1) + 2) {
                        dy2 = dy4 + disty2;
                    } else {
                        dy2 = titleImg.getHeight(1) + dy4 + 2;
                    }
                    titleImg.draw(g, 1, center, dy2, 0, 33);
                } else if (titleAniFrame < 10 + 51) {
                    int f = (titleAniFrame - (10 + 45)) + 1;
                    g.setColor(Tool.CLR_ITEM_WHITE);
                    g.drawLine(((World.viewWidth >> 1) / 6) * f, titley, World.viewWidth - (((World.viewWidth >> 1) / 6) * f), titley);
                    g.setColor(0);
                    g.drawLine(((World.viewWidth >> 1) / 6) * f, titley - 1, World.viewWidth - (((World.viewWidth >> 1) / 6) * f), titley - 1);
                    g.drawLine(((World.viewWidth >> 1) / 6) * f, titley + 1, World.viewWidth - (f * ((World.viewWidth >> 1) / 6)), titley + 1);
                    g.setColor(0);
                } else {
                    titleAniFrame = 0;
                    titleImg = null;
                }
            }
            g.setClip(0, 0, World.viewWidth, World.viewHeight);
        }
    }

    public static void redrawTile(Graphics g, int idx, int layer) {
        PipImage tile = getTileImg();
        int id = getTileId(mapData[idx][layer]);
        int mapx = ((idx % mapWidth) * 16) - viewX;
        int mapy = ((idx / mapWidth) * 16) - getMapDrawY();
        int[] cover = CommonData.player.getCoverTiles(CommonData.player.pixelX, CommonData.player.pixelY);
        int i = 0;
        while (true) {
            if (i >= cover.length) {
                break;
            } else if ((cover[i] & TextField.CONSTRAINT_MASK) == idx) {
                break;
            } else {
                i++;
            }
        }
        tile.draw(g, id, mapx, mapy, 0);
    }

    private static void sort(short[] items, int itemsCount) {
        for (int i = 0; i < itemsCount; i += 4) {
            for (int j = i; j < itemsCount; j += 4) {
                short[] it1 = {items[j], items[j + 1], items[j + 2], items[j + 3]};
                short[] it2 = {items[i], items[i + 1], items[i + 2], items[i + 3]};
                if (compareYOrder(it1, it2) < 0) {
                    items[i] = items[j];
                    items[i + 1] = items[j + 1];
                    items[i + 2] = items[j + 2];
                    items[i + 3] = items[j + 3];
                    items[j] = it2[0];
                    items[j + 1] = it2[1];
                    items[j + 2] = it2[2];
                    items[j + 3] = it2[3];
                }
            }
        }
    }

    private static int compareYOrder(short[] v1, short[] v2) {
        if (v1[2] == v2[2]) {
            return getYOrderXPoint(v1) - getYOrderXPoint(v2);
        }
        return v1[2] - v2[2];
    }

    private static int getYOrderXPoint(short[] in) {
        short type = in[0];
        short idx = in[1];
        switch (type) {
            case 0:
                return CommonData.player.pixelX;
            case 2:
                return (short) (npcs[idx].tileX * 16);
            case 8:
                if (Battle.battlePet != null) {
                    return Battle.battlePet[idx].pixelX;
                }
                return 0;
            case 9:
                if (Battle.battleArmy != null) {
                    return Battle.battleArmy[idx].pixelX;
                }
                return 0;
            case 10:
                return in[2];
            default:
                return 0;
        }
    }

    public static void calculateMapLocation() {
        oldMapDrawX = mapDrawX;
        oldMapDrawY = mapDrawY;
        oldMapEndX = mapDrawX + mapDrawWidth;
        oldMapEndY = mapDrawY + mapDrawHeight;
        mapDrawX = viewX / 16;
        mapDrawY = viewY / 16;
        if (mapDrawX < 0) {
            mapDrawX = 0;
        }
        if (mapDrawY < 0) {
            mapDrawY = 0;
        }
        int endX = Math.min(mapWidth, ((viewX + World.viewWidth) / 16) + 1);
        int endY = Math.min(mapHeight, ((getMapDrawY() + World.viewHeight) / 16) + 1);
        mapDrawWidth = endX - mapDrawX;
        mapDrawHeight = endY - mapDrawY;
        if (mapDrawHeight > 16) {
            mapDrawHeight = 16;
        }
    }

    public static void drawMapCell(int[] map, int sx, int sy, int ex, int ey) {
        PipImage img = getTileImg();
        for (int y = sy; y < ey; y++) {
            for (int x = sx; x < ex; x++) {
                short[] data = mapData[(mapWidth * y) + x];
                if (data == null) {
                    int desty = y * 16;
                    while (true) {
                        int mapY = desty;
                        if (mapY >= mapY + 16) {
                            break;
                        }
                        System.arraycopy(alphaLine, 0, map, (mapY - (y * 16)) + mapY, alphaLine.length);
                        desty = mapY + 1;
                    }
                } else {
                    for (int layer = data.length - 1; layer >= 0; layer--) {
                        img.drawRGB(map, (ex - sx) * 16, (int) (short) getTileId(data[layer]), (x * 16) - (sx * 16), (y * 16) - (sy * 16), 0);
                    }
                }
            }
        }
    }

    public static void setLoadProgress(int v, int max) {
        if (v < max) {
            loadProgress = v;
        } else {
            loadProgress = max;
        }
    }

    public static void incLoadProgress(int max) {
        if (loadProgress < max) {
            loadProgress++;
        }
    }

    private static InputStream getPackageFileAsStream(String s) {
        return new ByteArrayInputStream((byte[]) _packageCache.get(s));
    }

    /* JADX WARNING: type inference failed for: r9v31, types: [int] */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void initPackageAndStage(byte[] r9) throws java.io.IOException {
        /*
            com.sumsharp.monster.common.data.Player r0 = com.sumsharp.monster.common.CommonData.player
            r1 = -1
            r0.doorId = r1
            r0 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream
            r1.<init>(r9)
            r9 = 0
            r9 = 12
            byte[] r9 = new byte[r9]
            r9 = 0
            byte[] r9 = (byte[]) r9
            r9 = 3
            byte[] r9 = new byte[r9]
            r9 = {83, 83, 83} // fill-array
            java.io.DataInputStream r4 = new java.io.DataInputStream     // Catch:{ IOException -> 0x00f9 }
            r4.<init>(r1)     // Catch:{ IOException -> 0x00f9 }
            java.lang.String r9 = r4.readUTF()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            int r9 = r4.readInt()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            short r9 = r4.readShort()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            areaId = r9     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            short r0 = r4.readShort()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r9 = 0
            byte[] r9 = (byte[]) r9     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r2 = 0
        L_0x0036:
            if (r2 < r0) goto L_0x0061
            java.util.Hashtable r2 = _packageCache     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r2.clear()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r3 = 0
            r5 = 0
            r2 = 0
            r8 = r5
            r5 = r3
            r3 = r8
        L_0x0043:
            if (r2 < r0) goto L_0x0071
            if (r5 != 0) goto L_0x004d
            r9 = 0
            titleAniFrame = r9     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r9 = 0
            titleImg = r9     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
        L_0x004d:
            r4.close()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r9 = 0
            r2 = r9
        L_0x0052:
            if (r2 < r0) goto L_0x00b1
            r9 = 85
            r0 = 85
            setLoadProgress(r9, r0)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            if (r4 == 0) goto L_0x0060
            r4.close()     // Catch:{ IOException -> 0x00f2 }
        L_0x0060:
            return
        L_0x0061:
            java.lang.String r3 = r4.readUTF()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r1[r2] = r3     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r3 = r1[r2]     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.lang.String r5 = ".etf"
            r3.endsWith(r5)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            int r2 = r2 + 1
            goto L_0x0036
        L_0x0071:
            int r6 = r4.readInt()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            boolean r3 = r4.readBoolean()     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            byte[] r9 = new byte[r6]     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r4.readFully(r9)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            if (r3 == 0) goto L_0x0084
            byte[] r9 = com.sumsharp.monster.net.GZIP.inflate(r9)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
        L_0x0084:
            r3 = r1[r2]     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.lang.String r7 = "area_title.pip"
            boolean r3 = r3.equals(r7)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            if (r3 == 0) goto L_0x00a8
            r3 = 0
            titleAniFrame = r3     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r3 = 1
            com.sumsharp.monster.image.PipImage r5 = new com.sumsharp.monster.image.PipImage     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.io.ByteArrayInputStream r7 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r7.<init>(r9)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r5.<init>(r7)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            titleImg = r5     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
        L_0x009e:
            r5 = 85
            incLoadProgress(r5)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            int r2 = r2 + 1
            r5 = r3
            r3 = r6
            goto L_0x0043
        L_0x00a8:
            java.util.Hashtable r3 = _packageCache     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r7 = r1[r2]     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r3.put(r7, r9)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r3 = r5
            goto L_0x009e
        L_0x00b1:
            r5 = r1[r2]     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.util.Hashtable r9 = _packageCache     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.lang.Object r9 = r9.get(r5)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            byte[] r9 = (byte[]) r9     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            if (r9 != 0) goto L_0x00c1
        L_0x00bd:
            int r9 = r2 + 1
            r2 = r9
            goto L_0x0052
        L_0x00c1:
            java.lang.String r3 = ".pip"
            boolean r3 = r5.endsWith(r3)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            if (r3 == 0) goto L_0x00bd
            com.sumsharp.monster.image.PipImage r3 = new com.sumsharp.monster.image.PipImage     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.io.ByteArrayInputStream r6 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r6.<init>(r9)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r3.<init>(r6)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.util.Hashtable r9 = _packageCache     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r9.remove(r5)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            java.util.Hashtable r9 = _packageCache     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r9.put(r5, r3)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            r9 = 85
            incLoadProgress(r9)     // Catch:{ IOException -> 0x00e3, all -> 0x00f5 }
            goto L_0x00bd
        L_0x00e3:
            r9 = move-exception
            r0 = r4
        L_0x00e5:
            throw r9     // Catch:{ all -> 0x00e6 }
        L_0x00e6:
            r9 = move-exception
            r8 = r9
            r9 = r0
            r0 = r8
        L_0x00ea:
            if (r9 == 0) goto L_0x00ef
            r9.close()     // Catch:{ IOException -> 0x00f0 }
        L_0x00ef:
            throw r0
        L_0x00f0:
            r9 = move-exception
            goto L_0x00ef
        L_0x00f2:
            r9 = move-exception
            goto L_0x0060
        L_0x00f5:
            r9 = move-exception
            r0 = r9
            r9 = r4
            goto L_0x00ea
        L_0x00f9:
            r9 = move-exception
            goto L_0x00e5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.NewStage.initPackageAndStage(byte[]):void");
    }

    public static int getMapDrawY() {
        return viewY - screenY;
    }

    public static void moveMap() {
        moveMap((CommonData.player.pixelX - (World.viewWidth / 2)) - viewX);
    }

    public static void moveMap(int dx) {
        viewX = (short) (viewX + dx);
        int mapWholeHeight = World.viewHeight;
        if (mapHeight > 15) {
            mapWholeHeight = 240;
            if (240 > World.viewHeight) {
                mapWholeHeight = World.viewHeight;
            }
        }
        if (mapHeight * 16 > mapWholeHeight) {
            viewY = (short) (CommonData.player.pixelY - ((mapWholeHeight / 3) * 2));
        }
        if (viewX < 0) {
            viewX = 0;
        }
        if (viewY < 0) {
            viewY = 0;
        }
        if (viewMaxX < 0 || viewMaxY < 0) {
            if (mapWidth * 16 > World.viewWidth) {
                viewMaxX = (short) ((((mapWidth * 16) - World.viewWidth) - 1) & TextField.CONSTRAINT_MASK);
            } else {
                viewMaxX = (short) (((((mapWidth * 16) - World.viewWidth) - 1) / 2) & TextField.CONSTRAINT_MASK);
            }
            viewMaxY = (short) ((((mapHeight * 16) - mapWholeHeight) - 1) & TextField.CONSTRAINT_MASK);
            if (viewMaxY < 0) {
                viewMaxY = (short) ((mapHeight * 16) - 1);
            }
        }
        if (viewX > viewMaxX) {
            viewX = viewMaxX;
        }
        if (viewY > viewMaxY) {
            viewY = viewMaxY;
        }
        if (lastViewX == -1) {
            lastViewX = viewX;
            return;
        }
        int sub = (viewX - lastViewX) + moveMapAdd;
        if (Math.abs(sub) < 4) {
            moveMapAdd = sub;
        } else {
            moveMapAdd = 0;
        }
        int scroll = sub / 4;
        int sub2 = sub - (scroll * 4);
        lastViewX = viewX;
        backImgX -= scroll;
        if (backImgX > bgWidth * 16) {
            backImgX -= bgWidth * 16;
        } else if (backImgX < 0) {
            backImgX += bgWidth * 16;
        }
    }

    public static void cycle() {
        if (World.drawUI) {
            if (Battle.battleID == -1) {
                checkNPC();
                if (monsters != null) {
                    for (Monster doCycle : monsters) {
                        doCycle.doCycle();
                    }
                }
                if (npcs != null) {
                    for (int i = 0; i < npcs.length; i++) {
                        if (npcs[i] != null) {
                            npcs[i].doCycle();
                        }
                    }
                }
                if (netPlayers != null) {
                    for (int i2 = 0; i2 < netPlayers.size(); i2++) {
                        NetPlayer np = (NetPlayer) netPlayers.elementAt(i2);
                        np.doCycle();
                        if (np.getFollowPet() != null) {
                            np.getFollowPet().doCycle();
                        }
                    }
                }
                if (fieldPets != null) {
                    for (int i3 = 0; i3 < fieldPets.length; i3++) {
                        if (fieldPets[i3] != null) {
                            fieldPets[i3].doCycle();
                        }
                    }
                }
            } else {
                if (Battle.battlePet != null) {
                    for (Pet doCycle2 : Battle.battlePet) {
                        doCycle2.doCycle();
                    }
                }
                if (Battle.battleArmy != null) {
                    for (Pet doCycle3 : Battle.battleArmy) {
                        doCycle3.doCycle();
                    }
                }
            }
            if (titleImg != null) {
                titleAniFrame++;
            }
        }
    }

    public static void checkNPC() {
        if (yOrder != null) {
            boolean touched = false;
            for (int i = 0; i < yOrderCount; i += 4) {
                switch (yOrder[i]) {
                    case 2:
                        short idx = yOrder[i + 1];
                        if (idx >= npcs.length) {
                            break;
                        } else {
                            Npc npc = npcs[idx];
                            if ((npc.npcType & 1) != 0 && touchUnit(npc)) {
                                if (touchNpc == null || touchNpc != npc) {
                                    npc.touching = true;
                                    if (touchNpc != null) {
                                        touchNpc.touching = false;
                                    }
                                    touchNpc = npc;
                                    if ((npc.npcType & 2) != 0) {
                                        touchNpc.keyPressed(4);
                                    }
                                }
                                touched = true;
                                break;
                            }
                        }
                }
            }
            if (!touched) {
                if (touchNpc != null) {
                    touchNpc.touching = false;
                    touchNpc = null;
                }
                if (CommonComponent.message != null && CommonComponent.message.getName() != null && CommonComponent.message.getName().equals(NPC_TIPMSG_NAME)) {
                    CommonComponent.closeMessage();
                }
            }
        }
    }

    public static boolean rectIntersect(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        int rx1;
        int ry1;
        if (x1 > x2) {
            rx1 = x1;
        } else {
            rx1 = x2;
        }
        if (y1 > y2) {
            ry1 = y1;
        } else {
            ry1 = y2;
        }
        int rx2 = x1 + w1 < x2 + w2 ? x1 + w1 : x2 + w2;
        if (ry1 >= (y1 + h1 < y2 + h2 ? y1 + h1 : y2 + h2) || rx1 >= rx2) {
            return false;
        }
        return true;
    }

    public static boolean touchUnit(Npc npc) {
        if (npc == null || !npc.visible || npc.camp != CommonData.player.camp) {
            return false;
        }
        int[] npcBox = npc.getTouchUnitBox();
        int[] playerOldBox = CommonData.player.getCollisionBox();
        if (rectIntersect(npcBox[0], npcBox[1], npcBox[2], npcBox[3], playerOldBox[0], playerOldBox[1], playerOldBox[2], playerOldBox[3])) {
            return true;
        }
        return false;
    }

    public static void netPlayerPositionDetail(UWAPSegment segment) {
        int id = segment.readInt();
        NetPlayer np = getNetPlayer(id);
        boolean addNew = false;
        if (np == null) {
            np = new NetPlayer();
            addNew = true;
        }
        if (!addNew && netPlayers.indexOf(np) == -1) {
            addNew = true;
        }
        try {
            np.load(segment);
            if (addNew) {
                np.id = id;
                netPlayers.addElement(np);
            }
        } catch (IOException e) {
        }
    }

    public static void netPlayerLeave(int id) {
        NetPlayer np = getNetPlayer(id);
        if (np != null) {
            netPlayers.removeElement(np);
            np.wpList = null;
            np.setState(0);
        }
    }

    public static int getMaxNetPlayer() {
        switch (displayQuanlity) {
            case 0:
                return 20;
            case 1:
                return 5;
            case 2:
                return 0;
            default:
                return 20;
        }
    }

    public static void netPlayerPosition(UWAPSegment segment) {
        NetPlayer np = getNetPlayer(segment.readInt());
        if (np != null) {
            try {
                np.update(segment);
                int maxNetPlayer = getMaxNetPlayer();
                if (netPlayers.indexOf(np) == -1) {
                    netPlayers.addElement(np);
                }
            } catch (IOException e) {
            }
        }
    }

    public static NetPlayer getNetPlayer(int id) {
        AbstractUnit au = CommonData.team.getMember(id);
        if (au != null && (au instanceof NetPlayer)) {
            return (NetPlayer) au;
        }
        for (int i = 0; i < netPlayers.size(); i++) {
            NetPlayer np = (NetPlayer) netPlayers.elementAt(i);
            if (np.id == id) {
                return np;
            }
        }
        return null;
    }

    public static void clearNpcTaskState() {
        int i = 0;
        while (npcs != null && i < npcs.length) {
            npcs[i].clearEmote(AbstractUnit.EMOTEID_TASK);
            npcs[i].taskState = -1;
            i++;
        }
    }

    public static void addNpc(Npc npc) {
        synchronized (npcs) {
            Npc[] newNpcs = new Npc[(npcs.length + 1)];
            System.arraycopy(npcs, 0, newNpcs, 0, npcs.length);
            newNpcs[npcs.length] = npc;
            npcs = newNpcs;
        }
    }

    public static void removeNpc(int id) {
        synchronized (npcs) {
            int removeIdx = -1;
            int i = 0;
            while (true) {
                if (npcs == null || i >= npcs.length) {
                    break;
                } else if (npcs[i].id == id) {
                    removeIdx = i;
                    break;
                } else {
                    i++;
                }
            }
            if (removeIdx != -1) {
                Npc[] newNpcs = new Npc[(npcs.length - 1)];
                System.arraycopy(npcs, 0, newNpcs, 0, removeIdx);
                System.arraycopy(npcs, removeIdx + 1, newNpcs, removeIdx, newNpcs.length - removeIdx);
                npcs = newNpcs;
            }
        }
    }

    public static Pet getFieldPet(int id) {
        int i = 0;
        while (fieldPets != null && i < fieldPets.length) {
            if (fieldPets[i].id == id) {
                return fieldPets[i];
            }
            i++;
        }
        return null;
    }

    public static Npc getNpc(int id) {
        int i = 0;
        while (npcs != null && i < npcs.length) {
            if (npcs[i].id == id) {
                return npcs[i];
            }
            i++;
        }
        return null;
    }

    public static Monster getMonster(int id) {
        int i = 0;
        while (monsters != null && i < monsters.length) {
            if (monsters[i].id == id) {
                return monsters[i];
            }
            i++;
        }
        return null;
    }

    public static Door getDoor(int id) {
        int i = 0;
        while (doors != null && i < doors.length) {
            if (doors[i].id == id) {
                return doors[i];
            }
            i++;
        }
        return null;
    }

    public static void processDoor() {
        Door td;
        if (CommonData.player.teamState != 1 || CommonData.team.id == -1) {
            short xPoint = CommonData.player.getXPoint();
            short mapX = CommonData.player.getYPoint();
            Door td2 = null;
            int i = 0;
            while (true) {
                int i2 = i;
                td = td2;
                if (i2 >= doors.length) {
                    break;
                }
                int width = CommonData.player.pixelX + (CommonData.player.getWidth() / 2);
                int px = CommonData.player.pixelY;
                int[] pbox = CommonData.player.getCollisionBox();
                if (rectIntersect(pbox[0], pbox[1], pbox[2], pbox[3], doors[i2].x * 16, doors[i2].y * 16, 16, 16)) {
                    if (touchDoor == null) {
                        gotoMap(doors[i2]);
                    }
                    Door td3 = doors[i2];
                    if (CommonData.player.getState() == 1 || CommonData.player.getState() == 2 || CommonData.player.getState() == 3) {
                        CommonData.player.keepMoving = -1;
                        CommonData.player.pressTime = -1;
                        CommonData.player.setState(0);
                    }
                    td2 = td3;
                } else {
                    td2 = td;
                }
                i = i2 + 1;
            }
            if (td != touchDoor) {
                touchDoor = null;
            }
        }
    }

    public static boolean canPass(int tile) {
        return canPass(tile % mapWidth, tile / mapWidth);
    }

    public static boolean canPass(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX >= mapWidth || tileY >= mapHeight) {
            return false;
        }
        int v = tileX + (mapWidth * tileY);
        for (int i : collection) {
            if (i == v) {
                return false;
            }
        }
        return true;
    }

    public static int collisionMap(int x, int y, int w, int h, int direct, int step, int oldX, int oldY) {
        int result = step;
        if (x >= 0 && y >= 0 && x + w <= mapWidth * 16 && y + h <= mapHeight * 16) {
            return result;
        }
        int result2 = 0;
        switch (direct) {
            case 0:
                if (x < 0) {
                    result2 = oldX - (step + x);
                    break;
                }
                break;
            case 1:
                if (x + w > mapWidth * 16) {
                    result2 = oldX + (step - x);
                    break;
                }
                break;
            case 2:
                if (y < 0) {
                    result2 = oldY - (step + y);
                    break;
                }
                break;
            case 3:
                if (y + h > mapHeight * 16) {
                    result2 = oldY + (step - y);
                    break;
                }
                break;
        }
        if (result2 < 0) {
            result2 = 0;
        }
        return result2;
    }

    public static int calculateDistance(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2, int direct) {
        int result = 0;
        switch (direct) {
            case 0:
                result = x2 - (x1 + w1);
                break;
            case 1:
                result = x1 - (x2 + w2);
                break;
            case 2:
                result = y2 - (y1 + h1);
                break;
            case 3:
                result = y1 - (y2 + h2);
                break;
        }
        if (result < 0) {
            return 0;
        }
        return result;
    }

    private static boolean canPassTile(byte tile, int tileInfo) {
        return true;
    }

    public static void gotoMap(short mapId, short x, short y, int mirrorId, int load) {
        CommonData.player.doorId = -1;
        if (mapId == CommonData.player.mapId) {
            CommonData.player.pixelX = x;
            CommonData.player.pixelY = y;
            CommonData.player.resetFollowPetPosition();
            isMapLoadOk = true;
            if (CommonData.player.mirrorId != mirrorId) {
                npcs = new Npc[0];
                CommonData.player.mirrorId = mirrorId;
            }
        } else {
            isMapLoadOk = false;
            mapBuffer = null;
            CommonData.player.mapId = mapId;
            CommonData.player.pixelX = x;
            CommonData.player.pixelY = y;
            CommonData.player.resetFollowPetPosition();
            CommonData.player.mirrorId = mirrorId;
            if (load == 0) {
                CommonComponent.loadUI(Utilities.VMUI_LOADING);
            }
        }
        CommonData.player.go(0, 0);
    }

    public static void gotoMap(Door door) {
        if (door.needTip) {
            touchDoor = door;
        } else {
            isMapLoadOk = false;
        }
        CommonData.player.doorId = door.id;
        CommonComponent.loadUI(Utilities.VMUI_LOADING);
    }

    public static int getRequestStageVersion(int areaId2) {
        if (useFileConnection != 0) {
            return -1;
        }
        try {
            FileConnection fc = Connector.open(String.valueOf(MapLoader.msPath) + "s" + areaId2 + ".pkg", 1);
            if (!fc.exists()) {
                return -1;
            }
            DataInputStream dis = fc.openDataInputStream();
            dis.readInt();
            dis.readUTF();
            return dis.readInt();
        } catch (Exception e) {
            return -1;
        }
    }

    public static byte[] getRequestStageData(int areaId2) {
        if (useFileConnection != 0) {
            return null;
        }
        try {
            FileConnection fc = Connector.open(String.valueOf(MapLoader.msPath) + "s" + areaId2 + ".pkg", 1);
            if (!fc.exists()) {
                return null;
            }
            DataInputStream dis = fc.openDataInputStream();
            byte[] stageData = new byte[dis.readInt()];
            dis.readFully(stageData);
            return stageData;
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static void clear() {
        areaId = -1;
        currentMapId = 0;
        mapData = null;
        bgMapData = null;
        collection = null;
        fieldPets = null;
        npcs = null;
        doors = null;
        monsters = null;
        mapDoor = null;
        mapBuffer = null;
        bgBuffer = null;
        World.clearGamePackages();
        ChatUI.tarList.removeAllElements();
        Chat.clear();
        touchNpc = null;
        CommonProcessor.itemTable.clear();
    }
}
