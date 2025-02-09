package prizehoggers.songmaker.Controller;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prizehoggers.songmaker.service.VideoEditingService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@EnableAsync
@Controller
@RequestMapping("/videoeditor/")
public class VideoEditorController {

    @Autowired
    private VideoEditingService videoEditingService;
    public final Logger logger = org.slf4j.LoggerFactory.getLogger(VideoEditorController.class);


    @PostMapping("/upload-chunk")
    ResponseEntity<?> uploadChunk(@RequestParam("Hash") String hash, @RequestParam("FileName") String fileName,
                                  @RequestParam("Chunk") MultipartFile chunk, @RequestParam("Index") int index,
                                  @RequestParam("TotalChunks") int totalChunks) {
        logger.info("Uploading chunk: {} of {} for hash {}", index, totalChunks, hash);
        if (chunk.isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "Chunk is empty"));
        }
        try {
            Path chunkPath = Paths.get("uploads/" + hash + "/" + index + ".part");
            Files.createDirectories(chunkPath.getParent());  // create directories "uploads/hash"
            Files.write(chunkPath, chunk.getBytes());
            return ResponseEntity.ok().body(Map.of("Good", "Good"));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to upload chunk: " + e.getMessage()));
        }
    }

    @PostMapping("/combine")
    public ResponseEntity<?> combineChunks(@RequestParam("FileName") String fileName,
                                           @RequestParam("NoOfChunks") int NoOfChunks,
                                           @RequestParam("Hash") String hash) {
        logger.info("Combining chunks for hash: {}", hash);
        String extension = fileName.substring(fileName.lastIndexOf("."));
        Path outputPath = Paths.get("uploads" + "/" + hash + extension);
        try (OutputStream out = Files.newOutputStream(outputPath)) {
            for (int i = 0; i < NoOfChunks; i++) {
                Path chunkPath = Paths.get("uploads/" + hash + "/" + i + ".part");
                Files.copy(chunkPath, out);
            }
            FileUtils.deleteDirectory(Paths.get("uploads/" + hash).toFile());
        } catch (IOException e) {
            logger.error("Failed to combine chunks: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Failed to combine chunks" + e.getMessage()));
        }
        return ResponseEntity.ok().body(Map.of("success", "Chunks combined successfully"));
    }

    @PostMapping("/process")
    public ResponseEntity<?> process(
            @RequestParam("Hash") String hash,
            @RequestParam("FileName") String fileName,
            @RequestParam(value = "extension", required = false) String extension,
            @RequestParam(value = "codec", required = false) String codec,
            @RequestParam(value = "crop", required = false) String crop,
            @RequestParam(value = "trimStart", required = false) String trimStart,
            @RequestParam(value = "trimEnd", required = false) String trimEnd,
            @RequestParam(value = "resolution", required = false) String resolution,
            @RequestParam(value = "fps", required = false) String fps
    ) {
        logger.info("Processing file: {}", hash);
        try {
            Thread.sleep(1000); // Simulate processing time
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            Path file = Paths.get("uploads/" + hash + fileExtension);
            videoEditingService.editVideo(file, hash, extension, codec, crop, trimStart, trimEnd, resolution, fps);
            logger.info("File processed and split into parts");
            return ResponseEntity.ok().body(Map.of("ok", "ok"));
        } catch (Exception e) {
            logger.error("Failed to process file: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Failed to process file: " + e.getMessage()));
        }
    }


    @GetMapping("/status/{hash}")
    public ResponseEntity<?> isDone(@PathVariable String hash) {
        logger.info("Checking if conversion is done for hash: {}", hash);
        if (VideoEditingService.editingStatus.containsKey(hash + ".mp4")) { // Assuming output is always mp4 for now
            var status = VideoEditingService.editingStatus.get(hash + ".mp4");
            if (status.done().get()) {
                return ResponseEntity.ok().body(Map.of("done", status.parts().get()));
            } else {
                return ResponseEntity.ok().body(Map.of("done", false));
            }
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Hash not found"));
        }
    }

    @GetMapping("/downloads/{hash}/{partNos}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String hash, @PathVariable String partNos) throws IOException {
        int partNo = Integer.parseInt(partNos);
        logger.info("Downloading part: {} for hash: {}", partNo, hash);
        VideoEditingService.editingStatus.remove(hash + ".mp4");
        Path filePath = Paths.get("downloads" + "/" + hash + "/" + partNo + ".part");
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
