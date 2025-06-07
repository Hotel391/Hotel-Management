package utility;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Validation {

    private Validation() {
    }
    
    private static final Map<String, Pattern> regexMap=new HashMap<>(); 
    
    static{
        regexMap.put("FULLNAME", Pattern.compile("^[\\p{L} ]{2,100}$"));
        regexMap.put("PHONE_NUMBER", Pattern.compile("^0\\d{9,10}$"));
        regexMap.put("USERNAME",Pattern.compile("^[\\da-zA-Z_]{5,20}$"));
        regexMap.put("EMOJI",Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+"));
        regexMap.put("EMAIL",Pattern.compile("^[a-zA-Z\\d_]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$"));
        regexMap.put("PASSWORD",Pattern.compile("^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}"));
    }

    public static boolean checkFormatException(String input, String pattern){
        Pattern regex=regexMap.get(pattern);
        return regex.matcher(input).matches();
    }

    public static boolean validateField(jakarta.servlet.http.HttpServletRequest request, String attrKey, String value, String patternKey, String fieldLabel
                            ,String messageError) {
        if (value == null || value.trim().isEmpty()) {
            request.setAttribute(attrKey, fieldLabel + " is required.");
            return true;
        }
        if (!checkFormatException(value, patternKey)) {
            request.setAttribute(attrKey, messageError);
            return true;
        }
        request.removeAttribute(attrKey);
        return false;
    }
}
