package cn.eccto.study.springframework.tutorials.scope;

import java.util.regex.Pattern;

/**
 * description
 *
 * @author JonathanChen 2019/11/14 20:04
 */
public class UserRegistrationValidator {

    private static final Pattern VALID_EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9]{6,}$");

    public String validateEmail(String email) {
        if (!VALID_EMAIL_PATTERN.matcher(email).matches()) {
            return "Not a valid email: " + email;
        }
        return null;
    }

    public String validatePassword(String password) {
        if (!VALID_PASSWORD_PATTERN.matcher(password).matches()) {
            return "Not a valid password. The password should  be alphanumeric and " +
                    "at least 6 characters long ";
        }
        return null;
    }
}