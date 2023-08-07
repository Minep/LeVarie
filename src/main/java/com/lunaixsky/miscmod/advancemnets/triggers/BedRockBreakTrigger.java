package com.lunaixsky.miscmod.advancemnets.triggers;

import com.google.gson.JsonObject;
import com.lunaixsky.miscmod.ModInfo;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class BedRockBreakTrigger extends SimpleCriterionTrigger<BedRockBreakTrigger.Instance> {

    static final ResourceLocation ID = ModInfo.getLocalTrigger("break_bedrock");

    @Override
    protected Instance createInstance(JsonObject p_66248_, EntityPredicate.Composite p_66249_, DeserializationContext p_66250_) {
        return new Instance(EntityPredicate.Composite.ANY);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer p_66235_) {
        ItemStack mainhand_item = p_66235_.getMainHandItem();
        int lvl = mainhand_item.getEnchantmentLevel(MyEnchantments.BEDROCK_BREAKER.get());
        this.trigger(p_66235_, instance -> lvl > 0);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public static Instance get() {
            return new Instance(EntityPredicate.Composite.ANY);
        }

        public Instance(EntityPredicate.Composite p_74448_) {
            super(BedRockBreakTrigger.ID, p_74448_);
        }
    }
}
