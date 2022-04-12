package io.github.eggohito.eggolib.util;

import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.VariableIntPower;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.util.Pair;

public class MathUtils {

    public enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        MODULO,
        MIN,
        MAX,
        SWAP
    }

    private static Pair<Integer, Integer> result;

    public static Pair<Integer, Integer> operate(int a, Operation operation, int b) {
        switch (operation) {
            case ADD:
                a = a + b;
                break;
            case SUBTRACT:
                a = a - b;
                break;
            case MULTIPLY:
                a = a * b;
                break;
            case DIVIDE:
                a = Math.floorDiv(a, b);
                break;
            case MODULO:
                a = Math.floorMod(a, b);
                break;
            case MIN:
                a = Math.min(a, b);
                break;
            case MAX:
                a = Math.max(a, b);
                break;
            case SWAP:
                int a2 = a;
                a = b;
                b = a2;
                break;
        }
        return new Pair<>(a, b);
    }

    public static void operateScores(ScoreboardPlayerScore targetScore, Operation operation, ScoreboardPlayerScore sourceScore) {

        int x = targetScore.getScore();
        int y = sourceScore.getScore();

        result = operate(x, operation, y);

        targetScore.setScore(result.getLeft());
        sourceScore.setScore(result.getRight());
    }

    public static void operatePowers(Power targetPower, Operation operation, Power sourcePower) {

        int x = 0;
        int y = 0 ;

        if (targetPower instanceof VariableIntPower targetResource) x = targetResource.getValue();
        else if (targetPower instanceof CooldownPower targetCooldown) x = targetCooldown.getRemainingTicks();

        if (sourcePower instanceof VariableIntPower sourceResource) y = sourceResource.getValue();
        else if (sourcePower instanceof CooldownPower sourceCooldown) y = sourceCooldown.getRemainingTicks();

        result = operate(x, operation, y);

        if (targetPower instanceof VariableIntPower targetResource) targetResource.setValue(result.getLeft());
        else if (targetPower instanceof CooldownPower targetCooldown) targetCooldown.setCooldown(result.getLeft());

        if (sourcePower instanceof VariableIntPower sourceResource) sourceResource.setValue(result.getRight());
        else if (sourcePower instanceof CooldownPower sourceCooldown) sourceCooldown.setCooldown(result.getRight());
    }
}
