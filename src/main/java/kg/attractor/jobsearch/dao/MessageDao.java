package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.Message;

import java.util.List;

public interface MessageDao {

    Message save(Message message);

    List<Message> findByRespondedApplicantsId(Long respondedApplicantsId);
}