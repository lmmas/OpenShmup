package engine.scene.menu;

import engine.entity.hitbox.Hitbox;
import engine.scene.visual.SceneVisual;

import java.util.List;


public class MenuItem {
    final private List<SceneVisual> visuals;

    private Hitbox clickHitbox;

    private Runnable onClick;

    public MenuItem(List<SceneVisual> visuals, Hitbox clickHitbox, Runnable onClick){
        this.clickHitbox = clickHitbox;
        this.onClick = onClick;
        this.visuals = visuals;
    }

    public List<SceneVisual> getVisuals(){
        return visuals;
    }

    public Hitbox getClickHitbox(){
        return clickHitbox;
    }

    public void onClick() {
        onClick.run();
    }
}
