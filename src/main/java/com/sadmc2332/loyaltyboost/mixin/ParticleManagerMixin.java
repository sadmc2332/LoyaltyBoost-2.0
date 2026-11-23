package com.sadmc2332.loyaltyboost.mixin;

import com.sadmc2332.loyaltyboost.LoyaltyBoostConfig;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Unique
    private int loyaltyboost$particleCount = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void loyaltyboost$resetParticleCount(CallbackInfo ci) {
        loyaltyboost$particleCount = 0;
    }

    @Inject(
        method = "addParticle(Lnet/minecraft/client/particle/Particle;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void loyaltyboost$limitParticles(
        Particle particle,
        CallbackInfo ci
    ) {
        if (!LoyaltyBoostConfig.enabled) {
            return;
        }

        if (loyaltyboost$particleCount >= 30) {
            ci.cancel();
            return;
        }

        loyaltyboost$particleCount++;
    }
}
