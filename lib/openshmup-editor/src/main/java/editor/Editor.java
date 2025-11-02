package editor;

import engine.Application;

import java.io.IOException;

final public class Editor extends Application {
    public Editor(String gameFolderName, Runnable initScript, Runnable inLoopScript) throws IOException {
        super(initScript, inLoopScript);
    }
}
