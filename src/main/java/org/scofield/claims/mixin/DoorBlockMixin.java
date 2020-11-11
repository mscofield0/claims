package org.scofield.claims.mixin;

import net.minecraft.block.DoorBlock;
import org.scofield.claims.types.Door;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DoorBlock.class)
public class DoorBlockMixin implements Door {}
