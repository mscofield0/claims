package org.scofield.claims.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.scofield.claims.event_handlers.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBlock.class)
public class PistonMixin {

    @Inject(method = "isMovable", at = @At(value = "HEAD"), cancellable = true)
    private static void checkMovable(BlockState blockState, World world, BlockPos blockPos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> info) {
        if (!WorldEvents.permitPistonPushPull(blockState, (ServerWorld) world, blockPos, direction)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
}
