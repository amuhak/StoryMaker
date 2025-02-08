package prizehoggers.songmaker.Controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MakerController {
    public final Logger logger = org.slf4j.LoggerFactory.getLogger(MakerController.class);

    @GetMapping("/")
    public String index() {
        logger.info("User accessed the index page");
        return "index";
    }
}
