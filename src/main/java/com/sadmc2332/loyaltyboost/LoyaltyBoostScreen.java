package com.sadmc2332.loyaltyboost;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.text.Text;

public class LoyaltyBoostScreen extends Screen {

    private final Screen parent;
    private String category = "Genel";
    private int scrollOffset = 0;
    private int visibleRows = 4;
    private int listTop;
    private int rightX;
    private int rightWidth;

    private final String[] categories = {
        "Genel",
        "Performans",
        "Görüntü",
        "Gelişmiş"
    };

    private final int[] viewDistances = {
        4, 6, 8, 10, 12, 16, 20, 24, 32
    };

    private final int[] simulationDistances = {
        2, 4, 6, 8, 10, 12, 16
    };

    private final int[] fpsValues = {
        30, 60, 90, 120, 144, 165, 240, 260
    };

    private final double[] entityDistances = {
        0.5, 0.75, 1.0, 1.25, 1.5, 2.0
    };

    public LoyaltyBoostScreen(Screen parent) {
        super(Text.literal("LoyaltyBoost"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int leftX = Math.max(4, width / 2 - 160);
        int categoryWidth = Math.min(94, width / 3);
        rightX = leftX + categoryWidth + 6;
        rightWidth = Math.max(80, width - rightX - 4);

        int top = Math.max(35, height / 2 - 78);
        listTop = top + 4;

        visibleRows = Math.max(
            1,
            (height - listTop - 42) / 26
        );

        for (int i = 0; i < categories.length; i++) {
            String name = categories[i];

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
                    top + i * 27,
                    categoryWidth,
                    22
                ).build()
            );
        }

        int count = getOptionCount();

        scrollOffset = Math.max(
            0,
            Math.min(scrollOffset, count - visibleRows)
        );

        for (
            int i = scrollOffset;
            i < Math.min(count, scrollOffset + visibleRows);
            i++
        ) {
            int y = listTop + (i - scrollOffset) * 26;
            addSettingButton(i, y);
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
            case "Genel" -> 3;
            case "Performans" -> 6;
            case "Görüntü" -> 5;
            case "Gelişmiş" -> 3;
            default -> 0;
        };
    }

    private void addSettingButton(int index, int y) {
        switch (category) {

            case "Genel" -> {
                if (index == 0) {
                    addIntegerButton(
                        "FPS sınırı",
                        client.options.getMaxFps(),
                        fpsValues,
                        y
                    );
                } else if (index == 1) {
                    addBooleanButton(
                        "Dikey eşitleme",
                        client.options.getEnableVsync(),
                        y
                    );
                } else if (index == 2) {
                    addParticlesButton(y);
                }
            }

            case "Performans" -> {
                if (index == 0) {
                    addBooleanButton(
                        "Varlık gölgeleri",
                        client.options.getEntityShadows(),
                        y
                    );
                } else if (index == 1) {
                    addParticlesButton(y);
                } else if (index == 2) {
                    addDoubleButton(
                        "Varlık mesafesi",
                        client.options.getEntityDistanceScaling(),
                        entityDistances,
                        y
                    );
                } else if (index == 3) {
                    addIntegerButton(
                        "FPS sınırı",
                        client.options.getMaxFps(),
                        fpsValues,
                        y
                    );
                } else if (index == 4) {
                    addBooleanButton(
                        "Dikey eşitleme",
                        client.options.getEnableVsync(),
                        y
                    );
                } else if (index == 5) {
                    addBooleanButton(
                        "Şimşek efektlerini gizle",
                        client.options.getHideLightningFlashes(),
                        y
                    );
                }
            }

            case "Görüntü" -> {
                if (index == 0) {
                    addBooleanButton(
                        "Yaprak görünümü",
                        client.options.getCutoutLeaves(),
                        y
                    );
                } else if (index == 1) {
                    addBooleanButton(
                        "Kenar karartması",
                        client.options.getVignette(),
                        y
                    );
                } else if (index == 2) {
                    addBooleanButton(
                        "Geliştirilmiş şeffaflık",
                        client.options.getImprovedTransparency(),
                        y
                    );
                } else if (index == 3) {
                    addBooleanButton(
                        "Şimşek efektlerini gizle",
                        client.options.getHideLightningFlashes(),
                        y
                    );
                } else if (index == 4) {
                    addParticlesButton(y);
                }
            }

            case "Gelişmiş" -> {
                if (index == 0) {
                    addIntegerButton(
                        "Görüş mesafesi",
                        client.options.getViewDistance(),
                        viewDistances,
                        y
                    );
                } else if (index == 1) {
                    addIntegerButton(
                        "Simülasyon mesafesi",
                        client.options.getSimulationDistance(),
                        simulationDistances,
                        y
                    );
                } else if (index == 2) {
                    addDoubleButton(
                        "Varlık mesafesi",
                        client.options.getEntityDistanceScaling(),
                        entityDistances,
                        y
                    );
                }
            }
        }
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
                    button.setMessage(
                        toggleText(name, option.getValue())
                    );
                }
            ).dimensions(
                rightX, y, rightWidth, 22
            ).build()
        );
    }

    private void addIntegerButton(
        String name,
        SimpleOption<Integer> option,
        int[] values,
        int y
    ) {
        addDrawableChild(
            ButtonWidget.builder(
                Text.literal(name + ": " + option.getValue()),
                button -> {
                    int current = option.getValue();
                    int next = values[0];

                    for (int value : values) {
                        if (value > current) {
                            next = value;
                            break;
                        }
                    }

                    option.setValue(next);
                    clearAndInit();
                }
            ).dimensions(
                rightX, y, rightWidth, 22
            ).build()
        );
    }

    private void addDoubleButton(
        String name,
        SimpleOption<Double> option,
        double[] values,
        int y
    ) {
        addDrawableChild(
            ButtonWidget.builder(
                Text.literal(
                    name + ": " +
                    Math.round(option.getValue() * 100) + "%"
                ),
                button -> {
                    double current = option.getValue();
                    double next = values[0];

                    for (double value : values) {
                        if (value > current + 0.001) {
                            next = value;
                            break;
                        }
                    }

                    option.setValue(next);
                    clearAndInit();
                }
            ).dimensions(
                rightX, y, rightWidth, 22
            ).build()
        );
    }

    private void addParticlesButton(int y) {
        addDrawableChild(
            ButtonWidget.builder(
                particlesText(),
                button -> {
                    ParticlesMode current =
                        client.options.getParticles().getValue();

                    ParticlesMode next;

                    if (current == ParticlesMode.ALL) {
                        next = ParticlesMode.DECREASED;
                    } else if (current == ParticlesMode.DECREASED) {
                        next = ParticlesMode.MINIMAL;
                    } else {
                        next = ParticlesMode.ALL;
                    }

                    client.options.getParticles().setValue(next);
                    button.setMessage(particlesText());
                }
            ).dimensions(
                rightX, y, rightWidth, 22
            ).build()
        );
    }

    private Text particlesText() {
        ParticlesMode mode =
            client.options.getParticles().getValue();

        String value;

        if (mode == ParticlesMode.ALL) {
            value = "Tümü";
        } else if (mode == ParticlesMode.DECREASED) {
            value = "Azaltılmış";
        } else {
            value = "Minimum";
        }

        return Text.literal("Parçacıklar: " + value);
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
            mouseX,
            mouseY,
            horizontalAmount,
            verticalAmount
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
            12,
            0xFFFFFF
        );

        context.drawTextWithShadow(
            textRenderer,
            Text.literal("Kategori: " + category),
            rightX,
            listTop - 17,
            0xFFFFFF
        );

        context.drawTextWithShadow(
            textRenderer,
            Text.literal("Kaydır: ayarları görmek için"),
            rightX,
            height - 40,
            0xAAAAAA
        );

        super.render(context, mouseX, mouseY, delta);
    }
            }
