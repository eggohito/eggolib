package io.github.eggohito.eggolib.compat.emi.trinkets;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.compat.EggolibModCompat;
import io.github.eggohito.eggolib.compat.emi.trinkets.condition.entity.EquippedTrinketCondition;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

import static io.github.eggohito.eggolib.registry.factory.EggolibEntityConditions.register;

public class TrinketsCompat extends EggolibModCompat {

    public static final List<ConditionFactory<Entity>> REGISTERED_ENTITY_CONDITIONS = new ArrayList<>();

    public static void init(ModContainer trinkets) {

        REGISTERED_ENTITY_CONDITIONS.add(register(EquippedTrinketCondition.getFactory()));

        Eggolib.LOGGER.warn(
            String.format(
                "[%1$s] Detected '%2$s' by %3$s! Successfully registered these power/action/condition types:\n%4$s",
                Eggolib.MOD_ID,
                getModName(trinkets),
                getModAuthors(trinkets),
                getRegisteredConditionFactories(REGISTERED_ENTITY_CONDITIONS, "Entity Condition Type")
            )
        );

    }

}
