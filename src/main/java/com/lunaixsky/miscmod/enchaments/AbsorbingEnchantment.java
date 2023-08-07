package com.lunaixsky.miscmod.enchaments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class AbsorbingEnchantment extends CustomEnchantmentBase {
    protected AbsorbingEnchantment() {
        super(Rarity.VERY_RARE, MyEnchantments.ALL_EQUIPMENT, EquipmentSlot.values());
    }

    @Override
    protected boolean checkCompatibility(Enchantment p_44690_) {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }
}
