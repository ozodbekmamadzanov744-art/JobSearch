package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.EducationInfo;

import java.util.List;

public interface EducationInfoDao {

    EducationInfo save(EducationInfo educationInfo);

    List<EducationInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}