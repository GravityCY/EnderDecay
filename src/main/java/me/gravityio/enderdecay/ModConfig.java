package me.gravityio.enderdecay;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModConfig {

    public static ModConfig INSTANCE;

    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("enderdecay.json5");

    public static final ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler
            .createBuilder(ModConfig.class)
            .id(new Identifier(EnderDecayMod.MOD_ID))
            .serializer(serializer ->
                    GsonConfigSerializerBuilder.create(serializer)
                            .setPath(PATH)
                            .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                            .setJson5(true)
                            .build())
            .build();

    public static Screen getScreen(Screen parent) {
        return YetAnotherConfigLib.create(HANDLER, (defaults, config, builder) -> {
            Text title = Text.translatable("yacl.ender_decay.title");
            builder.title(title);

            List<Option<Integer>> colourOpts = getRGBAOptions("yacl.ender_decay.%s", defaults.rgb, config::getRGBA, config::setRGB, 3);

            Option.Builder<Boolean> isOnOpt = opt(
                    "yacl.ender_decay.on.label","yacl.ender_decay.on.desc",
                    defaults.on, config::isOn, config::setOn,
                    option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true)
            );

            Option.Builder<Integer> chanceOpt = opt(
                    "yacl.ender_decay.chance.label", "yacl.ender_decay.chance.desc",
                    defaults.chance, config::getChance, config::setChance,
                    option -> IntegerSliderControllerBuilder.create(option).range(0, 100).step(1)
            );

            Option.Builder<Float> ageOpt = opt(
                    "yacl.ender_decay.age.label", "yacl.ender_decay.age.desc",
                    defaults.maxAge / 20f, () -> config.maxAge / 20f, (v) -> config.setMaxAge((int) (v * 20)),
                    option -> FloatFieldControllerBuilder.create(option).min(0f).formatValue(value -> Text.literal(value + "s"))
            );

            Option.Builder<Double> velocityOpt = opt(
                    "yacl.ender_decay.velocity.label", "yacl.ender_decay.velocity.desc",
                    defaults.velocity, config::getVelocity, config::setVelocity,
                    option -> DoubleFieldControllerBuilder.create(option).min(0.00d)
            );

            OptionGroup.Builder coloursGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("yacl.ender_decay.colors.label"))
                    .description(OptionDescription.of(Text.translatable("yacl.ender_decay.colors.desc")))
                    .options(colourOpts);

            ConfigCategory.Builder category = ConfigCategory.createBuilder()
                    .name(title)
                    .option(isOnOpt.build())
                    .option(chanceOpt.build())
                    .option(ageOpt.build())
                    .option(velocityOpt.build())
                    .group(coloursGroup.build());

            builder.category(category.build());
            return builder;
        }).generateScreen(parent);
    }

    @SerialEntry
    public boolean on = true;
    @SerialEntry
    public int chance = 10;
    @SerialEntry
    public int maxAge = 100;
    @SerialEntry
    public double velocity = 0.015d;
    @SerialEntry
    public int rgb = 0x555555;

    public float[] rgbFloats = Helper.toFloat(rgb, 3);

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public int getRGBA() {
        return rgb;
    }

    public void setRGB(int rgb) {
        this.rgb = rgb;
        this.rgbFloats = Helper.toFloat(rgb, 3);
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    private static <T> Option.Builder<T> opt(String name, String desc, T def, Supplier<T> getter, Consumer<T> setter, Function<Option<T>, ControllerBuilder<T>> controllerBuilder) {
        return Option.<T>createBuilder()
                .name(Text.translatable(name))
                .description(OptionDescription.of(Text.translatable(desc)))
                .binding(def, getter, setter)
                .controller(controllerBuilder);
    }

    private static List<Option<Integer>> getRGBAOptions(String f, int def, Supplier<Integer> rgbaGetter, Consumer<Integer> rgbaSetter, int size) {
        byte[] rgbaDefaults = Helper.getBytes(def, true, size);
        char[] rgbaLabels = {'r', 'g', 'b', 'a'};


        List<Option<Integer>> opts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            char label = rgbaLabels[i];
            int defaultValue = (int) ((rgbaDefaults[i] & 0xFF) / 255f * 100);

            Text name = Text.translatable(f.formatted(label) + ".label");
            Text desc = Text.translatable(f.formatted(label) + ".desc");

            int byteIndex = i;
            Supplier<Integer> getter = () -> (int) ((Helper.getByteAt(rgbaGetter.get(), byteIndex, size, true) & 0xFF) / 255f * 100);
            Consumer<Integer> setter = v -> rgbaSetter.accept(Helper.setByteAt(rgbaGetter.get(), (byte) (v / 100f * 255), byteIndex, size, true));

            opts.add(Option.<Integer>createBuilder()
                    .name(name)
                    .description(OptionDescription.of(desc))
                    .controller(opt -> IntegerSliderControllerBuilder.create(opt).step(1).range(0, 100))
                    .binding(defaultValue, getter, setter)
                    .build());
        }
        return opts;
    }

}
