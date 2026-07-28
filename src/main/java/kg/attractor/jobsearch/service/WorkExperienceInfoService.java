package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.WorkExperienceInfo;

import java.util.List;

public interface WorkExperienceInfoService {

    WorkExperienceInfo save(WorkExperienceInfo workExperienceInfo);

    List<WorkExperienceInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}