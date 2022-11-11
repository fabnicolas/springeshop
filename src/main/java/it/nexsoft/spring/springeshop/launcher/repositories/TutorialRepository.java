package it.nexsoft.spring.springeshop.launcher.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.nexsoft.spring.springeshop.launcher.models.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
	List<Tutorial> findByPublished(boolean published);

	List<Tutorial> findByTitleContaining(String title);
}