package engine.scene;

import engine.EditorDataManager;
import engine.Game;
import engine.graphics.Graphic;
import engine.render.*;
import engine.graphics.DynamicImage;
import engine.graphics.StaticImage;
import engine.scene.display.SceneDisplay;
import engine.scene.spawnable.Spawnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;


abstract public class Scene {
    protected TreeMap<Integer,ArrayList<VAO<?,?>>> layers;
    final protected EditorDataManager editorDataManager;
    protected float sceneTime;
    protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    HashSet<SceneDisplay> displayList;
    HashSet<SceneDisplay> displaysToRemove;
    public Scene(Game game) {
        this.editorDataManager = game.getEditorDataManager();
        this.layers = new TreeMap<>();
        this.sceneTime = 0.0f;
        this.timer = new SceneTimer();
        this.displayList = new HashSet<>();
        this.displaysToRemove = new HashSet<>();
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
        for(Map.Entry<Integer,ArrayList<VAO<?,?>>> vaos: layers.entrySet()){
            for(VAO<?,?> vao: vaos.getValue()){
                vao.draw();
            }
        }
        /*float currentTime = timer.getTimeSeconds();
        System.out.println((1.0f / (currentTime - lastDrawTime)) + " FPS");
        lastDrawTime = currentTime;*/
    }

    public void addGraphic(Graphic<?,?> newGraphic){
        RenderInfo renderInfo = newGraphic.getRenderInfo();
        ArrayList<VAO<?,?>> vaos = layers.get(renderInfo.layer());
        assert vaos!= null: "bad VAO generation";
        for(VAO<?,?> vao: vaos){
            if(vao.getType() == renderInfo.renderType()){
                switch (renderInfo.renderType()){
                    case STATIC_IMAGE -> {
                        StaticImageVAO staticImageVAO = (StaticImageVAO) vao;
                        StaticImage staticImage = (StaticImage) newGraphic;
                        staticImageVAO.addGraphic(staticImage);
                    }
                    case DYNAMIC_IMAGE -> {
                        DynamicImageVAO dynamicImageVAO = (DynamicImageVAO) vao;
                        DynamicImage dynamicImage = (DynamicImage) newGraphic;
                        dynamicImageVAO.addGraphic(dynamicImage);
                    }
                }
                return;
            }
        }
    }

    public void createNewVAO(int layer, RenderType renderType){
        if(!layers.containsKey(layer)){
            layers.put(layer, new ArrayList<>());
        }
        ArrayList<VAO<?,?>> vaoList = layers.get(layer);
        switch (renderType) {
            case STATIC_IMAGE-> {
                StaticImageVAO newVAO = new StaticImageVAO();
                vaoList.add(newVAO);
            }
            case DYNAMIC_IMAGE -> {
                DynamicImageVAO newVAO = new DynamicImageVAO();
                vaoList.add(newVAO);
            }
        }
    }



    public void addDisplay(SceneDisplay display){
        Graphic<?,?>[] newGraphics = display.getGraphics();
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

    protected void constructVAOs(ArrayList<RenderInfo> allRenderInfos) {
        for(var renderInfo: allRenderInfos){
            if(!layers.containsKey(renderInfo.layer())){
                createNewVAO(renderInfo.layer(), renderInfo.renderType());
            }
            else{
                ArrayList<VAO<?,?>> vaoList = layers.get(renderInfo.layer());
                boolean vaoFound = false;
                for(var vao: vaoList){
                    if (vao.getType() == renderInfo.renderType()) {
                        vaoFound = true;
                        break;
                    }
                }
                if(!vaoFound){
                    createNewVAO(renderInfo.layer(), renderInfo.renderType());
                }
            }
        }
    }
}
