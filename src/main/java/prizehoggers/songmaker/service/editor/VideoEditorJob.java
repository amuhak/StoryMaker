package prizehoggers.songmaker.service.editor;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class VideoEditorJob implements Runnable {
    public final Logger logger = org.slf4j.LoggerFactory.getLogger(VideoEditorJob.class);
    private final Path inputFilePath;
    private final Path outputFilePath;
    private final VideoEditorConverter.Status done;
    private final String hash;
    private final String extension;
    private final String codec;
    private final String crop;
    private final String trimStart;
    private final String trimEnd;
    private final String resolution;
    private final String fps;


    public VideoEditorJob(Path inputFilePath, Path outputFilePath, VideoEditorConverter.Status done, String hash,
                          String extension, String codec, String crop, String trimStart, String trimEnd,
                          String resolution, String fps) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.done = done;
        this.hash = hash;
        this.extension = extension;
        this.codec = codec;
        this.crop = crop;
        this.trimStart = trimStart;
        this.trimEnd = trimEnd;
        this.resolution = resolution;
        this.fps = fps;
    }

    @Override
    public void run() {
        List<String> commandList = new ArrayList<>();
        commandList.add("ffmpeg");
        commandList.add("-i");
        commandList.add("\"" + inputFilePath.toAbsolutePath() + "\"");

        if (extension != null && !extension.isEmpty()) {
            // Changing extension is handled at the output file path
        }

        if (codec != null && !codec.isEmpty()) {
            commandList.add("-c:v");
            commandList.add(codec);
        }

        if (crop != null && !crop.isEmpty()) {
            commandList.add("-filter:v");
            commandList.add("crop=" + crop); // Example: "crop=w:h:x:y"
        }
        if (trimStart != null && !trimStart.isEmpty() && trimEnd != null && !trimEnd.isEmpty()) {
            commandList.add("-ss");
            commandList.add(trimStart); // Example: "00:00:10"
            commandList.add("-to");
            commandList.add(trimEnd); // Example: "00:00:20"
        } else if (trimStart != null && !trimStart.isEmpty()) {
            commandList.add("-ss");
            commandList.add(trimStart);
        } else if (trimEnd != null && !trimEnd.isEmpty()) {
            commandList.add("-to");
            commandList.add(trimEnd);
        }


        if (resolution != null && !resolution.isEmpty()) {
            commandList.add("-vf");
            commandList.add("scale=" + resolution); // Example: "scale=640:480"
        }

        if (fps != null && !fps.isEmpty()) {
            commandList.add("-r");
            commandList.add(fps); // Example: "30"
        }


        String outputFileName = outputFilePath.toAbsolutePath().toString();
        if (extension != null && !extension.isEmpty()) {
            String baseName = outputFileName.substring(0, outputFileName.lastIndexOf("."));
            outputFileName = baseName + "." + extension;
        }
        commandList.add("\"" + outputFileName + "\"");
        commandList.add("-y"); // Overwrite output files without asking

        String[] command = commandList.toArray(new String[0]);

        logger.info("Full FFmpeg command: {}", String.join(" ", command));
        logger.info("Starting the process: {}", Arrays.toString(command));
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            logger.info("Process started");

            // Capture and log output from the process
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), System.err::println);

            outputGobbler.start();
            errorGobbler.start();

            int exitCode = process.waitFor();
            outputGobbler.join();
            errorGobbler.join();

            logger.info("Process finished with exit code: {}", exitCode);
        } catch (IOException e) {
            logger.error("Failed to start the process: {}", e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Process interrupted: ", e);
        }
        done.doneInternal().set(true);

        try {
            Files.delete(inputFilePath);    // Delete the old file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path outputPath = Paths.get(outputFileName); // Output will always be mp4 after processing
        try {
            Files.createDirectories(Path.of(
                    outputPath.getParent() + "/" + hash ));   // create directories "downloads/{hash}"
        } catch (IOException e) {
            logger.error("Error creating output directories: ", e); // Log directory creation error
            done.done().set(true); // Indicate failure to frontend
            done.doneInternal().set(true);
            return; // Stop further processing
        }
        long noOfChunks = 0;
        try {
            noOfChunks = splitFile(outputFilePath, Path.of(outputPath.getParent()+ "/" + hash), 90 * 1024 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        done.parts().set((int) noOfChunks);
        done.done().set(true);
        try {
            Files.delete(outputFilePath); // Delete combined file after splitting
        } catch (IOException e) {
            logger.error("Error deleting combined output file: ", e);
        } finally {
            done.parts().set((int) noOfChunks);
            done.done().set(true);
            done.doneInternal().set(true);
        }
    }


    public long splitFile(Path inputPath, Path outputPath, int chunkSize) throws IOException {
        byte[] buffer = new byte[chunkSize];
        int chunkNumber = 0;
        long totalChunks = 0;
        try (InputStream inputStream = Files.newInputStream(inputPath)) {
            while (inputStream.available() > 0) {
                Path chunkPath = Paths.get(outputPath.toString() + "/" + chunkNumber + ".part");
                try (OutputStream outputStream = Files.newOutputStream(chunkPath)) {
                    int bytesRead = inputStream.read(buffer);
                    outputStream.write(buffer, 0, bytesRead);
                    totalChunks++;
                }
                chunkNumber++;
            }
        }
        return totalChunks;
    }


    private static class StreamGobbler extends Thread {
        private final InputStream inputStream;
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try (Scanner sc = new Scanner(inputStream)) {
                while (sc.hasNextLine()) {
                    consumer.accept(sc.nextLine());
                }
            }
        }
    }
}
