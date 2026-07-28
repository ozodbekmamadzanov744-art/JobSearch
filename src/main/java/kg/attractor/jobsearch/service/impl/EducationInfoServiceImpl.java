package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.EducationInfoDao;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.service.EducationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationInfoServiceImpl implements EducationInfoService {

    private final EducationInfoDao educationInfoDao;

    @Override
    public EducationInfo save(EducationInfo educationInfo) {
        return educationInfoDao.save(educationInfo);
    }

    @Override
    public List<EducationInfo> findByResumeId(Long resumeId) {
        return educationInfoDao.findByResumeId(resumeId);
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        educationInfoDao.deleteByResumeId(resumeId);
    }
}