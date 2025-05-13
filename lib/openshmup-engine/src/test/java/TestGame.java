import engine.Engine;

import java.io.IOException;

public class TestGame {
    public static void main(String[] args) throws IOException {
        String folderName = "testGame";
        String[] gameArgs = new String[]{folderName};
        Engine.main(gameArgs);
    }
}
