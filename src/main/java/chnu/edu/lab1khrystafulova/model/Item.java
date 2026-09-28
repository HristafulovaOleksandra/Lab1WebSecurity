package chnu.edu.lab1khrystafulova.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Khrystafulova
 * @project Lab1Khrystafulova
 * @class Item
 * @version 1.0.0
 * @since 28/09/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    private String id;
    private String name;
    private String description;
}