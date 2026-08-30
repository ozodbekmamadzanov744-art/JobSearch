package kg.attractor.jobsearch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "responded_applicants")
public class RespondedApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;

    private Boolean confirmation;

    @OneToMany(mappedBy = "respondedApplicant")
    private Set<Message> messages = new HashSet<>();

    public void addMessage(Message message) {
        messages.add(message);
        message.setRespondedApplicant(this);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
        if (message.getRespondedApplicant() == this) {
            message.setRespondedApplicant(null);
        }
    }
}
