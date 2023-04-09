package io.github.eggohito.eggolib.power;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
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

public class ModifyLabelRenderPower extends PrioritizedPower {

    public enum RenderMode {
        DEFAULT,
        HIDE_PARTIALLY,
        HIDE_COMPLETELY
    }

    private final RenderMode renderMode;
    private final int tickRate;

    private Text text;
    private Text replacementText;

    public ModifyLabelRenderPower(PowerType<?> powerType, LivingEntity livingEntity, Text text, RenderMode renderMode, int tickRate, int priority) {
        super(powerType, livingEntity, priority);
        this.text = text;
        this.renderMode = renderMode;
        this.tickRate = tickRate;
        this.setTicking();
    }

    @Override
    public void onGained() {
        if (!entity.world.isClient) parseText().ifPresent(t -> replacementText = t);
    }

    @Override
    public void tick() {

        if (entity.age % tickRate != 0) return;

        Optional<Text> rpt = parseText();
        if (rpt.isEmpty() || Objects.equals(replacementText, rpt.get())) return;

        replacementText = rpt.get();
        PowerHolderComponent.syncPower(entity, this.getType());

    }

    @Override
    public NbtElement toTag() {

        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("ReplacementText", Text.Serializer.toJson(replacementText));

        return nbtCompound;

    }

    @Override
    public void fromTag(NbtElement tag) {
        if (tag instanceof NbtCompound nbtCompound) replacementText = Text.Serializer.fromJson(nbtCompound.getString("ReplacementText"));
    }

    public RenderMode getMode() {
        return renderMode;
    }

    public Text getReplacementText() {
        return replacementText;
    }

    public void setReplacementText(Text text) {
        this.text = text;
    }

    private Optional<Text> parseText() {

        if (text == null) return Optional.empty();

        try {
            ServerCommandSource source = new ServerCommandSource(CommandOutput.DUMMY, entity.getPos(), entity.getRotationClient(), (ServerWorld) entity.world, 2, entity.getEntityName(), entity.getName(), entity.world.getServer(), entity);
            return Optional.of(Texts.parse(source, text, entity, 0));
        }

        catch (CommandSyntaxException e) {
            Eggolib.LOGGER.warn("Power {} could not parse replacement text: {}", this.getType().getIdentifier(), e.getMessage());
            return Optional.empty();
        }

    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("modify_label_render"),
            new SerializableData()
                .add("text", SerializableDataTypes.TEXT, null)
                .add("render_mode", SerializableDataType.enumValue(RenderMode.class), RenderMode.DEFAULT)
                .add("tick_rate", EggolibDataTypes.POSITIVE_INT, 1)
                .add("priority", SerializableDataTypes.INT, 0),
            data -> (powerType, livingEntity) -> new ModifyLabelRenderPower(
                powerType,
                livingEntity,
                data.get("text"),
                data.get("render_mode"),
                data.get("tick_rate"),
                data.get("priority")
            )
        ).allowCondition();
    }

}
