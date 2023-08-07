package com.lunaixsky.miscmod.mixins.villager;

import com.lunaixsky.miscmod.advancemnets.triggers.AllTriggers;
import com.lunaixsky.miscmod.behavior.VillagerQualityOfLife;
import com.lunaixsky.miscmod.interfaces.IArgumentedVillager;
import com.lunaixsky.miscmod.utils.DelayedExecution;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Villager.class)
public abstract class MixinVillager extends AbstractVillager implements IArgumentedVillager {

    private int cured_times = 0;
    private final VillagerQualityOfLife qol_manager = new VillagerQualityOfLife((Villager) (Object) this);

    public MixinVillager(EntityType<? extends AbstractVillager> p_35267_, Level p_35268_) {
        super(p_35267_, p_35268_);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void hookAddAdditionSaveData(CompoundTag p_35481_, CallbackInfo ci) {
        p_35481_.putInt("cured_times", this.cured_times);
        p_35481_.putDouble("qol_score", qol_manager.getQolScoring());
        p_35481_.putDouble("qol_bonus", qol_manager.getQolBonus());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void hookReadAdditionalSaveData(CompoundTag p_35451_, CallbackInfo ci) {
        this.cured_times = p_35451_.getInt("cured_times");
        this.qol_manager.setQolScoring(p_35451_.getDouble("qol_score"));
        this.qol_manager.setQolBonus(p_35451_.getDouble("qol_bonus"));
    }

    @Inject(method = "onReputationEventFrom", at = @At("HEAD"), cancellable = true)
    public void hookReceiveReputationEvent(ReputationEventType p_35431_, Entity p_35432_, CallbackInfo ci) {
        if (p_35431_.equals(ReputationEventType.ZOMBIE_VILLAGER_CURED) && !canBeGrateful()) {
            if (p_35432_ instanceof ServerPlayer player && !this.level.isClientSide()) {
                AllTriggers.BAD_CURE_TRIGGER.trigger(player);
            }
            ci.cancel();
        }
    }

    @Inject(method = "startTrading", at = @At("HEAD"))
    public void hookBeginTrading(Player p_35537_, CallbackInfo ci) {
        qol_manager.update();
        for (MerchantOffer offer: super.getOffers()) {
            int extra_cost = (int) Math.floor(qol_manager.getQoLAffectedPriceDiff() * offer.getPriceMultiplier());
            offer.addToSpecialPriceDiff(extra_cost);
        }
        if (!this.level.isClientSide()) {
            qol_manager.tryAwardAdvancement((ServerPlayer) p_35537_);
        }
    }

    @Override
    public boolean canBeGrateful() {
        cured_times++;
        if (cured_times <= 1) {
            return true;
        }
        return false;
    }

    @Override
    public int getCureTime() {
        return cured_times;
    }

    @Override
    public void setCureTime(int ct) {
        this.cured_times = ct;
    }

    @Shadow
    private long lastGossipTime;

    @Shadow
    abstract int countFoodPointsInInventory();

    @Override
    public int getFoodLevel() {
        return this.countFoodPointsInInventory();
    }

    @Override
    public long getLastGossipTime() {
        return lastGossipTime;
    }

    private void updateComfortValue() {
        this.qol_manager.update();
    }
}
