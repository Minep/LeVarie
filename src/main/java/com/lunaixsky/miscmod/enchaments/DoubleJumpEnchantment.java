package com.lunaixsky.miscmod.enchaments;

import com.lunaixsky.miscmod.interfaces.IArgumentedEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DoubleJumpEnchantment extends CustomEnchantmentBase {
    protected DoubleJumpEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    protected boolean checkCompatibility(Enchantment p_44690_) {
        return !(p_44690_ instanceof DoubleJumpEnchantment);
    }

    public static void DoDoubleJump(Entity entity) {
        IArgumentedEntity argumentedEntity = (IArgumentedEntity) entity;
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;
        if (player.getAbilities().flying || player.isFallFlying()) {
            return;
        }

        ItemStack feetArmor = player.getItemBySlot(EquipmentSlot.FEET);
        int lvl = feetArmor.getEnchantmentLevel(MyEnchantments.DOUBLEJMP.get());
        if (!argumentedEntity.tryMidAirJump(lvl)) {
            return;
        }

        BlockState block_below = player.level.getBlockState(argumentedEntity.getBlockPosBelow());
        if (!block_below.isAir()) {
            return;
        }

        float base_jump_factor = Blocks.STONE.getJumpFactor() * 0.42F;
        double d0 = (double) base_jump_factor + player.getJumpBoostPower();
        Vec3 vec3 = player.getDeltaMovement();
        player.setDeltaMovement(vec3.x, d0, vec3.z);
    }
}
