package model.abilities;

import model.world.Damageable;

import java.util.ArrayList;

public abstract class Ability {

    private String name;
    private int manaCost;
    private int baseCooldown;  //no. of turns a champ must wait after casting the ability in order to cast it again.
    private int currentCooldown;  //no. of turns the champ is currently waiting to recast the ability
    private int castRange;
    private int requiredActionPoints;
    private AreaOfEffect castArea;

    // idk if should be private?
    protected AbilityListener listener;

    public Ability(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required) {
        this.name = name;
        this.manaCost = cost;
        this.baseCooldown = baseCoolDown;
        this.currentCooldown = 0;  //starts off with a value of 0 since the ability hasn't been used yet.
        this.castRange = castRange;
        this.castArea = area;
        this.requiredActionPoints = required;
    }

    public abstract void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException;

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public void setCurrentCooldown(int currentCooldown) {
        if (currentCooldown < 0)
            this.currentCooldown = 0;
        else if (currentCooldown > baseCooldown)
            this.currentCooldown = baseCooldown;
        else
            this.currentCooldown = currentCooldown;
        if (listener != null)
            listener.onAbilityDetailsUpdated(this);
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getBaseCooldown() {
        return baseCooldown;
    }

    public int getCastRange() {
        return castRange;
    }

    public int getRequiredActionPoints() {
        return requiredActionPoints;
    }

    public AreaOfEffect getCastArea() {
        return castArea;
    }

    public void setListener(AbilityListener listener) {
        this.listener = listener;
    }
}
