package io.github.eggohito.eggolib.power;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ModifyLabelRenderPower extends PrioritizedPower {

	public enum RenderMode {
		DEFAULT,
		HIDE_PARTIALLY,
		HIDE_COMPLETELY
	}

	private final Consumer<Entity> beforeParseAction;
	private final Consumer<Entity> afterParseAction;
	private final RenderMode renderMode;
	private final Text text;
	private final int tickRate;

	private Text replacementText;
	private Integer initialTicks;

	private boolean shouldResolve;

	public ModifyLabelRenderPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Entity> beforeParseAction, Consumer<Entity> afterParseAction, Text text, RenderMode renderMode, boolean shouldResolve, int tickRate, int priority) {
		super(powerType, livingEntity, priority);
		this.beforeParseAction = beforeParseAction;
		this.afterParseAction = afterParseAction;
		this.text = text;
		this.renderMode = renderMode;
		this.shouldResolve = shouldResolve;
		this.tickRate = tickRate;
		if (shouldResolve) {
			this.setTicking(true);
		}
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

			Optional<Text> parsedText = parseText();
			if (parsedText.isEmpty() || Objects.equals(replacementText, parsedText.get())) {
				return;
			}
			if (afterParseAction != null) {
				afterParseAction.accept(entity);
			}

			replacementText = parsedText.get();
			PowerHolderComponent.syncPower(entity, this.getType());

		} else if (initialTicks != null) {
			initialTicks = null;
		}

	}

	@Override
	public NbtElement toTag() {

		NbtCompound rootNbt = new NbtCompound();

		rootNbt.putString("ReplacementText", Text.Serializer.toJson(replacementText));
		rootNbt.putBoolean("ShouldResolve", shouldResolve);

		return rootNbt;

	}

	@Override
	public void fromTag(NbtElement tag) {

		if (!(tag instanceof NbtCompound rootNbt)) {
			return;
		}

		this.replacementText = Text.Serializer.fromJson(rootNbt.getString("ReplacementText"));
		this.shouldResolve = rootNbt.getBoolean("ShouldResolve");

	}

	public RenderMode getMode() {
		return renderMode;
	}

	public Text getReplacementText() {
		return shouldResolve ? replacementText : text;
	}

	private Optional<Text> parseText() {

		if (text == null || entity.getWorld().isClient) {
			return Optional.empty();
		}

		try {

			if (beforeParseAction != null) {
				beforeParseAction.accept(entity);
			}

			ServerCommandSource source = new ServerCommandSource(CommandOutput.DUMMY, entity.getPos(), entity.getRotationClient(), (ServerWorld) entity.getWorld(), 2, entity.getEntityName(), entity.getName(), entity.getWorld().getServer(), entity);
			Text parsedText = Texts.parse(source, text, entity, 0);

			return Optional.of(parsedText);

		} catch (CommandSyntaxException e) {
			Eggolib.LOGGER.warn("Power {} could not parse replacement text: {}", this.getType().getIdentifier(), e.getMessage());
			return Optional.empty();
		}

	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("modify_label_render"),
			new SerializableData()
				.add("before_parse_action", ApoliDataTypes.ENTITY_ACTION, null)
				.add("after_parse_action", ApoliDataTypes.ENTITY_ACTION, null)
				.add("text", SerializableDataTypes.TEXT, null)
				.add("render_mode", SerializableDataType.enumValue(RenderMode.class), RenderMode.DEFAULT)
				.add("should_resolve", SerializableDataTypes.BOOLEAN, true)
				.add("tick_rate", EggolibDataTypes.POSITIVE_INT, 20)
				.add("priority", SerializableDataTypes.INT, 0),
			data -> (powerType, livingEntity) -> new ModifyLabelRenderPower(
				powerType,
				livingEntity,
				data.get("before_parse_action"),
				data.get("after_parse_action"),
				data.get("text"),
				data.get("render_mode"),
				data.get("should_resolve"),
				data.get("tick_rate"),
				data.get("priority")
			)
		).allowCondition();
	}

}
