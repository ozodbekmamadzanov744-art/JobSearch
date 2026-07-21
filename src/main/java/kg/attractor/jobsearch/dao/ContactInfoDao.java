package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.ContactInfo;

import java.util.List;

public interface ContactInfoDao {

    ContactInfo save(ContactInfo contactInfo);

    List<ContactInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}