
package com.sadmc2332.loyaltyboost;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class LoyaltyBoostScreen extends Screen {

    private final Screen parent;
    private String category = "Genel";
    private boolean enabled = true;

    public LoyaltyBoostScreen(Screen parent) {
        super(Text.literal("LoyaltyBoost"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 155;
        int top = this.height / 2 - 85;

        String[] categories = {
            "Genel",
            "Performans",
            "Goruntu",
            "Gelismis"
        };

        for (int i = 0; i < categories.length; i++) {
            String name = categories[i];

            this.addDrawableChild(
                ButtonWidget.builder(Text.literal(name), button -> {
                    category = name;
                    clearAndInit();
                }).dimensions(left, top + i * 27, 100, 22).build()
            );
        }

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Ozellik: " + (enabled ? "Acik" : "Kapali")),
                button -> {
                    enabled = !enabled;
                    button.setMessage(
                        Text.literal("Ozellik: " + (enabled ? "Acik" : "Kapali"))
                    );
                }
            ).dimensions(left + 115, top, 180, 22).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Geri"),
                button -> close()
            ).dimensions(this.width / 2 - 50, this.height / 2 + 105, 100, 20).build()
        );
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
        context.fill(0, 0, this.width, this.height, 0xC0101010);

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
            this.width / 2 - 40,
            this.height / 2 - 100,
            0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }
}
