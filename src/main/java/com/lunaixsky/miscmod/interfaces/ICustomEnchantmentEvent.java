package com.lunaixsky.miscmod.interfaces;

import net.minecraft.world.item.ItemStack;

public interface ICustomEnchantmentEvent {
    void onEnchantmentApplied(ItemStack stack);
}
