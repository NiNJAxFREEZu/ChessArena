package sample.Service;

public class Validator {
    public static boolean isNameValid(String name) {
        return !name.matches(" +.+") && !name.matches(".+[0-9]+");
    }

    public static boolean isSurnameValid(String surname) {
        return !surname.matches(" +.+") && !surname.matches(".+[0-9]+");
    }

    public static boolean isGenderValid(String gender) {
        if (gender.toLowerCase().equals("male")
                || gender.toLowerCase().equals("female")
                || gender.toLowerCase().equals("apache")
                || gender.toLowerCase().equals("apache chopper")
                || gender.toLowerCase().equals("attack helicopter")
                || gender.toLowerCase().equals("helicopter"))
            return true;
        else
            return false;
    }
}
