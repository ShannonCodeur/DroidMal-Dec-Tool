package com.sumsharp.monster.image;

class Item {
    public Object item;
    public int ref;

    Item() {
    }

    public synchronized int acquire() {
        this.ref++;
        return this.ref;
    }

    public synchronized int release() {
        try {
            this.ref--;
        }
        return this.ref;
    }
}
