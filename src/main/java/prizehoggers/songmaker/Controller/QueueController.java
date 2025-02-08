package prizehoggers.songmaker.Controller;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prizehoggers.songmaker.SongMakerApplication;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/queue")
public class QueueController {
    public final Logger logger = org.slf4j.LoggerFactory.getLogger(QueueController.class);
    private final List<String> userQueue = Collections.synchronizedList(new LinkedList<>());

    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> joinQueue() {
        String pass = SongMakerApplication.generator.generate(512);
        synchronized (userQueue) {
            userQueue.add(pass);
            int position = userQueue.size() - 1; // Position is zero-based
            logger.info("User joined the queue, current size: {} and password: {}", userQueue.size(), pass);
            return ResponseEntity.ok().body(Map.of("position", Integer.toString(position), "Password", pass));
        }
    }

    @GetMapping("/get-position")
    public ResponseEntity<Integer> getPosition(@RequestParam String password) {
        logger.info("sseUpdates" + userQueue);
        int position;
        synchronized (userQueue) {
            position = userQueue.indexOf(password);
            if (position == -1) {
                logger.debug("User {} is not in the queue", password);
                return null;
            }
        }
        return ResponseEntity.ok(position);
    }

    @PostMapping("/remove-first")
    public ResponseEntity<String> removeFirstUser(@RequestParam String password) {
        synchronized (userQueue) {
            if (userQueue.isEmpty() || !userQueue.getFirst().equals(password)) {
                return ResponseEntity.badRequest().body("You are not the first user in the queue");
            }
            String removedUser = userQueue.removeFirst();
            return ResponseEntity.ok("Removed user: " + removedUser);
        }
    }
}
