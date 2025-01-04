package com.shfloop.BlockSwitcher.mixins;


import com.shfloop.BlockSwitcher.BlockSwitcher;
import finalforeach.cosmicreach.gamestates.MainMenu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MainMenu.class)
public class MainMenuMixin {
    @Inject(method = "create", at = @At("TAIL"))
    private void injected(CallbackInfo ci) {
        BlockSwitcher.LOGGER.info("BlockSwitcher COMMON mixin logged!");
        BlockSwitcher.LOGGER.info("No access widening for common" );
    }
}