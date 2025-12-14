package editor;

import editor.scenes.MainMenuScene;
import engine.Engine;

import java.io.IOException;

final public class Editor extends Engine {
    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            throw new IllegalArgumentException("invalid editor arguments");
        }
        new Editor(() -> {
        }).run();
    }

    public Editor(Runnable inLoopScript) throws IOException {
        super(inLoopScript);
        window.setResolution(1920, 1080);
        currentScene = new MainMenuScene();
        window.show();
    }
}
