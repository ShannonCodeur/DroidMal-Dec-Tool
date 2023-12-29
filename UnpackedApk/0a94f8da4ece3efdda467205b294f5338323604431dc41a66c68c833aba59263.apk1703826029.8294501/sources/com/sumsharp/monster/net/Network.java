package com.sumsharp.monster.net;

import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.gtvm.GTVM;
import javax.microedition.io.HttpConnection;

public class Network extends Thread {
    public static int detected = -1;
    private GTVM listener;
    private boolean proxyFlag;

    public Network(GTVM l) {
        this.listener = l;
    }

    public static boolean useProxy() {
        if (detected == -1) {
            detectNetwork(null);
            while (detected == -1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static int detectNetwork(GTVM vm) {
        new Thread(new Network(vm)).start();
        return detected;
    }

    public void run() {
        HttpConnection conn = null;
        try {
            this.proxyFlag = false;
            HttpConnection conn2 = UWAPSegment.getConnection(Utilities.entryURL, this.proxyFlag);
            int responseCode = conn2.getResponseCode();
            detected = 1;
            if (this.listener != null) {
                this.listener.continueProcess(detected);
            }
            if (conn2 != null) {
                try {
                    conn2.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e3) {
                }
            }
            try {
                this.proxyFlag = true;
                HttpConnection conn3 = UWAPSegment.getConnection(Utilities.entryURL, this.proxyFlag);
                int responseCode2 = conn3.getResponseCode();
                detected = 0;
                if (this.listener != null) {
                    this.listener.continueProcess(detected);
                }
                if (conn3 != null) {
                    try {
                        conn3.close();
                    } catch (Exception e4) {
                    }
                }
            } catch (Exception e5) {
                e5.printStackTrace();
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception e6) {
                    }
                }
                detected = 2;
                if (this.listener != null) {
                    this.listener.continueProcess(detected);
                }
            } catch (Throwable th) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception e7) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e8) {
                }
            }
            throw th2;
        }
    }
}
