package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}