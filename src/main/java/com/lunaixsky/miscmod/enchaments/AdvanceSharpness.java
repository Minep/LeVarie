package com.lunaixsky.miscmod.enchaments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.DamageEnchantment;

public class AdvanceSharpness extends DamageEnchantment {
    public AdvanceSharpness() {
        super(Rarity.RARE, 0, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int p_44633_) {
        return super.getMinCost(super.getMaxLevel()) * (p_44633_ + 1);
    }

    @Override
    public float getDamageBonus(int p_44635_, MobType p_44636_) {
        return super.getDamageBonus(super.getMaxLevel() + 4 * p_44635_, p_44636_);
    }
}
