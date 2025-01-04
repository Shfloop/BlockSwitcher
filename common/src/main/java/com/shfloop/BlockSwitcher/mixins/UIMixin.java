package com.shfloop.BlockSwitcher.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shfloop.BlockSwitcher.RadialBlockStateSelector;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.settings.ControlSettings;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static finalforeach.cosmicreach.ui.UI.hotbar;

@Mixin(UI.class)
public abstract class UIMixin {
    //private static SpriteBatch playerListBatch = new SpriteBatch(8191);
    //private static Viewport uiViewport = new ScreenViewport();
    @Shadow
    protected Viewport uiViewport;
    @Shadow private ShapeRenderer shapeRenderer;


    @Shadow public static boolean uiNeedMouse;
    private static final Vector2 tmpTextDim = new Vector2();
    private static float radialMenuCountDown = 0f;
    private static RadialBlockStateSelector radialMenu = new RadialBlockStateSelector();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/ItemCatalog;render(Lcom/badlogic/gdx/utils/viewport/Viewport;Lcom/badlogic/gdx/graphics/glutils/ShapeRenderer;)V"))
    private void addRadialRender(CallbackInfo ci) {
        radialMenu.render(this.uiViewport,this.shapeRenderer);
    }
    @Inject(method = "render", at = @At("TAIL"))
    private void redoUINeedMouse(CallbackInfo ci) {
        UI.uiNeedMouse = UI.isInventoryOpen() || radialMenu.isShown();
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/ItemCatalog;drawItems(Lcom/badlogic/gdx/utils/viewport/Viewport;)V"))
    private void renderRadialBlockItems(CallbackInfo ci) {
        radialMenu.drawItems(uiViewport);
    }


    @Inject(method = "render", at = @At("HEAD"))
    private void renderAllPlayerNames(CallbackInfo ci ) {
         //boolean isRadialMenuPressed = false;
        if(ControlSettings.keySwapGroupItem.isPressed()) {
            if(!UI.isInventoryOpen() && GameState.currentGameState instanceof InGame) {

                if(radialMenuCountDown > 0.2f && !radialMenu.isShown()) {
                    //i need it to keep the radial menu open until its no longer pressed

                    radialMenu.show();
                } else {
                    radialMenuCountDown += Gdx.graphics.getDeltaTime();
                }

            } else {
                if(radialMenu.isShown()) {

                    radialMenu.hide();

                }
                radialMenuCountDown = 0f; // when in inventory reset the counter
            }



        } else {

            if(radialMenu.isShown()) {

                radialMenu.hideAndChangeBlockState();

            } else if (radialMenuCountDown >0) {
                //if the button was pressed for less than.2 seconds cycle the block
                hotbar.cycleSwapGroupItem();
            }
            radialMenuCountDown = 0f;
        }







    }
}
