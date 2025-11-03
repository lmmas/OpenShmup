package engine.scene.visual;

import engine.GlobalVars;
import engine.assets.Shader;
import engine.assets.Texture;
import engine.graphics.ColorRectangle;
import engine.graphics.Graphic;
import engine.render.RenderInfo;

import java.util.List;

import static engine.Application.assetManager;

final public class ColorRectangleVisual extends SceneVisual{
    final private ColorRectangle colorRectangle;

    public ColorRectangleVisual(ColorRectangle colorRectangle) {
        this.colorRectangle = colorRectangle;
    }

    public ColorRectangleVisual(int layer, float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a) {
        this(new ColorRectangle(layer, sizeX, sizeY, r, g, b, a, assetManager.getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/colorRectangle.glsl")));
        colorRectangle.setPosition(positionX, positionY);
    }


    @Override
    public SceneVisual copy() {
        return new ColorRectangleVisual(colorRectangle.copy());
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
        colorRectangle.setPosition(positionX, positionY);
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
