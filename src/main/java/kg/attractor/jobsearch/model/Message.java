package kg.attractor.jobsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Long id;
    private Long respondedApplicantsId;
    private String content;
    private LocalDateTime timestamp;
}