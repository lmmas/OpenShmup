package engine.level.entity.extraComponent;

import engine.Engine;
import engine.graphics.colorRectangle.ColorRectangle;
import engine.hitbox.SimpleRectangleHitbox;
import engine.level.Level;
import engine.level.entity.Entity;
import engine.level.spawnable.Spawnable;
import engine.types.RGBAValue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static engine.Engine.assetManager;
import static engine.GlobalVars.Paths.rootFolderAbsolutePath;

final public class HitboxDebugRectangle implements ExtraComponent {

    final static public Path hitboxDebugShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/debugRectangle.glsl");

    private final SimpleRectangleHitbox simpleRectangleHitbox;

    private final ColorRectangle debugDisplay;

    public HitboxDebugRectangle(SimpleRectangleHitbox simpleRectangleHitbox, RGBAValue color) {
        this.simpleRectangleHitbox = simpleRectangleHitbox;
        this.debugDisplay = new ColorRectangle(simpleRectangleHitbox.size.x, simpleRectangleHitbox.size.y, simpleRectangleHitbox.position.x, simpleRectangleHitbox.position.y, color.r, color.g, color.b, color.a, assetManager.getShader(rootFolderAbsolutePath.resolve(hitboxDebugShader)));
    }

    @Override
    public ExtraComponent copyIfNotReusable() {
        return this;
    }

    @Override
    public List<Spawnable> getSpawnables() {
        return List.of();
    }

    @Override
    public void init() {
        Engine.getGraphicsManager().addDebugGraphic(debugDisplay);
    }

    @Override
    public void onRemove() {
        this.debugDisplay.remove();
    }

    @Override
    public void update(Entity entity, Level level) {
        debugDisplay.setPosition(simpleRectangleHitbox.position.x, simpleRectangleHitbox.position.y);
        debugDisplay.setScale(simpleRectangleHitbox.size.x, simpleRectangleHitbox.size.y);
    }
}
