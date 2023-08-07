package com.lunaixsky.miscmod.mixins.villager;

import com.lunaixsky.miscmod.interfaces.IArgumentedVillager;
import com.lunaixsky.miscmod.interfaces.IExtendedZombieVillager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Zombie.class)
public class MixinZombie {

    @Inject(method = "wasKilled", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;onLivingConvert(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    void hookWasKilled(ServerLevel p_219160_, LivingEntity p_219161_, CallbackInfoReturnable<Boolean> cir, boolean flag, Villager villager, ZombieVillager zombievillager) {
        if (villager instanceof IArgumentedVillager iav) {
            if (zombievillager instanceof IExtendedZombieVillager iezv) {
                iezv.setVillagerCureTime(iav.getCureTime());
            }
        }
    }
}
