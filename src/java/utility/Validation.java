package utility;

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
        regexMap.put("FORBIDDEN_USERNAME", Pattern.compile("^(?!.*(admin|root|support|fuck|shit)).*$", Pattern.CASE_INSENSITIVE));
        regexMap.put("EMOJI", Pattern.compile("[\\p{So}\\p{Cn}&&[^\\x00-\\x7F]]"));
        regexMap.put("EMAIL", Pattern.compile("^[a-zA-Z\\d_]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$"));
        regexMap.put("PASSWORD", Pattern.compile("^(?=.*[a-z])(?=.*\\d)(?=.*[ @#$%^&+=!_])[A-Za-z\\d @#$%^&+=!_]{8,}"));
        regexMap.put("ADDRESS", Pattern.compile("^[\\p{L}\\d\\s,./-]{5,255}$"));
        regexMap.put("TYPE_ROOM_NAME_BASIC", Pattern.compile("^[A-Za-z\s]{1,100}$"));
        regexMap.put("ROOM_PRICE_INT", Pattern.compile("^[1-9]\\d{0,7}$"));
        regexMap.put("DESCRIPTION", Pattern.compile("^[\\p{L}\\d\\s,./-]{1,255}$"));
        regexMap.put("CCCD", Pattern.compile("^\\d{12}$"));
        regexMap.put("DATE_OF_BIRTH", Pattern.compile("^(19[0-9]{2}|20[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$"));

        ruleCheck.put("FULLNAME", List.of(
                new ValidationRule<String>(value -> value.length() >= 2 && value.length() <= 100,
                        "Họ tên phải gồm 2-100 kí tự"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "FULLNAME"),
                        "Họ tên chỉ bao gồm chữ cái"),
                new ValidationRule<String>(value -> !value.contains("  "),
                        "Họ tên không thể chứa khoảng cách liên tiếp")));
        ruleCheck.put("EMAIL", List.of(
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "EMAIL"),
                        "Email phải theo định dạng: user@example.com")));
        ruleCheck.put("USERNAME", List.of(
                new ValidationRule<String>(value -> value.length() >= 6 && value.length() <= 20,
                        "Username phải gồm 6-20 kí tự"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "USERNAME_FIRST_CHAR"),
                        "Username bắt đầu bằng 1 chữ cái"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "USERNAME"),
                        "Username chỉ bao gồm chữ cái, số và dấu gạch dưới"),
                new ValidationRule<String>(value -> !Validation.checkFormatException(value, "EMOJI"),
                        "Username không được chứa emoji")
        ));
        ruleCheck.put("PASSWORD", List.of(
                new ValidationRule<String>(value -> value.charAt(0) != ' ' && value.charAt(value.length() - 1) != ' ',
                        "Password không thể bắt đầu hoặc kết thúc bằng khoảng trắng"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "PASSWORD"),
                        "Password phải chứa ít nhất 8 ký tự với chữ thường, hoa, đặc biệt và số")));
        ruleCheck.put("PHONE_NUMBER", List.of(
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "PHONE_NUMBER"),
                        "Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số"),
                new ValidationRule<String>(value -> !value.contains(" "),
                        "Số điện thoại không được chứa khoảng trắng")));

        ruleCheck.put("ADDRESS", List.of(
                new ValidationRule<String>(value -> value.length() >= 5 && value.length() <= 255,
                        "Địa chỉ phải dài từ 5-255 ký tự"),
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "ADDRESS"),
                        "Địa chỉ chỉ có thể chứa các chữ cái, chữ số, khoảng trắng, dấu phẩy, dấu chấm, dấu gạch chéo và dấu gạch nối")));

        ruleCheck.put("CCCD", List.of(
                new ValidationRule<String>(value -> Validation.checkFormatException(value, "CCCD"),
                        "CCCD phải nhập 12 số")));
        ruleCheck.put("DATE_OF_BIRTH", List.of(
                new ValidationRule<Date>(value -> value != null,
                        "Ngày sinh không được để trống"),
                new ValidationRule<Date>(value -> {
                    if (value == null) {
                        return false;
                    }
                    long time = value.getTime();
                    long minTime = new Date(1900 - 1900, 0, 1).getTime(); // January 1, 1900
                    long maxTime = new Date(System.currentTimeMillis()).getTime(); // Today
                    return time >= minTime && time <= maxTime;
                }, "ngày sinh phải nằm trong khoảng 1900 đến hôm nay")));

    }

    public static boolean checkFormatException(String input, String pattern) {
        Pattern regex = regexMap.get(pattern);
        return regex.matcher(input).matches();
    }

    public static boolean validateField(jakarta.servlet.http.HttpServletRequest httpRequest, String attrKey,
            String value, String patternKey, String fieldLabel, String messageError) {
        if (value == null || value.trim().isEmpty()) {
            httpRequest.setAttribute(attrKey, fieldLabel + " không được để trống.");
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
            req.setAttribute(attrKey, fieldName + " không được để trống.");
            check = true;
        }
        T value = null;
        try {
            value = parser.apply(rawInput);
        } catch (Exception e) {
            req.setAttribute(attrKey, "Định dạng đầu vào không hợp lệ.");
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
            req.setAttribute(attrKey, fieldName + " không được để trống.");
            return true;
        }
        T value = null;
        try {
            value = parser.apply(rawInput);
        } catch (Exception e) {
            req.setAttribute(attrKey, "Định dạng đầu vào không hợp lệ.");
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
            req.setAttribute(attrKey, "Định dạng đầu vào không hợp lệ.");
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

    public static <T> boolean checkInputField(
            String rawInput,
            Function<String, T> parser,
            List<ValidationRule<T>> rules) {
        T value;
        if (rawInput == null || rawInput.trim().isEmpty()) {
            return false;
        }
        try {
            value = parser.apply(rawInput);
        } catch (Exception e) {
            return false;
        }
        for (ValidationRule<T> rule : rules) {
            if (!rule.isValid(value)) {
                return false;
            }
        }
        return true;
    }
}
