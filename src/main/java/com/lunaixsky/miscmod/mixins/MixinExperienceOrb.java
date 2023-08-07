package com.lunaixsky.miscmod.mixins;

import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ExperienceOrb.class)
public abstract class MixinExperienceOrb {

    @Shadow
    public int value;

    @Inject(method = "repairPlayerItems", at = @At(value = "HEAD"), cancellable = true)
    private void hookRepairItem(Player p_147093_, int p_147094_, CallbackInfoReturnable<Integer> cir) {
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(MyEnchantments.ADV_MENDING.get(), p_147093_, ItemStack::isDamaged);
        if (entry != null) {
            ItemStack itemstack = entry.getValue();
            int i = Math.min((int) (this.value * (itemstack.getXpRepairRatio() * 2)), itemstack.getDamageValue());
            itemstack.setDamageValue(itemstack.getDamageValue() - i);
            int j = p_147094_ - this.durabilityToXp(i);
            int retv = j > 0 ? this.repairPlayerItems(p_147093_, j) : 0;
            cir.setReturnValue(retv);
        }
    }

    @Shadow
    abstract int durabilityToXp(int p_20794_);

    @Shadow
    abstract int repairPlayerItems(Player p_147093_, int p_147094_);
}
