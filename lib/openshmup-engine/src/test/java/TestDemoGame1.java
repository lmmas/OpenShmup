import engine.Game;

import java.io.IOException;

public class TestDemoGame1 {

    public static void main(String[] args) throws IOException {
        String folderName = "demoGame1";
        String[] gameArgs = new String[]{folderName};
        Game.main(gameArgs);
    }
}
