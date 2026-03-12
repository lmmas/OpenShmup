package engine.menu;

import engine.Engine;
import engine.Game;

final public class MenuActions {

    final public static Runnable reloadGame = Game::gameStart;

    final public static Runnable terminateProgram = Engine::setProgramShouldTerminate;
}
