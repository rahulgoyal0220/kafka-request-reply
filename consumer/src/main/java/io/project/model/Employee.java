package io.project.model;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@ToString
@Component
@Document(indexName = "user", type = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private Long userId;
    private Long companyId;
    private String email;
    private String companyName;
    private String designation;

}
