package com.lunaixsky.miscmod;

import net.minecraft.resources.ResourceLocation;

public class ModInfo {
    public static final String MODID = "miscmod";

    public static ResourceLocation getLocalResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static ResourceLocation getLocalTrigger(String path) {
        return new ResourceLocation(MODID, "triggers."+path);
    }

    public static ResourceLocation getMinecraftResource(String path) {
        return new ResourceLocation("minecraft", path);
    }
}
