package engine.entity.extraComponent;

import engine.entity.Entity;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.graphics.colorRectangle.ColorRectangle;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;
import engine.types.RGBAValue;

import static engine.Engine.assetManager;
import static engine.Engine.graphicsManager;

final public class HitboxDebugRectangle implements ExtraComponent {
    final static public String hitboxDebugShader = "/lib/openshmup-engine/src/main/resources/shaders/debugRectangle.glsl";
    private final SimpleRectangleHitbox simpleRectangleHitbox;
    private final ColorRectangle debugDisplay;

    public HitboxDebugRectangle(SimpleRectangleHitbox simpleRectangleHitbox, RGBAValue color) {
        this.simpleRectangleHitbox = simpleRectangleHitbox;
        this.debugDisplay = new ColorRectangle(simpleRectangleHitbox.size.x, simpleRectangleHitbox.size.y, simpleRectangleHitbox.position.x, simpleRectangleHitbox.position.y, color.r, color.g, color.b, color.a, assetManager.getShader(hitboxDebugShader));
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
    public void init() {
        graphicsManager.addDebugGraphic(debugDisplay);
    }

    @Override
    public void onRemove() {
        this.debugDisplay.remove();
    }

    @Override
    public void update(Entity entity, LevelScene scene) {
        debugDisplay.setPosition(simpleRectangleHitbox.position.x, simpleRectangleHitbox.position.y);
        debugDisplay.setScale(simpleRectangleHitbox.size.x, simpleRectangleHitbox.size.y);
    }
}
