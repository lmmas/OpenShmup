package engine.scene.visual;

import engine.GlobalVars;
import engine.assets.Texture;
import engine.graphics.ColorRectangle;
import engine.graphics.Graphic;
import engine.render.RenderInfo;

import java.util.List;

import static engine.Application.assetManager;

final public class ScreenFilter extends SceneVisual{
    private ColorRectangle colorRectangle;

    public ScreenFilter(int layer, float r, float g, float b, float a){
        this.colorRectangle = new ColorRectangle(layer, 1.0f, 1.0f, r, g, b, a, assetManager.getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/colorRectangle.glsl"));
        colorRectangle.setPosition(0.5f, 0.5f);
    }

    public ScreenFilter(ScreenFilter screenFilter){
        this.colorRectangle = screenFilter.colorRectangle.copy();
    }

    @Override
    public SceneVisual copy() {
        return new ScreenFilter(this);
    }

    @Override
    public List<RenderInfo> getRenderInfos() {
        return List.of(colorRectangle.getRenderInfo());
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(colorRectangle);
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
        colorRectangle.setScale(scaleX, scaleY);
    }

    @Override
    public void initDisplay(float startingTimeSeconds) {

    }

    @Override
    public void update(float currentTimeSeconds) {

    }
}
