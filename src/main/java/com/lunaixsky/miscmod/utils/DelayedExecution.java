package com.lunaixsky.miscmod.utils;

import java.util.function.Predicate;

public class DelayedExecution {
    private int delayedTick = 0;
    private int currentTick = 0;
    private Runnable exec;

    public DelayedExecution(int delayedTick, Runnable runnable) {
        this.delayedTick = delayedTick;
        this.exec = runnable;
    }

    public boolean tryExecute() {
        if (currentTick >= delayedTick) {
            currentTick = 0;
            this.exec.run();
            return true;
        }
        currentTick++;
        return false;
    }
}
