package engine.scene;

import engine.EditorDataManager;
import engine.Game;
import engine.graphics.*;
import engine.render.*;
import engine.scene.display.SceneDisplay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;


abstract public class Scene {
    protected TreeMap<Integer,ArrayList<Renderer<?,?>>> layers;
    final protected EditorDataManager editorDataManager;
    protected float sceneTime;
    protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    HashSet<SceneDisplay> displayList;
    HashSet<SceneDisplay> displaysToRemove;
    protected boolean debugMode;
    public Scene(Game game, boolean debugMode) {
        this.editorDataManager = game.getEditorDataManager();
        this.layers = new TreeMap<>();
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
    }

    public void draw(){
        for(Map.Entry<Integer,ArrayList<Renderer<?,?>>> renderers: layers.entrySet()){
            for(Renderer<?,?> renderer : renderers.getValue()){
                renderer.draw();
            }
        }
        if(debugMode){
            float currentTime = timer.getTimeSeconds();
            System.out.println("current time: " + currentTime);
            System.out.println((1.0f / (currentTime - lastDrawTime)) + " FPS");
            lastDrawTime = currentTime;
        }
    }

    public void addGraphic(Graphic<?,?> newGraphic){
        RenderInfo renderInfo = newGraphic.getRenderInfo();
        ArrayList<Renderer<?,?>> renderers = layers.get(renderInfo.layer());
        assert renderers != null: "bad Renderer generation";
        for(Renderer<?,?> renderer : renderers){
            if(renderer.getType() == renderInfo.graphicType()){
                switch (renderInfo.graphicType()){
                    case STATIC_IMAGE -> {
                        StaticImageRenderer staticImageRenderer = (StaticImageRenderer) renderer;
                        StaticImage staticImage = (StaticImage) newGraphic;
                        staticImageRenderer.addGraphic(staticImage);
                    }
                    case DYNAMIC_IMAGE -> {
                        DynamicImageRenderer dynamicImageRenderer = (DynamicImageRenderer) renderer;
                        DynamicImage dynamicImage = (DynamicImage) newGraphic;
                        dynamicImageRenderer.addGraphic(dynamicImage);
                    }
                    case COLOR_RECTANGLE -> {
                        ColorRectangleRenderer colorRectangleRenderer = (ColorRectangleRenderer) renderer;
                        ColorRectangle colorRectangle = (ColorRectangle) newGraphic;
                        colorRectangleRenderer.addGraphic(colorRectangle);
                    }
                }
                return;
            }
        }
    }

    public void createNewRenderer(int layer, GraphicType graphicType){
        if(!layers.containsKey(layer)){
            layers.put(layer, new ArrayList<>());
        }
        ArrayList<Renderer<?,?>> rendererList = layers.get(layer);
        switch (graphicType) {
            case STATIC_IMAGE-> {
                StaticImageRenderer newRenderer = new StaticImageRenderer();
                rendererList.add(newRenderer);
            }
            case DYNAMIC_IMAGE -> {
                DynamicImageRenderer newRenderer = new DynamicImageRenderer();
                rendererList.add(newRenderer);
            }
            case COLOR_RECTANGLE -> {
                ColorRectangleRenderer newRenderer = new ColorRectangleRenderer();
                rendererList.add(newRenderer);
            }
        }
    }



    public void addDisplay(SceneDisplay display){
        ArrayList<Graphic<?,?>> newGraphics = display.getGraphics();
        for(Graphic<?,?> graphic: newGraphics){
            addGraphic(graphic);
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

    protected void constructRenderers(HashSet<RenderInfo> allRenderInfos) {
        for(var renderInfo: allRenderInfos){
            if(!layers.containsKey(renderInfo.layer())){
                createNewRenderer(renderInfo.layer(), renderInfo.graphicType());
            }
            else{
                ArrayList<Renderer<?,?>> rendererList = layers.get(renderInfo.layer());
                boolean rendererFound = false;
                for(var renderer: rendererList){
                    if (renderer.getType() == renderInfo.graphicType()) {
                        rendererFound = true;
                        break;
                    }
                }
                if(!rendererFound){
                    createNewRenderer(renderInfo.layer(), renderInfo.graphicType());
                }
            }
        }
    }
}
