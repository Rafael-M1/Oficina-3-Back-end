package br.edu.ifmt.cba.ifmthub.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifmt.cba.ifmthub.model.Tag;
import br.edu.ifmt.cba.ifmthub.repositories.TagRepository;

@Service
public class TagService {
	@Autowired
	private TagRepository tagRepository;

	public Tag save(Tag tag) {
		tag.setDateCreated(LocalDateTime.now());
		tag.setStatus(true);
		return tagRepository.save(tag);
	}

	public Tag findById(Long idTag) {
		return tagRepository.findById(idTag).get();
	}

	public List<Tag> findAll() {
		return tagRepository.findAll();
	}

	public Tag update(Tag tag) {
		Tag tagSaved = tagRepository.findById(tag.getIdTag()).get();
		tagSaved.setDescription(tag.getDescription());
		tagSaved.setStatus(tag.isStatus());
		tagRepository.save(tagSaved);
		return tagSaved;
	}

	public void delete(Long idTag) {
		tagRepository.deleteById(idTag);
	}
}
