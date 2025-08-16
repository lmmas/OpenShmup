package engine.scene;

import engine.graphics.*;
import engine.scene.display.SceneDisplay;
import engine.scene.display.TextDisplay;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;

import static engine.Engine.assetManager;
import static engine.Engine.graphicsManager;
import static engine.GlobalVars.Paths.defaultFont;
import static engine.GlobalVars.debugDisplayLayer;


abstract public class Scene {
    protected float sceneTime;
    protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    HashSet<SceneDisplay> displayList;
    HashSet<SceneDisplay> displaysToRemove;
    protected boolean debugModeEnabled = false;
    protected SceneDebug sceneDebug;
    public Scene(boolean debugModeEnabled) {
        this.sceneTime = 0.0f;
        this.timer = new SceneTimer();
        this.displayList = new HashSet<>();
        this.displaysToRemove = new HashSet<>();
        this.sceneDebug = new SceneDebug(debugModeEnabled);
        this.debugModeEnabled = debugModeEnabled;
    }

    abstract public void handleInputs();

    public void update(){
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


    public void addDisplay(SceneDisplay display){
        List<Graphic<?, ?>> newGraphics = display.getGraphics();
        for(Graphic<?,?> graphic: newGraphics){
            graphicsManager.addGraphic(graphic);
        }
        displayList.add(display);
        display.initDisplay(this.sceneTime);
    }

    public void deleteDisplay(SceneDisplay display){
        displayList.remove(display);
        List<Graphic<?, ?>> graphics = display.getGraphics();
        for(var graphic: graphics){
            graphic.delete();
        }
    }

    public float getSceneTimeSeconds() {
        return sceneTime;
    }

    final public void setSpeed(float speed){
        timer.setSpeed(speed);
    }

    protected class SceneDebug {
        protected TextDisplay fpsDisplay;

        public SceneDebug(boolean debugModeEnabled){
            if(debugModeEnabled){
                this.enable();
            }
        }

        public void enable(){
            this.fpsDisplay = new TextDisplay(debugDisplayLayer, true, 0.9f, 0.9f, 25, "", assetManager.getFont(defaultFont));
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
