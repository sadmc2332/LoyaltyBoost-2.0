package com.sadmc2332.loyaltyboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class LoyaltyBoostClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("LoyaltyBoost Client hazir!");
    }
}
