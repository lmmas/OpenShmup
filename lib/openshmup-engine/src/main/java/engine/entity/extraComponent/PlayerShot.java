package engine.entity.extraComponent;

import engine.types.Vec2D;
import engine.entity.Entity;
import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.render.Texture;
import engine.scene.GameControl;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.Collections;
import java.util.List;

public class PlayerShot implements ExtraComponent{
    private final Spawnable spawnable;
    private final float shotPeriodSeconds;
    private float nextShotTimeSeconds;
    public PlayerShot(Spawnable spawnable, float shotPeriodSeconds, float firstShotTimeSeconds) {
        this.spawnable = spawnable;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = firstShotTimeSeconds;
    }
    @Override
    public ExtraComponent copyIfNotReusable() {
        return this;
    }

    @Override
    public Spawnable getSpawnable() {
        return spawnable;
    }

    @Override
    public List<RenderInfo> getRenderInfos() {
        return Collections.emptyList();
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return Collections.emptyList();
    }

    @Override
    public List<Texture> getTextures() {
        return Collections.emptyList();
    }

    @Override
    public void update(Entity entity, LevelScene scene) {
        float currentTime = entity.getLifetimeSeconds();
        if(scene.getControlState(GameControl.FIRE) && currentTime >= nextShotTimeSeconds){
            Vec2D position = entity.getPosition();
            spawnable.copyWithOffset(position.x, position.y).spawn(scene);
            nextShotTimeSeconds = currentTime + shotPeriodSeconds;
        }
    }
}
