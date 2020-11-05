package org.scofield.claims.mixin;

import org.scofield.claims.event_handlers.BlockInteract;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Inject(method = "onEntityCollision", at = @At(value = "HEAD"), cancellable = true)
    public void collision(World world, BlockPos pos, Entity entity, CallbackInfo info) {
        if (!BlockInteract.permitEntityBlockCollision(this.asBlockState(), world, pos, entity)) {
            info.cancel();
        }
    }

    @Shadow
    protected abstract BlockState asBlockState();
}
