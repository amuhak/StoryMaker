package prizehoggers.songmaker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import prizehoggers.songmaker.data.Post;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final List<Post> posts = Collections.synchronizedList(new ArrayList<>());
    private final String uploadDir;
    private AtomicLong idCounter = new AtomicLong();

    public PostService(@Value("${fanwall.upload.dir}") String uploadDir) {
        this.uploadDir = uploadDir;
        new File(uploadDir).mkdirs();
    }

    public List<Post> getAllPosts() {
        posts.stream().sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate())).forEach(System.out::println);
        return posts.stream().sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate())).collect(Collectors.toList());
    }

    public Post createPost(String text, String author, MultipartFile image) throws IOException {
        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + getFileExtension(image.getOriginalFilename());
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            imagePath = "/uploads/fanwall/" + fileName;
        }

        Post post = new Post(idCounter.incrementAndGet(), text, author != null ? author : "Rockstar Fan", imagePath);

        posts.add(post);
        return post;
    }

    private String getFileExtension(String fileName) {
        return fileName != null && fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }
}
