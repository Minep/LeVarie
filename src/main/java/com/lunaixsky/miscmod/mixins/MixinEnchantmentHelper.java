package com.lunaixsky.miscmod.mixins;

import com.lunaixsky.miscmod.enchaments.AdvancedEfficiency;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    @Inject(method = "getBlockEfficiency", at = @At("RETURN"), cancellable = true)
    private static void hookGetBlockEfficiency(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = entity.getMainHandItem();
        AdvancedEfficiency adveff = MyEnchantments.ADV_DIGGING.get();
        int advdig_level = stack.getEnchantmentLevel(adveff);
        if (advdig_level!=0) {
            cir.setReturnValue(adveff.getEffectiveDigLevel(advdig_level));
        }
    }
}
