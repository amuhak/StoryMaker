package prizehoggers.songmaker.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import prizehoggers.songmaker.service.editor.VideoEditorConverter;
import prizehoggers.songmaker.service.editor.VideoEditorJob;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VideoEditingService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(VideoEditingService.class);
    public static ConcurrentHashMap<String, VideoEditorConverter.Status> editingStatus = new ConcurrentHashMap<>();
    @Value("${thread.pool.size}")
    private static int maximumPoolSize = 1;
    private static final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(1, maximumPoolSize, 5, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

    public static void addJob(VideoEditorJob job) {
        executor.execute(job);
    }

    public void editVideo(Path oldPath, String hash, String extension, String codec, String crop, String trimStart,
                          String trimEnd, String resolution, String fps) {
        AtomicBoolean isDoneInternal = new AtomicBoolean(false);
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicInteger parts = new AtomicInteger(0);
        VideoEditorConverter.Status status = new VideoEditorConverter.Status(isDoneInternal, isDone, parts);

        Path newPath = Path.of("downloads/" + hash + "." + extension).toAbsolutePath(); // Assuming mp4 output for now
        try {
            Files.createDirectories(newPath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        editingStatus.put(hash + ".mp4", status);

        VideoEditorJob job =
                new VideoEditorJob(oldPath, newPath, status, hash, extension, codec, crop, trimStart, trimEnd,
                        resolution, fps);

        VideoEditingService.addJob(job);
    }
}
