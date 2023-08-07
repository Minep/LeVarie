package com.lunaixsky.miscmod.mixins;

import com.lunaixsky.miscmod.advancemnets.triggers.AllTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    public void hookPlayerDestroy(Level p_49827_, Player player, BlockPos p_49829_, BlockState blockState, BlockEntity p_49831_, ItemStack p_49832_, CallbackInfo ci) {
        if (blockState.is(Blocks.BEDROCK)) {
            AllTriggers.BEDROCK_TRIGGER.trigger((ServerPlayer) player);
        }
    }
}
