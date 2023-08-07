package com.lunaixsky.miscmod.behavior;

import com.lunaixsky.miscmod.advancemnets.triggers.AllTriggers;
import com.lunaixsky.miscmod.interfaces.IArgumentedVillager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class VillagerQualityOfLife {

    private static final int BEDSEARCH_XZ_RANGE = 4;

    private static final double SKY_LIGHT_WEIGHT = 2.d;
    private static final double BLOCK_LIGHT_WEIGHT = 0.6d;
    private static final double FREE_SPACE_WEIGHT = 0.8d;
    private static final double HUNGRY_WEIGHT = 0.4d;
    private static final double HEALTH_WEIGHT = 0.9d;
    private static final double NEIGHBOR_WEIGHT = .5d;
    private static final double SOCIAL_WEIGHT = 1.d;
    private static final double SELF_DEVELOPMENT_WEIGHT = .2d;

    private static final double PHYSICAL_WEIGHT = 5.d;
    private static final double MATERIAL_WEIGHT = 0.6d;
    private static final double SAFETY_WEIGHT = .8d;
    private static final double SOCIAL_INTERACTION_WEIGHT = .5d;
    private static final double QOL_BONUS_FLOWER_WEIGHT = 0.5d;
    private static final double QOL_BONUS_FOOD_WEIGHT = 0.7d;
    private static final double SAFETY_CURED_TIME_WEIGHT = 2d;
    private static final double SAFETY_NUM_OF_ZOMBIE = 0.5d;
    private static final double RAMP_RAISE_PRICE = 0.7d;
    private static final double RAMP_NORMAL_PRICE = 0.8d;
    private static final int QoLExtraBaseCost = 64;
    private static final int QoLExtraDiscountCost = 8;


    private Villager villager;
    private IArgumentedVillager iavillager;
    private static TargetingConditions isHostile = TargetingConditions.forCombat().range(16d).ignoreLineOfSight();

    private double material_state_factor;
    private double physical_state_factor;
    private double safety_state_factor;
    private double social_state_factor;
    private double self_state_factor;
    private double qol_scoring;

    private double qol_bonus;

    public VillagerQualityOfLife(Villager villager) {
        this.villager = villager;
        this.iavillager = (IArgumentedVillager) villager;
    }

    public void update() {
        BlockPos villager_pos = this.villager.blockPosition();
        qol_bonus = 0d;

        this.updateMaterial(villager_pos);
        this.updatePhysical(villager_pos);
        this.updateSafety(villager_pos);
        this.updateSocialRelationship(villager_pos);
        this.updateSelfDevelopment(villager_pos);

        this.qol_scoring =
                MATERIAL_WEIGHT * material_state_factor +
                PHYSICAL_WEIGHT * physical_state_factor +
                SAFETY_WEIGHT * safety_state_factor +
                SOCIAL_INTERACTION_WEIGHT * social_state_factor +
                SELF_DEVELOPMENT_WEIGHT * self_state_factor;
        this.qol_scoring = this.qol_scoring / (
                MATERIAL_WEIGHT + PHYSICAL_WEIGHT + SAFETY_WEIGHT + SOCIAL_INTERACTION_WEIGHT + SELF_DEVELOPMENT_WEIGHT);

        this.qol_bonus = this.qol_bonus / (QOL_BONUS_FLOWER_WEIGHT);

        if (this.villager.level.isNight()) {
            this.qol_scoring *= 0.6;
            this.qol_bonus *= 0.3;
        }

    }

    public double getQolScoring() {
        return qol_scoring;
    }

    public void setQolScoring(double qol) {
        qol_scoring = qol;
    }

    public double getQolBonus() {
        return qol_bonus;
    }

    public void setQolBonus(double qol_bonus) {
        this.qol_bonus = qol_bonus;
    }

    public int getQoLAffectedPriceDiff() {
        int price_diff = 0;
        if (qol_scoring <= RAMP_RAISE_PRICE) {
            price_diff = (int) Math.floor(QoLExtraBaseCost * (1d - qol_scoring / RAMP_RAISE_PRICE));
        } else if (qol_scoring <= RAMP_NORMAL_PRICE) {
            price_diff = 0;
        } else {
            price_diff = -(int) Math.floor(QoLExtraBaseCost * (qol_scoring - RAMP_NORMAL_PRICE));
        }

        price_diff -= (int) Math.floor(QoLExtraDiscountCost * qol_bonus);

        return price_diff;
    }

    public void tryAwardAdvancement(ServerPlayer player) {
        if (qol_scoring <= RAMP_RAISE_PRICE) {
            AllTriggers.LOW_QOL_TRIGGER.trigger(player);
        }
        else if (qol_scoring >= RAMP_RAISE_PRICE && qol_scoring < 0.98) {
            AllTriggers.HIGH_QOL_TRIGGER.trigger(player);
        }
        else if (qol_scoring >= 0.98 && qol_bonus >= 0.98){
            AllTriggers.HIGH_QOL_BONUS_TRIGGER.trigger(player);
        }
    }

    public boolean canAccessBed(BlockPos location) {
        Optional<GlobalPos> bed_pos = this.villager.getBrain().getMemory(MemoryModuleType.HOME);
        return bed_pos.map(x -> {
            BlockPos pos = x.pos();
            if (pos.distManhattan(location) < BEDSEARCH_XZ_RANGE * 2) {
                return this.villager.level.getBlockState(pos).isBed(this.villager.level, pos, this.villager);
            }
            return false;
        }).orElse(false);
    }

    public int foreachAccessibleBlockPos(BlockPos location, BiConsumer<BlockPos, Boolean> callback) {
        int vx = location.getX();
        int vy = location.getY();
        int vz = location.getZ();

        BlockPos.MutableBlockPos loc = new BlockPos.MutableBlockPos(
                vx, vy, vz
        );

        boolean[] walls = new boolean[]{true, true, true, true};
        int coverage = 0;
        for (int o = 0; o < BEDSEARCH_XZ_RANGE; o++) {
            for (int j = 0; j < 4; j++) {
                if (!walls[j]) {
                    continue;
                }
                boolean all_blocked = true;
                for (int i = -o; i < o + 1; i++) {
                    if (j % 2 == 0) {
                        loc.set(vx + i, vy, vz + o * (j - 1));
                    }
                    else {
                        loc.set(vx + o * (j - 2), vy, vz + i);
                    }
                    boolean isfree = villager.level.getBlockState(loc).isAir();
                    isfree = isfree && villager.level.getBlockState(loc.above()).isAir();
                    isfree = isfree && !villager.level.getBlockState(loc.below()).isAir();
                    if (isfree) {
                        coverage++;
                    }
                    callback.accept(loc, isfree);
                    all_blocked = all_blocked && !isfree;
                }
                walls[j] = !all_blocked;
            }
        }

        return coverage;
    }

    public void updateMaterial(BlockPos location) {
        double no_bed_scale = 1d;
        if (!canAccessBed(location)) {
            no_bed_scale = 0.2d;
        }

        int sky_light = villager.level.getBrightness(LightLayer.SKY, location);
        int blk_light = villager.level.getBrightness(LightLayer.BLOCK, location);
        final AtomicInteger num_of_flower = new AtomicInteger();
        double fsr = foreachAccessibleBlockPos(location, (pos, is_walkable) -> {
            if (is_walkable) {
                return;
            }
            BlockState bs = villager.level.getBlockState(pos.above());
            if (bs.getBlock() instanceof FlowerPotBlock potBlock) {
                if (potBlock.getContent() instanceof FlowerBlock) {
                    num_of_flower.incrementAndGet();
                }
            }
        });

        int side = BEDSEARCH_XZ_RANGE * 2;
        side = side * side;
        fsr = Math.min(fsr, side);
        fsr = fsr / (double) (side);

        qol_bonus += QOL_BONUS_FLOWER_WEIGHT * Math.min(num_of_flower.doubleValue(), 10d) / 10d;

        material_state_factor =
                SKY_LIGHT_WEIGHT * ((double) sky_light / 15d) +
                BLOCK_LIGHT_WEIGHT * ((double) blk_light / 15d) +
                FREE_SPACE_WEIGHT * fsr;
        material_state_factor = material_state_factor / (SKY_LIGHT_WEIGHT + BLOCK_LIGHT_WEIGHT + FREE_SPACE_WEIGHT);
        material_state_factor *= no_bed_scale;
    }

    public void updatePhysical(BlockPos location) {
        int food_lvl = iavillager.getFoodLevel();
        double hungry = (double) Math.min(food_lvl, 12) / 12d;
        double food_excess = food_lvl > 12 ? food_lvl - 12d  : 0d;
        food_excess = Math.min(food_excess, 64d) / 64d;
        double health = this.villager.getHealth() / this.villager.getMaxHealth();

        physical_state_factor =
                HUNGRY_WEIGHT * hungry +
                HEALTH_WEIGHT * health;
        physical_state_factor = physical_state_factor / (HUNGRY_WEIGHT + HEALTH_WEIGHT);

        qol_bonus += QOL_BONUS_FOOD_WEIGHT * food_excess;
    }

    public void updateSelfDevelopment(BlockPos location) {
        int level = villager.getVillagerData().getLevel();
        self_state_factor = (double) level / 5d;
    }

    public void updateSafety(BlockPos location) {
        int num_of_neighbor_zombie =
                this.villager.level.getNearbyEntities(
                        Zombie.class,
                        this.isHostile,
                        this.villager,
                        this.villager.getBoundingBox().inflate(16d, 2d, 16d)).size();

        safety_state_factor = SAFETY_CURED_TIME_WEIGHT / ((double) iavillager.getCureTime() + 1d);
        safety_state_factor += SAFETY_NUM_OF_ZOMBIE * (1d - Math.min(num_of_neighbor_zombie, 8d) / 8d);
        safety_state_factor /= (SAFETY_CURED_TIME_WEIGHT + SAFETY_NUM_OF_ZOMBIE);
    }

    public void updateSocialRelationship(BlockPos blockPos) {
        int num_of_neighbor =
                this.villager.level.getNearbyEntities(
                        Villager.class,
                        TargetingConditions.forNonCombat().range(16d),
                        this.villager,
                        this.villager.getBoundingBox().inflate(16d, 4d, 16d)).size();
        double nosocial_scale = (villager.level.getGameTime() - iavillager.getLastGossipTime());
        nosocial_scale = 1d - (Math.min(nosocial_scale, 24000d) / 24000d);

        social_state_factor =
                SOCIAL_WEIGHT * nosocial_scale +
                NEIGHBOR_WEIGHT * (Math.min(num_of_neighbor, 3) / 3d);

        social_state_factor = social_state_factor / (SOCIAL_WEIGHT + NEIGHBOR_WEIGHT);
    }
}
