package prizehoggers.songmaker.data;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String text;
    private String author;
    private String imagePath;
    private LocalDateTime date;

    // Constructor
    public Post(Long id, String text, String author, String imagePath) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.imagePath = imagePath;
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", text='" + text + '\'' + ", author='" + author + '\'' + ", imagePath='"
                + imagePath + '\'' + ", date=" + date + '}';
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

