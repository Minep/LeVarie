package com.lunaixsky.miscmod.interfaces;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public interface IExtendedItemStack {
    void addAttributeModifierSafe(Attribute attribute, AttributeModifier modifier, EquipmentSlot slot);

    static IExtendedItemStack cast(ItemStack stack) {
        return (IExtendedItemStack) (Object) stack;
    }
}
