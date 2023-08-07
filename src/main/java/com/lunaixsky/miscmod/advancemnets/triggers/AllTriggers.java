package com.lunaixsky.miscmod.advancemnets.triggers;

import com.lunaixsky.miscmod.ModInfo;
import com.lunaixsky.miscmod.advancemnets.predicates.AllPredicates;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Function;

public class AllTriggers {
    public static final BedRockBreakTrigger BEDROCK_TRIGGER = new BedRockBreakTrigger();

    public static final BasicTrigger LOW_QOL_TRIGGER = new BasicTrigger(ModInfo.getLocalTrigger("low_qol"));
    public static final BasicTrigger HIGH_QOL_TRIGGER = new BasicTrigger(ModInfo.getLocalTrigger("high_qol"));
    public static final BasicTrigger HIGH_QOL_BONUS_TRIGGER = new BasicTrigger(ModInfo.getLocalTrigger("high_qol_bonus"));
    public static final BasicTrigger BAD_CURE_TRIGGER = new BasicTrigger(ModInfo.getLocalTrigger("bad_cure"));

    public static final InventoryChangeTrigger.TriggerInstance enchantmentInInventory(Enchantment e, MinMaxBounds.Ints level, MinMaxBounds.Ints count) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(
                new ItemPredicate(
                        null,
                        null,
                        count,
                        MinMaxBounds.Ints.ANY,
                        AllPredicates.withEnchantment(e, level),
                        EnchantmentPredicate.NONE,
                        null,
                        NbtPredicate.ANY
                )
        );
    }

    public static void register() {
        CriteriaTriggers.register(BEDROCK_TRIGGER);
        CriteriaTriggers.register(LOW_QOL_TRIGGER);
        CriteriaTriggers.register(HIGH_QOL_TRIGGER);
        CriteriaTriggers.register(HIGH_QOL_BONUS_TRIGGER);
        CriteriaTriggers.register(BAD_CURE_TRIGGER);
    }
}
