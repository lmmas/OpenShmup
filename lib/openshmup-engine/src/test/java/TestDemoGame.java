import engine.Game;

import java.io.IOException;

public class TestDemoGame {
    public static void main(String[] args) throws IOException {
        String folderName = "demoGame2";
        String[] gameArgs = new String[]{folderName};
        Game.main(gameArgs);
    }
}
