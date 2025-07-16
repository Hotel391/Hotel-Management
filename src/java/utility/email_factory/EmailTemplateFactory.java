package utility.email_factory;

/**
 *
 * @author HieuTT
 */
public class EmailTemplateFactory {
    private EmailTemplateFactory() {
        // Private constructor to prevent instantiation
    }

    public enum EmailType {
        REGISTER,
        RESET,
        RECEIPT,
        CHECKIN,
        EMPLOYEE_ACCOUNT,
        BOOKING_FROM_CART
    }

    public static EmailContentBuilder getBuilder(EmailType type) {
        switch (type) {
            case REGISTER -> {
                return new ConfirmEmailBuilder();
            }
            case RESET -> {
                return new ResetPasswordBuilder();
            }
            case RECEIPT -> {
                return new ReceiptBuilder();
            }
            case CHECKIN -> {
                return new CheckInBuilder();
            }
            case EMPLOYEE_ACCOUNT -> {
                return new EmployeeAccountEmailBuilder();
            }
            case BOOKING_FROM_CART -> {
                return new BookingFromCartEmailBuilder();
            }
            default -> throw new IllegalArgumentException("Unknown email type: " + type);
        }
    }
}
