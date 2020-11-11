package org.scofield.claims.mixin;

import net.minecraft.block.AbstractChestBlock;
import org.scofield.claims.types.Storage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractChestBlock.class)
public class AbstractChestBlockMixin implements Storage {}
