package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.Category;
import kg.attractor.jobsearch.repository.CategoryRepository;
import kg.attractor.jobsearch.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Категория с id " + id + " не найдена"));
    }
}
