package me.gravityio.enderdecay.mixins.impl;

import me.gravityio.enderdecay.EnderDecayMod;
import me.gravityio.enderdecay.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Use our custom particle
 */
@Mixin(EndPortalBlock.class)
public class EndPortalMixin {
    @Inject(method = "randomDisplayTick", at = @At("HEAD"), cancellable = true)
    private void ender_decay$addEndParticle(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!ModConfig.INSTANCE.on) return;

        if (random.nextDouble() < ModConfig.INSTANCE.chance / 100f)
        {
            double d = (double)pos.getX() + random.nextDouble();
            double e = (double)pos.getY() + 0.8;
            double f = (double)pos.getZ() + random.nextDouble();
            world.addParticle(EnderDecayMod.END_PORTAL, d, e, f, 0.0, 0, 0.0);
        }
        ci.cancel();
    }
}
