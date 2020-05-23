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
    private String fullName;
    private String licenseID;
    @Builder.Default
    private Float score = 0f;

    public CreatingPlayerForm(String name, String fullName, String licenseID) {
        this.name = name;
        this.fullName = fullName;
        this.licenseID = licenseID;
        this.playerID = new ObjectId();
        this.score = 0f;
    }
}
