package engine.entity.extraComponent;

import engine.entity.Entity;
import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.render.Texture;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.List;

public interface ExtraComponent {
    ExtraComponent copyIfNotReusable();
    Spawnable getSpawnable();
    List<RenderInfo> getRenderInfos();
    List<Graphic<?,?>> getGraphics();
    List<Texture> getTextures();
    void setScene(LevelScene scene);
    void update(Entity entity);
}
