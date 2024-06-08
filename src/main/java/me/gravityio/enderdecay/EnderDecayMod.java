package me.gravityio.enderdecay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderDecayMod implements ClientModInitializer {
    public static String MOD_ID = "ender_decay";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final SimpleParticleType END_PORTAL = FabricParticleTypes.simple();

    private static boolean IS_DEBUG = false;


    public static void DEBUG(String message, Object... args) {
        if (!IS_DEBUG) {
            return;
        }

        LOGGER.info(message, args);
    }

    @Override
    public void onInitializeClient() {
        IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

        ModConfig.HANDLER.load();
        ModConfig.INSTANCE = ModConfig.HANDLER.instance();

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "end_portal"), END_PORTAL);
        ParticleFactoryRegistry.getInstance().register(END_PORTAL, EnderDecayParticle.Factory::new);
    }
}
