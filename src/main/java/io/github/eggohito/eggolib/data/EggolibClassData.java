package io.github.eggohito.eggolib.data;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.ModifyBouncinessPower;
import io.github.eggohito.eggolib.power.ModifyFovPower;
import io.github.eggohito.eggolib.power.ModifyMouseSensitivityPower;

public class EggolibClassData {

    public static void register() {

        ClassDataRegistry<Power> power = ClassDataRegistry.getOrCreate(Power.class, "Power");

        power.addMapping("eggolib:modify_bounciness", ModifyBouncinessPower.class);
        power.addMapping("eggolib:modify_fov", ModifyFovPower.class);
        power.addMapping("eggolib:modify_mouse_sensitivity", ModifyMouseSensitivityPower.class);

        Eggolib.LOGGER.info("[{}] Class data registry has been successfully registered!", Eggolib.MOD_ID);

    }

}
