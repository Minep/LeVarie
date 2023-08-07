package com.lunaixsky.miscmod.enchaments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FIMEnchantment extends CustomEnchantmentBase {
    protected FIMEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int p_44679_) {
        return p_44679_ * 25;
    }

    @Override
    public int getMaxCost(int p_44679_) {
        return p_44679_ * 25 + 50;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }


    @Override
    public boolean isDiscoverable() {
        return true;
    }
}
