package org.scofield.claims.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.scofield.claims.claim.storage.IClaimDataContainer;
import org.scofield.claims.claim.storage.PlayerClaimData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public abstract class PlayerClaimMixin implements IClaimDataContainer {
    @Unique
    private PlayerClaimData claimData;

    @Shadow
    private MinecraftServer server;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void initData(CallbackInfo info) {
        this.claimData = new ;
    }

    @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
    private void readData(CompoundTag tag, CallbackInfo info) {
        this.claimData.read(this.server);
    }

    @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
    private void writeData(CompoundTag tag, CallbackInfo info) {
        this.claimData.save(this.server);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tickData(CallbackInfo info) {
        this.claimData.tick();
    }

    @Override
    public PlayerClaimData getClaimData() {
        return this.claimData;
    }
}
