package io.github.eggohito.eggolib.util;

import net.minecraft.entity.Entity;

import java.util.function.Consumer;

public class Key {

    public String key = "none";

    public Key() {}

    public Key(String key) {
        this.key = key;
    }

    public static class Timed {

        public String key = "none";
        public Integer ticks = null;

        public Timed() {}

        public Timed(String key, Integer ticks) {
            this.key = key;
            this.ticks = ticks;
        }

    }

    public static class Functional {

        public String key = "none";
        public boolean continuous = false;
        public Consumer<Entity> action = null;

        public Functional() {}

        public Functional(String key, boolean continuous, Consumer<Entity> action) {
            this.key = key;
            this.continuous = continuous;
            this.action = action;
        }

    }

}
