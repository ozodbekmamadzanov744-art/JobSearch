package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.model.ContactInfo;
import kg.attractor.jobsearch.repository.ContactInfoRepository;
import kg.attractor.jobsearch.service.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;

    @Override
    public ContactInfo save(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    @Override
    public List<ContactInfo> findByResumeId(Long resumeId) {
        return contactInfoRepository.findByResumeId(resumeId);
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        contactInfoRepository.deleteByResumeId(resumeId);
    }
}
