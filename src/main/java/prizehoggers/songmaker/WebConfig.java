package prizehoggers.songmaker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${fanwall.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Convert relative path to absolute path
        String absolutePath = new File(uploadDir).getAbsolutePath();

        registry.addResourceHandler("/uploads/fanwall/**").addResourceLocations("file:" + absolutePath + "/");

        // You might need to log the path to verify it's correct
        System.out.println("Serving files from: " + absolutePath);
    }
}
