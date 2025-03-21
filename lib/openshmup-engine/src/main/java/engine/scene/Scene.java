package engine.scene;

import engine.EditorDataManager;
import engine.Game;
import engine.graphics.Graphic;
import engine.graphics.MovingImage;
import engine.graphics.StaticImage;
import engine.render.MovingImageVAO;
import engine.render.StaticImageVAO;
import engine.render.VAO;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;


abstract public class Scene {
    protected long window;
    final private EditorDataManager editorDataManager;
    protected TreeMap<Integer,ArrayList<VAO<?,?>>> layers;//TODO: remplacer par un ArrayList trié 1 fois au démarrage de la scène
    protected float sceneTime;
    protected SceneTimer timer;
    protected float lastDrawTime = 0.0f;
    HashSet<SceneVisual> visualList;
    public Scene(Game game) {
        this.window = game.getWindow();
        this.editorDataManager = game.getEditorDataManager();
        this.layers = new TreeMap<>();
        this.sceneTime = 0.0f;
        this.timer = new SceneTimer();
        this.visualList = new HashSet<>();
        timer.start();
    }

    abstract public void handleInputs();

    public void update(){
        if(!timer.isPaused()){
            sceneTime = timer.getTimeSeconds();
            for(SceneVisual visual: visualList){
                visual.update(sceneTime);
            }
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
                        case MOVING_IMAGE -> {
                            MovingImage newImage = (MovingImage) newGraphic;
                            MovingImageVAO movingImageVAO = (MovingImageVAO)currentVAO;
                            movingImageVAO.addGraphic(newImage);
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
            case MOVING_IMAGE -> {
                MovingImage newImage = (MovingImage) graphic;
                MovingImageVAO newVAO = new MovingImageVAO();
                vaoList.add(newVAO);
                newVAO.addGraphic(newImage);
            }
        }
    }

    public void addVisual(SceneVisual visual){
        Graphic<?,?>[] newGraphics = visual.getGraphics();
        for(Graphic<?,?> graphic: newGraphics){
            addGraphic(graphic);
        }
        visualList.add(visual);
        visual.setScene(this);
    }

    public void deleteVisual(SceneVisual visual){
        visualList.remove(visual);
        var graphics = visual.getGraphics();
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
