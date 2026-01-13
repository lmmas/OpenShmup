package engine.scene.menu.item;

import engine.scene.Scene;

@FunctionalInterface
public interface MenuAction {

    void run(Scene scene);
}
