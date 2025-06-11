package utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Validation {

    /**
     *
     * @author HieuTT
     */
    private Validation() {
    }

    private static final Map<String, Pattern> regexMap = new HashMap<>();

    static {
        regexMap.put("FULLNAME", Pattern.compile("^[\\p{L} ]{2,100}$"));
        regexMap.put("PHONE_NUMBER", Pattern.compile("^0\\d{9,10}$"));
        regexMap.put("USERNAME", Pattern.compile("^[\\da-zA-Z_]{2,20}$"));
        regexMap.put("EMOJI", Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+"));
        regexMap.put("EMAIL", Pattern.compile("^[a-zA-Z\\d_]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$"));
        regexMap.put("PASSWORD", Pattern.compile("^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}"));
        regexMap.put("ADDRESS", Pattern.compile("^[\\p{L}\\d\\s,./-]{5,255}$"));
    }

    public static boolean checkFormatException(String input, String pattern) {
        Pattern regex = regexMap.get(pattern);
        return regex.matcher(input).matches();
    }

    public static boolean validateField(jakarta.servlet.http.HttpServletRequest httpRequest, String attrKey,
            String value, String patternKey, String fieldLabel, String messageError) {
        if (value == null || value.trim().isEmpty()) {
            httpRequest.setAttribute(attrKey, fieldLabel + " is required.");
            return true;
        }
        if (!checkFormatException(value, patternKey)) {
            httpRequest.setAttribute(attrKey, messageError);
            return true;
        }
        httpRequest.removeAttribute(attrKey);
        return false;
    }

    public static <T> boolean validateField(
            jakarta.servlet.http.HttpServletRequest req,
            String attrKey,
            String fieldName,
            String rawInput,
            Function<String, T> parser,
            List<ValidationRule<T>> rules) {
        boolean check = false;
        if (rawInput == null || rawInput.trim().isEmpty()) {
            req.setAttribute(attrKey, fieldName + " is required.");
            check = true;
        }
        T value = null;
        try {
            value = parser.apply(rawInput);
        } catch (Exception e) {
            req.setAttribute(attrKey, "Invalid input format.");
            check = true;
        }
        for (ValidationRule<T> rule : rules) {
            if (!rule.isValid(value)) {
                req.setAttribute(attrKey, rule.getErrorMessage());
                check = true;
                break;
            }
        }
        req.removeAttribute(attrKey);
        return check;
    }
}
