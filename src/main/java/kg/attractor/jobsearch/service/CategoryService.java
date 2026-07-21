package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Long id);
}