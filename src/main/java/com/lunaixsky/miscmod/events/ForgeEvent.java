package com.lunaixsky.miscmod.events;

import net.minecraftforge.eventbus.api.IEventBus;

public class ForgeEvent {
    public static void register(IEventBus eventBus) {
        eventBus.register(new ForgeItemStackEvent());
    }
}
