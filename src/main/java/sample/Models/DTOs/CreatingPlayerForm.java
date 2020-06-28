package sample.Models.DTOs;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatingPlayerForm {
    @Id
    private ObjectId playerID;
    private String name;
    private String surname;
    private String licenseID;
    private String gender;
    private String title;
    private String club;
    private Integer rating;
    @Builder.Default
    private Float score = 0f;

    public CreatingPlayerForm(String name, String surname, String licenseID, String gender, String club, String title, Integer rating) {
        this.name = name;
        this.surname = surname;
        this.licenseID = licenseID;
        this.playerID = new ObjectId();
        this.score = 0f;
        this.gender = gender;
        this.club = club;
        this.title = title;
        this.rating = rating;
    }
}
