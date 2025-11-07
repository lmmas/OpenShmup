package engine.entity.extraComponent;

import engine.GlobalVars;
import engine.entity.Entity;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.graphics.colorRectangle.ColorRectangle;
import engine.graphics.Graphic;
import engine.graphics.RenderInfo;
import engine.assets.Texture;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;
import engine.types.RGBAValue;

import java.util.Collections;
import java.util.List;

import static engine.Application.assetManager;

final public class HitboxDebugRectangle implements ExtraComponent{
    final static public String hitboxDebugShader = "/lib/openshmup-engine/src/main/resources/shaders/debugRectangle.glsl";
    private final SimpleRectangleHitbox simpleRectangleHitbox;
    private final ColorRectangle debugDisplay;

    public HitboxDebugRectangle(SimpleRectangleHitbox simpleRectangleHitbox, RGBAValue color){
        this.simpleRectangleHitbox = simpleRectangleHitbox;
        this.debugDisplay = new ColorRectangle(GlobalVars.debugDisplayLayer, simpleRectangleHitbox.position.x, simpleRectangleHitbox.position.y, simpleRectangleHitbox.size.x, simpleRectangleHitbox.size.y, color.r, color.g, color.b, color.a, assetManager.getShader(hitboxDebugShader));
    }

    @Override
    public ExtraComponent copyIfNotReusable() {
        return this;
    }

    @Override
    public Spawnable getSpawnable() {
        return Spawnable.DEFAULT_EMPTY();
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
        debugDisplay.setScale(simpleRectangleHitbox.size.x, simpleRectangleHitbox.size.y);
    }
}
