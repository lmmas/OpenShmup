package engine.entity.extraComponent;

import engine.GlobalVars;
import engine.entity.Entity;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.graphics.ColorRectangle;
import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.assets.Shader;
import engine.assets.Texture;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.Collections;
import java.util.List;

final public class HitboxDebugDisplay implements ExtraComponent{
    static private Shader hitboxShader;
    static public void setHitboxShader(Shader hitboxShader){
        HitboxDebugDisplay.hitboxShader = hitboxShader;
    }
    private final SimpleRectangleHitbox simpleRectangleHitbox;
    private final ColorRectangle debugDisplay;

    public HitboxDebugDisplay(SimpleRectangleHitbox simpleRectangleHitbox, float r, float g, float b, float a){
        this.simpleRectangleHitbox = simpleRectangleHitbox;
        this.debugDisplay = new ColorRectangle(GlobalVars.debugDisplayLayer, simpleRectangleHitbox.size.x, simpleRectangleHitbox.size.y, r, g, b, a, hitboxShader);
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
        debugDisplay.setPosition(simpleRectangleHitbox.position.x, simpleRectangleHitbox.position.y);
        debugDisplay.setSize(simpleRectangleHitbox.size.x, simpleRectangleHitbox.size.y);
    }
}
