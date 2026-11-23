
package com.sadmc2332.loyaltyboost.mixin;

import com.sadmc2332.loyaltyboost.LoyaltyBoostConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderManager.class)
public class DroppedItemRenderMixin {

    @Inject(
        method = "shouldRender",
        at = @At("HEAD"),
        cancellable = true
    )
    private void loyaltyboost$cullDistantItems(
        Entity entity,
        Frustum frustum,
        double x,
        double y,
        double z,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (!LoyaltyBoostConfig.enabled) {
            return;
        }

        if (!(entity instanceof ItemEntity)) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) {
            return;
        }

        if (entity.squaredDistanceTo(client.player) > 576.0) {
            cir.setReturnValue(false);
        }
    }
}
