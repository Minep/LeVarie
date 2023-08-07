package com.lunaixsky.miscmod.mixins.villager;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public abstract class MixinWanderingTrader extends AbstractVillager {
    private static final VillagerTrades.ItemListing[] new_trades = new VillagerTrades.ItemListing[] {
            new VillagerTrades.EmeraldForItems(Items.WATER_BUCKET, 1, 2, 1),
            new VillagerTrades.EmeraldForItems(Items.MILK_BUCKET, 1, 2, 1),
            new VillagerTrades.EmeraldForItems(Items.BLAZE_POWDER, 1, 5, 2),
            new VillagerTrades.EmeraldForItems(Items.FERMENTED_SPIDER_EYE, 1, 3, 2),
            new VillagerTrades.EmeraldForItems(Items.WHEAT, 16, 1, 2)
    };

    public MixinWanderingTrader(EntityType<? extends AbstractVillager> p_35267_, Level p_35268_) {
        super(p_35267_, p_35268_);
    }

    @Inject(method = "updateTrades", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/WanderingTrader;addOffersFromItemListings(Lnet/minecraft/world/item/trading/MerchantOffers;[Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;I)V"))
    void hookUpdateTrade(CallbackInfo ci) {
        for (VillagerTrades.ItemListing vt: new_trades) {
            super.offers.add(vt.getOffer(this, this.random));
        }
    }
}
