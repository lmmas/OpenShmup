import engine.Engine;

import java.io.IOException;

public class TestDemoGame {
    public static void main(String[] args) throws IOException {
        String folderName = "demoGame";
        String[] gameArgs = new String[]{folderName};
        Engine.main(gameArgs);
    }
}
