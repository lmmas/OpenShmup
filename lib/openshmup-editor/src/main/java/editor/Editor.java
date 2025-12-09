package editor;

import editor.scenes.MainMenuScene;
import engine.Application;

import java.io.IOException;

final public class Editor extends Application {
    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            throw new IllegalArgumentException("invalid editor arguments");
        }
        new Editor(() -> {
        }, () -> {
        });
    }

    public Editor(Runnable initScript, Runnable inLoopScript) throws IOException {
        super(initScript, inLoopScript);
        window.setResolution(1920, 1080);
        currentScene = new MainMenuScene();
        window.show();
        initScript.run();
        loop();
        terminate();
    }
}
