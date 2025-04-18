package gamebuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.stream.*;

public class GameBuilder {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Path rootFolderPath = java.nio.file.Paths.get(GameBuilder.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent();
        Path gamesDir = rootFolderPath.resolve("Games");
        Path gameJAR = rootFolderPath.resolve ("lib/openshmup-gameExecutable/target/openshmup-gameExecutable-1.0-SNAPSHOT.jar");

        if (!Files.exists(gameJAR)) {
            System.err.println("Engine JAR not found: " + gameJAR);
            return;
        }
        try (Stream<Path> paths = Files.list(gamesDir)) {
            paths.filter(Files::isDirectory).forEach(gameFolder -> {
                Path targetPath = gameFolder.resolve(gameFolder.getFileName() + ".jar");
                try {
                    Files.copy(gameJAR, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Copied game to " + targetPath);
                } catch (IOException e) {
                    System.err.println("Failed to copy to " + gameFolder + ": " + e.getMessage());
                }
            });
        }

        System.out.println("Game builder finished.");

    }
}
