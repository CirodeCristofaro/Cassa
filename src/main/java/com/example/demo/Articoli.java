package com.example.demo;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Articoli {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String nome;
	private BigDecimal price;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return nome;
	}
	public void setName(String name) {
		this.nome = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
//	@ManyToMany(mappedBy = "boughtarticle")
//	Set<Cart> cartbuyer;
//
//	public Set<Cart> getCartbuyer() {
//		return cartbuyer;
//	}
//	public void setCartbuyer(Set<Cart> cartbuyer) {
//		this.cartbuyer = cartbuyer;
//	}
	
	@OneToMany(cascade = CascadeType.ALL  )
	private Set<Quantita> carrello;

	public Set<Quantita> getCarrello() {
		return carrello;
	}
	public void setCarrello(Set<Quantita> carrello) {
		this.carrello = carrello;
	}
}
