package com.lunaixsky.miscmod.enchaments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NoCreeperEnchantment extends CustomEnchantmentBase{
    protected NoCreeperEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }
}
