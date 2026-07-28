package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.ContactInfoDao;
import kg.attractor.jobsearch.model.ContactInfo;
import kg.attractor.jobsearch.service.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoDao contactInfoDao;

    @Override
    public ContactInfo save(ContactInfo contactInfo) {
        return contactInfoDao.save(contactInfo);
    }

    @Override
    public List<ContactInfo> findByResumeId(Long resumeId) {
        return contactInfoDao.findByResumeId(resumeId);
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        contactInfoDao.deleteByResumeId(resumeId);
    }
}