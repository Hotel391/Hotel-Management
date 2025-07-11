package controllers.common;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utility.prompt_ai_build.ContextGenerator;
import utility.prompt_ai_build.CustomerContextCache;
import utility.prompt_ai_build.RolePromptStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utility.prompt_ai_build.CustomerContextStrategy;

/**
 *
 * @author HieuTT
 */
@WebServlet(name = "ChatServlet", urlPatterns = {"/chat"})
public class ChatServlet extends HttpServlet {

    private static final String API_KEY = "AIzaSyD8ZoT0H90t64zH7kkFDT1l8wabNTIyw0U";
    private static final Gson gson = new Gson();

    static class GeminiResponse {

        List<Candidate> candidates;

        static class Candidate {

            Content content;
        }

        static class Content {

            List<Part> parts;
        }

        static class Part {

            String text;
        }
    }

    @Override
    public void init() throws ServletException {
        CustomerContextCache.init(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Map<String, String>> chatHistory = (List<Map<String, String>>) session.getAttribute("chatHistory");
        if (chatHistory == null) {
            chatHistory = new ArrayList<>();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(chatHistory));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String question = request.getParameter("question");
        String geminiReply = "Không có phản hồi.";

        HttpSession session = request.getSession();
        List<Map<String, String>> chatHistory = (List<Map<String, String>>) session.getAttribute("chatHistory");
        if (chatHistory == null) {
            chatHistory = new ArrayList<>();
        }

        if (question != null && !question.trim().isEmpty()) {
            geminiReply = callGeminiAPI(question, chatHistory);

            // Lưu vào HttpSession
            String time = new Date().toString();

            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("type", "user");
            userMsg.put("text", question);
            userMsg.put("time", time);
            chatHistory.add(userMsg);

            Map<String, String> botMsg = new HashMap<>();
            botMsg.put("type", "bot");
            botMsg.put("text", geminiReply);
            botMsg.put("time", new Date().toString());
            chatHistory.add(botMsg);
            session.setAttribute("chatHistory", chatHistory);
        }

        Map<String, String> result = new HashMap<>();
        result.put("reply", geminiReply);
        result.put("status", "success");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("chatHistory");

        response.setContentType("application/json");
        response.getWriter().write("{\"status\":\"cleared\"}");
    }

    private String callGeminiAPI(String question, List<Map<String, String>> history) {
        try {
            URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            int roleId = 4;
            RolePromptStrategy strategy = null;
            if (roleId == 4) {
                strategy = new CustomerContextStrategy();
            }
            ContextGenerator contextGenerator = new ContextGenerator();
            contextGenerator.setStrategy(strategy);
            String context = contextGenerator.generateContext();
            StringBuilder chatContext = new StringBuilder(context).append("\n").append("Lịch sử trò chuyện:\n");

            for (Map<String, String> msg : history) {
                String type = msg.get("type");
                String text = msg.get("text");
                if ("user".equals(type)) {
                    chatContext.append("User: ").append(text).append("\n");
                } else if ("bot".equals(type)) {
                    chatContext.append("Bot: ").append(text).append("\n");
                }
            }
            chatContext.append("User: ").append(question);

            String inputJson = "{ \"contents\": [ { \"parts\": [ { \"text\": \""
                    + chatContext.toString().replace("\"", "\\\"") + "\" } ] } ] }";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(inputJson.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            conn.disconnect();

            GeminiResponse res = gson.fromJson(sb.toString(), GeminiResponse.class);
            if (res != null && res.candidates != null && !res.candidates.isEmpty()) {
                GeminiResponse.Candidate c = res.candidates.get(0);
                if (c.content != null && c.content.parts != null && !c.content.parts.isEmpty()) {
                    return c.content.parts.get(0).text;
                }
            }

            return "Không có phản hồi từ Gemini.";
        } catch (JsonSyntaxException | IOException e) {
            return "Đã xảy ra lỗi trong quá trình kết nối đến AI. Vui lòng thử lại sau.";
        }
    }
}
