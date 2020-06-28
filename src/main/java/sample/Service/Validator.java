package sample.Service;

public class Validator {
    public static boolean isNameValid(String name) {
        return !name.matches(" +.+") && !name.matches(".+[0-9]+");
    }

    public static boolean isSurnameValid(String surname) {
        return !surname.matches(" +.+") && !surname.matches(".+[0-9]+");
    }
}
