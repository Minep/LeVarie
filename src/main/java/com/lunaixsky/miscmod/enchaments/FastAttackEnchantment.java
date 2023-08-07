package com.lunaixsky.miscmod.enchaments;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Set;
import java.util.UUID;

public class FastAttackEnchantment extends CustomEnchantmentBase {

    private ImmutableMultimap<Attribute, AttributeModifier>[] attrMods;
    public static final UUID FATTK_MOD_UUID = UUID.fromString("917fe624-1773-4d5c-86c9-53d06e67e1ee");
    protected FastAttackEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
        attrMods = new ImmutableMultimap[getMaxLevel()];
        for (int i = 0; i < getMaxLevel(); i++) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(
                    FATTK_MOD_UUID, "Weapon modifier",
                    (double)getMaxLevel() / (double)(4 * (i + 1)),
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));
            attrMods[i] = builder.build();
        }
    }

    public float getAttackSpeedScalingFactor(int level) {
        return (float)getMaxLevel() / (float)(4 * (level));
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

}
