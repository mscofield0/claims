package org.scofield.claims.mixin;

import net.minecraft.block.FenceGateBlock;
import org.scofield.claims.types.Door;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FenceGateBlock.class)
public class FenceGateBlockMixin implements Door {}
