package org.scofield.claims.mixin;

import net.minecraft.entity.boss.WitherEntity;
import org.scofield.claims.event_handlers.EntityInteract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherEntity.class)
public abstract class WitherMixin {

    @Shadow
    private int field_7082;

    @Inject(method = "mobTick", at = @At(value = "HEAD"))
    public void preventClaimDamage(CallbackInfo info) {
        if (!EntityInteract.permitWitherDestruction((WitherEntity) (Object) this)) {
            this.field_7082 = -1;
        }
    }
}
