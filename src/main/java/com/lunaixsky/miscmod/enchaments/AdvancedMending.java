package com.lunaixsky.miscmod.enchaments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.MendingEnchantment;

public class AdvancedMending extends MendingEnchantment {
    protected AdvancedMending() {
        super(Rarity.VERY_RARE, EquipmentSlot.values());
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    public int getMinCost(int p_45102_) {
        return (p_45102_ + 1) * 25;
    }

    public int getMaxCost(int p_45105_) {
        return this.getMinCost(p_45105_) + 50;
    }

    public boolean isTreasureOnly() {
        return true;
    }
}
