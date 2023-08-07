package com.lunaixsky.miscmod.advancemnets.predicates;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Arrays;
import java.util.function.Function;

public class AllPredicates {

    public static EnchantmentPredicate[] withEnchantment(Enchantment e, MinMaxBounds.Ints level) {
        return new EnchantmentPredicate[] {
                new EnchantmentPredicate(e, level)
        };
    }
}
