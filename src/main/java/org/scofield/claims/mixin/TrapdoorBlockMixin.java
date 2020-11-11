package org.scofield.claims.mixin;

import net.minecraft.block.TrapdoorBlock;
import org.scofield.claims.types.Door;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TrapdoorBlock.class)
public class TrapdoorBlockMixin implements Door {}
