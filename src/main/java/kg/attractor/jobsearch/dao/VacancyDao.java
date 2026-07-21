package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.Vacancy;

import java.util.List;
import java.util.Optional;

public interface VacancyDao {

    Vacancy save(Vacancy vacancy);

    void update(Vacancy vacancy);

    void delete(Long id);

    Optional<Vacancy> findById(Long id);

    List<Vacancy> findAll();

    List<Vacancy> findByAuthorId(Long authorId);

    List<Vacancy> findByCategoryId(Long categoryId);
}
