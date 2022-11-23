package it.nexsoft.spring.springeshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.nexsoft.spring.springeshop.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
