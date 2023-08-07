package com.lunaixsky.miscmod.mixins;

import com.google.common.collect.Multimap;
import com.lunaixsky.miscmod.enchaments.FastAttackEnchantment;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import com.lunaixsky.miscmod.interfaces.IExtendedItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer {

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("TAIL"), cancellable = true)
    public void hookGetAttackDecay(CallbackInfoReturnable<Float> cir) {
        Player this$0 = (Player) (Object) this;
        ItemStack item_mainland = this$0.getItemInHand(InteractionHand.MAIN_HAND);
        FastAttackEnchantment fae = MyEnchantments.FAST_ATTACK.get();
        int lvl = item_mainland.getEnchantmentLevel(fae);
        if (lvl > 0) {
            cir.setReturnValue(cir.getReturnValue() * fae.getAttackSpeedScalingFactor(lvl));
        }
    }
}
