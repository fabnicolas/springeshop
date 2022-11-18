package it.nexsoft.spring.springeshop.launcher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.nexsoft.spring.springeshop.launcher.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
