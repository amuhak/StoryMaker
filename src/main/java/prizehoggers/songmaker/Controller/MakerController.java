package prizehoggers.songmaker.Controller;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@EnableWebSocketMessageBroker
@Controller
public class MakerController {
    public final Logger logger = org.slf4j.LoggerFactory.getLogger(MakerController.class);

    @GetMapping("/")
    public String index() {
        logger.info("User accessed the index page");
        return "index";
    }
/*
    @PostMapping("/input")
    ResponseEntity<?> input(@RequestBody Webho webhookRequest) {

    }
*/
}
