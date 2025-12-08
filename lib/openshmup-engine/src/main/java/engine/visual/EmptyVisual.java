package engine.visual;

import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.RenderInfo;

import java.util.List;

final public class EmptyVisual extends SceneVisual {
    public static EmptyVisual instance = null;

    private EmptyVisual() {
        super(0, List.of());
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
    public List<Graphic<?, ?>> getGraphics() {
        return List.of();
    }
}
