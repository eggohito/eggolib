package io.github.eggohito.eggolib.util;

import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.VariableIntPower;
import net.minecraft.scoreboard.ScoreboardPlayerScore;

public class MathUtils {

    public enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        MODULO,
        MIN,
        MAX,
        SWAP;
    }

    public static void operateScores(ScoreboardPlayerScore targetScore, Operation operation, ScoreboardPlayerScore sourceScore) {

        int targetValue = targetScore.getScore();
        int sourceValue = sourceScore.getScore();

        switch (operation) {
            case ADD:
                targetValue = targetValue + sourceValue;
                break;
            case SUBTRACT:
                targetValue = targetValue - sourceValue;
                break;
            case MULTIPLY:
                targetValue = targetValue * sourceValue;
                break;
            case DIVIDE:
                targetValue = Math.floorDiv(targetValue, sourceValue);
                break;
            case MODULO:
                targetValue = Math.floorMod(targetValue, sourceValue);
                break;
            case MIN:
                targetValue = Math.min(targetValue, sourceValue);
                break;
            case MAX:
                targetValue = Math.max(targetValue, sourceValue);
            case SWAP:
                int a = targetValue;
                targetValue = sourceValue;
                sourceValue = a;
                break;
        }

        targetScore.setScore(targetValue);
        sourceScore.setScore(sourceValue);
    }

    public static void operatePowers(Power targetPower, Operation operation, Power sourcePower) {

        int targetValue = 0;
        int sourceValue = 0 ;

        if (targetPower instanceof VariableIntPower targetResource) targetValue = targetResource.getValue();
        else if (targetPower instanceof CooldownPower targetCooldown) targetValue = targetCooldown.getRemainingTicks();

        if (sourcePower instanceof VariableIntPower sourceResource) sourceValue = sourceResource.getValue();
        else if (sourcePower instanceof CooldownPower sourceCooldown) sourceValue = sourceCooldown.getRemainingTicks();

        switch (operation) {
            case ADD:
                targetValue = targetValue + sourceValue;
                break;
            case SUBTRACT:
                targetValue = targetValue - sourceValue;
                break;
            case MULTIPLY:
                targetValue = targetValue * sourceValue;
                break;
            case DIVIDE:
                targetValue = Math.floorDiv(targetValue, sourceValue);
                break;
            case MODULO:
                targetValue = Math.floorMod(targetValue, sourceValue);
                break;
            case MIN:
                targetValue = Math.min(targetValue, sourceValue);
                break;
            case MAX:
                targetValue = Math.max(targetValue, sourceValue);
            case SWAP:
                int a = targetValue;
                targetValue = sourceValue;
                sourceValue = a;
                break;
        }

        if (targetPower instanceof VariableIntPower targetResource) targetResource.setValue(targetValue);
        else if (targetPower instanceof CooldownPower targetCooldown) targetCooldown.setCooldown(targetValue);

        if (sourcePower instanceof VariableIntPower sourceResource) sourceResource.setValue(sourceValue);
        else if (sourcePower instanceof CooldownPower sourceCooldown) sourceCooldown.setCooldown(sourceValue);
    }
}
