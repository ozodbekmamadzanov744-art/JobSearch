package kg.attractor.jobsearch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private Double salary;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "resume")
    private Set<ContactInfo> contactsInfo = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    private Set<EducationInfo> educationInfos = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    private Set<WorkExperienceInfo> workExperienceInfos = new HashSet<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<RespondedApplicant> respondedApplicants = new HashSet<>();

    public void addContactInfo(ContactInfo contactInfo) {
        contactsInfo.add(contactInfo);
        contactInfo.setResume(this);
    }

    public void removeContactInfo(ContactInfo contactInfo) {
        contactsInfo.remove(contactInfo);
        if (contactInfo.getResume() == this) {
            contactInfo.setResume(null);
        }
    }

    public void addEducationInfo(EducationInfo educationInfo) {
        educationInfos.add(educationInfo);
        educationInfo.setResume(this);
    }

    public void removeEducationInfo(EducationInfo educationInfo) {
        educationInfos.remove(educationInfo);
        if (educationInfo.getResume() == this) {
            educationInfo.setResume(null);
        }
    }

    public void addWorkExperienceInfo(WorkExperienceInfo workExperienceInfo) {
        workExperienceInfos.add(workExperienceInfo);
        workExperienceInfo.setResume(this);
    }

    public void removeWorkExperienceInfo(WorkExperienceInfo workExperienceInfo) {
        workExperienceInfos.remove(workExperienceInfo);
        if (workExperienceInfo.getResume() == this) {
            workExperienceInfo.setResume(null);
        }
    }

    public void addRespondedApplicant(RespondedApplicant respondedApplicant) {
        respondedApplicants.add(respondedApplicant);
        respondedApplicant.setResume(this);
    }

    public void removeRespondedApplicant(RespondedApplicant respondedApplicant) {
        respondedApplicants.remove(respondedApplicant);
        if (respondedApplicant.getResume() == this) {
            respondedApplicant.setResume(null);
        }
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updateTime = now;
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }
}
