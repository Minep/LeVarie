package com.lunaixsky.miscmod.advancemnets;

import com.lunaixsky.miscmod.ModInfo;
import com.lunaixsky.miscmod.advancemnets.predicates.AllPredicates;
import com.lunaixsky.miscmod.advancemnets.triggers.AllTriggers;
import com.lunaixsky.miscmod.advancemnets.triggers.BedRockBreakTrigger;
import com.lunaixsky.miscmod.enchaments.MyEnchantments;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class AllAdvancements implements DataProvider {
    static List<VarieAdvancement> pendings = new LinkedList<>();

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Advancement ROOT =
            advancement(
                    declare("root")
                            .withIcon(Items.SUNFLOWER)
                            .asTask()
                            .shouldSilent()
                            .grantForFree());

    public static final Advancement ADV_ENCHANTMENTS =
            advancement(
                    declare("adv_enchantments")
                            .dependsOn(ROOT)
                            .withIcon(Items.ENCHANTED_BOOK)
                            .asTask()
                            .shouldSilent()
                            .grantForFree());

    public static final Advancement BREAKLIMIT =
            advancement(
                    declare("limit_breaker")
                            .withIcon(Items.BEDROCK)
                            .asChallenge()
                            .dependsOn(ADV_ENCHANTMENTS)
                            .shouldHidden()
                            .awardXp(500)
                            .descriptionStyle(s -> s.withColor(0xbe39db))
                            .addTrigger(BedRockBreakTrigger.Instance.get()));

    public static final Advancement FRIENDSHIP =
            advancement(
                    declare("magical_friendship")
                            .withIcon(Items.CREEPER_HEAD)
                            .asLandmark()
                            .dependsOn(ADV_ENCHANTMENTS)
                            .awardXp(100)
                            .addTrigger(AllTriggers.enchantmentInInventory(
                                    MyEnchantments.FIM.get(),
                                    MinMaxBounds.Ints.atLeast(1),
                                    MinMaxBounds.Ints.atLeast(1))));

    public static final Advancement CALLOUS_CAPITALIST =
            advancement(
                    declare("callous_capitalist")
                            .withIcon(Items.GOLD_INGOT)
                            .asTask()
                            .shouldAnnounce()
                            .dependsOn(ROOT)
                            .awardXp(100)
                            .addTrigger(AllTriggers.LOW_QOL_TRIGGER.createInstance()));

    public static final Advancement LABOUR_REVOLUTION =
            advancement(
                    declare("labour_revolution")
                            .withIcon(Items.IRON_AXE)
                            .asLandmark()
                            .dependsOn(CALLOUS_CAPITALIST)
                            .awardXp(500)
                            .addTrigger(AllTriggers.HIGH_QOL_TRIGGER.createInstance()));

    public static final Advancement UTOPIA =
            advancement(
                    declare("utopia")
                            .withIcon(Items.POPPY)
                            .asChallenge()
                            .dependsOn(LABOUR_REVOLUTION)
                            .awardXp(1000)
                            .addTrigger(AllTriggers.HIGH_QOL_BONUS_TRIGGER.createInstance()));

    public static final Advancement BAD_CURE =
            advancement(
                    declare("bad_cure")
                            .withIcon(Items.GOLDEN_APPLE)
                            .asTask()
                            .asLandmark()
                            .dependsOn(CALLOUS_CAPITALIST)
                            .awardXp(50)
                            .addTrigger(AllTriggers.BAD_CURE_TRIGGER.createInstance()));


    public static VarieAdvancement.Builder declare(String id) {
        return VarieAdvancement.Builder.declare(id);
    }

    public static Advancement advancement(VarieAdvancement.Builder builder) {
        VarieAdvancement adv = builder.build();
        pendings.add(adv);

        return adv.built_advancement;
    }

    private DataGenerator dataGen;

    public AllAdvancements(DataGenerator dataGen) {
        this.dataGen = dataGen;
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        Path path = this.dataGen.getOutputFolder();
        Consumer<Advancement> consumer = (adv) -> {
            Path out_path = getPath(path, adv);

            try {
                DataProvider.saveStable(cache, adv.deconstruct()
                        .serializeToJson(), out_path);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save advancement {}", out_path, ioexception);
            }
        };

        for (VarieAdvancement va: pendings) {
            va.save(consumer);
        }
    }

    @Override
    public String getName() {
        return "AdvanceEnchantmentAdv";
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId()
                .getNamespace() + "/advancements/"
                + advancementIn.getId()
                .getPath()
                + ".json");
    }
}
