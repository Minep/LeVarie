package com.lunaixsky.miscmod.interfaces;

public interface IArgumentedVillager {
    boolean canBeGrateful();
    int getCureTime();
    void setCureTime(int ct);

    long getLastGossipTime();
    int getFoodLevel();
}
