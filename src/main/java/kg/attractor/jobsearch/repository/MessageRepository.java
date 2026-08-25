package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRespondedApplicantId(Long respondedApplicantId);
}