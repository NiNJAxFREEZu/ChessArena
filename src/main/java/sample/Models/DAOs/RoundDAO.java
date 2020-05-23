package sample.Models.DAOs;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoundDAO {
    @Id
    private ObjectId _id;
    private Set<GameDAO> games;
    private Integer nr;
}
