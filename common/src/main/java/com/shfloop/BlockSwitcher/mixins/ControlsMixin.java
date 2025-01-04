package com.shfloop.BlockSwitcher.mixins;

import finalforeach.cosmicreach.settings.Controls;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Controls.class)
public abstract  class ControlsMixin {
    //i want to use the keybind for vanilla block switching but it would interfere if i let the vanilla mechanics happen
    //force this to false so the vanilla mechanic doesnt occur
    @Inject(method = "cycleSwapGroupItemJustPressed", at = @At("RETURN"), cancellable = true)
    private static void forceFalseReturn(CallbackInfoReturnable<Boolean> cir) {

        cir.setReturnValue(false);
    }



}
