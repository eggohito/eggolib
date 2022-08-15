package io.github.eggohito.eggolib.util;

public class Key {

    public String key = "none";

    public Key() {}

    public Key(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Key key)) return false;
        else return this.key.equals(key.key);
    }

}
