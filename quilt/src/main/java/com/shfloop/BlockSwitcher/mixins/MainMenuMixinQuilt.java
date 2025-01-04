package com.shfloop.BlockSwitcher.mixins;


import com.shfloop.BlockSwitcher.BlockSwitcherQuilt;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MainMenu.class)
public class MainMenuMixinQuilt {
    @Inject(method = "create", at = @At("HEAD"))
    private void injected(CallbackInfo ci) {
        BlockSwitcherQuilt.LOGGER.info("BlockSwitcher QUILT mixin logged!");
        BlockSwitcherQuilt.LOGGER.info("Access for game start time widened, and giving " + Lwjgl3Launcher.startTime);
    }
}