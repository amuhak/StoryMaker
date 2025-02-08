package prizehoggers.songmaker;

import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import prizehoggers.songmaker.service.GameManager;

import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
public class SongMakerApplication {
    public static final Logger logger = org.slf4j.LoggerFactory.getLogger(SongMakerApplication.class);
    public static ConcurrentLinkedQueue<String> auth;
    public static RandomStringGenerator generator;
    public static GameManager gameManager;

    public static void main(String[] args) throws Exception {
        auth = new ConcurrentLinkedQueue<>();
        generator =
                new RandomStringGenerator.Builder().selectFrom(
                        "abcdefghijklmnopqrstuvwxyz123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())
                        .get();
        gameManager = new GameManager();
        SpringApplication.run(SongMakerApplication.class, args);
    }


}
