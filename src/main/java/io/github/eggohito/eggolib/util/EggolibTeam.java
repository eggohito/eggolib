package io.github.eggohito.eggolib.util;

import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

@SuppressWarnings("UnusedReturnValue")
public class EggolibTeam extends AbstractTeam {

    private String name;
    private Boolean friendlyFire;
    private Boolean showFriendlyInvisibles;
    private AbstractTeam.CollisionRule collisionRule;
    private AbstractTeam.VisibilityRule nametagVisibilityRule;
    private AbstractTeam.VisibilityRule deathMessageVisibilityRule;

    private final HashMap<Object, Boolean> fieldStates = new HashMap<>();

    public EggolibTeam() {
        this.name = "";
        this.friendlyFire = true;
        this.showFriendlyInvisibles = true;
        this.nametagVisibilityRule = VisibilityRule.ALWAYS;
        this.deathMessageVisibilityRule = VisibilityRule.ALWAYS;
        this.collisionRule = CollisionRule.ALWAYS;
    }

    public EggolibTeam setName(String name) {
        this.name = name;
        this.fieldStates.put(this.name, true);
        return this;
    }

    public EggolibTeam showFriendlyInvisibles(boolean bl) {
        this.showFriendlyInvisibles = bl;
        this.fieldStates.put(this.showFriendlyInvisibles, true);
        return this;
    }

    public EggolibTeam friendlyFireAllowed(boolean bl) {
        this.friendlyFire = bl;
        this.fieldStates.put(this.friendlyFire, true);
        return this;
    }

    public EggolibTeam setNameTagVisibilityRule(VisibilityRule visibilityRule) {
        this.nametagVisibilityRule = visibilityRule;
        this.fieldStates.put(this.nametagVisibilityRule, true);
        return this;
    }

    public EggolibTeam setDeathMessageVisibilityRule(VisibilityRule visibilityRule) {
        this.deathMessageVisibilityRule = visibilityRule;
        this.fieldStates.put(this.deathMessageVisibilityRule, true);
        return this;
    }

    public EggolibTeam setCollisionRule(CollisionRule collisionRule) {
        this.collisionRule = collisionRule;
        this.fieldStates.put(this.collisionRule, true);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MutableText decorateName(Text name) {
        return Text.empty();
    }

    @Override
    public boolean shouldShowFriendlyInvisibles() {
        return showFriendlyInvisibles;
    }

    @Override
    public boolean isFriendlyFireAllowed() {
        return friendlyFire;
    }

    @Override
    public VisibilityRule getNameTagVisibilityRule() {
        return nametagVisibilityRule;
    }

    @Override
    public Formatting getColor() {
        return Formatting.RESET;
    }

    @Override
    public Collection<String> getPlayerList() {
        return Collections.emptyList();
    }

    @Override
    public VisibilityRule getDeathMessageVisibilityRule() {
        return deathMessageVisibilityRule;
    }

    @Override
    public CollisionRule getCollisionRule() {
        return collisionRule;
    }

    @Override
    public boolean isEqual(@Nullable AbstractTeam team) {

        if (team == null) return false;

        int i = 0;

        if (fieldStates.getOrDefault(name, false) && name.equals(team.getName())) i++;
        if (fieldStates.getOrDefault(friendlyFire, false) && friendlyFire.equals(team.isFriendlyFireAllowed())) i++;
        if (fieldStates.getOrDefault(showFriendlyInvisibles, false) && showFriendlyInvisibles.equals(team.shouldShowFriendlyInvisibles())) i++;
        if (fieldStates.getOrDefault(collisionRule, false) && collisionRule.equals(team.getCollisionRule())) i++;
        if (fieldStates.getOrDefault(nametagVisibilityRule, false) && nametagVisibilityRule.equals(team.getNameTagVisibilityRule())) i++;
        if (fieldStates.getOrDefault(deathMessageVisibilityRule, false) && deathMessageVisibilityRule.equals(team.getDeathMessageVisibilityRule())) i++;

        return i > 0;

    }

}
