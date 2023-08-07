package com.lunaixsky.miscmod;

import com.google.common.collect.Multimap;
import com.lunaixsky.miscmod.advancemnets.AllAdvancements;
import com.lunaixsky.miscmod.advancemnets.triggers.AllTriggers;
import com.lunaixsky.miscmod.enchaments.FastAttackEnchantment;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import com.lunaixsky.miscmod.events.ForgeEvent;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.swing.text.html.parser.Entity;
import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModInfo.MODID)
public class MiscMod
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModInfo.MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModInfo.MODID);

    public MiscMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(MiscMod::gatherData);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        MyEnchantments.Register(modEventBus);

        IEventBus minecraftEventBus = MinecraftForge.EVENT_BUS;
        // Register ourselves for server and other game events we are interested in
        minecraftEventBus.register(this);
        ForgeEvent.register(minecraftEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        AllTriggers.register();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    //@SubscribeEvent
//    public void onItemChange(LivingEquipmentChangeEvent evt) {
//        LivingEntity e = evt.getEntity();
//        ItemStack item = evt.getTo();
//        ItemStack old = evt.getFrom();
//        if (evt.getSlot() == EquipmentSlot.MAINHAND) {
//            int lvl = item.getEnchantmentLevel(MyEnchantments.FAST_ATTACK.get());
//            int lvl_old = old.getEnchantmentLevel(MyEnchantments.FAST_ATTACK.get());
//            if (lvl_old == 0 && lvl == 0) {
//                return;
//            }
//
//            AttributeMap attrmap = e.getAttributes();
//            Multimap<Attribute, AttributeModifier> amods =
//                    MyEnchantments.FAST_ATTACK.get()
//                            .getAttackSpeedModifier(lvl == 0 ? 1 : lvl);
//            if (lvl > 0) {
//                attrmap.addTransientAttributeModifiers(amods);
//            }
//            else {
//                attrmap.removeAttributeModifiers(amods);
//            }
//        }
//    }

    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                // Tell generator to run only when server data are generating
                event.includeServer(),
                new AllAdvancements(event.getGenerator())
        );
    }
}
