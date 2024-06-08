package me.gravityio.enderdecay;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class EnderDecayParticle extends AscendingParticle {

    protected EnderDecayParticle(
            ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider
    ) {
        super(world, x, y, z, 0, 0.1f, 0, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 1, 16, 0, true);
        var floats = ModConfig.INSTANCE.rgbFloats;
        this.red = floats[0];
        this.green = floats[1];
        this.blue = floats[2];
        this.alpha = 1;
        this.maxAge = ModConfig.INSTANCE.maxAge;
        this.velocityY = ModConfig.INSTANCE.velocity;
        this.velocityMultiplier = 0.98f;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new EnderDecayParticle(clientWorld, d, e, f, g, h, i, 1, this.spriteProvider);
        }
    }

}
