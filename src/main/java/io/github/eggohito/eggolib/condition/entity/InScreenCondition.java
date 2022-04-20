package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class InScreenCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;

        List<String> screenClassStrings = new LinkedList<>();
        String currentScreenClassString = Eggolib.playerCurrentScreenHashMap.get(playerEntity);

        if (currentScreenClassString == null) return false;

        try {
            Class<?> currentScreenClass = Class.forName(currentScreenClassString);
            if (data.isPresent("screen") || data.isPresent("screens")) {

                if (data.isPresent("screen")) {

                    String screenField = data.getString("screen");
                    screenClassStrings.add(screenField);
                }

                if (data.isPresent("screens")) {

                    List<String> screensField = data.get("screens");
                    screenClassStrings.addAll(screensField);
                }

                Optional<ClassDataRegistry> optionalClassDataRegistry = ClassDataRegistry.get(ClassUtil.castClass(Screen.class));
                if (optionalClassDataRegistry.isPresent()) {

                    ClassDataRegistry<? extends Screen> classDataRegistry = optionalClassDataRegistry.get();
                    return screenClassStrings
                        .stream()
                        .map(classDataRegistry::mapStringToClass)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .anyMatch(classString -> classString.isAssignableFrom(currentScreenClass));
                }
            }
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("in_screen"),
            new SerializableData()
                .add("screen", SerializableDataTypes.STRING, null)
                .add("screens", SerializableDataTypes.STRINGS, null),
            InScreenCondition::condition
        );
    }
}
