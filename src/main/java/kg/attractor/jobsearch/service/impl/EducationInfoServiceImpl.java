package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.repository.EducationInfoRepository;
import kg.attractor.jobsearch.service.EducationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationInfoServiceImpl implements EducationInfoService {

    private final EducationInfoRepository educationInfoRepository;

    @Override
    public EducationInfo save(EducationInfo educationInfo) {
        return educationInfoRepository.save(educationInfo);
    }

    @Override
    public List<EducationInfo> findByResumeId(Long resumeId) {
        return educationInfoRepository.findByResumeId(resumeId);
    }

    @Override
    @Transactional
    public void deleteByResumeId(Long resumeId) {
        educationInfoRepository.deleteByResumeId(resumeId);
    }
}
