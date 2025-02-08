package prizehoggers.songmaker.Controller;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import prizehoggers.songmaker.SongMakerApplication;

@Controller
public class MakerController {
    public final Logger logger = org.slf4j.LoggerFactory.getLogger(MakerController.class);

    @GetMapping("/")
    public String index() {
        logger.info("User accessed the index page");
        return "index";
    }

    @GetMapping("/data")
    public ResponseEntity<String> getData() {
        logger.info("User accessed the data page");
        return ResponseEntity.ok(SongMakerApplication.gameManager.getWords());
    }
}
