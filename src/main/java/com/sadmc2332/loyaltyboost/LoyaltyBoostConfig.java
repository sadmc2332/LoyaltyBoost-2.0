package com.sadmc2332.loyaltyboost;

import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class LoyaltyBoostConfig {

    private static final Path FILE =
        FabricLoader.getInstance()
            .getConfigDir()
            .resolve("loyaltyboost.properties");

    public static boolean enabled = true;

    public static void load() {
        if (!Files.exists(FILE)) {
            save();
            return;
        }

        Properties properties = new Properties();

        try (var reader = Files.newBufferedReader(FILE)) {
            properties.load(reader);
            enabled = Boolean.parseBoolean(
                properties.getProperty("enabled", "true")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        Properties properties = new Properties();
        properties.setProperty("enabled", String.valueOf(enabled));

        try {
            Files.createDirectories(FILE.getParent());

            try (var writer = Files.newBufferedWriter(FILE)) {
                properties.store(writer, "LoyaltyBoost Settings");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
