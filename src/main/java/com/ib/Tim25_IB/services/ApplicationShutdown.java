package com.ib.Tim25_IB.services;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ApplicationShutdown {

    @EventListener
    public void onApplicationShutdown(ContextClosedEvent event) {
        // Define the folder path to be emptied
        String folderPath = "src/main/resources/Temp/";

        // Get the folder as a Path object
        Path folder = Paths.get(folderPath);

        // Check if the folder exists
        if (Files.exists(folder)) {
            try {
                // Delete all files and subdirectories within the folder
                Files.walk(folder)
                        .sorted((path1, path2) -> -path1.compareTo(path2)) // Sort in reverse order for subdirectories
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any exceptions during the cleanup process
            }
        }
    }
}
