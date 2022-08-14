package io.github.eggohito.eggolib.util;

import net.minecraft.entity.Entity;

import java.util.function.Consumer;

public class Key {

    public static class Timed {

        public String key;
        public Integer ticks;

        public Timed(String key, Integer ticks) {
            this.key = key;
            this.ticks = ticks;
        }

    }

    public static class Functional {

        public String key;
        public boolean continuous;
        public Consumer<Entity> action;

        public Functional(String key, boolean continuous, Consumer<Entity> action) {
            this.key = key;
            this.continuous = continuous;
            this.action = action;
        }

    }

}
