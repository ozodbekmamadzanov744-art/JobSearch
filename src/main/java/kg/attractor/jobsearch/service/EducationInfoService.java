package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.EducationInfo;

import java.util.List;

public interface EducationInfoService {

    EducationInfo save(EducationInfo educationInfo);

    List<EducationInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}