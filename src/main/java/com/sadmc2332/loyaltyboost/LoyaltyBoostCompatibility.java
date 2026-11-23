
package com.sadmc2332.loyaltyboost;

import net.fabricmc.loader.api.FabricLoader;

public class LoyaltyBoostCompatibility {

    public static boolean isSodiumLoaded() {
        return FabricLoader.getInstance()
            .isModLoaded("sodium");
    }

    public static boolean canUseCustomRendering() {
        return !isSodiumLoaded();
    }
}
