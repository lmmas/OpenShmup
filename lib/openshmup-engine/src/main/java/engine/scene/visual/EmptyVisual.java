package engine.scene.visual;

import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.render.RenderInfo;

import java.util.List;

final public class EmptyVisual extends SceneVisual {
    public static EmptyVisual instance = null;

    private EmptyVisual() {

    }

    public static EmptyVisual getInstance(){
        if(instance == null){
            instance = new EmptyVisual();
        }
        return instance;
    }

    @Override
    public SceneVisual copy() {
        return null;
    }

    @Override
    public List<RenderInfo> getRenderInfos() {
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
