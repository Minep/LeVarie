package com.lunaixsky.miscmod.mixins.villager;

import com.lunaixsky.miscmod.interfaces.IArgumentedVillager;
import com.lunaixsky.miscmod.interfaces.IExtendedZombieVillager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ZombieVillager.class)
public abstract class MixinZombieVillager implements IExtendedZombieVillager {
    private int villager_cured_time;


    @Inject(method = "finishConversion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;setVillagerXp(I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    void hookConvertFinished(ServerLevel p_34399_, CallbackInfo ci, Villager villager) {
        if (villager instanceof IArgumentedVillager iav) {
            iav.setCureTime(this.villager_cured_time);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void hookAddAdditionSaveData(CompoundTag p_35481_, CallbackInfo ci) {
        p_35481_.putInt("v_cured_times", this.villager_cured_time);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void hookReadAdditionalSaveData(CompoundTag p_35451_, CallbackInfo ci) {
        this.villager_cured_time = p_35451_.getInt("v_cured_times");
    }

    @Override
    public void setVillagerCureTime(int cureTime) {
        villager_cured_time = cureTime;
    }
}
