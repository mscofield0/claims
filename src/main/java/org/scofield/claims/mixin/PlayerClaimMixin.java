package org.scofield.claims.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.scofield.claims.claim.storage.IClaimDataContainer;
import org.scofield.claims.claim.storage.PlayerClaimData;
import org.scofield.claims.claim.storage.PlayerClaimDataFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(ServerPlayerEntity.class)
public abstract class PlayerClaimMixin implements IClaimDataContainer {
    @Unique
    private PlayerClaimData claimData;

    @Final
    @Shadow
    public MinecraftServer server;

    @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
    private void readData(CompoundTag tag, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        UUID playerId = player.getUuid();
        this.claimData = PlayerClaimDataFactory.Companion.readFromSave(this.server, playerId);
    }

    @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
    private void writeData(CompoundTag tag, CallbackInfo info) {
        this.claimData.save(this.server);
    }

    @Override
    public @NotNull PlayerClaimData getClaimData_() {
        return this.claimData;
    }

    @Override
    public @NotNull UUID getPlayerUUID() {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object) this;
        return player.getUuid();
    }
}
