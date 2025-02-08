package prizehoggers.songmaker.data;

public class PlayListData {
    private String name;
    private String URL;
    private String image;

    // Constructors, Getters, and Setters
    public PlayListData() {
    }

    public PlayListData(String name, String URL, String image) {
        this.name = name;
        this.URL = URL;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
