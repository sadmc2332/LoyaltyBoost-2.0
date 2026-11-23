package com.sadmc2332.loyaltyboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;

public class LoyaltyBoostClient implements ClientModInitializer {

    private static Screen previousScreen;

    @Override
    public void onInitializeClient() {

        LoyaltyBoostConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(
            LoyaltyBoostChunkOptimizer::update
        );

        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {

            if (screen instanceof VideoOptionsScreen) {
                Screen parent = previousScreen;
                previousScreen = screen;

                client.setScreen(new LoyaltyBoostScreen(parent));
                return;
            }

            previousScreen = screen;
        });
    }
}
