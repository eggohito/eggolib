package io.github.eggohito.eggolib.mixin.apace100.apoli;

import io.github.apace100.apoli.power.TooltipPower;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TooltipPower.class)
public interface TooltipPowerAccessor {

	@Accessor
	List<Text> getTexts();

}
