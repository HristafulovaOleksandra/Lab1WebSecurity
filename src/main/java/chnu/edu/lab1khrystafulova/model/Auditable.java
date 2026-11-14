package chnu.edu.lab1khrystafulova.model;

import lombok.Data;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;

@Data
public class Auditable {

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @Version
    private Long version;
}