package engine.menu;

import engine.Engine;
import engine.Game;

final public class MenuActions {

    final public static MenuAction reloadGame = scene -> Game.gameInit();

    final public static MenuAction terminateProgram = scene -> Engine.setProgramShouldTerminate();
}
