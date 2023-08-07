package com.lunaixsky.miscmod.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public interface IArgumentedEntity {
    BlockPos getBlockPosBelow();

    boolean tryMidAirJump(int maxLevel);
}
