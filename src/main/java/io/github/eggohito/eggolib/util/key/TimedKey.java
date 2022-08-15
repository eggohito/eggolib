package io.github.eggohito.eggolib.util.key;

import io.github.eggohito.eggolib.util.Key;

public class TimedKey extends Key {

    public int ticks = 0;
    public int offset = 20;

    public TimedKey() {}

    public TimedKey(String key, int ticks) {
        this.key = key;
        this.ticks = ticks;
    }

    public TimedKey(String key, int ticks, int offset) {
        this.key = key;
        this.ticks = ticks;
        this.offset = offset;
    }

}
