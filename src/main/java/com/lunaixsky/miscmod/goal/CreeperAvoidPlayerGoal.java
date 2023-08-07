package com.lunaixsky.miscmod.goal;

import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;

public class CreeperAvoidPlayerGoal extends AvoidEntityGoal<Player> {
    public CreeperAvoidPlayerGoal(PathfinderMob p_25027_, float p_25029_, double p_25030_, double p_25031_) {
        super(p_25027_, Player.class, p_25029_, p_25030_, p_25031_);
    }

    @Override
    public boolean canUse() {
        if (super.canUse()) {
            int lvl = this.toAvoid.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(MyEnchantments.NOCREEPER.get());
            return lvl > 0;
        }
        return false;
    }
}
