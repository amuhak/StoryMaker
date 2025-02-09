package prizehoggers.songmaker.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prizehoggers.songmaker.service.AiManager;

@RestController
@RequestMapping("/quote")
public class QuoteController {

    @GetMapping("/generate")
    public String generateQuote() {
        return AiManager.generateMotivationalQuote();
    }
}
