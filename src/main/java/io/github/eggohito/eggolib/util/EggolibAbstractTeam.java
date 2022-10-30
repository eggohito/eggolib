package io.github.eggohito.eggolib.util;

import net.minecraft.scoreboard.AbstractTeam;

public class EggolibAbstractTeam {

    private String name;
    private Boolean friendlyFire;
    private Boolean showFriendlyInvisibles;
    private AbstractTeam.VisibilityRule nametagVisibilityRule;
    private AbstractTeam.VisibilityRule deathMessageVisibilityRule;
    private AbstractTeam.CollisionRule collisionRule;

    public EggolibAbstractTeam() {
        this.name = null;
        this.friendlyFire = null;
        this.showFriendlyInvisibles = null;
        this.nametagVisibilityRule = null;
        this.deathMessageVisibilityRule = null;
        this.collisionRule = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isFriendlyFireAllowed() {
        return friendlyFire;
    }

    public void setFriendlyFireAllowed(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public Boolean shouldShowFriendlyInvisibles() {
        return showFriendlyInvisibles;
    }

    public void setShowFriendlyInvisibles(boolean showFriendlyInvisibles) {
        this.showFriendlyInvisibles = showFriendlyInvisibles;
    }

    public AbstractTeam.VisibilityRule getNametagVisibilityRule() {
        return nametagVisibilityRule;
    }

    public void setNametagVisibilityRule(AbstractTeam.VisibilityRule nametagVisibilityRule) {
        this.nametagVisibilityRule = nametagVisibilityRule;
    }

    public AbstractTeam.VisibilityRule getDeathMessageVisibilityRule() {
        return deathMessageVisibilityRule;
    }

    public void setDeathMessageVisibilityRule(AbstractTeam.VisibilityRule deathMessageVisibilityRule) {
        this.deathMessageVisibilityRule = deathMessageVisibilityRule;
    }

    public AbstractTeam.CollisionRule getCollisionRule() {
        return collisionRule;
    }

    public void setCollisionRule(AbstractTeam.CollisionRule collisionRule) {
        this.collisionRule = collisionRule;
    }

    public boolean isEqualTo(Object obj) {

        if (obj instanceof AbstractTeam abstractTeam) {

            int i = 0;

            if (name != null && name.equals(abstractTeam.getName())) i++;
            if (friendlyFire != null && friendlyFire.equals(abstractTeam.isFriendlyFireAllowed())) i++;
            if (showFriendlyInvisibles != null && showFriendlyInvisibles.equals(abstractTeam.shouldShowFriendlyInvisibles())) i++;
            if (nametagVisibilityRule != null && nametagVisibilityRule.equals(abstractTeam.getNameTagVisibilityRule())) i++;
            if (deathMessageVisibilityRule != null && deathMessageVisibilityRule.equals(abstractTeam.getDeathMessageVisibilityRule())) i++;
            if (collisionRule != null && collisionRule.equals(abstractTeam.getCollisionRule())) i++;

            return i > 0;

        }

        else if (obj instanceof EggolibAbstractTeam eggolibAbstractTeam) return this.equals(eggolibAbstractTeam);
        else return false;

    }

}
