package engine.visual;

import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.RenderInfo;

import java.util.List;

final public class EmptyVisual extends SceneVisual {
    public static EmptyVisual instance = null;

    private EmptyVisual() {
        super(0);
    }

    public static EmptyVisual getInstance(){
        if(instance == null){
            instance = new EmptyVisual();
        }
        return instance;
    }

    @Override
    public SceneVisual copy() {
        return getInstance();
    }

    @Override
    public List<Integer> getGraphicalSubLayers() {
        return List.of();
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of();
    }

    @Override
    public List<Texture> getTextures() {
        return List.of();
    }

    @Override
    public void setPosition(float positionX, float positionY) {

    }

    @Override
    public void setScale(float scaleX, float scaleY) {

    }

    @Override
    public void initDisplay(float startingTimeSeconds) {

    }

    @Override
    public void update(float currentTimeSeconds) {

    }
}
