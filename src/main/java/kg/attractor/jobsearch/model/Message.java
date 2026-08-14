package kg.attractor.jobsearch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "responded_applicants_id")
    private Long respondedApplicantsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responded_applicants_id", insertable = false, updatable = false)
    private RespondedApplicant respondedApplicant;

    @Lob
    private String content;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}