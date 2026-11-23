
package com.sadmc2332.loyaltyboost;

import net.minecraft.client.MinecraftClient;

public final class LoyaltyBoostChunkOptimizer {

    private static final long UPDATE_INTERVAL = 5000L;
    private static final int MAX_CHUNKS = 24;

    private static long lastUpdate;

    private LoyaltyBoostChunkOptimizer() {
    }

    public static void update(MinecraftClient client) {

        if (!LoyaltyBoostConfig.enabled || client.world == null) {
            return;
        }

        long now = System.currentTimeMillis();

        if (now - lastUpdate < UPDATE_INTERVAL) {
            return;
        }

        lastUpdate = now;

        int fps = client.getCurrentFps();

        if (fps <= 0) {
            return;
        }

        int targetDistance;

        if (fps < 25) {
            targetDistance = 12;
        } else if (fps < 35) {
            targetDistance = 16;
        } else if (fps < 45) {
            targetDistance = 20;
        } else {
            targetDistance = MAX_CHUNKS;
        }

        int currentDistance =
            client.options.getViewDistance().getValue();

        if (currentDistance != targetDistance) {
            client.options.getViewDistance()
                .setValue(targetDistance);
        }
    }
}
