package org.scofield.claims.mixin;

import org.scofield.claims.event_handlers.ItemInteract;

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
        if (!ItemInteract.permitUseBlockItem(context)) {
            info.setReturnValue(ActionResult.FAIL);
            info.cancel();
        }
    }
}
