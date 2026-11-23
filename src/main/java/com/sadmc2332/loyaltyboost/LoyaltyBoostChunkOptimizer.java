package com.sadmc2332.loyaltyboost;

import net.minecraft.client.MinecraftClient;

public final class LoyaltyBoostChunkOptimizer {

    private static final long UPDATE_INTERVAL = 1000L;

    private static long lastUpdate;
    private static int lastChunkDistance = -1;

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

        int distance = client.options.getViewDistance().getValue();

        if (distance != lastChunkDistance) {
            lastChunkDistance = distance;

            System.out.println(
                "LoyaltyBoost: Chunk mesafesi " +
                distance + " olarak ayarlandi."
            );
        }
    }
}
