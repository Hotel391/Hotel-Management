package utility.prompt_ai_build;

import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 *
 * Author: HieuTT
 */
public class CustomerContextCache {

    private static Path contextPath;
    private static final String PROMPT_FILE = "/web/prompts/customer_prompt.txt";
    private static final long REFRESH_INTERVAL_MINUTES = 60;

    private CustomerContextCache() {
    }

    public static void init(ServletContext context) {
        String webPath = context.getRealPath("/");
        String buildPath = new File(webPath).getParent();
        String root= new File(buildPath).getParent();
        String contextPathStr= root + PROMPT_FILE;
        contextPath = Paths.get(contextPathStr).toAbsolutePath();
    }

    public static String getContextIfExists() {
        if (contextPath == null) {
            return null;
        }

        File file = contextPath.toFile();
        if (!file.exists() || isExpired(file)) {
            return null;
        }

        try {
            return Files.readString(contextPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static void saveContext(String content) {
        if (contextPath == null) {
            return;
        }
        try {
            Files.createDirectories(contextPath.getParent());
            Files.writeString(contextPath, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            //
        }
    }

    private static boolean isExpired(File file) {
        long lastModified = file.lastModified();
        long now = System.currentTimeMillis();
        long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(now - lastModified);
        return diffMinutes >= REFRESH_INTERVAL_MINUTES;
    }
}
