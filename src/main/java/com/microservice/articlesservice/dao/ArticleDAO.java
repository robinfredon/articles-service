package com.microservice.articlesservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservice.articlesservice.model.Article;

public interface ArticleDAO extends JpaRepository<Article, Integer>  {
	public List<Article> findAll();
	public List<Article> findAllByOrderByNomAsc();
	public Article findById(int id);
	public Article save(Article article);
	List<Article> findByPrixGreaterThan(int prixLimit);
	List<Article> findByNomLike(String recherche);
	
	@Query("SELECT p FROM Article p WHERE p.prix > :prixLimit")
	List<Article> chercherUnArticleCher(@Param("prixLimit") int prix);

}
