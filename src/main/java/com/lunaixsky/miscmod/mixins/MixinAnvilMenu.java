package com.lunaixsky.miscmod.mixins;

import com.lunaixsky.miscmod.enchaments.AbsorbingEnchantment;
import com.lunaixsky.miscmod.enchaments.FastAttackEnchantment;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import com.lunaixsky.miscmod.enchaments.PurificationEnchantment;
import com.lunaixsky.miscmod.interfaces.IExtendedItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {
    @Shadow
    @Final
    private DataSlot cost;

    @Shadow
    public abstract void createResult();


    public MixinAnvilMenu(@Nullable MenuType<?> p_39773_, int p_39774_, Inventory p_39775_, ContainerLevelAccess p_39776_) {
        super(p_39773_, p_39774_, p_39775_, p_39776_);
    }

    @Inject(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 0), cancellable = true)
    protected void hookOnTake(Player p_150474_, ItemStack p_150475_, CallbackInfo ci) {
        ItemStack stack2 = this.inputSlots.getItem(1);

        boolean stk2_has_absorb = MyEnchantments.GetEnchantmentLevelUniversal(MyEnchantments.ABSORBING.get(), stack2) != 0;

        if (!stk2_has_absorb) {
            return;
        }

        ItemStack stack1 = this.inputSlots.getItem(0);
        if (EnchantmentHelper.getEnchantments(stack1).isEmpty() && stk2_has_absorb) {
            return;
        }

        if (stack1.getItem() == Items.ENCHANTED_BOOK) {
            stack1 = new ItemStack(Items.BOOK);
            this.inputSlots.setItem(0, stack1);
        }
        else {
            EnchantmentHelper.setEnchantments(Map.of(), stack1);
            stack1.setRepairCost(0);
        }

        this.inputSlots.setItem(1, ItemStack.EMPTY);

        this.cost.set(0);
        this.access.execute((p_150479_, p_150480_) -> {
            BlockState blockstate = p_150479_.getBlockState(p_150480_);
            if (!p_150474_.getAbilities().instabuild && blockstate.is(BlockTags.ANVIL) && p_150474_.getRandom().nextFloat() < 0.5) {
                BlockState blockstate1 = AnvilBlock.damage(blockstate);
                if (blockstate1 == null) {
                    p_150479_.removeBlock(p_150480_, false);
                    p_150479_.levelEvent(1029, p_150480_, 0);
                } else {
                    p_150479_.setBlock(p_150480_, blockstate1, 2);
                    p_150479_.levelEvent(1030, p_150480_, 0);
                }
            } else {
                p_150479_.levelEvent(1030, p_150480_, 0);
            }

        });

        ci.cancel();
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void prologueCreateResult(CallbackInfo ci) {
        ItemStack stack1 = this.inputSlots.getItem(0);
        ItemStack stack2 = this.inputSlots.getItem(1);
        if (stack1.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
            ci.cancel();
        }

        ItemStack stack_out;
        Map<Enchantment, Integer> enchantments_out;

        if (this.hasEnchantmentInAny(MyEnchantments.ABSORBING.get(), stack1, stack2)) {
            boolean is_enchantedBook = stack2.getItem() == Items.ENCHANTED_BOOK;
            stack_out = is_enchantedBook ? new ItemStack(Items.ENCHANTED_BOOK) : stack2.copy();
            enchantments_out = __applyAbsorbing(stack1, stack_out);
        } else if (this.hasEnchantmentInAny(MyEnchantments.PURIFIED.get(), stack1, stack2)) {
            boolean is_enchantedBook = stack1.getItem() == Items.ENCHANTED_BOOK;
            stack_out = is_enchantedBook ? new ItemStack(Items.ENCHANTED_BOOK) : stack1.copy();
            enchantments_out = __applyPurification(stack1, stack_out);
        }
        else {
            return;
        }

        assert stack_out != null && enchantments_out != null;

        if (!enchantments_out.isEmpty()) {
            EnchantmentHelper.setEnchantments(enchantments_out, stack_out);
        }
        else {
            this.cost.set(0);
            return;
        }

        stack_out.setRepairCost(stack1.getBaseRepairCost());
        __do_discount(stack_out);

        if (this.cost.get() >= 40 && !this.player.getAbilities().instabuild) {
            stack_out = ItemStack.EMPTY;
        }

        this.resultSlots.setItem(0, stack_out);
        this.broadcastChanges();
        ci.cancel();
    }

    private boolean hasEnchantmentInAny(Enchantment enchantment, ItemStack stack1, ItemStack stack2) {
        boolean stk1_has = MyEnchantments.GetEnchantmentLevelUniversal(enchantment, stack1) != 0;
        boolean stk2_has = MyEnchantments.GetEnchantmentLevelUniversal(enchantment, stack2) != 0;

        return stk2_has || stk1_has;
    }

    private Map<Enchantment, Integer> __applyAbsorbing(ItemStack src_stack, ItemStack stack_out) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(src_stack);
        Map<Enchantment, Integer> enchantments_out = new LinkedHashMap<>();
        for (Enchantment e:
                enchantments.keySet()) {
            if (e == null || e instanceof AbsorbingEnchantment) {
                continue;
            }
            if (!(stack_out.getItem() instanceof EnchantedBookItem) && !e.canEnchant(stack_out)) {
                continue;
            }
            int level = enchantments.getOrDefault(e, 0);
            enchantments_out.put(e, level);
        }

        this.cost.set(src_stack.getBaseRepairCost());

        return enchantments_out;
    }

    private Map<Enchantment, Integer> __applyPurification(ItemStack src_stacl, ItemStack stack_out) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(src_stacl);
        Map<Enchantment, Integer> enchantments_out = new LinkedHashMap<>();
        for (Enchantment e:
                enchantments.keySet()) {
            if (e == null || e instanceof PurificationEnchantment) {
                continue;
            }
            if (!e.canEnchant(stack_out) || e.isCurse()) {
                continue;
            }
            int level = enchantments.getOrDefault(e, 0);
            enchantments_out.put(e, level);
        }

        this.cost.set(39);
        return enchantments_out;
    }

    @Inject(method = "createResult", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V"))
    public void hookCreateResult(CallbackInfo ci) {
        ItemStack stack1 = this.resultSlots.getItem(0);
        
        this.__do_discount(stack1);
    }

    private void __do_discount(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        boolean has_harmony = MyEnchantments.GetEnchantmentLevelUniversal(MyEnchantments.HARMONY.get(), stack) != 0;

        int _cost = this.cost.get();
        int _repair_cost = stack.getBaseRepairCost();

        if (has_harmony) {
            _cost = Math.min(_cost, 39);
            _repair_cost = Math.min(_repair_cost, 39);
            this.cost.set(_cost);
        }

        stack.setRepairCost(_repair_cost);
    }
}
