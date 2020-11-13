package org.scofield.claims.mixin;

import net.minecraft.block.ShulkerBoxBlock;
import org.scofield.claims.types.Storage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin implements Storage {}
