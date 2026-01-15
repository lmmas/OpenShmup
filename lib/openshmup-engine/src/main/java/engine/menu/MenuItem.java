package engine.menu;

import engine.level.entity.hitbox.Hitbox;
import engine.scene.visual.SceneVisual;
import lombok.Getter;

import java.util.List;

@Getter
public class MenuItem {


    final private List<SceneVisual> visuals;

    private final Hitbox clickHitbox;

    private Runnable onClick;

    public MenuItem(List<SceneVisual> visuals, Hitbox clickHitbox, Runnable onClick) {
        this.clickHitbox = clickHitbox;
        this.onClick = onClick;
        this.visuals = visuals;
    }

    public void setOnclick(Runnable onClick) {
        this.onClick = onClick;
    }
}
