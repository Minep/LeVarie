package com.lunaixsky.miscmod.enchaments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PurificationEnchantment extends CustomEnchantmentBase {

    protected PurificationEnchantment() {
        super(Rarity.VERY_RARE, MyEnchantments.ALL_EQUIPMENT, EquipmentSlot.values());
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }
}
