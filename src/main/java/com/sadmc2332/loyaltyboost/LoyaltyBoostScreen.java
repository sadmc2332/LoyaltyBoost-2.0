
package com.sadmc2332.loyaltyboost;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class LoyaltyBoostScreen extends Screen {

    private final Screen parent;
    private String category = "Genel";
    private boolean enabled = true;

    private final int[] viewDistances =
        {4, 6, 8, 10, 12, 16, 20, 24, 32};

    private final int[] simulationDistances =
        {2, 4, 6, 8, 10, 12, 16};

    public LoyaltyBoostScreen(Screen parent) {
        super(Text.literal("LoyaltyBoost"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = width / 2 - 175;
        int right = left + 125;
        int top = height / 2 - 85;

        String[] categories = {
            "Genel", "Performans",
            "Görüntü", "Gelişmiş"
        };

        for (int i = 0; i < categories.length; i++) {
            String name = categories[i];

            addDrawableChild(
                ButtonWidget.builder(
                    Text.literal(name),
                    b -> {
                        category = name;
                        clearAndInit();
                    }
                ).dimensions(left, top + i * 28, 110, 22).build()
            );
        }

        if (category.equals("Genel")) {
            addDrawableChild(
                ButtonWidget.builder(
                    toggleText("Özellik", enabled),
                    b -> {
                        enabled = !enabled;
                        b.setMessage(toggleText("Özellik", enabled));
                    }
                ).dimensions(right, top, 210, 22).build()
            );
        }

        if (category.equals("Performans")) {
            addShadowButton(right, top);
        }

        if (category.equals("Gelişmiş")) {
            addDistanceButton(
                right, top, "Görüş mesafesi",
                false
            );

            addDistanceButton(
                right, top + 32, "Simülasyon mesafesi",
                true
            );
        }

        if (category.equals("Görüntü")) {
            addDrawableChild(
                ButtonWidget.builder(
                    Text.literal("Görüntü ayarları geliştirilecek"),
                    b -> {}
                ).dimensions(right, top, 210, 22).build()
            );
        }

        addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Geri"),
                b -> close()
            ).dimensions(
                width / 2 - 50,
                height / 2 + 105,
                100, 20
            ).build()
        );
    }

    private void addShadowButton(int x, int y) {
        var option = client.options.getEntityShadows();

        addDrawableChild(
            ButtonWidget.builder(
                toggleText("Varlık gölgeleri",
                    option.getValue()),
                b -> {
                    option.setValue(!option.getValue());
                    b.setMessage(
                        toggleText("Varlık gölgeleri",
                            option.getValue())
                    );
                }
            ).dimensions(x, y, 210, 22).build()
        );
    }

    private void addDistanceButton(
        int x, int y, String name, boolean simulation
    ) {
        int current = simulation
            ? client.options.getSimulationDistance().getValue()
            : client.options.getViewDistance().getValue();

        addDrawableChild(
            ButtonWidget.builder(
                Text.literal(name + ": " + current + " chunk"),
                b -> {
                    int[] values = simulation
                        ? simulationDistances
                        : viewDistances;

                    int next = values[0];

                    for (int i = 0; i < values.length; i++) {
                        if (values[i] > current) {
                            next = values[i];
                            break;
                        }
                    }

                    if (current >= values[values.length - 1]) {
                        next = values[0];
                    }

                    if (simulation) {
                        client.options.getSimulationDistance()
                            .setValue(next);
                    } else {
                        client.options.getViewDistance()
                            .setValue(next);
                    }

                    clearAndInit();
                }
            ).dimensions(x, y, 210, 22).build()
        );
    }

    private Text toggleText(String name, boolean value) {
        return Text.literal(
            name + ": " + (value ? "Açık" : "Kapalı")
        );
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    @Override
    public void render(
        DrawContext context,
        int mouseX,
        int mouseY,
        float delta
    ) {
        context.fill(
            0, 0, width, height, 0xC0101010
        );

        context.drawCenteredTextWithShadow(
            textRenderer,
            title,
            width / 2,
            25,
            0xFFFFFF
        );

        context.drawTextWithShadow(
            textRenderer,
            Text.literal("Kategori: " + category),
            width / 2 - 45,
            height / 2 - 100,
            0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }
}
