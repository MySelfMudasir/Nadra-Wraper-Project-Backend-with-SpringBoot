package com.NadraWraper.Model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "VERISY_LOGS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerisysLogsSchema {

    @Id
    // Use sequence or identity based on Oracle setup (see earlier advice)
    private Long id;

    @Column(name = "NADRA_VERIFY_RESPONSE")
    private String nadraVerifyCitizenResponse;

    @Column(name = "RESPONSE_TIMESTAMP")
    private String responseTimestamp;



    @Transient
    private List<NadraSchema> subRecords;
}
