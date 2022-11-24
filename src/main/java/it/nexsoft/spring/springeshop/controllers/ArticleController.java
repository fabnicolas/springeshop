package it.nexsoft.spring.springeshop.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.nexsoft.spring.springeshop.models.Article;
import it.nexsoft.spring.springeshop.repositories.ArticleRepository;

@RestController
@RequestMapping("/prova")
public class ArticleController {

	@Autowired
	private ArticleRepository repository;

	@PostMapping("/postarticle")
	public ResponseEntity<Article> addArticle(@RequestBody final Article article) {
		try {
			final Article _article = repository
					.save(new Article(article.getName(), article.getDescription(), article.getPrice()));
			return new ResponseEntity<>(_article, HttpStatus.CREATED);
		} catch (final Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getallarticles")
	public ResponseEntity<List<Article>> findAll() {
		try {
			List<Article> articles = new ArrayList<>();
			articles = repository.findAll();
			if (articles.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(articles, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/putarticle/{id}")
	public ResponseEntity<Article> updateArticle(@PathVariable("id") final long id,
			@RequestBody final Article article) {
		final Optional<Article> articleData = repository.findById(id);

		if (articleData.isPresent()) {
			final Article _article = articleData.get();
			_article.setName(article.getName());
			_article.setDescription(article.getDescription());
			_article.setPrice(article.getPrice());
			return new ResponseEntity<>(repository.save(_article), HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/deletearticle/{id}")
	public ResponseEntity<HttpStatus> deleteArticle(@PathVariable final long id) {
		try {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (final Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
