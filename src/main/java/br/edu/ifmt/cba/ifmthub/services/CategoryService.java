package br.edu.ifmt.cba.ifmthub.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifmt.cba.ifmthub.model.Category;
import br.edu.ifmt.cba.ifmthub.repositories.CategoryRepository;
import br.edu.ifmt.cba.ifmthub.resources.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	public Category save(Category category) {
		category.setDateCreated(LocalDateTime.now());
		category.setStatus(true);
		return categoryRepository.save(category);
	}

	public Category findById(Long idCategory) {
		return categoryRepository.findById(idCategory).orElseThrow(
				() -> new ResourceNotFoundException("No category present with idCategory = " + idCategory));
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category update(Category category) {
		Category categorySaved = this.findById(category.getIdCategory());
		categorySaved.setDescription(category.getDescription());
		categorySaved.setStatus(category.isStatus());
		categoryRepository.save(categorySaved);
		return categorySaved;
	}

	public void delete(Long idCategory) {
		categoryRepository.deleteById(idCategory);
	}
}
