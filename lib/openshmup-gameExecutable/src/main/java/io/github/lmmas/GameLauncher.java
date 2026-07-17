package io.github.lmmas;

import json.GameEditionData;

import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameLauncher {

    public static void main(String[] args) throws Exception {
        Path gameFolder = Paths.get(GameLauncher.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI())
            .getParent();
        GameEditionData gameEditionData = new GameEditionData(gameFolder.getFileName().toString(), gameFolder);
        gameEditionData.loadGameContents();

        Path projectFolder = gameFolder.getParent().getParent();
        System.out.println("Detected game folder: " + gameFolder);
        Path enginePath = projectFolder.resolve("lib/openshmup-engine/target/openshmup-engine-1.0-SNAPSHOT.jar");
        System.out.println("Detected engine path: " + enginePath.toAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(
            "java", "-jar", enginePath.toAbsolutePath().toString());
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
