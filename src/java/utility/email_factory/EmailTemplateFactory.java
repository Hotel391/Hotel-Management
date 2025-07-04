package utility.email_factory;

/**
 *
 * @author HieuTT
 */
public class EmailTemplateFactory {
    public static EmailContentBuilder getBuilder(String type){
        switch (type) {
            case "register":
                return new ConfirmEmailBuilder();
            case "reset":
                return new ResetPasswordBuilder();
            case "receipt":
                return new ReceiptBuilder();
            case "checkin":
                return new CheckInBuilder();
            default:
                throw new IllegalArgumentException("Unknown email type: " + type);
        }
    }
}
