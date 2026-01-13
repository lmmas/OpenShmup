package engine.scene.menu;

import engine.entity.hitbox.Hitbox;
import engine.scene.menu.item.MenuAction;
import engine.visual.SceneVisual;
import lombok.Getter;

import java.util.List;

@Getter
public class MenuItem {


    final private List<SceneVisual> visuals;

    private final Hitbox clickHitbox;

    private MenuAction onClick;

    public MenuItem(List<SceneVisual> visuals, Hitbox clickHitbox, MenuAction onClick) {
        this.clickHitbox = clickHitbox;
        this.onClick = onClick;
        this.visuals = visuals;
    }

    public void setOnclick(MenuAction onClick) {
        this.onClick = onClick;
    }
}
