package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.model.WorkExperienceInfo;
import kg.attractor.jobsearch.repository.WorkExperienceInfoRepository;
import kg.attractor.jobsearch.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {

    private final WorkExperienceInfoRepository workExperienceInfoRepository;

    @Override
    public WorkExperienceInfo save(WorkExperienceInfo workExperienceInfo) {
        return workExperienceInfoRepository.save(workExperienceInfo);
    }

    @Override
    public List<WorkExperienceInfo> findByResumeId(Long resumeId) {
        return workExperienceInfoRepository.findByResumeId(resumeId);
    }

    @Override
    @Transactional
    public void deleteByResumeId(Long resumeId) {
        workExperienceInfoRepository.deleteByResumeId(resumeId);
    }
}
