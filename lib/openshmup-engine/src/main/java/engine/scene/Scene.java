package engine.scene;

import engine.EditorDataManager;
import engine.Engine;
import engine.GraphicsManager;
import engine.graphics.*;
import engine.scene.display.SceneDisplay;

import java.util.ArrayList;
import java.util.HashSet;


abstract public class Scene {
    final protected EditorDataManager editorDataManager;
    final protected GraphicsManager graphicsManager;
    protected float sceneTime;
    protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    HashSet<SceneDisplay> displayList;
    HashSet<SceneDisplay> displaysToRemove;
    protected boolean debugMode;
    public Scene(Engine engine, boolean debugMode) {
        this.editorDataManager = engine.getEditorDataManager();
        this.graphicsManager = engine.getGraphicsManager();
        this.sceneTime = 0.0f;
        this.timer = new SceneTimer();
        this.displayList = new HashSet<>();
        this.displaysToRemove = new HashSet<>();
        this.debugMode = debugMode;
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
        if(debugMode){
            float currentTime = timer.getTimeSeconds();
            System.out.println("current time: " + currentTime);
            System.out.println((1.0f / (currentTime - lastDrawTime)) + " FPS");
            lastDrawTime = currentTime;
        }
    }


    public void addDisplay(SceneDisplay display){
        ArrayList<Graphic<?,?>> newGraphics = display.getGraphics();
        for(Graphic<?,?> graphic: newGraphics){
            graphicsManager.addGraphic(graphic);
        }
        displayList.add(display);
        display.setScene(this);
    }

    public void deleteDisplay(SceneDisplay display){
        displayList.remove(display);
        var graphics = display.getGraphics();
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

}
