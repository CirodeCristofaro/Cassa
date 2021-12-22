package com.example.demo;

import java.math.BigDecimal;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ControllerWeb {
	@Autowired
	private ArticoliRepository articoliRepository;
	@Autowired
	private CarrelloRepository carrelloRepository;
	

	@GetMapping(path = "/home")
	public String Home(Model model) {
		model.addAttribute("carrello", carrelloRepository.findAll());
		return "carrelloHomepage";

	}
	//creazione carrello personalizzato
	@PostMapping(path="/home")
	public String CartSubmit(@ModelAttribute Carrello cart,
			@RequestParam String name, Model model ) {
		Carrello carrello = new Carrello();
	    carrello.setName(name);
	    carrelloRepository.save(carrello);
		model.addAttribute("carrello", carrelloRepository.findAll());
		return "carrelloHomepage";
	}
	
	//cancella completamente il carrello 
	@GetMapping(path = "/CancellaCarrello")
	public String RemoveCart(@RequestParam Integer id, Model model) {
		Carrello carrello = carrelloRepository.findById(id).get();
		Iterator<Quantita> iterator = carrello.getQuantitaComprata().iterator();
		Quantita quantitaInCarrello = null;
		while (iterator.hasNext()) {
			quantitaInCarrello = iterator.next();
			Articoli articoloinCarrello = quantitaInCarrello.getArticoliCarrello();
			iterator.remove();
			articoloinCarrello.getCarrello().remove(quantitaInCarrello);
			quantitaInCarrello.setArticoliCarrello(null);
			quantitaInCarrello.setCartbuyer(null);	
		}
		carrelloRepository.save(carrello);
		carrelloRepository.deleteById(id);
		model.addAttribute("carrello", carrelloRepository.findAll());
		return "carrelloHomepage";
	}
	
	@GetMapping(path = "/MostraCarrello{Id}")
	public String ShowCartId(@RequestParam Integer id, Model model) {
		model.addAttribute("id", id);
		return "carrello";
	}
	
	@GetMapping(path = "/MostraCarrello")
	public String ShowCart(@RequestParam Integer id, Model model) {
		model.addAttribute("articoliCarrello", carrelloRepository.findById(id).get().getQuantitaComprata());
		model.addAttribute("carrelloAcquirenteId", carrelloRepository.findById(id).get().getId());
		model.addAttribute("carrelloAcquirente", carrelloRepository.findById(id).get().getName());		
		model.addAttribute("articoli", articoliRepository.findAll());
		return "carrello";

	}
	//aggiunge un articolo al carrello
	@GetMapping(path = "/AggiungiAlCarrello{id_article}{id_cart}")
	public String AddToCart(@RequestParam Integer id_article, @RequestParam Integer id_cart, Model model) {
		Carrello n = carrelloRepository.findById(id_cart).get();
		Articoli a = articoliRepository.findById(id_article).get();
		Quantita qic = new Quantita();
		int flag = 0;
		
		for (Quantita qui : n.getQuantitaComprata()) {
			if (qui.getArticoliCarrello().getName().equals(a.getName())) {
				qui.setQuantita(qui.getQuantita() + 1);
				flag = 1;
			}
		}
		
		if (flag == 0) {//se non è presente allora è la prima volta che inserisco l'articolo nel carrello
			qic.setArticoliCarrello(a);
			qic.setCartbuyer(n);
			qic.setQuantita(1);
			n.getQuantitaComprata().add(qic);
			a.getCarrello().add(qic);
		}
		n = carrelloRepository.save(n);

		model.addAttribute("articoliCarrello", n.getQuantitaComprata());
		model.addAttribute("carrelloAcquirenteId", n.getId());
		model.addAttribute("carrelloAcquirente", carrelloRepository.findById(id_cart).get().getName());
		model.addAttribute("articoli", articoliRepository.findAll());
		return "carrello";
	}
	
	@GetMapping(path="/CancellaCarrelloArticolo")
	public String CancellaCarrello(@RequestParam Integer id_cart, @RequestParam Integer id_article, Model model) {
		Carrello c = carrelloRepository.findById(id_cart).get();
		Articoli a = articoliRepository.findById(id_article).get();
		int flag = 0;
		Quantita qic = new Quantita();
		for (Quantita qui : c.getQuantitaComprata()) {
			if (qui.getArticoliCarrello().getName().equals(a.getName())) {
				if (qui.getQuantita() > 1) {
					qui.setQuantita(qui.getQuantita() - 1);
					flag = 1;
				} else {
					flag = 0;
					qic = qui;
				}
			}
		}
		if (flag == 0) {
			c.getQuantitaComprata().remove(qic);
			a.getCarrello().remove(qic);
			qic.setArticoliCarrello(null);
			qic.setCartbuyer(null);
			
		}

		carrelloRepository.save(c);

		model.addAttribute("articoliCarrello", c.getQuantitaComprata());
		model.addAttribute("carrelloAcquirenteId", c.getId());
		model.addAttribute("carrelloAcquirente", carrelloRepository.findById(id_cart).get().getName());
		model.addAttribute("articoli", articoliRepository.findAll());
		return "carrello";
	}
	
	@GetMapping(path = "/Checkout")
	public String Checkout(@RequestParam Integer id_cart, Model model) {
		Carrello c = carrelloRepository.findById(id_cart).get();
		BigDecimal dev = new BigDecimal(0);
		BigDecimal sum= new BigDecimal(0);
		BigDecimal sup= new BigDecimal(0);
		for (Quantita a : c.getQuantitaComprata()) {
			BigDecimal res =sum.add(new BigDecimal(a.getQuantita()));
			dev=res.multiply(a.getArticoliCarrello().getPrice());
			sup = sup.add(dev);
		}
		model.addAttribute("sommaCarelloTotale", sup);
		model.addAttribute("carrelloAcquirenteId", carrelloRepository.findById(id_cart).get().getId());
		model.addAttribute("carrelloAcquirente", carrelloRepository.findById(id_cart).get().getName());
		model.addAttribute("articoliCarrello", carrelloRepository.findById(id_cart).get().getQuantitaComprata());
		return "checkout";
	}

	@GetMapping(path="/Shop")
	public String Shop(Model model) {
		model.addAttribute("articoli", articoliRepository.findAll());
		return "magazzino";
	}
	
	@PostMapping(path="/Shop")
	public String ArticleSubmit(@ModelAttribute Articoli article,
			@RequestParam String name, @RequestParam BigDecimal price, Model model ) {
		Articoli articoli = new Articoli();
	    articoli.setName(name);
	    articoli.setPrice(price);
	    articoliRepository.save(articoli);
		model.addAttribute("articoli", articoliRepository.findAll());
		return "magazzino";
	}
	
	@GetMapping(path = "/CancellaArticolo")
	public String RemoveArticle(@RequestParam Integer id, Model model) {
		articoliRepository.deleteById(id);
		model.addAttribute("articoli", articoliRepository.findAll());
		return "magazzino";
	}
	
	
}
