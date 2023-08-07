package com.lunaixsky.miscmod.enchaments;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.DiggingEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

public class AdvancedEfficiency extends DiggingEnchantment {
    protected AdvancedEfficiency() {
        super(Rarity.RARE, EquipmentSlot.MAINHAND);
    }

    public int getEffectiveDigLevel(int level) {
        return super.getMaxLevel() + level;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean checkCompatibility(Enchantment p_44690_) {
        return super.checkCompatibility(p_44690_);
    }

    @Override
    public int getMinCost(int p_44679_) {
        return super.getMinCost(super.getMaxLevel() + p_44679_ + 1);
    }

    @Override
    public int getMaxCost(int p_44679_) {
        return super.getMaxCost(super.getMaxLevel() + p_44679_ + 1);
    }
}
