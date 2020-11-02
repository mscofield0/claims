package org.scofield.claims.mixin;

import net.minecraft.server.world.ServerWorld;
import org.scofield.claims.event_handlers.WorldEvents;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowableFluid.class)
public abstract class FluidMixin {

    @Inject(method = "canFlow", at = @At(value = "HEAD"), cancellable = true)
    public void flow(BlockView world, BlockPos fluidPos, BlockState fluidBlockState, Direction flowDirection, BlockPos flowTo,
                               BlockState flowToBlockState, FluidState fluidState, Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        if (!WorldEvents.permitFluidFlow(fluidBlockState, (ServerWorld) world, fluidPos, flowDirection)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
}
