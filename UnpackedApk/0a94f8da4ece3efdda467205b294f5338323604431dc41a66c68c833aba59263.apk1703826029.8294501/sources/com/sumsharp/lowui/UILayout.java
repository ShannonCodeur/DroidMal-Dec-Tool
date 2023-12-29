package com.sumsharp.lowui;

public class UILayout {
    protected int colSpan;
    protected boolean hGrib;
    protected int row;
    protected int rowSpan;
    protected boolean vGrib;

    public UILayout(int row2, boolean hGrib2, boolean vGrib2) {
        this.row = row2;
        this.hGrib = hGrib2;
        this.vGrib = vGrib2;
    }
}
