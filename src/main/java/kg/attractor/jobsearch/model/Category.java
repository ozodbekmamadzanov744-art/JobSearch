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
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "category")
    private Set<Vacancy> vacancies = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Resume> resumes = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    public void addVacancy(Vacancy vacancy) {
        vacancies.add(vacancy);
        vacancy.setCategory(this);
    }

    public void removeVacancy(Vacancy vacancy) {
        vacancies.remove(vacancy);
        if (vacancy.getCategory() == this) {
            vacancy.setCategory(null);
        }
    }

    public void addResume(Resume resume) {
        resumes.add(resume);
        resume.setCategory(this);
    }

    public void removeResume(Resume resume) {
        resumes.remove(resume);
        if (resume.getCategory() == this) {
            resume.setCategory(null);
        }
    }

    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        children.remove(child);
        if (child.getParent() == this) {
            child.setParent(null);
        }
    }
}
