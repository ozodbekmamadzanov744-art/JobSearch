package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}