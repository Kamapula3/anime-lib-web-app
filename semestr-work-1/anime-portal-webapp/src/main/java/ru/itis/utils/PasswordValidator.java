package ru.itis.utils;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final Pattern PASSWORD_PATTERN1 = Pattern.compile("^.*[A-za-z].*$");

    private static final Pattern PASSWORD_PATTERN2 = Pattern.compile("^.*\\d.*$");

    private static final Integer MIN_PASSWORD_LENGTH = 8;

    public static boolean isValidPasswordFormat(String password){
        return password != null && PASSWORD_PATTERN1.matcher(password).matches() && PASSWORD_PATTERN2.matcher(password).matches();
    }

    public static boolean isValidPasswordLength(String password){
        return password.length() >= MIN_PASSWORD_LENGTH;
    }
}
