import edition.GameEditionData;
import engine.Engine;
import engine.Game;
import json.readers.GameDataReader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestDemoGame1 {

    public static void main(String[] args) throws Exception {
        String folderName = "demoGame1";
        Path rootFolderAbsolutePath = Paths.get(Engine.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent().getParent().getParent();
        Path folderPath = rootFolderAbsolutePath.resolve("Games").resolve(folderName);
        GameEditionData gameEditionData = new GameDataReader().readGameData(folderPath);
        Game.init(gameEditionData);
        Game.run();
    }
}
