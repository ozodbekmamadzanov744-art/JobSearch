package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.ContactType;

import java.util.List;
import java.util.Optional;

public interface ContactTypeDao {

    List<ContactType> findAll();

    Optional<ContactType> findById(Long id);
}
