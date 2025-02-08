package prizehoggers.songmaker;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
public class SongMakerApplication {
    public static ConcurrentLinkedQueue<String> auth;
    public static RandomStringGenerator generator;

    public static void main(String[] args) {
        auth = new ConcurrentLinkedQueue<>();
        generator =
                new RandomStringGenerator.Builder().selectFrom(
                        "abcdefghijklmnopqrstuvwxyz123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())
                        .get();
        SpringApplication.run(SongMakerApplication.class, args);
    }
}
