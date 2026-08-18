package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.model.ContactType;
import kg.attractor.jobsearch.repository.ContactTypeRepository;
import kg.attractor.jobsearch.service.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactTypeServiceImpl implements ContactTypeService {

    private final ContactTypeRepository contactTypeRepository;

    @Override
    public List<ContactType> getAllContactTypes() {
        return contactTypeRepository.findAll();
    }
}
