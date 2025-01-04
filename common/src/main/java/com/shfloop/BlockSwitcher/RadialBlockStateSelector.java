package com.shfloop.BlockSwitcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.blocks.MissingBlockStateResult;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.util.exceptions.MissingBlockStateException;

public class RadialBlockStateSelector {
    private boolean shown = false;
    private Array<String> blockStateIDs;

    public Viewport itemViewport = new FitViewport(100.0f,100.0f);
    private static ItemStack lastItemStack;
    private final Vector2 mouse = new Vector2();
    private final Vector2 tmpPosition = new Vector2();
    private float selectedAnimationTime = 0f;
    private int prevSelectedidx = -1;

    public RadialBlockStateSelector() {

    }
    public void render(Viewport uiViewport, ShapeRenderer shapeRenderer) {
        if(this.shown) {
            //Gdx.input.setCursorCatched(true);
//             blockStateIDs = new Array<>();
//            ItemStack stack = UI.hotbar.getSelectedItemStack();
//            if (stack == null) {
//                return;
//            }
//            //hotbar -> itemStack -> item which is changed
//           if(stack.getItem() instanceof ItemBlock itemBlock) {
//               if (itemBlock.getBlockState().swapGroupId == null) return;
//
//               Block block = itemBlock.getBlockState().getBlock();
//
//               for (BlockState b :block.blockStates.values()) {
//                   if (b.allowSwapping&& itemBlock.getBlockState().swapGroupId.equals(b.swapGroupId) ) {
//                       //System.out.print(" FOUND: " + b.stringId  );
//                       blockStateIDs.add(b.getSaveKey()); // In ItemBlock blockstate.getSaveKey is used for the id in Item allItems
//                   }
//               }
//
//               //System.out.println("Found " + blockStateIDs.size + " blockStates " + blockStateIDs.toString());
//           }
            if ( UI.hotbar.getSelectedItemStack() != lastItemStack) {
                if(!updateSelectedBlockStates(true)) {
                    this.hideAndChangeBlockState();
                    return;
                }


            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f,0f,0f,0.4f);
            //shapeRenderer.circle(0,0,150f);
            // draw the mouse direction
            //Vector2 mouse = new Vector2( - 0f,  - 0f);
            mouse.set((float)Gdx.input.getX(),(float)Gdx.input.getY());
            float mouseX = mouse.x - Gdx.graphics.getWidth() / 2f;
            float mouseY = mouse.y - Gdx.graphics.getHeight() / 2f;
            float pi = 3.141592f;
            //mouse.nor(); //normalize to get the direction
            float radian = (float)Math.atan2(mouseY, mouseX) ; // add pi to get it from -pi to pi to 0 to 2pi
            float cursorRadian = radian;
            radian += pi;
            //if (radian < 0) radian += 2* pi;
            if (radian < 0) radian =0f; //clamp to 0
            radian += 1 * pi /2f;
            radian %= 2 * pi;
            float sectorSize = (2 * pi) / blockStateIDs.size;
            float index = radian * (blockStateIDs.size / (2* 3.141492f));
            index = radian / sectorSize;
            //round to nearest whole number rounding up but then if its



            index = Math.round(index);
            float radianIndexSize = 2 * pi / blockStateIDs.size;
            float newDegrees =  ((index * sectorSize ) * 180) / pi;
            float remaderad = ((index * sectorSize)) + sectorSize / 2f;
            if (index == blockStateIDs.size) index = 0; //if it goes past index than it should be indexing 0 meaning it wrapped around
            float newIndex = blockStateIDs.size  - index;


            if (newIndex == blockStateIDs.size) newIndex = 0;
            //index = blockStateIDs.size - 1 - index;

            shapeRenderer.setColor(0.5f,0.5f,0f,0.6f);
            float degrees = 180*(radian+ 3f*pi/4f) / pi;


            // 3 - pi/6
            //4 pi/4
            //5
            degrees = 150f + 180f* 2f / blockStateIDs.size * newIndex ;
            //System.out.println(index + " radian" +  radian + " New Index " +newIndex + " NEWRAD " + remaderad);
            //shapeRenderer.arc(0f,0f,140f,90f +newDegrees - 180f / blockStateIDs.size,360f/ blockStateIDs.size+ 0.000001f);
            float radToDegrees = radian * 180 / pi;



            //shapeRenderer.arc(0f,0f,40f,90f + radToDegrees - 180f / blockStateIDs.size,360f/ blockStateIDs.size+ 0.000001f);

            float cx = (float) Math.sin(radian) * -50 ; //TODO i think the drawItems is also inverting the item drawing so that might be why i need to invert the index
            float cy = (float) Math.cos(radian) * 50 ;

            float cursorDegrees =    radian * 180 / pi;


            shapeRenderer.setColor(0f,0f,0f,0.5f);
            float ofsX = cx /50f * 4f;
            float ofsY = cy / 50f * 4f;
            shapeRenderer.arc(cx +ofsX,cy +ofsY,26f, cursorDegrees - 135f - 2, 94f);


            shapeRenderer.setColor(1f,1f,1f,1.0f);
            shapeRenderer.arc(cx,cy,20f, cursorDegrees - 135f, 90f);

            shapeRenderer.setColor(0f,0f,0f,0.4f);
            float slotSize = 64.0f ;
            float ratioX = (float)uiViewport.getScreenWidth() / uiViewport.getWorldWidth();
            float ratioY = (float)uiViewport.getScreenHeight() / uiViewport.getWorldHeight();
            for(int i= 0; i < blockStateIDs.size; i ++) {
                float theta = 3.141592f * 2 / blockStateIDs.size * i;
                float x = (float) Math.sin(theta) * 100f - slotSize / 2f;
                float y = (float) Math.cos(theta) * 100f - slotSize / 2f;
                float sw = slotSize ;
                float sh = slotSize ;
                shapeRenderer.rect(x,y,sw,sh);
            }

            int blockidx = blockStateIDs.size - (int) index;
            if (prevSelectedidx != blockidx) {
                prevSelectedidx = blockidx;
                selectedAnimationTime = 0;
            }

            shapeRenderer.setColor(0f,0.8f,0f,0.6f);
            float theta = 3.141592f * 2 / blockStateIDs.size * prevSelectedidx;
            float x = (float) Math.sin(theta) * 100f - slotSize / 2f;
            float y = (float) Math.cos(theta) * 100f - slotSize / 2f;
            float sw = slotSize ;
            selectedAnimationTime += Gdx.graphics.getDeltaTime();
            float animationLength = 0.2f;
            float animationPerc = selectedAnimationTime / animationLength;
            animationPerc = Math.min(animationPerc,1);
           // System.out.println(selectedAnimationTime);
            float sh = slotSize  * (1 );
            shapeRenderer.rect(x,y,sw,sh);



            shapeRenderer.setColor(1f,1f,1f,0.8f);
            shapeRenderer.line(0,0, 100,0);
            shapeRenderer.line(100,0,100,100);
            shapeRenderer.line(100,100,0,0);






            //
            //5 - 54 + 72 = 126
            //4 45 + 90 = 135
            //3 30 + 120 = 150





            shapeRenderer.end();
        }

    }
    public void drawItems(Viewport uiViewport) {
        //positions of the items to draw is 2PI / items * item num
        // x = sin(theta// want to draw items starting at top so sin 0 = 0
        //y = cos(theta
        if(this.shown) {
            float slotSize = 32.0f;
            float ratioX = (float)uiViewport.getScreenWidth() / uiViewport.getWorldWidth();
            float ratioY = (float)uiViewport.getScreenHeight() / uiViewport.getWorldHeight();
            for(int i= 0; i < blockStateIDs.size; i ++){
                float theta = 3.141592f * 2 / blockStateIDs.size * i;
                float x = (float)Math.sin(theta) * 100f -slotSize / 2f;
                float y = (float)Math.cos(theta) * 100f + slotSize / 2f;
                float sw = slotSize * ratioX;
                float sh = slotSize * ratioY;
                tmpPosition.set(x,y);
                //tmp.set(0,0);
                uiViewport.project(tmpPosition);


                this.itemViewport.setScreenBounds((int)tmpPosition.x,(int)tmpPosition.y,(int)sw,(int)sh );
                this.itemViewport.apply();
                Item item = Item.getItem(blockStateIDs.get(i));
                ItemModel model = ItemRenderer.getModel(item, true);
                ItemRenderer.drawItem(model.getItemSlotCamera(), item);
                //System.out.println("render at " + tmp.x + " " + tmp.y);

            }
        }

    }





    public boolean updateSelectedBlockStates(boolean updateLastBlock) {
        ItemStack stack = UI.hotbar.getSelectedItemStack();
        if (stack == null) {
            return false;
        }
        //hotbar -> itemStack -> item which is changed
        if(stack.getItem() instanceof ItemBlock itemBlock) {
            if (itemBlock.getBlockState().swapGroupId == null) return false;


           Array<String> tmpBlockStateIDs = new Array<>();
            Block block = itemBlock.getBlockState().getBlock();

            for (BlockState b :block.blockStates.values()) {
                if (b.allowSwapping && itemBlock.getBlockState().swapGroupId.equals(b.swapGroupId) ) {
                    //System.out.print(" FOUND: " + b.stringId  );
                    tmpBlockStateIDs.add(b.getSaveKey()); // In ItemBlock blockstate.getSaveKey is used for the id in Item allItems
                }
            }
            if(tmpBlockStateIDs.size < 2) {
                //dont want single  blockstate blocks to trigger the wheel
                return false;
            }
            if(updateLastBlock) {
                this.changeBlockState();
            }
            lastItemStack = stack;
            blockStateIDs = tmpBlockStateIDs;

            //System.out.println("Found " + blockStateIDs.size + " blockStates " + blockStateIDs.toString());
        } else {
            return false;
        }
        return true;
    }





    public void show() {

        if (!this.shown) {
            if(!updateSelectedBlockStates(false)) {
                return;
            }
            Gdx.input.setCursorCatched(false);
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        }
        this.shown = true;
    }
    public void hide() {
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2); //this might fix issues with camera moving after cycle selecting block
        //though im not sure why its happeneing in the first palce
        this.shown = false;

    }
    public void changeBlockState() {

        float pi = 3.141592f;
        //mouse.nor(); //normalize to get the direction //mouse is reset before this activates
        float mouseX = mouse.x - Gdx.graphics.getWidth() / 2f;
        float mouseY = mouse.y - Gdx.graphics.getHeight() / 2f;
        float radian = (float)Math.atan2(mouseY, mouseX) ;
        radian += pi;
        //if (radian < 0) radian += 2* pi;
        if (radian < 0) radian =0f; //clamp to 0
        radian += 1 * pi /2f;
        radian %= 2 * pi;
        //round to nearest whole number rounding up but then if its



        float sectorSize = (2 * pi) / blockStateIDs.size;

       float  index = radian / sectorSize;
        //round to nearest whole number rounding up but then if its



        index = Math.round(index);
        if (index == blockStateIDs.size) index = 0;
        index = blockStateIDs.size  - index;
        if (index == blockStateIDs.size) index = 0;
        //System.out.println(index + " " + blockStateIDs );
        //ItemStack stack = UI.hotbar.getSelectedItemStack();

       try  {
           lastItemStack.setItem(BlockState.getInstance(blockStateIDs.get((int)index), MissingBlockStateResult.EXCEPTION).getItem());
       } catch (MissingBlockStateException e) {
           System.out.println("Missing Block??"); // dont think this is possible
       }
    }
    public void hideAndChangeBlockState() {
        this.changeBlockState();
        this.hide();
    }
    public void toggleShown() {
        this.setShown(!this.shown);
    }

    public boolean isShown() {
        return this.shown;
    }
    public void setShown(boolean shown) {
        if (shown) {
            this.show();
        } else {
            this.hide();
        }
    }
}
