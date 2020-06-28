package sample.Enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum Genders {
    MALE("Male"),
    FEMALE("Female"),
    APACHE_HELICOPTER("Apache Helicopter");

    @Getter
    private String genderText;

    public static ObservableList<String> getGenders() {
        List<String> genderTexts = new ArrayList<>();
        Arrays.stream(Genders.values()).forEach(gender -> genderTexts.add(gender.getGenderText()));
        return FXCollections.observableList(genderTexts);
    }
}