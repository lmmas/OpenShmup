package engine.scene;

import engine.entity.hitbox.Hitbox;
import engine.graphics.*;
import engine.scene.display.SceneVisual;
import engine.scene.display.TextDisplay;
import engine.scene.menu.MenuItem;
import engine.scene.menu.MenuScreen;
import engine.types.Vec2D;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static engine.Engine.*;
import static engine.GlobalVars.Paths.debugFont;
import static engine.GlobalVars.debugDisplayLayer;


abstract public class Scene {
    protected float sceneTime;
    final protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    final protected HashSet<SceneVisual> sceneVisuals;
    final protected HashSet<SceneVisual> visualsToRemove;
    final protected ArrayList<MenuScreen> displayedMenus;
    protected MenuScreen activeMenu;
    protected boolean debugModeEnabled = false;
    final protected SceneDebug sceneDebug;

    public Scene(boolean debugModeEnabled) {
        this.sceneTime = 0.0f;
        this.timer = new SceneTimer();
        this.sceneVisuals = new HashSet<>();
        this.visualsToRemove = new HashSet<>();
        this.displayedMenus = new ArrayList<>();
        this.sceneDebug = new SceneDebug(debugModeEnabled);
        this.debugModeEnabled = debugModeEnabled;
    }

    public void handleInputs(){
        if(activeMenu == null){
            return;
        }
        for (MenuItem menuItem: activeMenu.menuItems()){
            Vec2D cursorPosition = inputStatesManager.getCursorPosition();
            Hitbox clickHitbox = menuItem.getClickHitbox();
            if(inputStatesManager.getLeftClickState() && clickHitbox.containsPoint(cursorPosition)){
                menuItem.onClick();
            }
        }
    }

    public void update(){
        if(!timer.isPaused()){
            sceneTime = timer.getTimeSeconds();
            for(SceneVisual display: sceneVisuals){
                display.update(sceneTime);
                if(display.shouldBeRemoved()){
                    visualsToRemove.add(display);
                }
            }
            for(var display: visualsToRemove){
                deleteVisual(display);
            }
            visualsToRemove.clear();
        }
        sceneDebug.update();
        lastDrawTime = sceneTime;
    }


    final public void addVisual(SceneVisual visual){
        visual.getGraphics().forEach(graphic -> graphicsManager.addGraphic(graphic));
        sceneVisuals.add(visual);
        visual.initDisplay(this.sceneTime);
    }

    final public void deleteVisual(SceneVisual visual){
        sceneVisuals.remove(visual);
        List<Graphic<?, ?>> graphics = visual.getGraphics();
        for(var graphic: graphics){
            graphic.delete();
        }
    }

    final public float getSceneTimeSeconds() {
        return sceneTime;
    }

    final public void setSpeed(float speed){
        timer.setSpeed(speed);
    }

    final public void addMenu(MenuScreen menuScreen){
        menuScreen.menuItems().forEach(menuItem -> addVisual(menuItem.getVisual()));
        SceneVisual menuBackground = menuScreen.backgroundDisplay();
        if(menuBackground != null){
            addVisual(menuBackground);
        }
        displayedMenus.add(menuScreen);
    }

    final public void removeMenu(MenuScreen menuScreen){
        menuScreen.menuItems().forEach(menuItem -> deleteVisual(menuItem.getVisual()));
        SceneVisual menuBackground = menuScreen.backgroundDisplay();
        if(menuBackground != null){
            deleteVisual(menuBackground);
        }
        displayedMenus.remove(menuScreen);
        if(activeMenu == menuScreen){
            activeMenu = null;
        }
    }

    final protected void setActiveMenu(MenuScreen menuScreen){
        this.activeMenu = menuScreen;
    }

    protected class SceneDebug {
        private TextDisplay fpsDisplay;

        public SceneDebug(boolean debugModeEnabled){
            if(debugModeEnabled){
                this.enable();
            }
        }

        public void enable(){
            this.fpsDisplay = new TextDisplay(debugDisplayLayer, true, 0.9f, 0.9f, 25, "", assetManager.getFont(debugFont));
            fpsDisplay.setTextColor(1.0f, 1.0f, 1.0f, 1.0f);
            Scene.this.addVisual(fpsDisplay);
        }

        public void disable(){
            Scene.this.deleteVisual(fpsDisplay);
        }

        public void toggle(){
            if(debugModeEnabled){
                this.disable();
            }
            else{
                this.enable();
            }
        }

        public void update(){
            if(debugModeEnabled){
                DecimalFormat df = new DecimalFormat("#");
                df.setRoundingMode(RoundingMode.HALF_DOWN);
                float fpsVal = 1 / (sceneTime - lastDrawTime);
                fpsDisplay.setDisplayedString(df.format(fpsVal) + " FPS");
            }
        }
    }

}
