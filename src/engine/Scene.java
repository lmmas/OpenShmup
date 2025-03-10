package engine;

import engine.render.*;
import engine.visual.SimpleSprite;
import engine.visual.StaticImage;
import engine.visual.Visual;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


abstract public class Scene {
    protected long window;
    protected TreeMap<Integer,ArrayList<VAO<?,?>>> layers = new TreeMap<>();//TODO: remplacer par un ArrayList trié 1 fois au démarrage de la scène

    public Scene(long window) {
        this.window = window;
    }

    abstract public void update();

    public void draw(){
        for(Map.Entry<Integer,ArrayList<VAO<?,?>>> vaos: layers.entrySet()){
            for(VAO<?,?> vao: vaos.getValue()){
                vao.draw();
            }
        }
    }

    void addVisual( Visual<?,?> newVisual){
        int visualLayer = newVisual.getLayer();
        ArrayList<VAO<?,?>> vaos = layers.get(visualLayer);
        if(vaos!=null) {
            boolean newVisualAllocated = false;
            int i = 0;
            while (!newVisualAllocated && i < vaos.size()) {
                VAO<?,?> currentVAO = vaos.get(i);
                if (currentVAO.getType() == newVisual.getType()) {
                    switch(newVisual.getType()){
                        case STATIC_IMAGE -> {
                            StaticImage newImage = (StaticImage) newVisual;
                            StaticImageVAO staticImageVAO = (StaticImageVAO)currentVAO;
                            staticImageVAO.addVisual(newImage);
                        }
                        case SIMPLE_SPRITE -> {
                            SimpleSprite newSprite = (SimpleSprite) newVisual;
                            SimpleSpriteVAO simpleSpriteVAO = (SimpleSpriteVAO)currentVAO;
                            simpleSpriteVAO.addVisual(newSprite);
                        }
                    }
                    newVisualAllocated = true;
                }
                i++;
            }
            if (!newVisualAllocated) {
                createNewVAOFromVisual(newVisual, vaos);
            }
        }
        else{
            vaos = new ArrayList<>();
            layers.put(visualLayer, vaos);
            createNewVAOFromVisual(newVisual, vaos);
        }
    }

    private void createNewVAOFromVisual(Visual<?,?> visual, ArrayList<VAO<?,?>> vaoList){
        switch (visual.getType()) {
            case STATIC_IMAGE-> {
                StaticImage newImage = (StaticImage) visual;
                StaticImageVAO newVAO = new StaticImageVAO();
                vaoList.add(newVAO);
                newVAO.addVisual(newImage);
            }
            case SIMPLE_SPRITE-> {
                SimpleSprite newSprite = (SimpleSprite) visual;
                SimpleSpriteVAO newVAO = new SimpleSpriteVAO();
                vaoList.add(newVAO);
                newVAO.addVisual(newSprite);
            }
        }
    }
}
