package engine.scene.menu;

import engine.Engine;
import engine.Game;
import engine.scene.menu.item.MenuAction;

final public class MenuActions {

    final public static MenuAction reloadGame = scene -> Game.gameInit();

    final public static MenuAction terminateProgram = scene -> Engine.setProgramShouldTerminate();
}
