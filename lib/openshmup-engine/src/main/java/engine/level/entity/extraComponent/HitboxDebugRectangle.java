package engine.level.entity.extraComponent;

import engine.Engine;
import engine.graphics.colorRectangle.ColorRectangleGraphic;
import engine.hitbox.RectangleBox;
import engine.level.Level;
import engine.level.entity.Entity;
import engine.level.spawnable.Spawnable;
import types.RGBAValue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static engine.Engine.assetManager;

final public class HitboxDebugRectangle implements ExtraComponent {

    final static public Path hitboxDebugShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/debugRectangle.glsl");

    private final RectangleBox rectangleBox;

    private final ColorRectangleGraphic debugDisplay;

    public HitboxDebugRectangle(RectangleBox rectangleBox, RGBAValue color) {
        this.rectangleBox = rectangleBox;
        this.debugDisplay = new ColorRectangleGraphic(rectangleBox.getSize(), rectangleBox.getPosition(), color, assetManager.getShader(hitboxDebugShader));
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
        debugDisplay.setPosition(rectangleBox.getPosition());
        debugDisplay.setScale(rectangleBox.getSize());
    }
}
