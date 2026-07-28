package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.ContactInfo;

import java.util.List;

public interface ContactInfoService {

    ContactInfo save(ContactInfo contactInfo);

    List<ContactInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}