package engine.scene;

import engine.graphics.Graphic;
import engine.graphics.MovingImage;
import engine.graphics.StaticImage;
import engine.render.MovingImageVAO;
import engine.render.StaticImageVAO;
import engine.render.VAO;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


abstract public class Scene {
    protected long window;
    protected TreeMap<Integer,ArrayList<VAO<?,?>>> layers = new TreeMap<>();//TODO: remplacer par un ArrayList trié 1 fois au démarrage de la scène
    protected float sceneTime = 0.0f;
    protected SceneTimer timer = new SceneTimer();
    ArrayList<SceneVisual> visualList = new ArrayList<>();
    public Scene(long window) {
        this.window = window;
        timer.start();
    }

    abstract public void handleInputs();

    public void update(){
        if(!timer.isPaused()){
            sceneTime = timer.getTimeSeconds();
            for(SceneVisual visual: visualList){
                visual.update();
            }
        }
    }

    public void draw(){
        for(Map.Entry<Integer,ArrayList<VAO<?,?>>> vaos: layers.entrySet()){
            for(VAO<?,?> vao: vaos.getValue()){
                vao.draw();
            }
        }
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
        visualList.add(visual);
    }

    public void removeVisual(SceneVisual visual){
        visualList.remove(visual);
    }

    public float getSceneTime() {
        return sceneTime;
    }

    final public void setSpeed(float speed){
        timer.setSpeed(speed);
    }
}
