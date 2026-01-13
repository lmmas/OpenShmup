package engine.menu;

import engine.scene.Scene;

@FunctionalInterface
public interface MenuAction {

    void run(Scene scene);
}
