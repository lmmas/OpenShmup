package engine.scene;

import engine.EditorDataManager;
import engine.Game;
import engine.graphics.Graphic;
import engine.scene.display.DynamicImage;
import engine.scene.display.StaticImage;
import engine.render.DynamicImageVAO;
import engine.render.StaticImageVAO;
import engine.render.VAO;
import engine.scene.display.SceneDisplay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;


abstract public class Scene {
    protected long window;
    final private EditorDataManager editorDataManager;
    protected TreeMap<Integer,ArrayList<VAO<?,?>>> layers;
    protected float sceneTime;
    protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    HashSet<SceneDisplay> displayList;
    HashSet<SceneDisplay> displaysToRemove;
    public Scene(Game game) {
        this.window = game.getWindow();
        this.editorDataManager = game.getEditorDataManager();
        this.layers = new TreeMap<>();
        this.sceneTime = 0.0f;
        this.timer = new SceneTimer();
        this.displayList = new HashSet<>();
        this.displaysToRemove = new HashSet<>();
        timer.start();
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
        int graphicLayer = newGraphic.getLayer();
        ArrayList<VAO<?,?>> vaos = layers.get(graphicLayer);
        if(vaos!=null) {
            boolean newGraphicAllocated = false;
            int i = 0;
            while (!newGraphicAllocated && i < vaos.size()) {
                VAO<?,?> currentVAO = vaos.get(i);
                if (currentVAO.getType() == newGraphic.getType()) {
                    switch(newGraphic.getType()){
                        case STATIC_IMAGE -> {
                            StaticImage newImage = (StaticImage) newGraphic;
                            StaticImageVAO staticImageVAO = (StaticImageVAO)currentVAO;
                            staticImageVAO.addGraphic(newImage);
                        }
                        case DYNAMIC_IMAGE -> {
                            DynamicImage newImage = (DynamicImage) newGraphic;
                            DynamicImageVAO dynamicImageVAO = (DynamicImageVAO)currentVAO;
                            dynamicImageVAO.addGraphic(newImage);
                        }
                    }
                    newGraphicAllocated = true;
                }
                i++;
            }
            if (!newGraphicAllocated) {
                createNewVAOFromGraphic(newGraphic, vaos);
            }
        }
        else{
            vaos = new ArrayList<>();
            layers.put(graphicLayer, vaos);
            createNewVAOFromGraphic(newGraphic, vaos);
        }
    }

    private void createNewVAOFromGraphic(Graphic<?,?> graphic, ArrayList<VAO<?,?>> vaoList){
        switch (graphic.getType()) {
            case STATIC_IMAGE-> {
                StaticImage newImage = (StaticImage) graphic;
                StaticImageVAO newVAO = new StaticImageVAO();
                vaoList.add(newVAO);
                newVAO.addGraphic(newImage);
            }
            case DYNAMIC_IMAGE -> {
                DynamicImage newImage = (DynamicImage) graphic;
                DynamicImageVAO newVAO = new DynamicImageVAO();
                vaoList.add(newVAO);
                newVAO.addGraphic(newImage);
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
}
