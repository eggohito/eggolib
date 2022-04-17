package io.github.eggohito.eggolib.util;

import net.minecraft.util.Pair;

public class MathUtil {

    public enum MathOperation {
        ADD,
        DIVIDE,
        MAX,
        MIN,
        MODULO,
        MULTIPLY,
        SET,
        SUBTRACT,
        SWAP
    }

    public static Pair<Integer, Integer> operate(int a, MathOperation mathOperation, int b) {
        switch (mathOperation) {
            case ADD:
                a = a + b;
                break;
            case DIVIDE:
                if (b != 0) a = Math.floorDiv(a, b);
                break;
            case MAX:
                a = Math.max(a, b);
                break;
            case MIN:
                a = Math.min(a, b);
                break;
            case MODULO:
                if (b != 0) a = Math.floorMod(a, b);
                break;
            case MULTIPLY:
                a = a * b;
                break;
            case SET:
                a = b;
            case SUBTRACT:
                a = a - b;
                break;
            case SWAP:
                int a2 = a;
                a = b;
                b = a2;
                break;
        }

        return new Pair<>(a, b);
    }
}
