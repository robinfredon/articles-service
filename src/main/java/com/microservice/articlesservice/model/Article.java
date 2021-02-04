package com.microservice.articlesservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("monFiltreDynamique")
@Entity
public class Article {
	
	public Article(int id, String nom, int prix, int prixAchat) {
		super();
		this.id = id;
		this.nom = nom;
		this.prix = prix;
		this.prixAchat = prixAchat;
		this.marge = prix - prixAchat;
	}
	public Article() {
		
	}
	@Override
	public String toString() {
		return "Article :" +
				"\n\tid : " + this.id +
				"\n\tnom : " + this.nom +
				"\n\tprix : " + this.prix;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public int getPrixAchat() {
		return prixAchat;
	}
	public void setPrixAchat(int prixAchat) {
		this.prixAchat = prixAchat;
	}
	public int getMarge() {
		return prix - prixAchat;
	}
	public void setMarge(int marge) {
		this.marge = marge;
	}
	@Id
	//@GeneratedValue
	private int id;
	private String nom;
	private int prix;
	private int prixAchat;
	@Transient
	private int marge;

}
