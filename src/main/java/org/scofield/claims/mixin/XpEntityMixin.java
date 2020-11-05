package org.scofield.claims.mixin;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.scofield.claims.event_handlers.EntityInteract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public class XpEntityMixin {

    @Inject(method = "onPlayerCollision", at = @At(value = "HEAD"), cancellable = true)
    public void collision(PlayerEntity player, CallbackInfo info) {
        if (!EntityInteract.permitXpAbsorb((ServerPlayerEntity) player)) {
            info.cancel();
        }
    }

}
