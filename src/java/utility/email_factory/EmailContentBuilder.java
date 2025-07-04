package utility.email_factory;

import java.util.Map;

/**
 *
 * @author HieuTT
 */
public interface EmailContentBuilder {
    String build(Map<String, Object> data);
}
