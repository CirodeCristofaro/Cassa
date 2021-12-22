package com.example.demo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Quantita {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer quantita;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_article")
	private Articoli articoliCarrello;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_cart")
	private Carrello carrelloAcquirente;

	public Articoli getArticoliCarrello() {
		return articoliCarrello;
	}
	public void setArticoliCarrello(Articoli articoliCarrello) {
		this.articoliCarrello = articoliCarrello;
	}
	public Carrello getCartbuyer() {
		return carrelloAcquirente;
	}
	public void setCartbuyer(Carrello carrelloAcquirente) {
		this.carrelloAcquirente = carrelloAcquirente;
	}
	public Integer getQuantita() {
		return quantita;
	}
	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

}
