package com.sumsharp.monster.image;

import com.sumsharp.monster.World;
import com.sumsharp.monster.common.data.AbstractUnit;
import java.util.Hashtable;
import java.util.Vector;

public class ImageLoadManager {
    public static Hashtable aniCache = new Hashtable();
    public static Hashtable cache = new Hashtable();
    private static Hashtable request = new Hashtable();

    public static ImageSet getImage(String name) {
        return getImage(name, -1, -1);
    }

    public static ImageSet getImage(String name, int rows, int cols) {
        ImageSet img;
        Item item = (Item) cache.get(name);
        if (item == null) {
            byte[] data = World.findResource("/" + name, false);
            if (data == null) {
                return null;
            }
            if (rows == -1) {
                img = new ImageSet(data);
            } else {
                img = new ImageSet(data, rows, cols);
            }
            Item item2 = new Item();
            item2.item = img;
            img.name = name;
            item2.acquire();
            cache.put(name, item2);
            return img;
        }
        item.acquire();
        return (ImageSet) item.item;
    }

    public static PipAnimateSet getAnimate(String name) {
        Item item = (Item) cache.get(name);
        if (item == null) {
            String resname = name;
            if (!name.startsWith("/")) {
                resname = "/" + resname;
            }
            byte[] data = World.findResource(resname, false);
            if (data == null) {
                return null;
            }
            PipAnimateSet img = new PipAnimateSet(data);
            Item item2 = new Item();
            item2.item = img;
            img.name = name;
            item2.acquire();
            cache.put(name, item2);
            return img;
        }
        item.acquire();
        return (PipAnimateSet) item.item;
    }

    public static void release(String name) {
        if (name != null) {
            Object o = cache.get(name);
            if (o != null) {
                Item item = (Item) o;
                if (item != null && item.release() <= 0) {
                    cache.remove(name);
                    System.out.println("ImageLoadManager " + name + " removed");
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r2 = new com.sumsharp.monster.net.UWAPSegment(17, 3);
        r2.writeInt(r8);
        com.sumsharp.monster.common.Utilities.sendRequest(r2);
        r3 = new java.util.Vector();
        r3.addElement(r9);
        request.put(new java.lang.Integer(r8), r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0069, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006a, code lost:
        r4.printStackTrace();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean requestImage(int r8, com.sumsharp.monster.common.data.AbstractUnit r9) {
        /*
            r7 = 0
            java.util.Hashtable r4 = aniCache
            java.lang.Integer r5 = new java.lang.Integer
            r5.<init>(r8)
            java.lang.Object r4 = r4.get(r5)
            if (r4 == 0) goto L_0x0020
            java.util.Hashtable r4 = aniCache
            java.lang.Integer r5 = new java.lang.Integer
            r5.<init>(r8)
            java.lang.Object r1 = r4.get(r5)
            java.lang.String r1 = (java.lang.String) r1
            r9.updateImage(r1)
            r4 = 1
        L_0x001f:
            return r4
        L_0x0020:
            java.util.Hashtable r4 = request
            monitor-enter(r4)
            java.util.Hashtable r5 = request     // Catch:{ all -> 0x0066 }
            java.lang.Integer r6 = new java.lang.Integer     // Catch:{ all -> 0x0066 }
            r6.<init>(r8)     // Catch:{ all -> 0x0066 }
            java.lang.Object r5 = r5.get(r6)     // Catch:{ all -> 0x0066 }
            if (r5 == 0) goto L_0x0043
            java.util.Hashtable r5 = request     // Catch:{ all -> 0x0066 }
            java.lang.Integer r6 = new java.lang.Integer     // Catch:{ all -> 0x0066 }
            r6.<init>(r8)     // Catch:{ all -> 0x0066 }
            java.lang.Object r3 = r5.get(r6)     // Catch:{ all -> 0x0066 }
            java.util.Vector r3 = (java.util.Vector) r3     // Catch:{ all -> 0x0066 }
            r3.addElement(r9)     // Catch:{ all -> 0x0066 }
            monitor-exit(r4)     // Catch:{ all -> 0x0066 }
            r4 = r7
            goto L_0x001f
        L_0x0043:
            monitor-exit(r4)     // Catch:{ all -> 0x0066 }
            com.sumsharp.monster.net.UWAPSegment r2 = new com.sumsharp.monster.net.UWAPSegment     // Catch:{ IOException -> 0x0069 }
            r4 = 17
            r5 = 3
            r2.<init>(r4, r5)     // Catch:{ IOException -> 0x0069 }
            r2.writeInt(r8)     // Catch:{ IOException -> 0x0069 }
            com.sumsharp.monster.common.Utilities.sendRequest(r2)     // Catch:{ IOException -> 0x0069 }
            java.util.Vector r3 = new java.util.Vector     // Catch:{ IOException -> 0x0069 }
            r3.<init>()     // Catch:{ IOException -> 0x0069 }
            r3.addElement(r9)     // Catch:{ IOException -> 0x0069 }
            java.util.Hashtable r4 = request     // Catch:{ IOException -> 0x0069 }
            java.lang.Integer r5 = new java.lang.Integer     // Catch:{ IOException -> 0x0069 }
            r5.<init>(r8)     // Catch:{ IOException -> 0x0069 }
            r4.put(r5, r3)     // Catch:{ IOException -> 0x0069 }
        L_0x0064:
            r4 = r7
            goto L_0x001f
        L_0x0066:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0066 }
            throw r5
        L_0x0069:
            r4 = move-exception
            r0 = r4
            r0.printStackTrace()
            goto L_0x0064
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sumsharp.monster.image.ImageLoadManager.requestImage(int, com.sumsharp.monster.common.data.AbstractUnit):boolean");
    }

    public static void updateImage(int id, String name) {
        synchronized (request) {
            aniCache.put(new Integer(id), name);
            Vector vec = (Vector) request.get(new Integer(id));
            if (vec != null) {
                for (int i = 0; i < vec.size(); i++) {
                    ((AbstractUnit) vec.elementAt(i)).updateImage(name);
                }
            }
            request.remove(new Integer(id));
        }
    }
}
