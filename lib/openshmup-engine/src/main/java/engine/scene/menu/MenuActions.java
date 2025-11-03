package engine.scene.menu;

import engine.Application;
import engine.Engine;

final public class MenuActions{
    final public static Runnable reloadGame = Engine::gameInit;

    final public static Runnable terminateProgram = Application::setProgramShouldTerminate;
}
