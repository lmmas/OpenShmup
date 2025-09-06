package engine.scene.menu;

import engine.entity.hitbox.Hitbox;
import engine.scene.visual.SceneVisual;


public class MenuItem {
    private SceneVisual visual;

    private Hitbox clickHitbox;

    private Runnable onClick;

    public MenuItem(SceneVisual visual, Hitbox clickHitbox, Runnable onClick){
        this.visual = visual;
        this.clickHitbox = clickHitbox;
        this.onClick = onClick;
    }

    public SceneVisual getVisual(){
        return visual;
    }

    public Hitbox getClickHitbox(){
        return clickHitbox;
    }

    public void onClick() {
        onClick.run();
    }
}
