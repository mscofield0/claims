package org.scofield.claims.mixin;

import org.scofield.claims.event_handlers.EntityInteract;

import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin {

    @Inject(method = "onCollision", at = @At(value = "HEAD"), cancellable = true)
    public void collision(HitResult hitResult, CallbackInfo info) {
        if (EntityInteract.permitProjectileHit((EnderPearlEntity) (Object) this, hitResult)) {
            info.cancel();
        }
    }
}
