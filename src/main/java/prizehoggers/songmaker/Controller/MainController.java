package prizehoggers.songmaker.Controller;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import prizehoggers.songmaker.SongMakerApplication;

@Controller
public class MainController {
    public final Logger logger = org.slf4j.LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String home() {
        logger.info("User accessed the home page");
        return "index";
    }

    @GetMapping("/songmaker")
    public String index() {
        logger.info("User accessed the songmaker page");
        return "songmaker";
    }

    @GetMapping("/fanwall")
    public String fanwall() {
        logger.info("User accessed the fanwall page");
        return "fanwall";
    }

    @GetMapping("/login")
    public String login() {
        logger.info("User accessed the login page");
        return "login";
    }

    @GetMapping("/beatmaker")
    public String beatmaker() {
        logger.info("User accessed the beatmaker page");
        return "beatmaker";
    }

    @GetMapping("/data")
    public ResponseEntity<String> getData() {
        logger.info("User accessed the data page");
        return ResponseEntity.ok(SongMakerApplication.gameManager.getWords());
    }
}
