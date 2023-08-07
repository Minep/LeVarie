package com.lunaixsky.miscmod.advancemnets;

import com.lunaixsky.miscmod.ModInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class VarieAdvancement {
    private Builder builder;

    protected Advancement built_advancement;

    static final ResourceLocation STONE_BACKGROUND =
            ModInfo.getMinecraftResource("textures/gui/advancements/backgrounds/stone.png");

    protected VarieAdvancement(Builder builder) {
        this.builder = builder;

        Advancement.Builder adv_builder =
                Advancement.Builder.advancement()
                        .display(
                                this.builder.icon,
                                titleKey(),
                                descKey(),
                                this.builder.id.equals("root") ?
                                        STONE_BACKGROUND : null,
                                this.builder.frame,
                                this.builder.toast,
                                this.builder.announce,
                                this.builder.hidden
                        )
                        .rewards(
                                AdvancementRewards.Builder
                                        .experience(this.builder.xp)
                                        .build());

        if (this.builder.parent != null) {
            adv_builder.parent(this.builder.parent);
        }

        int i = 0;
        for (CriterionTriggerInstance cti: this.builder.triggerInstances) {
            adv_builder.addCriterion(Integer.toString(i), cti);
            i++;
        }

        this.built_advancement =
                adv_builder.build(
                        ModInfo.getLocalResource(this.builder.id));
    }

    Component titleKey() {
        return Component.translatable(
                "advancements." +
                        ModInfo.MODID + "." +
                        builder.id +
                        ".title"
        ).withStyle(this.builder.title_sty);
    }

    Component descKey() {
        return Component.translatable(
                "advancements." + ModInfo.MODID + "." + builder.id + ".description"
        ).withStyle(this.builder.desc_sty);
    }

    public void save(Consumer<Advancement> advancementConsumer) {
        advancementConsumer.accept(this.built_advancement);
    }

    public static class Builder {
        Advancement parent;

        private FrameType frame;
        private boolean announce = false;
        private boolean toast = true;
        private boolean hidden = false;
        private int xp = 0;
        private ItemLike icon;
        private String id;
        private List<CriterionTriggerInstance> triggerInstances;

        private UnaryOperator<Style> title_sty = s -> s, desc_sty = s -> s;

        protected Builder(String id) {
            this.id = id;
            this.triggerInstances = new LinkedList<>();
        }

        public static VarieAdvancement.Builder declare(String id) {
            return new VarieAdvancement.Builder(id);
        }

        public VarieAdvancement.Builder withIcon(ItemLike icon) {
            this.icon = icon;
            return this;
        }

        public VarieAdvancement.Builder asChallenge() {
            this.frame = FrameType.CHALLENGE;
            this.announce = true;
            return this;
        }

        public VarieAdvancement.Builder asTask() {
            this.frame = FrameType.TASK;
            this.announce = false;
            return this;
        }

        public VarieAdvancement.Builder asPersonalGoal() {
            this.frame = FrameType.GOAL;
            this.announce = false;
            return this;
        }

        public VarieAdvancement.Builder awardXp(int xp) {
            this.xp = xp;
            return this;
        }

        public VarieAdvancement.Builder asLandmark() {
            this.frame = FrameType.GOAL;
            this.announce = true;
            return this;
        }

        public VarieAdvancement.Builder shouldAnnounce() {
            this.announce = true;
            return this;
        }

        public VarieAdvancement.Builder shouldHidden() {
            this.hidden = true;
            return this;
        }

        public VarieAdvancement.Builder shouldSilent() {
            this.announce = false;
            this.toast = false;
            return this;
        }

        public VarieAdvancement.Builder titleStyle(UnaryOperator<Style> sty) {
            this.title_sty = sty;
            return this;
        }

        public VarieAdvancement.Builder descriptionStyle(UnaryOperator<Style> sty) {
            this.desc_sty = sty;
            return this;
        }

        public VarieAdvancement.Builder addTrigger(CriterionTriggerInstance triggerInstance) {
            this.triggerInstances.add(triggerInstance);
            return this;
        }

        public VarieAdvancement.Builder dependsOn(Advancement parent) {
            this.parent = parent;
            return this;
        }

        public VarieAdvancement.Builder grantForFree() {
            this.triggerInstances.add(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] {}));
            return this;
        }

        public VarieAdvancement build() {
            return new VarieAdvancement(this);
        }
    }
}
