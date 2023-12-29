package com.sumsharp.monster;

import com.sumsharp.monster.common.CommonData;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class MapLoader implements Runnable {
    public static boolean isLoading = false;
    public static int loadFinished = 0;
    public static String msPath = "file://data/data/com.sumsharp.monster/files/";
    private short areaId;
    private short mapId;
    private byte[] stageData;

    public MapLoader(byte[] stageData2, short areaId2, short mapId2) {
        if (!isLoading) {
            isLoading = true;
            loadFinished = 0;
            mapId2 = mapId2 == -1 ? (short) (CommonData.player.mapId & 15) : mapId2;
            this.stageData = stageData2;
            this.areaId = areaId2;
            this.mapId = mapId2;
            if (CommonData.player != null) {
                CommonData.player.keepMoving = -1;
            }
            new Thread(this).start();
        }
    }

    public void run() {
        NewStage.areaId = this.areaId;
        NewStage.currentMapId = this.mapId;
        boolean loadFromMS = false;
        if (this.stageData != null) {
            try {
                if (this.stageData.length == 0) {
                    this.stageData = NewStage.getRequestStageData(this.areaId);
                    loadFromMS = true;
                }
                NewStage.initPackageAndStage(this.stageData);
            } catch (SecurityException e) {
                SecurityException securityException = e;
                isLoading = false;
                loadFinished = 2;
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (!loadFromMS && this.stageData != null) {
            try {
                if (NewStage.useFileConnection == 0) {
                    FileConnection fc = Connector.open(String.valueOf(msPath) + "s" + this.areaId + ".pkg", 2);
                    DataOutputStream dos = fc.openDataOutputStream();
                    dos.writeInt(this.stageData.length);
                    dos.write(this.stageData);
                    dos.close();
                    fc.close();
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        try {
            NewStage.loadMap(this.mapId);
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        isLoading = false;
        loadFinished = 1;
    }
}
