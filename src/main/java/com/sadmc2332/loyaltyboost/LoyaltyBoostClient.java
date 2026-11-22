package com.sadmc2332.loyaltyboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class LoyaltyBoostClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (screen instanceof VideoOptionsScreen) {
                Screens.getButtons(screen).add(
                    ButtonWidget.builder(
                        Text.literal("LoyaltyBoost"),
                        button -> client.setScreen(
                            new LoyaltyBoostScreen(screen)
                        )
                    ).dimensions(
                        width / 2 - 100,
                        height - 52,
                        200,
                        20
                    ).build()
                );
            }
        });
    }
}
