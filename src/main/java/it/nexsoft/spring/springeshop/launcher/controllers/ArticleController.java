package it.nexsoft.spring.springeshop.launcher.controllers;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.nexsoft.spring.springeshop.launcher.models.Article;
import it.nexsoft.spring.springeshop.launcher.repositories.ArticleRepository;


@RestController
@RequestMapping("/prova")
public class ArticleController {
	
	@Autowired
	private ArticleRepository repository;
	
	@PostMapping("/post")
	public ResponseEntity<Article> addArticle(@RequestBody final Article article) {
		try {
			final Article _article = repository.save(new Article(article.getName(), article.getDescription(), article.getPrice()));
			return new ResponseEntity<>(_article, HttpStatus.CREATED);
		} catch (final Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<Article>> findAll() {
		try {
		List<Article> articles = new ArrayList<>();
		articles = repository.findAll();
		if(articles.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<>(articles,HttpStatus.OK);
		}
		catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping("/put/{id}")
	public ResponseEntity<Article> updateArticle(@PathVariable("id") final long id,
			@RequestBody final Article article) {
		final Optional<Article> articleData = repository.findById(id);

		if (articleData.isPresent()) {
			final Article _article  = articleData.get();
			_article.setName(article.getName());
			_article.setDescription(article.getDescription());
			_article.setPrice(article.getPrice());
			return new ResponseEntity<>(repository.save(_article), HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteArticle(@PathVariable final long id){
		try {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<HttpStatus> deleteAll(){
		try {
			repository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getL")
	public ResponseEntity<List<Article>> getArticleStartsWithLetter(@RequestParam(name="letter") final String letter) {
		try {
			List<Article> articles = new ArrayList<>();
			articles = repository.findNomeStartWith(letter);
			if(articles.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
			return new ResponseEntity<>(articles,HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
