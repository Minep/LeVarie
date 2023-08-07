package com.lunaixsky.miscmod.enchaments;

import com.lunaixsky.miscmod.ModInfo;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class MyEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENT = DeferredRegister.create(ForgeRegistries.Keys.ENCHANTMENTS, ModInfo.MODID);
    public static final RegistryObject<HarmonyEnchament> HARMONY = ENCHANTMENT.register("harmony", HarmonyEnchament::new);
    public static final RegistryObject<AbsorbingEnchantment> ABSORBING = ENCHANTMENT.register("absorbing", AbsorbingEnchantment::new);
    public static final RegistryObject<PurificationEnchantment> PURIFIED = ENCHANTMENT.register("purified", PurificationEnchantment::new);
    public static final RegistryObject<AdvancedProtection> ADVPROCT = ENCHANTMENT.register("adv_protection", AdvancedProtection::new);
    public static final RegistryObject<AdvanceSharpness> ADVSHARP = ENCHANTMENT.register("adv_sharpness", AdvanceSharpness::new);
    public static final RegistryObject<DoubleJumpEnchantment> DOUBLEJMP = ENCHANTMENT.register("double_jump", DoubleJumpEnchantment::new);
    public static final RegistryObject<AdvancedEfficiency> ADV_DIGGING = ENCHANTMENT.register("adv_digging", AdvancedEfficiency::new);
    public static final RegistryObject<AdvancedMending> ADV_MENDING = ENCHANTMENT.register("adv_mending", AdvancedMending::new);
    public static final RegistryObject<BedrockBreaker> BEDROCK_BREAKER = ENCHANTMENT.register("bedrock_breaker", BedrockBreaker::new);
    public static final RegistryObject<FIMEnchantment> FIM = ENCHANTMENT.register("friendship_is_magic", FIMEnchantment::new);
    public static final RegistryObject<FastAttackEnchantment> FAST_ATTACK = ENCHANTMENT.register("fast_attack", FastAttackEnchantment::new);
    public static final RegistryObject<CommonFateEnchantment> COMMON_FATE = ENCHANTMENT.register("common_fate", CommonFateEnchantment::new);
    public static final RegistryObject<NoCreeperEnchantment> NOCREEPER = ENCHANTMENT.register("no_creeper", NoCreeperEnchantment::new);

    public static final EnchantmentCategory ALL_EQUIPMENT = EnchantmentCategory.create(
            "miscmod_all_category",
            (item) -> item instanceof DiggerItem ||
                    item instanceof SwordItem ||
                    item instanceof ArmorItem ||
                    item instanceof FishingRodItem ||
                    item instanceof ProjectileWeaponItem
    );

    public static final EnchantmentCategory NEITHERITE_PICKAXE = EnchantmentCategory.create(
            "miscmod_pickaxe",
            (item) -> {
                if (item instanceof PickaxeItem pickaxeItem) {
                    return pickaxeItem.getTier().equals(Tiers.NETHERITE);
                }
                return false;
            }
    );


    public static final EquipmentSlot[] MAINHAND_SLOT = new EquipmentSlot[] {
            EquipmentSlot.MAINHAND
    };

    public static final EquipmentSlot[] HEAD_SLOT = new EquipmentSlot[] {
            EquipmentSlot.HEAD
    };

    public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public static void Register(IEventBus eventBus) {
        addEnchantmentTypeToItemGroup(CreativeModeTab.TAB_TOOLS, NEITHERITE_PICKAXE);
        ENCHANTMENT.register(eventBus);
    }

    public static int GetEnchantmentLevelUniversal(Enchantment enchantment, ItemStack itemStack) {
        Map<Enchantment, Integer> es = EnchantmentHelper.getEnchantments(itemStack);
        for (Enchantment e: es.keySet()) {
            if (e != null && e.getClass().isAssignableFrom(enchantment.getClass())) {
                return es.getOrDefault(e, 0);
            }
        }
        return 0;
    }

    public static void addEnchantmentTypeToItemGroup(CreativeModeTab itemTab, EnchantmentCategory category) {
        EnchantmentCategory[] group = itemTab.getEnchantmentCategories();
        if (group.length == 0) {
            itemTab.setEnchantmentCategories(category);
        } else {
            EnchantmentCategory[] temporary = new EnchantmentCategory[group.length + 1];
            System.arraycopy(group, 0, temporary, 0, group.length);
            temporary[group.length - 1] = category;
            itemTab.setEnchantmentCategories(temporary);
        }
    }

}
