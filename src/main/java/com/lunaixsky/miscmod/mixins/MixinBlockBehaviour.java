package com.lunaixsky.miscmod.mixins;

import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import com.lunaixsky.miscmod.interfaces.IArgumentedBlockBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class MixinBlockBehaviour {

    @Inject(method = "getDestroyProgress", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void hookGetDestoryProgress(BlockState p_60466_, Player player, BlockGetter p_60468_, BlockPos p_60469_, CallbackInfoReturnable<Float> cir) {
        int lvl = player.getMainHandItem().getEnchantmentLevel(MyEnchantments.BEDROCK_BREAKER.get());
        if (lvl == 0) {
            return;
        }

        float f = Blocks.OBSIDIAN.defaultBlockState().getDestroySpeed(p_60468_, p_60469_) * 5;
        float speed = player.getDigSpeed(p_60466_, p_60469_) / f;

        cir.setReturnValue(speed);
    }
}
