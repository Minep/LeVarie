package com.lunaixsky.miscmod.enchaments;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ThornsEnchantment;

public class CommonFateEnchantment extends CustomEnchantmentBase {

    public static class CommonFateDamageSource extends EntityDamageSource {
        public CommonFateDamageSource(Entity murderer) {
            super("common_fate", murderer);
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity p_19397_) {
            String key = "death.attack." + this.msgId;
            if (p_19397_ == null) {
                return Component.translatable(key, p_19397_.getDisplayName());
            }
            return Component.translatable(key + ".cause", p_19397_.getDisplayName(), this.entity.getDisplayName());
        }
    }
    public static DamageSource killInNameOf(LivingEntity entity) {
        return new CommonFateDamageSource(entity).bypassArmor().bypassEnchantments();
    }
    public CommonFateEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinCost(int p_44679_) {
        return 50;
    }

    @Override
    protected boolean checkCompatibility(Enchantment p_44690_) {
        return !(p_44690_ instanceof ThornsEnchantment) && super.checkCompatibility(p_44690_);
    }

    @Override
    public boolean isTradeable() {
        return false;
    }
}
