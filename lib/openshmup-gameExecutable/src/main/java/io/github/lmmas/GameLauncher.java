package io.github.lmmas;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameLauncher {
    public static void main(String[] args) throws Exception {
        try {
            // Get the path of the running JAR
            Path gameFolder = Paths.get(GameLauncher.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI())
                    .getParent();
            Path projectFolder = gameFolder.getParent().getParent();
            // Now pass it to your engine
            System.out.println("Detected game folder: " + gameFolder);
            Path enginePath = projectFolder.resolve("lib/openshmup-engine/target/openshmup-engine-1.0-SNAPSHOT-shaded.jar");
            System.out.println("Detected engine path: " + enginePath.toAbsolutePath());
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", enginePath.toAbsolutePath().toString(), gameFolder.getFileName().toString()
            );
            System.out.println("command: " + pb.command());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
