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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String surname;

    private Integer age;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", length = 55)
    private String phoneNumber;

    @Column(length = 500)
    private String avatar;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Transient
    private String accountType;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "applicant")
    private Set<Resume> resumes = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Vacancy> vacancies = new HashSet<>();

    public void addResume(Resume resume) {
        resumes.add(resume);
        resume.setApplicant(this);
    }

    public void removeResume(Resume resume) {
        resumes.remove(resume);
        if (resume.getApplicant() == this) {
            resume.setApplicant(null);
        }
    }

    public void addVacancy(Vacancy vacancy) {
        vacancies.add(vacancy);
        vacancy.setAuthor(this);
    }

    public void removeVacancy(Vacancy vacancy) {
        vacancies.remove(vacancy);
        if (vacancy.getAuthor() == this) {
            vacancy.setAuthor(null);
        }
    }
}
