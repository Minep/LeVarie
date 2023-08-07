package com.lunaixsky.miscmod.events;

import com.lunaixsky.miscmod.enchaments.FastAttackEnchantment;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class ForgeItemStackEvent {

    @SubscribeEvent
    public void onItemToolTipShown(ItemTooltipEvent tooltipEvent) {
        ItemStack stack = tooltipEvent.getItemStack();
        List<Component> tooltips = tooltipEvent.getToolTip();

        FastAttackEnchantment attackEnchantment = MyEnchantments.FAST_ATTACK.get();
        int lvl = stack.getEnchantmentLevel(attackEnchantment);
        if (lvl > 0) {
            float d1 = attackEnchantment.getAttackSpeedScalingFactor(lvl) * 100;
            tooltips.add(CommonComponents.EMPTY);
            tooltips.add(Component.translatable("item.miscmod.extra_info." + EquipmentSlot.MAINHAND.getName()).withStyle(ChatFormatting.GRAY));

            tooltips.add(
                    Component.translatable(
                            attackEnchantment.getDescriptionId() + ".tooltip",
                            ATTRIBUTE_MODIFIER_FORMAT.format(d1)).withStyle(ChatFormatting.AQUA));
        }
    }
}
