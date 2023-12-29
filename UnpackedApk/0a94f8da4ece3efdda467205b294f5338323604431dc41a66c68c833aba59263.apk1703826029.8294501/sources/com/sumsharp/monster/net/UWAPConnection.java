package com.sumsharp.monster.net;

public interface UWAPConnection {
    void close();

    void cut(boolean z);

    void start();

    int writeSegment(UWAPSegment uWAPSegment);
}
