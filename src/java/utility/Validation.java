package utility;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
        regexMap.put("TYPE_ROOM_NAME_BASIC", Pattern.compile("^[a-zA-Z\\s]+$"));
        regexMap.put("ROOM_PRICE_INT", Pattern.compile("^[1-9]\\d{0,7}$"));
        regexMap.put("DESCRIPTION", Pattern.compile("^[\\p{L}\\d\\s,./-]{5,255}$"));
        regexMap.put("CCCD", Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$"));
        regexMap.put("DATE_OF_BIRTH", Pattern.compile("^(19[0-9]{2}|20[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$"));

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
        ruleCheck.put("PHONE_NUMBER", List.of(
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "PHONE_NUMBER"),
                        "Phone number must start with 0 and be 10-11 digits"),
                new ValidationRule<String>(value -> !value.contains(" "),
                        "Phone number cannot contain spaces")));

        ruleCheck.put("ADDRESS", List.of(
                new ValidationRule<String>(value -> value.length() >= 5 && value.length() <= 255,
                        "Address must be 5-255 characters"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "ADDRESS"),
                        "Address can only contain letters, digits, spaces, commas, dots, slashes, and hyphens")));

        ruleCheck.put("CCCD", List.of(
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "CCCD"),
                        "CCCD must be in format XXX-XX-XXXX (e.g., 123-45-6789)")));
        ruleCheck.put("DATE_OF_BIRTH", List.of(
                new ValidationRule<Date>(value -> value != null,
                        "Date of birth is required"),
                new ValidationRule<Date>(value -> {
                    if (value == null) {
                        return false;
                    }
                    long time = value.getTime();
                    long minTime = new Date(1900 - 1900, 0, 1).getTime(); // January 1, 1900
                    long maxTime = new Date(System.currentTimeMillis()).getTime(); // Today
                    return time >= minTime && time <= maxTime;
                }, "Date of birth must be between 1900 and today")));

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

    public static <T> T readInputField(
            jakarta.servlet.http.HttpServletRequest req,
            String attrKey,
            String rawInput,
            Function<String, T> parser,
            List<ValidationRule<T>> rules) {
        if (rawInput == null || rawInput.trim().isEmpty()) {
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

    public static <T> T readInputField(
            String rawInput,
            Function<String, T> parser,
            List<ValidationRule<T>> rules,
            T defaultValue) {
        T value;
        if (rawInput == null || rawInput.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            value = parser.apply(rawInput);
        } catch (Exception e) {
            return defaultValue;
        }
        for (ValidationRule<T> rule : rules) {
            if (!rule.isValid(value)) {
                return defaultValue;
            }
        }
        return value;
    }
}
