package engine.scene;

import engine.graphics.*;
import engine.scene.display.SceneDisplay;
import engine.scene.display.TextDisplay;
import engine.scene.menu.MenuItem;
import engine.scene.menu.MenuScreen;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static engine.Engine.assetManager;
import static engine.Engine.graphicsManager;
import static engine.GlobalVars.Paths.debugFont;
import static engine.GlobalVars.debugDisplayLayer;


abstract public class Scene {
    protected float sceneTime;
    final protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    final protected HashSet<SceneDisplay> displayList;
    final protected HashSet<SceneDisplay> displaysToRemove;
    final protected ArrayList<MenuScreen> displayedMenus;
    protected MenuScreen activeMenu;
    protected boolean debugModeEnabled = false;
    final private SceneDebug sceneDebug;

    public Scene(boolean debugModeEnabled) {
        this.sceneTime = 0.0f;
        this.timer = new SceneTimer();
        this.displayList = new HashSet<>();
        this.displaysToRemove = new HashSet<>();
        this.displayedMenus = new ArrayList<>();
        this.sceneDebug = new SceneDebug(debugModeEnabled);
        this.debugModeEnabled = debugModeEnabled;
    }

    abstract public void handleInputs();

    final public void update(){
        if(!timer.isPaused()){
            sceneTime = timer.getTimeSeconds();
            for(SceneDisplay display: displayList){
                display.update(sceneTime);
                if(display.shouldBeRemoved()){
                    displaysToRemove.add(display);
                }
            }
            for(var display: displaysToRemove){
                deleteDisplay(display);
            }
            displaysToRemove.clear();
        }
        sceneDebug.update();
        lastDrawTime = sceneTime;
    }


    final public void addDisplay(SceneDisplay display){
        display.getGraphics().forEach(graphic -> graphicsManager.addGraphic(graphic));
        displayList.add(display);
        display.initDisplay(this.sceneTime);
    }

    final public void deleteDisplay(SceneDisplay display){
        displayList.remove(display);
        List<Graphic<?, ?>> graphics = display.getGraphics();
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
        menuScreen.menuItems().stream()
                .flatMap(item -> item.getGraphics().stream())
                .forEach(graphic -> graphicsManager.addGraphic(graphic));
        SceneDisplay menuBackground = menuScreen.backgroundDisplay();
        if(menuBackground != null){
            addDisplay(menuBackground);
        }
        displayedMenus.add(menuScreen);
    }

    final public void removeMenu(MenuScreen menuScreen){
        menuScreen.menuItems().stream()
                .flatMap(item -> item.getGraphics().stream())
                .forEach(Graphic::delete);
        displayedMenus.remove(menuScreen);
        if(activeMenu == menuScreen){
            activeMenu = null;
        }
    }

    private class SceneDebug {
        private TextDisplay fpsDisplay;

        public SceneDebug(boolean debugModeEnabled){
            if(debugModeEnabled){
                this.enable();
            }
        }

        public void enable(){
            this.fpsDisplay = new TextDisplay(debugDisplayLayer, true, 0.9f, 0.9f, 25, "", assetManager.getFont(debugFont));
            fpsDisplay.setTextColor(1.0f, 1.0f, 1.0f, 1.0f);
            Scene.this.addDisplay(fpsDisplay);
        }

        public void disable(){
            Scene.this.deleteDisplay(fpsDisplay);
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
