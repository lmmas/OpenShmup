package engine.entity.extraComponent;

import engine.GlobalVars;
import engine.entity.Entity;
import engine.entity.hitbox.HitboxRectangle;
import engine.graphics.ColorRectangle;
import engine.graphics.DynamicImage;
import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.render.Shader;
import engine.render.Texture;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.Collections;
import java.util.List;

public class HitboxDebugDisplay implements ExtraComponent{
    private final HitboxRectangle hitboxRectangle;
    private final ColorRectangle debugDisplay;

    public HitboxDebugDisplay(HitboxRectangle hitboxRectangle){
        this.hitboxRectangle = hitboxRectangle;
        this.debugDisplay = new ColorRectangle(GlobalVars.debugDisplayLayer, hitboxRectangle.size.x, hitboxRectangle.size.y, 1.0f, 1.0f, 1.0f, 1.0f, Shader.loadShader("lib/openshmup-engine/src/main/resources/shaders/debugRectangle.glsl"));
    }

    @Override
    public ExtraComponent copyIfNotReusable() {
        return this;
    }

    @Override
    public Spawnable getSpawnable() {
        return Spawnable.DEFAULT();
    }

    @Override
    public List<RenderInfo> getRenderInfos() {
        return List.of(debugDisplay.getRenderInfo());
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(debugDisplay);
    }

    @Override
    public List<Texture> getTextures() {
        return Collections.emptyList();
    }

    @Override
    public void update(Entity entity, LevelScene scene) {
        debugDisplay.setPosition(hitboxRectangle.position.x, hitboxRectangle.position.y);
        debugDisplay.setSize(hitboxRectangle.size.x, hitboxRectangle.size.y);
    }
}
