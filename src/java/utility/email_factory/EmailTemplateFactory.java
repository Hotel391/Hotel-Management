package utility.email_factory;

/**
 *
 * @author HieuTT
 */
public class EmailTemplateFactory {
    private EmailTemplateFactory() {
        // Private constructor to prevent instantiation
    }
    public static EmailContentBuilder getBuilder(String type){
        switch (type) {
            case "register" -> {
                return new ConfirmEmailBuilder();
            }
            case "reset" -> {
                return new ResetPasswordBuilder();
            }
            case "receipt" -> {
                return new ReceiptBuilder();
            }
            case "checkin" -> {
                return new CheckInBuilder();
            }
            case "employeeAccount" -> {
                return new EmployeeAccountEmailBuilder();
            }
            default -> throw new IllegalArgumentException("Unknown email type: " + type);
        }
    }
}
