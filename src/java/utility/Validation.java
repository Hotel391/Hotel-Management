package utility;

import java.util.ArrayList;
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
    private static final Map<String, List<ValidationRule<?>>> ruleCheck = new HashMap<>();

    static {
        regexMap.put("FULLNAME", Pattern.compile("^[\\p{L} ]{2,100}$"));
        regexMap.put("FULLNAME_FIRST_CHAR", Pattern.compile("^[\\p{L}].*"));
        regexMap.put("FULLNAME_NO_DIGIT", Pattern.compile("^[^\\d]*$"));
        regexMap.put("PHONE_NUMBER", Pattern.compile("^0\\d{9,10}$"));
        regexMap.put("USERNAME", Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{4,19}$"));
        regexMap.put("USERNAME_FIRST_CHAR", Pattern.compile("^[a-zA-Z].*"));
        regexMap.put("FORBIDDEN_USERNAME", Pattern.compile("^(?!.*(admin|manager|root|support|fuck|shit)).*$", Pattern.CASE_INSENSITIVE));
        regexMap.put("EMOJI", Pattern.compile("[\\p{So}\\p{Cn}&&[^\\x00-\\x7F]]"));
        regexMap.put("EMAIL", Pattern.compile("^[a-zA-Z\\d_]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$"));
        regexMap.put("PASSWORD", Pattern.compile("^(?=.*[a-z])(?=.*\\d)(?=.*[ @#$%^&+=!])[A-Za-z\\d @#$%^&+=!]{8,}"));
        regexMap.put("ADDRESS", Pattern.compile("^[\\p{L}\\d\\s,./-]{5,255}$"));
        regexMap.put("TYPE_ROOM_NAME_BASIC", Pattern.compile("^[a-zA-Z\\s_-]{2,50}$"));
        regexMap.put("ROOM_PRICE_INT", Pattern.compile("^[1-9]\\d{0,7}$"));
        regexMap.put("DESCRIPTION", Pattern.compile("^[\\p{L}\\d\\s,./-]{5,255}$"));

        ruleCheck.put("FULLNAME", List.of(
                new ValidationRule<String>(value -> value.length() >= 2 && value.length() <= 100,
                        "Fullname must be 2-100 characters"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "FULLNAME"),
                        "Fullname just contain letters"),
                new ValidationRule<String>(value -> !value.contains("  "),
                        "Fullname cannot contain consecutive spaces")));
        ruleCheck.put("EMAIL", List.of(
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "EMAIL"),
                        "Email must be in the format: user@example.com")));
        ruleCheck.put("USERNAME", List.of(
                new ValidationRule<String>(value -> value.length() >= 6 && value.length() <= 20,
                        "Username must be 6-20 characters"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "USERNAME_FIRST_CHAR"),
                        "Username must start with a letter"),
                new ValidationRule<String>(
                        value -> Validation.checkFormatException(value, "USERNAME"),
                        "Username just can contains letters, digits, and underscores"),
                new ValidationRule<String>(value -> !Validation.checkFormatException(value, "EMOJI"),
                        "Username cannot contain emoji."),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "FORBIDDEN_USERNAME"),
                        "Username contains restricted words")));
        ruleCheck.put("PASSWORD", List.of(
                new ValidationRule<String>(value -> value.charAt(0) != ' ' && value.charAt(value.length() - 1) != ' ',
                        "Password cannot start or end with space"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "PASSWORD"),
                        "Password must contains 8 characters with lower, upper, special and digit")));

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
        if (!check) {
            req.removeAttribute(attrKey);
        }
        return check;
    }

    public static <T> boolean validateField(
            jakarta.servlet.http.HttpServletRequest req,
            String attrKey,
            String fieldName,
            String rawInput,
            Function<String, T> parser,
            String ruleName,
            List<ValidationRule<T>> moreRule) {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            req.setAttribute(attrKey, fieldName + " is required.");
            return true;
        }
        T value = null;
        try {
            value = parser.apply(rawInput);
        } catch (Exception e) {
            req.setAttribute(attrKey, "Invalid input format.");
            return true;
        }
        List<ValidationRule<T>> baseRules = (List<ValidationRule<T>>) (List<?>) ruleCheck.get(ruleName);
        List<ValidationRule<T>> rules = new ArrayList<>();
        if (baseRules != null) {
            rules.addAll(baseRules);
        }
        if (moreRule != null) {
            rules.addAll(moreRule);
        }
        
        for (ValidationRule<T> rule : rules) {
            if (!rule.isValid(value)) {
                req.setAttribute(attrKey, rule.getErrorMessage());
                return true;
            }
        }
        req.removeAttribute(attrKey);
        return false;
    }
    
    public static <T> boolean validateField(
            jakarta.servlet.http.HttpServletRequest req,
            String attrKey,
            String fieldName,
            String rawInput,
            Function<String, T> parser,
            String ruleName) {
        return validateField(req, attrKey, fieldName, rawInput, parser, ruleName, null);
    }
    
    public static <T> T readPriceInput(
            jakarta.servlet.http.HttpServletRequest req,
            String attrKey,
            String rawInput,
            Function<String, T> parser,
            List<ValidationRule<T>> rules) {
        if(rawInput == null || rawInput.trim().isEmpty()) {
            return null;
        }
        T value;
        try {
            value = parser.apply(rawInput);
        } catch (Exception e) {
            req.setAttribute(attrKey, "Invalid input format.");
            return null;
        }
        for (ValidationRule<T> rule : rules) {
            if (!rule.isValid(value)) {
                req.setAttribute(attrKey, rule.getErrorMessage());
                return null;
            }
        }
        return value;
    }
}
