package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.WorkExperienceInfoDao;
import kg.attractor.jobsearch.model.WorkExperienceInfo;
import kg.attractor.jobsearch.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {

    private final WorkExperienceInfoDao workExperienceInfoDao;

    @Override
    public WorkExperienceInfo save(WorkExperienceInfo workExperienceInfo) {
        return workExperienceInfoDao.save(workExperienceInfo);
    }

    @Override
    public List<WorkExperienceInfo> findByResumeId(Long resumeId) {
        return workExperienceInfoDao.findByResumeId(resumeId);
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        workExperienceInfoDao.deleteByResumeId(resumeId);
    }
}