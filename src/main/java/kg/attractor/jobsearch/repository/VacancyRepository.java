package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    Page<Vacancy> findByIsActiveTrue(Pageable pageable);

    Page<Vacancy> findByAuthorId(Long authorId, Pageable pageable);

    List<Vacancy> findByCategoryId(Long categoryId);

    @Query("""
            select v from Vacancy v
            left join RespondedApplicant ra on ra.vacancy.id = v.id
            where v.isActive = true
            group by v
            order by count(ra) desc
            """)
    Page<Vacancy> findActiveOrderByResponseCountDesc(Pageable pageable);

    @Query("""
            select v from Vacancy v
            left join RespondedApplicant ra on ra.vacancy.id = v.id
            where v.author.id = :authorId
            group by v
            order by count(ra) desc
            """)
    Page<Vacancy> findByAuthorIdOrderByResponseCountDesc(Long authorId, Pageable pageable);
}