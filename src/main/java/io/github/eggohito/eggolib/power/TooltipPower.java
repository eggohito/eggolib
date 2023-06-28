package io.github.eggohito.eggolib.power;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.apace100.apoli.TooltipPowerAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class TooltipPower extends io.github.apace100.apoli.power.TooltipPower {

	private final int tickRate;

	private List<Text> tooltipTexts;
	private Integer initialTicks;

	public TooltipPower(PowerType<?> type, LivingEntity entity, Predicate<ItemStack> itemCondition, Text text, List<Text> texts, int tickRate, int order) {

		super(type, entity, itemCondition, order);
		List<Text> thisTexts = ((TooltipPowerAccessor) this).getTexts();

		if (text != null) {
			thisTexts.add(text);
		}
		if (texts != null) {
			thisTexts.addAll(texts);
		}

		this.tooltipTexts = new LinkedList<>();
		this.initialTicks = null;
		this.tickRate = tickRate <= 0 ? 1 : tickRate;
		this.setTicking(true);

	}

	@Override
	public void addToTooltip(List<Text> tooltip) {
		tooltip.addAll(tooltipTexts);
	}

	@Override
	public void tick() {

		if (isActive()) {

			if (initialTicks == null) {
				initialTicks = entity.age % tickRate;
				return;
			}

			if (entity.age % tickRate != initialTicks) {
				return;
			}

			List<Text> parsedTexts = parseTexts();
			if (parsedTexts.isEmpty() || !Collections.disjoint(tooltipTexts, parsedTexts)) {
				return;
			}

			tooltipTexts = parsedTexts;
			PowerHolderComponent.syncPower(entity, this.getType());

		} else if (initialTicks != null) {
			initialTicks = null;
		}

	}

	@Override
	public NbtElement toTag() {

		NbtCompound rootNbt = new NbtCompound();
		NbtList tooltipTextsNbt = new NbtList();

		for (Text tooltipText : tooltipTexts) {
			NbtString tooltipTextNbt = NbtString.of(Text.Serializer.toJson(tooltipText));
			tooltipTextsNbt.add(tooltipTextNbt);
		}

		rootNbt.put("Tooltips", tooltipTextsNbt);
		return rootNbt;

	}

	@Override
	public void fromTag(NbtElement tag) {

		tooltipTexts.clear();
		NbtCompound rootNbt = (NbtCompound) tag;
		NbtList tooltipTextsNbt = rootNbt.getList("Tooltips", NbtElement.STRING_TYPE);

		for (int i = 0; i < tooltipTextsNbt.size(); i++) {
			Text tooltipText = Text.Serializer.fromJson(tooltipTextsNbt.getString(i));
			tooltipTexts.add(tooltipText);
		}

	}

	private List<Text> parseTexts() {

		List<Text> parsedTexts = new LinkedList<>();
		List<Text> texts = ((TooltipPowerAccessor) this).getTexts();

		if (texts.isEmpty() || entity.world.isClient) {
			return parsedTexts;
		}

		ServerCommandSource source = new ServerCommandSource(
			CommandOutput.DUMMY,
			entity.getPos(),
			entity.getRotationClient(),
			(ServerWorld) entity.world,
			Apoli.config.executeCommand.permissionLevel,
			entity.getEntityName(),
			entity.getName(),
			entity.world.getServer(),
			entity
		);

		for (int i = 0; i < texts.size(); i++) {
			try {

				Text text = texts.get(i);
				Text parsedText = Texts.parse(source, text, entity, 0);

				parsedTexts.add(parsedText);

			} catch (CommandSyntaxException e) {
				Eggolib.LOGGER.warn("Power {} could not parse replacement text at index {}: {}", this.getType().getIdentifier(), i, e.getMessage());
			}
		}

		return parsedTexts;

	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("tooltip"),
			new SerializableData()
				.add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
				.add("text", SerializableDataTypes.TEXT, null)
				.add("texts", SerializableDataTypes.TEXTS, null)
				.add("tick_rate", EggolibDataTypes.POSITIVE_INT, 20)
				.add("order", SerializableDataTypes.INT, 0),
			data -> (powerType, livingEntity) -> new TooltipPower(
				powerType,
				livingEntity,
				data.get("item_condition"),
				data.get("text"),
				data.get("texts"),
				data.get("tick_rate"),
				data.get("order")
			)
		).allowCondition();
	}

}
