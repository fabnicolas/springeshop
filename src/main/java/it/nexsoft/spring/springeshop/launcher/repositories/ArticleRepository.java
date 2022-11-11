package it.nexsoft.spring.springeshop.launcher.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.nexsoft.spring.springeshop.launcher.models.Article;

public interface ArticleRepository extends JpaRepository<Article,Long>{

	@Query("SELECT a FROM Article a WHERE a.name LIKE ?1% ")
	List<Article> findNomeStartWith(String s);
}
