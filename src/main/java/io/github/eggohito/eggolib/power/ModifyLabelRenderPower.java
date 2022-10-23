package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.function.UnaryOperator;

public class ModifyLabelRenderPower extends PrioritizedPower {

    public enum RenderMode {
        DEFAULT,
        HIDE_PARTIALLY,
        HIDE_COMPLETELY
    }

    private Text text;
    private RenderMode renderMode;

    public ModifyLabelRenderPower(PowerType<?> powerType, LivingEntity livingEntity, Text text, RenderMode renderMode, int priority) {
        super(powerType, livingEntity, priority);
        this.text = text;
        this.renderMode = renderMode;
    }

    //  TODO: Allow users to use '%s' in the text component to reference the entity's name
    public Text getReplacementText() {
        return text;
    }

    public void setReplacementText(Text text) {
        this.text = text;
    }

    public RenderMode getMode() {
        return renderMode;
    }

    public void setMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("modify_label_render"),
            new SerializableData()
                .add("text", SerializableDataTypes.TEXT, null)
                .add("render_mode", SerializableDataType.enumValue(RenderMode.class), RenderMode.DEFAULT)
                .add("priority", SerializableDataTypes.INT, 0),
            data -> (powerType, livingEntity) -> new ModifyLabelRenderPower(
                powerType,
                livingEntity,
                data.get("text"),
                data.get("render_mode"),
                data.getInt("priority")
            )
        ).allowCondition();
    }

}
