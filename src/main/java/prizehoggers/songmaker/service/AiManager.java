package prizehoggers.songmaker.service;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import org.slf4j.Logger;

public class AiManager {
    public static OllamaAPI ollamaAPI;
    public static Logger logger = org.slf4j.LoggerFactory.getLogger(AiManager.class);

    public static void init(OllamaAPI ollamaAPI) {
        AiManager.ollamaAPI = ollamaAPI;
    }

    public static synchronized String summarize(String context) {
        for (int i = 0; i < 3; i++) {
            try {
                return ollamaAPI.generate("phi4",
                        "Summarize the following text: " + context, false, new OptionsBuilder().build()).getResponse();
            } catch (Exception e) {
                logger.error("Error: {}", e.getMessage());
            }
        }
        return "Error: Failed to generate response after 3 attempts.";
    }

    public static synchronized String generateMotivationalQuote() {
        for (int i = 0; i < 3; i++) {
            try {
                OllamaResult result =
                        ollamaAPI.generate("phi4", "Generate only one motivational quote. Dont give me any other "
                                + "text, no markdown, dont provide a source for the quote (dont say -- Nelson Mandela)", false, new OptionsBuilder().build());
                return result.getResponse();
            } catch (Exception e) {
                logger.error("Error: {}", e.getMessage());
            }
        }
        return "Error: Failed to generate response after 3 attempts.";
    }
}
