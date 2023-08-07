package com.lunaixsky.miscmod.mixins;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowerPotBlock.class)
public class MixinFlowerPotFix {

    @Shadow
    @Final
    private Block content;

    @Inject(method = "isEmpty", at = @At("HEAD"), cancellable = true)
    private void hookIsEmpty(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(content == null || content == Blocks.AIR);
    }
}
