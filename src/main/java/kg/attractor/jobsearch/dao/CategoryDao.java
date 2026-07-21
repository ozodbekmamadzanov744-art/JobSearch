package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {

    List<Category> findAll();

    Optional<Category> findById(Long id);
}