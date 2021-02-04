package com.microservice.articlesservice.web.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.microservice.articlesservice.dao.ArticleDAO;
import com.microservice.articlesservice.model.Article;
import com.microservice.articlesservice.web.exceptions.ArticleIntrouvableException;
import com.microservice.articlesservice.web.exceptions.PrixArticleIncorrectException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "API pour les opérations CRUD sur les articles")
public class ArticleController {
	
	@Autowired
	private ArticleDAO articleDAO;

	@RequestMapping(value = "/Articles", method = RequestMethod.GET)
	public MappingJacksonValue listeArticles() {
	 List<Article> articles = articleDAO.findAll();
	 String filters[] = new String[] { "prixAchat", "marge" };
	 SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept(filters);
	 FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
	 MappingJacksonValue articlesFiltres = new MappingJacksonValue(articles);
	 articlesFiltres.setFilters(listDeNosFiltres);
	 return articlesFiltres;
	}
	
	@GetMapping(value = "/AdminArticles")
	public MappingJacksonValue listeArticlesAdmin() {
		 List<Article> articles = articleDAO.findAll();
		 SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAll();
		 FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		 MappingJacksonValue articlesFiltres = new MappingJacksonValue(articles);
		 articlesFiltres.setFilters(listDeNosFiltres);
		 return articlesFiltres;
	}
	
	@ApiOperation(value = "Récupère un article grâce à son ID à	condition que celui-ci soit en stock!")
	@RequestMapping(value = "/Articles/{id}", method = RequestMethod.GET)
	public MappingJacksonValue afficherUnArticle(@PathVariable int id) {
		 Article articles = articleDAO.findById(id);
			if (articles == null)
				throw new ArticleIntrouvableException("Article introuvable");
		 String filters[] = new String[] { "prixAchat", "marge" };
		 SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept(filters);
		 FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		 MappingJacksonValue articlesFiltres = new MappingJacksonValue(articles);
		 articlesFiltres.setFilters(listDeNosFiltres);
		 return articlesFiltres;
	}
	
	@GetMapping(value = "/Articles/sort")
	public MappingJacksonValue articlesSorted() {
		 List<Article> articles = articleDAO.findAllByOrderByNomAsc();
		 SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("marge");
		 FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		 MappingJacksonValue articlesFiltres = new MappingJacksonValue(articles);
		 articlesFiltres.setFilters(listDeNosFiltres);
		 return articlesFiltres;
	}
	
	@GetMapping(value = "/test/articles/{prixLimit}")
	public MappingJacksonValue testDeRequetes(@PathVariable int prixLimit) {
		 List<Article> articles = articleDAO.findByPrixGreaterThan(prixLimit);
		 SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("marge");
		 FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		 MappingJacksonValue articlesFiltres = new MappingJacksonValue(articles);
		 articlesFiltres.setFilters(listDeNosFiltres);
		 return articlesFiltres;
	}
	
	@GetMapping(value = "/test/articles/like/{recherche}")
	public MappingJacksonValue testeDeRequetes(@PathVariable String	recherche) {
		 List<Article> articles = articleDAO.findByNomLike("%"+recherche+"%");
		 SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("marge");
		 FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
		 MappingJacksonValue articlesFiltres = new MappingJacksonValue(articles);
		 articlesFiltres.setFilters(listDeNosFiltres);
		 return articlesFiltres;
	}
	
	@DeleteMapping (value = "/Articles/{id}")
	public void supprimerArticle(@PathVariable int id) {
	 articleDAO.deleteById(id);
	}
	
	@PutMapping (value = "/Articles")
	public void updateArticle(@RequestBody Article article) {
	 articleDAO.save(article);
	}

	@PostMapping(value = "/Articles")
	public ResponseEntity<Void> ajouterArticle(@RequestBody Article article) {
		if (article.getPrix() == 0)
			throw new PrixArticleIncorrectException("Article introuvable");
		 Article articleAdded = articleDAO.save(article);
		 if (articleAdded == null)
			 return ResponseEntity.noContent().build();
				 URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						 .path("/{id}").buildAndExpand(articleAdded.getId()).toUri();
		 return ResponseEntity.created(location).build();
	 }
	
}
