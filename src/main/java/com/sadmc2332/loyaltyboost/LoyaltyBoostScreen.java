
package com.sadmc2332.loyaltyboost;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.text.Text;

public class LoyaltyBoostScreen extends Screen {

    private final Screen parent;

    private String category = "Genel";

    private int scrollOffset = 0;
    private int visibleRows = 1;

    private int rightX;
    private int rightWidth;
    private int listTop;

    private final String[] categories = {
        "Genel",
        "Performans",
        "Görüntü",
        "Gelişmiş"
    };

    public LoyaltyBoostScreen(Screen parent) {
        super(Text.literal("LoyaltyBoost"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        int leftX = Math.max(4, width / 2 - 160);
        int leftWidth = 94;

        rightX = leftX + leftWidth + 10;
        rightWidth = Math.max(80, width - rightX - 8);

        int top = 42;

        listTop = top + 27;

        visibleRows = Math.max(
            1,
            (height - listTop - 48) / 29
        );

        for (String name : categories) {

            addDrawableChild(
                ButtonWidget.builder(
                    Text.literal(name),
                    button -> {
                        category = name;
                        scrollOffset = 0;
                        clearAndInit();
                    }
                ).dimensions(
                    leftX,
                    top + java.util.Arrays.asList(categories).indexOf(name) * 29,
                    leftWidth,
                    23
                ).build()
            );
        }

        int count = getOptionCount();

        scrollOffset = Math.max(
            0,
            Math.min(scrollOffset, Math.max(0, count - visibleRows))
        );

        for (
            int i = scrollOffset;
            i < Math.min(count, scrollOffset + visibleRows);
            i++
        ) {
            int y = listTop + (i - scrollOffset) * 29;
            addSetting(i, y);
        }

        addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Geri"),
                button -> close()
            ).dimensions(
                width / 2 - 45,
                height - 27,
                90,
                20
            ).build()
        );
    }

    private int getOptionCount() {

        return switch (category) {
            case "Genel" -> 4;
            case "Performans" -> 6;
            case "Görüntü" -> 4;
            case "Gelişmiş" -> 3;
            default -> 0;
        };
    }

    private void addSetting(int index, int y) {

        switch (category) {

            case "Genel" -> {

                if (index == 0) {
                    addEnabledButton(y);
                }

                if (index == 1) {
                    addIntegerSlider(
                        "FPS sınırı",
                        client.options.getMaxFps(),
                        10, 260, 10, y
                    );
                }

                if (index == 2) {
                    addBooleanButton(
                        "Dikey eşitleme",
                        client.options.getEnableVsync(),
                        y
                    );
                }

                if (index == 3) {
                    addBooleanButton(
                        "Varlık gölgeleri",
                        client.options.getEntityShadows(),
                        y
                    );
                }
            }

            case "Performans" -> {

                if (index == 0) {
                    addIntegerSlider(
                        "Görüş mesafesi",
                        client.options.getViewDistance(),
                        2, 32, 1, y
                    );
                }

                if (index == 1) {
                    addIntegerSlider(
                        "Simülasyon mesafesi",
                        client.options.getSimulationDistance(),
                        5, 32, 1, y
                    );
                }

                if (index == 2) {
                    addDoubleSlider(
                        "Varlık mesafesi",
                        client.options.getEntityDistanceScaling(),
                        0.5, 5.0, 0.1, y
                    );
                }

                if (index == 3) {
                    addBooleanButton(
                        "Yaprak görünümü",
                        client.options.getCutoutLeaves(),
                        y
                    );
                }

                if (index == 4) {
                    addBooleanButton(
                        "Gölgelendirme",
                        client.options.getAo(),
                        y
                    );
                }

                if (index == 5) {
                    addParticlesButton(y);
                }
            }

            case "Görüntü" -> {

                if (index == 0) {
                    addBooleanButton(
                        "Kenar karartması",
                        client.options.getVignette(),
                        y
                    );
                }

                if (index == 1) {
                    addBooleanButton(
                        "Geliştirilmiş şeffaflık",
                        client.options.getImprovedTransparency(),
                        y
                    );
                }

                if (index == 2) {
                    addBooleanButton(
                        "Şimşek efektlerini gizle",
                        client.options.getHideLightningFlashes(),
                        y
                    );
                }

                if (index == 3) {
                    addBooleanButton(
                        "Yaprak görünümü",
                        client.options.getCutoutLeaves(),
                        y
                    );
                }
            }

            case "Gelişmiş" -> {

                if (index == 0) {
                    addIntegerSlider(
                        "Görüş mesafesi",
                        client.options.getViewDistance(),
                        2, 32, 1, y
                    );
                }

                if (index == 1) {
                    addIntegerSlider(
                        "Simülasyon mesafesi",
                        client.options.getSimulationDistance(),
                        5, 32, 1, y
                    );
                }

                if (index == 2) {
                    addDoubleSlider(
                        "Varlık mesafesi",
                        client.options.getEntityDistanceScaling(),
                        0.5, 5.0, 0.1, y
                    );
                }
            }
        }
    }

    private void addParticlesButton(int y) {

        SimpleOption<ParticlesMode> option =
            client.options.getParticles();

        addDrawableChild(
            ButtonWidget.builder(
                Text.literal(
                    "Parçacıklar: " +
                    particleName(option.getValue())
                ),
                button -> {

                    ParticlesMode current = option.getValue();

                    ParticlesMode next =
                        current == ParticlesMode.ALL
                        ? ParticlesMode.DECREASED
                        : current == ParticlesMode.DECREASED
                        ? ParticlesMode.MINIMAL
                        : ParticlesMode.ALL;

                    option.setValue(next);

                    client.options.write();

                    button.setMessage(
                        Text.literal(
                            "Parçacıklar: " +
                            particleName(next)
                        )
                    );
                }
            ).dimensions(
                rightX, y, rightWidth, 22
            ).build()
        );
    }

    private String particleName(ParticlesMode mode) {

        return switch (mode) {
            case ALL -> "Tam";
            case DECREASED -> "Azaltılmış";
            case MINIMAL -> "Minimum";
        };
    }

    private void addEnabledButton(int y) {

        addDrawableChild(
            ButtonWidget.builder(
                toggleText("LoyaltyBoost", LoyaltyBoostConfig.enabled),
                button -> {

                    LoyaltyBoostConfig.enabled =
                        !LoyaltyBoostConfig.enabled;

                    LoyaltyBoostConfig.save();

                    button.setMessage(
                        toggleText(
                            "LoyaltyBoost",
                            LoyaltyBoostConfig.enabled
                        )
                    );
                }
            ).dimensions(
                rightX, y, rightWidth, 22
            ).build()
        );
    }

    private void addBooleanButton(
        String name,
        SimpleOption<Boolean> option,
        int y
    ) {

        addDrawableChild(
            ButtonWidget.builder(
                toggleText(name, option.getValue()),
                button -> {

                    option.setValue(!option.getValue());

                    client.options.write();

                    button.setMessage(
                        toggleText(name, option.getValue())
                    );
                }
            ).dimensions(
                rightX, y, rightWidth, 22
            ).build()
        );
    }

    private void addIntegerSlider(
        String name,
        SimpleOption<Integer> option,
        int min,
        int max,
        int step,
        int y
    ) {

        double progress =
            (double) (option.getValue() - min) / (max - min);

        addDrawableChild(
            new IntegerSlider(
                rightX, y, rightWidth, 22,
                name, option, min, max, step, progress
            )
        );
    }

    private void addDoubleSlider(
        String name,
        SimpleOption<Double> option,
        double min,
        double max,
        double step,
        int y
    ) {

        double progress =
            (option.getValue() - min) / (max - min);

        addDrawableChild(
            new DoubleSlider(
                rightX, y, rightWidth, 22,
                name, option, min, max, step, progress
            )
        );
    }

    private class IntegerSlider extends SliderWidget {

        private final String name;
        private final SimpleOption<Integer> option;
        private final int min;
        private final int max;
        private final int step;

        IntegerSlider(
            int x, int y, int width, int height,
            String name,
            SimpleOption<Integer> option,
            int min, int max, int step,
            double progress
        ) {
            super(
                x, y, width, height,
                Text.literal(name),
                Math.max(0.0, Math.min(1.0, progress))
            );

            this.name = name;
            this.option = option;
            this.min = min;
            this.max = max;
            this.step = step;

            updateMessage();
        }

        @Override
        protected void updateMessage() {

            int value = min +
                (int) Math.round(
                    this.value * (max - min) / step
                ) * step;

            value = Math.min(max, value);

            String display =
                name.equals("FPS sınırı") && value >= 260
                ? "Sınırsız"
                : String.valueOf(value);

            setMessage(
                Text.literal(name + ": " + display)
            );
        }

        @Override
        protected void applyValue() {

            int value = min +
                (int) Math.round(
                    this.value * (max - min) / step
                ) * step;

            value = Math.min(max, value);

            option.setValue(value);

            client.options.write();

            updateMessage();
        }
    }

    private class DoubleSlider extends SliderWidget {

        private final String name;
        private final SimpleOption<Double> option;
        private final double min;
        private final double max;
        private final double step;

        DoubleSlider(
            int x, int y, int width, int height,
            String name,
            SimpleOption<Double> option,
            double min, double max, double step,
            double progress
        ) {
            super(
                x, y, width, height,
                Text.literal(name),
                Math.max(0.0, Math.min(1.0, progress))
            );

            this.name = name;
            this.option = option;
            this.min = min;
            this.max = max;
            this.step = step;

            updateMessage();
        }

        @Override
        protected void updateMessage() {

            double value =
                min + this.value * (max - min);

            value = Math.round(value / step) * step;

            setMessage(
                Text.literal(
                    name + ": " +
                    Math.round(value * 100) + "%"
                )
            );
        }

        @Override
        protected void applyValue() {

            double value =
                min + this.value * (max - min);

            value = Math.round(value / step) * step;

            option.setValue(value);

            client.options.write();

            updateMessage();
        }
    }

    private Text toggleText(String name, boolean value) {

        return Text.literal(
            name + ": " + (value ? "Açık" : "Kapalı")
        );
    }

    @Override
    public boolean mouseScrolled(
        double mouseX,
        double mouseY,
        double horizontalAmount,
        double verticalAmount
    ) {

        int maxScroll = Math.max(
            0,
            getOptionCount() - visibleRows
        );

        if (verticalAmount < 0 && scrollOffset < maxScroll) {
            scrollOffset++;
            clearAndInit();
            return true;
        }

        if (verticalAmount > 0 && scrollOffset > 0) {
            scrollOffset--;
            clearAndInit();
            return true;
        }

        return super.mouseScrolled(
            mouseX, mouseY,
            horizontalAmount, verticalAmount
        );
    }

    @Override
    public void close() {

        client.options.write();
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
            13,
            0xFFFFFF
        );

        context.drawTextWithShadow(
            textRenderer,
            Text.literal("Kategori: " + category),
            rightX,
            listTop - 17,
            0xFFFFFF
        );

        int count = getOptionCount();

        if (count > visibleRows) {

            int trackHeight = visibleRows * 29;
            int trackX = rightX + rightWidth - 3;

            context.fill(
                trackX, listTop,
                trackX + 3, listTop + trackHeight,
                0xFF555555
            );

            int thumbHeight = Math.max(
                12,
                trackHeight * visibleRows / count
            );

            int thumbY = listTop +
                (trackHeight - thumbHeight) *
                scrollOffset /
                Math.max(1, count - visibleRows);

            context.fill(
                trackX, thumbY,
                trackX + 3, thumbY + thumbHeight,
                0xFFFFFFFF
            );
        }

        super.render(context, mouseX, mouseY, delta);
    }
                }
                    
