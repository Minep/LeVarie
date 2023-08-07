package com.lunaixsky.miscmod.enchaments;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class AdvancedProtection extends ProtectionEnchantment {
    public AdvancedProtection() {
        super(Rarity.VERY_RARE, ProtectionEnchantment.Type.ALL, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getDamageProtection(int p_44680_, DamageSource p_44681_) {
        return super.getDamageProtection(super.getMaxLevel() + p_44680_ + 1, p_44681_);
    }

    @Override
    public boolean checkCompatibility(Enchantment p_44690_) {
        return super.checkCompatibility(p_44690_);
    }

    @Override
    public int getMinCost(int p_44679_) {
        return super.getMinCost(super.getMaxLevel()) * p_44679_;
    }
}
