package com.github.lmmas;

import edition.GameEditionData;
import json.readers.GameDataReader;

import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameLauncher {

    public static void main(String[] args) throws Exception {
        Path gameFolder;
        String appHome = System.getProperty("app.home");
        if (appHome != null) {
            gameFolder = Paths.get(appHome).toAbsolutePath();
        }
        else {
            gameFolder = Paths.get("").toAbsolutePath();
        }
        System.out.println("Detected game folder: " + gameFolder);
        GameEditionData gameEditionData = new GameDataReader().readGameData(gameFolder);

        Path projectFolder = gameFolder.getParent().getParent();
        Path enginePath = projectFolder.resolve("lib/openshmup-engine/gameEngine.jar");
        System.out.println("Detected engine path: " + enginePath.toAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(
            "../../lib/openjdk-21.0.1/bin/javaw.exe", "-Dapp.home=" + projectFolder.toAbsolutePath(), "-jar", enginePath.toAbsolutePath().toString());
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = pb.start();
        try (ObjectOutputStream out = new ObjectOutputStream(process.getOutputStream())) {
            out.writeObject(gameEditionData);
            out.flush();
        }
        process.waitFor();
    }
}
