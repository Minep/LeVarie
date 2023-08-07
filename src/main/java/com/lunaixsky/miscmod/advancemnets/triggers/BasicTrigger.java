package com.lunaixsky.miscmod.advancemnets.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public class BasicTrigger extends SimpleCriterionTrigger<BasicTrigger.Instance> {

    private ResourceLocation trigger_id;

    public BasicTrigger(ResourceLocation trigger_id) {
        this.trigger_id = trigger_id;
    }

    @Override
    protected Instance createInstance(JsonObject p_66248_, EntityPredicate.Composite p_66249_, DeserializationContext p_66250_) {
        return new Instance(trigger_id, EntityPredicate.Composite.ANY);
    }

    public Instance createInstance() {
        return new Instance(trigger_id, EntityPredicate.Composite.ANY);
    }

    public void trigger(ServerPlayer p_66235_) {
        this.trigger(p_66235_, x -> true);
    }

    @Override
    public ResourceLocation getId() {
        return trigger_id;
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public static BasicTrigger.Instance get(ResourceLocation id) {
            return new BasicTrigger.Instance(id, EntityPredicate.Composite.ANY);
        }

        public Instance(ResourceLocation id, EntityPredicate.Composite p_74448_) {
            super(id, p_74448_);
        }
    }
}
