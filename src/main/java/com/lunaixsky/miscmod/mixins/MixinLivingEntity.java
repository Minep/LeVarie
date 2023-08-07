package com.lunaixsky.miscmod.mixins;

import com.lunaixsky.miscmod.enchaments.CommonFateEnchantment;
import com.lunaixsky.miscmod.enchaments.DoubleJumpEnchantment;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import com.lunaixsky.miscmod.interfaces.IArgumentedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IArgumentedEntity {
    @Shadow private int noJumpDelay;
    private int midair_jmp_allowance = 0;
    private final Random random = new Random();

    public MixinLivingEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        midair_jmp_allowance = 0;
    }

    @Override
    public BlockPos getBlockPosBelow() {
        return this.getBlockPosBelowThatAffectsMyMovement();
    }

    @Override
    public boolean tryMidAirJump(int maxLevel) {
        if (midair_jmp_allowance < maxLevel) {
            midair_jmp_allowance++;
            return true;
        }
        return false;
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"))
    protected void hookCauseFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_, CallbackInfoReturnable<Boolean> cir) {
        midair_jmp_allowance = 0;
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getMaxHeightFluidType()Lnet/minecraftforge/fluids/FluidType;"))
    protected void hookAiStepOnJumping(CallbackInfo ci) {
        if (!this.isOnGround()) {
            DoubleJumpEnchantment.DoDoubleJump(this);
        }
    }

    @Inject(method = "canBeSeenAsEnemy", at = @At("RETURN"), cancellable = true)
    protected void hookCanBeSeenAsEnemy(CallbackInfoReturnable<Boolean> cir) {
        boolean ret_val = cir.getReturnValue();
        if (ret_val && ((Entity)this) instanceof Player player) {
            ItemStack headitem = player.getItemBySlot(EquipmentSlot.HEAD);
            int lvl = headitem.getEnchantmentLevel(MyEnchantments.FIM.get());
            if (lvl == 0){
                return;
            }

            double prob = Math.min(lvl * 0.1d, 1.0d);
            cir.setReturnValue(super.random.nextDouble() > prob);
        }
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;die(Lnet/minecraft/world/damagesource/DamageSource;)V", ordinal = 0))
    protected void hookOnHurt(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir) {
        if (p_21016_.getEntity() instanceof LivingEntity murderer) {
            if (murderer.isDeadOrDying()) {
                return;
            }

            float maxHealth = murderer.getMaxHealth();
            if (maxHealth > 100) {
                return;
            }

            LivingEntity victim = (LivingEntity)(Object)this;
            ItemStack chest = victim.getItemBySlot(EquipmentSlot.CHEST);
            if (chest.getEnchantmentLevel(MyEnchantments.COMMON_FATE.get()) > 0) {
                murderer.hurt(CommonFateEnchantment.killInNameOf(victim), maxHealth);
            }
        }
    }


}
