package com.sadmc2332.loyaltyboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class LoyaltyBoostClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        LoyaltyBoostConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(
            LoyaltyBoostChunkOptimizer::update
        );
    }
}
