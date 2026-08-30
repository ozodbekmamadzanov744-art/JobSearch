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
@Table(name = "contact_types")
public class ContactType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String type;

    @OneToMany(mappedBy = "contactType")
    private Set<ContactInfo> contactsInfo = new HashSet<>();

    public void addContactInfo(ContactInfo contactInfo) {
        contactsInfo.add(contactInfo);
        contactInfo.setContactType(this);
    }

    public void removeContactInfo(ContactInfo contactInfo) {
        contactsInfo.remove(contactInfo);
        if (contactInfo.getContactType() == this) {
            contactInfo.setContactType(null);
        }
    }
}
