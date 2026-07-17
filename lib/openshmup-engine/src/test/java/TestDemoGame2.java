import engine.Engine;
import engine.Game;
import json.GameEditionData;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestDemoGame2 {

    public static void main(String[] args) throws Exception {
        String folderName = "demoGame2";
        Path rootFolderAbsolutePath = Paths.get(Engine.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent().getParent().getParent();
        Path folderPath = rootFolderAbsolutePath.resolve("Games").resolve(folderName);
        GameEditionData gameEditionData = new GameEditionData(folderName, folderPath);
        gameEditionData.loadGameContents();
        Game.init(gameEditionData);
        Game.run();
    }
}
