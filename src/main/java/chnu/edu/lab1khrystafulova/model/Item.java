package chnu.edu.lab1khrystafulova.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document
public class Item extends Auditable {

    @Id
    private String id;
    private String name;
    private String description;
}