package org.scofield.claims.mixin;

import org.jetbrains.annotations.NotNull;
import org.scofield.claims.claim.storage.ClaimStorage;
import org.scofield.claims.claim.storage.ClaimStorageFactory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.scofield.claims.claim.storage.IClaimStorageContainer;
import org.scofield.claims.event_handlers.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements IClaimStorageContainer {
    @Unique
    private ClaimStorage claimStorage;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void initData(CallbackInfo info) {
        ServerWorld world = ((ServerWorld) (Object) this);
        this.claimStorage = ClaimStorageFactory.Companion.readFromSave(world.getServer(), world);
    }

    @Inject(method = "saveLevel()V", at = @At("RETURN"))
    private void saveClaimStorage(CallbackInfo info) {
        ServerWorld world = ((ServerWorld) (Object) this);
        this.claimStorage.save(world.getServer(), world);
    }

    @Inject(method = "createExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/Explosion;collectBlocksAndDamageEntities()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void explosionHook(Entity entity, DamageSource damageSource, ExplosionBehavior explosionBehavior, double d, double e, double f, float g, boolean bl, Explosion.DestructionType destructionType, CallbackInfoReturnable<Explosion> info, Explosion explosion) {
        WorldEvents.cancelExplosion((ServerWorld) (Object) this, explosion);
    }

    @Override
    public @NotNull ClaimStorage getClaimStorage_() {
        return this.claimStorage;
    }
}
