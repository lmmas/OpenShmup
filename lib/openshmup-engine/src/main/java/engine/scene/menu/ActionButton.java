package engine.scene.menu;

import engine.assets.Texture;
import engine.entity.hitbox.Hitbox;
import engine.graphics.Graphic;
import engine.render.RenderInfo;

import java.util.List;

final public class ActionButton implements MenuItem{
    private Hitbox clickHitbox;
    private Runnable buttonAction;

    @Override
    public List<RenderInfo> getRenderInfos() {
        return List.of();
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of();
    }

    @Override
    public List<Texture> getTexture() {
        return List.of();
    }

    @Override
    public Hitbox getClickHitbox() {
        return null;
    }

    @Override
    public void onClick() {
        buttonAction.run();
    }
}
