package org.scofield.claims.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "useOnBlock", at = @At(value = "HEAD"), cancellable = true)
    public void blockUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        ActionResult result = ItemInteract.onItemUseBlock(context);
        if (result != ActionResult.PASS) {
            info.setReturnValue(result);
            info.cancel();
        }
    }
}
