package com.example.demo;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Carrello {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Quantita> quantitaArticolo;

	public Set<Quantita> getQuantitaComprata() {
		return this.quantitaArticolo;
	}
	public void setBoughtarticle(Set<Quantita> quantitaArticolo) {
		this.quantitaArticolo = quantitaArticolo;
	}
}
