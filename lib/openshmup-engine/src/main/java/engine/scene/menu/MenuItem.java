package engine.scene.menu;

import engine.entity.hitbox.Hitbox;
import engine.visual.SceneVisual;
import lombok.Getter;

import java.util.List;


public class MenuItem {

    @Getter
    final private List<SceneVisual> visuals;
    @Getter
    private final Hitbox clickHitbox;

    private Runnable onClick;

    public MenuItem(List<SceneVisual> visuals, Hitbox clickHitbox, Runnable onClick) {
        this.clickHitbox = clickHitbox;
        this.onClick = onClick;
        this.visuals = visuals;
    }

    public void onClick() {
        if (onClick != null) {
            onClick.run();
        }
    }

    public void setOnclick(Runnable onClick) {
        this.onClick = onClick;
    }
}
