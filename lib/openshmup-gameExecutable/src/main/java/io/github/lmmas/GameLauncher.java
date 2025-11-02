package io.github.lmmas;

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
        Path projectFolder = gameFolder.getParent().getParent();
        System.out.println("Detected game folder: " + gameFolder);
        Path enginePath = projectFolder.resolve("lib/openshmup-engine/target/openshmup-engine-1.0-SNAPSHOT.jar");
        System.out.println("Detected engine path: " + enginePath.toAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(
                "java", "-jar", enginePath.toAbsolutePath().toString(), gameFolder.getFileName().toString()
        );
        pb.inheritIO();
        Process process = pb.start();
        process.waitFor();

    }
}
