
package com.sadmc2332.loyaltyboost;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class LoyaltyBoostScreen extends Screen {

    private final Screen parent;
    private String category = "Genel";
    private boolean enabled = true;

    private boolean reduceParticles = true;
    private boolean entityShadows = false;
    private boolean fastLoading = true;
    private boolean reduceAnimations = false;

    private boolean clouds = false;
    private boolean weather = true;
    private boolean waterAnimation = true;
    private boolean fireAnimation = true;

    private boolean advancedParticles = false;
    private boolean advancedAnimations = false;
    private boolean diagnostics = false;
    private boolean extraDetails = false;

    public LoyaltyBoostScreen(Screen parent) {
        super(Text.literal("LoyaltyBoost"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 175;
        int top = this.height / 2 - 85;
        int right = left + 125;

        String[] categories = {
            "Genel",
            "Performans",
            "Görüntü",
            "Gelişmiş"
        };

        for (int i = 0; i < categories.length; i++) {
            String name = categories[i];

            this.addDrawableChild(
                ButtonWidget.builder(Text.literal(name), button -> {
                    category = name;
                    clearAndInit();
                }).dimensions(left, top + i * 28, 110, 22).build()
            );
        }

        this.addDrawableChild(
            ButtonWidget.builder(
                toggleText("Özellik", enabled),
                button -> {
                    enabled = !enabled;
                    button.setMessage(toggleText("Özellik", enabled));
                }
            ).dimensions(right, top, 210, 22).build()
        );

        int y = top + 32;

        switch (category) {
            case "Performans":
                addToggle(right, y, "Parçacıkları azalt",
                    () -> reduceParticles, v -> reduceParticles = v);
                addToggle(right, y + 29, "Varlık gölgeleri",
                    () -> entityShadows, v -> entityShadows = v);
                addToggle(right, y + 58, "Hızlı yükleme",
                    () -> fastLoading, v -> fastLoading = v);
                addToggle(right, y + 87, "Animasyonları azalt",
                    () -> reduceAnimations, v -> reduceAnimations = v);
                break;

            case "Görüntü":
                addToggle(right, y, "Bulutlar",
                    () -> clouds, v -> clouds = v);
                addToggle(right, y + 29, "Hava efektleri",
                    () -> weather, v -> weather = v);
                addToggle(right, y + 58, "Su animasyonu",
                    () -> waterAnimation, v -> waterAnimation = v);
                addToggle(right, y + 87, "Ateş animasyonu",
                    () -> fireAnimation, v -> fireAnimation = v);
                break;

            case "Gelişmiş":
                addToggle(right, y, "Gelişmiş parçacıklar",
                    () -> advancedParticles, v -> advancedParticles = v);
                addToggle(right, y + 29, "Gelişmiş animasyonlar",
                    () -> advancedAnimations, v -> advancedAnimations = v);
                addToggle(right, y + 58, "Tanılama",
                    () -> diagnostics, v -> diagnostics = v);
                addToggle(right, y + 87, "Ek ayrıntılar",
                    () -> extraDetails, v -> extraDetails = v);
                break;

            default:
                addToggle(right, y, "Performans seçenekleri",
                    () -> reduceParticles, v -> reduceParticles = v);
                addToggle(right, y + 29, "Görüntü seçenekleri",
                    () -> clouds, v -> clouds = v);
                break;
        }

        this.addDrawableChild(
            ButtonWidget.builder(Text.literal("Geri"), button -> close())
                .dimensions(this.width / 2 - 50,
                    this.height / 2 + 105, 100, 20)
                .build()
        );
    }

    private void addToggle(
        int x,
        int y,
        String name,
        BooleanSupplier getter,
        Consumer<Boolean> setter
    ) {
        this.addDrawableChild(
            ButtonWidget.builder(
                toggleText(name, getter.getAsBoolean()),
                button -> {
                    boolean value = !getter.getAsBoolean();
                    setter.accept(value);
                    button.setMessage(toggleText(name, value));
                }
            ).dimensions(x, y, 210, 22).build()
        );
    }

    private Text toggleText(String name, boolean value) {
        return Text.literal(name + ": " + (value ? "Açık" : "Kapalı"));
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public void render(
        DrawContext context,
        int mouseX,
        int mouseY,
        float delta
    ) {
        context.fill(
            0, 0, this.width, this.height, 0xC0101010
        );

        context.drawCenteredTextWithShadow(
            this.textRenderer,
            this.title,
            this.width / 2,
            25,
            0xFFFFFF
        );

        context.drawTextWithShadow(
            this.textRenderer,
            Text.literal("Kategori: " + category),
            this.width / 2 - 45,
            this.height / 2 - 100,
            0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }
}
