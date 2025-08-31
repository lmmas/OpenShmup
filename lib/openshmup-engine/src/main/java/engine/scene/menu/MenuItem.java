package engine.scene.menu;

import engine.assets.Texture;
import engine.entity.hitbox.Hitbox;
import engine.graphics.Graphic;
import engine.render.RenderInfo;

import java.util.List;

public interface MenuItem {
    List<RenderInfo> getRenderInfos();
    List<Graphic<?, ?>> getGraphics();
    List<Texture> getTexture();
    Hitbox getClickHitbox();
    void onClick();
}
