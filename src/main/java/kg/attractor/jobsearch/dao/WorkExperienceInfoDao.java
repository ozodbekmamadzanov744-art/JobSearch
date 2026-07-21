package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.WorkExperienceInfo;

import java.util.List;

public interface WorkExperienceInfoDao {

    WorkExperienceInfo save(WorkExperienceInfo workExperienceInfo);

    List<WorkExperienceInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}